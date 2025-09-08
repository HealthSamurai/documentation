(ns gitbok.core
  (:require
   [gitbok.indexing.impl.sitemap :as sitemap]
   [gitbok.indexing.impl.sitemap-index :as sitemap-index]
   [clojure.string :as str]
   [hiccup2.core]
   [gitbok.markdown.core :as markdown]
   [gitbok.indexing.core :as indexing]
   [gitbok.indexing.impl.file-to-uri :as file-to-uri]
   [gitbok.indexing.impl.summary :as summary]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.ui.main-content :as main-content]
   [gitbok.ui.layout :as layout]
   [gitbok.ui.not-found :as not-found]
   [gitbok.ui.main-navigation :as main-navigation]
   [gitbok.ui.left-navigation :as left-navigation]
   [gitbok.ui.landing-hero :as landing-hero]
   [gitbok.ui.search]
   [gitbok.ui.meilisearch]
   [gitbok.reload :as reload]
   [klog.core :as log]
   [ring.middleware.gzip :refer [wrap-gzip]]
   [ring.middleware.content-type :refer [content-type-response]]
   [gitbok.http]
   [gitbok.products :as products]
   [gitbok.constants :as const]
   [http]
   [ring.util.response :as resp]
   [system]
   [gitbok.utils :as utils]
   [uui])
  (:gen-class))

(set! *warn-on-reflection* true)

(def dev? (= "true" (System/getenv "DEV")))
(def prefix (or (System/getenv "DOCS_PREFIX") "/"))
(def base-url (or (System/getenv "BASE_URL")
                  "http://localhost:8081"))

(defn read-markdown-file [context filepath]
  (let [[filepath section] (str/split filepath #"#")
        full-filepath (products/filepath context filepath)
        ;; In DEV mode, always read fresh content from disk/volume
        _ (when dev? (log/debug ::reading-file {:filepath filepath :full-path full-filepath}))
        content* (if dev?
                   ;; Force re-read from disk in DEV mode
                   (do
                     (log/debug ::reading-fresh {:path full-filepath})
                     (utils/slurp-resource full-filepath))
                   ;; In production, use cached content from memory
                   (or (get (indexing/get-md-files-idx context) filepath)
                       (utils/slurp-resource full-filepath)))
        {:keys [parsed description title]}
        (markdown/parse-markdown-content
         context
         [full-filepath content*])]
    (try
      {:content
       (main-content/render-file* context full-filepath parsed title content*)
       :title title
       :description description
       :section section}
      (catch Exception e
        (log/info ::cannot-render-file {:filepath full-filepath})
        {:content [:div {:role "alert"}
                   (.getMessage e)
                   [:pre (pr-str e)]
                   [:pre content*]]}))))

(defn picture-url? [url]
  (when url
    (or (str/starts-with? url "/.gitbook/assets")
        (str/includes? url "/.gitbook/assets"))))

(defn render-file
  [context filepath]
  (let [result
        (try
          (if dev?
            (read-markdown-file context filepath)
            (markdown/get-rendered context filepath))
          (catch Exception e
            [:div {:role "alert"}
             (.getMessage e)
             [:pre (pr-str e)]]))]
    {:content
     (if (map? result)
       (:content result)
       [:div result])
     :section (:section result)
     :description (:description result)
     :title (:title result)}))

(defn check-cache-lastmod [request last-mod]
  (let [if-modified-since
        (get-in request [:headers "if-modified-since"])
        lastmod (utils/iso-to-http-date last-mod)]
    (and if-modified-since
         lastmod
         (= if-modified-since lastmod))))

(defn check-cache-etag [request etag]
  (let [if-none-match (get-in request [:headers "if-none-match"])]
    (and if-none-match (= if-none-match etag))))

(defn render-pictures [context request]
  (let [uri (:uri request)
        uri-relative
        (utils/uri-to-relative uri prefix
                               (products/path context))
        file-path (->
                   uri-relative
                   (str/replace #"%20" " ")
                   (str/replace-first #".*.gitbook/" ""))
        ;; Try to find the image in multiple locations
        response (or
                  ;; First try the standard location (.gitbook/assets/)
                  (resp/resource-response file-path)
                  ;; If not found, try without 'assets/' prefix (for docs/.gitbook/assets/)
                  ;; Since 'docs' is in classpath directly, files are accessible as ".gitbook/assets/..."
                  (resp/resource-response (str ".gitbook/" file-path)))]
    (when response
      ;; Add proper Content-Type for SVG files
      (if (str/ends-with? file-path ".svg")
        (resp/content-type response "image/svg+xml")
        response))))

(defn render-favicon [context _]
  (let [product (products/get-current-product context)
        favicon-path (or (:favicon product) "public/favicon.ico")]
    (if (str/starts-with? favicon-path ".gitbook/")
      ;; Handle .gitbook/assets paths like render-pictures does
      (resp/resource-response
       (str/replace-first favicon-path #".*.gitbook/" ""))
      ;; Regular resource path
      (resp/resource-response favicon-path))))

(defn render-robots-txt [context _]
  (let [product (products/get-current-product context)
        robots-path (:robots product)]
    (if robots-path
      ;; Use custom robots.txt if specified
      (if (str/starts-with? robots-path ".gitbook/")
        ;; Handle .gitbook/assets paths like render-pictures does
        (resp/resource-response
         (str/replace-first robots-path #".*.gitbook/" ""))
        ;; Regular resource path
        (resp/resource-response robots-path))
      ;; Generate default robots.txt dynamically
      (let [sitemap-url (gitbok.http/get-product-absolute-url
                         context
                         "/sitemap.xml")]
        {:status 200
         :headers {"content-type" "text/plain"}
         :body (str "User-agent: *\n"
                    "Allow: /\n"
                    "Sitemap: " sitemap-url "\n")}))))

(defn render-landing
  [context request]
  (landing-hero/render-landing context request))

(defn render-file-view
  [context request]
  (let [uri (:uri request)
        uri-relative
        (utils/uri-to-relative
         uri
         prefix
         (products/path context))]
    (println "uri " uri-relative)
    (cond

      (or (picture-url? uri-relative)
          (picture-url? uri))
      (render-pictures context request)

      :else
      (let [filepath
            (indexing/uri->filepath context uri-relative)]
        (if filepath
          (let [lastmod (indexing/get-lastmod context filepath)
                lastmod-iso (utils/iso-to-http-date lastmod)
                etag (utils/etag lastmod-iso)]
            (if (or (check-cache-etag request etag)
                    (and lastmod
                         (check-cache-lastmod request lastmod)))
              {:status 304
               :headers {"Cache-Control" "public, max-age=300"
                         "Last-Modified" lastmod-iso
                         "ETag" etag}}
              (let [{:keys [title description content section]}
                    (render-file (assoc context :current-uri uri-relative) filepath)]
                (layout/layout
                 context request
                 {:content content
                  :lastmod lastmod
                  :title title
                  :description description
                  :section section
                  :filepath filepath}))))

          (layout/layout
           context request
           {:content (not-found/not-found-view context uri-relative)
            :status 404
            :title "Not found"
            :description "Page not found"
            :hide-breadcrumb true}))))))

(defn sitemap-xml
  [context _]
  {:status 200
   :headers {"content-type" "application/xml"}
   :body (sitemap/get-sitemap context)})

(defn sitemap-index-xml
  "Generates sitemap index that references all product sitemaps"
  [context _]
  {:status 200
   :headers {"content-type" "application/xml"}
   :body (gitbok.indexing.impl.sitemap-index/generate-sitemap-index context)})

(defn redirect-to-readme
  [context request]
  (let [uri (products/uri
             context prefix
             (or (products/readme-url context) "readme"))
        request
        (assoc request
               :uri uri
               :/ true)]
    (render-file-view context request)))

(defn root-redirect-handler
  "Handles root path redirect based on configuration"
  [context request]
  (let [full-config (products/get-full-config context)
        root-redirect (:root-redirect full-config)]
    (if root-redirect
      (let [redirect-uri (utils/concat-urls prefix root-redirect)]
        (resp/redirect redirect-uri))
      ;; If no root-redirect configured, show 404 or default page
      (layout/layout
       context request
       {:content (not-found/not-found-view context "/")
        :status 404
        :title "Not found"
        :description "Page not found"
        :hide-breadcrumb true}))))

(defn healthcheck
  [_ _]
  {:status 200 :body {:status "ok"}})

(defn version-endpoint
  [context _]
  {:status 200 :body {:version (gitbok.http/get-version context)}})

(defn debug-endpoint
  [context request]
  (let [products (products/get-products-config context)
        full-config (products/get-full-config context)
        current-product (products/get-current-product context)]
    {:status 200
     :headers {"content-type" "application/json"}
     :body {:products products
            :full-config full-config
            :current-product current-product
            :environment {:prefix prefix
                          :base-url base-url
                          :port (gitbok.http/get-port context)}
            :request-info {:uri (:uri request)
                           :headers (select-keys (:headers request)
                                                 ["host" "x-forwarded-for" "x-real-ip"
                                                  "x-forwarded-proto" "x-forwarded-host"])}}}))

(defn serve-static-file
  "Serves static files with proper content type headers"
  [context request]
  (let [uri (gitbok.http/url-without-prefix context (:uri request))
        path (str/replace uri #"^/static/" "")
        response (resp/resource-response (utils/concat-urls "public" path))]
    (when response
      (content-type-response response request))))

(defn serve-og-preview
  "Serves OG preview images from resources"
  [context request]
  (let [uri (gitbok.http/url-without-prefix context (:uri request))
        path (str/replace uri #"^/public/og-preview/" "")
        resource-path (utils/concat-urls "public/og-preview" path)]
    (if-let [response (resp/resource-response resource-path)]
      (resp/content-type response "image/png")
      {:status 404
       :body "OG preview not found"})))

(system/defmanifest
  {:description "gitbok"
   :deps ["http"]
   :config {:history {:type "boolean"}}})

(def default-port 8081)

(def port
  (let [p (System/getenv "PORT")]
    (or
     (when (string? p)
       (try (Integer/parseInt p)
            (catch Exception _ nil)))
     default-port)))

(def default-config
  {:services ["http" "uui" "gitbok.core"]
   :http {:port port}})

(defn product-middleware
  "Middleware to determine product from request URI"
  [handler]
  (fn [context request]
    (let [uri (:uri request)
          uri-without-prefix (if (str/starts-with? uri prefix)
                               (subs uri (count prefix))
                               uri)
          products-config (products/get-products-config context)
          product (products/determine-product-by-uri products-config uri-without-prefix)
          context (products/set-current-product-id context (:id product))]
      (handler context request))))

(defn gzip-middleware [f]
  (fn [ctx req]
    (let [ring-handler (fn [req] (f ctx req))
          gzip-handler (wrap-gzip ring-handler)]
      (gzip-handler req))))

(defn init-product-indices
  "Initializes indexes for a specific product"
  [context product]
  (let [;; Temporarily set current product for initialization
        ctx (products/set-current-product-id context (:id product))]
    ;; order is important
    (log/info ::init-product {:msg "1. read summary. create toc htmx."
                              :product (:id product)})
    (summary/set-summary ctx)
    (log/info ::init-product {:msg "2. get uris from summary (using slugging), merge with redirects"
                              :product (:id product)})
    (uri-to-file/set-idx ctx)
    (log/info ::init-product {:msg "3. reverse file to uri idx"
                              :product (:id product)})
    (file-to-uri/set-idx ctx)
    (log/info ::init-product {:msg "4. using files from summary (step 3), read all files into memory"
                              :product (:id product)})
    (indexing/set-md-files-idx ctx (file-to-uri/get-idx ctx))
    (log/info ::init-product {:msg "5. parse all files into memory, some things are already rendered as plain html"
                              :product (:id product)})
    (markdown/set-parsed-markdown-index ctx (indexing/get-md-files-idx ctx))
    (log/info ::init-product {:msg "6. render it on start"
                              :product (:id product)})
    (when-not dev?
      (log/info ::init-product {:msg "Not DEV, render all pages into memory"
                                :product (:id product)})
      (markdown/render-all! ctx
                            (markdown/get-parsed-markdown-index ctx)
                            read-markdown-file)
      (log/info ::init-product {:msg "render done"
                                :product (:id product)}))
    (log/info ::init-product {:msg "7. set lastmod data in context for Last Modified metadata"
                              :product (:id product)})
    (indexing/set-lastmod ctx)
    (log/info ::init-product {:msg "8. generate sitemap.xml for product"
                              :product (:id product)})
    (let [lastmod-data (products/get-product-state ctx [const/LASTMOD])]
      (sitemap/set-sitemap ctx lastmod-data))))

(defn init-products
  "Initializes all products from configuration"
  [context]
  (let [full-config (products/load-products-config)
        products-config (:products full-config)]
    (products/set-products-config context products-config)
    (products/set-full-config context full-config)
    products-config))

#_{:clj-kondo/ignore [:unresolved-symbol]}
(system/defstart
  [context config]
  (gitbok.http/set-port context port)

  (gitbok.http/set-prefix context prefix)

  (gitbok.http/set-base-url context base-url)

  (gitbok.http/set-version
   context
   (str/trim (utils/slurp-resource "version")))

  (gitbok.http/set-dev-mode context dev?)

  (http/register-endpoint
   context
   {:path (utils/concat-urls prefix "/static/:path*")
    :method :get
    :fn #'serve-static-file})

  ;; OG preview handler - registered early to avoid conflicts with product routes
  (http/register-endpoint
   context
   {:path (str prefix "/public/og-preview/:path*")
    :method :get
    :fn #'serve-og-preview})

  (http/register-endpoint
   context
   {:path (utils/concat-urls prefix "/.gitbook/assets/:path*")
    :method :get
    :middleware [gzip-middleware]
    :fn #'render-pictures})

  ;; Initialize all products
  (let [products-config
        (init-products context)]
    ;; Register endpoints for each product
    (doseq [product products-config]
      (let [product-path (utils/concat-urls prefix (:path product))]
        (log/debug ::base-path {:product-path product-path})

        ;; Product main page
        (http/register-endpoint
         context
         {:path product-path
          :method :get
          :middleware [product-middleware gzip-middleware]
          :fn #'redirect-to-readme})

        ;; Product search
        (http/register-endpoint
         context
         {:path (str product-path "/search/dropdown")
          :method :get
          :middleware [product-middleware gzip-middleware]
          :fn
          gitbok.ui.search/search-endpoint})

        ;; Meilisearch HTMX endpoint
        (http/register-endpoint
         context
         {:path (str product-path "/meilisearch/dropdown")
          :method :get
          :middleware [product-middleware gzip-middleware]
          :fn
          gitbok.ui.meilisearch/meilisearch-endpoint})

        ;; Product sitemap
        (http/register-endpoint
         context
         {:path (str product-path "/sitemap.xml")
          :method :get
          :middleware [product-middleware]
          :fn #'sitemap-xml})

        ;; Product favicon
        (http/register-endpoint
         context
         {:path (str product-path "/favicon.ico")
          :method :get
          :middleware [product-middleware gzip-middleware]
          :fn #'render-favicon})

        ;; Product robots.txt
        #_(http/register-endpoint
         context
         {:path (str product-path "/robots.txt")
          :method :get
          :middleware [product-middleware]
          :fn #'render-robots-txt})

        ;; Product landing page
        (http/register-endpoint
         context
         {:path (str product-path "/landing")
          :method :get
          :middleware [product-middleware gzip-middleware]
          :fn #'render-landing})

        ;; All product pages
        (http/register-endpoint
         context
         {:path (str product-path "/:path*")
          :method :get
          :middleware [product-middleware gzip-middleware]
          :fn #'render-file-view}))))

  ;; Root path handler
  (http/register-endpoint
   context
   {:path prefix
    :method :get
    :middleware [gzip-middleware]
    :fn #'root-redirect-handler})

  ;; Root path with trailing slash handler
  (http/register-endpoint
   context
   {:path (str prefix "/")
    :method :get
    :middleware [gzip-middleware]
    :fn #'root-redirect-handler})

  ;; Sitemap index at root
  (http/register-endpoint
   context
   {:path (utils/concat-urls prefix "/sitemap.xml")
    :method :get
    :fn #'sitemap-index-xml})

  ;; Root robots.txt
  #_(http/register-endpoint
   context
   {:path (utils/concat-urls prefix "/robots.txt")
    :method :get
    :fn (fn [ctx _]
          (let [sitemap-url (str base-url
                                 (utils/concat-urls prefix "/sitemap.xml"))]
            {:status 200
             :headers {"content-type" "text/plain"}
             :body (str "User-agent: *\n"
                        "Allow: /\n"
                        "Sitemap: " sitemap-url "\n")}))})

  (http/register-endpoint
   context
   {:path (utils/concat-urls prefix "/version")
    :method :get
    :fn #'version-endpoint})

  (http/register-endpoint
   context
   {:path (utils/concat-urls prefix "/debug")
    :method :get
    :fn #'debug-endpoint})

  (let [products-config (products/get-products-config context)]
    (doseq [product products-config]
      (log/info ::init-product-indices {:product-name (:name product)
                                        :product-id (:id product)})
      (init-product-indices context product))

    (log/info ::setup-complete {:msg "setup done!"
                                :version (utils/slurp-resource "version")
                                :port-env (System/getenv "PORT")
                                :port port
                                :base-url base-url
                                :prefix prefix
                                :products (mapv #(hash-map :name (:name %) :path (:path %))
                                                products-config)}))

  (http/register-endpoint
   context
   {:path "/healthcheck"
    :method :get
    :fn #'healthcheck})

  {})

(defn -main [& _args]
  ;; Initialize klog
  (log/enable-log)
  (if dev?
    (log/stdout-pretty-appender :debug) ;; Pretty output for development
    (log/stdout-appender :info)) ;; JSON output for production

  (.addShutdownHook (Runtime/getRuntime)
                    (Thread. #(log/info ::shutdown {:msg "Got SIGTERM."})))
  (log/info ::server-start {:msg "Server started"})
  (log/info ::server-port {:port port})

  ;; Start system
  (let [context (system/start-system default-config)]

    ;; Start reload watcher if in volume mode
    (when (System/getenv "DOCS_VOLUME_PATH")
      (reload/start-reload-watcher context init-product-indices init-products))

    context))

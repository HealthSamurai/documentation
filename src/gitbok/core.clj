(ns gitbok.core
  (:require
   [gitbok.indexing.impl.sitemap :as sitemap]
   [edamame.core :as edamame]
   [clojure.string :as str]
   [hiccup2.core]
   [gitbok.markdown.core :as markdown]
   [gitbok.markdown.widgets]
   [gitbok.indexing.core :as indexing]
   [gitbok.indexing.impl.file-to-uri :as file-to-uri]
   [gitbok.indexing.impl.summary :as summary]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.ui.main-content :as main-content]
   [gitbok.ui.layout :as layout]
   [gitbok.ui.not-found :as not-found]
   [gitbok.ui.search]
   [ring.middleware.gzip :refer [wrap-gzip]]
   [gitbok.http]
   [gitbok.products :as products]
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
        filepath (products/filepath context filepath)
        content* (utils/slurp-resource filepath)
        {:keys [parsed description title]}
        (markdown/parse-markdown-content
         context
         [filepath content*])]
    (try
      {:content
       (main-content/render-file* context filepath parsed title content*)
       :description
       (or
        description
        (let [stripped (utils/strip-markdown content*)]
          (if (>= (count stripped) 150)
            (subs stripped 0 150)
            stripped)))
       :section section}
      (catch Exception e
        (println "cannot render file " filepath)
        {:content [:div {:role "alert"}
                   (.getMessage e)
                   [:pre (pr-str e)]
                   [:pre content*]]}))))

(defn picture-url? [url]
  (when url
    (str/starts-with? url "/.gitbook/assets")))

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
     :description (:description result)}))

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

(defn render-pictures [_ request]
  (let [uri (:uri request)
        uri-without-prefix
        (if (str/starts-with? uri prefix)
          (subs uri (count prefix))
          uri)]
    (resp/resource-response
     (->
      uri-without-prefix
      (str/replace #"%20" " ")
      (str/replace-first #".*.gitbook/" "")))))

(defn render-favicon [context _]
  (let [product (products/get-current-product context)
        favicon-path (or (:favicon product) "public/favicon.ico")]
    (if (str/starts-with? favicon-path ".gitbook/")
      ;; Handle .gitbook/assets paths like render-pictures does
      (resp/resource-response
       (str/replace-first favicon-path #".*.gitbook/" ""))
      ;; Regular resource path
      (resp/resource-response favicon-path))))

(defn render-file-view
  [context request]
  (let [uri (:uri request)
        uri-relative
        (utils/uri-to-relative
         uri
         prefix
         (products/path context))]
    (cond

      (or (= uri-relative "robots.txt")
          (= uri "/robots.txt"))
      (resp/resource-response "public/robots.txt")

      (or (picture-url? uri-relative)
          (picture-url? uri))
      (render-pictures context request)

      (str/starts-with? uri-relative "public/og-preview")
      (resp/resource-response uri-relative)

      :else
      (let [filepath
            (indexing/uri->filepath context uri-relative)]
        (if filepath
          (let [lastmod (indexing/get-lastmod context filepath)
                etag (utils/etag lastmod)]
            (if (or (check-cache-etag request etag)
                    (and lastmod
                         (check-cache-lastmod request lastmod)))
              {:status 304
               :headers {"Cache-Control" "public, max-age=86400"
                         "Last-Modified" lastmod
                         "ETag" etag}}
              (let [title (:title (get (indexing/file->uri-idx context) filepath))
                    {:keys [description content section]}
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
            :description "Page not found"}))))))

(defn sitemap-xml
  [context _]
  {:status 200
   :headers {"content-type" "application/xml"}
   :body (sitemap/get-sitemap context)})

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
        :description "Page not found"}))))

(defn healthcheck
  [_ _]
  {:status 200 :body {:status "ok"}})

(defn version-endpoint
  [context _]
  {:status 200 :body {:version (gitbok.http/get-version context)}})

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
          products-config (products/get-products-config context)
          product (products/determine-product-by-uri products-config uri)
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
    (println "1. read summary. create toc htmx.")
    (summary/set-summary ctx)
    (println "2. get uris from summary (using slugging), merge with redirects")
    (uri-to-file/set-idx ctx)
    (println "3. reverse file to uri idx")
    (file-to-uri/set-idx ctx)
    (println "4. using files from summary (step 3), read all files into memory")
    (indexing/set-md-files-idx ctx (file-to-uri/get-idx ctx))
    (println "5. parse all files into memory, some things are already rendered as plain html")
    (markdown/set-parsed-markdown-index ctx (indexing/get-md-files-idx ctx))
    (println "6. using parsed markdown, set search index")
    (indexing/set-search-idx ctx (markdown/get-parsed-markdown-index ctx))
    (println "7. render it on start")
    (when-not dev?
      (println "Not DEV, render all pages into memory")
      (markdown/render-all! ctx
                            (markdown/get-parsed-markdown-index ctx)
                            read-markdown-file)
      (println "Render all pages done."))
    (println "8. generate sitemap.xml for product")
    (when-not dev?
      (println (str "generating sitemap.xml for " (:name product)))
      (sitemap/set-sitemap
       ctx
       (edamame/parse-string (utils/slurp-resource "lastmod.edn"))))
    (println "9. set lastmod.edn in context for Last Modified metadata")
    ;; todo
    (indexing/set-lastmod ctx)))

(defn init-products
  "Initializes all products from configuration"
  [context workdir]
  (let [full-config (products/load-products-config workdir)
        products-config (:products full-config)]
    (products/set-products-config context products-config)
    (products/set-full-config context full-config)
    products-config))

#_{:clj-kondo/ignore [:unresolved-symbol]}
(system/defstart
  [context config]
  ;; Read WORKDIR at startup and store in system state

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
    :fn (fn [_ request]
          (resp/resource-response
           (str "public/" (get-in request [:params :path]))))})

  (http/register-endpoint
   context
   {:path "/.gitbook/assets/:path*"
    :method :get
    :middleware [gzip-middleware]
    :fn #'render-pictures})

  (let [workdir (System/getenv "WORKDIR")]
    (when workdir
      (println "WORKDIR set to:" workdir)
      (system/set-system-state context [::workdir] workdir)))

  ;; Initialize all products
  (let [products-config
        (init-products
         context
         (system/get-system-state context [::workdir]))]
    ;; Register endpoints for each product
    (doseq [product products-config]
      (let [product-path (utils/concat-urls prefix (:path product))]
        (println "base path! " product-path)

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

  (http/register-endpoint
   context
   {:path "/version"
    :method :get
    :fn #'version-endpoint})

  (let [products-config (products/get-products-config context)]
    (doseq [product products-config]
      (println)
      (println)
      (println (str "Initializing product indices: "
                    (:name product)))
      (init-product-indices context product))

    (println "setup done!")
    (println "version " (utils/slurp-resource "version"))
    (println "PORT env " (System/getenv "PORT"))
    (println "port " port)
    (println "BASE_URL " base-url)
    (println "PREFIX " prefix)
    (println "Products:\n"
             (str/join "\n" (mapv #(str (:name %) " - " (:path %))
                                  products-config))
             "\n"))

  (http/register-endpoint
   context
   {:path "/healthcheck"
    :method :get
    :fn #'healthcheck})

  {})

(defn -main [& _args]
  (.addShutdownHook (Runtime/getRuntime)
                    (Thread. #(println "Got SIGTERM.")))
  (println "Server started")
  (println "port " port)
  (system/start-system default-config))

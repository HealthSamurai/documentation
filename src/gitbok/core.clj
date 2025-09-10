(ns gitbok.core
  (:require
   [clojure.stacktrace]
   [clojure.string :as str]
   [clojure.tools.logging :as log]
   [gitbok.constants :as const]
   [gitbok.examples.updater :as examples-updater]
   [gitbok.examples.webhook]
   [gitbok.http]
   [gitbok.indexing.core :as indexing]
   [gitbok.indexing.impl.file-to-uri :as file-to-uri]
   [gitbok.indexing.impl.sitemap :as sitemap]
   [gitbok.indexing.impl.sitemap-index :as sitemap-index]
   [gitbok.indexing.impl.summary :as summary]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.markdown.core :as markdown]
   [gitbok.products :as products]
   [gitbok.reload :as reload]
   [gitbok.ui.examples]
   [gitbok.ui.landing-hero :as landing-hero]
   [gitbok.ui.layout :as layout]
   [gitbok.ui.main-content :as main-content]
   [gitbok.ui.meilisearch]
   [gitbok.ui.not-found :as not-found]
   [gitbok.ui.search]
   [gitbok.utils :as utils]
   [hiccup2.core]
   [http]
   [ring.middleware.content-type :refer [content-type-response]]
   [ring.middleware.gzip :refer [wrap-gzip]]
   [ring.util.response :as resp]
   [system]
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
        _ (when dev? (log/debug "reading file" {:filepath filepath :full-path full-filepath}))
        content* (if dev?
                   ;; Force re-read from disk in DEV mode
                   (do
                     (log/debug "reading fresh" {:path full-filepath})
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
        (log/info "cannot render file" {:filepath full-filepath})
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
      (let [dev-mode? (gitbok.http/get-dev-mode context)
            ;; In dev mode: no cache
            ;; In production: cache for 1 year (images are typically versioned)
            cache-control (if dev-mode?
                            "no-cache, no-store, must-revalidate"
                            "public, max-age=31536000, immutable")
            ;; Add proper Content-Type for SVG files
            response-with-type (if (str/ends-with? file-path ".svg")
                                 (resp/content-type response "image/svg+xml")
                                 response)
            ;; Add cache-control to the headers map
            headers (assoc (or (:headers response-with-type) {})
                           "Cache-Control" cache-control)]
        ;; Return response with updated headers
        (assoc response-with-type :headers headers)))))

(defn render-favicon [context _]
  (let [product (products/get-current-product context)
        favicon-path (or (:favicon product) "public/favicon.ico")]
    (if (str/starts-with? favicon-path ".gitbook/")
      ;; Handle .gitbook/assets paths like render-pictures does
      (resp/resource-response
       (str/replace-first favicon-path #".*.gitbook/" ""))
      ;; Regular resource path
      (resp/resource-response favicon-path))))

(defn render-landing
  [context request]
  (landing-hero/render-landing context request))

(defn render-file-view
  [context request]
  (let [uri (:uri request)
        product-path (products/path context)
        _ (log/info "render file view start"
                    {:uri uri
                     :prefix prefix
                     :product-path product-path
                     :current-product-id (:current-product-id context)
                     :headers (select-keys (:headers request) ["user-agent" "referer"])})
        uri-relative
        (utils/uri-to-relative
         uri
         prefix
         product-path)]
    (log/info "uri relative calculated" {:uri uri :uri-relative uri-relative})
    (cond

      (or (picture-url? uri-relative)
          (picture-url? uri))
      (do
        (log/info "serving picture" {:uri-relative uri-relative})
        (render-pictures context request))

      :else
      ;; Check for redirects first
      (if-let [redirect-target (indexing/get-redirect context uri-relative)]
        ;; Return HTTP 301 redirect
        (let [target-url (gitbok.http/get-product-prefixed-url context redirect-target)]
          ;; Log the redirect for monitoring
          (log/info "redirect 301" {:from uri-relative
                                    :to redirect-target
                                    :target-url target-url
                                    :user-agent (get-in request [:headers "user-agent"])
                                    :referer (get-in request [:headers "referer"])})
          {:status 301
           :headers {"Location" target-url}})
        ;; Otherwise proceed with normal file handling
        (let [_ (log/info "looking up filepath" {:uri-relative uri-relative})
              filepath (indexing/uri->filepath context uri-relative)
              _ (log/info "filepath resolved" {:uri-relative uri-relative :filepath filepath})]
          (if filepath
            (let [lastmod (indexing/get-lastmod context filepath)
                  lastmod-iso (utils/iso-to-http-date lastmod)
                  etag (utils/etag lastmod-iso)
                  _ (log/info "file metadata" {:filepath filepath
                                               :lastmod lastmod
                                               :etag etag})]
              (if (or (check-cache-etag request etag)
                      (and lastmod
                           (check-cache-lastmod request lastmod)))
                (do
                  (log/info "returning 304 not modified" {:filepath filepath})
                  {:status 304
                   :headers {"Cache-Control" "public, max-age=300"
                             "Last-Modified" lastmod-iso
                             "ETag" etag}})
                (let [_ (log/info "rendering file" {:filepath filepath})
                      {:keys [title description content section]}
                      (render-file (assoc context :current-uri uri-relative) filepath)
                      _ (log/info "file rendered" {:filepath filepath
                                                   :title title
                                                   :has-content (boolean content)
                                                   :section section})]
                  (layout/layout
                   context request
                   {:content content
                    :lastmod lastmod
                    :title title
                    :description description
                    :section section
                    :filepath filepath}))))

            (do
              (log/warn "file not found" {:uri-relative uri-relative
                                          :uri uri
                                          :product-id (:current-product-id context)})
              (layout/layout
               context request
               {:content (not-found/not-found-view context uri-relative)
                :status 404
                :title "Not found"
                :description "Page not found"
                :hide-breadcrumb true}))))))))

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
  {:status 200
   :body
   {:build-engine-version (gitbok.http/get-version context)
    :docs-update
    (reload/get-reload-state context)}})

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
  "Serves static files with proper content type and cache headers"
  [context request]
  (let [uri (gitbok.http/url-without-prefix context (:uri request))
        path (str/replace uri #"^/static/" "")
        response (resp/resource-response (utils/concat-urls "public" path))]
    (when response
      (let [dev-mode? (gitbok.http/get-dev-mode context)
            ;; In dev mode: no cache
            ;; In production: cache for 1 year (files are versioned)
            cache-control (if dev-mode?
                            "no-cache, no-store, must-revalidate"
                            "public, max-age=31536000, immutable")
            ;; First add content-type
            response-with-type (content-type-response response request)
            ;; Then add cache-control to the headers map
            headers (assoc (or (:headers response-with-type) {})
                           "Cache-Control" cache-control)]
        (log/debug "serving static file" {:path path
                                          :dev-mode? dev-mode?
                                          :cache-control cache-control
                                          :headers headers})
        ;; Return response with updated headers
        (assoc response-with-type :headers headers)))))

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
    (try
      (let [uri (:uri request)
            uri-without-prefix (if (str/starts-with? uri prefix)
                                 (subs uri (count prefix))
                                 uri)
            products-config (products/get-products-config context)
            _ (log/info "product middleware" {:uri uri
                                              :uri-without-prefix uri-without-prefix
                                              :products-count (count products-config)
                                              :has-products (boolean (seq products-config))})
            product (products/determine-product-by-uri products-config uri-without-prefix)
            _ (when-not product
                (log/error "no product found" {:uri uri
                                               :uri-without-prefix uri-without-prefix
                                               :products-config products-config}))
            product-id (if product
                         (:id product)
                         (do
                           (log/warn "using fallback product id" {:uri uri})
                           "default"))
            context (products/set-current-product-id context product-id)]
        (handler context request))
      (catch Exception e
        (log/error "product middleware error" {:uri (:uri request)
                                               :error (.getMessage e)
                                               :stacktrace (with-out-str (clojure.stacktrace/print-stack-trace e))})
        ;; Try to continue with default product
        (handler (products/set-current-product-id context "default") request)))))

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
    (log/info "init product" {:msg "1. read summary. create toc htmx."
                              :product (:id product)})
    (summary/set-summary ctx)
    (log/info "init product" {:msg "2. get uris from summary (using slugging), merge with redirects"
                              :product (:id product)})
    (uri-to-file/set-idx ctx)
    (log/info "init product" {:msg "3. reverse file to uri idx"
                              :product (:id product)})
    (file-to-uri/set-idx ctx)
    (log/info "init product" {:msg "4. using files from summary (step 3), read all files into memory"
                              :product (:id product)})
    (indexing/set-md-files-idx ctx (file-to-uri/get-idx ctx))
    (log/info "init product" {:msg "5. parse all files into memory, some things are already rendered as plain html"
                              :product (:id product)})
    (markdown/set-parsed-markdown-index ctx (indexing/get-md-files-idx ctx))
    (log/info "init product" {:msg "6. render it on start"
                              :product (:id product)})
    (when-not dev?
      (log/info "init product" {:msg "Not DEV, render all pages into memory"
                                :product (:id product)})
      (markdown/render-all! ctx
                            (markdown/get-parsed-markdown-index ctx)
                            read-markdown-file)
      (log/info "init product" {:msg "render done"
                                :product (:id product)}))
    (log/info "init product" {:msg "7. set lastmod data in context for Last Modified metadata"
                              :product (:id product)})
    (indexing/set-lastmod ctx)
    (log/info "init product" {:msg "8. generate sitemap.xml for product"
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
    :middleware [gzip-middleware]
    :fn #'serve-static-file})

  (http/register-endpoint
   context
   {:path (utils/concat-urls prefix "service-worker.js")
    :method :get
    :fn
    (fn [_ request]
      (let [response
            (resp/resource-response "public/service-worker.js")]
        (when response
          (content-type-response response request))))})

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
        (log/debug "base path" {:product-path product-path})

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

  ;; Webhook endpoint for examples updates
  (http/register-endpoint
   context
   {:path (utils/concat-urls prefix "/webhook/examples")
    :method :post
    :fn gitbok.examples.webhook/webhook-handler})

  ;; Manual update endpoint (only if GitHub PAT is configured)
  (when dev?
    (http/register-endpoint
     context
     {:path (utils/concat-urls prefix "/update-examples")
      :method :post
      :fn (fn [ctx req]
            (if (examples-updater/manual-update ctx)
              {:status 200
               :headers {"content-type" "text/plain"}
               :body "Examples updated successfully"}
              {:status 500
               :headers {"content-type" "text/plain"}
               :body "Failed to update examples"}))}))

  ;; Examples page (hardcoded path for Aidbox)
  (http/register-endpoint
   context
   {:path (utils/concat-urls prefix "/aidbox/examples")
    :method :get
    :middleware [gzip-middleware]
    :fn gitbok.ui.examples/examples-handler})

  ;; Examples results endpoint (for HTMX partial updates)
  (http/register-endpoint
   context
   {:path (utils/concat-urls prefix "/aidbox/examples-results")
    :method :get
    :middleware [gzip-middleware]
    :fn gitbok.ui.examples/examples-results-handler})

  (let [products-config (products/get-products-config context)]
    (doseq [product products-config]
      (log/info "init product indices" {:product-name (:name product)
                                        :product-id (:id product)})
      (init-product-indices context product))

    (log/info "setup complete" {:msg "setup done!"
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
  (log/info "server start" {:msg "Server started"})
  (log/info "server port" {:port port})

  ;; Start system
  (let [context (system/start-system default-config)]

    ;; Setup shutdown hook with context
    (.addShutdownHook (Runtime/getRuntime)
                      (Thread. (fn []
                                 (log/info "shutdown" {:msg "Got SIGTERM."})
                                ;; Stop examples updater if running
                                 (examples-updater/stop-scheduler context))))

    ;; Start reload watcher if in volume mode
    (when (System/getenv "DOCS_VOLUME_PATH")
      (reload/start-reload-watcher context init-product-indices init-products))

    ;; Start examples updater if GitHub PAT is configured
    (when (System/getenv "GITHUB_TOKEN")
      (log/info "starting examples updater" {})
      (examples-updater/start-scheduler context))

    context))

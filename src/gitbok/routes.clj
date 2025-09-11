(ns gitbok.routes
  (:require [reitit.ring :as ring]
            [reitit.ring.middleware.parameters :as parameters]
            [ring.middleware.content-type]
            [reitit.core]
            [muuntaja.core :as m]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [ring.middleware.gzip :refer [wrap-gzip]]
            [gitbok.state :as state]
            [gitbok.handlers :as handlers]
            [ring.util.response]
            [gitbok.ui.search]
            [gitbok.ui.meilisearch]
            [gitbok.ui.examples]
            [gitbok.examples.webhook]
            [gitbok.examples.updater :as examples-updater]
            [gitbok.products :as products]
            [gitbok.utils :as utils]
            [clojure.string :as str]
            [cheshire.core]))

;; Middleware
(defn product-middleware
  "Middleware to set current product based on route"
  [handler]
  (fn [request]
    (let [uri (:uri request)
          prefix (state/get-config :prefix "")
          ;; Remove prefix from URI
          uri-without-prefix (if (and (not (str/blank? prefix))
                                      (str/starts-with? uri prefix))
                               (subs uri (count prefix))
                               uri)
          ;; Extract product path from URI (first segment after removing prefix)
          product-path (when-let [segments (seq (filter #(not (str/blank? %))
                                                        (str/split uri-without-prefix #"/")))]
                         (str "/" (first segments)))
          products-config (state/get-products)]
      (if-let [product (first (filter #(= (:path %) product-path) products-config))]
        (do
          (state/set-current-product! product)
          (handler (assoc request :product product)))
        {:status 404
         :headers {"Content-Type" "text/plain"}
         :body (str "Product not found for path: " product-path)}))))

(defn wrap-request-context
  "Add context information to request"
  [handler]
  (fn [request]
    (handler (assoc request
                    :prefix (state/get-config :prefix "")
                    :base-url (state/get-config :base-url)
                    :dev-mode (state/get-config :dev-mode)))))

;; Handler adapters - convert from old (context, request) signature to new (request) signature
(defn adapt-handler
  "Adapt old-style handler (context, request) to new style (request)"
  [old-handler]
  (fn [request]
    ;; Create a minimal context for compatibility
    (let [context {:prefix (:prefix request)
                   :base-url (:base-url request)
                   :dev-mode (:dev-mode request)
                   :current-product-id (when-let [p (:product request)]
                                         (:id p))
                   ::products/current-product (:product request)}]
      (old-handler context request))))

;; Route handlers - these will gradually be refactored to not need context
(def healthcheck
  (fn [_request]
    {:status 200
     :headers {"Content-Type" "text/plain"}
     :body "OK"}))

(def version-endpoint
  (fn [_request]
    {:status 200
     :headers {"Content-Type" "text/plain"}
     :body (state/get-config :version "unknown")}))

(def debug-endpoint
  (fn [request]
    (let [dev-mode (state/get-config :dev-mode)]
      (if dev-mode
        {:status 200
         :headers {"Content-Type" "application/json"}
         :body (cheshire.core/generate-string
                {:state (state/get-full-state)
                 :request (select-keys request [:uri :request-method :path-params :query-params])})}
        {:status 403
         :headers {"Content-Type" "text/plain"}
         :body "Debug endpoint only available in dev mode"}))))

(def root-redirect-handler
  (adapt-handler handlers/root-redirect-handler))

(def redirect-to-readme
  (adapt-handler handlers/redirect-to-readme))

(def render-file-view
  (adapt-handler handlers/render-file-view))

(def serve-static-file
  (adapt-handler handlers/serve-static-file))

(def serve-og-preview
  (adapt-handler handlers/serve-og-preview))

(def render-pictures
  (adapt-handler handlers/render-pictures))

(def render-favicon
  (adapt-handler handlers/render-favicon))

(def render-landing
  (adapt-handler handlers/render-landing))

(def sitemap-xml
  (adapt-handler handlers/sitemap-xml))

(def sitemap-index-xml
  (adapt-handler handlers/sitemap-index-xml))

(def service-worker-handler
  (fn [request]
    (let [response (ring.util.response/resource-response "public/service-worker.js")]
      (when response
        (ring.middleware.content-type/content-type-response response request)))))

(def search-endpoint
  (adapt-handler gitbok.ui.search/search-endpoint))

(def meilisearch-endpoint
  (adapt-handler gitbok.ui.meilisearch/meilisearch-endpoint))

(def examples-handler
  (adapt-handler gitbok.ui.examples/examples-handler))

(def examples-results-handler
  (adapt-handler gitbok.ui.examples/examples-results-handler))

(def webhook-handler
  (adapt-handler gitbok.examples.webhook/webhook-handler))

(def manual-update-handler
  (fn [_request]
    (if (state/get-config :dev-mode)
      (if (examples-updater/manual-update)
        {:status 200
         :headers {"Content-Type" "text/plain"}
         :body "Examples updated successfully"}
        {:status 500
         :headers {"Content-Type" "text/plain"}
         :body "Failed to update examples"})
      {:status 403
       :headers {"Content-Type" "text/plain"}
       :body "Manual update only available in dev mode"})))

;; Route definitions
(defn routes []
  (let [prefix (state/get-config :prefix "")]
    [;; Static routes
     [(str prefix "/static/*") {:get {:handler serve-static-file}}]
     [(str prefix "/service-worker.js") {:get {:handler service-worker-handler}}]
     [(str prefix "/public/og-preview/*") {:get {:handler serve-og-preview}}]
     [(str prefix "/.gitbook/assets/*") {:get {:handler render-pictures
                                               :middleware [wrap-gzip]}}]

     ;; System routes
     ["/healthcheck" {:get {:handler healthcheck}}]
     [(str prefix "/version") {:get {:handler version-endpoint}}]
     [(str prefix "/debug") {:get {:handler debug-endpoint}}]

     ;; Webhook
     [(str prefix "/webhook/examples") {:post {:handler webhook-handler}}]

     ;; Manual update (dev only)
     (when (state/get-config :dev-mode)
       [(str prefix "/update-examples") {:post {:handler manual-update-handler}}])

     ;; Examples (hardcoded for Aidbox)
     [(str prefix "/aidbox/examples") {:get {:handler examples-handler
                                             :middleware [wrap-gzip]}}]
     [(str prefix "/aidbox/examples-results") {:get {:handler examples-results-handler
                                                     :middleware [wrap-gzip]}}]

     ;; Sitemap at root
     [(str prefix "/sitemap.xml") {:get {:handler sitemap-index-xml}}]

     ;; Root handlers
     [prefix {:get {:handler root-redirect-handler
                    :middleware [wrap-gzip]}}]
     [(str prefix "/") {:get {:handler root-redirect-handler
                              :middleware [wrap-gzip]}}]

     ;; Product routes - these are dynamic based on products config
     ;; We'll generate these dynamically from products
     ]))

(defn product-routes
  "Generate routes for a specific product"
  [product prefix]
  (let [product-path (utils/concat-urls prefix (:path product))]
    [;; Specific routes first
     ;; Product search
     [(str product-path "/search/dropdown")
      {:get {:handler search-endpoint
             :middleware [product-middleware wrap-gzip]}}]

     ;; Meilisearch
     [(str product-path "/meilisearch/dropdown")
      {:get {:handler meilisearch-endpoint
             :middleware [product-middleware]}}]

     ;; Product sitemap
     [(str product-path "/sitemap.xml")
      {:get {:handler sitemap-xml
             :middleware [product-middleware]}}]

     ;; Product favicon
     [(str product-path "/favicon.ico")
      {:get {:handler render-favicon
             :middleware [product-middleware wrap-gzip]}}]

     ;; Product landing
     [(str product-path "/landing")
      {:get {:handler render-landing
             :middleware [product-middleware wrap-gzip]}}]

     ;; Product root
     [product-path {:get {:handler redirect-to-readme
                          :middleware [product-middleware wrap-gzip]}}]

     ;; All product pages (catch-all) - must be last!
     [(str product-path "/*")
      {:get {:handler render-file-view
             :middleware [product-middleware wrap-gzip]}}]]))

(defn all-routes
  "Generate all routes including dynamic product routes"
  []
  (let [prefix (state/get-config :prefix "")
        products-config (state/get-products)
        static-routes (routes)
        product-route-list (mapcat #(product-routes % prefix) products-config)
        ;; Separate specific routes from wildcards
        specific-static (filter #(and % (not (clojure.string/includes? (first %) "*"))) static-routes)
        wildcard-static (filter #(and % (clojure.string/includes? (first %) "*")) static-routes)]
    (vec (concat
          specific-static           ; Specific static routes first
          product-route-list        ; Product routes (with their own ordering)
          wildcard-static))))

(defn create-app
  "Create the ring handler with all routes"
  []
  (ring/ring-handler
   (ring/router
    (all-routes)
    {:data {:muuntaja m/instance
            :middleware [;; Request parsing
                         parameters/parameters-middleware
                         muuntaja/format-negotiate-middleware
                         muuntaja/format-response-middleware
                         muuntaja/format-request-middleware
                        ;; Custom context
                         wrap-request-context]}
     ;; Use linear-router for predictable route matching order
     :router reitit.core/linear-router
     ;; Disable conflict detection since we want ordered matching
     :conflicts nil})))

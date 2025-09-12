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
            [gitbok.ui.landing-hero]
            [gitbok.products :as products]
            [gitbok.utils :as utils]
            [clojure.string :as str]
            [cheshire.core]))

;; Middleware
(defn product-middleware
  "Middleware to set current product based on route"
  [handler]
  (fn [request]
    (let [context (:context request)
          uri (:uri request)
          prefix (state/get-config context :prefix "")
          ;; Remove prefix from URI
          uri-without-prefix (if (and (not (str/blank? prefix))
                                      (str/starts-with? uri prefix))
                               (subs uri (count prefix))
                               uri)
          ;; Extract product path from URI (first segment after removing prefix)
          product-path (when-let [segments (seq (filter #(not (str/blank? %))
                                                        (str/split uri-without-prefix #"/")))]
                         (str "/" (first segments)))
          products-config (state/get-products context)]
      (if-let [product (first (filter #(= (:path %) product-path) products-config))]
        (handler (assoc request :product product))
        {:status 404
         :headers {"Content-Type" "text/plain"}
         :body (str "Product not found for path: " product-path)}))))

(defn wrap-request-context
  "Add context information to request"
  [context]
  (fn [handler]
    (fn [request]
      ;; Request should be part of context, not the other way around
      (let [context-with-request (assoc context :request request)]
        (handler (assoc request :context context-with-request))))))

;; Handler adapters - convert from old (context, request) signature to new (request) signature
(defn adapt-handler
  "Adapt old-style handler (context, request) to new style (request)"
  [old-handler]
  (fn [request]
    ;; Use context from request
    (let [context (:context request)
          ;; Add product info to context if available
          context (if-let [product (:product request)]
                    (assoc context
                           :current-product-id (:id product)
                           ::products/current-product product)
                    context)]
      (old-handler context request))))

;; Route handlers - these will gradually be refactored to not need context
(def healthcheck
  (fn [_request]
    {:status 200
     :headers {"Content-Type" "text/plain"}
     :body "OK"}))

(def version-endpoint
  (fn [request]
    {:status 200
     :headers {"Content-Type" "text/plain"}
     :body (state/get-config (:context request) :version "unknown")}))

(def debug-endpoint
  (fn [request]
    (let [context (:context request)
          dev-mode (state/get-config context :dev-mode)]
      (if dev-mode
        {:status 200
         :headers {"Content-Type" "application/json"}
         :body (cheshire.core/generate-string
                {:state (state/get-full-state context)
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
  (adapt-handler gitbok.ui.landing-hero/render-landing))

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

;; Route definitions
(defn routes [context]
  (let [prefix (state/get-config context :prefix "")]
    [;; Static routes
     [(utils/concat-urls prefix "/static/*") {:get {:handler serve-static-file}}]
     [(utils/concat-urls prefix "/service-worker.js") {:get {:handler service-worker-handler}}]
     [(utils/concat-urls prefix "/public/og-preview/*") {:get {:handler serve-og-preview}}]
     [(utils/concat-urls prefix "/.gitbook/assets/*") {:get {:handler render-pictures
                                               :middleware [wrap-gzip]}}]

     ;; System routes
     ["/healthcheck" {:get {:handler healthcheck}}]
     [(str prefix "/version") {:get {:handler version-endpoint}}]
     [(str prefix "/debug") {:get {:handler debug-endpoint}}]

     ;; Removed manual update handler


     ;; Sitemap at root
     [(str prefix "/sitemap.xml") {:get {:handler sitemap-index-xml}}]

     ;; Root handlers
     [prefix {:get {:handler root-redirect-handler
                    :middleware [wrap-gzip]}}]
     [(str prefix "/") {:get {:handler root-redirect-handler
                              :middleware [wrap-gzip]}}]

     ;; Product routes are generated dynamically from products config
     ]))

(defn product-routes
  "Generate routes for a specific product"
  [product prefix]
  (let [product-path (utils/concat-urls prefix (:path product))
        routes [;; Specific routes first
                ;; Product search
                [(str product-path "/search/dropdown")
                 {:get {:handler search-endpoint
                        :middleware [product-middleware wrap-gzip]}}]

                ;; Meilisearch
                [(str product-path "/meilisearch/dropdown")
                 {:get {:handler meilisearch-endpoint
                        :middleware [product-middleware wrap-gzip]}}]

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
                                     :middleware [product-middleware wrap-gzip]}}]]]
    ;; Add examples routes only for Aidbox product
    (if (= (:id product) "aidbox")
      (concat routes
              [;; Examples page
               [(str product-path "/examples")
                {:get {:handler examples-handler
                       :middleware [product-middleware wrap-gzip]}}]
               ;; Examples results endpoint for HTMX
               [(str product-path "/examples-results")
                {:get {:handler examples-results-handler
                       :middleware [product-middleware wrap-gzip]}}]
               ;; All product pages (catch-all) - must be last!
               [(str product-path "/*")
                {:get {:handler render-file-view
                       :middleware [product-middleware wrap-gzip]}}]])
      ;; For non-Aidbox products, just add the catch-all
      (conj routes
            [(str product-path "/*")
             {:get {:handler render-file-view
                    :middleware [product-middleware wrap-gzip]}}]))))

(defn all-routes
  "Generate all routes including dynamic product routes"
  [context]
  (let [prefix (state/get-config context :prefix "")
        products-config (state/get-products context)
        static-routes (routes context)
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
  [context]
  (ring/ring-handler
   (ring/router
    (all-routes context)
    {:data {:muuntaja m/instance
            :middleware [;; Request parsing
                         parameters/parameters-middleware
                         muuntaja/format-negotiate-middleware
                         muuntaja/format-response-middleware
                         muuntaja/format-request-middleware
                        ;; Custom context
                         (wrap-request-context context)]}
     ;; Use linear-router for predictable route matching order
     :router reitit.core/linear-router
     ;; Disable conflict detection since we want ordered matching
     :conflicts nil})))

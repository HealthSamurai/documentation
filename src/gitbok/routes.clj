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
            [gitbok.ui.meilisearch]
            [gitbok.ui.examples]
            [gitbok.ui.landing-hero]
            [gitbok.utils :as utils]
            [clojure.string :as str]
            [cheshire.core]))

;; Middleware
(defn product-middleware
  "Middleware to set current product based on route"
  [handler]
  (fn [ctx]
    (let [request (:request ctx)
          uri (:uri request)
          prefix (state/get-config ctx :prefix "")
          ;; Remove prefix from URI
          uri-without-prefix (if (and (not (str/blank? prefix))
                                      (str/starts-with? uri prefix))
                               (subs uri (count prefix))
                               uri)
          ;; Extract product path from URI (first segment after removing prefix)
          product-path (when-let [segments (seq (filter #(not (str/blank? %))
                                                        (str/split uri-without-prefix #"/")))]
                         (str "/" (first segments)))
          products-config (state/get-products ctx)]
      (if-let [product (first (filter #(= (:path %) product-path) products-config))]
        (handler (assoc ctx
                        :product product
                        :current-product-id (:id product)))
        {:status 404
         :headers {"Content-Type" "text/plain"}
         :body (str "Product not found for path: " product-path)}))))

(defn wrap-request-context
  "Middleware that creates a unified context containing system state and request"
  [system-context]
  (fn [handler]
    (fn [request]
      ;; Create unified context with request inside
      (let [ctx (assoc system-context :request request)]
        (handler ctx)))))

;; Route handlers
(defn healthcheck
  [ctx]
  (if (state/get-cache ctx :app-initialized false)
    {:status 200
     :headers {"Content-Type" "text/plain"}
     :body "OK"}
    {:status 503
     :headers {"Content-Type" "text/plain"}
     :body "Service Unavailable - Application still initializing"}))

(defn version-endpoint
  [ctx]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (state/get-config ctx :version "unknown")})

(defn debug-endpoint
  [ctx]
  (let [request (:request ctx)
        dev-mode (state/get-config ctx :dev-mode)]
    (if dev-mode
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (cheshire.core/generate-string
              {:state (state/get-full-state ctx)
               :request (select-keys request [:uri :request-method :path-params :query-params])})}
      {:status 403
       :headers {"Content-Type" "text/plain"}
       :body "Debug endpoint only available in dev mode"})))

(defn service-worker-handler
  [ctx]
  (let [request (:request ctx)
        response (ring.util.response/resource-response "public/service-worker.js")]
    (when response
      (ring.middleware.content-type/content-type-response response request))))

;; Route definitions
(defn routes [context]
  (let [prefix (state/get-config context :prefix "")]
    [;; Static routes
     [(utils/concat-urls prefix "/static/*")
      {:get {:handler #'handlers/serve-static-file
             :middleware [wrap-gzip]}}]
     [(utils/concat-urls prefix "/service-worker.js")
      {:get {:handler #'service-worker-handler
             :middleware [wrap-gzip]}}]
     [(utils/concat-urls prefix "/public/og-preview/*")
      {:get {:handler #'handlers/serve-og-preview
             :middleware [wrap-gzip]}}]
     [(utils/concat-urls prefix "/.gitbook/assets/*")
      {:get {:handler #'handlers/render-pictures
             :middleware [wrap-gzip]}}]

     ;; System routes
     ["/healthcheck"
      {:get {:handler #'healthcheck}}]

     [(utils/concat-urls prefix "/version")
      {:get {:handler #'version-endpoint}}]
     [(utils/concat-urls prefix "/debug")
      {:get {:handler #'debug-endpoint}}]

     ;; Sitemap at root
     [(utils/concat-urls prefix "/sitemap.xml")
      {:get {:handler #'handlers/sitemap-index-xml
             :middleware [wrap-gzip]}}]

     ;; Root handlers - handle both with and without trailing slash
     [prefix {:get {:handler #'handlers/root-redirect-handler
                    :middleware [wrap-gzip]}}]
     [(str prefix "/")
      {:get {:handler #'handlers/root-redirect-handler
             :middleware [wrap-gzip]}}]

     ;; Product routes are generated dynamically from products config
     ]))

(defn product-routes
  "Generate routes for a specific product"
  [product prefix]
  (let [product-path (utils/concat-urls prefix (:path product))
        routes [;; Specific routes first

                ;; Meilisearch
                [(str product-path "/meilisearch/dropdown")
                 {:get {:handler #'gitbok.ui.meilisearch/meilisearch-endpoint
                        :middleware [product-middleware wrap-gzip]}}]

                ;; Product sitemap
                [(str product-path "/sitemap.xml")
                 {:get {:handler #'handlers/sitemap-xml
                        :middleware [product-middleware wrap-gzip]}}]

                ;; Product favicon
                [(str product-path "/favicon.ico")
                 {:get {:handler #'handlers/render-favicon
                        :middleware [product-middleware wrap-gzip]}}]

                ;; Product root - with trailing slash (redirect to without)
                [(str product-path "/") {:get {:handler (fn [ctx]
                                                          (let [request (:request ctx)
                                                                uri (:uri request)
                                                                ;; Remove trailing slash
                                                                new-uri (subs uri 0 (dec (count uri)))]
                                                            {:status 301
                                                             :headers {"Location" new-uri}}))
                                               :middleware [wrap-gzip]}}]
                ;; Product root - without trailing slash
                [product-path {:get {:handler #'handlers/redirect-to-readme
                                     :middleware [product-middleware wrap-gzip]}}]]]
    ;; Add Aidbox-specific routes
    (if (= (:id product) "aidbox")
      (concat routes
              [;; Landing page - Aidbox only
               [(str product-path "/landing")
                {:get {:handler #'gitbok.ui.landing-hero/render-landing
                       :middleware [product-middleware wrap-gzip]}}]
               ;; Examples page
               [(str product-path "/examples")
                {:get {:handler #'gitbok.ui.examples/examples-handler
                       :middleware [product-middleware wrap-gzip]}}]
               ;; Examples results endpoint for HTMX
               [(str product-path "/examples-results")
                {:get {:handler #'gitbok.ui.examples/examples-results-handler
                       :middleware [product-middleware wrap-gzip]}}]
               ;; All product pages (catch-all) - must be last!
               [(str product-path "/*")
                {:get {:handler #'handlers/render-file-view
                       :middleware [product-middleware wrap-gzip]}}]])
      ;; For non-Aidbox products, just add the catch-all
      (conj routes
            [(str product-path "/*")
             {:get {:handler #'handlers/render-file-view
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


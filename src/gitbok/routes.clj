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
            [gitbok.metrics :as metrics]
            [gitbok.middleware.session :as session]
            [gitbok.middleware.posthog :as posthog-middleware]
            [ring.util.response]
            [gitbok.ui.meilisearch]
            [gitbok.ui.examples]
            [gitbok.ui.landing-hero]
            [gitbok.utils :as utils]
            [clojure.string :as str]
            [clojure.tools.logging :as log]
            [clojure.stacktrace]
            [cheshire.core]
            [clojure.walk]
            [hiccup.util]))

;; Middleware
(defn wrap-request-logging
  "Middleware to log incoming requests and responses"
  [handler]
  (fn [request]
    (let [start-time (System/currentTimeMillis)
          method (:request-method request)
          uri (:uri request)
          query-string (:query-string request)]
      (try
        (let [response (handler request)
              duration (- (System/currentTimeMillis) start-time)
              status (:status response)]
          (log/info
           (str uri " " status)
           {:method method
            :uri uri
            :duration-ms duration
            :query-string query-string})
          response)
        (catch Exception e
          (let [duration (- (System/currentTimeMillis) start-time)]
            (log/error e "Request failed" {:method method
                                           :uri uri
                                           :duration-ms duration})
            (throw e)))))))

(defn wrap-exception-handler
  "Global exception handler middleware that captures and logs stack traces"
  [handler]
  (fn [request]
    (try
      (handler request)
      (catch Exception e
        (let [error-id (str (java.util.UUID/randomUUID))
              stack-trace (with-out-str (clojure.stacktrace/print-stack-trace e))
              uri (:uri request)
              method (:request-method request)]
          (log/error e "Unhandled exception caught by global handler"
                     {:error-id error-id
                      :uri uri
                      :method method
                      :message (.getMessage e)
                      :exception-class (.getName (.getClass e))
                      :stack-trace stack-trace})
          ;; Return appropriate error response
          {:status 500
           :headers {"Content-Type" "text/html; charset=utf-8"}
           :body (str "<!DOCTYPE html>\n"
                      "<html><head><title>Internal Server Error</title></head>\n"
                      "<body>\n"
                      "<h1>Internal Server Error</h1>\n"
                      "<p>An unexpected error occurred.</p>\n"
                      "<p><strong>Error ID:</strong> " error-id "</p>\n"
                     ;; Only show stack trace in dev mode
                      (when (state/get-config request :dev-mode)
                        (str "<h2>Stack Trace (Development Mode Only)</h2>\n"
                             "<pre style=\"background: #f4f4f4; padding: 10px; overflow: auto;\">"
                             (hiccup.util/escape-html stack-trace)
                             "</pre>"))
                      "</body></html>")})))))
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
        (do
          (log/warn "Product not found" {:product-path product-path
                                         :available-paths (map :path products-config)})
          {:status 404
           :headers {"Content-Type" "text/plain"}
           :body (str "Product not found for path: " product-path)})))))

(defn partial-product-middleware
  "Middleware to set current product for partial routes"
  [handler]
  (fn [ctx]
    (let [request (:request ctx)
          uri (:uri request)
          prefix (state/get-config ctx :prefix "")
          ;; Remove prefix and /partial from URI
          uri-without-prefix (if (and (not (str/blank? prefix))
                                      (str/starts-with? uri prefix))
                               (subs uri (count prefix))
                               uri)
          ;; Remove /partial prefix
          uri-without-partial (if (str/starts-with? uri-without-prefix "/partial")
                                (subs uri-without-prefix 8) ; length of "/partial"
                                uri-without-prefix)
          ;; Extract product path from URI (first segment after removing /partial)
          product-path (when-let [segments (seq (filter #(not (str/blank? %))
                                                        (str/split uri-without-partial #"/")))]
                         (str "/" (first segments)))
          products-config (state/get-products ctx)]
      (if-let [product (first (filter #(= (:path %) product-path) products-config))]
        (handler (assoc ctx
                        :product product
                        :current-product-id (:id product)))
        (do
          (log/warn "Product not found for partial" {:product-path product-path
                                                     :uri uri
                                                     :available-paths (map :path products-config)})
          {:status 404
           :headers {"Content-Type" "text/plain"}
           :body (str "Product not found for partial path: " product-path)})))))

(defn wrap-request-context
  "Middleware that creates a unified context containing system state and request"
  [system-context]
  (fn [handler]
    (fn [request]
      ;; Create unified context with request inside
      (let [ctx (assoc system-context :request request)]
        (handler ctx)))))

(defn head-handler
  "Create a HEAD handler from a GET handler that returns the same response without body"
  [get-handler]
  (fn [ctx]
    (let [response (get-handler ctx)]
      (assoc response :body ""))))

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
  (let [version (state/get-config ctx :version "unknown")
        reload-state (state/get-cache ctx :reload-state {})]
    {:status 200
     :headers {"Content-Type" "application/json"}
     :body (cheshire.core/generate-string
            {:build-engine-version version
             :docs-update {:git-head (:git-head reload-state)
                           :last-reload-time (:last-reload-time reload-state)
                           :app-version (:app-version reload-state)
                           :in-progress (:in-progress reload-state false)}})}))

(defn debug-endpoint
  [ctx]
  (let [request (:request ctx)
        dev-mode (state/get-config ctx :dev-mode)]
    (if dev-mode
      (let [full-state (state/get-full-state ctx)
            ;; Convert state to a JSON-safe structure
            json-safe-state (clojure.walk/postwalk
                             (fn [v]
                               (cond
                                 ;; Convert RawString to plain string
                                 (instance? hiccup.util.RawString v) (str v)
                                 ;; Convert other non-serializable objects to strings
                                 (and (not (or (nil? v)
                                               (boolean? v)
                                               (number? v)
                                               (string? v)
                                               (keyword? v)
                                               (map? v)
                                               (sequential? v)))
                                      (instance? Object v))
                                 (str (class v))
                                 :else v))
                             full-state)]
        {:status 200
         :headers {"Content-Type" "application/json"}
         :body (cheshire.core/generate-string
                {:state json-safe-state
                 :request (select-keys request [:uri :request-method :path-params :query-params])})})
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
      {:get {:handler #'handlers/serve-static-file}}]
     [(utils/concat-urls prefix "/service-worker.js")
      {:get {:handler #'service-worker-handler}}]
     [(utils/concat-urls prefix "/public/og-preview/*")
      {:get {:handler #'handlers/serve-og-preview}}]
     [(utils/concat-urls prefix "/.gitbook/assets/*")
      {:get {:handler #'handlers/render-pictures}}]

     ;; System routes
     ["/healthcheck"
      {:get {:handler #'healthcheck}}]

     ;; Metrics endpoint for Prometheus (without prefix)
     ["/metrics"
      {:get {:handler #'metrics/metrics-handler}}]

     [(utils/concat-urls prefix "/version")
      {:get {:handler #'version-endpoint}}]
     [(utils/concat-urls prefix "/debug")
      {:get {:handler #'debug-endpoint}}]

     ;; Sitemap at root
     [(utils/concat-urls prefix "/sitemap.xml")
      {:get {:handler #'handlers/sitemap-index-xml}}]

     ;; Root handlers - handle both with and without trailing slash
     [prefix {:get {:handler #'handlers/root-redirect-handler}}]
     [(str prefix "/")
      {:get {:handler #'handlers/root-redirect-handler}}]

     ;; Product routes are generated dynamically from products config
     ]))

(defn product-routes
  "Generate routes for a specific product"
  [product prefix]
  (let [product-path (utils/concat-urls prefix (:path product))
        partial-product-path (utils/concat-urls prefix "/partial" (:path product))
        trailing-slash-handler (fn [ctx]
                                 (let [request (:request ctx)
                                       uri (:uri request)
                                       ;; Remove trailing slash
                                       new-uri (subs uri 0 (dec (count uri)))]
                                   {:status 301
                                    :headers {"Location" new-uri}}))
        product-root-handler (if (= (:id product) "aidbox")
                               #'gitbok.ui.landing-hero/render-landing
                               #'handlers/redirect-to-readme)
        routes [;; Specific routes first

                ;; Meilisearch
                [(str product-path "/meilisearch/dropdown")
                 {:get {:handler #'gitbok.ui.meilisearch/meilisearch-endpoint
                        :middleware [product-middleware]}}]

                ;; Product sitemap
                [(str product-path "/sitemap.xml")
                 {:get {:handler #'handlers/sitemap-xml
                        :middleware [product-middleware]}}]

                ;; Product favicon
                [(str product-path "/favicon.ico")
                 {:get {:handler #'handlers/render-favicon
                        :middleware [product-middleware]}}]

                ;; Partial root route - redirect to overview/readme
                [partial-product-path
                 {:get {:handler #'handlers/render-partial-view
                        :middleware [partial-product-middleware]}}]

                ;; Partial routes for HTMX
                [(str partial-product-path "/*")
                 {:get {:handler #'handlers/render-partial-view
                        :middleware [partial-product-middleware]}}]

                ;; Product root - with trailing slash (redirect to without)
                [(str product-path "/") {:get {:handler trailing-slash-handler}}]
                ;; Product root - without trailing slash
                ;; For Aidbox - show landing, for others - redirect to readme
                [product-path {:get {:handler product-root-handler
                                     :middleware [product-middleware]}
                               :head {:handler (head-handler product-root-handler)
                                      :middleware [product-middleware]}}]]]
    ;; Add Aidbox-specific routes
    (if (= (:id product) "aidbox")
      (concat routes
              [;; Examples page
               [(str product-path "/examples")
                {:get {:handler #'gitbok.ui.examples/examples-handler
                       :middleware [product-middleware]}}]
               ;; Examples results endpoint for HTMX
               [(str product-path "/examples-results")
                {:get {:handler #'gitbok.ui.examples/examples-results-handler
                       :middleware [product-middleware]}}]
               ;; All product pages (catch-all) - must be last!
               [(str product-path "/*")
                {:get {:handler #'handlers/render-file-view
                       :middleware [product-middleware]}
                 :head {:handler (head-handler #'handlers/render-file-view)
                        :middleware [product-middleware]}}]])
      ;; For non-Aidbox products, just add the catch-all
      (conj routes
            [(str product-path "/*")
             {:get {:handler #'handlers/render-file-view
                    :middleware [product-middleware]}
              :head {:handler (head-handler #'handlers/render-file-view)
                     :middleware [product-middleware]}}]))))

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
          specific-static ; Specific static routes first
          product-route-list ; Product routes (with their own ordering)
          wildcard-static))))

(defn create-app
  "Create the ring handler with all routes"
  [context]
  (let [routes (all-routes context)
        products (state/get-products context)]
    (log/info "Creating app" {:routes-count (count routes)
                              :products-count (count products)
                              :products (map #(select-keys % [:id :name :path]) products)
                              :sample-routes (take 10 (map first routes))})
    (ring/ring-handler
     (ring/router
      routes
      {:data {:muuntaja m/instance
              :middleware [;; Session management (must be early to set session-id for all requests)
                           session/wrap-session
                           ;; PostHog tracking (after session, before metrics to track properly)
                           #(posthog-middleware/wrap-posthog-tracking % context)
                           ;; Metrics collection middleware (before gzip to measure actual response time)
                           metrics/wrap-metrics
                           ;; Gzip compression (must be early to compress final response)
                           wrap-gzip
                           ;; Global exception handler
                           wrap-exception-handler
                           ;; Request logging
                           wrap-request-logging
                           ;; Request parsing
                           parameters/parameters-middleware
                           muuntaja/format-negotiate-middleware
                           muuntaja/format-response-middleware
                           muuntaja/format-request-middleware
                          ;; Custom context
                           (wrap-request-context context)]}
       ;; Use linear-router for predictable route matching order
       :router reitit.core/linear-router
       ;; Disable conflict detection since we want ordered matching
       :conflicts nil}))))

(defn create-dynamic-handler
  "Creates a handler wrapper that reads the current router from state on each request.
   This allows the router to be updated (e.g., during reload) without restarting the HTTP server."
  [context]
  (fn [request]
    (if-let [router (state/get-cache context :app-router)]
      (router request)
      {:status 503
       :headers {"Content-Type" "text/plain"}
       :body "Service initializing - router not ready"})))

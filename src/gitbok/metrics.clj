(ns gitbok.metrics
  (:require [iapetos.core :as prometheus]
            [iapetos.export :as export]
            [iapetos.collector.ring :as ring-collector]
            [iapetos.collector.jvm :as jvm-collector]
            [clojure.tools.logging :as log]))

(defonce registry
  (atom nil))

(defn initialize-metrics!
  "Initialize Prometheus metrics registry with collectors"
  []
  (log/info "Initializing Prometheus metrics...")

  ;; Create and store the registry
  (reset! registry
          (-> (prometheus/collector-registry)
              ;; Register JVM metrics
              (jvm-collector/initialize)

              ;; Register custom metrics
              (prometheus/register
               (prometheus/histogram
                :http/request-duration-seconds
                {:description "HTTP request duration in seconds"
                 :labels [:method :status :path]}
                [0.001 0.005 0.01 0.025 0.05 0.075 0.1 0.25 0.5 0.75 1 2.5 5 7.5 10]))

              (prometheus/register
               (prometheus/counter
                :http/requests-total
                {:description "Total number of HTTP requests"
                 :labels [:method :status :path]}))

              (prometheus/register
               (prometheus/counter
                :http/errors-5xx-total
                {:description "Total number of 5xx errors"
                 :labels [:method :path]}))))

  (log/info "Prometheus metrics initialized successfully")
  @registry)

(defn normalize-path
  "Normalize path for metrics to avoid high cardinality"
  [path]
  (cond
    ;; Static assets
    (re-matches #"^/public/.*" path) "/public/*"
    (re-matches #"^/api/.*" path) "/api/*"
    ;; Health and metrics endpoints
    (= path "/health") "/health"
    (= path "/metrics") "/metrics"
    (= path "/version") "/version"
    ;; Documentation paths - group by product
    (re-matches #"^/[^/]+/.*" path)
    (let [[_ product] (re-matches #"^/([^/]+)/.*" path)]
      (str "/" product "/*"))
    :else path))

(defn wrap-metrics
  "Ring middleware for collecting HTTP metrics"
  [handler]
  (fn [request]
    (if-let [reg @registry]
      (let [start-time (System/currentTimeMillis)
            method (name (:request-method request))
            path (normalize-path (:uri request))]
        (try
          (let [response (handler request)
                status (:status response)
                duration (/ (- (System/currentTimeMillis) start-time) 1000.0)]

            ;; Record metrics
            (-> reg
                (prometheus/inc :http/requests-total
                               {:method method :status (str status) :path path})
                (prometheus/observe :http/request-duration-seconds
                                   {:method method :status (str status) :path path}
                                   duration))

            ;; Track 5xx errors specifically
            (when (and status (>= status 500) (< status 600))
              (prometheus/inc reg :http/errors-5xx-total
                             {:method method :path path})
              (log/warn "5xx error recorded" {:status status :method method :path path}))

            response)
          (catch Exception e
            ;; Record as 500 error
            (-> reg
                (prometheus/inc :http/requests-total
                               {:method method :status "500" :path path})
                (prometheus/inc :http/errors-5xx-total
                               {:method method :path path}))
            (throw e))))
      ;; If registry not initialized, just pass through
      (handler request))))

(defn metrics-handler
  "Handler for /metrics endpoint"
  [_]
  (if-let [reg @registry]
    {:status 200
     :headers {"Content-Type" "text/plain; version=0.0.4; charset=utf-8"}
     :body (export/text-format reg)}
    {:status 503
     :headers {"Content-Type" "text/plain"}
     :body "Metrics not initialized"}))
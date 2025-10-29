(ns gitbok.core
  (:gen-class)
  (:require
   [clojure.tools.logging :as log]
   [gitbok.analytics.posthog :as posthog]
   [gitbok.handlers :as handlers]
   [gitbok.initialization :as initialization]
   [gitbok.metrics :as metrics]
   [gitbok.routes :as routes]
   [gitbok.scheduler :as scheduler]
   [gitbok.state :as state]
   [org.httpkit.server :as http-kit]))

(set! *warn-on-reflection* true)

(defn start!
  "Start the server with optional config overrides"
  ([] (start! {}))
  ([config-overrides]
   (let [startup-time (System/currentTimeMillis)
         context (state/init-state! config-overrides)
         port (state/get-config context :port)
         prefix (state/get-config context :prefix "")
         base-url (state/get-config context :base-url)
         version (state/get-config context :version)
         posthog-api-key (state/get-config context :posthog-api-key)
         posthog-host (state/get-config context :posthog-host)]

     ;; Initialize PostHog client once
     (when-let [client (posthog/create-client posthog-api-key posthog-host)]
       (state/set-runtime! context :posthog-client client))

     ;; Initialize metrics
     (metrics/initialize-metrics!)

     ;; Initialize all products and their indices
     (initialization/init-all-products!
      context
      :read-markdown-fn handlers/read-markdown-file)

;; Create and start HTTP server
     (let [;; Create initial router and save to state
           initial-router (routes/create-app context)
           _ (state/set-cache! context :app-router initial-router)
           ;; Save router rebuild callback for use during reload
           _ (state/set-cache! context :rebuild-router-fn
                               (fn [] (routes/create-app context)))
           ;; Create dynamic handler that reads router from state
           handler (routes/create-dynamic-handler context)
           server-instance (http-kit/run-server handler {:port port
                                                         :thread 8})]
       (state/set-server! context server-instance)
       ;; Mark app as initialized for healthcheck
       (state/set-cache! context :app-initialized true)
       (log/info "Server started" {:port port
                                   :prefix prefix
                                   :base-url base-url
                                   :version version}))

     ;; Start schedulers with context
     (when-let [_reload-schedule (scheduler/start-reload-watcher! context)]
       (log/info "Reload watcher started"))

     (when-let [_examples-schedule (scheduler/start-examples-updater! context)]
       (log/info "Examples updater started"))

     (let [total-duration (- (System/currentTimeMillis) startup-time)]
       (log/info "🚀🚀🚀 STARTUP COMPLETE 🚀🚀🚀" {:TOTAL-DURATION-MS total-duration
                                                   :TOTAL-DURATION-SECONDS (/ total-duration 1000.0)
                                                   :port port}))

     {:status :started
      :port port
      :context context})))

(defn stop!
  "Stop the server and all schedulers"
  [context]
  ;; Stop schedulers
  (scheduler/stop-all-schedulers! context)

  ;; Stop HTTP server
  (when-let [stop-fn (state/get-server context)]
    (try
      ;; Call stop with timeout to force shutdown
      (stop-fn :timeout 100)
      (catch Exception e
        (log/warn e "Error stopping server")))
    (state/clear-server! context)
    (log/info "Server stopped"))

  ;; Shutdown PostHog client
  (posthog/shutdown! context)

  {:status :stopped})

(defn -main [& _args]
  (log/info "Starting server")
  (let [{:keys [context]} (start!)]
    ;; Setup shutdown hook
    (.addShutdownHook (Runtime/getRuntime)
                      (Thread. (fn []
                                 (log/info "shutdown" {:msg "Got SIGTERM."})
                                 (stop! context))))))

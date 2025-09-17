(ns user
  (:require
   [gitbok.core :as core]
   [gitbok.state :as state]
   [gitbok.scheduler :as scheduler]
   [gitbok.handlers :as handlers]
   [gitbok.initialization :as initialization]
   [clj-reload.core :as reload]
   [clojure.tools.logging :as log]))

(defonce ^:private dev-context (atom nil))

 ;; Store the actual server stop function separately to survive reloads
(defonce ^:private server-stop-fn (atom nil))

(defn start!
  "Start the server"
  []
  (reload/init {:dirs ["src", "dev"]})
  (let [result (core/start!)]
    (reset! dev-context (:context result))
    ;; Store the stop function separately so it survives reloads
    (when-let [stop-fn (state/get-server (:context result))]
      (reset! server-stop-fn stop-fn))
    (log/info "Server started")
    :started))

(defn stop!
  "Stop the server"
  []
  ;; Try to use the stored stop function first
  (when-let [stop-fn @server-stop-fn]
    (try
      (log/info "Stopping server with stored stop function...")
      (stop-fn)
      (reset! server-stop-fn nil)
      (catch Exception e
        (log/warn e "Error with stored stop function"))))

  ;; Also try the normal stop through context
  (when-let [ctx @dev-context]
    (try
      (log/info "Stopping through context...")
      (core/stop! ctx)
      (catch Exception e
        (log/warn e "Error stopping through context")))
    (reset! dev-context nil))

  :stopped)

(defn restart!
  "Restart the server with proper cleanup"
  []
  ;; Stop the server
  (stop!)

  ;; Give it time to clean up
  (Thread/sleep 500)

  ;; Reload the code
  (log/info "Reloading code...")
  (let [reload-result (reload/reload)]
    (log/info "Reloaded namespaces:" (:loaded reload-result)))

  ;; Start the server with a fresh context
  (log/info "Starting server...")
  (start!)

  :restarted)

(defn restart-server!
  "Restart just the server without reloading code - useful for debugging"
  []
  ;; Stop the server
  (stop!)

  ;; Small delay to ensure port is released
  (Thread/sleep 500)

  ;; Start the server
  (log/info "Starting server...")
  (start!)

  :server-restarted)

(defn reload!
  "Reload code without restarting server"
  []
  (let [result (reload/reload)]
    (if (seq (:loaded result))
      (do
        (log/info "Reloaded namespaces:" (:loaded result))
        {:status :reloaded
         :namespaces (:loaded result)})
      (do
        (log/info "No namespaces needed reloading")
        {:status :no-changes}))))

(defn status1
  "Get server status"
  [context]
  {:server (if (state/get-server context) :running :stopped)
   :schedulers (scheduler/scheduler-status context)
   :config (state/get-config context :prefix "")
   :products (count (state/get-products context))})

(defn status
  "Get server status"
  []
  (if-let [ctx @dev-context]
    (status1 ctx)
    :not-started))

(defn clear-all-caches
  "Clear all product caches from state - for development use only"
  [context]
  ;; Clear all product indices using context
  (state/set-state! context [:products :indices] {})
  (log/info "All caches cleared"))

(defn clear-caches!
  "Clear all caches (for development)"
  []
  (when-let [ctx @dev-context]
    (clear-all-caches ctx)
    (state/set-cache! ctx :lastmod {})
    (state/set-cache! ctx :reload-state {:git-head nil
                                         :last-reload-time nil
                                         :app-version (state/get-config ctx :version)
                                         :in-progress false})
    (log/info "All caches cleared")))

(defn reload-products!
  "Reload products configuration (for development)"
  []
  (when-let [ctx @dev-context]
    (initialization/init-all-products! ctx :read-markdown-fn handlers/read-markdown-file)
    (log/info "Products reloaded")))

(defn state
  "Get current application state"
  []
  (if-let [ctx @dev-context]
    (state/get-full-state ctx)
    {}))

(comment
  ;; Start server with hot-reload support via Var references
  (start!)

  ;; Stop server
  (stop!)

  ;; Full restart - reloads code and restarts server (use when reload doesn't work)
  (restart!)

  ;; Restart just the server without reloading code (useful for debugging)
  (restart-server!)

  ;; Hot reload - reloads code, handlers are automatically updated via Var references
  ;; Use this for handler logic changes
  (reload!)

  ;; Check server status
  (status)

  ;; Get application state
  (state)

  ;; Start with custom config
  (core/start! {:port 3000
                :prefix "/docs"
                :dev-mode true})

  ;; Clear all caches (for development)
  (clear-caches!)

  ;; Reload products configuration
  (reload-products!))

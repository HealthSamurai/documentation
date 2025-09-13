(ns user
  (:require
   [gitbok.core :as core]
   [gitbok.state :as state]
   [gitbok.scheduler :as scheduler]
   [clj-reload.core :as reload]
   [clojure.tools.logging :as log]))

(defonce ^:private dev-context (atom nil))

(defn start!
  "Start the server"
  []
  (reload/init {:dirs ["src"]})
  (let [result (core/start!)]
    (reset! dev-context (:context result))
    (log/info "Server started")
    :started))

(defn stop!
  "Stop the server"
  []
  (when-let [ctx @dev-context]
    (log/info "Server stopped")
    (core/stop! ctx))
  :stopped)

(defn restart!
  "Restart the server"
  []
  (stop!)
  (reload/reload)
  (start!)
  :restarted)

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
    (core/init-products! ctx)
    (core/init-all-product-indices! ctx)
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

  ;; Full restart - only needed for structural changes (new routes, middleware changes)
  (restart!)

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
  (reload-products!)
  )

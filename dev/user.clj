(ns user
  (:require
   [gitbok.core :as core]
   [gitbok.state :as state]
   [gitbok.indexing.core :as indexing]
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
    (core/stop! ctx))
  (log/info "Server stopped")
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
  (reload/reload)
  :reloaded)

(defn status
  "Get server status"
  []
  (if-let [ctx @dev-context]
    (core/status ctx)
    :not-started))

(defn clear-caches!
  "Clear all caches (for development)"
  []
  (when-let [ctx @dev-context]
    (indexing/clear-all-caches ctx)
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
  ;; Start server
  (start!)
  
  ;; Stop server
  (stop!)
  
  ;; Restart server (stop, reload code, start)
  (restart!)
  
  ;; Just reload code without restarting
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
  (when-let [ctx @dev-context]
    (core/clear-caches! ctx))
  
  ;; Reload products configuration
  (when-let [ctx @dev-context]
    (core/reload-products! ctx))
  )
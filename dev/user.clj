(ns user
  (:require
   [gitbok.core :as core]
   [gitbok.state :as state]
   [clj-reload.core :as reload]
   [clojure.tools.logging :as log]))

(defn start!
  "Start the server"
  []
  (reload/init {:dirs ["src"]})
  (core/start!)
  (log/info "Server started")
  :started)

(defn stop!
  "Stop the server"
  []
  (core/stop!)
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
  (core/status))

(defn state
  "Get current application state"
  []
  (state/get-full-state))

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
  (core/clear-caches!)
  
  ;; Reload products configuration
  (core/reload-products!)
  )
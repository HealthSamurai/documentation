(ns user
  (:require
   [gitbok.core :as core]
   [clj-reload.core :as reload]
   [clojure.tools.logging :as log]))

;; Initialize clj-reload once at namespace load
(reload/init
  {:dirs ["src" "dev"]
   :no-reload '#{user}})

;; Keep server context between reloads
(defonce ^:private server-context (atom nil))

(defn start
  "Start the server"
  []
  (let [result (core/start! {:dev-mode true})]
    (reset! server-context (:context result))
    (log/info "Server started")
    :started))

(defn stop
  "Stop the server"
  []
  (when-let [ctx @server-context]
    (try
      (core/stop! ctx)
      (log/info "Server stopped")
      (catch Exception e
        (log/error e "Error stopping server")))
    (reset! server-context nil))
  :stopped)

(defn restart
  "Restart the server with code reload"
  []
  (stop)
  (Thread/sleep 500)
  (log/info "Reloading code...")
  (let [reload-result (reload/reload)]
    (log/info "Reloaded:" (:loaded reload-result))
    (when (empty? (:loaded reload-result))
      (log/warn "No namespaces were reloaded. Files may not have changed on disk.")))
  (start)
  :restarted)

(comment
  ;; Start server
  (start)

  ;; Stop server
  (stop)

  ;; Restart with code reload
  (restart)

  )

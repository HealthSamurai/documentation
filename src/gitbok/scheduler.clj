(ns gitbok.scheduler
  (:require [chime.core :as chime]
            [gitbok.state :as state]
            [gitbok.reload :as reload]
            [gitbok.examples.updater :as examples-updater]
            [clojure.tools.logging :as log])
  (:import [java.time Instant Duration]))

(defn start-reload-watcher! 
  "Start periodic reload watcher using chime"
  [init-product-indices-fn init-products-fn]
  (when (state/get-env :docs-volume-path)
    (let [interval-seconds (Integer/parseInt (state/get-env :reload-check-interval "30"))]
      (log/info "Starting reload watcher" {:interval-seconds interval-seconds
                                           :volume-path (state/get-env :docs-volume-path)})
      
      ;; Initialize reload state
      (reload/init-reload-state!)
      
      ;; Start periodic check
      (let [schedule (chime/chime-at
                      (chime/periodic-seq (Instant/now) (Duration/ofSeconds interval-seconds))
                      (fn [time]
                        (when-not (state/get-cache :reload-state-in-progress false)
                          (try
                            (state/set-cache! :reload-state-in-progress true)
                            (reload/check-and-reload! init-product-indices-fn init-products-fn)
                            (catch Exception e
                              (log/error e "Reload check failed"))
                            (finally
                              (state/set-cache! :reload-state-in-progress false)))))
                      {:error-handler (fn [e]
                                       (log/error e "Reload watcher error")
                                       true)})] ; continue after error
        (state/set-scheduler! :reload schedule)
        (log/info "Reload watcher started")
        schedule))))

(defn start-examples-updater! 
  "Start periodic examples updater using chime"
  []
  (when (state/get-env :github-token)
    (let [interval-minutes (Integer/parseInt (state/get-env :examples-update-interval "60"))]
      (log/info "Starting examples updater" {:interval-minutes interval-minutes})
      
      ;; Do initial update immediately
      (try
        (examples-updater/update-examples-from-artifact)
        (catch Exception e
          (log/error e "Initial examples update failed")))
      
      ;; Start periodic updates
      (let [schedule (chime/chime-at
                      (chime/periodic-seq 
                        (.plus (Instant/now) (Duration/ofMinutes interval-minutes))
                        (Duration/ofMinutes interval-minutes))
                      (fn [time]
                        (try
                          (examples-updater/update-examples-from-artifact)
                          (catch Exception e
                            (log/error e "Scheduled examples update failed"))))
                      {:error-handler (fn [e]
                                       (log/error e "Examples updater error")
                                       true)})]
        (state/set-scheduler! :examples schedule)
        (log/info "Examples updater started")
        schedule))))

(defn stop-scheduler! 
  "Stop a scheduler by key"
  [scheduler-key]
  (when-let [schedule (state/get-scheduler scheduler-key)]
    (log/info "Stopping scheduler" {:scheduler scheduler-key})
    (.close schedule)  ;; chime-at returns a java.io.Closeable
    (state/remove-scheduler! scheduler-key)
    (log/info "Scheduler stopped" {:scheduler scheduler-key})))

(defn stop-all-schedulers! 
  "Stop all running schedulers"
  []
  (log/info "Stopping all schedulers")
  (doseq [scheduler-key [:reload :examples]]
    (stop-scheduler! scheduler-key))
  (log/info "All schedulers stopped"))

(defn scheduler-status
  "Get status of all schedulers"
  []
  {:reload (if (state/get-scheduler :reload) :running :stopped)
   :examples (if (state/get-scheduler :examples) :running :stopped)})
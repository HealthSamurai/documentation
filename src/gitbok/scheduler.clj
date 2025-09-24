(ns gitbok.scheduler
  (:require [chime.core :as chime]
            [gitbok.state :as state]
            [gitbok.reload :as reload]
            [gitbok.handlers :as handlers]
            [gitbok.examples.updater :as examples-updater]
            [clojure.tools.logging :as log]
            [clojure.stacktrace])
  (:import [java.time Instant Duration]))

(defn start-reload-watcher!
  "Start periodic reload watcher using chime"
  [context]
  (when (state/get-config context :docs-volume-path)
    (let [interval-seconds (Integer/parseInt (state/get-config context :reload-check-interval "30"))
          ;; Add random jitter 0-45 seconds to spread reloads between replicas
          ;; This helps prevent all replicas from reloading simultaneously
          jitter-seconds (rand-int 45)
          first-check (-> (Instant/now)
                          (.plusSeconds jitter-seconds))]
      (log/info "Starting reload watcher" {:interval-seconds interval-seconds
                                           :volume-path (state/get-config context :docs-volume-path)
                                           :jitter-seconds jitter-seconds
                                           :first-check-in (str jitter-seconds "s")})

      ;; Initialize reload state
      (reload/init-reload-state! context)

      ;; Start periodic check with jittered start time
      (let [schedule (chime/chime-at
                      (chime/periodic-seq first-check (Duration/ofSeconds interval-seconds))
                      (fn [_time]
                        (when-not (state/get-cache context :reload-state-in-progress false)
                          (try
                            (state/set-cache! context :reload-state-in-progress true)
                            (reload/check-and-reload! context :read-markdown-fn handlers/read-markdown-file)
                            (catch Exception e
                              (let [stack-trace (with-out-str (clojure.stacktrace/print-stack-trace e))]
                                (log/error e "Reload check failed"
                                           {:error (.getMessage e)
                                            :stack-trace stack-trace})))
                            (finally
                              (state/set-cache! context :reload-state-in-progress false)))))
                      {:error-handler (fn [e]
                                        (let [stack-trace (with-out-str (clojure.stacktrace/print-stack-trace e))]
                                          (log/error e "Reload watcher error"
                                                     {:stack-trace stack-trace}))
                                        true)})] ; continue after error
        (state/set-scheduler! context :reload schedule)
        (log/info "Reload watcher started")
        schedule))))

(defn start-examples-updater!
  "Start periodic examples updater using chime"
  [context]
  (when (state/get-config context :github-token)
    (let [interval-minutes (Integer/parseInt (state/get-config context :examples-update-interval "60"))]
      (log/info "Starting examples updater" {:interval-minutes interval-minutes})

      ;; Do initial update immediately
      (try
        (examples-updater/update-examples-from-artifact context)
        (catch Exception e
          (let [stack-trace (with-out-str (clojure.stacktrace/print-stack-trace e))]
            (log/error e "Initial examples update failed"
                       {:error (.getMessage e)
                        :stack-trace stack-trace}))))

      ;; Start periodic updates
      (let [schedule (chime/chime-at
                      (chime/periodic-seq
                       (.plus (Instant/now) (Duration/ofMinutes interval-minutes))
                       (Duration/ofMinutes interval-minutes))
                      (fn [_time]
                        (try
                          (examples-updater/update-examples-from-artifact context)
                          (catch Exception e
                            (let [stack-trace (with-out-str (clojure.stacktrace/print-stack-trace e))]
                              (log/error e "Scheduled examples update failed"
                                         {:error (.getMessage e)
                                          :stack-trace stack-trace})))))
                      {:error-handler (fn [e]
                                        (let [stack-trace (with-out-str (clojure.stacktrace/print-stack-trace e))]
                                          (log/error e "Examples updater error"
                                                     {:stack-trace stack-trace}))
                                        true)})]
        (state/set-scheduler! context :examples schedule)
        (log/info "Examples updater started")
        schedule))))

(defn stop-scheduler!
  "Stop a scheduler by key"
  [context scheduler-key]
  (when-let [schedule (state/get-scheduler context scheduler-key)]
    (log/info "Stopping scheduler" {:scheduler scheduler-key})
    (.close schedule) ;; chime-at returns a java.io.Closeable
    (state/remove-scheduler! context scheduler-key)
    (log/info "Scheduler stopped" {:scheduler scheduler-key})))

(defn stop-all-schedulers!
  "Stop all running schedulers"
  [context]
  (log/info "Stopping all schedulers")
  (doseq [scheduler-key [:reload :examples]]
    (stop-scheduler! context scheduler-key))
  (log/info "All schedulers stopped"))

(defn scheduler-status
  "Get status of all schedulers"
  [context]
  {:reload (if (state/get-scheduler context :reload) :running :stopped)
   :examples (if (state/get-scheduler context :examples) :running :stopped)})
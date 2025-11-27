(ns gitbok.reload
  "Git HEAD monitoring and automatic reload functionality"
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [clojure.tools.logging :as log]
   [clojure.java.shell :as shell]
   [clojure.stacktrace]
   [gitbok.blog.core :as blog]
   [gitbok.initialization :as initialization]
   [gitbok.state :as state]))

(defn get-volume-path
  "Get current volume path, with fallback to local docs/ directory for development"
  [context]
  (or (state/get-config context :docs-volume-path)
      (when (.exists (io/file "docs"))
        (log/warn "using local docs" {:path "docs"})
        "docs")
      (when (.exists (io/file "../docs"))
        (log/warn "using local docs" {:path "../docs"})
        "../docs")))

;; State management functions
(defn get-reload-state [context]
  (state/get-cache context :reload-state
                   {:git-head nil
                    :last-reload-time nil
                    :app-version nil
                    :in-progress false}))

(defn set-reload-state! [context reload-state]
  (state/set-cache! context :reload-state reload-state))

(defn update-reload-state! [context f & args]
  (state/update-cache! context :reload-state
                       (fn [state]
                         (apply f (or state {}) args))))

(defn is-reloading? [context]
  (:in-progress (get-reload-state context)))

(defn set-reloading! [context value]
  (update-reload-state! context assoc :in-progress value))

(defn rebuild-all-caches
  "Rebuild all caches by re-initializing all products.
   This is now a simple wrapper around the unified initialization."
  [context & {:keys [read-markdown-fn]}]
  (log/info "🔄 Rebuild start" {:action "re-initializing-products"})
  (let [start-time (System/currentTimeMillis)
        products-config (initialization/init-all-products! context :read-markdown-fn read-markdown-fn)
        _ (log/info "Reloading blog cache...")
        _ (blog/reload-blog-cache! context)
        duration (- (System/currentTimeMillis) start-time)]
    (log/info "Rebuild complete" {:product-count (count products-config)
                                  :duration-ms duration})
    true))

(defn check-and-reload!
  "Check if git HEAD changed and reload if needed.
   This ensures only one reload happens at a time."
  [context & {:keys [read-markdown-fn]}]
  (when (and (get-volume-path context)
             (not (is-reloading? context)))
    (let [;; Get current git HEAD
          repo-path (state/get-config context :docs-repo-path ".")
          new-head (try
                     (let [{:keys [out exit]} (shell/sh "git"
                                                        "-c" (str "safe.directory=" repo-path)
                                                        "rev-parse" "HEAD"
                                                        :dir repo-path)]
                       (when (zero? exit)
                         (str/trim out)))
                     (catch Exception e
                       (let [stack-trace (with-out-str (clojure.stacktrace/print-stack-trace e))]
                         (log/warn "⚠️get head failed" {:error (.getMessage e)
                                                        :stack-trace stack-trace}))
                       nil))
          reload-state (get-reload-state context)
          current-head (:git-head reload-state)
          app-version (state/get-config context :version)]

      (log/info "check status" {:🏷️app-version app-version
                                :📌current-head (or current-head "none")
                                :🆕new-head (or new-head "none")})

      (cond
        (not new-head)
        (log/warn "check skipped" {:reason "cannot-get-git-head"})

        (and new-head (not= new-head current-head))
        (do
          (log/info "📚repo changed" {:old-head (or current-head "none")
                                      :new-head new-head})

          (set-reloading! context true)
          (try
            (let [start-time (System/currentTimeMillis)]
              (log/info "🚀reload start" {:action "starting-reload"})

              ;; Use unified initialization (throws on error)
              (rebuild-all-caches context :read-markdown-fn read-markdown-fn)

              ;; Rebuild router with updated products using callback (throws on error)
              (when-let [rebuild-fn (state/get-cache context :rebuild-router-fn)]
                (log/info "🔄 Rebuilding router" {:action "router-rebuild-start"})
                (let [router-start-time (System/currentTimeMillis)
                      new-router (rebuild-fn)]
                  (state/set-cache! context :app-router new-router)
                  (let [duration (- (System/currentTimeMillis) router-start-time)
                        products (state/get-products context)]
                    (log/info "✅ Router rebuilt" {:duration-ms duration
                                                  :product-count (count products)
                                                  :products (mapv :id products)}))))

              ;; Update state ONLY after ALL operations succeeded
              (update-reload-state! context assoc
                                    :git-head new-head
                                    :last-reload-time (java.util.Date.)
                                    :app-version app-version)

              (let [duration (- (System/currentTimeMillis) start-time)]
                (log/info "reload success" {:duration-ms duration
                                            :new-head new-head})
                (log/info "🎯🎯🎯 RELOAD COMPLETE 🎯🎯🎯" {:TOTAL-DURATION-MS duration
                                                           :TOTAL-DURATION-SECONDS (/ duration 1000.0)})))
            (catch Exception e
              (let [stack-trace (with-out-str (clojure.stacktrace/print-stack-trace e))]
                (log/error e "❌ Reload failed - will retry on next check" {:error (.getMessage e)
                                                                           :exception-class (.getName (.getClass e))
                                                                           :stack-trace stack-trace})))
            (finally
              (set-reloading! context false))))

        :else
        (log/info "📚docs unchanged")))))

(defn init-reload-state!
  "Initialize reload state at startup"
  [context]
  (when-let [docs-path (get-volume-path context)]
    (let [app-version (state/get-config context :version)
          repo-path (state/get-config context :docs-repo-path ".")]

      (log/info "reload state init" {:app-version app-version
                                     :volume-path docs-path
                                     :repo-path repo-path})

      ;; Get initial git HEAD
      (let [initial-head (try
                           (let [{:keys [out exit]} (shell/sh "git"
                                                              "-c" (str "safe.directory=" repo-path)
                                                              "rev-parse" "HEAD"
                                                              :dir repo-path)]
                             (when (zero? exit)
                               (str/trim out)))
                           (catch Exception e
                             (let [stack-trace (with-out-str (clojure.stacktrace/print-stack-trace e))]
                               (log/warn "⚠️get initial head failed" {:error (.getMessage e)
                                                                      :stack-trace stack-trace}))
                             nil))]
        (set-reload-state! context
                           {:git-head initial-head
                            :last-reload-time (java.util.Date.)
                            :app-version app-version
                            :in-progress false})
        (log/info "initial git head" {:head initial-head
                                      :path repo-path})))))

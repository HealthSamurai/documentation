(ns gitbok.reload
  (:require
   [clojure.java.io :as io]
   [clojure.java.shell :as shell]
   [clojure.string :as str]
   [gitbok.constants :as const]
   [gitbok.http]
   [gitbok.products :as products]
   [gitbok.indexing.core :as indexing]
   [system]
   [klog.core :as log]))

(def volume-path (System/getenv "DOCS_VOLUME_PATH"))

(defn get-volume-path
  "Get current volume path, with fallback to local docs/ directory for development"
  []
  (or volume-path
      (when (.exists (io/file "docs"))
        (log/info ::using-local-docs {:path "docs"})
        "docs")
      (when (.exists (io/file "../docs"))
        (log/info ::using-local-docs {:path "../docs"})
        "../docs")))

;; Configuration from environment
(def reload-check-interval-ms
  (or (some-> (System/getenv "RELOAD_CHECK_INTERVAL_SEC")
              Integer/parseInt
              (* 1000))
      60000)) ;; Default: check every 60 seconds

;; Removed checksum functions - now using git HEAD for change detection

;; State management functions using context
(defn get-reload-state [context]
  (system/get-system-state context [const/RELOAD_STATE]
                           {:git-head nil
                            :last-reload-time nil
                            :app-version nil
                            :in-progress false}))

(defn set-reload-state [context state]
  (system/set-system-state context [const/RELOAD_STATE] state))

(defn update-reload-state [context f & args]
  (system/update-system-state context [const/RELOAD_STATE]
                              (fn [state]
                                (apply f (or state {}) args))))

;; Removed checksum getters/setters - using git HEAD instead

(defn is-reloading? [context]
  (:in-progress (get-reload-state context)))

(defn set-reloading [context value]
  (update-reload-state context assoc :in-progress value))

(defn rebuild-all-caches
  "Build complete new cache for all products.
   This reuses existing initialization logic."
  [context init-product-indices-fn init-products-fn]
  (log/info ::📦rebuild-start {:action "re-initializing-products"})
  ;; Simply re-initialize all products
  ;; This will reload products.yaml and all documentation
  (let [products-config (init-products-fn context)
        product-count (count products-config)]
    (log/info ::📚products-found {:count product-count})
    ;; Rebuild cache for each product
    (doseq [[idx product] (map-indexed vector products-config)]
      (let [product-name (:name product)
            product-id (:id product)]
        (log/info ::rebuilding-product {:product-id product-id
                                        :product-name product-name
                                        :index (inc idx)
                                        :total product-count})
        (let [start-time (System/currentTimeMillis)]
          ;; This will:
          ;; 1. Read SUMMARY.md
          ;; 2. Build URI mappings
          ;; 3. Load all markdown files
          ;; 4. Parse markdown
          ;; 5. Build search index
          ;; 6. Pre-render pages
          ;; 7. Generate sitemap
          (init-product-indices-fn context product)
          (let [duration (- (System/currentTimeMillis) start-time)]
            (log/info ::product-rebuilt {:product-id product-id
                                         :product-name product-name
                                         :duration-ms duration})))))
    (log/info ::rebuild-complete {:product-count product-count})
    true))

(defn check-and-reload!
  "Check if git HEAD changed and reload if needed.
   This ensures only one reload happens at a time."
  [context init-product-indices-fn init-products-fn]
  (when (and (get-volume-path)
             (not (is-reloading? context)))
    (let [;; Get current git HEAD
          repo-path (or (System/getenv "DOCS_REPO_PATH") ".")
          new-head (try
                     (let [{:keys [out exit]} (shell/sh "git" 
                                                        "-c" (str "safe.directory=" repo-path)
                                                        "rev-parse" "HEAD" 
                                                        :dir repo-path)]
                       (when (zero? exit)
                         (str/trim out)))
                     (catch Exception e
                       (log/warn ::⚠️get-head-failed {:error (.getMessage e)})
                       nil))
          reload-state (get-reload-state context)
          current-head (:git-head reload-state)
          app-version (gitbok.http/get-version context)]

      (log/info ::check-status {:🏷️app-version app-version
                                :📌current-head (or current-head "none")
                                :🆕new-head (or new-head "none")})

      (cond
        (not new-head)
        (log/warn ::check-skipped {:reason "cannot-get-git-head"})

        (and new-head (not= new-head current-head))
        (do
          (log/info ::📊repo-changed {:old-head (or current-head "none")
                                      :new-head new-head})

          (set-reloading context true)
          (try
            (let [start-time (System/currentTimeMillis)]
              (log/info ::🚀reload-start {:action "starting-reload"})

              ;; Build new caches
              (rebuild-all-caches context init-product-indices-fn init-products-fn)
              
              ;; Update lastmod for all products
              (log/info ::🔄updating-lastmod-data {})
              (doseq [product (products/get-products-config context)]
                (let [ctx (assoc context ::products/current-product product)]
                  (indexing/set-lastmod ctx)))

              ;; Update state only after successful reload
              (update-reload-state context assoc
                                   :git-head new-head
                                   :last-reload-time (java.util.Date.)
                                   :app-version app-version)

              (let [duration (- (System/currentTimeMillis) start-time)]
                (log/info ::reload-success {:duration-ms duration
                                            :new-head new-head})))
            (catch Exception e
              (log/error ::reload-failed {:error (.getMessage e)
                                          :exception e}))
            (finally
              (set-reloading context false))))

        :else
        (log/info ::📚docs-unchanged {})))))
;; Removed separate lastmod updater - now integrated into check-and-reload!

(defn start-reload-watcher
  "Start background thread that checks for git changes"
  [context init-product-indices-fn init-products-fn]
  (when-let [docs-path (get-volume-path)]
    (let [app-version (gitbok.http/get-version context)
          repo-path (or (System/getenv "DOCS_REPO_PATH") ".")]

      (log/info ::watcher-start {:app-version app-version
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
                             (log/warn ::⚠️get-initial-head-failed {:error (.getMessage e)})
                             nil))]
        (set-reload-state context
                          {:git-head initial-head
                           :last-reload-time (java.util.Date.)
                           :app-version app-version
                           :in-progress false})
        (log/info ::initial-git-head {:head initial-head
                                      :path repo-path}))

      (log/info ::check-interval {:interval-seconds (/ reload-check-interval-ms 1000)}))

    ;; Start background thread
    (future
      (loop []
        (try
          (Thread/sleep reload-check-interval-ms)
          (check-and-reload! context init-product-indices-fn init-products-fn)
          (catch Exception e
            (log/error ::watcher-error {:error (.getMessage e)})))
        (recur)))

    (log/info ::watcher-started {:status "success"})
    :started))

;; REPL testing helpers
(comment

  (defn set-volume-path!
    "Set the volume path for development/testing.
    Use this in REPL to point to your local docs directory."
    [path]
    (alter-var-root #'volume-path (constantly path))
    (log/info ::volume-path-set {:path path})
    path)

  ;; Set volume path for local development
  (set-volume-path! "docs")
  (set-volume-path! "../docs")

  ;; Check current checksum
  (calculate-docs-checksum)

  ;; Manually trigger reload check
  ;; You'll need context from your running system
  ;; (check-and-reload! context init-product-indices-fn init-products-fn)

  ;; Test that changes are detected
  ;; 1. Calculate initial checksum
  (def initial-checksum (calculate-docs-checksum))
  ;; 2. Make changes to docs/overview/faq.md or any .md file
  ;; 3. Check new checksum
  (def new-checksum (calculate-docs-checksum))
  ;; 4. Compare
  (= initial-checksum new-checksum) ;; Should be false after changes
  )

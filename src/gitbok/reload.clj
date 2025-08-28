(ns gitbok.reload
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [gitbok.constants :as const]
   [gitbok.http]
   [system]
   [klog.core :as log]))

(def volume-path (System/getenv "DOCS_VOLUME_PATH"))

(defn get-volume-path
  "Get current volume path, with fallback to local docs/ directory for development"
  []
  (or volume-path
      (when (.exists (io/file "docs"))
        (do
          (log/info ::using-local-docs {:path "docs"})
          "docs"))
      (when (.exists (io/file "../docs"))
        (do
          (log/info ::using-local-docs {:path "../docs"})
          "../docs"))))

;; Configuration from environment
(def reload-check-interval-ms
  (or (some-> (System/getenv "RELOAD_CHECK_INTERVAL_SEC")
              Integer/parseInt
              (* 1000))
      60000)) ;; Default: check every 60 seconds

(defn calculate-file-content-hash
  "Calculate MD5 hash of file content"
  [^java.io.File file]
  (let [md (java.security.MessageDigest/getInstance "MD5")]
    (with-open [is (io/input-stream file)]
      (let [buffer (byte-array 8192)]
        (loop []
          (let [n (.read is buffer)]
            (when (pos? n)
              (.update md buffer 0 n)
              (recur))))))
    (apply str (map #(format "%02x" %) (.digest md)))))

(defn calculate-docs-checksum
  "Calculate checksum based on actual file contents using MD5.
   This ensures reload only happens when content actually changes,
   not just timestamps."
  []
  (log/info ::checksum-start {:action "🔍 calculating-checksum"})
  (when-let [docs-path (get-volume-path)]
    (try
      (let [docs-dir (io/file docs-path)
            ;; Get all relevant files
            files (filter #(let [name (.getName %)]
                             (or (.endsWith name ".md")
                                 (.endsWith name ".yaml")
                                 (.endsWith name ".yml")
                                 (.endsWith name ".edn")))
                          (file-seq docs-dir))
            file-count (count files)
            _ (log/info ::files-found {:count file-count :path docs-path})
            ;; Sort files by path for consistent ordering
            sorted-files (sort-by #(.getPath %) files)
            ;; Calculate MD5 for each file and combine
            start-time (System/currentTimeMillis)
            file-hashes (map (fn [f]
                               (str (.getPath f) ":" (calculate-file-content-hash f)))
                             sorted-files)
            calc-time (- (System/currentTimeMillis) start-time)]
        ;; Return combined hash of all file hashes
        (log/info ::✅checksum-complete {:duration-ms calc-time
                                         :file-count file-count})
        (str (hash (vec file-hashes))))
      (catch Exception e
        (log/error ::❌checksum-error {:error (.getMessage e)})
        nil))))

;; State management functions using context
(defn get-reload-state [context]
  (system/get-system-state context [const/RELOAD_STATE]
                           {:checksum nil
                            :last-update-time nil
                            :last-reload-time nil
                            :app-version nil
                            :in-progress false}))

(defn set-reload-state [context state]
  (system/set-system-state context [const/RELOAD_STATE] state))

(defn update-reload-state [context f & args]
  (system/update-system-state context [const/RELOAD_STATE]
                              (fn [state]
                                (apply f (or state {}) args))))

(defn get-current-checksum [context]
  (:checksum (get-reload-state context)))

(defn set-current-checksum [context checksum]
  (update-reload-state context assoc :checksum checksum))

(defn get-last-update-time
  "Get last modification time of docs directory"
  []
  (when-let [docs-path (get-volume-path)]
    (try
      (java.util.Date. (.lastModified (io/file docs-path)))
      (catch Exception e nil))))

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
  "Check if documentation changed and reload if needed.
   Uses checksum-based comparison to detect changes.
   This ensures only one reload happens at a time."
  [context init-product-indices-fn init-products-fn]
  (when (and (get-volume-path)
             (not (is-reloading? context)))
    (let [;; Calculate current docs checksum
          new-checksum (calculate-docs-checksum)
          reload-state (get-reload-state context)
          current-checksum (:checksum reload-state)
          app-version (gitbok.http/get-version context)
          update-time (get-last-update-time)]

      (log/info ::check-status {:🏷️app-version app-version
                                :🔖cached-checksum (or current-checksum "none")
                                :📅cached-at (:last-reload-time reload-state)
                                :🔍current-checksum new-checksum})

      (cond
        (not new-checksum)
        (log/warn ::check-skipped {:reason "cannot-calculate-checksum"})

        (and new-checksum (not= new-checksum current-checksum))
        (do
          (log/info ::📚docs-changed {:old-checksum (or current-checksum "none")
                                      :new-checksum new-checksum})

          (set-reloading context true)
          (try
            (let [start-time (System/currentTimeMillis)]
              (log/info ::🚀reload-start {:action "starting-reload"})

              ;; Build new caches
              (rebuild-all-caches context init-product-indices-fn init-products-fn)

              ;; Update state only after successful reload
              (update-reload-state context assoc
                                   :checksum new-checksum
                                   :last-update-time update-time
                                   :last-reload-time (java.util.Date.)
                                   :app-version app-version)

              (let [duration (- (System/currentTimeMillis) start-time)]
                (log/info ::reload-success {:duration-ms duration
                                            :new-checksum new-checksum})))
            (catch Exception e
              (log/error ::reload-failed {:error (.getMessage e)
                                          :exception e}))
            (finally
              (set-reloading context false))))

        :else
        (log/info ::📚docs-unchanged {})))))

(defn start-reload-watcher
  "Start background thread that checks for documentation changes"
  [context init-product-indices-fn init-products-fn]
  (when-let [docs-path (get-volume-path)]
    (let [app-version (gitbok.http/get-version context)
          initial-update-time (get-last-update-time)]

      (log/info ::watcher-start {:app-version app-version
                                 :volume-path docs-path})

      ;; Always use checksum mode
      (let [initial-checksum (calculate-docs-checksum)]
        (set-reload-state context
                          {:checksum initial-checksum
                           :last-reload-time (java.util.Date.)
                           :app-version app-version
                           :in-progress false})
        (log/info ::initial-checksum {:checksum initial-checksum
                                      :path docs-path}))

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

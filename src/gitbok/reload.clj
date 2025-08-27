(ns gitbok.reload
  (:require
   [clojure.java.io :as io]
   [gitbok.products :as products]))

(def volume-path (System/getenv "DOCS_VOLUME_PATH"))

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
  (println "calculate docs checksum...")
  (when volume-path
    (try
      (let [docs-dir (io/file volume-path)
            ;; Get all relevant files
            files (filter #(let [name (.getName %)]
                            (or (.endsWith name ".md")
                                (.endsWith name ".yaml")
                                (.endsWith name ".yml")
                                (.endsWith name ".edn")))
                         (file-seq docs-dir))
            ;; Sort files by path for consistent ordering
            sorted-files (sort-by #(.getPath %) files)
            ;; Calculate MD5 for each file and combine
            file-hashes (map (fn [f]
                              (str (.getPath f) ":" (calculate-file-content-hash f)))
                            sorted-files)]
        ;; Return combined hash of all file hashes
        (str (hash (vec file-hashes))))
      (catch Exception e
        (println "Error calculating checksum:" (.getMessage e))
        nil))))

;; State management using a simple atom
(defonce reload-state (atom {:checksum nil
                              :in-progress false}))

(defn get-current-checksum []
  (:checksum @reload-state))

(defn set-current-checksum [checksum]
  (swap! reload-state assoc :checksum checksum))

(defn is-reloading? []
  (:in-progress @reload-state))

(defn set-reloading [value]
  (swap! reload-state assoc :in-progress value))

(defn rebuild-all-caches
  "Build complete new cache for all products.
   This reuses existing initialization logic."
  [context init-product-indices-fn init-products-fn]
  ;; Simply re-initialize all products
  ;; This will reload products.yaml and all documentation
  (let [products-config (init-products-fn context)]
    ;; Rebuild cache for each product
    (doseq [product products-config]
      (println "Rebuilding cache for product:" (:name product))
      ;; This will:
      ;; 1. Read SUMMARY.md
      ;; 2. Build URI mappings
      ;; 3. Load all markdown files
      ;; 4. Parse markdown
      ;; 5. Build search index
      ;; 6. Pre-render pages
      ;; 7. Generate sitemap
      (init-product-indices-fn context product))
    true))

(defn check-and-reload!
  "Check if documentation changed and reload if needed.
   This ensures only one reload happens at a time."
  [context init-product-indices-fn init-products-fn]
  (when (and volume-path
             (not (is-reloading?)))
    (let [new-checksum (calculate-docs-checksum)
          current-checksum (get-current-checksum)]
      (when (and new-checksum
                 (not= new-checksum current-checksum))
        (println "Documentation changed")
        (println "  Old checksum:" current-checksum)
        (println "  New checksum:" new-checksum)

        (set-reloading true)
        (try
          (let [start-time (System/currentTimeMillis)]
            (println "Starting documentation reload...")

            ;; Build new caches
            (rebuild-all-caches context init-product-indices-fn init-products-fn)

            ;; Update checksum only after successful reload
            (set-current-checksum new-checksum)

            (let [duration (- (System/currentTimeMillis) start-time)]
              (println (str "Documentation reloaded successfully in " duration "ms"))))
          (catch Exception e
            (println "ERROR: Documentation reload failed")
            (println "  Error:" (.getMessage e))
            (.printStackTrace e)
            ;; Keep old cache on error
            )
          (finally
            (set-reloading false)))))))

(defn start-reload-watcher
  "Start background thread that checks for documentation changes"
  [context init-product-indices-fn init-products-fn]
  (when volume-path
    (println "Starting documentation reload watcher")
    (println "  Volume path:" volume-path)
    (println "  Check interval:" reload-check-interval-ms "ms")

    ;; Set initial checksum
    (let [initial-checksum (calculate-docs-checksum)]
      (set-current-checksum initial-checksum)
      (println "  Initial checksum:" initial-checksum))

    ;; Start background thread
    (future
      (loop []
        (try
          (Thread/sleep reload-check-interval-ms)
          (check-and-reload! context init-product-indices-fn init-products-fn)
          (catch Exception e
            (println "Error in reload watcher:" (.getMessage e))))
        (recur)))

    (println "Reload watcher started successfully")))

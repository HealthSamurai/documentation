(ns gitbok.reload
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [gitbok.constants :as const]
   [gitbok.http]
   [system]
   [klog.core :as log]))

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
  (log/info ::checksum-start {:action "🔍 calculating-checksum"})
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
            file-count (count files)
            _ (log/info ::files-found {:count file-count})
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
                            :commit-hash nil
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

(defn get-git-commit-hash
  "Get current git commit hash from git-sync repository"
  []
  (when volume-path
    (try
      (let [git-head-file (io/file volume-path ".git/HEAD")]
        (when (.exists git-head-file)
          (let [head-content (slurp git-head-file)]
            (if (str/starts-with? head-content "ref:")
              ;; HEAD points to a branch reference
              (let [ref-path (str/trim (str/replace head-content "ref: " ""))
                    ref-file (io/file volume-path ".git" ref-path)]
                (when (.exists ref-file)
                  (subs (str/trim (slurp ref-file)) 0 8))) ;; Use short hash (8 chars)
              ;; HEAD contains direct commit hash
              (subs (str/trim head-content) 0 8)))))
      (catch Exception e
        (log/error ::❌git-commit-error {:error (.getMessage e)})
        nil))))

(defn get-last-update-time
  "Get last modification time of .git directory"
  []
  (when volume-path
    (try
      (let [git-dir (io/file volume-path ".git")]
        (when (.exists git-dir)
          (java.util.Date. (.lastModified git-dir))))
      (catch Exception e nil))))

(defn get-docs-identifier
  "Get identifier for current docs state - prefer git commit, fallback to checksum"
  []
  (or (get-git-commit-hash)
      (do
        (log/warn ::⚠️git-unavailable {:fallback "checksum"})
        (calculate-docs-checksum))))

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
   This ensures only one reload happens at a time."
  [context init-product-indices-fn init-products-fn]
  (when (and volume-path
             (not (is-reloading? context)))
    (let [;; Try git commit first, fallback to checksum
          new-commit (get-git-commit-hash)
          reload-state (get-reload-state context)
          current-commit (:commit-hash reload-state)
          app-version (gitbok.http/get-version context)
          update-time (get-last-update-time)
          ;; Use appropriate comparison based on what we have
          [new-identifier current-identifier identifier-type]
          (if new-commit
            [new-commit current-commit "commit"]
            [(calculate-docs-checksum) (get-current-checksum context) "checksum"])]

      ;; Enhanced logging with version and timestamp
      (log/info ::check-status {:🏷️app-version app-version
                                :🔖cached-commit (or current-commit
                                                     (:checksum reload-state)
                                                     "none")
                                :📅cached-at (:last-reload-time reload-state)
                                :🔍current-commit new-commit})

      (cond
        (not new-identifier)
        (log/warn ::check-skipped {:reason "cannot-determine-state"})

        (and new-identifier (not= new-identifier current-identifier))
        (do
          (log/info ::📚docs-changed {:identifier-type identifier-type
                                      :old-identifier (or current-identifier "none")
                                      :new-identifier new-identifier})

          (set-reloading context true)
          (try
            (let [start-time (System/currentTimeMillis)]
              (log/info ::🚀reload-start {:action "starting-reload"})

              ;; Build new caches
              (rebuild-all-caches context init-product-indices-fn init-products-fn)

              ;; Update state only after successful reload
              (update-reload-state context assoc
                                   (if (= identifier-type "commit")
                                     :commit-hash
                                     :checksum) new-identifier
                                   :last-update-time update-time
                                   :last-reload-time (java.util.Date.)
                                   :app-version app-version)

              (let [duration (- (System/currentTimeMillis) start-time)]
                (log/info ::reload-success {:duration-ms duration
                                            :🔖new-commit new-commit})))
            (catch Exception e
              (log/error ::reload-failed {:error (.getMessage e)
                                          :exception e}))
            (finally
              (set-reloading context false))))

        :else
        (log/debug ::docs-unchanged {})))))

(defn start-reload-watcher
  "Start background thread that checks for documentation changes"
  [context init-product-indices-fn init-products-fn]
  (when volume-path
    (let [app-version (gitbok.http/get-version context)
          initial-commit (get-git-commit-hash)
          initial-update-time (get-last-update-time)]

      (log/info ::watcher-start {:app-version app-version
                                 :volume-path volume-path})

      ;; Set initial state based on what's available
      (if initial-commit
        (do
          (log/info ::git-mode {:initial-commit initial-commit
                                :git-sync-updated initial-update-time})
          (set-reload-state context
                            {:commit-hash initial-commit
                             :last-update-time initial-update-time
                             :last-reload-time (java.util.Date.)
                             :app-version app-version
                             :in-progress false}))
        (do
          (log/warn ::checksum-mode {:reason "git-not-found"})
          (let [initial-checksum (calculate-docs-checksum)]
            (set-reload-state context
                              {:checksum initial-checksum
                               :last-reload-time (java.util.Date.)
                               :app-version app-version
                               :in-progress false})
            (log/info ::initial-checksum {:checksum initial-checksum}))))
      (log/info ::check-interval {:interval-seconds (/ reload-check-interval-ms 1000)})))

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
  :started)

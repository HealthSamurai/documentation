(ns gitbok.lastmod.generator
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [clojure.java.shell :as shell]
   [system]
   [klog.core :as log]))

(defn lastmod-for-file
  "Get git lastmod timestamp for a file"
  [file git-dir]
  (let [;; For symlinked files, we need to resolve to get the actual file
        canonical-file (.getCanonicalFile file)
        ;; But for git, we need the path relative to git-dir
        ;; Since git follows symlinks, we can use the original file path
        git-dir-path (.toPath (io/file git-dir))
        file-path (.toPath file)
        relative-path (try
                        (.relativize git-dir-path file-path)
                        (catch Exception _
                          ;; If can't relativize, use absolute path
                          file-path))
        {:keys [out exit err]} (shell/sh "git" 
                                         "-c" (str "safe.directory=" git-dir)
                                         "log" "-1" "--format=%ct"
                                         "--follow"  ; Important: follow renames/symlinks
                                         (str relative-path)
                                         :dir git-dir)]
    (when (and exit (zero? exit) (not (str/blank? out)))
      (try
        (let [timestamp (Long/parseLong (str/trim out))
              instant (java.time.Instant/ofEpochSecond timestamp)]
          (.toString instant))
        (catch Exception e
          (log/warn ::⚠️parse-timestamp-failed {:file (.getPath file)
                                              :error (.getMessage e)})
          nil)))))

(defn generate-lastmod-data
  "Generates lastmod data for MD files in directory"
  [docs-dir]
  (try
    (let [docs-file (io/file docs-dir)
          repo-path (or (System/getenv "DOCS_REPO_PATH") ".")]
      (log/info ::🔍checking-docs-dir {:dir docs-dir :exists (.exists docs-file) :git-dir repo-path})
      (if (.exists docs-file)
        (let [;; Use Files/walk with FOLLOW_LINKS to handle symlinks
              md-files (with-open [stream (java.nio.file.Files/walk 
                                            (.toPath docs-file)
                                            (into-array java.nio.file.FileVisitOption 
                                                       [java.nio.file.FileVisitOption/FOLLOW_LINKS]))]
                         (->> stream
                              .iterator
                              iterator-seq
                              (map #(.toFile %))
                              (filter #(and (.isFile %)
                                          (.endsWith (.getName %) ".md")))
                              doall))
              _ (log/debug ::📁found-md-files {:count (count md-files)})
              data (->> md-files
                       (map (fn [f]
                              (let [rel (.relativize (.toPath docs-file)
                                                    (.toPath f))
                                    date (lastmod-for-file f repo-path)]
                                (when date
                                  [(str rel) date]))))
                       (remove nil?)
                       (into (sorted-map)))]
          (log/info ::📅lastmod-generated {:dir docs-dir :entries (count data)})
          data)
        (do
          (log/warn ::⚠️directory-not-found {:dir docs-dir})
          {})))
    (catch Exception e
      (log/error ::❌lastmod-generation-failed {:dir docs-dir
                                              :error (.getMessage e)})
      {})))

;; Cache management using system state
(defn get-lastmod-cache [context]
  (system/get-system-state context [::lastmod-cache] {}))

(defn set-lastmod-cache [context cache]
  (system/set-system-state context [::lastmod-cache] cache))

(defn update-lastmod-cache [context product-id cache-entry]
  (let [current-cache (get-lastmod-cache context)
        updated-cache (assoc current-cache product-id cache-entry)]
    (set-lastmod-cache context updated-cache)))

(defn get-repo-head []
  (try
    (let [repo-path (or (System/getenv "DOCS_REPO_PATH") ".")
          {:keys [out exit]} (shell/sh "git" 
                                       "-c" (str "safe.directory=" repo-path)
                                       "rev-parse" "HEAD" 
                                       :dir repo-path)]
      (when (zero? exit)
        (str/trim out)))
    (catch Exception e
      (log/warn ::⚠️get-repo-head-failed {:error (.getMessage e)})
      nil)))

(defn generate-or-get-cached-lastmod
  "Generate lastmod or return from cache if HEAD unchanged"
  [context product-id docs-dir]
  (try
    (let [current-head (get-repo-head)
          cache-key product-id
          cached (get (get-lastmod-cache context) cache-key)]

      (if (and cached
               current-head
               (= (:head cached) current-head))
        ;; Return from cache
        (do
          (log/debug ::💾using-cached-lastmod {:product product-id})
          (:data cached))
        ;; Generate new data
        (let [new-data (generate-lastmod-data docs-dir)]
          (when current-head
            (update-lastmod-cache context product-id
                                  {:head current-head
                                   :data new-data
                                   :generated-at (java.time.Instant/now)}))
          new-data)))
    (catch Exception e
      (log/error ::❌cached-generation-failed {:error (.getMessage e)})
      {})))

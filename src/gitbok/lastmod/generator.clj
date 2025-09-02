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
  (log/info ::🔎checking-file {:file (.getPath file) :git-dir git-dir})
  (let [;; For symlinked files, we need to resolve to get the actual file
        canonical-file (.getCanonicalFile file)
        ;; But for git, we need the path relative to git-dir
        ;; Important: git-dir might be a symlink itself (git-sync case)
        ;; We need to ensure the relative path is within the git repository
        git-dir-canonical (.getCanonicalPath (io/file git-dir))
        ;; If the file is under the canonical git dir, use that path
        ;; Otherwise, try to find it relative to the original git-dir
        file-path-str (.getPath file)
        relative-path (cond
                        ;; If file path starts with canonical git dir, relativize from there
                        (str/starts-with? file-path-str git-dir-canonical)
                        (subs file-path-str (inc (count git-dir-canonical)))
                        
                        ;; If file path starts with git-dir, relativize from there
                        (str/starts-with? file-path-str git-dir)
                        (subs file-path-str (inc (count git-dir)))
                        
                        ;; Try to relativize using Path API
                        :else
                        (try
                          (str (.relativize (.toPath (io/file git-dir)) (.toPath file)))
                          (catch Exception _
                            ;; Last resort - use filename only
                            (.getName file))))
        git-cmd ["git" 
                 "-c" (str "safe.directory=" git-dir)
                 "log" "-1" "--format=%ct"
                 "--follow"  ; Important: follow renames/symlinks
                 (str relative-path)]
        _ (log/info ::🔍git-command {:cmd git-cmd :dir git-dir :file (.getPath file)})
        {:keys [out exit err]} (apply shell/sh (concat git-cmd [:dir git-dir]))]
    (when-not (zero? exit)
      (log/warn ::⚠️git-command-failed {:file (.getPath file)
                                        :exit exit
                                        :error err
                                        :cmd git-cmd}))
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
        (let [;; Check if it's a symlink
              is-symlink (java.nio.file.Files/isSymbolicLink (.toPath docs-file))
              _ (log/info ::📂directory-type {:path docs-dir :is-symlink is-symlink})
              
              ;; Resolve symlinks first - if docs-dir is a symlink, resolve it
              canonical-docs-file (.getCanonicalFile docs-file)
              _ (when (not= (.getPath docs-file) (.getPath canonical-docs-file))
                  (log/info ::📎symlink-resolved {:from (.getPath docs-file) 
                                                  :to (.getPath canonical-docs-file)}))
              
              ;; Log what we're about to walk
              _ (log/info ::🚶walking-directory {:path (.getPath canonical-docs-file)
                                                 :exists (.exists canonical-docs-file)
                                                 :is-directory (.isDirectory canonical-docs-file)})
              
              ;; Now walk the resolved directory
              md-files (try
                         (let [_ (log/info ::📚starting-files-walk {:path (.getPath canonical-docs-file)})]
                           (with-open [stream (java.nio.file.Files/walk 
                                              (.toPath canonical-docs-file)
                                              (into-array java.nio.file.FileVisitOption 
                                                         [java.nio.file.FileVisitOption/FOLLOW_LINKS]))]
                             (let [files (->> stream
                                            .iterator
                                            iterator-seq
                                            (map #(.toFile %))
                                            doall)
                                   _ (log/info ::🗂️total-files-found {:count (count files)})
                                   md-files (filter #(and (.isFile %)
                                                        (.endsWith (.getName %) ".md")) 
                                                   files)]
                               (log/info ::📝md-files-filtered {:count (count md-files)
                                                               :first-5 (take 5 (map #(.getName %) md-files))})
                               md-files)))
                         (catch java.nio.file.FileSystemLoopException e
                           ;; Handle circular symlinks
                           (log/warn ::⚠️circular-symlink {:dir docs-dir :error (.getMessage e)})
                           ;; Fallback to file-seq on canonical path
                           (let [files (->> (file-seq canonical-docs-file)
                                          (filter #(and (.isFile %)
                                                      (.endsWith (.getName %) ".md"))))]
                             (log/info ::📂using-file-seq-fallback {:count (count files)})
                             files))
                         (catch Exception e
                           (log/error ::❌files-walk-failed {:dir docs-dir 
                                                           :error (.getMessage e)
                                                           :error-type (type e)})
                           ;; Fallback to file-seq
                           (let [files (->> (file-seq canonical-docs-file)
                                          (filter #(and (.isFile %)
                                                      (.endsWith (.getName %) ".md"))))]
                             (log/info ::📂using-file-seq-error-fallback {:count (count files)})
                             files)))
              _ (log/info ::📁found-md-files {:count (count md-files) :dir docs-dir})
              ;; Important: relativize from original docs-dir, not canonical
              _ (log/info ::📋processing-files {:count (count md-files) 
                                                :first-5 (take 5 (map #(.getPath %) md-files))})
              data (->> md-files
                       (map (fn [f]
                              (let [;; Try to relativize from original path first
                                    rel (try
                                          (.relativize (.toPath docs-file) (.toPath f))
                                          (catch Exception _
                                            ;; If that fails, try from canonical path
                                            (try
                                              (.relativize (.toPath canonical-docs-file) (.toPath f))
                                              (catch Exception _
                                                ;; Last resort - use filename
                                                (.toPath (.getName f))))))
                                    date (lastmod-for-file f repo-path)]
                                (when date
                                  [(str rel) date]))))
                       (remove nil?)
                       (into (sorted-map)))
              _ (log/info ::🗓️git-dates-found {:with-dates (count data) 
                                              :without-dates (- (count md-files) (count data))})]
          (log/info ::📅lastmod-generated {:dir docs-dir :entries (count data)})
          ;; Log a sample of keys for debugging
          (when (> (count data) 0)
            (log/info ::📋sample-lastmod-keys {:first-5-keys (take 5 (keys data))}))
          data)
        (do
          (log/warn ::⚠️directory-not-found {:dir docs-dir})
          {})))
    (catch Exception e
      (log/error ::❌lastmod-generation-failed {:dir docs-dir
                                              :error (.getMessage e)
                                              :stacktrace (take 5 (.getStackTrace e))})
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

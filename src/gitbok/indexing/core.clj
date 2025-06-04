(ns gitbok.indexing.core
  (:require
   [gitbok.indexing.impl.summary]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.indexing.impl.common :as common]
   [gitbok.indexing.impl.search-index :as search-index]
   [gitbok.indexing.impl.file-to-uri :as file-to-uri]
   [gitbok.constants :as const]
   [system]
   [clojure.java.io :as io]
   [clojure.string :as str]
   [uui]
   [http]))

(set! *warn-on-reflection* true)

(defn uri->file-idx
  "Creates index to get filepath by uri.

  We read SUMMARY.md and use page names to create urls.
  This is used to match uri from the request to actual file to render."
  [context]
  (gitbok.indexing.impl.uri-to-file/uri->file-idx context))

(defn file->uri-idx
  "Creates index to get uri by filepath.

  We use it to add href attribute to links when we render markdown link."
  [context]
  (gitbok.indexing.impl.file-to-uri/file->uri-idx context))

(defn filepath->uri [context filepath]
  (gitbok.indexing.impl.file-to-uri/filepath->uri context filepath))

(defn uri->filepath [context ^String uri]
  (gitbok.indexing.impl.uri-to-file/uri->filepath (uri-to-file/get-idx context) uri))

(defn page-link->uri [context
                      ^String current-page-filepath
                      ^String relative-page-link]
  (let [current-page-filepath (str/replace
                               current-page-filepath
                               #"#.*$" "")

        section
        (last (re-matches #".*#(.*)" relative-page-link))

        relative-page-link
        (str/replace relative-page-link #"#.*$" "")

        current-page-filename
        (last (str/split current-page-filepath #"/"))

        same-page? (= current-page-filename relative-page-link)]
    (if same-page?
      (str "#" section)
      (let [file->uri-idx (file-to-uri/get-idx context)
            current-page-filepath
            (cond
              (str/starts-with? current-page-filepath "/docs")
              (subs current-page-filepath 6)

              (str/starts-with? current-page-filepath "./docs")
              (subs current-page-filepath 7)

              :else
              current-page-filepath)
            path
            (subs
             (common/get-filepath
              current-page-filepath
              relative-page-link)
             (count
              (System/getProperty
               "user.dir")))

            path (if (str/starts-with? path "/") (subs path 1) path)
            _ (def path path)
            _ (def file->uri-idx file->uri-idx)
            path (str "/" (:uri (get file->uri-idx path)))]

        (if section
          (str path "#" section)
          path)))))

(defn create-search-index
  "POC."
  [parsed-md-index]
  (search-index/parsed-md-idx->index parsed-md-index))

(defn list-markdown-files [dir]
  (let [dir-length (count dir)]
    (->> (file-seq (clojure.java.io/file dir))
         (filter #(.isFile %))
         (filter #(clojure.string/ends-with? (.getName %) ".md"))
         (map #(-> % .getPath (subs (inc dir-length)))))))

(defn slurp-md-files! [dir]
  (reduce (fn [acc filename]
            (assoc acc filename
                   (slurp (str dir "/" filename)))) {}
          (list-markdown-files dir)))

(defn set-md-files-idx [context]
  (system/set-system-state
   context
   [const/MD_FILES_IDX]
   (slurp-md-files! "./docs")))

(defn get-md-files-idx [context]
  (system/get-system-state
   context
   [const/MD_FILES_IDX]))

(defn set-search-idx
  [context parsed-md-index]
  (system/set-system-state
   context
   [const/SEARCH_IDX]
   (create-search-index parsed-md-index)))

(defn get-search-idx [context]
  (system/get-system-state context
                           [const/SEARCH_IDX]))

(defn search [context q]
  (search-index/search (get-search-idx context) q))

(ns gitbok.indexing.core
  (:require
    [gitbok.indexing.impl.summary]
    [gitbok.indexing.impl.uri-to-file]
    [gitbok.indexing.impl.file-to-uri]
    [gitbok.indexing.impl.common :as common]
    [gitbok.indexing.impl.search-index :as search-index]
    [gitbok.constants :as const]
    [gitbok.markdown.core :as markdown]
    [system]
    [clojure.string]
    [clojure.java.io]
    [uui]
    [http])
(:import [java.nio.file Files Paths]
           [java.nio.file.attribute BasicFileAttributes])
  )

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

(defn uri->filepath [uri->file-idx ^String uri]
  (let [fixed-url (if (= "/" (subs uri 0 1)) (subs uri 1) uri)]
    (str "/docs/" (get uri->file-idx fixed-url "readme/README.md"))))

(defn page-link->uri [context ^String current-page-uri ^String relative-page-link]
  (let [current-filepath (uri->filepath (gitbok.indexing.impl.uri-to-file/get-idx context) current-page-uri)
        real-file-path (common/get-filepath current-filepath relative-page-link)]
    (get file->uri-idx real-file-path "/")))

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

(defn set-parsed-markdown-index [context md-files-idx]
  (system/set-system-state
    context
    [const/PARSED_MARKDOWN_IDX]
    (mapv markdown/parse-markdown-content
          md-files-idx)))

(defn get-parsed-markdown-index [context]
  (system/get-system-state
    context
    [const/PARSED_MARKDOWN_IDX]))

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
  (def context context)
  (search-index/search (get-search-idx context) q))

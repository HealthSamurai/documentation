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
   [gitbok.utils :as utils]
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

        relative-page-link
        (if (str/ends-with? (str/lower-case relative-page-link) "/")
          (str relative-page-link "README.md")
          relative-page-link)

        current-page-filename
        (last (str/split current-page-filepath #"/"))

        same-page? (= current-page-filename relative-page-link)]
    (if same-page?
      (str "#" section)
      (let [file->uri-idx (file-to-uri/get-idx context)
            _ (when-not file->uri-idx (throw (Exception. "no idx")))
            current-page-filepath
            (cond
              (str/starts-with? current-page-filepath "/docs")
              (utils/safe-subs current-page-filepath 6)

              (str/starts-with? current-page-filepath "./docs")
              (subs current-page-filepath 7)

              :else
              current-page-filepath)
            path
            (utils/safe-subs
             (common/get-filepath
              current-page-filepath
              relative-page-link)
             (count
              (System/getProperty
               "user.dir")))

            path (if (str/starts-with? path "/") (utils/safe-subs path 1) path)
            _ (def path path)
            path (str "/" (:uri (get file->uri-idx path)))]

        (if section
          (str path "#" section)
          path)))))

(defn create-search-index
  "POC."
  [parsed-md-index]
  (search-index/parsed-md-idx->index parsed-md-index))

(defn read-content [filepath]
  (let [content (utils/slurp-resource filepath)]
    (if (str/starts-with? content "---")
      (last (str/split content #"---\n" 3))
      content)))

(defn slurp-md-files! [filepaths-from-summary]
  ;; "docs" is in resources classpath in deps.edn
  (reduce (fn [acc filename]
            (if (str/starts-with? filename "http")
              acc
              (assoc acc filename
                     (read-content filename)))) {}
          filepaths-from-summary))

(defn set-md-files-idx [context file-to-uri-idx]
  (system/set-system-state
   context
   [const/MD_FILES_IDX]
   (slurp-md-files! (keys file-to-uri-idx))))

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

(defn filepath->href [context filepath href]
  (if (str/starts-with? href "http")
    href
    (-> (page-link->uri
          context
          filepath
          href))))

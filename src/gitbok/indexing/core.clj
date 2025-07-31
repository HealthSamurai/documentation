(ns gitbok.indexing.core
  (:require
   [gitbok.indexing.impl.summary]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.indexing.impl.common :as common]
   [gitbok.indexing.impl.search-index :as search-index]
   [gitbok.indexing.impl.file-to-uri :as file-to-uri]
   [gitbok.constants :as const]
   [gitbok.products :as products]
   [edamame.core :as edamame]
   [system]
   [clojure.string :as str]
   [gitbok.utils :as utils]
   [gitbok.http]
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
  (def hhh (uri-to-file/get-idx context) )
  (gitbok.indexing.impl.uri-to-file/uri->filepath
    (uri-to-file/get-idx context) uri))

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
            path (gitbok.http/get-product-prefixed-url context (str "/" (:uri (get file->uri-idx path))))]

        (if section
          (str path "#" section)
          path)))))

(defn create-search-index
  [parsed-md-index]
  (let [si (search-index/parsed-md-idx->index parsed-md-index)]
    (println "created search index with " (count si) " entries")
    si))

(defn read-content [filepath]
  (utils/slurp-resource filepath))

(defn slurp-md-files! [context filepaths-from-summary]
  (let [files (reduce (fn [acc filename]
                        (if (str/starts-with? filename "http")
                          acc
                          (assoc acc filename
                                 (read-content filename)))) {}
                      (mapv #(products/filepath context %)
                            filepaths-from-summary))]
    (println (format "Slurped %s files" (count files)))
    files))

(defn set-md-files-idx [context file-to-uri-idx]
  (products/set-product-state
   context
   [const/MD_FILES_IDX]
   (slurp-md-files! context (keys file-to-uri-idx))))

(defn get-md-files-idx [context]
  (products/get-product-state
   context
   [const/MD_FILES_IDX]))

(defn set-search-idx
  [context parsed-md-index]
  (products/set-product-state
   context
   [const/SEARCH_IDX]
   (create-search-index parsed-md-index)))

(defn get-search-idx [context]
  (products/get-product-state context
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

(defn get-lastmod [context filepath]
  (when filepath
    (get
     (products/get-product-state context [const/LASTMOD])
     filepath)))

(defn set-lastmod [context]
  (products/set-product-state
   context
   [const/LASTMOD]
   (edamame/parse-string (utils/slurp-resource "lastmod.edn"))))

(defn parent? [context filepath]
  (let [uri (filepath->uri context filepath)]
    (when uri
      (->> context
           uri-to-file/get-idx
           (filter (fn [[k _]]
                     (str/starts-with? (name k) uri)))
           count
           (not= 1)))))

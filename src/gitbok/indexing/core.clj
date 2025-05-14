(ns gitbok.indexing.core
  (:require
   [gitbok.indexing.impl.summary]
   [gitbok.indexing.impl.uri-to-file]
   [gitbok.indexing.impl.file-to-uri]
   [gitbok.indexing.impl.common :as common]
   [system]
   [uui]
   [http]))

(set! *warn-on-reflection* true)

;; todo use this
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

(defn uri->filepath [uri->file-idx ^String uri]
  (let [fixed-url (if (= "/" (subs uri 0 1)) (subs uri 1) uri)]
    (def fixed-url fixed-url)
    (str "/docs/" (get uri->file-idx fixed-url "readme/README.md"))))

(defn page-link->uri [context ^String current-page-uri ^String relative-page-link]
  (let [current-filepath (uri->filepath (gitbok.indexing.impl.uri-to-file/get context) current-page-uri)
        real-file-path (common/get-filepath current-filepath relative-page-link)]
    (get file->uri-idx real-file-path "/")))

;; (defn summary []
;;   (gitbok.indexing.impl.summary/parse-summary))

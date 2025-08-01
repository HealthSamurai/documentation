(ns gitbok.indexing.impl.file-to-uri
  (:require
   [system]
   [clojure.string :as str]
   [gitbok.constants :as const]
   [gitbok.indexing.impl.summary]
   [gitbok.products :as products]
   [gitbok.utils :as utils]))

(defn file->uri-idx [context]
  ;; todo do not read summary again
  (let [summary-text (gitbok.indexing.impl.summary/read-summary context)
        link-pattern #"\[([^\]]+)\]\(([^)]+)\)"

        result
        (into {}
              (for [[_ page-name filepath] (re-seq link-pattern summary-text)
                    :when (not (str/starts-with? filepath "http"))]
                [filepath
                 {:title page-name
                  :uri
                  (str (str/replace filepath #"/[^/]*$" "") "/"
                       (if (str/ends-with? (str/lower-case filepath) "readme.md")
                         ""
                         (utils/s->url-slug page-name)))}]))]
  (println "file->uri idx is ready with " (count result) " entries" )
  result))

(defn set-idx [context]
  (products/set-product-state context [const/FILE->URI_IDX]
                              (file->uri-idx context)))

(defn get-idx [context]
  (products/get-product-state context [const/FILE->URI_IDX]))

(defn filepath->uri [context filepath]
  (:uri (get (get-idx context) filepath)))

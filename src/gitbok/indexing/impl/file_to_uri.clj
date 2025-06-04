(ns gitbok.indexing.impl.file-to-uri
  (:require
   [system]
   [clojure.string :as str]
   [gitbok.constants :as const]
   [gitbok.indexing.impl.summary]
   [gitbok.utils :as utils]))

(defn file->uri-idx [_]
  (let [summary-text (gitbok.indexing.impl.summary/read-summary)
        link-pattern #"\[([^\]]+)\]\(([^)]+)\)"]
    (into {}
          (for [[_ page-name filepath] (re-seq link-pattern summary-text)]
            [filepath
             (str (str/replace filepath #"/[^/]*$" "") "/"
                  (if (str/ends-with? (str/lower-case filepath) "readme.md")
                    ""
                    (utils/s->url-slug page-name)))]))))

(defn set-idx [context]
  (system/set-system-state context [const/FILE->URI_IDX]
                           (file->uri-idx context)))

(defn get-idx [context]
  (system/get-system-state context [const/FILE->URI_IDX]))

(defn filepath->uri [context filepath]
  (get (get-idx context) filepath))

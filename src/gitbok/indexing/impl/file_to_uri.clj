(ns gitbok.indexing.impl.file-to-uri
  (:require
   [system]
   [clojure.string :as str]
   [gitbok.constants :as const]
   [gitbok.indexing.impl.summary]
   [gitbok.indexing.impl.common :as common]))

(defn file->uri-idx [_]
  (let [summary-text (gitbok.indexing.impl.summary/read-summary)
        link-pattern #"\[([^\]]+)\]\(([^)]+)\)"]
    (into {}
          (for [[_ page-name filepath] (re-seq link-pattern summary-text)]
            [filepath
             (str (str/replace filepath #"/[^/]*$" "") "/"
                  (if (str/ends-with? (str/lower-case filepath) "readme.md")
                    ""
                    (common/sanitize-page-name page-name)))]))))

(defn set-idx [context]
  (system/set-system-state context [const/FILE->URI_IDX]
                           (file->uri-idx context)))

(defn get-idx [context]
  (system/get-system-state context [const/FILE->URI_IDX]))

(defn filepath->uri [context filepath]
  (get (get-idx context) filepath))

(for [[page-name filepath]
      [["FHIR Search" "api/rest-api/fhir-search/README.md"]]]
  [filepath
   (str (str/replace filepath #"/[^/]*$" "") "/"
        (if (str/ends-with? (str/lower-case filepath)
                            "readme.md")
          ""
          (common/sanitize-page-name page-name)))])

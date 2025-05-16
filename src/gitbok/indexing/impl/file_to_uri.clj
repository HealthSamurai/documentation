(ns gitbok.indexing.impl.file-to-uri
  (:require
   [system]
   [gitbok.constants :as const]
   [gitbok.indexing.impl.summary]
   [gitbok.indexing.impl.common :as common]))

(defn file->uri-idx [_]
  (let [summary-text (gitbok.indexing.impl.summary/read-summary)
        link-pattern #"\[([^\]]+)\]\(([^)]+)\)"]
    (into {}
          (for [[_ page-name filepath] (re-seq link-pattern summary-text)]
            [filepath (common/sanitize-page-name page-name)]))))

(defn uri+page-link [current-uri _relative-page-link]
  (str "docs" current-uri))

(defn set-idx [context]
  (system/set-system-state context [const/FILE->URI_IDX]
                           (file->uri-idx context)))

(defn get-idx [context]
  (system/get-system-state context [const/FILE->URI_IDX]))

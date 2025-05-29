(ns gitbok.indexing.impl.uri-to-file
  (:require
   [clojure.string :as str]
   [gitbok.constants :as const]
   [gitbok.indexing.impl.common :as common]
   [gitbok.indexing.impl.summary]
   [system]))

(defn uri->file-idx
  [_]
  (let [summary-text (gitbok.indexing.impl.summary/read-summary)
        lines (str/split-lines summary-text)]
    (loop [lines lines
           section nil
           stack []
           acc {}]
      (if (empty? lines)
        acc
        (let [line (first lines)
              trimmed (str/trim line)]
          (cond
            (str/starts-with? trimmed "# ")
            (recur (rest lines) section stack acc)

            (str/starts-with? trimmed "## ")
            (let [section-title (str/trim (subs trimmed 3))]
              (recur (rest lines) (common/sanitize-page-name section-title) [] acc))

            (re-matches #"\s*\*\s+\[([^\]]+)\]\(([^)]+)\)" line)
            (let [indent (->> line (re-find #"^(\s*)\*") second count)
                  level (quot indent 2)
                  [_ title path] (re-matches #"\s*\*\s+\[([^\]]+)\]\(([^)]+)\)" line)
                  new-stack (-> stack (subvec 0 level) (conj title))
                  prefix (if section (str section "/") "")
                  full-path (str prefix (str/join "/" (mapv common/sanitize-page-name new-stack)))]
              (recur (rest lines) section new-stack (assoc acc full-path path)))

            :else
            (recur (rest lines) section stack acc)))))))

;; todo...

;; {"aidbox-fhir-platform-documentation" "readme/README.md",
;;  "aidbox-fhir-platform-documentation/features" "readme/features.md",
;;  "aidbox-fhir-platform-documentation/architecture"
;;  "readme/architecture.md"}

;; (def summary-text
;;   "# Table of contents
;;
;; * [Aidbox FHIR platform documentation](readme/README.md)
;;   * [Features](readme/features.md)
;;   * [Architecture](readme/architecture.md)")
;;
;; (def lines (str/split-lines summary-text))
;;
;; (loop [lines lines
;;        section nil
;;        stack []
;;        acc {}]
;;   (if (empty? lines)
;;     acc
;;     (let [line (first lines)
;;           trimmed (str/trim line)]
;;       (cond
;;         (str/starts-with? trimmed "# ")
;;         (recur (rest lines) section stack acc)
;;
;;         (str/starts-with? trimmed "## ")
;;         (let [section-title (str/trim (subs trimmed 3))]
;;           (recur (rest lines) (common/sanitize-page-name section-title) [] acc))
;;
;;         (re-matches #"\s*\*\s+\[([^\]]+)\]\(([^)]+)\)" line)
;;         (let [indent (->> line (re-find #"^(\s*)\*") second count)
;;               level (quot indent 2)
;;               [_ title path] (re-matches #"\s*\*\s+\[([^\]]+)\]\(([^)]+)\)" line)
;;               new-stack (-> stack (subvec 0 level) (conj title))
;;               prefix (if section (str section "/") "")
;;               full-path (str prefix (str/join "/" (mapv common/sanitize-page-name new-stack)))]
;;           (recur (rest lines) section new-stack (assoc acc full-path path)))
;;     :else (recur (rest lines) section stack acc)
;;
;;     ))))

(defn get-idx [context]
  (system/get-system-state context [const/URI->FILE_IDX]))

(defn set-idx [context]
  (system/set-system-state context [const/URI->FILE_IDX]
                           (uri->file-idx context)))

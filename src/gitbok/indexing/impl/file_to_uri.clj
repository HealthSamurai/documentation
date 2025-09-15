(ns gitbok.indexing.impl.file-to-uri
  (:require
   [clojure.tools.logging :as log]
   [clojure.string :as str]
   [gitbok.indexing.impl.summary]
   [gitbok.products :as products]
   [gitbok.state :as state]))

(defn file->uri-idx [context]
  (let [summary-text (gitbok.indexing.impl.summary/read-summary context)
        link-pattern #"\[([^\]]+)\]\(([^)]+)\)"
        readme-path (products/readme-relative-path context)

        result
        (into {}
              (for [[_ page-name filepath] (re-seq link-pattern summary-text)
                    :when (not (str/starts-with? filepath "http"))]
                (let [;; Remove .md extension
                      clean-path (str/replace filepath #"\.md$" "")
                      ;; Generate URL from file path
                      uri (cond
                            ;; Root README - check against configured readme path
                            (and readme-path
                                 (= filepath readme-path))
                            "/"

                            ;; Also check clean path
                            (and readme-path
                                 (= clean-path (str/replace readme-path #"\.md$" "")))
                            "/"

                            ;; Legacy fallback
                            (= clean-path "README")
                            "/"

                            ;; Directory README files
                            (str/ends-with? clean-path "/README")
                            (str (subs clean-path 0 (- (count clean-path) 7)) "/")

                            ;; Regular files
                            :else
                            clean-path)]
                  [filepath
                   {:title page-name
                    :uri uri}])))]
    (log/info "index ready" {:type "file->uri" :entries (count result)})
    result))

(defn filepath->uri [context filepath]
  (-> (state/get-file-to-uri-idx context)
      (get filepath)
      :uri))

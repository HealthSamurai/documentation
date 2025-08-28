(ns gitbok.indexing.impl.file-to-uri
  (:require
   [klog.core :as log]
   [system]
   [clojure.string :as str]
   [gitbok.constants :as const]
   [gitbok.indexing.impl.summary]
   [gitbok.products :as products]))

(defn file->uri-idx [context]
  (let [summary-text (gitbok.indexing.impl.summary/read-summary context)
        link-pattern #"\[([^\]]+)\]\(([^)]+)\)"

        result
        (into {}
              (for [[_ page-name filepath] (re-seq link-pattern summary-text)
                    :when (not (str/starts-with? filepath "http"))]
                (let [;; Remove .md extension
                      clean-path (str/replace filepath #"\.md$" "")
                      ;; Generate URL from file path
                      uri (cond
                            ;; Root README
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
    (log/info ::index-ready {:type "file->uri" :entries (count result)})
    result))

(defn set-idx [context]
  (products/set-product-state context [const/FILE->URI_IDX]
                              (file->uri-idx context)))

(defn get-idx [context]
  (products/get-product-state context [const/FILE->URI_IDX]))

(defn filepath->uri [context filepath]
  (-> context
      get-idx
      (get filepath)
      :uri))

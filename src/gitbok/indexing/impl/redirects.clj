(ns gitbok.indexing.impl.redirects
  (:require
   [gitbok.utils :as utils]
   [clj-yaml.core :as yaml]))

(def ^:const ^:private
  file
  ".gitbook.yaml")

(defn redirects-from-file []
  (let [content (utils/slurp-resource file)]
    (when content
      (:redirects (yaml/parse-string content)))))

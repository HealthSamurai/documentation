(ns gitbok.indexing.impl.redirects
  (:require [clj-yaml.core :as yaml]))

(def ^:const ^:private
  file
  ".gitbook.yaml")

(defn redirects-from-file []
  (let [content (slurp file)]
    (when content
      (:redirects (yaml/parse-string content)))))

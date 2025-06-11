(ns gitbok.indexing.impl.redirects
  (:require
   [clojure.java.io :as io]
   [clj-yaml.core :as yaml]))

(def ^:const ^:private
  file
  ".gitbook.yaml")

(defn redirects-from-file []
  (let [content (slurp (io/resource file))]
    (when content
      (:redirects (yaml/parse-string content)))))

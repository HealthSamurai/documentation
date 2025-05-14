(ns gitbok.indexing.impl.common
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(set! *warn-on-reflection* true)

(defn sanitize-page-name [^String page-name]
  (-> page-name
      (clojure.string/replace #"[^a-zA-Z0-9]+" "-")
      (clojure.string/replace #"-{2,}" "-")
      (clojure.string/replace #"^-|-$" "")
      clojure.string/lower-case))

(defn get-filepath [filepath-a relative-filepath]
  (.getCanonicalPath (io/file filepath-a relative-filepath)))

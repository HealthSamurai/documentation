(ns gitbok.indexing.impl.common
  (:require
   [clojure.java.io :as io]))

(set! *warn-on-reflection* true)

(defn get-filepath [filepath-a relative-filepath]
  (.getCanonicalPath
    (io/file (.getParent (io/file filepath-a)) relative-filepath)))


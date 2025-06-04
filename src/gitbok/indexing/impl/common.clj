(ns gitbok.indexing.impl.common
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(set! *warn-on-reflection* true)

(defn get-filepath [filepath-a relative-filepath]
  (.getCanonicalPath
    (io/file (.getParent (io/file filepath-a)) relative-filepath)))

(comment

  ;; todo write tests
  (get-filepath
    "./docs/a/b/c/d"
    "../../../e/f/g/h") ;; "/home/svt/dev/hs/documentation/docs/e/f/g/h"
  )

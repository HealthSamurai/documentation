(ns gitbok.search
  (:require [gitbok.indexing.core :as indexing]
            [cheshire.core]))

(defn search [context query]
  (indexing/search context query))

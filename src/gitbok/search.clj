(ns gitbok.search
  (:require
   [gitbok.indexing.impl.meilisearch :as meilisearch]
   [cheshire.core]))

(defn search [query]
  (meilisearch/search query))

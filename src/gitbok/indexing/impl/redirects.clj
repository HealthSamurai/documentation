(ns gitbok.indexing.impl.redirects
  (:require
   [gitbok.products]))

(defn redirects [context]
  (let [redirects (:redirects
                    (gitbok.products/get-current-product context))]
    (println "got " (count redirects) " redirects")
    redirects))

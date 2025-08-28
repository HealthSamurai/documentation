(ns gitbok.indexing.impl.redirects
  (:require
   [klog.core :as log]
   [gitbok.products]))

(defn redirects [context]
  (let [redirects (:redirects
                    (gitbok.products/get-current-product context))]
    (log/info ::redirects-loaded {:count (count redirects)})
    redirects))

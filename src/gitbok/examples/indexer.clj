(ns gitbok.examples.indexer
  (:require [gitbok.products :as products]
            [klog.core :as log]))

(def EXAMPLES-KEY ::examples)

(defn set-examples
  "Store examples data in context for the current product"
  [context examples-data]
  (log/info ::set-examples {:count (count (:examples examples-data))})
  (products/set-product-state
   context
   [EXAMPLES-KEY]
   examples-data))

(defn get-examples
  "Retrieve examples data from context for the current product"
  [context]
  (products/get-product-state context [EXAMPLES-KEY]))

(defn update-examples!
  "Update examples data in context (called from webhook)"
  [context examples-data]
  (set-examples context examples-data)
  (log/info ::examples-updated
           {:timestamp (:timestamp examples-data)
            :count (count (:examples examples-data))}))

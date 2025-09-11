(ns gitbok.examples.indexer
  (:require [gitbok.products :as products]
            [gitbok.state :as state]
            [clojure.tools.logging :as log]))

(def EXAMPLES-KEY ::examples)

(defn set-examples
  "Store examples data for the current product"
  ([examples-data]
   ;; New API - works with state directly
   (log/info "set examples" {:count (count (:examples examples-data))})
   (state/set-product-state! [EXAMPLES-KEY] examples-data))
  ([context examples-data]
   ;; Legacy API - for compatibility
   (log/info "set examples (legacy)" {:count (count (:examples examples-data))})
   (products/set-product-state context [EXAMPLES-KEY] examples-data)))

(defn get-examples
  "Retrieve examples data for the current product"
  ([]
   ;; New API
   (state/get-product-state [EXAMPLES-KEY]))
  ([context]
   ;; Legacy API
   (products/get-product-state context [EXAMPLES-KEY])))

(defn update-examples!
  "Update examples data"
  ([examples-data]
   ;; New API
   (set-examples examples-data)
   (log/info "examples updated"
            {:timestamp (:timestamp examples-data)
             :count (count (:examples examples-data))}))
  ([context examples-data]
   ;; Legacy API
   (set-examples context examples-data)
   (log/info "examples updated (legacy)"
            {:timestamp (:timestamp examples-data)
             :count (count (:examples examples-data))})))

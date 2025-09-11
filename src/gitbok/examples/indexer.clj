(ns gitbok.examples.indexer
  (:require [gitbok.state :as state]
            [clojure.tools.logging :as log]))

(def EXAMPLES-KEY ::examples)

(defn set-examples
  "Store examples data for the current product"
  [context examples-data]
  (log/info "set examples" {:count (count (:examples examples-data))})
  (state/set-product-state! context [EXAMPLES-KEY] examples-data))

(defn get-examples
  "Retrieve examples data for the current product"
  [context]
  (state/get-product-state context [EXAMPLES-KEY]))

(defn update-examples!
  "Update examples data"
  [context examples-data]
  (set-examples context examples-data)
  (log/info "examples updated"
            {:timestamp (:timestamp examples-data)
             :count (count (:examples examples-data))}))

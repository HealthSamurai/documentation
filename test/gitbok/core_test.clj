(ns gitbok.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [system]
            [gitbok.core :as gitbok]))

(deftest start-test
  (def context (system/start-system gitbok/default-config))
  (is context)
  (system/stop-system context))


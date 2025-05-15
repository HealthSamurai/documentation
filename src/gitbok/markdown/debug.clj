(ns gitbok.markdown.debug
  (:require [clojure.string :as str]))

(defn log
  "Log message with timestamp to the console"
  [& msgs]
  (let [timestamp (java.time.LocalDateTime/now)]
    (println "[DEBUG]" timestamp (str/join " " msgs))))

(defn log-node
  "Log a node's structure for debugging"
  [prefix node]
  (log prefix "Type:" (or (:type node) "nil") 
       "Info:" (or (:info node) "nil")
       "Content length:" (if (sequential? (:content node))
                           (count (:content node))
                           (if (:content node) "1" "0"))))

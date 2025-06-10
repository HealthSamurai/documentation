(ns gitbok.utils
  (:require [clojure.string :as str]))

(defn s->url-slug [s]
  (when s
    (-> s
      (clojure.string/replace #"[^A-Za-z0-9]+" "-")
      (clojure.string/replace #"-{2,}" "-")
      (clojure.string/replace #"^-|-$" "")
      clojure.string/lower-case)))

(defn distinct-by [f coll]
  (vals (into {} (map (juxt f identity)) coll)))

(defn safe-subs [s start & [end]]
  (let [end (or end (count s))]
    (when (and s
               (>= start 0)
               (>= end start)
               (<= end (count s)))
      (subs s start end))))



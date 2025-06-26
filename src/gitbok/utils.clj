(ns gitbok.utils
  (:require
   [clojure.string :as str]
   [clojure.java.io :as io]))

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

(defn slurp-resource [path]
  (if-let [r
           (io/resource path)]
    (slurp r)
    (throw (Exception. (str "Cannot find " path)))))

(defn concat-urls [url1 url2]
  (str (java.net.URL.
        (java.net.URL. url1)
        url2)))

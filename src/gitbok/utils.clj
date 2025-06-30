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

(defn strip-markdown [text]
  (-> text
      (str/replace #"<table[\s\S]*?</table>" "")
      (str/replace #"<.*?>" "")
      (str/replace #"\[([^\]]+)\]\([^)]+\)" "$1")
      (str/replace #"\!\[.*?\]\(.*?\)" "")
      (str/replace #"`([^`]+)`" "$1")
      (str/replace #"(?m)^\s{0,3}#{1,6}\s*" "")
      (str/replace #"(?m)^\s*([-*+]|\d+\.)\s+" "")
      (str/replace #"(\*\*|__)(.*?)\1" "$2")
      (str/replace #"(\*|_)(.*?)\1" "$2")
      (str/replace #"(?m)^(-{3,}|_{3,}|\*{3,})$" "")
      (str/replace #"(?m)^>\s?" "")
      (str/replace #"\\([\\`*_{}\[\]()#+\-.!])" "$1")
      (str/trim)))

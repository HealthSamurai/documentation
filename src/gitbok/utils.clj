(ns gitbok.utils
  (:require
   [clojure.string :as str]
   [hiccup2.core]
   [system]
   [clojure.java.io :as io])
  (:import [java.time Instant ZoneId ZonedDateTime]
           [java.time.format DateTimeFormatter]
           [java.util Locale]))

(set! *warn-on-reflection* true)

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
  (println "reading " path)
  (if-let [r
           (io/resource path)]
    (slurp r)
    (throw (Exception. (str "Cannot find " path)))))

(defn concat-urls [& parts]
  (let [clean (fn [s] (str/replace s #"^/|/$" ""))
        url
        (->> parts
             (remove nil?)
             (remove #(= (str/trim %) "/"))
             (map-indexed (fn [i part]
                            (when part
                              (if (zero? i)
                                (str/replace part #"/+$" "")
                                (clean part)))))
             (str/join "/"))]
    (if (= "" url) "/" url)))

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

(defn etag [lastmod-iso-date]
  (str "\"" lastmod-iso-date "\""))

(defn ->html [hiccup]
  (str "<!DOCTYPE html>\n" (hiccup2.core/html hiccup)))

(def http-date-formatter
  (DateTimeFormatter/ofPattern "EEE, dd MMM yyyy HH:mm:ss 'GMT'" Locale/US))

(defn iso-to-http-date [iso-string]
  (let [instant (Instant/parse iso-string)
        zoned (ZonedDateTime/ofInstant instant (ZoneId/of "GMT"))]
    (.format ^DateTimeFormatter http-date-formatter zoned)))

(defn absolute-url
  [base-url prefix relative-url]
  (concat-urls base-url (or prefix "/") relative-url))

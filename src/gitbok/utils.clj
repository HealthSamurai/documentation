(ns gitbok.utils
  (:require
   [clojure.string :as str]
   [hiccup2.core]
   [system]
   [clojure.java.io :as io]
   [cheshire.core :as json])
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

(defn uri-to-relative
  "Removes prefix and product-path from URI to get relative path.
   Example: (uri-to-relative \"/docs/aidbox/readme\" \"/docs\" \"/aidbox\") => \"readme\"
            (uri-to-relative \"/docs/forms/api/endpoints\" \"/docs\" \"/forms\") => \"api/endpoints\""
  [uri prefix product-path]
  (when uri
    (let [;; First normalize multiple slashes
          normalized-uri (str/replace uri #"/+" "/")
          ;; Remove prefix first
          without-prefix (if (and prefix
                                  (not (str/blank? prefix))
                                  (str/starts-with? normalized-uri prefix))
                           (subs normalized-uri (count prefix))
                           normalized-uri)
          ;; Ensure we have a leading slash for proper comparison
          with-leading-slash (if (str/starts-with? without-prefix "/")
                               without-prefix
                               (str "/" without-prefix))
          ;; Then remove product path only if it matches
          without-product (if (and product-path
                                   (not (str/blank? product-path))
                                   (str/starts-with? with-leading-slash product-path))
                            (subs with-leading-slash (count product-path))
                            without-prefix) ;; Use original without-prefix if no match
          ;; Clean up leading slashes
          cleaned (str/replace without-product #"^/+" "")]
      (if (str/blank? cleaned) "/" cleaned))))

(defn concat-filenames [& parts]
  (when (seq parts)
    (let [result
          (reduce (fn [acc part]
                    (if (instance? java.io.File acc)
                      (java.io.File. ^java.io.File acc ^String part)
                      (java.io.File. ^String acc ^String part)))
                  (first parts)
                  (rest parts))]
      (let [^java.io.File file-result (if (instance? java.io.File result)
                                        result
                                        (java.io.File. ^String result))
            path (.getPath file-result)
            ;; Remove leading slash if present
            path (cond-> path
                   (str/starts-with? path "/")
                   (subs 1))
            ;; Replace "/./" with "/"
            path (str/replace path "/./" "/")]
        path))))

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

(defn ->json [data]
  (json/generate-string data {:key-fn name}))

(defn json->clj [json-str]
  (json/parse-string json-str true))

(defn parent [path]
  (.getParent (java.io.File. ^String path)))

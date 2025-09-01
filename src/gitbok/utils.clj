(ns gitbok.utils
  (:require
   [klog.core :as log]
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

(def volume-path (System/getenv "DOCS_VOLUME_PATH"))

(defn slurp-resource [path]
  (if volume-path
    (let [file-path (str volume-path "/" path)
          file (io/file file-path)]
      (if (.exists file)
        (do
          (log/debug ::read-volume {:path path})
          (slurp file))
        ;; Fallback to classpath for non-documentation resources
        (if-let [r (io/resource path)]
          (do
            (log/debug ::read-classpath {:path path :reason "not-in-volume"})
            (slurp r))
          (do
            (log/error ::file-not-found {:path path :locations ["volume" "classpath"]})
            (throw (Exception. (str "Cannot find " path " in volume or classpath")))))))
    ;; Original classpath logic for backward compatibility
    (if-let [r (io/resource path)]
      (do
        (log/debug ::read-classpath {:path path :reason "no-volume"})
        (slurp r))
      (do
        (log/error ::file-not-found {:path path :location "classpath"})
        (throw (Exception. (str "Cannot find " path)))))))

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
  (when iso-string
    (let [instant (Instant/parse iso-string)
        zoned (ZonedDateTime/ofInstant instant (ZoneId/of "GMT"))]
    (.format ^DateTimeFormatter http-date-formatter zoned))))

(defn format-relative-time
  "Format ISO date string to relative time (e.g. '2 hours ago', 'yesterday')"
  [iso-date-str]
  (when iso-date-str
    (try
      (let [then (Instant/parse iso-date-str)
            now (Instant/now)
            diff-ms (.toMillis (java.time.Duration/between then now))
            seconds (quot diff-ms 1000)
            minutes (quot seconds 60)
            hours (quot minutes 60)
            days (quot hours 24)
            weeks (quot days 7)
            months (quot days 30)
            years (quot days 365)]
        (cond
          (>= years 1) (if (= years 1) "a year ago" (str years " years ago"))
          (>= months 1) (if (= months 1) "a month ago" (str months " months ago"))
          (>= weeks 1) (if (= weeks 1) "a week ago" (str weeks " weeks ago"))
          (>= days 1) (if (= days 1) "yesterday" (str days " days ago"))
          (>= hours 1) (if (= hours 1) "an hour ago" (str hours " hours ago"))
          (>= minutes 1) (if (= minutes 1) "a minute ago" (str minutes " minutes ago"))
          :else "just now"))
      (catch Exception _
        ;; Fallback to absolute date
        (try
          (let [instant (Instant/parse iso-date-str)
                formatter (DateTimeFormatter/ofPattern "MMMM d, yyyy")
                zoned-time (.atZone instant (ZoneId/of "UTC"))]
            (.format formatter zoned-time))
          (catch Exception _
            iso-date-str))))))

(defn absolute-url
  [base-url prefix relative-url]
  (concat-urls base-url (or prefix "/") relative-url))

(defn ->json [data]
  (json/generate-string data {:key-fn name}))

(defn json->clj [json-str]
  (json/parse-string json-str true))

(defn parent [path]
  (.getParent (java.io.File. ^String path)))

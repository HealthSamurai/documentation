(ns gitbok.utils
  (:require
   [clojure.string :as str]
   [clojure.tools.logging :as log]
   [gitbok.state :as state]
   [hiccup2.core])
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

(defn safe-subs [s start & [end]]
  (let [end (or end (count s))]
    (when (and s
               (>= start 0)
               (>= end start)
               (<= end (count s)))
      (subs s start end))))

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
          cleaned (str/replace without-product #"^/+" "")
          result (if (str/blank? cleaned) "/" cleaned)]
      result)))

(defn concat-filenames [& parts]
  (when (seq parts)
    (let [result
          (reduce (fn [acc part]
                    (if (instance? java.io.File acc)
                      (java.io.File. ^java.io.File acc ^String part)
                      (java.io.File. ^String acc ^String part)))
                  (first parts)
                  (rest parts))
          ^java.io.File file-result
          (if (instance? java.io.File result)
            result
            (java.io.File. ^String result))
          path (.getPath file-result)
          ;; Remove leading slash if present
          path (cond-> path
                 (str/starts-with? path "/")
                 (subs 1))
          ;; Replace "/./" with "/"
          path (str/replace path "/./" "/")]
      path)))

(defn etag [lastmod-iso-date]
  (str "\"" lastmod-iso-date "\""))

(defn generate-versioned-etag
  "Generate an ETag that includes build version, docs commit, and content type"
  [context content-type lastmod]
  ;; content-type: "full" or "partial"
  ;; Format: "prefix-buildVersion-docsCommit-lastmod"
  (let [build-version (str/trim (state/get-config context :version "unknown"))
        reload-state (state/get-cache context :reload-state {})
        docs-commit (let [gh (:git-head reload-state)]
                     (if (and gh (not= gh "") (not= gh nil))
                       gh
                       "no-commit"))
        ;; Take first 7 chars of commits for brevity
        short-docs-commit (subs docs-commit 0 (min 7 (count docs-commit)))
        short-build-version (subs build-version 0 (min 7 (count build-version)))]
    ;; ETag must be quoted per RFC 7232
    (str "\"" content-type "-" short-build-version "-" short-docs-commit
         "-" (hash lastmod) "\"")))

(defn ->html [hiccup]
  (str "<!DOCTYPE html>\n" (hiccup2.core/html hiccup)))

(def http-date-formatter
  (DateTimeFormatter/ofPattern "EEE, dd MMM yyyy HH:mm:ss 'GMT'" Locale/US))

(defn iso-to-http-date [iso-string]
  (when iso-string
    (let [instant (Instant/parse iso-string)
          zoned (ZonedDateTime/ofInstant instant (ZoneId/of "GMT"))]
      (.format ^DateTimeFormatter http-date-formatter zoned))))

(defn parse-http-date
  "Parse HTTP date string to Instant"
  [date-string]
  (when date-string
    (try
      (.toInstant
       (ZonedDateTime/parse date-string http-date-formatter))
      (catch Exception _
        ;; Try alternative formats that browsers might send
        (try
          ;; Try parsing as RFC 850 format (deprecated but still used)
          (let [rfc850-formatter (DateTimeFormatter/ofPattern "EEEE, dd-MMM-yy HH:mm:ss 'GMT'" Locale/US)]
            (.toInstant
             (ZonedDateTime/parse date-string rfc850-formatter)))
          (catch Exception _
            ;; Try parsing as ANSI C asctime() format
            (try
              (let [asctime-formatter (DateTimeFormatter/ofPattern "EEE MMM dd HH:mm:ss yyyy" Locale/US)]
                (.toInstant
                 (ZonedDateTime/parse date-string asctime-formatter)))
              (catch Exception e3
                (log/debug "Could not parse HTTP date" {:date date-string
                                                        :error (.getMessage e3)})
                nil))))))))

(defn absolute-url
  [base-url prefix relative-url]
  (concat-urls base-url (or prefix "/") relative-url))

(defn parent [path]
  (.getParent (java.io.File. ^String path)))

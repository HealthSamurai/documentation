(ns gitbok.http
  (:require
   [system]
   [clojure.string :as str]
   [gitbok.constants :as const]
   [gitbok.utils :as utils]))

(defn response1
  ([body status lastmod section]
   (let [html (utils/->html body)]
     {:status (or status 200)
      :headers
      (cond-> {"content-type" "text/html; ; charset=utf-8"}
        section
        (assoc "Scroll-To-Id" section)

        lastmod
        (assoc
         "Scroll-To-Id" section
         "Cache-Control" "public, max-age=86400"
         "Last-Modified" (utils/iso-to-http-date lastmod)
         "ETag" (utils/etag lastmod)))
      :body html}))
  ([body]
   (response1 body 200 nil nil)))

(defn set-port [context port]
  (system/set-system-state
   context [const/PORT]
   port))

(defn get-port [context]
  (system/get-system-state context [const/PORT]))

(defn set-prefix [context prefix]
  (system/set-system-state
   context [const/PREFIX]
   prefix))

(defn get-prefix [context]
  (system/get-system-state context [const/PREFIX]))

(defn set-base-url [context base-url]

  (system/set-system-state
   context [const/BASE_URL]
   base-url))

(defn get-base-url [context]
  (system/get-system-state context [const/BASE_URL]))

(defn get-absolute-url [context relative-url]
  (utils/absolute-url (get-base-url context)
                      (get-prefix context)
                      relative-url))

(defn get-prefixed-url [context relative-url]
  (utils/concat-urls (get-prefix context) relative-url))

(defn get-url [context]
  (utils/concat-urls
   (get-base-url context)
   (get-prefix context)))

(defn url-without-prefix
  [context uri]
  (let [prefix
        (get-prefix context)]
    (if
     (str/starts-with? uri prefix)
      (subs uri (count prefix))
      uri)))

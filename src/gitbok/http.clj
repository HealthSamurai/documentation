(ns gitbok.http
  (:require
   [gitbok.state :as state]
   [clojure.string :as str]
   [gitbok.utils :as utils]
   [gitbok.products :as products]))

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
         "Cache-Control" "public, max-age=300"
         "Last-Modified" (utils/iso-to-http-date lastmod)
         "ETag" (utils/etag lastmod)))
      :body html}))
  ([body]
   (response1 body 200 nil nil)))

(defn set-port [context port]
  (state/set-state! [:config :port] port))

(defn get-port [context]
  (state/get-config :port))

(defn set-prefix [context prefix]
  (state/set-state! [:config :prefix] prefix))

(defn get-prefix [context]
  (state/get-config :prefix ""))

(defn set-base-url [context base-url]
  (state/set-state! [:config :base-url] base-url))

(defn get-base-url [context]
  (state/get-config :base-url))

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

(defn set-version [context version]
  (state/set-state! [:config :version] version))

(defn get-version [context]
  (state/get-config :version))

(defn set-dev-mode [context dev-mode]
  (state/set-state! [:config :dev-mode] dev-mode))

(defn get-dev-mode [context]
  (state/get-config :dev-mode))

(defn get-product-prefix
  [context]
  (let [product (products/get-current-product context)
        docs-prefix (get-prefix context)]
    (utils/concat-urls docs-prefix (:path product))))

(defn get-product-prefixed-url
  [context relative-url]
  (utils/concat-urls (get-product-prefix context) relative-url))

(defn get-product-absolute-url
  [context relative-url]
  (utils/absolute-url (get-base-url context)
                      (get-product-prefix context)
                      relative-url))



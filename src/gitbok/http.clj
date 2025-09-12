(ns gitbok.http
  (:require
   [gitbok.state :as state]
   [gitbok.products :as products]
   [clojure.string :as str]
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
         "Cache-Control" "public, max-age=300"
         "Last-Modified" (utils/iso-to-http-date lastmod)
         "ETag" (utils/etag lastmod)))
      :body html}))
  ([body]
   (response1 body 200 nil nil)))

;; All get-/set- functions removed - use state functions directly

(defn get-absolute-url [context relative-url]
  (utils/absolute-url (state/get-config context :base-url)
                      (state/get-config context :prefix "")
                      relative-url))

(defn get-prefixed-url [context relative-url]
  (utils/concat-urls (state/get-config context :prefix "") relative-url))

(defn get-url [context]
  (utils/concat-urls
   (state/get-config context :base-url)
   (state/get-config context :prefix "")))

(defn url-without-prefix
  [context uri]
  (let [prefix
        (state/get-config context :prefix "")]
    (if
     (str/starts-with? uri prefix)
      (subs uri (count prefix))
      uri)))


(defn get-product-prefix
  [context]
  (let [product (or (:product context)
                    (:gitbok.products/current-product context)
                    (products/get-current-product context))
        docs-prefix (state/get-config context :prefix "")]
    (utils/concat-urls docs-prefix (:path product))))

(defn get-product-prefixed-url
  [context relative-url]
  (utils/concat-urls (get-product-prefix context) relative-url))

(defn get-product-absolute-url
  [context relative-url]
  (utils/absolute-url (state/get-config context :base-url)
                      (get-product-prefix context)
                      relative-url))



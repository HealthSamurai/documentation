(ns gitbok.http
  (:require
   [gitbok.utils :as utils]))

(defn response1
  ([body status lastmod & [cache?]]
   (let [html (utils/->html body)]
     {:status (or status 200)
      :headers
      (cond-> {"content-type" "text/html; ; charset=utf-8"}
        (and true lastmod)
        (assoc
         "Cache-Control" "public, max-age=86400"
         "Last-Modified" (utils/iso-to-http-date lastmod)
         "ETag" (utils/etag lastmod)))
      :body html}))
  ([body]
   (response1 body 200 nil)))

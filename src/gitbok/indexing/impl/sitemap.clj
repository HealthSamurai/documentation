(ns gitbok.indexing.impl.sitemap
  (:require
   [gitbok.constants :as const]
   [system]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [clojure.data.xml :as xml]))

(defn make-url-entry-with-lastmod [loc lastmod]
  {:tag :url
   :content [{:tag :loc :content [loc]}
             {:tag :lastmod :content [lastmod]}]})

(defn generate-sitemap-pages [context base-url all-related-urls lastmod-page]
  (let [content
        (map
         (fn [related-url]
           (let [filepath (uri-to-file/uri->filepath
                           (uri-to-file/uri->file-idx context)
                           related-url)]
             (make-url-entry-with-lastmod
               (str (java.net.URL. (java.net.URL. base-url) related-url))
              (get lastmod-page filepath))))
         all-related-urls)

        urlset
        {:tag :urlset
         :attrs {:xmlns "http://www.sitemaps.org/schemas/sitemap/0.9"}
         :content content}]
    (xml/emit-str urlset)))

(defn set-sitemap [context base-url]
  (system/set-system-state
   context
   [const/SITEMAP]
   (xml/emit-str
    {:tag :sitemapindex
     :attrs {:xmlns "http://www.sitemaps.org/schemas/sitemap/0.9"}
     :content
     [{:tag :sitemap
       :content
       [{:tag :loc :content
         [(str base-url "/sitemap-pages.xml")]}]}]})))

(defn get-sitemap [context]
  (system/get-system-state context [const/SITEMAP]))

(defn set-sitemap-pages [context base-url lastmod]
  (system/set-system-state
   context
   [const/SITEMAP_PAGES]
   (generate-sitemap-pages
    context
    base-url
    (uri-to-file/all-urls context)
    lastmod)))

(defn get-sitemap-pages [context]
  (system/get-system-state context [const/SITEMAP_PAGES]))

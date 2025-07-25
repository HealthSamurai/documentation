(ns gitbok.indexing.impl.sitemap
  (:require
   [clojure.string :as str]
   [gitbok.constants :as const]
   [gitbok.utils :as utils]
   [system]
   [gitbok.http]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [clojure.data.xml :as xml]))

(defn make-url-entry-with-lastmod [loc lastmod priority]
  [:url
   [:loc loc]
   [:priority priority ]
   [:lastmod lastmod]])

(defn generate-sitemap [context all-related-urls lastmod-page]
  (let [content
        (mapv
         (fn [related-url]
          (let [filepath (uri-to-file/uri->filepath
                          (uri-to-file/uri->file-idx context)
                          related-url)
                priority
                (if
                 (or (str/starts-with? related-url "readme/")
                     (str/starts-with? related-url "getting-started/")
                     (str/starts-with? related-url "api/")
                     (str/starts-with? related-url "database/")
                     (str/starts-with? related-url "deployment"))
                 "1.0"
                 "0.5")]
             (make-url-entry-with-lastmod
              (gitbok.http/get-absolute-url context related-url)
              (get lastmod-page filepath)
              priority)))
         all-related-urls)

        urlset
        (xml/sexp-as-element
         (into
          [:urlset
           {:xmlns "http://www.sitemaps.org/schemas/sitemap/0.9"}]
          content))]
    (xml/emit-str urlset)))

(defn set-sitemap [context lastmod]
  (system/set-system-state
   context
   [const/SITEMAP]
   (generate-sitemap
    context
    (uri-to-file/all-urls context)
    lastmod)))

(defn get-sitemap [context]
  (system/get-system-state context [const/SITEMAP]))

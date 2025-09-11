(ns gitbok.indexing.impl.sitemap
  (:require
   [clojure.string :as str]
   [gitbok.http]
   [gitbok.products :as products]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.indexing.impl.summary]
   [clojure.data.xml :as xml]))

(defn make-url-entry-with-lastmod [loc lastmod priority]
  [:url
   [:loc loc]
   [:priority priority]
   [:lastmod lastmod]])

(defn generate-sitemap [context all-related-urls lastmod-page]
  (let [content
        (mapv
         (fn [related-url]
           (let [filepath (uri-to-file/uri->filepath
                           (uri-to-file/get-idx context)
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
              (gitbok.http/get-product-absolute-url context related-url)
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
  (products/set-product-state
   context
   [::sitemap]
   (let [;; Get primary navigation links (excluding cross-section references)
         primary-links (gitbok.indexing.impl.summary/get-primary-navigation-links context)
         ;; Extract just the hrefs, removing leading slashes
         primary-urls (mapv (fn [link]
                              (let [href (:href link)]
                                (if (str/starts-with? href "/")
                                  (subs href 1)
                                  href)))
                            primary-links)]
     (generate-sitemap
      context
      primary-urls
      lastmod))))

(defn get-sitemap [context]
  (products/get-product-state context [::sitemap]))

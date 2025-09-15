(ns gitbok.indexing.impl.sitemap
  (:require
   [gitbok.http :as http]
   [clojure.string :as str]
   [gitbok.state :as state]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.indexing.impl.summary]
   [clojure.data.xml :as xml]))

(defn make-url-entry-with-lastmod [loc lastmod priority]
  [:url
   [:loc loc]
   [:priority priority]
   [:lastmod lastmod]])

(defn generate-sitemap [context all-related-urls lastmod-page]
  (let [;; Filter out URLs ending with /readme to avoid duplicates
        filtered-urls (filterv #(not (str/ends-with? % "/readme")) all-related-urls)
        content
        (mapv
         (fn [related-url]
           (let [filepath (uri-to-file/uri->filepath
                           (state/get-uri-to-file-idx context)
                           related-url)
                 priority
                 (if
                  (or (= related-url "")  ;; Root page (previously readme)
                      (str/starts-with? related-url "getting-started/")
                      (str/starts-with? related-url "api/")
                      (str/starts-with? related-url "database/")
                      (str/starts-with? related-url "deployment"))
                   "1.0"
                   "0.5")]
             (make-url-entry-with-lastmod
              (http/get-product-absolute-url context related-url)
              (get lastmod-page filepath)
              priority)))
         filtered-urls)

        urlset
        (xml/sexp-as-element
         (into
          [:urlset
           {:xmlns "http://www.sitemaps.org/schemas/sitemap/0.9"}]
          content))]
    (xml/emit-str urlset)))


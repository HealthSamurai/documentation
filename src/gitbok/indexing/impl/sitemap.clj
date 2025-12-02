(ns gitbok.indexing.impl.sitemap
  (:require
   [gitbok.http :as http]
   [clojure.string :as str]
   [gitbok.state :as state]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.indexing.impl.summary]
   [clojure.data.xml :as xml]))

(defn make-url-entry-with-lastmod [loc lastmod priority]
  (if lastmod
    [:url
     [:loc loc]
     [:priority priority]
     [:lastmod lastmod]]
    [:url
     [:loc loc]
     [:priority priority]]))

(defn generate-sitemap [context all-related-urls lastmod-page]
  (let [;; Filter out URLs ending with /readme to avoid duplicates
        filtered-urls (filterv #(not (str/ends-with? % "/readme")) all-related-urls)
        ;; Get product prefix to strip from URLs (remove leading slash if present)
        raw-prefix (http/get-product-prefix context)
        product-prefix (if (str/starts-with? raw-prefix "/")
                         (subs raw-prefix 1)
                         raw-prefix)
        product-prefix-slash (str product-prefix "/")
        content
        (mapv
         (fn [related-url]
           ;; Strip product prefix from URL before looking up in index
           (let [url-without-prefix (cond
                                      (= related-url product-prefix) ""
                                      (str/starts-with? related-url product-prefix-slash)
                                      (subs related-url (count product-prefix-slash))
                                      :else related-url)
                 filepath (uri-to-file/uri->filepath
                           (state/get-uri-to-file-idx context)
                           url-without-prefix)
                 lastmod-value (get lastmod-page filepath)
                 priority
                 (if
                  (or (= url-without-prefix "")  ;; Root page
                      (str/starts-with? url-without-prefix "getting-started/")
                      (str/starts-with? url-without-prefix "api/")
                      (str/starts-with? url-without-prefix "database/")
                      (str/starts-with? url-without-prefix "deployment"))
                   "1.0"
                   "0.5")]
             (make-url-entry-with-lastmod
              (http/get-product-absolute-url context url-without-prefix)
              lastmod-value
              priority)))
         filtered-urls)

        urlset
        (xml/sexp-as-element
         (into
          [:urlset
           {:xmlns "http://www.sitemaps.org/schemas/sitemap/0.9"}]
          content))]
    (xml/emit-str urlset)))


(ns gitbok.indexing.impl.sitemap-index
  (:require
   [gitbok.http :as http]
   [clojure.data.xml :as xml]
   [gitbok.products :as products]))

(defn generate-sitemap-index
  "Generates a sitemap index XML that references all product sitemaps"
  [context]
  (let [products-config (products/get-products-config context)
        entries (mapv
                 (fn [product]
                   [:sitemap
                    [:loc (http/get-absolute-url
                           context
                           (str (:path product) "/sitemap.xml"))]
                    ;; TODO: Add actual lastmod when available
                    [:lastmod (str (java.time.Instant/now))]])
                 products-config)

        sitemapindex
        (xml/sexp-as-element
         (into
          [:sitemapindex
           {:xmlns "http://www.sitemaps.org/schemas/sitemap/0.9"}]
          entries))]
    (xml/emit-str sitemapindex)))

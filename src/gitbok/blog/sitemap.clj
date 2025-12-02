(ns gitbok.blog.sitemap
  "Sitemap generation for blog articles"
  (:require [gitbok.blog.core :as blog]
            [gitbok.state :as state]
            [gitbok.utils :as utils]
            [gitbok.indexing.impl.sitemap :as sitemap]
            [clojure.data.xml :as xml]))

(defn- format-date
  "Format date string for sitemap (YYYY-MM-DD format).
   Accepts ISO format '2024-07-26' (pass through) or 'Jul 26, 2024' (convert)."
  [date-str]
  (when date-str
    (cond
      ;; Already ISO format
      (re-matches #"\d{4}-\d{2}-\d{2}" date-str)
      date-str

      ;; Old format: "Jul 26, 2024"
      :else
      (try
        (let [months {"Jan" "01" "Feb" "02" "Mar" "03" "Apr" "04"
                      "May" "05" "Jun" "06" "Jul" "07" "Aug" "08"
                      "Sep" "09" "Oct" "10" "Nov" "11" "Dec" "12"}
              [_ month day year] (re-find #"(\w+)\s+(\d+),\s+(\d+)" date-str)]
          (when (and month day year)
            (str year "-" (get months month) "-" (format "%02d" (Integer/parseInt day)))))
        (catch Exception _ nil)))))

(defn- get-host
  "Extract host (scheme + domain + port) from base-url, stripping any path"
  [base-url]
  (when base-url
    (let [uri (java.net.URI. base-url)]
      (str (.getScheme uri) "://" (.getAuthority uri)))))

(defn generate-blog-sitemap
  "Generate sitemap.xml for blog articles"
  [context]
  (let [;; Get only the host part (e.g., http://localhost:8081), not the path
        host (get-host (state/get-config context :base-url))
        articles (blog/get-article-index context)
        tags (blog/get-all-tags context)

        entries
        (concat
         ;; Blog listing page
         [(sitemap/make-url-entry-with-lastmod
           (utils/concat-urls host blog/url-prefix "blog") nil "0.8")]

         ;; Article pages
         (map (fn [{:keys [slug published]}]
                (sitemap/make-url-entry-with-lastmod
                 (utils/concat-urls host blog/url-prefix "articles" slug)
                 (format-date published)
                 "0.6"))
              articles)

         ;; Category pages
         (map (fn [tag]
                (sitemap/make-url-entry-with-lastmod
                 (utils/concat-urls host blog/url-prefix "article-categories"
                                    (blog/category-to-slug tag))
                 nil
                 "0.4"))
              tags))

        urlset (xml/sexp-as-element
                (into [:urlset {:xmlns "http://www.sitemaps.org/schemas/sitemap/0.9"}]
                      entries))]
    (xml/emit-str urlset)))

(ns gitbok.blog.core
  (:require [gitbok.blog.frontmatter :as frontmatter]
            [gitbok.blog.ui :as blog-ui]
            [gitbok.markdown.core :as markdown]
            [gitbok.state :as state]
            [gitbok.ui.blog-header :as blog-header]
            [gitbok.ui.layout :as layout]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.tools.logging :as log]
            [hiccup2.core :as hiccup]
            [ring.util.response :as response]))

(def blog-dir "blog")
(def articles-per-page 15)

(defn- list-markdown-files
  "List all .md files in the blog directory."
  []
  (let [dir (io/file blog-dir)]
    (if (.exists dir)
      (->> (.listFiles dir)
           (filter #(.isFile %))
           (filter #(str/ends-with? (.getName %) ".md"))
           (map #(.getPath %))
           (sort))
      [])))

(defn- load-article-content
  "Load content from a markdown file."
  [filepath]
  (try
    (slurp filepath)
    (catch Exception e
      (log/error e "Failed to load article" {:filepath filepath})
      nil)))

(defn- parse-article
  "Parse article markdown file into metadata and rendered content."
  [context filepath]
  (when-let [content (load-article-content filepath)]
    (let [{:keys [metadata content]} (frontmatter/parse-frontmatter content)
          slug (or (:slug metadata)
                   (-> filepath
                       (str/replace blog-dir "")
                       (str/replace ".md" "")
                       (str/replace #"^/" "")))
          parsed (markdown/parse-markdown-content context [filepath content])
          rendered (markdown/render-md context filepath (:parsed parsed))]
      {:slug slug
       :filepath filepath
       :metadata (assoc metadata :slug slug)
       :parsed parsed
       :rendered rendered})))

(defn- extract-all-tags
  "Extract unique tags from all articles."
  [articles]
  (->> articles
       (mapcat (comp :tags :metadata))
       (remove nil?)
       (set)))

(defn- build-article-index
  "Build sorted index of articles with metadata."
  [articles]
  (->> articles
       (map (fn [{:keys [slug metadata]}]
              (assoc metadata :slug slug)))
       (sort-by :published)
       (reverse)
       (vec)))

(defn- build-cache
  "Load and parse all blog articles, building cache structure."
  [context]
  (log/info "Building blog cache...")
  (let [files (list-markdown-files)
        articles (keep #(parse-article context %) files)
        articles-map (into {} (map (juxt :slug identity) articles))
        index (build-article-index articles)
        tags (extract-all-tags articles)]
    (log/info "Blog cache built" {:articles (count articles) :tags (count tags)})
    {:articles articles-map
     :index index
     :tags tags}))

(defn initialize-blog-cache!
  "Initialize blog cache at application startup."
  [context]
  (let [cache (build-cache context)]
    (state/set-cache! context :blog cache)
    cache))

(defn reload-blog-cache!
  "Reload blog cache (for dev mode)."
  [context]
  (initialize-blog-cache! context))

(defn get-blog-cache
  "Get the entire blog cache."
  [context]
  (state/get-cache context :blog))

(defn get-article
  "Get a single article by slug."
  [context slug]
  (when-let [cache (get-blog-cache context)]
    (get-in cache [:articles slug])))

(defn get-all-tags
  "Get all unique tags."
  [context]
  (when-let [cache (get-blog-cache context)]
    (:tags cache)))

(defn get-article-index
  "Get the full article index (sorted by date)."
  [context]
  (when-let [cache (get-blog-cache context)]
    (:index cache)))

(defn filter-by-tag
  "Filter articles by tag."
  [articles tag]
  (if (str/blank? tag)
    articles
    (filter #(some #{tag} (:tags %)) articles)))

(defn paginate
  "Paginate articles list."
  [articles page]
  (let [page (or page 1)
        start (* (dec page) articles-per-page)
        end (+ start articles-per-page)]
    {:articles (vec (take articles-per-page (drop start articles)))
     :page page
     :total-pages (Math/ceil (/ (count articles) articles-per-page))
     :total-articles (count articles)
     :has-prev (> page 1)
     :has-next (< end (count articles))}))

(defn get-articles-for-listing
  "Get filtered and paginated articles for blog listing page."
  [context {:keys [tag page]}]
  (let [index (get-article-index context)
        filtered (filter-by-tag index tag)
        paginated (paginate filtered page)]
    (assoc paginated :tag tag :all-tags (get-all-tags context))))

;; HTTP Handlers

(defn- render-html-response
  "Render hiccup to HTML response."
  [hiccup-content]
  (-> (str "<!DOCTYPE html>\n" (hiccup/html hiccup-content))
      (response/response)
      (response/content-type "text/html; charset=utf-8")))

(defn list-handler
  "Handler for /blog listing page."
  [context]
  (let [params (get-in context [:request :params])
        tag (get params "tag")
        page (try
               (Integer/parseInt (or (get params "page") "1"))
               (catch Exception _ 1))
        listing-data (get-articles-for-listing context {:tag tag :page page})
        content [:html {:lang "en"}
                 [:head
                  [:meta {:charset "UTF-8"}]
                  [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
                  [:title (if tag
                            (str tag " - Blog - Health Samurai")
                            "Blog - Health Samurai")]
                  [:link {:rel "stylesheet" :href "/static/app.css"}]
                  [:script {:src "https://unpkg.com/htmx.org@1.9.10"}]]
                 [:body {:class "min-h-screen flex flex-col bg-surface text-on-surface"}
                  (blog-header/blog-nav context)
                  (blog-ui/blog-listing-page context listing-data)
                  (layout/site-footer context)]]]
    (render-html-response content)))

(defn article-handler
  "Handler for /articles/:slug page."
  [context]
  (let [slug (get-in context [:request :path-params :slug])
        article (get-article context slug)]
    (if article
      (let [content [:html {:lang "en"}
                     [:head
                      [:meta {:charset "UTF-8"}]
                      [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
                      [:title (str (get-in article [:metadata :title]) " - Health Samurai Blog")]
                      (when-let [teaser (get-in article [:metadata :teaser])]
                        [:meta {:name "description" :content teaser}])
                      [:link {:rel "stylesheet" :href "/static/app.css"}]
                      [:script {:src "https://unpkg.com/htmx.org@1.9.10"}]]
                     [:body {:class "min-h-screen flex flex-col bg-surface text-on-surface"}
                      (blog-header/blog-nav context)
                      (blog-ui/article-page context article)
                      (layout/site-footer context)]]]
        (render-html-response content))
      ;; Article not found
      (-> (response/response "Article not found")
          (response/status 404)
          (response/content-type "text/html; charset=utf-8")))))

(defn search-handler
  "Handler for /blog/search endpoint."
  [context]
  (let [query (get-in context [:request :params "q"])]
    (if (and query (> (count query) 2))
      ;; TODO: Implement actual Meilisearch integration
      ;; For now, return empty results
      (render-html-response
       [:div {:class "p-4"}
        [:p {:class "text-sm text-on-surface-muted"}
         "Search functionality will be integrated with Meilisearch."]])
      (render-html-response
       [:div {:class "p-4"}
        [:p {:class "text-sm text-on-surface-muted"}
         "Enter at least 3 characters to search."]]))))

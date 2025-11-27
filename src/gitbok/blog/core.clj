(ns gitbok.blog.core
  (:require [gitbok.blog.frontmatter :as frontmatter]
            [gitbok.markdown.core :as markdown]
            [gitbok.state :as state]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.tools.logging :as log]
            [clj-yaml.core :as yaml]))

(def articles-per-page 15)

(defn get-blog-dir
  "Get blog directory path from docs-volume-path."
  [context]
  (let [vol-path (or (state/get-config context :docs-volume-path) "docs-new")]
    (str vol-path "/blog")))

(defn load-nav-config
  "Load navigation dropdown config from blog/dropdown.yaml"
  [context]
  (let [blog-path (get-blog-dir context)
        file (io/file blog-path "dropdown.yaml")]
    (when (.exists file)
      (try
        (yaml/parse-string (slurp file))
        (catch Exception e
          (log/error e "Failed to load blog nav config")
          nil)))))

(defn- list-article-folders
  "List all article folders (containing index.md) in the blog directory."
  [context]
  (let [dir (io/file (get-blog-dir context))]
    (if (.exists dir)
      (->> (.listFiles dir)
           (filter #(.isDirectory %))
           (filter #(.exists (io/file % "index.md")))
           (map #(.getPath (io/file % "index.md")))
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

(defn- remove-first-h1
  "Remove the first h1 heading from markdown content (title is shown in hero)."
  [content]
  ;; Remove first line that starts with # (single #, not ##)
  (str/replace-first content #"(?m)^# .+\n?" ""))

(defn- resolve-image-paths
  "Convert relative image paths to absolute paths with blog folder prefix.
   Example: ![alt](image.jpeg) -> ![alt](/blog/static/img/folder-name/image.jpeg)"
  [content folder-name]
  ;; Match ![alt](path) where path doesn't start with http or /
  (str/replace content
               #"!\[([^\]]*)\]\(([^)]+)\)"
               (fn [[_ alt path]]
                 (if (or (str/starts-with? path "http")
                         (str/starts-with? path "/"))
                   (str "![" alt "](" path ")")
                   (str "![" alt "](/blog/static/img/" folder-name "/" path ")")))))

(defn- parse-article
  "Parse article markdown file into metadata and rendered content."
  [context filepath]
  (log/debug "Parsing article" {:filepath filepath})
  (when-let [content (load-article-content filepath)]
    (let [;; Skip link resolution for blog - no product index available
          ctx (assoc context :skip-link-resolution true)
          _ (log/debug "Context prepared" {:skip-link-resolution true})
          {:keys [metadata content]} (frontmatter/parse-frontmatter content)
          _ (log/debug "Frontmatter parsed" {:title (:title metadata)})
          ;; Get folder name as slug (e.g., blog/my-article/index.md -> my-article)
          folder-name (-> filepath
                          (io/file)
                          (.getParentFile)
                          (.getName))
          ;; Remove first h1 since title is shown in hero section
          content-without-h1 (remove-first-h1 content)
          ;; Resolve relative image paths to absolute
          content-with-images (resolve-image-paths content-without-h1 folder-name)
          slug (or (:slug metadata) folder-name)
          ;; Resolve cover image path if it's a relative path
          image (when-let [img (:image metadata)]
                  (if (str/starts-with? img "http")
                    img
                    (str "/blog/static/img/" folder-name "/" img)))
          _ (log/debug "Starting markdown parse" {:slug slug})
          parsed (markdown/parse-markdown-content ctx [filepath content-with-images])
          _ (log/debug "Markdown parsed, starting render" {:slug slug})
          rendered (markdown/render-md ctx filepath (:parsed parsed))
          _ (log/debug "Article rendered" {:slug slug})]
      {:slug slug
       :filepath filepath
       :folder folder-name
       :metadata (assoc metadata :slug slug :image image)
       :parsed parsed
       :rendered rendered})))

(defn category-to-slug
  "Convert category name to URL slug (lowercase, spaces to hyphens)."
  [category]
  (when category
    (-> category
        str/lower-case
        (str/replace #"\s+" "-")
        (str/replace #"[^a-z0-9-]" ""))))

(defn- extract-all-categories
  "Extract unique categories from all articles."
  [articles]
  (->> articles
       (map (comp :category :metadata))
       (remove str/blank?)
       (set)))

(defn- build-category-mappings
  "Build bidirectional mappings between category names and slugs."
  [categories]
  (let [slug-to-name (into {} (map (fn [cat] [(category-to-slug cat) cat]) categories))
        name-to-slug (into {} (map (fn [cat] [cat (category-to-slug cat)]) categories))]
    {:slug-to-name slug-to-name
     :name-to-slug name-to-slug}))

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
  (log/info "Building blog cache..." {:blog-dir (get-blog-dir context)})
  (let [files (list-article-folders context)
        _ (log/info "Found blog articles" {:count (count files)})
        articles (doall
                  (keep (fn [filepath]
                          (try
                            (log/debug "Processing" {:file filepath})
                            (parse-article context filepath)
                            (catch Exception e
                              (log/error e "Failed to parse article" {:filepath filepath})
                              nil)))
                        files))
        _ (log/info "Articles parsed" {:count (count articles)})
        articles-map (into {} (map (juxt :slug identity) articles))
        index (build-article-index articles)
        tags (extract-all-categories articles)
        category-mappings (build-category-mappings tags)]
    (log/info "Blog cache built" {:articles (count articles) :tags (count tags)})
    {:articles articles-map
     :index index
     :tags tags
     :category-mappings category-mappings}))

(defn initialize-blog-cache!
  "Initialize blog cache at application startup."
  [context]
  (let [cache (build-cache context)
        nav-config (load-nav-config context)]
    (state/set-cache! context :blog cache)
    (state/set-cache! context :blog-nav nav-config)
    (log/info "Blog nav config loaded" {:items (count (:nav-items nav-config))})
    cache))

(defn reload-blog-cache!
  "Reload blog cache (for dev mode)."
  [context]
  (initialize-blog-cache! context))

(defn get-nav-items
  "Get navigation items from cache."
  [context]
  (when-let [nav-config (state/get-cache context :blog-nav)]
    (:nav-items nav-config)))

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

(defn get-category-by-slug
  "Get category name by its slug."
  [context slug]
  (when-let [cache (get-blog-cache context)]
    (get-in cache [:category-mappings :slug-to-name slug])))

(defn get-slug-by-category
  "Get slug by category name."
  [context category]
  (when-let [cache (get-blog-cache context)]
    (get-in cache [:category-mappings :name-to-slug category])))

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
    (filter #(= tag (:category %)) articles)))

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

(defn get-featured-slug
  "Get featured article slug from blog nav config."
  [context]
  (when-let [nav-config (state/get-cache context :blog-nav)]
    (:featured-article nav-config)))

(defn get-articles-for-listing
  "Get filtered articles for blog listing page (no pagination).
   Featured article (from config) is always shown first."
  [context {:keys [tag]}]
  (let [index (get-article-index context)
        filtered (filter-by-tag index tag)
        featured-slug (get-featured-slug context)
        ;; Reorder: featured first, then rest sorted by date
        articles (if featured-slug
                   (let [featured (first (filter #(= (:slug %) featured-slug) filtered))
                         rest-articles (remove #(= (:slug %) featured-slug) filtered)]
                     (if featured
                       (vec (cons featured rest-articles))
                       (vec filtered)))
                   (vec filtered))]
    {:articles articles
     :tag tag
     :all-tags (get-all-tags context)}))

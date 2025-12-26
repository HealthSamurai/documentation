(ns gitbok.blog.ui
  (:require [hiccup2.core]
            [clojure.string :as str]
            [gitbok.ui.tags :as tags]
            [gitbok.ui.llm-share :as llm-share]
            [gitbok.ui.blog-subscribe :as blog-subscribe]
            [gitbok.blog.core :as blog]))

;; Font family from health-samurai.io
(def font-family "'Gotham Pro', Arial, sans-serif")

(defn format-date
  "Format date from YYYY-MM-DD to 'Month day, year'"
  [date-str]
  (when date-str
    (try
      (let [[year month day] (str/split date-str #"-")
            month-names ["January" "February" "March" "April" "May" "June"
                         "July" "August" "September" "October" "November" "December"]
            month-idx (dec (Integer/parseInt month))
            day-num (Integer/parseInt day)]
        (str (nth month-names month-idx) " " day-num ", " year))
      (catch Exception _
        date-str))))

(defn format-date-short
  "Format date from YYYY-MM-DD to 'Mon DD, YYYY' (for display)"
  [date-str]
  (when date-str
    (try
      (let [[year month day] (str/split date-str #"-")
            month-names ["Jan" "Feb" "Mar" "Apr" "May" "Jun"
                         "Jul" "Aug" "Sep" "Oct" "Nov" "Dec"]
            month-idx (dec (Integer/parseInt month))]
        (str (nth month-names month-idx) " " day ", " year))
      (catch Exception _
        date-str))))

(defn format-date-iso
  "Convert date to full ISO 8601 format with timezone for JSON-LD.
   YYYY-MM-DD -> YYYY-MM-DDT00:00:00Z
   Already full ISO -> pass through"
  [date-str]
  (when date-str
    (if (re-matches #"\d{4}-\d{2}-\d{2}" date-str)
      (str date-str "T00:00:00Z")
      date-str)))

(defn filter-bar
  "Render category filter bar"
  [{:keys [all-tags tag]}]
  [:div {:class "mb-8"}
   [:div {:class "flex flex-wrap gap-2"}
    ;; All filter
    [:a {:href (str blog/url-prefix "/blog")
         :class (str "px-4 py-2 rounded-lg transition-colors no-underline "
                     (if (nil? tag)
                       "bg-brand text-white"
                       "bg-surface-subtle text-on-surface-strong hover:bg-surface-hover"))}
     "All"]

    ;; Tag filters
    (for [t (sort all-tags)]
      [:a {:href (str blog/url-prefix "/article-categories/" (blog/category-to-slug t))
           :class (str "px-4 py-2 rounded-lg transition-colors no-underline "
                       (if (= t tag)
                         "bg-brand text-white"
                         "bg-surface-subtle text-on-surface-strong hover:bg-surface-hover"))}
       t])]])

(defn date-with-symbol
  "Render date with // symbol prefix like production"
  [published]
  [:div {:class "flex items-center text-on-surface text-base"}
   [:span {:class "text-brand mr-2 font-normal"} "//"]
   [:span (format-date published)]])

(defn author-link
  "Render clickable author name"
  [author]
  (when author
    (let [slug (blog/author-to-slug author)]
      [:a {:href (str blog/url-prefix "/authors/" slug)
           :class "hover:underline hover:text-brand transition-colors"}
       author])))

(defn author-badge
  "Render author with small photo and clickable name.
   Stops event propagation to allow clicking inside card."
  [context author-name]
  (when author-name
    (let [author-info (blog/get-author-by-name context author-name)
          slug (blog/author-to-slug author-name)]
      [:a {:href (str blog/url-prefix "/authors/" slug)
           :class "flex items-center gap-2 hover:text-brand transition-colors"
           :onclick "event.stopPropagation()"}
       (when (:image author-info)
         [:img {:src (:image author-info)
                :alt author-name
                :class "w-6 h-6 rounded-full object-cover flex-shrink-0"}])
       [:span author-name]])))

(defn article-featured
  "Render featured article (horizontal layout like bg-article-1)"
  [context {:keys [slug title teaser published image author category]}]
  [:div {:class "flex flex-col md:flex-row gap-4 md:gap-8 mb-8 md:mb-12 cursor-pointer group"
         :style {:font-family font-family}
         :onclick (str "window.location.href='" blog/url-prefix "/articles/" slug "'")}
   ;; Image (larger for featured, responsive with preserved aspect ratio)
   (when image
     [:div {:class "overflow-hidden rounded-xl flex-shrink-0 w-full md:w-[400px] aspect-[20/13]"}
      [:img {:src image
             :alt title
             :class "w-full h-full object-cover transition-transform group-hover:scale-105"
             :loading "lazy"}]])

   ;; Content
   [:div {:class "flex flex-col justify-center"}
    ;; Date with symbol
    (date-with-symbol published)

    ;; Title
    [:h3 {:class "transition-colors text-on-surface-strong group-hover:text-brand text-[28px] font-bold leading-9 my-3"}
     title]

    ;; Teaser
    (when teaser
      [:p {:class "text-on-surface-muted text-base leading-6 mt-2"}
       teaser])

    ;; Author + Tags
    [:div {:class "flex items-center flex-wrap gap-2 text-on-surface-muted text-base font-medium mt-3"}
     (when author
       (author-badge context author))
     (when category
       (tags/render-tags [category] :default))]]])

(defn article-card
  "Render a single article card (for 2 side-by-side after featured)"
  [context {:keys [slug title teaser published image author category]}]
  [:div {:class "flex flex-col cursor-pointer group py-6 md:py-8"
         :style {:font-family font-family}
         :onclick (str "window.location.href='" blog/url-prefix "/articles/" slug "'")}
   ;; Image (responsive with preserved aspect ratio 220:170 ≈ 22:17)
   (when image
     [:div {:class "mb-4 overflow-hidden rounded-xl w-full md:max-w-[220px] aspect-[22/17]"}
      [:img {:src image
             :alt title
             :class "w-full h-full object-cover transition-transform group-hover:scale-105"
             :loading "lazy"}]])

   ;; Date with symbol
   [:div {:class "mb-2"}
    (date-with-symbol published)]

   ;; Title
   [:h3 {:class "transition-colors text-on-surface-strong group-hover:text-brand text-[22px] font-bold leading-7 my-2.5"}
    title]

   ;; Teaser (optional)
   (when teaser
     [:p {:class "text-on-surface-muted text-base leading-6 mt-2"}
      teaser])

   ;; Author + Tags
   [:div {:class "flex items-center flex-wrap gap-2 text-on-surface-muted text-base font-medium mt-3"}
    (when author
      (author-badge context author))
    (when category
      (tags/render-tags [category] :default))]])

(defn article-card-list
  "Render article as a simple list item with image on the right"
  [context {:keys [slug title teaser published author image category]}]
  [:div {:class "flex flex-col-reverse sm:flex-row gap-4 sm:gap-6 py-6 border-b border-outline last:border-0 cursor-pointer group"
         :style {:font-family font-family}
         :onclick (str "window.location.href='" blog/url-prefix "/articles/" slug "'")}
   ;; Content (left)
   [:div {:class "flex-1"}
    ;; Date with symbol
    [:div {:class "mb-2"}
     (date-with-symbol published)]

    ;; Title
    [:h3 {:class "transition-colors text-on-surface-strong group-hover:text-brand text-xl font-bold leading-[26px] my-1"}
     title]

    ;; Teaser (optional)
    (when teaser
      [:p {:class "text-on-surface-muted text-base leading-6 mt-2"}
       teaser])

    ;; Author + Tags
    [:div {:class "flex items-center flex-wrap gap-2 text-on-surface-muted text-base font-medium mt-2"}
     (when author
       (author-badge context author))
     (when category
       (tags/render-tags [category] :default))]]

   ;; Image (top on mobile with full width, right on sm+ with fixed width)
   (when image
     [:div {:class "overflow-hidden rounded-xl w-full sm:w-[120px] md:w-[150px] sm:flex-shrink-0 aspect-[3/2]"}
      [:img {:src image
             :alt title
             :class "w-full h-full object-cover transition-transform group-hover:scale-105"
             :loading "lazy"}]])])

(defn pagination-controls
  "Render pagination controls"
  [{:keys [page total-pages has-prev has-next tag]}]
  (when (> total-pages 1)
    [:div {:class "flex justify-center items-center gap-4 mt-12"
           :style {:font-family font-family}}
     ;; Previous button
     (if has-prev
       [:a {:href (str blog/url-prefix "/blog?page=" (dec page) (when tag (str "&tag=" (java.net.URLEncoder/encode tag "UTF-8"))))
            :class "px-4 py-2 rounded-lg bg-surface-subtle text-on-surface-strong hover:bg-surface-hover transition-colors no-underline"}
        "← Previous"]
       [:span {:class "px-4 py-2 text-on-surface-muted"} "← Previous"])

     ;; Page indicator
     [:span {:class "text-on-surface"}
      (str "Page " page " of " (int total-pages))]

     ;; Next button
     (if has-next
       [:a {:href (str blog/url-prefix "/blog?page=" (inc page) (when tag (str "&tag=" (java.net.URLEncoder/encode tag "UTF-8"))))
            :class "px-4 py-2 rounded-lg bg-surface-subtle text-on-surface-strong hover:bg-surface-hover transition-colors no-underline"}
        "Next →"]
       [:span {:class "px-4 py-2 text-on-surface-muted"} "Next →"])]))

(defn topics-sidebar
  "Render Topics sidebar like health-samurai.io/blog"
  [{:keys [current-tag all-tags]}]
  (when (seq all-tags)
    [:aside {:class "hidden lg:block"
             :style {:width "262px"
                     :flex-shrink "0"
                     :margin-left "48px"}}
     ;; Topics heading
     [:h4 {:class "text-on-surface-strong text-lg font-bold mb-4"
           :style {:font-family font-family}}
      "Topics"]
     ;; Topics list using tags component
     [:div {:class "flex flex-wrap gap-2"}
      (for [tag (sort all-tags)]
        ^{:key tag}
        (tags/render-tag {:text tag
                          :href (str blog/url-prefix "/article-categories/" (blog/category-to-slug tag))
                          :variant :default}))]]))

(defn blog-listing-page
  "Render the main blog listing page matching health-samurai.io/blog style"
  [context listing-data]
  (let [{:keys [articles all-tags tag page total-pages has-prev has-next]} listing-data
        page (or page 1)
        first-page? (= page 1)]
    [:div {:class "min-h-screen flex flex-col"
           :style {:font-family font-family}}
     ;; Hidden h1 for page title (used by JS updatePageTitle)
     [:h1 {:class "sr-only"} "Health Samurai Blog"]
     ;; Main content - full width with responsive padding
     [:main {:class "flex-1 w-full pt-8 md:pt-12 px-4 md:px-8"}
      ;; Two-column layout: articles + sidebar (max-width slightly larger than header's 1200px)
      [:div {:class "flex mx-auto"
             :style {:max-width "1280px"}}
       ;; Articles column
       [:div {:class "flex-1 min-w-0"}
        (if (seq articles)
          (if first-page?
            ;; Page 1: Featured layout
            (let [featured (first articles)
                  next-two (take 2 (rest articles))
                  rest-articles (drop 3 articles)]
              [:div
               ;; Featured article (horizontal layout)
               (when featured
                 (article-featured context featured))
               ;; Next 2 articles side by side
               (when (seq next-two)
                 [:div {:class "grid grid-cols-1 md:grid-cols-2 gap-8 border-t border-outline"}
                  (for [article next-two]
                    ^{:key (:slug article)}
                    (article-card context article))])
               ;; Subscribe section after first 3 articles (like health-samurai.io/blog)
               (blog-subscribe/subscribe-section {:with-margin? true
                                                  :email-id "EMAIL-2"})
               ;; Rest of articles as vertical list
               (when (seq rest-articles)
                 [:div {:class "mt-4"}
                  (for [article rest-articles]
                    ^{:key (:slug article)}
                    (article-card-list context article))])
               ;; Pagination controls
               (pagination-controls {:page page
                                     :total-pages total-pages
                                     :has-prev has-prev
                                     :has-next has-next
                                     :tag tag})])
            ;; Page 2+: Simple vertical list
            [:div
             (for [article articles]
               ^{:key (:slug article)}
               (article-card-list context article))
             ;; Pagination controls
             (pagination-controls {:page page
                                   :total-pages total-pages
                                   :has-prev has-prev
                                   :has-next has-next
                                   :tag tag})])
          ;; No articles message
          [:div {:class "text-center py-12"}
           [:p {:class "text-on-surface-muted text-lg"}
            "No articles found."]])]
       ;; Topics sidebar
       (topics-sidebar {:current-tag tag :all-tags all-tags})]]]))

(defn category-listing-page
  "Render category page with breadcrumbs, large title and vertical list"
  [context {:keys [articles all-tags tag category-slug]}]
  [:div {:class "min-h-screen flex flex-col"
         :style {:font-family font-family}}
   [:main {:class "flex-1 w-full pt-8 md:pt-12 px-4 md:px-8"}
    [:div {:class "flex mx-auto"
           :style {:max-width "1280px"}}
     ;; Articles column
     [:div {:class "flex-1 min-w-0"}
      ;; Breadcrumbs
      [:div {:class "flex items-center gap-2 mb-4 text-on-surface-muted text-base"}
       [:a {:href (str blog/url-prefix "/blog")
            :class "text-brand no-underline hover:underline"}
        "Articles"]
       [:span "/"]
       [:span tag]]

      ;; Large category title (responsive)
      [:h1 {:class "text-3xl md:text-5xl font-black mb-6 md:mb-8 text-on-surface-strong leading-tight"}
       tag]

      ;; All articles as vertical list (no featured)
      (if (seq articles)
        [:div
         (for [article articles]
           ^{:key (:slug article)}
           (article-card-list context article))]
        [:div {:class "text-center py-12"}
         [:p {:class "text-on-surface-muted text-lg"}
          "No articles found."]])]

     ;; Topics sidebar
     (topics-sidebar {:current-tag tag :all-tags all-tags})]]])

(defn author-listing-page
  "Render author page with photo, name and their articles"
  [context {:keys [articles all-tags author]}]
  [:div {:class "min-h-screen flex flex-col"
         :style {:font-family font-family}}
   [:main {:class "flex-1 w-full pt-8 md:pt-12 px-4 md:px-8"}
    [:div {:class "flex mx-auto"
           :style {:max-width "1280px"}}
     ;; Articles column
     [:div {:class "flex-1 min-w-0"}
      ;; Breadcrumbs
      [:div {:class "flex items-center gap-2 mb-4 text-on-surface-muted text-base"}
       [:a {:href (str blog/url-prefix "/blog")
            :class "text-brand no-underline hover:underline"}
        "Articles"]
       [:span "/"]
       [:span (:name author)]]

      ;; Author header with photo
      [:div {:class "flex items-center gap-6 mb-8"}
       (when (:image author)
         [:img {:src (:image author)
                :alt (:name author)
                :class "w-24 h-24 rounded-full object-cover flex-shrink-0"}])
       [:h1 {:class "text-3xl md:text-5xl font-black text-on-surface-strong leading-tight"}
        (:name author)]]

      ;; All articles as vertical list
      (if (seq articles)
        [:div
         (for [article articles]
           ^{:key (:slug article)}
           (article-card-list context article))]
        [:div {:class "text-center py-12"}
         [:p {:class "text-on-surface-muted text-lg"}
          "No articles found."]])]

     ;; Topics sidebar
     (topics-sidebar {:all-tags all-tags})]]])

(defn article-hero
  "Full-width hero section for article page (like production)"
  [context {:keys [category title published author reading-time]}]
  ;; Full-width background with responsive padding
  [:div {:class "w-full py-8 md:py-12 px-4 md:px-5 bg-surface-alt"
         :style {:font-family font-family}}
   ;; Inner container (centered, max-width)
   [:div {:class "mx-auto max-w-[800px]"}
    ;; Breadcrumb: Articles / Category
    [:div {:class "flex items-center gap-2 text-on-surface-muted text-base"}
     [:a {:href (str blog/url-prefix "/blog")
          :class "text-brand no-underline hover:underline"}
      "Articles"]
     [:span "/"]
     (when category
       [:a {:href (str blog/url-prefix "/article-categories/" (blog/category-to-slug category))
            :class "text-on-surface-muted no-underline hover:underline"}
        category])]

    ;; Title - big and bold (responsive)
    [:h1 {:class "text-3xl md:text-5xl font-black my-4 md:my-6 text-on-surface-strong leading-tight"}
     title]

    ;; Author, date, reading time
    [:div {:class "flex items-center gap-3 text-on-surface-muted text-base"}
     (when author
       [:span {:class "font-medium"}
        (author-badge context author)])
     (when published
       [:span (format-date published)])
     (when reading-time
       [:span reading-time])]]])

(defn article-page
  "Render individual article page"
  [context article]
  (let [{:keys [metadata rendered]} article
        article-url (str "https://www.health-samurai.io" blog/url-prefix "/articles/" (:slug metadata))]
    [:div {:class "min-h-screen flex flex-col"
           :style {:font-family font-family}}
     ;; Hero section (full-width with background)
     (article-hero context metadata)

     ;; Article content (centered, narrower, responsive padding)
     [:main {:class "flex-1 w-full mx-auto py-8 md:py-12 px-4 md:px-0 max-w-[800px]"}
      ;; LLM sharing block
      (llm-share/share-block article-url)

      [:article {:class "prose prose-lg max-w-none
                         prose-headings:text-on-surface-strong
                         prose-p:text-on-surface-muted
                         prose-a:text-brand prose-a:no-underline hover:prose-a:underline
                         prose-strong:text-on-surface-strong
                         prose-code:text-brand prose-code:bg-surface-subtle prose-code:px-1 prose-code:py-0.5 prose-code:rounded
                         prose-pre:bg-surface-subtle prose-pre:border prose-pre:border-outline
                         prose-blockquote:border-l-brand prose-blockquote:text-on-surface-muted
                         prose-img:rounded-lg"
               :style {:font-family font-family}}
       rendered]

      ;; Social share buttons (sticky on the right side)
      [:script (hiccup2.core/raw
                (str "
(function() {
  function initShareThis() {
    if (window.__sharethis__ && window.__sharethis__.load) {
      window.__sharethis__.load('sticky-share-buttons', {
        alignment: 'right',
        enabled: true,
        labels: 'counts',
        min_count: 0,
        networks: ['facebook', 'twitter', 'email', 'sharethis', 'whatsapp', 'telegram', 'linkedin'],
        show_total: true,
        show_mobile: true,
        radius: 4,
        size: 48,
        top: 200,
        url: '" article-url "',
        title: '" (str/escape (:title metadata) {\" "\\\"" \\ "\\\\"}) "'
      });
    } else {
      setTimeout(initShareThis, 100);
    }
  }
  if (document.readyState === 'complete') {
    initShareThis();
  } else {
    window.addEventListener('load', initShareThis);
  }
})();
"))]

      ;; Comentario script loader
      [:script (hiccup2.core/raw "
(function() {
  var comentarioUrl = window.location.hostname === 'localhost'
    ? 'http://localhost:8091/comentario.js'
    : window.location.origin + '/docs/futureblog/comentario/comentario.js';
  var script = document.createElement('script');
  script.src = comentarioUrl;
  script.defer = true;
  document.head.appendChild(script);
})();")]

      ;; Comentario comments section
      [:div {:class "mt-12 pt-8 border-t border-outline"}
       [:h2 {:class "text-2xl font-bold mb-6 text-on-surface-strong"} "Comments"]
       [:div {:id "comentario-container"}]]

      ;; Create Comentario widget with correct initial theme
      [:script (hiccup2.core/raw "
(function() {
  var isDark = document.documentElement.classList.contains('dark');
  var container = document.getElementById('comentario-container');
  var widget = document.createElement('comentario-comments');
  widget.id = 'comentario-widget';
  widget.setAttribute('theme', isDark ? 'dark' : 'light');
  container.appendChild(widget);
})();")]

      ;; Comentario theme sync - watch for theme changes
      [:script (hiccup2.core/raw "
(function() {
  function updateComentarioTheme() {
    var widget = document.getElementById('comentario-widget');
    if (!widget) return;

    var isDark = document.documentElement.classList.contains('dark');
    var newTheme = isDark ? 'dark' : 'light';

    // Only update if theme changed
    if (widget.getAttribute('theme') !== newTheme) {
      widget.setAttribute('theme', newTheme);
    }
  }

  // Watch for theme changes
  var observer = new MutationObserver(function(mutations) {
    mutations.forEach(function(mutation) {
      if (mutation.attributeName === 'class') {
        updateComentarioTheme();
      }
    });
  });
  observer.observe(document.documentElement, { attributes: true });
})();")]

      ;; Back to blog link
      [:div {:class "mt-12 pt-8 border-t border-outline"}
       [:a {:href (str blog/url-prefix "/blog")
            :class "text-brand hover:underline"}
        "← Back to blog"]]]]))

(defn article-json-ld
  "Generate JSON-LD structured data for article SEO"
  [{:keys [title teaser image category tags published author reading-time]} article-url]
  (let [base-url "https://www.health-samurai.io"]
    [;; WebPage with breadcrumb and mainEntity Article
     {"@context" "http://schema.org"
      "@type" "WebPage"
      "breadcrumb" {"@type" "BreadcrumbList"
                    "itemListElement" [{"@type" "ListItem"
                                        "position" 1
                                        "name" "Articles"
                                        "item" (str base-url blog/url-prefix "/articles")}
                                       {"@type" "ListItem"
                                        "position" 2
                                        "name" title}]}
      "mainEntity" {"@type" "Article"
                    "name" title
                    "headline" title
                    "image" (when image
                              (if (str/starts-with? image "http")
                                image
                                (str base-url image)))
                    "genre" category
                    "keywords" (when (seq tags) (str/join ", " tags))
                    "url" article-url
                    "datePublished" (format-date-iso published)
                    "dateCreated" (format-date-iso published)
                    "dateModified" (format-date-iso published)
                    "description" teaser
                    "timeRequired" reading-time
                    "author" {"@type" "Person"
                              "name" author}
                    "interactionStatistic" [{"@type" "InteractionCounter"
                                             "interactionType" "https://schema.org/ShareAction"
                                             "userInteractionCount" "249"}]}}
     ;; AggregateRating
     {"@context" "https://schema.org"
      "@type" "AggregateRating"
      "ratingValue" 10
      "bestRating" 10
      "worstRating" 1
      "ratingCount" 122
      "itemReviewed" {"@type" "Article"
                      "name" title
                      "url" article-url}}]))

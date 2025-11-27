(ns gitbok.blog.ui
  (:require [hiccup2.core]
            [clojure.string :as str]
            [gitbok.ui.tags :as tags]
            [gitbok.blog.core :as blog]))

;; Font family from health-samurai.io
(def font-family "'Gotham Pro', Arial, sans-serif")
(def text-color "rgb(53, 59, 80)")
(def text-muted "rgba(53, 59, 80, 0.8)")
(def primary-red "#EA4A35")

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
  "Format date from YYYY-MM-DD to 'Mon DD, YYYY' (for JSON-LD)"
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

(defn filter-bar
  "Render category filter bar"
  [{:keys [all-tags tag]}]
  [:div {:class "mb-8"}
   [:div {:class "flex flex-wrap gap-2"}
    ;; All filter
    [:a {:href "/blog"
         :class (str "px-4 py-2 rounded-lg transition-colors no-underline "
                     (if (nil? tag)
                       "bg-brand text-white"
                       "bg-surface-subtle text-on-surface-strong hover:bg-surface-hover"))}
     "All"]

    ;; Tag filters
    (for [t (sort all-tags)]
      [:a {:href (str "/article-categories/" (blog/category-to-slug t))
           :class (str "px-4 py-2 rounded-lg transition-colors no-underline "
                       (if (= t tag)
                         "bg-brand text-white"
                         "bg-surface-subtle text-on-surface-strong hover:bg-surface-hover"))}
       t])]])

(defn date-with-symbol
  "Render date with // symbol prefix like production"
  [published]
  [:div {:class "flex items-center"
         :style {:font-size "16px"
                 :color text-color}}
   [:span {:style {:color primary-red
                   :margin-right "8px"
                   :font-weight "400"}}
    "//"]
   [:span (format-date published)]])

(defn article-featured
  "Render featured article (horizontal layout like bg-article-1)"
  [{:keys [slug title teaser published image author category]}]
  [:a {:href (str "/articles/" slug)
       :class "flex flex-row gap-8 mb-12 no-underline group"
       :style {:font-family font-family}}
   ;; Image (larger for featured)
   (when image
     [:div {:class "overflow-hidden rounded-xl flex-shrink-0"
            :style {:width "400px"
                    :height "260px"}}
      [:img {:src image
             :alt title
             :class "w-full h-full object-cover transition-transform group-hover:scale-105"
             :loading "lazy"}]])

   ;; Content
   [:div {:class "flex flex-col justify-center"}
    ;; Date with symbol
    (date-with-symbol published)

    ;; Title
    [:h3 {:class "transition-colors text-[rgb(53,59,80)] group-hover:text-[#EA4A35]"
          :style {:font-size "28px"
                  :font-weight "700"
                  :line-height "36px"
                  :margin "12px 0"}}
     title]

    ;; Teaser
    (when teaser
      [:p {:style {:font-size "16px"
                   :color text-muted
                   :line-height "24px"
                   :margin-top "8px"}}
       teaser])

    ;; Author + Tags
    [:div {:class "flex items-center flex-wrap gap-2"
           :style {:font-size "16px"
                   :color text-muted
                   :font-weight "500"
                   :margin-top "12px"}}
     (when author
       [:span author])
     (when category
       (tags/render-tags [category] :default))]]])

(defn article-card
  "Render a single article card (for 2 side-by-side after featured)"
  [{:keys [slug title teaser published image author category]}]
  [:a {:href (str "/articles/" slug)
       :class "flex flex-col no-underline group"
       :style {:padding "32px 0"
               :font-family font-family}}
   ;; Image (with fixed aspect ratio container ~220x170 like production)
   (when image
     [:div {:class "mb-4 overflow-hidden rounded-xl"
            :style {:height "170px"
                    :max-width "220px"}}
      [:img {:src image
             :alt title
             :class "w-full h-full object-cover transition-transform group-hover:scale-105"
             :loading "lazy"}]])

   ;; Date with symbol
   [:div {:class "mb-2"}
    (date-with-symbol published)]

   ;; Title
   [:h3 {:class "transition-colors text-[rgb(53,59,80)] group-hover:text-[#EA4A35]"
         :style {:font-size "22px"
                 :font-weight "700"
                 :line-height "28px"
                 :margin "10px 0"}}
    title]

   ;; Teaser (optional)
   (when teaser
     [:p {:style {:font-size "16px"
                  :color text-muted
                  :line-height "24px"
                  :margin-top "8px"}}
      teaser])

   ;; Author + Tags
   [:div {:class "flex items-center flex-wrap gap-2"
          :style {:font-size "16px"
                  :color text-muted
                  :font-weight "500"
                  :margin-top "12px"}}
    (when author
      [:span author])
    (when category
      (tags/render-tags [category] :default))]])

(defn article-card-list
  "Render article as a simple list item with image on the right"
  [{:keys [slug title teaser published author image category]}]
  [:a {:href (str "/articles/" slug)
       :class "flex gap-6 py-6 border-b border-outline last:border-0 no-underline group"
       :style {:font-family font-family}}
   ;; Content (left)
   [:div {:class "flex-1"}
    ;; Date with symbol
    [:div {:class "mb-2"}
     (date-with-symbol published)]

    ;; Title
    [:h3 {:class "transition-colors text-[rgb(53,59,80)] group-hover:text-[#EA4A35]"
          :style {:font-size "20px"
                  :font-weight "700"
                  :line-height "26px"
                  :margin "4px 0"}}
     title]

    ;; Teaser (optional)
    (when teaser
      [:p {:style {:font-size "16px"
                   :color text-muted
                   :line-height "24px"
                   :margin-top "8px"}}
       teaser])

    ;; Author + Tags
    [:div {:class "flex items-center flex-wrap gap-2"
           :style {:font-size "16px"
                   :color text-muted
                   :font-weight "500"
                   :margin-top "8px"}}
     (when author
       [:span author])
     (when category
       (tags/render-tags [category] :default))]]

   ;; Image (right)
   (when image
     [:div {:class "overflow-hidden rounded-xl flex-shrink-0"
            :style {:width "150px"
                    :height "100px"}}
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
       [:a {:href (str "/blog?page=" (dec page) (when tag (str "&tag=" (java.net.URLEncoder/encode tag "UTF-8"))))
            :class "px-4 py-2 rounded-lg bg-surface-subtle text-on-surface-strong hover:bg-surface-hover transition-colors no-underline"}
        "← Previous"]
       [:span {:class "px-4 py-2 text-on-surface-muted"} "← Previous"])

     ;; Page indicator
     [:span {:style {:color text-color}}
      (str "Page " page " of " (int total-pages))]

     ;; Next button
     (if has-next
       [:a {:href (str "/blog?page=" (inc page) (when tag (str "&tag=" (java.net.URLEncoder/encode tag "UTF-8"))))
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
     [:h4 {:style {:font-size "18px"
                   :font-weight "700"
                   :color "rgb(51, 51, 51)"
                   :margin-bottom "16px"
                   :font-family font-family}}
      "Topics"]
     ;; Topics list using tags component
     [:div {:class "flex flex-wrap gap-2"}
      (for [tag (sort all-tags)]
        ^{:key tag}
        (tags/render-tag {:text tag
                          :href (str "/article-categories/" (blog/category-to-slug tag))
                          :variant :default}))]]))

(defn blog-listing-page
  "Render the main blog listing page matching health-samurai.io/blog style"
  [context listing-data]
  (let [{:keys [articles all-tags tag page total-pages has-prev has-next]} listing-data]
    [:div {:class "min-h-screen flex flex-col"
           :style {:font-family font-family}}
     ;; Main content - full width with padding (wider than header)
     [:main {:class "flex-1 w-full"
             :style {:padding-top "48px"
                     :padding-left "32px"
                     :padding-right "32px"}}
      ;; Two-column layout: articles + sidebar (max-width slightly larger than header's 1200px)
      [:div {:class "flex mx-auto"
             :style {:max-width "1280px"}}
       ;; Articles column
       [:div {:class "flex-1 min-w-0"}
        (if (seq articles)
          (let [featured (first articles)
                next-two (take 2 (rest articles))
                rest-articles (drop 3 articles)]
            [:div
             ;; Featured article (horizontal layout)
             (when featured
               (article-featured featured))
             ;; Next 2 articles side by side
             (when (seq next-two)
               [:div {:class "grid grid-cols-1 md:grid-cols-2 gap-8 border-t border-outline"}
                (for [article next-two]
                  ^{:key (:slug article)}
                  (article-card article))])
             ;; Rest of articles as vertical list
             (when (seq rest-articles)
               [:div {:class "mt-4"}
                (for [article rest-articles]
                  ^{:key (:slug article)}
                  (article-card-list article))])])
          ;; No articles message
          [:div {:class "text-center py-12"}
           [:p {:style {:color text-muted :font-size "18px"}}
            "No articles found."]])]
       ;; Topics sidebar
       (topics-sidebar {:current-tag tag :all-tags all-tags})]]]))

(defn category-listing-page
  "Render category page with breadcrumbs, large title and vertical list"
  [context {:keys [articles all-tags tag category-slug]}]
  [:div {:class "min-h-screen flex flex-col"
         :style {:font-family font-family}}
   [:main {:class "flex-1 w-full"
           :style {:padding-top "48px"
                   :padding-left "32px"
                   :padding-right "32px"}}
    [:div {:class "flex mx-auto"
           :style {:max-width "1280px"}}
     ;; Articles column
     [:div {:class "flex-1 min-w-0"}
      ;; Breadcrumbs
      [:div {:class "flex items-center gap-2 mb-4"
             :style {:font-size "16px"
                     :color "rgba(53, 59, 80, 0.6)"}}
       [:a {:href "/blog"
            :class "no-underline hover:underline"
            :style {:color primary-red}}
        "Articles"]
       [:span "/"]
       [:span {:style {:color "rgba(53, 59, 80, 0.6)"}}
        tag]]

      ;; Large category title
      [:h1 {:style {:font-size "48px"
                    :font-weight "900"
                    :line-height "60px"
                    :color "rgb(53, 59, 80)"
                    :margin "0 0 32px 0"}}
       tag]

      ;; All articles as vertical list (no featured)
      (if (seq articles)
        [:div
         (for [article articles]
           ^{:key (:slug article)}
           (article-card-list article))]
        [:div {:class "text-center py-12"}
         [:p {:style {:color text-muted :font-size "18px"}}
          "No articles found."]])]

     ;; Topics sidebar
     (topics-sidebar {:current-tag tag :all-tags all-tags})]]])

(defn article-hero
  "Full-width hero section for article page (like production)"
  [{:keys [category title published author reading-time]}]
  ;; Full-width background
  [:div {:class "w-full"
         :style {:background-color "rgba(53, 59, 80, 0.05)"
                 :padding "48px 20px"
                 :font-family font-family}}
   ;; Inner container (centered, max-width)
   [:div {:class "mx-auto"
          :style {:max-width "800px"}}
    ;; Breadcrumb: Articles / Category
    [:div {:class "flex items-center gap-2"
           :style {:font-size "16px"
                   :color "rgba(53, 59, 80, 0.6)"}}
     [:a {:href "/blog"
          :class "no-underline hover:underline"
          :style {:color primary-red}}
      "Articles"]
     [:span "/"]
     (when category
       [:a {:href (str "/article-categories/" (blog/category-to-slug category))
            :class "no-underline hover:underline"
            :style {:color "rgba(53, 59, 80, 0.6)"}}
        category])]

    ;; Title - big and bold
    [:h1 {:style {:font-size "48px"
                  :font-weight "900"
                  :line-height "60px"
                  :color "rgb(53, 59, 80)"
                  :margin "24px 0"}}
     title]

    ;; Author, date, reading time
    [:div {:class "flex items-center gap-3"
           :style {:font-size "16px"
                   :color "rgba(53, 59, 80, 0.6)"}}
     (when author
       [:span {:style {:font-weight "500"}} author])
     (when published
       [:span (format-date published)])
     (when reading-time
       [:span reading-time])]]])

(defn article-page
  "Render individual article page"
  [context article]
  (let [{:keys [metadata rendered]} article]
    [:div {:class "min-h-screen flex flex-col"
           :style {:font-family font-family}}
     ;; Hero section (full-width with background)
     (article-hero metadata)

     ;; Article content (centered, narrower)
     [:main {:class "flex-1 w-full mx-auto py-12"
             :style {:max-width "800px"}}
      ;; Article content
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

      ;; Back to blog link
      [:div {:class "mt-12 pt-8 border-t border-outline"}
       [:a {:href "/blog"
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
                                        "item" (str base-url "/articles")}
                                       {"@type" "ListItem"
                                        "position" 2
                                        "name" title}]}
      "mainEntity" {"@type" "Article"
                    "name" title
                    "headline" title
                    "image" image
                    "genre" category
                    "keywords" (when (seq tags) (str/join ", " tags))
                    "url" article-url
                    "datePublished" (format-date-short published)
                    "dateCreated" (format-date-short published)
                    "dateModified" (format-date-short published)
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

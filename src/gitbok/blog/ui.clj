(ns gitbok.blog.ui
  (:require [hiccup2.core]
            [clojure.string :as str]))

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
      [:a {:href (str "/blog?tag=" (java.net.URLEncoder/encode t "UTF-8"))
           :class (str "px-4 py-2 rounded-lg transition-colors no-underline "
                       (if (= t tag)
                         "bg-brand text-white"
                         "bg-surface-subtle text-on-surface-strong hover:bg-surface-hover"))}
       t])]])

(defn article-card
  "Render a single article card"
  [{:keys [slug title teaser published author reading-time tags image]}]
  [:article {:class "mb-8 pb-8 border-b border-outline last:border-0"}
   ;; Image (if available)
   (when image
     [:a {:href (str "/articles/" slug)
          :class "block mb-4"}
      [:img {:src image
             :alt title
             :class "w-full h-48 object-cover rounded-lg"}]])

   ;; Date and reading time
   [:div {:class "flex items-center gap-2 text-sm text-on-surface-muted mb-2"}
    [:span published]
    (when reading-time
      [:<>
       [:span "•"]
       [:span reading-time]])]

   ;; Title
   [:h2 {:class "text-2xl font-bold mb-3"}
    [:a {:href (str "/articles/" slug)
         :class "text-on-surface-strong hover:text-brand transition-colors no-underline"}
     title]]

   ;; Teaser
   (when teaser
     [:p {:class "text-on-surface-muted mb-4"} teaser])

   ;; Author
   (when author
     [:div {:class "text-sm text-on-surface-strong mb-3"}
      author])

   ;; Tags
   (when (seq tags)
     [:div {:class "flex flex-wrap gap-2"}
      (for [t tags]
        [:a {:href (str "/blog?tag=" (java.net.URLEncoder/encode t "UTF-8"))
             :class "px-3 py-1 text-xs rounded-full bg-surface-subtle text-on-surface-strong hover:bg-surface-hover transition-colors no-underline"}
         t])])])

(defn pagination-controls
  "Render pagination controls"
  [{:keys [page total-pages has-prev has-next tag]}]
  (when (> total-pages 1)
    [:div {:class "flex justify-center items-center gap-4 mt-12"}
     ;; Previous button
     (if has-prev
       [:a {:href (str "/blog?page=" (dec page) (when tag (str "&tag=" (java.net.URLEncoder/encode tag "UTF-8"))))
            :class "px-4 py-2 rounded-lg bg-surface-subtle text-on-surface-strong hover:bg-surface-hover transition-colors no-underline"}
        "← Previous"]
       [:span {:class "px-4 py-2 text-on-surface-muted"} "← Previous"])

     ;; Page indicator
     [:span {:class "text-on-surface-strong"}
      (str "Page " page " of " total-pages)]

     ;; Next button
     (if has-next
       [:a {:href (str "/blog?page=" (inc page) (when tag (str "&tag=" (java.net.URLEncoder/encode tag "UTF-8"))))
            :class "px-4 py-2 rounded-lg bg-surface-subtle text-on-surface-strong hover:bg-surface-hover transition-colors no-underline"}
        "Next →"]
       [:span {:class "px-4 py-2 text-on-surface-muted"} "Next →"])]))

(defn blog-listing-page
  "Render the main blog listing page"
  [context listing-data]
  (let [{:keys [articles all-tags tag page total-pages has-prev has-next]} listing-data]
    [:div {:class "min-h-screen flex flex-col"}
     ;; Header will be added separately by blog-header component

     ;; Main content
     [:main {:class "flex-1 max-w-4xl mx-auto px-5 md:px-8 py-12 w-full"}
      [:h1 {:class "text-4xl font-bold mb-8 text-on-surface-strong"}
       "Blog"]

      ;; Filter bar
      (filter-bar {:all-tags all-tags :tag tag})

      ;; Articles list
      (if (seq articles)
        [:div {:class "space-y-8"}
         (for [article articles]
           (article-card article))

         ;; Pagination
         (pagination-controls {:page page
                               :total-pages total-pages
                               :has-prev has-prev
                               :has-next has-next
                               :tag tag})]

        ;; No articles message
        [:div {:class "text-center py-12"}
         [:p {:class "text-on-surface-muted text-lg"}
          "No articles found."]])]]))

(defn article-metadata
  "Render article metadata section"
  [{:keys [tags title published author reading-time]}]
  [:div {:class "mb-8"}
   ;; Tags
   (when (seq tags)
     [:div {:class "flex flex-wrap gap-2 mb-4"}
      (for [t tags]
        [:a {:href (str "/blog?tag=" (java.net.URLEncoder/encode t "UTF-8"))
             :class "px-3 py-1 text-sm rounded-full bg-brand/10 text-brand hover:bg-brand/20 transition-colors no-underline"}
         t])])

   ;; Title
   [:h1 {:class "text-4xl md:text-5xl font-bold mb-4 text-on-surface-strong"}
    title]

   ;; Author, date, reading time
   [:div {:class "flex flex-wrap items-center gap-3 text-sm text-on-surface-muted"}
    (when author
      [:span {:class "font-medium"} author])
    (when published
      [:<>
       [:span "•"]
       [:span published]])
    (when reading-time
      [:<>
       [:span "•"]
       [:span reading-time]])]])

(defn article-page
  "Render individual article page"
  [context article]
  (let [{:keys [metadata rendered]} article]
    [:div {:class "min-h-screen flex flex-col"}
     ;; Header will be added separately by blog-header component

     ;; Main content
     [:main {:class "flex-1 max-w-4xl mx-auto px-5 md:px-8 py-12 w-full"}
      ;; Metadata section
      (article-metadata metadata)

      ;; Article content
      [:article {:class "prose prose-lg max-w-none
                         prose-headings:text-on-surface-strong
                         prose-p:text-on-surface-muted
                         prose-a:text-brand prose-a:no-underline hover:prose-a:underline
                         prose-strong:text-on-surface-strong
                         prose-code:text-brand prose-code:bg-surface-subtle prose-code:px-1 prose-code:py-0.5 prose-code:rounded
                         prose-pre:bg-surface-subtle prose-pre:border prose-pre:border-outline
                         prose-blockquote:border-l-brand prose-blockquote:text-on-surface-muted
                         prose-img:rounded-lg"}
       rendered]

      ;; Back to blog link
      [:div {:class "mt-12 pt-8 border-t border-outline"}
       [:a {:href "/blog"
            :class "text-brand hover:underline"}
        "← Back to blog"]]]]))

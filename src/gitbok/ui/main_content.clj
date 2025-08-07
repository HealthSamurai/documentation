(ns gitbok.ui.main-content
  (:require
   [clojure.java.io :as io]
   [gitbok.indexing.core :as indexing]
   [gitbok.indexing.impl.file-to-uri :as file-to-uri]
   [clojure.string :as str]
   [system]
   [uui]
   [gitbok.markdown.core :as markdown]
   [gitbok.indexing.impl.summary :as summary]
   [gitbok.markdown.widgets.big-links :as big-links]
   [gitbok.markdown.widgets.headers :as headers]
   [gitbok.ui.right-toc :as right-toc]
   [gitbok.ui.breadcrumb :as breadcrumb]
   [gitbok.utils :as utils]
   [gitbok.products :as products]
   [gitbok.http]
   [hiccup2.core]))

(defn find-children-files [context filepath]
  (when
   (and filepath
        (str/ends-with? (str/lower-case filepath) "readme.md"))
    (let [index (file-to-uri/get-idx context)
          ;; Remove trailing slash if present
          filepath (if (str/ends-with? filepath "/")
                     (subs filepath 0 (dec (count filepath)))
                     filepath)
          ;; Process filepath to match index format
          filepath-normalized
          (cond
            ;; Remove aidbox/docs/ prefix if present
            (str/starts-with? filepath "aidbox/docs/")
            (subs filepath 12)
            ;; Remove ./docs/ prefix if present
            (str/starts-with? filepath "./docs/")
            (subs filepath 7)
            ;; Remove docs/ prefix if present
            (str/starts-with? filepath "docs/")
            (subs filepath 5)
            ;; Otherwise use as is
            :else filepath)
          dir (.getParent (io/file filepath-normalized))
          result (filterv
                  (fn [[file _info]]
                    (let [starts-with-dir (and dir (str/starts-with? file dir))
                          not-same-file (not= file filepath-normalized)
                          same-parent (= dir (.getParent (io/file file)))
                          readme-in-subdir (and
                                            dir
                                            (= dir (.getParent (io/file (.getParent (io/file file)))))
                                            (str/ends-with? (str/lower-case file) "readme.md"))
                          matches (and starts-with-dir
                                       not-same-file
                                       (or same-parent readme-in-subdir))]
                      matches))
                  index)]
      result)))

(defn render-empty-page [context filepath title]
  [:div
   (headers/render-h1
    (markdown/renderers context filepath) title)
   (for [[_path {:keys [title uri]}]
         (find-children-files context filepath)]
     (let [prefix (gitbok.http/get-prefix context)
           product-path (products/path context)
           full-href (str prefix product-path "/" uri)]
       (big-links/big-link-view full-href title)))])

(def nav-button-classes
  "group text-sm
  flex gap-4 flex-1 items-center
  p-2.5
  border border-tint-5 rounded
  hover:border-primary-9
  text-pretty
  md:text-base
  md:h-[80px]")

(defn navigation-buttons [context uri]
  (let [[[prev-page-url prev-page-title] [next-page-url next-page-title]]
        (summary/get-prev-next-pages context uri)]
    [:div {:class "flex flex-col sm:flex-row justify-between items-start mt-8 pt-4 gap-4"}
     (when prev-page-url
       [:div {:class "flex-1 w-full sm:w-auto"}
        [:a {:href prev-page-url
             :hx-target "#content"
             :hx-push-url prev-page-url
             :hx-get (str prev-page-url "?partial=true")
             :hx-swap "outerHTML"
             :class (str nav-button-classes " flex-row-reverse")}
         [:span {:class "flex flex-col flex-1 text-right justify-center"}
          [:span {:class "text-xs text-tint-9"} "Previous"]
          [:span {:class "text-tint-11 group-hover:text-primary-9 line-clamp-2"} prev-page-title]]
         [:svg {:class "size-4 text-tint-11 group-hover:text-primary-9 flex-shrink-0"
                :fill "none"
                :stroke "currentColor"
                :viewBox "0 0 24 24"
                :stroke-width "2"}
          [:path {:stroke-linecap "round"
                  :stroke-linejoin "round"
                  :d "M15 19l-7-7 7-7"}]]]])
     (when next-page-url
       [:div {:class "flex-1 w-full sm:w-auto"}
        [:a {:href next-page-url
             :hx-target "#content"
             :hx-push-url next-page-url
             :hx-get (str next-page-url "?partial=true")
             :hx-swap "outerHTML"
             :class nav-button-classes}
         [:span {:class "flex flex-col flex-1 justify-center"}
          [:span {:class "text-xs text-tint-9"} "Next"]
          [:span {:class "text-tint-11 group-hover:text-primary-9 line-clamp-2"} next-page-title]]
         [:svg {:class "size-4 text-tint-11 group-hover:text-primary-9 flex-shrink-0"
                :fill "none"
                :stroke "currentColor"
                :viewBox "0 0 24 24"
                :stroke-width "2"}
          [:path {:stroke-linecap "round"
                  :stroke-linejoin "round"
                  :d "M9 5l7 7-7 7"}]]]])]))

(defn render-file* [context filepath parsed title _raw-content]
  (let [content-count (count (:content parsed))]
    {:content [:div {:class "flex-1 min-w-0 max-w-full"}
               (if (= 1 content-count)
                 (render-empty-page context filepath title)
                 (markdown/render-md context filepath parsed))]
     :parsed parsed}))

(defn content-div [context uri content filepath & [htmx?]]
  (let [parsed (when (map? content) (:parsed content))
        body (if (map? content) (:content content) content)
        ;; Extract relative URI for breadcrumb
        uri-relative (utils/uri-to-relative
                      uri
                      (System/getenv "DOCS_PREFIX")
                      (:path (gitbok.products/get-current-product context)))
        ;; Generate breadcrumb
        breadcrumb-elem (breadcrumb/breadcrumb context uri-relative)
        ;; Handle breadcrumb insertion based on body type
        body-with-breadcrumb (cond
                               ;; No breadcrumb to add
                               (nil? breadcrumb-elem) body

                               ;; Body is a string (HTML)
                               (string? body)
                               (if (str/includes? body "id=\"page-header\"")
                                 (str/replace body
                                              #"<header[^>]*id=\"page-header\"[^>]*>"
                                              (str "$0" (hiccup2.core/html breadcrumb-elem)))
                                 (str (hiccup2.core/html breadcrumb-elem) body))

                               ;; Body is Hiccup (vector)
                               (vector? body)
                               [:div
                                breadcrumb-elem
                                body]

                               ;; Fallback
                               :else body)
        toc (when filepath
              (if parsed
                (let [toc-result (right-toc/render-right-toc parsed)]
                  toc-result)
                (try
                  (let [content* (utils/slurp-resource filepath)
                        {:keys [parsed]} (markdown/parse-markdown-content context [filepath content*])
                        toc-result (right-toc/render-right-toc parsed)]
                    toc-result)
                  (catch Exception _
                    nil))))]
    [:main#content {:class "flex-1 items-start"}
     [:script (uui/raw "hljs.highlightAll(); if (typeof initializeMermaid !== 'undefined') { initializeMermaid(); }")]
     [:div {:class "flex items-start"}
      [:article {:class "article__content py-8 min-w-0 flex-1
                 max-w-5xl transform-3d"}
       (when htmx?
         [:script (uui/raw "window.scrollTo(0, 0); updateLastUpdated(); updateActiveNavItem(window.location.pathname); updatePageTitle();")])
       [:div {:class "mx-auto max-w-full md:px-4"} body-with-breadcrumb]
       (navigation-buttons context uri)
       (let [lastupdated
             (indexing/get-lastmod context filepath)]
         (when lastupdated
           [:p {:class "mt-4 text-sm text-tint-11"
                :id "lastupdated"
                :data-updated-at lastupdated}
            "Last updated " lastupdated]))]
      toc]]))

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
   [gitbok.utils :as utils]))

(defn find-children-files [context filepath]
  (when
   (and filepath
        (str/ends-with? (str/lower-case filepath) "readme.md"))
    (let [index (file-to-uri/get-idx context)
          filepath (if (str/ends-with? filepath "/")
                     (subs filepath 0 (dec (count filepath)))
                     filepath)
          filepath
          (if (str/starts-with? filepath "./docs/")
            (subs filepath 7)
            filepath)
          dir (.getParent (io/file filepath))]
      (filterv
       (fn [[file _info]]
         (and
          (str/starts-with? file dir)
          (not= file filepath)
          (or
           (= dir (.getParent (io/file file)))
           (and
            (= dir (.getParent (io/file (.getParent (io/file file)))))
            (str/ends-with? (str/lower-case file) "readme.md")))))
       index))))

(defn render-empty-page [context filepath title]
  [:div
   (headers/render-h1
    (markdown/renderers context filepath) title)
   (for [[_path {:keys [title uri]}]
         (find-children-files context filepath)]
     (big-links/big-link-view (str "/" uri) title))])

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
  {:content [:div {:class "flex-1 min-w-0 max-w-full"}
             (if (= 1 (count (:content parsed)))
               (render-empty-page context filepath title)
               (markdown/render-md context filepath parsed))]
   :parsed parsed})

(defn content-div [context uri content filepath & [htmx?]]
  (let [parsed (when (map? content) (:parsed content))
        body (if (map? content) (:content content) content)
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
    [:main#content {:class "flex-1 items-start pl-3"}
     [:div {:class "flex items-start"}
      [:article {:class "article__content py-8 min-w-0 flex-1
                 sm:px-0 md:px-4 max-w-5xl transform-3d"}
       (when htmx?
         [:script "window.scrollTo(0, 0); updateLastUpdated(); updateActiveNavItem(window.location.pathname); updatePageTitle();"])
       [:div {:class "mx-auto max-w-full md:px-4"} body]
       (navigation-buttons context uri)
       (let [lastupdated
             (indexing/get-lastmod context filepath)]
         (when lastupdated
           [:p {:class "mt-4 text-sm text-tint-11"
                :id "lastupdated"
                :data-updated-at lastupdated}
            "Last updated " lastupdated]))]
      toc]]))

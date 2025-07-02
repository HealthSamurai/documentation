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
   [gitbok.markdown.widgets.headers :as headers]))

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

(defn navigation-buttons [context uri]
  (let [[[prev-page-url prev-page-title] [next-page-url next-page-title]]
        (summary/get-prev-next-pages context uri)]
    [:div {:class "flex flex-col sm:flex-row justify-between items-center mt-8 pt-4 gap-4"}
     (when prev-page-url
       [:div {:class "flex-1 w-full sm:w-auto"}
        [:a {:href prev-page-url
             ;; :hx-target "#content"
             ;; :hx-push-url prev-page-url
             ;; :hx-get (str prev-page-url "?partial=true")
             ;; :hx-swap "outerHTML"
             :class "group text-sm p-2.5 flex gap-4 flex-1 flex-row-reverse items-center pl-4 border border-gray-300 rounded hover:border-orange-500 text-pretty md:p-4 md:text-base"}
         [:span {:class "flex flex-col flex-1 text-right"}
          [:span {:class "text-xs text-gray-500"} "Previous"]
          [:span {:class "text-gray-700 group-hover:text-orange-600 line-clamp-2"} prev-page-title]]
         [:svg {:class "size-4 text-gray-400 group-hover:text-orange-600"
                :fill "none"
                :stroke "currentColor"
                :viewBox "0 0 24 24"
                :stroke-width "2"}
          [:path {:stroke-linecap "round"
                  :stroke-linejoin "round"
                  :d "M15 19l-7-7 7-7"}]]]])
     (when next-page-url
       [:div {:class "flex-1 w-full sm:w-auto text-left"}
        [:a {:href next-page-url
             ;; :hx-target "#content"
             ;; :hx-push-url next-page-url
             ;; :hx-get (str next-page-url "?partial=true")
             ;; :hx-swap "outerHTML"
             :class "group text-sm p-2.5 flex gap-4 flex-1 items-center pr-4 border border-gray-300 rounded hover:border-orange-500 text-pretty md:p-4 md:text-base"}
         [:span {:class "flex flex-col flex-1"}
          [:span {:class "text-xs text-gray-500"} "Next"]
          [:span {:class "text-gray-700 group-hover:text-orange-600 line-clamp-2"} next-page-title]]
         [:svg {:class "size-4 text-gray-400 group-hover:text-orange-600"
                :fill "none"
                :stroke "currentColor"
                :viewBox "0 0 24 24"
                :stroke-width "2"}
          [:path {:stroke-linecap "round"
                  :stroke-linejoin "round"
                  :d "M9 5l7 7-7 7"}]]]])]))

(defn render-file* [context filepath parsed title raw-content]
  [:div {:class "flex-1 min-w-0 max-w-4xl"}
   (when (re-find #"```" raw-content)
     [:div
      [:link {:rel "stylesheet" :href "/static/github.min.css"}]
      [:script {:src "/static/highlight.min.js"}]
      [:script {:src "/static/json.min.js"}]
      [:script {:src "/static/bash.min.js"}]
      [:script {:src "/static/yaml.min.js"}]
      [:script {:src "/static/json.min.js"}]
      [:script {:src "/static/http.min.js"}]
      [:script {:src "/static/graphql.min.js"}]
      [:script
       {:defer true}
       (uui/raw
        "if ( document.querySelectorAll('pre code:not(.hljs)') > 0 && (typeof hljs !== 'undefined')) { hljs.highlightAll(); } setTimeout(function() { if (typeof initializeCopyButtons === 'function') { initializeCopyButtons(); } }, 100);")]])
   (if (= 1 (count (:content parsed)))
     (render-empty-page context filepath title)
     (markdown/render-md context filepath parsed))])

(defn content-div [context uri content filepath & [htmx?]]
  [:main#content
   [:article {:class "flex-1 py-6 max-w-6xl min-w-0 overflow-x-hidden"}
    (when htmx?
      [:script "window.scrollTo(0, 0); updateLastUpdated();"])
    [:div {:class "mx-auto px-2 max-w-full"} content]
    (navigation-buttons context uri)
    (let [lastupdated
          (indexing/get-lastmod context filepath)]
      (when lastupdated
        [:p {:class "mt-4 text-gray-600"
             :id "lastupdated"
             :data-updated-at lastupdated}
         "Last updated " lastupdated]))]])

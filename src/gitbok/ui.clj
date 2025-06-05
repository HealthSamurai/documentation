(ns gitbok.ui
  (:require
   [cheshire.core]
   [http]
   [gitbok.indexing.impl.summary :as summary]
   [system]
   [uui]
   [uui.heroicons :as ico]))

(defn render-menu [items]
  (if (:children items)
    [:details {:class ""}
     [:summary {:class "flex items-center"}
      [:div {:class "flex-1 clickable-summary"} (:title items)]
      (ico/chevron-right "chevron size-5 text-gray-400")]
     [:div {:class "ml-4 border-l border-gray-200"}
      (for [c (:children items)]
        (render-menu c))]]
    [:div (:title items)]))

(defn menu [summary]
  [:div
   [:div {:class "px-5 py-1"}
    [:div {:class "flex flex-col gap-2"}
     [:a {:href "/admin/broken" :class "block w-full"} "Broken Links"]
     [:a {:href "/search"
          :class "block w-full flex items-center gap-2"
          :id "search-link"
          :hx-get "/search"
          :hx-target "#content"
          :hx-swap "innerHTML"
          :hx-push-url "false"
          :hx-trigger "click, keydown[key=='k'][ctrlKey||metaKey] from:body"
          :hx-on ":after-request \"document.querySelector('#search-input')?.focus()\""
          }
      "Search"
      [:span {:class "text-xs text-gray-400"} "⌘K"]]]]
   (for [item summary]
     [:div
      [:div {:class "pl-4 mt-4 mb-2"}
       [:b (:title item)]]
      (for [ch (:children item)]
        (render-menu ch))])])

(defn layout [context request content]
  (if (uui/hx-target request)
    (uui/response
     [:div#content
      {:class "m-x-auto flex-1 py-6 px-12  h-screen overflow-auto"}
      content])
    (uui/boost-response
     context request
     [:div {:class "flex items-top"}
      [:script {:src "/static/toc.js"}]
      [:link {:rel "stylesheet" :href "/static/github.min.css"}]
      [:script {:src "/static/highlight.min.js"}]
      [:script {:src "/static/json.min.js"}]
      [:script {:src "/static/bash.min.js"}]
      [:script {:src "/static/yaml.min.js"}]
      [:script {:src "/static/json.min.js"}]
      [:script {:src "/static/http.min.js"}]
      [:script "hljs.highlightAll();"]
      ;; [:script {:src "/static/tabs.js"}]
      ;; [:script {:src "/static/syntax-highlight.js"}]

      [:div.nav
       {:class "px-6 py-6 w-80 text-sm h-screen overflow-auto bg-gray-50 shadow-md"}
       (menu (summary/get-summary context))]
      [:div#content {:class "m-x-auto flex-1 py-6 px-12 h-screen overflow-auto"} content]])))

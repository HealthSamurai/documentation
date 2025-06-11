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
     #_[:a {:href "/admin/broken" :class "block w-full"} "Broken Links"]]]
   (for [item summary]
     [:div
      [:div {:class "pl-4 mt-4 mb-2"}
       [:b (:title item)]]
      (for [ch (:children item)]
        (render-menu ch))])])

(defn layout-view [context content]
  [:div
   {:class "flex flex-col h-screen"}
   [:script {:src "/static/toc.js"}]
   [:script {:src "/static/tabs.js"}]
   [:link {:rel "stylesheet" :href "/static/github.min.css"}]
   [:script {:src "/static/highlight.min.js"}]
   [:script {:src "/static/json.min.js"}]
   [:script {:src "/static/bash.min.js"}]
   [:script {:src "/static/yaml.min.js"}]
   [:script {:src "/static/json.min.js"}]
   [:script {:src "/static/http.min.js"}]
   [:script "hljs.highlightAll();"]

   [:div {:class "flex items-center justify-between w-full py-3 min-h-16 px-4 sm:px-6 md:px-8 max-w-screen-2xl mx-auto bg-white border-b border-gray-200 flex-shrink-0"}
    [:div {:class "flex max-w-full lg:basis-72 min-w-0 shrink items-center justify-start gap-2 lg:gap-4"}
     [:a {:href "/" :class "group/headerlogo min-w-0 shrink flex items-center"}
      [:img {:alt "Aidbox Logo"
             :class "block object-contain size-8"
             :src "/.gitbook/assets/aidbox_logo.jpg"}]
      [:div {:class "text-pretty line-clamp-2 tracking-tight max-w-[18ch] lg:max-w-[24ch] font-semibold ms-3 text-base/tight lg:text-lg/tight text-gray-900"}
       "Aidbox User Docs"]]]

    [:div {:class "flex items-center"}
     [:a {:href "/search"
          :class "flex items-center gap-2 px-3 py-2 bg-gray-100 border border-gray-300 rounded-md text-gray-700 text-sm transition-all duration-200 hover:bg-gray-200 hover:border-gray-400"
          :id "search-link"
          :hx-get "/search"
          :hx-target "#content"
          :hx-swap "innerHTML"
          :hx-push-url "false"
          :hx-on ":after-request \"document.querySelector('#search-input')?.focus()\""}
      [:svg {:class "size-4" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
       [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z"}]]
      "Search"
      [:span {:class "text-xs text-gray-400"} "⌘K"]]]]

   [:div {:class "flex flex-1 overflow-hidden"}
    [:div.nav
     {:class "px-6 py-6 w-80 text-sm overflow-auto bg-gray-50 shadow-md"}
     (menu (summary/get-summary context))]
    [:div#content {:class "flex-1 py-6 px-12 overflow-auto"} content]]])

(defn response1 [body status]
  {:status (or status 200)
   :headers {"content-type" "text/html; ; charset=utf-8"}
   :body (uui/hiccup body)})

(defn layout [context request content]
  (let [body (if (map? content) (:body content) content)
        status (if (map? content) (:status content 200) 200)]
    (response1
     (if (uui/hx-target request)
       [:div#content
        {:class "m-x-auto flex-1 py-6 px-12  h-screen overflow-auto"}
        body]
       (uui/document
        context request
        (layout-view context body)))
     status)))

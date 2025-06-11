(ns gitbok.ui
  (:require
   [cheshire.core]
   [http]
   [gitbok.indexing.impl.summary :as summary]
   [system]
   [uui]
   [clojure.string :as str]
   [uui.heroicons :as ico]))

(defn render-menu [items]
  (if (:children items)
    [:details
     [:summary {:class "flex items-center justify-between font-medium text-gray-900 hover:bg-gray-100 transition-colors duration-200 cursor-pointer"}
      [:div {:class "flex-1 clickable-summary"} (:title items)]
      (ico/chevron-right "chevron size-5 text-gray-400 transition-transform duration-200")]
     [:div {:class "border-l border-gray-200 ml-4"}
      (for [c (:children items)]
        (render-menu c))]]
    [:div (:title items)]))

(defn menu [summary]
  [:div {:class "w-100 flex-shrink-0 sticky top-0 h-screen overflow-y-auto py-4 bg-white"}
   (for [item summary]
     [:div
      (when-not
        (str/blank? (:title item))
        [:div {:class "mt-4 mb-2 mx-2 px-4"}
         [:b (:title item)]])
      (for [ch (:children item)]
        (render-menu ch))])])

(defn nav []
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

     [:span {:class "text-xs text-gray-400"} "⌘K"]]]])

(defn layout-view [context content]
  [:div
   (nav)
   [:div
    {:class "flex px-4 sm:px-6 md:px-8 max-w-screen-2xl mx-auto site-full-width:max-w-full gap-8"}
    (menu (summary/get-summary context))
    [:div#content {:class "flex-1 py-6 overflow-auto"}
     [:div {:class "mx-auto px-2"} content]]]])

(defn response1 [body status]
  {:status (or status 200)
   :headers {"content-type" "text/html; ; charset=utf-8"}
   :body (uui/hiccup body)})

(defn document [body]
  [:html
   [:head
    [:script {:src "/static/htmx.min.js"}]
    [:script {:src "/static/app.js"}]
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
    [:link {:rel "stylesheet", :href "/static/app.build.css"}]
    [:meta {:name "htmx-config", :content "{\"scrollIntoViewOnBoost\":false}"}]]
   [:body {:hx-boost "true"} body]])

(defn layout [context request content]
  (let [body (if (map? content) (:body content) content)
        status (if (map? content) (:status content 200) 200)]
    (response1
     (if (uui/hx-target request)
       [:div#content
        {:class "flex-1 py-6 overflow-auto"}
        [:script "hljs.highlightAll();"]
        [:div {:class "mx-auto px-2"} body]]
       (document
        (layout-view context body)))
     status)))

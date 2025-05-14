(ns gitbok.ui
  (:require
   [cheshire.core]
   [gitbok.markdown]
   [http]
   [gitbok.indexing.impl.summary :as summary]
   [system]
   [uui]
   [uui.heroicons :as ico]))

(defn render-menu [items & [open]]
  (if (:children items)
    [:details {:class "" :open open}
     [:summary {:class "flex items-center"} [:div {:class "flex-1"} (:title items)] (ico/chevron-right "chevron size-5 text-gray-400")]
     [:div {:class "ml-4 border-l border-gray-200"}
      (for [c (:children items)] (render-menu c))]]
    [:div {:class ""} (:title items)]))

(defn menu [summary]
  [:div
   [:a {:href "/admin/broken" :class "block px-5 py-1"} "Broken Links"]
   (for [item summary]
     [:div
      [:div {:class "pl-4 mt-4 mb-2"} [:b (:title item)]]
      (for [ch (:children item)]
        (render-menu ch))])])

(defn layout [context request content]
  (if (uui/hx-target request)
    (uui/response [:div#content {:class "m-x-auto flex-1 py-6 px-12  h-screen overflow-auto"} content])
    (uui/boost-response
     context request
     [:div {:class "flex items-top"}
      [:script {:src "/static/tabs.js"}]
      [:div.nav {:class "px-6 py-6 w-80 text-sm h-screen overflow-auto bg-gray-50 shadow-md"}
       (menu (summary/get context))]
      [:div#content {:class "m-x-auto flex-1 py-6 px-12  h-screen overflow-auto"} content]])))

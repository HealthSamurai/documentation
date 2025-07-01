(ns gitbok.ui.search
  (:require [gitbok.ui.layout :as layout]
            [gitbok.indexing.core :as indexing]
            [gitbok.search]))

(defn search-results-view
  [context request]
  (let [query (get-in request [:query-params :q])
        results (gitbok.search/search context query)
        results
        (mapv
         (fn [res]
           (assoc res :uri
                  (indexing/filepath->uri context (-> res :hit :filepath))))
         results)]
    (layout/layout
     context request
     {:content
      [:div.space-y-4
       (if (empty? results)
         [:div.text-gray-500.text-center.py-4 "No results found"]
         (for [result results]
           (gitbok.search/page-view result)))]
      :title "Search results"
      :description "Search results"})))

(defn search-view
  [context request]
  (layout/layout
   context request
   {:content
    [:div.flex.flex-col.items-center.min-h-screen.bg-gray-50.p-4
     [:div.w-full.max-w-2xl.mt-8
      [:div.relative
       [:input#search-input.w-full.px-4.py-3.text-lg.rounded-lg.border.border-gray-300.shadow-sm.focus:outline-none.focus:ring-2.focus:ring-blue-500.focus:border-transparent
        {:type "text"
         :name "q"
         :placeholder "Search documentation..."
         :hx-get "/search/results"
         :hx-trigger "keyup changed delay:500ms, search"
         :hx-target "#search-results"
         :hx-indicator ".htmx-indicator"}]
       [:div.htmx-indicator.absolute.right-3.top-3
        [:div.animate-spin.rounded-full.h-6.w-6.border-b-2.border-blue-500]]]
      [:div#search-results.mt-4.space-y-4]]]
    :title "Search"
    :description "Search"}))

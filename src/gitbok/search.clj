(ns gitbok.search
  (:require [gitbok.indexing.core :as indexing]
            [cheshire.core]
            [clojure.string :as str]
            [gitbok.ui]
            [gitbok.static]
            [http]
            [ring.util.response :as resp]
            [system]
            [uui]))

(defn search [context query]
  (indexing/search context query))

  (defn page-view [result]
    (let [hit-by (:hit-by result)
          {:keys [title h1 h2 h3 text]} (:hit result)]
      [:div.p-4.bg-white.rounded-lg.shadow-sm.hover:shadow-md.transition-shadow
       [:a {:href (:uri result)} title]
       (when-not (= :title hit-by)
         [:div
          (when h1 [:h1 h1])
          (when h2 [:h2 h1])
          (when h2 [:h3 h1])])]))

(defn
  ^{:http {:path "/search/results"}}
  search-results-view
  [context request]
  (let [query (get-in request [:query-params :q])
        results (search context query)
        results
        (mapv
          (fn [res]
            (assoc res :uri
                   (indexing/filepath->uri context (-> res :hit :filepath))))
          results)]
    (def results results)
    (gitbok.ui/layout
      context request
      [:div.space-y-4
       (if (empty? results)
         [:div.text-gray-500.text-center.py-4 "No results found"]
         (for [result results]
           (page-view result)))])))

(defn
  ^{:http {:path "/search"}}
  search-view
  [context request]
  (gitbok.ui/layout
    context request
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
      [:div#search-results.mt-4.space-y-4]]]))

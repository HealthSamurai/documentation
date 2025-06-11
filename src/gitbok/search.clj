(ns gitbok.search
  (:require [gitbok.indexing.core :as indexing]
            [cheshire.core]
            [gitbok.ui]
            [http]
            [system]
            [uui]))

(defn search [context query]
  (indexing/search context query))

(defn page-view [result]
  (let [hit-by (:hit-by result)
        {:keys [title h1 h2 h3 h4 text]} (:hit result)]
    [:div.p-4.bg-white.rounded-lg.shadow-sm.hover:shadow-md.transition-shadow.border.border-gray-100.mb-3
     [:div.flex.flex-col.gap-2
      (when-not (= :title hit-by)
        [:div.text-xs.font-medium.text-gray-500.uppercase.tracking-wide
         (case hit-by
           :h1 "Found in heading 1"
           :h2 "Found in heading 2"
           :h3 "Found in heading 3"
           :h4 "Found in heading 4"
           "Found in content")])
      [:a.text-lg.font-medium.text-blue-600.hover:text-blue-800
       {:href (str "/" (:uri result))}
       title]
      (when-not (= :title hit-by)
        (case hit-by
          :h1
          [:div.text-base.font-medium.text-gray-800.mb-1 h1]
          :h2
          [:div.text-sm.text-gray-700.mb-1 [:span.text-gray-500 "→ "] h2]
          :h3
          [:div.text-sm.text-gray-600 [:span.text-gray-500 "→ "] h3]

          :h4
          [:div.text-sm.text-gray-600 [:span.text-gray-500 "→ "] h4]

          :text
          [:div.text-sm.text-gray-600 [:span.text-gray-500 "→ "] text]))]]))

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

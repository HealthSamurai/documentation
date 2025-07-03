(ns gitbok.ui.search
  (:require [gitbok.ui.layout :as layout]
            [gitbok.indexing.core :as indexing]
            [gitbok.search]
            [clojure.string :as str]))

(defn highlight-text [text query]
  (when text
    (let [query-lower (str/lower-case query)
          text-lower (str/lower-case text)
          index (str/index-of text-lower query-lower)]
      (if (and index (pos? (count query)))
        (let [before (subs text 0 index)
              match (subs text index (+ index (count query)))
              after (subs text (+ index (count query)))]
          [:span
           before
           [:span.bg-orange-600.text-white.p-1.px-0.5.-mx-0.5.py-0.5.rounded match]
           after])
        text))))

(defn search-result-item [result query]
  (let [{:keys [title h1 h2 h3 h4 text]} (:hit result)
        uri (:uri result)
        hit-by (:hit-by result)
        show-details (and (not= hit-by "title")
                          (not= hit-by "h1")
                          (seq text))]
    [:div.mb-4
     [:a.flex.gap-4.flex-row.items-center.p-4.border.border-gray-200.rounded-lg.text-base.font-medium.hover:bg-gray-50.group
      {:href uri}
      [:div.size-4
       [:svg.size-4.text-gray-400
        {:fill "none"
         :stroke "currentColor"
         :viewBox "0 0 24 24"
         :stroke-width "2"}
        [:path {:stroke-linecap "round"
                :stroke-linejoin "round"
                :d "M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"}]]]
      [:div.flex.flex-col.w-full
       [:div.text-xs.text-gray-500.font-normal.uppercase.tracking-wider.mb-1.flex.flex-wrap.gap-x-2.gap-y-1.items-center
        [:span.line-clamp-1
         [:span.flex.items-center.gap-1 "Aidbox"]]]
       [:span.whitespace-break-spaces
        (highlight-text (or title h1 h2 h3 h4 text "Untitled") query)]
       (when show-details
         [:div.mt-2.border-l-2.border-gray-200.pl-4
          [:p.text-base.mb-2 (highlight-text (or h1 h2 h3 h4 title "Untitled") query)]
          [:p.text-sm.line-clamp-3.relative (highlight-text (or text "No content available") query)]])]
      [:div.p-2.rounded.opacity-60
       [:svg.size-4
        {:fill "none"
         :stroke "currentColor"
         :viewBox "0 0 24 24"
         :stroke-width "2"}
        [:path {:stroke-linecap "round"
                :stroke-linejoin "round"
                :d "M9 5l7 7-7 7"}]]]]]))


(defn search-results-only [context request]
  (let [query (get-in request [:query-params :q] "")
        results (when (pos? (count query))
                  (let [search-results (gitbok.search/search context query)]
                    (mapv
                     (fn [res]
                       (assoc res :uri
                              (indexing/filepath->uri context (-> res :hit :filepath))))
                     search-results)))]
    (if (empty? query)
      [:div.text-center.text-gray-500.py-8
       [:div.text-lg.font-medium.mb-2 "Search Documentation"]]
      (if (empty? results)
        [:div.text-center.text-gray-500.py-8
         [:div.text-lg.font-medium.mb-2 "No results found"]
         [:div.text-sm "Try different keywords or check spelling"]]
        [:div
         (for [result results]
           (search-result-item result query))]))))

(defn search-view
  [context request]
  (let [query (get-in request [:query-params :q] "")]
    (layout/layout
     context request
     {:content
      [:div.flex.flex-col.items-center.min-h-screen.p-4
       [:div.w-full.max-w-4xl.mt-8
        [:div.relative.mb-8
         [:input#search-input.w-full.px-4.py-4.text-xl.rounded-lg.border.border-gray-300.shadow-sm.focus:outline-none
          {:type "text"
           :name "q"
           :placeholder "Search documentation..."
           :value query
           :hx-get "/search/results-only"
           :hx-trigger "keyup changed delay:500ms, search"
           :hx-target "#search-results"
           :hx-indicator ".htmx-indicator"}]
         [:div.htmx-indicator.absolute.right-3.top-3
          [:div.animate-spin.rounded-full.h-6.w-6.border-b-2.border-orange-500]]]]
        [:div#search-results
         (search-results-only context request)]]
      :title "Search"
      :description "Search"
      :lastmod nil})))

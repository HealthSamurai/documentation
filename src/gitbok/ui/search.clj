(ns gitbok.ui.search
  (:require [gitbok.ui.layout :as layout]
            [gitbok.indexing.core :as indexing]
            [gitbok.search]
            [gitbok.utils :as utils]
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
           [:span {:class "bg-orange-600 text-tint-1 p-1 px-0.5 -mx-0.5 py-0.5 rounded"} match]
           after])
        text))))

(defn search-result-item [grouped-result query]
  (let [{:keys [results uri]} grouped-result
        ;; Get the title from the first result
        first-result (first results)
        page-title (or (-> first-result :hit :title)
                       (-> first-result :hit :h1)
                       "Untitled")
        ;; Check if any result is a title match
        has-title-match? (some #(= (:hit-by %) :title) results)]
    [:div {:class "mb-4 border border-tint-4 rounded-lg"}
     ;; Page title - always clickable
     [:a.flex.gap-4.flex-row.items-center.p-4.text-base.font-medium.hover:bg-tint-2.rounded-t-lg
      {:href uri}
      ;; File icon
      [:div.size-4.flex-shrink-0
       [:svg.size-4.text-tint-6
        {:fill "none"
         :stroke "currentColor"
         :viewBox "0 0 24 24"
         :stroke-width "2"}
        [:path {:stroke-linecap "round"
                :stroke-linejoin "round"
                :d "M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"}]]]
      ;; Page title
      [:div.flex-grow
       [:span.whitespace-break-spaces
        (highlight-text page-title query)]]
      ;; Arrow icon
      [:div.p-2.rounded.opacity-60.flex-shrink-0
       [:svg.size-4
        {:fill "none"
         :stroke "currentColor"
         :viewBox "0 0 24 24"
         :stroke-width "2"}
        [:path {:stroke-linecap "round"
                :stroke-linejoin "round"
                :d "M9 5l7 7-7 7"}]]]]
     ;; Match locations - only show if not a title match
     (when-not has-title-match?
       [:div.border-t.border-tint-3
        (for [result results]
          (let [{:keys [hit hit-by]} result
                {:keys [h1 h2 h3 h4 text]} hit
                ;; Don't show title matches as they're already in the header
                show-match? (not= hit-by :title)]
            (when show-match?
              (case hit-by
                :h1 [:a.block.px-4.py-2.hover:bg-primary-2.transition-colors
                     {:href uri}
                     [:div.text-base.font-semibold (highlight-text h1 query)]]
                :h2 [:a.block.px-4.py-2.hover:bg-primary-2.transition-colors
                     {:href (str uri "#" (utils/s->url-slug h2))}
                     [:div.text-base.font-medium (highlight-text h2 query)]]
                :h3 [:a.block.px-4.py-2.hover:bg-primary-2.transition-colors
                     {:href (str uri "#" (utils/s->url-slug h3))}
                     [:div.text-sm.font-medium (highlight-text h3 query)]]
                :h4 [:a.block.px-4.py-2.hover:bg-primary-2.transition-colors
                     {:href (str uri "#" (utils/s->url-slug h4))}
                     [:div.text-sm (highlight-text h4 query)]]
                :text [:a.block.px-4.py-2.hover:bg-primary-2.transition-colors
                       {:href uri}
                       [:div.text-sm.text-tint-10
                        [:p.line-clamp-2 (highlight-text text query)]]]))))])]))

(defn search-results-only [context request]
  (let [query (get-in request [:query-params :q] "")
        results (when (pos? (count query))
                  (let [search-results (gitbok.search/search context query)]
                    (mapv
                     (fn [res]
                       (assoc res :uri
                              (indexing/filepath->uri context (-> res :hit :filepath))))
                     search-results)))
        ;; Group results by filepath and sort by max score
        grouped-and-sorted (when results
                             (let [groups (group-by #(-> % :hit :filepath) results)
                                  ;; Create vector of [filepath, results, max-score]
                                   groups-with-scores (mapv (fn [[filepath page-results]]
                                                              (let [max-score (apply max (map :score page-results))]
                                                                [filepath page-results max-score]))
                                                            groups)]
                              ;; Sort by max score descending
                               (sort-by #(nth % 2) > groups-with-scores)))]

    (def gg grouped-and-sorted)
    (if (empty? query)
      [:div.text-center.text-tint-9.py-8
       [:div.text-lg.font-medium.mb-2 "Search Documentation"]]
      (if (empty? results)
        [:div.text-center.text-tint-9.py-8
         [:div.text-lg.font-medium.mb-2 "No results found"]
         [:div.text-sm "Try different keywords or check spelling"]]
        [:div
         (for [[filepath page-results _] grouped-and-sorted]
           (search-result-item {:results page-results
                                :filepath filepath
                                :uri (:uri (first page-results))} query))]))))

(defn search-view
  [context request]
  (let [query (get-in request [:query-params :q] "")]
    (layout/layout
     context request
     {:content
      [:div.flex.flex-col.items-center.min-h-screen.p-4
       [:div {:class "w-full max-w-4xl lg:min-w-[56rem] mt-8 flex flex-col items-center px-4 sm:px-6"}
        [:div.relative.mb-8.w-full
         [:input {:id "search-input"
                  :class "w-full px-4 py-4 text-xl rounded-lg border border-tint-5 shadow-sm focus:outline-none"
                  :type "text"
                  :name "q"
                  :placeholder "Search documentation..."
                  :value query
                  :hx-get "/search/results-only"
                  :hx-trigger "keyup changed delay:500ms, search"
                  :hx-target "#search-results"
                  :hx-indicator ".htmx-indicator"}]
         [:div.htmx-indicator.absolute.right-3.top-3
          [:div.animate-spin.rounded-full.h-6.w-6.border-b-2.border-orange-500]]]
        [:div#search-results {:class "w-full"}
         (search-results-only context request)]]]
      :title "Search"
      :description "Search"
      :lastmod nil})))

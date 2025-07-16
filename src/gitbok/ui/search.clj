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
           [:span {:class "bg-warning-2 text-tint-12 p-1 px-0.5 -mx-0.5 py-0.5 rounded"} match]
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
        has-title-match? (some #(#{:title :h1} (:hit-by %)) results)]
    [:div {:class "mb-4 border border-tint-4 rounded-lg"}
     ;; Page title - always clickable
     [:a.flex.gap-4.flex-row.items-center.p-4.text-base.font-medium.hover:bg-tint-2.rounded-t-lg
      {:href uri}
      ;; File icon
      [:div.size-4.flex-shrink-0
       [:svg.size-4.text-tint-9
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
                show-match? (not (#{:title :h1} hit-by))]
            (when show-match?
              (case hit-by
                :h2 [:a.block.px-4.py-2.hover:bg-primary-2.transition-colors
                     {:href (str uri "#" (utils/s->url-slug h2))}
                     [:div.flex.items-center.gap-2
                      [:span {:class "inline-flex items-center justify-center px-1.5 py-0.5 text-[10px] font-semibold rounded bg-primary-2 text-primary-11"}
                       "H2"]
                      [:span.text-base.font-medium (highlight-text h2 query)]]]
                :h3 [:a.block.px-4.py-2.hover:bg-primary-2.transition-colors
                     {:href (str uri "#" (utils/s->url-slug h3))}
                     [:div.flex.items-center.gap-2
                      [:span {:class "inline-flex items-center justify-center px-1.5 py-0.5 text-[10px] font-semibold rounded bg-tint-2 text-tint-11"}
                       "H3"]
                      [:span.text-sm.font-medium (highlight-text h3 query)]]]
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

(defn dropdown-result-item [context result query selected-index index]
  (let [{:keys [hit hit-by uri]} result
        {:keys [filepath title h1 h2 h3 h4 text]} hit
        page-title (or title h1 "Untitled")
        is-selected (= selected-index index)
        ;; Determine the specific URL based on the hit type
        ;; Ensure URI starts with / to make it absolute
        absolute-uri (if (str/starts-with? uri "/") uri (str "/" uri))
        ;; Determine the specific URL based on the hit type
        url (case hit-by
              :h2 (str absolute-uri "#" (utils/s->url-slug h2))
              :h3 (str absolute-uri "#" (utils/s->url-slug h3))
              :h4 (str absolute-uri "#" (utils/s->url-slug h4))
              absolute-uri)
        ;; Determine what text to show based on what was matched
        match-text (case hit-by
                     :title title
                     :h1 h1
                     :h2 h2
                     :h3 h3
                     :h4 h4
                     :text text
                     page-title)
        ;; Check if this is a title/h1 match
        is-title-match? (#{:title :h1} hit-by)]
    [:a {:href url
         :class (str "flex items-center gap-3 group px-4 py-3 transition-all duration-200 rounded-md "
                     (if is-selected
                       "bg-warning-2 text-tint-strong hover:bg-warning-2"
                       "text-tint hover:bg-tint-2 hover:text-tint-strong"))
         :data-search-result-index index}
     ;; File icon
     [:div {:class (str "size-5 shrink-0 "
                        (if is-selected "text-warning-9" "text-tint-9 group-hover:text-tint-strong"))}
      [:svg {:class "size-5"
             :fill "none"
             :stroke "currentColor"
             :viewBox "0 0 24 24"
             :stroke-width "2"}
       [:path {:stroke-linecap "round"
               :stroke-linejoin "round"
               :d "M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"}]]]
     ;; Content
     [:div {:class "grow min-w-0"}
      ;; Page title with section breadcrumb - only show if not a title/h1 match
      (when-not is-title-match?
        [:div {:class "text-xs font-medium uppercase leading-none tracking-wider mb-1.5 opacity-70"}
         [:span {:class "line-clamp-1"}
          page-title
          (when (#{:h2 :h3} hit-by)
            [:span " › "
             [:span {:class (str "inline-flex items-center justify-center px-1.5 py-0.5 text-[10px] font-semibold rounded "
                                 (case hit-by
                                   :h2 "bg-primary-2 text-primary-11"
                                   :h3 "bg-tint-2 text-tint-11"
                                   ""))}
              (case hit-by
                :h2 "H2"
                :h3 "H3"
                "")]])]])
      ;; Matched content
      [:p {:class "line-clamp-2 font-semibold text-base leading-snug"}
       [:span {:class "whitespace-break-spaces"}
        (highlight-text match-text query)]]]
     ;; Enter arrow icon
     [:div {:class (str "size-8 shrink-0 flex items-center justify-center transition-all duration-200 "
                        (if is-selected "text-orange-600 opacity-100" "text-tint-9 opacity-60 group-hover:opacity-100"))}
      [:svg {:class "size-4"
             :fill "none"
             :stroke "currentColor"
             :viewBox "0 0 24 24"
             :stroke-width "2"}
       [:path {:stroke-linecap "round"
               :stroke-linejoin "round"
               :d "M9 5l7 7-7 7"}]]]]))

(defn search-dropdown-results [context request]
  (let [query (get-in request [:query-params :query] "")
        selected-index (get-in request [:query-params :selected] "-1")
        selected-index (try (Integer/parseInt selected-index) (catch Exception _ 0))
        results (when (and query (pos? (count query)))
                  (let [search-results (take 10 (gitbok.search/search context query))]
                    (mapv
                     (fn [res]
                       (assoc res :uri
                              (indexing/filepath->uri context (-> res :hit :filepath))))
                     search-results)))]
    (if (empty? query)
      [:div] ;; Empty div when no query
      (if (empty? results)
        [:div {:class "z-50 overflow-y-auto overflow-x-hidden rounded-md text-sm text-tint
                       shadow-lg outline-none ring-1 ring-tint-subtle transition-all bg-tint-base
                       w-full p-2 max-h-[24rem] max-w-[35rem]"}
         [:div {:class "text-center text-tint-9 py-8"}
          [:div {:class "text-sm"} "No results found"]]]
        [:div {:class "z-50 animate-scaleIn overflow-y-auto overflow-x-hidden rounded-md text-sm text-tint
                       shadow-lg outline-none ring-1 ring-tint-subtle transition-all bg-tint-base
                       w-full p-2 max-h-[24rem]
                       max-w-[35rem] scroll-py-2"
               :data-search-dropdown "true"}
         [:div {:class "flex flex-col gap-y-1"}

          ;; Search results
          (map-indexed (fn [idx result]
                         (dropdown-result-item context result query selected-index idx))
                       results)
          ;; View all results link
          (when (= (count results) 10)
            [:a {:href (str "/search?q=" query)
                 :class "flex items-center gap-3 px-4 py-3 text-sm text-tint-subtle hover:text-tint-strong transition-colors"}
             [:span "View all results"]
             [:svg {:class "size-3 ml-1"
                    :fill "none"
                    :stroke "currentColor"
                    :viewBox "0 0 24 24"
                    :stroke-width "2"}
              [:path {:stroke-linecap "round"
                      :stroke-linejoin "round"
                      :d "M9 5l7 7-7 7"}]]])]]))))

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
          [:div.animate-spin.rounded-full.h-6.w-6.border-b-2.border-warning-9]]]
        [:div#search-results {:class "w-full"}
         (search-results-only context request)]]]
      :title "Search"
      :description "Search"
      :lastmod nil})))

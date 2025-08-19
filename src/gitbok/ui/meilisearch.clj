(ns gitbok.ui.meilisearch
  (:require
   [clojure.string :as str]
   [org.httpkit.client :as http-client]
   [cheshire.core :as json]
   [uui]
   [gitbok.utils :as utils]))

(def meilisearch-host (or (System/getenv "MEILISEARCH_URL") "http://localhost:7700"))
(def meilisearch-api-key (System/getenv "MEILISEARCH_API_KEY"))
(def index-name "docs")

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
           [:mark {:class "bg-warning-2 text-tint-12 p-1 px-0.5 -mx-0.5 py-0.5 rounded"} match]
           after])
        text))))

(defn search-meilisearch [query]
  (try
    (let [headers (if meilisearch-api-key
                    {"Authorization" (str "Bearer " meilisearch-api-key)
                     "Content-Type" "application/json"}
                    {"Content-Type" "application/json"})
          response @(http-client/post
                     (str meilisearch-host "/multi-search")
                     {:headers headers
                      :body
                      (json/generate-string
                       {:federation {:limit 20} ; Set limit at federation level
                        :queries [{:indexUid index-name
                                   :q query
                                   :filter (str "hierarchy_lvl6 = \"" query "\"")
                                   :federationOptions {:weight 1.2}
                                   :attributesToHighlight ["content" "hierarchy_lvl0" "hierarchy_lvl1"
                                                           "hierarchy_lvl2" "hierarchy_lvl3" "hierarchy_lvl6"]
                                   :highlightPreTag "<mark class=\"bg-warning-2 text-tint-12 p-1 px-0.5 -mx-0.5 py-0.5 rounded\">"
                                   :highlightPostTag "</mark>"}
                                  {:indexUid index-name
                                   :q query
                                   :attributesToHighlight ["content" "hierarchy_lvl0" "hierarchy_lvl1"
                                                           "hierarchy_lvl2" "hierarchy_lvl3" "hierarchy_lvl6"]
                                   :highlightPreTag "<mark class=\"bg-warning-2 text-tint-12 p-1 px-0.5 -mx-0.5 py-0.5 rounded\">"
                                   :highlightPostTag "</mark>"}]})})]
      (if (= 200 (:status response))
        (-> response :body json/parse-string (get "hits"))
        []))
    (catch Exception e
      (println "Meilisearch error:" (.getMessage e))
      [])))

(defn group-results-by-page [results]
  ;; Group only consecutive items with the same hierarchy_lvl1
  ;; This preserves the order from federated search
  (let [groups (partition-by #(get % "hierarchy_lvl1") results)]
    (map (fn [items]
           {:page-title (get (first items) "hierarchy_lvl1")
            :items (vec items)})
         groups)))

(defn render-result-item [item query index is-grouped?]
  (let [lvl0 (get item "hierarchy_lvl0")
        lvl1 (get item "hierarchy_lvl1")
        lvl2 (get item "hierarchy_lvl2")
        lvl3 (get item "hierarchy_lvl3")
        lvl6 (get item "hierarchy_lvl6") ; Get hierarchy_lvl6
        content (get item "content")
        url (get item "url")
        anchor (get item "anchor")
        formatted (get item "_formatted")

        ;; Determine title and subtitle
        title (if (and is-grouped? (or lvl2 lvl3))
                (or lvl2 lvl3 "Untitled")
                (or lvl1 lvl0 "Untitled"))
        subtitle (when (not is-grouped?)
                   (or lvl2 lvl3))

        ;; Use formatted versions if available
        highlighted-title (if formatted
                            (or (when (and is-grouped? (or lvl2 lvl3))
                                  (or (get formatted "hierarchy_lvl2")
                                      (get formatted "hierarchy_lvl3")))
                                (get formatted "hierarchy_lvl1")
                                (get formatted "hierarchy_lvl0")
                                (highlight-text title query))
                            (highlight-text title query))

        highlighted-subtitle (when subtitle
                               (if formatted
                                 (or (get formatted "hierarchy_lvl2")
                                     (get formatted "hierarchy_lvl3")
                                     (highlight-text subtitle query))
                                 (highlight-text subtitle query)))

        ;; Use formatted version of lvl6 if available
        highlighted-lvl6 (when lvl6
                           (if formatted
                             (get formatted "hierarchy_lvl6" lvl6)
                             lvl6))

        ;; Build final URL with anchor
        final-url (cond
                   ;; If anchor exists and URL doesn't already have one, add it
                    (and anchor (not (str/includes? url "#"))) (str url "#" anchor)
                   ;; Otherwise use URL as is
                    :else url)

        ;; Icon type
        has-anchor? (or subtitle anchor (and is-grouped? (or lvl2 lvl3)))
        padding-class (if (and is-grouped? (or lvl2 lvl3)) "pl-10" "px-3")]

    [:a {:href final-url
         :class (str "flex items-center gap-3 " padding-class " pr-3 py-2.5 rounded-md "
                     "text-tint-strong transition-colors block "
                     "hover:bg-tint-hover")
         :data-result-index index}
     [:div {:class "size-5 shrink-0 text-tint-9 opacity-60"}
      (if has-anchor?
        ;; Hash icon
        [:svg {:fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
         [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2"
                 :d "M7 20l4-16m2 16l4-16M6 9h14M4 15h14"}]]
        ;; File icon
        [:svg {:fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
         [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2"
                 :d "M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"}]])]
     [:div {:class "flex-1 min-w-0"}
      [:div {:class (str "text-sm " (if (and is-grouped? (or lvl2 lvl3)) "font-normal" "font-semibold")
                         " leading-tight text-tint-strong")}
       (if (string? highlighted-title)
         (uui/raw highlighted-title)
         highlighted-title)]
      (when (and subtitle (not is-grouped?))
        [:div {:class "text-xs text-tint-10 mt-0.5 opacity-90"}
         (if (string? highlighted-subtitle)
           (uui/raw highlighted-subtitle)
           highlighted-subtitle)])
      ;; Display hierarchy_lvl6 if present with highlighting
      (when highlighted-lvl6
        [:div {:class "text-xs font-mono bg-tint-3 text-tint-11 px-1.5 py-0.5 rounded inline-block mt-1"}
         (if (string? highlighted-lvl6)
           (uui/raw highlighted-lvl6)
           highlighted-lvl6)])
      (when (and content (not subtitle) (not is-grouped?) (not lvl6)) ; Don't show content if lvl6 is shown
        [:div {:class "text-xs text-tint-9 mt-1 line-clamp-1 opacity-80"}
         (let [highlighted-content (highlight-text (subs content 0 (min 100 (count content))) query)]
           (if (string? highlighted-content)
             (uui/raw highlighted-content)
             highlighted-content))
         (when (> (count content) 100) "...")])]
     [:div {:class "size-6 shrink-0 flex items-center justify-center text-tint-9 opacity-40"}
      [:svg {:class "size-3.5" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
       [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2.5"
               :d "M9 5l7 7-7 7"}]]]]))

(defn meilisearch-dropdown [_context request]
  (let [query (get-in request [:query-params :q] "")
        is-mobile (= "true" (get-in request [:query-params :mobile]))
        results (when (and query (pos? (count query)))
                  (search-meilisearch query))]

    (if (empty? query)
      [:div] ;; Empty div when no query

      (if (empty? results)
        ;; No results found
        [:div {:class (str "bg-white " (if is-mobile "border border-tint-6" "shadow-lg ring-1 ring-tint-subtle")
                           " rounded-md p-4 text-sm text-tint-9 "
                           (when-not is-mobile "md:w-[32rem]"))}
         (str "No results found for \"" query "\"")]

        ;; Results found - group and render
        (let [groups (group-results-by-page results)
              ;; Pre-calculate indices for each result item
              indexed-groups (loop [remaining-groups groups
                                    current-index 0
                                    acc []]
                               (if (empty? remaining-groups)
                                 acc
                                 (let [group (first remaining-groups)
                                       items (:items group)
                                       page-title (:page-title group)
                                       has-sections? (some #(or (get % "hierarchy_lvl2")
                                                                (get % "hierarchy_lvl3")
                                                                (get % "hierarchy_lvl6")) items)
                                      ;; Calculate how many items will be rendered
                                       item-count (cond
                                                    (= 1 (count items)) 1
                                                    (and page-title (> (count items) 1) has-sections?)
                                                    (inc (count (filter #(or (get % "hierarchy_lvl2")
                                                                             (get % "hierarchy_lvl3")
                                                                             (get % "hierarchy_lvl6"))
                                                                        items)))
                                                    :else (count items))]
                                   (recur (rest remaining-groups)
                                          (+ current-index item-count)
                                          (conj acc (assoc group :start-index current-index))))))]
          [:div {:class (str "bg-white " (if is-mobile "border border-tint-6" "shadow-lg ring-1 ring-tint-subtle")
                             " rounded-md overflow-hidden max-h-[48rem] overflow-y-auto "
                             (when-not is-mobile "md:w-[32rem]"))}
           [:div {:class "p-2 space-y-1"}

            ;; Render grouped results
            (for [group indexed-groups]
              (let [page-title (:page-title group)
                    items (:items group)
                    start-index (:start-index group)
                    has-sections? (some #(or (get % "hierarchy_lvl2")
                                             (get % "hierarchy_lvl3")
                                             (get % "hierarchy_lvl6")) items)]

                (cond
                  ;; Single result - show as one block
                  (= 1 (count items))
                  (render-result-item (first items) query start-index false)

                  ;; Multiple results with sections - show h1 header separately
                  (and page-title (> (count items) 1) has-sections?)
                  [:div {:class "rounded-md p-1 space-y-0.5 bg-tint-2"}
                   ;; Main page result - always create a clean h1 link
                   (let [first-item (first items)
                         clean-url (when-let [url (get first-item "url")]
                                     (first (str/split url #"#")))
                         main-page-item (-> first-item
                                            (assoc "hierarchy_lvl2" nil
                                                   "hierarchy_lvl3" nil
                                                   "hierarchy_lvl6" nil
                                                   "anchor" nil
                                                   "url" clean-url))]
                     (render-result-item main-page-item query start-index false))
                   ;; Section results - all items with lvl2, lvl3, or lvl6
                   (map-indexed
                    (fn [idx item]
                      (render-result-item item query (+ start-index 1 idx) true))
                    (filter #(or (get % "hierarchy_lvl2")
                                 (get % "hierarchy_lvl3")
                                 (get % "hierarchy_lvl6"))
                            items))]

                  ;; Multiple results without sections - show ungrouped
                  :else
                  (map-indexed
                   (fn [idx item]
                     (render-result-item item query (+ start-index idx) false))
                   items))))]])))))

(defn meilisearch-endpoint [context request]
  (let [result (meilisearch-dropdown context request)]
    {:status 200
     :headers {"content-type" "text/html; charset=utf-8"
               "Cache-Control" "no-cache, no-store, must-revalidate"}
     :body (utils/->html result)}))

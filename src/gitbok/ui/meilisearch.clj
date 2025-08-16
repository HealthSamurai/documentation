(ns gitbok.ui.meilisearch
  (:require
   [clojure.string :as str]
   [org.httpkit.client :as http-client]
   [cheshire.core :as json]
   [uui]
   [gitbok.utils :as utils]
   [gitbok.http :as gitbok.http]))

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
          response @(http-client/post (str meilisearch-host "/indexes/" index-name "/search")
                                      {:headers headers
                                       :body (json/generate-string
                                              {:q query
                                               :limit 20
                                               :attributesToHighlight ["content" "hierarchy_lvl0" "hierarchy_lvl1" 
                                                                       "hierarchy_lvl2" "hierarchy_lvl3"]
                                               :highlightPreTag "<mark class=\"bg-warning-2 text-tint-12 p-1 px-0.5 -mx-0.5 py-0.5 rounded\">"
                                               :highlightPostTag "</mark>"})})]
      (if (= 200 (:status response))
        (-> response :body json/parse-string (get "hits"))
        []))
    (catch Exception e
      (println "Meilisearch error:" (.getMessage e))
      [])))

(defn group-results-by-page [results]
  (let [groups (group-by #(get % "hierarchy_lvl1") results)
        ordered-groups (map (fn [[page-title items]]
                             {:page-title page-title
                              :items (vec items)})
                           groups)]
    ordered-groups))

(defn render-result-item [item query index is-grouped?]
  (let [lvl0 (get item "hierarchy_lvl0")
        lvl1 (get item "hierarchy_lvl1")
        lvl2 (get item "hierarchy_lvl2")
        lvl3 (get item "hierarchy_lvl3")
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
      (when (and content (not subtitle) (not is-grouped?))
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

(defn meilisearch-dropdown [context request]
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
              result-index (atom 0)]
          [:div {:class (str "bg-white " (if is-mobile "border border-tint-6" "shadow-lg ring-1 ring-tint-subtle")
                            " rounded-md overflow-hidden max-h-[48rem] overflow-y-auto "
                            (when-not is-mobile "md:w-[32rem]"))}
           [:div {:class "p-2 space-y-1"}
            
            ;; Render grouped results
            (for [group groups]
              (let [page-title (:page-title group)
                    items (:items group)
                    has-sections? (some #(or (get % "hierarchy_lvl2") (get % "hierarchy_lvl3")) items)]
                
                (if (and page-title (> (count items) 1) has-sections?)
                  ;; Grouped results with visual grouping
                  [:div {:class "rounded-md p-1 space-y-0.5 bg-tint-2"}
                   ;; Main page result - if no main item exists, create one from first section item
                   (let [main-item (first (filter #(and (not (get % "hierarchy_lvl2")) 
                                                        (not (get % "hierarchy_lvl3"))) items))
                         ;; If no main page item exists, create one from the first item
                         main-or-synthesized (or main-item
                                                (let [first-item (first items)]
                                                  (when first-item
                                                    ;; Create a synthesized main page item - remove anchor to link to page itself
                                                    (let [clean-url (when-let [url (get first-item "url")]
                                                                     (first (str/split url #"#")))]
                                                      (-> first-item
                                                          (assoc "hierarchy_lvl2" nil
                                                                 "hierarchy_lvl3" nil
                                                                 "anchor" nil
                                                                 "url" clean-url))))))]
                     (when main-or-synthesized
                       (let [idx @result-index]
                         (swap! result-index inc)
                         (render-result-item main-or-synthesized query idx false))))
                   ;; Section results
                   (for [item (filter #(or (get % "hierarchy_lvl2") (get % "hierarchy_lvl3")) items)]
                     (let [idx @result-index]
                       (swap! result-index inc)
                       (render-result-item item query idx true)))]
                  
                  ;; Ungrouped results
                  (for [item items]
                    (let [idx @result-index]
                      (swap! result-index inc)
                      (render-result-item item query idx false))))))]])))))

(defn meilisearch-endpoint [context request]
  (let [result (meilisearch-dropdown context request)]
    {:status 200
     :headers {"content-type" "text/html; charset=utf-8"
               "Cache-Control" "no-cache, no-store, must-revalidate"}
     :body (utils/->html result)}))
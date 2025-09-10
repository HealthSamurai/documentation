(ns gitbok.ui.search
  (:require
   [clojure.tools.logging :as log]
   [gitbok.indexing.core :as indexing]
   [gitbok.ui.breadcrumb :as breadcrumb]
   [gitbok.search]
   [gitbok.utils :as utils]
   [gitbok.products :as products]
   [gitbok.http :as http]
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

(defn build-search-result-url
  "Builds the correct URL for a search result, ensuring the /docs prefix is included"
  [context uri]
  (let [product-prefix (http/get-product-prefix context)
        product-path (products/path context)
        docs-prefix (http/get-prefix context)]
    (cond
      ;; No URI - just return product prefix
      (nil? uri) product-prefix
      ;; URI already contains full product prefix (with /docs) - use as is
      (str/starts-with? uri product-prefix) uri
      ;; URI starts with product path but missing /docs prefix
      (and (str/starts-with? uri product-path)
           (not (str/starts-with? uri product-prefix)))
      (str docs-prefix uri)
      ;; URI is absolute but missing product prefix - prepend it
      (str/starts-with? uri "/") (str product-prefix uri)
      ;; URI is relative - make absolute with product prefix
      :else (str product-prefix "/" uri))))

(defn get-page-breadcrumb [context uri]
  (when-let [filepath (indexing/uri->filepath context uri)]
    (let [breadcrumb-result (breadcrumb/breadcrumb context filepath)]
      (when breadcrumb-result
        ;; Extract text content from breadcrumb hiccup structure
        (letfn [(extract-text [hiccup]
                  (cond
                    (string? hiccup) hiccup
                    (vector? hiccup) (let [[tag attrs & content] hiccup]
                                       (if (= tag :a)
                                         (str/join " " (map extract-text content))
                                         (str/join " " (map extract-text content))))
                    :else ""))]
          (let [breadcrumb-text (extract-text breadcrumb-result)]
            (when (seq breadcrumb-text)
              breadcrumb-text)))))))

(defn dropdown-result-item [context result query index]
  (let [{:keys [hit hit-by uri]} result
        {:keys [filepath title h1 h2 h3 h4 text]} hit
        page-title (or title h1 "Untitled")
        breadcrumb (get-page-breadcrumb context uri)
        ;; Build the base URL with proper /docs prefix
        base-url (build-search-result-url context uri)
        ;; Determine the specific URL based on the hit type
        url (case hit-by
              :h2 (str base-url "#" (utils/s->url-slug h2))
              :h3 (str base-url "#" (utils/s->url-slug h3))
              :h4 (str base-url "#" (utils/s->url-slug h4))
              base-url)
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

    (if is-title-match?
      ;; Show page entry
      [:a {:href url
           :class "flex items-center gap-3 group px-4 py-3
                       text-tint
                       hover:bg-tint-2
                       hover:text-tint-strong
                       hover:bg-warning-2
                       group transition-colors rounded-md "
           :data-search-result-index index}
       ;; File icon
       [:div {:class (str "size-5 shrink-0 "
                          "text-tint-9 ")}
        [:svg {:class "size-5"
               :fill "none"
               :stroke "currentColor"
               :viewBox "0 0 24 24"
               :stroke-width "2"}
         [:path {:stroke-linecap "round"
                 :stroke-linejoin "round"
                 :d "M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"}]]]
       ;; Content
       [:div {:class "grow"}
        ;; Breadcrumb
        (when breadcrumb
          [:div {:class "text-xs text-tint/7 contrast-more:text-tint transition-colors font-normal uppercase leading-none tracking-wider mb-1 flex flex-wrap gap-x-2 gap-y-1 items-center"}
           [:span {:class "line-clamp-1"}
            [:span {:class "flex items-center gap-1"} breadcrumb]]])
        ;; Page title
        [:p {:class "line-clamp-2 font-semibold text-base text-tint-strong leading-snug"}
         [:span {:class "whitespace-break-spaces"}
          (highlight-text match-text query)]]]
       ;; Arrow icon
       [:div {:class (str "flex size-8 shrink-0 items-center justify-center search-action-button "
                          "text-tint-9 opacity-60 ")}
        [:svg {:class "size-4"
               :fill "none"
               :stroke "currentColor"
               :viewBox "0 0 24 24"
               :stroke-width "2"}
         [:path {:stroke-linecap "round"
                 :stroke-linejoin "round"
                 :d "M9 5l7 7-7 7"}]]]]

      ;; Show section entry with indentation
      [:a {:href url
           :class "flex items-center gap-3 group px-4 py-1.5
                       text-tint-10
                       hover:bg-tint-2
                       hover:text-tint-strong
                       transition-colors rounded-md "
           :data-search-result-index index}
       ;; Empty space for alignment
       [:div {:class "size-4 shrink-0"}]
       ;; Content with margin and border
       [:div {:class "grow ml-3"}
        [:div {:class "border-l-2 border-tint-6 pl-4"}
         (when (and match-text (not (str/blank? match-text)))
           [:p {:class "text-sm text-tint-10"}
            [:span {:class "whitespace-break-spaces"}
             (highlight-text match-text query)]])
         (when (and text (not= text match-text) (not (str/blank? text)))
           [:p {:class "relative line-clamp-3 text-xs text-tint-10 mt-1 [overflow-wrap:anywhere]"}
            [:span {:class "whitespace-break-spaces"}
             (highlight-text text query)]])]]
       ;; Action button
       [:div {:class (str "flex size-8 shrink-0 items-center justify-center "
                          "text-tint-9 opacity-60 group-hover:opacity-100")}
        [:svg {:class "size-4"
               :fill "none"
               :stroke "currentColor"
               :viewBox "0 0 24 24"
               :stroke-width "2"}
         [:path {:stroke-linecap "round"
                 :stroke-linejoin "round"
                 :d "M9 5l7 7-7 7"}]]]])))

(defn search-dropdown-results [context request]
  (let [query (get-in request [:query-params :q] "")
        results (when (and query (pos? (count query)))
                  (let [search-results (take 30 (gitbok.search/search context query))]
                    (mapv
                     (fn [res]
                       (let [filepath (-> res :hit :filepath)
                             uri (indexing/filepath->uri context filepath)
                             ;; If no URI found in index, generate one from filepath
                             ;; Remove common prefixes that shouldn't be in the URL
                             generated-uri (when (and (nil? uri) filepath)
                                             (-> filepath
                                                 (str/replace #"^\./" "") ; Remove leading ./
                                                 (str/replace #"^forms/" "") ; Remove forms/ prefix if present
                                                 (str/replace #"^aidbox/" "") ; Remove aidbox/ prefix if present
                                                 (str/replace #"^\./docs/" "") ; Remove ./docs/ prefix
                                                 (str/replace #"^docs/" "") ; Remove docs/ prefix
                                                 (str/replace #"\.md$" "") ; Remove .md extension
                                                 (str/replace #"/README$" "/") ; Convert README to /
                                                 (str/replace #"^/" "")))] ; Remove leading /
                         (when (nil? uri)
                           (log/warn "uri not found" {:filepath filepath
                                                      :current-product (products/get-current-product-id context)
                                                      :generated-uri generated-uri}))
                         ;; Add debug for all URIs
                         (when generated-uri
                           (log/debug "uri mapping" {:filepath filepath :uri generated-uri}))
                         (assoc res :uri (or uri generated-uri))))
                     search-results)))
        ;; Preserve original search order while grouping by filepath
        grouped-results (when results
                          (let [;; Add original index to preserve order
                                indexed-results (map-indexed (fn [idx res] (assoc res :original-index idx)) results)
                                groups (group-by #(-> % :hit :filepath) indexed-results)]
                            ;; Create flat list preserving original order
                            (->> groups
                                 (mapcat (fn [[filepath page-results]]
                                           (let [;; Sort by original index to maintain search order
                                                 sorted-results (sort-by :original-index page-results)
                                                 page-title-result (or (first (filter #(#{:title :h1} (:hit-by %)) sorted-results))
                                                                       (first sorted-results))
                                                 section-results (filter #(#{:h2 :h3 :h4 :text} (:hit-by %)) sorted-results)]
                                             ;; Return page first, then sections in original order
                                             (concat
                                              (when page-title-result [page-title-result])
                                              section-results))))
                                 ;; Sort final result by original index to maintain search order
                                 (sort-by :original-index))))]
    (if (empty? query)
      [:div] ;; Empty div when no query
      (if (empty? results)
        [:div {:class "z-50 animate-scaleIn overflow-y-auto overflow-x-hidden circular-corners:rounded-3xl rounded-corners:rounded-md text-sm text-tint shadow-lg outline-none ring-1 ring-tint-subtle transition-all empty:hidden bg-tint-base has-[.empty]:hidden scroll-py-2 w-full md:w-[32rem] p-2 max-h-[48rem] md:max-h-[48rem] md:max-w-[min(var(--radix-popover-content-available-width),32rem)]"}
         [:div {:class "text-center text-tint-9 py-8"}
          [:div {:class "text-sm"} "No results found"]]]
        [:div {:class "z-50 animate-scaleIn overflow-y-auto overflow-x-hidden circular-corners:rounded-3xl rounded-corners:rounded-md text-sm text-tint shadow-lg outline-none ring-1 ring-tint-subtle transition-all empty:hidden bg-tint-base has-[.empty]:hidden scroll-py-2 w-full md:w-[32rem] p-2 max-h-[48rem] md:max-h-[48rem] md:max-w-[min(var(--radix-popover-content-available-width),32rem)]"
               :data-search-dropdown "true"}
         [:div {:class "flex flex-col gap-y-1 pb-4"}

          ;; Search results
          (map-indexed (fn [idx result]
                         (dropdown-result-item context result query idx))
                       grouped-results)]]))))

(defn search-endpoint [context request]
  (let [result
        (search-dropdown-results context request)]
    {:status 200
     :headers {"content-type" "text/html; charset=utf-8"
               "Cache-Control" "no-cache, no-store, must-revalidate"}
     :body (gitbok.utils/->html result)}))

(ns gitbok.ui.meilisearch
  (:require
   [clojure.tools.logging :as log]
   [clojure.string :as str]
   [org.httpkit.client :as http-client]
   [cheshire.core :as json]
   [hiccup2.core]
   [gitbok.utils :as utils]
   [gitbok.products :as products]
   [gitbok.state :as state]
   [gitbok.ui.tags :as tags]))

;; ========== SVG Icons ==========

(defn github-icon
  "GitHub logo icon for example results (ungrouped items)"
  []
  [:svg {:class "size-5 shrink-0" :fill "currentColor" :viewBox "0 0 24 24"}
   [:path {:d "M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z"}]])

(defn github-icon-small
  "GitHub logo icon for example results (grouped items)"
  []
  [:svg {:class "size-4 shrink-0" :fill "currentColor" :viewBox "0 0 24 24"}
   [:path {:d "M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z"}]])

(defn arrow-icon
  "Arrow icon for documentation results"
  []
  [:svg {:class "size-3" :fill "currentColor" :viewBox "0 0 5 9"}
   [:path {:fill-rule "evenodd" :clip-rule "evenodd"
           :d "M4.38065 3.85065C4.45087 3.92096 4.49032 4.01627 4.49032 4.11565C4.49032 4.21502 4.45087 4.31033 4.38065 4.38065L0.630646 8.13065C0.559558 8.19689 0.465535 8.23295 0.368385 8.23123C0.271234 8.22952 0.178541 8.19016 0.109835 8.12146C0.0411284 8.05275 0.00177253 7.96006 5.84237e-05 7.86291C-0.00165568 7.76576 0.0344058 7.67173 0.100646 7.60065L3.58565 4.11565L0.100646 0.630646C0.0344058 0.559559 -0.00165568 0.465535 5.84237e-05 0.368385C0.00177253 0.271234 0.0411284 0.178542 0.109835 0.109835C0.178541 0.0411285 0.271234 0.00177253 0.368385 5.84235e-05C0.465535 -0.00165568 0.559558 0.0344059 0.630646 0.100646L4.38065 3.85065Z"}]])

;; ========== Search Functions ==========

(defn transform-example-to-doc
  "Transform example document to doc format for consistent rendering"
  [item]
  (if (get item "github_url")
    ;; It's an example - transform fields and combine languages/features into tags
    (let [languages (get item "languages" [])
          features (get item "features" [])
          tags (vec (concat languages features))]
      {"hierarchy_lvl0" (get item "category")
       "hierarchy_lvl1" (get item "title")
       "content" (get item "description")
       "url" (get item "github_url")
       "github_url" (get item "github_url")  ; Keep to detect it's an example
       "id" (get item "id")
       "tags" tags  ; Combine languages and features into tags
       "_formatted" (when-let [formatted (get item "_formatted")]
                      {"hierarchy_lvl0" (get formatted "category")
                       "hierarchy_lvl1" (get formatted "title")
                       "content" (get formatted "description")})})
    ;; It's a doc - return as is (will have tags if scraper added them)
    item))

(defn search-meilisearch [context query index-name]
  (try
    (let [search-url (str (state/get-config context :meilisearch-url) "/multi-search")
          meilisearch-api-key (state/get-config context :meilisearch-api-key)
          headers (if meilisearch-api-key
                    {"Authorization" (str "Bearer " meilisearch-api-key)
                     "Content-Type" "application/json"}
                    {"Content-Type" "application/json"})
          ;; Filter to exclude deprecated pages using STARTS WITH
          ;; This operator is available by default (no experimental features needed)
          deprecated-filter "NOT url STARTS WITH \"https://www.health-samurai.io/docs/aidbox/deprecated\""
          request-body {:federation {:limit 300} ; Increased limit to ensure examples appear even with many doc results
                        :queries [{:indexUid index-name
                                   :q query
                                   :filter (str deprecated-filter " AND hierarchy_lvl6 = \"" query "\"")
                                   :federationOptions {:weight 1.2}
                                   :attributesToHighlight ["content" "hierarchy_lvl0" "hierarchy_lvl1"
                                                           "hierarchy_lvl2" "hierarchy_lvl3" "hierarchy_lvl6"]
                                   :highlightPreTag "<mark class=\"!bg-highlight text-on-surface-strong px-0.5 py-0.5 -mx-0.5 rounded-sm\">"
                                   :highlightPostTag "</mark>"}
                                  {:indexUid index-name
                                   :q query
                                   :filter deprecated-filter
                                   :attributesToHighlight ["content" "hierarchy_lvl0" "hierarchy_lvl1"
                                                           "hierarchy_lvl2" "hierarchy_lvl3" "hierarchy_lvl6"]
                                   :highlightPreTag "<mark class=\"!bg-highlight text-on-surface-strong px-0.5 py-0.5 -mx-0.5 rounded-sm\">"
                                   :highlightPostTag "</mark>"}
                                  {:indexUid "examples"
                                   :q query
                                   :federationOptions {:weight 1.2}
                                   :attributesToHighlight ["title" "description" "category"]
                                   :highlightPreTag "<mark class=\"!bg-highlight text-on-surface-strong px-0.5 py-0.5 -mx-0.5 rounded-sm\">"
                                   :highlightPostTag "</mark>"}]}
          response @(http-client/post search-url
                                      {:headers headers
                                       :body (json/generate-string request-body)})]
      (if (= 200 (:status response))
        (let [hits (-> response :body json/parse-string (get "hits"))
              transformed-hits (mapv transform-example-to-doc hits)]
          transformed-hits)
        (do
          (log/warn "meilisearch-non-200" {:status (:status response)
                                           :body (:body response)})
          [])))
    (catch Exception e
      (log/error e "meilisearch error" {:query query
                                        :index index-name})
      [])))

(defn interpret-search-results
  "Interprets Meilisearch results to determine grouping strategy.
   Returns a sequence of {:group {:level N :title 'Title'} :items [...]}
   or {:item single-item} for ungrouped items."
  [results]
  ;; Strategy: partition by lvl1, then decide if each partition needs grouping
  (let [partitions (partition-by
                    (fn [item]
                      [(get item "hierarchy_lvl0")
                       (get item "hierarchy_lvl1")])
                    results)]
    (mapcat
     (fn [items]
       (if (= 1 (count items))
         ;; Single item - never group
         [{:item (first items)}]

         ;; Multiple items - analyze if they need grouping
         (let [;; Check what levels exist in this group
               has-main-page? (some #(and (get % "hierarchy_lvl1")
                                          (nil? (get % "hierarchy_lvl2"))) items)
               has-sections? (some #(get % "hierarchy_lvl2") items)

               ;; If we have both main page and sections, group by lvl1
               should-group-by-lvl1? (and has-main-page? has-sections?)

               ;; Now check for lvl2 grouping - partition by lvl2
               lvl2-groups (when (not should-group-by-lvl1?)
                             (partition-by #(vector (get % "hierarchy_lvl0")
                                                    (get % "hierarchy_lvl1")
                                                    (get % "hierarchy_lvl2"))
                                           items))]

           (cond
             ;; Group all by lvl1
             should-group-by-lvl1?
             [{:group {:level 1 :title (get (first items) "hierarchy_lvl1")}
               :items (vec items)}]

             ;; Check each lvl2 group
             lvl2-groups
             (mapcat
              (fn [lvl2-items]
                (let [first-item (first lvl2-items)
                      all-same-lvl2? (apply = (map #(get % "hierarchy_lvl2") lvl2-items))
                      distinct-lvl3 (count (distinct (map #(get % "hierarchy_lvl3") lvl2-items)))]

                  (if (and (> (count lvl2-items) 1)
                           all-same-lvl2?
                           (> distinct-lvl3 1)
                           (get first-item "hierarchy_lvl2"))
                    ;; Group by lvl2
                    [{:group {:level 2 :title (get first-item "hierarchy_lvl2")}
                      :items (vec lvl2-items)}]
                    ;; Don't group
                    (map (fn [item] {:item item}) lvl2-items))))
              lvl2-groups)

             ;; No grouping needed
             :else
             (map (fn [item] {:item item}) items)))))
     partitions)))

(defn prepare-for-render
  "Prepares interpreted results for rendering.
   For grouped items, creates a clean header and marks children.
   For ungrouped items, returns as-is."
  [{:keys [group items item]}]
  (if group
    ;; Grouped - create header and children
    (let [first-item (first items)
          ;; Create clean header without subsections
          header (-> first-item
                     (assoc "hierarchy_lvl2" nil
                            "hierarchy_lvl3" nil
                            "hierarchy_lvl4" nil
                            "hierarchy_lvl5" nil
                            "hierarchy_lvl6" nil
                            "anchor" nil)
                     (update "url" #(when % (first (str/split % #"#")))))]
      {:header header
       :children items
       :group group})
    ;; Ungrouped - return as-is
    item))

;; get-visual-properties removed - no longer needed

(defn group-results-by-hierarchy [results]
  ;; Generalized grouping algorithm for all hierarchy levels
  ;; Group consecutive items that share the same parent hierarchy
  (let [groups (partition-by
                (fn [item]
                  ;; Create grouping key - always group by the page (lvl1)
                  ;; This ensures all sections of the same page stay together
                  (let [lvl0 (get item "hierarchy_lvl0")
                        lvl1 (get item "hierarchy_lvl1")]
                    ;; Always group by lvl1 if it exists
                    (if lvl1
                      [lvl0 lvl1]
                      [lvl0 (gensym)])))
                results)]
    (map (fn [items]
           (let [first-item (first items)
                 ;; Check if this group needs grouping based on variety in levels
                 has-multiple-items? (> (count items) 1)

                 ;; Check what levels vary within the group
                 all-same-lvl1? (apply = (map #(get % "hierarchy_lvl1") items))
                 all-same-lvl2? (apply = (map #(get % "hierarchy_lvl2") items))
                 all-same-lvl3? (apply = (map #(get % "hierarchy_lvl3") items))
                 all-same-lvl4? (apply = (map #(get % "hierarchy_lvl4") items))
                 all-same-lvl5? (apply = (map #(get % "hierarchy_lvl5") items))

                 ;; Check if there are any sections at each level
                 has-any-lvl2? (some #(get % "hierarchy_lvl2") items)
                 has-any-lvl3? (some #(get % "hierarchy_lvl3") items)
                 has-any-lvl4? (some #(get % "hierarchy_lvl4") items)
                 has-any-lvl5? (some #(get % "hierarchy_lvl5") items)

                 ;; Special check: If all items are h1-only (no subsections), don't group
                 all-h1-only? (and all-same-lvl1?
                                   (not has-any-lvl2?)
                                   (not has-any-lvl3?)
                                   (not has-any-lvl4?)
                                   (not has-any-lvl5?))

                 ;; Determine the grouping level
                 deepest-common (cond
                                  ;; Don't group if all items are h1-only
                                  all-h1-only?
                                  nil

                                  ;; Group by lvl4 if all have same lvl4 but different lvl5
                                  (and has-multiple-items?
                                       all-same-lvl4?
                                       (not all-same-lvl5?)
                                       has-any-lvl5?
                                       (get first-item "hierarchy_lvl4"))
                                  {:level 4 :title (get first-item "hierarchy_lvl4")}

                                  ;; Group by lvl3 if all have same lvl3 but different lvl4
                                  (and has-multiple-items?
                                       all-same-lvl3?
                                       (not all-same-lvl4?)
                                       has-any-lvl4?
                                       (get first-item "hierarchy_lvl3"))
                                  {:level 3 :title (get first-item "hierarchy_lvl3")}

                                  ;; Group by lvl2 if all have same lvl2 but different lvl3
                                  (and has-multiple-items?
                                       all-same-lvl2?
                                       (not all-same-lvl3?)
                                       has-any-lvl3?
                                       (get first-item "hierarchy_lvl2"))
                                  {:level 2 :title (get first-item "hierarchy_lvl2")}

                                  ;; Group by lvl1 if all have same lvl1 and any subsections
                                  (and has-multiple-items?
                                       all-same-lvl1?
                                       (or has-any-lvl2? has-any-lvl3? has-any-lvl4? has-any-lvl5?)
                                       (get first-item "hierarchy_lvl1"))
                                  {:level 1 :title (get first-item "hierarchy_lvl1")}

                                  ;; No grouping needed
                                  :else nil)]
             {:group-info deepest-common
              :items (vec (take 10 items))}))
         groups)))

(defn build-final-url
  "Builds the final URL with anchor if needed."
  [url anchor]
  (cond
    ;; If anchor exists and URL doesn't already have one, add it
    (and anchor (not (str/includes? url "#"))) (str url "#" anchor)
    ;; Otherwise use URL as is
    :else url))

(defn render-result-item
  ([item index is-grouped? is-last?]
   (render-result-item item index is-grouped? is-last? false))
  ([item index is-grouped? is-last? is-first?]
   (let [lvl0 (get item "hierarchy_lvl0")
         lvl1 (get item "hierarchy_lvl1")
         lvl2 (get item "hierarchy_lvl2")
         lvl3 (get item "hierarchy_lvl3")
         lvl4 (get item "hierarchy_lvl4")
         lvl5 (get item "hierarchy_lvl5")
         lvl6 (get item "hierarchy_lvl6")
         content (get item "content")
         url (get item "url")
         anchor (get item "anchor")
         formatted (get item "_formatted")
         is-example? (boolean (get item "github_url"))

         ;; Build final URL with anchor
         final-url (build-final-url url anchor)

         ;; Get highlighted versions if available
         get-highlighted (fn [level-key level-value]
                           (if formatted
                             (or (get formatted level-key) level-value)
                             level-value))

         ;; Border classes for grouped items
         border-classes (if (and is-grouped? (not is-first?))
                          "border-t border-outline hover:border-transparent"
                          "")

         ;; Link attributes
         link-attrs (cond-> {:href final-url
                             :class (str "flex items-center gap-3 p-2 rounded-lg text-on-surface-strong transition-all block hover:bg-surface-result-hover peer " border-classes)
                             :data-result-index index}
                      is-example? (assoc :target "_blank" :rel "noopener noreferrer"))]

     [:a link-attrs

      ;; Main content container
      [:div {:class "flex-1 min-w-0"}

       (if is-grouped?
        ;; Grouped item - simpler display
        [:div
         ;; For grouped items, show the deepest level as title
         (let [title (or lvl5 lvl4 lvl3 lvl2 lvl1)]
           (when title
             [:div {:class "flex items-center gap-1.5 text-sm font-normal leading-5 tracking-tight text-on-surface-secondary"}
              (when is-example?
                (github-icon-small))
              [:span
               (if-let [highlighted (get-highlighted
                                     (cond
                                       lvl5 "hierarchy_lvl5"
                                       lvl4 "hierarchy_lvl4"
                                       lvl3 "hierarchy_lvl3"
                                       lvl2 "hierarchy_lvl2"
                                       :else "hierarchy_lvl1")
                                     title)]
                 (hiccup2.core/raw highlighted)
                 title)]]))

         ;; Show content for grouped items
         (when content
           [:div {:class "text-xs text-text-muted leading-5 tracking-tight mt-1 line-clamp-2"}
            (let [highlighted-content (get-highlighted "content" content)]
              (if (string? highlighted-content)
                (hiccup2.core/raw highlighted-content)
                highlighted-content))])]

        ;; Ungrouped item - show all levels
        [:div
         ;; lvl0 - uppercase brand color
         (when lvl0
           [:div {:class "text-xs font-medium uppercase text-brand tracking-wider leading-4 mb-0.5"}
            (let [highlighted (get-highlighted "hierarchy_lvl0" lvl0)]
              (if (string? highlighted)
                (hiccup2.core/raw highlighted)
                highlighted))])

         ;; lvl1 - semibold, main title
         (when lvl1
           [:div {:class "flex items-center gap-1.5 text-base font-semibold leading-5 tracking-tight text-on-surface-strong"}
            (when is-example?
              (github-icon))
            [:span
             (let [highlighted (get-highlighted "hierarchy_lvl1" lvl1)]
               (if (string? highlighted)
                 (hiccup2.core/raw highlighted)
                 highlighted))]])

         ;; lvl2 - semibold
         (when lvl2
           [:div {:class "text-sm font-semibold leading-5 tracking-tight text-on-surface-strong mt-0.5"}
            (let [highlighted (get-highlighted "hierarchy_lvl2" lvl2)]
              (if (string? highlighted)
                (hiccup2.core/raw highlighted)
                highlighted))])

         ;; lvl3 - medium weight
         (when lvl3
           [:div {:class "text-sm font-medium leading-5 tracking-tight text-on-surface-strong mt-0.5"}
            (let [highlighted (get-highlighted "hierarchy_lvl3" lvl3)]
              (if (string? highlighted)
                (hiccup2.core/raw highlighted)
                highlighted))])

         ;; lvl4 - normal weight
         (when lvl4
           [:div {:class "text-sm font-normal leading-5 tracking-tight text-on-surface-strong mt-0.5"}
            (let [highlighted (get-highlighted "hierarchy_lvl4" lvl4)]
              (if (string? highlighted)
                (hiccup2.core/raw highlighted)
                highlighted))])

         ;; lvl5 - normal weight, slightly muted
         (when lvl5
           [:div {:class "text-sm font-normal leading-5 tracking-tight text-on-surface-muted mt-0.5"}
            (let [highlighted (get-highlighted "hierarchy_lvl5" lvl5)]
              (if (string? highlighted)
                (hiccup2.core/raw highlighted)
                highlighted))])

         ;; lvl6 - monospace with background
         (when lvl6
           [:div {:class "mt-1"}
            [:span {:class "text-xs font-mono bg-surface-alt text-on-surface-muted px-1.5 py-0.5 rounded inline-block"}
             (let [highlighted (get-highlighted "hierarchy_lvl6" lvl6)]
               (if (string? highlighted)
                 (hiccup2.core/raw highlighted)
                 highlighted))]])

         ;; content - description text
         (when content
           [:div {:class "text-xs text-text-muted leading-5 tracking-tight mt-1 line-clamp-2"}
            (let [highlighted-content (get-highlighted "content" content)
                  truncated (if (> (count content) 150)
                              (str (subs content 0 150) "...")
                              content)]
              (if (string? highlighted-content)
                (hiccup2.core/raw highlighted-content)
                truncated))])

         ;; tags - show tags if present (only for examples)
         (when (and is-example? (seq (get item "tags")))
           [:div {:class "flex flex-wrap gap-2 mt-2"}
            (for [tag (take 5 (get item "tags"))]
              (tags/render-tag {:text tag :key tag :variant :default}))])])]

      ;; Icon (arrow for all results)
      [:div {:class "size-6 shrink-0 flex items-center justify-center text-on-surface-strong"}
       (arrow-icon)]])))

(def login-button-class
  "Shared button style matching the login button in main-navigation"
  "inline-flex items-center justify-center px-4 py-2 text-sm font-semibold leading-5 bg-surface text-on-surface hover:bg-button-hover-bg border border-outline rounded-md transition-colors cursor-pointer")

(def dropdown-class
  "Shared dropdown container style for search results"
  "w-[555px] rounded-lg p-6 pr-8 bg-surface border border-outline shadow-dropdown")

(defn render-no-results
  "Renders the no results message with feedback form."
  [query]
  [:div {:class dropdown-class}
   [:div {:id "no-results-feedback-form"}
    [:div {:class "text-sm leading-[22.75px] tracking-[-0.02em] text-on-surface-strong mb-3"}
     (str "No results found for \"" query "\"")]
    [:textarea {:id "no-results-feedback-input"
                :class "w-full px-3 py-2 text-sm border border-outline-subtle rounded-md text-on-surface-strong bg-surface hover:border-outline-input-focus focus:outline-none focus:border-outline-input-focus transition-all resize-none"
                :rows "2"
                :placeholder "Tell us what you were looking for..."}]
    [:div {:class "flex justify-end mt-3"}
     [:button {:type "button"
               :class login-button-class
               :onclick (str "submitNoResultsFeedback('" (str/escape query {\' "\\'" \\ "\\\\"}) "', event)")}
      "Send"]]]])

(defn render-group-header
  "Renders a group header for grouped results."
  [first-item group-info start-index]
  (let [group-level (:level group-info)
        clean-url (when-let [url (get first-item "url")]
                    (first (str/split url #"#")))
        ;; Create header item based on group level
        header-item (case group-level
                      1 (-> first-item
                            (assoc "hierarchy_lvl2" nil
                                   "hierarchy_lvl3" nil
                                   "hierarchy_lvl4" nil
                                   "hierarchy_lvl5" nil
                                   "hierarchy_lvl6" nil
                                   "anchor" nil
                                   "url" clean-url))
                      2 (-> first-item
                            (assoc "hierarchy_lvl3" nil
                                   "hierarchy_lvl4" nil
                                   "hierarchy_lvl5" nil
                                   "hierarchy_lvl6" nil
                                   "anchor" nil))
                      3 (-> first-item
                            (assoc "hierarchy_lvl4" nil
                                   "hierarchy_lvl5" nil
                                   "hierarchy_lvl6" nil
                                   "anchor" nil))
                      4 (-> first-item
                            (assoc "hierarchy_lvl5" nil
                                   "hierarchy_lvl6" nil
                                   "anchor" nil))
                      first-item)]
    (render-result-item header-item start-index false false)))

(defn calculate-indexed-groups
  "Pre-calculates indices for each result item in groups."
  [groups]
  (loop [remaining-groups groups
         current-index 0
         acc []]
    (if (empty? remaining-groups)
      acc
      (let [group (first remaining-groups)
            items (:items group)
            group-info (:group-info group)
            ;; Calculate how many items will be rendered
            item-count (if group-info
                         ;; Grouped: header + child items
                         (inc (count items))
                         ;; Ungrouped: just the items
                         (count items))]
        (recur (rest remaining-groups)
               (+ current-index item-count)
               (conj acc (assoc group :start-index current-index)))))))

(defn render-search-results
  "Renders the search results with appropriate grouping."
  [groups]
  (let [indexed-groups (calculate-indexed-groups groups)]
    (for [group indexed-groups]
      (let [group-info (:group-info group)
            items (:items group)
            start-index (:start-index group)]

        (if group-info
          ;; Grouped results - show header and children with dividers
          (let [first-item (first items)
                ;; Check if first item is h1-only (it becomes the header)
                first-is-h1-only? (and (get first-item "hierarchy_lvl1")
                                       (not (get first-item "hierarchy_lvl2"))
                                       (not (get first-item "hierarchy_lvl3"))
                                       (not (get first-item "hierarchy_lvl4"))
                                       (not (get first-item "hierarchy_lvl5")))
                ;; If first item is h1-only and used as header, skip it in children
                children-to-render (if first-is-h1-only?
                                     (rest items)
                                     items)]
            [:div {:class "rounded-2xl p-4 pr-5 pb-3 pl-5 bg-surface-alt"}
             ;; Group header
             (render-group-header first-item group-info start-index)
             ;; Child items container with border-top
             [:div {:class "border-t border-outline"}
              (map-indexed
               (fn [idx item]
                 (let [is-last? (= idx (dec (count children-to-render)))
                       is-first? (= idx 0)]
                   (render-result-item item (+ start-index 1 idx) true is-last? is-first?)))
               children-to-render)]])

          ;; Ungrouped results - show as individual items without dividers
          [:div {:class "rounded-2xl p-4 pr-5 pb-3 pl-5 space-y-2 bg-surface-alt"}
           (map-indexed
            (fn [idx item]
              (let [is-last? (= idx (dec (count items)))]
                (render-result-item item (+ start-index idx) false is-last?)))
            items)])))))

(defn meilisearch-dropdown [ctx]
  (let [request (:request ctx)
        query (or (get-in request [:query-params "q"])
                  (get-in request [:params "q"])
                  (get-in request [:query-params :q])
                  (get-in request [:params :q])
                  "")
        current-product (products/get-current-product ctx)
        product-index (get current-product :meilisearch-index "docs")
        results (when (and query (pos? (count query)))
                  (search-meilisearch ctx query product-index))]

    (if (empty? query)
      [:div] ;; Empty div when no query

      (if (empty? results)
        ;; No results found
        (render-no-results query)

        ;; Results found - group and render
        (let [groups (group-results-by-hierarchy results)]
          [:div {:id "meilisearch-dropdown"
                 :class (str dropdown-class " max-h-[767px] overflow-y-auto")}
           [:div {:class "space-y-4"}
            ;; Render grouped results
            (render-search-results groups)]])))))

(defn meilisearch-endpoint [ctx]
  (let [request (:request ctx)
        query (or (get-in request [:query-params "q"])
                  (get-in request [:params "q"])
                  (get-in request [:query-params :q])
                  (get-in request [:params :q])
                  "")]
    (log/info "meilisearch-endpoint called" {:uri (:uri request)
                                             :query-params (:query-params request)
                                             :context-keys (keys ctx)})
    (let [result (meilisearch-dropdown ctx)]
      {:status 200
       :headers {"content-type" "text/html; charset=utf-8"
                 "Cache-Control" "no-cache, no-store, must-revalidate"}
       :body (utils/->html result)})))

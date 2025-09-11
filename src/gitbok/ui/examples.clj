(ns gitbok.ui.examples
  (:require
   [gitbok.ui.layout :as layout]
   [gitbok.products :as products]
   [gitbok.examples.indexer :as indexer]
   [gitbok.http]
   [clojure.string :as str]
   [hiccup2.core]
   [clojure.tools.logging :as log]))

(defn render-example-card
  "Render a single example card"
  [{:keys [id title description features languages github_url]}]
  [:div.border.border-tint-2.rounded-lg.p-6.hover:shadow-lg.transition-shadow.duration-200.bg-white
   {:data-example-id id
    :class "example-card"}
   ;; Title as clickable link
   [:a {:href github_url :target "_blank" :rel "noopener noreferrer"}
    [:h3.text-lg.font-semibold.mb-2.text-tint-12.hover:text-primary-9.transition-colors title]]
   [:p.text-sm.text-tint-10.mb-4.line-clamp-3 description]

   ;; Languages tags - green color with lighter border
   (when (seq languages)
     [:div.flex.flex-wrap.gap-2.mb-2
      (for [lang languages]
        [:span.inline-flex.items-center.px-2.py-0.5.rounded.text-xs.font-medium.bg-success-2.text-success-11.border.border-success-3
         {:key lang}
         lang])])

   ;; Features tags - blue color, same style as languages
   (when (seq features)
     [:div.flex.flex-wrap.gap-2
      (for [feature features]
        [:span.inline-flex.items-center.px-2.py-0.5.rounded.text-xs.font-medium.bg-primary-2.text-primary-11.border.border-primary-4
         {:key feature}
         feature])])])

(defn render-filter-checkbox
  "Render a single filter checkbox"
  [type value label count checked?]
  [:label.flex.items-center.space-x-2.cursor-pointer.hover:bg-tint-1.px-2.py-1.rounded
   [:input.w-4.h-4.text-primary-9.border-tint-4.rounded.focus:ring-primary-5.filter-checkbox
    {:type "checkbox"
     :name type
     :value value
     :checked checked?
     ;; todo fix hardcode
     :hx-get "/docs/aidbox/examples-results"
     :hx-target "#examples-results"
     :hx-trigger "change"
     :hx-include "#examples-form"}]
   [:span.text-sm.text-tint-11 label]
   (when count
     [:span.text-xs.text-tint-10 (str "(" count ")")])])

(defn count-examples-by-filter
  "Count examples that have a specific filter value"
  [examples filter-type filter-value]
  (count (filter #(some #{filter-value} (or (get % (keyword filter-type)) [])) examples)))

(defn render-filters
  "Render filter sidebar"
  [examples features-list languages-list selected-languages selected-features]
  [:div.space-y-6
   ;; Languages filter - sorted by count
   [:div
    [:h4.text-sm.font-semibold.text-tint-12.mb-3 "Languages"]
    [:div.space-y-1
     (let [langs-with-counts (map (fn [lang]
                                    [lang (count-examples-by-filter examples "languages" lang)])
                                  languages-list)
           sorted-langs (sort-by second > langs-with-counts)]
       (for [[lang count] sorted-langs]
         ^{:key lang}
         (render-filter-checkbox "languages" lang lang
                                 count
                                 (contains? selected-languages lang))))]]

   ;; Features filter - sorted by count with search
   [:div
    [:h4.text-sm.font-semibold.text-tint-12.mb-3 "Features"]
    [:input.w-full.px-3.py-1.5.mb-3.text-sm.border.border-tint-3.rounded-md.focus:outline-none.focus:ring-2.focus:ring-primary-5
     {:type "text"
      :id "features-search"
      :placeholder "Search features..."
      :oninput "filterFeatures(this.value)"}]
    [:div.space-y-1.max-h-96.overflow-y-auto
     {:id "features-list"}
     (let [features-with-counts (map (fn [feature]
                                       [feature (count-examples-by-filter examples "features" feature)])
                                     features-list)
           sorted-features (sort-by second > features-with-counts)]
       (for [[feature count] sorted-features]
         ^{:key feature}
         [:div.feature-item
          {:data-feature-name (str/lower-case feature)}
          (render-filter-checkbox "features" feature feature
                                  count
                                  (contains? selected-features feature))]))]]

   ;; Note: filterFeatures function is now defined globally in ui-bundle.js
   ])

(defn render-search-bar [search-term]
  [:div.w-full
   [:div.relative.flex.items-center
    [:svg.absolute.left-3.w-5.h-5.text-tint-8.pointer-events-none
     {:fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
     [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2"
             :d "M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"}]]
    [:input#examples-search.w-full.px-4.py-3.pl-10.pr-4.text-tint-12.bg-white.border.border-tint-4.rounded-lg.focus:outline-none.focus:ring-2.focus:ring-primary-5.focus:border-transparent
     {:type "text"
      :name "search"
      :placeholder "Search examples by title, description, or tags..."
      :autocomplete "off"
      :value (or search-term "")
      ;; todo fix hardcode
      :hx-get "/docs/aidbox/examples-results"
      :hx-target "#examples-results"
      :hx-trigger "keyup changed delay:500ms"
      :hx-include "#examples-form"}]]])

(defn render-examples-grid [examples]
  [:div#examples-grid.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-6
   (if (seq examples)
     (for [example examples]
       ^{:key (:id example)}
       (render-example-card example))
     [:div.col-span-full.text-center.py-12
      [:p.text-tint-8 "No examples match your filters"]])])


(defn filter-examples
  "Filter examples based on search and filter criteria"
  [examples search-term languages features]
  (let [search-lower (when (and search-term (not (str/blank? search-term)))
                       (str/lower-case search-term))]
    (filter (fn [example]
              (and
               ;; Search filter
               (or (nil? search-lower)
                   (let [example-features (or (:features example) [])
                         example-languages (or (:languages example) [])
                         searchable (str/lower-case
                                     (str (:title example) " "
                                          (:description example) " "
                                          (str/join " " example-features) " "
                                          (str/join " " example-languages)))]
                     (str/includes? searchable search-lower)))
               ;; Language filter
               (or (empty? languages)
                   (let [example-languages (or (:languages example) [])]
                     (some #(contains? languages %) example-languages)))
               ;; Feature filter
               (or (empty? features)
                   (let [example-features (or (:features example) [])]
                     (some #(contains? features %) example-features)))))
            examples)))

(defn render-examples-results
  "Render filtered examples results"
  [examples search-term selected-languages selected-features]
  (let [filtered-examples (filter-examples examples search-term selected-languages selected-features)]

    [:div#examples-results
     [:div.mb-4
      [:p.text-sm.text-tint-10
       (str "Showing " (count filtered-examples)
            (when (or search-term (seq selected-languages) (seq selected-features))
              (str " of " (count examples)))
            " examples")]]

     (render-examples-grid filtered-examples)]))

(defn render-examples-content
  "Render the full examples content"
  [context request]
  (let [ctx-with-product (products/set-current-product-id context "aidbox")
        examples-data (indexer/get-examples ctx-with-product)
        {:keys [examples features_list languages_list]} examples-data

        ;; Get query parameters
        params (:query-params request)
        _ (log/info "request params" {:params params
                                      :query-string (:query-string request)
                                      :headers (:headers request)})
        search-term (get params :search "")
        ;; For multiple checkbox values, they come as a vector
        selected-languages (into #{} (let [langs (get params :languages)]
                                       (cond
                                         (nil? langs) []
                                         (string? langs) [langs]
                                         (sequential? langs) langs
                                         :else [])))
        selected-features (into #{} (let [feats (get params :features)]
                                      (cond
                                        (nil? feats) []
                                        (string? feats) [feats]
                                        (sequential? feats) feats
                                        :else [])))

        ;; Filter examples
        filtered-examples (filter-examples examples search-term selected-languages selected-features)
        _ (log/info "filtering results" {:total-examples (count examples)
                                         :filtered-count (count filtered-examples)
                                         :search-term search-term
                                         :selected-languages selected-languages
                                         :selected-features selected-features})]

    [:div#examples-content
     ;; Wrap everything in a form for HTMX
     [:form {:id "examples-form"
             ;; todo fix
             :hx-get "/docs/aidbox/examples-results"
             :hx-target "#examples-results"
             :hx-push-url "false"}

      ;; Search bar
      [:div.mb-6
       (render-search-bar search-term)]

      ;; Main content
      [:div.flex.flex-col.lg:flex-row.gap-6
       ;; Filters sidebar
       [:div.lg:w-64.flex-shrink-0
        [:div.bg-white.rounded-lg.border.border-tint-3.p-4
         [:div.flex.items-center.justify-between.mb-4
          [:h3.text-lg.font-semibold.text-tint-12 "Filters"]
          (when (or (seq selected-languages) (seq selected-features) search-term)
            [:button.text-sm.text-primary-9.hover:text-primary-10.cursor-pointer
             {:type "button"
              :onclick "document.getElementById('examples-search').value='';
                       document.querySelectorAll('.filter-checkbox').forEach(cb => cb.checked = false);
                       htmx.ajax('GET', '/docs/aidbox/examples-results', {target: '#examples-results'})"}
             "Clear all"])]
         (when (and features_list languages_list)
           (render-filters filtered-examples features_list languages_list
                           selected-languages selected-features))]]

       [:div.flex-1
        (render-examples-results examples search-term selected-languages selected-features)]]]]))

(defn render-examples-page
  "Main examples page component - full page"
  [context request]
  [:div
   ;; Header
   [:div.mb-8
    [:h1.text-3xl.font-bold.text-tint-12.mb-3 "Aidbox Examples"]
    [:p.text-lg.text-tint-10
     "Browse Aidbox integration examples and sample applications"]]

   ;; Content
   (render-examples-content context request)])

(defn examples-results-handler
  "HTTP handler for examples results (HTMX partial updates)"
  [context request]
  (let [ctx-with-product (products/set-current-product-id context "aidbox")
        examples-data (indexer/get-examples ctx-with-product)
        {:keys [examples]} examples-data

        ;; Get query parameters
        params (:query-params request)
        search-term (get params :search "")
        ;; For multiple checkbox values, they come as a vector
        selected-languages (into #{} (let [langs (get params :languages)]
                                       (cond
                                         (nil? langs) []
                                         (string? langs) [langs]
                                         (sequential? langs) langs
                                         :else [])))
        selected-features (into #{} (let [feats (get params :features)]
                                      (cond
                                        (nil? feats) []
                                        (string? feats) [feats]
                                        (sequential? feats) feats
                                        :else [])))

        content (render-examples-results examples search-term selected-languages selected-features)]
    (gitbok.http/response1 content)))

(defn examples-handler
  "HTTP handler for examples page"
  [context request]
  (let [content (render-examples-page context request)
        ;; Use shared page-wrapper for consistent layout
        full-page (layout/page-wrapper context content)]
    ;; Always return full page through layout
    (gitbok.http/response1
     (layout/document
      context
      full-page
      {:title "Aidbox examples"
       :description "Browse Aidbox integration examples and sample applications"
       :canonical-url
       (gitbok.http/get-absolute-url
        context
        (:uri request))
       :og-preview nil
       :lastmod nil
       :favicon-url
       (gitbok.http/get-product-prefixed-url context "/favicon.ico")}))))

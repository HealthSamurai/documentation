(ns gitbok.ui.examples
  (:require
   [gitbok.http :as http]
   [gitbok.ui.layout :as layout]
   [gitbok.ui.main-navigation :as main-navigation]
   [gitbok.ui.tags :as tags]
   [gitbok.products :as products]
   [gitbok.examples.indexer :as indexer]
   [clojure.string :as str]
   [hiccup2.core]
   [clojure.tools.logging :as log]
   [ring.util.codec]))

(defn render-example-card
  "Render a single example card"
  [{:keys [id title description features languages github_url]}]
  [:a {:href github_url :target "_blank" :rel "noopener noreferrer"
       :class "block"}
   [:div.rounded-lg.p-6.hover:shadow-lg.transition-shadow.duration-200.flex.flex-col.gap-4
    {:class "border border-[#E7E9EF] bg-gradient-to-b from-white to-[#F8F9FA] example-card h-[275px] w-[349px]"
     :data-example-id id}
    ;; Content section (title and description)
    [:div.flex.flex-col.gap-2
     ;; Title - truncate after 2 lines
     [:h3 {:class "text-lg font-medium leading-7 tracking-[-0.03em] text-[#1F1C1C] hover:text-primary-9 transition-colors line-clamp-2"} title]
     ;; Description - truncate after 3 lines
     [:p {:class "text-sm leading-[22.75px] tracking-[-0.02em] text-[#353B50] line-clamp-3"} description]]

    ;; Tags container - pushed to bottom with mt-auto
    [:div.mt-auto.flex.flex-col.gap-2
     ;; Languages tags - clickable with green color
     (when (seq languages)
       [:div.flex.flex-wrap.gap-2
        (for [lang languages]
          (tags/render-tag {:text lang
                           :key lang
                           :variant :language
                           :data-type "languages"
                           :data-value lang
                           :onclick (str "event.preventDefault(); event.stopPropagation(); toggleFilter('languages', '" lang "'); return false;")}))])

     ;; Features tags - clickable
     (when (seq features)
       [:div.flex.flex-wrap.gap-2
        (for [feature features]
          (tags/render-tag {:text feature
                           :key feature
                           :variant :clickable
                           :data-type "features"
                           :data-value feature
                           :onclick (str "event.preventDefault(); event.stopPropagation(); toggleFilter('features', '" feature "'); return false;")}))])]]])

(defn render-filter-checkbox
  "Render a single filter checkbox"
  [type value label count checked?]
  [:label.flex.items-center.space-x-2.cursor-pointer.hover:bg-tint-1.px-2.py-1.rounded
   [:input.w-4.h-4.text-primary-9.border-tint-4.rounded.focus:ring-primary-5.filter-checkbox
    {:type "checkbox"
     :name type
     :value value
     :checked checked?
     :data-filter-type type
     :data-filter-value value
     :onchange "updateFiltersAndURL()"}]
   [:span {:class "text-sm text-[#353B50] leading-5"} label]
   (when count
     [:span {:class "text-sm text-[#353B50] leading-5"}
      (str "(" count ")")])])

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
    [:h4 {:class "text-sm font-semibold text-[#1F1C1C] leading-5 mb-3"} "Languages"]
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
    [:h4 {:class "text-sm font-semibold text-[#1F1C1C] leading-5 mb-3"} "Features"]
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
      :name "q"
      :placeholder "Search examples by title, description, or tags..."
      :autocomplete "off"
      :value (or search-term "")
      :oninput "updateFiltersAndURL(true)"}]]])

(defn render-examples-grid [examples]
  [:div#examples-grid
   {:class "w-full flex flex-wrap gap-6"}
   (if (seq examples)
     (for [example examples]
       ^{:key (:id example)}
       (render-example-card example))
     ;; Empty state - just empty space to maintain layout
     [:div
      {:class "min-h-[300px] w-full"}])])

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

    [:div#examples-results.w-full
     [:div.mb-4
      [:p {:class "text-sm text-[#808492] leading-5"}
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

        ;; Get query parameters from URL
        query-string (:query-string request)
        params (if query-string
                 (ring.util.codec/form-decode query-string)
                 {})
        _ (log/info "request params" {:params params
                                      :query-string query-string})

        ;; Parse q parameter
        search-term (get params "q" "")

        ;; Parse comma-separated languages and features
        selected-languages (into #{}
                                 (when-let [langs (get params "languages")]
                                   (if (string? langs)
                                     (when-not (str/blank? langs)
                                       (str/split langs #","))
                                     [])))
        selected-features (into #{}
                                (when-let [feats (get params "features")]
                                  (if (string? feats)
                                    (when-not (str/blank? feats)
                                      (str/split feats #","))
                                    [])))

        ;; Filter examples
        filtered-examples (filter-examples examples search-term selected-languages selected-features)
        _ (log/info "filtering results" {:total-examples (count examples)
                                         :filtered-count (count filtered-examples)
                                         :search-term search-term
                                         :selected-languages selected-languages
                                         :selected-features selected-features})]

    [:div#examples-content.w-full
     ;; JavaScript for updating URL and fetching results
     [:script
      (hiccup2.core/raw
       "let searchTimeout;

        function updateFiltersAndURL(isFromSearch = false) {
          // Clear existing timeout if updating from search
          if (isFromSearch && searchTimeout) {
            clearTimeout(searchTimeout);
          }

          const doUpdate = () => {
            const searchInput = document.getElementById('examples-search');
            const languageCheckboxes = document.querySelectorAll('input[name=\"languages\"]:checked');
            const featureCheckboxes = document.querySelectorAll('input[name=\"features\"]:checked');

            const params = new URLSearchParams();

            // Add search query
            if (searchInput && searchInput.value) {
              params.set('q', searchInput.value);
            }

            // Add selected languages
            const languages = Array.from(languageCheckboxes).map(cb => cb.value);
            if (languages.length > 0) {
              params.set('languages', languages.join(','));
            }

            // Add selected features
            const features = Array.from(featureCheckboxes).map(cb => cb.value);
            if (features.length > 0) {
              params.set('features', features.join(','));
            }

            // Update URL without page reload
            const newUrl = window.location.pathname + (params.toString() ? '?' + params.toString() : '');
            window.history.replaceState({}, '', newUrl);

            // Fetch updated results via HTMX
            const resultsUrl = window.location.pathname.replace('/examples', '/examples-results');
            htmx.ajax('GET', resultsUrl + '?' + params.toString(), {
              target: '#examples-results',
              swap: 'innerHTML'
            });
          };

          // If from search input, debounce the update
          if (isFromSearch) {
            searchTimeout = setTimeout(doUpdate, 500);
          } else {
            // For checkboxes, update immediately
            doUpdate();
          }
        }

        function toggleFilter(type, value) {
          // Find the corresponding checkbox
          const checkbox = document.querySelector(`input[name=\"${type}\"][value=\"${value}\"]`);
          if (checkbox) {
            // Toggle the checkbox state
            checkbox.checked = !checkbox.checked;
            // Update filters
            updateFiltersAndURL();
          }
        }

        function clearAllFilters() {
          document.getElementById('examples-search').value = '';
          document.querySelectorAll('.filter-checkbox').forEach(cb => cb.checked = false);
          window.history.replaceState({}, '', window.location.pathname);
          updateFiltersAndURL();
        }")]

     ;; Search bar
     [:div.mb-6
      (render-search-bar search-term)]

     ;; Main content
     [:div.flex.flex-col.lg:flex-row.gap-6.w-full
      ;; Filters sidebar
      [:div.lg:w-64.flex-shrink-0
       [:div {:class "bg-white rounded-lg border border-[#E7E9EF] p-[17px]"}
        [:div.flex.items-center.justify-between.mb-4
         [:h3 {:class "text-xl font-medium leading-8 text-[#1F1C1C]"} "Filters"]
         (when (or (seq selected-languages) (seq selected-features) (not (str/blank? search-term)))
           [:button.text-sm.text-primary-9.hover:text-primary-10.cursor-pointer
            {:type "button"
             :onclick "clearAllFilters()"}
            "Clear all"])]
        (when (and features_list languages_list)
          ;; Pass the complete examples list for correct count calculation
          (render-filters examples features_list languages_list
                          selected-languages selected-features))]]

      [:div.flex-1.min-w-0
       (render-examples-results examples search-term selected-languages selected-features)]]]))

(defn render-examples-page
  "Main examples page component - full page"
  [context request]
  [:div.w-full
   ;; Header
   [:div.mb-8
    [:h1 {:class "text-[28px] font-semibold leading-9 tracking-[-0.03em] text-[#1F1C1C] mb-3"} "Aidbox Examples"]
    [:p {:class "text-base leading-6 text-[#353B50]"}
     "Browse Aidbox integration examples and sample applications"]]

   ;; Content
   (render-examples-content context request)])

(defn examples-results-handler
  "HTTP handler for examples results (HTMX partial updates)"
  [ctx]
  (let [request (:request ctx)
        ctx-with-product (products/set-current-product-id ctx "aidbox")
        examples-data (indexer/get-examples ctx-with-product)
        {:keys [examples]} examples-data

        ;; Get query parameters from URL
        query-string (:query-string request)
        params (if query-string
                 (ring.util.codec/form-decode query-string)
                 {})

        ;; Parse q parameter
        search-term (get params "q" "")

        ;; Parse comma-separated languages and features
        selected-languages (into #{}
                                 (when-let [langs (get params "languages")]
                                   (if (string? langs)
                                     (when-not (str/blank? langs)
                                       (str/split langs #","))
                                     [])))
        selected-features (into #{}
                                (when-let [feats (get params "features")]
                                  (if (string? feats)
                                    (when-not (str/blank? feats)
                                      (str/split feats #","))
                                    [])))

        content (render-examples-results examples search-term selected-languages selected-features)]
    (http/response1 content)))

(defn examples-handler
  "HTTP handler for examples page"
  [ctx]
  (let [request (:request ctx)
        content (render-examples-page ctx request)
        ;; Custom page layout for examples to maintain full width
        full-page [:div.min-h-screen.flex.flex-col
                   (main-navigation/nav ctx)
                   [:div.mobile-menu-overlay]
                   [:div.flex-1
                    [:div.max-w-screen-2xl.mx-auto.w-full.px-4.md:px-8.py-8
                     [:main#content.w-full
                      content]]]
                   (layout/site-footer ctx)]]
    ;; Always return full page through layout
    (http/response1
     (layout/document
      ctx
      full-page
      {:title "Aidbox examples"
       :description "Browse Aidbox integration examples and sample applications"
       :canonical-url
       (http/get-absolute-url
        ctx
        (:uri request))
       :og-preview nil
       :lastmod nil
       :favicon-url
       (http/get-product-prefixed-url ctx "/favicon.ico")}))))

(ns gitbok.ui.examples
  (:require [gitbok.http :as http]
            [gitbok.ui.layout :as layout]
            [gitbok.ui.main-navigation :as main-navigation]
            [gitbok.products :as products]
            [gitbok.examples.indexer :as indexer]
            [gitbok.utils :as utils]
            [system]
            [clojure.string :as str]
            [clojure.data.json :as json]
            [hiccup.core]
            [klog.core :as log]))

(defn render-example-card
  "Render a single example card"
  [{:keys [id title description features languages github_url readme_url]}]
  [:div.border.border-tint-2.rounded-lg.p-6.hover:shadow-lg.transition-shadow.duration-200.bg-white
   {:data-example-id id
    :class "example-card"}
   ;; Title as clickable link
   [:a {:href github_url :target "_blank" :rel "noopener noreferrer"}
    [:h3.text-lg.font-semibold.mb-2.text-tint-12.hover:text-primary-9.transition-colors title]]
   [:p.text-sm.text-tint-10.mb-4.line-clamp-3 description]

   ;; Languages tags - green color with lighter border
   [:div.flex.flex-wrap.gap-2.mb-2
    (for [lang languages]
      [:span.inline-flex.items-center.px-2.py-0.5.rounded.text-xs.font-medium.bg-success-2.text-success-11.border.border-success-3
       {:key lang}
       lang])]

   ;; Features tags - blue color, same style as languages
   [:div.flex.flex-wrap.gap-2
    (for [feature features]
      [:span.inline-flex.items-center.px-2.py-0.5.rounded.text-xs.font-medium.bg-primary-2.text-primary-11.border.border-primary-4
       {:key feature}
       feature])]])

(defn render-filter-checkbox
  "Render a single filter checkbox"
  [type value label count checked?]
  [:label.flex.items-center.space-x-2.cursor-pointer.hover:bg-tint-1.px-2.py-1.rounded
   [:input.w-4.h-4.text-primary-9.border-tint-4.rounded.focus:ring-primary-5.filter-checkbox
    {:type "checkbox"
     :name type
     :value value
     :checked checked?
     :hx-get "/docs/aidbox/examples"
     :hx-target "#examples-results"
     :hx-trigger "change"
     :hx-include "#examples-form"}]
   [:span.text-sm.text-tint-11 label]
   (when count
     [:span.text-xs.text-tint-10 (str "(" count ")")])])

(defn count-examples-by-filter
  "Count examples that have a specific filter value"
  [examples filter-type filter-value]
  (count (filter #(some #{filter-value} (get % (keyword filter-type))) examples)))

(defn render-filters
  "Render filter sidebar"
  [examples features-list languages-list selected-languages selected-features]
  [:div.space-y-6
   ;; Languages filter
   [:div
    [:h4.text-sm.font-semibold.text-tint-12.mb-3 "Languages"]
    [:div.space-y-1
     (for [lang languages-list]
       ^{:key lang}
       (render-filter-checkbox "languages" lang lang
                               (count-examples-by-filter examples "languages" lang)
                               (contains? selected-languages lang)))]]

   ;; Features filter
   [:div
    [:h4.text-sm.font-semibold.text-tint-12.mb-3 "Features"]
    [:div.space-y-1.max-h-96.overflow-y-auto
     (for [feature features-list]
       ^{:key feature}
       (render-filter-checkbox "features" feature feature
                               (count-examples-by-filter examples "features" feature)
                               (contains? selected-features feature)))]]])

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
      :hx-get "/docs/aidbox/examples"
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

(defn format-timestamp
  "Format ISO timestamp to USA format"
  [timestamp]
  (when timestamp
    (try
      (let [instant (java.time.Instant/parse timestamp)
            formatter (java.time.format.DateTimeFormatter/ofPattern "MMMM d, yyyy 'at' h:mm a")
            zoned-time (.atZone instant (java.time.ZoneId/of "America/New_York"))]
        (.format formatter zoned-time))
      (catch Exception e
        timestamp))))

(defn filter-examples
  "Filter examples based on search and filter criteria"
  [examples search-term languages features]
  (let [search-lower (when (and search-term (not (str/blank? search-term)))
                       (str/lower-case search-term))]
    (filter (fn [example]
              (and
               ;; Search filter
               (or (nil? search-lower)
                   (let [searchable (str/lower-case
                                    (str (:title example) " "
                                         (:description example) " "
                                         (str/join " " (:features example)) " "
                                         (str/join " " (:languages example))))]
                     (str/includes? searchable search-lower)))
               ;; Language filter
               (or (empty? languages)
                   (some #(contains? languages %) (:languages example)))
               ;; Feature filter
               (or (empty? features)
                   (some #(contains? features %) (:features example)))))
            examples)))

(defn render-examples-results
  "Render just the results (for HTMX updates)"
  [context request]
  (let [ctx-with-product (products/set-current-product-id context "aidbox")
        examples-data (indexer/get-examples ctx-with-product)
        {:keys [examples features_list languages_list timestamp]} examples-data

        ;; Get query parameters
        params (:query-params request)
        _ (log/info ::request-params {:params params 
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
        _ (log/info ::filtering-results {:total-examples (count examples)
                                         :filtered-count (count filtered-examples)
                                         :search-term search-term
                                         :selected-languages selected-languages
                                         :selected-features selected-features})]

    [:div#examples-results
     [:div.mb-4.flex.items-center.justify-between
      [:p.text-sm.text-tint-10
       (str "Showing " (count filtered-examples)
            (when (or search-term (seq selected-languages) (seq selected-features))
              (str " of " (count examples)))
            " examples")]
      (when timestamp
        [:p.text-sm.text-tint-10.italic
         (str "Last updated: " (format-timestamp timestamp))])]
     
     (render-examples-grid filtered-examples)]))

(defn render-examples-content
  "Render the full examples content"
  [context request]
  (let [ctx-with-product (products/set-current-product-id context "aidbox")
        examples-data (indexer/get-examples ctx-with-product)
        {:keys [examples features_list languages_list timestamp]} examples-data

        ;; Get query parameters
        params (:query-params request)
        _ (log/info ::request-params {:params params 
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
        _ (log/info ::filtering-results {:total-examples (count examples)
                                         :filtered-count (count filtered-examples)
                                         :search-term search-term
                                         :selected-languages selected-languages
                                         :selected-features selected-features})]

    [:div#examples-content
     ;; Wrap everything in a form for HTMX
     [:form {:id "examples-form"
             :hx-get "/docs/aidbox/examples"
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
            [:button.text-sm.text-primary-9.hover:text-primary-10
             {:type "button"
              :onclick "document.getElementById('examples-search').value=''; 
                       document.querySelectorAll('.filter-checkbox').forEach(cb => cb.checked = false);
                       htmx.ajax('GET', '/docs/aidbox/examples', {target: '#examples-results'})"}
             "Clear all"])]
         (when (and features_list languages_list)
           (render-filters filtered-examples features_list languages_list
                         selected-languages selected-features))]]

      ;; Examples results section
      [:div.flex-1
       (render-examples-results context request)]]]]))

(defn render-examples-page
  "Main examples page component - full page"
  [context request]
  (let [ctx-with-product (products/set-current-product-id context "aidbox")
        examples-data (indexer/get-examples ctx-with-product)]
    [:div.w-full.bg-tint-1.min-h-screen.py-8
     [:div.container.mx-auto.px-4.max-w-7xl
      ;; Header
      [:div.mb-8
       [:h1.text-3xl.font-bold.text-tint-12.mb-3 "Examples"]
       [:p.text-lg.text-tint-10
        "Browse Aidbox integration examples and sample applications"]]

      ;; Content
      (render-examples-content context request)]]))

(defn examples-handler
  "HTTP handler for examples page"
  [context request]
  (let [uri (:uri request)
        ;; Check if this is an HTMX request
        htmx-request? (get-in request [:headers "hx-request"])

        content (if htmx-request?
                  ;; For HTMX requests, return just the results
                  (render-examples-results context request)
                  ;; For regular requests, return the full page
                  (render-examples-page context request))]

    (if htmx-request?
      ;; Return partial HTML for HTMX
      {:status 200
       :headers {"content-type" "text/html"}
       :body (hiccup.core/html content)}
      ;; Return full page
      (let [full-page [:div
                       ;; Main navigation header
                       (main-navigation/nav context)
                       [:div.mobile-menu-overlay]
                       ;; Content without left sidebar - full width
                       [:div.flex.max-w-screen-2xl.mx-auto.site-full-width:max-w-full.items-start.overflow-visible.md:px-8
                        ;; Main content takes full width - no left navigation
                        [:main#content.flex-1.w-full
                         [:div.flex.items-start
                          [:article.article__content.min-w-0.flex-1.transform-3d
                           [:div.mx-auto.max-w-full
                            content]]]]]]]
        (http/response1
         (layout/document
          context
          full-page
          {:title "Examples"
           :description "Browse Aidbox integration examples and sample applications"
           :canonical-url (http/get-absolute-url context uri)
           :og-preview (http/get-absolute-url
                        context
                        "/public/og-preview/aidbox/examples.png")
           :favicon-url (http/get-product-prefixed-url context "/favicon.ico")}))))))

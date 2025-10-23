(ns gitbok.ui.breadcrumb
  (:require
   [gitbok.http :as http]
   [clojure.string :as str]
   [gitbok.products]
   [gitbok.state :as state]
   [gitbok.ui.page-actions :as page-actions]))

(defn find-breadcrumb-path
  "Recursively searches for an item with matching href in the summary tree.
   Returns a vector of items representing the path from root to the matching item."
  ([summary target-href]
   (find-breadcrumb-path summary target-href []))
  ([node target-href path]
   (cond
     (nil? node) nil

     ;; If this is a map with :href matching our target
     (and (map? node) (:href node))
     (if (= (:href node) target-href)
       (conj path node)
       ;; Search in children
       (when-let [children (:children node)]
         (some #(find-breadcrumb-path % target-href (conj path node)) children)))

     ;; If this is a sequence (like the root summary), search each item
     (sequential? node)
     (some #(find-breadcrumb-path % target-href path) node)

     ;; Otherwise, check for children
     :else
     (when-let [children (:children node)]
       (some #(find-breadcrumb-path % target-href path) children)))))

(defn render-breadcrumb-nav
  "Renders the breadcrumb navigation element"
  [context parent-pages section-title]
  [:nav {:aria-label "Breadcrumb"
         :class "flex-1"}
   [:ol {:class "flex flex-wrap items-center"}
    (let [items (vec (concat
                      ;; Add section title as first item if available
                      (when (and section-title (not (str/blank? section-title)))
                        [{:type :section :title section-title}])
                      ;; Add parent pages
                      (when parent-pages
                        (map (fn [page]
                               {:type :page
                                :title (get-in page [:parsed :title])
                                :href (:href page)
                                :relative-href (str/replace-first (get-in page [:parsed :href] "") #"^/" "")})
                             parent-pages))))]
      (interpose
       [:span {:class "text-xs font-semibold leading-none text-breadcrumb-separator mx-2"} "/"]
       (map-indexed
        (fn [idx item]
          (if (= :section (:type item))
            ;; Section title without link
            [:li {:key idx
                  :class "flex items-center gap-2 bg-breadcrumb-bg rounded-md px-2 py-0.5"}
             [:span {:class "text-sm font-normal leading-6 text-on-surface"}
              (:title item)]]
            ;; Parent page with link
            [:li {:key idx
                  :class "flex items-center gap-2 bg-breadcrumb-bg rounded-md px-2 py-0.5"}
             [:a {:href (:href item)
                  :hx-get (http/get-partial-product-prefixed-url context (:relative-href item))
                  :hx-target "#content"
                  :hx-push-url (:href item)
                  :hx-swap "outerHTML"
                  :class "text-sm font-normal leading-6 text-on-surface hover:text-on-surface-strong"}
              (:title item)]]))
        items)))]])

(defn render-breadcrumb-container
  "Renders the container with breadcrumb and/or copy page dropdown"
  [breadcrumb-nav dropdown]
  [:div {:class "flex items-center gap-2 mb-[11px]"}
   (when breadcrumb-nav breadcrumb-nav)
   (when dropdown dropdown)])

(defn breadcrumb [context uri & [filepath]]
  (when (and uri (not (str/blank? uri)))
    (let [summary (state/get-summary context)
          ;; Build full URI with product prefix for matching
          full-uri (http/get-product-prefixed-url context uri)
          ;; Find the breadcrumb path in the navigation structure
          breadcrumb-path (when summary (find-breadcrumb-path summary full-uri))
          ;; Get the current page (last item in path)
          current-page (last breadcrumb-path)
          ;; Get section title from current page
          section-title (:section-title current-page)
          ;; Get parent pages (all except current page)
          parent-pages (when (> (count breadcrumb-path) 1)
                         (drop-last breadcrumb-path))

          show-breadcrumb? (and (not (str/starts-with? (str/lower-case uri) "readme/"))
                                (or (and section-title (not (str/blank? section-title)))
                                    (seq parent-pages)))
          show-dropdown? (boolean filepath)

          breadcrumb-nav (when show-breadcrumb?
                           (render-breadcrumb-nav context parent-pages section-title))
          dropdown (when show-dropdown?
                     (page-actions/page-actions-dropdown context uri filepath))]

      (when (or show-breadcrumb? show-dropdown?)
        (render-breadcrumb-container breadcrumb-nav dropdown)))))

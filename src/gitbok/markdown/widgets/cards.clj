(ns gitbok.markdown.widgets.cards
  (:require
   [gitbok.state :as state]
   [clojure.string :as str]
   [hiccup2.core]
   [gitbok.http]
   [gitbok.indexing.core :as indexing]))

(defn render-cards-from-table
  [context filepath [_ _ _ tbody]]
  (let [rows (->> tbody
                  (filter vector?)
                  (filter #(= :tr (first %))))]
    [:div
     {:class "grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mt-4"}
     (for [row rows
           :let [cells (->> row (filter vector?)
                            (mapv (fn [a] (next (next a)))))
                 ;; Check if first cell is empty or contains a badge
                 first-cell (first cells)
                 first-cell-empty? (or (nil? first-cell)
                                       (and (seq? first-cell) (empty? first-cell))
                                       (and (seq? first-cell)
                                            (string? (last first-cell))
                                            (empty? (str/trim (last first-cell)))))
                 has-badge? (and first-cell
                                 (not first-cell-empty?)
                                 (let [content (if (seq? first-cell) (last first-cell) first-cell)]
                                   (or (and (vector? content) (= :mark (first content)))
                                       (and (string? content)
                                            (< (count content) 10)
                                            (re-matches #"(?i)^(beta|alpha|new|preview)$" content)))))

                 ;; Adjust cell assignment based on table structure
                 ;; For modules table (5 cells, first empty): [empty, title, desc, empty, link]
                 ;; For quickstarts (5 cells): [title, desc, footer, image, link]
                 ;; For platform (3 cells): [title, desc, image]
                 [badge one two three four five] (cond
                                                   ;; First cell has badge content
                                                   has-badge? cells
                                                   ;; First cell is empty (modules table format) - shift cells
                                                   first-cell-empty? (into [nil] (rest cells))
                                                   ;; Standard format (no badge column)
                                                   :else (cons nil cells))

                 pic1 (when four
                        (first (filterv
                                #(and (sequential? %) (= (first %) :a))
                                four)))
                 pic2 (when five
                        (first (filterv
                                #(and (sequential? %) (= (first %) :a))
                                five)))

                 pic-href1 (get-in pic1 [1 :href])
                 pic-href2 (get-in pic2 [1 :href])

                 ;; Extract the actual link for navigation
                 ;; For Modules table: link is in the 5th column (five)
                 ;; For Quickstarts: link is also in 5th column (five)
                 ;; For Support table: link is in the 4th column (four)
                 navigation-link (or
                                  ;; Check 5th column first (Modules, Quickstarts)
                                  (when five
                                    (let [five-content (if (seq? five) (last five) five)]
                                      (when (and (vector? five-content)
                                                 (= :a (first five-content)))
                                        (get-in five-content [1 :href]))))
                                  ;; Check 4th column (Support table)
                                  (when four
                                    (let [four-content (if (seq? four) (last four) four)]
                                      (when (and (vector? four-content)
                                                 (= :a (first four-content))
                                                 ;; Make sure it's not an image link
                                                 (not (re-matches #".*(png|jpg|jpeg|svg)$"
                                                                  (get-in four-content [1 :href]))))
                                        (get-in four-content [1 :href])))))

                 pic-footer (when three
                              (let [three-content (if (seq? three) (last three) three)]
                                (when (and (vector? three-content)
                                           (= :a (first three-content)))
                                  (get-in three-content [1 :href]))))

                 pic-footer? (when pic-footer
                               (re-matches #".*(png|jpg|jpeg|svg)$" pic-footer))

                 ;; Use navigation-link if available, otherwise fall back to pic-footer
                 title-filepath (or navigation-link
                                    (when-not pic-footer? pic-footer))
                 ;; For external URLs (http/https), use them directly
                 ;; For internal paths, convert using indexing
                 title-href
                 (when title-filepath
                   (if (or (str/starts-with? title-filepath "http://")
                           (str/starts-with? title-filepath "https://"))
                     title-filepath
                     (indexing/page-link->uri context filepath title-filepath)))

                 img-href
                 (first (filter (fn [s]
                                  (when s (re-matches #".*(png|jpg|jpeg|svg)$" s)))
                                [pic-footer pic-href1 pic-href2]))

                 processed-footer
                 (when (and three (not pic-footer?))
                   (let [three-content (if (seq? three) (last three) three)]
                     (when (and (vector? three-content)
                                (= :a (first three-content)))
                       (let [footer-link three-content
                             opts (-> footer-link
                                      second
                                      (assoc :class "text-primary-9 hover:text-primary-10 hover:underline underline text-sm")
                                      (update
                                       :href
                                       #(indexing/filepath->href context filepath %)))]
                         [:a opts (nth footer-link 2)]))))

                 ;; Final href is title-href (processed) or nothing
                 href title-href

                 ;; Determine if card should be clickable
                 clickable? (boolean href)
                 ;; Check if href is external
                 external? (and href
                                (or (str/starts-with? href "http://")
                                    (str/starts-with? href "https://")))]]

       ;; Render card
       [:div {:class (str "card group flex hover:shadow-md transition-all duration-200 "
                          (when clickable? "cursor-pointer ")
                          "flex-col bg-tint-1 border border-tint-4 "
                          "hover:border-tint-5 "
                          "rounded-sm overflow-hidden h-full "
                          "min-h-[150px] md:max-h-[300px] "
                          "transition-colors duration-200 relative")
              :hx-get (when (and clickable? (not external?)) (str href "?partial=true"))
              :hx-target (when (and clickable? (not external?)) "#content")
              :hx-push-url (when (and clickable? (not external?)) href)
              :hx-swap (when (and clickable? (not external?)) "outerHTML")}
        ;; Hidden link for browser hover preview
        (when clickable?
          [:a {:href href
               :class "absolute inset-0 z-10"
               :style "font-size: 0; opacity: 0;"
               :onclick (if external?
                          nil ;; Let the browser handle external links normally
                          "event.preventDefault(); htmx.trigger(this.parentElement, 'click'); return false;")}
           "Link"])
        ;; Card content
        [:div {:class "relative z-20 flex flex-col h-full pointer-events-none"}
         (when img-href
           (let [processed-img-href
                 (cond
                   ;; External URLs - keep as is
                   (str/starts-with? img-href "http") img-href

                   ;; .gitbook/assets paths - normalize and add prefix
                   (str/includes? img-href ".gitbook/assets")
                   (let [normalized (str ".gitbook/assets" (last (str/split img-href #"\.gitbook/assets")))]
                     (str (state/get-config context :prefix "") "/" normalized))

                   ;; Other paths - keep as is for now
                   :else img-href)]
             [:img {:src processed-img-href :alt "card" :class "pointer-events-none"}]))
         [:div
          {:class (str "flex flex-col gap-0 p-4 flex-1 "
                       (when-not img-href "justify-start"))}
          (when badge
            (into [:div {:class "mb-2"}]
                  (if (seq? badge) badge [badge])))
          (when one
            (let [one-content (if (seq? one) (last one) one)]
              (when one-content
                (into [:div {:class "text-sm text-tint-11"}]
                      (if (seq? one-content) one-content [one-content])))))
          ;; Description
          (when two
            (let [two-content (if (seq? two) (last two) two)]
              (when two-content
                (into [:p {:class "text-tint-11 text-sm group-hover:text-tint-12"}]
                      (if (seq? two-content) two-content [two-content])))))
          (when processed-footer
            [:div {:class "text-sm mt-2 pointer-events-auto relative z-30"}
             processed-footer])]]])]))

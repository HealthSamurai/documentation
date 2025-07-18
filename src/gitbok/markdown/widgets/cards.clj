(ns gitbok.markdown.widgets.cards
  (:require
   [clojure.string :as str]
   [hiccup2.core]
   [gitbok.http]
   [uui]
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
                                       (and (string? (first first-cell))
                                            (empty? (str/trim (first first-cell)))))
                 has-badge? (and first-cell
                                 (not first-cell-empty?)
                                 (or (and (seq? first-cell)
                                          (some #(and (vector? %) (= :mark (first %))) first-cell))
                                     (and (string? (first first-cell))
                                          (< (count (first first-cell)) 10)
                                          (re-matches #"(?i)^(beta|alpha|new|preview)$" (first first-cell)))))

                 ;; Adjust cell assignment based on table structure
                 [badge one two three four five] (cond
                                                   ;; First cell has badge content
                                                   has-badge? cells
                                                   ;; First cell is empty (modules table format)
                                                   first-cell-empty? (cons nil (rest cells))
                                                   ;; Standard format (no badge column)
                                                   :else (cons nil cells))

                 pic1 (first (filterv
                              #(and (sequential? %) (= (first %) :a))
                              four))
                 pic2 (first (filterv
                              #(and (sequential? %) (= (first %) :a))
                              five))

                 pic-href1 (get-in pic1 [1 :href])
                 pic-href2 (get-in pic2 [1 :href])

                 pic-footer
                 (get-in
                  (first (filterv
                          #(and (sequential? %) (= (first %) :a))
                          three))
                  [1 :href])
                 pic-footer? (when pic-footer
                               (re-matches #".*(png|jpg|jpeg|svg)$" pic-footer))

                 title-filepath (when-not pic-footer? pic-footer)
                 title-href
                 (when title-filepath
                   (indexing/page-link->uri context filepath title-filepath))
                 img-href
                 (first (filter (fn [s]
                                  (when s (re-matches #".*(png|jpg|jpeg|svg)$" s)))
                                [pic-footer pic-href1 pic-href2]))

                 processed-footer
                 (when-not pic-footer?
                   (if (and (sequential? three)
                            (seq three)
                            (some #(and (vector? %) (= :a (first %))) three))
                     (let [footer-link (first (filter #(and (vector? %) (= :a (first %))) three))]
                       (when footer-link
                         (let [opts
                               (-> footer-link
                                   second
                                   (assoc :class "text-primary-9 hover:text-primary-10 hover:underline underline text-sm")
                                   (update
                                    :href
                                    #(indexing/filepath->href context filepath %)))]
                           [:a opts (nth footer-link 2)])))
                     nil))
                 href (or title-href title-filepath pic-href1 pic-href2)]]
       [:div {:href href
              :class "card group block hover:shadow-md transition-all duration-200 cursor-pointer
               flex flex-col bg-tint-1 border border-tint-4
               hover:border-tint-5
               rounded-sm overflow-hidden h-full
               min-h-[150px] md:max-h-[300px]
               transition-colors duration-200"
              :hx-get (str href "?partial=true")
              :hx-target "#content"
              ;; :hx-push-url "true"
              :hx-push-url (gitbok.http/get-absolute-url context href)
              :hx-swap "outerHTML"}
         (when img-href [:img {:src img-href :alt "card"}])
         [:div
          {:class
           (str "flex flex-col gap-0 p-4 flex-1 " (when-not img-href "justify-start"))}
          (when badge
            (into [:div {:class "mb-2"}]
                  (if (seq? badge) badge [badge])))
          (when one
            (into [:div {:class "text-sm text-tint-11"}]
                  (if (seq? one) one [one])))
          ;; Description
          (when two
            (into [:p {:class "text-tint-11
                       text-sm group-hover:text-tint-12"}]
                  (if (seq? two) two [two])))
          (when processed-footer
            [:div {:class "text-sm mt-2"} processed-footer])]])]))

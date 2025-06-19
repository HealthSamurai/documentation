(ns gitbok.markdown.widgets.cards
  (:require
   [gitbok.indexing.core :as indexing]))

(defn render-cards-from-table
  [context filepath [_ _ _ tbody]]
  (let [rows (->> tbody
                  (filter vector?)
                  (filter #(= :tr (first %))))]
    [:div
     {:class "grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"}
     (for [row rows
           :let [[title desc footer pic1 pic2]
                 (->> row (filter vector?)
                      (mapv (fn [a] (into [:div] (next (next a))))))
                 pic1 (first (filterv
                              #(and (sequential? %) (= (first %) :a))
                              pic1))
                 pic2 (first (filterv
                              #(and (sequential? %) (= (first %) :a))
                              pic2))
                 pic-href1 (get-in pic1 [1 :href])
                 pic-href2 (get-in pic2 [1 :href])
                 pic-footer
                 (get-in
                  (first (filterv
                          #(and (sequential? %) (= (first %) :a))
                          footer))
                  [1 :href])
                 pic-footer? (when pic-footer (re-matches #".*(png|jpg|jpeg|svg)$" pic-footer))
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
                   (if (and (sequential? footer) (= (first footer) :div))
                     (let [footer-content (second footer)]
                       (if (and (sequential? footer-content) (= (first footer-content) :a))
                         (let [opts
                               (-> footer-content
                                   second
                                   (assoc :class "link")
                                   (update
                                     :href
                                     #(indexing/filepath->href context filepath %)))]
                           [:div
                            [:a
                             opts
                             (nth footer-content 2)]])
                         footer))
                     footer))
                 href  (or title-href title-filepath pic-href1)]]
       [:div {:class "flex flex-col bg-white rounded-2xl shadow overflow-hidden h-full min-h-[300px]"}
        (when img-href [:img {:src img-href}])
        [:div
         {:class
          (str "flex flex-col gap-2 p-4 flex-1 "
               (when-not img-href "justify-start"))}
         [:a {:href href
              :class "text-lg hover:underline"} title]
         [:p {:class "text-gray-600 text-sm"} desc]
         processed-footer]])]))

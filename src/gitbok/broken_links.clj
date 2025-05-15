(ns gitbok.broken-links
  (:require
   [cheshire.core]
   [clojure.string :as str]

   [http]
   [system]
   [uui]
   [gitbok.ui]))

(defn collect-broken-links [context]
 ;; todo
  ;; (->> (gitbok.state/uri->file-idx context)
  ;;      #_#_(sort-by #(:path (second %)))
  ;;      (map (fn [[_ x]]
  ;;             (let [broken (gitbok.markdown/broken-links
  ;;                           (assoc context :uri (str/replace (:path x) #"^docs" ""))
  ;;                           (slurp (:path x)))]
  ;;               (assoc x :broken-links broken)))))
  []
  )

(defn broken-links-view [context request]
  (gitbok.ui/layout
   context request
   [:div
    (for [x (collect-broken-links context)]
      (when-not (empty? (:broken-links x))
        [:details {:class "w-full py-1"}
         [:summary {:class "w-full border-b border-gray-200 py-1 flex items-center space-x-4 cursor-pointer"}
          [:b {:class "flex-1"} (uui/raw (:title x))]
          [:a {:class "" :href (str/replace (:path x) #"^docs" "")} "link"]
          [:b {:class "text-red-500"} "(" (count (:broken-links x)) ")"]]
         [:div {:class "py-6 mb-4"}
          [:table.uui
           [:thead [:tr [:th "Title"] [:th "href"]]]
           [:tbody
            (for [bl (:broken-links x)]
              [:tr
               [:td (uui/raw (last bl))]
               [:td
                [:a {:href (first bl)}
                 [:span {:class "text-red-500"} (first bl)]]]])]]]]))]))

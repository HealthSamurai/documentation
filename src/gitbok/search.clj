(ns gitbok.search
  (:require [gitbok.indexing.core :as indexing]
            [cheshire.core]
            [http]
            [system]
            [uui]))

(defn search [context query]
  (indexing/search context query))

(defn page-view [result]
  (let [hit-by (:hit-by result)
        {:keys [title h1 h2 h3 h4 text]} (:hit result)]
    [:div.p-4.bg-white.rounded-lg.shadow-sm.hover:shadow-md.transition-shadow.border.border-gray-100.mb-3
     [:div.flex.flex-col.gap-2
      (when-not (= :title hit-by)
        [:div.text-xs.font-medium.text-gray-500.uppercase.tracking-wide
         (case hit-by
           :h1 "Found in heading 1"
           :h2 "Found in heading 2"
           :h3 "Found in heading 3"
           :h4 "Found in heading 4"
           "Found in content")])
      [:a.text-lg.font-medium.text-blue-600.hover:text-blue-800
       {:href (str "/" (:uri result))}
       title]
      (when-not (= :title hit-by)
        (case hit-by
          :h1
          [:div.text-base.font-medium.text-gray-800.mb-1 h1]
          :h2
          [:div.text-sm.text-gray-700.mb-1 [:span.text-gray-500 "→ "] h2]
          :h3
          [:div.text-sm.text-gray-600 [:span.text-gray-500 "→ "] h3]

          :h4
          [:div.text-sm.text-gray-600 [:span.text-gray-500 "→ "] h4]

          :text
          [:div.text-sm.text-gray-600 [:span.text-gray-500 "→ "] text]))]]))

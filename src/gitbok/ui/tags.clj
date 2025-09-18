(ns gitbok.ui.tags)

(defn render-tag
  "Render a reusable tag component with optional click handler"
  [{:keys [text onclick href variant key]}]
  (let [base-classes "inline-flex items-center px-2 py-0.5 h-[25px] rounded text-xs font-medium transition-colors"
        variant-classes (case (or variant :default)
                          :default "bg-[#EBEFF2B2] text-tint-11 hover:bg-[#DDE1E8] hover:text-[#7E8291]"
                          :clickable "bg-[#EBEFF2B2] text-tint-11 hover:bg-[#DDE1E8] hover:text-[#7E8291] cursor-pointer"
                          "bg-[#EBEFF2B2] text-tint-11 hover:bg-[#DDE1E8] hover:text-[#7E8291]")
        attrs {:class (str base-classes " " variant-classes)}
        attrs (if key (assoc attrs :key key) attrs)]
    (cond
      onclick [:span (assoc attrs :onclick onclick) text]
      href [:a (assoc attrs :href href) text]
      :else [:span attrs text])))

(defn render-tags
  "Render a collection of tags"
  [tags variant]
  [:div.flex.flex-wrap.gap-2
   (for [tag tags]
     (if (map? tag)
       (render-tag (assoc tag :variant variant))
       (render-tag {:text tag :key tag :variant variant})))])

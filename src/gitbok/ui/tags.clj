(ns gitbok.ui.tags)

(def default-tag-classes
  "Default tag styling classes"
  "bg-tag-bg text-on-surface-secondary hover:bg-outline-hover hover:text-on-surface-placeholder")

(defn render-tag
  "Render a reusable tag component with optional click handler"
  [{:keys [text onclick href variant key data-type data-value]}]
  (let [base-classes "inline-flex items-center px-2 py-0.5 h-[25px] rounded text-[13px] font-normal transition-colors"
        variant-classes (case (or variant :default)
                          :default default-tag-classes
                          :language "bg-tag-language-bg text-white hover:bg-tag-language-hover hover:text-white leading-[160%] tracking-[-0.03em] text-center"
                          :clickable (str default-tag-classes " cursor-pointer")
                          default-tag-classes)
        attrs {:class (str base-classes " " variant-classes)}
        attrs (if key (assoc attrs :key key) attrs)
        attrs (if data-type (assoc attrs :data-tag-type data-type) attrs)
        attrs (if data-value (assoc attrs :data-tag-value data-value) attrs)]
    (cond
      ;; For onclick, use span with cursor-pointer instead of button to avoid HTML nesting issues
      onclick [:span (assoc attrs :onclick onclick :style "cursor: pointer;") text]
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

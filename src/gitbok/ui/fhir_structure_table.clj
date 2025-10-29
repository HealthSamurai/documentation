(ns gitbok.ui.fhir-structure-table
  (:require
   [gitbok.ui.fhir-icons :as icons]))

(defn nest-by-level
  "Converts a flat list of elements with :lvl keys into a nested tree structure"
  [items]
  (loop [result []
         stack  []
         [x & xs] items]
    (if-not x
      result
      (let [node (assoc x :children [])
            lvl  (:lvl x)
            stack
            (loop [st stack]
              (if (empty? st)
                st
                (let [parent-lvl (:lvl (get-in result (peek st)))]
                  (if (>= parent-lvl lvl)
                    (recur (pop st))
                    st))))]

        (if (empty? stack)
          (let [idx     (count result)
                result' (conj result node)]
            (recur result'
                   (conj stack [idx])
                   xs))

          (let [parent-path   (peek stack)
                parent        (get-in result parent-path)
                updated-parent (update parent :children conj node)
                result'        (assoc-in result parent-path updated-parent)
                new-child-idx  (dec (count (:children updated-parent)))
                child-path     (conj parent-path :children new-child-idx)]

            (recur result'
                   (conj stack child-path)
                   xs)))))))

(defn name-cell
  "Renders the name cell with appropriate icon"
  [element]
  [:div {:class "flex pt-2 pb-1 ml-1"}
   [:div {:class "pt-[1px]"}
    (icons/type-icon element)]
   [:div {:class "pl-2"}
    (or (:name element) (:path element))
    (when (:union? element) "[x]")]])

(defn cardinality-cell
  "Renders the cardinality (e.g., 0..1, 0..*, 1..1)"
  [element]
  [:div {:class "flex flex-row h-full font-mono pt-2"}
   (str (or (:min element) 0) ".." (or (:max element) 1))])

(defn type-cell
  "Renders the type/datatype"
  [element]
  [:div {:class "flex flex-row h-full pt-2"}
   (:type element)])

(defn description-cell
  "Renders the description"
  [element parse-markdown-fn]
  [:div
   (when-let [v (:desc element)]
     [:div (parse-markdown-fn v)])])

(defn tree-node
  "Recursively renders a tree node with proper indentation and connecting lines"
  [{:keys [path children lvl] :as element} last-childs parse-markdown-fn]
  (if (empty? children)
    ;; Leaf node
    (let [last-child? (->> last-childs
                           (filter #(= path (:path %)))
                           (not-empty))]
      [:tr {:class "group"}
       [:td {:class "flex h-full pl-[15px] py-0 px-4 align-top"}
        [:div {:class "element flex"}
         (for [i (range lvl)]
           ^{:key i}
           (let [is-last? (and last-child? (= i (dec lvl)))]
             [:span {:class (str "block li w-[15px]" (when is-last? " last-li"))}]))]
        [:div {:class "relative"}
         (name-cell element)
         (when last-child?
           [:div {:class "relative -left-[15px] h-full -top-[10px] border-l border-surface group-even:border-surface-alt"}])]]
       [:td {:class "px-4 py-0 align-top"}
        (cardinality-cell element)]
       [:td {:class "px-4 py-0 align-top"}
        (type-cell element)]
       [:td {:class "px-4 pt-2 pb-0 align-top text-[12px]"}
        (description-cell element parse-markdown-fn)]])

    ;; Node with children
    (cons
     [:tr {:class "group"}
      [:td {:class "flex h-full pl-[15px] py-0 px-4 align-top"}
       [:div {:class "element flex"}
        (for [_ (range lvl)]
          [:span {:class "block li w-[15px]"}])]
       [:div {:class "relative"}
        (name-cell element)
        (when (not= 0 lvl)
          [:div {:class "ml-[10px] h-[calc(100%-6px)] border-l border-dotted border-outline-tree"}])]]
      [:td {:class "px-4 py-0 align-top"}
       (cardinality-cell element)]
      [:td {:class "px-4 py-0 align-top"}
       (type-cell element)]
      [:td {:class "px-4 pt-2 pb-0 align-top text-[12px]"}
       (description-cell element parse-markdown-fn)]]

     (for [child children]
       ^{:key (:path child)}
       (tree-node child last-childs parse-markdown-fn)))))

(defn render-table
  "Main rendering function for FHIR structure table"
  [elements parse-markdown-fn & [{:keys [resource-type]}]]
  (let [;; Increase lvl of all elements by 1 to make them children of resource root
        adjusted-elements (if resource-type
                            (map #(update % :lvl inc) elements)
                            elements)
        nested-elements (nest-by-level adjusted-elements)
        last-childs (->> adjusted-elements
                         (map-indexed vector)
                         (filter (fn [[idx {lvl :lvl}]]
                                   (let [next-el-lvl (-> (nth adjusted-elements (inc idx) nil)
                                                         :lvl)]
                                     (or (= (dec lvl) next-el-lvl) (= 0 next-el-lvl)))))
                         (map second))]

    [:table {:class "fhir-structure w-full font-sans text-[12px] font-normal border border-outline border-separate border-spacing-0"}
     [:thead
      [:tr {:class "sticky top-0 z-50"}
       [:th {:class "px-4 py-2 text-left font-normal bg-surface-alt text-on-surface-strong"} "Name"]
       [:th {:class "px-4 py-2 text-left font-normal bg-surface-alt text-on-surface-strong"} "Card."]
       [:th {:class "px-4 py-2 text-left font-normal bg-surface-alt text-on-surface-strong"} "Type"]
       [:th {:class "px-4 py-2 text-left font-normal bg-surface-alt text-on-surface-strong"} "Description"]]]
     [:tbody.tree
      (when resource-type
        [:tr {:class "group"}
         [:td {:class "flex h-full pl-[15px] py-0 px-4 align-top"}
          [:div {:class "element flex"}]
          [:div {:class "relative"}
           [:div {:class "flex pt-2 pb-1 ml-1"}
            [:div {:class "pt-[1px]"}
             (icons/resource-icon)]
            [:div {:class "pl-2 font-medium"} resource-type]]
           ;; Add vertical dotted line to show this is root of tree
           [:div {:class "ml-[10px] h-[calc(100%-6px)] border-l border-dotted border-outline-tree"}]]]
         [:td {:class "px-4 py-0 align-top"}]
         [:td {:class "px-4 py-0 align-top"}]
         [:td {:class "px-4 pt-2 pb-0 align-top"}]])
      (for [node nested-elements]
        ^{:key (:path node)}
        (tree-node node last-childs parse-markdown-fn))]]))

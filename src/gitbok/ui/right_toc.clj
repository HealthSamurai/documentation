(ns gitbok.ui.right-toc
  (:require
   [clojure.string :as str]
   [gitbok.utils :as utils]))

(defn render-right-toc-item [item]
  (when (:content item)
    (let [content
          (->> (:content item)
               (remove (fn [node]
                         (= :html-inline (:type node))))
               (map #(if (= :text (:type %))
                       (:text %)
                       (->> (:content %)
                            (map :text)
                            (str/join " "))))
               (str/join " "))
          href (str "#" (utils/s->url-slug (:id (:attrs item))))
          level (when (= :toc (:type item))
                  (:heading-level item))
          ;; Add border styling for nested items like left navigation
          li-class (str
                    "hover:bg-tint-hover hover:text-tint-strong hover:border-tint-7 max-w-56"
                    (cond
                      (= level 2) "break-words py-0.5 border-transparent"
                      (>= level 3) "break-words ml-2 border-l-2 border-tint-3 my-0"))

          link-class (str "block px-3 text-small font-content text-tint-strong/70 "
                          "transition-colors duration-200 ease-in-out no-underline "
                          "relative py-1 "
                          (if (>= level 3)
                            " opacity-60 text-sm "
                            " opacity-80 text-sm "))]
      [:li {:class li-class}
       [:a {:href href
            :class link-class}
        [:span content]]

       ;; Render children if any
       (when (:children item)
         (for [child (:children item)]
           (render-right-toc-item child)))])))

(defn render-right-toc [parsed]
  (when (:toc parsed)
    (let [toc (-> parsed :toc :children first :children)
          rendered
          (remove nil?
                  (for [item toc]
                    (render-right-toc-item item)))

          rendered (if (seq rendered)

                     rendered
                     (for [item (:children (first toc))]
                       (render-right-toc-item item)))]

      [:nav#toc-container
       {:class "max-w-56 basis-56 flex-shrink-0 sticky top-16 h-[calc(100vh-4rem)]
        ml-12 overflow-y-auto py-8 bg-tint-base
        font-content hidden lg:block
        "
        :aria-label "On-page navigation"}
       [:ul {:class "space-y-0.5"} rendered]])))

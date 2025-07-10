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
          li-class (cond
                     (= level 2) "break-words"
                     (= level 3) "break-words ml-4 border-l-2 border-tint-3 pl-2"
                     :else "break-words ml-6 pl-2 border-l-2 border-tint-3")
          link-class (str "block py-2 px-3 text-small font-content text-tint-strong/70 "
                          "hover:bg-tint-hover hover:text-tint-strong "
                          "transition-colors duration-200 ease-in-out no-underline "
                          "rounded-md relative "
                          (when (> level 1)
                            "opacity-80 text-sm"))]
      [:li {:class li-class}
       [:a {:href href
            :class link-class}
        [:span content]]

       ;; Render children if any
       (when (:children item)
         (for [child (:children item)]
           (render-right-toc-item child)))])))

(defn render-right-toc [parsed]
  (println "DEBUG render-right-toc - parsed keys:" (keys parsed))
  (println "DEBUG render-right-toc - has :toc?" (contains? parsed :toc))
  (when (:toc parsed)
    (println "DEBUG render-right-toc - :toc value:" (:toc parsed))
    (println "DEBUG render-right-toc - :toc type:" (type (:toc parsed)))
    (let [toc-path (-> parsed :toc :children first :children)
          ;; Handle two different TOC structures:
          ;; 1. Items with :children (nested structure) 
          ;; 2. Items without :children (flat structure)
          actual-items (if (and (seq toc-path) (:children (first toc-path)))
                         (-> toc-path first :children) ; nested structure
                         toc-path)] ; flat structure
      (println "DEBUG render-right-toc - toc-path:" toc-path)
      (println "DEBUG render-right-toc - actual-items:" actual-items)
      [:nav#toc-container {:class "w-72 flex-shrink-0 sticky top-16 h-[calc(100vh-4rem)] overflow-y-auto py-6 bg-tint-base border-l border-tint-subtle font-content hidden lg:block"
                           :aria-label "On-page navigation"}
       [:ul {:class "space-y-1 px-4"}
        (for [item actual-items]
          (render-right-toc-item item))]])))



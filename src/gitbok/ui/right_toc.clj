(ns gitbok.ui.right-toc
  (:require
   [system]
   [gitbok.indexing.core :as indexing]
   [gitbok.http]
   [clojure.string :as str]
   [gitbok.utils :as utils]
   [gitbok.markdown.core :as markdown]))

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
  (when (:toc parsed)
    [:nav#toc-container {:class "w-64 flex-shrink-0 sticky top-16 h-[calc(100vh-4rem)] overflow-y-auto py-6 bg-tint-base  font-content hidden lg:block"
                         :aria-label "On-page navigation"}
     [:ul {:class "space-y-1 px-4"}
      (for [item (-> parsed :toc :children first :children)]
        (render-right-toc-item item))]]))

(defn get-right-toc [context filepath]
  (let [rendered (markdown/get-rendered context filepath)]
    (if (map? rendered)
      (:toc rendered)
      nil)))

(defn get-toc-view
  [context request]
  (let [uri (str/replace (:uri request) #"^/toc" "")
        filepath (indexing/uri->filepath context uri)
        lastmod
        (indexing/get-lastmod context filepath)]
    (if filepath
      (gitbok.http/response1
       (get-right-toc context filepath) 200 lastmod nil)
      (gitbok.http/response1 [:div] 404 nil nil))))

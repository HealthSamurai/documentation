(ns gitbok.ui.right-toc
  (:require
   [system]
   [gitbok.indexing.core :as indexing]
   [gitbok.http]
   [clojure.string :as str]
   [gitbok.utils :as utils]
   [gitbok.markdown.core :as markdown]))

(defn render-right-toc-item [item]
  [:div {:class "w-full"}
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
           padding-class (case (long level)
                           1 "pl-0"
                           2 "pl-4"
                           3 "pl-8"
                           4 "pl-12"
                           5 "pl-16"
                           6 "pl-20"
                           "pl-0")]
       [:a {:href href
            :class (str padding-class)}
        content]))

   (when (:children item)
     (for [child (:children item)]
       (render-right-toc-item child)))])

(defn render-right-toc [parsed]
  (when (:toc parsed)
    [:nav#toc-container {:class "toc-container sticky top-16 h-[calc(100vh-4rem)] overflow-y-auto p-6 bg-white w-60 rounded-lg z-50"
                         :aria-label "On-page navigation"}
     [:div {:class "toc w-full max-w-full"}
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
       (get-right-toc context filepath) 200 lastmod)
      (gitbok.http/response1 [:div] 404 nil))))

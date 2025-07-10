(ns gitbok.ui.left-navigation
  (:require
   [uui.heroicons :as ico]
   [gitbok.http]
   [gitbok.utils :as utils]
   [clojure.string :as str]))

(defn add-active-class [item add?]
  (let [link-element (:title item)
        current-class (get-in link-element [1 :class] "")
        active-class (if add? " active" "")
        updated-class (str current-class active-class)]
    (assoc-in link-element [1 :class] updated-class)))

(defn render-left-navigation [url item]
  (let [open? (str/starts-with? url (:href item))]
    (if (:children item)
      [:details.group (when open? {:open ""})
       [:summary
        {:class "flex items-center justify-between
         md:font-medium
         hover:bg-gray-100
         transition-colors duration-200
         cursor-pointer group"}
        [:div {:class
               (cond-> "flex-1 clickable-summary"
                 open?
                 (str " border-l border-gray-200"))}
         (add-active-class item (= url (:href item)))]
        (ico/chevron-right "chevron size-3 text-tint-12 group-hover:text-primary-9 transition-all
                           duration-200 transform group-open:rotate-90 mr-4")]
       [:div {:class "border-l border-gray-200 ml-4"}
        (for [c (:children item)]
          (render-left-navigation url c))]]
      (add-active-class item open?))))

(defn left-navigation [summary url]
  [:nav#navigation
   {:class "w-[17.5rem] flex-shrink-0 sticky top-16 h-[calc(100vh-4rem)] overflow-y-auto py-4 bg-white"
    :aria-label "Documentation menu"}
   (for [item summary]
     [:div {:class "break-words"}
      (when-not
       (str/blank? (:title item))
        [:div {:class "mt-4 mb-2 mx-4"}
         [:b (:title item)]])
      (for [ch (:children item)]
        (render-left-navigation url ch))])
   [:div "version " (utils/slurp-resource "version")]])

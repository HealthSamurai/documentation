(ns gitbok.ui.left-navigation
  (:require
   [uui.heroicons :as ico]
   [gitbok.http]
   [gitbok.utils :as utils]
   [clojure.string :as str]
   [gitbok.indexing.impl.summary :as summary]))

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
                  text-small font-normal
                  hover:bg-tint-hover
                  transition-colors duration-200 ease-in-out
                  cursor-pointer group"}
        [:div {:class "flex-1 clickable-summary"}
         (add-active-class
          (update item :title
                  (fn [title] (assoc-in title [1 :class] summary/summary-classes)))
          (= url (:href item)))]
        (ico/chevron-right "chevron size-3 text-small font-normal
                            group-hover:text-primary-9 transition-all
                            duration-200 transform rotate-0 group-open:rotate-90 mr-4")]
       [:div {:class "ml-6 border-l-1 border-tint-8"}
        (for [c (:children item)]
          (render-left-navigation url c))]]
      (add-active-class item open?))))

(defn left-navigation [summary url]
  [:nav#navigation
   {:class "w-80 flex-shrink-0 sticky top-16 h-[calc(100vh-4rem)]
    overflow-y-auto py-6 bg-tint-base border-r border-tint-subtle
    font-content lg:mr-20 space-y-4
    lg:-ms-4"
    :aria-label "Documentation menu"}
   (for [item summary]
     [:div {:class "break-words"}
      (when-not
       (str/blank? (:title item))
        [:div {:class "mt-4 mb-1 first:mt-2 ml-4"}
         [:span {:class "text-mini font-semibold text-tint-strong uppercase tracking-wider"}
          (:title item)]])
      (for [ch (:children item)]
        (render-left-navigation url ch))])])

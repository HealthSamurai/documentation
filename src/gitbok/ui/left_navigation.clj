(ns gitbok.ui.left-navigation
  (:require
   [gitbok.ui.heroicons :as ico]
   [gitbok.http]
   [clojure.string :as str]
   [gitbok.indexing.impl.summary :as summary]))

(defn add-active-class [item add?]
  (let [link-element (:title item)
        current-class (get-in link-element [1 :class] "")
        active-class (if add? " active" "")
        updated-class (str current-class active-class)]
    (assoc-in link-element [1 :class] updated-class)))

(defn render-left-navigation [url item]
  (let [;; For exact match - item is active only if URLs are exactly the same
        active? (= url (:href item))
        ;; For opening - check if current URL is under this item's path
        open? (str/starts-with? url (:href item))]
    (if (:children item)
      [:div.nav-item {:class (when open? "open")}
       [:button.nav-summary
        {:type "button"
         :aria-expanded (if open? "true" "false")
         :class "flex items-center justify-between w-full
                 text-small font-normal
                 transition-colors duration-200 ease-in-out
                 cursor-pointer bg-transparent border-0 text-left"}
        [:div {:class "flex-1 clickable-summary"}
         (add-active-class
          (update item :title
                  (fn [title] (assoc-in title [1 :class] summary/summary-classes)))
          active?)]
        (ico/chevron-right "nav-chevron size-3 text-small font-normal
                            transition-all
                            duration-200 mr-9")]
       [:div.nav-children {:class "ml-6 border-l-1 border-outline"
                           :hidden (not open?)}
        (for [c (:children item)]
          (render-left-navigation url c))]]
      (add-active-class item active?))))

(defn left-navigation [summary url]
  [:nav#navigation
   {:class "w-80 flex-shrink-0 sticky top-16 h-[calc(100vh-4rem)]
    overflow-y-auto py-6 bg-surface border-r border-outline-subtle
    font-content lg:mr-20 space-y-4
    lg:-ms-4"
    :aria-label "Documentation menu"}
   (for [item summary]
     [:div {:class "break-words mt-4 border-t border-outline-subtle pt-4 first:mt-0 first:border-t-0 first:pt-0"}
      (when-not
       (str/blank? (:title item))
        [:div {:class "mb-1 first:mt-2 ml-4"}
         [:span {:class "text-xs font-medium leading-4 text-brand uppercase tracking-wider"}
          (:title item)]])
      (for [ch (:children item)]
        (render-left-navigation url ch))])])

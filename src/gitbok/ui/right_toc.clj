(ns gitbok.ui.right-toc
  (:require
   [clojure.string :as str]
   [gitbok.utils :as utils]))

(defn render-feedback-section []
  [:div {:class "px-3 pt-0 pb-2 mb-3 border-b-1 border-outline-subtle"}
   [:button {:id "feedback-send-button"
             :class "w-full inline-flex items-center justify-center px-4 py-2.5 text-sm font-semibold leading-5 bg-surface text-on-surface hover:bg-button-hover-bg border border-outline rounded-md transition-colors cursor-pointer"}
    "Send feedback"]])

(defn render-right-toc-item [item]
  (when (:content item)
    (let [;; Get heading level early to check if we should render
          level (when (= :toc (:type item))
                  (:heading-level item))]
      ;; Skip rendering for level 4 and higher headers
      (when-not (and level (>= level 4))
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
              ;; Try to extract id from inline HTML anchor tag if present
              html-anchor-id (some (fn [node]
                                     (when (= :html-inline (:type node))
                                       (let [html-text (-> node :content first :text)]
                                         ;; Extract id from <a ... id="some-id">
                                         (when-let [match (re-find #"id=\"([^\"]+)\"" html-text)]
                                           (second match)))))
                                   (:content item))
              ;; Use extracted id if found, otherwise generate from text
              href (if html-anchor-id
                     (str "#" html-anchor-id)
                     (str "#" (utils/s->url-slug (:id (:attrs item)))))
              ;; Add border styling for nested items like left navigation
              li-class (str
                        "max-w-56"
                        (cond
                          (= level 2) " break-words py-0.5"
                          (>= level 3) " break-words ml-2 border-l-1 border-outline-subtle my-0"))

              link-class (str "block px-3 text-sm leading-5 font-normal no-underline relative py-1 "
                              "transition-colors duration-[300ms] ease-out "
                              "hover:bg-surface-nav-hover "
                              (if (= level 2)
                                "text-on-surface-placeholder"
                                "text-on-surface"))]
          [:li {:class li-class}
           [:a {:href href
                :class link-class}
            [:span content]]

           ;; Render children if any
           (when (:children item)
             (for [child (:children item)]
               (render-right-toc-item child)))])))))

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
        ml-12 overflow-y-auto py-8 bg-surface
        font-content hidden lg:block
        "
        :aria-label "On-page navigation"}
       (render-feedback-section)
       [:ul {:class "space-y-0.5"} rendered]])))

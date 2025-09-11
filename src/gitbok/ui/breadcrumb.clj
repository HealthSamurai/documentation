(ns gitbok.ui.breadcrumb
  (:require
   [gitbok.http :as http]
   [clojure.string :as str]
   [gitbok.ui.heroicons :as ico]
   [gitbok.products]))

(defn breadcrumb [context uri]
  (when (and uri (not (str/blank? uri)))
    (let [parts (->> (str/split uri #"/")
                     (remove str/blank?)
                     vec)]

      ;; Special handling based on specific requirements
      (cond
        ;; Don't show breadcrumb for readme pages
        (= (first parts) "readme")
        nil

        ;; For single-level pages like "getting-started", show "overview" link
        (= 1 (count parts))
        [:nav {:aria-label "Breadcrumb"}
         [:ol {:class "flex flex-wrap items-center"}
          [:li {:class "flex items-center gap-1.5"}
           [:a {:href (http/get-product-prefixed-url context "overview")
                :hx-get (str (http/get-product-prefixed-url context "overview") "?partial=true")
                :hx-target "#content"
                :hx-push-url (http/get-product-prefixed-url context "overview")
                :hx-swap "outerHTML"
                :class "text-xs font-semibold uppercase items-center gap-1.5 hover:text-tint-strong text-primary-9"}
            "overview"]]]]

        ;; For other nested pages, show normal breadcrumb
        :else
        (let [parts-to-show (vec (drop-last parts))]
          (when (seq parts-to-show)
            [:nav {:aria-label "Breadcrumb"}
             [:ol {:class "flex flex-wrap items-center"}
              (interpose
               (ico/chevron-right "chevron size-3 text-tint-10 group-hover:text-primary-9 mx-2")
               (map-indexed
                (fn [idx part]
                  (let [path (->> (take (inc idx) parts-to-show)
                                  (str/join "/"))
                        href (http/get-product-prefixed-url context path)
                        part-text (str/replace part #"-" " ")]
                    [:li {:key idx
                          :class "flex items-center gap-1.5"}
                     [:a {:href href
                          :hx-get (str href "?partial=true")
                          :hx-target "#content"
                          :hx-push-url href
                          :hx-swap "outerHTML"
                          :class "text-xs font-semibold uppercase items-center gap-1.5 hover:text-tint-strong text-primary-9"}
                      part-text]]))
                parts-to-show))]]))))))

(ns gitbok.ui.breadcrumb
  (:require
   [clojure.string :as str]
   [uui.heroicons :as ico]
   [gitbok.http]
   [gitbok.indexing.core :as indexing]))

(defn breadcrumb [context filepath]
  (def f filepath)
  (when filepath
    (let [parts (->> (str/split filepath #"/")
                     (remove str/blank?)
                     vec)
          parent? (indexing/parent? context filepath)
          parts-to-show
          (cond-> (drop-last parts)

            (and parent? (>= 3 (count parts)))
            (drop-last)

            :always (vec))]

      (when-not (and (= (count parts) 2)
                     (= (first parts) "readme"))
        [:nav {:aria-label "Breadcrumb"}
         [:ol {:class "flex flex-wrap items-center"}
          (interpose
           (ico/chevron-right "chevron size-3 text-tint-10 group-hover:text-primary-9 mx-2")
           (map-indexed
            (fn [idx part]
              (let [path (->> (subvec parts 0 (inc idx)) (str/join "/"))
                    href (str "/" (gitbok.http/get-prefixed-url context path))
                    part (str/replace part #"-" " ")]
                [:li {:key idx
                      :class "flex items-center gap-1.5"}
                 [:a {:href href
                      :hx-get (str href "?partial=true")
                      :hx-target "#content"
                      :hx-push-url href
                      :hx-swap "outerHTML"
                      :class "text-xs font-semibold uppercase items-center gap-1.5 hover:text-tint-strong text-primary-9"}
                  part]]))
            parts-to-show))]]))))

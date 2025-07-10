(ns gitbok.markdown.widgets.headers
  (:require
   [nextjournal.markdown.transform :as transform]
   [gitbok.utils :as utils]
   [clojure.string :as str]))

(defn normalize-heading-id [heading-id]
  (-> heading-id
      (str/replace #"^-|-$" "")
      (utils/s->url-slug)))

(defn render-heading
  [ctx node]
  ((comp
    (fn [header-hiccup]
      (let [tag (first header-hiccup)
            classes (case tag
                      :h1 "mt-2 text-5xl font-bold text-gray-900 mx-auto"
                      :h2 "mt-8 text-4xl font-semibold text-gray-900 pb-2 mb-6 mx-auto"
                      :h3 "mt-6 text-2xl font-semibold text-gray-900 mb-4 mx-auto"
                      :h4 "mt-4 text-lg font-semibold text-gray-900 mb-3 mx-auto"
                      :h5 "mt-3 text-base font-semibold text-gray-900 mb-2 mx-auto"
                      :h6 "mt-2 text-sm font-semibold text-gray-900 mb-1 mx-auto"
                      "text-gray-900")]
        (cond-> header-hiccup
          (-> header-hiccup (get 1) :id)
          (update-in [1 :id] normalize-heading-id)
          :always
          (update-in
           [1 :class]
           (fnil
            (fn [existing-class]
              (str/trim
               (str existing-class " " classes))) "")))))
    (:heading transform/default-hiccup-renderers)) ctx node))

(defn render-h1 [ctx title]
  (render-heading
   ctx
   {:type :heading
    :content [{:type :text
               :level 1
               :text title}]}))

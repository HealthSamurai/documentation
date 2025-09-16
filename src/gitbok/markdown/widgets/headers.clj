(ns gitbok.markdown.widgets.headers
  (:require
   [nextjournal.markdown.transform :as transform]
   [gitbok.utils :as utils]
   [clojure.string :as str]))

(defn normalize-heading-id [heading-id]
  (-> heading-id
      (str/replace #"^-|-$" "")
      (utils/s->url-slug)))

(defn extract-anchor-id-from-content
  "Extract id from inline HTML anchor tags in heading content"
  [content]
  (some (fn [item]
          (when (and (= :html-inline (:type item))
                     (:content item))
            (let [html-text (-> item :content first :text)]
              ;; Extract id from <a ... id="some-id">
              (when-let [match (re-find #"id=\"([^\"]+)\"" html-text)]
                (second match)))))
        content))

(defn render-heading
  [ctx node]
  ((comp
    (fn [header-hiccup]
      (let [tag (first header-hiccup)
            classes (case tag
                      :h1 "mt-2 text-5xl font-bold text-tint-12 mx-auto max-w-full break-words"
                      :h2 "mt-[1.05em] text-4xl font-semibold text-tint-12 pb-2 mx-auto"
                      :h3 "mt-6 text-2xl font-semibold text-tint-12 mb-4 mx-auto"
                      :h4 "mt-4 text-lg font-semibold text-tint-12 mb-3 mx-auto"
                      :h5 "mt-3 text-base font-semibold text-tint-12 mb-2 mx-auto"
                      :h6 "mt-2 text-sm font-semibold text-tint-12 mb-1 mx-auto"
                      "text-tint-12")
            ;; Check if there's an inline anchor with id in the node content
            anchor-id (when (:content node)
                       (extract-anchor-id-from-content (:content node)))
            ;; Use the anchor id if found, otherwise normalize the existing id
            final-id (if anchor-id
                      anchor-id
                      (when (-> header-hiccup (get 1) :id)
                        (normalize-heading-id (-> header-hiccup (get 1) :id))))]
        (cond-> header-hiccup
          final-id
          (assoc-in [1 :id] final-id)
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

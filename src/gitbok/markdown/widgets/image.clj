(ns gitbok.markdown.widgets.image
  (:require
    [nextjournal.markdown.utils :as u]))

(def github-image-regex #"\!\[([^\]]*)\]\(([^)]+)\)")

(def image-tokenizer
  (u/normalize-tokenizer
   {:regex github-image-regex
    :handler (fn [match] {:type :image
                          :text (match 2)
                          :alt (match 1)})}))

(defn image-renderer [_ctx node]
  [:img {:src (some->
                node
                :attrs
                :src)
         :alt (or (:alt node)
                  (:title (:attrs node)))
         :class "max-w-full h-auto mx-auto rounded-lg shadow-lg my-6"}])

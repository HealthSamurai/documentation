(ns gitbok.markdown.widgets.image
  (:require
    [clojure.string :as str]
    [gitbok.markdown.widgets.code-highlight :as code-highlight]
    [gitbok.markdown.widgets.content-ref :as content-ref]
    [gitbok.markdown.widgets.github-hint :as github-hint]
    [gitbok.markdown.widgets.hint :as hint]
    [nextjournal.markdown :as md]
    [nextjournal.markdown.transform :as transform]
    [nextjournal.markdown.utils :as u]
    [edamame.core :as edamame]
    [clojure.zip :as z]))

(def github-image-regex #"\!\[([^\]]*)\]\(([^)]+)\)")

(def image-tokenizer
  (u/normalize-tokenizer
   {:regex github-image-regex
    :handler (fn [match] {:type :image
                          :text (match 2)
                          :alt (match 1)})}))

(defn image-renderer [ctx node]
  [:img {:src
         (some->
           node :attrs :src
           (str/replace #".*\.gitbook/assets/" "/pictures/"))
         :alt (:alt node)}])

(ns gitbok.markdown.widgets.link
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

(defn link-renderer [_ctx node]
  (def node node)
  [:a {:href (-> node :attrs :href (str/replace #"\.md$" ""))}
   (-> node :content (get 0) :text)])

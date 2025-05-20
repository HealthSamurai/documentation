(ns gitbok.markdown.widgets.big-links
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

(def big-link-tokenizer
  (u/normalize-tokenizer
   {:regex #"\[\[\[([^\]]+)\]\]\]"
    :handler (fn [match] {:type :big-link
                          :text (match 1)})}))

(def icon
  {:dangerouslySetInnerHTML
   {:__html "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"24\" height=\"24\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\"><path d=\"M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6\"></path><polyline points=\"15 3 21 3 21 9\"></polyline><line x1=\"10\" y1=\"14\" x2=\"21\" y2=\"3\"></line></svg>"}})

(defn big-link-renderer [ctx node]
  (def node node)
  [:div.content-ref
   {:class "my-4 p-4 border border-gray-200 rounded-md bg-gray-50 hover:bg-gray-100 transition-colors flex items-center"}
   [:div.content-ref-icon
    {:class "mr-3 text-blue-500"}
    [:span icon]]
   [:div.content-ref-content
    {:class "flex-1"}
    [:a.content-ref-link
     ;; todo file -> url
     {:href (:text node)
      :class "text-blue-600 hover:text-blue-800 font-medium"}
     (:text node)]]])

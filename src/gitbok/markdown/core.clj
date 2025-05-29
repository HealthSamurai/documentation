(ns gitbok.markdown.core
  (:require
    [clojure.string :as str]
    [gitbok.markdown.widgets.big-links :as big-links]
    [gitbok.markdown.widgets.link :as link]
    [gitbok.markdown.widgets.image :as image]
    [gitbok.markdown.widgets.code-highlight :as code-highlight]
    [gitbok.markdown.widgets.content-ref :as content-ref]
    [gitbok.markdown.widgets.github-hint :as github-hint]
    [gitbok.markdown.widgets.hint :as hint]
    [nextjournal.markdown :as md]
    [nextjournal.markdown.transform :as transform]
    [hiccup2.core]
    [nextjournal.markdown.utils :as u]
    [edamame.core :as edamame]
    [clojure.zip :as z]
    [uui]))

(def custom-doc
  (update u/empty-doc
          :text-tokenizers
          conj
          big-links/big-link-tokenizer
          image/image-tokenizer))

(defn parse-markdown-content
  [[filepath content]]
  {:filepath filepath :parsed (md/parse* custom-doc content)})

(def renderers
  (assoc transform/default-hiccup-renderers
         :big-link big-links/big-link-renderer
         :image image/image-renderer
         :link link/link-renderer
         :html-block
         (fn [ctx node]
           (uui/raw (-> node :content first :text)))
         ;; :text (fn [_ctx node]
         ;;         (let [text (:text node)]
         ;;           (if (= text "<>")
         ;;             [:<>]
         ;;             text)))
         ))

;; todo reuse :toc
(defn render-gitbook
  [content]
  (let [{:keys [parsed]} (parse-markdown-content [nil content])
        rendered (transform/->hiccup renderers parsed)]
    rendered))

 #_(def readme (slurp "./docs/getting-started/run-aidbox-locally.md"))

#_(:parsed (parse-markdown-content [nil readme]))

#_(parse-markdown-content "See [hello](../../hello.md)")
#_(render-gitbook "See [hello](../../hello.md)")

#_(render-gitbook readme)

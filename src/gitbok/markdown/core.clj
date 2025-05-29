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
   [nextjournal.markdown.utils :as u]
   [edamame.core :as edamame]
   [clojure.zip :as z]))

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
         :link link/link-renderer))

(defn render-gitbook
  [content]
  (let [[filepath parsed] (parse-markdown-content content)
        rendered (transform/->hiccup renderers parsed)]
    rendered))

#_(def readme (slurp "./docs/getting-started/run-aidbox-locally.md"))

#_(:content (parse-markdown-content readme))

#_(parse-markdown-content "See [hello](../../hello.md)")
#_(render-gitbook "See [hello](../../hello.md)")

#_(render-gitbook readme)

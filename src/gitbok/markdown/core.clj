(ns gitbok.markdown.core
  (:require
   [clojure.string :as str]
   [gitbok.markdown.widgets.big-links :as big-links]
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
  [content]
  (md/parse* custom-doc content))

(def renderers
  (assoc transform/default-hiccup-renderers
         :big-link big-links/big-link-renderer
         :image image/image-renderer))

(defn render-gitbook
  "Render GitBook-compatible markdown to hiccup."
  [content]
  (let [parsed (parse-markdown-content content)
        rendered (transform/->hiccup renderers parsed)]
    rendered))


#_(def readme (slurp "./docs/readme/README.md"))
#_(parse-markdown-content readme)


#_(render-gitbook "[[[../tutorials/some-tutorial.md]]]")

#_(render-gitbook readme)

(ns gitbok.markdown.core
  "Core namespace for GitBook-compatible markdown parsing.
   Handles custom GitBook blocks (hint, content-ref) and standard markdown."
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

;; Define the regex pattern for GitHub hint blocks
;; Matches the pattern:
;; > [!TYPE]
;; > content
;; > more content
(def github-hint-regex #"(?ms)^>\s*\[\!(\w+)\](?:\s+(.*))?(?:\n(?:>\s?.*\n?)*)")


;; Create a custom tokenizer for GitHub hint blocks
(def github-hint-tokenizer
  (u/normalize-tokenizer
   {:regex github-hint-regex
    :handler (fn [match]
               (println "matched.. " match)
               (let [hint-type (match 1)
                     title (or (match 2) "")
                     content (str/replace-first (match 0) #"(?ms)^>\s*\[\!\w+\](?:\s+.*)?\n?" "")
                     ;; Remove the ">" prefix from each line of content
                     cleaned-content (str/replace content #"(?m)^>\s?" "")]
                 {:type :github-hint
                  :hint-type hint-type
                  :title title
                  :content cleaned-content}))}))

;; Define the custom document with the tokenizer added
(defn custom-doc []
  (update u/empty-doc
          :text-tokenizers
          conj github-hint-tokenizer))

;; Parse the markdown content with the custom tokenizer
(defn parse-markdown-content
  "Parse markdown content with GitHub hint extensions."
  [content]
  (:content (md/parse* (custom-doc) content)))

(def renderers
  "Renderers for all supported block types"
  (-> transform/default-hiccup-renderers
      (merge
        hint/renderers
        content-ref/renderers
        github-hint/renderers
        code-highlight/renderers)))

(defn render-gitbook
  "Render GitBook-compatible markdown to hiccup."
  [content]
  (let [parsed (parse-markdown-content content)
        rendered (transform/->hiccup renderers parsed)]
    rendered))

;; For testing and development
(comment

  ((:tokenizer-fn github-hint-tokenizer) hint-text)

  (re-seq github-hint-regex hint-text)
  ;; Test with a simple GitHub hint
  (def hint-text "> [!HINT]\n> This is a hint")

  ;; Parse the hint text
  (parse-markdown-content hint-text)

  ;; Test with a normal blockquote
  (def blockquote-text "> Hello!\n> How are you?")

  ;; Parse the blockquote
  (parse-markdown-content blockquote-text)

  ;; Test with multiple hint types
  (def multi-hint-text "> [!NOTE]\n> This is a note\n\n> [!WARNING]\n> This is a warning")

  ;; Parse the multiple hints
  (parse-markdown-content multi-hint-text)
)



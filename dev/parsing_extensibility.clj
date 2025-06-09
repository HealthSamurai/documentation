(ns parsing-extensibility
  (:require
   [nextjournal.markdown.transform :as md.transform]
   [nextjournal.markdown :as md]
   [nextjournal.markdown.utils :as u]
   [edamame.core :as edamame]
   [clojure.zip :as z]))

;; With recent additions to our `nextjournal.markdown.parser` we're allowing for a customizable parsing layer on top of the tokenization provided by `markdown-it` ([n.markdown/tokenize](https://github.com/nextjournal/markdown/blob/ae2a2f0b6d7bdc6231f5d088ee559178b55c97f4/src/nextjournal/markdown.clj#L50-L52)).
;; We're acting on the text (leaf) tokens, splitting each of those into a collection of [nodes](https://github.com/nextjournal/markdown/blob/ff68536eb15814fe81db7a6d6f11f049895a4282/src/nextjournal/markdown/parser.cljc#L5).  We'll explain how that works by means of three examples.
;;
;; ## Regex-based tokenization
;;
;; A `Tokenizer` is a map with keys `:doc-handler` and `:tokenizer-fn`. For convenience, the function `u/normalize-tokenizer` will fill in the missing keys
;; starting from a map with a `:regex` and a `:handler`:

;; (def internal-link-tokenizer
;;   (u/normalize-tokenizer
;;    {:regex #"\[\[([^\]]+)\]\]"
;;     :handler (fn [match] {:type :internal-link
;;                           :text (match 1)})}))
;;
;; ((:tokenizer-fn internal-link-tokenizer) "some [[set]] of [[wiki]] link")
;;
;; (u/tokenize-text-node internal-link-tokenizer {} {:text "some [[set]] of [[wiki]] link"})
;;
;; ;; In order to opt-in of the extra tokenization above, we need to configure the document context as follows:
;; (md/parse* (update u/empty-doc :text-tokenizers conj internal-link-tokenizer)
;;            "some [[set]] of [[wiki]] link")
;;
;; ;; --------------------------------------------------------------
;; ;; We provide an `internal-link-tokenizer` as well as a `hashtag-tokenizer` as part of the `nextjournal.markdown.parser` namespace. By default, these are not used during parsing and need to be opted-in for like explained above.
;;
;; ;; ## Read-based tokenization
;; ;;
;; ;; Somewhat inspired by the Racket text processor [Pollen](https://docs.racket-lang.org/pollen/pollen-command-syntax.html) we'd like to parse a `text` like this
;;
;; (def text "At some point in text a losange
;; will signal ◊(foo \"one\" [[vector]]) we'll want to write
;; code and ◊not text. Moreover it has not to conflict with
;; existing [[links]] or #tags")
;; ;; and _read_ any valid Clojure code comining after the lozenge character (`◊`) which we'll also call a
;; ;; _losange_ as in French it does sound much better 🇫🇷!
;; ;;
;;
;; ;; Now, when a form is read with [Edamame](https://github.com/borkdude/edamame#edamame), it preserves its location metadata. This allows
;; ;; us to produce an `IndexedMatch` from matching text
;; (defn match->data+indexes [m text]
;;   (let [start (.start m) end (.end m)
;;         form (edamame/parse-string (subs text end))]
;;     [form start (+ end (dec (:end-col (meta form))))]))
;; ;; and our modified `re-seq` becomes
;; (defn losange-tokenizer-fn [text]
;;   (let [m (re-matcher #"◊" text)]
;;     ((fn step []
;;        (when (.find m)
;;          (cons (match->data+indexes m text)
;;                (lazy-seq (step))))))))
;;
;; (losange-tokenizer-fn text)
;; (losange-tokenizer-fn "non matching text")
;;
;; (def losange-tokenizer
;;   (u/normalize-tokenizer
;;    {:tokenizer-fn losange-tokenizer-fn
;;     :handler (fn [clj-data] {:type :losange
;;                              :data clj-data})}))
;;
;; (u/tokenize-text-node losange-tokenizer {} {:text text})
;;
;; ;; putting it all together
;; (md/parse* (update u/empty-doc :text-tokenizers conj losange-tokenizer)
;;            text)
;;
;; ;; --------------------------------------------------------------
;; ;; ## Parsing with Document Handlers
;; ;;
;; ;; Using tokenizers with document handlers we can let parsed tokens act upon the whole document tree. Consider
;; ;; the following textual example (**TODO** _rewrite parsing with a zipper state_):
;; (def text-with-meta
;;   "# Example ◊(add-meta {:attrs {:id \"some-id\"} :class \"semantc\"})
;; In this example we're using the losange tokenizer to modify the
;; document AST in conjunction with the following functions:
;; * `add-meta`: looks up the parent node, merges a map in it
;; and adds a flag to its text.
;; * `hui`: makes the text ◊(hui much more impactful) indeeed.
;; ")
;;
;; (defn add-meta [doc-loc meta]
;;   (-> doc-loc (z/edit merge meta)
;;       z/down (z/edit update :text str "🚩️")
;;       z/up))
;;
;; (defn hui [doc & terms]
;;   (-> doc
;;       (z/append-child {:type :hui}) z/down z/rightmost   ;; open-node
;;       (z/insert-child (u/text-node (apply str (interpose " " terms))))
;;       z/up)) ;; close-node
;;
;; (def data
;;   (md/parse* (-> u/empty-doc
;;                  (update :text-tokenizers conj
;;                          (assoc losange-tokenizer
;;                                 :doc-handler (fn [doc {:keys [match]}]
;;                                                (apply (eval (first match)) doc (rest match))))))
;;              text-with-meta))
;;
;;
;; (md.transform/->hiccup data)
;;
;; (md.transform/->hiccup
;;  (assoc md.transform/default-hiccup-renderers
;;         :hui
;;         (:strong md.transform/default-hiccup-renderers)
;;         ;; :doc specify a custom container for the whole doc
;;         :doc (partial md.transform/into-markup [:div.viewer-markdown])
;;         ;; :text is funkier when it's zinc toned
;;         :text (fn [_ctx node] [:span {:style {:color "#71717a"}} (:text node)])
;;         ;; :plain fragments might be nice, but paragraphs help when no reagent is at hand
;;         :plain (partial md.transform/into-markup [:p {:style {:margin-top "-1.2rem"}}])
;;         ;; :ruler gets to be funky, too
;;         :ruler (constantly [:hr {:style {:border "2px dashed #71717a"}}]))
;;  data)
;;
;;
;;
;;
;;
;;
;;
;;
;;
;;
;;
;;
;;
;;


(def text "hello\n{hui\nsomecontent\npizda}")

(def my-tokenizer
  (u/normalize-tokenizer
   {:regex #"(?s)\{hui\n(.*)\npizda\}"
    :handler (fn [match]
               {:type :some-type
                :text (match 1)})}))

((:tokenizer-fn my-tokenizer) text)

(u/tokenize-text-node my-tokenizer {} {:text text})

(:content
  (md/parse*
    (update u/empty-doc :text-tokenizers conj my-tokenizer)
    text))

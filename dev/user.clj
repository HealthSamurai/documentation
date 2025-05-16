(ns user
  (:require
   [system.dev :as dev]
   [system]
   [gitbok.core :as gitbok]
   ;; [nextjournal.clerk :as clerk]
   [nextjournal.markdown :as md]
   [nextjournal.markdown.transform :as md.transform]
   [nextjournal.markdown.utils :as u]
   [edamame.core :as edamame]
   [clojure.zip :as z]))

(comment
  (dev/update-libs)

  (def context (system/start-system gitbok/default-config))
  (system/stop-system context))

(def internal-link-tokenizer
  (u/normalize-tokenizer
   {:regex #"\[\[([^\]]+)\]\]"
    :handler (fn [match] {:type :internal-link
                          :text (match 1)})}))

((:tokenizer-fn internal-link-tokenizer) "some [[set]] of [[wiki]] link")

(u/tokenize-text-node internal-link-tokenizer {} {:text "some [[set]] of [[wiki]] link"})

;; In order to opt-in of the extra tokenization above, we need to configure the document context as follows:
(md/parse* (update u/empty-doc :text-tokenizers conj internal-link-tokenizer)
           "some [[set]] of [[wiki]] link")

(def text "At some point in text a losange
will signal ◊(foo \"one\" [[vector]]) we'll want to write
code and ◊not text. Moreover it has not to conflict with
existing [[links]] or #tags")

(defn match->data+indexes [m text]
  (let [start (.start m) end (.end m)
        form (edamame/parse-string (subs text end))]
    [form start (+ end (dec (:end-col (meta form))))]))

(defn losange-tokenizer-fn [text]
  (let [m (re-matcher #"◊" text)]
    ((fn step []
       (when (.find m)
         (cons (match->data+indexes m text)
               (lazy-seq (step))))))))

(losange-tokenizer-fn text)
(losange-tokenizer-fn "non matching text")

(defn hui [doc & terms]
  (-> doc
      (z/append-child {:type :hui})
      z/down
      z/rightmost   ;; open-node
      (z/insert-child (u/text-node (apply str (interpose " " terms))))
      z/up))

(def losange-tokenizer
  (u/normalize-tokenizer
   {:tokenizer-fn losange-tokenizer-fn
    :handler (fn [clj-data] {:type :losange
                             :data clj-data})}))

(u/tokenize-text-node losange-tokenizer {} {:text text})

(md/parse* (update u/empty-doc :text-tokenizers conj losange-tokenizer)
           text)

;; ## Parsing with Document Handlers
;;
;; Using tokenizers with document handlers we can let parsed tokens
;; act upon the whole document tree. Consider
;; the following textual example

(def text-with-meta
  "# Examples
## Example1
* `hui`: makes the text ◊(hui much more impactful) indeeed.
")

;; close-node

(def data
  (md/parse*
    (-> u/empty-doc
        (update
          :text-tokenizers conj
          (assoc losange-tokenizer
                 :doc-handler
                 (fn [doc {:keys [match]}]
                   (apply (eval (first match)) doc (rest match))))))
             text-with-meta))

(md.transform/->hiccup data)

(md.transform/->hiccup
 (assoc md.transform/default-hiccup-renderers
        ;; :doc specify a custom container for the whole doc
        :doc (partial md.transform/into-markup [:div.viewer-markdown])
        ;; :text is funkier when it's zinc toned
        :text (fn [_ctx node] [:span {:style {:color "#71717a"}} (:text node)])
        ;; :plain fragments might be nice, but paragraphs help when no reagent is at hand
        :plain (partial md.transform/into-markup [:p {:style {:margin-top "-1.2rem"}}])
        ;; :ruler gets to be funky, too
        :ruler (constantly [:hr {:style {:border "2px dashed #71717a"}}]))
 data)

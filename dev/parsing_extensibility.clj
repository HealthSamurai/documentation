;; (ns parsing-extensibility
;;   (:require
;;    [nextjournal.markdown.transform :as md.transform]
;;    [nextjournal.markdown :as md]
;;    [nextjournal.markdown.utils :as u]
;;    [edamame.core :as edamame]
;;    [clojure.zip :as z]))
;;
;; (def text "hello\n{hui\nsomecontent\npizda}")
;;
;; (def my-tokenizer
;;   (u/normalize-tokenizer
;;    {:regex #"(?s)\{hui\n(.*)\npizda\}"
;;     :handler (fn [match]
;;                {:type :some-type
;;                 :text (match 1)})}))
;;
;; ((:tokenizer-fn my-tokenizer) text)
;;
;; (u/tokenize-text-node my-tokenizer {} {:text text})
;;
;; (:content
;;   (md/parse*
;;     (update u/empty-doc :text-tokenizers conj my-tokenizer)
;;     text))

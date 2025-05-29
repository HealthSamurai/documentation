(ns playground-search
  (:require
   [clojure.string :as str]
   [msync.lucene.analyzers :as analyzers]
   [msync.lucene :as lucene]
   [msync.lucene.document :as ld]
   [msync.lucene.query :as q]))

;; (defonce default-analyzer (analyzers/standard-analyzer))
;;
;; (defonce keyword-analyzer (analyzers/keyword-analyzer))
;;
;; (def album-data-analyzer
;;   (analyzers/per-field-analyzer
;;     default-analyzer
;;     {:h1     default-analyzer
;;      :title  default-analyzer}))
;;
;; (def data-index (lucene/create-index!
;;                    :type :memory
;;                    :analyzer album-data-analyzer))
;;
;; (def data
;;   [{:h1 ["Aidbox" "AidboxDB" "PostgreSQL"]
;;     :title "Aidbox Architecture"}
;;    {:h1 ["Telegram chat" "Zulip"]
;;     :title "Contact us"}
;;
;;    {:h1 ["SearchParameter" "SearchParameter types"]
;;     :title "FHIR Search"}
;;
;;    {:h1 ["Explore SearchParameter in AidboxUI"
;;          "Create SearchParameter"]
;;     :title "Custom SearchParameter"}])
;;
;; (lucene/index! data-index data {:stored-fields [:h1 :title]})

;; (defn search [s]
;;   (let [fields [[:title 100] [:h1 50] [:h2 25] [:h3 12] [:text 6]]]
;;     (->>
;;       (for [[field priority] fields]
;;         (->>
;;           (concat
;;             (lucene/search
;;               data-index
;;               {field s}
;;               {:results-per-page 2
;;                :hit->doc ld/document->map})
;;             (lucene/search
;;               data-index
;;               {field s}
;;               {:results-per-page 2
;;                :fuzzy? true
;;                :hit->doc ld/document->map}))
;;           (filter seq)
;;           (mapv #(update % :score (partial * priority)))))
;;       (flatten)
;;       (distinct)
;;       (sort-by :score))))
;;
;; (search "contact us")
;; #_(for [t ["contact us" "contact u" "contact"]]
;;   (search t))
;;

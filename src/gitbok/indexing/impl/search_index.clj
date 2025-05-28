(ns gitbok.indexing.impl.search-index
  (:require
   [system]
   [msync.lucene.analyzers :as analyzers]
   [msync.lucene :as lucene]
   [msync.lucene.document :as ld]
   [clojure.string :as str]
   [msync.lucene.query :as q]
   [gitbok.constants :as const]))


(defn process-text [text-nodes]
  (mapv :text text-nodes))

(defn filter-types [types doc]
  (filterv #(contains? types (:type %)) doc))

(defn heading-level-preprocess [doc]
  (mapv
   (fn [{:keys [heading-level content] :as heading}]
     (cond
       heading-level
       {(keyword (str "h" heading-level))
        (first (process-text (filter-types #{:text} content)))}

       :else
       heading)) doc))

(defn flatten-nodes
  ([nodes]
   (flatten-nodes nodes #{:heading :paragraph :bullet-list :list-item :code :text}))
  ([nodes allowed-types]
   (letfn [(walk [node]
             (let [current (when (allowed-types (:type node))
                             [node])
                   children (cond
                              (map? node) (vals node)
                              (sequential? node) node
                              :else nil)]
               (into (or current []) (mapcat walk children))))]
     (vec (mapcat walk nodes)))))

(defn flatten-headings-to-map [coll]
  (reduce
   (fn [acc item]
     (let [[k v] (first item)]
       (update acc k (fnil conj []) v)))
   {}
   coll))


(defn process-paragraph [paragraph-nodes]
  (vec (flatten (mapv (fn [{:keys [content]}]
                        (process-text (filter-types #{:text} content)))
                      paragraph-nodes))))

(def selected-keys #{:h1 :h2 :h3 :text})

(defn get-title [parsed-md-file]
  (->> parsed-md-file
       (some #(when (= :heading (:type %))  %))
       :content
       first
       :text))

(defn parsed-md->pre-index [parsed-md]
  (let [title (get-title parsed-md)
        _ (def parsed-md parsed-md)
        _ (when-not title (throw (Exception. "!!!!!!!!!!!!") ) )

        groupped-by-type (->>
                           parsed-md
                           (flatten-nodes)
                           (group-by :type))
        good-headings
        (merge (dissoc groupped-by-type :heading)
               (flatten-headings-to-map
                 (heading-level-preprocess
                   (:heading groupped-by-type))))
        good-text
        (update good-headings :text process-text)
        paragraphs (process-paragraph (:paragraph good-text))
        index (update good-text :text concat paragraphs)
        index (update index :text vec)]
    (assoc (select-keys index selected-keys) :title title)))

(def default-analyzer (analyzers/standard-analyzer))

(def keyword-analyzer (analyzers/keyword-analyzer))

(def gitbok-data-analyzer
  (analyzers/per-field-analyzer
   default-analyzer
   {:title default-analyzer
    :h1 default-analyzer
    :h2 keyword-analyzer
    :h3 keyword-analyzer
    :text keyword-analyzer
    }))

(defn replace-nils [data]
  (mapv (fn [{:keys [h1 title h2 h3 text] :as d}]
          (cond-> d
            (not h1)
            (assoc :h1 "")

            (not h2)
            (assoc :h2 "")

            (not h3)
            (assoc :h3 "")

            (not text)
            (assoc :text "")

            (not title)
            (assoc :title ""))) data))

(defn create-search-index [data]
  (let [index (lucene/create-index!
                :type :memory
                :analyzer gitbok-data-analyzer)
        data (mapv #(select-keys % [:h1 :title]) data)
        data (remove empty? data)
        ;; data (replace-nils data)
        ]
    (def data data)
    (first data)
    (filter #(= "FHIR Search" (:title %)) data)
    (lucene/index!
      index
      data
      {:stored-fields  [:title :h1 :h2 :h3 :text]
       ;; :suggest-fields [:title :h1 :h2]
       ;; :context-fn     :Genre
       })
    index))

(defn parsed-md-idx->index [parsed-md-idx]
  (create-search-index
    (mapv (fn [page]
            (parsed-md->pre-index (:content page)))
          parsed-md-idx)))


;; todo
;; ---
;; description: Aidbox has support of GCP Pub/Sub integration
;; ---

;; (q/parse "title" {:analyzer gitbok-data-analyzer} )
(defn search [index q]
  (def q q)
  (def index index)
  (lucene/search
    index
    {:title q
     #_#{"fhir" "search"}}
    {:results-per-page 5
     :analyzer gitbok-data-analyzer
     :field-name :title
     :hit->doc
     #(ld/document->map %)
     ;; #(ld/document->map % :multi-fields [:h1 :h2 :title])
     :fuzzy? true}))

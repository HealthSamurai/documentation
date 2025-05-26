(ns gitbok.indexing.impl.search-index
  (:require
   [system]

   [msync.lucene.analyzers :as analyzers]
   [msync.lucene :as lucene]
   [msync.lucene.document :as ld]
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

(defn parsed-md->pre-index [parsed-md]
  (let [groupped-by-type (->>
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
        index (update good-text :text concat paragraphs)]
    (select-keys index selected-keys)))

(def default-analyzer (analyzers/standard-analyzer))

(def keyword-analyzer (analyzers/keyword-analyzer))

(def gitbok-data-analyzer
  (analyzers/per-field-analyzer
   default-analyzer
   {:h1 keyword-analyzer
    :h2 keyword-analyzer}))

(defn create-search-index [pre-index]
  (lucene/index!
    (lucene/create-index!
      :type :memory
      :analyzer gitbok-data-analyzer)
    pre-index
    {:stored-fields  [:title :h1 :h2]
     :suggest-fields [:title :h1 :h2]
     ;; :context-fn     :Genre
     }))

(defn parsed-md-idx->index [parsed-md-idx]
  (def parsed-md-idx parsed-md-idx)
  (type parsed-md-idx)
  (:content (first parsed-md-idx))
  (create-search-index
    (mapv (fn [page]
            (parsed-md->pre-index (:content page)))
          parsed-md-idx)))

(defn search [index q]
  (lucene/search
    index
    {:title q}
    {:results-per-page 3
     :hit->doc
     #(ld/document->map %)
     ;; #(ld/document->map % :multi-fields [:h1 :h2 :title])
     :fuzzy? true}))

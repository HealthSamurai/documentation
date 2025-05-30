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

(def selected-keys #{:h1 :h2 :h3 :h4 :text})

(defn get-title [parsed-md-file]
  (->> parsed-md-file
       (some #(when (and
                     (= :heading (:type %))
                     (-> % :content first :text
                         (str/starts-with? "description")
                         not))  %))
       :content
       first
       :text))

(defn parsed-md->pre-index [parsed-md]
  (let [title (get-title parsed-md)
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
    :h2 default-analyzer
    :h3 default-analyzer
    :h4 default-analyzer
    :text default-analyzer
    :filepath keyword-analyzer}))

(defn clean-field [item]
  (let [col? (sequential? item)
        without-nils (if col? (remove nil? item) item)
        default? (empty? without-nils)]
    (if default? ["-"] without-nils)))

(defn ensure-headings-and-text [items]
  (mapv
   (fn [item]
     (-> item
         (update :h1 clean-field)
         (update :h2 clean-field)
         (update :h3 clean-field)
         (update :h4 clean-field)
         (update :text clean-field)))
   items))

(defn create-search-index [data]
  (let [index (lucene/create-index!
               :type :memory
               :analyzer gitbok-data-analyzer)
        data (ensure-headings-and-text data)
        data
        (mapv
         #(select-keys %
                       [:h1 :title :filepath
                        :h2 :h3 :h4 :text])
         data)
        data (remove empty? data)]
    (lucene/index!
     index
     data
     {:stored-fields [:title
                      :h1
                      :h2
                      :h3
                      :h4
                      :text
                      :filepath]})
    index))

(defn parsed-md-idx->index [parsed-md-idx]
  (create-search-index
   (mapv (fn [{:keys [filepath parsed]}]
           (assoc
            (parsed-md->pre-index
             (:content parsed))
            :filepath filepath))
         parsed-md-idx)))

(defn distinct-by [f coll]
  (vals (into {} (map (juxt f identity)) coll)))

(defn search [index q]
  (let [fields [[:title 100]
                [:h1 100] ;; not sure...
                [:h2 50]
                [:h3 12]
                [:h4 6]
                [:text 3]]]
    (->>
     (for [[field priority] fields]
       (->>
        (concat
         (lucene/search
          index
          {field q}
          {:results-per-page 5
           :hit->doc ld/document->map})
         (lucene/search
          index
          {field q}
          {:results-per-page 5
           :fuzzy? true
           :hit->doc ld/document->map}))
        (filter seq)
        (mapv (fn [result]
                (-> result
                    (update :score (partial * priority))
                    (assoc :hit-by field)
                    (assoc :filepath ()))))))

     (flatten)
     (distinct)
     (distinct-by (juxt :hit-by :doc-id))
     (sort-by :score >))))

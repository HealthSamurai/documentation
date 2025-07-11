(ns gitbok.indexing.impl.search-index
  (:require
   [system]
   [msync.lucene.analyzers :as analyzers]
   [msync.lucene :as lucene]
   [msync.lucene.document :as ld]
   [clojure.string :as str]
   [gitbok.utils :as utils]))

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

(defn parsed-md->pre-index [parsed-md title]
  (let [groupped-by-type (->>
                          parsed-md
                          (flatten-nodes)
                          (group-by :type))
        good-headings
        (merge
         (dissoc groupped-by-type :heading)
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
   (mapv (fn [{:keys [filepath
                      parsed
                      title
                      _description]}]
           (when-not title
             (throw (Exception. (str "Cannot find title in " filepath))))
           (assoc
            (parsed-md->pre-index
             (:content parsed)
             title)
            :filepath filepath))
         parsed-md-idx)))

(defn clean-search-res [search-res]
  (update search-res :hit
          (fn [hit-map]
            (cond-> hit-map

              (= "-" (:title hit-map))
              (dissoc :title)

              (= "-" (:h1 hit-map))
              (dissoc :h1)

              (= "-" (:h2 hit-map))
              (dissoc :h2)

              (= "-" (:h3 hit-map))
              (dissoc :h3)

              (= "-" (:h4 hit-map))
              (dissoc :h4)

              (= "-" (:text hit-map))
              (dissoc :text)))))

(defn search [index q]
  (let [fields [[:title 100]
                [:h1 100] ;; not sure...
                [:h2 50]
                [:h3 12]
                [:h4 6]
                [:text 3]]
        ;; Split query into individual words
        words (map str/lower-case (str/split (str/trim q) #"\s+"))
        total-words (count words)]

;; Start with single word search to verify it works
    (if (= 1 total-words)
      ;; Single word - use original logic
      (let [results (->>
                     (for [[field priority] fields]
                       (->>
                        (concat
                         (lucene/search
                          index
                          {field q}
                          {:results-per-page 100
                           :hit->doc ld/document->map})
                         (lucene/search
                          index
                          {field q}
                          {:results-per-page 100
                           :fuzzy? true
                           :hit->doc ld/document->map}))
                        (filter seq)
                        (mapv (fn [result]
                                (-> result
                                    (update :score (partial * priority))
                                    (assoc :hit-by field)
                                    (clean-search-res))))))
                     (flatten)
                     (distinct)
                     (utils/distinct-by (juxt :hit-by :doc-id))
                     (sort-by :score >))]

        results)

      ;; Multi-word search
      (let [all-results (for [word words
                              :when (pos? (count word))]
                          (for [[field priority] fields]
                            (->>
                             (concat
                              (lucene/search
                               index
                               {field word}
                               {:results-per-page 100
                                :hit->doc ld/document->map})
                              (lucene/search
                               index
                               {field word}
                               {:results-per-page 100
                                :fuzzy? true
                                :hit->doc ld/document->map}))
                             (filter seq)
                             (mapv (fn [result]
                                     (-> result
                                         (update :score (fn [score] (* (or score 0) priority)))
                                         (assoc :hit-by field)
                                         (assoc :word word)
                                         (clean-search-res)))))))
            flattened (flatten all-results)
            filtered (filter #(and (map? %) (:score %) (pos? (:score %))) flattened)]

;; Group by document and calculate scores with aggressive multi-word bonuses
        (->> filtered
             (group-by :doc-id)
             (vals)
             (map (fn [doc-results]
                    ;; Count unique words found in this document
                    (let [unique-words (set (map :word doc-results))
                          ;; Also check for words that might be in title/h2 but not found by Lucene
                          doc-hit (first doc-results)
                          title-text (str (:title (:hit doc-hit)) " " (:h2 (:hit doc-hit)))
                          title-text-lower (str/lower-case title-text)
                          additional-words (filter #(str/includes? title-text-lower %) words)
                          all-found-words (set (concat unique-words additional-words))
                          word-count (count all-found-words)
                          ;; Group by field within the document
                          field-groups (group-by :hit-by doc-results)
                          ;; Check if multiple words appear in title/h1
                          title-words (set (map :word (filter #(#{:title :h1} (:hit-by %)) doc-results)))
                          title-multi-word? (> (count title-words) 1)
                          ;; Sum scores for each field, keeping the best score per field
                          field-scores (map (fn [[field field-results]]
                                              (apply max (map :score field-results)))
                                            field-groups)
                          base-score (apply + field-scores)
                          ;; Calculate coverage ratio
                          coverage-ratio (double (/ word-count total-words))
                          ;; Apply aggressive bonuses for multi-word matches
                          multi-word-bonus (cond
                                             (>= word-count 3) 20 ; 3+ words: 20x bonus
                                             (= word-count 2) 10 ; 2 words: 10x bonus  
                                             :else 1) ; 1 word: no bonus
                          ;; Additional bonus for multi-word title matches
                          title-bonus (if title-multi-word? 5 1)
                          ;; Coverage bonus
                          coverage-bonus (+ 1.0 (* coverage-ratio 4.0)) ; 1x to 5x based on coverage
                          ;; Calculate final score
                          final-score (* base-score multi-word-bonus title-bonus coverage-bonus)
                          ;; Use the first result as template and update the score
                          result (first doc-results)]

                      (-> result
                          (assoc :score final-score)
                          (assoc :debug-info {:unique-words unique-words
                                              :additional-words additional-words
                                              :all-found-words all-found-words
                                              :title-text title-text})
                          (dissoc :word)))))
             (filter map?)
             (sort-by :score >))))))

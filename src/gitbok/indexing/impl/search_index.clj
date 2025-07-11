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
        ;; Enhanced filtering for meaningful text content
        filtered (if col?
                   (filter (fn [text]
                             (when (string? text)
                               (let [trimmed (str/trim text)
                                     length (count trimmed)
                                     ;; Count letters vs total characters
                                     letter-count (count (re-seq #"\p{L}" trimmed))
                                     letter-ratio (if (> length 0) (/ letter-count length) 0)
                                     ;; Check for common stop words and fragments
                                     stop-words #{"and" "the" "a" "an" "or" "but" "in" "on" "at" "to" "for" "of" "with" "by"}
                                     is-stop-word? (stop-words (str/lower-case trimmed))
                                     ;; Check for parenthetical fragments or incomplete sentences
                                     starts-with-punct? (and (> length 0) (re-matches #"^[^\p{L}].*" trimmed))
                                     ends-with-punct-only? (and (> length 0) (re-matches #".*[^\p{L}\.]$" trimmed))
                                     mostly-punct? (< letter-ratio 0.5)]
                                 (and
                                  (>= length 8) ; Minimum 8 characters
                                  (not is-stop-word?) ; Not a common stop word
                                  (> letter-ratio 0.3) ; At least 30% letters
                                  (not mostly-punct?) ; Not mostly punctuation
                                  (not (and starts-with-punct? ends-with-punct-only?)))))) ; Not a fragment
                           without-nils)
                   without-nils)
        default? (empty? filtered)]
    (if default? ["-"] filtered)))

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

(defn search-field-for-word
  "Search a specific field for a word with both exact and fuzzy matching."
  [index field word priority]
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
               (clean-search-res))))))

(defn find-additional-words-in-title
  "Find query words that appear in title/h2 but weren't found by Lucene."
  [doc-results words]
  (let [doc-hit (first doc-results)
        title-text (str (:title (:hit doc-hit)) " " (:h2 (:hit doc-hit)))
        title-text-lower (str/lower-case title-text)]
    (filter #(str/includes? title-text-lower %) words)))

(defn calculate-word-bonuses
  "Calculate various bonuses for multi-word matches."
  [unique-words additional-words words doc-results total-words]
  (let [all-found-words (set (concat unique-words additional-words))
        word-count (count all-found-words)
        title-words (set (map :word (filter #(#{:title :h1} (:hit-by %)) doc-results)))
        title-multi-word? (> (count title-words) 1)
        coverage-ratio (double (/ word-count total-words))
        multi-word-bonus (cond
                           (>= word-count 3) 20 ; 3+ words: 20x bonus
                           (= word-count 2) 10 ; 2 words: 10x bonus  
                           :else 1) ; 1 word: no bonus
        title-bonus (if title-multi-word? 5 1)
        coverage-bonus (+ 1.0 (* coverage-ratio 4.0))]
    {:word-count word-count
     :all-found-words all-found-words
     :multi-word-bonus multi-word-bonus
     :title-bonus title-bonus
     :coverage-bonus coverage-bonus}))

(defn calculate-document-score
  "Calculate final score for a document based on found words and bonuses."
  [doc-results words total-words]
  (let [unique-words (set (map :word doc-results))
        additional-words (find-additional-words-in-title doc-results words)
        field-groups (group-by :hit-by doc-results)
        field-scores (map (fn [[field field-results]]
                            (apply max (map :score field-results)))
                          field-groups)
        base-score (apply + field-scores)
        bonuses (calculate-word-bonuses unique-words additional-words words doc-results total-words)
        word-count (or (:word-count bonuses) 0)
        ;; Check for exact title match (all query words in title)
        doc-hit (first doc-results)
        title-text (str/lower-case (str (:title (:hit doc-hit))))
        words-in-title (filter #(str/includes? title-text %) words)
        title-coverage (double (/ (count words-in-title) total-words))
        exact-title-match? (= title-coverage 1.0)
        ;; Use additive bonuses instead of multiplicative to prevent inflation
        multi-word-boost (case word-count
                           3 50 ; 3+ words: +50 points
                           2 20 ; 2 words: +20 points  
                           1 0 ; 1 word: no bonus
                           0)
        title-words (set (map :word (filter #(#{:title :h1} (:hit-by %)) doc-results)))
        title-boost (if (> (count title-words) 1)
                      15 ; Multiple words in title: +15 points
                      0) ; Single word in title: no bonus
        ;; Add significant bonus for exact title matches
        exact-title-bonus (if exact-title-match? 100 0) ; +100 points for exact title match
        coverage-ratio (or (:coverage-ratio bonuses) 0.0)
        coverage-boost (* coverage-ratio 10) ; Up to +10 points for coverage
        final-score (+ base-score multi-word-boost title-boost exact-title-bonus coverage-boost)
        result (first doc-results)]
    (-> result
        (assoc :score final-score)
        (dissoc :word))))

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
                            (search-field-for-word index field word priority)))
            flattened (flatten all-results)
            filtered (filter #(and (map? %) (:score %) (pos? (:score %))) flattened)]

;; Group by document and calculate scores with aggressive multi-word bonuses
        (->> filtered
             (group-by :doc-id)
             (vals)
             (map #(calculate-document-score % words total-words))
             (filter map?)
             (sort-by :score >))))))

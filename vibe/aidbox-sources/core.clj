(ns box.search.core
  (:require [aidbox.config.common :as config.common]
            [aidbox.settings.web :as settings.web]
            [aidbox.settings.fhir :as settings.fhir]
            [io.healthsamurai.settings.core :as settings]
            [aidbox.context :as context]
            [box.search.query]
            [cheshire.core]
            [clj-pg.honey :as pg]
            [clojure.java.io]
            [clojure.string :as str]
            [dsql.core]
            [dsql.pg :as dsql]
            [ql.core]
            [sci.core :as sci]
            [zen.core]))

  ;; query-string -> params -> +meta -> validation -> query -> includes etc

(ql.core/ql-fn :aidbox_text_search :texts)
(ql.core/ql-fn :knife_extract_text :doc :expr)

(defmethod ql.core/to-sql :jsonb
  [acc [_ v]]
  (ql.core/conj-sql acc (str "$$" (cheshire.core/generate-string v) "$$")))

(def projection
  {:resource {:ql/type :pg/||
              :left :r.resource
              :right {:ql/type :jsonb/build-object
                      :jsonb/strip-nulls true
                      :id :r.id
                      :resourceType :r.resource_type
                      :meta {:ql/type :pg/||
                             :jsonb/strip-nulls true
                             :left [:ql/call :coalesce [:jsonb/-> :r.resource :meta] "{}"]
                             :right {:ql/type :jsonb/build-object
                                     :lastUpdated :r.ts
                                     :versionId [:pg/to-text :r.txid]}}}}})

(defn build-query [_ctx rt _qs]
  (ql.core/sql
   {:ql/type :ql/select
    :ql/select projection
    :ql/from {:r (keyword (str/lower-case rt))}
    :ql/limit 100}
   {:format :jdbc}))

;; (build-query {} "Patient" "?")

(defn mk-bundle [rows]
  {:resourceType "Bundle"
   :type         "search-result"
   :entry        rows})

(defn build-expression [_k {expr :expression tp :type :as param}]
  (cond
    (= tp "string")
    (let [pth (->> expr (mapv (fn [pth] (->> pth (mapv :el)))))]
      {:ql/type :aidbox_text_search
       :texts {:ql/type :knife_extract_text
               :doc :r.resource
               :expr [:jsonb pth]}})

    (= tp "date")
    (let [pth (keyword (str "{" (->> (first expr) (mapv :el) (str/join ".") ) "}"))]
      {:ql/type :ql/cast
       :type :timestamp
       :val {:ql/type :jsonb/->>
             :left :r.resource
             :right pth}})

    :else
    (assert false (pr-str param))))

(def ql-map {:or :ql/or :and :ql/and})

(defn build-where-expr [q expr]
  (let [op (first expr)]
    (cond
      (= :start-with op)
      (let [[_ param-name val] expr
            _param (get-in q [:params param-name])]
        [:ql/ilike (keyword (str (name param-name) ".expr"))
         [:ql/param (str " %" val "%")]])

      (= :lt op)
      (let [[_ param-name val] expr
            _param (get-in q [:params param-name])]
        [:ql/< (keyword (str (name param-name) ".expr"))
         [:ql/param val]])

      :else
      (assert false (pr-str expr)))))

(defn build-where [q expr]
  (cond
    (sequential? expr)
    (let [op (first expr)
          ql-op (get ql-map op)]
      (if ql-op
        (into [ql-op] (mapv #(build-where q %) (rest expr)))
        (build-where-expr q expr)))))

;; example
{:ql/select {:resource :r.resource
             :name_expr :name_expr.expr}
 :ql/from {:r :patient
           :name_expr {:ql/type :pg/lateral
                       :ql/select {:ql/select
                                   {:expr {:ql/type :aidbox_text_search
                                           :texts {:ql/type :knife_extract_text
                                                   :doc :r.resource
                                                   :expr [:jsonb [["name" "given"] ["name" "family"]]]}}}}}}
 :ql/where {:by-name {:ql/type :ql/and
                      :dave [:ql/ilike :name_expr.expr [:ql/param "% dav%"]]}}}

(defn query-by-params [_ctx {rt :resourceType
                            params :params
                            filter :filter
                            count :count
                            :as q}]
  (let [tbl (keyword (str/lower-case rt))]
    {:ql/select projection
     :ql/from (reduce (fn [acc [k param]]
                        (assoc acc k {:ql/type :pg/lateral
                                      :ql/select {:ql/select {:expr (build-expression k param)}}})
                        ) {:r tbl} params)
     :ql/where (build-where q filter)
     :ql/limit (or count 100)}))

(defn search-query [_params])

(defn search-operation
  [{db :db/connection {params :params route-params :route-params qs :query-string :as _req} :request :as ctx}]
  (if (:query params)
    (box.search.query/query-operation ctx)
    (let [{rt :resource/type} route-params
          query (build-query ctx rt qs)
          rows (pg/query db query)
          bundle (-> (mk-bundle rows)
                     (assoc :query-sql query))]
      {:response {:status 200
                  :body bundle}})))


(comment
  ;; fhir search dsl


  {:type "Patient"
   :page 20
   :count 10
   :elements {:name true}
   :joins [:encounter {:table "encounter"
                :on {:ql/type "="
                     :type "reference"}}]
   :filter {:ql/type :and
            :name {:ql/type :pred
                   :on :patient
                   :type :string
                   :op :start-with
                   :value "John"
                   :expressions [{:path [:name]
                                  :type :HumanName}]}

            :birth {:ql/type :pred
                    :type :date
                    :on :patient
                    :op :lt
                    :value "1980-01-01"
                    :expression [{:path [:birthDate]
                                  :type :date}]}}})


;;--------------------------------------------------------------------------------------------------------------------------------


(defmethod dsql.core/to-sql
  :aidbox/text_search
  [acc opts [_ res jsonpath]]
  (-> acc
      (dsql.core/to-sql
       opts
       ^:pg/fn[:lower
               ^:pg/fn[:unaccent
                       ^:pg/fn[:regexp_replace
                               [:pg/cast ^:pg/fn[:jsonb_path_query_array res jsonpath] :text]
                               "(\", |[\\[\\]])"
                               ""
                               "g"]]])))


(defn zenbox-str [_ctx & args]
  (str/join args))


(defn prefix-table [{:keys [rt]} attr]
  (keyword (str (name rt) "." (name attr))))


(defn text-value [{:keys [values]}]
  (str/join "," values))


(defn list-params [{:keys [values]}]
  [:pg/params-list values])


(defn date-value [{:keys [values]}]
  (let [date (first values)]
    (cond
      (or
       (re-find #"^\d{4}$" date)
       (re-find #"^\d{4}-\d{2}$" date))
      date

      :else
      [:pg/cast date :timestamp])))


(defn extract-date [{:as ctx, path :path}]
  (let [date (get-in ctx [:values 0])]
    (cond
      (re-find #"^\d{4}$" date)
      [:pg/extract
       [:pg/identifier "YEAR"]
       [:pg/cast [:#>> (prefix-table ctx :resource) path] :timestamp]]

      (re-find #"^\d{4}-\d{2}$" date)
      [:||
       [:pg/extract
        [:pg/identifier "YEAR"]
        [:pg/cast [:#>> (prefix-table ctx :resource) path] :timestamp]]
       "-"
       ^:pg/fn[:lpad
               [:pg/cast [:pg/extract
                          [:pg/identifier "MONTH"]
                          [:pg/cast [:#>> (prefix-table ctx :resource) path] :timestamp]]
                :text] 2 "0"]]

      :else
      [:pg/cast [:#>> (prefix-table ctx :resource) path] :timestamp])))


(defn parse-quantity [s]
  (let [[op-value system unit] (mapv not-empty (str/split s #"\|" 3))
        [_ op value]           (re-matches #"(eq|lt|gt|le|ge|ne|sa|eb|ap)?(.+)" op-value)]
    {:value  value
     :unit   unit
     :system system
     :op     op}))


#_(defonce ucum-serv (org.fhir.ucum.UcumEssenceService. (.openStream (clojure.java.io/resource "ucum-essence.xml"))))


(defn to-canonical-quantity [{:as _q :keys [_unit _value]}]
  nil
  #_(when (not (str/blank? unit))
    (let [canonical-pair (.getCanonicalForm ucum-serv (org.fhir.ucum.Pair. (org.fhir.ucum.Decimal. value) unit))]
      (assoc q
             :unit (.getCode canonical-pair)
             :value (bigdec (.toString (.getValue canonical-pair)))))))


(defn extract-quantity-unit [{:keys [values]}]
  (:unit (parse-quantity (first values))))


(defn extract-quantity-value [{:keys [values]}]
  (:value (parse-quantity (first values))))


(defn extract-quantity-canonical-unit [{:keys [values]}]
  (:unit (to-canonical-quantity (parse-quantity (first values)))))


(defn extract-quantity-canonical-value [{:keys [values]}]
  (:value (to-canonical-quantity (parse-quantity (first values)))))


(defn quantity-unit-path [{:keys [path]}]
  (conj path :code))


(defn quantity-value-path [{:keys [path]}]
  (conj path :value))


(defn extract-period-start [{:as ctx, path :path}]
  (let [_date (date-value ctx)]
    [:pg/cast [:#>> (prefix-table ctx :resource) (conj path :start)] :date]))


(defn extract-period-end [{:as ctx, path :path}]
  (let [_date (date-value ctx)]
    [:pg/cast [:#>> (prefix-table ctx :resource) (conj path :end)] :date]))


(defn generate-opts [ctx]
  (let [fns (into {}
                  (map (fn [[k v]]
                         (if (fn? v)
                           {k (partial v ctx)}
                           {k v})))
                  {'str                  zenbox-str
                   'table                prefix-table
                   'list-params          list-params
                   'text-value           text-value
                   'date-value           date-value
                   'extract-date         extract-date
                   'extract-period-start extract-period-start
                   'extract-period-end   extract-period-end

                   'quantity-unit-path               quantity-unit-path
                   'quantity-value-path              quantity-value-path
                   'extract-quantity-unit            extract-quantity-unit
                   'extract-quantity-value           extract-quantity-value
                   'extract-quantity-canonical-unit  extract-quantity-canonical-unit
                   'extract-quantity-canonical-value extract-quantity-canonical-value})]
    {:namespaces {'zenbox.search fns}}))


(defn eval-template [ctx tpl]
  (let [opts (generate-opts ctx)]
    (sci/eval-form (sci/init opts) tpl)))


(defn add-full-url-and-link [entry ctx]
  (let [base-url (some-> (context/get-settings ctx) (settings.web/get-base-url))
        rt (get-in entry [:resource :resourceType])
        id (get-in entry [:resource :id])
        full-url (str base-url "/" rt "/" id)]
    (assoc entry :fullUrl full-url :link [{:relation "self" :url full-url}])))


(def prefixes #{"eq" "lt" "gt" "le" "ge" "ne" "sa" "eb" "ap"})


(defn has-prefix? [v]
  (let [prefix (subs v 0 2)]
    (prefixes prefix)))

(defn parse-page-param [query]
  (if-let [p (second (re-matches #".*page=(\d+).*" (or query "")))]
    (Integer. p)
    1))


(defn remove-duplicates [query]
  (cond-> query
    (and (str/includes? query "_page") (some #(str/includes? query %) ["?page" "&page"]))
    (str/replace #"_page=(\d+)&?" "")
    (and #(str/includes? query "_totalMethod") (some #(str/includes? query %) ["_total="]))
    (str/replace #"_totalMethod=(\w+)&?" "")))


(defn page-param-replace [q p]
  (-> q
      remove-duplicates
      (str/replace #"page=(\d+)" (fn [_] (str "page=" p)))))


(defn build-paging [parsed-qs]
  (let [page (Integer. (or (get-in parsed-qs [:page :default 0])
                           (get-in parsed-qs [:_page :default 0])
                           0))
        page (if (> page 0) (dec page) page)
        limit (Integer. (or (get-in parsed-qs [:count :default 0])
                            (get-in parsed-qs [:_count :default 0])
                            100) ) ]
    {:limit limit
     :offset (* page limit)}))


(defn build-links [ctx rt query total parsed-qs]
  (let [compliant-mode-enabled? (-> (context/get-settings ctx)
                                    (settings/get-value settings.fhir/compliant-mode)
                                    deref)
        {limit :limit} (build-paging parsed-qs)
        query (cond (str/blank? query)      "page=1"
                    (re-find #"page" query) query
                    :else                   (str query "&page=1"))
        base-url (cond->> (if-let [uri (:uri (:request ctx))]
                            (str uri "?" query)
                            (str "/"
                                 (when (:fhir/request? ctx) "fhir/")
                                 (name rt)
                                 "?"
                                 query))
                   compliant-mode-enabled?
                   (str (some-> (context/get-settings ctx) (settings.web/get-base-url))))
        page (parse-page-param query)
        last (let [pages-count (if (pos? limit)
                                 (int (Math/ceil (/ total limit)))
                                 0)]
               (if (= 0 pages-count) 1 pages-count))
        links [{:relation "first" :url (page-param-replace base-url 1)}]]
    (cond-> links
      :always
      (conj {:relation "self"     :url (page-param-replace base-url page)})
      (< page last)
      (conj {:relation "next"     :url (page-param-replace base-url (inc page))})
      (and (> page 1) (<= page last))
      (conj {:relation "previous" :url (page-param-replace base-url (dec page))})
      (> last 1)
      (conj {:relation "last"     :url (page-param-replace base-url last)}))))


(defn build-sort [ctx resolved-params parsed-qs]
  (let [sorts (->> (get-in parsed-qs [:_sort :default])
                   (reduce (fn [acc param]
                             (let [sort-dir (if (str/starts-with? param "-") :desc :asc)
                                   param-name (if (= :desc sort-dir) (subs param 1) param)
                                   sort-q (get-in resolved-params [(keyword param-name) :sort])
                                   ]
                               (assoc acc sort-q sort-dir))
                             )
                           {})
                   (eval-template ctx))]
    {:order-by sorts}))

(defn parse-params [query-string resolved-params]
  (->> (str/split query-string #"&")
       (mapv #(str/split % #"="))
       (reduce (fn [acc [k v]]
                 (let [[prop modifier] (mapv keyword (str/split k #":" 2))
                       ordered-param? (get-in resolved-params [prop :ordered-param])
                       original-values (str/split v #",")
                       processed-values (if ordered-param?
                                          (mapv (fn [v] (if (has-prefix? v) (subs v 2) v)) original-values)
                                          original-values)
                       prefix (when (and ordered-param?
                                         (has-prefix? (first original-values)))
                                (-> original-values first (subs 0 2) keyword))]
                   (assoc-in acc [prop (or modifier prefix :default)] processed-values))) {})))

;; 1. param-name:modifier=[prefix]value
;; 2. param-name=v1&param-name&v2
;; 3. param-name=v1&param-name&v2
;; 2. chained

;; date=2013&date=2014

{:prefixes  {}
 :modifiers {}}

;; _filter => parse
;; query => parse2
;; parse || parse2

;; _filter => conditions tree

;; (given eq "peter" and birthdate ge 2014-10-10) OR id = "pt1" OR NOT (gender = "male")

{:expr "name.given"
 :type "string"}

;; {
;;  :join {}
;;  :where
;;  [:and
;;   [:or
;;    [:parens
;;     [:and
;;      [:query 'spname {:op :eq :value "?"}]
;;      [:eq {ref 'sp-name} "peter"] ;;=> dsql
;;      => (query 'sp-name {:op :eq :value "peter"})
     
;;      [:ge {:sp :birthdate} "2014-10-10"]]]
;;    [:eq {:ref 'sub-name :as :x} "value"]
;;    [:= :id "pt1"]
;;    [:NOT [:= {:sp :gender "male"}]]]
;;   [:query 'custom {:op :eq :params {}}]
;;   [:eq :_id ["v1" "v2"]]]}

;; join, where,sort, [group-by, select]

;; query=> filter || filter => join => interpret
;; ++ include/revincludes => _with merge with _with => interpret

;; Patient?birthdate=gt2000&_filter='(org eq o1 or mangpract eq p1)'&_query=my&prid=p1
;; => AST?

;; [:and
;;  [:filter  [....]]
;;  [:params   ....]
;;  [:query    ....]]

;; AST
;; (query elements ) => select
;; (query sort ) => sort
;; (query join ) => join
;; (query name) => WHERE (join,sort)
;; (query SP name)

;; EncounterSearch
;; {:join {:patient {:on {}
;;                   :ref PatientSearch
;;                   :params {:name {}
;;                            :birthdate {}}}}
;;  :params {:name name-sp}
;;  :query  {:mypt QueryDef}
;;  :with   {:patient {}
;;           :practitioner {}
;;           :Condition.based-on {}}}

;; rpc/search
;; {:type "Patient"
;;  :params {:name      {:op "start-with" :val "john"}
;;           :birthDate {:op ">" :val "180"}}}

;; => fhir querystring => ref search
;; => search dsl => ref search
;; => graphql =>  ref search

;; => ref search
;; ;; internal api
;; {:query {:select {:name #{:given :family}}
;;          :from "Encounter"
;;          :join {:pt {:on "Patient" :ref "subject"}}
;;          :where [:and [:or [:sp/pt.name "join"]] [:sp/date ">" 2021]]
;;          :limit 100}
;;  :timetout 1000
;;  :with {}}


(defn search [{:as ctx, :keys [zen]} {:as _req, :keys [query-string]} op-sym]
  (let [{rt :resourceType, params :params, paths :paths} (zen.core/get-symbol zen op-sym)
        resolved-params (into {} (mapv (fn [[k v]] [k (zen.core/get-symbol zen v)]) params))
        parsed-query-string (parse-params query-string resolved-params)
        where (reduce-kv (fn [acc prop v]
                           (let [[[modifier values]] (seq v)]
                             (if-let [sql-tpl (get-in resolved-params [prop :where modifier])]
                               (assoc acc (str prop modifier) (eval-template {:values values
                                                                              :rt rt
                                                                              :path (get paths prop)} sql-tpl))
                               acc))) {} parsed-query-string)

        query-total (-> (:db/connection ctx)
                        (pg/query (dsql/format {:ql/type :pg/select
                                                :select  ^:pg/fn[:count :*]
                                                :from    rt
                                                :where   where}))
                        first
                        :count)
        query (cond-> {:ql/type :pg/select
                       :select  :*
                       :from    rt
                       :where   where}
                true
                (merge (build-paging parsed-query-string))

                (seq (:_sort parsed-query-string))
                (merge (build-sort {:rt rt}
                                   resolved-params
                                   parsed-query-string)))
        rows (pg/query (:db/connection ctx) (dsql/format query))
        entry (mapv #(add-full-url-and-link % ctx) rows)]
    {:resourceType "Bundle"
     :total query-total
     :link (build-links ctx rt query-string query-total parsed-query-string)
     :entry entry}))

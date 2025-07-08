(ns proto.crud
  (:refer-clojure :exclude [update find read])
  (:require [aidbox.settings.web :as settings.web]
            [aidbox.context :as context]
            [box.metrics.core]
            [cheshire.core :as json]
            [clj-pg.honey :as db]
            [clojure.java.jdbc]
            [clojure.set]
            [clojure.string :as str]
            [honeysql.core :as hsql]
            [honeysql.types :as hsql-types]
            [klog.core :as klog]
            [proto.cache.core :as cache]
            [proto.hooks]
            [proto.outcome :as oo]
            [proto.pg :as pg]
            [proto.preprocessors]
            [proto.search.acl]
            [proto.search.params]
            [proto.search.utils]
            [proto.validation :as validation]
            [trace.core :as trace])
  (:import java.net.URLDecoder
           java.util.regex.Matcher
           (java.util UUID)))

(defn gen-uuid [] (str (UUID/randomUUID)))

(defn map-or-seq? [v]
  (or (map? v)
      (and (sequential? v) (not (string? v)))))

(defn not-primitive? [k]
  (when k (not= \_ (.charAt (name k) 0))))

(defn strip-empty-primitive [x]
  (cond
    (map? x)
    (let [mp* (reduce (fn [acc [k v]]
                        (if (nil? v)
                          (dissoc acc k)
                          (if (map-or-seq? v)
                            (assoc acc k (strip-empty-primitive v))
                            acc))) x x)]
      (if (empty? mp*)
        nil
        mp*))
    (and (sequential? x) (not (string? x)))
    (reduce (fn [acc v]
              (if (nil? v)
                (conj acc v)
                (conj acc
                      (if (map-or-seq? v)
                        (strip-empty-primitive v)
                        v)))) [] x)
    :else x))

(defn strip-empty
  [x]
  (cond
    (map? x)
    (reduce (fn [acc [k v]]
              (if (nil? v)
                (dissoc acc k)
                (if (map-or-seq? v)
                  (if (not-primitive? k)
                    (assoc acc k (strip-empty v))
                    (assoc acc k (strip-empty-primitive v)))
                  acc))) x x)

    (and (sequential? x) (not (string? x)))
    (reduce (fn [acc v]
              (if (nil? v)
                acc
                (conj acc
                      (if (map-or-seq? v)
                        (strip-empty v)
                        v)))) [] x)
    :else x))


(defn url-decode [^String s]
  (-> s
      (.replaceAll "\\+" "%2b") ;; fix plus sign
      URLDecoder/decode))

(defn raw-json
  [json]
  (let [h (hash json)
        h (if (neg? h) (- h) h)]
    (hsql/raw (str "$h" h "$" json "$h" h "$"))))

(defn string-or-column [target]
  (if (string? target)
    (raw-json target)
    target))
;; :search-by 'aidbox.rest.v1/search-by-knife
;;
(defn param-expr [target {:keys [expression value]}]
  (when (seq value)
    [:&&
     (hsql/call :knife_extract_text
                (string-or-column target)
                (raw-json (json/generate-string expression)))
     (hsql-types/array (if (sequential? value) value [value]))]))


(defn expand-params [target  params]
  (let [expr (reduce-kv
              (fn [acc _sp param]
                (conj acc (param-expr target param)))
              [] params)
        expr (remove nil? expr)]
    (when (seq expr)
      (into [:and] expr))))

(defn rn [r] (remove nil? r))

(defn match-and-replace
  [s re replacement]
  (let [buf (StringBuffer.)
        m (re-matcher re s)]
    (loop [res []]
      (if (.find m)
        (do
          (.appendReplacement m buf (Matcher/quoteReplacement replacement))
          (recur (conj res (.group m 1))))
        (do
          (.appendTail m buf)
          [(str buf) res])))))

(defmacro sql-string
  "Replaces ~(foo) to value of `foo`. Replaces ~{bar} to ?. Returns jdbc-like vector with params.
  (sql-string \"SELECT * FROM ~(table) WHERE id = ~{id}\") =>
  [(format \"SELECT * FROM %s WHERE id = ?\" table) id]"
  [s]
  (let [[s' format-replacements] (match-and-replace s #"~\(([\w.*+!?$%&=<>'-]+)\)" "%s")
        [s'' jdbc-replacements] (match-and-replace s' #"~\{([\w.*+!?$%&=<>'-]+)\}" "?")]
    `(vector (format ~s'' ~@(map symbol format-replacements)) ~@(map symbol jdbc-replacements))))


(defn archive-hsql [resource-type id  & [{:keys [ensure-updated
                                                 ensure-deleted]}]]
  (let [tbl-name (pg/table-name resource-type)
        hx-tbl-name (pg/hx-table-name resource-type)]
    {:insert-into [[(hsql/raw hx-tbl-name) [:id :txid :cts :ts :status :resource]]
                   {:select [:id :txid :cts :ts :status :resource]
                    :from [(hsql/raw tbl-name)]
                    :where (rn [:and
                                [:= :id id]
                                (when ensure-updated
                                  [:in "updated" (hsql/raw "(select status from inserted)")])
                                (when ensure-deleted
                                  [:or [:in "created" (hsql/raw "(select status from deleted)")]
                                   [:in "updated" (hsql/raw "(select status from deleted)")]])])}]
     :upsert {:on-conflict [:id :txid]
              :do-update-set! {:status :EXCLUDED.status
                               :ts :EXCLUDED.ts
                               :cts :EXCLUDED.cts
                               :resource :EXCLUDED.resource}}
     :returning [:*]}))

(defn to-json [x] (json/generate-string x))

(defn clear-resource [resource]
  (let [res-meta (when-let [m (:meta resource)]
                   (let [m (dissoc m :versionId :lastUpdated :createdAt)]
                     (when-not (empty? m) m)))]
    (cond->
     (-> resource
         (dissoc :id :resourceType)
         strip-empty
         (or {}))
     res-meta (assoc :meta res-meta)
      (nil? res-meta) (dissoc :meta))))


(def resource-columns [:id :txid :ts :resource_type [(hsql/raw "status::text") :status] :resource :cts])

(defn id-gen [rt {idg :idGeneration seq-pref :sequencePrefix}]
  (if (= idg "sequence")
    (hsql/raw
     (if seq-pref
       (format "( '%s' || nextval('%s_seq'::text)::text)" seq-pref (pg/raw-table-name rt))
       (format "nextval('%s_seq'::text)::text" (pg/raw-table-name rt))))
    (hsql/raw "gen_random_uuid()")))


(defn build-acl-expression [args acl-filter]
  (let [{:keys [filter-table filter-table-alias expression] :as param}
        (proto.search.acl/build-expression-param acl-filter args)]
    (merge {:select [true]
            :where (proto.search.acl/expressions->hsql expression param)
            :limit 1}
           (when filter-table
             {:from [[filter-table filter-table-alias]]}))))

(defn permission-expr
  [rt
   {:keys [resource-json resource-id
           resource-column id-column]
    :as   _expr}
   {crud-params :crud/params
    acl-params  :box.rest.acl/filter
    :as         _params}]
  (cond
    acl-params
    {:json-expr   (when (or resource-json resource-id) #_"TODO: check should be more complex, not just or"
                        (build-acl-expression
                         {:target-resource resource-json
                          :target-id       resource-id
                          :resource-type   rt}
                         acl-params))
     :column-expr (when (or resource-column id-column)
                    (build-acl-expression
                     {:target-resource resource-column
                      :target-id       id-column
                      :resource-type   rt}
                     acl-params))}

    crud-params
    {:json-expr   (when resource-json (expand-params resource-json crud-params))
     :column-expr (when resource-column (expand-params resource-column crud-params))}))

(defn create-sql [txid {rt :resourceType id :id :as resource} & [{{hx :history :as rt-meta} :meta
                                                                  params :params
                                                                  flags :flags}]]
  (when-not rt (throw (Exception. "Resource type required")))
  (let [table-name      (pg/table-name rt)
        resource        (clear-resource resource)
        resource-json   (to-json resource)
        disable-history (= hx "none")
        enable-history  (not disable-history)
        upsert?         (not (:no-upsert flags))
        _duplicate-check? (and (:no-upsert flags) id)
        {:keys [json-expr]} (permission-expr rt
                                             {:resource-json resource-json
                                              :resource-id id}
                                             params)
        ;; TODO investigate honeysql is slow to produce SQL
        q {:with   (remove nil?
                           [(when (and id enable-history) [:archived (archive-hsql rt id)]) ;; do not touch history if new
                            [:_txid {:select [txid]}]
                            ;; "NOTE: there is possibility of insertion into history table when operation is invalid"
                            [:inserted {:insert-into [[[(hsql/raw table-name) (hsql/raw "as t")]   [:id :txid :status :resource]]
                                                      (cond-> {:select   [(or id (id-gen rt rt-meta))
                                                                          (or txid (hsql/raw "nextval('transaction_id_seq'::text)"))
                                                                          (hsql/raw "'created'")
                                                                          resource-json]}
                                                        json-expr
                                                        (clojure.core/update :where (fnil conj [:and]) json-expr))]

                                        :upsert      (when upsert?
                                                       {:on-conflict    [:id]
                                                        :do-update-set! {:txid     :EXCLUDED.txid
                                                                         :status   (hsql/raw "'updated'")
                                                                         :ts       (hsql/raw "current_timestamp")
                                                                         :resource :EXCLUDED.resource}
                                                        :where          (hsql/raw "t.resource <> EXCLUDED.resource")})
                                        :returning   [:*]}]])
           :select [:*]


           :from   [[{:union-all (remove nil?
                                         [{:select resource-columns :from [:inserted] :limit 1}
                                          (when upsert?
                                            {:select [:id :txid :ts :resource_type [(hsql/raw "'unchanged'") :status] :resource :cts]
                                             :from   [(hsql/raw table-name)] :where [:= :id id] :limit 1})])} :_]]



           :limit  2}]
    (hsql/format q)))

(defn upsert-sql [ctx txid resources]
  (let [resources (if (vector? resources) resources [resources])
        rt (get-in resources [0 :resourceType])
        rt-meta (or (cache/get-entity ctx (keyword rt)) {})
        table-name    (pg/table-name rt)
        values (->> resources
                    (mapv (fn [x]
                            {:id (or (:id x) (id-gen rt rt-meta))
                             :txid (or txid (hsql/raw "nextval('transaction_id_seq'::text)"))
                             :status (hsql/raw  "'updated'")
                             :resource (hsql/call :cast (to-json (dissoc x :id)) :jsonb)})))]
    (hsql/format
     {:with [[:upserted {:insert-into [(hsql/raw table-name) (hsql/raw "as t")]
                         :values values
                         :upsert {:on-conflict [:id]
                                  :do-update-set [:resource]
                                  :where (hsql/raw "t.resource <> EXCLUDED.resource")}
                         :returning [:*]}]]

      :select [:*] :from [:upserted]})))


(defn read-sql [rt id & [{params :params}]]
  ;; TODO: add check id
  (let [table-alias    [(hsql/raw (pg/table-name rt)) (hsql/raw "as t")]
        hx-table-alias [(hsql/raw (pg/hx-table-name rt)) (hsql/raw "as t")]

        {:keys [column-expr]} (permission-expr rt
                                               {:resource-column :t.resource
                                                :id-column :t.id}
                                               params)]
    (hsql/format
     {:select   [:*]
      :from     [[{:union [{:select resource-columns
                            :from   [table-alias]
                            :where  (rn [:and [:= :t.id id] column-expr])}
                           {:select resource-columns
                            :from   [hx-table-alias]
                            :where  (rn [:and [:= :t.id id] [:= :t.status "deleted"] column-expr])}]} :_]]
      :order-by [[:ts :desc] [:txid :desc]]
      :limit    1})))

(defn vread-sql [rt id vid & [_params]]
  (let [tbl-name    (pg/table-name rt)
        hx-tbl-name (pg/hx-table-name rt)]
    (hsql/format
     {:select [:*]
      :from [[{:union [{:select resource-columns  :from [(hsql/raw tbl-name)] :where [:= :id id]}
                       {:select resource-columns  :from [(hsql/raw hx-tbl-name)] :where [:= :id id]}]} :_]]
      :where [:= :txid vid]
      :order-by [:ts]
      :limit 1})))

(defn update-sql [txid {rt :resourceType id :id :as resource} & [{:keys [hx params]}]]
  (when-not rt (throw (Exception. "Resource type required.")))
  (assert id "Id required for update.")
  (let [table-alias     [(hsql/raw (pg/table-name rt)) (hsql/raw "as t")]
        resource-json   (-> resource clear-resource to-json)
        enable-history  (not (= "none" hx))

        {:keys [json-expr column-expr]} (permission-expr rt
                                                         {:resource-column :t.resource
                                                          :id-column       :t.id
                                                          :resource-json   resource-json
                                                          :resource-id     id}
                                                         params)]
    (hsql/format
     {:with (remove nil? [[:inserted {:insert-into [[table-alias   [:id :txid :status :resource]]
                                                    (merge {:select   [id
                                                                       (or txid (hsql/raw "nextval('transaction_id_seq'::text)"))
                                                                       "created"
                                                                       resource-json]}
                                                           ;; verify inserted data
                                                           (when json-expr
                                                             {:where json-expr}))]

                                      :upsert      {:on-conflict    [:id]
                                                    :do-update-set! {:txid     :EXCLUDED.txid
                                                                     :status   "updated"
                                                                     :ts       :current_timestamp
                                                                     :resource :EXCLUDED.resource}
                                                    :where          (rn [:and
                                                                         [:<> :t.resource :EXCLUDED.resource]
                                                                         ;; verify existing data
                                                                         (when column-expr
                                                                           column-expr)])}
                                      :returning   [:*]}]
                          (when enable-history
                            [:archived (archive-hsql rt id {:ensure-updated true})])])
      :select [:*]
      :from   [[{:union-all
                 [;; Insertion result
                  {:select resource-columns :from [:inserted] :limit 1}
                  ;; Check existing data
                  {:select [:id :txid :ts :resource_type ["unchanged" :status] :resource :cts]
                   :from   [table-alias]
                   :where (rn [:and
                               [:= :id id]
                               (when (and column-expr json-expr)
                                 [:and column-expr json-expr])])
                   :limit 1}]}
                :_]]
      :limit  2})))

(defn delete-sql [txid {rt :resourceType :as resource} & [{:keys [hx params]}]]
  (let [id              (:id resource)
        tbl-name        (pg/table-name rt)
        table-alias     [(hsql/raw (pg/table-name rt)) (hsql/raw "as t")]
        hx-tbl-name     (pg/hx-table-name rt)
        disable-history (= "none" hx)
        {:keys [column-expr]} (permission-expr rt {:resource-column :t.resource :id-column :t.id} params)

        enable-history  (not disable-history)

        q  {:with (remove nil?
                          [[:deleted {:delete-from table-alias
                                      :where       (rn [:and [:= :t.id id] column-expr])
                                      :returning   [:*]}]

                           (when enable-history
                             [:archived (archive-hsql (:resourceType resource) id {:ensure-deleted true})])

                           (when enable-history
                             [:archived-deleted {:insert-into [[(hsql/raw hx-tbl-name) [:id :txid :ts :status :resource]]
                                                               {:select [:id
                                                                         (or txid (hsql/raw "nextval('transaction_id_seq'::text)"))
                                                                         (hsql/raw "current_timestamp")
                                                                         (hsql/raw "'deleted'")
                                                                         :resource]
                                                                :from   [(hsql/raw tbl-name)]
                                                                :where  (rn [:and
                                                                             [:or
                                                                              [:in "created" (hsql/raw "(select status from deleted)")]
                                                                              [:in "updated" (hsql/raw "(select status from deleted)")]]
                                                                             [:= :id id]])}]
                                                 :upsert    {:on-conflict [:id,:txid]
                                                             :do-nothing  true}
                                                 :returning [:*]}])])
            :select [:*]
            :from (if enable-history
                    [:archived-deleted]
                    [:deleted])}]
    (hsql/format q)))


(defn to-query-string [m]
  (->> m
       (mapv (fn [[k v]]
               (str (name k) "=" v)))
       (str/join "&")))

(defn history-hack-values [sort-map]
  (let [sort-param (:_sort sort-map)]
    (cond
      (= sort-param "_lastUpdated") {:_sort "ts"}
      (= sort-param "-_lastUpdated") {:_sort "-ts"}
      :else sort-map)))

(defn parse-sort
  [q]
  (->> (-> q (select-keys [:_sort])
           (history-hack-values)
           (to-query-string))
       (proto.search.params/parse-sort :smth)
       (mapv (fn [{dir :dir param-name :name}] [(keyword param-name) dir]))))

(defn parse-history-params [query]
  (let [where (cond-> []
                (:_txid query)  (conj [:> :history.txid (:_txid query)])
                (:_since query) (conj [:> :history.ts (hsql/call :knife_date_bound (:_since query) "max")])
                (:_at query)    (conj [:>= :history.ts (hsql/call :knife_date_bound (:_at query) "min")]
                                      [:<= :history.ts (hsql/call :knife_date_bound (:_at query) "max")]))]

    (cond-> {}
      (not-empty where) (assoc :where  (into [:and] where))
      (:_count query)  (assoc :limit (:_count query))
      (and (:_sort query) (not= (:_sort query) "none")) (assoc :order-by (parse-sort query)))))


(defn history-where [query]
  (let [where (cond-> []
                (:_txid query)  (conj [:> :txid (:_txid query)])
                (:_since query) (conj [:> :ts (hsql/call :knife_date_bound (:_since query) "max")])
                (:_at query)    (conj [:>= :ts (hsql/call :knife_date_bound (:_at query) "min")]
                                      [:<= :ts (hsql/call :knife_date_bound (:_at query) "max")]))]
    (cond-> {}
      (not-empty where) (assoc :where  (into [:and] where)))))


(defn build-paging [query]
  (let [page  (or (get query :_page) 0)
        page  (if (> page 0) (dec page) page)
        limit (or (get query :_count) 100)]
    {:limit limit
     :offset (* page limit)}))


(defn history-sql [{rt :resourceType id :id :as params}]
  (let [tbl-name (pg/table-name rt)
        hx-tbl-name (pg/hx-table-name rt)
        params (clojure.core/update params :_count (fn [x] (or x 100)))
        hsq (merge
             {:with [[:history {:union [{:select resource-columns :from  [(hsql/raw tbl-name)]
                                         :where  [:= :id id]}
                                        {:select resource-columns :from  [(hsql/raw hx-tbl-name)]
                                         :where  [:= :id id]}]}]]
              :select   [:*]
              :from     [:history]
              :order-by [[:txid :desc]]}
             (parse-history-params params)
             (build-paging params))]
    hsq))



(defn history-type-sql [{rt :resourceType :as params}]
  (let [tbl-name (pg/table-name rt)
        hx-tbl-name (pg/hx-table-name rt)
        params (clojure.core/update params :_count (fn [x] (or x 100)))
        where (history-where params)
        hsq (merge
             {:with [[:history {:union-all [{:select resource-columns
                                             :from [[(merge {:select [:*] :from [(hsql/raw tbl-name)] :order-by [:txid]} where)
                                                     :_]]}
                                            {:select resource-columns
                                             :from [[(merge {:select [:*] :from [(hsql/raw hx-tbl-name)] :order-by [:txid]} where)
                                                     :_]]}]}]]
              :select   [:*]
              :from     [:history]
              :order-by [:txid]}
             (build-paging params))]
    hsq))

(defn history-all-sql [db ctx params]
  (let [resources (->> (:elements ctx)
                       (filter #(= :resource (:fhirbase.element/kind %)))
                       (map #(str/lower-case
                              (name (first (:fhirbase.element/path %))))))
        tables    (->> (hsql/format {:select [:table_name]
                                     :from [:information_schema.tables]
                                     :where [:like :table_name "%_history"]})
                       (db/query db)
                       (map :table_name)
                       (map #(second (re-find #"(\S+)_history" %))))
        intersection (clojure.set/intersection (set resources) (set tables))

        union (reduce (fn [acc tbl]
                        (into acc [{:select resource-columns
                                    :from   [(hsql/raw tbl)]}
                                   {:select resource-columns
                                    :from   [(hsql/raw (str tbl "_history"))]}]))
                      [] intersection)]
    (hsql/format
     (merge
      {:with [[:history {:union union}]]
       :select   [:*]
       :from     [:history]
       :order-by [[:txid :desc]]}
      (parse-history-params params)))))

(defn resource-types-sql []
  ["SELECT enum_range(NULL::resource_type)"])

;; db methods

(defn send-txid-metric
  [ctx value]
  (when (and value (some-> ctx :zen deref :box/metrics))
    (box.metrics.core/set-counter
     (:zen ctx) :last_transaction_id_seq (box.metrics.core/patch-labels (:zen ctx) {:box (get-in ctx [:config :db :database])})
     (if (string? value)
       (Integer/parseInt value)
       value))))

(defn next-txid [db]
  (db/query-first db "select nextval('transaction_id_seq'::text) as id"))

(defn latest-txid [db]
  (try
    (:last_value (first (db/jdbc-query db "SELECT last_value from transaction_id_seq")))
    (catch Exception _ -1)))

;; Backward compatibility. TODO: remove unneded aliases
(def refs-query               validation/refs-query)
(def check-resource-type-refs validation/check-resource-type-refs)
(def ref-to-key               validation/ref-to-key)
(def validate-refs            validation/validate-refs)
(def validate-uniqs           validation/validate-uniqs)
(def *validate                validation/validate*)
(def validate                 validation/oo-validate)

(defn create* [db ctx tx resource]
  (let [rt (:resourceType resource)
        rt-meta               (or (cache/get-entity ctx (keyword rt)) {})
        {resource :resource}  (proto.hooks/call-hooks [:resource/before-create :resource/before-save]
                                                      ctx {:hooks/result true
                                                           :action :create
                                                           :entity rt-meta
                                                           :resource resource
                                                           :tx tx})
        resource              (proto.preprocessors/pre-process ctx resource)
        sql                   (create-sql (:id tx) resource {:meta rt-meta
                                                             :params {:crud/params (get-in ctx [:request :crud/params])

                                                                      :box.rest.acl/filter
                                                                      (get-in ctx [:request :box.rest.acl/filter])}
                                                             :flags (:proto.operations/flags ctx)})
        [raw-result previous] (db/unlogged-query db sql)
        result                (pg/row-to-resource raw-result)
        previous              (pg/row-to-resource previous)
        tx-id                 (get-in result [:meta :versionId])]
    (when-not (= "unchanged" (:status raw-result))
      (send-txid-metric ctx tx-id))
    (when result
      (if (= "unchanged" (:status raw-result))
        (klog/log :resource/create-dup {:rid (:id result) :rtp (:resourceType result)})
        (if (= "updated" (:status raw-result))
          (do
            (klog/log :resource/update {:rid (:id result) :rtp (:resourceType result) :txid tx-id})
            (proto.hooks/notify-tx-hooks
             [:resource/after-update :resource/after-save :resource/after-change]
             ctx {:resource result
                  :previous previous
                  :tx {:id tx-id}
                  :entity rt-meta
                  :action :update}))
          (do
            (klog/log :resource/create {:rid (:id result) :rtp (:resourceType result) :txid tx-id})
            (proto.hooks/notify-tx-hooks
             [:resource/after-create :resource/after-save :resource/after-change]
             ctx {:resource result
                  :tx {:id tx-id}
                  :entity rt-meta
                  :action :create})))))
    result))

(defn create
  ([db ctx tx resource]
   (if-let [outcome (validate db ctx resource :create)]
     outcome
     (create* db ctx tx resource)))

  ;; FIXME: Remove uneccesary transaction initialization from CRUD
  ([db ctx resource]
   (if-let [outcome (validate db ctx resource :create)]
     outcome
     (create* db ctx {} resource))))

(defn create! [& args]
  (let [res (apply create args)]
    (if (= "OperationOutcome" (:resourceType res))
      (throw (Exception. (pr-str res)))
      res)))

(defn meta-resource? [ctx resource]
  (get (cache/get-entity ctx (keyword (:resourceType resource)))
       :isMeta))

(defn log-update [db ctx {:keys [unchanged entity result-status previous result tx-id]}]
  (cond unchanged
        (klog/log :resource/update-dup {:rid (:id result) :rtp (:resourceType result)})

        (= "created" result-status)
        (let [rt (:resourceType result)]
          (klog/log :resource/create {:rid (:id result) :rtp rt  :txid tx-id})
          (proto.hooks/notify-tx-hooks
           [:resource/after-create :resource/after-save :resource/after-change]
           ctx {:resource result
                :tx {:id tx-id}
                :entity entity
                :action :create}))

        :else
        (let [rt (:resourceType result)]
          (klog/log :resource/update {:rid (:id result) :rtp rt :txid tx-id})
          (proto.hooks/notify-tx-hooks
           [:resource/after-update :resource/after-save :resource/after-change]
           ctx {:resource result
                :previous previous
                :entity entity
                :tx {:id tx-id}
                :action :update}))))

(defn unchanged-meta-resource [ctx resource]
  (and (:id resource)
       (:resourceType resource)
       (meta-resource? ctx resource)
       (when-let [meta-res (cache/get-meta-resource ctx (keyword (:resourceType resource)) (keyword (:id resource)))]
         (when (= (dissoc meta-res :meta :id :resourceType)
                  (dissoc resource :meta :id :resourceType))
           (merge meta-res (select-keys resource [:id :resourceType]))))))


;; TODO: merge create and update
(defn update* [db ctx tx resource]
  (if-let [result (unchanged-meta-resource ctx resource)]
    (do (log-update db ctx {:unchanged true, :result result})
        result)  ;; TODO: if meta resource unchanged but also has version mismatch, unchanged will be returned, but should be mismatch
    (let [rt-meta              (or (cache/get-entity ctx (keyword (:resourceType resource))) {})
          {resource :resource} (proto.hooks/call-hooks [:resource/before-update :resource/before-save]
                                                       ctx {:resource resource
                                                            :entity   rt-meta
                                                            :tx       tx
                                                            :action   :update})
          resource             (proto.preprocessors/pre-process ctx resource)
          sql                  (update-sql (:id tx) resource
                                           {:hx     (:history rt-meta)
                                            :params {:crud/params (get-in ctx [:request :crud/params])

                                                     :box.rest.acl/filter
                                                     (get-in ctx [:request :box.rest.acl/filter])}})

          ;; TODO: denied when result is empty

          [raw-result raw-previous] (db/unlogged-query db sql)
          result                    (pg/row-to-resource raw-result)
          previous                  (pg/row-to-resource raw-previous)
          tx-id                     (get-in result [:meta :versionId])
          unchanged                 (= "unchanged" (:status raw-result))]
      (when-not (= "unchanged" (:status raw-result))
        (send-txid-metric ctx tx-id))
      (when result
        (log-update db ctx {:tx-id         tx-id
                            :unchanged     unchanged
                            :entity        rt-meta
                            :result-status (:status raw-result)
                            :result        result
                            :previous      previous}))
      (when (some? result)
        (with-meta result {:previous previous})))))

(defn update
  ([db ctx resource]
   (update db ctx {} resource))

  ([db ctx tx resource] ;; TODO: remove transaction logic from CRUD fns
   (if-let [outcome (trace/span "fhir-validate"
                      (trace/set-kind :internal)
                      (validate db ctx resource :update))]
     outcome
     (update* db ctx tx resource))))

(defn upsert* [db ctx tx resource]
  (->> resource
       (proto.preprocessors/pre-process ctx)
       (upsert-sql ctx (:id tx))
       (db/unlogged-query db)
       (mapv pg/row-to-resource)))

(defn upsert
  ([db ctx resource]
   (if-let [outcome (validate db ctx resource :create)]
     outcome
     (upsert db ctx {} resource)))

  ([db ctx tx resource]
   (if-let [outcome (validate db ctx resource :create)]
     outcome
     (upsert* db ctx tx resource))))

(defn upsert! [& args]
  (let [res (apply upsert args)]
    (if (= "OperationOutcome" (:resourceType res))
      (throw (Exception. (pr-str res)))
      res)))

(defn find [db _ctx {rt :resourceType id :id}]
  (->> {:select [:*]
        :from [(hsql/raw (pg/table-name rt))]
        :where [:= :id id]}
       (db/query-first db)
       pg/row-to-resource))

(defn read [db ctx {rt :resourceType id :id vid :versionId :as _params}]

  (let [params {:params {:crud/params (get-in ctx [:request :crud/params])

                         :box.rest.acl/filter
                         (get-in ctx [:request :box.rest.acl/filter])}}
        row (->> (if (nil? vid)
                   (read-sql rt id params)
                   (vread-sql rt id vid params))
                 (db/query-first db))]
    (cond
      (= "deleted" (:status row))
      (oo/operation-outcome (str "Resource " rt "/" id (when vid (str " (version " vid ")")) " not found") :deleted)

      (and vid row)
      (pg/row-to-resource row)

      (nil? row)
      (oo/operation-outcome (str "Resource " rt "/" id (when vid (str " (version " vid ")")) " not found") :not-found)

      :else
      (pg/row-to-resource row))))

(defn update! [& args]
  (let [res (apply update args)]
    (if (= "OperationOutcome" (:resourceType res))
      (throw (Exception. (pr-str res)))
      res)))

(defn patch
  ([db ctx resource]
   (patch db ctx {} resource))
  ([db ctx tx {_rt :resourceType _id :id :as resource}]
   (if-let [old (find db ctx resource)]
     (if (not (= old (merge old resource)))
       (update db ctx tx (merge old resource))
       old)
     (update db ctx tx resource))))

(defn patch! [& args]
  (let [res (apply patch args)]
    (if (= "OperationOutcome" (:resourceType res))
      (throw (Exception. (pr-str res)))
      res)))

(defn delete
  ([db ctx resource]
   (delete db ctx {} resource))
  ([db ctx tx resource]
   (let [rt-meta (or (cache/get-entity ctx (keyword (:resourceType resource))) {})
         {resource :resource} (proto.hooks/call-hooks [:resource/before-delete :resource/before-save]
                                                      ctx {:hooks/result true
                                                           :action :delete
                                                           :entity rt-meta
                                                           :resource resource
                                                           :tx tx})

         sql (delete-sql (:id tx)
                         resource
                         {:hx (:history rt-meta)
                          :params {:crud/params (get-in ctx [:request :crud/params])
                                   :box.rest.acl/filter
                                   (get-in ctx [:request :box.rest.acl/filter])}})
         result (first (db/unlogged-query db sql))
         result (and result (pg/row-to-resource result))
         tx-id (get-in result [:meta :versionId])]
     (send-txid-metric ctx tx-id)
     (when result
       (klog/log :resource/delete {:rid (:id result) :rtp (:resourceType result) :txid tx-id})
       (proto.hooks/notify-tx-hooks
        [:resource/after-delete :resource/after-change]
        ctx {:resource result
             :entity rt-meta
             :tx {:id tx-id}
             :action :delete}))
     result)))

(defn delete! [& args]
  (let [res (apply delete args)]
    (if (= "OperationOutcome" (:resourceType res))
      (throw (Exception. (pr-str res)))
      res)))


(defn parse-int [n fallback]
  (if (and (int? n) (pos? n))
    n
    (try (Integer/parseUnsignedInt n) (catch Exception _e fallback))))

(defn coerce-params [query]
  (-> query
      (clojure.core/update :_page parse-int 0)
      (clojure.core/update :_count parse-int 100)))

(defn add-full-url
  [ctx {{:keys [resourceType id]} :resource :as entry}]
  (let [settings (context/get-settings ctx)
        base-url (cond
                   (:smart/base-url ctx) (:smart/base-url ctx)
                   (:fhir/request? ctx) (str (settings.web/get-base-url settings) "/fhir")
                   :else (settings.web/get-base-url settings))]
    (assoc entry :fullUrl (str base-url \/ resourceType \/ id))))


(defn history [db ctx {:as query, res :resourceType id :id}]
  (let [coerced-query (coerce-params query)
        sql (history-sql coerced-query)
        formatted-sql (hsql/format sql)
        total (proto.search.utils/search-total db sql nil {:timeout (get-in ctx [:config :search :timeout])})
        qs (to-query-string (dissoc coerced-query :resourceType :id))
        links
        (proto.search.utils/build-links ctx (str res "/" id "/" "_history") qs total sql)
        bundle (->> (db/query db formatted-sql)
                    (pg/rows-to-bundle :history))]
    (cond-> bundle
      :always
      (clojure.core/update :entry (fn [es] (mapv (partial add-full-url ctx) es)))

      :always
      (assoc :total total :link links)

      (not (:fhir/request? ctx))
      (assoc :query-sql formatted-sql))))

(defn history-type [db ctx {:as query, res :resourceType}]
  (let [start-time   (System/currentTimeMillis)
        coerced-query (coerce-params query)
        sql (history-type-sql coerced-query)
        formatted-sql (hsql/format sql)
        total (proto.search.utils/search-total db sql nil {:timeout (get-in ctx [:config :search :timeout])})
        qs (to-query-string (dissoc coerced-query :resourceType))
        links
        (proto.search.utils/build-links ctx (str res "/" "_history") qs total sql)
        bundle (->>
                (db/query db formatted-sql)
                (pg/rows-to-bundle :history))]
    (cond-> bundle
      :always
      (clojure.core/update :entry (fn [es] (mapv (partial add-full-url ctx) es)))

      :always
      (clojure.core/update :id (fn [x] (or x (:_txid query))))

      :always
      (assoc :total total :link links)

      (not (:fhir/request? ctx))
      (assoc :query-sql sql
             :query-time (- (System/currentTimeMillis) start-time)))))

(defn history-all [db ctx query]
  (let [bundle (->>
                (history-all-sql db ctx query)
                (db/query db)
                (pg/rows-to-bundle :history))]
    (clojure.core/update bundle :entry (fn [es] (mapv (partial add-full-url ctx) es)))))

(defn truncate [db res-name]
  (db/execute db (str "DELETE FROM " (pg/table-name res-name) "; DELETE FROM " (pg/hx-table-name res-name))))

(defn prune [db ctx resource-type]
  (let [res-tbl (pg/table-name resource-type)
        res-hx-tbl (pg/hx-table-name resource-type)]
    (if (get (cache/get-entity ctx (keyword resource-type)) :isMeta)
      (db/execute db (str "DELETE FROM " res-tbl " WHERE resource->>'_source' <> 'code' or resource->>'_source' is null;"
                          "DELETE FROM " res-hx-tbl " WHERE resource->>'_source' <> 'code' or resource->>'_source' is null;"))
      (db/execute db (str "DELETE FROM " res-tbl ";"  "DELETE FROM " res-hx-tbl ";"))))

  (let [rt-meta (or (cache/get-entity ctx (keyword resource-type)) {})]
    (proto.hooks/notify-tx-hooks
     [:resource/after-prune]
     ctx {:resourceType resource-type :entity rt-meta})))

(defn select-all [db res-name]
  (->> (db/query db {:select [:*] :from [(hsql/raw (pg/table-name res-name))]})
       (pg/rows-to-bundle res-name)))

(defn query [db query-hql]
  (->> (db/query db query-hql)
       (mapv pg/row-to-resource)))

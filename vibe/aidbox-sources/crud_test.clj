(ns proto.crud-test
  (:require
   [clj-pg.honey :as db]
   [clojure.string :as str]
   [clojure.test :refer [deftest is testing]]
   [common.datetime.local-date :as date]
   [libsodium.core]
   [box.world :as world]
   [matcho.core :as matcho]
   [proto.core :as proto]
   [proto.crud :as subj]
   [proto.hooks]
   [test-db :as tdb]
   [unifn.core :as u]))


(defmethod proto.hooks/hook
  ::validate-user
  [_ ctx {resource :resource}]
  ;; (println "validate" resource)
  (when (= (:id resource) "special")
    {:errors [{:path [] :message "Special Validation"}]}))


;; Hack for testing :resource/after-update hook
(defonce hook-state (atom {}))
(defmethod proto.hooks/hook
  ::update-user
  [hook-key ctx {resource :resource prev :previous :as payload}]
  ;; Hack for testing :resource/after-update hook
  ;; in case of providing previous version of resource
  (if prev
    (reset! hook-state (select-keys payload [:resource :previous]))
    (reset! hook-state {})))

(def m
  (proto/define-manifest
         {:entities
          {:User
           {:attrs {:email {:type "email" :isUnique true :isRequired true}
                    :name {:type "string"}
                    :password {:type "password"}
                    :type {:type "Coding" :isUnique true}
                    :card {:type "secret"}
                    :groups {:isCollection true
                             :type "Reference"}
                    :bio {:type "Narrative"}}}

           :SecurityGroup {:attrs {:name {:type "string"}}}
           :TestResource {:isOpen true}

           :NoHistory {:history "none" :isOpen true}
           :TestSeqId  {:idGeneration "sequence"
                        :sequencePrefix "p-"
                        :isOpen true}
           :Narrative {:type "type"
                       :attrs {:div {:type "string"}
                               :status {:type "string"}}}}

          :resources {:AidboxSubscription {:user {:resources ["User"] :action ::on-user-changed}}}
          :config {:encrypt {:secret (libsodium.core/gen-key)}}
          :hooks {:resource/after-update {::update-user {:hooks/match {:resource {:resourceType "User"}}}}
                  :resource/after-change {::update-user {:hooks/match {:resource {:resourceType "User"}}}}
                  :resource/validate {::validate-user {:hooks/match {:resource {:resourceType "User"}}} }}}))

(deftest test-strip-empty
  (is (not (contains?
            (subj/strip-empty {:name nil :birthDate "123"})
            :name)))

  (matcho/match
   (subj/strip-empty {:name nil :birthDate "123"})
   {:birthDate "123"})

  (subj/strip-empty {:name [] :birthDate "123"})
  (is  (=
        (subj/strip-empty {:name [{:x [nil {:x nil}]}] :birthDate "123"})
        {:name [{:x [{}]}] :birthDate "123"})))

(deftest pure-test-crud
  (matcho/match
   (subj/refs-query
    [{:path [:groups 0], :value {:id "id-1" :resourceType "Group"}}
     {:path [:groups 1], :value {:id "id-2" :resourceType "Group"}}
     {:path [:organization 0], :value {:id "org-1" :resourceType "Organization"}}
     {:path [:organization 1], :value {:id "org-2" :resourceType "Organization"}}])
   {:union [{:select [:id :resource_type]
             :from [#sql/raw "\"group\""]
             :where [:and [:in :id #{"id-1" "id-2"}]]}
            {:select [:id :resource_type]
             :from [#sql/raw "\"organization\""]
             :where [:and [:in :id #{"org-1" "org-2"}]]}]}))

(defonce subscription-state (atom nil))

(defmethod u/*fn
  ::on-user-changed
  [{{res :resource action :action :as ev} :event}]
  (reset! subscription-state ev)
  {})

(defn not-nil? [x]
  (not (nil? x)))


(defmacro match-create [m res mtch]
  `(let [res# (subj/create (:db/connection ~m) ~m ~res)]
     (matcho/match res# ~mtch)
     res#))

(defmacro match-update [m res mtch]
  `(let [res# (subj/update (:db/connection ~m) ~m ~res)]
     (matcho/match res# ~mtch)
     res#))

(defmacro match-delete [m res mtch]
  `(let [res# (subj/delete (:db/connection ~m) ~m ~res)]
     (matcho/match res# ~mtch)
     res#))

(defmacro match-read [m res mtch]
  `(let [res# (subj/read (:db/connection ~m) ~m ~res)]
     (matcho/match res# ~mtch)
     res#))

(deftest test-crud
  (tdb/ensure-db :test_crud)
  (def d (tdb/get-db :test_crud))
  (def db-spec (tdb/get-db-spec :test_crud))
  (def m (assoc m :db/connection d))

  (tdb/migrate d db-spec m)

  (subj/truncate d :User)

  (subj/truncate d :SecurityGroup)
  (subj/truncate d :TestResource)

  (reset! subscription-state nil)


  (def tx (subj/next-txid d))

  (matcho/match
   (subj/create d m tx {:resourceType "User" :id "upser"})
   {:resourceType "OperationOutcome"
    :issue [{:severity "fatal", :code "invalid"
             :diagnostics "Property email is required"}]})


  (def c-upser (subj/create d m tx {:resourceType "User"
                                    :id "upser"
                                    :meta {:profile ["https://some/profile"],
                                           :tag [{:system "somesystem",:code "somecode"}]}
                                    :type {:system "sys" :code "code"}
                                    :name "ups"
                                    :email "ups@gmial.com"}))

  (matcho/match
   c-upser
   {:resourceType "User"
    :cts nil?
    :meta {:profile ["https://some/profile"],
           :versionId (str (:id tx))
           :lastUpdated not-nil?
           :createdAt not-nil?
           :tag [{:system "somesystem",:code "somecode"}]}
    :id "upser"
    :name "ups"
    :email "ups@gmial.com"})

  (testing "unique constraint on create"
    (match-create m
                  {:resourceType "User"
                   :id "upser-2"
                   :name "ups"
                   :email "ups@gmial.com"}
                  {:resourceType "OperationOutcome"
                   :issue [{:diagnostics #"is not unique"}]})

    (match-create m
                  {:resourceType "User"
                   :id "upser-2"
                   :name "ups"
                   :type {:system "sys" :code "code"}
                   :email "ups2@gmial.com"}
                  {:resourceType "OperationOutcome"
                   :issue [{:diagnostics #"is not unique"}]}))


  (matcho/match @subscription-state {:action :create :resource {:id "upser"}})



  (testing "unique constraint on update"
    (matcho/match
     (subj/create! d m tx {:resourceType "User"
                           :password "foobar"
                           :id "upser_pass"
                           :type {:system "sys" :code "code2"}
                           :name "foobar"
                           :email "upser_pass@gmial.com"})
     {:resourceType "User"
      :id "upser_pass"
      :password #(str/starts-with? % "$")
      :meta {:versionId (str (:id tx))}
      :name "foobar"
      :email "upser_pass@gmial.com"})

    (match-update m
                  {:resourceType "User"
                   :password "foobar"
                   :id "upser_pass"
                   :name "foobar"
                   :email "ups@gmial.com"}
                  {:resourceType "OperationOutcome"
                   :issue [{:diagnostics #"is not unique"}]}))

  (def tx2 (subj/next-txid d))

  (matcho/match
   (subj/create d m tx2 {:resourceType "User" :id "upser2"
                         :meta {:profile ["https://some/profile"],
                                :security [{:system "http://terminology.hl7.org/CodeSystem/v3-ActCode" :code "DELAU"}]}
                         :name "ups2"
                         :email "ups2@gmial.com"})
   {:resourceType "User"
    :meta {:profile ["https://some/profile"],
           :versionId (str (:id tx2))
           :security [{:system "http://terminology.hl7.org/CodeSystem/v3-ActCode" :code "DELAU"}]}
    :id "upser2"
    :name "ups2"
    :email "ups2@gmial.com"})

  (subj/delete d m {:resourceType "User" :id "upser_pass"})

  (matcho/match
   (subj/read d m {:resourceType "User" :id "upser"})
   {:resourceType "User"
    :id "upser"
    :name "ups"
    :meta {:versionId (str (:id tx))
           :createdAt not-nil?}
    :email "ups@gmial.com"})

  (matcho/match (subj/select-all d :User) #(= 2 (count (:entry %))))

  (matcho/match (subj/history d m {:resourceType "User" :id "upser"})
                #(= 1 (count (:entry %))))

  (def tx2 (subj/next-txid d))

  (is (get-in c-upser [:meta :createdAt]))

  (matcho/match
   (subj/update d m tx2 {:resourceType "User" :id "upser"
                         :meta {:profile ["https://some/profile"],
                                :tag [{:system "somesystem",:code "somecode"}]}
                         :email "changed@gmial.com" :name "changed"})
   {:resourceType "User"
    :id "upser"
    :name "changed"
    :meta {:versionId (str (:id tx2))
           :profile ["https://some/profile"],
           :tag [{:system "somesystem",:code "somecode"}]
           :createdAt (get-in c-upser [:meta :createdAt])
           :lastUpdated not-nil?}
    :email "changed@gmial.com"})

  (matcho/match @subscription-state
                {:action :update
                 :resource {:id "upser"}})

  (matcho/match
   (subj/read d m {:resourceType "User" :id "upser"})
   {:resourceType "User"
    :id "upser"
    :meta {:versionId (str (:id tx2))
           :lastUpdated some?}
    :email "changed@gmial.com"})

  (matcho/match (subj/select-all d :User) #(= 2 (count (:entry %))))

  (matcho/match (subj/history d m {:resourceType "User" :id "upser"})
                #(= 2 (count (:entry %))))

  (matcho/match (subj/history-type d m {:resourceType "User"})
                #(= 5 (count (:entry %))))

  (def tx2-1 (subj/next-txid d))

  (matcho/match
   (subj/patch d m tx2-1 {:resourceType "User" :id "upser" :email "changed@gmial.com"})
   {:resourceType "User"
    :id "upser"
    :meta {:versionId (str (:id tx2))
           :lastUpdated some?}
    :name "changed"
    :email "changed@gmial.com"})

  (def tx3 (subj/next-txid d))

  (matcho/match (subj/delete d m tx3 {:resourceType "User" :id "upser"})
                {:email "changed@gmial.com"
                 :id "upser"
                 :resourceType "User"
                 :meta {:lastUpdated some?
                        :versionId (str (:id tx3))}})

  (matcho/match @subscription-state
                {:action :delete
                 :resource {:id "upser"}})

  (matcho/match (subj/history-type d m {:resourceType "User"})
                #(= 6 (count (:entry %))))

  (match-read m
              {:resourceType "User" :id "unexisting"}
              {:resourceType "OperationOutcome"})

  (match-read m
              {:resourceType "User" :id "upser" :versionId (str (:id tx))}
              {:id "upser"})

  (match-read m
              {:resourceType "User" :id "upser" :versionId (str (:id tx2))}
              {:id "upser"})

  (match-read m
              {:resourceType "User" :id "upser" :versionId (str (:id tx3))}
              {:id "deleted"
               :resourceType "OperationOutcome"})

  (match-read m
              {:resourceType "User" :id "upser"}
              {:resourceType "OperationOutcome"})

  (match-create m
                {:resourceType "User"
                 :id "admin"
                 :email "admin@email.com"
                 :groups [{:id "admin" :resourceType "SecurityGroup"}]}
                {:resourceType "OperationOutcome"})

  (match-create m
                {:resourceType "User"
                 :id "admin"
                 :email "admin@email.com"
                 :groups [{:id "admin" :resourceType "Unexisting"}]}
                {:resourceType "OperationOutcome"})

  (match-create m
                {:resourceType "User"
                 :id "admin"
                 :email "admin@email.com"
                 :groups [{:id "admin" :resourceType "Unexisting"}
                          {:id "admin" :resourceType "SecurityGroup"}]}
                {:resourceType "OperationOutcome"})

  (match-create m
                {:resourceType "SecurityGroup" :id "admin" :name "admin"}
                {:resourceType "SecurityGroup" :id "admin"})

  (match-create m
                {:resourceType "User" :id "upser_with_meta"
                 :meta {:versionId "123"}
                 :email "foo@mail.ru"}
                {:resourceType "User"})

  (match-create m
                {:resourceType "User" :id "unusual_email"
                 :email "liya@beda.software"}
                {:resourceType "User"})

  ;; Recreate user
  (match-create m
                {:resourceType "User"
                 :id "admin"
                 :email "admin@email.com"
                 :groups [{:id "admin" :resourceType "SecurityGroup"}]}
                {:resourceType "User"
                 :groups [{:id "admin" :resourceType "SecurityGroup"}]})

  (match-delete m
                {:resourceType "User" :id "admin"}
                {:id "admin"})

  (match-create m
                {:resourceType "User" :id "admin" :email "admin@email.com"
                 :groups [{:id "admin" :resourceType "SecurityGroup"}]}
                {:resourceType "User"
                 :groups [{:id "admin" :resourceType "SecurityGroup"}]})

  (match-read m
              {:resourceType "User" :id "admin"}
              {:resourceType "User" :id "admin" })

  (testing "History params"
    (def today-date-str (date/to-iso (date/now-utc)))
    (def yesterday-date-str (date/to-iso (date/-days (date/now-utc) 1)))

    (subj/truncate d :TestResource)

    (match-create m
                  {:resourceType "TestResource" :id "tr1" :a 1}
                  {:resourceType "TestResource"})

    (match-update m
                  {:resourceType "TestResource" :id "tr1" :a 2}
                  {:resourceType "TestResource"})

    (def latest-res (subj/create d m {:resourceType "TestResource" :id "tr2" :a 3}))
    (def latest-date-str (get-in latest-res [:meta :lastUpdated]))


    (matcho/match
     latest-res
     {:resourceType "TestResource"})

    (testing "paging on resource"
      (matcho/match (subj/history d m {:resourceType "TestResource" :id "tr1" :_count 1 :_page 1})
                    #(= 1 (count (:entry %))))

      (matcho/match (subj/history d m {:resourceType "TestResource" :id "tr1" :_count 1 :_page 1})
                    {:link [{:relation "first" :url "/TestResource/tr1/_history?_count=1&_page=1"}
                            {:relation "self" :url "/TestResource/tr1/_history?_count=1&_page=1"}
                            {:relation "next" :url "/TestResource/tr1/_history?_count=1&_page=2"}
                            {:relation "last" :url "/TestResource/tr1/_history?_count=1&_page=2"}]})

      (matcho/match (subj/history d m {:resourceType "TestResource" :id "tr1" :_count 2 :_page 1})
                    #(= 2 (count (:entry %))))

      (matcho/match (subj/history d m {:resourceType "TestResource" :id "tr1" :_count 2 :_page 2})
                    #(= 0 (count (:entry %))))

      (matcho/match (subj/history d m {:resourceType "TestResource" :id "tr1" :_count 2})
                    #(= 2 (count (:entry %))))

      (matcho/match (subj/history d m {:resourceType "TestResource" :id "tr1" :_count 1})
                    #(= 1 (count (:entry %))))

      (matcho/match (subj/history d m {:resourceType "TestResource" :id "tr1" :_page 1})
                    #(= 2 (count (:entry %))))

      (matcho/match (subj/history d m {:resourceType "TestResource" :id "tr1" :_page 2})
                    #(= 0 (count (:entry %))))

      (matcho/match (subj/history d m {:resourceType "TestResource" :id "tr1" :_page -500})
                    #(= 2 (count (:entry %))))

      (matcho/match (subj/history d m {:resourceType "TestResource" :id "tr1" :_page "A???"})
                    #(= 2 (count (:entry %))))

      (matcho/match (subj/history d m {:resourceType "TestResource" :id "tr1" :_count -500 :_page 1})
                    #(= 2 (count (:entry %)))))

    (testing "paging on type"
      (matcho/match (subj/history-type d m {:resourceType "TestResource" :_count 1 :_page 1})
                    #(= 1 (count (:entry %))))

      (matcho/match (subj/history-type d m {:resourceType "TestResource" :_count 1 :_page 1})
                    {:link [{:relation "first" :url "/TestResource/_history?_count=1&_page=1"}
                            {:relation "self" :url "/TestResource/_history?_count=1&_page=1"}
                            {:relation "next" :url "/TestResource/_history?_count=1&_page=2"}
                            {:relation "last" :url "/TestResource/_history?_count=1&_page=3"}]})

      (matcho/match (subj/history-type d m {:resourceType "TestResource" :_count 2 :_page 1})
                    #(= 2 (count (:entry %))))

      (matcho/match (subj/history-type d m {:resourceType "TestResource" :_count 2 :_page 2})
                    #(= 1 (count (:entry %))))

      (matcho/match (subj/history-type d m {:resourceType "TestResource" :_count 2})
                    #(= 2 (count (:entry %))))

      (matcho/match (subj/history-type d m {:resourceType "TestResource" :_count 1})
                    #(= 1 (count (:entry %))))

      (matcho/match (subj/history-type d m {:resourceType "TestResource" :_page 1})
                    #(= 3 (count (:entry %))))

      (matcho/match (subj/history-type d m {:resourceType "TestResource" :_page 2})
                    #(= 0 (count (:entry %))))

      (matcho/match (subj/history-type d m {:resourceType "TestResource" :_page -500})
                    #(= 3 (count (:entry %))))

      (matcho/match (subj/history-type d m {:resourceType "TestResource" :_page "A???"})
                    #(= 3 (count (:entry %))))

      (matcho/match (subj/history-type d m {:resourceType "TestResource" :_count -500 :_page 1})
                    #(= 3 (count (:entry %)))))

    (testing "_sort on resource"
      (matcho/match
       (subj/history d m {:resourceType "TestResource" :id "tr1" :_sort "txid"})
       {:resourceType "Bundle",
        :entry
        [{:resource
          {:a 1,
           :id "tr1",
           :resourceType "TestResource"}}
         {:resource
          {:a 2,
           :id "tr1",
           :resourceType "TestResource"}}
         nil]})

      (matcho/match
       (subj/history d m {:resourceType "TestResource" :id "tr1" :_sort "-txid"})
       {:resourceType "Bundle",
        :entry
        [{:resource
          {:a 2,
           :id "tr1",
           :resourceType "TestResource"}}
         {:resource
          {:a 1,
           :id "tr1",
           :resourceType "TestResource"}}
         nil]}))

    (testing "keep cts"
      (is
       (-> (subj/history d m {:resourceType "TestResource" :id "tr1"})
           (get-in [:entry])
           (->> (mapv #(get-in % [:resource :meta :createdAt]))
                (into #{})
                (count)
                (= 1)))))

    (matcho/match (subj/history d m {:resourceType "TestResource" :id "tr1"})
                  #(= 2 (count (:entry %))))

    (matcho/match (subj/history d m {:resourceType "TestResource" :id "tr1" :_count 1})
                  #(= 1 (count (:entry %))))

    (matcho/match (subj/history d m {:resourceType "TestResource" :id "tr1" :_at yesterday-date-str})
                  #(= 0 (count (:entry %))))

    (matcho/match (subj/history d m {:resourceType "TestResource" :id "tr1" :_at today-date-str})
                  #(= 2 (count (:entry %))))

    (matcho/match (subj/history d m {:resourceType "TestResource" :id "tr1" :_since today-date-str})
                  #(= 0 (count (:entry %))))

    (matcho/match (subj/history d m {:resourceType "TestResource" :id "tr1" :_since yesterday-date-str})
                  #(= 2 (count (:entry %))))

    (matcho/match (subj/history-type d m {:resourceType "TestResource"})
                  #(= 3 (count (:entry %))))

    (matcho/match (subj/history-type d m {:resourceType "TestResource" :_count 1})
                  #(= 1 (count (:entry %))))

    (matcho/match (subj/history-type d m {:resourceType "TestResource" :_at yesterday-date-str})
                  #(= 0 (count (:entry %))))

    (matcho/match (subj/history-type d m {:resourceType "TestResource" :_at today-date-str})
                  #(= 3 (count (:entry %))))

    (matcho/match (subj/history-type d m {:resourceType "TestResource" :_since today-date-str})
                  #(= 0 (count (:entry %))))

    (matcho/match (subj/history-type d m {:resourceType "TestResource" :_since yesterday-date-str})
                  #(= 3 (count (:entry %))))

    (matcho/match (subj/history-type d m {:resourceType "TestResource" :_since latest-date-str})
                  #(= 0 (count (:entry %))))

    (matcho/match (subj/history-type d m {:resourceType "TestResource" :_at latest-date-str})
                  #(= 1 (count (:entry %)))))


  (testing "two updates with same tx"

    (def tx-dup (subj/next-txid d))

    (subj/update d m tx-dup {:resourceType "User" :id "upser" :email "changed@gmial.com" :name "changed-1"})
    (subj/update d m tx-dup {:resourceType "User" :id "upser" :email "changed@gmial.com" :name "changed-2"})
    ;; this update were failing with txid dup
    (subj/update d m tx-dup {:resourceType "User" :id "upser" :email "changed@gmial.com" :name "changed-3"})
    (subj/delete d m tx-dup {:resourceType "User" :id "upser"})
    )

  (testing "get resource when ts in table and history are the same"
    (world/ensure-box)
    (world/truncate :Patient)

    (matcho/match (world/dispatch {:uri "/"
                                   :request-method :post
                                   :resource {:resourceType "Bundle"
                                              :type "transaction"
                                              :entry [{:request {:method "put" :url "/Patient/test"}}
                                                      {:request {:method "delete" :url "/Patient/test"}}
                                                      {:request {:method "put" :url "/Patient/test"}}
                                                      {:request {:method "get" :url "/Patient/test"}}]}})
                  {:status 200}))

  (testing "Meta links"
    (subj/prune d m :Attribute)
    (match-create m
                  {:resourceType "Attribute" :id "TestResource.dynattr"
                   :type {:resourceType "Entity" :id "string"}
                   :path ["dynattr"]
                   :resource {:resourceType "Entity" :id "TestResource"}}
                  {:resourceType "Attribute"}))

  (testing "No history"

    (subj/truncate d :NoHistory)

    (is (empty? (db/query d "select * from nohistory")))

    (subj/create! d m {:resourceType "NoHistory" :id "n1"})
    (subj/update! d m {:resourceType "NoHistory" :id "n1" :change 1})
    (subj/upsert! d m {:resourceType "NoHistory" :id "n1" :change 1})
    (subj/delete d m {:resourceType "NoHistory" :id "n1" :change 1})

    (is (empty? (db/query d "select * from nohistory_history")))
    )

  (testing "Seq resource"

    (subj/truncate d :TestSeqId)

    (matcho/match (subj/create! d m {:resourceType "TestSeqId"})
                  {:id #"p-\d"})

    (matcho/match (subj/create! d m {:resourceType "TestSeqId" :id "p-10"})
                  {:id #"p-10"})

    (matcho/match (subj/create! d m {:resourceType "TestSeqId"})
                  {:id #"p-\d"})

    (matcho/match
     (subj/upsert! d m [{:resourceType "TestSeqId"} {:resourceType "TestSeqId"}])
     [{:id #"p-\d"} {:id #"p-\d"}]))

  (testing "Read resource with different resource and history tables structure"

    (subj/read d m {:resourceType "User" :id "upser"})
    (db/exec! d "ALTER TABLE user_history DROP COLUMN IF EXISTS custom_attr; ")
    (db/exec! d "ALTER TABLE user_history ADD COLUMN IF NOT EXISTS custom_attr varchar(30); ")

    (subj/read d m {:resourceType "User" :id "upser"})
    (subj/read d m {:resourceType "User" :id "upser" :versionId 12})
    (subj/history d m {:resourceType "User" :id "upser" :versionId "ups"})

    (db/exec! d "ALTER TABLE user_history DROP COLUMN IF EXISTS custom_attr; ")
    )

  (testing "encrpytion"
    (match-create m
                  {:resourceType "User"
                   :id "sec"
                   :name "ups"
                   :card "777888"
                   :email "sec@gmial.com"}
                  {:card #"^\$.*"}))


  (testing "dup creation"

    (subj/delete d m {:resourceType "User" :id "dup"})

    (def first-create
      (match-create m
                    {:resourceType "User"
                     :id "dup"
                     :name "ups"
                     :email "dup@ru.x"}
                    {:id "dup"}))

    (match-create m
                  {:resourceType "User"
                   :id "dup"
                   :name "ups"
                   :email "dup@ru.x"}
                  first-create)

    (def changed-res
      (match-create m
                    {:resourceType "User"
                     :id "dup"
                     :name "change"
                     :email "dup@ru.x"}
                    {:name "change"}))

    (match-create m
                  {:resourceType "User"
                   :id "dup"
                   :name "change"
                   :email "dup@ru.x"}
                  changed-res)

    (match-create m
                  {:resourceType "User"
                   :name "some"
                   :email "some@ru.x"}
                  {:id string?
                   :meta {:versionId string?}})

    (def first-updated
      (match-create m
                    {:resourceType "User"
                     :id "dup"
                     :name "change2"
                     :email "dup@ru.x"}
                    {:name "change2"}))

    (match-create m
                  {:resourceType "User"
                   :id "dup"
                   :name "change2"
                   :email "dup@ru.x"}
                  first-updated)

    (match-create m
                  {:resourceType "User"
                   :id "dup"
                   :name "change2"
                   :email "dup2@ru.x"}
                  {:email "dup2@ru.x"})

    ;; Test hook
    (matcho/match
     @hook-state
     {:previous {:email "dup@ru.x"}
      :resource {:email "dup2@ru.x"}})

    (subj/upsert d m
                 [{:resourceType "User"
                   :id "dup"
                   :name "change2"
                   :email "dup@ru.x"}
                  {:resourceType "User"
                   :name "some2"
                   :email "some@ru.x"}])

    )

  (testing "validation hook"
    (match-create m
                  {:resourceType "User"
                   :id "special"
                   :name "somename"
                   :email "someuser@email.com"}
                  {:resourceType "OperationOutcome"
                   :issue [{:diagnostics "Special Validation"}]})

    (match-create m
                  {:resourceType "TestResource"
                   :present [nil {:a 1}]
                   :empty [{:ups [nil {:a nil}]}]}
                  {:empty [{:ups [{}]}]
                   :present [{:a 1}]})

    )


  (testing "Ensure dedublicate write in to history table"
    (subj/truncate d :User)
    (match-create m
                  {:resourceType "User"
                   :id "test-user"
                   :email "test@meta.com"}
                  {:id "test-user"})

    ;; Do update with the same resource
    (match-update m
                  {:resourceType "User" :id "test-user" :email "test@meta.com"}
                  {:id "test-user"})

    ;; Ensure that history table is empty
    (is (= 0 (db/query-value (:db/connection m) "select count(*) from user_history")))

    ;; Do update with new data
    (match-update m
                  {:resourceType "User" :id "test-user" :email "test-1@meta.com"}
                  {:id "test-user"})

    ;; Ensure that history table has one record
    (is (= 1 (db/query-value (:db/connection m) "select count(*) from user_history")))

    ;; Do second update with new data
    (match-update m
                  {:resourceType "User" :id "test-user" :email "test-1@meta.com"}
                  {:id "test-user"})

    ;; Ensure that history table has only one record
    (is (= 1 (db/query-value (:db/connection m) "select count(*) from user_history")))

    )


  (testing "meta"
    (subj/truncate d  :User)
    (match-create m
                  {:resourceType "User"
                   :id "test-meta"
                   :meta {:createdAt "2019-11-20T01:01:01.199315Z"
                          :tag [{:system "urn:sys" :code "x"}]
                          :lastUpdated "2019-11-20T01:01:01.199315Z"
                          :versionId "123"}
                   :email "test@meta.com"}
                  {:resourceType "User"
                   :meta {:createdAt #(not (= % "2019-11-20T01:01:01.199315Z"))
                          :tag [{:system "urn:sys" :code "x"}]
                          :lastUpdated #(not (= % "2019-11-20T01:01:01.199315Z"))
                          :versionId #(not (= % "123"))}
                   :id "test-meta"})

    (matcho/match
     (db/query-first d "select id,resource from \"user\"")
     {:id "test-meta"
      :resource {:meta {:createdAt nil?
                        :tag [{:system "urn:sys" :code "x"}]
                        :lastUpdated nil?
                        :versionId nil?}}})

    (match-update m
                  {:resourceType "User"
                   :id "test-meta"
                   :meta {:createdAt "2019-11-20T01:01:01.199315Z"
                          :tag [{:system "urn:sys" :code "x"}]
                          :lastUpdated "2019-11-20T01:01:01.199315Z"
                          :versionId "123"}
                   :email "test@meta2.com"}
                  {:resourceType "User"
                   :meta {:createdAt #(not (= % "2019-11-20T01:01:01.199315Z"))
                          :tag [{:system "urn:sys" :code "x"}]
                          :lastUpdated #(not (= % "2019-11-20T01:01:01.199315Z"))
                          :versionId #(not (= % "123"))}
                   :email "test@meta2.com"
                   :id "test-meta"})

    ;; Test hook
    (matcho/match
     @hook-state
     {:previous {:email "test@meta.com"}
      :resource {:email "test@meta2.com"}})


    (matcho/match
     (db/query-first d "select id,resource from \"user\"")
     {:id "test-meta"
      :resource {:meta {:createdAt nil?
                        :tag [{:system "urn:sys" :code "x"}]
                        :lastUpdated nil?
                        :versionId nil?}
                 :email "test@meta2.com"}})

    (match-update m
                  {:resourceType "User"
                   :id "test-meta"
                   :meta {:createdAt "2019-11-20T01:01:01.199315Z"
                          :lastUpdated "2019-11-20T01:01:01.199315Z"
                          :versionId "123"}
                   :email "test@meta3.com"}
                  {:resourceType "User"
                   :email "test@meta3.com"
                   :id "test-meta"})

    ;; Test hook
    (matcho/match
     @hook-state
     {:previous {:email "test@meta2.com"}
      :resource {:email "test@meta3.com"}})

    (matcho/match
     (db/query-first d "select id,resource from \"user\"")
     {:id "test-meta"
      :resource {:meta nil?
                 :email "test@meta3.com"}})))

(deftest crud-with-params

  (tdb/ensure-db :test_crud)
  (def d (tdb/get-db :test_crud))
  (def db-spec (tdb/get-db :test_crud))
  (def m (assoc m
                :db/connection d
                :settings (box.world/initialize-settings {})))
  (tdb/migrate d db-spec m)

  (testing "Update-sql with params"

    (def m*
      (->> {:email {:expression  [["email"]]
                    :engine 'aidbox.rest.v1/param
                    :name "subject"
                    :type "string"
                    :search-by 'aidbox.rest.v1/search-by-knife
                    :value ["user@mail.ru"]}}
           (assoc-in m [:request :crud/params])))

    (match-create m
     {:resourceType "User" :id "marat"
      :email "marat@yandex.ru"}
     {:id "marat"})

    (match-create m
     {:resourceType "User" :id "niquola"
      :email "niquola@yandex.ru"}
     {:id "niquola"})

    (testing "Read"
      (def m-niquola
        (->> {:email {:expression  [["email"]]
                      :engine 'aidbox.rest.v1/param
                      :value ["niquola@yandex.ru"]}}
             (assoc-in m [:request :crud/params])))

      (matcho/match
       (->> {:resourceType "User" :id "marat"}
            (subj/read d m-niquola))
       {:resourceType "OperationOutcome"
        :id "not-found"})

      (matcho/match
       (->> {:resourceType "User" :id "niquola"}
            (subj/read d m-niquola))
       {:id "niquola"}))

    (testing "Create"
      (subj/truncate d :User)
      (def m-niquola
        (->> {:email {:expression  [["email"]]
                      :engine 'aidbox.rest.v1/param
                      :value ["niquola@yandex.ru"]}}
             (assoc-in m [:request :crud/params])))

      (matcho/match
       (->> {:resourceType "User" :id "niquola"
             :email "ups@yandex.ru"}
            (subj/create d m-niquola))
       nil?)

      (matcho/match
       (->> {:resourceType "User" :id "niquola"
             :email "niquola@yandex.ru"}
            (subj/create d m-niquola))
       {:id "niquola"}))

    (testing "Delete"
      (subj/truncate d :User)
      (def m-niquola
        (->> {:email {:expression  [["email"]]
                      :engine 'aidbox.rest.v1/param
                      :value ["niquola@yandex.ru"]}}
             (assoc-in m [:request :crud/params])))

      (match-create m
                    {:resourceType "User" :id "marat"
                     :email "marat@yandex.ru"}
                    {:id "marat"})

      (match-create m
                    {:resourceType "User" :id "niquola"
                     :email "niquola@yandex.ru"}
                    {:id "niquola"})

      (matcho/match
       (->> {:resourceType "User" :id "marat"}
            (subj/delete d m-niquola))
       nil?)

      (matcho/match
       (->> {:resourceType "User" :id "niquola"}
            (subj/delete d m-niquola))
       {:id "niquola"}))


    (testing "Verify body by crud/params"
      (subj/truncate d :User)

      ;; Vrong email
      (matcho/match
       (->> {:resourceType "User" :id "12"
             :email "_ups_@mail.ru"}
            (subj/update d m*))
       nil?)

      ;; Email matched
      (matcho/match
       (->> {:resourceType "User" :id "12"
             :email "user@mail.ru"}
            (subj/update d m*))
       {:id "12"})

      ;; Email matched, update User.name
      (matcho/match
       (->> {:resourceType "User" :id "12"
             :email "user@mail.ru"
             :name "marat"}
            (subj/update d m*))
       {:id "12"
        :name "marat"}))

    (testing "Verify existing data"
      (subj/truncate d :User)
      ;; Prepare User with email marat@mail.ru
      (match-update
       m
       {:resourceType "User" :id "5"
        :name "marat"
        :email "marat@mail.ru"}
       {:id string?
        :name "marat"})

      ;; try change email from marat@mail.ru to user@mail.ru
      (matcho/match
       (->> {:resourceType "User" :id "5"
             :email "user@mail.ru"}
            (subj/update d m*))
       nil?)

      )



    )
  )

(deftest touchstone-fullurl-history-bundle
  (world/ensure-box)
  (world/truncate :Patient)

  (world/dispatch {:uri "/Patient/pt1"
                   :request-method :put
                   :resource {:resourceType "Patient"
                              :gender "male"}})

  (world/dispatch {:uri "/Patient/pt2"
                   :request-method :put
                   :resource {:resourceType "Patient"
                              :gender "female"}})

  (world/match {:uri "/Patient/_history"
                :request-method :get}
               {:body
                {:resourceType "Bundle",
                 :type "history",
                 :entry [{:fullUrl #"http.*/Patient/pt1"}
                         {:fullUrl #"http.*/Patient/pt2"}
                         nil]}})

  (world/match {:uri "/fhir/Patient/_history"
                :request-method :get}
               {:body
                {:resourceType "Bundle",
                 :type "history",
                 :entry [{:fullUrl #"http.*/fhir/Patient/pt1"}
                         {:fullUrl #"http.*/fhir/Patient/pt2"}
                         nil]}}))

(deftest touchstone-history-request-and-response-bundle
  (world/ensure-box)
  (world/truncate :Patient)

  (world/dispatch {:uri "/Patient/"
                   :request-method :post
                   :resource {:resourceType "Patient"
                              :id "pt1"
                              :gender "male"}})

  (world/dispatch {:uri "/Patient/"
                   :request-method :post
                   :resource {:resourceType "Patient"
                              :id "pt2"
                              :gender "female"}})

  (world/dispatch {:uri "/Patient/pt2"
                   :request-method :put
                   :resource {:resourceType "Patient"
                              :gender "male"}})

  (world/match {:uri "/Patient/_history"
                :request-method :get
                :params {:_sort "id"}}
               {:body
                {:resourceType "Bundle",
                 :type "history",
                 :entry [{:request {:method "POST", :url "Patient"}
                          :response {:status "201"}
                          :resource {:id "pt1"}}
                         {:request {:method "POST", :url "Patient"}
                          :response {:status "201"}
                          :resource {:id "pt2"}}
                         {:request {:method "PUT", :url "Patient"}
                          :response {:status "200"}
                          :resource {:id "pt2"}}
                         nil]}}))

(deftest touchstone-history-query-sql-field-bundle
  (world/ensure-box)
  (world/truncate :Patient)

  (world/dispatch {:uri "/Patient/"
                   :request-method :post
                   :resource {:resourceType "Patient"
                              :id "pt1"
                              :gender "male"}})

  (world/dispatch {:uri "/Patient/"
                   :request-method :post
                   :resource {:resourceType "Patient"
                              :id "pt2"
                              :gender "female"}})

  (world/dispatch {:uri "/Patient/pt2"
                   :request-method :put
                   :resource {:resourceType "Patient"
                              :gender "male"}})

  (world/match {:uri "/Patient/_history"
                :request-method :get
                :params {:_sort "id"}}
               {:body
                {:resourceType "Bundle",
                 :type "history",
                 :query-sql some?
                 :query-time some?}})

  (world/match {:uri "/fhir/Patient/_history"
                :request-method :get
                :params {:_sort "id"}}
               {:body
                {:resourceType "Bundle",
                 :type "history",
                 :query-sql nil
                 :query-time nil}}))

(deftest do-not-strip-null-in-primitive-extension

  (testing "non-primitive"
    (is (= {}
           (subj/strip-empty {:hello nil})))

    (is (= {:hello [{:world 1}]}
           (subj/strip-empty {:hello [{:world 1 :a nil}]})))
    (is (= {:hello {:world 1}}
           (subj/strip-empty  {:hello {:world 1 :a nil}}))))


  (testing "primitive"
    (is (= {} (subj/strip-empty {:_hello nil})))

    (is (= {:_hello [{:world 1} nil nil]}
            (subj/strip-empty {:_hello [{:world 1}
                                        nil
                                        {:a nil}]})))

    (is (= {:_hello [{:world 1} nil {:b 1}]}
            (subj/strip-empty {:_hello [{:world 1} nil {:a nil
                                                        :b 1}]}))))
  (world/ensure-box)
  (world/truncate :Location)

  (world/create
    {:resourceType "Location"
     :alias ["a" "b" "c"],
     :_alias
     [{:extension
       [{:url "http://hl7.org/fhir/StructureDefinition/translation",
         :extension
         [{:url "lang", :valueCode "fr-CA"}
          {:url "content", :valueString "a-fr-ca"}]}
        {:url "http://hl7.org/fhir/StructureDefinition/translation",
         :extension
         [{:url "lang", :valueCode "en-CA"}
          {:url "content", :valueString "a-en-ca"}]}]}
      nil
      {:extension
       [{:url "http://hl7.org/fhir/StructureDefinition/translation",
         :extension
         [{:url "lang", :valueCode "fr-CA"}
          {:url "content", :valueString "c-fr-ca"}]}
        {:url "http://hl7.org/fhir/StructureDefinition/translation",
         :extension
         [{:url "lang", :valueCode "en-CA"}
          {:url "content", :valueString "c-en-ca"}]}]}],
     :status "active"})

  (world/query-match "select resource from location"
                     [{:resource {:_alias [{} nil {}]}}
                      nil]))

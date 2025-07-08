(ns proto.crud-op-test
  (:require [box.world :as world]
            [clojure.test :refer :all]))


(deftest crud-operations-test

  ;; (world/restart-box)
  (world/ensure-box)

  (world/truncate :Patient)

  (testing "create"

    (world/match
     {:uri "/Patient"
      :request-method :post
      :resource {:resourceType "Patient"
                 :name [{:family "John"}]}}
     {:status 201
      :body {:id string?
             :name [{:family "John"}]}})


    (world/match
     {:uri "/Patient"
      :request-method :post
      :resource {:resourceType "Patient"
                 :id "pt-1"
                 :name [{:family "Ivan"}]}}
     {:status 201
      :body {:id "pt-1"}})

    (world/match
     {:uri "/Patient"
      :request-method :post
      :resource {:resourceType "Patient"
                 :id "pt-1"
                 :name [{:family "Ivan"}]}}
     {:status 409})

    (world/match
     {:uri "/Patient/pt-1"
      :request-method :put
      :resource {:resourceType "Patient"
                 :id "pt-1"
                 :name [{:family "Ivanov"}]}}
     {:status 200
      :body {:id "pt-1"
             :name [{:family "Ivanov"}]}})

    (world/match
     {:uri "/Patient"
      :query-string "name=ivan"
      :request-method :put
      :resource {:name [{:family "Ivanov" :given ["Ivan"]}]}}
     {:status 200
      :body {:id "pt-1"
             :name [{:family "Ivanov" :given ["Ivan"]}]}})

    (world/match
     {:uri "/Patient"
      :query-string "name=ivan"
      :request-method :put
      :resource {:resourceType "Patient"
                 :id "pt-2"
                 :name [{:family "Ivanov"}]}}
     {:status 400
      :body {:resourceType "OperationOutcome"
             :text #"does not match"}})


    (world/match
     {:uri "/Patient"
      :request-method :post
      :resource {:resourceType "Patient"
                 :id "pt-2"
                 :name [{:family "Ivanchenco"}]}}
     {:status 201})

    (world/match
     {:uri "/Patient"
      :query-string "name=ivan"
      :request-method :put
      :resource {:name [{:family "Ivanov" :given ["Ivan"]}]}}
     {:status 412
      :body {:resourceType "OperationOutcome"
             :text #"Multiple"}})







    )





  )

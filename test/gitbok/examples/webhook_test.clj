(ns gitbok.examples.webhook-test
  (:require [clojure.test :refer [deftest testing is]]
            [gitbok.examples.webhook :as webhook]
            [gitbok.examples.indexer :as indexer]
            [gitbok.products :as products]
            [system]
            [clojure.data.json :as json]))

(defn create-test-context
  "Create a test context for webhook testing"
  []
  (system/new-context {}))

(def test-examples-data
  {:timestamp "2024-01-15T10:30:00Z"
   :examples [{:id "test-example-1"
               :title "Test Example 1"
               :description "Test description"
               :features ["feature1" "feature2"]
               :languages ["typescript"]
               :github_url "https://github.com/test/example1"
               :readme_url "https://github.com/test/example1/README.md"}
              {:id "test-example-2"
               :title "Test Example 2"
               :description "Another test"
               :features ["feature3"]
               :languages ["python" "java"]
               :github_url "https://github.com/test/example2"
               :readme_url "https://github.com/test/example2/README.md"}]
   :features_list ["feature1" "feature2" "feature3"]
   :languages_list ["java" "python" "typescript"]})

(deftest test-ip-validation
  (testing "IP to long conversion"
    (is (= 2130706433 (webhook/ip-to-long "127.0.0.1")))
    (is (= 3232235521 (webhook/ip-to-long "192.168.0.1")))
    (is (= 167772160 (webhook/ip-to-long "10.0.0.0"))))
  
  (testing "CIDR range validation"
    (let [[start end] (webhook/cidr-to-range "192.168.0.0/24")]
      (is (= 3232235520 start))
      (is (= 3232235775 end))))
  
  (testing "IP in CIDR check"
    (is (webhook/ip-in-cidr? "192.168.0.1" "192.168.0.0/24"))
    (is (webhook/ip-in-cidr? "192.168.0.255" "192.168.0.0/24"))
    (is (not (webhook/ip-in-cidr? "192.168.1.1" "192.168.0.0/24")))
    (is (webhook/ip-in-cidr? "10.0.0.5" "10.0.0.0/8"))
    (is (not (webhook/ip-in-cidr? "11.0.0.1" "10.0.0.0/8"))))
  
  (testing "GitHub IP validation"
    ;; These should be valid GitHub IPs
    (is (webhook/validate-github-ip "140.82.112.5"))
    (is (webhook/validate-github-ip "192.30.252.10"))
    ;; These should be invalid
    (is (not (webhook/validate-github-ip "127.0.0.1")))
    (is (not (webhook/validate-github-ip "8.8.8.8")))))

(deftest test-client-ip-extraction
  (testing "Extract client IP from headers"
    (let [request-forwarded {:headers {"x-forwarded-for" "1.2.3.4, 5.6.7.8"}
                             :remote-addr "9.9.9.9"}
          request-real {:headers {"x-real-ip" "2.3.4.5"}
                       :remote-addr "9.9.9.9"}
          request-remote {:remote-addr "3.4.5.6"}]
      (is (= "1.2.3.4" (webhook/get-client-ip request-forwarded)))
      (is (= "2.3.4.5" (webhook/get-client-ip request-real)))
      (is (= "3.4.5.6" (webhook/get-client-ip request-remote))))))

(deftest test-webhook-handler-dev-mode
  (testing "Webhook accepts any IP in dev mode"
    (with-redefs [system/getenv (fn [key] 
                                  (when (= key "DEV") "true"))]
      (let [context (create-test-context)
            request {:body (java.io.ByteArrayInputStream. 
                           (.getBytes (json/write-str test-examples-data)))
                    :headers {"x-forwarded-for" "127.0.0.1"}}
            response (webhook/webhook-handler context request)]
        (is (= 200 (:status response)))
        (is (= "OK" (:body response)))
        ;; Check that data was stored in context using indexer
        (let [ctx-with-product (products/set-current-product-id context "aidbox")
              stored-data (indexer/get-examples ctx-with-product)]
          (is (= test-examples-data stored-data))
          (is (= 2 (count (:examples stored-data))))
          (is (= 3 (count (:features_list stored-data))))
          (is (= 3 (count (:languages_list stored-data)))))))))

(deftest test-webhook-handler-prod-mode
  (testing "Webhook rejects non-GitHub IPs in production"
    (with-redefs [system/getenv (fn [key] nil)]  ; No DEV env var
      (let [context (create-test-context)
            request {:body (java.io.ByteArrayInputStream. 
                           (.getBytes (json/write-str test-examples-data)))
                    :headers {"x-forwarded-for" "127.0.0.1"}}
            response (webhook/webhook-handler context request)]
        (is (= 403 (:status response)))
        (is (= "Forbidden: Invalid source IP" (:body response)))
        ;; Check that data was NOT stored
        (let [ctx-with-product (products/set-current-product-id context "aidbox")]
          (is (nil? (indexer/get-examples ctx-with-product)))))))
  
  (testing "Webhook accepts GitHub IPs in production"
    (with-redefs [system/getenv (fn [key] nil)]
      (let [context (create-test-context)
            request {:body (java.io.ByteArrayInputStream. 
                           (.getBytes (json/write-str test-examples-data)))
                    :headers {"x-forwarded-for" "140.82.112.5"}}  ; GitHub IP
            response (webhook/webhook-handler context request)]
        (is (= 200 (:status response)))
        (is (= "OK" (:body response)))))))

(deftest test-webhook-validation
  (testing "Webhook rejects invalid data structure"
    (with-redefs [system/getenv (fn [key] 
                                  (when (= key "DEV") "true"))]
      (let [context (create-test-context)
            invalid-data {:invalid "data"}
            request {:body (java.io.ByteArrayInputStream. 
                           (.getBytes (json/write-str invalid-data)))
                    :headers {"x-forwarded-for" "127.0.0.1"}}
            response (webhook/webhook-handler context request)]
        (is (= 500 (:status response)))
        (is (= "Internal error" (:body response)))
        ;; Check that invalid data was NOT stored
        (let [ctx-with-product (products/set-current-product-id context "aidbox")]
          (is (nil? (indexer/get-examples ctx-with-product)))))))
  
  (testing "Webhook accepts valid data and stores it"
    (with-redefs [system/getenv (fn [key] 
                                  (when (= key "DEV") "true"))]
      (let [context (create-test-context)
            request {:body (java.io.ByteArrayInputStream. 
                           (.getBytes (json/write-str test-examples-data)))
                    :headers {"x-forwarded-for" "127.0.0.1"}}
            response (webhook/webhook-handler context request)]
        (is (= 200 (:status response)))
        (let [ctx-with-product (products/set-current-product-id context "aidbox")
              stored (indexer/get-examples ctx-with-product)]
          (is (= "2024-01-15T10:30:00Z" (:timestamp stored)))
          (is (= 2 (count (:examples stored))))
          (is (= "Test Example 1" (-> stored :examples first :title))))))))
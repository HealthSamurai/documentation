(ns gitbok.products-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [gitbok.products :as products]
   [gitbok.utils :as utils]
   [clj-yaml.core :as yaml]))

(deftest test-load-products-config
  (testing "load-products-config with root-redirect"
    (with-redefs [utils/slurp-resource 
                  (fn [path]
                    (when (= path "products.yaml")
                      "root-redirect: \"/aidbox\"\n\nproducts:\n  - id: test\n    name: Test Product\n    path: /test\n    config: .gitbook.yaml"))
                  products/read-product-config-file
                  (fn [config-file]
                    {:structure {:summary "SUMMARY.md"
                                 :readme "README.md"}})]
      (let [result (products/load-products-config "test-workdir")]
        (is (= "/aidbox" (:root-redirect result)))
        (is (= 1 (count (:products result))))
        (is (= "test" (-> result :products first :id)))
        (is (= "/test" (-> result :products first :path))))))
  
  (testing "load-products-config without root-redirect"
    (with-redefs [utils/slurp-resource 
                  (fn [path]
                    (when (= path "products.yaml")
                      "products:\n  - id: test\n    name: Test Product\n    path: /test\n    config: .gitbook.yaml"))
                  products/read-product-config-file
                  (fn [config-file]
                    {:structure {:summary "SUMMARY.md"
                                 :readme "README.md"}})]
      (let [result (products/load-products-config "test-workdir")]
        (is (nil? (:root-redirect result)))
        (is (= 1 (count (:products result)))))))
  
  (testing "load-products-config with no workdir returns default"
    (let [result (products/load-products-config nil)]
      (is (= products/default-aidbox (:products result)))
      (is (nil? (:root-redirect result)))))
  
  (testing "load-products-config handles exceptions"
    (with-redefs [utils/slurp-resource 
                  (fn [path]
                    (throw (Exception. "File not found")))]
      (let [result (products/load-products-config "test-workdir")]
        (is (= products/default-aidbox (:products result)))
        (is (nil? (:root-redirect result)))))))
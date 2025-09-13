(ns gitbok.products-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [gitbok.products :as products]
   [gitbok.test-helpers :as th]
   [gitbok.state :as state]))

(deftest test-load-products-config
  (testing "load-products-config with root-redirect"
    (with-redefs [state/slurp-resource
                  (fn [_context path]
                    (when (= path "products.yaml")
                      "root-redirect: \"/aidbox\"\n\nproducts:\n  - id: test\n    name: Test Product\n    path: /test\n    config: .gitbook.yaml"))
                  products/read-product-config-file
                  (fn [_context _config-file]
                    {:structure {:summary "SUMMARY.md"
                                 :readme "README.md"}})]
      (let [context (th/create-test-context)
            result (products/load-products-config context)]
        (is (= "/aidbox" (:root-redirect result)))
        (is (= 1 (count (:products result))))
        (is (= "test" (-> result :products first :id)))
        (is (= "/test" (-> result :products first :path))))))

  (testing "load-products-config without root-redirect"
    (with-redefs [state/slurp-resource
                  (fn 
                    ([path]
                     (when (= path "products.yaml")
                       "products:\n  - id: test\n    name: Test Product\n    path: /test\n    config: .gitbook.yaml"))
                    ([_context path]
                     (when (= path "products.yaml")
                       "products:\n  - id: test\n    name: Test Product\n    path: /test\n    config: .gitbook.yaml")))
                  products/read-product-config-file
                  (fn [_context _config-file]
                    {:structure {:summary "SUMMARY.md"
                                 :readme "README.md"}})]
      (let [context (th/create-test-context)
            result (products/load-products-config context)]
        (is (nil? (:root-redirect result)))
        (is (= 1 (count (:products result)))))))

  (testing "load-products-config handles exceptions"
    (with-redefs [state/slurp-resource
                  (fn 
                    ([_path]
                     (throw (Exception. "File not found")))
                    ([_context _path]
                     (throw (Exception. "File not found"))))]
      (let [context (th/create-test-context)
            result (products/load-products-config context)]
        (is (= products/default-aidbox (:products result)))
        (is (nil? (:root-redirect result)))))))

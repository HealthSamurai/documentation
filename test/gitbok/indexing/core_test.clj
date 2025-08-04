(ns gitbok.indexing.core-test
  (:require [clojure.test :refer :all]
            [gitbok.indexing.core :as indexing]
            [gitbok.products :as products]
            [gitbok.utils :as utils]))

(defn mock-context []
  {:current-product-id "test"})

(deftest slurp-md-files!-test
  (testing "Should filter out external URLs before processing"
    (let [test-files ["README.md" 
                      "getting-started.md" 
                      "https://fhir-schema.github.io/fhir-schema/"
                      "https://example.com/docs"
                      "api/reference.md"]]
      (with-redefs [products/filepath (fn [_ path] (str "./docs/" path))
                    indexing/read-content (fn [path] (str "Content of " path))]
        (let [result (indexing/slurp-md-files! (mock-context) test-files)]
          ;; Should only process internal files
          (is (= 3 (count result)))
          ;; Keys should be original paths without prefix
          (is (contains? result "README.md"))
          (is (contains? result "getting-started.md"))
          (is (contains? result "api/reference.md"))
          ;; Values should contain content read from full paths
          (is (= "Content of ./docs/README.md" (get result "README.md")))
          (is (= "Content of ./docs/getting-started.md" (get result "getting-started.md")))
          (is (= "Content of ./docs/api/reference.md" (get result "api/reference.md")))
          ;; Should not process external URLs
          (is (not (some #(re-find #"https?://" %) (keys result))))))))
  
  (testing "Should handle empty list"
    (with-redefs [products/filepath (fn [_ path] (str "./docs/" path))
                  indexing/read-content (fn [path] (str "Content of " path))]
      (let [result (indexing/slurp-md-files! (mock-context) [])]
        (is (= 0 (count result))))))
  
  (testing "Should handle list with only external URLs"
    (let [test-files ["https://example.com/1"
                      "http://example.com/2"
                      "https://github.com/docs"]]
      (with-redefs [products/filepath (fn [_ path] (str "./docs/" path))
                    indexing/read-content (fn [path] (str "Content of " path))]
        (let [result (indexing/slurp-md-files! (mock-context) test-files)]
          (is (= 0 (count result)))))))
  
  (testing "Should correctly process internal files"
    (let [test-files ["file1.md" "dir/file2.md"]]
      (with-redefs [products/filepath (fn [_ path] (str "./test/" path))
                    indexing/read-content (fn [path] 
                                           (str "Mock content for " path))]
        (let [result (indexing/slurp-md-files! (mock-context) test-files)]
          (is (= 2 (count result)))
          ;; Keys should be original paths
          (is (= "Mock content for ./test/file1.md" 
                 (get result "file1.md")))
          (is (= "Mock content for ./test/dir/file2.md" 
                 (get result "dir/file2.md")))))))
  
  (testing "Should handle mixed HTTP and HTTPS URLs"
    (let [test-files ["internal.md"
                      "http://example.com"
                      "another.md"
                      "https://example.com"]]
      (with-redefs [products/filepath (fn [_ path] path)
                    indexing/read-content (fn [path] "content")]
        (let [result (indexing/slurp-md-files! (mock-context) test-files)]
          (is (= 2 (count result)))
          (is (contains? result "internal.md"))
          (is (contains? result "another.md")))))))
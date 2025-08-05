(ns gitbok.ui.search-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [gitbok.ui.search :as search]
   [gitbok.http :as http]
   [gitbok.products :as products]))

(deftest highlight-text-test
  (testing "highlight-text highlights matching text case-insensitively"
    (is (= [:span
            "Hello "
            [:span {:class "bg-warning-2 text-tint-12 p-1 px-0.5 -mx-0.5 py-0.5 rounded"} "world"]
            " from Clojure"]
           (search/highlight-text "Hello world from Clojure" "world"))))
  
  (testing "highlight-text handles case-insensitive matching"
    (is (= [:span
            "Hello "
            [:span {:class "bg-warning-2 text-tint-12 p-1 px-0.5 -mx-0.5 py-0.5 rounded"} "WORLD"]
            " from Clojure"]
           (search/highlight-text "Hello WORLD from Clojure" "world"))))
  
  (testing "highlight-text returns original text when no match"
    (is (= "Hello world" (search/highlight-text "Hello world" "foo"))))
  
  (testing "highlight-text handles nil text"
    (is (nil? (search/highlight-text nil "query"))))
  
  (testing "highlight-text handles empty query"
    (is (= "Hello world" (search/highlight-text "Hello world" ""))))
  
  (testing "highlight-text highlights first occurrence only"
    (is (= [:span
            "Hello "
            [:span {:class "bg-warning-2 text-tint-12 p-1 px-0.5 -mx-0.5 py-0.5 rounded"} "world"]
            " and world again"]
           (search/highlight-text "Hello world and world again" "world")))))

(deftest build-search-result-url-test
  (let [mock-context (atom {})]
    ;; Mock the functions we need
    (with-redefs [http/get-product-prefix (fn [_] "/docs/aidbox")
                  http/get-prefix (fn [_] "/docs")
                  products/path (fn [_] "/aidbox")]
      
      (testing "build-search-result-url returns product prefix when URI is nil"
        (is (= "/docs/aidbox" (search/build-search-result-url @mock-context nil))))
      
      (testing "build-search-result-url preserves URI that already has full prefix"
        (is (= "/docs/aidbox/api/rest" 
               (search/build-search-result-url @mock-context "/docs/aidbox/api/rest"))))
      
      (testing "build-search-result-url adds /docs prefix to URI with product path only"
        (is (= "/docs/aidbox/api/rest" 
               (search/build-search-result-url @mock-context "/aidbox/api/rest"))))
      
      (testing "build-search-result-url prepends full prefix to absolute URI without product"
        (is (= "/docs/aidbox/api/rest" 
               (search/build-search-result-url @mock-context "/api/rest"))))
      
      (testing "build-search-result-url handles relative URI"
        (is (= "/docs/aidbox/api/rest" 
               (search/build-search-result-url @mock-context "api/rest")))))))
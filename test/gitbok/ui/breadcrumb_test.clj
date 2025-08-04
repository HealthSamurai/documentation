(ns gitbok.ui.breadcrumb-test
  (:require [clojure.test :refer [deftest testing is]]
            [clojure.string :as string]
            [gitbok.ui.breadcrumb :as breadcrumb]
            [gitbok.http :as http]))

(defn mock-context []
  {:current-product-id "forms"
   :system (atom {:products-config [{:id "forms" :path "/forms"}]
                  :full-config {:products [{:id "forms" :path "/forms"}]}})})

(defn extract-breadcrumb-items [hiccup]
  (when hiccup
    (->> hiccup
         (tree-seq coll? seq)
         (filter #(and (vector? %)
                       (>= (count %) 3)
                       (map? (second %))
                       (= :a (first %))))
         (map last))))

(deftest breadcrumb-test
  (testing "root pages have no breadcrumb"
    (is (nil? (breadcrumb/breadcrumb (mock-context) "")))
    (is (nil? (breadcrumb/breadcrumb (mock-context) nil))))
  
  (testing "readme pages have no breadcrumb"
    (is (nil? (breadcrumb/breadcrumb (mock-context) "readme")))
    (is (nil? (breadcrumb/breadcrumb (mock-context) "readme/README.md"))))
  
  (testing "single level pages show overview link"
    (let [result (breadcrumb/breadcrumb (mock-context) "getting-started")]
      (is (some? result))
      (let [items (extract-breadcrumb-items result)]
        (is (= 1 (count items)))
        (is (= "overview" (first items))))))
  
  (testing "overview page shows overview link"
    (let [result (breadcrumb/breadcrumb (mock-context) "overview")]
      (is (some? result))
      (let [items (extract-breadcrumb-items result)]
        (is (= 1 (count items)))
        (is (= "overview" (first items))))))
  
  (testing "pages under overview show two overview links"
    (let [result (breadcrumb/breadcrumb (mock-context) "overview/what-is-form")]
      (is (some? result))
      (let [items (extract-breadcrumb-items result)]
        (is (= 2 (count items)))
        (is (= "overview" (first items)))
        (is (= "overview" (second items))))))
  
  (testing "other nested pages show normal breadcrumb path"
    (let [result (breadcrumb/breadcrumb (mock-context) "docs/api/rest")]
      (is (some? result))
      (let [items (extract-breadcrumb-items result)]
        (is (= 2 (count items)))
        (is (= "docs" (first items)))
        (is (= "api" (second items))))))
  
  (testing "handles pages with hyphens correctly"
    (let [result (breadcrumb/breadcrumb (mock-context) "docs/getting-started/quick-start")]
      (is (some? result))
      (let [items (extract-breadcrumb-items result)]
        (is (= 2 (count items)))
        (is (= "docs" (first items)))
        (is (= "getting started" (second items)))))))
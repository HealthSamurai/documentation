(ns gitbok.ui.breadcrumb-test
  (:require [clojure.test :refer [deftest testing is]]
            [clojure.string :as str]
            [gitbok.ui.breadcrumb :as breadcrumb]
            [gitbok.test-helpers :as th]
            [gitbok.state :as state]))

(defn mock-context []
  (let [summary [{:href "/forms/overview"
                  :parsed {:title "Overview" :href "/forms/overview"}
                  :section-title "Overview"
                  :children [{:href "/forms/overview/what-is-form"
                              :parsed {:title "What is Form" :href "/forms/overview/what-is-form"}}]}
                 {:href "/forms/getting-started"
                  :parsed {:title "Getting Started" :href "/forms/getting-started"}
                  :section-title "Overview"}
                 {:href "/forms/docs"
                  :parsed {:title "Docs" :href "/forms/docs"}
                  :children [{:href "/forms/docs/api"
                              :parsed {:title "API" :href "/forms/docs/api"}
                              :children [{:href "/forms/docs/api/rest"
                                          :parsed {:title "REST" :href "/forms/docs/api/rest"}}]}
                             {:href "/forms/docs/getting-started"
                              :parsed {:title "Getting Started" :href "/forms/docs/getting-started"}
                              :children [{:href "/forms/docs/getting-started/quick-start"
                                          :parsed {:title "Quick Start" :href "/forms/docs/getting-started/quick-start"}}]}]}]
        product {:id "forms" :path "/forms"}
        context (th/create-test-context {:products {:config [product]
                                                    :indices {"forms" {:summary-hiccup summary}}}})]
    (assoc context
           :current-product-id "forms"
           :product product)))

(defn extract-breadcrumb-items [hiccup]
  (when hiccup
    (->> hiccup
         (tree-seq coll? seq)
         (filter #(and (vector? %)
                       (>= (count %) 3)
                       (map? (second %))
                       (or (= :a (first %))
                           (and (= :span (first %))
                                ;; Skip breadcrumb separators
                                (not (some-> (second %) :class (str/includes? "breadcrumb-separator")))))))
         (map last)
         (filter string?))))

(deftest breadcrumb-test
  (testing "root pages have no breadcrumb"
    (is (nil? (breadcrumb/breadcrumb (mock-context) "")))
    (is (nil? (breadcrumb/breadcrumb (mock-context) nil))))

  (testing "readme pages have no breadcrumb"
    (is (nil? (breadcrumb/breadcrumb (mock-context) "readme")))
    (is (nil? (breadcrumb/breadcrumb (mock-context) "readme/README.md"))))

  (testing "single level pages show overview link"
    (let [ctx (mock-context)
          result (breadcrumb/breadcrumb ctx "getting-started")]
      (is (some? result))
      (let [items (extract-breadcrumb-items result)]
        (is (= 1 (count items)))
        (is (= "Overview" (first items))))))

  (testing "overview page shows overview link"
    (let [ctx (mock-context)
          result (breadcrumb/breadcrumb ctx "overview")]
      (is (some? result))
      (let [items (extract-breadcrumb-items result)]
        (is (= 1 (count items)))
        (is (= "Overview" (first items))))))

  (testing "pages under overview show single overview link"
    (let [result (breadcrumb/breadcrumb (mock-context) "overview/what-is-form")]
      (is (some? result))
      (let [items (extract-breadcrumb-items result)]
        (is (= 1 (count items)))
        (is (= "Overview" (first items))))))

  (testing "other nested pages show normal breadcrumb path"
    (let [result (breadcrumb/breadcrumb (mock-context) "docs/api/rest")]
      (is (some? result))
      (let [items (extract-breadcrumb-items result)]
        (is (= 2 (count items)))
        (is (= "Docs" (first items)))
        (is (= "API" (second items))))))

  (testing "handles pages with hyphens correctly"
    (let [result (breadcrumb/breadcrumb (mock-context) "docs/getting-started/quick-start")]
      (is (some? result))
      (let [items (extract-breadcrumb-items result)]
        (is (= 2 (count items)))
        (is (= "Docs" (first items)))
        (is (= "Getting Started" (second items)))))))

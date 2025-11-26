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

(defn mock-context-with-deep-nesting []
  (let [summary [{:href "/aidbox/section"
                  :parsed {:title "Aidbox" :href "/aidbox/section"}
                  :section-title "Aidbox"
                  :children [{:href "/aidbox/deprecated"
                              :parsed {:title "Deprecated" :href "/aidbox/deprecated"}
                              :children [{:href "/aidbox/deprecated/deprecated2"
                                          :parsed {:title "Deprecated2" :href "/aidbox/deprecated/deprecated2"}
                                          :children [{:href "/aidbox/deprecated/deprecated2/zen-related"
                                                      :parsed {:title "Zen Related" :href "/aidbox/deprecated/deprecated2/zen-related"}
                                                      :children [{:href "/aidbox/deprecated/deprecated2/zen-related/topic-subscriptions"
                                                                  :parsed {:title "Topic-based Subscriptions" :href "/aidbox/deprecated/deprecated2/zen-related/topic-subscriptions"}
                                                                  :children [{:href "/aidbox/deprecated/deprecated2/zen-related/topic-subscriptions/r4b-api"
                                                                              :parsed {:title "R4B API Reference" :href "/aidbox/deprecated/deprecated2/zen-related/topic-subscriptions/r4b-api"}
                                                                              :children [{:href "/aidbox/deprecated/deprecated2/zen-related/topic-subscriptions/r4b-api/subscription-api"
                                                                                          :parsed {:title "Subscription API" :href "/aidbox/deprecated/deprecated2/zen-related/topic-subscriptions/r4b-api/subscription-api"}}]}]}]}]}]}]}]
        product {:id "aidbox" :path "/aidbox"}
        context (th/create-test-context {:products {:config [product]
                                                    :indices {"aidbox" {:summary-hiccup summary}}}})]
    (assoc context
           :current-product-id "aidbox"
           :product product)))

(deftest breadcrumb-test
  (testing "root pages have no breadcrumb"
    (is (nil? (breadcrumb/breadcrumb (mock-context) "")))
    (is (nil? (breadcrumb/breadcrumb (mock-context) nil))))

  (testing "readme pages have no breadcrumb"
    (is (nil? (breadcrumb/breadcrumb (mock-context) "readme/README.md")))
    (is (nil? (breadcrumb/breadcrumb (mock-context) "readme/setup"))))

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
        (is (= "Getting Started" (second items))))))

  (testing "limits breadcrumbs to 4 elements with ellipsis for deeply nested pages"
    (let [result (breadcrumb/breadcrumb (mock-context-with-deep-nesting) "deprecated/deprecated2/zen-related/topic-subscriptions/r4b-api/subscription-api")]
      (is (some? result))
      ;; Check that hiccup contains ellipsis
      (let [hiccup-str (pr-str result)]
        (is (str/includes? hiccup-str "...")))
      (let [items (extract-breadcrumb-items result)]
        ;; Should show: Aidbox / Deprecated / Deprecated2 / R4B API Reference (4 visible items, ellipsis in between)
        (is (= 4 (count items)))
        (is (= "Aidbox" (nth items 0)))
        (is (= "Deprecated" (nth items 1)))
        (is (= "Deprecated2" (nth items 2)))
        (is (= "R4B API Reference" (nth items 3)))))))

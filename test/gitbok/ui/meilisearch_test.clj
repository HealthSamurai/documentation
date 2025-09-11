(ns gitbok.ui.meilisearch-test
  (:require [clojure.test :refer [deftest testing is]]
            [gitbok.ui.meilisearch :as m]
            [hiccup2.core :as h]))

(deftest test-interpret-search-results
  (testing "Single page without sections should not be grouped"
    (let [results [{"hierarchy_lvl0" "ARTIFACT REGISTRY"
                    "hierarchy_lvl1" "SearchParameter"
                    "hierarchy_lvl2" nil}]
          interpreted (m/interpret-search-results results)
          expected [{:item {"hierarchy_lvl0" "ARTIFACT REGISTRY"
                            "hierarchy_lvl1" "SearchParameter"
                            "hierarchy_lvl2" nil}}]]
      (is (= expected interpreted))))

  (testing "Page with multiple sections should be grouped"
    (let [results [{"hierarchy_lvl0" "ARTIFACT REGISTRY"
                    "hierarchy_lvl1" "SearchParameter"
                    "hierarchy_lvl2" nil}
                   {"hierarchy_lvl0" "ARTIFACT REGISTRY"
                    "hierarchy_lvl1" "SearchParameter"
                    "hierarchy_lvl2" "SearchParameter fields"}
                   {"hierarchy_lvl0" "ARTIFACT REGISTRY"
                    "hierarchy_lvl1" "SearchParameter"
                    "hierarchy_lvl2" "Search Parameter Types"}]
          interpreted (m/interpret-search-results results)
          expected [{:group {:level 1 :title "SearchParameter"}
                     :items [{"hierarchy_lvl0" "ARTIFACT REGISTRY"
                              "hierarchy_lvl1" "SearchParameter"
                              "hierarchy_lvl2" nil}
                             {"hierarchy_lvl0" "ARTIFACT REGISTRY"
                              "hierarchy_lvl1" "SearchParameter"
                              "hierarchy_lvl2" "SearchParameter fields"}
                             {"hierarchy_lvl0" "ARTIFACT REGISTRY"
                              "hierarchy_lvl1" "SearchParameter"
                              "hierarchy_lvl2" "Search Parameter Types"}]}]]
      (is (= expected interpreted))))

  (testing "Different pages with same lvl1 but no sections should not be grouped"
    (let [results [{"hierarchy_lvl0" "ARTIFACT REGISTRY"
                    "hierarchy_lvl1" "SearchParameter"
                    "hierarchy_lvl2" nil}
                   {"hierarchy_lvl0" "REST API"
                    "hierarchy_lvl1" "SearchParameter"
                    "hierarchy_lvl2" nil}]
          interpreted (m/interpret-search-results results)
          expected [{:item {"hierarchy_lvl0" "ARTIFACT REGISTRY"
                            "hierarchy_lvl1" "SearchParameter"
                            "hierarchy_lvl2" nil}}
                    {:item {"hierarchy_lvl0" "REST API"
                            "hierarchy_lvl1" "SearchParameter"
                            "hierarchy_lvl2" nil}}]]
      (is (= expected interpreted))))

  (testing "Multiple lvl3 items under same lvl2 should group by lvl2"
    (let [results [{"hierarchy_lvl0" "ARTIFACT REGISTRY"
                    "hierarchy_lvl1" "SearchParameter"
                    "hierarchy_lvl2" "Search Parameter Types"
                    "hierarchy_lvl3" "string"}
                   {"hierarchy_lvl0" "ARTIFACT REGISTRY"
                    "hierarchy_lvl1" "SearchParameter"
                    "hierarchy_lvl2" "Search Parameter Types"
                    "hierarchy_lvl3" "token"}
                   {"hierarchy_lvl0" "ARTIFACT REGISTRY"
                    "hierarchy_lvl1" "SearchParameter"
                    "hierarchy_lvl2" "Search Parameter Types"
                    "hierarchy_lvl3" "reference"}]
          interpreted (m/interpret-search-results results)
          expected [{:group {:level 2 :title "Search Parameter Types"}
                     :items [{"hierarchy_lvl0" "ARTIFACT REGISTRY"
                              "hierarchy_lvl1" "SearchParameter"
                              "hierarchy_lvl2" "Search Parameter Types"
                              "hierarchy_lvl3" "string"}
                             {"hierarchy_lvl0" "ARTIFACT REGISTRY"
                              "hierarchy_lvl1" "SearchParameter"
                              "hierarchy_lvl2" "Search Parameter Types"
                              "hierarchy_lvl3" "token"}
                             {"hierarchy_lvl0" "ARTIFACT REGISTRY"
                              "hierarchy_lvl1" "SearchParameter"
                              "hierarchy_lvl2" "Search Parameter Types"
                              "hierarchy_lvl3" "reference"}]}]]
      (is (= expected interpreted))))

  (testing "Mixed results - some grouped, some not"
    (let [results [;; This should be grouped
                   {"hierarchy_lvl0" "ARTIFACT REGISTRY"
                    "hierarchy_lvl1" "SearchParameter"
                    "hierarchy_lvl2" nil}
                   {"hierarchy_lvl0" "ARTIFACT REGISTRY"
                    "hierarchy_lvl1" "SearchParameter"
                    "hierarchy_lvl2" "SearchParameter fields"}
                   {"hierarchy_lvl0" "ARTIFACT REGISTRY"
                    "hierarchy_lvl1" "SearchParameter"
                    "hierarchy_lvl2" "Search Parameter Types"}
                   ;; These should NOT be grouped (different pages, no sections)
                   {"hierarchy_lvl0" "SEARCH TUTORIALS"
                    "hierarchy_lvl1" "string"
                    "hierarchy_lvl2" nil}
                   {"hierarchy_lvl0" "SEARCH TUTORIALS"
                    "hierarchy_lvl1" "token"
                    "hierarchy_lvl2" nil}
                   {"hierarchy_lvl0" "SEARCH TUTORIALS"
                    "hierarchy_lvl1" "reference"
                    "hierarchy_lvl2" nil}]
          interpreted (m/interpret-search-results results)
          expected [{:group {:level 1 :title "SearchParameter"}
                     :items [{"hierarchy_lvl0" "ARTIFACT REGISTRY"
                              "hierarchy_lvl1" "SearchParameter"
                              "hierarchy_lvl2" nil}
                             {"hierarchy_lvl0" "ARTIFACT REGISTRY"
                              "hierarchy_lvl1" "SearchParameter"
                              "hierarchy_lvl2" "SearchParameter fields"}
                             {"hierarchy_lvl0" "ARTIFACT REGISTRY"
                              "hierarchy_lvl1" "SearchParameter"
                              "hierarchy_lvl2" "Search Parameter Types"}]}
                    {:item {"hierarchy_lvl0" "SEARCH TUTORIALS"
                            "hierarchy_lvl1" "string"
                            "hierarchy_lvl2" nil}}
                    {:item {"hierarchy_lvl0" "SEARCH TUTORIALS"
                            "hierarchy_lvl1" "token"
                            "hierarchy_lvl2" nil}}
                    {:item {"hierarchy_lvl0" "SEARCH TUTORIALS"
                            "hierarchy_lvl1" "reference"
                            "hierarchy_lvl2" nil}}]]
      (is (= expected interpreted))))

  (testing "Nested hierarchy with lvl3 under lvl2"
    (let [results [{"hierarchy_lvl0" "REST API"
                    "hierarchy_lvl1" "FHIR Search"
                    "hierarchy_lvl2" "Including referenced resources"
                    "hierarchy_lvl3" nil}
                   {"hierarchy_lvl0" "REST API"
                    "hierarchy_lvl1" "FHIR Search"
                    "hierarchy_lvl2" "Including referenced resources"
                    "hierarchy_lvl3" "Reverse Include"}]
          interpreted (m/interpret-search-results results)
          expected [{:group {:level 2 :title "Including referenced resources"}
                     :items [{"hierarchy_lvl0" "REST API"
                              "hierarchy_lvl1" "FHIR Search"
                              "hierarchy_lvl2" "Including referenced resources"
                              "hierarchy_lvl3" nil}
                             {"hierarchy_lvl0" "REST API"
                              "hierarchy_lvl1" "FHIR Search"
                              "hierarchy_lvl2" "Including referenced resources"
                              "hierarchy_lvl3" "Reverse Include"}]}]]
      (is (= expected interpreted))))

  (testing "Real search scenario - 'search' query"
    ;; Результаты как на скриншоте
    (let [results [;; Первая группа - должна группироваться
                   {"hierarchy_lvl0" "ARTIFACT REGISTRY"
                    "hierarchy_lvl1" "SearchParameter"
                    "hierarchy_lvl2" nil
                    "content" "Search parameters can be defined in"}
                   {"hierarchy_lvl0" "ARTIFACT REGISTRY"
                    "hierarchy_lvl1" "SearchParameter"
                    "hierarchy_lvl2" nil
                    "content" "Search parameters can be defined in"}
                   {"hierarchy_lvl0" "ARTIFACT REGISTRY"
                    "hierarchy_lvl1" "SearchParameter"
                    "hierarchy_lvl2" "SearchParameter fields"
                    "content" "Search parameter unique canonical url"}
                   {"hierarchy_lvl0" "ARTIFACT REGISTRY"
                    "hierarchy_lvl1" "SearchParameter"
                    "hierarchy_lvl2" "Search Parameter Types"}
                   ;; Отдельные элементы типов поиска - НЕ должны группироваться
                   {"hierarchy_lvl0" "SEARCH TUTORIALS"
                    "hierarchy_lvl1" "string"
                    "hierarchy_lvl2" nil}
                   {"hierarchy_lvl0" "SEARCH TUTORIALS"
                    "hierarchy_lvl1" "token"
                    "hierarchy_lvl2" nil}
                   {"hierarchy_lvl0" "SEARCH TUTORIALS"
                    "hierarchy_lvl1" "uri"
                    "hierarchy_lvl2" nil}
                   {"hierarchy_lvl0" "SEARCH TUTORIALS"
                    "hierarchy_lvl1" "reference"
                    "hierarchy_lvl2" nil}
                   {"hierarchy_lvl0" "SEARCH TUTORIALS"
                    "hierarchy_lvl1" "date"
                    "hierarchy_lvl2" nil}
                   {"hierarchy_lvl0" "SEARCH TUTORIALS"
                    "hierarchy_lvl1" "number"
                    "hierarchy_lvl2" nil}
                   {"hierarchy_lvl0" "SEARCH TUTORIALS"
                    "hierarchy_lvl1" "quantity"
                    "hierarchy_lvl2" nil}
                   {"hierarchy_lvl0" "SEARCH TUTORIALS"
                    "hierarchy_lvl1" "composite"
                    "hierarchy_lvl2" nil}
                   {"hierarchy_lvl0" "SEARCH TUTORIALS"
                    "hierarchy_lvl1" "special"
                    "hierarchy_lvl2" nil}
                   ;; Специальные параметры поиска
                   {"hierarchy_lvl0" "SEARCH TUTORIALS"
                    "hierarchy_lvl1" "_id"
                    "hierarchy_lvl2" nil
                    "content" "Search by resource ID"}
                   {"hierarchy_lvl0" "SEARCH TUTORIALS"
                    "hierarchy_lvl1" "_lastUpdated"
                    "hierarchy_lvl2" nil
                    "content" "Search by the last modification time"}]
          interpreted (m/interpret-search-results results)]

      ;; Проверяем количество групп/элементов
      (is (= 12 (count interpreted))) ; 1 группа + 11 отдельных элементов

      ;; Проверяем первую группу
      (is (= {:level 1 :title "SearchParameter"}
             (get-in (first interpreted) [:group])))
      (is (= 4 (count (get-in (first interpreted) [:items]))))

      ;; Проверяем, что остальные не сгруппированы
      (is (every? #(contains? % :item) (rest interpreted))))))

(deftest test-prepare-for-render
  (testing "Grouped items should have correct render structure"
    (let [interpreted-group {:group {:level 1 :title "SearchParameter"}
                             :items [{"hierarchy_lvl0" "ARTIFACT REGISTRY"
                                      "hierarchy_lvl1" "SearchParameter"
                                      "hierarchy_lvl2" nil
                                      "url" "http://example.com/search#section"}
                                     {"hierarchy_lvl0" "ARTIFACT REGISTRY"
                                      "hierarchy_lvl1" "SearchParameter"
                                      "hierarchy_lvl2" "SearchParameter fields"
                                      "url" "http://example.com/search#fields"}]}
          prepared (m/prepare-for-render interpreted-group)
          expected {:header {"hierarchy_lvl0" "ARTIFACT REGISTRY"
                             "hierarchy_lvl1" "SearchParameter"
                             "hierarchy_lvl2" nil
                             "hierarchy_lvl3" nil
                             "hierarchy_lvl4" nil
                             "hierarchy_lvl5" nil
                             "hierarchy_lvl6" nil
                             "anchor" nil
                             "url" "http://example.com/search"}
                    :children [{"hierarchy_lvl0" "ARTIFACT REGISTRY"
                                "hierarchy_lvl1" "SearchParameter"
                                "hierarchy_lvl2" nil
                                "url" "http://example.com/search#section"}
                               {"hierarchy_lvl0" "ARTIFACT REGISTRY"
                                "hierarchy_lvl1" "SearchParameter"
                                "hierarchy_lvl2" "SearchParameter fields"
                                "url" "http://example.com/search#fields"}]
                    :group {:level 1 :title "SearchParameter"}}]
      (is (= expected prepared))))

  (testing "Ungrouped items should be returned as-is"
    (let [interpreted-item {:item {"hierarchy_lvl0" "SEARCH TUTORIALS"
                                   "hierarchy_lvl1" "string"
                                   "hierarchy_lvl2" nil}}
          prepared (m/prepare-for-render interpreted-item)
          expected {"hierarchy_lvl0" "SEARCH TUTORIALS"
                    "hierarchy_lvl1" "string"
                    "hierarchy_lvl2" nil}]
      (is (= expected prepared)))))

(deftest test-h1-with-content
  (testing "H1-only items should display content inline"
    (let [item {"hierarchy_lvl0" "API Reference"
                "hierarchy_lvl1" "SearchParameter"
                "hierarchy_lvl2" nil
                "hierarchy_lvl3" nil
                "hierarchy_lvl4" nil
                "hierarchy_lvl5" nil
                "content" "SearchParameter is a FHIR resource that defines a search parameter."
                "url" "/docs/api/searchparameter"}
          ;; Render as ungrouped item (is-grouped? = false)
          rendered (m/render-result-item item "search" 0 false)
          html-str (h/html rendered)]
      ;; Should contain the content snippet
      (is (re-find #"SearchParameter is a FHIR resource" (str html-str)))))

  (testing "H1-only items should NOT be grouped when multiple exist"
    (let [results [{"hierarchy_lvl0" "API Reference"
                    "hierarchy_lvl1" "SearchParameter"
                    "hierarchy_lvl2" nil
                    "hierarchy_lvl3" nil
                    "hierarchy_lvl4" nil
                    "hierarchy_lvl5" nil
                    "content" "SearchParameter is a FHIR resource..."
                    "url" "/docs/api/searchparameter"}
                   {"hierarchy_lvl0" "API Reference"
                    "hierarchy_lvl1" "SearchParameter"
                    "hierarchy_lvl2" nil
                    "hierarchy_lvl3" nil
                    "hierarchy_lvl4" nil
                    "hierarchy_lvl5" nil
                    "content" "Another match about SearchParameter..."
                    "url" "/docs/api/searchparameter#details"}]
          grouped (m/group-results-by-hierarchy results)]
      ;; Should be one group with nil group-info (no grouping)
      (is (= 1 (count grouped)))
      (is (nil? (:group-info (first grouped))))
      (is (= 2 (count (:items (first grouped)))))))

  (testing "H1 with subsections should still be grouped"
    (let [results [{"hierarchy_lvl0" "API Reference"
                    "hierarchy_lvl1" "Patient"
                    "hierarchy_lvl2" nil
                    "content" "Patient resource overview"
                    "url" "/docs/api/patient"}
                   {"hierarchy_lvl0" "API Reference"
                    "hierarchy_lvl1" "Patient"
                    "hierarchy_lvl2" "Demographics"
                    "content" "Patient demographics section"
                    "url" "/docs/api/patient#demographics"}]
          grouped (m/group-results-by-hierarchy results)]
      ;; Should be grouped
      (is (= 1 (count grouped)))
      (is (= {:level 1 :title "Patient"} (:group-info (first grouped))))
      (is (= 2 (count (:items (first grouped))))))))

(deftest test-render-search-results-no-duplication
  (testing "H1-only item used as group header should not be duplicated in children"
    (let [results [{"hierarchy_lvl0" "artifact registry"
                    "hierarchy_lvl1" "SearchParameter"
                    "hierarchy_lvl2" nil
                    "content" "Search parameters can be defined in"
                    "url" "https://example.com/searchparameter"}
                   {"hierarchy_lvl0" "artifact registry"
                    "hierarchy_lvl1" "SearchParameter"
                    "hierarchy_lvl2" "SearchParameter fields"
                    "content" "Search parameter unique canonical url"
                    "url" "https://example.com/searchparameter#fields"}]
          grouped (m/group-results-by-hierarchy results)
          rendered (m/render-search-results grouped "search")
          html-str (h/html rendered)]

      ;; Should only have one "SearchParameter" title (in the header)
      (is (= 1 (count (re-seq #">SearchParameter<" (str html-str)))))

      ;; Should have "SearchParameter fields" as a child
      (is (re-find #"SearchParameter fields" (str html-str)))

      ;; Should show content from h1-only item in header
      (is (re-find #"Search parameters can be defined in" (str html-str)))

      ;; Should show content from h2 item
      (is (re-find #"Search parameter unique canonical url" (str html-str)))))

  (testing "When no h1-only item exists, all items should be rendered as children"
    (let [results [{"hierarchy_lvl0" "artifact registry"
                    "hierarchy_lvl1" "SearchParameter"
                    "hierarchy_lvl2" "SearchParameter fields"
                    "content" "Field 1 content"
                    "url" "https://example.com/searchparameter#fields"}
                   {"hierarchy_lvl0" "artifact registry"
                    "hierarchy_lvl1" "SearchParameter"
                    "hierarchy_lvl2" "Search Parameter Types"
                    "content" "Types content"
                    "url" "https://example.com/searchparameter#types"}]
          grouped (m/group-results-by-hierarchy results)
          rendered (m/render-search-results grouped "search")
          html-str (h/html rendered)]

      ;; Should have header with SearchParameter
      (is (re-find #">SearchParameter<" (str html-str)))

      ;; Both h2 items should be present
      (is (re-find #"SearchParameter fields" (str html-str)))
      (is (re-find #"Search Parameter Types" (str html-str))))))

;; Run tests with: clojure -M:test

(ns gitbok.indexing.summary-test
  (:require [clojure.test :refer [deftest is testing]]
            [gitbok.indexing.impl.summary :as summary]
            [gitbok.state :as state]))

(defn mock-context []
  {:current-product-id "test"})

(deftest parse-md-link-test
  (testing "Should parse internal links"
    (is (= {:title "Getting Started" :href "getting-started"}
           (summary/parse-md-link "* [Getting Started](getting-started.md)")))
    (is (= {:title "API Reference" :href "api/reference"}
           (summary/parse-md-link "* [API Reference](api/reference.md)"))))

  (testing "Should parse external links"
    (is (= {:title "FHIR Schema" :href "https://fhir-schema.github.io/fhir-schema/"}
           (summary/parse-md-link "* [FHIR Schema](https://fhir-schema.github.io/fhir-schema/)")))
    (is (= {:title "GitHub" :href "https://github.com/aidbox"}
           (summary/parse-md-link "* [GitHub](https://github.com/aidbox)"))))

  (testing "Should handle README.md"
    (is (= {:title "Home" :href ""}
           (summary/parse-md-link "* [Home](README.md)")))
    (is (= {:title "Section Home" :href "section"}
           (summary/parse-md-link "* [Section Home](section/README.md)")))))

(deftest file->href-test
  (testing "Should preserve external URLs"
    (is (= "https://example.com"
           (summary/file->href "https://example.com")))
    (is (= "http://example.com"
           (summary/file->href "http://example.com"))))

  (testing "Should transform internal paths"
    (is (= "getting-started"
           (summary/file->href "getting-started.md")))
    (is (= "api/reference"
           (summary/file->href "api/reference.md")))
    (is (= ""
           (summary/file->href "README.md")))
    (is (= "section"
           (summary/file->href "section/README.md")))))

(deftest get-navigation-links-test
  (testing "Should filter out external links from navigation"
    (with-redefs [state/get-summary
                  (fn [_]
                    [{:parsed {:title "Internal" :href "/docs/internal"}
                      :href "/docs/internal"}
                     {:parsed {:title "External" :href "https://example.com"}
                      :href "https://example.com"}
                     {:parsed {:title "Another Internal" :href "/docs/another"}
                      :href "/docs/another"}])]
      (let [result (summary/get-navigation-links (mock-context))]
        (is (= 2 (count result)))
        (is (every? #(not (re-find #"^https?://" (:href %))) result))
        (is (= "/docs/internal" (:href (first result))))
        (is (= "/docs/another" (:href (second result)))))))

  (testing "Should handle empty navigation"
    (with-redefs [state/get-summary (fn [_] [])]
      (let [result (summary/get-navigation-links (mock-context))]
        (is (= 0 (count result))))))

  (testing "Should filter various URL schemes"
    (with-redefs [state/get-summary
                  (fn [_]
                    [{:parsed {:title "HTTPS" :href "https://example.com"}
                      :href "https://example.com"}
                     {:parsed {:title "HTTP" :href "http://example.com"}
                      :href "http://example.com"}
                     {:parsed {:title "Internal" :href "/internal"}
                      :href "/internal"}])]
      (let [result (summary/get-navigation-links (mock-context))]
        (is (= 1 (count result)))
        (is (= "/internal" (:href (first result))))))))

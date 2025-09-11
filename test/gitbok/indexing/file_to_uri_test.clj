(ns gitbok.indexing.file-to-uri-test
  (:require [clojure.test :refer :all]
            [gitbok.indexing.impl.file-to-uri :as file-to-uri]
            [gitbok.indexing.impl.summary :as summary]
            [gitbok.products :as products]
))

(defn mock-context []
  {:current-product-id "test"})

(deftest file->uri-idx-test
  (testing "External URLs should be filtered out"
    (with-redefs [summary/read-summary 
                  (fn [_] 
                    "# Table of Contents
* [Getting Started](getting-started.md)
* [API Reference](api/reference.md)
* [FHIR Schema reference](https://fhir-schema.github.io/fhir-schema/)
* [External Docs](https://example.com/docs)
* [GitHub](https://github.com/aidbox/aidbox)")]
      (let [result (file-to-uri/file->uri-idx (mock-context))]
        ;; Should only have internal links
        (is (= 2 (count result)))
        (is (contains? result "getting-started.md"))
        (is (contains? result "api/reference.md"))
        ;; Should not have external links
        (is (not (contains? result "https://fhir-schema.github.io/fhir-schema/")))
        (is (not (contains? result "https://example.com/docs")))
        (is (not (contains? result "https://github.com/aidbox/aidbox"))))))
  
  (testing "Should correctly parse internal links"
    (with-redefs [summary/read-summary 
                  (fn [_] 
                    "* [Home](README.md)
* [Features](readme/features.md)")]
      (let [result (file-to-uri/file->uri-idx (mock-context))]
        (is (= 2 (count result)))
        ;; Check README.md handling - for root README.md
        (is (= {:title "Home" :uri "/"} (get result "README.md")))
        ;; Check regular file - now uses filename without extension
        (is (= {:title "Features" :uri "readme/features"} 
               (get result "readme/features.md"))))))
  
  (testing "Mixed HTTP and HTTPS links should be filtered"
    (with-redefs [summary/read-summary 
                  (fn [_] 
                    "* [Internal](docs/internal.md)
* [HTTP Link](http://example.com)
* [HTTPS Link](https://example.com)
* [Another Internal](guide.md)")]
      (let [result (file-to-uri/file->uri-idx (mock-context))]
        (is (= 2 (count result)))
        (is (contains? result "docs/internal.md"))
        (is (contains? result "guide.md"))
        (is (not (contains? result "http://example.com")))
        (is (not (contains? result "https://example.com"))))))
  
  (testing "Terminology Overview case - filename vs title"
    (with-redefs [summary/read-summary 
                  (fn [_] 
                    "* [Terminology Overview](terminology/overview.md)
* [API Reference](api/reference.md)")]
      (let [result (file-to-uri/file->uri-idx (mock-context))]
        (is (= 2 (count result)))
        ;; Should use filename "overview" not title-based "terminology-overview"
        (is (= {:title "Terminology Overview" :uri "terminology/overview"} 
               (get result "terminology/overview.md")))
        (is (= {:title "API Reference" :uri "api/reference"} 
               (get result "api/reference.md")))))))

(deftest filepath->uri-test
  (testing "Should return URI for existing filepath"
    (with-redefs [file-to-uri/get-idx 
                  (fn [_] {"test.md" {:uri "/test"}})]
      (is (= "/test" (file-to-uri/filepath->uri (mock-context) "test.md")))))
  
  (testing "Should return nil for non-existing filepath"
    (with-redefs [file-to-uri/get-idx 
                  (fn [_] {"test.md" {:uri "/test"}})]
      (is (nil? (file-to-uri/filepath->uri (mock-context) "non-existing.md")))))
  
  (testing "Should return nil for external URLs"
    (with-redefs [file-to-uri/get-idx 
                  (fn [_] {"test.md" {:uri "/test"}})]
      (is (nil? (file-to-uri/filepath->uri (mock-context) "https://example.com"))))))
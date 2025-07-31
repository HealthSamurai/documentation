(ns gitbok.utils-test
  (:require [clojure.test :refer :all]
            [gitbok.utils :as utils]))

(deftest concat-urls-test
  (testing "Basic path concatenation"
    (is (= "/foo/bar" (utils/concat-urls "/foo" "bar")))
    (is (= "/foo/bar" (utils/concat-urls "/foo/" "bar")))
    (is (= "/foo/bar" (utils/concat-urls "/foo" "/bar")))
    (is (= "/foo/bar" (utils/concat-urls "/foo/" "/bar"))))

  (testing "Multiple paths"
    (is (= "/foo/bar/baz" (utils/concat-urls "/foo" "bar" "baz")))
    (is (= "/foo/bar/baz" (utils/concat-urls "/foo/" "/bar/" "/baz"))))

  (testing "Empty and nil values"
    (is (= "/" (utils/concat-urls)))
    (is (= "/" (utils/concat-urls "")))
    (is (= "/" (utils/concat-urls nil)))
    (is (= "/foo" (utils/concat-urls "/foo" nil)))
    (is (= "/foo" (utils/concat-urls nil "/foo")))
    (is (= "/foo/bar" (utils/concat-urls "/foo" nil "bar"))))

  (testing "Root path handling"
    (is (= "/" (utils/concat-urls "/")))
    (is (= "/" (utils/concat-urls "/" "/")))
    (is (= "foo" (utils/concat-urls "/" "foo"))))

  (testing "Paths with dots"
    (is (= "/foo/./bar" (utils/concat-urls "/foo" "." "bar")))
    (is (= "/foo/../bar" (utils/concat-urls "/foo" ".." "bar")))
    (is (= "./foo" (utils/concat-urls "/" "." "foo")))
    (is (= "../foo" (utils/concat-urls "/" ".." "foo")))
    (is (= "/foo/bar/." (utils/concat-urls "/foo/bar" ".")))
    (is (= "/foo/bar/.." (utils/concat-urls "/foo/bar" ".."))))

  (testing "Multiple slashes normalization"
    (is (= "/foo/bar" (utils/concat-urls "/foo//" "bar")))
    (is (= "/foo//bar" (utils/concat-urls "/foo" "//bar")))
    (is (= "/foo///bar" (utils/concat-urls "/foo///" "///bar")))))

(deftest concat-filenames-test
  (testing "Basic file path concatenation"
    (is (= "foo/bar" (utils/concat-filenames "foo" "bar")))
    (is (= "foo/bar/baz" (utils/concat-filenames "foo" "bar" "baz"))))

  (testing "Absolute paths"
    (is (= "foo/bar" (utils/concat-filenames "/foo" "bar")))
    (is (= "foo/bar/baz" (utils/concat-filenames "/foo" "bar" "baz"))))

  (testing "Paths with dots"
    (is (= "foo/./bar" (utils/concat-filenames "foo" "." "bar")))
    (is (= "foo/../bar" (utils/concat-filenames "foo" ".." "bar")))
    (is (= "./foo/bar" (utils/concat-filenames "." "foo" "bar")))
    (is (= "../foo/bar" (utils/concat-filenames ".." "foo" "bar")))
    (is (= "foo/bar/." (utils/concat-filenames "foo" "bar" ".")))
    (is (= "foo/bar/.." (utils/concat-filenames "foo" "bar" ".."))))

  (testing "Empty paths"
    ;; concat-filenames requires at least one argument
    ;; (is (= nil (utils/concat-filenames))) - это вызовет ошибку 
    (is (= "foo/bar" (utils/concat-filenames "foo" "bar")))
    (is (= "./foo" (utils/concat-filenames "." "foo"))))

  (testing "Complex paths with dots"
    (is (= "foo/./bar/../baz" (utils/concat-filenames "foo" "." "bar" ".." "baz")))
    (is (= "../foo/./bar" (utils/concat-filenames ".." "foo" "." "bar")))
    (is (= "./foo/../bar/." (utils/concat-filenames "." "foo" ".." "bar" ".")))))

(deftest uri-to-relative-test
  (testing "Basic URI to relative conversion"
    (is (= "readme" (utils/uri-to-relative "/docs/aidbox/readme" "/docs" "/aidbox")))
    (is (= "api/endpoints" (utils/uri-to-relative "/docs/forms/api/endpoints" "/docs" "/forms")))
    (is (= "getting-started/installation" (utils/uri-to-relative "/docs/aidbox/getting-started/installation" "/docs" "/aidbox")))
    (is (= "readme/features" (utils/uri-to-relative "/aidbox/readme/features" "/" "/aidbox"))))

  (testing "Root paths"
    (is (= "/" (utils/uri-to-relative "/docs/aidbox" "/docs" "/aidbox")))
    (is (= "/" (utils/uri-to-relative "/docs/aidbox/" "/docs" "/aidbox")))
    (is (= "/" (utils/uri-to-relative "/docs/forms/" "/docs" "/forms"))))

  (testing "Without prefix"
    (is (= "readme" (utils/uri-to-relative "/aidbox/readme" nil "/aidbox")))
    (is (= "readme" (utils/uri-to-relative "/aidbox/readme" "" "/aidbox"))))

  (testing "Without product path"
    (is (= "aidbox/readme" (utils/uri-to-relative "/docs/aidbox/readme" "/docs" nil)))
    (is (= "aidbox/readme" (utils/uri-to-relative "/docs/aidbox/readme" "/docs" ""))))

  (testing "Neither prefix nor product path"
    (is (= "docs/aidbox/readme" (utils/uri-to-relative "/docs/aidbox/readme" nil nil)))
    (is (= "docs/aidbox/readme" (utils/uri-to-relative "/docs/aidbox/readme" "" ""))))

  (testing "Edge cases"
    (is (= nil (utils/uri-to-relative nil "/docs" "/aidbox")))
    (is (= "/" (utils/uri-to-relative "/" "/" "/")))
    (is (= "readme" (utils/uri-to-relative "///docs///aidbox///readme" "/docs" "/aidbox"))))

  (testing "Paths with dots"
    (is (= "./readme" (utils/uri-to-relative "/docs/aidbox/./readme" "/docs" "/aidbox")))
    (is (= "../readme" (utils/uri-to-relative "/docs/aidbox/../readme" "/docs" "/aidbox")))
    (is (= "path/./to/../file" (utils/uri-to-relative "/docs/forms/path/./to/../file" "/docs" "/forms"))))

  (testing "Mismatched prefixes"
    (is (= "api/aidbox/readme" (utils/uri-to-relative "/api/aidbox/readme" "/docs" "/aidbox")))
    (is (= "docs/aidbox/readme" (utils/uri-to-relative "/docs/aidbox/readme" "/api" "/aidbox")))
    (is (= "aidbox/readme" (utils/uri-to-relative "/docs/aidbox/readme" "/docs" "/forms")))
    (is (= "aidbox/readme" (utils/uri-to-relative "/docs/aidbox/readme" "/docs" "/api")))))

(deftest s->url-slug-test
  (testing "Basic string to URL slug conversion"
    (is (= "hello-world" (utils/s->url-slug "Hello World")))
    (is (= "test-123" (utils/s->url-slug "Test 123")))
    (is (= "simple-text" (utils/s->url-slug "simple text")))
    (is (= "mixed-case-text" (utils/s->url-slug "MiXeD CaSe TeXt"))))

  (testing "Special characters handling"
    (is (= "hello-world" (utils/s->url-slug "Hello, World!")))
    (is (= "test-example-com" (utils/s->url-slug "test@example.com")))
    (is (= "price-99-99" (utils/s->url-slug "Price: $99.99")))
    (is (= "question-answer" (utils/s->url-slug "Question? Answer!")))
    (is (= "path-to-file" (utils/s->url-slug "path/to/file")))
    (is (= "c-programming" (utils/s->url-slug "C++ Programming"))))

  (testing "Multiple dashes handling"
    (is (= "hello-world" (utils/s->url-slug "Hello---World")))
    (is (= "test-example" (utils/s->url-slug "Test - - - Example")))
    (is (= "spaced-out" (utils/s->url-slug "Spaced    Out")))
    (is (= "many-special-chars" (utils/s->url-slug "Many!!!Special???Chars"))))

  (testing "Leading and trailing dashes"
    (is (= "hello" (utils/s->url-slug "-Hello-")))
    (is (= "world" (utils/s->url-slug "---World---")))
    (is (= "test" (utils/s->url-slug "   Test   ")))
    (is (= "example" (utils/s->url-slug "!!!Example!!!"))))

  (testing "Edge cases"
    (is (= nil (utils/s->url-slug nil)))
    (is (= "" (utils/s->url-slug "")))
    (is (= "" (utils/s->url-slug "   ")))
    (is (= "" (utils/s->url-slug "---")))
    (is (= "" (utils/s->url-slug "!!!")))
    (is (= "123" (utils/s->url-slug "123")))
    (is (= "abc123xyz" (utils/s->url-slug "abc123xyz"))))

  (testing "Unicode and accented characters"
    (is (= "" (utils/s->url-slug "Привет мир"))) ;; Russian - removed
    (is (= "caf-men" (utils/s->url-slug "Café Menü"))) ;; Accented - only ASCII remains
    (is (= "" (utils/s->url-slug "你好"))) ;; Chinese - removed
    (is (= "test-test" (utils/s->url-slug "test™ test®"))) ;; Trademark symbols
    (is (= "emoji-text" (utils/s->url-slug "😀 emoji text"))))

  (testing "Real-world examples"
    (is (= "getting-started-with-aidbox" (utils/s->url-slug "Getting Started with Aidbox")))
    (is (= "api-reference-v2-0" (utils/s->url-slug "API Reference v2.0")))
    (is (= "how-to-configure-oauth-2-0" (utils/s->url-slug "How to Configure OAuth 2.0")))
    (is (= "fhir-r4-implementation-guide" (utils/s->url-slug "FHIR R4 Implementation Guide")))
    (is (= "what-s-new-in-2024" (utils/s->url-slug "What's New in 2024?")))
    (is (= "10-best-practices" (utils/s->url-slug "10 Best Practices")))
    (is (= "json-vs-xml-comparison" (utils/s->url-slug "JSON vs. XML: Comparison")))))
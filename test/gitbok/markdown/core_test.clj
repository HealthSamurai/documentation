(ns gitbok.markdown.core-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [gitbok.markdown.core :as markdown]
   [nextjournal.markdown :as md]
   [nextjournal.markdown.transform :as transform]))

(defn parse-test-markdown
  "Helper to parse markdown with custom renderers for testing"
  [text]
  (let [parsed (md/parse text)
        ;; Use minimal context and filepath for testing
        custom-renderers (markdown/renderers {} "test.md")]
    (transform/->hiccup custom-renderers parsed)))

(deftest formula-latex-detection-test
  (testing "LaTeX formulas with backslash commands should be detected"
    (let [rendered (parse-test-markdown "Text $r \\le s$ here")]
      (is (some? (re-find #"katex-inline" (str rendered)))
          "Formula with \\le should be rendered as LaTeX")))

  (testing "LaTeX formulas with supset should be detected"
    (let [rendered (parse-test-markdown "Formula $(s, S) \\supset (r, R)$ works")]
      (is (some? (re-find #"katex-inline" (str rendered)))
          "Formula with \\supset should be rendered as LaTeX")))

  (testing "LaTeX formulas with math notation (x, y) should be detected"
    (let [rendered (parse-test-markdown "Let $(s, S)$ be the range")]
      (is (some? (re-find #"katex-inline" (str rendered)))
          "Formula with (x, y) pattern should be rendered as LaTeX")))

  (testing "LaTeX formulas with superscript should be detected"
    (let [rendered (parse-test-markdown "Formula $x^2$ is quadratic")]
      (is (some? (re-find #"katex-inline" (str rendered)))
          "Formula with ^ should be rendered as LaTeX")))

  (testing "LaTeX formulas with subscript should be detected"
    (let [rendered (parse-test-markdown "Formula $a_i$ is indexed")]
      (is (some? (re-find #"katex-inline" (str rendered)))
          "Formula with _ should be rendered as LaTeX")))

  (testing "LaTeX formulas with braces should be detected"
    (let [rendered (parse-test-markdown "Formula $x_{max}$ has braces")]
      (is (some? (re-find #"katex-inline" (str rendered)))
          "Formula with {} should be rendered as LaTeX"))))

(deftest fhir-operations-not-latex-test
  (testing "FHIR operations should NOT be rendered as LaTeX"
    (let [rendered (parse-test-markdown "Use the $export endpoint")]
      (is (nil? (re-find #"katex-inline" (str rendered)))
          "$export should not be rendered as LaTeX")
      (is (some? (re-find #"export" (str rendered)))
          "$export should appear as plain text")))

  (testing "$import should not be LaTeX"
    (let [rendered (parse-test-markdown "The $import operation")]
      (is (nil? (re-find #"katex-inline" (str rendered)))
          "$import should not be rendered as LaTeX")))

  (testing "$everything should not be LaTeX"
    (let [rendered (parse-test-markdown "Patient $everything works")]
      (is (nil? (re-find #"katex-inline" (str rendered)))
          "$everything should not be rendered as LaTeX")))

  (testing "$dump should not be LaTeX"
    (let [rendered (parse-test-markdown "Use $dump to backup")]
      (is (nil? (re-find #"katex-inline" (str rendered)))
          "$dump should not be rendered as LaTeX")))

  (testing "$load should not be LaTeX"
    (let [rendered (parse-test-markdown "Use $load to restore")]
      (is (nil? (re-find #"katex-inline" (str rendered)))
          "$load should not be rendered as LaTeX"))))

(deftest mixed-content-test
  (testing "Mixed LaTeX and FHIR operations in same document"
    (let [rendered (parse-test-markdown "Use `$export` endpoint. Formula: $r \\le s$.")]
      (is (some? (re-find #"katex-inline" (str rendered)))
          "Should contain LaTeX rendering for $r \\le s$")
      (is (some? (re-find #"export" (str rendered)))
          "$export in code should appear as code text")))

  (testing "Multiple formulas in same paragraph"
    (let [rendered (parse-test-markdown "Range $(s, S)$ and value $r \\le s$ are defined.")]
      (is (= 2 (count (re-seq #"katex-inline" (str rendered))))
          "Should have two LaTeX formulas")))

  (testing "FHIR operations should be plain text"
    (let [rendered (parse-test-markdown "Use the $export and $import operations")]
      (is (nil? (re-find #"katex-inline" (str rendered)))
          "FHIR operations should be plain text"))))

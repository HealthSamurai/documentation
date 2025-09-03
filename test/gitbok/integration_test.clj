(ns gitbok.integration-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [clojure.string :as str]
   [gitbok.markdown.core :as md-core]
   [hiccup2.core]))

(deftest test-gitbook-code-with-empty-lines
  (testing "GitBook code blocks with empty lines should render correctly"
    (let [content "{% code title=\"example.json\" %}
```json
{
  \"field1\": \"value1\",

  \"field2\": \"value2\"
}
```
{% endcode %}"
          context {}
          ;; Process through the full pipeline as the server does
          parsed-map (md-core/parse-markdown-content context ["test.md" content])
          result-hiccup (md-core/render-md context "test.md" (:parsed parsed-map))
          result-html (str (hiccup2.core/html result-hiccup))]

      ;; Check that we don't have nested <pre> tags
      (is (= 1 (count (re-seq #"<pre" result-html)))
          "Should have exactly one <pre> tag")

      ;; Check that quotes are properly escaped (not double-escaped)
      (is (re-find #"&quot;" result-html)
          "Should have escaped quotes")
      (is (not (re-find #"&amp;quot;" result-html))
          "Should not have double-escaped quotes")

      ;; Check that the content is present
      (is (str/includes? result-html "field1"))
      (is (str/includes? result-html "field2"))

      ;; Check that markers are removed
      (when (str/includes? result-html "%%GITBOOK_EMPTY_LINE%%")
        (println "HTML still contains markers:")
        (let [idx (.indexOf result-html "%%GITBOOK_EMPTY_LINE%%")]
          (println (subs result-html (max 0 (- idx 50)) (min (count result-html) (+ idx 80))))))
      (is (not (str/includes? result-html "%%GITBOOK_EMPTY_LINE%%"))
          "Should not have markers in final output")

      ;; Check that empty lines are preserved (as newlines in the code)
      (let [code-match (re-find #"<code[^>]*>(.*?)</code>" result-html)]
        (when code-match
          (let [code-content (second code-match)]
            (is (re-find #"value1.*\n\n.*value2" code-content)
                "Should preserve empty line between values")))))))
(ns gitbok.debug-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [clojure.string :as str]
   [gitbok.markdown.core :as md-core]
   [gitbok.markdown.widgets.gitbook-code :as gitbook-code]
   [hiccup2.core]))

(deftest test-debug-pipeline
  (testing "Debug the rendering pipeline step by step"
    (let [content "{% code title=\"my-patient-profile.json\" %}
```json
{
    // Type of the FHIR resource.
    \"resourceType\": \"StructureDefinition\",
    
    // How the type relates to the baseDefinition.
    \"derivation\": \"constraint\"
}
```
{% endcode %}"
          context {}

          ;; Step 1: Check what hack-gitbook-code produces
          hacked (gitbook-code/hack-gitbook-code
                  context
                  "test.md"
                  md-core/parse-markdown-content
                  md-core/render-md
                  content)]

      (println "\n=== STEP 1: After hack-gitbook-code ===")
      (println "Content type:" (type hacked))
      (println "Has markers:" (str/includes? hacked "%%GITBOOK_EMPTY_LINE%%"))
      (println "Number of <div class=\"code-gitbook\":" (count (re-seq #"<div[^>]*code-gitbook" hacked)))
      (println "Number of <pre tags:" (count (re-seq #"<pre" hacked)))
      (println "Has &amp;quot;:" (boolean (re-find #"&amp;quot;" hacked)))
      (println "Has &quot;:" (boolean (re-find #"&quot;" hacked)))

      ;; Show a snippet
      (when-let [snippet (re-find #"(?s)<div[^>]*code-gitbook.{0,500}" hacked)]
        (println "\nFirst 500 chars after code-gitbook div:")
        (println snippet))

      ;; Step 2: Parse and render the full content
      (let [parsed-map (md-core/parse-markdown-content context ["test.md" hacked])
            result-hiccup (md-core/render-md context "test.md" (:parsed parsed-map))
            result-html (str (hiccup2.core/html result-hiccup))]

        (println "\n=== STEP 2: After full pipeline ===")
        (println "Number of <pre tags:" (count (re-seq #"<pre" result-html)))
        (println "Has &amp;quot;:" (boolean (re-find #"&amp;quot;" result-html)))
        (println "Has markers:" (str/includes? result-html "%%GITBOOK_EMPTY_LINE%%"))

        ;; Check the assertions
        (is (= 1 (count (re-seq #"<pre" result-html)))
            "Should have exactly one <pre> tag")
        (is (not (re-find #"&amp;quot;" result-html))
            "Should not have double-escaped quotes")))))
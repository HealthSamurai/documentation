(ns gitbok.markdown.widgets.gitbook-code-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [clojure.string :as str]
   [gitbok.markdown.widgets.gitbook-code :as gitbook-code]
   [gitbok.markdown.core :as md-core]
   [hiccup2.core]))

(deftest test-parse-code-blocks
  (testing "Basic code block parsing"
    (let [content "{% code title=\"example.json\" %}
```json
{
  \"key\": \"value\"
}
```
{% endcode %}"
          blocks (#'gitbok.markdown.widgets.gitbook-code/parse-code-blocks content)]
      (is (= 1 (count blocks)))
      (is (= "example.json" (get-in (first blocks) [:attributes :title])))
      (is (str/includes? (:content (first blocks)) "\"key\": \"value\""))))

  (testing "Multiple code blocks"
    (let [content "Text before
{% code title=\"first.js\" %}
```javascript
console.log('first');
```
{% endcode %}
Text between
{% code title=\"second.py\" %}
```python
print('second')
```
{% endcode %}
Text after"
          blocks (#'gitbok.markdown.widgets.gitbook-code/parse-code-blocks content)]
      (is (= 2 (count blocks)))
      (is (= "first.js" (get-in (first blocks) [:attributes :title])))
      (is (= "second.py" (get-in (second blocks) [:attributes :title])))))

  (testing "Code block without title"
    (let [content "{% code %}
```bash
echo 'hello'
```
{% endcode %}"
          blocks (#'gitbok.markdown.widgets.gitbook-code/parse-code-blocks content)]
      (is (= 1 (count blocks)))
      (is (nil? (get-in (first blocks) [:attributes :title])))
      (is (str/includes? (:content (first blocks)) "echo 'hello'")))))

(deftest test-code-with-empty-lines
  (testing "Code without empty lines renders correctly"
    (let [content "{% code title=\"no-empty.json\" %}
```json
{
  \"field1\": \"value1\",
  \"field2\": \"value2\"
}
```
{% endcode %}"
          context {}
          ;; Test the full rendering pipeline
          parsed-map (md-core/parse-markdown-content context ["test.md" content])
          result-hiccup (md-core/render-md context "test.md" (:parsed parsed-map))
          result (str (hiccup2.core/html result-hiccup))]
      (is (str/includes? result "<pre"))
      (is (str/includes? result "<code"))
      (is (str/includes? result "field1"))
      (is (str/includes? result "field2"))
      ;; Should have exactly one <pre> tag
      (is (= 1 (count (re-seq #"<pre" result))))
      ;; Should not contain nested <pre> tags
      (is (not (re-find #"<pre.*<pre" result)))))

  (testing "Code with one empty line"
    (let [content "{% code title=\"one-empty.json\" %}
```json
{
  \"field1\": \"value1\",

  \"field2\": \"value2\"
}
```
{% endcode %}"
          context {}
          ;; Test the full rendering pipeline
          parsed-map (md-core/parse-markdown-content context ["test.md" content])
          result-hiccup (md-core/render-md context "test.md" (:parsed parsed-map))
          result (str (hiccup2.core/html result-hiccup))]
      (is (str/includes? result "field1"))
      (is (str/includes? result "field2"))
      ;; Main assertions - these should pass after fix
      (is (= 1 (count (re-seq #"<pre" result)))
          "Should have exactly one <pre> tag")
      (is (not (re-find #"<pre.*<pre" result))
          "Should not have nested <pre> tags")
      ;; Check for double-escaped quotes
      (is (not (re-find #"&amp;quot;" result))
          "Should not have double-escaped quotes")))

  (testing "Code with two consecutive empty lines"
    (let [content "{% code title=\"two-empty.json\" %}
```json
{
  \"field1\": \"value1\",


  \"field2\": \"value2\"
}
```
{% endcode %}"
          context {}
          parsed-map (md-core/parse-markdown-content context ["test.md" content])
          result-hiccup (md-core/render-md context "test.md" (:parsed parsed-map))
          result (str (hiccup2.core/html result-hiccup))]
      (is (str/includes? result "field1"))
      (is (str/includes? result "field2"))
      (is (= 1 (count (re-seq #"<pre" result)))
          "Should have exactly one <pre> tag")
      (is (not (re-find #"<pre.*<pre" result))
          "Should not have nested <pre> tags")
      (is (not (re-find #"&amp;quot;" result))
          "Should not have double-escaped quotes")))

  (testing "Code with three empty lines"
    (let [content "{% code title=\"three-empty.json\" %}
```json
{
  \"field1\": \"value1\",



  \"field2\": \"value2\"
}
```
{% endcode %}"
          context {}
          parsed-map (md-core/parse-markdown-content context ["test.md" content])
          result-hiccup (md-core/render-md context "test.md" (:parsed parsed-map))
          result (str (hiccup2.core/html result-hiccup))]
      (is (str/includes? result "field1"))
      (is (str/includes? result "field2"))
      (is (= 1 (count (re-seq #"<pre" result)))
          "Should have exactly one <pre> tag")))

  (testing "Code with empty lines at start and end"
    (let [content "{% code title=\"edges.json\" %}
```json

{
  \"field\": \"value\"
}

```
{% endcode %}"
          context {}
          parsed-map (md-core/parse-markdown-content context ["test.md" content])
          result-hiccup (md-core/render-md context "test.md" (:parsed parsed-map))
          result (str (hiccup2.core/html result-hiccup))]
      (is (str/includes? result "field"))
      (is (= 1 (count (re-seq #"<pre" result)))
          "Should have exactly one <pre> tag")))

  (testing "Code with multiple empty lines in different places"
    (let [content "{% code title=\"complex.js\" %}
```javascript
function example() {

  // First comment
  const a = 1;


  // Second comment  
  const b = 2;

  return a + b;
}
```
{% endcode %}"
          context {}
          parsed-map (md-core/parse-markdown-content context ["test.md" content])
          result-hiccup (md-core/render-md context "test.md" (:parsed parsed-map))
          result (str (hiccup2.core/html result-hiccup))]
      (is (str/includes? result "function example"))
      (is (str/includes? result "First comment"))
      (is (str/includes? result "Second comment"))
      (is (= 1 (count (re-seq #"<pre" result)))
          "Should have exactly one <pre> tag")
      (is (not (re-find #"&amp;quot;" result))
          "Should not have double-escaped quotes"))))

(deftest test-no-invisible-characters
  (testing "Result should not contain non-breaking spaces or markers"
    (let [content "{% code title=\"test.json\" %}
```json
{
  \"field1\": \"value1\",

  \"field2\": \"value2\"
}
```
{% endcode %}"
          context {}
          ;; Test through the full markdown pipeline
          parsed-map (md-core/parse-markdown-content context ["test.md" content])
          result-hiccup (md-core/render-md context "test.md" (:parsed parsed-map))
          result (str (hiccup2.core/html result-hiccup))]
      ;; After fix, should not contain U+00A0 (non-breaking space)
      (is (not (str/includes? result "\u00A0"))
          "Should not contain non-breaking spaces")
      ;; Should not contain our temporary marker either
      (is (not (str/includes? result "%%GITBOOK_EMPTY_LINE%%"))
          "Should not contain temporary markers"))))

(deftest test-debug-escaping
  (testing "Debug: Check hack-gitbook-code output directly"
    (let [content "{% code title=\"test.json\" %}
```json
{
  \"key1\": \"value1\",

  \"key2\": \"value2\"
}
```
{% endcode %}"
          context {}
          ;; Just call hack-gitbook-code
          hacked (gitbook-code/hack-gitbook-code
                  context
                  "test.md"
                  md-core/parse-markdown-content
                  md-core/render-md
                  content)]

      (println "\n=== After hack-gitbook-code ===")
      (println "Has %%GITBOOK_EMPTY_LINE%%:" (str/includes? hacked "%%GITBOOK_EMPTY_LINE%%"))
      (println "Has nested <pre>:" (boolean (re-find #"<pre.*<pre" hacked)))
      (println "Has &amp;quot;:" (boolean (re-find #"&amp;quot;" hacked)))

      ;; Show the HTML
      (println "\nHTML output:")
      (println hacked)

      ;; These should pass for hack-gitbook-code alone
      (is (not (str/includes? hacked "%%GITBOOK_EMPTY_LINE%%"))
          "Should not have markers in final output")
      (is (= 1 (count (re-seq #"<pre" hacked)))
          "Should have exactly 1 <pre> tag"))))

(deftest test-real-world-examples
  (testing "Example from the problematic file"
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
          parsed-map (md-core/parse-markdown-content context ["test.md" content])
          result-hiccup (md-core/render-md context "test.md" (:parsed parsed-map))
          result (str (hiccup2.core/html result-hiccup))]
      (is (str/includes? result "resourceType"))
      (is (str/includes? result "StructureDefinition"))
      (is (str/includes? result "derivation"))
      (is (str/includes? result "constraint"))
      (is (= 1 (count (re-seq #"<pre" result)))
          "Should have exactly one <pre> tag")
      (is (not (re-find #"&amp;quot;" result))
          "Should not have double-escaped quotes")
      ;; Check that quotes are properly escaped once
      (is (re-find #"&quot;resourceType&quot;" result)
          "Should have properly escaped quotes")
      (is (not (re-find #"&amp;quot;resourceType&amp;quot;" result))
          "Should not have double-escaped quotes")))

  (testing "Complex JSON with comments and empty lines"
    (let [content "{% code title=\"package.json\" %}
```json
{
    // The globally unique identifier of the package.
    // A package name consists of two or more namespaces separated by a dot.
    \"name\": \"my.fhir.package.name\",

    // The version of the package (SemVer).
    \"version\": \"1.0.0\",

    // The description of the package.
    \"description\": \"My FHIR NPM Package\",

    // The list of package dependencies.
    \"dependencies\": {
        \"hl7.fhir.r4.core\": \"4.0.1\",
        \"hl7.fhir.us.core\": \"5.0.1\"
    }
}
```
{% endcode %}"
          context {}
          parsed-map (md-core/parse-markdown-content context ["test.md" content])
          result-hiccup (md-core/render-md context "test.md" (:parsed parsed-map))
          result (str (hiccup2.core/html result-hiccup))]
      (is (str/includes? result "name"))
      (is (str/includes? result "version"))
      (is (str/includes? result "description"))
      (is (str/includes? result "dependencies"))
      (is (= 1 (count (re-seq #"<pre" result)))
          "Should have exactly one <pre> tag")
      (is (not (re-find #"&amp;quot;" result))
          "Should not have double-escaped quotes")
      ;; Check proper quote escaping
      (is (re-find #"&quot;name&quot;" result)
          "Should have properly escaped quotes for 'name'")
      (is (re-find #"&quot;my\.fhir\.package\.name&quot;" result)
          "Should have properly escaped quotes for value")))

  (testing "Check that HTML structure is correct"
    (let [content "{% code title=\"simple.json\" %}
```json
{
    \"key\": \"value\"
}
```
{% endcode %}"
          context {}
          parsed-map (md-core/parse-markdown-content context ["test.md" content])
          result-hiccup (md-core/render-md context "test.md" (:parsed parsed-map))
          result (str (hiccup2.core/html result-hiccup))]
      ;; Check the structure
      (is (re-find #"<div[^>]*code-gitbook" result)
          "Should have gitbook code wrapper div")
      (is (re-find #"<pre[^>]*><code>" result)
          "Should have <pre><code> structure")
      (is (re-find #"</code></pre>" result)
          "Should have closing </code></pre>")
      ;; Should not have nested pre tags
      (is (not (re-find #"<pre[^>]*>.*<pre" result))
          "Should not have nested <pre> tags")
      ;; Check quote escaping - should be &quot; not &amp;quot;
      (is (= 4 (count (re-seq #"&quot;" result)))
          "Should have exactly 4 escaped quotes")
      (is (= 0 (count (re-seq #"&amp;quot;" result)))
          "Should have no double-escaped quotes"))))
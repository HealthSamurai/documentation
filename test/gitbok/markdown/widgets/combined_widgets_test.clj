(ns gitbok.markdown.widgets.combined-widgets-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [clojure.string :as str]
   [gitbok.markdown.core :as md-core]
   [hiccup2.core]))

(deftest test-tabs-with-code-blocks
  (testing "Tabs with code blocks containing empty lines"
    (let [content "{% tabs %}
{% tab title=\"First Tab\" %}
Here's some JSON with empty lines:

{% code title=\"example.json\" %}
```json
{
    // First comment
    \"field1\": \"value1\",
    
    // Second comment after empty line
    \"field2\": \"value2\",
    
    
    // Third comment after two empty lines
    \"field3\": \"value3\"
}
```
{% endcode %}
{% endtab %}

{% tab title=\"Second Tab\" %}
Here's some JavaScript:

{% code title=\"example.js\" %}
```javascript
function example() {

    // First comment
    const a = 1;

    // Second comment
    const b = 2;


    // Third comment after two empty lines
    return a + b;
}
```
{% endcode %}
{% endtab %}
{% endtabs %}"
          context {}
          parsed-map (md-core/parse-markdown-content context ["test.md" content])
          result-hiccup (md-core/render-md context "test.md" (:parsed parsed-map))
          result (str (hiccup2.core/html result-hiccup))]
      
      ;; Check for content
      (is (str/includes? result "field1"))
      (is (str/includes? result "field2"))
      (is (str/includes? result "field3"))
      (is (str/includes? result "function example"))
      
      ;; Check for no double escaping
      (is (not (re-find #"&amp;quot;" result))
          "Should not have double-escaped quotes in tabs")
      
      ;; Check for no markers
      (is (not (str/includes? result "%%%NL%%%"))
          "Should not have newline markers in tabs")
      (is (not (str/includes? result "%%GITBOOK_EMPTY_LINE%%"))
          "Should not have empty line markers in tabs"))))

(deftest test-stepper-with-code-blocks
  (testing "Stepper with code blocks containing empty lines"
    (let [content "{% stepper %}
{% step %}
## Step 1: Create the JSON structure

First, create a JSON file with proper formatting:

{% code title=\"step1.json\" %}
```json
{
    \"name\": \"my-package\",
    
    // Add version after name
    \"version\": \"1.0.0\",
    
    
    // Dependencies come last
    \"dependencies\": {}
}
```
{% endcode %}
{% endstep %}

{% step %}
## Step 2: Add dependencies

Now add the required dependencies:

{% code title=\"step2.json\" %}
```json
{
    \"name\": \"my-package\",
    \"version\": \"1.0.0\",
    
    \"dependencies\": {
        \"package1\": \"^1.0.0\",
        
        // Add more as needed
        \"package2\": \"^2.0.0\"
    }
}
```
{% endcode %}
{% endstep %}
{% endstepper %}"
          context {}
          parsed-map (md-core/parse-markdown-content context ["test.md" content])
          result-hiccup (md-core/render-md context "test.md" (:parsed parsed-map))
          result (str (hiccup2.core/html result-hiccup))]
      
      ;; Check for content
      (is (str/includes? result "my-package"))
      (is (str/includes? result "version"))
      (is (str/includes? result "dependencies"))
      (is (str/includes? result "package1"))
      (is (str/includes? result "package2"))
      
      ;; Check for no double escaping
      (is (not (re-find #"&amp;quot;" result))
          "Should not have double-escaped quotes in stepper")
      
      ;; Check for no markers
      (is (not (str/includes? result "%%%NL%%%"))
          "Should not have newline markers in stepper")
      (is (not (str/includes? result "%%GITBOOK_EMPTY_LINE%%"))
          "Should not have empty line markers in stepper"))))

(deftest test-hints-with-code-blocks
  (testing "Hints (info blocks) with code blocks containing empty lines"
    (let [content "{% hint style=\"info\" %}
**Important Configuration**

Here's an example configuration with proper spacing:

{% code title=\"config.json\" %}
```json
{
    // Database configuration
    \"database\": {
        \"host\": \"localhost\",
        
        // Port is optional
        \"port\": 5432,
        
        
        // Credentials section
        \"credentials\": {
            \"user\": \"admin\",
            \"password\": \"secret\"
        }
    }
}
```
{% endcode %}
{% endhint %}

{% hint style=\"warning\" %}
**Warning: Check your syntax**

Make sure your JSON is valid:

{% code title=\"invalid.json\" %}
```json
{
    \"field1\": \"value1\"
    
    // Missing comma above!
    \"field2\": \"value2\"
}
```
{% endcode %}
{% endhint %}"
          context {}
          parsed-map (md-core/parse-markdown-content context ["test.md" content])
          result-hiccup (md-core/render-md context "test.md" (:parsed parsed-map))
          result (str (hiccup2.core/html result-hiccup))]
      
      ;; Check for content
      (is (str/includes? result "database"))
      (is (str/includes? result "host"))
      (is (str/includes? result "localhost"))
      (is (str/includes? result "credentials"))
      (is (str/includes? result "field1"))
      (is (str/includes? result "field2"))
      
      ;; Check for no double escaping
      (is (not (re-find #"&amp;quot;" result))
          "Should not have double-escaped quotes in hints")
      
      ;; Check for no markers
      (is (not (str/includes? result "%%%NL%%%"))
          "Should not have newline markers in hints")
      (is (not (str/includes? result "%%GITBOOK_EMPTY_LINE%%"))
          "Should not have empty line markers in hints"))))

(deftest test-nested-widgets-with-code
  (testing "Nested widgets (tabs inside hints) with code blocks"
    (let [content "{% hint style=\"success\" %}
## Configuration Examples

{% tabs %}
{% tab title=\"Development\" %}
{% code title=\"dev.json\" %}
```json
{
    \"env\": \"development\",
    
    \"debug\": true,
    
    
    \"logging\": {
        \"level\": \"debug\"
    }
}
```
{% endcode %}
{% endtab %}

{% tab title=\"Production\" %}
{% code title=\"prod.json\" %}
```json
{
    \"env\": \"production\",
    
    \"debug\": false,
    
    
    \"logging\": {
        \"level\": \"error\"
    }
}
```
{% endcode %}
{% endtab %}
{% endtabs %}
{% endhint %}"
          context {}
          parsed-map (md-core/parse-markdown-content context ["test.md" content])
          result-hiccup (md-core/render-md context "test.md" (:parsed parsed-map))
          result (str (hiccup2.core/html result-hiccup))]
      
      ;; Check for content from both tabs
      (is (str/includes? result "development"))
      (is (str/includes? result "production"))
      (is (str/includes? result "debug"))
      (is (str/includes? result "logging"))
      
      ;; Check for no double escaping
      (is (not (re-find #"&amp;quot;" result))
          "Should not have double-escaped quotes in nested widgets")
      
      ;; Check for no markers
      (is (not (str/includes? result "%%%NL%%%"))
          "Should not have newline markers in nested widgets")
      (is (not (str/includes? result "%%GITBOOK_EMPTY_LINE%%"))
          "Should not have empty line markers in nested widgets"))))

(deftest test-all-widgets-no-nested-pre-tags
  (testing "All widgets should not create nested <pre> tags"
    (let [test-cases
          [;; Simple code block
           "{% code title=\"test.json\" %}
```json
{
    \"a\": 1,
    
    \"b\": 2
}
```
{% endcode %}"
           
           ;; Code in tabs
           "{% tabs %}
{% tab title=\"Tab\" %}
{% code title=\"test.json\" %}
```json
{
    \"a\": 1,
    
    \"b\": 2
}
```
{% endcode %}
{% endtab %}
{% endtabs %}"
           
           ;; Code in stepper
           "{% stepper %}
{% step %}
{% code title=\"test.json\" %}
```json
{
    \"a\": 1,
    
    \"b\": 2
}
```
{% endcode %}
{% endstep %}
{% endstepper %}"
           
           ;; Code in hint
           "{% hint style=\"info\" %}
{% code title=\"test.json\" %}
```json
{
    \"a\": 1,
    
    \"b\": 2
}
```
{% endcode %}
{% endhint %}"]]
      
      (doseq [content test-cases]
        (let [context {}
              parsed-map (md-core/parse-markdown-content context ["test.md" content])
              result-hiccup (md-core/render-md context "test.md" (:parsed parsed-map))
              result (str (hiccup2.core/html result-hiccup))
              pre-count (count (re-seq #"<pre" result))]
          
          ;; Each test case should have exactly 1 <pre> tag
          (is (= 1 pre-count)
              (str "Should have exactly 1 <pre> tag, but found " pre-count 
                   " in content: " (subs content 0 (min 50 (count content)))))
          
          ;; No nested pre tags
          (is (not (re-find #"<pre[^>]*>.*<pre" result))
              "Should not have nested <pre> tags"))))))
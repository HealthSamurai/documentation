(ns gitbok.markdown.widgets.stepper-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [clojure.string :as str]
   [gitbok.markdown.widgets.stepper :as stepper]
   [gitbok.markdown.core :as md-core]
   [hiccup2.core]))

(deftest test-parse-steppers
  (testing "Basic stepper parsing with **text** format"
    (let [content "{% stepper %}
{% step %}
**Step 1**
Content 1
{% endstep %}
{% step %}
**Step 2**
Content 2
{% endstep %}
{% endstepper %}"
          result (stepper/parse-steppers content)]
      (is (= 1 (count result)))
      (is (= 2 (count (:steps (first result)))))
      (is (str/includes? (-> result first :steps first :text) "Step 1"))
      (is (str/includes? (-> result first :steps second :text) "Step 2"))))

  (testing "Multiple steppers in content"
    (let [content "Text before
{% stepper %}
{% step %}
Step A
{% endstep %}
{% endstepper %}
Text between
{% stepper %}
{% step %}
Step B
{% endstep %}
{% endstepper %}
Text after"
          result (stepper/parse-steppers content)]
      (is (= 2 (count result)))
      (is (str/includes? (-> result first :steps first :text) "Step A"))
      (is (str/includes? (-> result second :steps first :text) "Step B"))))

  (testing "Empty stepper"
    (let [content "{% stepper %}{% endstepper %}"
          result (stepper/parse-steppers content)]
      (is (= 1 (count result)))
      (is (= 0 (count (:steps (first result))))))))

(deftest test-stepper-with-nested-widgets
  (testing "Stepper with tabs inside"
    (let [content "{% stepper %}
{% step %}
**Setup Environment**
Here are platform-specific instructions:
{% tabs %}
{% tab title=\"macOS\" %}
```bash
brew install node
```
{% endtab %}
{% tab title=\"Linux\" %}
```bash
apt-get install nodejs
```
{% endtab %}
{% endtabs %}
{% endstep %}
{% step %}
**Verify Installation**
Run this command:
```bash
node --version
```
{% endstep %}
{% endstepper %}"
          context {}
          result (stepper/hack-stepper
                  context
                  "test.md"
                  md-core/parse-markdown-content
                  md-core/render-md
                  content)]
      (is (str/includes? result "stepper-container"))
      (is (str/includes? result "Setup Environment"))
      (is (str/includes? result "bg-surface-subtle"))
      (is (str/includes? result "data-tab"))
      (is (str/includes? result "macOS"))
      (is (str/includes? result "Linux"))
      (is (str/includes? result "brew install"))
      (is (str/includes? result "apt-get install"))))

  (testing "Complex nesting: stepper -> tabs -> code blocks"
    (let [content "{% stepper %}
{% step %}
**Database Setup**
Choose your database:
{% tabs %}
{% tab title=\"PostgreSQL\" %}
Install PostgreSQL:
```bash
docker run -d \\
  --name postgres \\
  -e POSTGRES_PASSWORD=secret \\
  -p 5432:5432 \\
  postgres:latest
```

Configure connection:
```json
{
  \"db\": {
    \"host\": \"localhost\",
    \"port\": 5432,
    \"password\": \"secret\"
  }
}
```
{% endtab %}
{% tab title=\"MySQL\" %}
Install MySQL:
```bash
docker run -d \\
  --name mysql \\
  -e MYSQL_ROOT_PASSWORD=secret \\
  -p 3306:3306 \\
  mysql:latest
```

Configure connection:
```json
{
  \"db\": {
    \"host\": \"localhost\",
    \"port\": 3306,
    \"password\": \"secret\"
  }
}
```
{% endtab %}
{% endtabs %}
{% endstep %}
{% step %}
**Test Connection**
```javascript
const db = require('./db');
db.connect().then(() => {
  console.log('Connected!');
});
```
{% endstep %}
{% endstepper %}"
          context {}
          result (stepper/hack-stepper
                  context
                  "test.md"
                  md-core/parse-markdown-content
                  md-core/render-md
                  content)]
      (is (str/includes? result "stepper-container"))
      (is (str/includes? result "Database Setup"))
      (is (str/includes? result "Test Connection"))
      (is (str/includes? result "PostgreSQL"))
      (is (str/includes? result "MySQL"))
      (is (str/includes? result "docker run"))
      (is (str/includes? result "postgres:latest"))
      (is (str/includes? result "mysql:latest"))
      (is (str/includes? result "db.connect"))
      (is (str/includes? result "5432"))
      (is (str/includes? result "3306"))))

  (testing "Stepper with gitbook code blocks"
    (let [content "{% stepper %}
{% step %}
**API Example**
{% code %}
```http
GET /api/users HTTP/1.1
Host: example.com
Authorization: Bearer token123
```
{% endcode %}
{% endstep %}
{% endstepper %}"
          context {}
          result (stepper/hack-stepper
                  context
                  "test.md"
                  md-core/parse-markdown-content
                  md-core/render-md
                  content)]
      (is (str/includes? result "stepper-container"))
      (is (str/includes? result "API Example")))))

(deftest test-render-stepper
  (testing "HTML structure of rendered stepper with ### headers"
    (let [content "{% stepper %}
{% step %}
### First Step
This is the first step.
{% endstep %}
{% step %}
### Second Step
This is the second step.
{% endstep %}
{% step %}
### Third Step
This is the third step.
{% endstep %}
{% endstepper %}"
          context {}
          result (stepper/hack-stepper
                  context
                  "test.md"
                  md-core/parse-markdown-content
                  md-core/render-md
                  content)]
      (is (str/includes? result ">1</div>"))
      (is (str/includes? result ">2</div>"))
      (is (str/includes? result ">3</div>"))
      (is (str/includes? result "rounded-full"))
      (is (str/includes? result "bg-brand"))
      (is (str/includes? result "text-white"))
      (is (str/includes? result "pb-0"))
      (is (str/includes? result "pb-6"))
      (is (str/includes? result "First Step"))
      (is (str/includes? result "Second Step"))
      (is (str/includes? result "Third Step"))))

  (testing "Step title extraction with mixed formats"
    (let [content "{% stepper %}
{% step %}
### Step with Header
Content here
{% endstep %}
{% step %}
**Bold Header**
Content here
{% endstep %}
{% step %}
Just text without header
More content
{% endstep %}
{% endstepper %}"
          context {}
          result (stepper/hack-stepper
                  context
                  "test.md"
                  md-core/parse-markdown-content
                  md-core/render-md
                  content)]
      (is (str/includes? result "Step with Header"))
      (is (str/includes? result "Bold Header"))
      (is (str/includes? result "Just text without header"))
      (is (not (str/includes? result "### Step with Header")))
      (is (not (str/includes? result "**Bold Header**"))))))

(deftest test-stepper-edge-cases
  (testing "Empty stepper"
    (let [content "{% stepper %}{% endstepper %}"
          context {}
          result (stepper/hack-stepper 
                  context 
                  "test.md"
                  md-core/parse-markdown-content
                  md-core/render-md
                  content)]
      (is (string? result))
      (is (str/includes? result "stepper-container"))))
  
  (testing "Stepper with empty steps"
    (let [content "{% stepper %}
{% step %}
{% endstep %}
{% step %}
{% endstep %}
{% endstepper %}"
          context {}
          result (stepper/hack-stepper 
                  context 
                  "test.md"
                  md-core/parse-markdown-content
                  md-core/render-md
                  content)]
      (is (string? result))
      (is (str/includes? result "stepper-container"))
      (is (str/includes? result ">1</div>"))
      (is (str/includes? result ">2</div>"))))
  
  (testing "Stepper with only whitespace in steps"
    (let [content "{% stepper %}
{% step %}
   
   
{% endstep %}
{% step %}
\t\t
{% endstep %}
{% endstepper %}"
          context {}
          result (stepper/hack-stepper 
                  context 
                  "test.md"
                  md-core/parse-markdown-content
                  md-core/render-md
                  content)]
      (is (string? result))
      (is (str/includes? result "stepper-container"))))
  
  (testing "Nested steppers (should handle gracefully)"
    (let [content "{% stepper %}
{% step %}
Outer step 1
{% stepper %}
{% step %}
Inner step - should be treated as text
{% endstep %}
{% endstepper %}
{% endstep %}
{% endstepper %}"
          context {}
          result (stepper/hack-stepper 
                  context 
                  "test.md"
                  md-core/parse-markdown-content
                  md-core/render-md
                  content)]
      (is (string? result))
      (is (str/includes? result "stepper-container"))))
  
  (testing "Malformed stepper tags"
    (let [content "{% stepper %}
{% step %}
Content without endstep
{% stepper %}"
          context {}
          result (stepper/hack-stepper 
                  context 
                  "test.md"
                  md-core/parse-markdown-content
                  md-core/render-md
                  content)]
      (is (string? result))))
  
  (testing "Step with special characters in title"
    (let [content "{% stepper %}
{% step %}
### Step with <special> & \"characters\" 'test'
Content here
{% endstep %}
{% step %}
**Step with $pecial ch@rs!**
More content
{% endstep %}
{% endstepper %}"
          context {}
          result (stepper/hack-stepper 
                  context 
                  "test.md"
                  md-core/parse-markdown-content
                  md-core/render-md
                  content)]
      (is (string? result))
      (is (str/includes? result "stepper-container"))
      (is (str/includes? result "special"))
      (is (str/includes? result "characters"))))
  
  (testing "Very long step title"
    (let [long-title (apply str (repeat 200 "a"))
          content (str "{% stepper %}
{% step %}
### " long-title "
Content
{% endstep %}
{% endstepper %}")
          context {}
          result (stepper/hack-stepper 
                  context 
                  "test.md"
                  md-core/parse-markdown-content
                  md-core/render-md
                  content)]
      (is (string? result))
      (is (str/includes? result "stepper-container"))))
  
  (testing "Step with multiple ### headers (should use first)"
    (let [content "{% stepper %}
{% step %}
### First Header
### Second Header
### Third Header
Content here
{% endstep %}
{% endstepper %}"
          context {}
          result (stepper/hack-stepper 
                  context 
                  "test.md"
                  md-core/parse-markdown-content
                  md-core/render-md
                  content)]
      (is (string? result))
      (is (str/includes? result "First Header"))
      (is (not (str/includes? result "### First Header")))))
  
  (testing "Mixed bold and normal text in first line"
    (let [content "{% stepper %}
{% step %}
**Bold** and normal text
Content here
{% endstep %}
{% endstepper %}"
          context {}
          result (stepper/hack-stepper 
                  context 
                  "test.md"
                  md-core/parse-markdown-content
                  md-core/render-md
                  content)]
      (is (string? result))
      (is (str/includes? result "stepper-container"))))
  
  (testing "Unicode and emoji in steps"
    (let [content "{% stepper %}
{% step %}
### 🚀 Шаг с Unicode символами
Содержимое с эмодзи 😊 и разными языками: 中文, العربية
{% endstep %}
{% step %}
**步骤 2️⃣**
More unicode: ñ, ü, é, ß
{% endstep %}
{% endstepper %}"
          context {}
          result (stepper/hack-stepper 
                  context 
                  "test.md"
                  md-core/parse-markdown-content
                  md-core/render-md
                  content)]
      (is (string? result))
      (is (str/includes? result "stepper-container"))
      (is (str/includes? result "🚀")))))

(ns gitbok.browser-test
  (:require
   [clojure.java.io :as io]
   [gitbok.markdown.core :as md-core]
   [hiccup2.core]))

;; Читаем реальный файл с проблемой
(def content (slurp (io/file "docs/tutorials/artifact-registry-tutorials/how-to-create-fhir-npm-package.md")))

;; Находим первый блок с JSON кодом
(def problem-block
  "{% code title=\"my-patient-profile.json\" %}
```json
{
    // Type of the FHIR resource.
    \"resourceType\": \"StructureDefinition\",
    
    // How the type relates to the baseDefinition.
    \"derivation\": \"constraint\"
}
```
{% endcode %}")

(println "=== ORIGINAL BLOCK ===")
(println problem-block)

;; Обрабатываем через полный pipeline как делает сервер
(def parsed-map (md-core/parse-markdown-content {} ["test.md" problem-block]))
(def result-hiccup (md-core/render-md {} "test.md" (:parsed parsed-map)))
(def result-html (str (hiccup2.core/html result-hiccup)))

(println "\n=== FINAL HTML ===")
(println result-html)

;; Проверяем на проблемы
(println "\n=== CHECKS ===")
(println "Has &quot;:" (boolean (re-find #"&quot;" result-html)))
(println "Has &amp;quot;:" (boolean (re-find #"&amp;quot;" result-html)))
(println "Number of <pre> tags:" (count (re-seq #"<pre" result-html)))

;; Смотрим, что происходит на каждом этапе
(println "\n=== AFTER HACK-GITBOOK-CODE ===")
(require '[gitbok.markdown.widgets.gitbook-code :as gitbook-code])
(def after-gitbook (gitbook-code/hack-gitbook-code {} "test.md" nil nil problem-block))
(println "Has %%GITBOOK_EMPTY_LINE%%:" (boolean (re-find #"%%GITBOOK_EMPTY_LINE%%" after-gitbook)))
(println "Has nested <pre>:" (boolean (re-find #"<pre.*<pre" after-gitbook)))

(println "\n=== AFTER HACK-MD ===")
(def hacked (md-core/hack-md {} "test.md" problem-block))
(println "Has &quot; after hack-md:" (boolean (re-find #"&quot;" hacked)))
(println "Has &amp;quot; after hack-md:" (boolean (re-find #"&amp;quot;" hacked)))
(println "Has %%GITBOOK_EMPTY_LINE%% after hack-md:" (boolean (re-find #"%%GITBOOK_EMPTY_LINE%%" hacked)))

;; Показываем кусок с проблемой
(when-let [match (re-find #"(?s).{50}resourceType.{100}" result-html)]
  (println "\n=== Area around 'resourceType' in final HTML ===")
  (println match))
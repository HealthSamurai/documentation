(ns user
  (:require
   [system.dev :as dev]
   [system]
   [clojure.string :as str]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.indexing.impl.file-to-uri :as file-to-uri]
   [gitbok.core :as gitbok]
   [gitbok.markdown.core :as markdown]))

(comment
  (dev/update-libs)

  (def context (system/start-system gitbok/default-config))
  (system/stop-system context)

  (def uri->file-idx
    (uri-to-file/get-idx context))

  (take 10 uri->file-idx)

  (filter
    (fn [[url file]]
      (str/starts-with? url "overview/faq"))
    uri->file-idx)

  (def content
    "```yaml
POST /v2/fhir/$import
Accept: text/yaml
Content-Type: text/yaml

id: synthea-100
inputs:
- resourceType: AllergyIntolerance
  url: url1
- resourceType: CarePlan
  url: url2
```")

  (def content
"{% tabs %}
{% tab title=\"100 ~ 300MB\" %}
```yaml
POST /v2/fhir/$import
Accept: text/yaml
Content-Type: text/yaml

id: synthea-100
inputs:
- resourceType: AllergyIntolerance
  url: url1
- resourceType: CarePlan
  url: url2
```
{% endtab %}
{% endtabs %}")

  (def content "`code1`")
  (def p
    (markdown/parse-markdown-content context [""  content]))

  (markdown/render-md context "" (:parsed p))
  )

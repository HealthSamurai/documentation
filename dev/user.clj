(ns user
  (:require
   [system.dev :as dev]
   [system]
   [clojure.string :as str]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.indexing.impl.file-to-uri :as file-to-uri]
   [gitbok.indexing.impl.summary :as summary]
   [gitbok.core :as gitbok]
   [clj-reload.core :as reload]
   [gitbok.markdown.core :as markdown]))

(comment

  (do (reload/init {:dirs ["src"]})
      (def context (system/start-system gitbok/default-config)))

  (do (reload/reload)
      (system/stop-system context)
      (def context (system/start-system gitbok/default-config)))

  (dev/update-libs)

  (def uri->file-idx
    (uri-to-file/get-idx context))

  (take 10 uri->file-idx)

  (filter
    (fn [[url file]]
      (str/starts-with? url "overview/faq"))
    uri->file-idx)

  (def content
    "---
description: This page explains how to make Aidbox respect self-signed SSL certificates
---")

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

  (def content "{% embed url=\"https://youtu.be/zXzy-is20e8\" %}")
  (def p
    (:content (:parsed (markdown/parse-markdown-content context [""  content]))))

  (markdown/render-md context ""  (:parsed (markdown/parse-markdown-content context [""  content])))
  )

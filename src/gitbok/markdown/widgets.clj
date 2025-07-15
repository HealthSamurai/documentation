(ns gitbok.markdown.widgets
  (:require
   [gitbok.ui.main-content :as main-content]
   [gitbok.ui.right-toc :as right-toc]
   [gitbok.utils :as utils]
   [gitbok.markdown.core :as markdown]
   [gitbok.ui.layout :as layout]))

(defn widgets
  [context request]
  (layout/layout
   context request
   (let [content*
         "---
description: This page contains widgets of gitbook.
---

# Widgets

## Text

Lorem [ipsum](./readme) dolor *sit* amet, [consectetur](https://google.com) `adipiscing elit`. Ut **pharetra sapien in massa tristique**, ~~eleifend bibendum~~ ligula porttitor. Integer vehicula scelerisque sapien, et ultrices urna viverra a. Maecenas tristique bibendum fringilla. Nulla ornare massa eu ipsum consequat, id porttitor risus finibus. Suspendisse finibus rhoncus orci, mattis consectetur erat sodales maximus. Nulla bibendum ipsum nulla, rhoncus tincidunt purus ullamcorper non. Quisque eget ex condimentum, finibus leo vel, consequat enim. Pellentesque dignissim leo vitae leo accumsan eleifend. Duis id lacus sit amet elit dictum vehicula nec sed mi. Mauris volutpat vulputate leo, ut facilisis nulla sollicitudin eu. Suspendisse non posuere nibh. Donec sed leo lacinia, facilisis mi vel, porttitor velit.

Bullets:
- FHIR Configuration
- API
- Auth
- [graphql](widgets.md#graphql)
- Extensibility: Apps & SDK

Ordered list:
1. One
2. Two
   * TwoOne
   * TwoTwo
3. Three

## Big link
[[./getting-started/run-aidbox-locally.md]]

{% embed url=\"https://github.com\" %}

## Table
| Syntax | Description |
| ----------- | ----------- |
| Header | Title |
| Paragraph | Text |

## Hints

### Danger

{% hint style=\"danger\" %}
Work In Progress
{% endhint %}

### Warning

{% hint style=\"warning\" %}
Aidbox notebooks is a beta. Please join the discussion or contact us if you want to contribute.
{% endhint %}

### info

{% hint style=\"info\" %}
Please start a discussion or contact us if you have questions, feedback, or suggestions.
{% endhint %}

### tip

{% hint style=\"tip\" %}
tip
{% endhint %}

### success
{% hint style=\"success\" %}
cuccess
{% endhint %}

## tabs

{% tabs %}
{% tab title=\"Request\" %}
Request:
```http
GET /$version
```
text
{% endtab %}
{% tab title=\"Response\" %}
Response:
{% code title=\"Status 200\" %}
```
version: '2204'
channel: edge
commit: c51312c6
zen-fhir-version: 0.5.8
```
{% endcode %}
text
{% endtab %}
{% endtabs %}

## image

![docker](./.gitbook/assets/docker.png)


## Code
### bash

```bash
curl -JO https://aidbox.app/runme && docker compose up
```

### http
```http
POST /fhir/Patient
content-type: application/json
accept: application/json

{
  \"resourceType\": \"Patient\",
  \"gender\": \"female\"
}
```

### json
```json
{
  \"resourceType\": \"Patient\",
  \"gender\": \"female\"
}
```

### yaml

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

### sql

```sql
WITH history AS (
    (SELECT id, txid, ts, resource_type, status::text AS status, resource, cts
           FROM \"patient\" WHERE id = ?)
    UNION
    (SELECT id, txid, ts, resource_type, status::text AS status, resource, cts
           FROM \"patient_history\" WHERE id = ?))
SELECT * FROM history ORDER BY txid DESC
LIMIT ? OFFSET ?
```

### graphql

```graphql
query {
  Patient(id: \"pt-1\") {
    id
    name {
      given
    }
  }
}
```

### javascript


```javascript
import { Client } from \"aidbox-sdk\"

export const aidbox = new Client(\"<HOST_URL>\", {
    username: \"<CLIENT_NAME>\",
    password: \"<CLIENT_SECRET>\"
})
```

### python

```python
if 5 > 2:
  print(\"Five is greater than two!\")
```

### mermaid

```mermaid
sequenceDiagram
    Alice ->> Bob: Hello Bob, how are you?
    Bob-->>John: How about you John?
    Bob--x Alice: I am good thanks!
    Bob-x John: I am good thanks!
    Note right of John: Bob thinks a long<br/>long time, so long<br/>that the text does<br/>not fit on a row.

    Bob-->Alice: Checking with John...
    Alice->John: Yes... John, how are you?
```

## Code with title

{% code title=\".env\" %}
```bash
AIDBOX_FHIR_SCHEMA_VALIDATION=true
AIDBOX_FHIR_PACKAGES=hl7.fhir.us.core#5.0.1:hl7.fhir.us.mcode#3.0.0
AIDBOX_TERMINOLOGY_SERVICE_BASE_URL=https://tx.health-samurai.io/fhir
```
{% endcode %}

## youtube
{% embed url=\"https://www.youtube.com/watch?v=BtLxICcQNWw&feature=youtu.be\" %}
that's it.


"
         {:keys [parsed description title]}
         (markdown/parse-markdown-content
          context
          ["widgets.md" content*])]
     {:content
      (main-content/render-file* context "widgets.md" parsed title content*)
      :description
      (or
       description
       (let [stripped (utils/strip-markdown content*)]
         (if (>= (count stripped) 150)
           (subs stripped 0 150)
           stripped)))
      :filepath "widgets.md"
      :toc (right-toc/render-right-toc parsed)})))

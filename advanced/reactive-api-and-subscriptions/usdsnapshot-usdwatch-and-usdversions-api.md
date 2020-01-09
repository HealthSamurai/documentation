---
description: Simple API to react on resource changes
---

# Changes API

{% hint style="info" %}
Base url for reactive API is **`/react/v1`**
{% endhint %}

```yaml
---
GET /[base]/versions/Patient

# status 200
version: 1

---
GET /[base]/changes/Patient?version=1

# status 302 (not changed)

---
POST /Patient

id: pt-1

---
GET /[base]/changes/Patient?version=1

version: 2
changes:
- action: created
  resource: {id: pt-1}

```




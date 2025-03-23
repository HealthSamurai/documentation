---
description: Search by the resource id
---

# \_id

Search by the resource id

{% tabs %}
{% tab title="FHIR format" %}
```yaml
GET /fhir/Patient?_id=pt-1
```
{% endtab %}

{% tab title="Aidbox format" %}
```javascript
GET /Patient?_id=pt-1
```
{% endtab %}
{% endtabs %}

You can search by multiple ids separated by a comma:

{% tabs %}
{% tab title="FHIR format" %}
```yaml
GET /Patient?_id=pt-1,pt-2,pt-3
```
{% endtab %}

{% tab title="Aidbox format" %}
```yaml
GET /Patient?_id=pt-1,pt-2,pt-3
```
{% endtab %}
{% endtabs %}

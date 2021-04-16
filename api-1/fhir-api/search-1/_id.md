---
description: Search by the resource id
---

# \_id

Search by the resource id

{% tabs %}
{% tab title="FHIR format" %}
```javascript
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
```javascript
GET /Patient?_id=pt-1,pt-2,pt-3
```
{% endtab %}

{% tab title="Aidbox format" %}
```javascript
GET /Patient?_id=pt-1,pt-2,pt-3
```
{% endtab %}
{% endtabs %}

You can use \_id parameter in sort expressions:

{% tabs %}
{% tab title="FHIR format" %}
```yaml
GET /fhir/Entity?_sort=_id

#200
resourceType: Bundle
type: searchset
entry:
- resource:
    type: resource
    isMeta: true
    module: auth
    source: code
    resourceType: Entity
    id: AccessPolicy
    meta: {lastUpdated: '2019-07-30T16:51:52.763470Z', versionId: '0'}
....
```
{% endtab %}

{% tab title="Aidbox format" %}
```yaml
GET /Entity?_sort=_id

# 200
resourceType: Bundle
type: searchset
entry:
- resource:
    type: resource
    isMeta: true
    module: auth
    source: code
    resourceType: Entity
    id: AccessPolicy
    meta: {lastUpdated: '2019-07-30T16:51:52.763470Z', versionId: '0'}
....
```
{% endtab %}
{% endtabs %}


---
description: Basic endpoints to manage resources
---

# CRUD

{% api-method method="get" host="" path="/fhir/:resourceType/:id" %}
{% api-method-summary %}
Read Endpoint
{% endapi-method-summary %}

{% api-method-description %}
This endpoint accesses the current version of a resource. Aidbox will return an `ETag` header with the current version ID of the resource and a `Last-Modified` header.
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-path-parameters %}
{% api-method-parameter name="id" type="string" required=false %}
ID of resource
{% endapi-method-parameter %}

{% api-method-parameter name="resourceType" type="string" required=false %}
Type of resource being read
{% endapi-method-parameter %}
{% endapi-method-path-parameters %}

{% api-method-query-parameters %}
{% api-method-parameter name="\_format" type="string" required=false %}
Specifies response serialization format, possible values are "json" \(default\) or "yaml"
{% endapi-method-parameter %}
{% endapi-method-query-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}
Successful read, returns full resource content
{% endapi-method-response-example-description %}

```javascript
{
  "id": "example-resource",
  "resourceType": "Patient",
  "name": [{"given": ["Alex"]}]
}
```
{% endapi-method-response-example %}

{% api-method-response-example httpCode=410 %}
{% api-method-response-example-description %}
Reading already deleted resource
{% endapi-method-response-example-description %}

```javascript
{
  "resourceType": "OperationOutcome",
  "status": 410
}
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

{% hint style="warning" %}
Deleted and never existed resources are treated differently on reading. Reading deleted resource will return `410 - Gone` response, when reading never existed resource will end up with `404 - Not found`.
{% endhint %}




---
description: Basic endpoints to manage resources
---

# CRUD

{% api-method method="get" host="<base-url>" path="/fhir/:resourceType/:id" %}
{% api-method-summary %}
Read Resource
{% endapi-method-summary %}

{% api-method-description %}
This endpoint accesses the current version of a resource.
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-path-parameters %}
{% api-method-parameter name="id" type="string" required=true %}
ID of resource
{% endapi-method-parameter %}

{% api-method-parameter name="resourceType" type="string" required=true %}
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

{% hint style="info" %}
Aidbox will return an `ETag` header with the current version ID of the resource and a `Last-Modified` header.
{% endhint %}

{% hint style="warning" %}
Deleted and never existed resources are treated differently on reading. Reading deleted resource will return `410 - Gone` response, when reading never existed resource will end up with `404 - Not found`.
{% endhint %}

{% api-method method="post" host="<base-url>" path="/fhir/:resourceType" %}
{% api-method-summary %}
Create Resource
{% endapi-method-summary %}

{% api-method-description %}
The create endpoint creates a new resource in a server-assigned location.   
  
The request body should contain a valid FHIR resource. If resource contains an `id` or `meta` attributes, they will be ignored.
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-path-parameters %}
{% api-method-parameter name="resourceType" type="string" required=true %}
Type of resource being created
{% endapi-method-parameter %}
{% endapi-method-path-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=201 %}
{% api-method-response-example-description %}
Resource was successfully created
{% endapi-method-response-example-description %}

```http
HTTP/1.1 201 Created
Content-Type: application/json
Location: /fhir/SomeResource/62be2832-94b0-11e8-9eb6-529269fb1459/_history/692738da-94b0-11e8-9eb6-529269fb1459

{
  "resourceType": "SomeReason",
  "someAttribute": "some-value",
  "id": "62be2832-94b0-11e8-9eb6-529269fb1459"
}
```
{% endapi-method-response-example %}

{% api-method-response-example httpCode=422 %}
{% api-method-response-example-description %}
References Integrity check failed
{% endapi-method-response-example-description %}

```javascript
{
  "resourceType": "OperationOutcome",
  "errors": [
    {
      "path": ["managingOrganization"],
      "value": { "id": "1", "resourceType": "Organization" },
      "deferred": "reference",
      "message": "Referenced resource Organization/1 does not exist"
    }
  ],
  "warnings": []
}
```
{% endapi-method-response-example %}

{% api-method-response-example httpCode=422 %}
{% api-method-response-example-description %}
Schema validation failed
{% endapi-method-response-example-description %}

```javascript
{
  "resourceType": "OperationOutcome",
  "errors": [
    {
     "path": ["race"],
     "message": "extra property"
    }
  ],
  "warnings": []
}
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

{% hint style="info" %}
Server will validate resource body against FHIR schema and in case of errors will respond with `422 Unprocessable Entity` error code and OperationOutcome resource containing information about validation errors. All references contained within resource will be checked for existence as well.
{% endhint %}

{% hint style="info" %}
If the client wishes to have control over the ID of a newly submitted resource, it should use the Update Endpoint instead.
{% endhint %}


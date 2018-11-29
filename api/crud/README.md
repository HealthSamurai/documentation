---
description: Basic endpoints to manage resources
---

# CRUD

This part of documentation describes how to create, read, update and delete resources. Also, it covers some advanced topic like conditional create, update and delete. Aidbox REST API slightly differs from canonical FHIR REST API. There is an [article](../../basic-concepts/aidbox-vs-fhir.md), which describes those differences.

## Terms

A **resource** is an object with a type, associated data, relationships to other resources \(all that information can be found in FHIR [specification](https://www.hl7.org/fhir/resourcelist.html) or through Aidbox [metadata](../custom-metadata.md)\), and a set of methods that operate on it. In most cases a resource represented as a JSON/XML/YAML document.

Each resource has its own resource **type**, this type define set of data, which can be stored with this resource and possible relationships with other resources.

Attribute is a part of resource definition, which describe what fields can or must be present in resource document, type of such field and cardinality.

Every resource type has the same set of **interactions** available. Those interactions are described below. 

Each interaction can fail with:

* **`403`** **Forbidden** - client are not authorized to perform the interaction

## create

```text
POST [base]/[type]
```

One of the most basic interactions, which gives an ability to create a resource. It uses `POST` HTTP method, accepts resource type via path params and resource as a body of request. Response of this interaction may be one of the following:

* **`201`** **Created** - resource successfully created
* **`400`** **Bad Request** - resource could not be parsed or failed basic FHIR validation rules
* **`409`** **Conflict** - resource with such id already exists
* **`422`** **Unprocessable Entity** - the proposed resource violated applicable FHIR profiles or server business rules

Successful response `2xx` also contains a created resource as a body and additional header `Location`, which contains full path to resource \(base url, resource type and id of newly created resource\) and additionally information about version of that resource \(vid\).

```text
Location: [base]/[type]/[id]/_history/[vid]
```

Unsuccessful response  `4xx` contains `OperationOutcome` resource, which describes issues server faced creating this resource.

### `201` Created

{% tabs %}
{% tab title="Request" %}
```yaml
POST /Patient

name: [{given: ["Bob"]}]
```
{% endtab %}

{% tab title="Response" %}
**Status:** `201`

```yaml
name:
- given: [Bob]
id: 17b69d79-3d9b-45f8-af79-75f958502763
resourceType: Patient
meta:
  lastUpdated: '2018-11-29T10:44:10.588Z'
  versionId: '13'
  tag:
  - {system: 'https://aidbox.app', code: created}
```
{% endtab %}
{% endtabs %}

### `422` Unprocessable entity

{% tabs %}
{% tab title="Request" %}
```yaml
POST /Patient

name: "Bob"
```
{% endtab %}

{% tab title="Response" %}
**Status:** `422`

```yaml
resourceType: OperationOutcome
errors:
- path: [name]
  message: expected array
warnings: []
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
Aidbox REST API doesn't ignore `id` and treat it as all other attributes in contrast to FHIR API. Read more about differences [here](../../basic-concepts/aidbox-vs-fhir.md).
{% endhint %}

## conditional create

```text
POST [base]/[type]?[search parameters]
```

Much more complex way to create a resource \(it requires knowledge of [search](../history.md)\), but it gives some additional flexibility. If you provide search parameters `create` becomes `conditional create` and works in following way \(depending on the number of search results\): 

* **No matches**: The server performs a `create` interaction
* **One Match**: The server ignore the post and returns `200 OK`
* **Multiple matches**: The server returns a `412 Precondition Failed` error indicating the client's criteria were not selective enough

### `200` OK

Create patient if there is no patient with name Bob.

{% tabs %}
{% tab title="Request" %}
```yaml
POST /Patient?name=Bob

name: [{given: ["Bob"]}]
gender: male
```
{% endtab %}

{% tab title="Response" %}
**Status:** `200`

```yaml
name:
- given: [Bob]
id: 17b69d79-3d9b-45f8-af79-75f958502763
resourceType: Patient
meta:
  lastUpdated: '2018-11-29T10:44:10.588Z'
  versionId: '13'
  tag:
  - {system: 'https://aidbox.app', code: created}
```
{% endtab %}
{% endtabs %}

Patient not created, existing patient was returned.

## read

```text
GET [base]/[type]/[id]
```

One of the most basic operations, used to obtain a resource by a given `id`. For more advanced options for getting resources check out [Search](../history.md).

* **`200`** **OK** - resource successfully found and returned
* **`404`** **Not Found** - resource with a given `id` doesn't exist on the server
* **`410`** **Gone** - resource was deleted

### `200` OK

Get existing patient:

{% tabs %}
{% tab title="Request" %}
```text
GET /Patient/17b69d79-3d9b-45f8-af79-75f958502763
```
{% endtab %}

{% tab title="Response" %}
**Status:** `200`

```yaml
name:
- given: [Bob]
id: 17b69d79-3d9b-45f8-af79-75f958502763
resourceType: Patient
meta:
  lastUpdated: '2018-11-29T10:44:10.588Z'
  versionId: '13'
  tag:
  - {system: 'https://aidbox.app', code: created}
```
{% endtab %}
{% endtabs %}

### `404` Not Found

Attempt to get not-existing patient:

{% tabs %}
{% tab title="Request" %}
```text
GET /Patient/some-not-existing-id
```
{% endtab %}

{% tab title="Response" %}
**Status:** `404`

```yaml
resourceType: OperationOutcome
status: 404
text: Resource Patient/some-not-existing-id not found
```
{% endtab %}
{% endtabs %}

## vread

```text
GET [base]/[type]/[id]/_history/[vid]
```

Another operation, which returns a specific version resource. Similar to read, but additionally requires to specify version id.

### `200` OK

Version id `13` was extracted from response of `create` interaction.

{% tabs %}
{% tab title="Request" %}
```text
GET /Patient/17b69d79-3d9b-45f8-af79-75f958502763/_history/13
```
{% endtab %}

{% tab title="Response" %}
**Status:** `200`

```yaml
name:
- given: [Bob]
id: 17b69d79-3d9b-45f8-af79-75f958502763
resourceType: Patient
meta:
  lastUpdated: '2018-11-29T10:44:10.588Z'
  versionId: '13'
  tag:
  - {system: 'https://aidbox.app', code: created}
```
{% endtab %}
{% endtabs %}

{% api-method method="get" host="\[base\]" path="/<resourceType>/<id>" %}
{% api-method-summary %}

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

{% hint style="info" %}
Server will validate resource body against FHIR schema and in case of errors will respond with `422 Unprocessable Entity` error code and OperationOutcome resource containing information about validation errors. All references contained within resource will be checked for existence as well.
{% endhint %}

{% hint style="info" %}
If the client wishes to have control over the ID of a newly submitted resource, it should use the Update Endpoint instead.
{% endhint %}

{% api-method method="put" host="\[base\]" path="/<resourceType>/<id>" %}
{% api-method-summary %}
Update Resource
{% endapi-method-summary %}

{% api-method-description %}
The update endpoint creates a new version for an existing resource or creates an initial version if no resource already exists for the given id.  
  
The request body should contain valid resource with an `id` element that has an identical value to the `:id` in the URL. If no `id` element is provided or value is wrong, server will respond with `400 Bad Request`.
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-path-parameters %}
{% api-method-parameter name="id" type="string" required=true %}
ID of resource
{% endapi-method-parameter %}

{% api-method-parameter name="resourceType" type="string" required=true %}
Type of resource being updated
{% endapi-method-parameter %}
{% endapi-method-path-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}

{% endapi-method-response-example-description %}

```

```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}


---
description: >-
  The Bundle resource is a container for a collection of resources and
  optionally additional info
---

# Bundle

{% hint style="info" %}
Please start [a discussion](https://github.com/Aidbox/Issues/discussions) or [contact](../../contact-us.md) us if you have questions, feedback, or suggestions.
{% endhint %}

The Bundle resource is a resource which groups multiple resources in one. It is important to differentiate Bundle resource and operation which take or return Bundles.

Let's look at some operations working with Bundle resources.

* `POST /` , `POST /fhir`\
  Main article: [transaction.md](../transaction.md "mention").\
  This operation accepts Bundle resource (with type batch or transaction); executes contained requests; then returns Bundle resource in response (with type batch-response or transaction-response).\
  Operation `POST /` accepts and return Bundle in Aidbox format; operation `POST /fhir` accepts and returns Bundle in FHIR format.
* `GET /<resourceType>`, `GET /fhir/<resourceType>`\
  Main article: [search-1](search-1/ "mention")\
  This operation searches for resource of type `resourceType` using search parameters provided in query string, and returns Bundle resource (with type `searchset`) containing all resources matching the given filters.
* `GET /<resourceType>/<id>/_history`, `GET /fhir/<resourceType>/<id>/_history`\
  Main article: [history-1.md](history-1.md "mention")\
  This operation returns the Bundle resource (with type `history`) containing previous versions of the specified resource.
* `GET /<resourceType>/_history`, `GET /fhir/<resourceType>/_history`\
  Main article: [history-1.md](history-1.md "mention")\
  This operations returns the Bundle resource (with type `history`) containing previous versions of resources with the specified type.
* CRUD operations with Bundle resource\
  Main article: [crud-1](../api/crud-1/ "mention")\
  These are the usual FHIR CRUD operations with Bundle resource. They only store/update/get/search Bundle resources without additional semantics.\
  These operations are rarely used.\
  Examples: `POST /Bundle`, `GET /Bundle`, `GET /Bundle/<id>`, `PUT /Bundle`,\
  `POST /fhir/Bundle`, `GET /fhir/Bundle`, `GET /fhir/Bundle/<id>`,\
  `PUT /fhir/Bundle/<id>`

See more about the difference between Aidbox and FHIR formats (`/...` and `/fhir/...` endpoints) in the [aidbox-and-fhir-formats.md](../../modules-1/fhir-resources/aidbox-and-fhir-formats.md "mention") page.

### `POST /` , `POST /fhir` endpoint

Aidbox supports FHIR batch and transaction interactions, as well as some additional options to this endpoint.

Same as with other endpoint, `POST /` accepts and returns Bundle in Aidbox format,\
while `POST /fhir` accepts and returns Bundle in FHIR format.

```yaml
POST /
content-type: text/yaml
accept: text/yaml

resourceType: Bundle
type: <type>
...
```

Behavior of the endpoint depends on the `type` of a bundle resource provided in a request body.

Supported `type` values:

* `transaction`\
  Executes provided rest requests in a transaction.\
  In case of an entry execution error the whole transaction is rolled back and the error is returned in the response.\
  Returns `transaction-response` type bundle.
* `batch`\
  Executes provided rest requests, execution doesn't stop on error, all results and errors are returned in the response.\
  Returns `batch-response` type bundle.
* `collection`\
  Works the same way as the `batch` type, but does `PUT /<resourceType>/<id>` for each resource in entry.\
  Returns `batch-response` type bundle.

### Validate Bundle

To validate bundle without storing it content, use `Bundle/$validate` operation.

```json
POST /fhir/Bundle/$validate
content-type: application/json
accept: application/json

{
 "resourceType": "Bundle",
 "type": "message",
 // ...
}
```

{% hint style="warning" %}
In [fhir-schema-validator](../../modules/profiling-and-validation/fhir-schema-validator/ "mention") mode, `/fhir/Bundle/$validate`  doesn't validates Aidbox built-in resources (User, AccessPolicy, etc.). &#x20;

Use validation in Aidbox format with `Bundle/$validate` if you need to validate a bundle with built-in resources. This endpoint only validates entries of the bundle, but not the bundle itself.
{% endhint %}




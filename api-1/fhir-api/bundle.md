---
description: >-
  The Bundle resource is a container for a collection of resources and
  optionally additional info
---

# Bundle

{% hint style="info" %}
Please start [a discussion](https://github.com/Aidbox/Issues/discussions) or [contact](../../contact-us.md) us if you have questions, feedback, or suggestions.
{% endhint %}

The Bundle resource has a variety of different applications.\
Usage of bundles by different endpoints:

* `POST /`\
  Accepts bundles of certain types and executes REST requests contained in them.\
  Returns bundles with `transaction-response` or `batch-response` types.
* `POST /Bundle/`, `PUT /Bundle/<id>`\
  Accepts a bundle and saves it as a regular resource regardless of its type.\
  Returns the saved resource.
* `GET /<resourceType>`\
  Searches for resources of specified resource type.\
  Returns bundle of `searchset` type.
* `GET /<resourceType>/_history`, `GET /<resourceType>/<id>/_history`\
  Searches for previous versions of the specified resource or resource type\
  Returns bundle of `history` type.

### `POST /` endpoint

Aidbox supports FHIR batch/transaction interaction, as well as some additional options to this endpoint.

```yaml
POST /
content-type: text/yaml

resourceType: Bundle
type: <type>
...
```

Behavior of the endpoint depends on the `type` of a bundle resource provided in a request body:

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

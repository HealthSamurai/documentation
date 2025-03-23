---
description: Updating a part of your resource
---

# Patch

In most Operations in FHIR, you manipulate a resource as a whole (create, update, delete operations). But sometimes you want to update specific data elements in a resource and do not care about the rest. In other words, you need an element/attribute level operation.

With the `patch` operation, you can update a part of a resource by sending a declarative description of operations that should be performed on an existing resource. To describe these operations in Aidbox, you can use different notations (methods):

* Merge Patch — simple merge semantics ([read more in RFC](https://tools.ietf.org/html/rfc7386));
* JSON Patch — advanced JSON transformation ([read more in RFC](https://tools.ietf.org/html/rfc6902));
* [FHIRpath Patch](https://www.hl7.org/fhir/fhirpatch.html) - JSON patch with [FHIRpath](https://www.hl7.org/fhir/fhirpatch.html) (since version 2503).

### Patch Method

You can specify a `patch` method by the `content-type` header or by the `_method` parameter.

| method             | parameter        | header                       |
| ------------------ | ---------------- | ---------------------------- |
| **json-patch**     | `json-patch`     | application/json-patch+json  |
| **merge-patch**    | `merge-patch`    | application/merge-patch+json |
| **fhirpath-patch** | `fhirpath-patch` | -                            |

If the method is not specified, Aidbox will try to guess it by the following algorithm:

* if the body contains "Parameters" resourceType, it is `fhirpath-patch`
* if the payload is an array, it is `json-patch`&#x20;
* else `merge-patch`

### Binary JSON-patch

Since version 2503, It is also possible to [encode JSON patches using base64 Binary.data](https://www.hl7.org/fhir/http.html#jsonpatch-transaction):

```
PATCH /fhir/Patient/1?_method=json-patch
content-type: application/json
accept: application/json

{
  "resourceType": "Binary",
  "contentType": "application/json-patch+json",
  "data": "WyB7ICJvcCI6InJlcGxhY2UiLCAicGF0aCI6Ii9hY3RpdmUiLCAidmFsdWUiOmZhbHNlIH0gXQ=="
}
```

## Examples

Let's suppose we've created a Patient resource with the id `pt-1`

```yaml
POST /Patient

resourceType: Patient
id: pt-1
active: true
name:
  - given: ['John']
    family: Doe
    use: official
  - given: ['Johny']
    family: Doe
telecom:
  -  system: phone
     value: '(03) 5555 6473'
     use: work
     rank: 1
birthDate: '1979-01-01'
```

### Merge Patch

Let's say we want to switch to an `active` flag to false and remove `telecom`:

```yaml
PATCH /Patient/pt-1

active: false
telecom: null

# 200

id: pt-1
resourceType: Patient
name:
- use: official
  given:
  - John
  family: Doe
- given:
  - Johny
  family: Doe
active: false
birthDate: '1979-01-01'
```

### JSON Patch

With JSON patch, we can do more sophisticated transformations — change the first `given` name, delete the second `name`, and change the `active` attribute value to `true`:

```yaml
PATCH /Patient/pt-1

- op: replace
  path: '/name/0/given/0'
  value: Nikolai
- op: remove
  path: '/name/1'
- op: replace
  path: '/active'
  value: true
```

Response:

```
# 200 OK
id: pt-1
resourceType: Patient
name:
- use: official
  given:
  - Nikolai
  family: Doe
active: true
birthDate: '1979-01-01'
```

### FHIRpath Patch

The example sets `active`to `false`:

```
PATCH /fhir/Patient/pt-1

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "operation",
      "part": [
        {
          "name": "type",
          "valueCode": "add"
        },
        {
          "name": "path",
          "valueString": "Patient"
        },
        {
          "name": "name",
          "valueString": "active"
        },
        {
          "name": "value",
          "valueBoolean": false
        }
      ]
    }
  ]
}
```

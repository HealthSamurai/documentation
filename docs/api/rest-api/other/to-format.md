---
description: Debug FHIR to Aidbox format transformations using $to-format endpoints to inspect conversion results and DSL.
---

# $to-format

Aidbox converts [FHIR and AIdbox formats](aidbox-and-fhir-formats.md) on the fly. To debug this transformation, you can use `POST /$to-format/fhir` and `POST /$to-format/aidbox` endpoints, which can show you the result of your transformation with some additional info:

```yaml
POST /$to-format/aidbox

resourceType: Observation
valueString: test

# 200

# result of transformation
resource:
  resourceType: Observation
  value: {string: test}
# dsl which is used for transformation
transform:
  hasMember: {tr/reference: true, tr/ref: Reference, tr/isCollection: true}
  derivedFrom: {tr/reference: true, tr/ref: Reference, tr/isCollection: true}
  ...
```

And back:

```yaml
POST /$to-format/fhir

resourceType: Observation
value: 
  string: test

# 200
resource: 
  resourceType: Observation
  valueString: test
transform:
  hasMember: {tr/reference: true, tr/ref: Reference, tr/isCollection: true}
  derivedFrom: {tr/reference: true, tr/ref: Reference, tr/isCollection: true}
  encounter: {tr/reference: true, tr/ref: Reference}
```

#### Notes:

How to read transform DSL

* **tr/reference** - instruction to transform references
* **tr/ref** - recursively go to type definition or follow content-ref
* **tr/union** - translate union / choice types
* **tr/ext** - translate known extensions

### Remove transform from response

Response can be very large because of `transform` key. Use `include-transform "false"` url parameter to remove it.

```yaml
POST /$to-format/fhir?include-transform=false

resourceType: Observation
value: 
  string: test

# 200
resource: 
  resourceType: Observation
  valueString: test
```

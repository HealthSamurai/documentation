---
description: With patch operation you can update only part of FHIR resource.
---

# Patch

Aidbox support different patch methods.

* merge-patch  - [https://tools.ietf.org/html/rfc7386](https://tools.ietf.org/html/rfc7386)
* json-patch - [https://tools.ietf.org/html/rfc6902](https://tools.ietf.org/html/rfc6902)
* fhir-patch - [https://www.hl7.org/fhir/fhirpatch.html](https://www.hl7.org/fhir/fhirpatch.html)

### Specify patch method

You can specify patch method by `content-type` header or by `_method` parameter.

| Method | merge-patch | json-patch | fhir-patch |
| :--- | :--- | :--- | :--- |
| header | application/merge-patch+json | application/json-patch+json | application/fhir-patch+json |
| \_method | merge-patch | json-patch | fhir-patch |

if method is not specified, aidbox will try to guess it by following algorithm: 

* if payload is array - `json-merge`
* if payload is object with `resourceType = 'Parameter'` - `fhir-patch` 
* else  `merge-patch`

### Merge patch

```yaml
PATCH /Patient/pt-1?_method=merge-patch

name:
- given: 
   - Nikolai
  prefix: null
 
birthDate: 1980-05-03
```

### JSON patch

```yaml
PATCH /Patient/pt-1?_method=merge-patch

- op: replace
  path: 'name/0/given/0'
  value: Nikolai
```

### FHIR patch


---
description: With patch operation you can update only part of FHIR resource.
---

# Patch

Aidbox support different patch methods.

* json-merge-patch  - [https://tools.ietf.org/html/rfc7386](https://tools.ietf.org/html/rfc7386)
* json-patch - [https://tools.ietf.org/html/rfc6902](https://tools.ietf.org/html/rfc6902)
* fhir-patch - [https://www.hl7.org/fhir/fhirpatch.html](https://www.hl7.org/fhir/fhirpatch.html)

### JSON merge patch

```yaml
PUT /Patient/pt-1?method=json-merge-patch

name:
 - given: Nikolai
birthDate: 1980-05-03
```


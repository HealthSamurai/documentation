# \_security

The standard search parameter `_security` is used to match resources based on security labels in the [Resource.meta.security](https://www.hl7.org/fhir/resource-definitions.html#Meta.security) element.

Examples:

```
GET /fhir/Observation?_security=http://terminology.hl7.org/CodeSystem/v3-Confidentiality|R
```

```
GET /fhir/Observation?_security:not=http://terminology.hl7.org/CodeSystem/v3-Confidentiality|R
```

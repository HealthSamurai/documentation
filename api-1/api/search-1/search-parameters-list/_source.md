# \_source

[Search parameter \_source](https://www.hl7.org/fhir/search.html#\_source) is used to match resources based on source information in the [Resource.meta.source](https://www.hl7.org/fhir/resource-definitions.html#Meta.source) element.

Examples:

```
GET /fhir/Patient?_source=http://example.com/Organization/123
```

```
GET /fhir/Patient?_source:below=http://example.com/
```

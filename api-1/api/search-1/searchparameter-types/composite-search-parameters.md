# Composite Search Parameters

Since version 2308 Aidbox supports Composite Search Parameters.

[Composite Search Parameters](https://www.hl7.org/fhir/search.html#composite) are special search parameters that match resources by two or more values, separated by a `$` sign. Such search parameters will search not by simple intersection, like `search-param1=value1&search-param2=value2`, but more strictly.

For example, take a look at [Observation](https://www.hl7.org/fhir/observation.html) resource structure and suppose we have following resource:

```yaml
id: my-observation
component:
- code: loinc|12907-2
  valueQuantity: 
    value: 1
- code: loinc|12907-1
  valueQuantity: 
    value: 2
# ...
```

If we want search to match _my-observation_ only if some component has both `code = loinc|12907-2` and `valueQuantity=1`, we must use composite search:

```
GET /fhir/Observation?code-value-quantity=loinc|12907-2$1 // found
GET /fhir/Observation?code-value-quantity=loinc|12907-2$2 // not found
GET /fhir/Observation?code-value-quantity=loinc|12907-1$1 // not found
GET /fhir/Observation?code-value-quantity=loinc|12907-1$2 // found
```

However, if we use simple intersection, _my-observation_ may be found in all cases:

```
GET /fhir/Observation?code=loinc|12907-2&value-quantity=1 // found
GET /fhir/Observation?code=loinc|12907-2&value-quantity=2 // found
GET /fhir/Observation?code=loinc|12907-1&value-quantity=1 // found
GET /fhir/Observation?code=loinc|12907-1&value-quantity=2 // found
```

## Enable composite search

To turn on in Aidbox project use:

```
BOX_SEARCH_COMPOSITE__SEARCH=true
```

## Create Composite SearchParameter

Here's an example of the changed FHIR`Questionnaire.context-type-value` SearchParameter to search by `useContext.value.as(Reference)` instead of CodeableConcept.

```
POST /fhir/SearchParameter
content-type: application/json
accept: application/json

{
  "url": "http://hl7.org/fhir/SearchParameter/Questionnaire-context-type-value-ref",
  "component": [
    {
      "definition": "http://hl7.org/fhir/SearchParameter/Questionnaire-context-type",
      "expression": "code"
    },
    {
      "definition": "http://mycompany.com/Questionnaire-context-ref",
      "expression": "value.as(Reference)"
    }
  ],
  "id": "Questionnaire-context-type-value",
  "base": [
    "Questionnaire"
  ],
  "expression": "Questionnaire.useContext",
  "name": "context-type-value",
  "status": "draft",
  "multipleOr": false,
  "type": "composite",
  "version": "4.0.1",
  "xpathUsage": "normal",
  "resourceType": "SearchParameter",
  "code": "context-type-value-ref",
  "description": "A use context type and reference value assigned to the questionnaire"
}
```

Usage:

```
GET /fhir/Questionnaire?context-type-value-ref=somecode$Organization/someorg&_explain=1
```

# Composite Search Parameters

Since version 2308 Aidbox supports Composite Search Parameters.

[Composite Search Parameters](https://www.hl7.org/fhir/search.html#composite) are special search parameters to match resources by two or more values, separated by `$` sign. Such search parameters will search not by simple intersection, like `search-param1=value1&search-param2=value2`, but more strictly.&#x20;

For example, take a look at [Observation](https://www.hl7.org/fhir/observation.html) resource structure and suppose we have following resource: &#x20;

```
//Observation
...
id: my-observation
component:
- code: loinc|12907-2
  valueQuantity: 
    value: 1
- code: loinc|12907-1
  valueQuantity: 
    value: 2
...
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
:search {...
         :composite-search true
         ...}
```

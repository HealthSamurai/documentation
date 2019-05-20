---
description: Explanation of FHIR Search implementation in Aidbox
---

# FHIR Search

### FHIR Search

FHIR defines Search API  by meta-resource SearchParameters. SearchParameter has a name, a type \(string, date, number, code\) and FHIRPath expression to extract specific elements from resource. Here is param for searching patients by name:

```yaml
resourceType: SearchParameter
name: name
type: string
expression: Patient.name
```

If you are sending query `GET /Patient?name=john` specification expects something like this:

```text
filter each patient resource:
  where elements_of fhirpath(resource,'Patient.name') start_with 'john'
```

{% hint style="warning" %}
FHIR specification is not precise because often parameter expression returns elements of not primitive type \(in our case HumanName\) and your implementation have to decide how exactly interpret/implement it.  Things are getting worse when search expressions return collection of elements sometimes collections of mixed types.
{% endhint %}

For example Patient.name parameter has description element with more detailed instructions:

> A server defined search that may match any of the string fields in the HumanName, including family, give, prefix, suffix, suffix, and/or text

Aidbox compiles search parameters into SQL query, trying to resolve this ambiguity:

```sql
SELECT * FROM patient 
WHERE extract(resource, ['name.given', 'name.family' ....]) ilike '% john%'
```

### Aidbox search steps

Aidbox performs following steps:

* parse parameters of request
* resolve definitions of search parameters
* build SQL query to get data
* execute it
* load associated resources if \(\_include or \_revinclude params are sent\)
* calculate total by another query \(if \_total=accurate\)


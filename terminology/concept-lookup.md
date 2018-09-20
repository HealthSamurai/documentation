# Concept Lookup

> Given a code/system, or a Coding, get additional details about the concept, including definition, status, designations, and properties. One of the products of this operation is a full decomposition of a code from a structure terminology

In aidbox this is essentially the same as search Concept by system and code

```http
GET [base]/fhir/CodeSystem/$lookup?system=http://loinc.org&code=1963-8
```

equals to

```http
GET [base]/Concept?system=http://loinc.org&code=1963-8
```

The only difference is response format.

With Concept Search you can do even more, than just getting one concept. For example search and filter by any concept elements. 


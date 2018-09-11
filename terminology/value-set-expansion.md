---
description: 'Implementation of https://www.hl7.org/fhir/operation-valueset-expand.html'
---

# Value Set Expansion

Aidbox use de-normalized  approach to ValueSets. That means we pre-calculate valuesets in design time and store valueset id's into _Concept.valuset_ element \(see /Concept article\). That's why ValueSet expansion in aidbox is just a special case of Concept Search:

```http
GET [base]/fhir/ValueSet/23/$expand?filter=abdo
```

means

```http
GET [base]/Concept?valueset=23&filter=abdo
```

The only difference is output format.


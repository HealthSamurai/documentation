---
description: C-CDA Objective Section conversion rules and FHIR resource mapping for clinical document transformation.
---

# Objective Section

OID: 2.16.840.1.113883.10.20.21.2.1

LOINCs: #{"61149-1"}

Alias: objective

Entries Required: N/A

Internal ID: ObjectiveSection

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.21.2.1.html)

## sample1

```json
[ {
  "title" : "OBJECTIVE",
  "code" : {
    "coding" : [ {
      "code" : "61149-1",
      "system" : "http://loinc.org",
      "display" : "OBJECTIVE"
    } ],
    "text" : "OBJECTIVE"
  },
  "text" : {
    "div" : "OBJECTIVE NARRATIVE",
    "status" : "generated"
  }
} ]
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.21.2.1"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="OBJECTIVE"
          code="61149-1">
      <originalText>OBJECTIVE</originalText>
   </code>
   <title>OBJECTIVE</title>
   <text>OBJECTIVE NARRATIVE</text>
</section>
```
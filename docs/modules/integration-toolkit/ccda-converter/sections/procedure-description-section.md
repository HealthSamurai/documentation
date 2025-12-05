---
description: C-CDA Procedure Description Section conversion rules and FHIR resource mapping for clinical document transformation.
---

# Procedure Description Section

OID: 2.16.840.1.113883.10.20.22.2.27

LOINCs: #{"29554-3"}

Alias: procedure-description

Entries Required: N/A

Internal ID: ProcedureDescriptionSection

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.27.html)

## sample1

```json
[ {
  "title" : "PROCEDURE DESCRIPTION",
  "code" : {
    "coding" : [ {
      "code" : "29554-3",
      "system" : "http://loinc.org",
      "display" : "PROCEDURE DESCRIPTION"
    } ],
    "text" : "PROCEDURE DESCRIPTION"
  },
  "text" : {
    "div" : "PROCEDURE DESCRIPTION NARRATIVE",
    "status" : "generated"
  }
} ]
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.27"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="PROCEDURE DESCRIPTION"
          code="29554-3">
      <originalText>PROCEDURE DESCRIPTION</originalText>
   </code>
   <title>PROCEDURE DESCRIPTION</title>
   <text>PROCEDURE DESCRIPTION NARRATIVE</text>
</section>
```
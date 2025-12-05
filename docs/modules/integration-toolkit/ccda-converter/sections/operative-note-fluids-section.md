---
description: C-CDA Operative Note Fluids Section conversion rules and FHIR resource mapping for clinical document transformation.
---

# Operative Note Fluids Section

OID: 2.16.840.1.113883.10.20.7.12

LOINCs: #{"10216-0"}

Alias: operative-note-fluids

Entries Required: N/A

Internal ID: OperativeNoteFluidsSection

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.7.12.html)

## sample1

```json
[ {
  "title" : "OPERATIVE NOTE FLUIDS",
  "code" : {
    "coding" : [ {
      "code" : "10216-0",
      "system" : "http://loinc.org",
      "display" : "OPERATIVE NOTE FLUIDS"
    } ],
    "text" : "OPERATIVE NOTE FLUIDS"
  },
  "text" : {
    "div" : "OPERATIVE NOTE FLUIDS NARRATIVE",
    "status" : "generated"
  }
} ]
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.7.12"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="OPERATIVE NOTE FLUIDS"
          code="10216-0">
      <originalText>OPERATIVE NOTE FLUIDS</originalText>
   </code>
   <title>OPERATIVE NOTE FLUIDS</title>
   <text>OPERATIVE NOTE FLUIDS NARRATIVE</text>
</section>
```
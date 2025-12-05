---
description: C-CDA General Status Section conversion rules and FHIR resource mapping for clinical document transformation.
---

# General Status Section

OID: 2.16.840.1.113883.10.20.2.5

LOINCs: #{"10210-3"}

Alias: general-status

Entries Required: N/A

Internal ID: GeneralStatusSection

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.2.5.html)

## sample1

```json
[ {
  "title" : "GENERAL STATUS",
  "code" : {
    "coding" : [ {
      "code" : "10210-3",
      "system" : "http://loinc.org",
      "display" : "GENERAL STATUS"
    } ],
    "text" : "GENERAL STATUS"
  },
  "text" : {
    "div" : "GENERAL STATUS NARRATIVE",
    "status" : "generated"
  }
} ]
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.2.5"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="GENERAL STATUS"
          code="10210-3">
      <originalText>GENERAL STATUS</originalText>
   </code>
   <title>GENERAL STATUS</title>
   <text>GENERAL STATUS NARRATIVE</text>
</section>
```
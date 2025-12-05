---
description: C-CDA Chief Complaint Section conversion rules and FHIR resource mapping for clinical document transformation.
---

# Chief Complaint Section

OID: 1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1

LOINCs: #{"10154-3"}

Alias: chief-complaint

Entries Required: N/A

Internal ID: ChiefComplaintSection

[IG Link](https://www.hl7.org/ccdasearch/templates/1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1.html)

## sample1

```json
{
  "title" : "CHIEF COMPLAINT",
  "text" : {
    "div" : "Back Pain",
    "status" : "generated"
  },
  "code" : {
    "text" : "CHIEF COMPLAINT",
    "coding" : [ {
      "code" : "10154-3",
      "display" : "CHIEF COMPLAINT",
      "system" : "http://loinc.org"
    } ]
  }
}
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="CHIEF COMPLAINT"
          code="10154-3">
      <originalText>CHIEF COMPLAINT</originalText>
   </code>
   <title>CHIEF COMPLAINT</title>
   <text>Back Pain</text>
</section>
```
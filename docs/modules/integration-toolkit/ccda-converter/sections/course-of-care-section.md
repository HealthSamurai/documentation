---
description: C-CDA Course of Care Section conversion rules and FHIR resource mapping for clinical document transformation.
---

# Course of Care Section

OID: 2.16.840.1.113883.10.20.22.2.64

LOINCs: #{"8648-8"}

Alias: course-of-care

Entries Required: N/A

Internal ID: CourseofCareSection

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.64.html)

## sample1

```json
[ {
  "title" : "COURSE OF CARE",
  "code" : {
    "coding" : [ {
      "code" : "8648-8",
      "system" : "http://loinc.org",
      "display" : "COURSE OF CARE"
    } ],
    "text" : "COURSE OF CARE"
  },
  "text" : {
    "div" : "COURSE OF CARE NARRATIVE",
    "status" : "generated"
  }
} ]
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="1.3.6.1.4.1.19376.1.5.3.1.3.5"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="COURSE OF CARE"
          code="8648-8">
      <originalText>COURSE OF CARE</originalText>
   </code>
   <title>COURSE OF CARE</title>
   <text>COURSE OF CARE NARRATIVE</text>
</section>
```
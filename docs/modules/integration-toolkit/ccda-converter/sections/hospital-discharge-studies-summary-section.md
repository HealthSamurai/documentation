---
description: C-CDA Hospital Discharge Studies Summary Section conversion rules and FHIR resource mapping for clinical document transformation.
---

# Hospital Discharge Studies Summary Section

OID: 2.16.840.1.113883.10.20.22.2.16

LOINCs: #{"11493-4"}

Alias: hospital-discharge-studies-summary

Entries Required: N/A

Internal ID: HospitalDischargeStudiesSummarySec

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.16.html)

## sample1

```json
[ {
  "title" : "HOSPITAL DISCHARGE STUDIES SUMMARY",
  "code" : {
    "coding" : [ {
      "code" : "11493-4",
      "system" : "http://loinc.org",
      "display" : "HOSPITAL DISCHARGE STUDIES SUMMARY"
    } ],
    "text" : "HOSPITAL DISCHARGE STUDIES SUMMARY"
  },
  "text" : {
    "div" : "HOSPITAL DISCHARGE STUDIES SUMMARY NARRATIVE",
    "status" : "generated"
  }
} ]
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.16"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="HOSPITAL DISCHARGE STUDIES SUMMARY"
          code="11493-4">
      <originalText>HOSPITAL DISCHARGE STUDIES SUMMARY</originalText>
   </code>
   <title>HOSPITAL DISCHARGE STUDIES SUMMARY</title>
   <text>HOSPITAL DISCHARGE STUDIES SUMMARY NARRATIVE</text>
</section>
```
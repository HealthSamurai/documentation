---
description: C-CDA Review of Systems Section conversion rules and FHIR resource mapping for clinical document transformation.
---

# Review of Systems Section

OID: 1.3.6.1.4.1.19376.1.5.3.1.3.18

LOINCs: #{"10187-3"}

Alias: review-of-systems

Entries Required: N/A

Internal ID: ReviewofSystemsSection

[IG Link](https://www.hl7.org/ccdasearch/templates/1.3.6.1.4.1.19376.1.5.3.1.3.18.html)

## sample1

```json
{
  "title" : "REVIEW OF SYSTEMS",
  "text" : {
    "div" : "\n    <paragraph>\n         Patient denies recent history of fever or malaise. Positive\n         For weakness and shortness of breath. One episode of melena. No recent\n        headaches. Positive for osteoarthritis in hips, knees and hands.\n      </paragraph>\n  ",
    "status" : "generated"
  },
  "code" : {
    "text" : "REVIEW OF SYSTEMS",
    "coding" : [ {
      "code" : "10187-3",
      "display" : "REVIEW OF SYSTEMS",
      "system" : "http://loinc.org"
    } ]
  }
}
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="1.3.6.1.4.1.19376.1.5.3.1.3.18"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="REVIEW OF SYSTEMS"
          code="10187-3">
      <originalText>REVIEW OF SYSTEMS</originalText>
   </code>
   <title>REVIEW OF SYSTEMS</title>
   <text>
      <paragraph>
         Patient denies recent history of fever or malaise. Positive
         For weakness and shortness of breath. One episode of melena. No recent
        headaches. Positive for osteoarthritis in hips, knees and hands.
      </paragraph>
   </text>
</section>
```
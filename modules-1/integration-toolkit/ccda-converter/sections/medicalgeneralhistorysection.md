# Medical (General) History Section

OID: 2.16.840.1.113883.10.20.22.2.39

LOINCs: #{"11329-0"}

Alias: medical-general-history

Entries Required: N/A

Internal ID: MedicalGeneralHistorySection

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.39.html)

## sample1

```json
[ {
  "title" : "MEDICAL GENERAL HISTORY",
  "code" : {
    "coding" : [ {
      "code" : "11329-0",
      "system" : "http://loinc.org",
      "display" : "MEDICAL GENERAL HISTORY"
    } ],
    "text" : "MEDICAL GENERAL HISTORY"
  },
  "text" : {
    "div" : "MEDICAL GENERAL HISTORY NARRATIVE",
    "status" : "generated"
  }
} ]
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.39"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="MEDICAL GENERAL HISTORY"
          code="11329-0">
      <originalText>MEDICAL GENERAL HISTORY</originalText>
   </code>
   <title>MEDICAL GENERAL HISTORY</title>
   <text>MEDICAL GENERAL HISTORY NARRATIVE</text>
</section>
```
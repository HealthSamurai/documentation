# Hospital Consultations Section

OID: 2.16.840.1.113883.10.20.22.2.42

LOINCs: #{"18841-7"}

Alias: hospital-consultations

Entries Required: N/A

Internal ID: HospitalConsultationsSection

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.42.html)

## sample1

```json
[ {
  "title" : "HOSPITAL CONSULTATIONS",
  "code" : {
    "coding" : [ {
      "code" : "18841-7",
      "system" : "http://loinc.org",
      "display" : "HOSPITAL CONSULTATIONS"
    } ],
    "text" : "HOSPITAL CONSULTATIONS"
  },
  "text" : {
    "div" : "HOSPITAL CONSULTATIONS NARRATIVE",
    "status" : "generated"
  }
} ]
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.42"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="HOSPITAL CONSULTATIONS"
          code="18841-7">
      <originalText>HOSPITAL CONSULTATIONS</originalText>
   </code>
   <title>HOSPITAL CONSULTATIONS</title>
   <text>HOSPITAL CONSULTATIONS NARRATIVE</text>
</section>
```
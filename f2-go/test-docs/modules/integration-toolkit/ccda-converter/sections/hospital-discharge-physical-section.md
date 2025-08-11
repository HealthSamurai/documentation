# Hospital Discharge Physical Section

OID: 1.3.6.1.4.1.19376.1.5.3.1.3.26

LOINCs: #{"10184-0"}

Alias: hospital-discharge-physical

Entries Required: N/A

Internal ID: HospitalDischargePhysicalSection

[IG Link](https://www.hl7.org/ccdasearch/templates/1.3.6.1.4.1.19376.1.5.3.1.3.26.html)

## sample1

```json
[ {
  "title" : "HOSPITAL DISCHARGE PHYSICAL",
  "code" : {
    "coding" : [ {
      "code" : "10184-0",
      "system" : "http://loinc.org",
      "display" : "HOSPITAL DISCHARGE PHYSICAL"
    } ],
    "text" : "HOSPITAL DISCHARGE PHYSICAL"
  },
  "text" : {
    "div" : "HOSPITAL DISCHARGE PHYSICAL NARRATIVE",
    "status" : "generated"
  }
} ]
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="1.3.6.1.4.1.19376.1.5.3.1.3.26"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="HOSPITAL DISCHARGE PHYSICAL"
          code="10184-0">
      <originalText>HOSPITAL DISCHARGE PHYSICAL</originalText>
   </code>
   <title>HOSPITAL DISCHARGE PHYSICAL</title>
   <text>HOSPITAL DISCHARGE PHYSICAL NARRATIVE</text>
</section>
```
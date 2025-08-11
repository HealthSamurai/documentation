# Chief Complaint and Reason for Visit Section

OID: 2.16.840.1.113883.10.20.22.2.13

LOINCs: #{"46239-0"}

Alias: chief-complaint-and-reason-for-visit

Entries Required: N/A

Internal ID: ChiefComplaintandReasonforVisitS

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.13.html)

## sample1

```json
{
  "title" : "CHIEF COMPLAINT AND REASON FOR VISIT",
  "text" : {
    "div" : "Back Pain",
    "status" : "generated"
  },
  "code" : {
    "text" : "CHIEF COMPLAINT AND REASON FOR VISIT",
    "coding" : [ {
      "code" : "46239-0",
      "display" : "CHIEF COMPLAINT AND REASON FOR VISIT",
      "system" : "http://loinc.org"
    } ]
  }
}
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.13"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="CHIEF COMPLAINT AND REASON FOR VISIT"
          code="46239-0">
      <originalText>CHIEF COMPLAINT AND REASON FOR VISIT</originalText>
   </code>
   <title>CHIEF COMPLAINT AND REASON FOR VISIT</title>
   <text>Back Pain</text>
</section>
```
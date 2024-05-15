# Reason for Visit Section

OID: 2.16.840.1.113883.10.20.22.2.12

LOINCs: #{"29299-5"}

Alias: reason-for-visit

Entries Required: N/A

Internal ID: ReasonforVisitSection

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.12.html)

## sample1

```json
{
  "title" : "REASON FOR VISIT",
  "text" : {
    "div" : "\n    <paragraph>Back pain</paragraph>\n  ",
    "status" : "generated"
  },
  "code" : {
    "text" : "REASON FOR VISIT",
    "coding" : [ {
      "code" : "29299-5",
      "display" : "REASON FOR VISIT",
      "system" : "http://loinc.org"
    } ]
  }
}
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.12"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="REASON FOR VISIT"
          code="29299-5">
      <originalText>REASON FOR VISIT</originalText>
   </code>
   <title>REASON FOR VISIT</title>
   <text>
      <paragraph>Back pain</paragraph>
   </text>
</section>
```
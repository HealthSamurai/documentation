# Operative Note Surgical Procedure Section

OID: 2.16.840.1.113883.10.20.7.14

LOINCs: #{"10223-6"}

Alias: operative-note-surgical-procedure

Entries Required: N/A

Internal ID: OperativeNoteSurgicalProcedureSect

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.7.14.html)

## sample1

```json
[ {
  "title" : "OPERATIVE NOTE SURGICAL PROCEDURE",
  "code" : {
    "coding" : [ {
      "code" : "10223-6",
      "system" : "http://loinc.org",
      "display" : "OPERATIVE NOTE SURGICAL PROCEDURE"
    } ],
    "text" : "OPERATIVE NOTE SURGICAL PROCEDURE"
  },
  "text" : {
    "div" : "OPERATIVE NOTE SURGICAL PROCEDURE NARRATIVE",
    "status" : "generated"
  }
} ]
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.7.14"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="OPERATIVE NOTE SURGICAL PROCEDURE"
          code="10223-6">
      <originalText>OPERATIVE NOTE SURGICAL PROCEDURE</originalText>
   </code>
   <title>OPERATIVE NOTE SURGICAL PROCEDURE</title>
   <text>OPERATIVE NOTE SURGICAL PROCEDURE NARRATIVE</text>
</section>
```
# Procedure Disposition Section

OID: 2.16.840.1.113883.10.20.18.2.12

LOINCs: #{"59775-7"}

Alias: procedure-disposition

Entries Required: N/A

Internal ID: ProcedureDispositionSection

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.18.2.12.html)

## sample1

```json
[ {
  "title" : "PROCEDURE DISPOSITION",
  "code" : {
    "coding" : [ {
      "code" : "59775-7",
      "system" : "http://loinc.org",
      "display" : "PROCEDURE DISPOSITION"
    } ],
    "text" : "PROCEDURE DISPOSITION"
  },
  "text" : {
    "div" : "PROCEDURE DISPOSITION NARRATIVE",
    "status" : "generated"
  }
} ]
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.18.2.12"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="PROCEDURE DISPOSITION"
          code="59775-7">
      <originalText>PROCEDURE DISPOSITION</originalText>
   </code>
   <title>PROCEDURE DISPOSITION</title>
   <text>PROCEDURE DISPOSITION NARRATIVE</text>
</section>
```
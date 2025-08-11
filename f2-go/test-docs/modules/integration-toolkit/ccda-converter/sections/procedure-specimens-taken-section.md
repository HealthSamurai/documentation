# Procedure Specimens Taken Section

OID: 2.16.840.1.113883.10.20.22.2.31

LOINCs: #{"59773-2"}

Alias: procedure-specimens-taken

Entries Required: N/A

Internal ID: ProcedureSpecimensTakenSection

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.31.html)

## sample1

```json
[ {
  "title" : "PROCEDURE SPECIMENS TAKEN",
  "code" : {
    "coding" : [ {
      "code" : "59773-2",
      "system" : "http://loinc.org",
      "display" : "PROCEDURE SPECIMENS TAKEN"
    } ],
    "text" : "PROCEDURE SPECIMENS TAKEN"
  },
  "text" : {
    "div" : "PROCEDURE SPECIMENS TAKEN NARRATIVE",
    "status" : "generated"
  }
} ]
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.31"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="PROCEDURE SPECIMENS TAKEN"
          code="59773-2">
      <originalText>PROCEDURE SPECIMENS TAKEN</originalText>
   </code>
   <title>PROCEDURE SPECIMENS TAKEN</title>
   <text>PROCEDURE SPECIMENS TAKEN NARRATIVE</text>
</section>
```
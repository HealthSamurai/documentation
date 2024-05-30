# Procedure Implants Section

OID: 2.16.840.1.113883.10.20.22.2.40

LOINCs: #{"59771-6"}

Alias: procedure-implants

Entries Required: N/A

Internal ID: ProcedureImplantsSection

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.40.html)

## sample1

```json
[ {
  "title" : "PROCEDURE IMPLANTS",
  "code" : {
    "coding" : [ {
      "code" : "59771-6",
      "system" : "http://loinc.org",
      "display" : "PROCEDURE IMPLANTS"
    } ],
    "text" : "PROCEDURE IMPLANTS"
  },
  "text" : {
    "div" : "PROCEDURE IMPLANTS NARRATIVE",
    "status" : "generated"
  }
} ]
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.40"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="PROCEDURE IMPLANTS"
          code="59771-6">
      <originalText>PROCEDURE IMPLANTS</originalText>
   </code>
   <title>PROCEDURE IMPLANTS</title>
   <text>PROCEDURE IMPLANTS NARRATIVE</text>
</section>
```
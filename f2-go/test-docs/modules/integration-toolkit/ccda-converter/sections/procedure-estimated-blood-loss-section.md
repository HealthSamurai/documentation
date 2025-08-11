# Procedure Estimated Blood Loss Section

OID: 2.16.840.1.113883.10.20.18.2.9

LOINCs: #{"59770-8"}

Alias: procedure-estimated-blood-loss

Entries Required: N/A

Internal ID: ProcedureEstimatedBloodLossSection

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.18.2.9.html)

## sample1

```json
[ {
  "title" : "PROCEDURE ESTIMATED BLOOD LOSS",
  "code" : {
    "coding" : [ {
      "code" : "59770-8",
      "system" : "http://loinc.org",
      "display" : "PROCEDURE ESTIMATED BLOOD LOSS"
    } ],
    "text" : "PROCEDURE ESTIMATED BLOOD LOSS"
  },
  "text" : {
    "div" : "PROCEDURE ESTIMATED BLOOD LOSS NARRATIVE",
    "status" : "generated"
  }
} ]
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.18.2.9"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="PROCEDURE ESTIMATED BLOOD LOSS"
          code="59770-8">
      <originalText>PROCEDURE ESTIMATED BLOOD LOSS</originalText>
   </code>
   <title>PROCEDURE ESTIMATED BLOOD LOSS</title>
   <text>PROCEDURE ESTIMATED BLOOD LOSS NARRATIVE</text>
</section>
```
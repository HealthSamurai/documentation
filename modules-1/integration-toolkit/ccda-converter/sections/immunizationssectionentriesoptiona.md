# Immunizations Section (entries optional) (V3)

OID: 2.16.840.1.113883.10.20.22.2.2

LOINCs: #{"11369-6"}

Alias: immunizations

Entries Required: N/A

Internal ID: ImmunizationsSectionentriesoptiona

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.2.html)

## Immunization refusal example
NB Immunization.status


```json
{
  "patient" : {
    "resourceType" : "Patient",
    "id" : "patient"
  },
  "meta" : {
    "profile" : [ "http://hl7.org/fhir/us/core/StructureDefinition/us-core-immunization" ]
  },
  "vaccineCode" : {
    "coding" : [ {
      "code" : "166",
      "display" : "influenza, intradermal, quadrivalent, preservative free, injectable",
      "system" : "http://hl7.org/fhir/sid/cvx"
    } ]
  },
  "statusReason" : {
    "coding" : [ {
      "code" : "PATOBJ",
      "display" : "patient objection",
      "system" : "http://terminology.hl7.org/CodeSystem/v3-ActReason"
    } ]
  },
  "route" : null,
  "resourceType" : "Immunization",
  "recorded" : null,
  "primarySource" : false,
  "status" : "not-done",
  "id" : "SOME-STRING",
  "lotNumber" : [ ],
  "occurrence" : {
    "dateTime" : "2015-06-22T00:00:00-04:00"
  }
}
```

C-CDA Equivalent:
```xml
<entry>
   <substanceAdministration classCode="SBADM" moodCode="EVN">
      <templateId root="2.16.840.1.113883.10.20.22.4.52" extension="2015-08-01"/>
      <templateId root="2.16.840.1.113883.10.20.22.4.52"/>
      <id root="7df888b6-0268-1e62-ff60-ceb2f2f88630"/>
      <statusCode code="completed"/>
      <effectiveTime value="20150622000000-0400"/>
      <consumable>
         <manufacturedProduct classCode="MANU">
            <templateId root="2.16.840.1.113883.10.20.22.4.54" extension="2014-06-09"/>
            <templateId root="2.16.840.1.113883.10.20.22.4.54"/>
            <manufacturedMaterial>
               <code codeSystem="2.16.840.1.113883.12.292"
                      codeSystemName="HTTP://HL7.ORG/FHIR/SID/CVX"
                      displayName="influenza, intradermal, quadrivalent, preservative free, injectable"
                      code="166"/>
            </manufacturedMaterial>
         </manufacturedProduct>
      </consumable>
   </substanceAdministration>
</entry>
```

## sample1

```json
{
  "resourceType" : "Immunization",
  "id" : "SOME-STRING",
  "status" : "completed",
  "lotNumber" : "1",
  "vaccineCode" : {
    "coding" : [ {
      "code" : "88",
      "display" : "Influenza virus vaccine",
      "system" : "http://hl7.org/fhir/sid/cvx"
    } ]
  },
  "occurrence" : {
    "dateTime" : "2010-08-15"
  },
  "patient" : {
    "resourceType" : "Patient",
    "id" : "patient"
  },
  "primarySource" : false
}
```

C-CDA Equivalent:
```xml
<entry>
   <substanceAdministration classCode="SBADM" moodCode="EVN">
      <templateId root="2.16.840.1.113883.10.20.22.4.52" extension="2015-08-01"/>
      <templateId root="2.16.840.1.113883.10.20.22.4.52"/>
      <id root="7df888b6-0268-1e62-ff60-ceb2f2f88630"/>
      <statusCode code="completed"/>
      <effectiveTime value="20100815"/>
      <consumable>
         <manufacturedProduct classCode="MANU">
            <templateId root="2.16.840.1.113883.10.20.22.4.54" extension="2014-06-09"/>
            <templateId root="2.16.840.1.113883.10.20.22.4.54"/>
            <manufacturedMaterial>
               <code codeSystem="2.16.840.1.113883.12.292"
                      codeSystemName="HTTP://HL7.ORG/FHIR/SID/CVX"
                      displayName="Influenza virus vaccine"
                      code="88"/>
               <lotNumberText>1</lotNumberText>
            </manufacturedMaterial>
         </manufacturedProduct>
      </consumable>
   </substanceAdministration>
</entry>
```
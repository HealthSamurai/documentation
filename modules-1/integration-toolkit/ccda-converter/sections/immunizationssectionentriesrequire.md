# Immunizations Section (entries required) (V3)

OID: 2.16.840.1.113883.10.20.22.2.2.1

LOINCs: #{"11369-6"}

Alias: immunizations

Entries Required: true

Internal ID: ImmunizationsSectionentriesrequire

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.2.1.html)

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
  "id" : "09f1fbb5-766e-487c-8029-c3bf6cb056f0",
  "occurrence" : {
    "dateTime" : "2015-06-22T00:00:00-04:00"
  }
}
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.2.1" extension="2015-08-01"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.2.1"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="11369-6"/>
   <title>Immunizations Section (entries required) (V3)</title>
   <text>
      <table border="1" width="100%">
         <thead>
            <tr>
               <th>Vaccine</th>
               <th>Date</th>
               <th>Status</th>
               <th>Lot</th>
               <th>Notes</th>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td>influenza, intradermal, quadrivalent, preservative free, injectable (166)</td>
               <td>2015-06-22T00:00:00-04:00</td>
               <td>not-done</td>
               <td>~</td>
               <td>~</td>
            </tr>
         </tbody>
      </table>
   </text>
   <entry>
      <substanceAdministration negationInd="false" moodCode="EVN" classCode="SBADM">
         <templateId root="2.16.840.1.113883.10.20.22.4.52" extension="2015-08-01"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.52"/>
         <id root="09f1fbb5-766e-487c-8029-c3bf6cb056f0"/>
         <statusCode code="completed"/>
         <effectiveTime value="20150622000000-0400"/>
         <consumable>
            <manufacturedProduct classCode="MANU">
               <templateId root="2.16.840.1.113883.10.20.22.4.54" extension="2014-06-09"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.54"/>
               <manufacturedMaterial>
                  <code codeSystem="2.16.840.1.113883.12.292"
                         codeSystemName="http://hl7.org/fhir/sid/cvx"
                         displayName="influenza, intradermal, quadrivalent, preservative free, injectable"
                         code="166"/>
               </manufacturedMaterial>
            </manufacturedProduct>
         </consumable>
         <entryRelationship typeCode="COMP" inversionInd="true">
            <observation classCode="OBS" moodCode="EVN">
               <templateId root="2.16.840.1.113883.10.20.22.4.53"/>
               <id root="52d23ddb-dd7d-4be7-85c5-9fa8e28abf90"/>
               <code code="PATOBJ"
                      codeSystem="2.16.840.1.113883.5.8"
                      codeSystemName="HL7 Act Reason"
                      displayName="patient objection"/>
               <statusCode code="completed"/>
            </observation>
         </entryRelationship>
      </substanceAdministration>
   </entry>
</section>
```

## sample1

```json
{
  "resourceType" : "Immunization",
  "id" : "e6f1ba43-c0ed-4b9b-9f12-f435d8ad8f92",
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
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.2.1" extension="2015-08-01"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.2.1"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="11369-6"/>
   <title>Immunizations Section (entries required) (V3)</title>
   <text>
      <table border="1" width="100%">
         <thead>
            <tr>
               <th>Vaccine</th>
               <th>Date</th>
               <th>Status</th>
               <th>Lot</th>
               <th>Notes</th>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td>Influenza virus vaccine (88)</td>
               <td>2010-08-15</td>
               <td>completed</td>
               <td>1</td>
               <td>~</td>
            </tr>
         </tbody>
      </table>
   </text>
   <entry>
      <substanceAdministration negationInd="false" classCode="SBADM" moodCode="EVN">
         <templateId root="2.16.840.1.113883.10.20.22.4.52" extension="2015-08-01"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.52"/>
         <id root="e6f1ba43-c0ed-4b9b-9f12-f435d8ad8f92"/>
         <statusCode code="completed"/>
         <effectiveTime value="20100815"/>
         <consumable>
            <manufacturedProduct classCode="MANU">
               <templateId root="2.16.840.1.113883.10.20.22.4.54" extension="2014-06-09"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.54"/>
               <manufacturedMaterial>
                  <code codeSystem="2.16.840.1.113883.12.292"
                         codeSystemName="http://hl7.org/fhir/sid/cvx"
                         displayName="Influenza virus vaccine"
                         code="88"/>
                  <lotNumberText>1</lotNumberText>
               </manufacturedMaterial>
            </manufacturedProduct>
         </consumable>
      </substanceAdministration>
   </entry>
</section>
```
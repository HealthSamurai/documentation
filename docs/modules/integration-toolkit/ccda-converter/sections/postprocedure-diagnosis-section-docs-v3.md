# Postprocedure Diagnosis Section (/V3)

OID: 2.16.840.1.113883.10.20.22.2.36

LOINCs: #{"59769-0"}

Alias: postprocedure-diagnosis

Entries Required: N/A

Internal ID: PostprocedureDiagnosisSectionV3

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.36.html)

## sample1

```json
[ {
  "onset" : {
    "dateTime" : "2012-09-09T19:04:00-04:00"
  },
  "category" : [ {
    "coding" : [ {
      "system" : "http://terminology.hl7.org/CodeSystem/condition-category",
      "code" : "problem-list-item",
      "display" : "Problem List Item"
    } ]
  } ],
  "clinicalStatus" : {
    "coding" : [ {
      "code" : "active",
      "system" : "http://terminology.hl7.org/CodeSystem/condition-clinical"
    } ]
  },
  "meta" : {
    "profile" : [ "http://hl7.org/fhir/us/core/StructureDefinition/us-core-condition-problems-health-concerns" ]
  },
  "resourceType" : "Condition",
  "recordedDate" : "2008-08-14T10:30:00-08:00",
  "id" : "5a784260-6856-4f38-9638-80c751aff2fb",
  "recorder" : {
    "practitioner" : {
      "id" : "bfb13e46-16ac-d6f5-d42c-5de5641e8f56",
      "identifier" : [ {
        "value" : "555555555",
        "system" : "http://hl7.org/fhir/sid/us-npi"
      } ],
      "resourceType" : "Practitioner"
    },
    "specialty" : [ {
      "text" : "Allopathic & Osteopathic Physicians; Family Medicine, Adult Medicine",
      "coding" : [ {
        "code" : "207QA0505X",
        "display" : "Allopathic & Osteopathic Physicians; Family Medicine, Adult Medicine",
        "system" : "http://hl7.org/fhir/ValueSet/provider-taxonomy"
      } ]
    } ],
    "id" : "e6e7cc9c-dc7d-c6cd-45aa-c1bc2e53921a",
    "resourceType" : "PractitionerRole",
    "period" : {
      "start" : "2008-08-14T10:30:00-08:00"
    }
  },
  "code" : {
    "text" : "Pneumonia",
    "coding" : [ {
      "code" : "233604007",
      "display" : "Pneumonia",
      "system" : "http://snomed.info/sct"
    } ]
  },
  "subject" : {
    "id" : "patient",
    "resourceType" : "Patient"
  }
} ]
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.36" extension="2015-08-01"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.36"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="59769-0"/>
   <title>Postprocedure Diagnosis Section (V3)</title>
   <text>
      <table border="1" width="100%">
         <caption>Active Problems</caption>
         <thead>
            <tr>
               <th>Problem</th>
               <th>Noted Date</th>
               <th>Diagnosed Date</th>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td>Pneumonia</td>
               <td>09/09/2012 11:04PM UTC</td>
               <td>08/14/2008 6:30PM UTC</td>
            </tr>
         </tbody>
      </table>
      <table border="1" width="100%">
         <caption>Resolved Problems</caption>
         <thead>
            <tr>
               <th>Problem</th>
               <th>Noted Date</th>
               <th>Diagnosed Date</th>
               <th>Resolved Date</th>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td colspan="4">No records</td>
            </tr>
         </tbody>
      </table>
   </text>
   <entry>
      <act classCode="ACT" moodCode="EVN">
         <templateId root="2.16.840.1.113883.10.20.22.4.51" extension="2015-08-01"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.51"/>
         <id root="5a784260-6856-4f38-9638-80c751aff2fb"/>
         <code code="59769-0" codeSystem="2.16.840.1.113883.6.1"/>
         <statusCode code="active"/>
         <effectiveTime>
            <low value="20120909190400-0400"/>
            <high nullFlavor="NA"/>
         </effectiveTime>
         <entryRelationship typeCode="SUBJ">
            <observation moodCode="EVN" classCode="OBS">
               <templateId root="2.16.840.1.113883.10.20.22.4.4" extension="2015-08-01"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.4"/>
               <id root="5a784260-6856-4f38-9638-80c751aff2fb"/>
               <code xsi:type="CD"
                      code="55607006"
                      displayName="Problem"
                      codeSystemName="SNOMED-CT"
                      codeSystem="2.16.840.1.113883.6.96">
                  <translation nullFlavor="UNK"/>
               </code>
               <statusCode code="completed"/>
               <effectiveTime>
                  <low value="20120909190400-0400"/>
                  <high nullFlavor="NI"/>
               </effectiveTime>
               <value codeSystem="2.16.840.1.113883.6.96"
                       codeSystemName="http://snomed.info/sct"
                       displayName="Pneumonia"
                       code="233604007"
                       xsi:type="CD">
                  <originalText>Pneumonia</originalText>
               </value>
               <author>
                  <templateId root="2.16.840.1.113883.10.20.22.4.119"/>
                  <time value="20080814103000-0800"/>
                  <assignedAuthor>
                     <id root="e6e7cc9c-dc7d-c6cd-45aa-c1bc2e53921a"/>
                     <id extension="555555555" root="2.16.840.1.113883.4.6"/>
                     <code codeSystem="2.16.840.1.113883.6.101"
                            codeSystemName="http://hl7.org/fhir/ValueSet/provider-taxonomy"
                            displayName="Allopathic &amp; Osteopathic Physicians; Family Medicine, Adult Medicine"
                            code="207QA0505X">
                        <originalText>Allopathic &amp; Osteopathic Physicians; Family Medicine, Adult Medicine</originalText>
                     </code>
                     <addr nullFlavor="UNK"/>
                     <telecom nullFlavor="UNK"/>
                     <assignedPerson>
                        <name nullFlavor="UNK"/>
                     </assignedPerson>
                  </assignedAuthor>
               </author>
            </observation>
         </entryRelationship>
      </act>
   </entry>
</section>
```

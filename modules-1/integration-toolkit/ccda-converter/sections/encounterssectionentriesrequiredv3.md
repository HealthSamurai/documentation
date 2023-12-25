# Encounters Section (entries required) (V3)

OID: 2.16.840.1.113883.10.20.22.2.22.1

LOINCs: #{"46240-8"}

Alias: encounters

Entries Required: N/A

Internal ID: EncountersSectionentriesrequiredV3

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.22.1.html)

## Outpatient visit example

```json
{
  "diagnosis" : [ {
    "condition" : {
      "code" : {
        "coding" : [ {
          "code" : "64109004",
          "display" : "Costal Chondritis",
          "system" : "http://snomed.info/sct"
        } ]
      },
      "id" : "db734647-fc99-424c-a864-7e3cda82e704",
      "onset" : {
        "dateTime" : "2012-08-15"
      },
      "recorder" : null,
      "category" : [ {
        "coding" : [ {
          "system" : "http://terminology.hl7.org/CodeSystem/condition-category",
          "code" : "encounter-diagnosis",
          "display" : "Encounter Diagnosis"
        } ]
      } ],
      "subject" : {
        "id" : "patient",
        "resourceType" : "Patient"
      },
      "resourceType" : "Condition"
    }
  } ],
  "meta" : {
    "profile" : [ "http://hl7.org/fhir/us/core/StructureDefinition/us-core-encounter" ]
  },
  "type" : [ {
    "coding" : [ {
      "code" : "99213",
      "display" : "Office outpatient visit 15 minutes",
      "system" : "http://terminology.hl7.org/ValueSet/v3-ActEncounterCode"
    } ]
  } ],
  "resourceType" : "Encounter",
  "status" : "unknown",
  "id" : "2a620155-9d11-439e-92b3-5d9815ff4de8",
  "hospitalization" : {
    "dischargeDisposition" : {
      "coding" : [ {
        "code" : "04",
        "display" : "Discharged/Transferred to a Facility that Provides Custodial or Supportive Care",
        "system" : "urn:oid:2.16.840.1.113883.6.301.5"
      } ]
    }
  },
  "period" : {
    "start" : "2012-08-15"
  },
  "location" : [ {
    "location" : {
      "address" : {
        "line" : [ "1004 Healthcare Dr." ],
        "use" : null,
        "city" : "Portland",
        "state" : "OR",
        "postalCode" : "97005",
        "country" : null
      },
      "telecom" : [ {
        "system" : "phone",
        "value" : "+1(555)555-1004",
        "use" : null
      } ],
      "id" : "2a620155-9d11-439e-92b3-5d9815ff4de8",
      "resourceType" : "Location"
    }
  } ],
  "subject" : {
    "id" : "patient",
    "resourceType" : "Patient"
  },
  "reasonReference" : [ {
    "status" : "final",
    "code" : {
      "coding" : [ {
        "code" : "404684003",
        "display" : "Finding",
        "system" : "http://snomed.info/sct"
      } ]
    },
    "id" : "2a620155-9d11-439e-92b3-5d9815ff4de8",
    "value" : {
      "CodeableConcept" : {
        "coding" : [ {
          "code" : "233604007",
          "display" : "Pneumonia",
          "system" : "http://snomed.info/sct"
        } ]
      }
    },
    "effective" : {
      "Period" : {
        "start" : "2012-09-25T11:30:00-05:00",
        "end" : null
      }
    },
    "subject" : {
      "id" : "patient",
      "resourceType" : "Patient"
    },
    "resourceType" : "Observation"
  } ]
}
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.22.1" extension="2015-08-01"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.22.1"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="46240-8"/>
   <title>Encounters Section (entries required) (V3)</title>
   <text>
      <table border="1" width="100%">
         <thead>
            <tr>
               <td>Type</td>
               <td>Period</td>
               <td>Dx</td>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td>Office outpatient visit 15 minutes (99213)</td>
               <td>2012-08-15 - Now</td>
               <td>Costal Chondritis (64109004)</td>
            </tr>
         </tbody>
      </table>
   </text>
   <entry>
      <encounter moodCode="EVN" classCode="ENC">
         <sdtc:dischargeDispositionCode codeSystem="2.16.840.1.113883.6.301.5"
                                         codeSystemName="urn:oid:2.16.840.1.113883.6.301.5"
                                         displayName="Discharged/Transferred to a Facility that Provides Custodial or Supportive Care"
                                         code="04"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.49" extension="2015-08-01"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.49"/>
         <id root="2a620155-9d11-439e-92b3-5d9815ff4de8"/>
         <code codeSystem="2.16.840.1.113883.6.12"
                codeSystemName="http://terminology.hl7.org/ValueSet/v3-ActEncounterCode"
                displayName="Office outpatient visit 15 minutes"
                code="99213"/>
         <effectiveTime>
            <low value="20120815"/>
            <high nullFlavor="UNK"/>
         </effectiveTime>
         <participant typeCode="LOC">
            <participantRole classCode="SDLOC">
               <templateId root="2.16.840.1.113883.10.20.22.4.32"/>
               <id root="2a620155-9d11-439e-92b3-5d9815ff4de8"/>
               <code nullFlavor="NI"/>
               <addr>
                  <state>OR</state>
                  <city>Portland</city>
                  <postalCode>97005</postalCode>
                  <streetAddressLine>1004 Healthcare Dr.</streetAddressLine>
               </addr>
               <telecom value="+1(555)555-1004"/>
            </participantRole>
         </participant>
         <entryRelationship typeCode="RSON">
            <observation classCode="OBS" moodCode="EVN">
               <templateId root="2.16.840.1.113883.10.20.22.4.19" extension="2014-06-09"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.19"/>
               <id root="2a620155-9d11-439e-92b3-5d9815ff4de8"/>
               <code codeSystem="2.16.840.1.113883.6.96"
                      codeSystemName="http://snomed.info/sct"
                      displayName="Finding"
                      code="404684003"/>
               <statusCode code="completed"/>
               <effectiveTime>
                  <low value="20120925113000-0500"/>
                  <high nullFlavor="UNK"/>
               </effectiveTime>
               <value codeSystem="2.16.840.1.113883.6.96"
                       codeSystemName="http://snomed.info/sct"
                       displayName="Pneumonia"
                       code="233604007"
                       xsi:type="CD"/>
            </observation>
         </entryRelationship>
         <entryRelationship typeCode="SUBJ">
            <act classCode="ACT" moodCode="EVN">
               <templateId root="2.16.840.1.113883.10.20.22.4.80" extension="2015-08-01"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.80"/>
               <code code="29308-4" codeSystem="2.16.840.1.113883.6.1"/>
               <entryRelationship typeCode="SUBJ">
                  <observation classCode="OBS" moodCode="EVN">
                     <templateId root="2.16.840.1.113883.10.20.22.4.4" extension="2015-08-01"/>
                     <templateId root="2.16.840.1.113883.10.20.22.4.4"/>
                     <id root="db734647-fc99-424c-a864-7e3cda82e704"/>
                     <code xsi:type="CD"
                            code="55607006"
                            displayName="Problem"
                            codeSystemName="SNOMED-CT"
                            codeSystem="2.16.840.1.113883.6.96">
                        <translation nullFlavor="UNK"/>
                     </code>
                     <statusCode code="completed"/>
                     <effectiveTime>
                        <low value="20120815"/>
                        <high nullFlavor="NI"/>
                     </effectiveTime>
                     <value codeSystem="2.16.840.1.113883.6.96"
                             codeSystemName="http://snomed.info/sct"
                             displayName="Costal Chondritis"
                             code="64109004"
                             xsi:type="CD"/>
                  </observation>
               </entryRelationship>
            </act>
         </entryRelationship>
      </encounter>
   </entry>
</section>
```
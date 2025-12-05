---
description: C-CDA Problem section (/entries required) (/V3) conversion rules and FHIR resource mapping for clinical document transformation.
---

# Problem section (/entries required) (/V3)

OID: 2.16.840.1.113883.10.20.22.2.5.1

LOINCs: #{"11450-4"}

Alias: problems

Entries Required: true

Internal ID: ProblemSectionentriesrequiredV3

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.5.1.html)

## Condition to Observation section example.
The main idea that each Condition resource is related to nested Observation in Problems section (i.e. Act.ProblemObservation).


```json
{
  "onset" : {
    "dateTime" : "2014-03-02T12:45:36-05:00"
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
  "abatement" : {
    "dateTime" : "2014-04-02T12:45:36-05:05"
  },
  "resourceType" : "Condition",
  "recordedDate" : "2014-03-02T12:45:36Z",
  "id" : "SOME-STRING",
  "recorder" : {
    "practitioner" : {
      "name" : [ {
        "given" : [ "Heartly" ],
        "family" : "Sixer",
        "use" : "official",
        "suffix" : [ "MD" ]
      } ],
      "address" : [ {
        "line" : [ "6666 StreetName St." ],
        "use" : null,
        "city" : "Silver Spring",
        "state" : "MD",
        "postalCode" : "20901",
        "country" : "US"
      } ],
      "telecom" : [ {
        "system" : "phone",
        "value" : "+1(301)666-6666",
        "use" : "work"
      } ],
      "id" : "SOME-STRING",
      "resourceType" : "Practitioner"
    },
    "specialty" : [ {
      "text" : "Cardiovascular Disease",
      "coding" : [ {
        "code" : "207RC0000X",
        "display" : "Cardiovascular Disease",
        "system" : "http://hl7.org/fhir/ValueSet/provider-taxonomy"
      } ]
    } ],
    "resourceType" : "PractitionerRole",
    "id" : "4b309dce-a2a7-576a-4520-98f92c510568"
  },
  "code" : {
    "text" : "Community Acquired Pneumonia",
    "coding" : [ {
      "code" : "385093006",
      "display" : "Community Acquired Pneumonia",
      "system" : "http://snomed.info/sct"
    } ]
  },
  "subject" : {
    "id" : "patient",
    "resourceType" : "Patient"
  }
}
```

The section contains an Act.entryRelationship.Observation entry converted from the input Condition resource.
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.5" extension="2015-08-01"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.5"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="11450-4"/>
   <title>Problem Section (entries required) (V3)</title>
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
               <td>Community Acquired Pneumonia</td>
               <td>03/02/2014 5:45PM UTC</td>
               <td>03/02/2014 12:45PM UTC</td>
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
         <templateId root="2.16.840.1.113883.10.20.22.4.3" extension="2015-08-01"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.3"/>
         <id root="7df888b6-0268-1e62-ff60-ceb2f2f88630"/>
         <code code="CONC" codeSystem="2.16.840.1.113883.5.6"/>
         <statusCode code="active"/>
         <effectiveTime>
            <low value="20140302124536-0500"/>
            <high value="20140402124536-0505"/>
         </effectiveTime>
         <entryRelationship typeCode="SUBJ">
            <observation moodCode="EVN" classCode="OBS">
               <templateId root="2.16.840.1.113883.10.20.22.4.4" extension="2015-08-01"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.4"/>
               <id root="7df888b6-0268-1e62-ff60-ceb2f2f88630"/>
               <code xsi:type="CD"
                      code="55607006"
                      displayName="Problem"
                      codeSystemName="SNOMED-CT"
                      codeSystem="2.16.840.1.113883.6.96">
                  <translation nullFlavor="UNK"/>
               </code>
               <statusCode code="completed"/>
               <effectiveTime>
                  <low value="20140302124536-0500"/>
                  <high value="20140402124536-0505"/>
               </effectiveTime>
               <value codeSystem="2.16.840.1.113883.6.96"
                       codeSystemName="http://snomed.info/sct"
                       displayName="Community Acquired Pneumonia"
                       code="385093006"
                       xsi:type="CD">
                  <originalText>Community Acquired Pneumonia</originalText>
               </value>
               <author>
                  <templateId root="2.16.840.1.113883.10.20.22.4.119"/>
                  <time value="20140302124536-0000"/>
                  <assignedAuthor>
                     <id root="4b309dce-a2a7-576a-4520-98f92c510568"/>
                     <code codeSystem="2.16.840.1.113883.6.101"
                            codeSystemName="http://hl7.org/fhir/ValueSet/provider-taxonomy"
                            displayName="Cardiovascular Disease"
                            code="207RC0000X">
                        <originalText>Cardiovascular Disease</originalText>
                     </code>
                     <addr>
                        <country>US</country>
                        <state>MD</state>
                        <city>Silver Spring</city>
                        <postalCode>20901</postalCode>
                        <streetAddressLine>6666 StreetName St.</streetAddressLine>
                     </addr>
                     <telecom value="+1(301)666-6666" use="WP"/>
                     <assignedPerson>
                        <name use="L">
                           <given>Heartly</given>
                           <family>Sixer</family>
                           <suffix>MD</suffix>
                        </name>
                     </assignedPerson>
                  </assignedAuthor>
               </author>
            </observation>
         </entryRelationship>
      </act>
   </entry>
</section>
```

## sample1

```json
[ {
  "onset" : {
    "dateTime" : "2014-03-02T12:45:36-05:00"
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
  "abatement" : {
    "dateTime" : "2014-04-02T12:45:36-05:05"
  },
  "resourceType" : "Condition",
  "recordedDate" : "2014-03-02T12:45:36Z",
  "id" : "102ca2e9-884c-4523-a2b4-1b6c3469c397",
  "recorder" : {
    "resourceType" : "PractitionerRole",
    "id" : "abfba15e-d567-4173-b522-80e40d8b2321",
    "specialty" : [ {
      "text" : "Cardiovascular Disease",
      "coding" : [ {
        "code" : "207RC0000X",
        "display" : "Cardiovascular Disease",
        "system" : "http://hl7.org/fhir/ValueSet/provider-taxonomy"
      } ]
    } ],
    "practitioner" : {
      "name" : [ {
        "given" : [ "Heartly" ],
        "family" : "Sixer",
        "use" : "official",
        "suffix" : [ "MD" ]
      } ],
      "address" : [ {
        "line" : [ "6666 StreetName St." ],
        "use" : null,
        "city" : "Silver Spring",
        "state" : "MD",
        "postalCode" : "20901",
        "country" : "US"
      } ],
      "telecom" : [ {
        "system" : "phone",
        "value" : "+1(301)666-6666",
        "use" : "work"
      } ],
      "id" : "86f71898-e94e-469f-8851-00193b48ea85",
      "identifier" : [ {
        "value" : "66666",
        "system" : "http://hl7.org/fhir/sid/us-npi"
      } ],
      "resourceType" : "Practitioner"
    }
  },
  "code" : {
    "text" : "Community Acquired Pneumonia",
    "coding" : [ {
      "code" : "385093006",
      "display" : "Community Acquired Pneumonia",
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
   <templateId root="2.16.840.1.113883.10.20.22.2.5" extension="2015-08-01"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.5"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="11450-4"/>
   <title>Problem Section (entries required) (V3)</title>
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
               <td>Community Acquired Pneumonia</td>
               <td>03/02/2014 5:45PM UTC</td>
               <td>03/02/2014 12:45PM UTC</td>
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
         <templateId root="2.16.840.1.113883.10.20.22.4.3" extension="2015-08-01"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.3"/>
         <id root="102ca2e9-884c-4523-a2b4-1b6c3469c397"/>
         <code code="CONC" codeSystem="2.16.840.1.113883.5.6"/>
         <statusCode code="active"/>
         <effectiveTime>
            <low value="20140302124536-0500"/>
            <high value="20140402124536-0505"/>
         </effectiveTime>
         <entryRelationship typeCode="SUBJ">
            <observation moodCode="EVN" classCode="OBS">
               <templateId root="2.16.840.1.113883.10.20.22.4.4" extension="2015-08-01"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.4"/>
               <id root="102ca2e9-884c-4523-a2b4-1b6c3469c397"/>
               <code xsi:type="CD"
                      code="55607006"
                      displayName="Problem"
                      codeSystemName="SNOMED-CT"
                      codeSystem="2.16.840.1.113883.6.96">
                  <translation nullFlavor="UNK"/>
               </code>
               <statusCode code="completed"/>
               <effectiveTime>
                  <low value="20140302124536-0500"/>
                  <high value="20140402124536-0505"/>
               </effectiveTime>
               <value codeSystem="2.16.840.1.113883.6.96"
                       codeSystemName="http://snomed.info/sct"
                       displayName="Community Acquired Pneumonia"
                       code="385093006"
                       xsi:type="CD">
                  <originalText>Community Acquired Pneumonia</originalText>
               </value>
               <author>
                  <templateId root="2.16.840.1.113883.10.20.22.4.119"/>
                  <time value="20140302124536-0000"/>
                  <assignedAuthor>
                     <id root="abfba15e-d567-4173-b522-80e40d8b2321"/>
                     <id extension="66666" root="2.16.840.1.113883.4.6"/>
                     <code codeSystem="2.16.840.1.113883.6.101"
                            codeSystemName="http://hl7.org/fhir/ValueSet/provider-taxonomy"
                            displayName="Cardiovascular Disease"
                            code="207RC0000X">
                        <originalText>Cardiovascular Disease</originalText>
                     </code>
                     <addr>
                        <country>US</country>
                        <state>MD</state>
                        <city>Silver Spring</city>
                        <postalCode>20901</postalCode>
                        <streetAddressLine>6666 StreetName St.</streetAddressLine>
                     </addr>
                     <telecom value="+1(301)666-6666" use="WP"/>
                     <assignedPerson>
                        <name use="L">
                           <given>Heartly</given>
                           <family>Sixer</family>
                           <suffix>MD</suffix>
                        </name>
                     </assignedPerson>
                  </assignedAuthor>
               </author>
            </observation>
         </entryRelationship>
      </act>
   </entry>
</section>
```

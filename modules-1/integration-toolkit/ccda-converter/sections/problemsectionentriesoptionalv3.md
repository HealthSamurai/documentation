# Problem Section (entries optional) (V3)

OID: 2.16.840.1.113883.10.20.22.2.5

LOINCs: #{"11450-4"}

Alias: problems

Entries Required: N/A

Internal ID: ProblemSectionentriesoptionalV3

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.5.html)

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
    "qualification" : [ {
      "code" : {
        "text" : "Cardiovascular Disease",
        "coding" : [ {
          "code" : "207RC0000X",
          "display" : "Cardiovascular Disease",
          "system" : "http://hl7.org/fhir/ValueSet/provider-taxonomy"
        } ]
      }
    } ],
    "id" : "SOME-STRING",
    "resourceType" : "Practitioner"
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
                    codeSystemName="HTTP://SNOMED.INFO/SCT"
                    displayName="Community Acquired Pneumonia"
                    code="385093006"
                    xsi:type="CD"/>
            <author>
               <templateId root="2.16.840.1.113883.10.20.22.4.119"/>
               <time value="20140302124536"/>
               <assignedAuthor>
                  <id root="7df888b6-0268-1e62-ff60-ceb2f2f88630"/>
                  <code codeSystem="2.16.840.1.113883.6.101"
                         codeSystemName="HTTP://HL7.ORG/FHIR/VALUESET/PROVIDER-TAXONOMY"
                         displayName="Cardiovascular Disease"
                         code="207RC0000X"/>
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
```
# Functional Status Section (/V2)

OID: 2.16.840.1.113883.10.20.22.2.14

LOINCs: #{"47420-5"}

Alias: funcstatus

Entries Required: N/A

Internal ID: FunctionalStatusSectionV2

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.14.html)

## sample2

```json
{
  "id" : "a7bc1062-8649-42a0-833d-eed65bd017c9",
  "hasMember" : [ {
    "value" : {
      "CodeableConcept" : {
        "coding" : [ {
          "code" : "165245003",
          "display" : " Independent walking",
          "system" : "http://snomed.info/sct"
        } ]
      }
    },
    "resourceType" : "Observation",
    "status" : "final",
    "effective" : {
      "dateTime" : "2013-03-11"
    },
    "id" : "b63a8636-cfff-4461-b018-40ba58ba8b32",
    "code" : {
      "coding" : [ {
        "code" : "54522-8",
        "display" : "Functional status",
        "system" : "http://loinc.org"
      } ]
    },
    "issued" : "2013-07-06T11:45:00-08:00",
    "subject" : {
      "id" : "patient",
      "resourceType" : "Patient"
    },
    "performer" : [ {
      "resourceType" : "PractitionerRole",
      "id" : "b63a8636-cfff-4461-b018-53236c541111",
      "period" : {
        "start" : "2013-07-06T11:45:00-08:00"
      },
      "specialty" : [ {
        "coding" : [ {
          "code" : "207QA0505X",
          "display" : "Adult Medicine",
          "system" : "urn:oid:2.16.840.1.113883.5.53"
        } ]
      } ],
      "practitioner" : {
        "id" : "b63a8636-cfff-4461-b018-53236c541111",
        "resourceType" : "Practitioner"
      }
    } ]
  } ],
  "code" : {
    "coding" : [ {
      "code" : "d5",
      "display" : "Self-Care",
      "system" : "http://hl7.org/fhir/sid/icf"
    } ]
  },
  "status" : "final",
  "resourceType" : "Observation",
  "subject" : {
    "id" : "patient",
    "resourceType" : "Patient"
  }
}
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.14" extension="2014-06-09"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.14"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="47420-5"/>
   <title>Functional Status Section (V2)</title>
   <text>Functional Status Section (V2)</text>
   <entry>
      <organizer classCode="CLUSTER" moodCode="EVN">
         <templateId root="2.16.840.1.113883.10.20.22.4.66" extension="2014-06-09"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.66"/>
         <id root="a7bc1062-8649-42a0-833d-eed65bd017c9"/>
         <code codeSystem="2.16.840.1.113883.6.254"
                codeSystemName="http://hl7.org/fhir/sid/icf"
                displayName="Self-Care"
                code="d5"/>
         <statusCode code="completed"/>
         <component>
            <observation moodCode="EVN" classCode="OBS">
               <templateId root="2.16.840.1.113883.10.20.22.4.67" extension="2014-06-09"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.67"/>
               <id root="b63a8636-cfff-4461-b018-40ba58ba8b32"/>
               <code codeSystem="2.16.840.1.113883.6.1"
                      codeSystemName="http://loinc.org"
                      displayName="Functional status"
                      code="54522-8"/>
               <statusCode code="completed"/>
               <effectiveTime value="20130311"/>
               <value codeSystem="2.16.840.1.113883.6.96"
                       codeSystemName="http://snomed.info/sct"
                       displayName=" Independent walking"
                       code="165245003"
                       xsi:type="CD"/>
               <author>
                  <templateId root="2.16.840.1.113883.10.20.22.4.119"/>
                  <time value="20130706114500-0800"/>
                  <assignedAuthor>
                     <id root="b63a8636-cfff-4461-b018-53236c541111"/>
                     <code codeSystem="2.16.840.1.113883.5.53"
                            codeSystemName="urn:oid:2.16.840.1.113883.5.53"
                            displayName="Adult Medicine"
                            code="207QA0505X"/>
                     <addr nullFlavor="UNK"/>
                     <telecom nullFlavor="UNK"/>
                     <assignedPerson>
                        <name nullFlavor="UNK"/>
                     </assignedPerson>
                  </assignedAuthor>
               </author>
            </observation>
         </component>
      </organizer>
   </entry>
</section>
```

## sample1

```json
{
  "value" : {
    "CodeableConcept" : {
      "coding" : [ {
        "code" : "165245003",
        "display" : " Independent walking",
        "system" : "http://snomed.info/sct"
      } ]
    }
  },
  "resourceType" : "Observation",
  "status" : "final",
  "effective" : {
    "dateTime" : "2013-03-11"
  },
  "id" : "b63a8636-cfff-4461-b018-53236c544444",
  "code" : {
    "coding" : [ {
      "code" : "54522-8",
      "display" : "Functional status",
      "system" : "http://loinc.org"
    } ]
  },
  "issued" : "2013-07-06T11:45:00-08:00",
  "subject" : {
    "id" : "patient",
    "resourceType" : "Patient"
  },
  "performer" : [ {
    "resourceType" : "PractitionerRole",
    "period" : {
      "start" : "2013-07-06T11:45:00-08:00"
    },
    "id" : "b63a8636-cfff-4461-b018-53236c546666",
    "specialty" : [ {
      "coding" : [ {
        "code" : "207QA0505X",
        "display" : "Adult Medicine",
        "system" : "urn:oid:2.16.840.1.113883.5.53"
      } ]
    } ],
    "practitioner" : {
      "id" : "b63a8636-cfff-4461-b018-53236c546666",
      "resourceType" : "Practitioner"
    }
  } ]
}
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.14" extension="2014-06-09"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.14"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="47420-5"/>
   <title>Functional Status Section (V2)</title>
   <text>Functional Status Section (V2)</text>
   <entry>
      <observation moodCode="EVN" classCode="OBS">
         <templateId root="2.16.840.1.113883.10.20.22.4.67" extension="2014-06-09"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.67"/>
         <id root="b63a8636-cfff-4461-b018-53236c544444"/>
         <code codeSystem="2.16.840.1.113883.6.1"
                codeSystemName="http://loinc.org"
                displayName="Functional status"
                code="54522-8"/>
         <statusCode code="completed"/>
         <effectiveTime value="20130311"/>
         <value codeSystem="2.16.840.1.113883.6.96"
                 codeSystemName="http://snomed.info/sct"
                 displayName=" Independent walking"
                 code="165245003"
                 xsi:type="CD"/>
         <author>
            <templateId root="2.16.840.1.113883.10.20.22.4.119"/>
            <time value="20130706114500-0800"/>
            <assignedAuthor>
               <id root="b63a8636-cfff-4461-b018-53236c546666"/>
               <code codeSystem="2.16.840.1.113883.5.53"
                      codeSystemName="urn:oid:2.16.840.1.113883.5.53"
                      displayName="Adult Medicine"
                      code="207QA0505X"/>
               <addr nullFlavor="UNK"/>
               <telecom nullFlavor="UNK"/>
               <assignedPerson>
                  <name nullFlavor="UNK"/>
               </assignedPerson>
            </assignedAuthor>
         </author>
      </observation>
   </entry>
</section>
```

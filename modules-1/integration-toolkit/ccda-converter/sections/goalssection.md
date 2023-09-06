# Goals Section

OID: 2.16.840.1.113883.10.20.22.2.60

LOINC: 61146-7

Alias: goals

Internal ID: GoalsSection

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.60.html)

## sample2

```json
{
  "description" : {
    "coding" : [ {
      "code" : "404684003",
      "display" : "http://snomed.info/sct",
      "system" : "http://terminology.hl7.org/CodeSystem/goal-priority"
    } ]
  },
  "expressedBy" : {
    "name" : [ {
      "given" : [ "Mary" ],
      "family" : "McDonald",
      "use" : "official",
      "suffix" : [ "RN" ]
    } ],
    "qualification" : [ {
      "code" : {
        "text" : "Registered nurse",
        "coding" : [ {
          "code" : "163W00000X",
          "display" : "Registered nurse",
          "system" : "http://hl7.org/fhir/ValueSet/provider-taxonomy"
        } ]
      }
    } ],
    "id" : "d839038b-7172-4165-a760-467925b43857",
    "resourceType" : "Practitioner"
  },
  "resourceType" : "Goal",
  "extension" : [ {
    "value" : {
      "dateTime" : "2013-07-30"
    },
    "url" : "authoring-time"
  } ],
  "priority" : {
    "coding" : [ {
      "code" : "high-priority",
      "display" : "High Priority",
      "system" : "http://terminology.hl7.org/CodeSystem/goal-priority"
    } ]
  },
  "id" : "3700b3b0-fbed-11e2-b788-0800200c9a66",
  "target" : [ {
    "detail" : {
      "Range" : {
        "low" : {
          "value" : 98,
          "unit" : "[degF]"
        },
        "high" : {
          "value" : 99,
          "unit" : "[degF]"
        }
      }
    },
    "measure" : {
      "text" : "Negotiated Goal for Body Temperature",
      "coding" : [ {
        "code" : "8310-5",
        "display" : "Negotiated Goal for Body Temperature",
        "system" : "http://loinc.org"
      } ]
    },
    "due" : {
      "date" : "2015-06-23"
    }
  } ],
  "subject" : {
    "resourceType" : "Patient",
    "id" : "patient"
  },
  "lifecycleStatus" : "active"
}
```

C-CDA Equivalent:
```xml
<entry>
   <observation moodCode="GOL" classCode="OBS">
      <templateId root="2.16.840.1.113883.10.20.22.4.121"/>
      <id root="3700b3b0-fbed-11e2-b788-0800200c9a66"/>
      <code codeSystem="2.16.840.1.113883.6.1"
             codeSystemName="HTTP://LOINC.ORG"
             displayName="Negotiated Goal for Body Temperature"
             code="8310-5"/>
      <statusCode code="active"/>
      <effectiveTime value="20150623"/>
      <value xsi:type="IVL_PQ">
         <low value="98" unit="[degF]"/>
         <high value="99" unit="[degF]"/>
      </value>
      <author>
         <templateId root="2.16.840.1.113883.10.20.22.4.119"/>
         <time value="20130730"/>
         <assignedAuthor>
            <id root="d839038b-7172-4165-a760-467925b43857"/>
            <code codeSystem="2.16.840.1.113883.6.101"
                   codeSystemName="HTTP://HL7.ORG/FHIR/VALUESET/PROVIDER-TAXONOMY"
                   displayName="Registered nurse"
                   code="163W00000X"/>
            <addr nullFlavor="UNK"/>
            <telecom nullFlavor="UNK"/>
            <assignedPerson>
               <name use="L">
                  <given>Mary</given>
                  <family>McDonald</family>
                  <suffix>RN</suffix>
               </name>
            </assignedPerson>
         </assignedAuthor>
      </author>
      <entryRelationship typeCode="REFR">
         <observation classCode="OBS" moodCode="EVN">
            <templateId root="2.16.840.1.113883.10.20.22.4.143"/>
            <id root="453eb74e-0ab8-4db3-8caf-e73e9747eb6a"/>
            <code code="225773000" codeSystem="2.16.840.1.113883.6.96"/>
            <value code="394849002"
                    displayName="High Priority"
                    codeSystemName="SNOMED"
                    codeSystem="2.16.840.1.113883.6.96"
                    xsi:type="CD"/>
         </observation>
      </entryRelationship>
   </observation>
</entry>
```

## sample1

```json
{
  "description" : {
    "coding" : [ {
      "code" : "404684003",
      "display" : "http://snomed.info/sct",
      "system" : "http://terminology.hl7.org/CodeSystem/goal-priority"
    } ]
  },
  "expressedBy" : {
    "name" : [ {
      "given" : [ "Mary" ],
      "family" : "McDonald",
      "use" : "official",
      "suffix" : [ "RN" ]
    } ],
    "qualification" : [ {
      "code" : {
        "text" : "Registered nurse",
        "coding" : [ {
          "code" : "163W00000X",
          "display" : "Registered nurse",
          "system" : "http://hl7.org/fhir/ValueSet/provider-taxonomy"
        } ]
      }
    } ],
    "id" : "d839038b-7171-4165-a760-467925b43857",
    "resourceType" : "Practitioner"
  },
  "resourceType" : "Goal",
  "extension" : [ {
    "value" : {
      "dateTime" : "2013-07-30"
    },
    "url" : "authoring-time"
  } ],
  "priority" : { },
  "id" : "3700b3b0-fbed-11e2-b778-0800200c9a66",
  "target" : [ {
    "detail" : {
      "string" : "Need to gain more energy to do regular activities. (Visual Inspection)"
    },
    "measure" : {
      "text" : "Resident's overall goal established during assessment process",
      "coding" : [ {
        "code" : "58144-7",
        "display" : "Resident's overall goal established during assessment process",
        "system" : "http://loinc.org"
      } ]
    },
    "due" : {
      "date" : "2015-06-23"
    }
  } ],
  "subject" : {
    "resourceType" : "Patient",
    "id" : "patient"
  },
  "lifecycleStatus" : "active"
}
```

C-CDA Equivalent:
```xml
<entry>
   <observation moodCode="GOL" classCode="OBS">
      <templateId root="2.16.840.1.113883.10.20.22.4.121"/>
      <id root="3700b3b0-fbed-11e2-b778-0800200c9a66"/>
      <code codeSystem="2.16.840.1.113883.6.1"
             codeSystemName="HTTP://LOINC.ORG"
             displayName="Resident's overall goal established during assessment process"
             code="58144-7"/>
      <statusCode code="active"/>
      <effectiveTime value="20150623"/>
      <value xsi:type="ST">Need to gain more energy to do regular activities. (Visual Inspection)</value>
      <author>
         <templateId root="2.16.840.1.113883.10.20.22.4.119"/>
         <time value="20130730"/>
         <assignedAuthor>
            <id root="d839038b-7171-4165-a760-467925b43857"/>
            <code codeSystem="2.16.840.1.113883.6.101"
                   codeSystemName="HTTP://HL7.ORG/FHIR/VALUESET/PROVIDER-TAXONOMY"
                   displayName="Registered nurse"
                   code="163W00000X"/>
            <addr nullFlavor="UNK"/>
            <telecom nullFlavor="UNK"/>
            <assignedPerson>
               <name use="L">
                  <given>Mary</given>
                  <family>McDonald</family>
                  <suffix>RN</suffix>
               </name>
            </assignedPerson>
         </assignedAuthor>
      </author>
   </observation>
</entry>
```
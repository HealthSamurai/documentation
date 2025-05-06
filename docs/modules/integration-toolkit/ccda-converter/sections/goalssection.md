# Goals Section

OID: 2.16.840.1.113883.10.20.22.2.60

LOINCs: #{"61146-7"}

Alias: goals

Entries Required: N/A

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
    "resourceType" : "PractitionerRole",
    "id" : "4e42ca15-5054-4b53-a181-0b945c7f58de",
    "period" : {
      "start" : "2013-08-01"
    },
    "specialty" : [ {
      "text" : "Registered nurse",
      "coding" : [ {
        "code" : "163W00000X",
        "display" : "Registered nurse",
        "system" : "http://hl7.org/fhir/ValueSet/provider-taxonomy"
      } ]
    } ],
    "practitioner" : {
      "name" : [ {
        "given" : [ "Mary" ],
        "family" : "McDonald",
        "use" : "official",
        "suffix" : [ "RN" ]
      } ],
      "id" : "d839038b-7172-4165-a760-467925b43857",
      "resourceType" : "Practitioner"
    }
  },
  "resourceType" : "Goal",
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
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.60"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="61146-7"/>
   <title>Goals Section</title>
   <text>
      <table border="1" width="100%">
         <thead>
            <tr>
               <th>Target</th>
               <th>Target detail</th>
               <th>Due date</th>
               <th>Responsible Person</th>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td>Negotiated Goal for Body Temperature</td>
               <td>{:value 98, :unit "[degF]"} - {:value 99, :unit "[degF]"}</td>
               <td>06/23/2015 12:00AM UTC</td>
               <td>Mary McDonald</td>
            </tr>
         </tbody>
      </table>
   </text>
   <entry>
      <observation moodCode="GOL" classCode="OBS">
         <templateId root="2.16.840.1.113883.10.20.22.4.121" extension="2022-06-01"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.121"/>
         <id root="3700b3b0-fbed-11e2-b788-0800200c9a66"/>
         <code codeSystem="2.16.840.1.113883.6.1"
                codeSystemName="http://loinc.org"
                displayName="Negotiated Goal for Body Temperature"
                code="8310-5">
            <originalText>Negotiated Goal for Body Temperature</originalText>
         </code>
         <statusCode code="active"/>
         <effectiveTime value="20150623"/>
         <value xsi:type="IVL_PQ">
            <low value="98" unit="[degF]"/>
            <high value="99" unit="[degF]"/>
         </value>
         <author>
            <templateId root="2.16.840.1.113883.10.20.22.4.119"/>
            <time value="20130801"/>
            <assignedAuthor>
               <id root="4e42ca15-5054-4b53-a181-0b945c7f58de"/>
               <code codeSystem="2.16.840.1.113883.6.101"
                      codeSystemName="http://hl7.org/fhir/ValueSet/provider-taxonomy"
                      displayName="Registered nurse"
                      code="163W00000X">
                  <originalText>Registered nurse</originalText>
               </code>
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
               <id nullFlavor="NI"/>
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
</section>
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
    "resourceType" : "PractitionerRole",
    "id" : "728f4009-c432-4323-b909-dd57324dfdbe",
    "period" : {
      "start" : "2013-07-30"
    },
    "specialty" : [ {
      "text" : "Registered nurse",
      "coding" : [ {
        "code" : "163W00000X",
        "display" : "Registered nurse",
        "system" : "http://hl7.org/fhir/ValueSet/provider-taxonomy"
      } ]
    } ],
    "practitioner" : {
      "name" : [ {
        "given" : [ "Mary" ],
        "family" : "McDonald",
        "use" : "official",
        "suffix" : [ "RN" ]
      } ],
      "id" : "d839038b-7171-4165-a760-467925b43857",
      "resourceType" : "Practitioner"
    }
  },
  "resourceType" : "Goal",
  "priority" : {
    "coding" : [ {
      "code" : "high-priority",
      "display" : "High Priority",
      "system" : "http://terminology.hl7.org/CodeSystem/goal-priority"
    } ]
  },
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
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.60"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="61146-7"/>
   <title>Goals Section</title>
   <text>
      <table border="1" width="100%">
         <thead>
            <tr>
               <th>Target</th>
               <th>Target detail</th>
               <th>Due date</th>
               <th>Responsible Person</th>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td>Resident's overall goal established during assessment process</td>
               <td>Need to gain more energy to do regular activities. (Visual Inspection)</td>
               <td>06/23/2015 12:00AM UTC</td>
               <td>Mary McDonald</td>
            </tr>
         </tbody>
      </table>
   </text>
   <entry>
      <observation moodCode="GOL" classCode="OBS">
         <templateId root="2.16.840.1.113883.10.20.22.4.121" extension="2022-06-01"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.121"/>
         <id root="3700b3b0-fbed-11e2-b778-0800200c9a66"/>
         <code codeSystem="2.16.840.1.113883.6.1"
                codeSystemName="http://loinc.org"
                displayName="Resident's overall goal established during assessment process"
                code="58144-7">
            <originalText>Resident's overall goal established during assessment process</originalText>
         </code>
         <statusCode code="active"/>
         <effectiveTime value="20150623"/>
         <value xsi:type="ST">Need to gain more energy to do regular activities. (Visual Inspection)</value>
         <author>
            <templateId root="2.16.840.1.113883.10.20.22.4.119"/>
            <time value="20130730"/>
            <assignedAuthor>
               <id root="728f4009-c432-4323-b909-dd57324dfdbe"/>
               <code codeSystem="2.16.840.1.113883.6.101"
                      codeSystemName="http://hl7.org/fhir/ValueSet/provider-taxonomy"
                      displayName="Registered nurse"
                      code="163W00000X">
                  <originalText>Registered nurse</originalText>
               </code>
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
               <id nullFlavor="NI"/>
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
</section>
```
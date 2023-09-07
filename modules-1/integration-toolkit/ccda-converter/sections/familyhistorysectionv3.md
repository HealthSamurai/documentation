# Family History Section (V3)

OID: 2.16.840.1.113883.10.20.22.2.15

LOINC: 10157-6

Alias: family-history

Internal ID: FamilyHistorySectionV3

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.15.html)

## sample2

```json
{
  "patient" : {
    "resourceType" : "Patient",
    "id" : "patient"
  },
  "sex" : {
    "coding" : [ {
      "code" : "M",
      "display" : "M",
      "system" : "2.16.840.1.113883.5.1"
    } ]
  },
  "name" : "Gerald",
  "relationship" : {
    "coding" : [ {
      "code" : "BRO",
      "display" : "Brother",
      "system" : "http://terminology.hl7.org/CodeSystem/v3-RoleCode"
    } ]
  },
  "resourceType" : "FamilyMemberHistory",
  "status" : "completed",
  "id" : "01faa204-1111-4610-864f-cb50b650d0fa",
  "condition" : [ {
    "code" : {
      "coding" : [ {
        "code" : "195967001",
        "display" : "Asthma (disorder)",
        "system" : "http://snomed.info/sct"
      } ]
    },
    "onset" : {
      "Period" : [ ]
    }
  } ],
  "born" : {
    "date" : "1985"
  }
}
```

C-CDA Equivalent:
```xml
<entry>
   <organizer classCode="CLUSTER" moodCode="EVN">
      <templateId root="2.16.840.1.113883.10.20.22.4.45" extension="2015-08-01"/>
      <templateId root="2.16.840.1.113883.10.20.22.4.45"/>
      <id root="01faa204-1111-4610-864f-cb50b650d0fa"/>
      <statusCode code="completed"/>
      <subject>
         <relatedSubject classCode="PRS">
            <code codeSystem="2.16.840.1.113883.5.111"
                   codeSystemName="HTTP://TERMINOLOGY.HL7.ORG/CODESYSTEM/V3-ROLECODE"
                   displayName="Brother"
                   code="BRO"/>
            <subject>
               <name>Gerald</name>
               <administrativeGenderCode codeSystem="2.16.840.1.113883.5.1"
                                          codeSystemName="2.16.840.1.113883.5.1"
                                          displayName="M"
                                          code="M"/>
            </subject>
         </relatedSubject>
      </subject>
      <component>
         <observation classCode="OBS" moodCode="EVN">
            <templateId root="2.16.840.1.113883.10.20.22.4.46" extension="2015-08-01"/>
            <templateId root="2.16.840.1.113883.10.20.22.4.46"/>
            <id root="a4433514-4dea-4e4c-8a0d-d58d7937da88"/>
            <code codeSystem="2.16.840.1.113883.6.96"
                   displayName="Condition"
                   code="64572001">
               <translation codeSystem="2.16.840.1.113883.6.1"
                             displayName="Condition Family Member"
                             code="75315-2"/>
            </code>
            <statusCode code="completed"/>
            <effectiveTime>
               <low nullFlavor="UNK"/>
               <high nullFlavor="UNK"/>
            </effectiveTime>
            <value codeSystem="2.16.840.1.113883.6.96"
                    codeSystemName="HTTP://SNOMED.INFO/SCT"
                    displayName="Asthma (disorder)"
                    code="195967001"
                    xsi:type="CD"/>
         </observation>
      </component>
   </organizer>
</entry>
```

## sample1

```json
{
  "patient" : {
    "resourceType" : "Patient",
    "id" : "patient"
  },
  "sex" : {
    "coding" : [ {
      "code" : "M",
      "display" : "M",
      "system" : "2.16.840.1.113883.5.1"
    } ]
  },
  "name" : "James",
  "relationship" : {
    "coding" : [ {
      "code" : "BRO",
      "display" : "Brother",
      "system" : "http://terminology.hl7.org/CodeSystem/v3-RoleCode"
    } ]
  },
  "resourceType" : "FamilyMemberHistory",
  "status" : "completed",
  "id" : "01faa204-0000-4610-864f-cb50b650d0fa",
  "condition" : [ {
    "code" : {
      "coding" : [ {
        "code" : "59621000",
        "display" : "Essential Hypertension",
        "system" : "http://snomed.info/sct"
      } ]
    },
    "onset" : {
      "Period" : [ ]
    },
    "note" : [ {
      "text" : "High blood pressure"
    } ]
  } ],
  "born" : {
    "date" : "1982"
  }
}
```

C-CDA Equivalent:
```xml
<entry>
   <organizer classCode="CLUSTER" moodCode="EVN">
      <templateId root="2.16.840.1.113883.10.20.22.4.45" extension="2015-08-01"/>
      <templateId root="2.16.840.1.113883.10.20.22.4.45"/>
      <id root="01faa204-0000-4610-864f-cb50b650d0fa"/>
      <statusCode code="completed"/>
      <subject>
         <relatedSubject classCode="PRS">
            <code codeSystem="2.16.840.1.113883.5.111"
                   codeSystemName="HTTP://TERMINOLOGY.HL7.ORG/CODESYSTEM/V3-ROLECODE"
                   displayName="Brother"
                   code="BRO"/>
            <subject>
               <name>James</name>
               <administrativeGenderCode codeSystem="2.16.840.1.113883.5.1"
                                          codeSystemName="2.16.840.1.113883.5.1"
                                          displayName="M"
                                          code="M"/>
            </subject>
         </relatedSubject>
      </subject>
      <component>
         <observation moodCode="EVN" classCode="OBS">
            <templateId root="2.16.840.1.113883.10.20.22.4.46" extension="2015-08-01"/>
            <templateId root="2.16.840.1.113883.10.20.22.4.46"/>
            <id root="0de6c67c-4649-4e4a-8cf1-043bd24bce51"/>
            <code codeSystem="2.16.840.1.113883.6.96"
                   displayName="Condition"
                   code="64572001">
               <translation codeSystem="2.16.840.1.113883.6.1"
                             displayName="Condition Family Member"
                             code="75315-2"/>
            </code>
            <text>High blood pressure</text>
            <statusCode code="completed"/>
            <effectiveTime>
               <low nullFlavor="UNK"/>
               <high nullFlavor="UNK"/>
            </effectiveTime>
            <value codeSystem="2.16.840.1.113883.6.96"
                    codeSystemName="HTTP://SNOMED.INFO/SCT"
                    displayName="Essential Hypertension"
                    code="59621000"
                    xsi:type="CD"/>
         </observation>
      </component>
   </organizer>
</entry>
```
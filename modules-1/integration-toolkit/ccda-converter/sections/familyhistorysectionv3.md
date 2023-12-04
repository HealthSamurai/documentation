# Family History Section (V3)

OID: 2.16.840.1.113883.10.20.22.2.15

LOINCs: #{"10157-6"}

Alias: family-history

Entries Required: N/A

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
      "system" : "urn:oid:2.16.840.1.113883.5.1"
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
    }
  } ],
  "born" : {
    "date" : "1985"
  }
}
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.15" extension="2015-08-01"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.15"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="10157-6"/>
   <title>Family History Section (V3)</title>
   <text>Family History Section (V3)</text>
   <entry>
      <organizer classCode="CLUSTER" moodCode="EVN">
         <templateId root="2.16.840.1.113883.10.20.22.4.45" extension="2015-08-01"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.45"/>
         <id root="01faa204-1111-4610-864f-cb50b650d0fa"/>
         <statusCode code="completed"/>
         <subject>
            <relatedSubject classCode="PRS">
               <code codeSystem="2.16.840.1.113883.5.111"
                      codeSystemName="http://terminology.hl7.org/CodeSystem/v3-RoleCode"
                      displayName="Brother"
                      code="BRO"/>
               <subject>
                  <name>Gerald</name>
                  <administrativeGenderCode codeSystem="2.16.840.1.113883.5.1"
                                             codeSystemName="urn:oid:2.16.840.1.113883.5.1"
                                             displayName="M"
                                             code="M"/>
               </subject>
            </relatedSubject>
         </subject>
         <component>
            <observation classCode="OBS" moodCode="EVN">
               <templateId root="2.16.840.1.113883.10.20.22.4.46" extension="2015-08-01"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.46"/>
               <id root="745c0025-6bad-42e2-a9e8-b48ef2062068"/>
               <code codeSystem="2.16.840.1.113883.6.96"
                      displayName="Condition"
                      code="64572001">
                  <translation codeSystem="2.16.840.1.113883.6.1"
                                displayName="Condition Family Member"
                                code="75315-2"/>
               </code>
               <statusCode code="completed"/>
               <value codeSystem="2.16.840.1.113883.6.96"
                       codeSystemName="http://snomed.info/sct"
                       displayName="Asthma (disorder)"
                       code="195967001"
                       xsi:type="CD"/>
            </observation>
         </component>
      </organizer>
   </entry>
</section>
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
      "system" : "urn:oid:2.16.840.1.113883.5.1"
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
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.15" extension="2015-08-01"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.15"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="10157-6"/>
   <title>Family History Section (V3)</title>
   <text>Family History Section (V3)</text>
   <entry>
      <organizer classCode="CLUSTER" moodCode="EVN">
         <templateId root="2.16.840.1.113883.10.20.22.4.45" extension="2015-08-01"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.45"/>
         <id root="01faa204-0000-4610-864f-cb50b650d0fa"/>
         <statusCode code="completed"/>
         <subject>
            <relatedSubject classCode="PRS">
               <code codeSystem="2.16.840.1.113883.5.111"
                      codeSystemName="http://terminology.hl7.org/CodeSystem/v3-RoleCode"
                      displayName="Brother"
                      code="BRO"/>
               <subject>
                  <name>James</name>
                  <administrativeGenderCode codeSystem="2.16.840.1.113883.5.1"
                                             codeSystemName="urn:oid:2.16.840.1.113883.5.1"
                                             displayName="M"
                                             code="M"/>
               </subject>
            </relatedSubject>
         </subject>
         <component>
            <observation classCode="OBS" moodCode="EVN">
               <templateId root="2.16.840.1.113883.10.20.22.4.46" extension="2015-08-01"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.46"/>
               <id root="b9bc51ac-434f-4c44-be76-510e8d75a88b"/>
               <code codeSystem="2.16.840.1.113883.6.96"
                      displayName="Condition"
                      code="64572001">
                  <translation codeSystem="2.16.840.1.113883.6.1"
                                displayName="Condition Family Member"
                                code="75315-2"/>
               </code>
               <text>High blood pressure</text>
               <statusCode code="completed"/>
               <value codeSystem="2.16.840.1.113883.6.96"
                       codeSystemName="http://snomed.info/sct"
                       displayName="Essential Hypertension"
                       code="59621000"
                       xsi:type="CD"/>
            </observation>
         </component>
      </organizer>
   </entry>
</section>
```
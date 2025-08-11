# Nutrition Section

OID: 2.16.840.1.113883.10.20.22.2.57

LOINCs: #{"61144-2"}

Alias: nutrition

Entries Required: N/A

Internal ID: NutritionSection

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.57.html)

## Nutrition assesment sample
Nutrition status observation can be converted from the Observation resource.


```json
{
  "code" : {
    "coding" : [ {
      "code" : "75305-3",
      "display" : "Nutritional status",
      "system" : "http://loinc.org"
    } ]
  },
  "id" : "c12ecaaf-53f8-4593-8f79-359aeaa3948b",
  "value" : {
    "CodeableConcept" : {
      "coding" : [ {
        "code" : "248324001",
        "display" : "well nourished",
        "system" : "http://snomed.info/sct"
      } ]
    }
  },
  "effective" : {
    "dateTime" : "2013-05-12"
  },
  "status" : "final",
  "subject" : {
    "id" : "patient",
    "resourceType" : "Patient"
  },
  "resourceType" : "Observation",
  "hasMember" : [ {
    "value" : {
      "CodeableConcept" : {
        "coding" : [ {
          "code" : "386619000",
          "display" : "low sodium diet (finding)",
          "system" : "http://snomed.info/sct"
        } ]
      }
    },
    "resourceType" : "Observation",
    "status" : "final",
    "effective" : {
      "dateTime" : "2013-05-12"
    },
    "id" : "ab1791b0-5c71-11db-b0de-0800200c9a66",
    "code" : {
      "coding" : [ {
        "code" : "75303-8",
        "display" : "Nutrition assessment",
        "system" : "http://loinc.org"
      } ]
    },
    "subject" : {
      "id" : "patient",
      "resourceType" : "Patient"
    },
    "performer" : [ {
      "resourceType" : "PractitionerRole",
      "id" : "e6048cf7-9803-5d4c-043f-ec72335d57f3",
      "period" : {
        "start" : "2013-07-30"
      },
      "specialty" : [ {
        "coding" : [ {
          "code" : "207QA0505X",
          "display" : "Adult Medicine",
          "system" : "http://hl7.org/fhir/ValueSet/provider-taxonomy"
        } ]
      } ],
      "practitioner" : {
        "name" : [ {
          "given" : [ "Patricia", "Patty" ],
          "family" : "Primary",
          "use" : "official",
          "suffix" : [ "M.D." ]
        } ],
        "address" : [ {
          "line" : [ "1004 Healthcare Drive " ],
          "use" : null,
          "city" : "Portland",
          "state" : "OR",
          "postalCode" : "99123",
          "country" : "US"
        } ],
        "telecom" : [ {
          "system" : "phone",
          "value" : "+1(555)555-1004",
          "use" : "work"
        } ],
        "id" : "f0cfcf63-db7a-f341-2941-afbb8fc41e18",
        "resourceType" : "Practitioner"
      }
    } ]
  }, {
    "value" : {
      "CodeableConcept" : {
        "coding" : [ {
          "code" : "430186007",
          "display" : "excessive dietary carbohydrate intake(finding)",
          "system" : "http://snomed.info/sct"
        } ]
      }
    },
    "resourceType" : "Observation",
    "status" : "final",
    "effective" : {
      "dateTime" : "2013-05-12"
    },
    "id" : "ab1791b0-5c71-11db-b0de-0800200c9a66",
    "code" : {
      "coding" : [ {
        "code" : "75303-8",
        "display" : "Nutrition assessment",
        "system" : "http://loinc.org"
      } ]
    },
    "subject" : {
      "id" : "patient",
      "resourceType" : "Patient"
    },
    "performer" : [ {
      "resourceType" : "PractitionerRole",
      "id" : "1a43f967-4c16-4920-16f9-f57a1e60726b",
      "specialty" : [ {
        "coding" : [ {
          "code" : "207QA0505X",
          "display" : "Adult Medicine",
          "system" : "http://hl7.org/fhir/ValueSet/provider-taxonomy"
        } ]
      } ],
      "practitioner" : {
        "name" : [ {
          "given" : [ "Patricia", "Patty" ],
          "family" : "Primary",
          "use" : "official",
          "suffix" : [ "M.D." ]
        } ],
        "address" : [ {
          "line" : [ "1004 Healthcare Drive " ],
          "use" : null,
          "city" : "Portland",
          "state" : "OR",
          "postalCode" : "99123",
          "country" : "US"
        } ],
        "telecom" : [ {
          "system" : "phone",
          "value" : "+1(555)555-1004",
          "use" : "work"
        } ],
        "id" : "bc6132f8-079e-94c5-3205-4cecfa9aef67",
        "resourceType" : "Practitioner"
      }
    } ]
  } ]
}
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.57"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="61144-2"/>
   <title>Nutrition Section</title>
   <text>Nutrition Section</text>
   <entry>
      <observation moodCode="EVN" classCode="OBS">
         <templateId root="2.16.840.1.113883.10.20.22.4.124"/>
         <id root="c12ecaaf-53f8-4593-8f79-359aeaa3948b"/>
         <code codeSystem="2.16.840.1.113883.6.1"
                codeSystemName="http://loinc.org"
                displayName="Nutritional status"
                code="75305-3"/>
         <statusCode code="completed"/>
         <effectiveTime value="20130512"/>
         <value codeSystem="2.16.840.1.113883.6.96"
                 codeSystemName="http://snomed.info/sct"
                 displayName="well nourished"
                 code="248324001"
                 xsi:type="CD"/>
         <entryRelationship typeCode="SUBJ">
            <observation moodCode="EVN" classCode="OBS">
               <templateId root="2.16.840.1.113883.10.20.22.4.138"/>
               <id root="ab1791b0-5c71-11db-b0de-0800200c9a66"/>
               <code codeSystem="2.16.840.1.113883.6.1"
                      codeSystemName="http://loinc.org"
                      displayName="Nutrition assessment"
                      code="75303-8"/>
               <statusCode code="completed"/>
               <effectiveTime value="20130512"/>
               <value codeSystem="2.16.840.1.113883.6.96"
                       codeSystemName="http://snomed.info/sct"
                       displayName="low sodium diet (finding)"
                       code="386619000"
                       xsi:type="CD"/>
               <author>
                  <templateId root="2.16.840.1.113883.10.20.22.4.119"/>
                  <time value="20130730"/>
                  <assignedAuthor>
                     <id root="e6048cf7-9803-5d4c-043f-ec72335d57f3"/>
                     <code codeSystem="2.16.840.1.113883.6.101"
                            codeSystemName="http://hl7.org/fhir/ValueSet/provider-taxonomy"
                            displayName="Adult Medicine"
                            code="207QA0505X"/>
                     <addr>
                        <country>US</country>
                        <state>OR</state>
                        <city>Portland</city>
                        <postalCode>99123</postalCode>
                        <streetAddressLine>1004 Healthcare Drive </streetAddressLine>
                     </addr>
                     <telecom value="+1(555)555-1004" use="WP"/>
                     <assignedPerson>
                        <name use="L">
                           <given>Patricia</given>
                           <given>Patty</given>
                           <family>Primary</family>
                           <suffix>M.D.</suffix>
                        </name>
                     </assignedPerson>
                  </assignedAuthor>
               </author>
            </observation>
         </entryRelationship>
         <entryRelationship typeCode="SUBJ">
            <observation moodCode="EVN" classCode="OBS">
               <templateId root="2.16.840.1.113883.10.20.22.4.138"/>
               <id root="ab1791b0-5c71-11db-b0de-0800200c9a66"/>
               <code codeSystem="2.16.840.1.113883.6.1"
                      codeSystemName="http://loinc.org"
                      displayName="Nutrition assessment"
                      code="75303-8"/>
               <statusCode code="completed"/>
               <effectiveTime value="20130512"/>
               <value codeSystem="2.16.840.1.113883.6.96"
                       codeSystemName="http://snomed.info/sct"
                       displayName="excessive dietary carbohydrate intake(finding)"
                       code="430186007"
                       xsi:type="CD"/>
               <author>
                  <templateId root="2.16.840.1.113883.10.20.22.4.119"/>
                  <assignedAuthor>
                     <id root="1a43f967-4c16-4920-16f9-f57a1e60726b"/>
                     <code codeSystem="2.16.840.1.113883.6.101"
                            codeSystemName="http://hl7.org/fhir/ValueSet/provider-taxonomy"
                            displayName="Adult Medicine"
                            code="207QA0505X"/>
                     <addr>
                        <country>US</country>
                        <state>OR</state>
                        <city>Portland</city>
                        <postalCode>99123</postalCode>
                        <streetAddressLine>1004 Healthcare Drive </streetAddressLine>
                     </addr>
                     <telecom value="+1(555)555-1004" use="WP"/>
                     <assignedPerson>
                        <name use="L">
                           <given>Patricia</given>
                           <given>Patty</given>
                           <family>Primary</family>
                           <suffix>M.D.</suffix>
                        </name>
                     </assignedPerson>
                  </assignedAuthor>
               </author>
            </observation>
         </entryRelationship>
      </observation>
   </entry>
</section>
```
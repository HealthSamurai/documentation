# Document Header

OID: N/A

LOINCs: #{}

Alias: header

Entries Required: N/A

Internal ID: Header

## sample1

```json
{
  "subject" : {
    "address" : [ {
      "line" : [ "1007 Amber Dr" ],
      "use" : "home",
      "city" : "Beaverton",
      "state" : "OR",
      "postalCode" : "97266",
      "country" : "US"
    }, {
      "line" : [ "1002 Oak Dr" ],
      "use" : "old",
      "city" : "Washington",
      "state" : "DC",
      "postalCode" : "3423423",
      "country" : "US",
      "period" : {
        "start" : "2017-09-21",
        "end" : "2019-09-21"
      }
    } ],
    "name" : [ {
      "given" : [ "Rabecca", "Jones" ],
      "family" : "Angeles",
      "use" : "official",
      "suffix" : null
    }, {
      "given" : [ "Becky", "Jones" ],
      "family" : "Angeles",
      "use" : "old",
      "suffix" : null
    } ],
    "birthDate" : "1970-07-01",
    "resourceType" : "Patient",
    "extension" : [ {
      "extension" : [ {
        "value" : {
          "CodeableConcept" : {
            "text" : "White",
            "coding" : [ {
              "code" : "2106-3",
              "display" : "White",
              "system" : "urn:oid:2.16.840.1.113883.6.238"
            } ]
          }
        },
        "url" : "ombCategory"
      }, {
        "value" : {
          "CodeableConcept" : {
            "text" : "White European",
            "coding" : [ {
              "code" : "2108-9",
              "display" : "White European",
              "system" : "urn:oid:2.16.840.1.113883.6.238"
            } ]
          }
        },
        "url" : "detailed"
      } ],
      "url" : "http://hl7.org/fhir/StructureDefinition/us-core-race"
    }, {
      "value" : {
        "CodeableConcept" : {
          "text" : "Mexican",
          "coding" : [ {
            "code" : "2186-5",
            "display" : "Mexican",
            "system" : "urn:oid:2.16.840.1.113883.6.238"
          } ]
        }
      },
      "url" : "http://hl7.org/fhir/StructureDefinition/us-core-ethnicity"
    } ],
    "id" : "patient",
    "identifier" : [ {
      "value" : "118283339",
      "system" : "http://hl7.org/fhir/sid/us-ssn"
    } ],
    "telecom" : [ {
      "system" : "phone",
      "value" : "+1(555)-225-1234",
      "use" : "mobile"
    }, {
      "system" : "phone",
      "value" : "+1(555)-226-1544",
      "use" : "home"
    } ],
    "gender" : "female"
  },
  "composition" : {
    "identifier" : {
      "value" : "TT107",
      "system" : "urn:oid:2.16.840.1.113883.19.5.99999.1",
      "extension" : [ {
        "url" : "cda-assigning-authority-name",
        "value" : {
          "string" : "ABC"
        }
      } ]
    }
  },
  "custodian" : {
    "name" : "Community Health and Hospitals",
    "telecom" : [ {
      "system" : "phone",
      "value" : "+1(555)-555-1002",
      "use" : "work"
    } ],
    "identifier" : [ {
      "system" : "http://hl7.org/fhir/sid/us-npi",
      "value" : "99998899"
    } ],
    "address" : [ {
      "line" : [ "1002 Healthcare Dr" ],
      "use" : "work",
      "city" : "Beaverton",
      "state" : "OR",
      "postalCode" : "97266",
      "country" : "US"
    } ],
    "id" : "SOME-STRING",
    "resourceType" : "Organization"
  },
  "authors" : [ {
    "practitioner" : {
      "name" : [ {
        "given" : [ "Henry" ],
        "family" : "Seven",
        "use" : "official"
      } ],
      "address" : [ {
        "line" : [ "1002 Healthcare Dr" ],
        "city" : "Beaverton",
        "state" : "OR",
        "postalCode" : "97266",
        "country" : "US"
      } ],
      "telecom" : [ {
        "system" : "phone",
        "value" : "+1(555)-555-1002",
        "use" : "work"
      } ],
      "id" : "ca3e2395-79e9-d8a4-783d-252892f31fb7",
      "identifier" : [ {
        "value" : "111111",
        "system" : "http://hl7.org/fhir/sid/us-npi"
      } ],
      "resourceType" : "Practitioner"
    },
    "specialty" : [ {
      "text" : "Chronic Disease Hospital",
      "coding" : [ {
        "code" : "281P00000X",
        "display" : "Chronic Disease Hospital",
        "system" : "http://hl7.org/fhir/ValueSet/provider-taxonomy"
      } ]
    } ],
    "id" : "dc6a82a9-8999-f7c9-b73d-160351225091",
    "resourceType" : "PractitionerRole",
    "period" : {
      "start" : "2015-06-22"
    }
  } ]
}
```

C-CDA Equivalent:
```xml
<ClinicalDocument xmlns="urn:hl7-org:v3"
                   xmlns:sdtc="urn:hl7-org:sdtc"
                   xmlns:voc="urn:hl7-org:v3/voc"
                   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
   <realmCode code="US"/>
   <typeId extension="POCD_HD000040" root="2.16.840.1.113883.1.3"/>
   <templateId root="2.16.840.1.113883.10.20.22.1.1" extension="2015-08-01"/>
   <templateId root="2.16.840.1.113883.10.20.22.1.1"/>
   <id root="ee05e1da-df1b-7f7e-166c-e182e1d39f86"/>
   <id extension="TT107"
        assigningAuthorityName="ABC"
        root="2.16.840.1.113883.19.5.99999.1"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="TESTDOC"
          code="TESTDOC"/>
   <title>Document Title</title>
   <effectiveTime value="20200202"/>
   <confidentialityCode code="N"
                         displayName="normal"
                         codeSystem="2.16.840.1.113883.5.25"
                         codeSystemName="Confidentiality"/>
   <languageCode code="en-US"/>
   <recordTarget>
      <patientRole>
         <id root="b39024ef-bc6d-e619-76f5-85c8421c6bba"/>
         <id extension="118283339" root="2.16.840.1.113883.4.1"/>
         <addr use="HP">
            <country>US</country>
            <state>OR</state>
            <city>Beaverton</city>
            <postalCode>97266</postalCode>
            <streetAddressLine>1007 Amber Dr</streetAddressLine>
         </addr>
         <addr>
            <country>US</country>
            <state>DC</state>
            <city>Washington</city>
            <postalCode>3423423</postalCode>
            <streetAddressLine>1002 Oak Dr</streetAddressLine>
         </addr>
         <telecom value="+1(555)-225-1234" use="MC"/>
         <telecom value="+1(555)-226-1544" use="HP"/>
         <patient>
            <name use="L">
               <given>Rabecca</given>
               <given>Jones</given>
               <family>Angeles</family>
            </name>
            <name>
               <given qualifier="BR">Becky</given>
               <given qualifier="BR">Jones</given>
               <family>Angeles</family>
            </name>
            <administrativeGenderCode code="F"
                                       displayName="Female"
                                       codeSystem="2.16.840.1.113883.5.1"
                                       codeSystemName="AdministrativeGender"/>
            <birthTime value="19700701"/>
            <raceCode codeSystem="2.16.840.1.113883.6.238"
                       codeSystemName="urn:oid:2.16.840.1.113883.6.238"
                       displayName="White"
                       code="2106-3">
               <originalText>White</originalText>
            </raceCode>
            <sdtc:raceCode codeSystem="2.16.840.1.113883.6.238"
                            codeSystemName="urn:oid:2.16.840.1.113883.6.238"
                            displayName="White European"
                            code="2108-9">
               <originalText>White European</originalText>
            </sdtc:raceCode>
            <ethnicGroupCode codeSystem="2.16.840.1.113883.6.238"
                              codeSystemName="urn:oid:2.16.840.1.113883.6.238"
                              displayName="Mexican"
                              code="2186-5">
               <originalText>Mexican</originalText>
            </ethnicGroupCode>
         </patient>
      </patientRole>
   </recordTarget>
   <author>
      <time value="20150622"/>
      <assignedAuthor>
         <id root="c569ccb7-c838-fdf3-149b-beecd8e9bd41"/>
         <addr nullFlavor="UNK"/>
         <telecom nullFlavor="UNK"/>
         <assignedPerson>
            <name nullFlavor="UNK"/>
         </assignedPerson>
      </assignedAuthor>
   </author>
   <custodian>
      <assignedCustodian>
         <representedCustodianOrganization>
            <id root="7df888b6-0268-1e62-ff60-ceb2f2f88630"/>
            <id extension="99998899" root="2.16.840.1.113883.4.6"/>
            <name>Community Health and Hospitals</name>
            <telecom value="+1(555)-555-1002" use="WP"/>
            <addr use="WP">
               <country>US</country>
               <state>OR</state>
               <city>Beaverton</city>
               <postalCode>97266</postalCode>
               <streetAddressLine>1002 Healthcare Dr</streetAddressLine>
            </addr>
         </representedCustodianOrganization>
      </assignedCustodian>
   </custodian>
   <documentationOf>
      <serviceEvent classCode="PCPR">
         <id root="6b5da943-050a-6ae1-5f4b-2daea74b6103"/>
         <effectiveTime>
            <low value="20150622"/>
            <high value="20150624"/>
         </effectiveTime>
      </serviceEvent>
   </documentationOf>

```
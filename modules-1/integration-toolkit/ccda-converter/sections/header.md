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
              "system" : "2.16.840.1.113883.6.238"
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
              "system" : "2.16.840.1.113883.6.238"
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
            "system" : "2.16.840.1.113883.6.238"
          } ]
        }
      },
      "url" : "http://hl7.org/fhir/StructureDefinition/us-core-ethnicity"
    } ],
    "id" : "patient",
    "identifier" : [ {
      "value" : "118283339",
      "system" : "2.16.840.1.113883.4.1"
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
      "system" : "2.16.840.1.113883.19.5.99999.1",
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
      "system" : "2.16.840.1.113883.4.6",
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
    "name" : [ {
      "given" : [ "Henry" ],
      "family" : "Seven",
      "use" : "official",
      "suffix" : null
    } ],
    "address" : [ {
      "line" : [ "1002 Healthcare Dr" ],
      "use" : null,
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
    "qualification" : [ {
      "code" : {
        "coding" : [ {
          "code" : "281P00000X",
          "display" : "Chronic Disease Hospital",
          "system" : "http://hl7.org/fhir/ValueSet/provider-taxonomy"
        } ]
      }
    } ],
    "id" : "SOME-STRING",
    "resourceType" : "Practitioner"
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
          codeSystemName="HTTP://LOINC.ORG"
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
                       codeSystemName="2.16.840.1.113883.6.238"
                       displayName="White"
                       code="2106-3"/>
            <sdtc:raceCode codeSystem="2.16.840.1.113883.6.238"
                            codeSystemName="2.16.840.1.113883.6.238"
                            displayName="White European"
                            code="2108-9"/>
            <ethnicGroupCode codeSystem="2.16.840.1.113883.6.238"
                              codeSystemName="2.16.840.1.113883.6.238"
                              displayName="Mexican"
                              code="2186-5"/>
         </patient>
      </patientRole>
   </recordTarget>
   <author>
      <time value="20200101"/>
      <assignedAuthor>
         <id root="02bd92fa-a38a-aa6c-c0ea-75e59937a1ef"/>
         <addr nullFlavor="UNK"/>
         <telecom nullFlavor="UNK"/>
         <assignedPerson>
            <name>
               <given>Docauthor</given>
               <family>Foobar</family>
            </name>
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
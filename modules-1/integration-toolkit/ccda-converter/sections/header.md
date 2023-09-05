# Document Header

OID: N/A

LOINC: N/A

Alias: header

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
  "custodian" : {
    "name" : "Community Health and Hospitals",
    "telecom" : [ {
      "system" : "phone",
      "value" : "+1(555)-555-1002",
      "use" : "work"
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
  "authors" : [ "Unbound: #'ccda.rules.header-test/author-sample" ]
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
   <templateId root="2.16.840.1.113883.10.20.22.1.1" extension="2014-06-09"/>
   <templateId root="2.16.840.1.113883.10.20.22.1.1"/>
   <id root="ee05e1da-df1b-7f7e-166c-e182e1d39f86"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="HTTP://LOINC.ORG"
          displayName="Discharge Summary"
          code="18842-5"/>
   <title>2020202020</title>
   <effectiveTime value="20200202"/>
   <confidentialityCode code="N"
                         displayName="normal"
                         codeSystem="2.16.840.1.113883.5.25"
                         codeSystemName="Confidentiality"/>
   <languageCode code="en-US"/>
   <recordTarget>
      <patientRole>
         <id root="b39024ef-bc6d-e619-76f5-85c8421c6bba"/>
         <patient>
            <name>
               <given>Testpat</given>
               <family>Foobar</family>
            </name>
            <administrativeGenderCode code="UN"
                                       displayName="Undifferentiated"
                                       codeSystem="2.16.840.1.113883.5.1"
                                       codeSystemName="AdministrativeGender"/>
            <raceCode nullFlavor="UNK"/>
            <sdtc:raceCode nullFlavor="UNK"/>
            <ethnicGroupCode nullFlavor="UNK"/>
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
            <id root="158b17e5-c830-5b79-0450-8b188e37d609"/>
            <name>Custodian org</name>
            <telecom nullFlavor="UNK"/>
         </representedCustodianOrganization>
      </assignedCustodian>
   </custodian>
   
```
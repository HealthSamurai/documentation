# Medications Section (entries optional) (V2)

OID: 2.16.840.1.113883.10.20.22.2.1

LOINCs: #{"10160-0"}

Alias: medications

Entries Required: false

Internal ID: MedicationsSectionentriesoptional

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.1.html)

## sample-medication-administration

```json
{
  "dosage" : {
    "route" : {
      "text" : "Oral Route of Administration",
      "coding" : [ {
        "code" : "C38288",
        "display" : "Oral Route of Administration",
        "system" : "http://ncithesaurus-stage.nci.nih.gov"
      } ]
    },
    "dose" : {
      "value" : 2
    }
  },
  "extension" : [ {
    "value" : {
      "Reference" : {
        "resourceType" : "PractitionerRole",
        "id" : "7455d60f-2808-f746-30d9-df2d72c74bf6",
        "specialty" : [ {
          "text" : "Allopathic & Osteopathic Physicians; Internal Medicine, Cardiovascular Disease",
          "coding" : [ {
            "code" : "207RC0000X",
            "display" : "Allopathic & Osteopathic Physicians; Internal Medicine, Cardiovascular Disease",
            "system" : "http://hl7.org/fhir/ValueSet/provider-taxonomy"
          } ]
        } ],
        "practitioner" : {
          "name" : [ {
            "given" : [ "Heartly" ],
            "family" : "Sixer",
            "use" : "official",
            "suffix" : [ "MD" ]
          } ],
          "address" : [ {
            "line" : [ "6666 StreetName St." ],
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
          "identifier" : [ {
            "value" : "66666",
            "system" : "http://hl7.org/fhir/sid/us-npi"
          } ],
          "resourceType" : "Practitioner"
        }
      }
    },
    "url" : "author"
  } ],
  "resourceType" : "MedicationAdministration",
  "status" : "completed",
  "effective" : {
    "dateTime" : "2013-09-11T16:03:00-07:00"
  },
  "id" : "1061a257-3b5c-4b09-9dc7-23e59b788b18",
  "subject" : {
    "resourceType" : "Patient",
    "id" : "patient"
  },
  "medication" : {
    "CodeableConcept" : {
      "text" : "Aspirin 81mg Oral Tablet",
      "coding" : [ {
        "code" : "243670",
        "display" : "aspirin 81 MG Oral Tablet",
        "system" : "http://www.nlm.nih.gov/research/umls/rxnorm"
      } ]
    }
  }
}
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.1.1" extension="2014-06-09"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.1.1"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="10160-0"/>
   <title>Medications Section (entries optional) (V2)</title>
   <text>
      <table border="1" width="100%">
         <caption>Prescribed Medications</caption>
         <thead>
            <tr>
               <td>Medication</td>
               <td>Timing</td>
               <td>Route</td>
               <td>Freq</td>
               <td>Dose</td>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td colspan="5">No records</td>
            </tr>
         </tbody>
      </table>
      <table border="1" width="100%">
         <caption>Administered Medications</caption>
         <thead>
            <tr>
               <td>Medication</td>
               <td>Date</td>
               <td>Route</td>
               <td>Dose</td>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td>aspirin 81 MG Oral Tablet (243670)</td>
               <td>2013-09-11T16:03:00-07:00</td>
               <td>Oral Route of Administration (C38288)</td>
               <td>No information</td>
            </tr>
         </tbody>
      </table>
   </text>
   <entry>
      <substanceAdministration moodCode="EVN" classCode="SBADM">
         <templateId root="2.16.840.1.113883.10.20.22.4.16" extension="2014-06-09"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.16"/>
         <id root="1061a257-3b5c-4b09-9dc7-23e59b788b18"/>
         <statusCode code="completed"/>
         <effectiveTime value="20130911160300-0700"/>
         <routeCode codeSystem="2.16.840.1.113883.3.26.1.1"
                     codeSystemName="http://ncithesaurus-stage.nci.nih.gov"
                     displayName="Oral Route of Administration"
                     code="C38288">
            <originalText>Oral Route of Administration</originalText>
         </routeCode>
         <doseQuantity value="2"/>
         <consumable>
            <manufacturedProduct classCode="MANU">
               <templateId root="2.16.840.1.113883.10.20.22.4.23" extension="2014-06-09"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.23"/>
               <manufacturedMaterial>
                  <code codeSystem="2.16.840.1.113883.6.88"
                         codeSystemName="http://www.nlm.nih.gov/research/umls/rxnorm"
                         displayName="aspirin 81 MG Oral Tablet"
                         code="243670">
                     <originalText>Aspirin 81mg Oral Tablet</originalText>
                  </code>
               </manufacturedMaterial>
            </manufacturedProduct>
         </consumable>
         <author>
            <templateId root="2.16.840.1.113883.10.20.22.4.119"/>
            <assignedAuthor>
               <id root="7455d60f-2808-f746-30d9-df2d72c74bf6"/>
               <id extension="66666" root="2.16.840.1.113883.4.6"/>
               <code codeSystem="2.16.840.1.113883.6.101"
                      codeSystemName="http://hl7.org/fhir/ValueSet/provider-taxonomy"
                      displayName="Allopathic &amp; Osteopathic Physicians; Internal Medicine, Cardiovascular Disease"
                      code="207RC0000X">
                  <originalText>Allopathic &amp; Osteopathic Physicians; Internal Medicine, Cardiovascular Disease</originalText>
               </code>
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
      </substanceAdministration>
   </entry>
</section>
```

## Empty medication example
Even if MedicationStatement is without effective, produced C-CDA entry contains effectiveTime with nullFlavor values.


```json
{
  "resourceType" : "MedicationStatement",
  "id" : "id",
  "status" : "active",
  "medication" : {
    "CodeableConcept" : {
      "coding" : [ {
        "code" : "1049529",
        "display" : "Sudafed 30 MG Oral Tablet",
        "system" : "http://www.nlm.nih.gov/research/umls/rxnorm"
      } ]
    }
  }
}
```

So there are nullFlavor values placed in required field effectiveTime.
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.1.1" extension="2014-06-09"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.1.1"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="10160-0"/>
   <title>Medications Section (entries optional) (V2)</title>
   <text>
      <table border="1" width="100%">
         <caption>Prescribed Medications</caption>
         <thead>
            <tr>
               <td>Medication</td>
               <td>Timing</td>
               <td>Route</td>
               <td>Freq</td>
               <td>Dose</td>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td>Sudafed 30 MG Oral Tablet (1049529)</td>
               <td>No information</td>
               <td>No information</td>
               <td>No information</td>
               <td>No information</td>
            </tr>
         </tbody>
      </table>
      <table border="1" width="100%">
         <caption>Administered Medications</caption>
         <thead>
            <tr>
               <td>Medication</td>
               <td>Date</td>
               <td>Route</td>
               <td>Dose</td>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td colspan="4">No records</td>
            </tr>
         </tbody>
      </table>
   </text>
   <entry>
      <substanceAdministration moodCode="INT" classCode="SBADM">
         <templateId root="2.16.840.1.113883.10.20.22.4.16" extension="2014-06-09"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.16"/>
         <id root="b80bb774-0288-fda1-f201-890375a60c8f"/>
         <statusCode code="active"/>
         <effectiveTime xsi:type="IVL_TS">
            <low nullFlavor="UNK"/>
            <high nullFlavor="UNK"/>
         </effectiveTime>
         <doseQuantity nullFlavor="UNK"/>
         <consumable>
            <manufacturedProduct classCode="MANU">
               <templateId root="2.16.840.1.113883.10.20.22.4.23" extension="2014-06-09"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.23"/>
               <manufacturedMaterial>
                  <code codeSystem="2.16.840.1.113883.6.88"
                         codeSystemName="http://www.nlm.nih.gov/research/umls/rxnorm"
                         displayName="Sudafed 30 MG Oral Tablet"
                         code="1049529"/>
               </manufacturedMaterial>
            </manufacturedProduct>
         </consumable>
      </substanceAdministration>
   </entry>
</section>
```

## Medication Statement sample
This is a typical medication resource sample.
          
* :dosage field contains information about dosage and timing.
          
* :effective field contains information about start and end dates of medication administration.



```json
{
  "resourceType" : "MedicationStatement",
  "id" : "9aff221f-e689-5e3d-71ff-6edcb00406cb",
  "status" : "active",
  "medication" : {
    "CodeableConcept" : {
      "coding" : [ {
        "code" : "1049529",
        "display" : "Sudafed 30 MG Oral Tablet",
        "system" : "http://www.nlm.nih.gov/research/umls/rxnorm"
      } ]
    }
  },
  "effective" : {
    "Period" : {
      "start" : "2014-01-18",
      "end" : "2014-01-28"
    }
  },
  "dosage" : [ {
    "route" : {
      "coding" : [ {
        "code" : "C38288",
        "display" : "Oral",
        "system" : "http://ncithesaurus-stage.nci.nih.gov"
      } ]
    },
    "doseAndRate" : [ {
      "dose" : {
        "Quantity" : {
          "value" : 2.0,
          "unit" : "mg"
        }
      }
    } ],
    "timing" : {
      "repeat" : {
        "periodUnit" : "h",
        "period" : 4.0,
        "periodMax" : 6.0
      }
    }
  } ]
}
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.1.1" extension="2014-06-09"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.1.1"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="10160-0"/>
   <title>Medications Section (entries optional) (V2)</title>
   <text>
      <table border="1" width="100%">
         <caption>Prescribed Medications</caption>
         <thead>
            <tr>
               <td>Medication</td>
               <td>Timing</td>
               <td>Route</td>
               <td>Freq</td>
               <td>Dose</td>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td>Sudafed 30 MG Oral Tablet (1049529)</td>
               <td>2014-01-18 - 2014-01-28</td>
               <td>Oral (C38288)</td>
               <td>x per 4.0 h</td>
               <td>2.0 mg</td>
            </tr>
         </tbody>
      </table>
      <table border="1" width="100%">
         <caption>Administered Medications</caption>
         <thead>
            <tr>
               <td>Medication</td>
               <td>Date</td>
               <td>Route</td>
               <td>Dose</td>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td colspan="4">No records</td>
            </tr>
         </tbody>
      </table>
   </text>
   <entry>
      <substanceAdministration moodCode="INT" classCode="SBADM">
         <templateId root="2.16.840.1.113883.10.20.22.4.16" extension="2014-06-09"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.16"/>
         <id root="9aff221f-e689-5e3d-71ff-6edcb00406cb"/>
         <statusCode code="active"/>
         <effectiveTime operator="A" xsi:type="PIVL_TS">
            <period xsi:type="IVL_PQ">
               <low value="4.0" unit="h"/>
               <high value="6.0" unit="h"/>
            </period>
         </effectiveTime>
         <effectiveTime xsi:type="IVL_TS">
            <low value="20140118"/>
            <high value="20140128"/>
         </effectiveTime>
         <routeCode codeSystem="2.16.840.1.113883.3.26.1.1"
                     codeSystemName="http://ncithesaurus-stage.nci.nih.gov"
                     displayName="Oral"
                     code="C38288"/>
         <doseQuantity value="2.0" unit="mg"/>
         <consumable>
            <manufacturedProduct classCode="MANU">
               <templateId root="2.16.840.1.113883.10.20.22.4.23" extension="2014-06-09"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.23"/>
               <manufacturedMaterial>
                  <code codeSystem="2.16.840.1.113883.6.88"
                         codeSystemName="http://www.nlm.nih.gov/research/umls/rxnorm"
                         displayName="Sudafed 30 MG Oral Tablet"
                         code="1049529"/>
               </manufacturedMaterial>
            </manufacturedProduct>
         </consumable>
      </substanceAdministration>
   </entry>
</section>
```

## sample-prn-medication

```json
{
  "dosage" : [ {
    "timing" : {
      "repeat" : {
        "periodUnit" : "h",
        "count" : 1,
        "period" : 6.0
      }
    },
    "route" : {
      "text" : "Oral Route of Administration",
      "coding" : [ {
        "code" : "C38288",
        "display" : "Oral Route of Administration",
        "system" : "http://ncithesaurus-stage.nci.nih.gov"
      } ]
    },
    "doseAndRate" : [ {
      "dose" : {
        "Quantity" : {
          "value" : 1.0
        }
      }
    } ]
  } ],
  "resourceType" : "MedicationStatement",
  "status" : "active",
  "effective" : {
    "Period" : {
      "start" : "2013-12-18"
    }
  },
  "id" : "47d3e719-f688-459d-bcdc-47c6de0767a9",
  "informationSource" : {
    "resourceType" : "PractitionerRole",
    "specialty" : [ {
      "text" : "Allopathic & Osteopathic Physicians; Internal Medicine, Cardiovascular Disease",
      "coding" : [ {
        "code" : "207RC0000X",
        "display" : "Allopathic & Osteopathic Physicians; Internal Medicine, Cardiovascular Disease",
        "system" : "http://hl7.org/fhir/ValueSet/provider-taxonomy"
      } ]
    } ],
    "practitioner" : {
      "name" : [ {
        "given" : [ "Heartly" ],
        "family" : "Sixer",
        "use" : "official",
        "suffix" : [ "MD" ]
      } ],
      "address" : [ {
        "line" : [ "6666 StreetName St." ],
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
      "identifier" : [ {
        "value" : "66666",
        "system" : "http://hl7.org/fhir/sid/us-npi"
      } ],
      "resourceType" : "Practitioner"
    }
  },
  "subject" : {
    "resourceType" : "Patient",
    "id" : "patient"
  },
  "medication" : {
    "CodeableConcept" : {
      "text" : "Ibuprofen 600mg Oral Tablet",
      "coding" : [ {
        "code" : "197806",
        "display" : "ibuprofen 600 MG Oral Tablet",
        "system" : "http://www.nlm.nih.gov/research/umls/rxnorm"
      }, {
        "code" : "00603402221",
        "display" : "Ibuprofen 600mg Oral Tablet",
        "system" : "urn:oid:2.16.840.1.113883.6.69"
      } ]
    }
  }
}
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.1.1" extension="2014-06-09"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.1.1"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="10160-0"/>
   <title>Medications Section (entries optional) (V2)</title>
   <text>
      <table border="1" width="100%">
         <caption>Prescribed Medications</caption>
         <thead>
            <tr>
               <td>Medication</td>
               <td>Timing</td>
               <td>Route</td>
               <td>Freq</td>
               <td>Dose</td>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td>ibuprofen 600 MG Oral Tablet (197806)</td>
               <td>2013-12-18 - </td>
               <td>Oral Route of Administration (C38288)</td>
               <td>x per 6.0 h</td>
               <td>1.0 </td>
            </tr>
         </tbody>
      </table>
      <table border="1" width="100%">
         <caption>Administered Medications</caption>
         <thead>
            <tr>
               <td>Medication</td>
               <td>Date</td>
               <td>Route</td>
               <td>Dose</td>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td colspan="4">No records</td>
            </tr>
         </tbody>
      </table>
   </text>
   <entry>
      <substanceAdministration moodCode="INT" classCode="SBADM">
         <templateId root="2.16.840.1.113883.10.20.22.4.16" extension="2014-06-09"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.16"/>
         <id root="47d3e719-f688-459d-bcdc-47c6de0767a9"/>
         <statusCode code="active"/>
         <effectiveTime operator="A" institutionSpecified="true" xsi:type="PIVL_TS">
            <period value="6.0" unit="h"/>
         </effectiveTime>
         <effectiveTime xsi:type="IVL_TS">
            <low value="20131218"/>
            <high nullFlavor="UNK"/>
         </effectiveTime>
         <routeCode codeSystem="2.16.840.1.113883.3.26.1.1"
                     codeSystemName="http://ncithesaurus-stage.nci.nih.gov"
                     displayName="Oral Route of Administration"
                     code="C38288">
            <originalText>Oral Route of Administration</originalText>
         </routeCode>
         <doseQuantity value="1.0"/>
         <consumable>
            <manufacturedProduct classCode="MANU">
               <templateId root="2.16.840.1.113883.10.20.22.4.23" extension="2014-06-09"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.23"/>
               <manufacturedMaterial>
                  <code codeSystem="2.16.840.1.113883.6.88"
                         codeSystemName="http://www.nlm.nih.gov/research/umls/rxnorm"
                         displayName="ibuprofen 600 MG Oral Tablet"
                         code="197806">
                     <originalText>Ibuprofen 600mg Oral Tablet</originalText>
                     <translation codeSystem="2.16.840.1.113883.6.69"
                                   codeSystemName="urn:oid:2.16.840.1.113883.6.69"
                                   displayName="Ibuprofen 600mg Oral Tablet"
                                   code="00603402221"/>
                  </code>
               </manufacturedMaterial>
            </manufacturedProduct>
         </consumable>
         <author>
            <templateId root="2.16.840.1.113883.10.20.22.4.119"/>
            <assignedAuthor>
               <id extension="66666" root="2.16.840.1.113883.4.6"/>
               <code codeSystem="2.16.840.1.113883.6.101"
                      codeSystemName="http://hl7.org/fhir/ValueSet/provider-taxonomy"
                      displayName="Allopathic &amp; Osteopathic Physicians; Internal Medicine, Cardiovascular Disease"
                      code="207RC0000X">
                  <originalText>Allopathic &amp; Osteopathic Physicians; Internal Medicine, Cardiovascular Disease</originalText>
               </code>
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
      </substanceAdministration>
   </entry>
</section>
```
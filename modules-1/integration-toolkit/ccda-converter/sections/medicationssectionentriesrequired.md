# Medications Section (entries required) (V2)

OID: 2.16.840.1.113883.10.20.22.2.1.1

LOINCs: #{"10160-0"}

Alias: medications

Entries Required: N/A

Internal ID: MedicationsSectionentriesrequired

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.1.1.html)

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
   <title>Medications Section (entries required) (V2)</title>
   <text>
      <table border="1" width="100%">
         <thead>
            <tr>
               <td>Medication</td>
               <td>Timing</td>
               <td>Route</td>
               <td>Frequency</td>
               <td>Dose</td>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td>Sudafed 30 MG Oral Tablet (1049529)</td>
               <td>No information</td>
            </tr>
         </tbody>
      </table>
   </text>
   <entry>
      <substanceAdministration classCode="SBADM" moodCode="EVN">
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
   <title>Medications Section (entries required) (V2)</title>
   <text>
      <table border="1" width="100%">
         <thead>
            <tr>
               <td>Medication</td>
               <td>Timing</td>
               <td>Route</td>
               <td>Frequency</td>
               <td>Dose</td>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td>Sudafed 30 MG Oral Tablet (1049529)</td>
               <td>2014-01-18 - 2014-01-28</td>
               <td>Oral (C38288)</td>
               <td>every 4.0 h</td>
               <td>2.0 mg</td>
            </tr>
         </tbody>
      </table>
   </text>
   <entry>
      <substanceAdministration moodCode="EVN" classCode="SBADM">
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
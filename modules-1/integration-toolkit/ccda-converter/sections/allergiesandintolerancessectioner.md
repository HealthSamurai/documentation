# Allergies and Intolerances Section (entries required) (V3)

OID: 2.16.840.1.113883.10.20.22.2.6.1

LOINCs: #{"48765-2"}

Alias: allergies

Entries Required: true

Internal ID: AllergiesandIntolerancesSectioner

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.6.1.html)

## No known allergies sample

```json
{
  "patient" : {
    "resourceType" : "Patient",
    "id" : "patient"
  },
  "clinicalStatus" : {
    "coding" : [ {
      "code" : "active",
      "system" : "http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical"
    } ]
  },
  "type" : "allergy",
  "resourceType" : "AllergyIntolerance",
  "asserter" : {
    "id" : "SOME-STRING",
    "name" : [ {
      "family" : "Seven",
      "given" : [ "Henry" ],
      "suffix" : null,
      "use" : "official"
    } ],
    "qualification" : [ {
      "code" : {
        "coding" : [ {
          "code" : "207Q00000X",
          "display" : "Family Medicine",
          "system" : "http://hl7.org/fhir/ValueSet/provider-taxonomy"
        } ]
      }
    } ],
    "resourceType" : "Practitioner",
    "telecom" : [ {
      "system" : "phone",
      "use" : "work",
      "value" : "555-555-1002"
    } ]
  },
  "recordedDate" : "2010-01-03",
  "recorder" : {
    "id" : "SOME-STRING",
    "name" : [ {
      "family" : "Seven",
      "given" : [ "Henry" ],
      "suffix" : null,
      "use" : "official"
    } ],
    "qualification" : [ {
      "code" : {
        "coding" : [ {
          "code" : "207Q00000X",
          "display" : "Family Medicine",
          "system" : "http://hl7.org/fhir/ValueSet/provider-taxonomy"
        } ]
      }
    } ],
    "resourceType" : "Practitioner",
    "telecom" : [ {
      "system" : "phone",
      "use" : "work",
      "value" : "555-555-1002"
    } ]
  },
  "code" : {
    "coding" : [ {
      "system" : "http://snomed.info/sct",
      "code" : "716186003",
      "display" : "No Known Allergy (situation)"
    } ]
  },
  "reaction" : null
}
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.6.1" extension="2015-08-01"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.6.1"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="48765-2"/>
   <title>Allergies and Intolerances Section (entries required) (V3)</title>
   <text>
      <table border="1" width="100%">
         <thead>
            <tr>
               <td>Type</td>
               <td>Substance</td>
               <td>Onset</td>
               <td>Reactions</td>
               <td>Status</td>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td>allergy</td>
               <td>No Known Allergy (situation) (716186003)</td>
               <td>No information</td>
               <td>-</td>
               <td>active</td>
            </tr>
         </tbody>
      </table>
   </text>
   <entry>
      <act classCode="ACT" moodCode="EVN">
         <templateId root="2.16.840.1.113883.10.20.22.4.30" extension="2015-08-01"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.30"/>
         <code code="CONC" codeSystem="2.16.840.1.113883.5.6"/>
         <statusCode code="active"/>
         <effectiveTime>
            <low value="20100103"/>
            <high nullFlavor="UNK"/>
         </effectiveTime>
         <author>
            <templateId root="2.16.840.1.113883.10.20.22.4.119"/>
            <assignedAuthor>
               <id root="7df888b6-0268-1e62-ff60-ceb2f2f88630"/>
               <code codeSystem="2.16.840.1.113883.6.101"
                      codeSystemName="http://hl7.org/fhir/ValueSet/provider-taxonomy"
                      displayName="Family Medicine"
                      code="207Q00000X"/>
               <addr nullFlavor="UNK"/>
               <telecom value="555-555-1002" use="WP"/>
               <assignedPerson>
                  <name use="L">
                     <given>Henry</given>
                     <family>Seven</family>
                  </name>
               </assignedPerson>
            </assignedAuthor>
         </author>
         <entryRelationship typeCode="SUBJ">
            <observation moodCode="EVN" classCode="OBS">
               <templateId root="2.16.840.1.113883.10.20.22.4.7" extension="2014-06-09"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.7"/>
               <code xsi:type="CD" code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
               <statusCode code="completed"/>
               <effectiveTime>
                  <low nullFlavor="UNK"/>
                  <high nullFlavor="UNK"/>
               </effectiveTime>
               <value code="419199007"
                       displayName="Allergy to substance (finding)"
                       xsi:type="CD"
                       codeSystemName="SNOMED-CT"
                       codeSystem="2.16.840.1.113883.6.96"/>
               <author>
                  <templateId root="2.16.840.1.113883.10.20.22.4.119"/>
                  <time nullFlavor="UNK"/>
                  <assignedAuthor>
                     <id root="7df888b6-0268-1e62-ff60-ceb2f2f88630"/>
                     <code codeSystem="2.16.840.1.113883.6.101"
                            codeSystemName="http://hl7.org/fhir/ValueSet/provider-taxonomy"
                            displayName="Family Medicine"
                            code="207Q00000X"/>
                     <addr nullFlavor="UNK"/>
                     <telecom value="555-555-1002" use="WP"/>
                     <assignedPerson>
                        <name use="L">
                           <given>Henry</given>
                           <family>Seven</family>
                        </name>
                     </assignedPerson>
                  </assignedAuthor>
               </author>
               <participant typeCode="CSM">
                  <participantRole classCode="MANU">
                     <playingEntity classCode="MMAT">
                        <code codeSystem="2.16.840.1.113883.6.96"
                               codeSystemName="http://snomed.info/sct"
                               displayName="No Known Allergy (situation)"
                               code="716186003"/>
                     </playingEntity>
                  </participantRole>
               </participant>
               <entryRelationship typeCode="SUBJ" inversionInd="true">
                  <observation classCode="OBS" moodCode="EVN">
                     <templateId root="2.16.840.1.113883.10.20.22.4.145"/>
                     <code code="82606-5" codeSystem="2.16.840.1.113883.6.1"/>
                     <statusCode code="completed"/>
                     <value code="CRITU"
                             displayName="unable to assess criticality"
                             xsi:type="CD"
                             codeSystem="2.16.840.1.113883.5.1063"/>
                  </observation>
               </entryRelationship>
            </observation>
         </entryRelationship>
      </act>
   </entry>
</section>
```

## sample2
Another trivial sample


```json
{
  "patient" : {
    "id" : "patient",
    "resourceType" : "Patient"
  },
  "onset" : {
    "Period" : {
      "start" : "2010-03-15",
      "end" : null
    }
  },
  "criticality" : "high",
  "clinicalStatus" : {
    "coding" : [ {
      "code" : "active",
      "system" : "http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical"
    } ]
  },
  "meta" : {
    "profile" : [ "http://hl7.org/fhir/us/core/StructureDefinition/us-core-allergyintolerance" ]
  },
  "type" : "allergy",
  "resourceType" : "AllergyIntolerance",
  "asserter" : null,
  "extension" : [ {
    "url" : "authoring-time",
    "value" : {
      "dateTime" : "2020-06-22"
    }
  } ],
  "recordedDate" : "2014-01-03",
  "id" : "4a2ac5fc-0c85-4223-baee-c2e254803974",
  "recorder" : {
    "resourceType" : "Practitioner",
    "id" : "pract",
    "name" : [ {
      "text" : "Dr. Henry Seven",
      "family" : "Seven",
      "given" : [ "Henry" ]
    } ],
    "address" : [ {
      "use" : "work",
      "line" : [ "1002 Healthcare Dr" ],
      "city" : "Portland",
      "state" : "OR",
      "postalCode" : "97266",
      "country" : "USA"
    } ]
  },
  "code" : {
    "coding" : [ {
      "code" : "2670",
      "display" : "Codeine",
      "system" : "http://www.nlm.nih.gov/research/umls/rxnorm"
    } ]
  },
  "verificationStatus" : {
    "coding" : [ {
      "code" : "confirmed",
      "system" : "http://terminology.hl7.org/CodeSystem/allergyintolerance-verification"
    } ]
  },
  "reaction" : [ {
    "manifestation" : [ {
      "coding" : [ {
        "code" : "422587007",
        "display" : "Nausea",
        "system" : "http://snomed.info/sct"
      } ]
    } ],
    "severity" : "severe",
    "onset" : "2012-01-29"
  }, {
    "manifestation" : [ {
      "coding" : [ {
        "code" : "422587007",
        "display" : "Nausea",
        "system" : "http://snomed.info/sct"
      } ]
    } ],
    "severity" : "mild",
    "onset" : "2010-03-15"
  } ]
}
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.6.1" extension="2015-08-01"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.6.1"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="48765-2"/>
   <title>Allergies and Intolerances Section (entries required) (V3)</title>
   <text>
      <table border="1" width="100%">
         <thead>
            <tr>
               <td>Type</td>
               <td>Substance</td>
               <td>Onset</td>
               <td>Reactions</td>
               <td>Status</td>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td>allergy</td>
               <td>Codeine (2670)</td>
               <td>2010-03-15 - </td>
               <td>
                  <paragraph>2012-01-29 Nausea severe</paragraph>
                  <paragraph>2010-03-15 Nausea mild</paragraph>
               </td>
               <td>active</td>
            </tr>
         </tbody>
      </table>
   </text>
   <entry>
      <act moodCode="EVN" classCode="ACT">
         <templateId root="2.16.840.1.113883.10.20.22.4.30" extension="2015-08-01"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.30"/>
         <id root="4a2ac5fc-0c85-4223-baee-c2e254803974"/>
         <code code="CONC" codeSystem="2.16.840.1.113883.5.6"/>
         <statusCode code="active"/>
         <effectiveTime>
            <low value="20140103"/>
            <high nullFlavor="UNK"/>
         </effectiveTime>
         <author>
            <templateId root="2.16.840.1.113883.10.20.22.4.119"/>
            <time value="20200622"/>
            <assignedAuthor>
               <id root="4d7964bc-788b-9976-a728-4e17f8bfd249"/>
               <addr use="WP">
                  <country>USA</country>
                  <state>OR</state>
                  <city>Portland</city>
                  <postalCode>97266</postalCode>
                  <streetAddressLine>1002 Healthcare Dr</streetAddressLine>
               </addr>
               <telecom nullFlavor="UNK"/>
               <assignedPerson>
                  <name>
                     <given>Henry</given>
                     <family>Seven</family>
                  </name>
               </assignedPerson>
            </assignedAuthor>
         </author>
         <entryRelationship typeCode="SUBJ">
            <observation moodCode="EVN" classCode="OBS">
               <templateId root="2.16.840.1.113883.10.20.22.4.7" extension="2014-06-09"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.7"/>
               <id root="4a2ac5fc-0c85-4223-baee-c2e254803974"/>
               <code xsi:type="CD" code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
               <statusCode code="completed"/>
               <effectiveTime>
                  <low nullFlavor="UNK"/>
                  <high nullFlavor="UNK"/>
               </effectiveTime>
               <value code="419199007"
                       displayName="Allergy to substance (finding)"
                       xsi:type="CD"
                       codeSystemName="SNOMED-CT"
                       codeSystem="2.16.840.1.113883.6.96"/>
               <participant typeCode="CSM">
                  <participantRole classCode="MANU">
                     <playingEntity classCode="MMAT">
                        <code codeSystem="2.16.840.1.113883.6.88"
                               codeSystemName="http://www.nlm.nih.gov/research/umls/rxnorm"
                               displayName="Codeine"
                               code="2670"/>
                     </playingEntity>
                  </participantRole>
               </participant>
               <entryRelationship typeCode="SUBJ" inversionInd="true">
                  <observation classCode="OBS" moodCode="EVN">
                     <templateId root="2.16.840.1.113883.10.20.22.4.145"/>
                     <code code="82606-5" codeSystem="2.16.840.1.113883.6.1"/>
                     <statusCode code="completed"/>
                     <value code="CRITH"
                             displayName="high criticality"
                             xsi:type="CD"
                             codeSystem="2.16.840.1.113883.5.1063"/>
                  </observation>
               </entryRelationship>
               <entryRelationship typeCode="MFST" inversionInd="true">
                  <observation moodCode="EVN" classCode="OBS">
                     <templateId root="2.16.840.1.113883.10.20.22.4.9" extension="2014-06-09"/>
                     <templateId root="2.16.840.1.113883.10.20.22.4.9"/>
                     <id nullFlavor="NI"/>
                     <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
                     <statusCode code="completed"/>
                     <effectiveTime value="20120129"/>
                     <value codeSystem="2.16.840.1.113883.6.96"
                             codeSystemName="http://snomed.info/sct"
                             displayName="Nausea"
                             code="422587007"
                             xsi:type="CD"/>
                     <entryRelationship typeCode="SUBJ" inversionInd="true">
                        <observation classCode="OBS" moodCode="EVN">
                           <templateId root="2.16.840.1.113883.10.20.22.4.8" extension="2014-06-09"/>
                           <templateId root="2.16.840.1.113883.10.20.22.4.8"/>
                           <code code="SEV" codeSystem="2.16.840.1.113883.5.4"/>
                           <statusCode code="completed"/>
                           <value code="24484000"
                                   displayName="Severe (severity modifier) (qualifier value)"
                                   codeSystemName="SNOMED-CT"
                                   xsi:type="CD"
                                   codeSystem="2.16.840.1.113883.6.96"/>
                        </observation>
                     </entryRelationship>
                  </observation>
               </entryRelationship>
               <entryRelationship typeCode="MFST" inversionInd="true">
                  <observation moodCode="EVN" classCode="OBS">
                     <templateId root="2.16.840.1.113883.10.20.22.4.9" extension="2014-06-09"/>
                     <templateId root="2.16.840.1.113883.10.20.22.4.9"/>
                     <id nullFlavor="NI"/>
                     <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
                     <statusCode code="completed"/>
                     <effectiveTime value="20100315"/>
                     <value codeSystem="2.16.840.1.113883.6.96"
                             codeSystemName="http://snomed.info/sct"
                             displayName="Nausea"
                             code="422587007"
                             xsi:type="CD"/>
                     <entryRelationship typeCode="SUBJ" inversionInd="true">
                        <observation classCode="OBS" moodCode="EVN">
                           <templateId root="2.16.840.1.113883.10.20.22.4.8" extension="2014-06-09"/>
                           <templateId root="2.16.840.1.113883.10.20.22.4.8"/>
                           <code code="SEV" codeSystem="2.16.840.1.113883.5.4"/>
                           <statusCode code="completed"/>
                           <value code="255604002"
                                   displayName="Mild (qualifier value)"
                                   codeSystemName="SNOMED-CT"
                                   xsi:type="CD"
                                   codeSystem="2.16.840.1.113883.6.96"/>
                        </observation>
                     </entryRelationship>
                  </observation>
               </entryRelationship>
            </observation>
         </entryRelationship>
      </act>
   </entry>
</section>
```

## sample1
Trivial allergy example


```json
{
  "patient" : {
    "id" : "patient",
    "resourceType" : "Patient"
  },
  "onset" : {
    "Period" : {
      "start" : "2010-03-15",
      "end" : null
    }
  },
  "criticality" : "high",
  "clinicalStatus" : {
    "coding" : [ {
      "code" : "active",
      "system" : "http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical"
    } ]
  },
  "meta" : {
    "profile" : [ "http://hl7.org/fhir/us/core/StructureDefinition/us-core-allergyintolerance" ]
  },
  "type" : "intolerance",
  "resourceType" : "AllergyIntolerance",
  "asserter" : {
    "name" : [ {
      "given" : [ "Henry" ],
      "family" : "Seven",
      "use" : "official",
      "suffix" : null
    } ],
    "telecom" : [ {
      "system" : "phone",
      "value" : "555-555-1002",
      "use" : "work"
    } ],
    "qualification" : [ {
      "code" : {
        "coding" : [ {
          "code" : "207Q00000X",
          "display" : "Family Medicine",
          "system" : "http://hl7.org/fhir/ValueSet/provider-taxonomy"
        } ]
      }
    } ],
    "id" : "SOME-STRING",
    "resourceType" : "Practitioner"
  },
  "recordedDate" : "2014-01-03",
  "id" : "4a2ac5fc-0c85-4223-baee-c2e254803974",
  "recorder" : {
    "name" : [ {
      "given" : [ "Henry" ],
      "family" : "Seven",
      "use" : "official",
      "suffix" : null
    } ],
    "telecom" : [ {
      "system" : "phone",
      "value" : "555-555-1002",
      "use" : "work"
    } ],
    "qualification" : [ {
      "code" : {
        "coding" : [ {
          "code" : "207Q00000X",
          "display" : "Family Medicine",
          "system" : "http://hl7.org/fhir/ValueSet/provider-taxonomy"
        } ]
      }
    } ],
    "id" : "SOME-STRING",
    "resourceType" : "Practitioner"
  },
  "code" : {
    "coding" : [ {
      "code" : "2670",
      "display" : "Codeine",
      "system" : "http://www.nlm.nih.gov/research/umls/rxnorm"
    } ]
  },
  "reaction" : [ {
    "manifestation" : [ {
      "coding" : [ {
        "code" : "422587007",
        "display" : "Nausea",
        "system" : "http://snomed.info/sct"
      } ]
    } ],
    "severity" : "severe",
    "onset" : "2012-01-29"
  }, {
    "manifestation" : [ {
      "coding" : [ {
        "code" : "422587007",
        "display" : "Nausea",
        "system" : "http://snomed.info/sct"
      } ]
    } ],
    "severity" : "mild",
    "onset" : "2010-03-15"
  } ]
}
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.6.1" extension="2015-08-01"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.6.1"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="48765-2"/>
   <title>Allergies and Intolerances Section (entries required) (V3)</title>
   <text>
      <table border="1" width="100%">
         <thead>
            <tr>
               <td>Type</td>
               <td>Substance</td>
               <td>Onset</td>
               <td>Reactions</td>
               <td>Status</td>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td>intolerance</td>
               <td>Codeine (2670)</td>
               <td>2010-03-15 - </td>
               <td>
                  <paragraph>2012-01-29 Nausea severe</paragraph>
                  <paragraph>2010-03-15 Nausea mild</paragraph>
               </td>
               <td>active</td>
            </tr>
         </tbody>
      </table>
   </text>
   <entry>
      <act moodCode="EVN" classCode="ACT">
         <templateId root="2.16.840.1.113883.10.20.22.4.30" extension="2015-08-01"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.30"/>
         <id root="4a2ac5fc-0c85-4223-baee-c2e254803974"/>
         <code code="CONC" codeSystem="2.16.840.1.113883.5.6"/>
         <statusCode code="active"/>
         <effectiveTime>
            <low value="20140103"/>
            <high nullFlavor="UNK"/>
         </effectiveTime>
         <author>
            <templateId root="2.16.840.1.113883.10.20.22.4.119"/>
            <assignedAuthor>
               <id root="7df888b6-0268-1e62-ff60-ceb2f2f88630"/>
               <code codeSystem="2.16.840.1.113883.6.101"
                      codeSystemName="http://hl7.org/fhir/ValueSet/provider-taxonomy"
                      displayName="Family Medicine"
                      code="207Q00000X"/>
               <addr nullFlavor="UNK"/>
               <telecom value="555-555-1002" use="WP"/>
               <assignedPerson>
                  <name use="L">
                     <given>Henry</given>
                     <family>Seven</family>
                  </name>
               </assignedPerson>
            </assignedAuthor>
         </author>
         <entryRelationship typeCode="SUBJ">
            <observation moodCode="EVN" classCode="OBS">
               <templateId root="2.16.840.1.113883.10.20.22.4.7" extension="2014-06-09"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.7"/>
               <id root="4a2ac5fc-0c85-4223-baee-c2e254803974"/>
               <code xsi:type="CD" code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
               <statusCode code="completed"/>
               <effectiveTime>
                  <low nullFlavor="UNK"/>
                  <high nullFlavor="UNK"/>
               </effectiveTime>
               <value code="59037007"
                       displayName="Intolerance to drug (finding)"
                       xsi:type="CD"
                       codeSystemName="SNOMED-CT"
                       codeSystem="2.16.840.1.113883.6.96"/>
               <author>
                  <templateId root="2.16.840.1.113883.10.20.22.4.119"/>
                  <time nullFlavor="UNK"/>
                  <assignedAuthor>
                     <id root="7df888b6-0268-1e62-ff60-ceb2f2f88630"/>
                     <code codeSystem="2.16.840.1.113883.6.101"
                            codeSystemName="http://hl7.org/fhir/ValueSet/provider-taxonomy"
                            displayName="Family Medicine"
                            code="207Q00000X"/>
                     <addr nullFlavor="UNK"/>
                     <telecom value="555-555-1002" use="WP"/>
                     <assignedPerson>
                        <name use="L">
                           <given>Henry</given>
                           <family>Seven</family>
                        </name>
                     </assignedPerson>
                  </assignedAuthor>
               </author>
               <participant typeCode="CSM">
                  <participantRole classCode="MANU">
                     <playingEntity classCode="MMAT">
                        <code codeSystem="2.16.840.1.113883.6.88"
                               codeSystemName="http://www.nlm.nih.gov/research/umls/rxnorm"
                               displayName="Codeine"
                               code="2670"/>
                     </playingEntity>
                  </participantRole>
               </participant>
               <entryRelationship typeCode="SUBJ" inversionInd="true">
                  <observation classCode="OBS" moodCode="EVN">
                     <templateId root="2.16.840.1.113883.10.20.22.4.145"/>
                     <code code="82606-5" codeSystem="2.16.840.1.113883.6.1"/>
                     <statusCode code="completed"/>
                     <value code="CRITH"
                             displayName="high criticality"
                             xsi:type="CD"
                             codeSystem="2.16.840.1.113883.5.1063"/>
                  </observation>
               </entryRelationship>
               <entryRelationship typeCode="MFST" inversionInd="true">
                  <observation moodCode="EVN" classCode="OBS">
                     <templateId root="2.16.840.1.113883.10.20.22.4.9" extension="2014-06-09"/>
                     <templateId root="2.16.840.1.113883.10.20.22.4.9"/>
                     <id nullFlavor="NI"/>
                     <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
                     <statusCode code="completed"/>
                     <effectiveTime value="20120129"/>
                     <value codeSystem="2.16.840.1.113883.6.96"
                             codeSystemName="http://snomed.info/sct"
                             displayName="Nausea"
                             code="422587007"
                             xsi:type="CD"/>
                     <entryRelationship typeCode="SUBJ" inversionInd="true">
                        <observation classCode="OBS" moodCode="EVN">
                           <templateId root="2.16.840.1.113883.10.20.22.4.8" extension="2014-06-09"/>
                           <templateId root="2.16.840.1.113883.10.20.22.4.8"/>
                           <code code="SEV" codeSystem="2.16.840.1.113883.5.4"/>
                           <statusCode code="completed"/>
                           <value code="24484000"
                                   displayName="Severe (severity modifier) (qualifier value)"
                                   codeSystemName="SNOMED-CT"
                                   xsi:type="CD"
                                   codeSystem="2.16.840.1.113883.6.96"/>
                        </observation>
                     </entryRelationship>
                  </observation>
               </entryRelationship>
               <entryRelationship typeCode="MFST" inversionInd="true">
                  <observation moodCode="EVN" classCode="OBS">
                     <templateId root="2.16.840.1.113883.10.20.22.4.9" extension="2014-06-09"/>
                     <templateId root="2.16.840.1.113883.10.20.22.4.9"/>
                     <id nullFlavor="NI"/>
                     <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
                     <statusCode code="completed"/>
                     <effectiveTime value="20100315"/>
                     <value codeSystem="2.16.840.1.113883.6.96"
                             codeSystemName="http://snomed.info/sct"
                             displayName="Nausea"
                             code="422587007"
                             xsi:type="CD"/>
                     <entryRelationship typeCode="SUBJ" inversionInd="true">
                        <observation classCode="OBS" moodCode="EVN">
                           <templateId root="2.16.840.1.113883.10.20.22.4.8" extension="2014-06-09"/>
                           <templateId root="2.16.840.1.113883.10.20.22.4.8"/>
                           <code code="SEV" codeSystem="2.16.840.1.113883.5.4"/>
                           <statusCode code="completed"/>
                           <value code="255604002"
                                   displayName="Mild (qualifier value)"
                                   codeSystemName="SNOMED-CT"
                                   xsi:type="CD"
                                   codeSystem="2.16.840.1.113883.6.96"/>
                        </observation>
                     </entryRelationship>
                  </observation>
               </entryRelationship>
            </observation>
         </entryRelationship>
      </act>
   </entry>
</section>
```

## sample3
Another trivial sample


```json
{
  "patient" : {
    "uri" : "urn:uuid:9f8231b9-aa93-0d4b-04a7-44e76a7a0a5b"
  },
  "onset" : {
    "Period" : {
      "start" : "1980-05-01"
    }
  },
  "clinicalStatus" : {
    "coding" : [ {
      "code" : "active",
      "system" : "http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical"
    } ]
  },
  "meta" : {
    "profile" : [ "http://hl7.org/fhir/us/core/StructureDefinition/us-core-allergyintolerance" ]
  },
  "type" : "intolerance",
  "resourceType" : "AllergyIntolerance",
  "recordedDate" : "1980-05-01",
  "id" : "36e3e930-7b14-11db-9fe1-0800200c9a66",
  "code" : {
    "coding" : [ {
      "code" : "7980",
      "display" : "Penicillin G",
      "system" : "http://www.nlm.nih.gov/research/umls/rxnorm"
    } ]
  },
  "reaction" : [ {
    "manifestation" : [ {
      "coding" : [ {
        "code" : "247472004",
        "display" : "Hives",
        "system" : "http://snomed.info/sct"
      } ]
    } ],
    "severity" : "moderate"
  }, {
    "manifestation" : [ {
      "coding" : [ {
        "code" : "422587007",
        "display" : "Nausea",
        "system" : "http://snomed.info/sct"
      } ]
    } ],
    "onset" : "2010-03-15",
    "severity" : "mild"
  } ]
}
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.6.1" extension="2015-08-01"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.6.1"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="48765-2"/>
   <title>Allergies and Intolerances Section (entries required) (V3)</title>
   <text>
      <table border="1" width="100%">
         <thead>
            <tr>
               <td>Type</td>
               <td>Substance</td>
               <td>Onset</td>
               <td>Reactions</td>
               <td>Status</td>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td>intolerance</td>
               <td>Penicillin G (7980)</td>
               <td>1980-05-01 - </td>
               <td>
                  <paragraph>- Hives moderate</paragraph>
                  <paragraph>2010-03-15 Nausea mild</paragraph>
               </td>
               <td>active</td>
            </tr>
         </tbody>
      </table>
   </text>
   <entry>
      <act classCode="ACT" moodCode="EVN">
         <templateId root="2.16.840.1.113883.10.20.22.4.30" extension="2015-08-01"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.30"/>
         <id root="36e3e930-7b14-11db-9fe1-0800200c9a66"/>
         <code code="CONC" codeSystem="2.16.840.1.113883.5.6"/>
         <statusCode code="active"/>
         <effectiveTime>
            <low value="19800501"/>
            <high nullFlavor="UNK"/>
         </effectiveTime>
         <entryRelationship typeCode="SUBJ">
            <observation moodCode="EVN" classCode="OBS">
               <templateId root="2.16.840.1.113883.10.20.22.4.7" extension="2014-06-09"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.7"/>
               <id root="36e3e930-7b14-11db-9fe1-0800200c9a66"/>
               <code xsi:type="CD" code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
               <statusCode code="completed"/>
               <effectiveTime>
                  <low nullFlavor="UNK"/>
                  <high nullFlavor="UNK"/>
               </effectiveTime>
               <value code="59037007"
                       displayName="Intolerance to drug (finding)"
                       xsi:type="CD"
                       codeSystemName="SNOMED-CT"
                       codeSystem="2.16.840.1.113883.6.96"/>
               <participant typeCode="CSM">
                  <participantRole classCode="MANU">
                     <playingEntity classCode="MMAT">
                        <code codeSystem="2.16.840.1.113883.6.88"
                               codeSystemName="http://www.nlm.nih.gov/research/umls/rxnorm"
                               displayName="Penicillin G"
                               code="7980"/>
                     </playingEntity>
                  </participantRole>
               </participant>
               <entryRelationship typeCode="SUBJ" inversionInd="true">
                  <observation classCode="OBS" moodCode="EVN">
                     <templateId root="2.16.840.1.113883.10.20.22.4.145"/>
                     <code code="82606-5" codeSystem="2.16.840.1.113883.6.1"/>
                     <statusCode code="completed"/>
                     <value code="CRITU"
                             displayName="unable to assess criticality"
                             xsi:type="CD"
                             codeSystem="2.16.840.1.113883.5.1063"/>
                  </observation>
               </entryRelationship>
               <entryRelationship typeCode="MFST" inversionInd="true">
                  <observation classCode="OBS" moodCode="EVN">
                     <templateId root="2.16.840.1.113883.10.20.22.4.9" extension="2014-06-09"/>
                     <templateId root="2.16.840.1.113883.10.20.22.4.9"/>
                     <id nullFlavor="NI"/>
                     <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
                     <statusCode code="completed"/>
                     <value codeSystem="2.16.840.1.113883.6.96"
                             codeSystemName="http://snomed.info/sct"
                             displayName="Hives"
                             code="247472004"
                             xsi:type="CD"/>
                     <entryRelationship typeCode="SUBJ" inversionInd="true">
                        <observation classCode="OBS" moodCode="EVN">
                           <templateId root="2.16.840.1.113883.10.20.22.4.8" extension="2014-06-09"/>
                           <templateId root="2.16.840.1.113883.10.20.22.4.8"/>
                           <code code="SEV" codeSystem="2.16.840.1.113883.5.4"/>
                           <statusCode code="completed"/>
                           <value code="6736007"
                                   displayName="Moderate (severity modifier) (qualifier value)"
                                   codeSystemName="SNOMED-CT"
                                   xsi:type="CD"
                                   codeSystem="2.16.840.1.113883.6.96"/>
                        </observation>
                     </entryRelationship>
                  </observation>
               </entryRelationship>
               <entryRelationship typeCode="MFST" inversionInd="true">
                  <observation moodCode="EVN" classCode="OBS">
                     <templateId root="2.16.840.1.113883.10.20.22.4.9" extension="2014-06-09"/>
                     <templateId root="2.16.840.1.113883.10.20.22.4.9"/>
                     <id nullFlavor="NI"/>
                     <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
                     <statusCode code="completed"/>
                     <effectiveTime value="20100315"/>
                     <value codeSystem="2.16.840.1.113883.6.96"
                             codeSystemName="http://snomed.info/sct"
                             displayName="Nausea"
                             code="422587007"
                             xsi:type="CD"/>
                     <entryRelationship typeCode="SUBJ" inversionInd="true">
                        <observation classCode="OBS" moodCode="EVN">
                           <templateId root="2.16.840.1.113883.10.20.22.4.8" extension="2014-06-09"/>
                           <templateId root="2.16.840.1.113883.10.20.22.4.8"/>
                           <code code="SEV" codeSystem="2.16.840.1.113883.5.4"/>
                           <statusCode code="completed"/>
                           <value code="255604002"
                                   displayName="Mild (qualifier value)"
                                   codeSystemName="SNOMED-CT"
                                   xsi:type="CD"
                                   codeSystem="2.16.840.1.113883.6.96"/>
                        </observation>
                     </entryRelationship>
                  </observation>
               </entryRelationship>
            </observation>
         </entryRelationship>
      </act>
   </entry>
</section>
```
---
description: >-
  This page contains list of supported C-CDA sections.
---

# Supported sections

Here is a list of supported sections.


## Allergies and Intolerances Section (entries required) (V3)
OID: 2.16.840.1.113883.10.20.22.2.6.1

LOINC: 48765-2

Internal ID: AllergiesandIntolerancesSectioner

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.6.1.html)

### No known allergies sample

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
                   codeSystemName="HTTP://HL7.ORG/FHIR/VALUESET/PROVIDER-TAXONOMY"
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
            <id root="3d502c0f-d225-440b-b23c-19a5858189cf"/>
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
                         codeSystemName="HTTP://HL7.ORG/FHIR/VALUESET/PROVIDER-TAXONOMY"
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
                            codeSystemName="HTTP://SNOMED.INFO/SCT"
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
```

### sample2
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
  "recordedDate" : "2014-01-03",
  "id" : "4a2ac5fc-0c85-4223-baee-c2e254803974",
  "recorder" : null,
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
<entry>
   <act classCode="ACT" moodCode="EVN">
      <templateId root="2.16.840.1.113883.10.20.22.4.30" extension="2015-08-01"/>
      <templateId root="2.16.840.1.113883.10.20.22.4.30"/>
      <id root="4a2ac5fc-0c85-4223-baee-c2e254803974"/>
      <code code="CONC" codeSystem="2.16.840.1.113883.5.6"/>
      <statusCode code="active"/>
      <effectiveTime>
         <low value="20140103"/>
         <high nullFlavor="UNK"/>
      </effectiveTime>
      <entryRelationship typeCode="SUBJ">
         <observation moodCode="EVN" classCode="OBS">
            <templateId root="2.16.840.1.113883.10.20.22.4.7" extension="2014-06-09"/>
            <templateId root="2.16.840.1.113883.10.20.22.4.7"/>
            <id root="3ac66c1a-0429-4e80-b508-d0e7568cc5a0"/>
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
                            codeSystemName="HTTP://WWW.NLM.NIH.GOV/RESEARCH/UMLS/RXNORM"
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
                  <id root="5b246f34-1f38-46cc-a2cc-86729e321d98"/>
                  <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
                  <statusCode code="completed"/>
                  <effectiveTime value="20120129"/>
                  <value codeSystem="2.16.840.1.113883.6.96"
                          codeSystemName="HTTP://SNOMED.INFO/SCT"
                          displayName="Nausea"
                          code="422587007"
                          xsi:type="CD"/>
                  <entryRelationship typeCode="SUBJ" inversionInd="true">
                     <observation classCode="OBS" moodCode="EVN">
                        <templateId root="2.16.840.1.113883.10.20.22.4.8" extension="2014-06-09"/>
                        <templateId root="2.16.840.1.113883.10.20.22.4.8"/>
                        <id root="24dff665-6818-4896-8014-0eaf7d6ecf91"/>
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
                  <id root="4638a539-abf0-44f9-9da2-7cfcb52838f2"/>
                  <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
                  <statusCode code="completed"/>
                  <effectiveTime value="20100315"/>
                  <value codeSystem="2.16.840.1.113883.6.96"
                          codeSystemName="HTTP://SNOMED.INFO/SCT"
                          displayName="Nausea"
                          code="422587007"
                          xsi:type="CD"/>
                  <entryRelationship typeCode="SUBJ" inversionInd="true">
                     <observation classCode="OBS" moodCode="EVN">
                        <templateId root="2.16.840.1.113883.10.20.22.4.8" extension="2014-06-09"/>
                        <templateId root="2.16.840.1.113883.10.20.22.4.8"/>
                        <id root="56d9092c-43e2-4661-9887-03c578c23787"/>
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
```

### sample1
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
                   codeSystemName="HTTP://HL7.ORG/FHIR/VALUESET/PROVIDER-TAXONOMY"
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
            <id root="70e51186-9037-4053-a231-9a126b774d96"/>
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
                         codeSystemName="HTTP://HL7.ORG/FHIR/VALUESET/PROVIDER-TAXONOMY"
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
                            codeSystemName="HTTP://WWW.NLM.NIH.GOV/RESEARCH/UMLS/RXNORM"
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
                  <id root="eaab6422-b0e6-4694-860e-5fa007798393"/>
                  <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
                  <statusCode code="completed"/>
                  <effectiveTime value="20120129"/>
                  <value codeSystem="2.16.840.1.113883.6.96"
                          codeSystemName="HTTP://SNOMED.INFO/SCT"
                          displayName="Nausea"
                          code="422587007"
                          xsi:type="CD"/>
                  <entryRelationship typeCode="SUBJ" inversionInd="true">
                     <observation classCode="OBS" moodCode="EVN">
                        <templateId root="2.16.840.1.113883.10.20.22.4.8" extension="2014-06-09"/>
                        <templateId root="2.16.840.1.113883.10.20.22.4.8"/>
                        <id root="f9009309-7c9a-4009-9a98-da08fe8701db"/>
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
                  <id root="7e1e02b9-3f9d-477b-9afe-3c60e9d02dcf"/>
                  <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
                  <statusCode code="completed"/>
                  <effectiveTime value="20100315"/>
                  <value codeSystem="2.16.840.1.113883.6.96"
                          codeSystemName="HTTP://SNOMED.INFO/SCT"
                          displayName="Nausea"
                          code="422587007"
                          xsi:type="CD"/>
                  <entryRelationship typeCode="SUBJ" inversionInd="true">
                     <observation classCode="OBS" moodCode="EVN">
                        <templateId root="2.16.840.1.113883.10.20.22.4.8" extension="2014-06-09"/>
                        <templateId root="2.16.840.1.113883.10.20.22.4.8"/>
                        <id root="9c18ad35-4374-4a50-b912-8552177964f8"/>
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
```

### sample3
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
            <id root="31fe4716-d30c-4d6c-808d-682f88267ecd"/>
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
                            codeSystemName="HTTP://WWW.NLM.NIH.GOV/RESEARCH/UMLS/RXNORM"
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
                  <id root="05b0e302-ac0f-4914-9d49-bbe2bf4ea2a8"/>
                  <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
                  <statusCode code="completed"/>
                  <value codeSystem="2.16.840.1.113883.6.96"
                          codeSystemName="HTTP://SNOMED.INFO/SCT"
                          displayName="Hives"
                          code="247472004"
                          xsi:type="CD"/>
                  <entryRelationship typeCode="SUBJ" inversionInd="true">
                     <observation classCode="OBS" moodCode="EVN">
                        <templateId root="2.16.840.1.113883.10.20.22.4.8" extension="2014-06-09"/>
                        <templateId root="2.16.840.1.113883.10.20.22.4.8"/>
                        <id root="3fd65124-ccb6-4d7f-9532-3f9cb0b35c8d"/>
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
                  <id root="8b1c56ae-732c-4b8b-9dc0-e3b709145ac2"/>
                  <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
                  <statusCode code="completed"/>
                  <effectiveTime value="20100315"/>
                  <value codeSystem="2.16.840.1.113883.6.96"
                          codeSystemName="HTTP://SNOMED.INFO/SCT"
                          displayName="Nausea"
                          code="422587007"
                          xsi:type="CD"/>
                  <entryRelationship typeCode="SUBJ" inversionInd="true">
                     <observation classCode="OBS" moodCode="EVN">
                        <templateId root="2.16.840.1.113883.10.20.22.4.8" extension="2014-06-09"/>
                        <templateId root="2.16.840.1.113883.10.20.22.4.8"/>
                        <id root="af082231-ced4-492a-b7f6-c317c401fc00"/>
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
```

## Vital Signs Section (entries optional) (V3)
OID: 2.16.840.1.113883.10.20.22.2.4

LOINC: 8716-3

Internal ID: VitalSignsSectionentriesoptional

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.4.html)



## Encounters Section (entries required) (V3)
OID: 2.16.840.1.113883.10.20.22.2.22.1

LOINC: 46240-8

Internal ID: EncountersSectionentriesrequiredV3

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.22.1.html)



## Allergies and Intolerances Section (entries optional) (V3)
OID: 2.16.840.1.113883.10.20.22.2.6

LOINC: 48765-2

Internal ID: AllergiesandIntolerancesSectioneo

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.6.html)

### No known allergies sample

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
                   codeSystemName="HTTP://HL7.ORG/FHIR/VALUESET/PROVIDER-TAXONOMY"
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
            <id root="2da81dee-fc28-4758-90a6-f9a252098b92"/>
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
                         codeSystemName="HTTP://HL7.ORG/FHIR/VALUESET/PROVIDER-TAXONOMY"
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
                            codeSystemName="HTTP://SNOMED.INFO/SCT"
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
```

### sample2
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
  "recordedDate" : "2014-01-03",
  "id" : "4a2ac5fc-0c85-4223-baee-c2e254803974",
  "recorder" : null,
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
<entry>
   <act classCode="ACT" moodCode="EVN">
      <templateId root="2.16.840.1.113883.10.20.22.4.30" extension="2015-08-01"/>
      <templateId root="2.16.840.1.113883.10.20.22.4.30"/>
      <id root="4a2ac5fc-0c85-4223-baee-c2e254803974"/>
      <code code="CONC" codeSystem="2.16.840.1.113883.5.6"/>
      <statusCode code="active"/>
      <effectiveTime>
         <low value="20140103"/>
         <high nullFlavor="UNK"/>
      </effectiveTime>
      <entryRelationship typeCode="SUBJ">
         <observation moodCode="EVN" classCode="OBS">
            <templateId root="2.16.840.1.113883.10.20.22.4.7" extension="2014-06-09"/>
            <templateId root="2.16.840.1.113883.10.20.22.4.7"/>
            <id root="f1b06d4f-79a1-474e-81fe-f48188692918"/>
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
                            codeSystemName="HTTP://WWW.NLM.NIH.GOV/RESEARCH/UMLS/RXNORM"
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
                  <id root="70619167-1829-469c-912b-671b2ab512a2"/>
                  <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
                  <statusCode code="completed"/>
                  <effectiveTime value="20120129"/>
                  <value codeSystem="2.16.840.1.113883.6.96"
                          codeSystemName="HTTP://SNOMED.INFO/SCT"
                          displayName="Nausea"
                          code="422587007"
                          xsi:type="CD"/>
                  <entryRelationship typeCode="SUBJ" inversionInd="true">
                     <observation classCode="OBS" moodCode="EVN">
                        <templateId root="2.16.840.1.113883.10.20.22.4.8" extension="2014-06-09"/>
                        <templateId root="2.16.840.1.113883.10.20.22.4.8"/>
                        <id root="d97ad122-547f-4f2c-a330-6d5968042da6"/>
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
                  <id root="03ea2fc7-b9cf-448a-bdd8-35756b459656"/>
                  <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
                  <statusCode code="completed"/>
                  <effectiveTime value="20100315"/>
                  <value codeSystem="2.16.840.1.113883.6.96"
                          codeSystemName="HTTP://SNOMED.INFO/SCT"
                          displayName="Nausea"
                          code="422587007"
                          xsi:type="CD"/>
                  <entryRelationship typeCode="SUBJ" inversionInd="true">
                     <observation classCode="OBS" moodCode="EVN">
                        <templateId root="2.16.840.1.113883.10.20.22.4.8" extension="2014-06-09"/>
                        <templateId root="2.16.840.1.113883.10.20.22.4.8"/>
                        <id root="08db9033-77b1-493e-9183-56fc2cfa04d9"/>
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
```

### sample1
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
                   codeSystemName="HTTP://HL7.ORG/FHIR/VALUESET/PROVIDER-TAXONOMY"
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
            <id root="aa80da62-d1f9-4ded-bc5b-2a2b51cb5bb6"/>
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
                         codeSystemName="HTTP://HL7.ORG/FHIR/VALUESET/PROVIDER-TAXONOMY"
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
                            codeSystemName="HTTP://WWW.NLM.NIH.GOV/RESEARCH/UMLS/RXNORM"
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
                  <id root="0ce5df64-0c9c-4177-95b0-9e84f47077e3"/>
                  <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
                  <statusCode code="completed"/>
                  <effectiveTime value="20120129"/>
                  <value codeSystem="2.16.840.1.113883.6.96"
                          codeSystemName="HTTP://SNOMED.INFO/SCT"
                          displayName="Nausea"
                          code="422587007"
                          xsi:type="CD"/>
                  <entryRelationship typeCode="SUBJ" inversionInd="true">
                     <observation classCode="OBS" moodCode="EVN">
                        <templateId root="2.16.840.1.113883.10.20.22.4.8" extension="2014-06-09"/>
                        <templateId root="2.16.840.1.113883.10.20.22.4.8"/>
                        <id root="34de33fa-450d-4a7c-a6a4-9a768c680fbe"/>
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
                  <id root="3c0fd047-58b2-47a0-85e6-87db2ae76755"/>
                  <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
                  <statusCode code="completed"/>
                  <effectiveTime value="20100315"/>
                  <value codeSystem="2.16.840.1.113883.6.96"
                          codeSystemName="HTTP://SNOMED.INFO/SCT"
                          displayName="Nausea"
                          code="422587007"
                          xsi:type="CD"/>
                  <entryRelationship typeCode="SUBJ" inversionInd="true">
                     <observation classCode="OBS" moodCode="EVN">
                        <templateId root="2.16.840.1.113883.10.20.22.4.8" extension="2014-06-09"/>
                        <templateId root="2.16.840.1.113883.10.20.22.4.8"/>
                        <id root="4e962b0c-97c2-48b6-b069-b967fe842146"/>
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
```

### sample3
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
            <id root="70a6b3bc-e22a-42e5-ab85-d4573862c9b6"/>
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
                            codeSystemName="HTTP://WWW.NLM.NIH.GOV/RESEARCH/UMLS/RXNORM"
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
                  <id root="f239b11a-de7b-4eae-8aa9-3c5386a1538f"/>
                  <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
                  <statusCode code="completed"/>
                  <value codeSystem="2.16.840.1.113883.6.96"
                          codeSystemName="HTTP://SNOMED.INFO/SCT"
                          displayName="Hives"
                          code="247472004"
                          xsi:type="CD"/>
                  <entryRelationship typeCode="SUBJ" inversionInd="true">
                     <observation classCode="OBS" moodCode="EVN">
                        <templateId root="2.16.840.1.113883.10.20.22.4.8" extension="2014-06-09"/>
                        <templateId root="2.16.840.1.113883.10.20.22.4.8"/>
                        <id root="e1e531c7-ef71-4713-940b-330c41fdbff9"/>
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
                  <id root="09271994-1a0e-4cb1-a882-576232fca270"/>
                  <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
                  <statusCode code="completed"/>
                  <effectiveTime value="20100315"/>
                  <value codeSystem="2.16.840.1.113883.6.96"
                          codeSystemName="HTTP://SNOMED.INFO/SCT"
                          displayName="Nausea"
                          code="422587007"
                          xsi:type="CD"/>
                  <entryRelationship typeCode="SUBJ" inversionInd="true">
                     <observation classCode="OBS" moodCode="EVN">
                        <templateId root="2.16.840.1.113883.10.20.22.4.8" extension="2014-06-09"/>
                        <templateId root="2.16.840.1.113883.10.20.22.4.8"/>
                        <id root="f96031b4-e223-4f75-9297-70553ed0f978"/>
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
```

## Problem Section (entries optional) (V3)
OID: 2.16.840.1.113883.10.20.22.2.5

LOINC: 11450-4

Internal ID: ProblemSectionentriesoptionalV3

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.5.html)



## 
OID: 

LOINC: 

Internal ID: Header

[IG Link](https://www.hl7.org/ccdasearch/templates/.html)



## Problem Section (entries required) (V3)
OID: 2.16.840.1.113883.10.20.22.2.5.1

LOINC: 11450-4

Internal ID: ProblemSectionentriesrequiredV3

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.5.1.html)



## Encounters Section (entries optional) (V3)
OID: 2.16.840.1.113883.10.20.22.2.22

LOINC: 46240-8

Internal ID: EncountersSectionentriesoptionalV3

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.22.html)



## Procedures Section (entries required) (V2)
OID: 2.16.840.1.113883.10.20.22.2.7.1

LOINC: 47519-4

Internal ID: ProceduresSectionentriesrequiredV

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.7.1.html)



## Immunizations Section (entries required) (V3)
OID: 2.16.840.1.113883.10.20.22.2.2.1

LOINC: 11369-6

Internal ID: ImmunizationsSectionentriesrequire

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.2.1.html)



## Results Section (entries required) (V3)
OID: 2.16.840.1.113883.10.20.22.2.3.1

LOINC: 30954-2

Internal ID: ResultsSectionentriesrequiredV3

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.3.1.html)



## Medications Section (entries required) (V2)
OID: 2.16.840.1.113883.10.20.22.2.1.1

LOINC: 10160-0

Internal ID: MedicationsSectionentriesrequired

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.1.1.html)



## Goals Section
OID: 2.16.840.1.113883.10.20.22.2.60

LOINC: 61146-7

Internal ID: GoalsSection

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.60.html)



## Vital Signs Section (entries required) (V3)
OID: 2.16.840.1.113883.10.20.22.2.4.1

LOINC: 8716-3

Internal ID: VitalSignsSectionentriesrequired

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.4.1.html)



## Mental Status Section (V2)
OID: 2.16.840.1.113883.10.20.22.2.56

LOINC: 10190-7

Internal ID: MentalStatusSectionV2

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.56.html)



## Immunizations Section (entries optional) (V3)
OID: 2.16.840.1.113883.10.20.22.2.2

LOINC: 11369-6

Internal ID: ImmunizationsSectionentriesoptiona

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.2.html)



## Health Concerns Section (V2)
OID: 2.16.840.1.113883.10.20.22.2.58

LOINC: 75310-3

Internal ID: HealthConcernsSectionV2

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.58.html)



## Plan of Treatment Section (V2)
OID: 2.16.840.1.113883.10.20.22.2.10

LOINC: 18776-5

Internal ID: PlanofTreatmentSectionV2

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.10.html)



## Procedures Section (entries optional) (V2)
OID: 2.16.840.1.113883.10.20.22.2.7

LOINC: 47519-4

Internal ID: ProceduresSectionentriesoptionalV2

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.7.html)



## Medications Section (entries optional) (V2)
OID: 2.16.840.1.113883.10.20.22.2.1

LOINC: 10160-0

Internal ID: MedicationsSectionentriesoptional

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.1.html)



## Functional Status Section (V2)
OID: 2.16.840.1.113883.10.20.22.2.14

LOINC: 47420-5

Internal ID: FunctionalStatusSectionV2

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.14.html)



## Results Section (entries optional) (V3)
OID: 2.16.840.1.113883.10.20.22.2.3

LOINC: 30954-2

Internal ID: ResultsSectionentriesoptionalV3

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.3.html)



## Nutrition Section
OID: 2.16.840.1.113883.10.20.22.2.57

LOINC: 61144-2

Internal ID: NutritionSection

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.57.html)



## Medical Equipment Section (V2)
OID: 2.16.840.1.113883.10.20.22.2.23

LOINC: 46264-8

Internal ID: MedicalEquipmentSectionV2

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.23.html)



## Payers Section (V3)
OID: 2.16.840.1.113883.10.20.22.2.18

LOINC: 48768-6

Internal ID: PayersSectionV3

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.18.html)



## Family History Section (V3)
OID: 2.16.840.1.113883.10.20.22.2.15

LOINC: 10157-6

Internal ID: FamilyHistorySectionV3

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.15.html)



## Social History Section (V3)
OID: 2.16.840.1.113883.10.20.22.2.17

LOINC: 29762-2

Internal ID: SocialHistorySectionV3

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.17.html)



## Notes
OID: 2.16.840.1.113883.10.20.22.2.65

LOINC: 

Internal ID: NotesSection

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.65.html)


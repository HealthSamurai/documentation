# Allergies and Intolerances Section (entries optional) (V3)

OID: 2.16.840.1.113883.10.20.22.2.6

LOINC: 48765-2

Alias: allergies

Internal ID: AllergiesandIntolerancesSectioneo

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.6.html)

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
            <id root="e71aef62-7c3d-4cd3-a52c-285fda640b2d"/>
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
            <id root="bf412e02-fbe0-4aa9-bdf1-f05736ad55b8"/>
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
                  <id root="312a61d8-306f-4b89-96f3-bf7d6f2191cc"/>
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
                        <id root="20c28c04-37e1-45a9-b24a-37080346be7f"/>
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
                  <id root="1bff51b2-bcf0-48d6-a3bf-bf79a2a6a132"/>
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
                        <id root="94a05a93-b887-4919-b773-e2d803ee4a44"/>
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
            <id root="3b605c7f-27e8-40ba-be93-8f653462d847"/>
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
                  <id root="aa981781-9087-4ac4-a0ef-a07d3f402a7f"/>
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
                        <id root="38f274a1-7c11-4d31-bf9e-2aa7992f3bf8"/>
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
                  <id root="31a68ea7-d050-4508-bba7-6e8a3f91a30c"/>
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
                        <id root="6d488a2b-d6a2-4f25-b2ea-135a01c3dcc1"/>
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
            <id root="3230af3d-4832-4139-8fe9-6d95cb3d2556"/>
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
                  <id root="eaa3242c-7cca-4c33-b446-3f1141e43baa"/>
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
                        <id root="0a5dca19-556c-4817-b938-6bbb8cc4f160"/>
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
                  <id root="3f722ac5-1440-4f17-b9dc-960659e18815"/>
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
                        <id root="f139fa9f-a038-4bdb-affb-7e2c3eedfce9"/>
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
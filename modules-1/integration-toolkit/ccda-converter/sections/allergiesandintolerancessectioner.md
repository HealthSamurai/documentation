# Allergies and Intolerances Section (entries required) (V3)

OID: 2.16.840.1.113883.10.20.22.2.6.1

LOINCs: #{"48765-2"}

Alias: allergies

Entries Required: N/A

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
            <id root="463fcef7-44a7-41cc-8e6f-a276b4e54c07"/>
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
            <id root="38ae7ea0-dd1e-4083-a45f-f65b86ba8d06"/>
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
                  <id root="8930f42c-6041-40af-8997-65bb6bbbced6"/>
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
                        <id root="f50822c7-f59b-4166-b162-af1ce206cc53"/>
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
                  <id root="82dc0d68-c2f7-408f-9dcc-3bc5f9d2c15e"/>
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
                        <id root="539aa88a-b1b4-4244-8672-b7ed96b3c8a1"/>
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
            <id root="e18a8377-1c83-44e3-9fc6-b8cfcf8fea18"/>
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
                  <id root="88301e52-fe02-4017-9e0e-3747e6a300bb"/>
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
                        <id root="72bc1ac4-f3e9-463f-85aa-6355a6b73005"/>
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
                  <id root="28f9febd-13ca-46ae-87a0-11edd9a941eb"/>
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
                        <id root="d2419aeb-e655-4cfd-a545-898818397bf4"/>
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
            <id root="2ca125c1-c0b5-4825-9b85-8a81220c8f03"/>
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
                  <id root="0043f284-ac78-45d2-8a42-7019bd06d7c2"/>
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
                        <id root="524a41cd-7221-42e3-841e-22419aeff5ba"/>
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
                  <id root="cc4f5b2b-7b89-49d5-9414-b49dfed95b48"/>
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
                        <id root="358f7515-6213-4dd6-92ba-f0b6a0ae2c62"/>
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
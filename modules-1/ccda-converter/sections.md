---
description: >-
  This page contains list of supported C-CDA sections.
---

# Supported sections

Blah-blah-blah.


## Allergies and Intolerances Section (entries required) (V3)
OID: 2.16.840.1.113883.10.20.22.2.6.1

LOINC: 48765-2

Internal ID: AllergiesandIntolerancesSectioner

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.6.1.html)

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



## 
OID: 2.16.840.1.113883.10.20.22.2.65

LOINC: 

Internal ID: NotesSection

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.65.html)


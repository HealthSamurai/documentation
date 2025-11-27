---
title: "FHIR Profiling: FHIRPath Constraints"
slug: "fhir-profiling-fhirpath-constraints"
published: "2025-04-11"
author: "Ivan Bagrov"
reading-time: "3 min "
tags: []
category: "FHIR"
teaser: "Constraint - a validation rule that is defined on a profile and described using the FHIRPath syntax."
image: "cover.png"
---

## FHIRPath Constraints

Constraint - a validation rule that is defined on a profile and described using the [FHIRPath syntax](https://hl7.org/fhirpath/N1/).

Constraints allow for describing more complex data requirements.

Example of a [profile](https://www.health-samurai.io/articles/fhir-profiling) with a constraint:

```javascript
resourceType: StructureDefinition
url: http://example.org/fhir/StructureDefinition/patient-profile
name: patient-profile
derivation: constraint
type: Patient
status: active
kind: resource
abstract: false
differential:
  element:
	- id: Patient
  	  path: Patient
  	  constraint: 
           - key: patient-data
             severity: error
             human: Patient's name or address are required
             expression: Patient.name.exists() or Patient.address.exists()
```

In this example, a profile with a constraint is described that checks whether the patient must have either a name or an address specified.

Constraints can be specified for the entire resource and for specific complex and primitive types.

Aidbox [FHIR server supports the entire FHIRPath syntax](https://www.health-samurai.io/aidbox).

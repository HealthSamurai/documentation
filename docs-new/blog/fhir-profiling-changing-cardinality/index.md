---
title: "FHIR Profiling: Changing Cardinality"
slug: "fhir-profiling-changing-cardinality"
published: "2025-04-11"
author: "Ivan Bagrov"
reading-time: "3 min "
tags: []
category: "FHIR"
teaser: "One key function of profiles is to modify the cardinality of an element. A profile can restrict an element's cardinality within its base structure."
image: "cover.png"
---

## FHIR Profiling

One key function of profiles is to modify the cardinality of an element. A profile can restrict an element's cardinality within its base structure.  
  
The foundational FHIR structure defines the following types of cardinality:

- Optional array (0..\*)
- Optional scalar (0..1)
- Required array (1..\*)
- Required scalar (1..1)

With the following constraints:

- You cannot make a required element that is optional in the base profile.
- You cannot convert an array element into a scalar if it exists as an array in the base profile.
- You cannot change a scalar element into an array if it is defined as a scalar in the base profile.

Example of how to create a profile that makes the `Patient.name` field mandatory:

```javascript
resourceType: StructureDefinition
url: http://example.org/fhir/StructureDefinition/patient-with-required-name
name: patient-profile
derivation: constraint
type: Patient
status: active
kind: resource
abstract: false
differential:
  element:
	- id: Patient.name
  	  path: Patient.name
  	  min: 1
  	  max: "*"
```

[Create a profile in your own FHIR server](https://www.health-samurai.io/aidbox)

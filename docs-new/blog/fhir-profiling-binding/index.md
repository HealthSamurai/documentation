---
title: "FHIR Profiling: Binding"
slug: "fhir-profiling-binding"
published: "2025-04-11"
author: "Ivan Bagrov"
reading-time: "2 min "
tags: []
category: "FHIR"
teaser: "Binding is used to define an allowed set of values for an element."
image: "cover.png"
---

## Binding

[FHIR Profiling](https://www.health-samurai.io/articles/fhir-profiling) Binding is used to define an allowed set of values for an element.

For example, to limit the value of a patient’s `maritalStatus` field, the following profile can be described:

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
	- id: Patient.maritalStatus
  	  path: Patient.maritalStatus
  	  binding:
           strength: required
           valueSet: http://hl7.org/fhir/ValueSet/marital-status
```

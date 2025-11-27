---
title: "FHIR Profiling: Slicing"
slug: "fhir-profiling-slicing"
published: "2025-04-11"
author: "Ivan Bagrov"
reading-time: "2 min "
tags: []
category: "FHIR"
teaser: "The slicing mechanism enables the specification of requirements for array elements."
image: "cover.png"
---

## Slicing

The slicing mechanism enables the specification of requirements for array elements. For instance, slices can be used to define requirements such as:

- A specific extension must be included on the resource.
- If a patient has a home address, it must include state information.
- A transactional bundle must contain a [resource](https://www.health-samurai.io/articles/extending-fhir-resources) that conforms to a specific profile.

An example of slicing that requires the patient’s home address to include state information:

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
	- id: Patient.address
  	  path: Patient.address
  	  slicing:
    	    discriminator:
      	      - type: pattern
               path: use
    	    rules: open
	- id: Patient.address:home
  	  path: Patient.address
  	  sliceName: home
  	  min: 1
  	  max: "1"
  	  type:
    	    - code: Address
  	  patternAddress:
    	    use: home
	- id: Patient.address:home.state
  	  path: Patient.address.state
  	  min: 1
  	  type:
    	    - code: string
```

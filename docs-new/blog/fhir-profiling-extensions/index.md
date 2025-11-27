---
title: "FHIR Profiling: Extensions"
slug: "fhir-profiling-extensions"
published: "2025-04-11"
author: "Ivan Bagrov"
reading-time: "3 min "
tags: []
category: "FHIR"
teaser: "In FHIR, you can define a profile not only for a resource but also for an extension."
image: "cover.png"
---

## Extensions

In FHIR, you can define a [profile](https://www.health-samurai.io/articles/fhir-profiling) not only for a [resource](https://www.health-samurai.io/articles/extending-fhir-resources) but also for an extension. A profile on an extension allows for restricting the acceptable types of extension values, specifying a binding for a coded value, and describing the validation of the value using FHIRPath.

Example of how to describe your own extension that describes a patient’s place of birth:

```javascript
POST /fhir/StructureDefinition

resourceType: StructureDefinition
id: my-extension
type: Extension
baseDefinition: http://hl7.org/fhir/StructureDefinition/Extension
url: http://example.org/fhir/StructureDefinition/my-extension
name: MyExtension
kind: complex-type
abstract: false
derivation: constraint
differential:
  element:
   - id: Extension.url
     path: Extension.url
     fixedUri: http://example.org/fhir/StructureDefinition/patient-birthPlace
   - id: Extension.value[x]
     path: Extension.value[x]
     min: 1
     type:
      - code: string
```

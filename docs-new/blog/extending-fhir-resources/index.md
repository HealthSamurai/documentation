---
title: "Extending FHIR Resources"
slug: "extending-fhir-resources"
published: "2025-04-11"
author: "Ivan Bagrov"
reading-time: "4 min"
tags: []
category: "FHIR"
teaser: "The FHIR specification includes many resources that describe various medical data. Sometimes, however, the basic resources are insufficient to describe the data, and in such cases, FHIR provides the following mechanisms for extending the basic resources."
image: "cover.png"
---

## FHIR Resources

The FHIR specification includes many resources that describe various medical data. Sometimes, however, the basic resources are insufficient to describe the data, and in such cases, FHIR provides the following mechanisms for extending the basic resources.

A FHIR Extension is a mechanism that allows adding additional elements to **FHIR resources** beyond the base specification. This is necessary to meet the specific requirements of various implementations while maintaining the simplicity and universality of the base FHIR model.

FHIR provides its own set of extensions. For example, to indicate a patient’s place of birth, the following extension is proposed:

```javascript
resourceType: Patient
extension:
  - url: http://hl7.org/fhir/StructureDefinition/patient-birthPlace
    valueAddress: 
      city: Springfield
      district: Sangamon 
      state: Illinois
```

FHIR provides comprehensive extensions that allow for describing more complex objects. For example:

```javascript
resourceType: Patient
extension:
  - url: http://hl7.org/fhir/StructureDefinition/geolocation
    extension:
      - url: latitude
        valueDecimal: 39.7817
      - url: longitude
        valueDecimal: -89.6501
```

Extensions can be specified at the root of a resource, as well as on complex (HumanName, Address, etc.) and primitive (string, decimal, code, etc.) FHIR types.

An example of how to specify an extension on a complex type:

```javascript
resourceType: Patient
name:
  - use: official
    family: Doe
    given:
      - John
    extension:
      - url: http://hl7.org/fhir/StructureDefinition/humanname-assembly-order
    	 valueCode: F
```

An example of how to specify an extension on a primitive:

```javascript
resourceType: Patient
birthDate: '1985-06-15'
_birthDate:
  extension:
    - url: 'http://hl7.org/fhir/StructureDefinition/patient-birthTime'
      valueDateTime: '1985-06-15T08:30:00-04:00'
```

If a field containing a primitive type value is an array and specifying an extension on a specific element of this array is necessary:

```javascript
resourceType: Patient
name:
  - use: official
    family: Doe
    given:
      - John
      - Michael
    _given:
      extension:
        - null
        - url: http://hl7.org/fhir/StructureDefinition/humanname-assembly-order
    	    valueCode: F
```

Test it on your [local FHIR server](https://www.health-samurai.io/aidbox)

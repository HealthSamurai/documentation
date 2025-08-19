---
description: FHIR data types for coded values - code, Coding, and CodeableConcept with examples and use cases
---

# Coded Values

FHIR provides three specialized data types for representing coded values, each designed for specific use cases and levels of complexity.

Healthcare systems need different approaches for coded data depending on context. Sometimes you need a simple fixed value from a predefined list. Other times you need to specify exactly which terminology system a code comes from. And occasionally you need to represent the same concept using multiple coding systems simultaneously for maximum interoperability.

FHIR's coded data types address these scenarios systematically, ensuring both precision and flexibility in how healthcare data is exchanged.

## Data Types

### Code

A simple string value strictly bound to a predefined ValueSet. Used for fixed vocabularies where the meaning is well-established and the possible values are constrained by the FHIR specification itself.

```json
{
  "resourceType": "Patient", 
  "gender": "female"
}
```

In this example, the `gender` element is bound to `http://hl7.org/fhir/ValueSet/administrative-gender`. The concept *Female* is represented by the simple code `female`.

See [definition](https://www.hl7.org/fhir/R4/datatypes.html#code)

### Coding

A complex structure that pairs a code with its source system, providing precise identification of concepts from specific terminologies. Essential when you need to specify exactly which terminology system defines the code's meaning.

```json
{
  "resourceType": "Observation",
  "code": {
    "system": "http://loinc.org",
    "code": "1751-7", 
    "display": "Albumin [Mass/volume] in Serum or Plasma"
  }
}
```

In this example, the `Coding` specifies not only the code `1751-7` (which has no meaning by itself), but also identifies LOINC as the source vocabulary that defines this code. The `display` element provides human-readable text for validation and user interfaces.

See [definition](https://www.hl7.org/fhir/R4/datatypes.html#coding)

### CodeableConcept

A CodeableConcept represents a value that is usually supplied by providing a reference to one or more terminologies or ontologies but may also be defined by the provision of text. This is a common pattern in healthcare data.

This is the most flexible option, containing multiple `Coding` elements plus optional human-readable text. It allows the same concept to be represented using different terminology systems simultaneously, supporting both local and standard codes.

```json
{
  "resourceType": "Observation",
  "code": {
    "coding": [
      {
        "system": "http://loinc.org",
        "code": "8310-5",
        "display": "Body temperature"
      },
      {
        "system": "http://snomed.info/sct", 
        "code": "386725007",
        "display": "Body temperature (observable entity)"
      }
    ],
    "text": "Body temperature"
  }
}
```

See [definition](https://www.hl7.org/fhir/R4/datatypes.html#codeableconcept)

> **Note:** The progression from `code` to `Coding` to `CodeableConcept` reflects increasing complexity and flexibility, allowing implementers to choose the appropriate level of semantic precision for their use case.

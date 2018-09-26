---
description: 'See https://www.hl7.org/fhir/terminology-service.html#4.6.5'
---

# Value Set Code Validation

## Overview

Value set code validation provide ability to validate that a coded value is in the set of codes allowed by a value set.

## Api

Official documentation [FHIR Terminology Value Set based Validation](https://www.hl7.org/fhir/valueset-operations.html#validate-code)

```text
GET/POST URL: [base]/ValueSet/$validate-code
```

```text
GET/POST URL: [base]/ValueSet/[id]/$validate-code
```

Example for validate that `female` code allowed by a default AdministrativeGender ValueSet. 

```text
GET [base]/ValueSet/administrative-gender/$validate-code?code=femalex
```

## Parameters

| Parameter | Type | Status | Example |
| :--- | :--- | :--- | :--- |
| url | [uri](https://www.hl7.org/fhir/datatypes.html#uri) |  |  |
| context | [uri](https://www.hl7.org/fhir/datatypes.html#uri) |  |  |
| valueSet | [ValueSet](https://www.hl7.org/fhir/valueset.html) |  |  |
| code | [code](https://www.hl7.org/fhir/datatypes.html#code) |  |  |
| system | [uri](https://www.hl7.org/fhir/datatypes.html#uri) |  |  |
| version | [string](https://www.hl7.org/fhir/datatypes.html#string) |  |  |
| display | [string](https://www.hl7.org/fhir/datatypes.html#string) |  |  |
| coding | [Coding](https://www.hl7.org/fhir/datatypes.html#Coding) |  |  |
| codeableConcept | [CodeableConcept](https://www.hl7.org/fhir/datatypes.html#CodeableConcept) |  |  |
| date | [dateTime](https://www.hl7.org/fhir/datatypes.html#dateTime) |  |  |
| abstract | [boolean](https://www.hl7.org/fhir/datatypes.html#boolean) |  |  |
| displayLanguage | [code](https://www.hl7.org/fhir/datatypes.html#code) |  |  |




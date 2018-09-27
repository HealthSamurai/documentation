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
GET [base]/ValueSet/administrative-gender/$validate-code?code=female
```

## Parameters

| Parameter | Type | Status | Example |
| :--- | :--- | :--- | :--- |
| url | [uri](https://www.hl7.org/fhir/datatypes.html#uri) | `supported` | [url](value-set-validation.md#url-code-system-version-display) |
| context | [uri](https://www.hl7.org/fhir/datatypes.html#uri) | `not supported` |  |
| valueSet | [ValueSet](https://www.hl7.org/fhir/valueset.html) | `supported` | valueSet |
| code | [code](https://www.hl7.org/fhir/datatypes.html#code) | `supported` | [code](value-set-validation.md#url-code-system-version-display) |
| system | [uri](https://www.hl7.org/fhir/datatypes.html#uri) | `supported` | [system](value-set-validation.md#url-code-system-version-display) |
| version | [string](https://www.hl7.org/fhir/datatypes.html#string) | `supported` | [version](value-set-validation.md#url-code-system-version-display) |
| display | [string](https://www.hl7.org/fhir/datatypes.html#string) | `supported` | [display](value-set-validation.md#url-code-system-version-display) |
| coding | [Coding](https://www.hl7.org/fhir/datatypes.html#Coding) | `supported` | coding |
| codeableConcept | [CodeableConcept](https://www.hl7.org/fhir/datatypes.html#CodeableConcept) | `supported` | codeableConcept |
| date | [dateTime](https://www.hl7.org/fhir/datatypes.html#dateTime) | `not supported` |  |
| abstract | [boolean](https://www.hl7.org/fhir/datatypes.html#boolean) | `not supported` |  |
| displayLanguage | [code](https://www.hl7.org/fhir/datatypes.html#code) | `not supported` |  |

### url code system version display

{% tabs %}
{% tab title="Request" %}
```javascript
GET [base]/ValueSet/$validate-code?url=http://hl7.org/fhir/ValueSet/administrative-gender&code=male&display=Male
```

Or

```javascript
POST [base]/ValueSet/$validate-code
{ 
  "resourceType" : "Parameters",
  "parameter" : [
     {
      "name" : "url",
      "valueUri" : "http://hl7.org/fhir/ValueSet/administrative-gender"
     },
     {
      "name" : "code",
      "valueCode" : "male"
     },
     {
      "name" : "system",
      "valueUri" : "http://hl7.org/fhir/administrative-gender"
     },
     {
      "name" : "display",
      "valueString" : "Male"
     }
  ]
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
    "resourceType": "Parameters",
    "parameter": [
        {
            "name": "result",
            "valueBoolean": true
        }
    ]
}
```
{% endtab %}
{% endtabs %}




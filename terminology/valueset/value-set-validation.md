# ValueSet Code Validation

## Overview

Value set code validation provides the ability to validate that a coded value is in the set of codes allowed by a value set. For more details, see the official documentation [FHIR Terminology Value Set based Validation](https://www.hl7.org/fhir/valueset-operations.html#validate-code).

## API

```
GET/POST URL: [base]/ValueSet/$validate-code
```

```
GET/POST URL: [base]/ValueSet/[id]/$validate-code
```

Example: for validation that `female` code allowed by a default AdministrativeGender ValueSet. 

```
GET [base]/ValueSet/administrative-gender/$validate-code?code=female
```

## Parameters

| Parameter       | Type                                                                       | Status          | Example                                                            |
| --------------- | -------------------------------------------------------------------------- | --------------- | ------------------------------------------------------------------ |
| url             | [uri](https://www.hl7.org/fhir/datatypes.html#uri)                         | `supported`     | [url](value-set-validation.md#url-code-system-version-display)     |
| context         | [uri](https://www.hl7.org/fhir/datatypes.html#uri)                         | `not supported` |                                                                    |
| valueSet        | [ValueSet](https://www.hl7.org/fhir/valueset.html)                         | `supported`     | [valueSet](value-set-validation.md#valueset)                       |
| code            | [code](https://www.hl7.org/fhir/datatypes.html#code)                       | `supported`     | [code](value-set-validation.md#url-code-system-version-display)    |
| system          | [uri](https://www.hl7.org/fhir/datatypes.html#uri)                         | `supported`     | [system](value-set-validation.md#url-code-system-version-display)  |
| version         | [string](https://www.hl7.org/fhir/datatypes.html#string)                   | `supported`     | [version](value-set-validation.md#url-code-system-version-display) |
| display         | [string](https://www.hl7.org/fhir/datatypes.html#string)                   | `supported`     | [display](value-set-validation.md#url-code-system-version-display) |
| coding          | [Coding](https://www.hl7.org/fhir/datatypes.html#Coding)                   | `supported`     | [coding](value-set-validation.md#coding)                           |
| codeableConcept | [CodeableConcept](https://www.hl7.org/fhir/datatypes.html#CodeableConcept) | `supported`     | [codeableConcept](value-set-validation.md#codeableconcept)         |
| date            | [dateTime](https://www.hl7.org/fhir/datatypes.html#dateTime)               | `not supported` |                                                                    |
| abstract        | [boolean](https://www.hl7.org/fhir/datatypes.html#boolean)                 | `not supported` |                                                                    |
| displayLanguage | [code](https://www.hl7.org/fhir/datatypes.html#code)                       | `not supported` |                                                                    |

### url code system version display

One of the concept property

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

### valueSet

The value set is provided directly as a part of the request.

{% tabs %}
{% tab title="Request" %}
```javascript
POST [base]/ValueSet/$validate-code
{ 
  "resourceType" : "Parameters",
  "parameter" : [
     {
      "name" : "code",
      "valueCode" : "male"
     },
     {
      "name" : "system",
      "valueUri" : "http://hl7.org/fhir/administrative-gender"
     },
     {
      "name" : "valueSet",
      "resource" : {
        "resourceType": "ValueSet",
        "url": "http://custom/administrative-gender",
        "compose": {
           "include": [
              {
                "valueSet": ["http://hl7.org/fhir/ValueSet/administrative-gender"]
              }
          ]
        } 
       }
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

### coding

A coding to validate

{% hint style="info" %}
ValueSet needs `url` to work `coding` properly
{% endhint %}

{% tabs %}
{% tab title="Request" %}
```javascript
POST [base]/ValueSet/$validate-code
{
  "resourceType" : "Parameters",
  "parameter" : [
     {
      "name" : "coding",
      "valueCoding" : 
        {
          "system": "http://hl7.org/fhir/administrative-gender",
          "code": "male"
        }
     },
     {
      "name" : "url",
      "valueUri" : "http://hl7.org/fhir/ValueSet/administrative-gender"
     }
  ]
}
```
{% endtab %}

{% tab title="Rsponse" %}
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

### codeableConcept

A full codeableConcept to validate.

{% tabs %}
{% tab title="Request" %}
```javascript
POST [base]/ValueSet/$validate-code
{
  "resourceType" : "Parameters",
  "parameter" : [
     {
      "name" : "codeableConcept",
      "valueCodeableConcept" : 
        {
          "coding": 
          [
          {
            "system": "http://hl7.org/fhir/administrative-gender",
            "code": "male_wrong"
          },
          {
            "system": "http://hl7.org/fhir/administrative-gender",
            "code": "male"
          }
          ]
        }
     },
     {
      "name" : "url",
      "valueUri" : "http://hl7.org/fhir/ValueSet/administrative-gender"
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

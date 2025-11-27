---
title: "FHIR Built-in Search Parameters and Beyond Them"
slug: "fhir-built-in-search-parameters"
published: "2025-04-11"
author: "Alexandr Penskoi"
reading-time: "5 min "
tags: []
category: "FHIR"
teaser: "FHIR specification defines an HTTP interface to complex medical data, including \"simple\" patient info like the history of their statements, endorsements, observations and so on."
image: "cover.png"
---

## FHIR Built-in Search Parameters

FHIR specification defines an HTTP interface to complex medical data, including "simple" patient info like the history of their statements, endorsements, observations and so on. That data is represented as a set of connected hierarchical resources (JSON objects), which can contain: optional elements, objects, arrays, polymorphic elements (having more than one datatype, like Algebraic Data Types), and extensions (FHIR specific things, which I'll explain later).

One of the problems: How to request/search such data? For example, we need to find all patients with a specific occupation. How to fit such requests in the URL? Writing complex queries like [FHIRPath](https://www.hl7.org/fhir/fhirpath.html) or [JSONPath](https://datatracker.ietf.org/doc/html/rfc9535) directly in URL is not readable.

### Built-in Search Params

FHIR's answer to this is a [Search Parameter](https://build.fhir.org/searchparameter.html). For simplicity, we can think of them as aliases for FHIRPath queries. Let's see some examples for [Patient resource](https://www.hl7.org/fhir/patient.html) these are [built-in search parameters](https://www.hl7.org/fhir/patient-search.html):

- `active`

To find active patient records. In a URL it will look like `GET /fhir/Patient?active=true`. It is a little bit simpler than the actual FHIRPath

```javascript
FHIRPath: `Patient.active`.
```

- `email`

This example is a little bit more complex, because email can be placed in different locations for different resources.

FHIRPath:

```javascript

    Patient.telecom.where(system='email') 
    | Person.telecom.where(system='email') 
    | Practitioner.telecom.where(system='email') 
    | PractitionerRole.contact.telecom.where(system='email') 
    | RelatedPerson.telecom.where(system='email')

```

What we see here:

• It can be applicable to many resource types.

• Information about email is placed in different locations for different resources (usually it's the `telecom` object, but it may be inside `contact`)

• Typically, all contact information is placed in one array, so we need to filter only "email" information which we do via the `where(...)` expression.

### Beyond Built-in Search Parameters

It wouldn't be very useful if we could only cover standard cases with the built-in search parameters.

Sometimes, we need to store "custom" information, e.g., information about patient occupation, which is not a part of the `Patient` resource, but can be meaningful for some applications. In this case we can attach this information as an `extension` for the resource in the following way from [Extension Element](https://www.hl7.org/fhir/extensibility.html):

```javascript
```json
{
  "resourceType": "Patient",
  "extension": [
    {
      "url": "http://hl7.org/fhir/StructureDefinition/patient-citizenship",
      "extension": [
        {
          "url": "code",
          "valueCodeableConcept": {
            "coding": [
              {
                "system": "urn:iso:std:iso:3166",
                "code": "DE"
              }
            ]
          }
        },
        {
          "url": "period",
          "valuePeriod": {
            "start": "2009-03-14"
          }
        }
      ]
    }
  ]
}
```
```

To search for such objects, we can define custom search parameters like:

```javascript
```json
{
  "resourceType": "SearchParameter",
  "id": "patient-citizenship",
  "url": "http://example.org/fhir/SearchParameter/patient-citizenship",
  "version": "1.0.0",
  "name": "citizenship",
  "status": "active",
  "description": "Search patients by citizenship",
  "code": "citizenship",
  "base": [
    "Patient"
  ],
  "type": "string",
  "expression": "Patient.extension.where(url='http://hl7.org/fhir/StructureDefinition/patient-citizenship').value.as(string)"
}
```
```

And we can use it in a URL like: `GET /fhir/Patient?citizenship=GB`

In conclusion, FHIR Search Parameters together with FHIRPath are powerful and extensible tools to search for complex medical data. You can find information about how to work with them in Aidbox and other examples in the [documentation](https://docs.aidbox.app/api-1/fhir-api/search-1/custom-search-parameter/custom-search-parameters).

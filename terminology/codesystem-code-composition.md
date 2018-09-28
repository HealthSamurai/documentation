# CodeSystem Code Composition

## Overview

Given a set of properties, return one or more possible matching codes. For more details see official FHIR terminology documentation [CodeSystem Code Composition](https://www.hl7.org/fhir/codesystem-operations.html#compose)

## Api

```text
GET/POST URL: [base]/ValueSet/$compose
```

```text
GET/POST URL: [base]/ValueSet/[id]/$compose
```

## Parameters

| Parameter | Type | Status |
| :--- | :--- | :--- |
| system | [uri](https://www.hl7.org/fhir/datatypes.html#uri) | `supported` |
| version | [string](https://www.hl7.org/fhir/datatypes.html#string) | `not supported` |
| property |  | `supported` |
| property.code | [code](https://www.hl7.org/fhir/datatypes.html#code) | `supported` |
| property.value | code \| Coding \| string \| integer \| boolean \| dateTime | `supported` |
| property.subproperty |  | `not supported` |
| property.subproperty.code | [code](https://www.hl7.org/fhir/datatypes.html#code) | `not supported` |
| property.subproperty.value | code \| Coding \| string \| integer \| boolean \| dateTime | `not supported` |
| exact | [boolean](https://www.hl7.org/fhir/datatypes.html#boolean) | `supported` |
| compositional | [boolean](https://www.hl7.org/fhir/datatypes.html#boolean) | `not supported` |

## Example




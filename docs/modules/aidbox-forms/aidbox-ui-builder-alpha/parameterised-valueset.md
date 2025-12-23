---
description: Learn how to create dynamic, parameterized ValueSets that filter answer options based on other form fields using FHIRPath expressions.
---

# Parameterised ValueSet

## Overview

Parameterised ValueSet is a FHIR feature that allows you to dynamically filter ValueSet answer options based on parameters derived from other fields in the questionnaire. This is particularly useful for creating dependent dropdowns, such as filtering states based on a selected country.

The feature is specified in the [FHIR Tools IG: Parameterized ValueSets](https://build.fhir.org/ig/FHIR/fhir-tools-ig/parameterized-valuesets.html).

## Use Case Example

A common use case is a Country/State relationship:
- User selects a country from a dropdown
- The state/province dropdown is automatically filtered to show only states belonging to the selected country
- This is achieved by passing the country code as a parameter to the ValueSet expansion

## How to Configure in Aidbox Forms Builder

### Prerequisites

1. Configure an external terminology server (e.g., Ontoserver) in your Aidbox instance
2. Have a parameterized ValueSet available on the terminology server
3. Ensure your questionnaire has the source field (e.g., country) that will provide the parameter value

### Step-by-Step Configuration

#### 1. Add the Source Field

First, add the field that will provide the parameter value. For example, a country selection:

```json
{
  "type": "choice",
  "text": "Country",
  "linkId": "countryCode",
  "answerValueSet": "http://example.com/countries/vs"
}
```

#### 2. Add the Dependent Choice Field

Create a new Choice item in the form builder (e.g., "State"):

1. Click "Add Item" and select "Choice"
2. Enter the item text (e.g., "State")
3. In the settings panel, configure the terminology server:
   - Select or enter your terminology server URL (e.g., `https://r4.ontoserver.csiro.au/fhir`)
   - Select or enter the ValueSet URL for the parameterized ValueSet

#### 3. Enable Expansion Parameters

1. Check the "Expansion Parameters" checkbox in the settings panel
2. Click "Add Parameter" to add a new expansion parameter

#### 4. Configure the Parameter

For each parameter:

1. **Name**: Enter the parameter name as defined in the ValueSet (e.g., `p-country`)
2. **Expression**: Enter a FHIRPath expression to extract the value from the questionnaire response

Example FHIRPath expression to get the country code:
```
%resource.item.where(linkId = 'countryCode').answer.value.code
```

This expression:
- Uses `%resource` to reference the current QuestionnaireResponse
- Finds the item with `linkId = 'countryCode'`
- Extracts the `code` from the answer's value

### How It Works at Runtime

When requesting answer options from the terminology server:

1. The FHIRPath expression is evaluated against the current QuestionnaireResponse
2. If the expression evaluates to `AU` (for Australia), the parameter becomes `p-country=AU`
3. This parameter is added to the ValueSet `$expand` request
4. The final request looks like: `https://r4.ontoserver.csiro.au/fhir/ValueSet/$expand?url=http://example.com/limited-states/vs&p-country=AU`
5. The terminology server uses this parameter to filter the ValueSet expansion and return only relevant states

## Full Example Questionnaire
<details>
<summary>Click to expand full Questionnaire JSON</summary>

```json
{
  "item": [
    {
      "text": "Country",
      "type": "choice",
      "linkId": "countryCode",
      "extension": [
        {
          "url": "http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-preferredTerminologyServer",
          "valueUrl": "https://r4.ontoserver.csiro.au/fhir"
        }
      ],
      "answerValueSet": "http://example.com/countries/vs"
    },
    {
      "text": "State GET request url: https://r4.ontoserver.csiro.au/fhir/ValueSet/$expand?url=https://example.com/limited-states&includeDefinition=true",
      "type": "display",
      "_text": {
        "extension": [
          {
            "url": "http://hl7.org/fhir/StructureDefinition/cqf-expression",
            "valueExpression": {
              "language": "text/fhirpath",
              "expression": "'State GET request url: https://r4.ontoserver.csiro.au/fhir/ValueSet/$expand?url=https://example.com/limited-states&p-country=' + %resource.item.where(linkId = 'countryCode').answer.value.code + '&includeDefinition=true'"
            }
          }
        ]
      },
      "linkId": "requestDisplay"
    },
    {
      "text": "State",
      "type": "choice",
      "linkId": "stateCodeDropdown",
      "extension": [
        {
          "url": "http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-preferredTerminologyServer",
          "valueUrl": "https://r4.ontoserver.csiro.au/fhir"
        }
      ],
      "answerValueSet": "http://example.com/limited-states/vs",
      "_answerValueSet": {
        "extension": [
          {
            "url": "http://hl7.org/fhir/tools/StructureDefinition/binding-parameter",
            "extension": [
              {
                "url": "name",
                "valueString": "p-country"
              },
              {
                "url": "expression",
                "valueExpression": {
                  "language": "text/fhirpath",
                  "expression": "%resource.item.where(linkId = 'countryCode').answer.value.code"
                }
              }
            ]
          },
          {
            "url": "http://hl7.org/fhir/tools/StructureDefinition/binding-parameter",
            "extension": [
              {
                "url": "name",
                "valueString": "includeDefinition"
              },
              {
                "url": "expression",
                "valueExpression": {
                  "language": "text/fhirpath",
                  "expression": "true"
                }
              }
            ]
          }
        ]
      }
    }
  ],
  "resourceType": "Questionnaire",
  "title": "Parameterised ValueSet Example",
  "status": "draft",
  "url": "https://forms.aidbox.io/parameterised-valueset",
  "version": "0.1.0"
}
```

</details>

---
description: FHIR CodeSystem resource definition, content types, and examples of external references, complete definitions, supplements, and hierarchical systems
---

# CodeSystem

A CodeSystem resource defines a set of codes and their meanings within a specific terminology domain. Think of it as a dictionary that establishes what codes exist and what they mean, providing the foundation for consistent data interpretation across healthcare systems.

CodeSystems serve multiple purposes: they declare the existence of terminology systems, describe their key properties, and optionally define the actual concepts and codes within the system. This flexibility allows FHIR to work with both massive external terminologies like SNOMED CT and simple custom code sets defined for specific implementations.

See also: [FHIR CodeSystem Specification](https://www.hl7.org/fhir/codesystem.html)

### Examples

**External CodeSystem Reference** - Large established terminologies are typically referenced without including their full content, as they're maintained externally. These CodeSystem resources usually have `content: not-present`.

```json
{
  "resourceType": "CodeSystem",
  "url": "http://loinc.org", 
  "version": "3.1.0",
  "name": "LOINC",
  "title": "Logical Observation Identifiers, Names and Codes (LOINC)",
  "status": "active",
  "date": "2022-11-22",
  "publisher": "LOINC and Health Data Standards, Regenstrief Institute, Inc.",
  "content": "not-present"
}
```

**Complete CodeSystem Definition** - Smaller, well-defined code sets can include their full content within the FHIR resource.

```json
{
  "resourceType": "CodeSystem",
  "url": "http://hl7.org/fhir/administrative-gender",
  "version": "6.0.0-ballot2", 
  "content": "complete",
  "concept": [
    {
      "code": "male",
      "display": "Male"
    },
    {
      "code": "female", 
      "display": "Female"
    },
    {
      "code": "other",
      "display": "Other"
    },
    {
      "code": "unknown",
      "display": "Unknown"
    }
  ]
}
```

**Supplement CodeSystem** - Adds translations or additional properties to existing code systems without modifying the original.

```json
{
  "resourceType": "CodeSystem",
  "url": "http://example.org/fhir/administrative-gender-es",
  "content": "supplement",
  "supplements": "http://hl7.org/fhir/administrative-gender",
  "concept": [
    {
      "code": "male",
      "designation": [
        {
          "language": "es",
          "value": "Masculino"
        }
      ]
    },
    {
      "code": "female",
      "designation": [
        {
          "language": "es", 
          "value": "Femenino"
        }
      ]
    }
  ]
}
```

**Complex CodeSystem with Properties** - Demonstrates advanced features like filters, properties, and hierarchical concepts.

```json
{
  "resourceType": "CodeSystem",
  "url": "http://www.nlm.nih.gov/research/umls/rxnorm",
  "version": "04072025",
  "content": "complete",
  "filter": [
    {
      "code": "concept",
      "operator": ["is-a", "generalizes", "descendent-of"],
      "value": "comma-separated list of concept codes for direct equality testing"
    },
    {
      "code": "NDC", 
      "operator": ["=", "in", "exists"],
      "value": "NDC code"
    }
    ...
  ],
  "property": [
    {"code": "tty", "type": "string"},
    {"code": "NDC", "type": "string"},
    {"code": "RXN_STRENGTH", "type": "string"}
    ...
  ],
  "concept": [
    ...
    {
      "code": "5640",
      "display": "ibuprofen",
      "property": [
        {
          "code": "tty",
          "valueString": "IN"
        },
        {
          "code": "has_tradename", 
          "valueCode": "1100067"
        }
      ]
    }
    ...
  ]
}
```

**Hierarchical CodeSystem** - Shows how CodeSystems can represent complex taxonomies with parent-child relationships.

```json
{
  "resourceType": "CodeSystem",
  "url": "http://hl7.org/fhir/sid/icd-10-cm",
  "version": "2025",
  "content": "complete", 
  "concept": [
    {
      "code": "Chapter-1",
      "display": "Certain infectious and parasitic diseases (A00-B99)",
      "concept": [
        {
          "code": "Section-A00-A09",
          "display": "Intestinal infectious diseases (A00-A09)",
          "concept": [
            {
              "code": "A00",
              "display": "Cholera",
              "concept": [
                {
                  "code": "A00.0",
                  "display": "Cholera due to Vibrio cholerae 01, biovar cholerae"
                }
              ]
            }
          ]
        }
      ]
    }
    ...
  ]
}
```

> **Note:** The `content` field indicates how much of the CodeSystem is actually defined: `not-present` for external references, `complete` for full definitions, `supplement` for extensions to existing systems, and `example` or `fragment` for partial representations. This flexibility allows FHIR to work efficiently with both massive external terminologies and focused local code sets.

## Operations

### $lookup

Retrieves detailed information about a specific code within a CodeSystem, including its display name, definition, and additional properties.

{% tabs %}
{% tab title="Request" %}
```
GET /fhir/CodeSystem/$lookup?system=http://loinc.org&code=1751-7
```
{% endtab %}

{% tab title="Response" %}
```json
{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "name",
      "valueString": "LOINC"
    },
    {
      "name": "display",
      "valueString": "Albumin [Mass/volume] in Serum or Plasma"
    },
    {
      "name": "definition",
      "valueString": "Albumin measurement in serum or plasma by mass per volume"
    },
    {
      "name": "property",
      "part": [
        {
          "name": "code",
          "valueCode": "COMPONENT"
        },
        {
          "name": "value",
          "valueString": "Albumin"
        }
      ]
    }
  ]
}
```
{% endtab %}
{% endtabs %}

### $validate-code

Validates whether a given code exists in the CodeSystem and optionally checks if the provided display name is correct.

{% tabs %}
{% tab title="Valid Code Request" %}
```
GET /fhir/CodeSystem/$validate-code?url=http://hl7.org/fhir/administrative-gender&code=male&display=Male
```
{% endtab %}

{% tab title="Valid Code Response" %}
```json
{
  "resourceType": "Parameters", 
  "parameter": [
    {
      "name": "result",
      "valueBoolean": true
    },
    {
      "name": "display",
      "valueString": "Male"
    }
  ]
}
```
{% endtab %}

{% tab title="Invalid Code Request" %}
```
GET /fhir/CodeSystem/$validate-code?url=http://hl7.org/fhir/administrative-gender&code=invalid
```
{% endtab %}

{% tab title="Invalid Code Response" %}
```json
{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "result", 
      "valueBoolean": false
    },
    {
      "name": "message",
      "valueString": "Code 'invalid' is not found in code system 'http://hl7.org/fhir/administrative-gender'"
    }
  ]
}
```
{% endtab %}
{% endtabs %}

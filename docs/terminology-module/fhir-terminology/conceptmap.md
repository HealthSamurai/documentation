---
description: FHIR ConceptMap resource for defining relationships and translations between concepts from different CodeSystems
---

# ConceptMap

Healthcare organizations frequently need to map between different terminology systems—translating internal codes to standard vocabularies, converting between different versions of the same terminology, or bridging legacy systems with modern FHIR implementations. Without proper management of these mappings, organizations face integration failures, data quality issues, and costly manual translation processes. **ConceptMap** resources solve this problem by providing standardized, structured relationships between concepts from different CodeSystems.

[ConceptMap](https://www.hl7.org/fhir/conceptmap.html) is a [FHIR canonical resource](https://build.fhir.org/canonicalresource.html) that defines how concepts in one CodeSystem relate to concepts in another. When you need to translate your internal "HTN" code to SNOMED CT's "38341003 | Hypertensive disorder", or convert FHIR administrative gender codes to HL7 V2 format, ConceptMaps provide the structured translation rules that enable automated, consistent terminology conversion.

See also: [FHIR ConceptMap Specification](https://www.hl7.org/fhir/conceptmap.html)

## Examples

### Simple Administrative Mapping

**Administrative Gender to HL7 V2** - Maps FHIR administrative gender codes to their HL7 V2 equivalents for system integration.

```json
{
  "resourceType": "ConceptMap",
  "url": "http://hl7.org/fhir/ConceptMap/cm-administrative-gender-v2",
  "version": "4.0.1", 
  "name": "AdminGenderV2Map",
  "title": "Administrative Gender to V2 Mapping",
  "status": "active",
  "group": [
    {
      "source": "http://hl7.org/fhir/administrative-gender",
      "target": "http://terminology.hl7.org/CodeSystem/v2-0001",
      "element": [
        {
          "code": "male",
          "display": "Male",
          "target": [
            {
              "code": "M",
              "display": "Male",
              "relationship": "equivalent"
            }
          ]
        },
        {
          "code": "female",
          "display": "Female", 
          "target": [
            {
              "code": "F",
              "display": "Female",
              "relationship": "equivalent"
            }
          ]
        }
      ]
    }
  ]
}
```

### Status Translation ConceptMap

**Event Status to Resource Status** - Translates FHIR event status codes to general resource status codes for workflow management.

```json
{
  "resourceType": "ConceptMap",
  "url": "http://hl7.org/fhir/ConceptMap/sc-event-status",
  "version": "5.0.0",
  "name": "EventStatusMap", 
  "status": "active",
  "group": [
    {
      "source": "http://hl7.org/fhir/event-status",
      "target": "http://hl7.org/fhir/resource-status",
      "element": [
        {
          "code": "preparation",
          "target": [
            {
              "code": "planned",
              "relationship": "equivalent"
            }
          ]
        },
        {
          "code": "in-progress", 
          "target": [
            {
              "code": "active",
              "relationship": "equivalent"
            }
          ]
        },
        {
          "code": "completed",
          "target": [
            {
              "code": "complete",
              "relationship": "equivalent"
            }
          ]
        }
      ]
    }
  ]
}
```

### Complex Multi-Target ConceptMap

**Local Codes to Multiple Target Systems** - Maps internal organizational codes to both SNOMED CT and LOINC for comprehensive interoperability.

```json
{
  "resourceType": "ConceptMap",
  "url": "http://example.org/fhir/ConceptMap/local-to-snomed-loinc",
  "name": "LocalToSnomedLoinc",
  "status": "active",
  "sourceScopeUri": "http://example.org/fhir/CodeSystem/local-cs",
  "group": [
    {
      "source": "http://example.org/fhir/CodeSystem/local-cs",
      "target": "http://snomed.info/sct",
      "element": [
        {
          "code": "A1",
          "display": "Local Code A1",
          "target": [
            {
              "code": "123456",
              "display": "Snomed Concept 123456",
              "relationship": "equivalent"
            }
          ]
        }
      ]
    },
    {
      "source": "http://example.org/fhir/CodeSystem/local-cs", 
      "target": "http://loinc.org",
      "element": [
        {
          "code": "A1",
          "display": "Local Code A1",
          "target": [
            {
              "code": "789-0",
              "display": "Loinc Concept 789-0", 
              "relationship": "equivalent"
            }
          ]
        }
      ]
    }
  ]
}
```

### ConceptMap with Dependencies

**Complex Mapping with Context Dependencies** - Demonstrates ConceptMaps that include additional context through dependsOn attributes.

```json
{
  "resourceType": "ConceptMap",
  "url": "http://hl7.org/fhir/ConceptMap/example2",
  "name": "ExampleDependencyMap",
  "status": "active", 
  "group": [
    {
      "source": "http://example.org/fhir/example1",
      "target": "http://example.org/fhir/example2",
      "element": [
        {
          "code": "code",
          "target": [
            {
              "code": "code2",
              "display": "Some Example Code",
              "relationship": "equivalent",
              "dependsOn": [
                {
                  "attribute": "ex3",
                  "valueCoding": {
                    "system": "http://example.org/fhir/example3",
                    "code": "some-code",
                    "display": "Something Coded"
                  }
                }
              ]
            }
          ]
        }
      ]
    }
  ]
}
```

## Operations

### $translate

Translates concepts from one CodeSystem to another using ConceptMap definitions. The operation accepts various input formats and returns structured translation results with relationship information.

{% tabs %}
{% tab title="Basic Translation Request" %}
```
POST /fhir/ConceptMap/$translate
Content-Type: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "sourceCode", 
      "valueCode": "female"
    },
    {
      "name": "sourceSystem",
      "valueUri": "http://hl7.org/fhir/administrative-gender"
    },
    {
      "name": "targetSystem",
      "valueUri": "http://terminology.hl7.org/CodeSystem/v2-0001"
    }
  ]
}
```
{% endtab %}

{% tab title="Basic Translation Response" %}
```json
{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "result",
      "valueBoolean": true
    },
    {
      "name": "match",
      "part": [
        {
          "name": "relationship",
          "valueCode": "equal"
        },
        {
          "name": "concept", 
          "valueCoding": {
            "system": "http://terminology.hl7.org/CodeSystem/v2-0001",
            "code": "F",
            "display": "F"
          }
        },
        {
          "name": "source",
          "valueUri": "http://hl7.org/fhir/ConceptMap/cm-administrative-gender-v2"
        }
      ]
    }
  ]
}
```
{% endtab %}
{% endtabs %}

{% tabs %}
{% tab title="Coding Parameter Request" %}
```
POST /fhir/ConceptMap/$translate
Content-Type: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "sourceCoding",
      "valueCoding": {
        "code": "preliminary",
        "system": "http://hl7.org/fhir/composition-status"
      }
    }
  ]
}
```
{% endtab %}

{% tab title="Coding Parameter Response" %}
```json
{
  "resourceType": "Parameters", 
  "parameter": [
    {
      "name": "result",
      "valueBoolean": true
    },
    {
      "name": "match",
      "part": [
        {
          "name": "relationship", 
          "valueCode": "equivalent"
        },
        {
          "name": "concept",
          "valueCoding": {
            "code": "active",
            "display": "active", 
            "system": "http://terminology.hl7.org/CodeSystem/v3-ActStatus"
          }
        },
        {
          "name": "source",
          "valueUri": "http://hl7.org/fhir/ConceptMap/cm-composition-status-v3"
        }
      ]
    }
  ]
}
```
{% endtab %}
{% endtabs %}

{% tabs %}
{% tab title="Instance-Level Operation Request" %}
```
POST /fhir/ConceptMap/cm1/$translate
Content-Type: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "sourceCode",
      "valueCode": "in-progress"
    },
    {
      "name": "sourceSystem", 
      "valueUri": "http://hl7.org/fhir/event-status"
    }
  ]
}
```
{% endtab %}

{% tab title="Instance-Level Operation Response" %}
```json
{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "result",
      "valueBoolean": true
    },
    {
      "name": "match",
      "part": [
        {
          "name": "relationship",
          "valueCode": "equivalent"
        },
        {
          "name": "concept",
          "valueCoding": {
            "code": "active",
            "display": "active",
            "system": "http://hl7.org/fhir/resource-status"
          }
        },
        {
          "name": "source",
          "valueUri": "http://hl7.org/fhir/ConceptMap/sc-clinicalimpression-status-TEST"
        }
      ]
    }
  ]
}
```
{% endtab %}
{% endtabs %}

{% tabs %}
{% tab title="Missing Code Request" %}
```
POST /fhir/ConceptMap/$translate
Content-Type: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "sourceCode",
      "valueCode": "invalid-code"
    },
    {
      "name": "sourceSystem",
      "valueUri": "http://hl7.org/fhir/administrative-gender"
    }
  ]
}
```
{% endtab %}

{% tab title="Missing Code Response" %}
```json
{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "result",
      "valueBoolean": false
    }
  ]
}
```
{% endtab %}
{% endtabs %}

The `$translate` operation also supports GET requests with query parameters and CodeableConcept inputs for complex translation scenarios. Multiple ConceptMaps can provide translations for the same source code, and all applicable mappings are returned in the response.

> **Note**: ConceptMap resources are accessible through the standard FHIR REST API at `/fhir/ConceptMap`, supporting Create, Read, Update, and Delete operations. Search operations on ConceptMap resources are currently in development and will be available in future releases.
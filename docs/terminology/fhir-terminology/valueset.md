---
description: FHIR ValueSet resource for defining collections of codes from CodeSystems for specific use cases and contexts
---

# ValueSet

A ValueSet resource defines a set of codes drawn from one or more CodeSystems, intended for use in specific contexts. ValueSets serve as the bridge between CodeSystem definitions and their actual use in coded elements, specifying exactly which codes are allowable for particular data elements.

While a CodeSystem might contain thousands of concepts, a ValueSet curates the relevant subset for specific use cases. For example, LOINC contains thousands of laboratory codes, but a ValueSet for "Cholesterol Tests" would select only the codes relevant to cholesterol measurements.

ValueSets are also commonly used in healthcare applications to populate UI controls like dropdown lists. The `$expand` operation converts ValueSet definitions into enumerated lists that can be directly consumed by user interfaces. They can also be filtered during expansion using search parameters, text matching, or full-text search to provide dynamic typeahead functionality.

See also: [FHIR ValueSet Specification](https://build.fhir.org/valueset.html)

## Types of ValueSets

ValueSets can be defined in different ways depending on your needs for precision, maintenance, and flexibility.

### Extensional vs Intensional

**Extensional Definition** - Explicitly lists every code that should be included. Provides precise control but requires manual maintenance when new codes are added to the underlying CodeSystems.

**Intensional Definition** - Uses rules or filters to algorithmically determine which codes to include. Automatically stays current as new codes are added to CodeSystems, but may be less predictable.

### Implicit vs Explicit

**Implicit ValueSets** - Automatically generated from CodeSystems without explicit definition. For example, a ValueSet that includes "all codes from SNOMED CT" would be implicit.

**Explicit ValueSets** - Formally defined FHIR resources with specific composition rules and metadata.

### Expanded vs Not-Expanded

**Not-Expanded** - Contains only the definition (composition rules) but not the actual list of codes.

**Expanded** - Contains both the definition and the complete enumerated list of codes that result from applying the composition rules.

## Examples

### Simple Extensional ValueSet

**Administrative Gender** - Explicitly lists all allowed codes from a single CodeSystem.

```json
{
  "resourceType": "ValueSet",
  "url": "http://hl7.org/fhir/ValueSet/administrative-gender",
  "status": "active",
  "compose": {
    "include": [
      {
        "system": "http://hl7.org/fhir/administrative-gender"
      }
    ]
  }
}
```

This ValueSet includes all codes from the administrative-gender CodeSystem.

### Intensional ValueSet with Filters

**LOINC Cholesterol Tests** - Uses filters to algorithmically select relevant codes.

```json
{
  "resourceType": "ValueSet", 
  "url": "http://hl7.org/fhir/ValueSet/example-intensional",
  "compose": {
    "include": [
      {
        "system": "http://loinc.org",
        "filter": [
          {
            "property": "parent",
            "op": "=",
            "value": "LP382412-7"
          }
        ]
      }
    ],
    "exclude": [
      {
        "system": "http://loinc.org", 
        "concept": [
          {
            "code": "5932-9",
            "display": "Cholesterol [Presence] in Blood by Test strip"
          }
        ]
      }
    ]
  }
}
```

This ValueSet includes all LOINC codes that are children of the parent concept "LP382412-7" but excludes the specific test strip method.

### Extensional ValueSet with Specific Concepts

**LOINC Cholesterol Codes** - Explicitly lists specific cholesterol-related codes.

```json
{
  "resourceType": "ValueSet",
  "url": "http://hl7.org/fhir/ValueSet/example", 
  "title": "LOINC Codes for Cholesterol in Serum/Plasma",
  "compose": {
    "include": [
      {
        "system": "http://loinc.org",
        "version": "2.36",
        "concept": [
          {
            "code": "14647-2",
            "display": "Cholesterol [Moles/Volume]"
          },
          {
            "code": "2093-3", 
            "display": "Cholesterol [Mass/Volume]"
          },
          {
            "code": "35200-5",
            "display": "Cholesterol [Mass Or Moles/Volume]"
          },
          {
            "code": "9342-7",
            "display": "Cholesterol [Percentile]"
          }
        ]
      }
    ]
  }
}
```

This ValueSet explicitly enumerates specific cholesterol measurement codes from LOINC version 2.36.

### Complex ValueSet with Multiple Sources

**Yes/No/Don't Know** - Combines codes from multiple CodeSystems and includes expansion results.

```json
{
  "resourceType": "ValueSet",
  "url": "http://hl7.org/fhir/ValueSet/yesnodontknow",
  "name": "YesNoDontKnow", 
  "compose": {
    "include": [
      {
        "valueSet": ["http://terminology.hl7.org/ValueSet/v2-0136"]
      },
      {
        "system": "http://terminology.hl7.org/CodeSystem/data-absent-reason",
        "concept": [
          {
            "code": "asked-unknown",
            "display": "Don't know"
          }
        ]
      }
    ]
  },
  "expansion": {
    "identifier": "urn:uuid:bf99fe50-2c2b-41ad-bd63-bee691981b4",
    "timestamp": "2015-07-14T10:00:00Z",
    "contains": [
      {
        "system": "http://terminology.hl7.org/CodeSystem/v2-0532",
        "code": "Y",
        "display": "Yes"
      },
      {
        "system": "http://terminology.hl7.org/CodeSystem/v2-0532", 
        "code": "N",
        "display": "No"
      },
      {
        "system": "http://terminology.hl7.org/CodeSystem/data-absent-reason",
        "code": "asked-unknown",
        "display": "Don't know"
      }
    ]
  }
}
```

This ValueSet combines codes from multiple sources: it includes another ValueSet (`v2-0136`) and adds a specific concept from the data-absent-reason CodeSystem. The expansion shows the actual codes that result from this composition.

## Operations

### $validate-code

Validates whether a given code is a member of the ValueSet. This operation is essential for ensuring data quality by checking that coded values conform to the allowed set.

**Example:**
{% tabs %}
{% tab title="Request" %}
```
GET /fhir/ValueSet/$validate-code?url=http://hl7.org/fhir/ValueSet/administrative-gender&code=male&inferSystem=true
```
{% endtab %}

{% tab title="Response" %}
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
{% endtabs %}

### $expand

Transforms a ValueSet definition into a complete enumerated list of codes. This operation resolves all composition rules, filters, and inclusions to produce the actual codes that can be used in applications.

**Example:**
{% tabs %}
{% tab title="Request" %}
```
GET /fhir/ValueSet/$expand?url=http://hl7.org/fhir/ValueSet/administrative-gender
```
{% endtab %}

{% tab title="Response" %}
```json
{
  "resourceType": "ValueSet",
  "expansion": {
    "timestamp": "2025-01-31T14:30:00Z",
    "total": 4,
    "contains": [
      {
        "system": "http://hl7.org/fhir/administrative-gender",
        "code": "male",
        "display": "Male"
      },
      {
        "system": "http://hl7.org/fhir/administrative-gender", 
        "code": "female",
        "display": "Female"
      },
      {
        "system": "http://hl7.org/fhir/administrative-gender",
        "code": "other", 
        "display": "Other"
      },
      {
        "system": "http://hl7.org/fhir/administrative-gender",
        "code": "unknown",
        "display": "Unknown"
      }
    ]
  }
}
```
{% endtab %}
{% endtabs %}

The `$expand` operation also supports filtering parameters for dynamic search functionality:
- `filter`: Text to match against code or display values
- `count`: Maximum number of codes to return
- `offset`: Number of codes to skip (for paging)

> **Note:** ValueSets provide the flexibility to define code collections that evolve with your needs. Extensional definitions offer precision, while intensional definitions provide automatic updates. The choice depends on your requirements for control versus maintenance overhead.

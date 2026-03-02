---
description: >-
  Introduction to FHIR terminology framework, canonicals concept, and overview
  of core terminology resources
---

# FHIR Terminology

Healthcare data exchange relies heavily on coded values to ensure consistent meaning across different systems and organizations. FHIR terminology provides a comprehensive framework for managing these coded values, from simple administrative codes to complex clinical terminologies used in patient care.

The FHIR terminology module defines how to represent, validate, and exchange coded data through a set of interconnected resources and data types. This system enables healthcare applications to share meaningful data while maintaining semantic precision and supporting various terminology standards like SNOMED CT, LOINC, and ICD-10.

See also: [FHIR Terminology Module Specification](https://build.fhir.org/terminology-module.html)

## Canonicals

In FHIR terminology, resources are identified using **canonical URLs** rather than physical resource IDs. A canonical URL serves as a globally unique, logical identifier that remains stable regardless of where the resource is stored or how it's accessed.

For example, the canonical URL `http://hl7.org/fhir/ValueSet/observation-status` identifies the observation status value set globally, whether it's stored in your local FHIR server, an external terminology service, or referenced in a profile binding.

This approach provides several key benefits:

* **Version independence**: Canonical URLs can include version information (`|4.0.1`) when specific versions are required
* **Location independence**: The same logical resource can be stored in multiple servers
* **Stable references**: Resource IDs may change during migrations, but canonical URLs remain constant

Here's how canonicals are used in practice:

**Profile binding example:**

```json
{
  "id": "Observation.status",
  "binding": {
    "strength": "required",
    "description": "Codes providing the status of an observation.",
    "valueSet": "http://hl7.org/fhir/ValueSet/observation-status|4.0.1"
  }
}
```

**Terminology operation example:**

{% tabs %}
{% tab title="Request" %}
```
GET /fhir/ValueSet/$expand?url=http://hl7.org/fhir/ValueSet/condition-clinical
```
{% endtab %}

{% tab title="Response" %}
```json
{
  "resourceType": "ValueSet",
  "url": "http://hl7.org/fhir/ValueSet/condition-clinical",
  "expansion": {
    "identifier": "urn:uuid:9e6375e6-bd2d-4301-8926-5274899038da",
    "timestamp": "2026-02-01T12:00:00+00:00",
    "total": 6,
    "contains": [
      {
        "system": "http://terminology.hl7.org/CodeSystem/condition-clinical",
        "code": "active",
        "display": "Active"
      },
      {
        "system": "http://terminology.hl7.org/CodeSystem/condition-clinical",
        "code": "inactive",
        "display": "Inactive"
      },
      {
        "system": "http://terminology.hl7.org/CodeSystem/condition-clinical",
        "code": "recurrence",
        "display": "Recurrence"
      },
      {
        "system": "http://terminology.hl7.org/CodeSystem/condition-clinical",
        "code": "relapse",
        "display": "Relapse"
      },
      {
        "system": "http://terminology.hl7.org/CodeSystem/condition-clinical",
        "code": "remission",
        "display": "Remission"
      },
      {
        "system": "http://terminology.hl7.org/CodeSystem/condition-clinical",
        "code": "resolved",
        "display": "Resolved"
      }
    ]
  }
}
```
{% endtab %}
{% endtabs %}

## Concepts

FHIR terminology is built around core concepts that work together to provide a complete solution for coded healthcare data.

[**Coded Values**](coded-values.md) - FHIR provides three specialized data types for representing coded values: `code` for simple fixed vocabularies, `Coding` for precise system-code pairs, and `CodeableConcept` for complex multi-system representations.

[**CodeSystem**](codesystem.md) - Resources that define sets of codes and their meanings within specific terminology domains. CodeSystems establish what codes exist and what they mean, from simple administrative codes to complex clinical terminologies.

[**ValueSet**](valueset.md) - Collections of codes from one or more CodeSystems that are appropriate for specific contexts. ValueSets curate relevant subsets of codes for particular use cases or implementation requirements.

**ConceptMap** - Resources that define relationships and translations between codes from different CodeSystems, enabling structured mapping and transformation between different terminology systems.

See also:

* [Coded Values](coded-values.md)
* [CodeSystem](codesystem.md)
* [ValueSet](valueset.md)
* [Profiles, Binding, and Validation](profiles-binding-validation.md)

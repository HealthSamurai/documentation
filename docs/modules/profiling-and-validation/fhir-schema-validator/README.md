---
description: FHIR resource validation and profiling with FHIR Schema Validator in Aidbox.
---

# FHIR Schema Validator

{% hint style="info" %}
Test FHIR Schema Validator on public [demo page](https://fhir-validator.aidbox.app).
{% endhint %}

This new validation engine is set to replace the existing [Zen Schema Validator](../../../reference/all-settings.md) and JSON Schema validators.\
\
The primary objectives for this new validator are _enhanced performance_, _easy configuration_, and _straightforward interaction_. Internally, the new validator utilizes the [FHIR Schema](https://github.com/fhir-schema/fhir-schema) for the validation process. We believe that it has the potential to become a [community](https://chat.fhir.org/#narrow/stream/391879-FHIR-Schema/topic/early.20draft) standard, and we are actively working towards this goal.\
\
Building on our past experiences, we've simplified the interaction process with the validation module. If you wish to use the StructureDefinition resource as a source of truth, simply POST it.

## FHIR Schema

FHIR Schema is a format designed to simplify the implementation and validation of FHIR resources. It is heavily inspired by the design of JSON Schema and introduces a more developer-friendly representation of FHIR StructureDefinitions.

### Key features of FHIR Schema include:

* _**Simplified Structure**_\
  FHIR Schema represents FHIR resources and their elements in a more straightforward and intuitive manner compared to FHIR StructureDefinition. Each element is represented as a property of the resource with its type specified directly. This representation is similar to how data structures are typically defined in programming languages.
* _**Nested Elements**_\
  FHIR Schema provides a clear and simple way to represent and validate nested elements in FHIR resources. This is a key requirement for many healthcare data use cases.
* _**First-class Arrays**_\
  Identify and label arrays. Most non-XML implementations distinguish between arrays and singular elements, so it's beneficial to pre-calculate this distinction.
* _**Clear Implementation Semantics**_\
  FHIR Schema offers clear semantics for implementing FHIR validation rules. This clarity can make it easier for developers to create robust and reliable FHIR implementations.
* _**Source of metadata**_\
  This is essential for FHIRPath, CQL, and code-generation.

In summary, FHIR Schema is a format that aims to make FHIR more accessible and easier to work with for developers, potentially leading to improved interoperability of healthcare systems.

## Validator Features

Comparison of features with previous validation engines

<table data-full-width="false"><thead><tr><th width="284">Feature</th><th width="136" data-type="checkbox">FHIR Schema</th><th width="131" data-type="checkbox">Zen Schema</th><th data-type="checkbox">JSON Schema</th></tr></thead><tbody><tr><td>Invariants</td><td>true</td><td>false</td><td>false</td></tr><tr><td>Forbidden elements (max 0)</td><td>true</td><td>false</td><td>false</td></tr><tr><td>Required elements</td><td>true</td><td>true</td><td>true</td></tr><tr><td>Constants</td><td>true</td><td>true</td><td>false</td></tr><tr><td>Union types</td><td>true</td><td>false</td><td>false</td></tr><tr><td>Empty values check</td><td>true</td><td>false</td><td>false</td></tr><tr><td>Primitive types</td><td>true</td><td>true</td><td>true</td></tr><tr><td>Slicing</td><td>true</td><td>true</td><td>false</td></tr><tr><td>Ordered slicing</td><td>true</td><td>false</td><td>false</td></tr><tr><td>Default slice</td><td>true</td><td>false</td><td>false</td></tr><tr><td>Type slicing</td><td>true</td><td>false</td><td>false</td></tr><tr><td>Binding slicing</td><td>true</td><td>false</td><td>false</td></tr><tr><td>Target slicing</td><td>true</td><td>false</td><td>false</td></tr><tr><td>Re-slicing</td><td>true</td><td>false</td><td>false</td></tr><tr><td>Terminology bindings</td><td>true</td><td>true</td><td>false</td></tr><tr><td>Extensions validation</td><td>true</td><td>false</td><td>false</td></tr><tr><td>Underscore properties</td><td>true</td><td>false</td><td>false</td></tr><tr><td>References</td><td>true</td><td>true</td><td>true</td></tr><tr><td>Recursive schemas</td><td>true</td><td>true</td><td>true</td></tr><tr><td>Error on <code>null</code>s*</td><td>true</td><td>false</td><td>false</td></tr></tbody></table>

{% hint style="warning" %}
Error on `nulls`

Starting from [the 2405 ](../../../overview/release-notes.md#may-2024-stable-2405)release when using a FHIR schema validation engine Aidbox throws errors when posting values that contain`null`s. This behavior is FHIR compliant.\
\
When using Zen schema and JSON schema Aidbox automatically strips `null` values.
{% endhint %}

## Useful pages

* [Setup ⚙️](setup-aidbox-with-fhir-schema-validation-engine.md)
* [Upload FHIR IG to Aidbox ⬆️](../../../tutorials/artifact-registry-tutorials/upload-fhir-implementation-guide/)

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

## Reference validation and targetProfile

The **References** and **Target slicing** features in the table above mean that when a Reference element (or a slice of it) specifies `targetProfile`, Aidbox validates that the **content** of the referenced resource conforms to the specified profile(s). This section describes how that works.

### How targetProfile validation works

When a profile constrains a Reference with `targetProfile`:

1. Aidbox resolves the reference (e.g. `Observation/obs-1`) and loads the referenced resource.
2. It validates the **content** of that resource against each profile listed in `targetProfile`.
3. If the content does not conform to any of the target profiles, validation fails with:
   * **Code**: `invalid-target-profile`
   * **Message**: `Referenced resource <reference> content doesn't conform to any of target profiles: <profile URLs>`

The referenced resource does not need to declare `meta.profile`; conformance is checked by validating the resource content against the target profile(s). If the reference points to a non-existent resource, a separate error (e.g. `non-existent-resource`) is raised.

### Interaction with slicing

When a profile defines **slicing** on a Reference array (e.g. `DiagnosticReport.result`):

* Each **named slice** can have its own `targetProfile`. The slicing **discriminator** (e.g. type or profile) determines which slice a given reference belongs to.
* Only the **matched slice's** `targetProfile` is applied to that reference. Other slices' targetProfiles are not checked for it.
* **Open slicing**: references that do not match any named slice are accepted **without** targetProfile checks. To require that every reference conform to some profile, use a closed slice or a single Reference element with one targetProfile.

### Example: DiagnosticReport with sliced result

**1. Observation profile** (referenced by result slice) with fixed constraints:

```json
{
  "resourceType": "StructureDefinition",
  "url": "http://example.org/StructureDefinition/LabObservation",
  "name": "LabObservation",
  "kind": "resource",
  "type": "Observation",
  "baseDefinition": "http://hl7.org/fhir/StructureDefinition/Observation",
  "differential": {
    "element": [
      { "path": "Observation.status", "fixedCode": "final" },
      { "path": "Observation.code", "fixedCode": "laboratory" }
    ]
  }
}
```

**2. DiagnosticReport profile** with sliced `result` and targetProfile on one slice:

```json
{
  "resourceType": "StructureDefinition",
  "url": "http://example.org/StructureDefinition/LabReport",
  "name": "LabReport",
  "kind": "resource",
  "type": "DiagnosticReport",
  "baseDefinition": "http://hl7.org/fhir/StructureDefinition/DiagnosticReport",
  "differential": {
    "element": [
      {
        "path": "DiagnosticReport.result",
        "slicing": {
          "discriminator": [{ "type": "profile", "path": "$this" }],
          "rules": "open"
        }
      },
      {
        "path": "DiagnosticReport.result",
        "sliceName": "labResult",
        "type": [{ "code": "Reference", "targetProfile": ["http://example.org/StructureDefinition/LabObservation"] }]
      }
    ]
  }
}
```

**Valid**: A DiagnosticReport whose `result` references an Observation that has `status: final` and `code: laboratory` (or conforms to LabObservation) — the reference matches the `labResult` slice and its content conforms to the target profile.

**Invalid**: A DiagnosticReport whose `result` references an Observation with `status: preliminary` or a different `code`. The referenced resource does not conform to LabObservation, so validation fails with `invalid-target-profile`: "Referenced resource Observation/xyz content doesn't conform to any of target profiles: http://example.org/StructureDefinition/LabObservation".

## Useful pages

* [Setup ⚙️](setup-aidbox-with-fhir-schema-validation-engine.md)
* [Upload FHIR IG to Aidbox ⬆️](../../../tutorials/artifact-registry-tutorials/upload-fhir-implementation-guide/)

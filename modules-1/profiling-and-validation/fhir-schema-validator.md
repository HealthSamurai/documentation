---
description: New validation engine
---

# FHIR Schema Validator

{% hint style="info" %}
**Alpha Feature** 🏗️

While all validation features works correctly, we're still integrating FHIR Schema validator into Aidbox.&#x20;

Test FHIR Schema Validator on public [demo page](https://fhir-validator.aidbox.app).
{% endhint %}

This new validation engine is set to replace the existing [Zen Schema Validator](../../reference/zen-schema-reference/) and [JSON Schema](../../core-modules/usdjson-schema.md) validator. \
\
The primary objectives for this new validator are _enhanced performance_, _easy configuration_, and _straightforward interaction_. Internally, the new validator utilizes the [FHIR Schema](https://github.com/fhir-schema/fhir-schema) for the validation process. We believe that it has the potential to become a [community](https://chat.fhir.org/#narrow/stream/391879-FHIR-Schema/topic/early.20draft) standard, and we are actively working towards this goal. \
\
Building on our past experiences, we've simplified the interaction process with the validation module. If you wish to use the StructureDefinition resource as a source of truth, simply POST it.

<table data-view="cards"><thead><tr><th></th><th></th><th></th><th data-hidden data-card-cover data-type="files"></th><th data-hidden data-card-target data-type="content-ref"></th></tr></thead><tbody><tr><td>Setup ⚙️</td><td></td><td></td><td></td><td><a href="fhir-schema-validator/setup.md">setup.md</a></td></tr><tr><td>FHIR Schema 🔥</td><td></td><td></td><td></td><td><a href="fhir-schema-validator/fhir-schema.md">fhir-schema.md</a></td></tr><tr><td>Validator Features ✅</td><td></td><td></td><td></td><td><a href="fhir-schema-validator/validator-features.md">validator-features.md</a></td></tr><tr><td>Supported IGs 📦</td><td></td><td></td><td></td><td><a href="fhir-schema-validator/supported-implementation-guides.md">supported-implementation-guides.md</a></td></tr></tbody></table>


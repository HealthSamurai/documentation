---
description: How to enable new validator engine and specify IGs
---

# Setup Aidbox with FHIR Schema validation engine

### Configure Aidbox to Use FHIR Schema Validation Engine

To configure Aidbox to use FHIR Schema Validation Engine you need to:

* Enable FHIR Schema Validation Engine
* Specify the IG you want to be loaded during the startup
* Configure external terminology server

#### Enable the FHIR Schema Validation Engine

To enable the FHIR Schema Validator engine, set the following environment variable:

```
BOX_FHIR_SCHEMA_VALIDATION=true
```

#### Enable a Specific Implementation Guide (IG)

To enable a specific IG, list it in the following environment variable. Separate different packages using a colon (`:`). Package entry template: `<package_name>#<package_version>`.

```
BOX_BOOTSTRAP_FHIR_PACKAGES=hl7.fhir.r4.core#4.0.1
```

#### Configure Terminology Engine

Aidbox supports different terminology engine modes. To use hybrid mode (recommended) with an external terminology server for codes not available locally, set:

```
BOX_FHIR_TERMINOLOGY_ENGINE=hybrid
```

See [Hybrid Mode](../../../terminology-module/aidbox-terminology-module/hybrid.md) for details on terminology engine modes.

#### Configure External Terminology Service

To validate coded values with an external Terminology service, set it in the following environment variable.

```
BOX_FHIR_TERMINOLOGY_SERVICE_BASE_URL=https://tx.health-samurai.io/fhir
```

{% hint style="warning" %}
Please note that this external terminology server will be used exclusively to validate terminology bindings.

If you don't specify the `BOX_FHIR_TERMINOLOGY_SERVICE_BASE_URL` environment variable the validation of terminology bindings will be skipped.
{% endhint %}

### Validation Engine Settings

#### FHIRSchema Validator Strict Extension Resolution

Extensions referenced in data instances must be known to Aidbox. If Aidbox encounters an unknown extension during validation, it will raise a validation error.

```
BOX_FHIR_VALIDATOR_STRICT_EXTENSION_RESOLUTION_ENABLED=true
```

#### FHIRSchema Validator Strict Profile Resolution

Profiles referenced in data instances _(e.g. meta.profile)_ must be known to Aidbox. If Aidbox encounters an unknown profile during validation, it will raise a validation error.

```
BOX_FHIR_VALIDATOR_STRICT_PROFILE_RESOLUTION_ENABLED=true
```

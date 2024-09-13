---
description: How to enable new validator engine and specify IGs
---

# Setup Aidbox with FHIR Schema validation engine

To correctly set up Aidbox, we'll utilize the Aidbox configuration projects. \
\
There's an [existing guide](../../../getting-started/run-aidbox-locally-with-docker/) for this process. Adhere to this guide, <mark style="background-color:green;">but note a variation</mark> when you reach the `Configure the Aidbox` step: instead of using the recommended configuration projects (R4,R4B,R5,etc.) — clone this specific project:

```sh
git clone \
  https://github.com/Aidbox/aidbox-project-template-fhir-schema.git \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```

This project is tailored with specific configurations for FHIR Schema Validator.

### Configuration Overview: Key Features and Distinctions

If you already have a configuration project, you can replicate these steps to enable FHIR Schema Validator in your Aidbox instance.

#### Enable the FHIR Schema Validator Engine

To enable the FHIR Schema Validator engine, set the following environment variable:

```
AIDBOX_FHIR_SCHEMA_VALIDATION=true
```

#### Enable a Specific Implementation Guide (IG)

To enable a specific IG, list it in the following environment variable. Separate different packages using a colon (`:`). You can visit the following page to get a complete [list of IGs](supported-implementation-guides.md) supported by the FHIR Schema validator. Package entry template: `<package_name>#<package_version>`.

```
AIDBOX_FHIR_PACKAGES=hl7.fhir.us.core#5.0.1
```

### External Terminology Service

To validate coded values with an external Terminology service, set it in the following environment variable.

```
AIDBOX_TERMINOLOGY_SERVICE_BASE_URL=https://tx.fhir.org/r4
```

{% hint style="warning" %}
Please note that this external terminology server will be used exclusively to validate terminology bindings.
{% endhint %}

### Validation Engine Settings

#### FHIRSchema Validator Strict Extension Resolution

Extensions referenced in data instances must be known to Aidbox. If Aidbox encounters an unknown extension during validation, it will raise a validation error.

```
AIDBOX_VALIDATOR_STRICT_EXTENSION_RESOLUTION_ENABLED=true
```

#### FHIRSchema Validator Strict Profile Resolution

Profiles referenced in data instances _(e.g. meta.profile)_ must be known to Aidbox. If Aidbox encounters an unknown profile during validation, it will raise a validation error.

```
AIDBOX_VALIDATOR_STRICT_PROFILE_RESOLUTION_ENABLED=true
```

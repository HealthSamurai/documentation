---
description: How to enable new validator engine and specify IGs
---

# Setup

To correctly set up Aidbox, we'll utilize the Aidbox configuration projects. \
\
There's an [existing guide](../../../getting-started-1/run-aidbox/run-aidbox-locally-with-docker.md) for this process. Adhere to this guide, <mark style="background-color:green;">but note a variation</mark> when you reach the `Configure the Aidbox` step: instead of using the recommended configuration projects (R4,R4B,R5,etc.) — clone this specific project:

```sh
git clone \
  https://github.com/Aidbox/aidbox-project-template-fhir-schema.git \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```

This project is tailored with specific configurations for FHIR Schema Validator.

### Configuration Overview: Key Features and Distinctions

#### Enable the FHIR Schema Validator Engine

To enable the FHIR Schema Validator engine, set the following environment variable:

```
AIDBOX_FHIR_SCHEMA_VALIDATION_ENABLED=true
```

#### Enable a Specific Implementation Guide (IG)

To enable a specific IG, list it in the following environment variable. Separate different packages using a colon (`:`). You can visit the following page to get a complete [list of IGs](supported-implementation-guides.md) supported by the FHIR Schema validator. Package entry template: `<package_name>#<package_version>`.

```
AIDBOX_FHIR_PACKAGES=hl7.fhir.us.core#5.0.1
```

### External Terminology Server

To validate coded values with an external Terminology server, set it in the following environment variable by specifying the `ValueSet/$validate-code` endpoint.

```
AIDBOX_FHIR_SCHEMA_VALIDATION_ENABLED=https://tx.fhir.org/r4/ValueSet/$validate-code
```

{% hint style="warning" %}
Please note that this external terminology server will be used exclusively for validating terminology bindings.
{% endhint %}

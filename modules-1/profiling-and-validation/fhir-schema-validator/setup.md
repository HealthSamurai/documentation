---
description: How to enable new validator engine and specify IGs
---

# Setup

## Enable the FHIR Schema Validator Engine

To enable the FHIR Schema Validator engine, provide the following environment variable:

{% code title=".env" %}
```
BOX_FEATURES_VALIDATION_MODE=fhir-schema
```
{% endcode %}

{% hint style="info" %}
Please note that this validation mode suppresses other validation engines and does not cooperate with them
{% endhint %}

## Enable a Specific Implementation Guide (IG)

To enable a specific IG, list it in the following environment variable. Separate different packages using a colon (`:`). You can visit the following page to get a complete list of IGs supported by the FHIR Schema validator. Package entry template: `<package_name>#<package_version>`.

{% code title=".env" %}
```
BOX_FHIR__PACKAGES=hl7.fhir.r4.core#4.0.1:hl7.fhir.us.core#5.0.1
```
{% endcode %}

# FHIR Schema Validator

{% hint style="warning" %}
**Alpha Feature**

We've tested all the validation features, including slicings, constraints, types, and terminology bindings, on numerous profiles with positive results. However, we're still in the process of integrating the new validator engine into Aidbox. \
\
Additionally, you can test this validator on our public demo page [here](https://fhir-validator.aidbox.app/).
{% endhint %}

## FHIR Schema

FHIR Schema is a format designed to simplify the implementation and validation of FHIR resources. It is heavily inspired by the design of JSON Schema and introduces a more developer-friendly representation of FHIR StructureDefinitions.





\-----

* Definition
* Features
* Supported packages
* Base syntax (maybe link to doc)

## Setup

### How to Enable the FHIR Schema Validator Engine

To enable the FHIR Schema Validator engine, provide the following environment variable:

{% code title=".env" %}
```
BOX_FEATURES_VALIDATION_MODE=fhir-schema
```
{% endcode %}

{% hint style="info" %}
Please note that this validation mode suppresses other validation engines and does not cooperate with them
{% endhint %}

### How to Enable a Specific Implementation Guide (IG)

To enable a specific IG, list it in the following environment variable. Separate different packages using a colon (`:`)

{% code title=".env" %}
```
BOX_FHIR__PACKAGES=hl7.fhir.r4.core#4.0.1:hl7.fhir.us.core#5.0.1
```
{% endcode %}

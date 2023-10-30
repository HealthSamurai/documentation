# FHIR Schema Validator

{% hint style="info" %}
**Alpha Feature Note:**

We've tested all the validation features such as _slicings_, _constraints_, _types_, _terminology bindings_ on many profiles and they work great, but we're still working on integrating the new validator engine into Aidbox.
{% endhint %}

## FHIR Schema

* Definition
* Features
* Supported packages
* Base syntax (maybe link to doc)

## Setup

### How to enable FHIR Schema Validator engine

To enable FHIR Schema Validator engine provide following environment variable:

{% code title=".env" %}
```makefile
BOX_FEATURES_VALIDATION_MODE=fhir-schema
```
{% endcode %}

{% hint style="info" %}
Please note that this validation mode suppress other validation engines and doesn't cooperate with them&#x20;
{% endhint %}

### How to enable specific Implementation Guide (IG)

To enable specific IG list it in the following environment variable; different packages separated by `":"`

```makefile
BOX_FHIR__PACKAGES=hl7.fhir.r4.core#4.0.1:hl7.fhir.us.core#5.0.1
```

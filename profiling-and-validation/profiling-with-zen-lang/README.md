---
description: Validate your resources with zen-lang schemas
---

# zen-lang validator

{% hint style="danger" %}
The zen-lang validator is scheduled for deprecation in several upcoming releases. Please consider using [FHIR Schema validator](../../modules-1/profiling-and-validation/fhir-schema-validator.md).
{% endhint %}

Aidbox supports a powerful profile validation mechanism powered by [Zen language](https://github.com/zen-lang/zen). It allows you to define a set (or multiple sets) of profiles in [edn](https://github.com/edn-format/edn) format and later load them into your Aidbox instance. These profiles can be used to validate resources against the specified schemas, including individual properties and also interoperation with other profiles in a composable way.

## Start using zen profiling in Aidbox

1. Write profiles with schemas to validate against

{% content-ref url="write-a-custom-zen-profile.md" %}
[write-a-custom-zen-profile.md](write-a-custom-zen-profile.md)
{% endcontent-ref %}

2\. Load profiles into your Aidbox instance

{% content-ref url="extend-an-ig-with-a-custom-zen-profile.md" %}
[extend-an-ig-with-a-custom-zen-profile.md](extend-an-ig-with-a-custom-zen-profile.md)
{% endcontent-ref %}

{% hint style="info" %}
Resource examples and external (not-present) terminologies are not loaded into the storage by default
{% endhint %}

3\. Validate resources against profiles

Use FHIR [$validate](../../api-1/fhir-api/usdvalidate.md) operation or a CRUD request with a profile URL specified in `.meta.profile` or in `.profile` query parameters.

## Zen FHIR packages

Aidbox team has created an [open-source tool to generate Zen FHIR packages](https://github.com/zen-lang/fhir) from FHIR packages.

Generated packages are available under [zen-fhir Github organization](https://github.com/orgs/zen-fhir/repositories).

{% hint style="warning" %}
Zen FHIR packages require `AIDBOX_CORRECT_AIDBOX_FORMAT=true` environment variable.
{% endhint %}

### Use Zen FHIR packages

We recommend to load Zen FHIR packages using an [Aidbox Configuration project](../../aidbox-configuration/aidbox-zen-lang-project/). Visit the following page for a detailed guide:

{% content-ref url="../../aidbox-configuration/aidbox-zen-lang-project/enable-igs.md" %}
[enable-igs.md](../../aidbox-configuration/aidbox-zen-lang-project/enable-igs.md)
{% endcontent-ref %}

### Convert custom FHIR profiles to Zen FHIR package

Our [`zen-lang/fhir`](https://github.com/zen-lang/fhir/blob/main/README.md) tool allows you to generate zen schemas for custom FHIR profiles and use them in your [Aidbox Configuration projects](../../aidbox-configuration/aidbox-zen-lang-project/).

### Create custom Zen FHIR package based on other Zen FHIR packages

You can use existing Zen FHIR packages as a foundation for your custom Zen FHIR package. See our [guide on profiling with Zen-lang](extend-an-ig-with-a-custom-zen-profile.md).

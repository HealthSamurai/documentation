---
description: Validate your resources with zen-lang schemas
---

# Profiling with zen-lang

Aidbox supports an alternative yet very powerful profile validation mechanism powered by [Zen language](https://github.com/zen-lang/zen). You can just define a set (or multiple sets) of validation profiles in [EDN](https://github.com/edn-format/edn) format and let your Aidbox server know its location. [Zen-lang](https://github.com/zen-lang/zen) allows Aidbox to validate resources against schemas. It can validate individual properties as well as large profiles in a composable way

## Enable zen profiling in Aidbox

1. Add environment variables to the docker-compose.yaml

| Env name                | Sample value                      | Description                                                                                                             |
| ----------------------- | --------------------------------- | ----------------------------------------------------------------------------------------------------------------------- |
| AIDBOX\_ZEN\_PATHS      | url:zip:\<url to zen project>     | Use released project from [zen-lang/fhir repo](https://github.com/zen-lang/fhir/releases/latest)                        |
| AIDBOX\_ZEN\_PATHS      | path:zip:\<file system path>      | Mount a Zen FHIR [NPM package](https://www.npmjs.com/search?q=%40zen-lang) with precompiled IGs or a custom zen project |
| AIDBOX\_ZEN\_ENTRYPOINT | hl7-fhir-us-davinci-pdex-plan-net | Main zen namespace of an IG                                                                                             |

&#x20;2\. Start Aidbox with the docker-compose

StructureDefinitions and inlined terminologies are created during startup.&#x20;

{% hint style="info" %}
Resource examples and External (not-present) terminologies are not loaded in the storage by default
{% endhint %}

&#x20;3\. Validate resources&#x20;

Use FHIR $validate operation or a CRUD request with profile URL specified in `.meta.profile` or in `.profile` query URL parameter.

## Validation modes supported with zen schemas

For validation (e.g. in [FHIR CRUD API](../../api-1/api/crud-1/)) Aidbox uses zen schemas tagged with `zen.fhir/base-schema` or `zen.fhir/profile-schema`.

Schemas with these tags must have `:zen.fhir/type` and `:zen.fhir/profileUri` keys specified.

{% content-ref url="write-a-custom-zen-profile.md" %}
[write-a-custom-zen-profile.md](write-a-custom-zen-profile.md)
{% endcontent-ref %}

### `zen.fhir/base-schema`

Schemas tagged with `zen.fhir/base-schema` are used to validate every resource of their type. When loaded into Aidbox such schema will be used instead of default json schema validation

### `zen.fhir/profile-schema`

Schemas tagged with `zen.fhir/profile-schema` are used to validate resources that mention their `:zen.fhir/profileUri` in the `meta.profile` attribute

## Zen FHIR packages

Aidbox team has created an [open-source tool to generate Zen FHIR packages](https://github.com/zen-lang/fhir) from FHIR packages.

Generated packages are available under [zen-fhir Github organization](https://github.com/orgs/zen-fhir/repositories).

{% hint style="warning" %}
Zen FHIR Packages require `AIDBOX_CORRECT_AIDBOX_FORMAT=true` environment variable.
{% endhint %}

### Use Zen FHIR packages

We recommend to load Zen FHIR packages using an [Aidbox Configuration project](../../aidbox-configuration/aidbox-zen-lang-project/). Visit the following page for a detailed guide:

{% content-ref url="../../aidbox-configuration/aidbox-zen-lang-project/enable-igs.md" %}
[enable-igs.md](../../aidbox-configuration/aidbox-zen-lang-project/enable-igs.md)
{% endcontent-ref %}

There are also several other _deprecated_ ways to use Zen FHIR packages:

#### [npm FHIR R4](https://www.npmjs.com/browse/depended/@zen-lang/hl7-fhir-r4-core) and [FHIR STU 3](https://www.npmjs.com/browse/depended/@zen-lang/hl7-fhir-r3-core)

Install the required `npm` packages and set `AIDBOX_ZEN_PATHS` to a directory which _contains_ `node_modules` with them:

```
AIDBOX_ZEN_PATHS=path/to/parent-of-node_modules/
```

#### [Standalone Aidbox projects](https://github.com/zen-lang/fhir/releases/latest)

Set `AIDBOX_ZEN_PATHS` to download URLs of all the required packages. Multiple URLs are separated by a comma.

```
AIDBOX_ZEN_PATHS="url:zip:https://github.com/zen-lang/fhir/releases/latest/download/hl7-fhir-us-core.zip
                 ,url:zip:https://github.com/zen-lang/fhir/releases/latest/download/hl7-fhir-us-carin-bb.zip
                 ,url:zip:https://github.com/zen-lang/fhir/releases/latest/download/hl7-fhir-us-davinci-pdex.zip"
```

### Convert custom FHIR profiles to Zen FHIR package

Our [`zen-lang/fhir`](https://github.com/zen-lang/fhir/blob/main/README.md) tool allows you to generate zen schemas for custom FHIR profiles and use them in your [Aidbox Configuration projects](../../aidbox-configuration/aidbox-zen-lang-project/).&#x20;

### Create custom Zen FHIR package based on existing Zen FHIR packages

You can use existing Zen FHIR packages as a foundation for your custom Zen FHIR package. See our [guide on profiling with Zen-lang](extend-an-ig-with-a-custom-zen-profile.md).

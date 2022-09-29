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

Aidbox team created an open-source tool to generate Zen FHIR packages from FHIR packages

It is distributed as a jar file available here&#x20;

Zen FHIR  package is an [Aidbox project](../../aidbox-configuration/aidbox-zen-lang-project/) saved into zip archive

{% hint style="warning" %}
Zen FHIR Packages require `AIDBOX_CORRECT_AIDBOX_FORMAT` environment variable to be declared
{% endhint %}

### Use Zen FHIR packages

You can enable Zen FHIR packages that contain FHIR profiles of the following implementation guides:

* FHIR R4
  * `hl7-fhir-us-core` - US Core
  * `hl7-fhir-us-davinci-pdex` - Payer Data Exchange (PDex)
  * `hl7-fhir-us-davinci-pdex-plan-net` - PDEX Payer Network
  * `hl7-fhir-us-davinci-hrex` - The Da Vinci Payer Health Record exchange (HRex)
  * `hl7-fhir-us-davinci-drug-formulary` - DaVinci Payer Data Exchange US Drug Formulary
  * `hl7-fhir-us-carin-bb` - CARIN Consumer Directed Payer Data Exchange (CARIN IG for Blue Button®)
  * `hl7-fhir-us-mcode` - mCODE™ (short for Minimal Common Oncology Data Elements)
* FHIR STU 3
  * `nictiz-fhir-nl-stu3-zib2017` - Nictiz NL, including MedMij and HL7 NL

Zen FHIR packages are distributed as npm packages or as zen-lang standalone Aidbox projects

Existing Zen FHIR packages are available as:

* [npm FHIR R4](https://www.npmjs.com/browse/depended/@zen-lang/hl7-fhir-r4-core)&#x20;
* [npm FHIR STU 3](https://www.npmjs.com/browse/depended/@zen-lang/hl7-fhir-r3-core)
* [standalone Aidbox projects](https://github.com/zen-lang/fhir/releases/latest)

#### Example configuration

```makefile
AIDBOX_CORRECT_AIDBOX_FORMAT=true
AIDBOX_ZEN_PATHS="url:zip:https://github.com/zen-lang/fhir/releases/latest/download/hl7-fhir-us-core.zip
                 ,url:zip:https://github.com/zen-lang/fhir/releases/latest/download/hl7-fhir-us-carin-bb.zip
                 ,url:zip:https://github.com/zen-lang/fhir/releases/latest/download/hl7-fhir-us-davinci-pdex.zip
                 ,url:zip:https://github.com/zen-lang/fhir/releases/latest/download/hl7-fhir-us-davinci-drug-formulary.zip
                 ,url:zip:https://github.com/zen-lang/fhir/releases/latest/download/hl7-fhir-us-davinci-pdex-plan-net.zip"
AIDBOX_ZEN_LOAD="{ns project
                  import #{hl7-fhir-us-core
                           hl7-fhir-us-carin-bb
                           hl7-fhir-us-davinci-pdex
                           hl7-fhir-us-davinci-drug-formulary
                           hl7-fhir-us-davinci-pdex-plan-net}}"
AIDBOX_ZEN_ENTRYPOINT=project
```

### Convert custom FHIR profiles to Zen FHIR package

Using [this tool](https://github.com/zen-lang/fhir/blob/main/README.md) you can generate zen-lang schemas and use them in your [Aidbox projects](../../aidbox-configuration/aidbox-zen-lang-project/).

### Create custom Zen FHIR package based on existing Zen FHIR packages

You can use existing Zen FHIR packages as a foundation for your custom Zen FHIR package. Check out [Profiling with Zen-lang guide](extend-an-ig-with-a-custom-zen-profile.md).

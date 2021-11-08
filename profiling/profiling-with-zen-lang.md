---
description: Validate your resources with zen-lang schemas
---

# Profiling with zen-lang

[Zen-lang](https://github.com/zen-lang/zen) allows Aidbox to validate resources against schemas. It can validate individual properties as well as large profiles in a composable way

## Zen-lang schemas

For validation (e.g. in [FHIR CRUD API](../api-1/api/crud-1/)) Aidbox uses zen schemas tagged with `zenbox/base-schema` or `zenbox/profile-schema`

Schemas with these tags must have `:zenbox/type` and `:zenbox/profileUri` keys specified

### `zenbox/base-schema`

Schemas tagged with `zenbox/base-schema` are used to validate every resource of their type. When loaded into Aidbox such schema will be used instead of default json schema validation

### `zenbox/profile-schema`

Schemas tagged with `zenbox/profile-schema` are used to validate resources which mention their `:zenbox/profileUri` in the `meta.profile` attribute

## Zen-lang packages

Aidbox team created a tool to generate Zen-lang schemas from FHIR npm packages

The tool is open source, distributed as a jar file, it is available here [https://github.com/zen-lang/fhir](https://github.com/zen-lang/fhir)

Zen-lang package is an [Aidbox project](../aidbox-configuration/aidbox-zen-lang-project.md) saved into zip archive

You can get zen-lang packages in two ways:

#### Pregenerated Zen-lang packages

You can enable zen-lang packages of the following implementation guides:

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

Zen-lang packages are distributed as npm packages or as zen-lang standalone Aidbox projects

Pregenerated Zen-lang packages :

* [npm FHIR R4](https://www.npmjs.com/browse/depended/@zen-lang/hl7-fhir-r4-core)&#x20;
* [npm FHIR STU 3](https://www.npmjs.com/browse/depended/@zen-lang/hl7-fhir-r3-core)
* [standalone Aidbox projects](https://github.com/zen-lang/fhir/releases)

#### Convert custom FHIR profiles to Zen-lang package

Using this tool you can generate zen-lang schemas (see [zen-lang/fhir/README.md](https://github.com/zen-lang/fhir/blob/main/README.md))

#### Create custom Zen-lang package using existing Zen-lang packages

You can use existing Zen-lang packages as a foundation for your custom Zen-lang package (check [Profiling with Zen-lang guide](draft-profiling-with-zen-lang.md))



## Examples

You can see example defining base and profile schemas in the [🎓 Profiling with zen-lang](draft-profiling-with-zen-lang.md) tutorial.

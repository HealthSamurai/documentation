---
description: Validate your resources with zen-lang schemas
---

# Profiling with zen-lang

[Zen-lang](https://github.com/zen-lang/zen) allows Aidbox to validate resources against schemas. It can validate individual properties as well as large profiles in a composable way

## Zen schemas

For validation \(e.g. in [FHIR CRUD API](../api-1/api/crud-1/)\) Aidbox uses zen schemas tagged with `zenbox/base-schema` or `zenbox/profile-schema`

Schemas with these tags must have `:zenbox/type` and `:zenbox/profileUri` keys specified

### `zenbox/base-schema`

Schemas tagged with `zenbox/base-schema` are used to validate every resource of their type. When loaded into Aidbox such schema will be used instead of default json schema validation

### `zenbox/profile-schema`

Schemas tagged with `zenbox/profile-schema` are used to validate resources which mention their `:zenbox/profileUri` in the `meta.profile` attribute

## Examples

You can see example defining base and profile schemas in the [🎓 Profiling with zen-lang](draft-profiling-with-zen-lang.md) tutorial.


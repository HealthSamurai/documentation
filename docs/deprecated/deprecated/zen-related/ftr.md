# FTR

> \[!WARNING]\
> ⚠️ Since the 2405 release, using Aidbox in FHIR schema validation engine is recommended,\
> which is incompatible with zen or Entity/Attribute options.[Setup Aidbox with FHIR Schema validation engine](broken-reference/)

{% content-ref url="fhir-terminology-repository/" %}
[fhir-terminology-repository](fhir-terminology-repository/)
{% endcontent-ref %}

## `BOX_FEATURES_FTR_PULL_ENABLE`

Either `true` or `false`. By default, `false`.

It tells Aidbox whether or not it should load [Concepts](../forms/terminology/concept/), [ValueSets](../forms/terminology/valueset/) and [CodeSystems](../forms/terminology/codesystem-and-concept/) into the database. Note that loading into the database is needed for [concept lookups](broken-reference/) and [value set expansions](broken-reference/) as well as for accessing `GET /Concept`, `GET /ValueSet`, `GET /CodeSystem` endpoints but not for validation.

## `BOX_FEATURES_FTR_PULL_SYNC`

Either `true` or `false`. By default, `false`.

Specifies whether or not Aidbox startup should be blocked by loading [Concepts](../forms/terminology/concept/), [ValueSets](../forms/terminology/valueset/) and [CodeSystems](../forms/terminology/codesystem-and-concept/) into the database.

Used when `BOX_FEATURES_FTR_PULL_ENABLE=true`.

## `BOX_FEATURES_FTR_BUILD__INDEX__ON__STARTUP_ENABLE`

Either `true` or `false`. By default, `true`.

Specifies whether or not Aidbox should build in-memory index for concepts validation.

## `BOX_FEATURES_FTR_BUILD__INDEX__ON__STARTUP_SYNC`

Either `true` or `false`. By default, `false`.

Specifies whether or not Aidbox should be blocked by building in-memory index for concepts validation.

Used when `BOX_FEATURES_FTR_BUILD__INDEX__ON__STARTUP_ENABLE=true`.

## `BOX_FEATURES_FTR_INCREMENTAL__INDEX__UPDATES_ENABLE`

Either `true` or `false`. By default, `true`.

Whether to perform incremental FTR index updates or not can be beneficial in scenarios where `build-index-on-startup` is disabled or index already built on startup and later Aidbox doesn't have access to the network.\
For each binding validation attempt, it checks if the ValueSet is present in the index. If not, it resolves the ValueSet using the FTR manifest and adds it to the local index.

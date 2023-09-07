# FTR

{% content-ref url="broken-reference" %}
[Broken link](broken-reference)
{% endcontent-ref %}

## `BOX_FEATURES_FTR_PULL_ENABLE`

Either `true` or `false`. By default, `false`.

It tells Aidbox whether or not it should load [Concepts](broken-reference), [ValueSets](broken-reference) and [CodeSystems](broken-reference) into the database. Note that loading into the database is needed for [concept lookups](broken-reference) and [value set expansions](broken-reference) as well as for accessing `GET /Concept`, `GET /ValueSet`, `GET /CodeSystem` endpoints but not for validation.

## `BOX_FEATURES_FTR_PULL_SYNC`

Either `true` or `false`. By default, `false`.

Specifies whether or not Aidbox startup should be blocked by loading [Concepts](broken-reference), [ValueSets](broken-reference) and [CodeSystems](broken-reference) into the database.

Used when `BOX_FEATURES_FTR_PULL_ENABLE=true`.

## `BOX_FEATURES_FTR_BUILD__INDEX__ON__STARTUP_ENABLE`

Either `true` or `false`. By default, `true`.

Specifies whether or not Aidbox should build in-memory index for concepts validation.

## `BOX_FEATURES_FTR_BUILD__INDEX__ON__STARTUP_SYNC`

Either `true` or `false`. By default, `false`.

Specifies whether or not Aidbox should be blocked by building in-memory index for concepts validation.

Used when `BOX_FEATURES_FTR_BUILD__INDEX__ON__STARTUP_ENABLE=true`.

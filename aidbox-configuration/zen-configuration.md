---
description: Configure Aidbox using environment variables and Zen
---

# Zen Configuration

{% hint style="warning" %}
Since the 2405 release, using Aidbox in FHIRSchema mode is recommended, which is incompatible with zen or Entity/Attribute options.

[setup.md](../modules-1/profiling-and-validation/fhir-schema-validator/setup.md "mention")
{% endhint %}

## Find configuration options

`aidbox/config` schema lists available Zen configuration options with descriptions

Open _Profiles_ in Aidbox UI. Navigate to `aidbox/config` schema.

![](<../.gitbook/assets/image (102) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (2) (2) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (3) (1).png>)

## Set configuration option

To set a configuration option set a corresponding environment variable prefixed with `box_`. Path elements are separated with underscore (`_`), dashes (`-`) are replaced with double underscore (`__`)

### Example

To set the `rest-search` value of the `features.graphql.access-control` option use the `box_features_graphql_access__control` environment variable:

```
box_features_graphql_access__control=rest-search
```

## Enable Zen dev mode

Aidbox exits when encounters major Zen schema errors.

You can disable this behavior by enabling Zen dev mode:

```
AIDBOX_ZEN_DEV_MODE=true
```

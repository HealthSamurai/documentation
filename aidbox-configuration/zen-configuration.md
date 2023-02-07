---
description: Configure Aidbox using environment variables and Zen
---

# Zen Configuration

{% hint style="warning" %}
This feature is in alpha. It may change in backward-incompatible way in the future.
{% endhint %}

## Find configuration options

`aidbox/config` schema lists available Zen configuration options with descriptions

Open _Profiles_ in Aidbox UI. Navigate to `aidbox/config` schema.&#x20;

![](<../.gitbook/assets/image (102) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (2) (2) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (2) (1).png>)

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

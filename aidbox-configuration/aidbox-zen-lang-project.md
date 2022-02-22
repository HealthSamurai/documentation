---
description: Use zen-lang to configure Aidbox
---

# Aidbox project

## Aidbox project

To start configuring Aidbox with [zen-lang](https://github.com/zen-lang/zen) you need to create an Aidbox project.

Aidbox project is a directory with zen-lang edn files. Aidbox project can be shared with many Aidbox instances giving them the same configuration.

### Load project using environment variables

Aidbox uses environment variables to load project.

#### `AIDBOX_ZEN_PATHS`

The `AIDBOX_ZEN_PATHS` environment variable is used to specify how Aidbox loads project.

**Format**:

```
AIDBOX_ZEN_PATHS=<source>:<format>:<path>[,<source>:<format>:<path>]*
```

Source is either `url` or `path`. `url` is used to download Aidbox project from the remote location; `path` is used to load Aidbox project from the filesystem.

Format can be `zip`, `dir` or `edn`. Note that `dir` is only applicable to the `path` source.

Table of source and format compatibility:

| source\format | `zip` | `dir` | `edn` |
| ------------- | ----- | ----- | ----- |
| `url`         | ✓     |       | ✓     |
| `path`        | ✓     | ✓     | ✓     |

Example:

```
AIDBOX_ZEN_PATHS=AIDBOX_ZEN_PATHS=url:zip:https://github.com/zen-lang/fhir/releases/download/0.2.13-1/hl7-fhir-us-core.zip
```

#### `AIDBOX_ZEN_LOAD`

The `AIDBOX_ZEN_LOAD` environment variable is used to load a single namespace represented as [edn](https://github.com/edn-format/edn).

**Format:**

```
AIDBOX_ZEN_LOAD=<zen-edn>
```

**Example:**

```
AIDBOX_ZEN_LOAD='{ns my-zen-namespace import #{zen-proj1 zen-proj2 zen-proj3}}'
```

#### `AIDBOX_ZEN_ENTRYPOINT`

The `AIDBOX_ZEN_ENTRYPOINT` environment variable specifies a zen namespace or a zen symbol. Aidbox starts reading its configuration from the entry point.

**Format:**

Using a zen namespace as an entry point:

```
AIDBOX_ZEN_ENTRYPOINT=zen.namespace
```

Using a zen symbol as an entry point:

```
AIDBOX_ZEN_ENTRYPOINt=zen.namespace/zen-symbol
```

**Examples:**

```
AIDBOX_ZEN_ENTRYPOINT=aidbox-project1/dev-server
AIDBOX_ZEN_ENTRYPOINT=aidbox-project2
```

#### `AIDBOX_ZEN_PROJECT`

{% hint style="warning" %}
This environment variable will be merged with the `AIDBOX_ZEN_PATHS`.
{% endhint %}

The `AIDBOX_ZEN_PROJECT` environment variable is used to load Aidbox project from the path specified

**Format:**

```
AIDBOX_ZEN_PROJECT=<path>
```

#### `AIDBOX_ZEN_DEV_MODE`

The `AIDBOX_ZEN_DEV_MODE` environment variable is used to enable the hot reloading of the aidbox project.

Format: if the variable is defined in the environment then Zen dev mode is enabled

## Examples

You can see example using the Aidbox project and hot reload in the [🎓 Profiling with zen-lang](../profiling-and-validation/profiling-with-zen-lang/draft-profiling-with-zen-lang.md) tutorial.

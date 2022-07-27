# Aidbox project environment variables

#### `AIDBOX_ZEN_PATHS`

#### `AIDBOX_ZEN_ENTRYPOINT`

#### `AIDBOX_ZEN_LOAD`

The `AIDBOX_ZEN_LOAD` environment variable is used to load a single namespace represented as [edn](https://github.com/edn-format/edn).

**Format:**

```
AIDBOX_ZEN_LOAD=<zen-edn>
```

**Example:**

```
AIDBOX_ZEN_LOAD='{ns my-zen-namespace import #{zen-proj1 zen-proj2 zen-proj3
```

#### `AIDBOX_ZEN_DEV_MODE`

The `AIDBOX_ZEN_DEV_MODE` environment variable is used to enable the hot reloading of the aidbox project.

Format: if the variable is defined in the environment then Zen dev mode is enabled.

{% hint style="info" %}
Hot reloading works only with directories and files in local file system. I.e. `path:dir` and `path:edn` in `AIDBOX_ZEN_PATHS`.
{% endhint %}

`AIDBOX_ZEN_ENTRY`, `AIDBOX_ZEN_PROJECT`, `BOX_ENTRYPOINT` are deprecated.

#### `AIDBOX_ZEN_PROJECT`, `AIDBOX_ZEN_ENTRY`, `BOX_ENTRYPOINT`

{% hint style="warning" %}
Deprecated. Use `AIDBOX_ZEN_PATHS` and `AIDBOX_ZEN_ENTRYPOINT` instead.
{% endhint %}

`AIDBOX_ZEN_PROJECT` environment variable is used to load the Aidbox project from the path specified.

`AIDBOX_ZEN_ENTRY` environment variable is used to specify zen project entry namespace.

`BOX_ENTRYPOINT` environment variable is used to specify zen entry symbol.12

# Aidbox Configuration project

## `AIDBOX_ZEN_PATHS`

Syntax:

```
AIDBOX_ZEN_PATHS=<source>:<format>:<path>[,<source>:<format>:<path>]*
```

`<source>` is either a `url` or a `path`.

* `url` is used to load a project from a remote location
* `path` is used to load a project from a local location

`<format>` is either `zip`, `dir` or `edn`.

Table of compatibility between sources and formats:

<table data-header-hidden><thead><tr><th></th><th width="178"></th><th></th><th></th></tr></thead><tbody><tr><td>source\format</td><td><code>zip</code></td><td><code>dir</code></td><td><code>edn</code></td></tr><tr><td><code>url</code></td><td>✓</td><td></td><td>✓</td></tr><tr><td><code>path</code></td><td>✓</td><td>✓</td><td>✓</td></tr></tbody></table>

{% hint style="warning" %}
Though it is possible to load multiple projects with `AIDBOX_ZEN_PATHS`, it is not recommended as it may lead to namespace clashes and hard-to-debug issues.
{% endhint %}

### Load from a remote source

You can load Aidbox Configuration project from a public HTTPS endpoint.

For example, set

```yaml
AIDBOX_ZEN_PATHS=url:zip:https://github.com/zen-lang/fhir/releases/latest/download/hl7.fhir.r4.core.zip
```

to load FHIR R4 profiles.

Aidbox can load a single file too:

```yaml
AIDBOX_ZEN_PATHS=url:edn:https://example.org/my-namespace.edn
```

### Load from a local filesystem source&#x20;

You can use any of the three options: directory, a zip file or an `edn` file.

For example, set

```
AIDBOX_ZEN_PATHS=path:dir:/srv/aidbox-project
```

to load a project from a directory located at `/srv/aidbox-project`.

## `AIDBOX_ZEN_ENTRYPOINT`

## `AIDBOX_ZEN_LOAD`

The `AIDBOX_ZEN_LOAD` environment variable is used to load a single namespace represented as [edn](https://github.com/edn-format/edn).

**Format:**

```
AIDBOX_ZEN_LOAD=<zen-edn>
```

**Example:**

```
AIDBOX_ZEN_LOAD='{ns my-zen-namespace import #{zen-proj1 zen-proj2 zen-proj3
```

## `AIDBOX_ZEN_DEV_MODE`

The `AIDBOX_ZEN_DEV_MODE` environment variable is used to enable the hot reloading of the aidbox project.

Format: if the variable is defined in the environment then Zen dev mode is enabled.

{% hint style="info" %}
Hot reloading works only with directories and files in local file system. I.e. `path:dir` and `path:edn` in `AIDBOX_ZEN_PATHS`.
{% endhint %}

`AIDBOX_ZEN_ENTRY`, `AIDBOX_ZEN_PROJECT`, `BOX_ENTRYPOINT` are deprecated.

## `AIDBOX_ZEN_PROJECT`, `AIDBOX_ZEN_ENTRY`, `BOX_ENTRYPOINT`

{% hint style="warning" %}
Deprecated. Use `AIDBOX_ZEN_PATHS` and `AIDBOX_ZEN_ENTRYPOINT` instead.
{% endhint %}

`AIDBOX_ZEN_PROJECT` environment variable is used to load the Aidbox project from the path specified.

`AIDBOX_ZEN_ENTRY` environment variable is used to specify zen project entry namespace.

`BOX_ENTRYPOINT` environment variable is used to specify zen entry symbol.12

## `BOX_PROJECT_GIT_SUB__PATH`

With the use of `BOX_PROJECT_GIT_SUB__PATH`, it is possible to check out only a specific directory from a repository. The value of the variable should be set to a path starting with a repository name.&#x20;

For example, suppose you have repository `xyz` with the following organization:

```
/a
  b/
/c
```

If you now set `BOX_PROJECT_GIT_SUB__PATH=xyz/a/b`, then only folder `b` is going to be cloned.

---
description: Use zen-lang to configure Aidbox
---

# Aidbox project

To start configuring Aidbox with [zen-lang](https://github.com/zen-lang/zen) you need to create an Aidbox project.

Aidbox project is a directory with zen-lang edn files. Aidbox project can be shared with many Aidbox instances giving them the same configuration.

## Load Aidbox project

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
AIDBOX_ZEN_PATHS=url:zip:https://github.com/zen-lang/fhir/releases/download/0.2.13-1/hl7-fhir-us-core.zip
```

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

#### `AIDBOX_ZEN_ENTRYPOINT`

The `AIDBOX_ZEN_ENTRYPOINT` environment variable specifies a zen namespace or a zen symbol. Aidbox starts reading its configuration from the entrypoint.

**Format:**

Using a zen namespace as an entrypoint:

```
AIDBOX_ZEN_ENTRYPOINT=zen.namespace
```

Using a zen symbol as an entrypoint:

```
AIDBOX_ZEN_ENTRYPOINT=zen.namespace/zen-symbol
```

**Examples:**

```
AIDBOX_ZEN_ENTRYPOINT=aidbox-project1/dev-server
AIDBOX_ZEN_ENTRYPOINT=aidbox-project2
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

`BOX_ENTRYPOINT` environment variable is used to specify zen entry symbol.

### Load project from git repository

#### Environment variables

| Env variable name               | Meaning                                                                                |
| ------------------------------- | -------------------------------------------------------------------------------------- |
| `BOX_PROJECT_GIT_PROTOCOL`      | Possible values: "https", "ssh"                                                        |
| `BOX_PROJECT_GIT_URL`           | GIt repository URL (https://github.com/\<user>/\<repo>.git)                            |
| `BOX_PROJECT_GIT_SUB__PATH`     | If want to place edn files inside another directory, you can provide this env variable |
| `BOX_PROJECT_GIT_ACCESS__TOKEN` | Access token can be used for accessing private repository                              |
| `BOX_PROJECT_GIT_TARGET__PATH`  | Override target clone directory. By default Aidbox will use /tmp/aidbox-project-git.   |
| `BOX_PROJECT_GIT_CHECKOUT`      | Checkout into branch or commit                                                         |
| `BOX_PROJECT_GIT_PUBLIC_KEY`    | Public ssh key                                                                         |
| `BOX_PROJECT_GIT_PRIVATE_KEY`   | Private ssh key                                                                        |

#### Example configuration

SSH example

```
AIDBOX_ZEN_ENTRYPOINT=smartbox.portal/box
BOX_PROJECT_GIT_PROTOCOL=ssh
BOX_PROJECT_GIT_PUBLIC__KEY="...."
BOX_PROJECT_GIT_PRIVATE__KEY="-----BEGIN OPENSSH PRIVATE KEY-----\n....\n-----END OPENSSH PRIVATE KEY-----\n"
BOX_PROJECT_GIT_URL=git@github.com:Aidbox/aidbox-project-samples.git
BOX_PROJECT_GIT_SUB__PATH=aidbox-project-samples
```

#### HTTPS example

```
AIDBOX_ZEN_ENTRYPOINT=smartbox.portal/box
BOX_PROJECT_GIT_PROTOCOL=https
BOX_PROJECT_GIT_URL=https://github.com/Aidbox/aidbox-project-samples.git
BOX_PROJECT_GIT_SUB__PATH=aidbox-project-samples
```

#### Startup errors

**Wrong zen source files path**

{% hint style="danger" %}
Entrypoint 'smartbox.portal' not loaded.

{:message "No file for ns 'smartbox.portal", :missing-ns smartbox.portal, :ns smartbox.portal}
{% endhint %}

**Incorrect git repo url**

{% hint style="danger" %}
Cloning into '/tmp/aidbox-project-git'...

remote: Repository not found.

fatal: repository 'https://github.com/Aidfdfdfbox/aidbox-project-samples.git/' not found
{% endhint %}

**Incorrect checkout branch/commit**

{% hint style="danger" %}
error: pathspec 'git-project-unexist' did not match any file(s) known to git
{% endhint %}

### Loading dependencies

#### `AIDBOX_ZEN_PATHS`

If the `node_modules` directory is present in a location from `AIDBOX_ZEN_PATHS` and it's populated with zen packages they will be loaded as dependencies. Thus for `zip` and `dir` paths if you have `package.json` file with dependencies you need to do `npm install` to ensure that `node_modules` directory is populated when Aidbox loads this path.

#### Project from git repository

For a git repository project you need to create `package.json` file in the root of the repository (or inside `BOX_PROJECT_GIT_SUB__PATH` if you're using it) and commit it. Aidbox will install specified dependencies while pulling project from git.

## Examples

You can see an example in the [🎓 Profiling with zen-lang](../../profiling-and-validation/profiling-with-zen-lang/extend-an-ig-with-a-custom-zen-profile.md) tutorial.

### Example of the `package.json` content

```json
{
  "dependencies": {
    "@zen-lang/hl7-fhir-r4-core": "^0.5.11",
    ...
    }
}
```

## Configure Aidbox Project via `aidbox/system`

{% hint style="info" %}
This section is still under active development check it out later 🚧
{% endhint %}

## Seed Import

You can declare a set of resources in Aidbox project and get them loaded in one or many Aidboxes on start. To do this you need to describe the seed service in the system entrypoint.

### Example

```clojure
{ns     importbox
 import #{aidbox
          zenbox}

 seed
 {:zen/tags  #{aidbox/service}
  :engine    aidbox/seed
  :files     ["patients.ndjson.gz"]
  :resources [{:id "rpt-1" :resourceType "Patient"}
              {:id "rpt-2" :resourceType "Patient"}]
  :migrations [{:id "mig-1" :sql "create table mytable (id text)"}
               {:id "mig-2" :sql "insert into mytable values ('hello')"}]}

 importbox
 {:zen/tags #{aidbox/system}
  :zen/desc "Import box for test"
  :services {:seed seed}}}
```

In this example `importbox/importbox` is the system entrypoint defined in the **`AIDBOX_ZEN_ENTRYPOINT`** variable, the files described in the `:files` field are located inside the zen project defined in the variable **`AIDBOX_ZEN_PATHS`**.

### Seed Service

`:files` - which `ndjson.gz` files will be imported at system startup. These files must be located inside the zen project described in the variable **`AIDBOX_ZEN_PATHS`**.

`:resources` - in-place resources definitions that will be imported at system startup.

`:migrations` — vector of migrations. Each migration is a map containing `id` and `sql` keys.

{% hint style="warning" %}
`:resources` are imported sequentially, so make sure you don't break referential integrity.
{% endhint %}
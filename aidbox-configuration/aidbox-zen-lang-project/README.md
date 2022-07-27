---
description: Use zen-lang to configure Aidbox
---

# Aidbox project

To start configuring Aidbox with [zen-lang](https://github.com/zen-lang/zen) you need to create an Aidbox project.

Aidbox project is a directory with zen-lang edn files. Aidbox project can be shared with many Aidbox instances giving them the same configuration.

## Load Aidbox project

Aidbox project can be loaded in two ways: stored in git repository (for example Github) or using environment variables (by url from some website or using path to local files).

### Load project using environment variables

Aidbox uses environment variables to load project. Requared are `AIDBOX_ZEN_PATHS` and `AIDBOX_ZEN_ENTRYPOINT`

#### `AIDBOX_ZEN_PATHS`

The `AIDBOX_ZEN_PATHS` environment variable is used to specify which files Aidbox needs to loads project.

**Format**:

```
AIDBOX_ZEN_PATHS=<source>:<format>:<path>[,<source>:<format>:<path>]*
```

`<source>` is either **`url`** or **`path`**. **`url`** is used to download Aidbox project from the remote location; **`path`** is used to load Aidbox project from the filesystem.

\<format> parameter can be **`zip`**, **`dir`** or [**`edn`**](https://github.com/edn-format/edn). Note that **`dir`** is only applicable to the **`path`** source.

Table of sources and format compatibility:

| source\format | `zip` | `dir` | `edn` |
| ------------- | ----- | ----- | ----- |
| `url`         | ✓     |       | ✓     |
| `path`        | ✓     | ✓     | ✓     |

Example, downloading zip file from url:

```yaml
AIDBOX_ZEN_PATHS=url:zip:https://github.com/zen-lang/fhir/releases/download/0.2.13-1/hl7-fhir-us-core.zip
```

Getting files from local directory:

```
AIDBOX_ZEN_PATHS=path:dir:/home/user/dir_edn_files
```

Get one .edn file remotely and another one from local system:

```
AIDBOX_ZEN_PATHS=path:edn:/home/user/dir_edn_files/main.edn12
                 ,url:edn:https://edn-website/edn-file.edn
```

#### `AIDBOX_ZEN_ENTRYPOINT`

The `AIDBOX_ZEN_ENTRYPOINT` environment variable require a zen namespace or a zen symbol. Aidbox starts reading its configuration from the entrypoint.

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

HTTPS example

```
AIDBOX_ZEN_ENTRYPOINT=smartbox.portal/box
BOX_PROJECT_GIT_PROTOCOL=https
BOX_PROJECT_GIT_URL=https://github.com/Aidbox/aidbox-project-samples.git
BOX_PROJECT_GIT_SUB__PATH=aidbox-project-samples
```

#### Startup errors

{% hint style="danger" %}
Entrypoint 'smartbox.portal' not loaded.

{:message "No file for ns 'smartbox.portal", :missing-ns smartbox.portal, :ns smartbox.portal}
{% endhint %}

Meaning: Wrong zen source files path

****

{% hint style="danger" %}
Cloning into '/tmp/aidbox-project-git'...

remote: Repository not found.

fatal: repository 'https://github.com/Aidfdfdfbox/aidbox-project-samples.git/' not found
{% endhint %}

Meaning: Incorrect git repo url

****

{% hint style="danger" %}
error: pathspec 'git-project-unexist' did not match any file(s) known to git
{% endhint %}

Meaning: Incorrect checkout branch/commit

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


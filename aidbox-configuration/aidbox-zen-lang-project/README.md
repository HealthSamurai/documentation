---
description: Use zen-lang to configure Aidbox
---

# Aidbox project

Aidbox project is a collection of [zen](https://github.com/zen-lang/zen) namespaces which configure Aidbox.

You can use Aidbox project to set up API constructor, load terminologies, profiles, and more.

To use a project you need to

* specify how to load Aidbox project,
* set zen entrypoint.

## Load Aidbox project

Aidbox project can be splitted into multiple parts. For example, you can load IGs from a remote location, load API constructor configuration from git repository, and load seed resources from a local directory.

Aidbox can load project parts from

* git repository,
* remote location,
* local location,
* npm.

Each part can depend on part distributed via npm. And vice versa, the only way to load npm part is to specify it as a dependency.

Local and remote locations are specified using `AIDBOX_ZEN_PATHS` environment variable.

### AIDBOX\_ZEN\_PATHS

Syntax:

```
AIDBOX_ZEN_PATHS=<source>:<format>:<path>[,<source>:<format>:<path>]*
```

`<source>` is either `url`, or `path`.

* `url` is used to load project from remote location
* `path` is used to load project from local location

`<format>` is either `zip`, or `dir`, or `edn`.

Table of sources and format compatibility:

| source\format | `zip` | `dir` | `edn` |
| ------------- | ----- | ----- | ----- |
| `url`         | ✓     |       | ✓     |
| `path`        | ✓     | ✓     | ✓     |

### Load remote part

You can load Aidbox project part from a public HTTPS endpoint.

For example, set

```yaml
AIDBOX_ZEN_PATHS=url:zip:https://github.com/zen-lang/fhir/releases/latest/download/hl7.fhir.r4.core.zip
```

to load FHIR R4 profile part.

Aidbox can load a single file too:

```yaml
AIDBOX_ZEN_PATHS=url:edn:https://example.org/my-namespace.edn
```

### Load local part

You have three options: directory, zip file, and a single file.

For example, let's load a part from a directory

```
AIDBOX_ZEN_PATHS=path:dir:/srv/aidbox-project
```

### Load git repository

Aidbox can load repository using either https or ssh. You can optionally specify a branch or commit, path of a project part inside repository, and location in which to clone repository.

To set up git repository location you need to set multiple environment variables:

* `BOX_PROJECT_GIT_PROTOCOL`: `https` for HTTPS, `ssh` for SSH method.
* `BOX_PROJECT_GIT_URL`: full URL to git repository.

Then you need to specify authentication.&#x20;

For HTTPS:

* `BOX_PROJECT_GIT_ACCESS__TOKEN`: access token for private repositories.&#x20;

For SSH:

* `BOX_PROJECT_GIT_PUBLIC__KEY`: public SSH key,
* `BOX_PROJECT_GIT_PRIVATE__KEY`: private SSH key.

Additionally, you can control clone and checkout:

* `BOX_PROJECT_GIT_CHECKOUT`: checkout specific commit or branch
* `BOX_PROJECT_GIT_TARGET__PATH`: where to clone repository. Default is `/tmp/aidbox-project-git`.

If your Aidbox project part is in subdirectory of the repository, you can specify its location relative to the repository root with `BOX_PROJECT_GIT_SUB__PATH` environment variable.

For example, let's load one of the Aidbox project samples using HTTPS and SSH methods.&#x20;

SSH:

```
AIDBOX_ZEN_ENTRYPOINT=smartbox.portal/box
BOX_PROJECT_GIT_PROTOCOL=ssh
BOX_PROJECT_GIT_PUBLIC__KEY="...."
BOX_PROJECT_GIT_PRIVATE__KEY="-----BEGIN OPENSSH PRIVATE KEY-----\n....\n-----END OPENSSH PRIVATE KEY-----\n"
BOX_PROJECT_GIT_URL=git@github.com:Aidbox/aidbox-project-samples.git
BOX_PROJECT_GIT_SUB__PATH=aidbox-project-samples
```

HTTPS:

```
AIDBOX_ZEN_ENTRYPOINT=smartbox.portal/box
BOX_PROJECT_GIT_PROTOCOL=https
BOX_PROJECT_GIT_URL=https://github.com/Aidbox/aidbox-project-samples.git
BOX_PROJECT_GIT_SUB__PATH=aidbox-project-samples
```

### Aidbox project part dependencies

Aidbox project part can have dependencies. They are designed to be distributed via npm. Aidbox loads every directory in

```
node_modules/\@*/
```

as an Aidbox project part.

For example you can set up US Core profiles using local Aidbox project part with dependencies.

Create a directory for Aidbox project part&#x20;

```
mkdir -p /srv/aidbox-project
```

Go to the Aidbox project part directory

```
cd /srv/aidbox-project
```

Create a `package.json` file

```
{
  "dependencies": {
    "@zen-lang/hl7-fhir-r4-core": "^0.5.11"
  }
}
```

Install dependencies

```
npm install
```

Load Aidbox project part:

```
AIDBOX_ZEN_PATHS=path:dir:/srv/aidbox-project
```

**Note**: _Aidbox automatically installs dependencies when using git repository part. For local and remote location parts you need to install them manually._

### Load multiple Aidbox project parts

Aidbox can load only one git project part and any number of parts using remote location, local location, and dependencies.

To specify multiple locations, split them with comma in `AIDBOX_ZEN_PATHS` environment variable. E.g.

```
AIDBOX_ZEN_PATHS=path:edn:/home/user/dir_edn_files/main.edn12
                 ,url:edn:https://edn-website/edn-file.edn
```

## Set Aidbox zen entrypoint

Aidbox starts reading configuration from the zen entrypoint. The entrypoint is either namespaced symbol or namespace. The `AIDBOX_ZEN_ENTRYPOINT` environment variable specifies the zen entrypoint.

We recommend using namespaced symbol because some functionality depends on entrypoint symbol. For example, `:services` for API constructor.

Using a zen symbol as an entrypoint:

Example:

```
AIDBOX_ZEN_ENTRYPOINT=zen.namespace/zen-symbol
```

## Examples

You can see an example in the [Profiling with zen-lang](../../profiling-and-validation/profiling-with-zen-lang/extend-an-ig-with-a-custom-zen-profile.md) tutorial.

## Common errors

```
Entrypoint 'smartbox.portal' not loaded.

{:message "No file for ns 'smartbox.portal", :missing-ns smartbox.portal, :ns smartbox.portal}
```

Meaning: Wrong zen source files path



```
Cloning into '/tmp/aidbox-project-git'...

remote: Repository not found.

fatal: repository 'https://github.com/Aidfdfdfbox/aidbox-project-samples.git/' not found
```

Meaning: Incorrect git repo url



```
error: pathspec 'git-project-unexist' did not match any file(s) known to git
```

Meaning: Incorrect checkout branch/commit

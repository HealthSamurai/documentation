# Setting up a configuration project

To set up a configuration project you need two things:&#x20;

* [Set entrypoint symbol](setting-up-a-configuration-project.md#set-entrypoint-for-system-configuration). Entrypoint points to settings and services for this instance.
* [Provide load configuration](setting-up-a-configuration-project.md#provide-aidbox-configuration-project-from-a-git-repo). This allows Aidbox to load your project on start.

## Set system entrypoint&#x20;

Aidbox starts reading configuration from a Zen entrypoint. The entrypoint is a namespaced symbol which serves as a starting point for the whole system. The `AIDBOX_ZEN_ENTRYPOINT` environment variable is used to specify it.

Namespace is related to a system config filename. The entrypoint symbol you use should be tagged with `aidbox/system`.

If your system config file looks like this

{% code title="project/zrc/system.edn" %}
```clojure
{ns     system
 import #{…}

 box
 {:zen/tags #{aidbox/system}
  …}
 
 …}
```
{% endcode %}

you should specify the following environment variable

```
AIDBOX_ZEN_ENTRYPOINT=system/box
```

{% hint style="info" %}
Besides system environment variables for setting up aidbox configuration project, there are many other envs Aidbox respects as part of backward compatibility. Still they are considered as legacy.&#x20;

It's more preferable to set up Aidbox with [aidbox.config/config](../../reference/configuration/aidbox-project/aidbox.config-config.md). You may provide it with `aidbox/system`

{% code title="project/zrc/system.edn" overflow="wrap" %}
```clojure
{ns system
 import #{aidbox aidbox.config}

 box-config
 {:zen/tags #{aidbox.config/config}
  ,,,}

 box
 {:zen/tags #{aidbox/system}
  :config box-config
  ,,,}}
```
{% endcode %}

This is more preferable way, than configuring Aidbox via envs.
{% endhint %}

## Provide Aidbox configuration project from a git repo

Aidbox container needs to know where Aidbox configuration project is located. For that you set `BOX_PROJECT_GIT_URL` environment variable to a git repository URL or a local directory path.

#### Load git repository

Aidbox can load repository using either https or ssh. You can optionally specify a branch or commit, path of a project part inside repository, and location in which to clone repository.

To set up git repository location you need to set multiple environment variables:

* `BOX_PROJECT_GIT_PROTOCOL`: `https` for HTTPS, `ssh` for SSH method.
* `BOX_PROJECT_GIT_URL`: full URL to git repository. This can be HTTPS, SSH or local path.

### Authentication

Authentication set up depends on the protocol used:&#x20;

For HTTPS:

* `BOX_PROJECT_GIT_ACCESS__TOKEN`: access token for private repositories.&#x20;

For SSH:

* `BOX_PROJECT_GIT_PUBLIC__KEY`: public SSH key,
* `BOX_PROJECT_GIT_PRIVATE__KEY`: private SSH key.

### Specify branch and paths

Additionally, you can control clone and checkout:

* `BOX_PROJECT_GIT_CHECKOUT`: checkout specific commit or branch
* `BOX_PROJECT_GIT_TARGET__PATH`: where to clone repository. Default is `/tmp/aidbox-project-git`.

If your Aidbox configuration project is in subdirectory of the repository, you can specify its location relative to the repository root with `BOX_PROJECT_GIT_SUB__PATH` environment variable.

For example, let's load one of the [Aidbox configuration project samples](https://github.com/Aidbox/aidbox-project-samples/tree/main/aidbox-project-samples) using HTTPS and SSH methods.&#x20;

{% code title="SSH" %}
```bash
AIDBOX_ZEN_ENTRYPOINT=smartbox.portal/box
BOX_PROJECT_GIT_PROTOCOL=ssh
BOX_PROJECT_GIT_PUBLIC__KEY="...."
BOX_PROJECT_GIT_PRIVATE__KEY="-----BEGIN OPENSSH PRIVATE KEY-----\n....\n-----END OPENSSH PRIVATE KEY-----\n"
BOX_PROJECT_GIT_URL=git@github.com:Aidbox/aidbox-project-samples.git
BOX_PROJECT_GIT_SUB__PATH=aidbox-project-samples/smartbox
```
{% endcode %}

{% code title="HTTPS" %}
```bash
AIDBOX_ZEN_ENTRYPOINT=smartbox.portal/box
BOX_PROJECT_GIT_PROTOCOL=https
BOX_PROJECT_GIT_URL=https://github.com/Aidbox/aidbox-project-samples.git
BOX_PROJECT_GIT_SUB__PATH=aidbox-project-samples/smartbox
```
{% endcode %}

## Alternative ways to provide Aidbox configuration project

Sometimes your production environment can not access an external git repository. In such cases you can configure Aidbox to load project via:

* zip archive url
* mounted file system path

Local and remote locations are specified using `AIDBOX_ZEN_PATHS` environment variable.

#### AIDBOX\_ZEN\_PATHS

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

#### Load from remote source

You can load Aidbox configuration project from a public HTTPS endpoint.

For example, set

```yaml
AIDBOX_ZEN_PATHS=url:zip:https://github.com/zen-lang/fhir/releases/latest/download/hl7.fhir.r4.core.zip
```

to load FHIR R4 profiles.

Aidbox can load a single file too:

```yaml
AIDBOX_ZEN_PATHS=url:edn:https://example.org/my-namespace.edn
```

#### Load from local filesystem source&#x20;

You have three options: directory, zip file, and a single file.

For example, let's load a project from a directory

```
AIDBOX_ZEN_PATHS=path:dir:/srv/aidbox-project
```

##

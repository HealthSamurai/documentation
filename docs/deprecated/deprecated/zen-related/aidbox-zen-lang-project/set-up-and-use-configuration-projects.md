# Set up and use configuration projects

{% hint style="warning" %}
Since the 2405 release, using Aidbox in FHIR schema validation engine is recommended, which is incompatible with zen or Entity/Attribute options.

[Setup Aidbox with FHIR Schema validation engine](https://docs.aidbox.app/modules-1/profiling-and-validation/fhir-schema-validator/setup)
{% endhint %}

Aidbox Configuration project requires a few things:

* [Project folder](set-up-and-use-configuration-projects.md#create-configuration-project-folder), which wraps all the necessary files required for a project package to function.
* [Package file](set-up-and-use-configuration-projects.md#list-project-dependencies), which lists project’s dependencies.
* [Entrypoint symbol](set-up-and-use-configuration-projects.md#set-entrypoint-for-system-configuration), which points to settings and services for an instance under configuration.

{% hint style="info" %}
We provide a CLI tool, called `zen`, which makes working with projects easier. It is distributed as `jar` file and can be downloaded from our [zen Github releases](https://github.com/HealthSamurai/ftr/releases).
{% endhint %}

## Create Aidbox Configuration project folder

Configuration projects, which are also _zen projects_, work as git repositories under the hood. This means that you need to have a directory initialized as a git repo. You can either create it from scratch or clone an existing one.

You can also bootstrap a project with the help of [`zen`](https://github.com/HealthSamurai/ftr/releases) tool:

```bash
java -jar path/to/zen.jar zen init my-project
```

Then you need to set `BOX_PROJECT_GIT_URL` environment variable to the location of this repository so that Aidbox knows where to find it. Note that the location can be either a local path or a remote URL, for which you can optionally specify a commit and/or branch.

See an example of an [initialized project on our Github](https://github.com/Aidbox/aidbox-docker-compose).

## Provide access to your project

Aidbox can load project from an URL or read directly from its file system.

* `BOX_PROJECT_GIT_URL` — where to clone your project from. Aidbox substitutes it to `git clone <url>` command.
* `BOX_PROJECT_GIT_PROTOCOL` — either `https` or `ssh`. Assumes local dir if omitted.
* `BOX_PROJECT_GIT_TARGET__PATH` — where to clone your project to and where to read it from. Default value is a directory in `/tmp`

If you specify only the target path then Aidbox expects you to clone a project into this location before it starts. This allows mounting persistent file system cache and using local file system for development. See [tips for development](set-up-and-use-configuration-projects.md#tips-for-development) and [tips for production](set-up-and-use-configuration-projects.md#tips-for-production).

### Private repositories as projects

If a repository you want to use as a project is under restricted access, you can provide the necessary SSH or HTTPS credentials via the following environment variables:

* `BOX_PROJECT_GIT_ACCESS__TOKEN` — access token for HTTPS
* `BOX_PROJECT_GIT_PUBLIC__KEY` — SSH public key
* `BOX_PROJECT_GIT_PRIVATE__KEY` — SSH private key

## List project’s dependencies

Project’s package file, `zen-package.edn`, should be located at the root of a configuration project. It is used to specify what dependencies, which are other zen projects, you wish to use.

If you don’t have any dependencies, the file should look like this:

{% code title="zen-package.edn" %}
```clojure
{:deps {}}
```
{% endcode %}

See also an [example of `zen-package.edn` with specified dependencies](enable-igs.md#specify-zen-fhir-igs-in-your-zen-package.edn).

You can [read more about how zen-package file is organized](aidbox-configuration-project-structure.md#zen-package.edn).

## Set system entrypoint

Aidbox starts reading configuration project from a zen entrypoint. It is a namespaced symbol which is tagged with `aidbox/system` tag. It is used to bootstrap the whole system.

You can specify an entrypoint symbol via `AIDBOX_ZEN_ENTRYPOINT` environment variable.

The entrypoint file should import all the packages and other namespaces you wish to use.\
Its `ns` value should correspond to its filename (with `-` replaced by `_`).

As an example, consider the following config file for a system which happens to import [zen-fhir US Core IG package](https://github.com/zen-fhir/hl7-fhir-us-core):

{% code title="zrc/system.edn" %}
```clojure
{:ns     system
 :import #{hl7-fhir-us-core}

 box
 {:zen/tags #{aidbox/system}
  …}
 
 …}
```
{% endcode %}

You need to specify `AIDBOX_ZEN_ENTRYPOINT` environment variable. This variable is formatted as `<entrypoint namespace>/<entrypoint symbol name>`. Given entry file such as above, you get the following:

```
AIDBOX_ZEN_ENTRYPOINT=system/box
```

{% hint style="info" %}
Besides system environment variables for setting up aidbox configuration project, there are many other envs Aidbox respects as part of backward compatibility. Still they are considered as legacy.

It's more preferable to set up Aidbox with [aidbox.config/config](aidbox-project-environment-variables/aidbox-config-config.md). You may provide it with `aidbox/system`

{% code title="zrc/system.edn" overflow="wrap" %}
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

## Commit your changes

Aidbox Configuration project is a git repository. You need to commit all the changes you’ve made otherwise Aidbox won’t see them.

```bash
cd path/to/project && git add -A && git commit -m "new commit"
```

## Tips for development

### Cache and reuse project’s dependencies

Suppose that you’ve configured your Aidbox instance with some heavy dependencies. During the development phase, you may wish to reuse these dependencies downloaded data. `BOX_PROJECT_GIT_TARGET__PATH` environment variable allows you to specify a path where your project with downloaded dependencies are going to be cached. By mounting this path to a persistent file system you'll provide downloaded data to Aidbox after a restart, thus making Aidbox reusing data (Aidbox also checks for dependencies updates in this case).

#### Usage with Docker

If you want to cache dependencies for Aidbox Docker container, you need to mount a cache directory as a volume.

See an [example in our Aidbox starter repository](https://github.com/Aidbox/aidbox-docker-compose).

### Hot reload for changes in Aidbox Configuration project

By default, Aidbox requires a restart after making changes to a configuration project. If you wish to enable a hot reload, set the following environment variable:

```shell
AIDBOX_ZEN_DEV_MODE=true
```

You can also use Aidbox UI to reload namespaces: proceed to `Profiles` page on the left sidebar and click the reload button in the upper left corner.

Additionally, there’s [`aidbox.zen/reload-namespaces`](aidbox-project-environment-variables/aidbox-project-rpc-reference.md#aidbox.zen-reload-namespaces) RPC method that does the same thing.

```yaml
POST /rpc

method: aidbox.zen/reload-namespaces
params: {}
```

{% hint style="info" %}
Hot reload feature is a work in progress. Please contact us if you encounter any issues.
{% endhint %}

### Editor support

If you are using VS Code, we have [zen-lsp](https://github.com/zen-lang/zen-lsp) plugin which should help you write configs with less errors.

{% hint style="info" %}
VS Code plugin is in the alpha phase. Please contact us if you encounter any issues.
{% endhint %}

## Tips for production

### Cache and reuse project’s dependencies

This tip is the same as for development.

### Provide prepackaged Aidbox project with all the dependencies

In production you may not always have an access to a git repository with your project. It can also be convenient to bundle the project together with all its dependencies. You can use our [`zen`](https://github.com/HealthSamurai/ftr/releases) tool to address both of these concerns.

Execute the following command inside the project directory to create a zip file with your project:

```bash
java -jar path/to/ zen.jar zen build target zen-package
```

It will be available inside the `target` directory in the root of the project.

Now all you need is to set `AIDBOX_ZEN_PATHS` environment variable. There are two ways to do it:

```
# 1 — provided at remote URL
AIDBOX_ZEN_PATHS=url:package-zip:https://hostname.tld/zen-package.zip

# 2 — provided at filesystem path
AIDBOX_ZEN_PATHS=path:package-zip:path/to/zen-package.zip
```

It is also possible to provide an unpacked zip of your project:

```
AIDBOX_ZEN_PATHS=path:package-dir:path/to/unpacked-zip-content
```

## Share configuration projects with others

Since configuration projects are just git repositories you can publish them in any git registry and other people will be able to use them for their own configurations: either as a standalone project or as a dependency.

If your repository is available only under a restricted access, others will need to [set the appropriate environment variables so that Aidbox is able to access it](set-up-and-use-configuration-projects.md#private-repositories-as-projects).

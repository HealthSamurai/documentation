# Migrate to Git Aidbox Configuration Projects

{% hint style="warning" %}
Since the 2405 release, using Aidbox in FHIR schema validation engine is recommended, which is incompatible with zen or Entity/Attribute options.

[Setup Aidbox with FHIR Schema validation engine](broken-reference/)
{% endhint %}

November 2022 release of Aidbox comes with a couple of features to reduce the startup time when using FHIR IG packages. First, we prepared a new version for our distribution of those packages which allows for their more efficient load into the Aidbox. Second, for those using configuration projects, it is now possible to remount downloaded IGs.

## How to use new FHIR IG packages?

### If you are using configuration projects

Just update their version so that it is at least 0.6.0. You can see the [latest version on the Releases page](https://github.com/zen-lang/fhir/releases).

### If you are using `AIDBOX_ZEN_PATHS`

#### `zen-lang/fhir` urls

All urls associated with the `zen-lang/fhir` [GitHub repository](https://github.com/zen-lang/fhir) should be **removed**. All standalone packages represented as zip archives have been migrated to the new zen-package format, now you can specify desired IG's via `AIDBOX_ZEN_PACKAGE_LOAD` environment variable or via `zen-package.edn`.

Add a new environment variable `AIDBOX_ZEN_PACKAGE_LOAD="{:deps {hl7-fhir-us-core \"https://github.com/zen-fhir/hl7-fhir-us-core\"}}"`

`:deps` should list all the IGs that were in `AIDBOX_ZEN_PATHS` but with their URLs changed to a corresponding repository from our [list of packages repositories](https://github.com/orgs/zen-fhir/repositories).

If your `AIDBOX_ZEN_PATHS` looks like

{% code overflow="wrap" %}
```javascript
url:zip:https://github.com/zen-lang/fhir/releases/download/0.5.13/hl7-fhir-us-core.zip,
url:zip:https://github.com/zen-lang/fhir/releases/download/0.5.13/hl7-fhir-us-carin-bb.zip
```
{% endcode %}

then`AIDBOX_ZEN_PACKAGE_LOAD` should be equal to

{% code overflow="wrap" %}
```clojure
"
{:deps
 {hl7-fhir-us-core \"https://github.com/zen-fhir/hl7-fhir-us-core\",
  hl7-fhir-us-carin-bb \"https://github.com/zen-fhir/hl7-fhir-us-carin-bb\"}}
"
```
{% endcode %}

#### IGs outside of `zen-lang/fhir`

If you have urls pointing to IGs hosted outside the `zen-lang/fhir` repo, you should also **remove** them, recompile those IGs into zen-packages and store them on a filesystem or any git repo server.

Compile IG to zen-package. Download zen-fhir.jar CLI utility, which can be found [there](https://github.com/zen-lang/fhir/releases/latest) under following name pattern zen-fhir-\<release-tag>-standalone.jar.

Create and open directory:

```bash
mkdir zen-profiling && cd zen-profiling && mkdir output
```

Get the tarball link for the desired IG, in this example we will use hl7.fhir.us.davinci-pdex@latest, although it is already presented in the [zen-packages gallery](https://github.com/zen-fhir). This step is unnecessary if you already have a folder containing the IG and its dependencies:

```bash
npm --registry https://packages.simplifier.net view hl7.fhir.us.davinci-pdex@latest
```

Copy resulting tarball link and install this IG:

```bash
npm --registry https://packages.simplifier.net install https://packages.simplifier.net/hl7.fhir.us.davinci-pdex/2.0.0-ballot
```

Convert downloaded IGs to zen-packages if you want to process each provided IG omit `-m` option, but if you provide additional IGs as _dependencies_, select the main package (via `-m` option) to which all transformations will be applied:

```bash
java -jar -Xmx8g ~/Downloads/zen-fhir.jar ig-to-zenpackage -i node_modules -o output -m hl7.fhir.us.davinci-pdex
```

You can store resulting repo on a file system or any git repository servers (e.g. Github, Gitlab, etc.)

List such dependencies using `AIDBOX_ZEN_PACKAGE_LOAD:`

```
"
{:deps
 {hl7-fhir-us-davinci-pdex \"path/to/git/repo/hl7.fhir.us.davinci-pdex"}}
"
```

### If you have zip with zen configuration files

In this [guide](broken-reference/) you will find instructions on how to create zen-packages and how to store/import your zen-configs. As a result, you will get a zen-package that can be stored on a filesystem or git repo server, so you can easily declare this package as a dependency in your `AIDBOX_ZEN_PACKAGE_LOAD` or `BOX_PROJECT_GIT_TARGET__PATH`.

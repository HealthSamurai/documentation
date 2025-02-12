# FTR from FTR — Supplement

{% hint style="warning" %}
Since the 2405 release, using Aidbox in FHIRSchema mode is recommended, which is incompatible with zen or Entity/Attribute options.

[setup.md](../../../../../modules/profiling-and-validation/fhir-schema-validator/setup.md "mention")
{% endhint %}

## Prerequisites

1. Download zen-ftr CLI: [Download link](https://github.com/HealthSamurai/ftr/releases/latest/download/zen.jar)

This guide will walk you through the creation of an FTR using the IG and a supplementary FTR. The ValueSet described in the IG relies heavily on our internal CodeSystem, which, in our case, is SNOMED-Subset. This system expands into codes that are descendants of `"Angina (disorder)" (194828000)`. The supplementary FTR is stored in a remote GitHub source-code repository. You can customize the `:extractor-options.supplements` property in the FTR manifest to provide additional supplementary FTRs and use the `:source-url` to specify the IG's path. For more information on the FTR manifest, please refer to this page.

```
project/
  zen-package.edn #Package deps declaration
  resources/ig/
    angina-valueset.json #ValueSet resource
    package.json #IG package meta
  zrc/
    system.edn #System entrypoint importing a ValueSet definion
    angina.edn # ValueSet definition
```

{% code title="zen-package.edn" %}
```clojure
{:deps {}}
```
{% endcode %}

{% code title="zrc/system.edn" %}
```clojure
{:ns system
 :import #{aidbox angina}
 
 box
 {:zen/tags #{aidbox/system}}}
```
{% endcode %}

{% code title="resources/ig/angina-valueset.json" %}
```json
{
  "compose": {
    "include": [
      {
        "valueSet": [
          "http://snomed.info/sct"
        ],
        "filter": [
          {
            "op": "is-a",
            "property": "concept",
            "value": "194828000"
          }
        ]
      }
    ]
  },
  "id": "anginavs",
  "name": "anginavs",
  "resourceType": "ValueSet",
  "status": "active",
  "url": "anginavs"
}
```
{% endcode %}

{% code title="resources/ig/package.json" %}
```
{
  "name": "supplementdependant.core",
  "version": "0.0.1",
  "type": "supplementdependant",
  "title": "supplementdependant",
  "description": "supplementdependant",
  "author": "supdep",
  "url": "supplementdependant"
}
```
{% endcode %}

The ValueSet definition conforms to the [zen.fhir ValueSet schema](../../profiling-with-zen-lang/) and includes a :ftr property. This property contains an FTR manifest that defines an IG source using the :source-url property, and provides coordinates for supplementary ValueSets using :extractor-options.supplements. The supplementary ValueSets are used to create an expanded version of the ValueSet, which is then stored in the resulting FTR. To learn more about the FTR manifest, please refer to this [page](../ftr-manifest.md).

#### Spec for supplement coordinate

```clojure
{
  :source-url <path to some FTR repo, FS path or network URL>
  :module <desired module> 
  :tag <desired tag>
}
```

{% code title="zrc/angina.edn" %}
```
{ns angina
 import #{zen.fhir}
 angina-vs
 {:zen/tags #{zen.fhir/value-set}
  :zen.fhir/version "0.6.14"
  :uri "angina-vs"
  :ftr {:module            "nanosubset"
        :source-url        "resources/ig"
        :ftr-path          "ftr"
        :tag               "prod"
        :source-type       :ig
        :extractor-options {:supplements [{:source-url "https://raw.githubusercontent.com/HealthSamurai/ftr/main/examples"
                                           :module "microsnomed"
                                           :tag "prod"}]}}}}
```
{% endcode %}

Initialize this directory as a git repository and commit your initial set-up:

```bash
git init && git add . && git commit -m "init"
```

## Generate FTR

Replace `<PATH_TO_JAR>` placeholder with absolute path to `zen.jar`.

Execute command listed below in project directory:

```bash
java -jar <PATH_TO_JAR> build-ftr
```

Commit FTR directory:

```bash
git add . && git commit -m "Build ftr"
```

Now you can run Aidbox with the following configuration project and use [FHIR Terminology API ](../../../../../modules/terminology/valueset/)methods like `$validate-code/$lookup` on generated `angina-vs` ValueSet. Resource validation performed when someone invocates a FHIR REST operations will also validate ValueSet binding via FTR.

For detailed instructions about using Aidbox with Aidbox configuration project, please refer to this [page](../../../../../getting-started/run-aidbox-locally-with-docker.md).

## Instruct Aidbox to load terminologies into the DB

Set the following environment variable:

```
BOX_FEATURES_FTR_PULL_ENABLE=true
```

By default, Aidbox does not load terminologies into the database as that can take a lot of disk space. This means that full terminology functionality won’t be available until you enable it manually. [BOX\_FEATURES\_FTR\_PULL\_ENABLE](../../ftr.md) environment variable is just for that. When you set it to `true`, Aidbox will load terminologies into the database on the next startup and start functioning as a fully-featured terminology server.

## Further steps

For guidance on development and production usage, visit the links below:

* [Development tips](../../aidbox-zen-lang-project/setting-up-a-configuration-project.md#tips-for-local-development)
* [Production tips](../../aidbox-zen-lang-project/setting-up-a-configuration-project.md#tips-for-production)

For customizing Aidbox startup behavior when using FTR, read about [FTR environment variables](../../ftr.md).

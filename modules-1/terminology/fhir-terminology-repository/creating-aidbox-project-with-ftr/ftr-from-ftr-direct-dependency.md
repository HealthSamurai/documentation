# FTR from FTR — Direct Dependency

## Prerequisites

1. Download zen-ftr CLI: [Download link](https://github.com/HealthSamurai/ftr/releases/latest/download/zen.jar)

In this guide, we will create an FTR that represents a SNOMED CT subset that includes codes descending to `"Angina (disorder)" (194828000)`. The FTR on which we depend is stored remotely in a GitHub source-code repository. You can modify the `source-url` field in the FTR manifest to provide a desired URL, which can be a file path or network URL. Main use case for this extraction engine — design new ValueSets.

## Creating [Aidbox Configuration project](../../../../aidbox-configuration/aidbox-zen-lang-project/)

Create a directory `project` with following structure:

```
project/
  zen-package.edn #Package deps declaration
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

This ValueSet definition conforms to the[ zen.fhir ValueSet schema](../../../profiling-and-validation/profiling-with-zen-lang/) and includes a `:ftr` property. The `:ftr` property contains an FTR manifest that defines an FTR-dependency source through the `:source-url` property, which allows the creation of an expanded version of the ValueSet to be stored in the resulting FTR. In addition, the `:extractor-options.target-tag` specifies the tag to be selected within the provided FTR dependency. For more information on the FTR manifest, please refer to this [page](../ftr-manifest.md).

{% code title="zrc/angina.edn" %}
```clojure
{ns angina
 import #{zen.fhir}
 angina-vs
 {:zen/tags #{zen.fhir/value-set}
  :zen.fhir/version "0.6.14"
  :uri "angina-vs"
  :ftr {:module            "nanosubset"
        :source-url        "https://raw.githubusercontent.com/HealthSamurai/ftr/main/examples/microsnomed"
        :ftr-path          "/tmp"
        :tag               "prod"
        :source-type       :ftr
        :extractor-options {:target-tag "prod"
                            :value-set
                            {:compose      {:include [{:valueSet ["http://snomed.info/sct"]
                                                       :filter [{:op       "is-a"
                                                                 :property "concept"
                                                                 :value    "194828000"}]}]}
                             :id           "nanosubset"
                             :name         "nanosubset"
                             :resourceType "ValueSet"
                             :status       "active"
                             :url          "nanosubset"}}}}}
```
{% endcode %}

Initialize this directory as a git repository and commit your initial set-up:

```bash
git init && git add . && git commit -m "init"
```

## Generate FTR

Replace `<PATH_TO_JAR>` placeholder with absolute path to `zen.jar`.&#x20;

Execute command listed below in project directory:

```bash
java -jar <PATH_TO_JAR> build-ftr
```

Commit FTR directory:

```bash
git add . && git commit -m "Build ftr"
```

Now you can run Aidbox with the following configuration project and use [FHIR Terminology API ](../../valueset/)methods like `$validate-code/$lookup` on generated `angina-vs` ValueSet. Resource validation performed when someone invocates a FHIR REST operations will also validate ValueSet binding via FTR.

For detailed instructions about using Aidbox with Aidbox configuration project, please refer to this [page](../../../../getting-started/run-aidbox-locally-with-docker.md).

## Instruct Aidbox to load terminologies into the DB

Set the following environment variable:

```
BOX_FEATURES_FTR_PULL_ENABLE=true
```

By default, Aidbox does not load terminologies into the database as that can take a lot of disk space. This means that full terminology functionality won’t be available until you enable it manually. [BOX\_FEATURES\_FTR\_PULL\_ENABLE](../../../../reference/configuration/environment-variables/ftr.md) environment variable is just for that. When you set it to `true`, Aidbox will load terminologies into the database on the next startup and start functioning as a fully-featured terminology server.

## Further steps

For guidance on development and production usage, visit the links below:

* [Development tips](../../../../aidbox-configuration/aidbox-zen-lang-project/setting-up-a-configuration-project.md#tips-for-local-development)
* [Production tips](../../../../aidbox-configuration/aidbox-zen-lang-project/setting-up-a-configuration-project.md#tips-for-production)

For customizing Aidbox startup behavior when using FTR, read about [FTR environment variables](../../../../reference/configuration/environment-variables/ftr.md).

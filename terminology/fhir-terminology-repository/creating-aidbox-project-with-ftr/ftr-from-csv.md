# FTR from CSV

## Prerequisites

1. Download zen-ftr CLI: [Download link](https://github.com/HealthSamurai/ftr/releases/latest/download/zen.jar)

## Creating [Aidbox Configuration project](../../../aidbox-configuration/aidbox-zen-lang-project/)

Create a directory `project` with following structure:

```bash
project/
  zen-package.edn #Package deps declaration
  resources/
    icd-10.csv #CSV source file
  zrc/
    system.edn #System entrypoint importing a ValueSet definion
    diagnosis.edn # ValueSet definition
```

{% code title="zen-package.edn" %}
```clojure
{:deps {}}
```
{% endcode %}

{% code title="zrc/system.edn" %}
```clojure
{:ns system
 :import #{aidbox diagnosis}
 
 box
 {:zen/tags #{aidbox/system}}}
```
{% endcode %}

This ValueSet definition confirms to [zen.fhir ValueSet schema](../../../profiling-and-validation/profiling-with-zen-lang/) and has a `:ftr` property, it contains an FTR manifest that defines a csv source via `:source-url` property to create an expanded version of the ValueSet to be stored in the resulting FTR. For details about FTR manifest, please, refer to this [page](../ftr-manifest.md).

```clojure
{:ns diagnosis
 :import #{zen.fhir}
 
 diagnosis-vs
 {:zen/tags #{zen.fhir/value-set},
  :zen.fhir/version "0.5.0",
  :uri "diagnosis-vs",
  :ftr
  {:module "ig",
   :source-url "resources/icd-10.csv",
   :ftr-path "ftr",
   :tag "v1",
   :source-type :flat-table,
   :extractor-options
   {:format "csv",
    :csv-format {:delimiter ";", :quote "'"},
    :header false,
    :data-row 0,
    :mapping {:concept {:code {:column 2}, :display {:column 3}}},
    :code-system {:id "icd10", :url "http://hl7.org/fhir/sid/icd-10"},
    :value-set
    {:id "icd10", :name "icd10.accidents", :url "diagnosis-vs"}}}}}

```

Create `resources/icd-10.csv` file, containing CSV source for your ValueSet:

```csv
10344;20;XX;External causes of morbidity and mortality;;;1;
16003;2001;V01-X59;Accidents;10344;;1;
15062;20012;W00-X59;Other external causes of accidental injury;16003;;1;10/07/2020
11748;2001203;W50-W64;Exposure to animate mechanical forces;15062;;1;
11870;2001203W64;W64;Exposure to other and unspecified animate mechanical forces;11748;;1;
11871;2001203W640;W64.0;Exposure to other and unspecified animate mechanical forces home while engaged in sports activity;11870;;1;
11872;2001203W641;W64.00;Exposure to other and unspecified animate mechanical forces, home, while engaged in sports activity;11871;;1;
11873;2001203W641;W64.01;Exposure to other and unspecified animate mechanical forces, home, while engaged in leisure activity;11871;;1;
```

Initialize this directory as a git repository, commit your initial set-up

```bash
git init && git add --all && git commit -m "init"
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

Now you can run Aidbox with the following configuration project and use [FHIR Terminology API ](../../valueset/)methods like `$validate-code/$lookup` on generated `diagnosis-vs` ValueSet. Resource validation performed when someone invocates a FHIR REST operations will also validate ValueSet binding via FTR.

```json
// resources/package.json
{
   "name":"ig",
   "version":"0.0.1",
   "type":"ig",
   "title":"ig",
   "description":"ig",
   "author":"hs",
   "url":"url"
}
```

Initialize this directory as a git repository and commit your initial set-up:

```bash
git init && git add . && git commit -m "init"
```

## Further steps

For guidance on development and production usage, visit the links below:

* [Development tips](../../../aidbox-configuration/aidbox-zen-lang-project/setting-up-a-configuration-project.md#tips-for-local-development)
* [Production tips](../../../aidbox-configuration/aidbox-zen-lang-project/setting-up-a-configuration-project.md#tips-for-production)

For customizing Aidbox startup behavior when using FTR, read about [FTR environment variables](../../../reference/configuration/environment-variables/ftr.md).

# FTR from FHIR IG

{% hint style="warning" %}
Since the 2405 release, using Aidbox in FHIRSchema mode is recommended, which is incompatible with zen or Entity/Attribute options.

[setup.md](../../../../../modules/profiling-and-validation/fhir-schema-validator/setup.md "mention")
{% endhint %}

## Prerequisites

1. Download zen-ftr CLI: [Download link](https://github.com/HealthSamurai/ftr/releases/latest/download/zen.jar)

## Creating [Aidbox Configuration project](../../aidbox-zen-lang-project/)

Create a directory `project` with following structure:

```bash
project/
  zen-package.edn #Package deps declaration
  resources/ig/
    gender-codesystem.json #CodeSystem resource, lightweight http://hl7.org/fhir/administrative-gender version 
    gender-valueset.json #ValueSet resource
    package.json #IG package meta
  zrc/
    system.edn #System entrypoint importing a ValueSet definion
    gender.edn # ValueSet definition
```

{% code title="zen-package.edn" %}
```clojure
{:deps {}}
```
{% endcode %}

{% code title="zrc/system.edn" %}
```clojure
{:ns system
 :import #{aidbox gender}
 
 box
 {:zen/tags #{aidbox/system}}}
```
{% endcode %}

This ValueSet definition confirms to [zen.fhir ValueSet schema](../../profiling-with-zen-lang/) and has a `:ftr` property, it contains an FTR manifest that defines an IG source via `:source-url` property to create an expanded version of the ValueSet to be stored in the resulting FTR. For details about FTR manifest, please, refer to this [page](../ftr-manifest.md).

```clojure
{ns gender
 import #{zen.fhir}
 gender-vs
 {:zen/tags #{zen.fhir/value-set}
  :zen.fhir/version "0.5.0"
  :uri "gender-vs"
  :ftr {:module "ig"
        :source-url "resources/ig/"
        :ftr-path "ftr"
        :tag "v1"
        :source-type :ig}}}
```

```json
// resources/gender-codesystem.json
{
   "resourceType":"CodeSystem",
   "id":"gender-cs-id",
   "url":"gender-cs-url",
   "status":"active",
   "content":"complete",
   "concept":[
      {
         "code":"male",
         "display":"Male"
      },
      {
         "code":"female",
         "display":"Female"
      },
      {
         "code":"other",
         "display":"Other"
      },
      {
         "code":"unknown",
         "display":"Unknown"
      }
   ]
}
```

```json
// resources/gender-valueset.json
{
   "resourceType":"ValueSet",
   "id":"gender-vs-id",
   "url":"gender-vs",
   "status":"active",
   "compose":{
      "include":[
         {
            "system":"gender-cs-url"
         }
      ]
   }
}
```

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

Now you can run Aidbox with the following configuration project and use [FHIR Terminology API ](../../../../../modules/terminology/valueset/)methods like `$validate-code/$lookup` on generated `diagnosis-vs` ValueSet. Resource validation performed when someone invocates a FHIR REST operations will also validate ValueSet binding via FTR.

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

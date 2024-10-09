---
description: Start working with ICD-10-CM terminology in Aidbox
---

# Load ICD-10-CM into Aidbox

[ICD-10-CM](https://www.cdc.gov/nchs/icd/icd-10-cm.htm) is a large medical terminology which can be used in [FHIR ValueSet](http://hl7.org/fhir/valueset.html) resources. It is distributed under a license which you have to set up with them.

We provide out-of-the box integration with ICD-10-CM through [Aidbox Configuration Project](../../../aidbox-configuration/aidbox-zen-lang-project/). You may start using it after we make sure you have the required ICD-10-CM license.

## How to set up Aidbox with ICD-10-CM terminology

To correctly set up Aidbox, we'll utilize the Aidbox configuration projects. \
\
There's an [existing guide](../../../getting-started/run-aidbox-locally-with-docker/) for this process. Adhere to this guide, <mark style="background-color:green;">but note a variation</mark> when you reach the `Configure the Aidbox` step: instead of using the recommended configuration projects (R4,R4B,R5,etc.) — clone this specific project:      &#x20;

```sh
git clone \
  https://github.com/Aidbox/aidbox-project-template-icd10cm.git \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```

This project is tailored with specific configurations essential for terminology loading.

### Confirm with us your ICD-10-CM license

ICD-10-CM is distributed under a license which means that we can not redistribute it without making sure that other people have this license. You can confirm your eligibility for accessing ICD-10-CM by contacting Aidbox team. See [our contacts here](../../../contact-us.md).

### Configuration Overview: Key Features and Distinctions

If you already have a configuration project, you can replicate these steps to enable ICD-10-CM terminology in your Aidbox instance.

#### Added ICD-10-CM dependency to configuration project

{% code title="zen-package.edn" %}
```
{:deps {icd10cm "https://github.com/zen-fhir/icd10-cm.git"}}
```
{% endcode %}

By adding this dependency, we instruct Aidbox to load the `zen.fhir` ValueSet definition, which is meant to include all codes from ICD-10-CM. This ValueSet definition contains a specific directive detailing the FTR manifest. Aidbox'll use this manifest to input the actual ICD-10-CM concepts into the database.

{% code title="zrc/icd10-cm.edn" lineNumbers="true" %}
```clojure
{ns icd10cm,
 import #{zen.fhir},
 value-set
 {:zen/tags #{zen.fhir/value-set},
  :zen/desc "This value set includes all ICD-10-CM codes.",
  :zen.fhir/version "0.6.0",
  :fhir/code-systems
  #{{:fhir/url "http://hl7.org/fhir/sid/icd-10",
     :zen.fhir/content :bundled}},
  :uri "http://hl7.org/fhir/ValueSet/icd-10",
  :version "2024",
  :ftr
  {:module "icd10cm",
   :source-url "https://storage.googleapis.com",
   :ftr-path "ftr",
   :source-type :cloud-storage,
   :tag "prod"}}}
```
{% endcode %}

#### Imported ICD-10-CM namespace to configuration project entrypoint

<pre><code>{ns main
 import #{aidbox
          config
          <a data-footnote-ref href="#user-content-fn-1">icd10cm</a>}
 …}
</code></pre>

Zen requires importing a namespace into the entrypoint to load the ValueSet definition into the definitions store.

#### FTR Pull Feature — instruct Aidbox to load concepts into the database

By default, Aidbox does not load terminologies into the database as that can take a lot of disk space. This means that full terminology functionality won’t be available until you enable it manually. When you set it to `true`, Aidbox will load terminologies into the database on the next startup and start functioning as a fully-featured terminology server.

To achieve that we set `ftr.pull.enable` to true in `features` map.

{% hint style="info" %}
When adding this feature to existing configuration projects, be mindful. If you include dependencies like `hl7-fhir-r4-core` or `hl7-fhir-us-core`, Aidbox will load terminologies from these packages, which are sizable. Therefore, loading all the concepts into the database might take a while.
{% endhint %}

{% code title="zrc/config.edn" %}
```
 features
 {:zen/tags #{aidbox.config/features}
  :ftr {:pull {:enable true}}}
```
{% endcode %}

### What else you can do with configs related to terminology?

#### Lock Aidbox's start until all concepts are stored in the database

When `ftr.pull.enable` is set to `true`, Aidbox loads concepts asynchronously by default. This means that immediately after starting, there might be no concepts available because they are still loading. To address this behavior, set `ftr.pull.sync` to `true`.

{% code title="zrc/config.edn" %}
```
 features
 {:zen/tags #{aidbox.config/features}
  :ftr {:pull {:enable true
               :sync true}}}
```
{% endcode %}

### How can you determine if the concepts are still loading or have already loaded? (Usable for `async` pulls)

Access the Aidbox UI and navigate to `Database` > [`Running Queries`](../../../overview/aidbox-ui/db-queries.md). Look for a query that includes `"_import"`; this query is responsible for loading concepts into your database. Once this query disappears from the list, you can check the concepts in the database. Proceed to `Database` > `DB Console` and enter the following query:

{% code title="" %}
```sql
SELECT count(*) from concept where resource->>'system' = 'http://hl7.org/fhir/sid/icd-10'
```
{% endcode %}



[^1]: Namespace we've imported

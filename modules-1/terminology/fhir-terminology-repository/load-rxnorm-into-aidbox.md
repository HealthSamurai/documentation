---
description: Setting up Aidbox with RxNorm terminology loaded via FTR
---

# Load RxNorm into Aidbox

[RxNorm](https://www.nlm.nih.gov/research/umls/rxnorm/index.html) provides normalized names for clinical drugs and links its names to many of the drug vocabularies commonly used in pharmacy management and drug interaction software, including those of First Databank, Micromedex, Multum, and Gold Standard Drug Database. \
\
By providing links between these vocabularies, RxNorm can mediate messages between systems not using the same software and vocabulary. The license is free if you intend to use only the RxNorm codes.

We offer out-of-the-box integration with the _most recent version_ of RxNorm, accessible at any given time, via the [Aidbox Configuration project](load-rxnorm-into-aidbox.md#setting-up-aidbox-configuration-project) and [FTR](../../../terminology/fhir-terminology-repository/ftr-specification.md).

{% hint style="info" %}
This bundle includes only codes with the `Source Abbreviation (SAB)` labeled as `RXNORM`. Codes from vocabularies such as SNOMED, Micromedix, GSDD, etc., <mark style="background-color:red;">are not part of this bundle</mark>. If you require these vocabularies, please [reach out to us](../../../contact-us.md).
{% endhint %}

## How to set up Aidbox with RxNorm terminology

To correctly set up Aidbox, we'll utilize the Aidbox configuration projects. \
\
There's an [existing guide](../../../getting-started/run-aidbox-locally-with-docker/) for this process. Adhere to this guide, <mark style="background-color:green;">but note a variation</mark> when you reach the `Configure the Aidbox` step: instead of using the recommended configuration projects (R4,R4B,R5,etc.) — clone this specific project:      &#x20;

```sh
git clone \
  https://github.com/Aidbox/aidbox-project-template-rxnorm.git \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```

This project is tailored with specific configurations essential for terminology loading.

### Configuration Overview: Key Features and Distinctions

If you already have a configuration project, you can replicate these steps to enable RxNorm terminology in your Aidbox instance.

#### Added RxNorm dependency to configuration project

{% code title="zen-package.edn" %}
```
{:deps {rxnorm "https://github.com/zen-fhir/rxnorm.git"}}
```
{% endcode %}

By adding this dependency, we instruct Aidbox to load the `zen.fhir` ValueSet definition, which is meant to include all codes from RxNorm. This ValueSet definition contains a specific directive detailing the FTR manifest. Aidbox'll use this manifest to input the actual RxNorm concepts into the database.

{% code title="rxnorm/zrc/rxnorm.edn" lineNumbers="true" %}
```clojure
{ns rxnorm,
 import #{zen.fhir},
 value-set
 {:zen/tags #{zen.fhir/value-set},
  :zen/desc "Includes all concepts from RxNorm.",
  :zen.fhir/version "0.6.0",
  :fhir/code-systems
  #{{:fhir/url "http://www.nlm.nih.gov/research/umls/rxnorm",
     :zen.fhir/content :bundled}},
  :uri "http://www.nlm.nih.gov/research/umls/rxnorm/valueset",
  :version "10022023",
  :ftr
  {:module "rxnorm",
   :source-url "https://storage.googleapis.com",
   :ftr-path "ftr",
   :source-type :cloud-storage,
   :tag "prod"}}}
```
{% endcode %}

#### Imported RxNorm namespace to configuration project entrypoint

<pre><code>{ns main
 import #{aidbox
          config
          <a data-footnote-ref href="#user-content-fn-1">rxnorm</a>}
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

```sql
SELECT count(*) from concept where resource->>'system' = 'http://www.nlm.nih.gov/research/umls/rxnorm'
```



[^1]: Namespace we've imported

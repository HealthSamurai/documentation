---
description: Start working with SNOMED CT terminology in Aidbox
---

# Load SNOMED CT into Aidbox

{% hint style="warning" %}
Since the 2405 release, using Aidbox in FHIRSchema mode is recommended, which is incompatible with zen or Entity/Attribute options.

[setup.md](../../../../modules/profiling-and-validation/fhir-schema-validator/setup.md "mention")
{% endhint %}

[SNOMED CT](https://www.snomed.org/snomed-ct/Use-SNOMED-CT) is a large medical terminology which can be used in [FHIR ValueSet](http://hl7.org/fhir/valueset.html) resources. It is distributed under a free license which you have to set up with them.

We provide out-of-the-box integration with SNOMED CT through [Aidbox Configuration project](../aidbox-zen-lang-project/). You may start using it after we make sure you have the required SNOMED license.

## How to set up Aidbox with SNOMED terminology

To correctly set up Aidbox, we'll utilize the Aidbox configuration projects. \
\
There's an [existing guide](../../../../getting-started/run-aidbox-locally-with-docker.md) for this process. Adhere to this guide, <mark style="background-color:green;">but note a variation</mark> when you reach the `Configure the Aidbox` step: instead of using the recommended configuration projects (R4,R4B,R5,etc.) — clone this specific project:     &#x20;

```sh
git clone \
  https://github.com/Aidbox/aidbox-project-template-snomed.git \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```

This project is tailored with specific configurations essential for terminology loading.

### Confirm with us your SNOMED license

SNOMED CT is distributed under a license which means that we can not redistribute it without making sure that other people have this license. You can confirm your eligibility for accessing SNOMED CT by contacting Aidbox team. See [our contacts here](https://docs.aidbox.app/contact-us).

## Configuration Overview: Key Features and Distinctions

If you already have a configuration project, you can replicate these steps to enable SNOMED terminology in your Aidbox instance.

#### Added SNOMED dependency to configuration project

{% code title="zen-package.edn" %}
```
{:deps {snomed "https://github.com/zen-fhir/snomed.git"}}
```
{% endcode %}

By adding this dependency, we instruct Aidbox to load the `zen.fhir` ValueSet definition, which is meant to include all codes from SNOMED. This ValueSet definition contains a specific directive detailing the FTR manifest. Aidbox'll use this manifest to input the actual SNOMED concepts into the database.

{% code title="snomed/zrc/snomed.edn" lineNumbers="true" %}
```clojure
{ns snomed,
 import #{zen.fhir},
 value-set
 {:zen/tags #{zen.fhir/value-set},
  :zen/desc
  "Includes all concepts from SNOMEDCT US edition snapshot. Both active and inactive concepts included, but is-a relationships only stored for active concepts",
  :zen.fhir/version "0.6.0",
  :fhir/code-systems
  #{{:fhir/url "http://snomed.info/sct", :zen.fhir/content :bundled}},
  :uri "http://snomed.info/sct",
  :version "20230901",
  :ftr
  {:module "snomed",
   :source-url "https://storage.googleapis.com",
   :ftr-path "ftr",
   :source-type :cloud-storage,
   :tag "prod"}}}
```
{% endcode %}

#### Imported SNOMED namespace to configuration project entrypoint

<pre><code>{ns main
 import #{aidbox
          config
          <a data-footnote-ref href="#user-content-fn-1">snomed</a>}
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

Access the Aidbox UI and navigate to `Database` > [`Running Queries`](../../../../overview/aidbox-ui/db-queries.md). Look for a query that includes `"_import"`; this query is responsible for loading concepts into your database. Once this query disappears from the list, you can check the concepts in the database. Proceed to `Database` > `DB Console` and enter the following query:

```sql
SELECT count(*) from concept where resource->>'system' = 'http://snomed.info/sct'
```

## Terminology translations

Currently, we support various SNOMED CT terminology translations, and we can incorporate additional translation variants as needed. Translations are stored in the concept .`designation` property, and when a package includes multiple languages, the original language is also retained within the `.designation` property. To select the desired translation, simply modify the link to the corresponding package in `zen-package.edn.`

| Zen Package Link                     | Languages (IETF BCP 47 tags) |
| ------------------------------------ | ---------------------------- |
| git@github.com:zen-fhir/snomed.git   | en                           |
| git@github.com:zen-fhir/snomedca.git | en, fr                       |

See also [$translate-concepts RPC](../../../../modules/terminology/concept/usdtranslate-concepts.md).

[^1]: Namespace we've imported

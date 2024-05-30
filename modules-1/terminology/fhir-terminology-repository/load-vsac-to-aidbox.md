---
description: >-
  Load all official versions of vocabulary value sets provided by the Value Set
  Authority Center (VSAC) at the National Library of Medicine (NLM)
---

# Load US VSAC Package to Aidbox

{% hint style="warning" %}
The current package is a pre-built version of the VSAC contents 0.7.0 (2022-06-11). Please [reach out to us](../../../contact-us.md) if you need a newer version.
{% endhint %}

## How to set up Aidbox with VSAC value sets

To correctly set up Aidbox, we'll utilize the Aidbox configuration projects.&#x20;

{% hint style="info" %}
Check the [existing guide](../../../getting-started/run-aidbox-locally-with-docker/) that explains how to run Aidbox locally      &#x20;
{% endhint %}

```sh
git clone \
  https://github.com/Aidbox/aidbox-project-template-vsac \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```

### Start Aidbox with Docker Compose

```shell
docker compose up --force-recreate
```

Navigate to [http://localhost:8888/](http://localhost:8888/) and Sign In to the Aidbox UI using the login `admin` and password `password`.

### Configuration Overview: Key Features and Distinctions

If you already have a configuration project, you can replicate these steps to enable US VSAC package in your Aidbox instance.

#### Added US VSAC dependency to configuration project

{% code title="zen-package.edn" %}
```
{:deps {us-nlm-vsac "https://github.com/zen-fhir/us-nlm-vsac.git"}}
```
{% endcode %}

#### Imported VSAC namespace to configuration project entrypoint

```
{ns main
 import #{aidbox
          config
          us-nlm-vsac}
 …}
```

Zen requires importing a namespace into the entrypoint to load the ValueSet definition into the definitions store.

#### FTR Pull Feature — instruct Aidbox to load concepts into the database

By default, Aidbox does not load terminologies into the database as that can take a lot of disk space. This means that full terminology functionality won’t be available until you enable it manually. When you set it to `true`, Aidbox will load terminologies into the database on the next startup and start functioning as a fully-featured terminology server.

To achieve that we set `ftr.pull.enable` to true in `features` map.

{% code title="zrc/config.edn" %}
```
 features
 {:zen/tags #{aidbox.config/features}
  :ftr {:pull {:enable true}}}
```
{% endcode %}

{% hint style="warning" %}
Please be aware the initial loading of terminologies may take a significant amount of time.
{% endhint %}

### How to check if the concepts are still loading or have already loaded? (Usable for `async` pulls)

Run this query to check the concepts

```sql
select resource -> 'code', resource -> 'display' from concept where resource -> 'valueset' @> '["http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113762.1.4.1190.58"]'::jsonb
```

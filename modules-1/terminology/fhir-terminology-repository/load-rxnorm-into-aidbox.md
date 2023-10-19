# Load RxNorm into Aidbox

RxNorm is a large medical terminology which can be used in [FHIR ValueSet](http://hl7.org/fhir/valueset.html) resources. It is distributed under a free license.

We provide out-of-the-box integration with RxNorm through [Aidbox Configuration project](https://docs.aidbox.app/aidbox-configuration/aidbox-zen-lang-project).&#x20;

There is a step-by-step guide below which takes you from our [default Aidbox Configuration project](https://github.com/Aidbox/aidbox-docker-compose) to a fully enabled RxNorm terminology.

{% hint style="warning" %}
This bundle contains only codes with SAB(Source Abbreviation) = RXNORM
{% endhint %}

## Prerequisites

You need to have an [Aidbox Configuration project](https://docs.aidbox.app/aidbox-configuration/aidbox-zen-lang-project) to load prepackaged RxNorm terminology. One of the easiest way is to start with our [Docker Getting started guide](../../../getting-started-1/run-aidbox/run-aidbox-locally-with-docker.md).

## Step-by-step guide

### Add RxNorm to Aidbox Configuration project dependencies

Edit `zen-package.edn` to include RxNorm repository in the dependencies:

{% code title="zen-package.edn" %}
```clojure
{:deps {some-other-dep "…"
        rxnorm "https://github.com/zen-fhir/rxnorm.git"}}
```
{% endcode %}

You also need to import `rxnorm` namespace in your Aidbox entrypoint file. Assuming you are working with `docker-compose-template` from [Prerequisites](load-rxnorm-into-aidbox.md#prerequisites), it is `zrc/main.edn` file.

```clojure
{:ns main
 :import {some-other-dep
          rxnorm}

 …}
```

### Instruct Aidbox to load terminologies into the DB

Set the following environment variable:

```
BOX_FEATURES_FTR_PULL_ENABLE=true
```

By default, Aidbox does not load terminologies into the database as that can take a lot of disk space. This means that full terminology functionality won’t be available until you enable it manually. [BOX\_FEATURES\_FTR\_PULL\_ENABLE](../../../reference/configuration/environment-variables/ftr.md) environment variable is just for that. When you set it to `true`, Aidbox will load terminologies into the database on the next startup and start functioning as a fully-featured terminology server.

### Further steps

If you want to customize Aidbox startup behavior when using FTR, read more about [FTR environment variables](../../../reference/configuration/environment-variables/ftr.md).

One of the options you may want to consider is enabling`BOX_FEATURES_FTR_BUILD__INDEX__ON__STARTUP_SYNC`. Setting this environment variable to `true` will make Aidbox startup blocked until RxNorm terminology is fully available for fast and efficient validation.

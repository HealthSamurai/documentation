---
description: Start working with LOINC terminology in Aidbox
---

# Load LOINC into Aidbox

[LOINC](https://loinc.org/), or Logical Observation Identifiers Names and Codes, is an extensive medical terminology system that can be incorporated into FHIR ValueSet resources. It is a universal standard for identifying health measurements, observations, and documents, which helps facilitate the sharing and aggregation of clinical results.

We provide out-of-the box integration with LOINC through [Aidbox Configuration Project](../../../aidbox-configuration/aidbox-zen-lang-project/).&#x20;

This guide will walk you through the process, starting from [default Aidbox Configuration project](https://github.com/Aidbox/aidbox-docker-compose) and leading you all the way to having a fully enabled LOINC terminology.

### Prerequisites

You need to have an [Aidbox Configuration project](../../../getting-started/installation/) to load prepackaged LOINC terminology. One of the easiest way is to start with our [Docker Getting started guide](../../../getting-started/run-aidbox/run-aidbox-locally-with-docker.md).

### Step-by-step guide

#### Provide SSH keys to access our prepackaged LOINC repository

We distribute LOINC through a private Github repository. This means that you have to provide us with your public SSH key which we’ll add to the repository’s access list.

You also need to set Aidbox environment variables to use this public key and its corresponding private key. They are required so that Aidbox is able to work with private LOINC repository.

The required environment variables are listed below:

```shell
BOX_PROJECT_GIT_PRIVATE__KEY="-----BEGIN …
…
-----END … -----
"
BOX_PROJECT_GIT_PUBLIC__KEY="ssh-…"
```

Be aware that there’s a newline at the end of `BOX_PROJECT_GIT_PRIVATE__KEY`. Make sure that it is present otherwise the key becomes invalid.

#### Add LOINC to Aidbox Configuration project dependencies

Edit `zen-package.edn` to include LOINC repository in the dependencies:

{% code title="zen-package.edn" %}
```clojure
{:deps {some-other-dep "…"
        loinc "git@github.com:zen-fhir/loinc.git"}}
```
{% endcode %}

You also need to import `loinc` namespace in your Aidbox entrypoint file. Assuming you are working with `docker-compose-template` from [Prerequisites](load-loinc-into-aidbox.md#prerequisites), it is `zrc/system.edn` file.

```clojure
{:ns system
 :import {some-other-dep
          loinc}

 …}
```

#### Instruct Aidbox to load terminologies into the DB

Set the following environment variable:

```
BOX_FEATURES_FTR_PULL_ENABLE=true
```

By default, Aidbox does not load terminologies into the database as that can take a lot of disk space. This means that full terminology functionality won’t be available until you enable it manually. [BOX\_FEATURES\_FTR\_PULL\_ENABLE](../../../reference/configuration/environment-variables/ftr.md) environment variable is just for that. When you set it to `true`, Aidbox will load terminologies into the database on the next startup and start functioning as a fully-featured terminology server.

#### Terminology translations

Currently, we support various LOINC terminology translations, and we can incorporate additional translation variants as needed. Translations are stored in the concept .`designation` property, and when a package includes multiple languages, the original language is also retained within the `.designation` property. To select the desired translation, simply modify the link to the corresponding package in `zen-package.edn.`

<table><thead><tr><th width="383">Zen Package Link</th><th>Languages (IETF BCP 47 tags)</th></tr></thead><tbody><tr><td>git@github.com:zen-fhir/loinc.git</td><td>en</td></tr><tr><td>git@github.com:zen-fhir/loinc-fr-ca.git</td><td>en, fr-ca</td></tr></tbody></table>

#### Further steps

If you want to customize Aidbox startup behavior when using FTR, read more about [FTR environment variables](../../../reference/configuration/environment-variables/ftr.md).

One of the options you may want to consider is enabling`BOX_FEATURES_FTR_BUILD__INDEX__ON__STARTUP_SYNC`. Setting this environment variable to `true` will make Aidbox startup blocked until LOINC terminology is fully available for fast and efficient validation.

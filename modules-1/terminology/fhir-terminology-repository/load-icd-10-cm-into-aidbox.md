---
description: Start working with ICD-10-CM terminology in Aidbox
---

# Load ICD-10-CM into Aidbox

[ICD-10-CM](https://www.cdc.gov/nchs/icd/icd-10-cm.htm) is a large medical terminology which can be used in [FHIR ValueSet](http://hl7.org/fhir/valueset.html) resources. It is distributed under a license which you have to set up with them.

We provide out-of-the box integration with ICD-10-CM through [Aidbox Configuration Project](../../../aidbox-configuration/aidbox-zen-lang-project/). You may start using it after we make sure you have the required ICD-10-CM license.

There is a step-by-step guide below which takes you from our [default Aidbox Configuration project](https://github.com/Aidbox/aidbox-docker-compose) to a fully enabled ICD-10-CM terminology.

### Prerequisites

You need to have an [Aidbox Configuration project](../../../getting-started/installation/) to load prepackaged ICD-10-CM terminology. One of the easiest way is to start with our [Docker Getting started guide](../../../getting-started/run-aidbox/run-aidbox-locally-with-docker.md).

### Step-by-step guide

#### Confirm with us your ICD-10-CM license

ICD-10-CM is distributed under a license which means that we can not redistribute it without making sure that other people have this license. You can confirm your eligibility for accessing ICD-10-CM by contacting Aidbox team. See [our contacts here](../../../overview/contact-us.md).

#### Provide SSH keys to access our prepackaged ICD-10-CM repository

We distribute ICD-10-CM through a private Github repository. This means that you have to provide us with your public SSH key which we’ll add to the repository’s access list.

You also need to set Aidbox environment variables to use this public key and its corresponding private key. They are required so that Aidbox is able to work with private ICD-10-CM repository.

The required environment variables are listed below:

```shell
BOX_PROJECT_GIT_PRIVATE__KEY="-----BEGIN …
…
-----END … -----
"
BOX_PROJECT_GIT_PUBLIC__KEY="ssh-…"
```

Be aware that there’s a newline at the end of `BOX_PROJECT_GIT_PRIVATE__KEY`. Make sure that it is present otherwise the key becomes invalid.

#### Add ICD-10-CM to Aidbox Configuration project dependencies

Edit `zen-package.edn` to include ICD-10-CM repository in the dependencies:

{% code title="zen-package.edn" %}
```clojure
{:deps {some-other-dep "…"
        icd10cm "git@github.com:zen-fhir/icd10-cm.git"}}
```
{% endcode %}

You also need to import `icd10cm` namespace in your Aidbox entrypoint file. Assuming you are working with `docker-compose-template` from [Prerequisites](load-icd-10-cm-into-aidbox.md#prerequisites), it is `zrc/system.edn` file.

```clojure
{:ns system
 :import {some-other-dep
          icd10cm}

 …}
```

#### Instruct Aidbox to load terminologies into the DB

Set the following environment variable:

```
BOX_FEATURES_FTR_PULL_ENABLE=true
```

By default, Aidbox does not load terminologies into the database as that can take a lot of disk space. This means that full terminology functionality won’t be available until you enable it manually. [BOX\_FEATURES\_FTR\_PULL\_ENABLE](../../../reference/configuration/environment-variables/ftr.md) environment variable is just for that. When you set it to `true`, Aidbox will load terminologies into the database on the next startup and start functioning as a fully-featured terminology server.

#### Further steps

If you want to customize Aidbox startup behavior when using FTR, read more about [FTR environment variables](../../../reference/configuration/environment-variables/ftr.md).

One of the options you may want to consider is enabling`BOX_FEATURES_FTR_BUILD__INDEX__ON__STARTUP_SYNC`. Setting this environment variable to `true` will make Aidbox startup blocked until ICD-10-CM terminology is fully available for fast and efficient validation.

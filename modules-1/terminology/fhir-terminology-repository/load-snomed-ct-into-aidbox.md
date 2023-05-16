---
description: Start working with SNOMED CT terminology in Aidbox
---

# Load SNOMED CT into Aidbox

[SNOMED CT](https://www.snomed.org/snomed-ct/Use-SNOMED-CT) is a large medical terminology which can be used in [FHIR ValueSet](http://hl7.org/fhir/valueset.html) resources. It is distributed under a free license which you have to set up with them.

We provide out-of-the-box integration with SNOMED CT through [Aidbox Configuration project](../../../reference/configuration/environment-variables/aidbox-project-environment-variables.md). You may start using it after we make sure you have the required SNOMED license.

There is a step-by-step guide below which takes you from our [default Aidbox Configuration project](https://github.com/Aidbox/aidbox-docker-compose) to a fully enabled SNOMED terminology.

## Prerequisites

You need to have an [Aidbox Configuration project](broken-reference) to load prepackaged SNOMED CT terminology. One of the easiest way is to start with our [Docker Getting started guide](../../../getting-started/run-aidbox/run-aidbox-locally-with-docker.md).

## Step-by-step guide

### Confirm with us your SNOMED license

SNOMED CT is distributed under a license which means that we can not redistribute it without making sure that other people have this license. You can confirm your eligibility for accessing SNOMED CT by contacting Aidbox team. See [our contacts here](https://docs.aidbox.app/contact-us).

### Provide SSH keys to access our prepackaged SNOMED CT repository

We distribute SNOMED CT through a private Github repository. This means that you have to provide us with your public SSH key which we’ll add to the repository’s access list.

You also need to set Aidbox environment variables to use this public key and its corresponding private key. They are required so that Aidbox is able to work with private SNOMED CT repository.

The required environment variables are listed below:

```
BOX_PROJECT_GIT_PRIVATE__KEY="-----BEGIN …
…
-----END … -----
"
BOX_PROJECT_GIT_PUBLIC__KEY="ssh-…"
```

Be aware that there’s a newline at the end of `BOX_PROJECT_GIT_PRIVATE__KEY`. Make sure that it is present otherwise the key becomes invalid.

### Add SNOMED CT to Aidbox Configuration project dependencies

Edit `zen-package.edn` to include SNOMED CT repository in the dependencies:

{% code title="zen-package.edn" %}
```clojure
{:deps {some-other-dep "…"
        snomed "git@github.com:zen-fhir/snomed.git"}}
```
{% endcode %}

You also need to import `snomed` namespace in your Aidbox entrypoint file. Assuming you are working with `docker-compose-template` from [Prerequisites](load-snomed-ct-into-aidbox.md#prerequisites), it is `zrc/system.edn` file.

```clojure
{:ns system
 :import {some-other-dep
          snomed}

 …}
```

### Instruct Aidbox to load terminologies into the DB

Set the following environment variable:

```
BOX_FEATURES_FTR_PULL_ENABLE=true
```

By default, Aidbox does not load terminologies into the database as that can take a lot of disk space. This means that full terminology functionality won’t be available until you enable it manually. [BOX\_FEATURES\_FTR\_PULL\_ENABLE](../../../reference/configuration/environment-variables/ftr.md) environment variable is just for that. When you set it to `true`, Aidbox will load terminologies into the database on the next startup and start functioning as a fully-featured terminology server.

### Terminology translations

Currently, we support various SNOMED CT terminology translations, and we can incorporate additional translation variants as needed. Translations are stored in the concept .`designation` property, and when a package includes multiple languages, the original language is also retained within the `.designation` property. To select the desired translation, simply modify the link to the corresponding package in `zen-package.edn.`

| Zen Package Link                     | Languages (IETF BCP 47 tags) |
| ------------------------------------ | ---------------------------- |
| git@github.com:zen-fhir/snomed.git   | en                           |
| git@github.com:zen-fhir/snomedca.git | en, fr                       |

### Further steps

If you want to customize Aidbox startup behavior when using FTR, read more about [FTR environment variables](../../../reference/configuration/environment-variables/ftr.md).

One of the options you may want to consider is enabling`BOX_FEATURES_FTR_BUILD__INDEX__ON__STARTUP_SYNC`. Setting this environment variable to `true` will make Aidbox startup blocked until SNOMED CT terminology is fully available for fast and efficient validation.

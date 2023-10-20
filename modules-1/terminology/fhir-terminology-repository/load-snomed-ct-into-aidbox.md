---
description: Start working with SNOMED CT terminology in Aidbox
---

# Load SNOMED CT into Aidbox

[SNOMED CT](https://www.snomed.org/snomed-ct/Use-SNOMED-CT) is a large medical terminology which can be used in [FHIR ValueSet](http://hl7.org/fhir/valueset.html) resources. It is distributed under a free license which you have to set up with them.

We provide out-of-the-box integration with SNOMED CT through [Aidbox Configuration project](../../../aidbox-configuration/aidbox-zen-lang-project/). You may start using it after we make sure you have the required SNOMED license.

## Step-by-step guide

### Confirm with us your SNOMED license

SNOMED CT is distributed under a license which means that we can not redistribute it without making sure that other people have this license. You can confirm your eligibility for accessing SNOMED CT by contacting Aidbox team. See [our contacts here](https://docs.aidbox.app/contact-us).

### Setting up Aidbox configuration project

To set up the Aidbox configuration project, carefully follow [this](../../../getting-started-1/run-aidbox/run-aidbox-locally-with-docker.md) guide.&#x20;

During the step labeled `Configure the Aidbox` instead of cloning the proposed configuration projects, clone the following pre-packaged configuration project with the SNOMED-related configuration:

```sh
git clone \
  https://github.com/Panthevm/aidbox-project-template-snomed.git \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```

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

### Configuration overview

#### Added SNOMED dependency to configuration project

{% code title="zen-package.edn" %}
```
{:deps {snomed "https://github.com/zen-fhir/snomed.git"}}
```
{% endcode %}

#### Imported SNOMED namespace to configuration project entrypoint

<pre><code>{ns main
 import #{aidbox
          config
          <a data-footnote-ref href="#user-content-fn-1">snomed</a>}
 …}
</code></pre>

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

## Terminology translations

Currently, we support various SNOMED CT terminology translations, and we can incorporate additional translation variants as needed. Translations are stored in the concept .`designation` property, and when a package includes multiple languages, the original language is also retained within the `.designation` property. To select the desired translation, simply modify the link to the corresponding package in `zen-package.edn.`

| Zen Package Link                     | Languages (IETF BCP 47 tags) |
| ------------------------------------ | ---------------------------- |
| git@github.com:zen-fhir/snomed.git   | en                           |
| git@github.com:zen-fhir/snomedca.git | en, fr                       |

See also [$translate-concepts RPC](../concept/usdtranslate-concepts.md).

[^1]: Namespace we've imported

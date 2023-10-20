---
description: Start working with ICD-10-CM terminology in Aidbox
---

# Load ICD-10-CM into Aidbox

[ICD-10-CM](https://www.cdc.gov/nchs/icd/icd-10-cm.htm) is a large medical terminology which can be used in [FHIR ValueSet](http://hl7.org/fhir/valueset.html) resources. It is distributed under a license which you have to set up with them.

We provide out-of-the box integration with ICD-10-CM through [Aidbox Configuration Project](../../aidbox-configuration/aidbox-zen-lang-project/). You may start using it after we make sure you have the required ICD-10-CM license.

## Step-by-step guide

### Confirm with us your ICD-10-CM license

ICD-10-CM is distributed under a license which means that we can not redistribute it without making sure that other people have this license. You can confirm your eligibility for accessing ICD-10-CM by contacting Aidbox team. See [our contacts here](../../contact-us.md).

### Provide SSH keys to access our prepackaged ICD-10-CM repository

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

### Setting up Aidbox configuration project

To set up the Aidbox configuration project, carefully follow [this](../../getting-started-1/run-aidbox/run-aidbox-locally-with-docker.md) guide.&#x20;

During the step labeled `Configure the Aidbox` instead of cloning the proposed configuration projects, clone the following pre-packaged configuration project with the ICD-10-CM-related configuration:

```sh
git clone \
  https://github.com/Panthevm/aidbox-project-template-icd10cm.git \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```

### Configuration overview

#### Added ICD-10-CM dependency to configuration project

{% code title="zen-package.edn" %}
```
{:deps {icd10cm "git@github.com:zen-fhir/icd10-cm.git"}}
```
{% endcode %}

#### Imported ICD-10-CM namespace to configuration project entrypoint

<pre><code>{ns main
 import #{aidbox
          config
          <a data-footnote-ref href="#user-content-fn-1">icd10cm</a>}
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

[^1]: Namespace we've imported

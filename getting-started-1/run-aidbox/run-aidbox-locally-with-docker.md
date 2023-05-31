---
description: Get up-to-speed with Aidbox features via interactive tutorials.
---

# Run Aidbox locally

## Intro

This guide helps you to launch and explore the Aidbox key features locally through built-in interactive tutorials called Notebooks.

It introduces you to:&#x20;

* The aidbox installation process with Docker & Docker Compose,
* Basic API capabilities like RESTful FHIR and SQL API,
* Aidbox UI features that make the Aidbox instance transparent for you.

{% hint style="warning" %}
<img src="../../.gitbook/assets/docker.png" alt="" data-size="line">&#x20;

Please **make sure** that both [installed Docker & Docker Compose](https://docs.docker.com/engine/install/) are installed.
{% endhint %}

##

## Quickstart Guide

### 1. Get the Aidbox License for self-hosting

Create the Aidbox License for the **14-day trial period** on [https://aidbox.app/](https://aidbox.app/), select **self-hosted,** or use the license that you already have.&#x20;

{% hint style="success" %}
The _<mark style="color:green;"><mark style="color:green;background-color:yellow;">Aidbox License Key<mark style="color:green;background-color:yellow;"></mark>_ will be required in the next step, where we will prepare the configuration for Aidbox.
{% endhint %}

### 2. Configure the Aidbox

Aidbox is configured by dedicated [Aidbox Configuration Projects](../../aidbox-configuration/aidbox-zen-lang-project/).&#x20;

You can start with the default configuration project published on our GitHub and customize it for your specific needs later. Select the FHIR version and clone the corresponding project with the Bash commands below:

{% tabs %}
{% tab title="FHIR R4" %}
```shell
git clone \
  --branch=main \
  --depth=1 \
  https://github.com/Aidbox/aidbox-project-template.git \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```
{% endtab %}

{% tab title="FHIR R4B" %}
```sh
git clone \
  --branch=fhir-r4b \
  --depth=1 \
  https://github.com/Aidbox/aidbox-project-template.git \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```
{% endtab %}

{% tab title="FHIR R5" %}
```shell
git clone \
  --branch=fhir-r5 \
  --depth=1 \
  https://github.com/Aidbox/aidbox-project-template.git \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```
{% endtab %}
{% endtabs %}

Here is the basic structure of the Aidbox Configuration Project:

```
aidbox-project/
├── .env
├── docker-compose.yaml
├── zen-package.edn
└── zrc/
    ├── config.edn
    └── main.edn
```

{% hint style="info" %}
**Aidbox Configuration Projects**

Everything in Aidbox can be configured with a dedicated Aidbox Configuration Project from the FHIR version definition to enabling add-on modules.&#x20;

This approach helps you keep configurations under a version control system and share them between Aidbox Instances.

[Learn more.](broken-reference)
{% endhint %}

#### Add the license key to your configuration project.

Update the **.env** file within your configuration project with the Aidbox License Key from [Step 1](run-aidbox-locally-with-docker.md#1.-get-the-aidbox-license-with-a-self-hosting-option):&#x20;

{% code title=".env" %}
```ini
AIDBOX_LICENSE=YOUR_AIDBOX_LICENSE_KEY

PGHOSTPORT=5437
PGUSER=postgres
...
```
{% endcode %}

### 3. Start Aidbox with Docker Compose

Start Aidbox with Docker Compose:

```shell
docker compose up --force-recreate
```

Navigate to [http://localhost:8888/](http://localhost:8888/) and Sign In to the Aidbox UI using the login `admin` and password `password`.

### 4. Discover Aidbox features with Notebooks

Go to the Notebooks section within [the Aidbox UI](../../overview/aidbox-ui/) and open Getting Started Notebooks.&#x20;

Use the pre-defined Getting Started Notebooks to explore the basic features of Aidbox through interactive steps with API queries:&#x20;

<figure><img src="../../.gitbook/assets/notebooks.png" alt=""><figcaption></figcaption></figure>

{% hint style="info" %}
**Aidbox Notebooks**

Notebooks are interactive tutorials within the Aidbox UI with built-in REST, RPC, and SQL editors and the ability to execute requests and queries on the fly and see the result. You can use pre-built or create your own Notebooks. [Learn more.](../../overview/aidbox-ui/notebooks.md)
{% endhint %}



## Next Steps

* Learn more about [Aidbox Configuration](../../aidbox-configuration/aidbox-zen-lang-project/)
* Unlock additional capabilities of [Aidbox UI](../../overview/aidbox-ui/)
* Dive into the built-in [Aidbox Notebooks](../../overview/aidbox-ui/notebooks.md)


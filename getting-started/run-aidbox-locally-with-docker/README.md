---
description: >-
  Aidbox project is a configuration of Aidbox instance, written in zen-lang.
  This getting started guide shows you how to setup a new Aidbox project.
---

# Run Aidbox locally with Docker

## Init & run Aidbox project

In order to run Aidbox locally, you need to have [Docker & Docker compose installed](https://docs.docker.com/engine/install/). To begin with new aidbox project,&#x20;

* run the following command depending on FHIR version you want to have

{% tabs %}
{% tab title="FHIR R4" %}
```shell
git clone \
  --branch=main \
  --depth=1 \
  git@github.com:Aidbox/aidbox-project-template.git \
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
  git@github.com:Aidbox/aidbox-project-template.git \
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
  git@github.com:Aidbox/aidbox-project-template.git \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```
{% endtab %}
{% endtabs %}

* get your Aidbox license on [aidbox.app](https://aidbox.app/)
* and then set `AIDBOX_LICENSE` env variable in .env file

Once you set up the project and provided the license key, you may start Aidbox service with Docker compose by running:

```shell
docker compose up --force-recreate
```

Open [http://localhost:8888](http://localhost:8888) and see Aidbox login page. Sign in using login `admin` and password `password`.

{% hint style="info" %}
`--force-recreate` argument for Docker Compose makes Docker to pull the latest Aidbox version if you have already pulled it before.
{% endhint %}

Now you are ready to use Aidbox. Let's see, what have we created.

## Aidbox project structure

Aidbox project folder contains configuration files written in [zen-lang](https://github.com/zen-lang/zen) and the docker-compose.yaml file to start Aidbox locally for development purposes.

```
aidbox-project/
├── .env
├── docker-compose.yaml
├── zen-package.edn
└── zrc/
    ├── config.edn
    └── main.edn
```

### zrc/main.edn

The main.edn file contains `main/box` entrypoint for your Aidbox configuration.

```clojure
{ns main
 import {aidbox
         config}
 
 box
 {:zen/tags #{aidbox/system}
  :config   config/base-config
  :services {:admin-user-seed config/admin-user-seed
             :root-client-seed config/root-client-seed}}}
```

The config.edn file, imported by main.edn, contains configuration variables for Aidbox.

### zen-package.edn

The zen-package.edn file is the project meta file. You can specify dependencies for external zen packages there.

```
{:deps {hl7-fhir-r4-core "https://github.com/zen-fhir/hl7-fhir-r4-core.git"}}
```

## Troubleshooting

### Could not find main/box

If you see the message in logs&#x20;

```
:entrypoint/error Could not find main/box
```

it means, Aidbox project wasn't configured correctly.

## That's it

Now you have Aidbox project initialized and you ready to develop with Aidbox.

### What's next

Read more on [how to setup US core IG with Aidbox project](https://docs.aidbox.app/tutorials/fhir-conformance/how-to-enable-us-core-ig/start-aidbox-with-us-core-ig-enabled).

## Talk to a Health Samurai Engineer

If you'd like to learn more about using Aidbox or have any questions about this guide, [connect with us on Telegram](https://t.me/aidbox). We're happy to help.

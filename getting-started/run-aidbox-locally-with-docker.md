---
description: >-
  Aidbox project is a configuration of Aidbox instance, written in zen-lang.
  This getting started guide shows you how to setup a new Aidbox project.
---

# Run Aidbox locally with Docker

#### Setting FHIR version

By default, configuration project selects `FHIR R4` version and `hl7.fhir.r4.core` zen package respectively, to change this, do following:

**FHIR R5**

Update `project/zrc/system.edn` file:

```clojure
:fhir-version            "5.0.0"
```

Update `project/zen-package.edn`, add  corresponding dependency:

```clojure
{:deps {hl7-fhir-r5-core "https://github.com/zen-fhir/hl7-fhir-r5-core.git"}}
```

**FHIR R4B**

Update `project/zrc/system.edn` file:

```clojure
:fhir-version            "4.3.0"
```

Update `project/zen-package.edn`, add  corresponding dependency:

```clojure
{:deps {hl7-fhir-r4b-core "https://github.com/zen-fhir/hl7-fhir-r4b-core.git"}}
```

{% hint style="info" %}
Don't forget to commit your changes to zen configuration project:\
`git add --all && git commit -m "<your_commit_message>"`
{% endhint %}

## Init & run Aidbox project

In order to run Aidbox locally, you need to have [Docker & Docker compose installed](https://docs.docker.com/engine/install/). To begin with new aidbox project,&#x20;

* run the following command

```sh
git clone --depth=1 --branch=main git@github.com:Aidbox/aidbox-project-template.git aidbox-project && cd aidbox-project && rm -rf .git
```

* get your Aidbox license on [aidbox.app](https://aidbox.app/),
* and then set `AIDBOX_LICENSE` env variable in .env file.

Once you set up the project and provided the license key, you may run Aidbox service with Docker compose by running:

```shell
docker compose up --force-recreate
```

Open [http://localhost:8888](http://localhost:8888). You'll see Aidbox login page. Sign in using login admin and password secret.

{% hint style="info" %}
`--force-recreate` argument for Docker Compose will make Docker pull the latest Aidbox version, if you already have on pulled before.
{% endhint %}

Now you are ready to use Aidbox. Let's see, what have we created.

## Aidbox project structure

Aidbox project folder contains configuration files written in [zen-lang](https://github.com/zen-lang/zen) and docker-compose.yaml file to start Aidbox locally for development purposes.

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

main.edn file contains `main/box` entrypoint for your Aidbox configuration.

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

config.edn file, imported by main.edn, contains configuration variables for Aidbox.

### zen-package.edn

zen-package.edn file is a project meta file. You can specify dependencies for external zen packages there.

```
{:deps {hl7-fhir-r4-core "https://github.com/zen-fhir/hl7-fhir-r4-core.git"}}
```

## That's it

Now you have Aidbox project initialised and you ready to develop with Aidbox.

### What's next

Read more on [how to setup US core IG with Aidbox project](https://docs.aidbox.app/tutorials/fhir-conformance/how-to-enable-us-core-ig/start-aidbox-with-us-core-ig-enabled).

## Talk to a Health Samurai Engineer

If you'd like to learn more about using Aidbox or have any questions about this guide, [connect with us on Telegram](https://t.me/aidbox). We're happy to help.

## Troubleshooting

### Could not find main/box

If you see in logs the message

```
:entrypoint/error Could not find main/box
```

it means, aidbox project wasn't configured correctly.

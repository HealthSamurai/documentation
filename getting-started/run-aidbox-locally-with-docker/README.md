# Run Aidbox locally with Docker

This quickstart guide explains how to run Aidbox locally using docker compose. You will learn how to obtain a free short-term license and how to set up Aidbox.

### Get a license

Go to the [Aidbox portal](https://aidbox.app). Sign up and click the new license button. Choose product type “Aidbox” and hosting type “Self-hosted”.

You'll see your license in the “My Licenses” list. Click on your new license and copy credentials. It is a long string like

```
eyJhbGciOiJ...
```

This string is your license key.

### Install Docker and Docker Compose

Follow the [official Docker guide](https://docs.docker.com/compose/install/#install-compose) to install Docker and Docker Compose.

### Clone and set up Aidbox template repository

```shell
git clone https://github.com/Aidbox/aidbox-docker-compose --recurse-submodules && cd aidbox-docker-compose
```

This repository contains [Aidbox configuration project](../../aidbox-configuration/aidbox-zen-lang-project/) used to configure Aidbox and also additional files used to spin up Aidbox container.

Create `.env` file from `env.tpl`:

```bash
cp env.tpl .env
```

`env.tpl` is a template with environment variables. We copy it into a separate `.env` which is included in `.gitignore` to protect from secrets breach.

Now update `.env` file so that `AIDBOX_LICENSE` matches your license key:

```shell
AIDBOX_LICENSE=<your-license-key>
```

If you want to learn more about Aidbox environment variables, refer to the following page:

{% content-ref url="../../reference/configuration/environment-variables/" %}
[environment-variables](../../reference/configuration/environment-variables/)
{% endcontent-ref %}

If you want to learn more about Aidbox configuration project set up, refer to the following page:

{% content-ref url="../../aidbox-configuration/aidbox-zen-lang-project/setting-up-a-configuration-project.md" %}
[setting-up-a-configuration-project.md](../../aidbox-configuration/aidbox-zen-lang-project/setting-up-a-configuration-project.md)
{% endcontent-ref %}

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

### Lauch Aidbox

Start Aidbox with Docker Compose:

```shell
docker compose up
```

This command downloads and starts Aidbox container. It can take a few minutes.

### Go to the Aidbox UI

Open [http://localhost:8888](http://localhost:8888). You'll see Aidbox login page. Sign in using login `admin` and password `secret`.

Now you are ready to use Aidbox.

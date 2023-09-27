---
description: Get up-to-speed with Aidbox observability features via interactive tutorials.
---

# Run Aidbox with OpenTelemetry locally

## Intro

This guide helps you to launch and explore the Aidbox observability features locally.

It introduces you to:

* The aidbox installation process with Docker & Docker Compose,
* Logs & metrics exporting to Elasticsearch & Prometheus using OpenTelemetry collector service.

{% hint style="warning" %}
<img src="../../.gitbook/assets/docker.png" alt="" data-size="original">

Please **make sure** that both [Docker & Docker Compose](https://docs.docker.com/engine/install/) are installed.
{% endhint %}

## Quickstart Guide

### 1. Get the Aidbox License for self-hosting

Create the Aidbox License for the **14-day trial period** on [https://aidbox.app/](https://aidbox.app/), select **self-hosted,** or use the license that you already have.

{% hint style="success" %}
The _<mark style="color:green;background-color:yellow;">Aidbox License Key</mark>_ will be required in the next step, where we will prepare the configuration for Aidbox.
{% endhint %}

### 2. Configure the Aidbox

Aidbox is configured by dedicated [Aidbox Configuration Projects](../../aidbox-configuration/aidbox-zen-lang-project/).

You can start with the default configuration project published on our GitHub and customize it for your specific needs later. Select the FHIR version and clone the corresponding project with the Bash commands below:

```
git clone \
  --branch=open-telemetry-fhir-r5 \
  --depth=1 \
  https://github.com/Aidbox/aidbox-project-template.git \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```

Here is the basic structure of the Aidbox Configuration Project:

```
aidbox-project/
├── .env
├── docker-compose.yaml
├── open-telemetry-config.yaml
├── prometheus
│   └── prometheus.yml
├── grafana
│   └── datasources
│       └── all.yaml
├── zen-package.edn
└── zrc/
    ├── config.edn
    └── main.edn
```

{% hint style="info" %}
**Aidbox Configuration Projects**

Everything in Aidbox can be configured with a dedicated Aidbox Configuration Project from the FHIR version definition to enabling add-on modules.

This approach helps you keep configurations under a version control system and share them between Aidbox Instances.

[Learn more.](../../getting-started-1/run-aidbox/broken-reference/)
{% endhint %}

#### Add the license key to your configuration project.

Update the **.env** file within your configuration project with the Aidbox License Key from [Step 1](run-aidbox-locally-with-docker.md#1.-get-the-aidbox-license-with-a-self-hosting-option):

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

### 4. Discover Aidbox logs with Kibana

Kibana should be available on [http://localhost:5602](http://localhost:5602) address. To view logs in Kibana we need to&#x20;

* make sure that logs are coming to Elasticsearch and then&#x20;
* create a data view to observe over logs.

Go to [Index Management](http://localhost:5602/app/management/data/index\_management/indices) page in Kibana (Menu → Stack Management → Index Management). You should see `aidbox_logs` index there.

<figure><img src="../../.gitbook/assets/Screenshot 2023-09-27 at 15.19.04.png" alt=""><figcaption><p>Index management page</p></figcaption></figure>

Then we may go to [Data Views](http://localhost:5602/app/management/kibana/dataViews) page (Menu → Stack Management → Data Views) and create a data view there.

<figure><img src="../../.gitbook/assets/Screenshot 2023-09-27 at 15.19.50.png" alt=""><figcaption><p>Data view creation</p></figcaption></figure>

Then go to [Discover](http://localhost:5602/app/discover) page (Menu → Discover) and observe your logs.

<figure><img src="../../.gitbook/assets/Screenshot 2023-09-27 at 15.21.57.png" alt=""><figcaption><p>Log discover page</p></figcaption></figure>

### 5. Discover Aidbox metrics with Prometheus

Prometheus should be available on [http://localhost:9090](http://localhost:9090) address. To view metrics in Prometheus we need just to open it and search for it.

<figure><img src="../../.gitbook/assets/Screenshot 2023-09-27 at 15.26.34.png" alt=""><figcaption><p>Prometheus UI</p></figcaption></figure>

## Next Steps

* Learn more about [Aidbox Configuration](../../aidbox-configuration/aidbox-zen-lang-project/)

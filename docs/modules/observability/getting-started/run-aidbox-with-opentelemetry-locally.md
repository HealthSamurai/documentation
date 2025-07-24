---
description: Get up-to-speed with Aidbox observability features via interactive tutorials.
---

# Run Aidbox with OpenTelemetry locally

## Intro

This guide helps you to launch and explore the Aidbox observability features locally.

It introduces you to:

* The Aidbox installation process with Docker & Docker Compose,
* Logs & metrics & traces exporting to Elasticsearch & Prometheus & Zipkin using OpenTelemetry collector service.

<figure><img src="../../../../.gitbook/assets/f1142038-ab89-4ab2-93a7-5d7627bd088c.png" alt=""><figcaption></figcaption></figure>

{% hint style="warning" %}
<img src="../../../../.gitbook/assets/docker.png" alt="" data-size="original">

Please **make sure** that both [Docker & Docker Compose](https://docs.docker.com/engine/install/) are installed.
{% endhint %}

### Prerequisites

* [Docker](https://www.docker.com/)
* Cloned repository: [Github: Aidbox/examples](https://github.com/Aidbox/examples/tree/main)
* Working directory: `OpenTelemetry`

To clone the repository and navigate to the `OpenTelemetry` directory, run:

```sh
git clone git@github.com:Aidbox/examples.git && cd examples/OpenTelemetry
```

### 1. Start Aidbox and demo components with Docker Compose

```shell
docker compose up --force-recreate
```

Wait until all components are pulled and started.

### 2. Activate Aidbox

Open Aidbox on [http://localhost:8080](http://localhost:8080) address, activate it, and login with `admin`/`password` credentials

### 3. Discover Aidbox logs with Kibana

Kibana should be available on [http://localhost:5602](http://localhost:5602) address. To see logs in Kibana we should&#x20;

* make sure that logs are coming to Elasticsearch and then&#x20;
* create a data view to observe over logs.

Go to [Index Management](http://localhost:5602/app/management/data/index_management/indices) page in Kibana (Menu → Stack Management → Index Management). You should see `aidbox_logs` index there.

<figure><img src="../../../../.gitbook/assets/5a4c8db2-41ee-4bc2-815c-753fac7887f4.png" alt=""><figcaption><p>Index management page</p></figcaption></figure>

Then we should go to [Data Views](http://localhost:5602/app/management/kibana/dataViews) page (Menu → Stack Management → Data Views) and create a data view there.

<figure><img src="../../../../.gitbook/assets/6c139544-8975-428a-bdbc-63666d5e15b7.png" alt=""><figcaption><p>Data view creation</p></figcaption></figure>

Then go to [Discover](http://localhost:5602/app/discover) page (Menu → Discover) and observe your logs.

<figure><img src="../../../../.gitbook/assets/f5297a2b-1592-4a25-abbe-d45941f68a8c.png" alt=""><figcaption><p>Log discover page</p></figcaption></figure>

### 4. Discover Aidbox metrics with Grafana

Grafana should be available on [http://localhost:3001](http://localhost:3001) address, login with `admin`/`password` credentials.

<figure><img src="../../../../.gitbook/assets/grafana-login.png" alt=""><figcaption><p>Grafana LogIn</p></figcaption></figure>

Navigate to `Dashboards->Aidbox Dashboards->Aidbox Dashboard` to see the dashboard. You probably need to wait for a while until the data is collected.

<figure><img src="../../../../.gitbook/assets/grafana-dashboard.png" alt=""><figcaption><p>Grafana Dashboard</p></figcaption></figure>

### 5. Discover Aidbox traces with Zipkin

Zipkin should be available on [http://localhost:9411/](http://localhost:9411/zipkin/) address. To see traces in Zipkin click the `Run query` button.

<figure><img src="../../../../.gitbook/assets/8f6a613a-c0fa-4f78-bcd8-587509f16a20.png" alt=""><figcaption><p>Traces list in Zipkin</p></figcaption></figure>

Click the `Show` button to see all spans of a certain request.

<figure><img src="../../../../.gitbook/assets/870cbd14-7055-400d-8f6b-17460bf492a4.png" alt=""><figcaption><p>Trace view in Zipkin</p></figcaption></figure>

## Next Steps

* Learn more about [Aidbox Configuration](../../../deprecated/deprecated/zen-related/aidbox-zen-lang-project/README.md)

# Run Multibox locally

This quickstart guide explains how to run Multibox locally using `docker compose`. You will learn how to obtain a free short-term license and set up Multibox.

## Get a license

[Contact HealthSamurai](../../contact-us.md) and get a Multibox license. It is a long string like

```
eyJhbGciOiJ...
```

This string is your license key.

#### Install Docker and Docker Compose

Follow the [official Docker guide](https://docs.docker.com/compose/install/#install-compose) to install Docker and Docker Compose

## Get docker compose

Follow the Getting Started Aidbox guide to get the recommended `docker-compose.yaml`file.

{% content-ref url="../../getting-started/run-aidbox-locally.md" %}
[run-aidbox-locally.md](../../getting-started/run-aidbox-locally.md)
{% endcontent-ref %}

## Change aidbox to multibox

Change image name from aidboxone to multibox like this:

{% code title="docker-compose.yaml" %}
```
image: healthsamurai/multibox:edge
```
{% endcode %}

## Create .env file

To start Multibox needs additional environment variables. Pass the following:

{% code title="docker-compose.yaml" %}
```shell
    environment:
      AIDBOX_LICENSE: <your-license-key>
      AIDBOX_CLUSTER_SECRET: 'secret'
      AIDBOX_CLUSTER_DOMAIN: '127.0.0.1.nip.io'
      AIDBOX_SUPERUSER: 'admin:secret'
      AIDBOX_BASE_URL: 'http://127.0.0.1.nip.io:8080'
```
{% endcode %}

Insert your license key into the environment file. Change the line

```shell
AIDBOX_LICENSE=<your-license-key>
```

where `<your-license-key>` is a license key.

## Launch Multibox

Start Multibox with Docker Compose

```shell
docker compose up --force-recreate
```

This command will download and start Multibox and its dependencies. This can take a few minutes.

## Go to the Multibox UI

Open [http://127.0.](http://127.0.0.1.nip.io:8888)`.env` file.[0.1.nip.io:8888](http://127.0.0.1.nip.io:8888).&#x20;

You'll see Multibox login page. Sign in using the credentials specified in the `AIDBOX_SUPERUSER` variable (`admin`/`secret`).

Now you are ready to use Multibox.

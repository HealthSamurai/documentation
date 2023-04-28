# Run Multibox locally with Docker

This quickstart guide explains how to run Multibox locally using docker compose. You will learn how to obtain a free short-time license and set up Multibox.

### Get a license

Go to the [Aidbox portal](https://aidbox.app). Sign up and click the new license button. Choose product type "Multibox".

You'll see your license in the "My Licenses" list. Click on your new license and copy credentials. It is a long string like

```
eyJhbGciOiJ...
```

This string is your license key.

### Install Docker and Docker Compose

Follow the [official Docker guide](https://docs.docker.com/compose/install/#install-compose) to install Docker and Docker Compose

### Create docker-compose.yaml

Firstly, let's make the configuration file. There are two services: `aidbox-db` and `multibox`. The first one is PostgreSQL database and the second one is the Multibox itself.

{% code title="docker-compose.yaml" %}
```yaml
version: '3.7'
services:
  aidbox-db:
    image: "${PGIMAGE}"
    ports:
      - "${PGHOSTPORT}:${PGPORT}"
    volumes:
    - "./pgdata:/data"
    environment:
      POSTGRES_USER:     "${PGUSER}"
      POSTGRES_PASSWORD: "${PGPASSWORD}"
      POSTGRES_DB:       "${PGDATABASE}"

  multibox:
    image: "${AIDBOX_IMAGE}"
    pull_policy: always
    depends_on: ["aidbox-db"]
    links:
      - "aidbox-db:database"
    ports:
      - "${AIDBOX_PORT}:${AIDBOX_PORT}"
    env_file:
      - .env
    environment:
      PGHOST: database
```
{% endcode %}

### Create .env file

To configure Multibox we need to pass environment variables to it. We can pass them with `.env` file.

{% code title=".env" %}
```shell
# postgres image to run
PGIMAGE=healthsamurai/aidboxdb:14.2

# aidbox image to run
# AIDBOX_IMAGE=healthsamurai/aidboxone:stable
AIDBOX_IMAGE=healthsamurai/multibox:edge

# license details
AIDBOX_LICENSE=<your-license-key>

# if you got pair of id and key use this instead
# AIDBOX_LICENSE_ID=<your-license-id>
# AIDBOX_LICENSE_KEY=<your-license-key>

# port to run webserver at
AIDBOX_PORT=8888
AIDBOX_FHIR_VERSION=4.0.1

# db connection params
PGPORT=5432
PGHOSTPORT=5437
PGUSER=postgres
PGPASSWORD=postgres
PGDATABASE=aidbox

AIDBOX_CLUSTER_SECRET=secret
AIDBOX_CLUSTER_DOMAIN=127.0.0.1.nip.io
AIDBOX_SUPERUSER=admin:secret
AIDBOX_BASE_URL=http://${AIDBOX_CLUSTER_DOMAIN}:${AIDBOX_PORT}

AIDBOX_COMPLIANCE=enabled
```
{% endcode %}

Insert your license key into environment file. Change the line

```shell
AIDBOX_LICENSE=<your-license-key>
```

in the `.env` file where `<your-license-key>` is a license key which you obtained on the [get a license](run-multibox-locally-with-docker.md#get-a-license) step.

You can find more about required Multibox environment variables [here.](../../reference/configuration/environment-variables/multibox-required-environment-variables.md)

### Launch Multibox

Start Multibox with Docker Compose

```shell
docker compose up --force-recreate
```

This command will download and start Multibox and its dependencies. This can take a few minutes.

### Go to the Multibox UI

Open [http://127.0.0.1.nip.io:8888](http://127.0.0.1.nip.io:8888). You'll see Multibox login page. Sign in using credentials specified in `AIDBOX_SUPERUSER` variable in `.env` file.

Now you are ready to use Multibox.

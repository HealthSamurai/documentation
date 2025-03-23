---
description: >-
  This guide shows you how to migrate from Multibox instance to few Aidbox
  instances
---

# Migrate from Multibox to Aidbox

Let's say you have Multibox deployed locally. And you have a couple of boxes launched there.

* `fhirr4box` box with FHIR R4 version
* `fhirr5box` box with FHIR R5 version

To migrate them to Aidbox instance, you need to issue an Aidbox license for each box in multibox.

Stop Multibox instance

```
docker compose stop multibox
```

## Docker compose file

Add two new Aidbox instances to docker compose file

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

  fhirr4box:
    image: healthsamurai/aidboxone:edge
    pull_policy: always
    depends_on: ["aidbox-db"]
    ports:
      - "8888:${AIDBOX_PORT}"
    env_file:
      - .env
    environment:
      AIDBOX_LICENSE: <license-for-fhirr4box>
      AIDBOX_FHIR_VERSION: "4.0.1"
      PGDATABASE: fhirr4box
      PGPORT: 5432
      PGHOST: aidbox-db

  fhirr5box:
    image: healthsamurai/aidboxone:edge
    pull_policy: always
    depends_on: ["aidbox-db"]
    ports:
      - "9999:${AIDBOX_PORT}"
    env_file:
      - .env
    environment:
      AIDBOX_LICENSE: <license-for-fhirr5box>
      AIDBOX_FHIR_VERSION: "5.0.0"
      PGDATABASE: fhirr5box
      PGPORT: 5432
      PGHOST: aidbox-db

```

{% hint style="warning" %}
If you use Aidbox configuration project, you should also pass BOX\_PROJECT\_GIT\_TARGET\_\_PATH and AIDBOX\_ZEN\_ENTRYPOINT.
{% endhint %}

## .env file

As we specified `AIDBOX_LICENSE, AIDBOX_FHIR_VERSION and PGDATABASE` for every Aidbox instance, we should remove it from .env file.

Multibox requires `AIDBOX_SUPERUSER` env variable to be set. Aidbox instance creates admin user and root client resources at startup, and expects 4 env variables instead of one `AIDBOX_SUPERUSER`:

```
AIDBOX_ADMIN_ID=admin
AIDBOX_ADMIN_PASSWORD=password
AIDBOX_CLIENT_ID=root
AIDBOX_CLIENT_SECRET=secret

# not needed envs by Aidbox
# AIDBOX_SUPERUSER=...
# AIDBOX_LICENSE=...
# AIDBOX_FHIR_VERSION=...
# PGDATABASE=...
```

## Run Aidbox instances

Now you can run your aidbox instances

```
docker compose up -d fhirr4box fhirr5box
```

`fhirr4box` will be accessible on `http://localhost:8888`, and `fhirr5box` will be accessible on `http://localhost:9999`.

## Talk to a Health Samurai Engineer

If you'd like to learn more about using Aidbox or have any questions about this guide, [connect with us on Telegram](https://t.me/aidbox). We're happy to help.

# Set up SMARTbox

### Get licenses

Go to the [Aidbox user portal](https://aidbox.app) and request 2 "on-premises" Aidbox licenses. It is a long string like

```
eyJhbGciOiJ...
```

This string is your license key.

### Install Docker and Docker Compose

Follow the [official Docker guide](https://docs.docker.com/compose/install/#install-compose) to install Docker and Docker Compose

### Configure cloud storage

GCP Cloud Storage is used in Bulk API for storing and distributing exported data. To enable bulk API, you need to create GCP Cloud Storage, set up GCP Service Account, and provide full access to the service account on this Cloud Storage.

{% content-ref url="../../storage-1/gcp-cloud-storage.md" %}
[gcp-cloud-storage.md](../../storage-1/gcp-cloud-storage.md)
{% endcontent-ref %}

### **Set up Mailgun**

[Mailgun](https://www.mailgun.com/) is used to communicate with users (developers, patients). It sends emails for resetting a password, email verification, etc.

{% content-ref url="../../tutorials/mailgun-integration.md" %}
[mailgun-integration.md](../../tutorials/mailgun-integration.md)
{% endcontent-ref %}

## Installation

### **Create docker-compose.yaml**

Make the configuration file. There are two services: aidbox-db and aidbox. The first one is PostgreSQL database and the second one is Aidbox itself.

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

  portal:
    image: "${AIDBOX_IMAGE}"
    depends_on: ["aidbox-db"]
    links:
      - "aidbox-db:database"
      - "sandbox:sandbox"
    ports:
      - "8888:8888"
    env_file:
      - .env
    environment:
      PGHOST: "database"
      PGDATABASE: "portal"
      SMARTBOX_SANDBOX_HOST: "http://sandbox:8888"
      SMARTBOX_SANDBOX_BASIC: "root:secret"
      BOX_AUTH_LOGIN__REDIRECT: "/admin/portal"
      BOX_PROJECT_ENTRYPOINT: "smartbox.portal/box"
      AIDBOX_LICENSE: "<YOUR_AIDBOX_LICENSE_FOR_PORTAL>"
      AIDBOX_BASE_URL: "http://localhost:8888"

  sandbox:
    image: "${AIDBOX_IMAGE}"
    depends_on: ["aidbox-db"]
    links:
      - "aidbox-db:database"
    ports:
      - "9999:8888"
    env_file:
      - .env
    environment:
      PGHOST: "database"
      PGDATABASE: "sandbox"
      BOX_AUTH_LOGIN__REDIRECT: "/"
      BOX_PROJECT_ENTRYPOINT: "smartbox.dev-portal/box"
      AIDBOX_LICENSE: "<YOUR_AIDBOX_LICENSE_FOR_SANDBOX>"
      AIDBOX_BASE_URL: "http://localhost:9999"
```

### **Create .env file**

To configure Aidbox we need to pass environment variables to it. We can pass them with .env file.

```
# postgres image to run
PGIMAGE=healthsamurai/aidboxdb:14.2

# aidbox image to run
# AIDBOX_IMAGE=healthsamurai/aidboxone:stable
AIDBOX_IMAGE=healthsamurai/aidboxone:edge

# Client to create on startup
AIDBOX_CLIENT_ID=root
AIDBOX_CLIENT_SECRET=secret

# root user to create on startup
AIDBOX_ADMIN_ID=admin
AIDBOX_ADMIN_PASSWORD=secret

# db connection params
PGPORT=5432
PGHOSTPORT=5437
PGUSER=postgres
PGPASSWORD=postgres
PGDATABASE=postgres
AIDBOX_PORT=8888

# Compliance mode
AIDBOX_COMPLIANCE=enabled
AIDBOX_CREATED_AT_URL=http://fhir.aidbox.app/extension/createdat
BOX_COMPATIBILITY_VALIDATION_JSON__SCHEMA_REGEX=#{:fhir-datetime}
AIDBOX_FHIR_VERSION=4.0.1
BOX_AUTH_GRANT__PAGE__URL=/auth/consent-screen
BOX_COMPATIBILITY_AUTH_PKCE_CODE__CHALLENGE_S256_CONFORMANT=true

# Mailgun
BOX_PROVIDER_MAILGUN__PROVIDER_TYPE=mailgun
BOX_PROVIDER_MAILGUN__PROVIDER_FROM=<YOUR_MAILGUN_FROM_EMAIL>
BOX_PROVIDER_MAILGUN__PROVIDER_USERNAME=<YOUR_MAILGUN_USERNAME>
BOX_PROVIDER_MAILGUN__PROVIDER_PASSWORD=<YOUR_MAILGUN_PASSWORD>
BOX_PROVIDER_MAILGUN__PROVIDER_URL=<YOUR_MAILGUN_URL>

# GCP Cloud Storage
BOX_BULK__STORAGE_BACKEND=gcp
BOX_BULK__STORAGE_GCP_SERVICE__ACCOUNT=gcp-acc
BOX_BULK__STORAGE_GCP_SERVICE__ACCOUNT__EMAIL=<YOUR_SERVICE_ACCOUNT_EMAIL>
BOX_BULK__STORAGE_GCP_SERVICE__ACCOUNT__PRIVATE__KEY=<YOUR_SERVICE_ACCOUNT_PRIVATE_KEY>
BOX_BULK__STORAGE_GCP_BUCKET=<YOUR_GCP_BUCKET_NAME>

# Aidbox project
BOX_PROJECT_GIT_PROTOCOL=https
BOX_PROJECT_GIT_URL=https://github.com/Aidbox/aidbox-project-samples.git
BOX_PROJECT_GIT_SUB__PATH=aidbox-project-samples/smartbox-single-tenant
```

### Launch Aidbox

Start Aidbox with Docker Compose

```shell
docker compose up
```

This command will download and start Aidbox and its dependencies. This can take a few minutes.

FHIR portal is available on

```
"http://localhost:8888"
```

Developer sandbox is available on

```
"http://localhost:9999"
```

### Create a FHIR portal admin resource

```yaml
PUT /User/administrator
content-type: text/yaml
accept: text/yaml

email: example@email.io
password: 'secret'
roles:
  - type: operator
active: true
```

Log in to the admin interface using specified credentials

```
http://localhost:8888/admin/portal#/welcome
```


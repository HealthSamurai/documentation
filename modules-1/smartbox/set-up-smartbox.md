---
description: >-
  This tutorial will guide you through SMARTbox installation and accepting
  developer SMART application process
---

# Set up SMARTbox

### Get licenses

Go to the [Aidbox user portal](https://aidbox.app) and request 2 "on-premises" Aidbox licenses for Portal and Develop Sandbox. It is a long string like

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

Create a `docker-compose.yaml` file and paste there following content.

```yaml
# docker-compose.yaml
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
      AIDBOX_LICENSE: "${PORTAL_LICENSE}"
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
      AIDBOX_LICENSE: "${SANDBOX_LICENSE}"
      AIDBOX_BASE_URL: "http://localhost:9999"
```

There are three services: aidbox-db, smartbox and developer sandbox. The first one is PostgreSQL database and the other ones are Aidboxes.

### **Create .env file**

To configure Aidbox we need to pass environment variables to it. We can pass them with .env file.

```
PORTAL_LICENSE=<YOUR_PORTAL_LICENSE>
SANDBOX_LICENSE=<YOUR_SANDBOX_LICENSE>

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

BOX_FEATURES_TERMINOLOGY_IMPORT_SYNC=true
```

{% hint style="info" %}
By default Aidbox logs are turned off, you can enable them by setting:

* `AIDBOX_STDOUT_JSON=true`
{% endhint %}

### Launch Aidbox

At first init AidboxDB with Docker Compose

```bash
docker compose pull && docker compose up aidbox-db
```

Wait till AidboxDB is ready to accept connections. You will the following log message:&#x20;

```
LOG: database system is ready to accept connections
```

Stop AidboxDB with `Ctrl+C` and start all three services with Docker Compose:

```shell
docker compose up
```

Now SMARTbox is ready.

### Create admin portal

Open the login page on the Portal [http://localhost:8888/auth/login](http://localhost:8888/auth/login) and use your credentials to login.&#x20;

Aidbox creates admin user at first start using env variables `AIDBOX_ADMIN_ID` and `AIDBOX_ADMIN_PASSWORD`.

Once you logged into, open Aidbox Console on [http://localhost:8888/ui/console](http://localhost:8888/ui/console). And open Users tab on the left navigation panel and create New user by clicking the New button on the top right corner of the screen. Paste the following User resource and save it.

```yaml
resourceType: User

email: operator@example.com
password: secret

name:
  givenName: Test
  familyName: Admin
roles:
  - type: operator
active: true
```

Logout from Aidbox Console by clicking the Logout button on the left bottom corner of the screen.

### Admin portal

To get to admin portal:

* Open the login page on the Portal [http://localhost:8888/auth/login](http://localhost:8888/auth/login),
* Use the email and password from the previous step as credentials

On the admin portal you can manage apps, patients and other admins.

### Developer sandbox

#### Register developer

Submit developer registration form

* Open Developer Sandbox on [http://localhost:9999](http://localhost:9999)
* Click the Sign Up button
* Register a new developer

Once you submitted the developer registration form, you should receive an email with the verification link.

* Follow the link to confirm your email address.
* You will be redirected on creation password form
* Create a password, submit it.

Now you can Sign In as developer to the Developer Sandbox.

#### Create a SMART app in developer sandbox

Once you logged into Developer Sandbox as a developer, you can create a new SMART Application&#x20;

* Click the Create app button
* Fill out and submit the new app form
* Click the Submit for Review button to send the application to review

#### Approve SMART App Publishing Request

Go back to Administrator Portal on [http://localhost:8888](http://localhost:8888). You will see list of SMART App waiting for review.

* Open the review request, made on the previous step
* Click the Approve button.

Go to [Apps page](http://localhost:8888/admin/portal#/administrator/deployed) and you will see approved SMART App there.

### That's it

In this tutorial we learned how to install SMARTbox and to get your first SMART app approved.


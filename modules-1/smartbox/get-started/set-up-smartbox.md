---
description: >-
  This tutorial will guide you through Smartbox installation and accepting
  developer SMART application process
---

# Set up Smartbox locally

### Get licenses

Go to the [Aidbox user portal](https://aidbox.app) and request 2 "self-hosted" Aidbox licenses for Portal and Develop Sandbox. It is a long string like

```
eyJhbGciOiJ...
```

This string is your license key.

### Install Docker and Docker Compose

Follow the [official Docker guide](https://docs.docker.com/compose/install/#install-compose) to install Docker and Docker Compose

### Configure cloud storage

GCP Cloud Storage is used in Bulk API for storing and distributing exported data. To enable bulk API, you need to create GCP Cloud Storage, set up GCP Service Account, and provide full access to the service account on this Cloud Storage.

{% content-ref url="../../../storage-1/gcp-cloud-storage.md" %}
[gcp-cloud-storage.md](../../../storage-1/gcp-cloud-storage.md)
{% endcontent-ref %}

### **Set up email provider**

{% hint style="info" %}
In this guide `mailgun` is used to send email. Also Smartbox supports [different email providers](../how-to-guides/setup-email-provider.md) and [SMTP](../how-to-guides/setup-email-provider.md#how-to-set-up-smtp)
{% endhint %}

Email provider (`mailgun`) is used to communicate with users (developers, patients). It sends emails for resetting a password, email verification, etc.

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
      BOX_SMARTBOX_SANDBOX__URL: "http://sandbox:8888"
      BOX_SMARTBOX_SANDBOX__BASIC: "root:secret"
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
PGIMAGE=healthsamurai/aidboxdb:14.5

# aidbox image to run
AIDBOX_IMAGE=healthsamurai/smartbox:edge

# Client to create on startup
AIDBOX_CLIENT_ID=root
AIDBOX_CLIENT_SECRET=secret

# root user to create on startup
AIDBOX_ADMIN_ID=admin
AIDBOX_ADMIN_PASSWORD=password

# db connection params
PGPORT=5432
PGHOSTPORT=5437
PGUSER=postgres
PGPASSWORD=postgres
PGDATABASE=postgres
AIDBOX_PORT=8888

# Mailgun
BOX_PROVIDER_DEFAULT_TYPE=mailgun
BOX_PROVIDER_DEFAULT_FROM=<YOUR_MAILGUN_FROM_EMAIL>
BOX_PROVIDER_DEFAULT_USERNAME=<YOUR_MAILGUN_USERNAME>
BOX_PROVIDER_DEFAULT_PASSWORD=<YOUR_MAILGUN_PASSWORD>
BOX_PROVIDER_DEFAULT_URL=<YOUR_MAILGUN_URL>

# GCP Cloud Storage
BOX_BULK__STORAGE_BACKEND=gcp
BOX_BULK__STORAGE_GCP_SERVICE__ACCOUNT=gcp-acc
BOX_BULK__STORAGE_GCP_SERVICE__ACCOUNT__EMAIL=<YOUR_SERVICE_ACCOUNT_EMAIL>
BOX_BULK__STORAGE_GCP_SERVICE__ACCOUNT__PRIVATE__KEY=<YOUR_SERVICE_ACCOUNT_PRIVATE_KEY>
BOX_BULK__STORAGE_GCP_BUCKET=<YOUR_GCP_BUCKET_NAME>
```

{% hint style="info" %}
By default Aidbox logs are turned off, you can enable them by setting:

* `AIDBOX_STDOUT_JSON=true`
{% endhint %}

{% hint style="success" %}
To use alternative email provider see the [document](../how-to-guides/setup-email-provider.md)
{% endhint %}

### Launch Aidbox

Run the following command:

```shell
docker compose pull && docker compose up
```

Now Smartbox is ready.

### Admin portal

Open the admin portal [http://localhost:8888/](http://localhost:8888/) and login using credentials from the .env file `AIDBOX_ADMIN_ID` and `AIDBOX_ADMIN_PASSWORD`.

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

#### Get and deploy Growth Chart

To get and the Growth Chart downloaded and start it

```bash
git clone git@github.com:smart-on-fhir/growth-chart-app.git
cd growth-chart-app
npm install
npm start
```

Register a SMART App

Once you launched the Growth Chart app, you can register it in the Sandbox.

* Click the Create app button
* Populate the form:
  * App name: Growth Chart
  * Confidentiality: public
  * Redirect URL: [http://localhost:9000/](http://localhost:9000/)
  * Launch URL: [http://localhost:9000/launch.html](http://localhost:9000/launch.html)
* Submit the new app form
* Click the Submit for Review button to send the application to review

After Growth Chart is registered copy its `Client ID`.

#### Update Growth Chart `client_id`

Open the file `growth-chart-app/launch.html` and fill the `client_id` property. Then save changes to the file.

#### Approve SMART App Publishing Request

Go back to admin portal on [http://localhost:8888](http://localhost:8888). You will see list of SMART App waiting for review.

* Open the review request, made on the previous step,
* click the Approve button.

Now the smart app is available for your patients

### Enable FHIR API for tenant

#### Register a tenant

In order to register a tenant your need to create Tenant resource in Aidbox.

1. Open admin portal.
2. Go to tenants page.
3. Create new tenant named My Clinic (id will be `my-clinic`).

Once you created tenant, you enabled FHIR API for patient, practitioners and bulk clients. Patient portal is related to the tenant as well. The approved smart app is available for patient in that tenant.

#### Populate test data

1. Go to Aidbox REST Console. You may open it from admin portal
2.  Run the following import:

    ```yaml
    POST /$load
    Content-Type: text/yaml

    source: 'https://storage.googleapis.com/aidbox-public/smartbox/rows.ndjson.gz'
    merge:
      meta:
        tenant:
          id: my-clinic
          resourceType: Tenant
    ```

Once you saw 200 OK, Patient resource (id=test-pt-1) and corresponding resources had been uploaded into Aidbox. New we can create a User which has access to that data.

#### Create User resource

In order to enroll your patient, you need to create User resource. Open Aidbox REST Console and run the following command:

```yaml
POST /User

email: example@mail.com
name:
  givenName: Amy
  familyName: Shaw
active: true
fhirUser:
  id: test-pt-1
  resourceType: Patient
roles:
- type: patient
password: password
meta:
  tenant:
    id: my-clinic
    resourceType: Tenant
```

#### Sign in as a `User`

Go to My Clinic's patient portal and login as the user, created above with `example@mail.com` login and `password` password. Loaunch samrt app and provide requested consent.

### That's it

In this tutorial we learned how to install Smartbox and to get your first SMART app approved.

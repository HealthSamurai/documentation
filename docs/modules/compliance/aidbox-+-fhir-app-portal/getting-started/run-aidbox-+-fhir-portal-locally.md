---
description: >-
  This tutorial will guide you through FHIR App Portal installation and exploration of
  developer, admin and patient SMART application processes
---

TODO reactivate details everywhere

TODO add to each header link like this: ### Aidbox license<a href="#license" id="license"></a>
# Set up Aidbox and FHIR App Portal locally

## Get licenses

Go to the [Aidbox user portal](https://aidbox.app) and request 2 "self-hosted" Aidbox licenses for Admin and Sandbox instances. It is a JWT token that looks like

```
eyJhbGciOiJ...
```

This string is your license key.

## Install Docker Desktop

Follow the [official Docker guide](https://docs.docker.com/compose/install) to install Docker Desktop

<!-- ## Configure cloud storage

Aidbox bulk API supports [GCP](../../../file-storage/gcp-cloud-storage.md), [AWS](../../../file-storage/aws-s3.md) and [Azure](../../../file-storage/azure-blob-storage.md) cloud storages. To pass the Inferno tests cloud storage should be [properly set up](../../../api/bulk-api/export.md). -->

## **Set up email provider**

{% hint style="info" %}
In this guide `mailgun` is used to send email. FHIR App Portal also supports [different email providers](../how-to-guides/setup-email-provider.md) and [SMTP](../how-to-guides/setup-email-provider.md#how-to-set-up-smtp)
{% endhint %}

Email provider is used to communicate with users (developers, patients). It sends emails for email verification, resetting of a password and etc.

## **Create docker-compose.yaml**
Create a `docker-compose.yaml` file and paste the following content (under Details button):

<details>
```yaml
# docker-compose.yaml
version: "3.7"

volumes:
  postgres_data: {}

services:
  postgres:
    image: postgres:17
    restart: unless-stopped
    volumes:
      - postgres_data:/var/lib/postgresql/data:delegated
    command:
      - postgres
      - -c
      - shared_preload_libraries=pg_stat_statements
    environment:
      POSTGRES_USER: ${POSTGRES_USER:-aidbox}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD:-password}
      POSTGRES_DB: ${POSTGRES_DB:-aidbox}
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U ${POSTGRES_USER:-aidbox} -d postgres"]
      interval: 10s
      timeout: 5s
      retries: 5

  aidbox-admin:
    image: healthsamurai/aidboxone:edge
    pull_policy: always
    restart: unless-stopped
    depends_on:
      postgres:
        condition: service_healthy
    ports:
      - "${AIDBOX_ADMIN_PORT:-8080}:8080"
    volumes:
      - ./initBundleAdmin.json:/tmp/initBundle.json:ro
    environment:
      BOX_ADMIN_PASSWORD: ${AIDBOX_ADMIN_PASSWORD:-password}
      BOX_ROOT_CLIENT_SECRET: ${ADMIN_CLIENT_SECRET:-secret}
      BOX_DB_HOST: postgres
      BOX_DB_PORT: "5432"
      BOX_DB_USER: ${POSTGRES_USER:-aidbox}
      BOX_DB_PASSWORD: ${POSTGRES_PASSWORD:-password}
      BOX_DB_DATABASE: ${ADMIN_DB_NAME:-aidbox-admin}
      BOX_WEB_PORT: 8080
      BOX_WEB_BASE_URL: ${ADMIN_BASE_URL:-http://localhost:8080}
      BOX_FHIR_COMPLIANT_MODE: "true"
      BOX_FHIR_CORRECT_AIDBOX_FORMAT: "true"
      BOX_FHIR_SCHEMA_VALIDATION: "true"
      BOX_FHIR_SEARCH_AUTHORIZE_INLINE_REQUESTS: "true"
      BOX_FHIR_SEARCH_CHAIN_SUBSELECT: "true"
      BOX_FHIR_SEARCH_COMPARISONS: "true"
      BOX_FHIR_CREATEDAT_URL: https://aidbox.app/ex/createdAt
      BOX_SEARCH_INCLUDE_CONFORMANT: "true"
      BOX_BOOTSTRAP_FHIR_PACKAGES: "hl7.fhir.r4.core#4.0.1:hl7.fhir.us.core#6.1.0"
      BOX_FHIR_TERMINOLOGY_ENGINE: hybrid
      BOX_FHIR_TERMINOLOGY_ENGINE_HYBRID_EXTERNAL_TX_SERVER: https://tx.health-samurai.io/fhir
      BOX_FHIR_TERMINOLOGY_SERVICE_BASE_URL: https://tx.health-samurai.io/fhir
      BOX_LICENSE: ${AIDBOX_ADMIN_LICENSE}
      BOX_SECURITY_AUDIT_LOG_ENABLED: "true"
      BOX_SECURITY_DEV_MODE: ${DEV_MODE:-true}
      BOX_SECURITY_ORGBAC_ENABLED: "true"
      BOX_SECURITY_AUTH_KEYS_SECRET: ${AUTH_KEYS_SECRET:-change-this-secret}
      BOX_MODULE_SDC_STRICT_ACCESS_CONTROL: "true"
      BOX_SETTINGS_MODE: read-write
      BOX_INIT_BUNDLE: "file:///tmp/initBundle.json"
      BOX_MODULE_PROVIDER_DEFAULT_TYPE: ${EMAIL_PROVIDER_TYPE:-mailgun}
      BOX_MODULE_PROVIDER_DEFAULT_URL: ${EMAIL_PROVIDER_URL}
      BOX_MODULE_PROVIDER_DEFAULT_USERNAME: ${EMAIL_PROVIDER_USERNAME:-api}
      BOX_MODULE_PROVIDER_DEFAULT_FROM: ${EMAIL_FROM:-noreply@example.com}
      BOX_MODULE_PROVIDER_DEFAULT_PASSWORD: ${EMAIL_PROVIDER_PASSWORD}
      BOX_COMPATIBILITY_VALIDATION_JSON__SCHEMA_REGEX: "#{:fhir-datetime}"
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/health"]
      interval: 10s
      timeout: 5s
      retries: 30
      start_period: 60s
  aidbox-dev:
    image: healthsamurai/aidboxone:edge
    pull_policy: always
    restart: unless-stopped
    depends_on:
      postgres:
        condition: service_healthy
    ports:
      - "${AIDBOX_DEV_PORT:-8090}:8090"
    volumes:
      - ./initBundleDeveloper.json:/tmp/initBundle.json:ro
    environment:
      BOX_ADMIN_PASSWORD: ${AIDBOX_ADMIN_PASSWORD:-password}
      BOX_ROOT_CLIENT_SECRET: ${DEV_CLIENT_SECRET:-secret}
      BOX_DB_HOST: postgres
      BOX_DB_PORT: "5432"
      BOX_DB_USER: ${POSTGRES_USER:-aidbox}
      BOX_DB_PASSWORD: ${POSTGRES_PASSWORD:-changeme}
      BOX_DB_DATABASE: ${DEV_DB_NAME:-aidbox-dev}
      BOX_WEB_PORT: 8090
      BOX_WEB_BASE_URL: ${DEV_BASE_URL:-http://localhost:8090}
      BOX_FHIR_COMPLIANT_MODE: "true"
      BOX_FHIR_CORRECT_AIDBOX_FORMAT: "true"
      BOX_FHIR_SCHEMA_VALIDATION: "true"
      BOX_FHIR_SEARCH_AUTHORIZE_INLINE_REQUESTS: "true"
      BOX_FHIR_SEARCH_CHAIN_SUBSELECT: "true"
      BOX_FHIR_SEARCH_COMPARISONS: "true"
      BOX_FHIR_CREATEDAT_URL: https://aidbox.app/ex/createdAt
      BOX_SEARCH_INCLUDE_CONFORMANT: "true"
      BOX_BOOTSTRAP_FHIR_PACKAGES: "hl7.fhir.r4.core#4.0.1:hl7.fhir.us.core#6.1.0"
      BOX_FHIR_TERMINOLOGY_ENGINE: hybrid
      BOX_FHIR_TERMINOLOGY_ENGINE_HYBRID_EXTERNAL_TX_SERVER: https://tx.health-samurai.io/fhir
      BOX_FHIR_TERMINOLOGY_SERVICE_BASE_URL: https://tx.health-samurai.io/fhir
      BOX_LICENSE: ${AIDBOX_DEV_LICENSE}
      BOX_SECURITY_AUDIT_LOG_ENABLED: "true"
      BOX_SECURITY_DEV_MODE: ${DEV_MODE:-true}
      BOX_MODULE_SDC_STRICT_ACCESS_CONTROL: "true"
      BOX_SETTINGS_MODE: read-write
      BOX_INIT_BUNDLE: "file:///tmp/initBundle.json"
      BOX_MODULE_PROVIDER_DEFAULT_URL: ${EMAIL_PROVIDER_URL}
      BOX_MODULE_PROVIDER_DEFAULT_USERNAME: ${EMAIL_PROVIDER_USERNAME:-api}
      BOX_MODULE_PROVIDER_DEFAULT_FROM: ${EMAIL_FROM:-noreply@example.com}
      BOX_MODULE_PROVIDER_DEFAULT_PASSWORD: ${EMAIL_PROVIDER_PASSWORD}
      BOX_COMPATIBILITY_VALIDATION_JSON__SCHEMA_REGEX: "#{:fhir-datetime}"
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8090/health"]
      interval: 10s
      timeout: 5s
      retries: 30
      start_period: 60s

  fhir-app-portal:
    image: healthsamurai/fhir-app-portal:edge
    pull_policy: always
    restart: unless-stopped
    depends_on:
      aidbox-admin:
        condition: service_healthy
      aidbox-dev:
        condition: service_healthy
    ports:
      - "${ADMIN_PORTAL_PORT:-8095}:8095"
      - "${DEV_PORTAL_PORT:-8096}:8096"
    environment:
      NODE_ENV: production
      PORTAL_BACKEND_PORT: ${PORTAL_BACKEND_PORT:-8081}
      SESSION_SECRET: ${SESSION_SECRET:-changeme-secure-session-secret}
      DEPLOYMENT_MODE: prod
      AIDBOX_DEV_URL: http://aidbox-dev:8090
      AIDBOX_ADMIN_URL: http://aidbox-admin:8080
      ADMIN_AIDBOX_PUBLIC_URL: http://localhost:8080
      DEVELOPER_AIDBOX_PUBLIC_URL: http://localhost:8090
      ADMIN_FRONTEND_URL: http://localhost:8095
      DEVELOPER_FRONTEND_URL: http://localhost:8096
      AIDBOX_DEV_PUBLIC_URL: http://localhost:8090
      AIDBOX_ADMIN_PUBLIC_URL: http://localhost:8080
      CORS_ORIGINS: http://localhost:8095,http://localhost:8096
    healthcheck:
      test:
        [
          "CMD",
          "wget",
          "--quiet",
          "--tries=1",
          "--spider",
          "http://localhost:80/health",
        ]
      interval: 10s
      timeout: 5s
      retries: 5
      start_period: 10s
```
</details>

## **Create .env file**

Create .env file in the same folder alongside with docker-compose.yaml and set environment variables:

<details>
```shell

# -----------------------------------------------------------------------------
# Licenses
# -----------------------------------------------------------------------------
AIDBOX_ADMIN_LICENSE=<YOUR-ADMIN-LICENCE-JWT-TOKEN>
AIDBOX_DEV_LICENSE=<YOUR-SANDBOX-LICENCE-JWT-TOKEN>

# -----------------------------------------------------------------------------
# PostgreSQL Database Configuration
# -----------------------------------------------------------------------------
POSTGRES_USER=aidbox
POSTGRES_PASSWORD=password
POSTGRES_DB=aidbox

# Database names (automatically created)
ADMIN_DB_NAME=aidbox-admin
DEV_DB_NAME=aidbox-dev

# -----------------------------------------------------------------------------
# Admin Portal Configuration
# -----------------------------------------------------------------------------
AIDBOX_ADMIN_PORT=8080
ADMIN_BASE_URL=http://localhost:8080

# Admin credentials (used to login to Aidbox UI)
AIDBOX_ADMIN_PASSWORD=secure-admin-password
ADMIN_CLIENT_SECRET=secure-admin-client-secret

# -----------------------------------------------------------------------------
# Developer Sandbox Configuration
# -----------------------------------------------------------------------------
AIDBOX_DEV_PORT=8090
DEV_BASE_URL=http://localhost:8090

# Developer sandbox client secret
DEV_CLIENT_SECRET=secure-developer-client-secret

# -----------------------------------------------------------------------------
# FHIR App Portal Configuration
# -----------------------------------------------------------------------------
# Admin portal port
ADMIN_PORTAL_PORT=8095

# Developer portal port  
DEV_PORTAL_PORT=8096

# Backend API port
PORTAL_BACKEND_PORT=8081

# Aidbox internal URLs (for backend-to-Aidbox API calls)
AIDBOX_DEV_URL=http://aidbox-dev:8090
AIDBOX_ADMIN_URL=http://aidbox-admin:8080

# Aidbox public URLs (for browser redirects - accessible from user's machine)
AIDBOX_DEV_PUBLIC_URL=http://localhost:8090
AIDBOX_ADMIN_PUBLIC_URL=http://localhost:8080

ADMIN_AIDBOX_PUBLIC_URL=http://localhost:8080
DEVELOPER_AIDBOX_PUBLIC_URL=http://localhost:8090

# Aidbox UI URLs
ADMIN_FRONTEND_URL=http://localhost:8095
DEVELOPER_FRONTEND_URL=http://localhost:8096

# -----------------------------------------------------------------------------
# Email Provider Configuration (Mailgun Example)
# -----------------------------------------------------------------------------

EMAIL_PROVIDER_TYPE=your-email-provider
EMAIL_PROVIDER_URL=your-email-url
EMAIL_PROVIDER_USERNAME=your-email-username
EMAIL_PROVIDER_PASSWORD=your-email-password
EMAIL_FROM=your-email-from

# -----------------------------------------------------------------------------
# Security Configuration
# -----------------------------------------------------------------------------
# Change this to a secure random string for production
AUTH_KEYS_SECRET=change-this-secret
SESSION_SECRET=changeme-secure-session-secret

# Development mode (set to false for production)
DEV_MODE=true

# -----------------------------------------------------------------------------
# Cloud Storage Configuration (Optional - for Bulk Export)
# -----------------------------------------------------------------------------
# Uncomment and configure one of the following based on your cloud provider:

# --- Google Cloud Storage (GCP) ---
# BOX_BULK__STORAGE_BACKEND=gcp
# BOX_BULK__STORAGE_GCP_SERVICE__ACCOUNT=gcp-service-account-name
# BOX_BULK__STORAGE_GCP_SERVICE__ACCOUNT__EMAIL=your-service-account@project.iam.gserviceaccount.com
# BOX_BULK__STORAGE_GCP_SERVICE__ACCOUNT__PRIVATE__KEY=your-private-key-here
# BOX_BULK__STORAGE_GCP_BUCKET=your-gcp-bucket-name

# --- Amazon Web Services (AWS S3) ---
# BOX_BULK__STORAGE_BACKEND=aws
# BOX_BULK__STORAGE_AWS_ACCESS__KEY__ID=your-aws-access-key
# BOX_BULK__STORAGE_AWS_SECRET__ACCESS__KEY=your-aws-secret-key
# BOX_BULK__STORAGE_AWS_REGION=us-east-1
# BOX_BULK__STORAGE_AWS_BUCKET=your-s3-bucket-name

# --- Microsoft Azure Blob Storage ---
# BOX_BULK__STORAGE_BACKEND=azure
# BOX_BULK__STORAGE_AZURE_ACCOUNT__NAME=your-storage-account
# BOX_BULK__STORAGE_AZURE_ACCOUNT__KEY=your-account-key
# BOX_BULK__STORAGE_AZURE_CONTAINER=your-container-name
```
</details>

<!-- {% hint style="info" %}
By default Aidbox logs are turned off, you can enable them by setting:

* `AIDBOX_STDOUT_JSON=true`
{% endhint %} -->

{% hint style="success" %}
To use alternative email provider see the [document](../how-to-guides/setup-email-provider.md)
{% endhint %}

## Launch Aidbox

Run the following command:

```shell
docker compose up
```
<!-- docker compose pull && docker compose up -->

Now FHIR App Portal is ready.


TODO describe here ports of services
TODO check out product/04-documentation/CUSTOMER_SETUP.md
TODO apply envbust for Client secrets in init bundles https://github.com/Aidbox/examples/tree/main/aidbox-features/init-bundle-env-template
TODO add step when user defines redirect_to in email template or set it with envbust

## Admin portal

Open the admin portal [http://localhost:8888/](http://localhost:8888/) and login using credentials from the .env file `AIDBOX_ADMIN_ID` and `AIDBOX_ADMIN_PASSWORD`.

On the admin portal you can manage apps, patients and other admins.

### Register developer

Submit developer registration form

* Open Developer Sandbox on [http://localhost:9999](http://localhost:9999)
* Click the Sign Up button
* Register a new developer

Once you submitted the developer registration form, you should receive an email with the verification link.

* Follow the link to confirm your email address.
* You will be redirected on creation password form
* Create a password, submit it.

Now you can Sign In as developer to the Developer Sandbox.

## Create a SMART app in developer sandbox

### Get and deploy Growth Chart

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

### Update Growth Chart `client_id`

Open the file `growth-chart-app/launch.html` and fill the `client_id` property. Then save changes to the file.

### Approve SMART App Publishing Request

Go back to admin portal on [http://localhost:8888](http://localhost:8888). You will see list of SMART App waiting for review.

* Open the review request, made on the previous step,
* click the Approve button.

Now the smart app is available for your patients

## Enable FHIR API for tenant

### Register a tenant

In order to register a tenant you need to create Tenant resource in Aidbox.

1. Open admin portal.
2. Go to tenants page.
3. Create new tenant named My Clinic (id will be `my-clinic`).

Once you created tenant, you enabled FHIR API for patient, practitioners and bulk clients. Patient portal is related to the tenant as well. The approved smart app is available for patient in that tenant.

### Populate test data

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

### Create User resource

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

### Sign in as a `User`

Go to My Clinic's patient portal and login as the user, created above with `example@mail.com` login and `password` password. Launch smart app and provide requested consent.

## That's it

In this tutorial we learned how to install Smartbox and to get your first SMART app approved.

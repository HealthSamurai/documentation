# Run Aidbox + CMS App locally

## Prerequisites

#### 1. Docker

* **Docker** - [Install Docker](https://docs.docker.com/get-docker/)
* **Docker Compose** v2.0 or later

#### 2. Aidbox License

Get an Aidbox license from the [Aidbox User Portal](https://aidbox.app):

This is a JWT token that looks like:

```
eyJhbGciOiJ...
```

## Quick Start

### Step 1: Create Project Directory

```bash
mkdir cms-0057-f
cd cms-0057-f
```

### Step 2: Create docker-compose.yaml

- Create a `docker-compose.yaml` file;
- Paste the following content into the file;
- Replace `<AIDBOX_LICENSE>` with your Aidbox license.

```yaml
volumes:
  postgres_data: {}

services:
  postgres:
    image: postgres:18
    volumes:
      - postgres_data:/var/lib/postgresql/data:delegated
    command:
      - postgres
      - -c
      - shared_preload_libraries=pg_stat_statements
    environment:
      POSTGRES_USER: aidbox
      POSTGRES_PORT: 5432
      POSTGRES_DB: aidbox
      POSTGRES_PASSWORD: password
    networks:
      - cms-network

  aidbox:
    image: healthsamurai/aidboxone:edge
    pull_policy: always
    depends_on:
      - postgres
    ports:
      - 8080:8080
    volumes:
      - ./init-bundle.json:/app/init-bundle.json:ro
    environment:
      AIDBOX_LICENSE: <AIDBOX_LICENSE>
      BOX_INIT_BUNDLE: file:///app/init-bundle.json
      BOX_ADMIN_PASSWORD: password
      BOX_BOOTSTRAP_FHIR_PACKAGES: hl7.fhir.r4.core#4.0.1
      BOX_COMPATIBILITY_VALIDATION_JSON__SCHEMA_REGEX: "#{:fhir-datetime}"
      BOX_DB_DATABASE: aidbox
      BOX_DB_HOST: postgres
      BOX_DB_PASSWORD: password
      BOX_DB_PORT: 5432
      BOX_DB_USER: aidbox
      BOX_FHIR_COMPLIANT_MODE: true
      BOX_FHIR_CORRECT_AIDBOX_FORMAT: true
      BOX_FHIR_CREATEDAT_URL: https://aidbox.app/ex/createdAt
      BOX_FHIR_SCHEMA_VALIDATION: true
      BOX_FHIR_SEARCH_AUTHORIZE_INLINE_REQUESTS: true
      BOX_FHIR_SEARCH_CHAIN_SUBSELECT: true
      BOX_FHIR_SEARCH_COMPARISONS: true
      BOX_ROOT_CLIENT_SECRET: secret
      BOX_SEARCH_INCLUDE_CONFORMANT: true
      AIDBOX_SECURITY_AUDIT__LOG_ENABLED: true
      BOX_WEB_BASE_URL: http://localhost:8789
      BOX_WEB_PORT: 8080
      BOX_SECURITY_ORGBAC_ENABLED: true
    healthcheck:
      test: curl -f http://localhost:8080/health || exit 1
      interval: 5s
      timeout: 5s
      retries: 90
      start_period: 30s
    networks:
      - cms-network

  app:
    image: healthsamurai/cms-0057:edge
    pull_policy: always
    depends_on:
      aidbox:
        condition: service_healthy
    ports:
      - 8088:8088
    environment:
      AIDBOX_URL: http://aidbox:8080
      AIDBOX_CLIENT_ID: cms-app
      AIDBOX_CLIENT_SECRET: secret
      AIDBOX_APP_PORT: 8088
      CMS_APP_URL: http://app:8088/
    networks:
      - cms-network
    healthcheck:
      test: curl -f http://localhost:8088/health || exit 1
      interval: 10s
      timeout: 5s
      retries: 5
      start_period: 30s

networks:
  cms-network:
    driver: bridge
```

### Step 3: Create init-bundle.json

Create a `init-bundle.json` file and paste the following content:

```json
{
  "type": "transaction",
  "resourceType": "Bundle",
  "entry": [
    {
      "request": {
        "method": "POST",
        "url": "/$fhir-package-install"
      },
      "resource": {
        "resourceType": "Parameters",
        "parameter": [
          {
            "name": "package",
            "valueString": "hl7.fhir.us.core@6.1.0"
          },
          {
            "name": "package",
            "valueString": "hl7.fhir.us.carin-bb@2.0.0"
          },
          {
            "name": "package",
            "valueString": "hl7.fhir.us.davinci-pdex@2.0.0"
          },
          {
            "name": "package",
            "valueString": "hl7.fhir.us.davinci-drug-formulary@2.0.1"
          },
          {
            "name": "package",
            "valueString": "hl7.fhir.us.davinci-pdex-plan-net@1.1.0"
          },
          {
            "name": "package",
            "valueString": "https://storage.googleapis.com/aidbox-public/smartbox/hl7.fhir.us.davinci-pas%402.0.1.tar.gz"
          },
          {
            "name": "package",
            "valueString": "https://storage.googleapis.com/aidbox-public/smartbox/hl7.fhir.us.davinci-dtr%402.0.1.tar.gz"
          },
          {
            "name": "package",
            "valueString": "https://storage.googleapis.com/aidbox-public/smartbox/hl7.fhir.us.davinci-crd%402.0.1.tar.gz"
          }
        ]
      }
    },
    {
      "request": {
        "method": "PUT",
        "url": "/Client/cms-app"
      },
      "resource": {
        "resourceType": "Client",
        "id": "cms-app",
        "secret": "secret",
        "grant_types": ["basic"]
      }
    },
    {
      "request": {
        "method": "PUT",
        "url": "/AccessPolicy/allow-cms-app"
      },
      "resource": {
        "resourceType": "AccessPolicy",
        "id": "allow-cms-app",
        "engine": "allow",
        "link": [
          {
            "reference": "Client/cms-app"
          }
        ]
      }
    }
  ]
}
```

### Step 4: Run docker-compose

Run the following command to start the Aidbox and CMS App and wait for all services to be ready:

```bash
docker compose up
```

### Step 5: Explore FHIR CMS-0057-F API

Aidbox with CMS-0057-F API is available at `http://localhost:8080`. Creds from Aidbox UI: `admin/password`

CMS-0057-F API documentation:

{% content-ref url="../prior-authorization-support-pas-api.md" %}
[prior-authorization-support-pas-api.md](../prior-authorization-support-pas-api.md)
{% endcontent-ref %}

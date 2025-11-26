# Migrate from Smartbox to Aidbox + FHIR App portal

## Overview

This guide explains how to migrate from Smartbox to Aidbox + FHIR Portal by running both systems side by side on the same database. All examples in this guide demonstrate local installation using Docker. For Kubernetes deployment, follow the same steps but with Kubernetes-specific configurations.

**Architecture (5 containers):**

* 1 PostgreSQL database (`aidbox-db`)
* 2 Smartbox instances (existing):
  * `portal` - Smartbox portal on port 8888
  * `sandbox` - Smartbox sandbox on port 9999
* 2 Aidbox instances (new):
  * `aidbox-admin` - Aidbox Admin on port 8080 (uses `portal` database)
  * `aidbox-dev` - Aidbox Dev on port 8090 (uses `sandbox` database)

**Database mapping:**

* Smartbox Portal + Aidbox Admin → `portal` database
* Smartbox Sandbox + Aidbox Dev → `sandbox` database

Once all containers are running, you'll apply migrations to both Aidbox instances to transform the data structure. After migration, you can shut down the Smartbox containers.

## Prerequisites

**Required files in your project root:**

1. `.env` - Environment variables (see below)
2. `docker-compose.yaml` - 5-container setup (see below)
3. `init-bundle.json` - Init bundle for legacy FCE package
4. `us-core-extensions.legacy.aidbox.tar.gz` - Legacy FCE package file.

## Migration Process

### 1. Reuse your existing `.env` file from Smartbox

Keep your existing Smartbox `.env` file with all the same values:

* Database credentials (`PGIMAGE`, `PGPORT`, `PGHOSTPORT`, `PGUSER`, `PGPASSWORD`, `PGDATABASE`)
* Licenses (`PORTAL_LICENSE`, `SANDBOX_LICENSE`)
* Smartbox image (`AIDBOX_IMAGE`)
* Client and admin credentials

The Aidbox containers will reuse these same environment variables to connect to the same database.

### 2. Create `init-bundle.json`

```json
{
  "type": "transaction",
  "entry": [
    {
      "resource": {
          "legacy-fce.aidbox#0.0.0": "file:///tmp/us-core-extensions.legacy.aidbox.tar.gz"
      },
      "request": {
        "method": "POST",
        "url": "/$upload-fhir-npm-packages"
      }
    }
  ]
}
```

### 3. Download `us-core-extensions.legacy.aidbox.tar.gz`

Place this file in the project root (same directory as docker-compose.yaml). Download this file [here](https://storage.googleapis.com/aidbox-public/smartbox/us-core-extensions.legacy.aidbox.tar.gz).

### 4. Create `docker-compose.yaml` (5-container setup)

```yaml
version: '3.7'
services:
  # Database service (shared by all containers)
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

  # Smartbox Portal (existing - port 8888)
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
      BOX_AUTH_LOGIN__REDIRECT: "/admin/portal"
      BOX_PROJECT_ENTRYPOINT: "smartbox.portal/box"
      AIDBOX_LICENSE: "${PORTAL_LICENSE}"
      AIDBOX_BASE_URL: "http://localhost:8888"
      BOX_SMARTBOX_SANDBOX__URL: "http://sandbox:8888"
      BOX_SMARTBOX_SANDBOX__BASIC: "${AIDBOX_CLIENT_ID}:${AIDBOX_CLIENT_SECRET}"

  # Smartbox Sandbox (existing - port 9999)
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

  # Aidbox Dev (new - port 8090, uses sandbox database)
  aidbox-dev:
    image: healthsamurai/aidboxone:edge
    pull_policy: always
    depends_on:
      - aidbox-db
    links:
      - "aidbox-db:database"
    ports:
      - 8090:8090
    volumes:
      - ./init-bundle.json:/tmp/initBundle.json:ro
      - ./us-core-extensions.legacy.aidbox.tar.gz:/tmp/us-core-extensions.legacy.aidbox.tar.gz:ro
    environment:
      BOX_ADMIN_PASSWORD: password
      BOX_BOOTSTRAP_FHIR_PACKAGES: hl7.fhir.r4.core#4.0.1:hl7.fhir.us.core#6.1.0
      BOX_COMPATIBILITY_VALIDATION_JSON__SCHEMA_REGEX: "#{:fhir-datetime}"
      BOX_DB_DATABASE: sandbox  # Same as Smartbox sandbox
      BOX_DB_HOST: database
      BOX_DB_PASSWORD: "${PGPASSWORD}"
      BOX_DB_PORT: "${PGPORT}"
      BOX_DB_USER: "${PGUSER}"
      BOX_FHIR_COMPLIANT_MODE: "true"
      BOX_FHIR_CORRECT_AIDBOX_FORMAT: "true"
      BOX_FHIR_CREATEDAT_URL: https://aidbox.app/ex/createdAt
      BOX_FHIR_SCHEMA_VALIDATION: "true"
      BOX_FHIR_SEARCH_AUTHORIZE_INLINE_REQUESTS: "true"
      BOX_FHIR_SEARCH_CHAIN_SUBSELECT: "true"
      BOX_FHIR_SEARCH_COMPARISONS: "true"
      BOX_FHIR_TERMINOLOGY_ENGINE: hybrid
      BOX_FHIR_TERMINOLOGY_ENGINE_HYBRID_EXTERNAL_TX_SERVER: https://tx.health-samurai.io/fhir
      BOX_FHIR_TERMINOLOGY_SERVICE_BASE_URL: https://tx.health-samurai.io/fhir
      BOX_LICENSE: "${SANDBOX_LICENSE}"  # Reuse sandbox license
      BOX_MODULE_SDC_STRICT_ACCESS_CONTROL: "true"
      BOX_ROOT_CLIENT_SECRET: l1sE4McoQw
      BOX_SEARCH_INCLUDE_CONFORMANT: "true"
      BOX_SECURITY_AUDIT_LOG_ENABLED: "true"
      BOX_SECURITY_DEV_MODE: "true"
      BOX_SETTINGS_MODE: read-write
      BOX_WEB_BASE_URL: http://dev.localhost:8090
      BOX_WEB_PORT: 8090
      BOX_INIT_BUNDLE: "file:///tmp/initBundle.json"
      BOX_FHIR_LEGACY_FCE_PACKAGE: us-core-extensions.legacy.aidbox#0.0.0
    healthcheck:
      test: curl -f http://dev.localhost:8090/health || exit 1
      interval: 5s
      timeout: 5s
      retries: 90
      start_period: 30s

  # Aidbox Admin (new - port 8080, uses portal database)
  aidbox-admin:
    image: healthsamurai/aidboxone:edge
    pull_policy: always
    depends_on:
      - aidbox-db
    links:
      - "aidbox-db:database"
    ports:
      - 8080:8080
    volumes:
      - ./init-bundle.json:/tmp/initBundle.json:ro
      - ./us-core-extensions.legacy.aidbox.tar.gz:/tmp/us-core-extensions.legacy.aidbox.tar.gz:ro
    environment:
      BOX_ADMIN_PASSWORD: password
      BOX_BOOTSTRAP_FHIR_PACKAGES: hl7.fhir.r4.core#4.0.1:hl7.fhir.us.core#6.1.0
      BOX_COMPATIBILITY_VALIDATION_JSON__SCHEMA_REGEX: "#{:fhir-datetime}"
      BOX_DB_DATABASE: portal  # Same as Smartbox portal
      BOX_DB_HOST: database
      BOX_DB_PASSWORD: "${PGPASSWORD}"
      BOX_DB_PORT: "${PGPORT}"
      BOX_DB_USER: "${PGUSER}"
      BOX_FHIR_COMPLIANT_MODE: "true"
      BOX_FHIR_CORRECT_AIDBOX_FORMAT: "true"
      BOX_FHIR_CREATEDAT_URL: https://aidbox.app/ex/createdAt
      BOX_FHIR_SCHEMA_VALIDATION: "true"
      BOX_FHIR_SEARCH_AUTHORIZE_INLINE_REQUESTS: "true"
      BOX_FHIR_SEARCH_CHAIN_SUBSELECT: "true"
      BOX_FHIR_SEARCH_COMPARISONS: "true"
      BOX_FHIR_TERMINOLOGY_ENGINE: hybrid
      BOX_FHIR_TERMINOLOGY_ENGINE_HYBRID_EXTERNAL_TX_SERVER: https://tx.health-samurai.io/fhir
      BOX_FHIR_TERMINOLOGY_SERVICE_BASE_URL: https://tx.health-samurai.io/fhir
      BOX_LICENSE: "${PORTAL_LICENSE}"  # Reuse portal license
      BOX_MODULE_SDC_STRICT_ACCESS_CONTROL: "true"
      BOX_ROOT_CLIENT_SECRET: l1sE4McoQw
      BOX_SEARCH_INCLUDE_CONFORMANT: "true"
      BOX_SECURITY_AUDIT_LOG_ENABLED: "true"
      BOX_SECURITY_DEV_MODE: "true"
      BOX_SECURITY_ORGBAC_ENABLED: "true"
      BOX_SETTINGS_MODE: read-write
      BOX_WEB_BASE_URL: http://admin.localhost:8080
      BOX_WEB_PORT: 8080
      BOX_INIT_BUNDLE: "file:///tmp/initBundle.json"
      BOX_SECURITY_AUTH_KEYS_SECRET: "very-secret"
      BOX_FHIR_LEGACY_FCE_PACKAGE: us-core-extensions.legacy.aidbox#0.0.0
    healthcheck:
      test: curl -f http://admin.localhost:8080/health || exit 1
      interval: 5s
      timeout: 5s
      retries: 90
      start_period: 30s
```

### 5. Start all containers

```bash
docker-compose up -d
```

This will start:

* **aidbox-db** on port 5437
* **portal** (Smartbox) on port 8888
* **sandbox** (Smartbox) on port 9999
* **aidbox-admin** on port 8080
* **aidbox-dev** on port 8090

***

### 6. Apply Migrations

Once all 5 containers are running and healthy, apply the migrations using the `/db/migrations` endpoint.

> **Important**: Admin and Dev Aidbox instances require different migrations:
>
> * **Admin Aidbox (portal)**: Apply all 3 migrations (has Tenant resources)
> * **Dev Aidbox (sandbox)**: Apply only Client migration (no Tenant resources)

#### 6a. Apply Migrations to Admin Aidbox (port 8080)

```bash
POST /db/migrations
content-type: text/yaml
accept: text/yaml

- id: sb-001-tenant-to-organization
  sql: |
    INSERT INTO organization (
        id, txid, ts, cts, resource_type, status, resource
    )
    SELECT
        t.id,
        t.txid,
        t.ts,
        t.cts,
        'Organization',
        t.status,
        jsonb_build_object(
            'resourceType', 'Organization',
            'id', t.id,
            'name', COALESCE(t.resource->>'name', t.id),
            'active', true,
            'meta', jsonb_build_object(
                'lastUpdated', t.ts,
                'versionId', t.txid::text
            )
        )
        || COALESCE(
            (
                SELECT jsonb_object_agg(key, value)
                FROM jsonb_each(t.resource)
                WHERE key NOT IN ('name','resourceType','id','extension')
            ),
            '{}'::jsonb
        )
    FROM tenant t
    WHERE t.status = 'created'
    ON CONFLICT (id) DO UPDATE
    SET resource = EXCLUDED.resource,
        ts = EXCLUDED.ts,
        status = EXCLUDED.status;

- id: sb-002-tenant-meta-to-extension
  sql: |
    DO $$
    DECLARE
      rec RECORD;
    BEGIN
      FOR rec IN
        SELECT table_name
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND column_name = 'resource'
          AND data_type = 'jsonb'
      LOOP
        EXECUTE format($f$
          UPDATE %I
          SET resource =
            jsonb_set(
              CASE
                WHEN resource->'meta' IS NULL THEN jsonb_set(resource, '{meta}', '{}'::jsonb)
                ELSE resource
              END,
              '{meta,extension}',
              COALESCE(resource->'meta'->'extension','[]'::jsonb)
              || jsonb_build_array(
                   jsonb_build_object(
                     'url','https://aidbox.app/tenant-organization-id',
                     'value', jsonb_build_object(
                       'Reference', jsonb_build_object(
                         'id', resource->'meta'->'tenant'->'id',
                         'resourceType','Organization'
                       )
                     )
                   )
                 )
            )
          WHERE resource->'meta'->'tenant' IS NOT NULL
            AND resource->'meta'->'tenant' <> 'null'::jsonb
        $f$, rec.table_name);
      END LOOP;
    END;
    $$;

- id: sb-003-client-resource-migration
  sql: |
    UPDATE client
    SET resource = (
      SELECT CASE
        WHEN resource->>'type' IN ('provider-facing-smart-app','patient-facing-smart-app')
          THEN jsonb_set(resource, '{type}', '"smart-app"'::jsonb)
        ELSE resource
      END
    )
    WHERE resource->>'type' IN ('provider-facing-smart-app','patient-facing-smart-app');

    UPDATE client
    SET resource =
      CASE
        WHEN resource->'details'->'user-id' IS NOT NULL
          OR resource->'details'->'status' IS NOT NULL
        THEN
          jsonb_set(
            CASE
              WHEN resource->'meta' IS NULL THEN jsonb_set(resource, '{meta}', '{}'::jsonb)
              ELSE resource
            END #- '{details,user-id}' #- '{details,status}',
            '{meta,extension}',
            COALESCE(resource->'meta'->'extension','[]'::jsonb)
            || (CASE WHEN resource->'details'->'user-id' IS NOT NULL
                     THEN jsonb_build_array(jsonb_build_object(
                       'url','http://aidbox.app/StructureDefinition/Client/created-by',
                       'value', jsonb_build_object(
                         'Reference', jsonb_build_object(
                           'id', resource->'details'->'user-id',
                           'resourceType', 'User'
                         )
                       )
                     ))
                     ELSE '[]'::jsonb END)
            || (CASE WHEN resource->'details'->'status' IS NOT NULL
                     THEN jsonb_build_array(jsonb_build_object(
                       'url','http://aidbox.app/StructureDefinition/Client/status',
                       'value', jsonb_build_object(
                         'Code',
                         CASE resource->'details'->>'status'
                           WHEN 'in-review'  THEN 'review'
                           WHEN 'not-synced' THEN 'draft'
                           WHEN 'synced'     THEN 'active'
                           ELSE resource->'details'->>'status'
                         END
                       )
                     ))
                     ELSE '[]'::jsonb END)
          )
        ELSE resource
      END
    WHERE resource->'details'->'user-id' IS NOT NULL
       OR resource->'details'->'status' IS NOT NULL;

    UPDATE client
    SET resource = resource #- '{details}'
    WHERE resource->'details' = '{}'::jsonb;
```

#### 6b. Apply Migrations to Dev Aidbox (port 8090)

Dev Aidbox (sandbox) does not have Tenant resources, so we only apply the Client migration:

```bash
POST /db/migrations
content-type: text/yaml
accept: text/yaml

- id: sb-003-client-resource-migration
  sql: |
    UPDATE client
    SET resource = (
      SELECT CASE
        WHEN resource->>'type' IN ('provider-facing-smart-app','patient-facing-smart-app')
          THEN jsonb_set(resource, '{type}', '"smart-app"'::jsonb)
        ELSE resource
      END
    )
    WHERE resource->>'type' IN ('provider-facing-smart-app','patient-facing-smart-app');

    UPDATE client
    SET resource =
      CASE
        WHEN resource->'details'->'user-id' IS NOT NULL
          OR resource->'details'->'status' IS NOT NULL
        THEN
          jsonb_set(
            CASE
              WHEN resource->'meta' IS NULL THEN jsonb_set(resource, '{meta}', '{}'::jsonb)
              ELSE resource
            END #- '{details,user-id}' #- '{details,status}',
            '{meta,extension}',
            COALESCE(resource->'meta'->'extension','[]'::jsonb)
            || (CASE WHEN resource->'details'->'user-id' IS NOT NULL
                     THEN jsonb_build_array(jsonb_build_object(
                       'url','http://aidbox.app/StructureDefinition/Client/created-by',
                       'value', jsonb_build_object(
                         'Reference', jsonb_build_object(
                           'id', resource->'details'->'user-id',
                           'resourceType', 'User'
                         )
                       )
                     ))
                     ELSE '[]'::jsonb END)
            || (CASE WHEN resource->'details'->'status' IS NOT NULL
                     THEN jsonb_build_array(jsonb_build_object(
                       'url','http://aidbox.app/StructureDefinition/Client/status',
                       'value', jsonb_build_object(
                         'Code',
                         CASE resource->'details'->>'status'
                           WHEN 'in-review'  THEN 'review'
                           WHEN 'not-synced' THEN 'draft'
                           WHEN 'synced'     THEN 'active'
                           ELSE resource->'details'->>'status'
                         END
                       )
                     ))
                     ELSE '[]'::jsonb END)
          )
        ELSE resource
      END
    WHERE resource->'details'->'user-id' IS NOT NULL
       OR resource->'details'->'status' IS NOT NULL;

    UPDATE client
    SET resource = resource #- '{details}'
    WHERE resource->'details' = '{}'::jsonb;
```

### 7. Post-Migration Verification

the Now you can run Inferno G10 test kit on both Aidbox instances to verify compliance with G10 certification. See [Pass Inferno Tests with Aidbox](pass-inferno-tests-with-aidbox.md) for step-by-step instructions.

## API Changes: Smartbox → Aidbox

If you have applications built on top of Smartbox API, you need to update your API calls to use the new Aidbox endpoint patterns.

### Endpoint Pattern Comparison

| API Category                                 | Smartbox Pattern                                                         | Aidbox Pattern                                                    | Notes                                     |
| -------------------------------------------- | ------------------------------------------------------------------------ | ----------------------------------------------------------------- | ----------------------------------------- |
| **US Core FHIR Resources (Patient-facing)**  | `/tenant/{tenant}/patient/smart-api/{ResourceType}`                      | `/Organization/{org-id}/fhir/{ResourceType}`                      | Tenant replaced with Organization         |
| **US Core FHIR Resources (Provider-facing)** | `/tenant/{tenant}/provider/smart-api/{ResourceType}`                     | `/Organization/{org-id}/fhir/{ResourceType}`                      | Same endpoint for both patient & provider |
| **FHIR Resource by ID**                      | `/tenant/{tenant}/patient/smart-api/{ResourceType}/{id}`                 | `/Organization/{org-id}/fhir/{ResourceType}/{id}`                 | Resource ID access                        |
| **FHIR Search**                              | `GET/POST /tenant/{tenant}/patient/smart-api/{ResourceType}?param=value` | `GET/POST /Organization/{org-id}/fhir/{ResourceType}?param=value` | Search parameters unchanged               |
| **OAuth2 - Authorize**                       | `/tenant/{tenant}/patient/auth/authorize`                                | `/auth/authorize`                                                 | OAuth2 authorization                      |
| **OAuth2 - Token**                           | `/tenant/{tenant}/patient/auth/token`                                    | `/auth/token`                                                     | Token endpoint                            |
| **Provider Auth - Authorize**                | `/tenant/{tenant}/provider/auth/authorize`                               | `/auth/authorize`                                                 | No separate provider auth                 |
| **SMART Configuration**                      | `/tenant/{tenant}/patient/smart-api/.well-known/smart-configuration`     | `/.well-known/smart-configuration`                                | SMART metadata endpoint                   |
| **FHIR Metadata**                            | `/tenant/{tenant}/patient/smart-api/metadata`                            | `/Organization/{org-id}/fhir/metadata`                            | CapabilityStatement                       |
| **Bulk Data - Patient Export**               | `/fhir/Patient/$export`                                                  | `/fhir/Patient/$export`                                           | ✅ No change                               |
| **Bulk Data - Group Export**                 | `/fhir/Group/{id}/$export`                                               | `/fhir/Group/{id}/$export`                                        | ✅ No change                               |
| **Bulk Data - Status**                       | `/fhir/bulkstatus/{request-id}`                                          | `/fhir/bulkstatus/{request-id}`                                   | ✅ No change                               |
| **C-CDA Document Generation**                | `/ccda/make-doc`                                                         | `/ccda/make-doc`                                                  | ✅ No change                               |

### Key Changes Summary

1. **Tenant → Organization** (FHIR Resources only): The primary change is replacing `/tenant/{tenant-id}` with `/Organization/{org-id}` for FHIR resource endpoints
2. **Unified Patient/Provider**: Aidbox uses a single set of endpoints instead of separate `/patient/` and `/provider/` paths
3. **Removed `/smart-api/` path segment**: FHIR resources are now directly under `/fhir/` instead of `/smart-api/`
4. **Auth Consolidation**: Authentication endpoints are now at root level (`/auth/*`) without tenant prefix
5. **Bulk Data & C-CDA**: These endpoints remain at root level and **do not change** between Smartbox and Aidbox

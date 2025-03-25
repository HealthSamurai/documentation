# Migrate to FHIR Schema

{% hint style="danger" %}
Entity & Attributes and Zen Schema are planned to be retired and will be replaced by FHIR Schema. Here’s a migration guide to help you transition your custom resources defined via Entity & Attributes / Zen Schema.
{% endhint %}

### Migration Overview

The migration process involves six key steps:

1. Identifying deprecated capabilities
2. Setting up a parallel environment with FHIR Schema
3. Migrating profiles
4. Migrating custom resources
5. Migrating search parameters
6. Validating resources and resolving issues

### Step 1: Identify Deprecated Capabilities

First, obtain a comprehensive list of all your EA and Zen components that will need migration:

```http
GET /deprecated/capabilities
```

Example response:

```json
{
  "custom-entities": {
    "total": 0,
    "entities": []
  },
  "custom-attributes": {
    "total": 0,
    "entities": []
  },
  "custom-search-param-resource": {
    "total": 1,
    "params": [
      "MyCustomResource.name"
    ]
  },
  "custom-zen-resources": {
    "total": 0,
    "resources": []
  },
  "zen-profiles": {
    "total": 111,
    "profiles": [
      "http://hl7.org/fhir/us/core/StructureDefinition/us-core-bmi",
      // ...additional profiles...
    ]
  },
  "custom-zen-search-params": {
    "total": 1,
    "params": [
      "main/my-parameter"
    ]
  },
  "zen-fhir-search-params": {
    "total": 100,
    "params": [
      "hl7-fhir-us-core.search.us-core-observation-code/search",
      // ...additional search params...
    ]
  }
}
```

### Step 2: Set Up Parallel Environment with FHIR Schema

Launch a parallel Aidbox instance configured to use the FHIR Schema, connected to the same database and the same Zen configuration project (if you have one).

Below is an example of a `docker-compose` file with two Aidbox instances running on the same database: the first Aidbox (`aidbox_zen`) is configured with a Zen configuration project and uses Zen validation mode, while the second (`aidbox_schema`) is set up to use the FHIR Schema.

```yaml
version: '3.7'
volumes:
  aidbox_pg_data: {}
version: '3.7'
volumes:
  aidbox_pg_data: {}

services:

  aidbox_db:
    image: healthsamurai/aidboxdb:17
    pull_policy: always
    volumes:
    - aidbox_pg_data:/data:delegated
    environment:
      PGDATA: /data
      POSTGRES_USER: aidbox
      POSTGRES_PORT: 5432
      POSTGRES_DB: aidbox
      POSTGRES_PASSWORD: password

  aidbox_zen:
    image: "healthsamurai/aidboxone:edge"
    pull_policy: always
    depends_on: 
      - aidbox_db
    ports:
      - "8888:8888"
    environment:
      PGHOST: aidbox_db
      PGPORT: 5432
      PGUSER: aidbox
      PGDATABASE: aidbox
      PGPASSWORD: password
      AIDBOX_PORT: 8888
      AIDBOX_BASE_URL: http://localhost:8888
      BOX_PROJECT_GIT_TARGET__PATH: /aidbox-project
      AIDBOX_ZEN_ENTRYPOINT: main/box
      AIDBOX_DEV_MODE: "true"
      AIDBOX_ZEN_DEV_MODE: "true"
      AIDBOX_CLIENT_SECRET: very_secret
      AIDBOX_ADMIN_PASSWORD: password
      AIDBOX_COMPLIANCE: enabled
      AIDBOX_LICENSE: <aidbox-license>
      AIDBOX_FHIR_VERSION: "4.0.1"
    volumes:
      - "./zrc:/aidbox-project/zrc"
      - "./zen-package.edn:/aidbox-project/zen-package.edn"
      - "./zen-packages:/aidbox-project/zen-packages"

  aidbox_schema:
    image: healthsamurai/aidboxone:edge
    pull_policy: always
    depends_on:
    - aidbox_db
    ports:
    - 8080:8080
    environment:
      BOX_BOOTSTRAP_FHIR_PACKAGES: hl7.fhir.r4.core#4.0.1
      BOX_FHIR_SCHEMA_VALIDATION: true
      AIDBOX_CLIENT_SECRET: very_secret
      AIDBOX_ADMIN_PASSWORD: password
      AIDBOX_COMPLIANCE: enabled
      PGHOST: aidbox_db
      PGUSER: aidbox
      AIDBOX_PORT: 8080
      AIDBOX_BASE_URL: http://localhost:8080
      BOX_PROJECT_GIT_TARGET__PATH: /aidbox-project
      AIDBOX_ZEN_ENTRYPOINT: main/box
      AIDBOX_DEV_MODE: "true"
      AIDBOX_ZEN_DEV_MODE: "true"
      PGDATABASE: aidbox
      PGPASSWORD: password
      PGPORT: 5432
      AIDBOX_LICENSE: <aidbox-license>
      AIDBOX_FHIR_VERSION: "4.0.1"
    volumes:
      - "./zrc:/aidbox-project/zrc"
      - "./zen-package.edn:/aidbox-project/zen-package.edn"
      - "./zen-packages:/aidbox-project/zen-packages"
```

For detailed FHIR Schema engine configuration instructions, refer to the [FHIR Schema setup documentation](../../profiling-and-validation/fhir-schema-validator/setup.md).

### Step 3: Migrate FHIR Packages to FHIR Schema

Set FHIR core packages using an environment variable:

```
AIDBOX_FHIR_PACKAGES=hl7.fhir.r4.core#4.0.1:hl7.fhir.us.core#3.1.0
```

### Step 4: Migrate Custom Resources

If you have custom resource defined by Entity & Attributes, follow this guide

{% content-ref url="migrate-custom-resources-defined-with-entity-and-attributes-to-fhir-schema.md" %}
[migrate-custom-resources-defined-with-entity-and-attributes-to-fhir-schema.md](migrate-custom-resources-defined-with-entity-and-attributes-to-fhir-schema.md)
{% endcontent-ref %}

If you have custom resource defined by Zen, follow this guide -

{% content-ref url="migrate-custom-resources-defined-with-zen-to-fhir-schema.md" %}
[migrate-custom-resources-defined-with-zen-to-fhir-schema.md](migrate-custom-resources-defined-with-zen-to-fhir-schema.md)
{% endcontent-ref %}

### Step 6: Validate Resources and Resolve Issues

Since FHIR Schema validation engine is more reliable and validates all the FHIR container cases, we need to validate existing data with FHIR Schema. To do it use [Aidbox Asynchronous Validation API](../../profiling-and-validation/validation-api.md).

### Check deprecated capabilities

```
GET /deprecated/capabilities
```

If you have total 0 for each section, your migration is done. You can delete old Entity & Attributes and Zen configuration.

### Verification Steps

After completing the migration:

1. Verify API functionality:

```http
GET /fhir/Patient?_summary=count
```

2. Test search parameters:

```http
GET /fhir/Patient?custom-param=value
```

3. Validate that resource creation and updates work properly:

```http
POST /fhir/Patient
Content-Type: application/json
{
  "resourceType": "Patient",
  "name": [
    {
      "given": ["Test"],
      "family": "Patient"
    }
  ]
}
```

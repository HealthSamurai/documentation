---
description: Run one-time migrations at startup — install FHIR packages or execute SQL using AidboxMigration and Init Bundle.
---

# Migrations

Aidbox supports one-time migrations that run exactly once during startup. Migrations are useful for:

* Installing FHIR Implementation Guide packages
* Running SQL statements (creating indexes, tables, seed data)

Migrations use the `AidboxMigration` resource combined with [Init Bundle](init-bundle.md) for declarative, idempotent execution.

## How it works

1. You define an `AidboxMigration` resource with an action and parameters
2. You wrap it in an Init Bundle with `ifNoneExist` to ensure it runs only once
3. On startup, Aidbox executes the bundle — if the migration already exists, it is skipped

## Install a FHIR package

Use the `far-migration-fhir-package-install` action to install a FHIR IG package from the registry.

```json
{
  "type": "transaction",
  "resourceType": "Bundle",
  "entry": [
    {
      "request": {
        "method": "POST",
        "url": "AidboxMigration",
        "ifNoneExist": "id=us-core-install"
      },
      "resource": {
        "resourceType": "AidboxMigration",
        "id": "us-core-install",
        "action": "far-migration-fhir-package-install",
        "status": "to-run",
        "params": {
          "resourceType": "Parameters",
          "parameter": [
            {
              "name": "package",
              "valueString": "hl7.fhir.us.core@3.1.1"
            }
          ]
        }
      }
    }
  ]
}
```

The `package` parameter value follows the format `<package-name>@<version>`.

After execution, the migration status changes to `done` and the `result` field contains the number of installed canonicals.

### Uninstall a FHIR package

Use `far-migration-fhir-package-uninstall` to remove a previously installed package:

```json
{
  "resourceType": "AidboxMigration",
  "id": "us-core-uninstall",
  "action": "far-migration-fhir-package-uninstall",
  "status": "to-run",
  "params": {
    "resourceType": "Parameters",
    "parameter": [
      {
        "name": "package",
        "valueString": "hl7.fhir.us.core@3.1.1"
      }
    ]
  }
}
```

## Run a SQL migration

Use the `aidbox-migration-run-sql` action to execute arbitrary SQL statements.

{% hint style="info" %}
Available since the 2602 release.
{% endhint %}

```json
{
  "type": "transaction",
  "resourceType": "Bundle",
  "entry": [
    {
      "request": {
        "method": "POST",
        "url": "AidboxMigration",
        "ifNoneExist": "id=create-encounter-index"
      },
      "resource": {
        "resourceType": "AidboxMigration",
        "id": "create-encounter-index",
        "action": "aidbox-migration-run-sql",
        "status": "to-run",
        "params": {
          "resourceType": "Parameters",
          "parameter": [
            {
              "name": "sql",
              "valueString": "CREATE INDEX IF NOT EXISTS encounter_subject_id ON encounter ((resource #>> '{subject, id}'));"
            }
          ]
        }
      }
    }
  ]
}
```

After execution, the migration status changes to `done` and `result.valueBoolean` is `true`.

{% hint style="warning" %}
Invalid SQL causes the migration to fail with a 422 error. In a transaction bundle, this rolls back the entire transaction and prevents Aidbox from starting.
{% endhint %}

## Using with Init Bundle

Set the `BOX_INIT_BUNDLE` environment variable to load migrations on startup:

```yaml
volumes:
  - ./init-bundle.json:/tmp/init-bundle.json
environment:
  BOX_INIT_BUNDLE: file:///tmp/init-bundle.json
```

{% hint style="info" %}
The `ifNoneExist` parameter in the bundle entry ensures idempotency — if a migration with the same `id` already exists, it is skipped. Without `ifNoneExist`, a repeated POST returns a 409 duplicate key error.
{% endhint %}

## Combining multiple migrations

You can include multiple migrations in a single Init Bundle:

```json
{
  "type": "transaction",
  "resourceType": "Bundle",
  "entry": [
    {
      "request": {
        "method": "POST",
        "url": "AidboxMigration",
        "ifNoneExist": "id=install-us-core"
      },
      "resource": {
        "resourceType": "AidboxMigration",
        "id": "install-us-core",
        "action": "far-migration-fhir-package-install",
        "status": "to-run",
        "params": {
          "resourceType": "Parameters",
          "parameter": [
            { "name": "package", "valueString": "hl7.fhir.us.core@3.1.1" }
          ]
        }
      }
    },
    {
      "request": {
        "method": "POST",
        "url": "AidboxMigration",
        "ifNoneExist": "id=create-custom-index"
      },
      "resource": {
        "resourceType": "AidboxMigration",
        "id": "create-custom-index",
        "action": "aidbox-migration-run-sql",
        "status": "to-run",
        "params": {
          "resourceType": "Parameters",
          "parameter": [
            {
              "name": "sql",
              "valueString": "CREATE INDEX IF NOT EXISTS patient_birthdate ON patient ((resource #>> '{birthDate}'));"
            }
          ]
        }
      }
    }
  ]
}
```

## Checking migration status

List all migrations:

```http
GET /fhir/AidboxMigration
```

Each migration has a `status` field:

| Status | Description |
|--------|-------------|
| `to-run` | Migration is queued for execution |
| `done` | Migration completed successfully |

## See also

{% content-ref url="init-bundle.md" %}
[init-bundle.md](init-bundle.md)
{% endcontent-ref %}

{% content-ref url="../reference/system-resources-reference/core-module-resources.md" %}
[AidboxMigration resource reference](../reference/system-resources-reference/core-module-resources.md)
{% endcontent-ref %}

{% content-ref url="../tutorials/artifact-registry-tutorials/upload-fhir-implementation-guide/how-to-load-fhir-ig-with-init-bundle.md" %}
[Tutorial: Load FHIR IG with Init Bundle](../tutorials/artifact-registry-tutorials/upload-fhir-implementation-guide/how-to-load-fhir-ig-with-init-bundle.md)
{% endcontent-ref %}

{% content-ref url="../tutorials/other-tutorials/how-to-run-sql-via-init-bundle.md" %}
[Tutorial: Run SQL statements via Init Bundle](../tutorials/other-tutorials/how-to-run-sql-via-init-bundle.md)
{% endcontent-ref %}

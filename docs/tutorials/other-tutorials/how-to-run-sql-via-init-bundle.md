---
description: >-
  In this tutorial, you will learn how to use Aidbox Init Bundle to run SQL statements.
---

# How to run SQL statements via Init Bundle

## Objectives

* Run SQL statements via [Init Bundle](../../../configuration/init-bundle.md)
* Make it sage - don't run it on each Aidbox startup but instead do it **exactly once**.


## Before you begin

* Make sure your Aidbox version is 2602 or newer
* Setup the local Aidbox instance using getting started [guide](../../../getting-started/run-aidbox-locally.md)

## Using init bundle to run SQL statements

Init bundle allows you to automatically execute a bundle of resources on Aidbox startup.
The following example shows how to use `AidboxMigration' resource to call an API for executing SQL statements **exactly once**.

1. Create a new file for the Init Bundle.

```bash
touch init-bundle.json
```

paste the following content into the file:
```json
{
  "type": "transaction",
  "resourceType": "Bundle",
  "entry": [
    {
      "request": {
        "method": "POST",
        "url": "AidboxMigration",
        "ifNoneExist": "id=create-index-on-encounter-subject-id"
      },
      "resource": {
        "action": "aidbox-migration-run-sql",
        "status": "to-run",
        "params": {
          "parameter": [
            {
              "name": "sql",
              "valueString": "CREATE INDEX IF NOT EXISTS encounter_subject_id ON encounter ((resource #>> '{ subject, id }'));"
            }
          ],
          "resourceType": "Parameters"
        },
        "resourceType": "AidboxMigration",
        "id": "create-index-on-encounter-subject-id"
      }
    }
  ]
}

```

2. Modify the docker-compose.yml file to set the init bundle.

```yaml
volumes:
  - ./init-bundle.json:/tmp/init-bundle.json
environment:
  BOX_INIT_BUNDLE: file:///tmp/init-bundle.json
```

3. Restart the Aidbox instance.

```bash
docker-compose down
docker-compose up -d
```

4. Navigate to the Aidbox UI -> "DB console" tab and execute the following SQL statement to verify that the index is created.

```sql
SELECT
    indexname,
    indexdef
FROM pg_indexes
WHERE tablename = 'encounter';
```

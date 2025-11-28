---
description: >-
  In this tutorial, you will learn how to use Aidbox Init Bundle to load a FHIR IG.
---


## Objectives

* Load a FHIR IG with Aidbox via [Init Bundle](../../../configuration/init-bundle.md)
* Make it efficient - don't reload the IG on each Aidbox startup but instead do it **exactly once**.


## Before you begin

* Make sure your Aidbox version is newer than 2511
* Setup the local Aidbox instance using getting started [guide](../../getting-started/run-aidbox-locally.md)

## Using init bundle to load a FHIR IG

Init bundle allows you to automatically execute a bundle of resources on Aidbox startup.
The following example shows how to use `AidboxMigration' resource to call an API for loading a FHIR IG **exactly once**.

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
        "ifNoneExist": "id=hl7-fhir-us-core-installation"
      },
      "resource": {
        "action": "far-migration-fhir-package-install",
        "status": "to-run",
        "params": {
          "parameter": [
            {
              "name": "package",
              "valueString": "hl7.fhir.us.core@3.1.1"
            }
          ],
          "resourceType": "Parameters"
        },
        "resourceType": "AidboxMigration",
        "id": "hl7-fhir-us-core-installation"
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

4. Navigate to the Aidbox UI -> "FAR" tab and check that the US core IG is loaded.

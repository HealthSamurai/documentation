---
description: Execute multiple FHIR operations atomically using batch or transaction bundles with rollback support on failure.
---

# Batch/Transaction

## Introduction

Transaction interaction allows for several interactions using one HTTP request. There are two types of transaction interaction (type is specified by field `type`): **batch** and **transaction**. The first one executes requests one by one, and the second one does the same but rolls back all changes if any request fails.

```
POST /fhir
```

The body of such a request contains one resource of type Bundle, which contains field entry with an array of interactions, for example:

```json
POST /fhir
accept: application/json
content-type: application/json

{
  "type": "transaction",
  "entry": [
    {
      "resource": {},
      "request": {
        "method": "PUT",
        "url": "/Practitioner/pr1"
      }
    },
    {
      "request": {
        "method": "GET",
        "url": "/Patient"
      }
    },
    {
      "resource": {
        "id": "admin123",
        "email": "admin@mail.com",
        "password": "password"
      },
      "request": {
        "method": "POST",
        "url": "/User"
      }
    }
  ]
}
```

Each element of the entry array contains a resource field (body of the request) and a request field (request line in terms of the HTTP request).

```yaml
resource:
  # body here (if needed)
request:
  method: POST # POST/GET/PUT/PATCH/DELETE
  url: "/ResourceType" # request url
# needed if you want to refer the resource inside the bundle
fullUrl: "urn:uuid:<uuid | string>"
```

## Using references

Sometimes, we may need to reference a resource within a bundle that has not yet been created. For this purpose, you can use the `fullUrl` field, available for each resource, which acts as a unique identifier at the bundle level (a logical reference).&#x20;

You can then reference this identifier from another resource using a FHIR reference, e.g.,`"reference": "urn:uuid:<uuid|string>"`.  Those references are temporary (existing during bundle processing) and will be translated to valid FHIR references when the bundle is processed.

## Processing rules

Differences from the FHIR transaction [processing rules](https://www.hl7.org/fhir/http.html#trules):

* Each entry is processed in the order provided in a bundle&#x20;
* For `type: batch` references to resources inside a bundle won't be resolved.
* For `type: transaction` before processing interactions, all references in a resource will attempt to resolve. In this example, Patient will refer to a newly created Organization:

```json
POST /fhir
accept: application/json
content-type: application/json

{
  "type": "transaction",
  "entry": [
    {
      "resource": {
        "resourceType": "Patient",
        "managingOrganization": {
          "reference": "urn:uuid:14121321-4af5-424c-a0e1-ed3aab1c349d"
        }
      },
      "request": {
        "method": "POST",
        "url": "/Patient"
      }
    },
    {
      "resource": {
        "resourceType": "Organization",
        "name": "org10"
      },
      "request": {
        "method": "POST",
        "url": "/Organization"
      },
      "fullUrl": "urn:uuid:14121321-4af5-424c-a0e1-ed3aab1c349d"
    }
  ]
}
```

## Multiple resources with the same id

If you have multiple entries with the same resource id, Aidbox will execute them one by one and thus you can create a resource with a history within a single transaction:

```json
POST /fhir
Accept: application/json
Content-Type: application/json

{
  "resourceType": "Bundle",
  "type": "transaction",
  "entry": [
    {
      "request": {
        "method": "PUT",
        "url": "/Patient/pt-1"
      },
      "resource": {
        "birthDate": "2021-01-01"
      }
    },
    {
      "request": {
        "method": "PUT",
        "url": "/Patient/pt-1"
      },
      "resource": {
        "birthDate": "2021-01-02"
      }
    },
    {
      "request": {
        "method": "PUT",
        "url": "/Patient/pt-1"
      },
      "resource": {
        "birthDate": "2021-01-03"
      }
    }
  ]
}

```

Response of `GET /fhir/Patient/pt-1/_history`:

```json
{
  "resourceType": "Bundle",
  "type": "history",
  "total": 3,
  "entry": [
    {
      "resource": {
        "birthDate": "2021-01-03",
        "id": "pt-1",
        "resourceType": "Patient"
      }
    },
    {
      "resource": {
        "birthDate": "2021-01-02",
        "id": "pt-1",
        "resourceType": "Patient"
      }
    },
    {
      "resource": {
        "birthDate": "2021-01-01",
        "id": "pt-1",
        "resourceType": "Patient"
      }
    }
  ]
}
```

## Change transaction isolation level

By default Aidbox uses `SERIALIZABLE` transaction isolation level. This may lead to some transactions being rejected when there are many concurrent requests.

See more about [transaction isolation in Postgres documentation](https://www.postgresql.org/docs/current/transaction-iso.html).

The best way to handle rejected transactions is to retry them. If it is not possible, you can set the maximum isolation level with the HTTP header or [environment variable](../reference/all-settings.md#fhir.transaction-max-isolation-level). If both the HTTP header and environment variable are provided, the header will be used.

{% hint style="danger" %}
Using an isolation level lower than serializable may lead to data serialization anomalies.
{% endhint %}

Example:

```json
POST /fhir
x-max-isolation-level: read-committed
Accept: application/json
Content-Type: application/json

{
  "resourceType": "Bundle",
  "type": "transaction",
  "entry": [
    {
      "request": {
        "method": "PUT",
        "url": "/Patient/pt-1"
      },
      "resource": {
        "active": true
      }
    },
    {
      "request": {
        "method": "PUT",
        "url": "/Patient/pt-2"
      },
      "resource": {
        "active": false
      }
    }
  ]
}
```

---
description: Delete FHIR resources in Aidbox using REST API, transactions, SQL queries, and truncate operations.
---

# Delete data

## FHIR delete

You can delete a single resource using [`DELETE` FHIR API method](../../api/rest-api/crud/delete.md). This method deletes the resource specified and creates a history entry for the operation. The history entry created contains the entire deleted resource.

Note that Aidbox does not enforce referential integrity for `DELETE` method.

### Example

Request:

```
DELETE /fhir/Patient/pt-1
```

Response:

```yaml
meta:
  lastUpdated: '2022-09-20T12:55:25.226619Z'
  versionId: '5169631'
name:
  - given:
      - John
    family: Smith
id: pt-2
```

## Patient $purge

{% hint style="info" %}
Available since version 2602.
{% endhint %}

To delete a Patient and **all resources in their compartment** (Observations, Conditions, Encounters, etc.) including full history, use the [`$purge` operation](../../api/bulk-api/purge.md):

```
POST /fhir/Patient/pt-1/$purge
```

This is the recommended FHIR-compliant approach for complete patient data removal. It supports both synchronous and asynchronous execution for large datasets.

**When to use $purge vs manual DELETE:**

| Approach | Use case |
|---|---|
| `$purge` | Delete a patient and all their compartment resources in one operation |
| `DELETE` / Transaction | Delete specific individual resources |
| SQL (see below) | Delete resource history only, without deleting the resource itself |

See the [$purge documentation](../../api/bulk-api/purge.md) for async mode, custom compartment definitions, and more details.

## Transactional Delete

If you need to delete multiple resources, use [FHIR Transaction](../../api/batch-transaction.md) with multiple `DELETE` requests.

This operation groups multiple REST API operations in a transaction. If all `DELETE` operations are successful, the transaction with `DELETE` requests is equivalent to calling `DELETE` multiple times.

## Delete resource history

If you need to delete resource history, you need to execute SQL manually.

Example:

```
DELETE * FROM Patient_history
WHERE id='pt-1'
```

Or you can make a REST endpoint from SQL query using AidboxQuery.

Example:

```yaml
PUT /AidboxQuery/remove-patient-history

params:
  id:
    isRequired: true
    type: string
    format: '%s'
query: 'DELETE FROM Patient_history where id = {{params.id}}'
type: execute
```

And then run it with

```
GET /$query/remove-patient-history?id=pt-1
```

## Delete all resources and history

You can use `truncate` PostgreSQL statement to delete everything from the specified table.

Example:

```
truncate Patient;
truncate Patient_history;
```

Similarly you can create an endpoint for this

```yaml
PUT /AidboxQuery/truncate

query: 'TRUNCATE "{{resourceType}}"; TRUNCATE "{{resourceType}}_history"'
type: execute
```

And then run it with

```
GET /Patient?_query=truncate
```

Note that AidboxQuery with variable `resourceType` can only be called with `_query` parameter.

## Delete resources in CI environment

In CI environments it is often desired to have same state at the beginning of each test suite. You can simply stop Aidbox, drop database, and start Aidbox again.

Use [Init Bundle](../../configuration/init-bundle.md) to set up fixtures.&#x20;

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

## Transactional Delete

If you need to delete multiple resources, use [FHIR Transaction](broken-reference) with multiple `DELETE` requests.

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

Use [Aidbox project](broken-reference) to set up fixtures. Note that you can use different Aidbox projects for each test suite (or different entrypoints).

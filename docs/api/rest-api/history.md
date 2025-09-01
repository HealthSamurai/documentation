---
description: History operation
---

# History

## Overview

The history operation retrieves the history of either a particular resource, all resources of a given type, or all resources supported by the system. The operation is performed by HTTP `GET` command. See the FHIR documentation [http://hl7.org/fhir/http.html#history](http://hl7.org/fhir/http.html#history) for more details.

| Operation Scope   | Request                                                                 | Aidbox          |
| ----------------- | ----------------------------------------------------------------------- | --------------- |
| Specific Resource | `GET /<RESOURCE_TYPE>/<ID>/_history{?[parameters]&_format=[mime-type]}` | `Supported`     |
| Resource Type     | `GET /<RESOURCE_TYPE>/_history{?[parameters]&_format=[mime-type]}`      | `Supported`     |
| All Resources     | `GET /_history{?[parameters]&_format=[mime-type]}`                      | `Not Supported` |

The result of the history operation is a Bundle with type `history` containing links section for pagination, the specified version history, sorted with newest versions first, and including deleted resources.

The `request` element provides information about the interaction that occurred and caused the new version of the resource.

Only operations create, update, and delete create history entries. Conditional creates, updates and deletes are converted to direct updates and deletes in a history list.

#### Internals of History API

History table is a separate table into which a previous resource version is inserted on the resource update. When you make a call through History API the following query is invoked joining the _resource table_ and _history table_ for the same resource (example for Patient resource):

```sql
WITH history AS (
    (SELECT id, txid, ts, resource_type, status::text AS status, resource, cts 
           FROM "patient" WHERE id = ?) 
    UNION 
    (SELECT id, txid, ts, resource_type, status::text AS status, resource, cts
           FROM "patient_history" WHERE id = ?)) 
SELECT * FROM history ORDER BY txid DESC
LIMIT ? OFFSET ?
```

On the create operation (e.g. via PUT/POST CRUD requests or [$import](../bulk-api/import-and-fhir-import.md)), i.e. the table does not contain the resource with same id and resource type, History API call will return the most recent resource version which is stored in the resource table with name `<resourceType>_history`(e.g. `patient_history` table).

On update operation the old version of the resource in the main table is replaced by a new one. The old version firstly is moved to resource table and replaced with a new version in the main history table.

As all previous versions of the resource are stored in history table, the size of this table will grow with every update, on the size of the resource that was updated. At the same time, as history records are stored in a separate table, the _size_ of this table doesn't affect the performance of the operations not related to history (search, CRUD, etc.).

#### Example: create (POST, PUT)

```yaml
resourceType: Bundle
type: history
total: 1
entry:
- resource:
    name:
    - family: History
    id: patient123
    resourceType: Patient
    meta:
      lastUpdated: '2018-11-27T11:33:59.497Z'
      versionId: '54'
      tag:
      - system: https://aidbox.app
        code: created
  request:
    method: POST
    url: Patient
```

#### Example: update (PUT, PATCH)

```yaml
resourceType: Bundle
type: history
total: 2
entry:
- resource:
    name:
    - family: History
    birthDate: '1967-03-14'
    id: patient123
    resourceType: Patient
    meta:
      lastUpdated: '2018-11-27T21:58:51.396Z'
      versionId: '57'
      tag:
      - system: https://aidbox.app
        code: updated
  request:
    method: PUT
    url: Patient
- resource:
    name:
    - family: History
    id: patient123
    resourceType: Patient
    meta:
      lastUpdated: '2018-11-27T11:33:59.497Z'
      versionId: '54'
      tag:
      - system: https://aidbox.app
        code: created
  request:
    method: POST
    url: Patient
```

#### Example of the delete (DELETE) operation in history:

```yaml
resourceType: Bundle
type: history
total: 3
entry:
- resource:
    name:
    - family: History
    birthDate: '1967-03-14'
    id: patient123
    resourceType: Patient
    meta:
      lastUpdated: '2018-11-27T22:01:16.609Z'
      versionId: '58'
      tag:
      - system: https://aidbox.app
        code: deleted
  request:
    method: DELETE
    url: Patient
- resource:
    name:
    - family: History
    birthDate: '1967-03-14'
    id: patient123
    resourceType: Patient
    meta:
      lastUpdated: '2018-11-27T21:58:51.396Z'
      versionId: '57'
      tag:
      - system: https://aidbox.app
        code: updated
  request:
    method: PUT
    url: Patient
- resource:
    name:
    - family: History
    id: patient123
    resourceType: Patient
    meta:
      lastUpdated: '2018-11-27T11:33:59.497Z'
      versionId: '54'
      tag:
      - system: https://aidbox.app
        code: created
  request:
    method: POST
    url: Patient
```

## History parameters

The parameters to the history interaction may include:

| Parameter | Type    | Description                                                                                                                                    |
| --------- | ------- | ---------------------------------------------------------------------------------------------------------------------------------------------- |
| \_count   | integer | Number of return records requested. The server is not bound to return the number requested, but cannot return more.                            |
| \_txid    | integer | Only include resource versions whose logical transaction sequence number (= versionId) is greater than the given number.                       |
| \_since   | instant | Only include resource versions that were created at or after the given instant in time.                                                        |
| \_at      | date    | Only include resource versions that were current at some point during the time period specified in the date time value (may be more than one). |
| \_count   | integer | Total records on the page (default - 100)                                                                                                      |
| \_page    | integer | Specific page pointer                                                                                                                          |
| \_format  | enum    | Result format: yaml, json, xml, edn, transit                                                                                                   |

### Examples of the history requests with parameters:

`{{base}}/Patient/_history?_count=3`

`{{base}}/Patient/_history?_since=2018-11-27T22:00:16.609Z`

`{{base}}/Patient/_history?_at=2018-11-27`

`{{base}}/Patient/_history?_format=yaml`

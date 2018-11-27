---
description: History operation
---

# History

## Overview

The history operation retrieves the history of either a particular resource, all resources of a given type, or all resources supported by the system. The operation is performed by HTTP `GET` command. See the FHIR documentation [http://hl7.org/fhir/http.html\#history](http://hl7.org/fhir/http.html#history) for more details.

All sample requests are available in the Postman collection:[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/view-collection/81e23283893f85027f4f?referrer=https%3A%2F%2Fapp.getpostman.com%2Frun-collection%2F81e23283893f85027f4f%23%3Fenv[Aidbox.Cloud]%3DW3sia2V5IjoiYmFzZTEiLCJ2YWx1ZSI6Imh0dHBzOi8vbWVyZWRpdGguYWlkYm94LmFwcCIsImRlc2NyaXB0aW9uIjoiIiwiZW5hYmxlZCI6ZmFsc2V9LHsia2V5IjoiYmFzZSIsInZhbHVlIjoiaHR0cHM6Ly9wYXZseXNoaW5hMjAxODExMDkuYWlkYm94LmFwcCIsImRlc2NyaXB0aW9uIjoiIiwiZW5hYmxlZCI6dHJ1ZX1d&_ga=2.191880237.625263863.1543359065-654445837.1543359065)

| Operation Scope | Request | Aidbox |
| :--- | :--- | :--- |
| Specific Resource | `GET /<RESOURCE_TYPE>/<ID>/_history{?[parameters]&_format=[mime-type]}` | `Supported` |
| Resource Type | `GET /<RESOURCE_TYPE>/_history{?[parameters]&_format=[mime-type]}` | `Supported` |
| All Resources | `GET /_history{?[parameters]&_format=[mime-type]}` | `Not Supported` |

The result of history operation is a Bundle with type `history` containing the specified version history, sorted with newest versions first, and including deleted resources.

The `request` element provides information about the interaction that occurred and caused the new version of the resource.

Only operations create, update, and delete create history entries. Conditional creates, updates and deletes are converted to direct updates and deletes in a history list.

### Example of the create \(POST, PUT\) operation in history:

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

### Example of the update \(PUT, PATCH\) operation in history:

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

#### Example of the delete \(DELETE\) operation in history:

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

## History Parameters

 The parameters to the history interaction may include:

| Parameter | Type | Description |
| :--- | :--- | :--- |
| `_count` : [`integer`](http://hl7.org/fhir/datatypes.html#integer) | single | Number of return records requested. The server is not bound to return the number requested, but cannot return more. |
| `_since` : [`instant`](http://hl7.org/fhir/datatypes.html#integer) | single | Only include resource versions that were created at or after the given instant in time. |
| `_at` : [`dateTime`](http://hl7.org/fhir/datatypes.html#integer) | single | Only include resource versions that were current at some point during the time period specified in the date time value \(may be more than one\). |
|  `_format` | single | Result format. E.g. JSON, XML, etc. |

### Examples of the history requests with parameters:

 `{{base}}/Patient/_history?_count=3`

 `{{base}}/Patient/_history?_since=2018-11-27T22:00:16.609Z`

 `{{base}}/Patient/_history?_at=2018-11-27`

 `{{base}}/Patient/_history?_format=yaml`


# $import & /fhir/$import

`$import` is an implementation of the upcoming FHIR Bulk Import API. This is an asynchronous Operation, which returns url to monitor progress. There are two versions of this operation - `/fhir/$import` accepts data in FHIR format,  `/$import` works with [Aidbox format](../../modules-1/fhir-resources/aidbox-and-fhir-formats.md).

#### Resource requirements for all import operations:

| Operation       | id       | resourceType |
| --------------- | -------- | ------------ |
| `/$import`      | Required | Not required |
| `/fhir/$import` | Required | Not required |

{% hint style="warning" %}
Keep in mind that $import **does not validate** inserted resources for the sake of performance. Pay attention to the structure of data you insert and use the correct URL for your data format, i.e.: use /fhir prefix for FHIR data.
{% endhint %}

{% hint style="info" %}
Please consider using [Asynchronous validation API](../../profiling-and-validation/validation-api.md#asynchronous-batch-validation-draft) to validate data after $import
{% endhint %}

### Example

{% tabs %}
{% tab title="Request" %}
```yaml
POST /fhir/$import
Accept: text/yaml
Content-Type: text/yaml

id: synthea
contentEncoding: gzip
inputs:
- resourceType: Encounter
  url: https://storage.googleapis.com/aidbox-public/synthea/100/Encounter.ndjson.gz
- resourceType: Organization
  url: https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
- resourceType: Patient
  url: https://storage.googleapis.com/aidbox-public/synthea/100/Patient.ndjson.gz
```
{% endtab %}

{% tab title="Response" %}
```
200
```
{% endtab %}
{% endtabs %}

You can monitor progress by using `id` you provided in request body.

```yaml
GET /BulkImportStatus/synthea
```

{% hint style="info" %}
If you didn't provide `id` in request body, you can use `content-location` in response header.
{% endhint %}

### Parameters

| Parameter         | Description                                             |
| ----------------- | ------------------------------------------------------- |
| `id`              | Identifier of the import                                |
| `contentEncoding` | Supports `gzip` or `plain` (non-gzipped .ndjson files)  |
| `inputs`          | Resources to import                                     |
| `update`          | Update history for updated resources (false by default) |

### Note

For performance reasons `$import` does raw upsert into resource table without history update. If you want to store the previous version of resources in history, please set `update = true`

With this flag Aidbox will update history for updated resources. For each resource:

* if resource was not present in DB before the import, the import time will be the same.
* if resource was present in DB before and it's updated during the import, it will double the time importing this resource because of additional insert operation into `_history` table.

## $import on top of the Task API (beta)

Aidbox has introduced a new and improved version of the $import operation, currently in beta, to enhance its reliability and performance. By implementing this operation on top of the [task-api.md](../task-api.md "mention"), it allows the $import operation to be more reliable, continue work after restarts, and handle errors correctly. The Task API also enables the operation to accept multiple requests and execute them from a queue while simultaneously processing multiple items from the "inputs" field (with a default of two items processed simultaneously). Users can monitor the status of the operation through the   [#task-ui](../task-api.md#task-ui "mention").

In the future, the ability to list and cancel $import operations will be added, as well as detailed progress info of the operation.

{% hint style="info" %}
To enable new version of $import API (`/v2/$import` & `/v2/fhir/$import`) set environment variable `BOX_BULK__API_ENGINE=task-api`
{% endhint %}

**Changes in the new $import API:**

1. Executing more than one import with the same `id` is not possible. Users can omit the \`id\` field from the request, allowing Aidbox to generate the ID.
2. The status of the workflow can be accessed with a GET request to `/v2/$import/<id>` instead of `/BulkImportStatus/<id>`. The URL for the import status is returned in the `content-location` header of the $import request.

Please note that the new implementation is currently in beta, and further improvements and refinements may be made as needed.

To start import make a POST request to `/v2[/fhir]/$import`:

{% tabs %}
{% tab title="Request" %}
```yaml
POST /v2/fhir/$import
Accept: text/yaml
Content-Type: text/yaml

id: synthea
contentEncoding: gzip
inputs:
- resourceType: Encounter
  url: https://storage.googleapis.com/aidbox-public/synthea/100/Encounter.ndjson.gz
- resourceType: Organization
  url: https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
- resourceType: Patient
  url: https://storage.googleapis.com/aidbox-public/synthea/100/Patient.ndjson.gz
```
{% endtab %}

{% tab title="Response" %}
#### Status
```
200 OK
```
#### Headers
```
Content-Location:  /v2/$import/synthea
```
{% endtab %}
{% endtabs %}
### Parameters

| Parameter         | Description                                                                         |
| ----------------- | ----------------------------------------------------------------------------------- |
| `id`              | Identifier of the import. If you don't provide this, the id will be auto-generated. |
|                   | You can check it on `Content-Location` header in response                           |
| `contentEncoding` | Supports `gzip` or `plain` (non-gzipped .ndjson files)                              |
| `inputs`          | Resources to import                                                                 |
| `update`          | Update history for updated resources (false by default)                             |


To check the staus of import make a GET request to `/v2/$import/<id>`:

{% tabs %}
{% tab title="Request" %}
```html
GET /v2/$import/<id>
```
{% endtab %}

{% tab title="Response (done - succeeded)" %}
#### Status
```
200 OK
```
#### Body
```yaml
type: fhir
inputs:
  - url: >-
      https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
    resourceType: Organization
    status: done
    outcome: succeeded
    result:
      imported-resources: 0
  - url: >-
      https://storage.googleapis.com/aidbox-public/synthea/100/Encounter.ndjson.gz
    resourceType: Encounter
    status: done
    outcome: succeeded
    result:
      imported-resources: 3460
  - url: https://storage.googleapis.com/aidbox-public/synthea/100/Patient.ndjson.gz
    resourceType: Patient
    status: done
    outcome: succeeded
    result:
      imported-resources: 124
contentEncoding: gzip
status: done
outcome: succeeded
result:
  message: All input files imported, 3584 new resources loaded
  total-files: 3
  total-imported-resources: 3584
  ```
{% endtab %}
{% tab title="Response (done - failed)" %}
#### Status
```
200 OK
```
#### Body
```yaml
type: fhir
inputs:
  - url: >-
      https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
    resourceType: Organization
    status: done
    outcome: failed
    error:
      message: '403: Forbidden'
  - url: >-
      https://storage.googleapis.com/aidbox-public/synthea/100/Encounter.ndjson.gz
    resourceType: Encounter
    status: done
    outcome: failed
    error:
      message: '403: Forbidden'
  - url: >-
      https://storage.googleapis.com/aidbox-public/synthea/100/Patient.ndjson.gz
    resourceType: Patient
    status: done
    outcome: failed
    error:
      message: '403: Forbidden'
contentEncoding: gzip
status: done
outcome: failed
error:
  message: >-
    Import for some files failed with an error: task 'Organization
    https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
    failed
```
{% endtab %}
{% endtabs %}

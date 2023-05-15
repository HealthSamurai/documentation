# $import & /fhir/$import

`$import` is an implementation of the upcoming FHIR Bulk Import API. This is an asynchronous Operation, which returns url to monitor progress. There are two versions of this operation - `/fhir/$import` accepts data in FHIR format, `/$import` works with [Aidbox format](../../getting-started/aidbox-and-fhir-formats.md).

#### Resource requirements for all import operations:

| Operation       | id       | resourceType |
| --------------- | -------- | ------------ |
| `/$import`      | Required | Not required |
| `/fhir/$import` | Required | Not required |

{% hint style="warning" %}
Keep in mind that $import **does not validate** inserted resources for the sake of performance. Pay attention to the structure of data you insert and use the correct URL for your data format, i.e.: use /fhir prefix for FHIR data.
{% endhint %}

{% hint style="info" %}
Please consider using [Asynchronous validation API](../../modules-1/profiling-and-validation/validation-api.md#asynchronous-batch-validation-draft) to validate data after $import
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
```yaml
status: 200
```
{% endtab %}
{% endtabs %}

### Parameters

| Parameter         | Description                                             |
| ----------------- | ------------------------------------------------------- |
| `id`              | Identifier of the import                                |
| `contentEncoding` | Supports `gzip` or `plain` (non-gzipped .ndjson files)  |
| `inputs`          | Resources to import                                     |
| `update`          | Update history for updated resources (false by default) |



You can monitor progress by using `id` you provided in request body.

{% tabs %}
{% tab title="Request" %}
```http
GET /BulkImportStatus/synthea
```
{% endtab %}

{% tab title="Response (Not Finished)" %}
**Status**

```
200
```

**Body**

```yaml
time:
  start: '2023-05-15T14:45:33.28722+02:00'
type: aidbox
inputs:
  - url: >-
      https://storage.googleapis.com/aidbox-public/synthea/100/Encounter.ndjson.gz
    resourceType: Encounter
contentEncoding: gzip
id: >-
  synthea
resourceType: BulkImportStatus
meta:
  lastUpdated: '2023-05-15T12:45:33.278829Z'
  createdAt: '2023-05-15T12:45:33.278829Z'
  versionId: '129363'

```
{% endtab %}

{% tab title="Response (Finished)" %}
**Status**

```
200
```

**Body**

```yaml
time:
  end: '2023-05-15T14:45:33.820465+02:00'
  start: '2023-05-15T14:45:33.28722+02:00'
type: aidbox
inputs:
  - ts: '2023-05-15T14:45:33.819425+02:00'
    url: >-
      https://storage.googleapis.com/aidbox-public/synthea/100/Encounter.ndjson.gz
    total: 3460
    status: finished
    duration: 530
    resourceType: Encounter
status: finished
contentEncoding: gzip
id: >-
  synthea
resourceType: BulkImportStatus
meta:
  lastUpdated: '2023-05-15T12:45:33.278829Z'
  createdAt: '2023-05-15T12:45:33.278829Z'
  versionId: '129363'
```
{% endtab %}

{% tab title="Response (Failed)" %}
**Status**

```
200
```

**Body**

```yaml
time:
  end: '2023-05-15T14:45:33.820465+02:00'
  start: '2023-05-15T14:45:33.28722+02:00'
type: aidbox
inputs:
  - ts: '2023-05-15T14:45:33.819425+02:00'
    url: >-
      https://storage.googleapis.com/aidbox-public/synthea/100/Encounter.ndjson.gz
    error: '403: Forbidden'
    status: failed
    resourceType: Encounter
status: finished
contentEncoding: gzip
id: >-
  synthea
resourceType: BulkImportStatus
meta:
  lastUpdated: '2023-05-15T12:45:33.278829Z'
  createdAt: '2023-05-15T12:45:33.278829Z'
  versionId: '129363'
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
If you didn't provide `id` in request body, you can use `content-location` in response header.
{% endhint %}

### Result

| Parameter               | Type      | Description                                                                                                                                                                                                                                                                                                        |
| ----------------------- | --------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **`id`**                | string    | Identifier of the import                                                                                                                                                                                                                                                                                           |
| **`resourceType`**      | string    | <p>Type of resource where the progress of import operation is recorded. <br><em>Possible value</em>: <code>BulkImportStatus</code></p>                                                                                                                                                                             |
| **`meta`**              | object    |                                                                                                                                                                                                                                                                                                                    |
| `meta.createdAt`        | string    | Timestamp string at which the resource was created                                                                                                                                                                                                                                                                 |
| `meta.lastUpdated`      | string    | Timestamp string at which the resource was updated last time                                                                                                                                                                                                                                                       |
| `meta.versionId`        | string    | Version id of this resource                                                                                                                                                                                                                                                                                        |
| **`contentEncoding`**   | string    |  `gzip` or `plain`                                                                                                                                                                                                                                                                                                 |
| **`time`**              | object    |                                                                                                                                                                                                                                                                                                                    |
| `time.start`            | string    | Timestamp string at which the operation started in ISO format                                                                                                                                                                                                                                                      |
| `time.end`              | string    | <p>Timestamp string at which the operation was completed in ISO format.<br>Only present after the entire import operation has been completed</p>                                                                                                                                                                   |
| **`type`**              | string    | <p>Data format type to be loaded.</p><p><em>Possible values</em>: <code>aidbox</code>, <code>fhir</code> </p>                                                                                                                                                                                                      |
| **`inputs`**            | object\[] |                                                                                                                                                                                                                                                                                                                    |
| `inputs[].url`          | string    | URL from which load resources                                                                                                                                                                                                                                                                                      |
| `inputs[].resourceType` | string    | Resource type to be loaded                                                                                                                                                                                                                                                                                         |
| `inputs[].status`       | string    | <p>Load status for each input.<br>Only present after the operation for this input has been completed.</p><p><em>Possible values</em>: <code>finished</code>, <code>failed</code> </p>                                                                                                                              |
| `inputs[].total`        | integer   | <p>The number of loaded resources.<br>Only present after the operation for this input has been completed successfully</p>                                                                                                                                                                                          |
| `inputs[].ts`           | string    | <p>Timestamp string at which the loading was completed in ISO format.<br>Only present after the operation for this input has been completed</p>                                                                                                                                                                    |
| `inputs[].duration`     | integer   | <p>Duration of loading in milliseconds.<br>Only present after the operation for this input has been completed successfully</p>                                                                                                                                                                                     |
| **`status`**            | string    | <p>Load status for all inputs.</p><p>Only present after the entire import operation has been completed.<br>After completed, this value is always <code>finished</code>, regardless of whether each input is <code>finished</code> or <code>failed</code>.</p><p><em>Possible value</em>: <code>finished</code></p> |

###

### Note

For performance reasons `$import` does raw upsert into resource table without history update. If you want to store the previous version of resources in history, please set `update = true`

With this flag Aidbox will update history for updated resources. For each resource:

* if resource was not present in DB before the import, the import time will be the same.
* if resource was present in DB before and it's updated during the import, it will double the time importing this resource because of additional insert operation into `_history` table.





## /v2/$import on top of the Workflow Engine

Aidbox has introduced a new and improved version of the $import operation, currently in beta, to enhance its reliability and performance. By implementing this operation on top of the [workflow-engine](../../modules-1/workflow-engine/ "mention"), it allows the $import operation to be more reliable, continue work after restarts, and handle errors correctly. The Task API also enables the operation to accept multiple requests and execute them from a queue while simultaneously processing multiple items from the "inputs" field (with a default of two items processed simultaneously). Users can monitor the status of the operation through the [monitoring.md](../../modules-1/workflow-engine/monitoring.md "mention").

In the future, the ability to list and cancel $import operations will be added, as well as detailed progress info on the operation.

{% hint style="info" %}
To enable new version of $import API (`/v2/$import` & `/v2/fhir/$import`) set environment variable `BOX_BULK__API_ENGINE=task-api`
{% endhint %}

**Changes in the new $import API:**

1. Executing more than one import with the same `id` is not possible. Users can omit the \`id\` field from the request, allowing Aidbox to generate the ID.
2. The status of the workflow can be accessed with a GET request to `/v2/$import/<id>` instead of `/BulkImportStatus/<id>`. The URL for the import status is returned in the `content-location` header of the $import request.

{% hint style="warning" %}
* The new implementation is currently in beta, and further improvements and refinements may be made as needed.
* This feature is currently not available in [Multibox](https://docs.aidbox.app/multibox/multibox-box-manager-api)
{% endhint %}

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
**Status**

```
200 OK
```

**Headers**

```
Content-Location:  /v2/$import/synthea
```
{% endtab %}
{% endtabs %}

### Parameters

| Parameter           | Description                                                                                                                                                            |
| ------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `id`                | <p>Identifier of the import.<br>If you don't provide this, the id will be auto-generated. You can check it on <code>Content-Location</code> header in the response</p> |
| `contentEncoding`   | Supports `gzip` or `plain` (non-gzipped .ndjson files)                                                                                                                 |
| `inputs` (required) | <p>Resources to import</p><ul><li><code>url</code> - URL from which load resources</li><li><code>resourceType</code> - Resource type to be loaded</li></ul>            |
| `update`            | Update history for updated resources (false by default)                                                                                                                |



To check the status of the import make a GET request to `/v2/$import/<id>`:

As the operation is built on top of our workflow engine, the statuses and outcomes of the files and import as a whole are inherited from [#task-statuses-and-outcomes](../../modules-1/workflow-engine/task/#task-statuses-and-outcomes "mention")



{% tabs %}
{% tab title="Request" %}
```http
GET /v2/$import/<id>
```
{% endtab %}

{% tab title="Response (In progress)" %}
**Status**

```
200 OK
```

**Body**

```yaml
type: fhir
inputs:
  - url: >-
      https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
    resourceType: Organization
    status: in-progress
  - url: >-
      https://storage.googleapis.com/aidbox-public/synthea/100/Encounter.ndjson.gz
    resourceType: Encounter
    status: waiting
  - url: https://storage.googleapis.com/aidbox-public/synthea/100/Patient.ndjson.gz
    resourceType: Patient
    status: waiting
contentEncoding: gzip
status: in-progress
```
{% endtab %}

{% tab title="Response (done - succeeded)" %}
**Status**

```
200 OK
```

**Body**

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
**Status**

```
200 OK
```

**Body**

```yaml
type: fhir
inputs:
  - url: >-
      https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
    resourceType: Organization
    status: done
    outcome: succeeded
    result:
      imported-resources: 225
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
    Import for some files failed with an error: task 'Encounter
    https://storage.googleapis.com/aidbox-public/synthea/100/Encounter.ndjson.gz
    failed
```
{% endtab %}
{% endtabs %}

###

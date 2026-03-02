---
description: Bulk import FHIR resources asynchronously using $import operation with progress monitoring.
---

# $import and /fhir/$import

`$import` is an implementation of the upcoming FHIR Bulk Import API. This is an asynchronous Operation, which returns url to monitor progress. There are two versions of this operation - `/fhir/$import` accepts data in FHIR format, `/$import` works with [Aidbox format](../rest-api/other/aidbox-and-fhir-formats.md).

#### Resource requirements for all import operations:

| Operation       | id       | resourceType |
| --------------- | -------- | ------------ |
| `/$import`      | Required | Not required |
| `/fhir/$import` | Required | Not required |

{% hint style="warning" %}
Keep in mind that $import **does not validate** inserted resources for the sake of performance. Pay attention to the structure of data you insert and use the correct URL for your data format, i.e.: use /fhir prefix for FHIR data.
{% endhint %}

{% hint style="info" %}
Please consider using [Asynchronous validation API](../../modules/profiling-and-validation/asynchronous-resource-validation.md) to validate data after $import
{% endhint %}

## Example

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
HTTP 200 OK
```

The response body is empty. A `200` status code indicates the import has started successfully.
{% endtab %}
{% endtabs %}

## Parameters

<table><thead><tr><th width="258">Parameter</th><th>Description</th></tr></thead><tbody><tr><td><code>id</code></td><td>Identifier of the import</td></tr><tr><td><code>contentEncoding</code></td><td>Supports <code>gzip</code> or <code>plain</code> (non-gzipped .ndjson files)</td></tr><tr><td><code>inputs</code></td><td>Resources to import</td></tr><tr><td><code>update</code></td><td>Update history for updated resources (false by default)</td></tr></tbody></table>

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

## Result

<table><thead><tr><th width="262">Parameter</th><th width="95.33333333333331">Type</th><th>Description</th></tr></thead><tbody><tr><td><strong><code>id</code></strong></td><td>string</td><td>Identifier of the import</td></tr><tr><td><strong><code>resourceType</code></strong></td><td>string</td><td>Type of resource where the progress of import operation is recorded.<br><em>Possible value</em>: <code>BulkImportStatus</code></td></tr><tr><td><strong><code>meta</code></strong></td><td>object</td><td></td></tr><tr><td><code>meta.createdAt</code></td><td>string</td><td>Timestamp string at which the resource was created</td></tr><tr><td><code>meta.lastUpdated</code></td><td>string</td><td>Timestamp string at which the resource was updated last time</td></tr><tr><td><code>meta.versionId</code></td><td>string</td><td>Version id of this resource</td></tr><tr><td><strong><code>contentEncoding</code></strong></td><td>string</td><td><code>gzip</code> or <code>plain</code></td></tr><tr><td><strong><code>time</code></strong></td><td>object</td><td></td></tr><tr><td><code>time.start</code></td><td>string</td><td>Timestamp string at which the operation started in ISO format</td></tr><tr><td><code>time.end</code></td><td>string</td><td>Timestamp string at which the operation was completed in ISO format.<br>Only present after the entire import operation has been completed</td></tr><tr><td><strong><code>type</code></strong></td><td>string</td><td><p>Data format type to be loaded.</p><p><em>Possible values</em>: <code>aidbox</code>, <code>fhir</code></p></td></tr><tr><td><strong><code>inputs</code></strong></td><td>object[]</td><td></td></tr><tr><td><code>inputs[].url</code></td><td>string</td><td>URL from which load resources</td></tr><tr><td><code>inputs[].resourceType</code></td><td>string</td><td>Resource type to be loaded</td></tr><tr><td><code>inputs[].status</code></td><td>string</td><td><p>Load status for each input.<br>Only present after the operation for this input has been completed.</p><p><em>Possible values</em>: <code>finished</code>, <code>failed</code></p></td></tr><tr><td><code>inputs[].total</code></td><td>integer</td><td>The number of loaded resources.<br>Only present after the operation for this input has been completed successfully</td></tr><tr><td><code>inputs[].ts</code></td><td>string</td><td>Timestamp string at which the loading was completed in ISO format.<br>Only present after the operation for this input has been completed</td></tr><tr><td><code>inputs[].duration</code></td><td>integer</td><td>Duration of loading in milliseconds.<br>Only present after the operation for this input has been completed successfully</td></tr><tr><td><strong><code>status</code></strong></td><td>string</td><td><p>Load status for all inputs.</p><p>Only present after the entire import operation has been completed.<br>After completed, this value is always <code>finished</code>, regardless of whether each input is <code>finished</code> or <code>failed</code>.</p><p><em>Possible value</em>: <code>finished</code></p></td></tr></tbody></table>

## Note

For performance reasons `$import` does raw upsert into the resource table without history update. If you want to store the previous version of resources in history, please set `update = true`

With this flag, Aidbox will update the history for updated resources. For each resource:

* if the resource was not present in DB before the import, the import time will be the same.
* if the resource was present in DB before and it's updated during the import, it will double the time importing this resource because of the additional insert operation into the `_history` table.

## /v2/$import

Improved version of the $import operation with enhanced reliability and performance. This version continues work after restarts, handles errors correctly, accepts multiple requests executing them from a queue, and simultaneously processes multiple items from the "inputs" field (with a default of two items processed simultaneously).

**Changes in the new $import API:**

1. Executing more than one import with the same `id` is not possible. Users can omit the \`id\` field from the request, allowing Aidbox to generate the ID.
2. The status of the workflow can be accessed with a GET request to `/v2/$import/<id>` instead of `/BulkImportStatus/<id>`. The URL for the import status is returned in the `content-location` header of the $import request.

{% hint style="warning" %}
This feature is not available in Multibox
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

<table><thead><tr><th width="226">Parameter</th><th>Description</th></tr></thead><tbody><tr><td><code>id</code></td><td>Identifier of the import.<br>If you don't provide this, the id will be auto-generated. You can check it on <code>Content-Location</code> header in the response</td></tr><tr><td><code>contentEncoding</code></td><td>Supports <code>gzip</code> or <code>plain</code> (non-gzipped .ndjson files)</td></tr><tr><td><code>inputs</code> (required)</td><td><p>Resources to import</p><ul><li><code>url</code> - URL from which load resources</li><li><code>resourceType</code> - Resource type to be loaded</li></ul></td></tr><tr><td><code>update</code></td><td>Update history for updated resources (false by default)</td></tr><tr><td><code>allowedRetryCount</code></td><td>Set the maximum number of import retries for each input (2 by default)</td></tr></tbody></table>

To check the status of the import make a GET request to `/v2/$import/<id>`:

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

## Import local file

Sometimes you want to import local file into local Aidbox. Possible solutions for local development:

#### Add volume to the `aidboxone` container (not `aidboxdb`):

```
volumes:
- ./Encounter.ndjson.gz:/resources/Encounter.ndjson.gz
# url: file:///resources/Encounter.ndjson.gz
```

#### Use tunneling e.g. ngrok:&#x20;

<pre><code>python3 -m http.server 
ngrok http 8000
<strong># url: https://&#x3C;...>.ngrok-free.app/Encounter.ndjson.gz
</strong></code></pre>

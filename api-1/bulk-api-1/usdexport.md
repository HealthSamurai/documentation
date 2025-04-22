---
description: FHIR Bulk Data Export
---

# $export

The [FHIR Bulk Data Export](https://hl7.org/fhir/uv/bulkdata/export.html) feature allows to export FHIR resources in ndjson format.

Aidbox supports patient-level and group-level export. When the export request is submitted the server returns URL to check the export status. When export is finished, status endpoint returns URLs to download resources.

Only one export process can be run at the same time. If you try to submit an export request while there is active export, you get `429 Too Many Requests` error.

## Setup storage

Aidbox can export data to GCP or AWS cloud. Export results will be in `<datetime>_<uuid>` folder on the bucket.

### GCP

[Create bucket](https://cloud.google.com/storage/docs/creating-buckets) and [service account](https://cloud.google.com/iam/docs/creating-managing-service-accounts) that has read and write access to the bucket.

[Create `GcpServiceAccount` resource](../../storage/gcp-cloud-storage.md) in Aidbox. Example:

```yaml
private-key: |
  -----BEGIN PRIVATE KEY-----
  your-key-here
  -----END PRIVATE KEY-----
service-account-email: service-account@email
id: gcp-service-account
resourceType: GcpServiceAccount
```

Set the following environment variables:

* `box_bulk__storage_backend=gcp` — backend for export
* `box_bulk__storage_gcp_service__account` — id of the `GcpServiceAccount` resource
* `box_bulk__storage_gcp_bucket` — bucket name

### Azure

[Create Azure storage account](https://learn.microsoft.com/en-us/azure/storage/common/storage-account-create?tabs=azure-portal) and [storage container](https://learn.microsoft.com/en-us/azure/storage/blobs/blob-containers-portal#create-a-container).

Create `AzureAccount` resource in Aidbox.

```yaml
resourceType: AzureAccount
id: azureaccount                  # your storage account id
key: 7x..LA                       # your storage account key
```

Create `AzureContainer` resource in Aidbox.

```yaml
resourceType: AzureContainer
id: smartboxexporttestcontainer
account:
  resourceType: AzureAccount
  id: azureaccount
storage: azureaccount             # your storage account
container: azureaccountcontainer  # your account container
```

Set the following environment variables:

* `box_bulk__storage_backend=azure` — backend for export
* `box_bulk__storage_azure_container=smartboxexporttestcontainer` — id of the `AzureContainer` resource

### AWS

Create [S3 bucket](https://docs.aws.amazon.com/AmazonS3/latest/userguide/create-bucket-overview.html) and [IAM user](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html) that has read and write access to the bucket.

[Create `AwsAccount` resource](../../storage/aws-s3.md) in Aidbox. Example:

```yaml
region: us-east-1
access-key-id: your-key-id
secret-access-key: key
id: aws-account
resourceType: AwsAccount
```

Set the following environment variables:

* `box_bulk__storage_backend=aws` — backend for export
* `box_bulk__storage_aws_account` — id of the `AwsAccount` resource
* `box_bulk__storage_aws_bucket` — bucket name

## Parameters

| Parameter       | Description                                                                                                                                                                                                                                                                                               |
| --------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `_outputFormat` | <p>Specifies format in which the server generates files.<br>The following formats are supported:<br></p><ul><li><code>application/fhir+ndjson</code> — <code>.ndjson</code> files will be saved</li><li><code>application/fhir+ndjson+gzip</code> — <code>.ndjson.gz</code> files will be saved</li></ul> |
| `_type`         | Includes only the specified types. This list is comma-separated.                                                                                                                                                                                                                                          |
| `_since`        | Includes only resources changed after the specified time.                                                                                                                                                                                                                                                 |
| `patient`       | Export data that belongs only to listed patient. Format: comma-separated list of patient ids. Available only for patient-level export.                                                                                                                                                                    |

## Patient-level export

Patient-level export exports all Patient resources and resources associated with them. This association is defined by [FHIR Compartments](http://hl7.org/fhir/r4/compartmentdefinition-patient.html).

To start export make a request to `/fhir/Patient/$export`:

{% tabs %}
{% tab title="Request" %}
**Rest console**

```http
GET /fhir/Patient/$export
Accept: application/fhir+json
Prefer: respond-async
```
{% endtab %}

{% tab title="Response" %}
**Status**

202 Accepted

**Headers**

* `Content-Location` — Link to check export status (e.g. `/fhir/$export-status/<id>`)
{% endtab %}
{% endtabs %}

Make a request to the export status endpoint to check the status:

{% tabs %}
{% tab title="Request" %}
**Rest console**

```
GET /fhir/$export-status/<id>
```
{% endtab %}

{% tab title="Response (completed)" %}
**Status**

200 OK

**Body**

```jsonp
{
  "status": "completed",
  "transactionTime": "2021-12-08T08:28:06.489Z",
  "requiresAccessToken": false,
  "request": "[base]/fhir/Patient/$export"
  "output": [
    {
      "type": "Patient",
      "url": "https://storage/some-url",
      "count": 2
    },
    {
      "type": "Person",
      "url": "https://storage/some-other-url",
      "count": 1
    }
  ]
}
```
{% endtab %}
{% endtabs %}

Delete request on the export status endpoint cancels export.

{% tabs %}
{% tab title="Request" %}
**Rest console**

```
DELETE /fhir/$export-status/<id>
```
{% endtab %}

{% tab title="Response" %}
**Status**

202 Accepted
{% endtab %}
{% endtabs %}

## Group-level export

Group-level export exports all Patient resources that belong to the specified group and resources associated with them. Characteristics of the group are not exported. This association is defined by [FHIR Compartments](http://hl7.org/fhir/r4/compartmentdefinition-patient.html).

To start export make a request to `/fhir/Group/<group-id>/$export`:

{% tabs %}
{% tab title="Request" %}
**Rest console**

```http
GET /fhir/Group/<group-id>/$export
Accept: application/fhir+json
Prefer: respond-async
```
{% endtab %}

{% tab title="Response" %}
**Status**

202 Accepted

**Headers**

* `Content-Location` — Link to check export status (e.g. `/fhir/$export-status/<id>`)
{% endtab %}
{% endtabs %}

Make a request to the export status endpoint to check the status:

{% tabs %}
{% tab title="Request" %}
**Rest console**

```
GET /fhir/$export-status/<id>
```
{% endtab %}

{% tab title="Response (completed)" %}
**Status**

200 OK

**Body**

```jsonp
{
  "status": "completed",
  "transactionTime": "2021-12-08T08:28:06.489Z",
  "requiresAccessToken": false,
  "output": [
    {
      "type": "Patient",
      "url": "https://storage/some-url",
      "count": 2
    },
    {
      "type": "Person",
      "url": "https://storage/some-other-url",
      "count": 1
    }
  ]
}
```
{% endtab %}
{% endtabs %}

Delete request on the export status endpoint cancels export.

{% tabs %}
{% tab title="Request" %}
**Rest console**

```http
DELETE /fhir/$export-status/<id>
```
{% endtab %}

{% tab title="Response" %}
**Status**

202 Accepted
{% endtab %}
{% endtabs %}

## System-level export

System-level export exports data from a FHIR server, whether or not it is associated with a patient. You may restrict the resources returned using the `_type` parameter.

{% hint style="warning" %}
Limitation: export operation will work for standard FHIR resources only, not for custom resources.
{% endhint %}

{% tabs %}
{% tab title="Request" %}
```http
GET /fhir/$export
Accept: application/fhir+json
Prefer: respond-async
```
{% endtab %}

{% tab title="Response(completed)" %}
**Status**

200 OK

**Body**

```jsonp
{
  "status": "completed",
  "transactionTime": "2021-12-08T08:28:06.489Z",
  "requiresAccessToken": false,
  "output": [
    {
      "type": "Patient",
      "url": "https://storage/some-url",
      "count": 2
    },
    {
      "type": "Person",
      "url": "https://storage/some-other-url",
      "count": 1
    }
  ]
}
```
{% endtab %}
{% endtabs %}

Delete request on the export status endpoint cancels export.

{% tabs %}
{% tab title="Request" %}
**Rest console**

```http
DELETE /fhir/$export-status/<id>
```
{% endtab %}

{% tab title="Response" %}
**Status**

202 Accepted
{% endtab %}
{% endtabs %}

## Troubleshooting guide

$export operation expects you setup external storage, Aidbox exports data into. In most cases issues with $exoprt are the consequence of incorrect Adbox configuration. In order to exclude this run the following rpc:

```
POST /rpc
Content-Type: text/yaml

method: aidbox.bulk/storage-healthcheck
```

Normally, you should see something like this in response body:

```
result:
  message: ok
  storage:
    type: gcp
    bucket: my_bucket
    account:
      id: gcp-acc
      resourceType: GcpServiceAccount
```

This means, that integration between Aidbox and your storage setup correctly.

What other responses you may see

### Storage-type not specified

Storage-type not specified error means, `box_bulk__storage_backend` env variable wasn't setup. Valid values are `aws` and `gcp`.

### Unsupported storage-type

unsupported storage-type error means, `box_bulk__storage_backend` env variable has invalid value. Valid values are `aws` and `gcp`.

### bulk-storage account not specified

This error means account is not specified

* `box_bulk__storage_gcp_service__account` for GCP
* `box_bulk__storage_aws_account` for AWS

### Account not found

This means there is no account for aws or gcp

Create [AWSAccount](../../storage/aws-s3.md) or [GCPServiceAccount](../../storage/gcp-cloud-storage.md), depending on your config.

### Bucket is not specified

This error means, bucket is not specified.

Specify `box_bulk__storage_gcp_bucket` for GCP.

Specify `box_bulk__storage_aws_bucket` for AWS.

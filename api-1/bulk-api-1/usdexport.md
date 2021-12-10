---
description: FHIR Bulk Data Export
---

# $export

The [FHIR Bulk Data Export](https://hl7.org/fhir/uv/bulkdata/export.html) feature allows to export FHIR resources in ndjson format.

Aidbox supports patient-level and group-level export. When the export request is submitted the server returns URL to check the export status. When export is finished, status endpoint returns URLs to download resources.&#x20;

Only one export process can be run at the same time. If you try to submit an export request while there is active export, you get `429 Too Many Requests` error.

## Setup storage

Aidbox can export data to GCP or AWS cloud. Export results will be in `<datetime>_<uuid>` folder on the bucket.&#x20;

### GCP

[Create bucket](https://cloud.google.com/storage/docs/creating-buckets) and [service account](https://cloud.google.com/iam/docs/creating-managing-service-accounts) that has read and write access to the bucket.

[Create `GcpServiceAccount` resource](../../storage-1/gcp-cloud-storage.md) in Aidbox. Example:

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

* `box_bulk__storage_backend=gcp`  — backend for export
* `box_bulk__storage_gcp_service__account` — id of the `GcpServiceAccount` resource
* `box_bulk__storage_gcp_bucket` — bucket name

### AWS

Create [S3 bucket](https://docs.aws.amazon.com/AmazonS3/latest/userguide/create-bucket-overview.html) and [IAM user](https://docs.aws.amazon.com/IAM/latest/UserGuide/id\_users\_create.html) that has read and write access to the bucket.

[Create `AwsAccount` resource](../../storage-1/aws-s3.md) in Aidbox. Example:

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

| Parameter       | Description                                                                                        |
| --------------- | -------------------------------------------------------------------------------------------------- |
| `_outputFormat` | Specifies format in which the server generates files. Only `application/fhir+ndjson` is supported. |
| `_type`         | Includes only the specified types. This list is comma-separated.                                   |
| `_since`        | Includes only resources changed after the specified time.                                          |

## Patient-level export

Patient-level export exports all Patient resources and resources associated with them. This association is defined by [FHIR Compartments](http://hl7.org/fhir/r4/compartmentdefinition-patient.html).

To start export make a request to `/fhir/Patient/$export`:

{% tabs %}
{% tab title="Request" %}
#### Rest console

```http
GET /fhir/Patient/$export
Accept: application/fhir+json
Prefer: respond-async
```
{% endtab %}

{% tab title="Response" %}
#### Status

202 Accepted

#### Headers

* `Content-Location` — Link to check export status (e.g. `/fhir/$export-status/<id>`)
{% endtab %}
{% endtabs %}

Make a request to the export status endpoint to check the status:

{% tabs %}
{% tab title="Request" %}
#### Rest console

```
GET /fhir/$export-status/<id>
```
{% endtab %}

{% tab title="Response (completed)" %}
#### Status

200 OK

#### Body

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
#### Rest console

```
DELETE /fhir/$export-status/<id>
```
{% endtab %}

{% tab title="Response" %}
#### Status

202 Accepted
{% endtab %}
{% endtabs %}

## Group-level export

Group-level export exports all Patient resources that belong to the specified group and resources associated with them. Characteristics of the group are not exported. This association is defined by [FHIR Compartments](http://hl7.org/fhir/r4/compartmentdefinition-patient.html).

To start export make a request to `/fhir/Group/<group-id>/$export`:

{% tabs %}
{% tab title="Request" %}
#### Rest console

```http
GET /fhir/Group/<group-id>/$export
Accept: application/fhir+json
Prefer: respond-async
```
{% endtab %}

{% tab title="Response" %}
#### Status

202 Accepted

#### Headers

* `Content-Location` — Link to check export status (e.g. `/fhir/$export-status/<id>`)
{% endtab %}
{% endtabs %}

Make a request to the export status endpoint to check the status:

{% tabs %}
{% tab title="Request" %}
#### Rest console

```
GET /fhir/$export-status/<id>
```
{% endtab %}

{% tab title="Response (completed)" %}
#### Status

200 OK

#### Body

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
#### Rest console

```http
DELETE /fhir/$export-status/<id>
```
{% endtab %}

{% tab title="Response" %}
#### Status

202 Accepted
{% endtab %}
{% endtabs %}

## System-level export

Export data from a FHIR server, whether or not it is associated with a patient. This supports use cases like backing up a server, or exporting terminology data by restricting the resources returned using the `_type` parameter.

{% tabs %}
{% tab title="Request" %}
```http
GET /fhir/$export
Accept: application/fhir+json
Prefer: respond-async
```
{% endtab %}

{% tab title="Response(completed)" %}
#### Status

200 OK

#### Body

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
#### Rest console

```http
DELETE /fhir/$export-status/<id>
```
{% endtab %}

{% tab title="Response" %}
#### Status

202 Accepted
{% endtab %}
{% endtabs %}

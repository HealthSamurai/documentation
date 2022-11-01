---
description: >-
  Load data from files in an existing Amazon Simple Storage Service (Amazon S3)
  with maximum performance.
---

# Bulk import from an S3 bucket

{% hint style="info" %}
If you want to import S3 objects with a presigned url, refer to [aidbox.bulk data import](aidbox.bulk-data-import.md).
{% endhint %}

### `aidbox.bulk/load-from-bucket`

It allows loading data from a bunch of `.ndjson.gz` files on an AWS bucket directly to the Aidbox database with maximum performance.

{% hint style="warning" %}
**Be careful** You should run _only one_ replica of Aidbox to use `aidbox.bulk/load-from-bucket` operation.
{% endhint %}

### Files content and naming requirement

1. The file must consist of Resources of the same type.
2. The file name should start with a name of the Resource type, then some postfix is possible, and extension `.ndjson` is required. Files can be placed in subdirectories of any level. Files with the wrong path structure will be **ignored**.
3. Every resource in `.ndjson` files MUST contain id property.

#### Resource requirements for `aidbox.bulk/load-from-bucket`:

| Operation                      | id       | resourceType |
| ------------------------------ | -------- | ------------ |
| `aidbox.bulk/load-from-bucket` | Required | Not required |

#### Valid file structure example:

```
fhir/1/Patient.ndjson.gz
fhir/1/patient-01.ndjson.gz
Observation.ndjson.gz
```

#### Invalid file structure example:

```
import.ndjson
01-patient.ndjson.gz
fhir/Patient
```

### Parameters

{% tabs %}
{% tab title="Parameters" %}
Object with the following structure:

* `bucket` <mark style="color:red;">\*</mark> defines your bucket connection string in format`s3://<bucket-name>`
* `thread-num` defines how many threads will process the import. The **default** is 4.
* `account` credential:
  * `access-key-id` <mark style="color:red;">\*</mark> AWS key ID
  * `secret-access-key` <mark style="color:red;">\*</mark> AWS secret key
  * `region` <mark style="color:red;">\*</mark> AWS Bucket region
* `disable-idx?` the **default** is `false`. Allows to drop all indexes for resources, which data are going to be loaded. Indexes will be restored at the end of successful import. All information about dropped indexes is stored at `DisabledIndex` resources.
* `drop-primary-key?` the **default** is `false`. The same as the previous parameter, but drops primary key constraint for resources tables. This parameter disables all checks for duplicates for imported resources.
* `upsert?` the **default** is `false`. If `upsert?` is `false`, import for files with `id` uniqueness constraint violation will fail with an error, if `true` - records in the database will be overridden with records from import. Even when `upsert?` is `true`, it's still not allowed to have more than one record with the same id in one import file. Setting this option to true will cause a decrease in performance.
* `scheduler` possible **values**: `optimal` , `by-last-modified`, the **default** is `optimal` . Establishes the order in which the files are processed. The `optimal` value provides the best performance. `by-last-modified` should be used with `thread-num = 1` to guarantee a stable order of file processing.
* `prefixes` array of prefixes to specify which files should be processed. Example: with value `["fhir/1/", "fhir/2/Patient"]` only files from directory `"fhir/1"` and `Patient` files from directory `"fhir/2"` will be processed.
* `connect-timeout` the **default** is `0`. Specifies the number of milliseconds after which the file is considered as failed if connection to the resource could not be established. (e.g. in case of network issues). Zero is interpreted as an infinite timeout.
* `read-timeout` the **default** is `0`. Specifies the number of milliseconds after which the file is considered as failed if there is no data available to read (e.g. in case of network issues). Zero is interpreted as an infinite timeout.
{% endtab %}

{% tab title="Result" %}
Returns the string "Upload started"
{% endtab %}

{% tab title="Error" %}
Returns error message
{% endtab %}
{% endtabs %}

#### Example

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: aidbox.bulk/load-from-bucket
params:
  bucket: s3://your-bucket-id
  thread-num: 4
  account:
    access-key-id: your-key-id
    secret-access-key: your-secret-access-key
    region: us-east-1
```
{% endtab %}

{% tab title="Response" %}
{% code title="Status: 200" %}
```
result:
  message: Upload from bucket <s3://your-bucket-id> started. 6 new files added.
  progress:
    total: 6
  new-files-count: 6
```
{% endcode %}
{% endtab %}
{% endtabs %}

#### Loader File

For each file being imported via `load-from-bucket` method, Aidbox creates `LoaderFile` resource. To find out how many resources were imported from a file, check the `loaded` field.

#### Loader File Example

```json
{
  "end": "2022-04-11T14:50:27.893Z",
  "file": "/tmp/patient.ndjson.gz",
  "size": 100,
  "type": "Patient",
  "bucket": "local",
  "loaded": 20,
  "status": "done"
}
```

#### How to reload a file one more time

On launch `aidbox.bulk/load-from-bucket` checks if files from the bucket were planned to import and decides what to do:

* If `ndjson.gz` file has it's related `LoaderFile` resource, the loader skips this file from import
* If there is no related `LoaderFile` resource, Aidbox puts this file to the queue creating a `LoaderFile` resource

In order to import a file one more time you should delete related `LoaderFile` resource and relaunch `aidbox.bulk/load-from-bucket`.

Files are processed completely. The loader doesn't support partial re-import.

### `aidbox.bulk/load-from-bucket-status`

Returns status and progress of import for specified bucket. Possible states are: `in-progress`, `completed`, `interrupted`.

{% hint style="info" %}
State `interrupted` means that aidbox was restarted during the loading process. If you run `aidbox.bulk/load-from-bucket` operation again on the same bucket, it will be continued.
{% endhint %}

#### Example

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: aidbox.bulk/load-from-bucket-status
params:
  bucket: s3://your-bucket-id
```
{% endtab %}

{% tab title="Response" %}
{% code title="Status: 200" %}
```
result:
  state: in-progress
  progress:
    total: 6
    pending: 2
    done: 4
```
{% endcode %}
{% endtab %}
{% endtabs %}

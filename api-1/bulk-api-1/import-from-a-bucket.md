# Import from a bucket

### `aidbox.bulk/load-from-bucket`

It allows loading data from an AWS bucket directly to the Aidbox database with maximum performance.

{% tabs %}
{% tab title="Parameters" %}
Object with the following structure:

* `bucket` defines your bucket connection string
* `thread-num` defines how many threads will process the import. The default value is 4.
* `account` object
  * `access-key-id` AWS key ID
  * `secret-access-key` AWS secret key
  * `region` AWS Bucket region
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
  message: "Upload started"
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

* If `ndjson` file has it's related `LoaderFile` resource, the loader skips this file from import
* If there is no related `LoaderFile` resource, Aidbox puts this file to the queue creating a `LoaderFile` resource

In order to import a file one more time you should delete related `LoaderFile` resource and relaunch `aidbox.bulk/load-from-bucket`.

Files are processed completely. The loader doesn't support partial re-import.

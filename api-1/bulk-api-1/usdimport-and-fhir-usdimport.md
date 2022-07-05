# $import & /fhir/$import

`$import` is an implementation of the upcoming FHIR Bulk Import API. This is an async Operation, which returns url to monitor progress.&#x20;

{% hint style="warning" %}
Keep in mind that $import does not validate inserted resources for the sake of performance. Pay attention to the structure of data you insert and use the correct URL for your data format, i.e.: use /fhir prefix for FHIR data.
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
inputFormat: application/fhir+ndjson
contentEncoding: gzip
mode: bulk
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
content-location: /BulkImportStatus/synthea
```
{% endtab %}
{% endtabs %}

Option `contentEncoding` supports `gzip` or `plain` (non-gzipped .ndjson files).

You can monitor progress using:

```yaml
GET /BulkImportStatus/[id]
```

### update: true

For performance reasons `$import` does raw upsert into resource table without history update. If you want to store the previous version of resources in history, please set `update = true`

With this flag Aidbox will update history for updated resources.

```yaml
POST /fhir/$import
Accept: text/yaml
Content-Type: text/yaml

id: synthea
update: true
inputFormat: application/fhir+ndjson
contentEncoding: gzip
mode: bulk
inputs:
- resourceType: Encounter
  url: https://storage.googleapis.com/aidbox-public/synthea/100/Encounter.ndjson.gz
- resourceType: Organization
  url: https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
- resourceType: Patient
  url: https://storage.googleapis.com/aidbox-public/synthea/100/Patient.ndjson.gz
```

##

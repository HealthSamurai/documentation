# $load & /fhir/$load

You can efficiently load data into Aidbox  in _ndjson_ _gz_ format from external web service or bucket. There are two versions of $load - `/$load` and `/[resourceType]/$load`.  First can load multiple resource types from one ndjson file, second is more efficient, but loads only for a specific resource type. Both operations accept body with **source** element, which should be publicly available url. If you want to secure your import use Signed URLs by Amazon S3 or Google Storage.&#x20;

There are two versions of this operation - `/fhir/$load` accepts data in FHIR format,  `/$load` works with [Aidbox format](../../modules-1/fhir-resources/aidbox-and-fhir-formats.md).

#### Resource requirements for all load operations:

| Operation                    | id       | resourceType |
| ---------------------------- | -------- | ------------ |
| `/$load`                     | Required | Required     |
| `/fhir/$load`                | Required | Required     |
| `/[resourceType]/$load`      | Required | Not required |
| `/fhir/[resourceType]/$load` | Required | Not required |

{% hint style="warning" %}
Keep in mind that $load **does not validate** inserted resources for the sake of performance. Pay attention to the structure of data you insert and use the correct URL for your data format, i.e.: use /fhir prefix for FHIR data.&#x20;

When loading resources with references, remember that '`<resourceType>/<id>'` is FHIR format and should be used in `/fhir/$load` operation, as well as `{"resourceType": "<resourceType>","id": "<id>"}` should be used in `/$load endpoint.`
{% endhint %}

{% hint style="info" %}
Please consider using [Asynchronous validation API](../../profiling-and-validation/validation-api.md#asynchronous-batch-validation-draft) to validate data after $load
{% endhint %}

&#x20;Load 100 synthea Patients to Aidbox (see [tutorial](synthea-by-bulk-api.md)):

```yaml
POST /fhir/Patient/$load
Content-Type: text/yaml

source: 'https://storage.googleapis.com/aidbox-public/synthea/100/Patient.ndjson.gz'

#resp
{total: 124}
```

### update: true

By default for performance reasons `$load` does raw upsert into resource table without updating history. If you want to store the previous version of resources in history, you have to set `update = true,` with this flag Aidbox will update history for updated resources.

```yaml
POST /fhir/Patient/$load
Content-Type: text/yaml

source: <new-version-of-data>
update: true
```

### strip-nulls: true

By default for performance reasons `$load` does raw upsert into the resource table without cleaning up `null` values from imported resources. To make import behave in the same way as the `Create` operation, use options `strip-nulls: true`

```yaml
POST /fhir/Patient/$load
Content-Type: text/yaml

source: <new-version-of-data>
strip-nulls: true
```

### merge: `object literal`

It's possible to merge some data into every loaded resource using `merge` option.  A **shallow** merge will be used.

```yaml
POST /fhir/Patient/$load
Content-Type: text/yaml

source: <new-version-of-data>
merge:
  active: true
```


### $load multiple resource types

Or load the whole synthea package:

```yaml
POST /fhir/$load
Content-Type: text/yaml

source: 'https://storage.googleapis.com/aidbox-public/synthea/100/all.ndjson.gz'

# resp

{CarePlan: 356, Observation: 20382, MedicationAdministration: 150, .... }
```

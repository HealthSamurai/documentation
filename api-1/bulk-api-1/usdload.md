# $load

You can efficiently load data into Aidbox  in _ndjson_ _gz_ format from external web service or bucket. There are two versions of $load - `/$load` and `/[resourceType]/$load`.  First can load multiple resource types from one ndjson file, second is more efficient, but loads only for a specific resource type. Both operations accept body with **source** element, which should be publicly available url. If you want to secure your import use Signed URLs by Amazon S3 or Google Storage.&#x20;

There are two versions of this operation - `/fhir/$load` accepts data in FHIR format,  `/$load` works with Aidbox format.

{% hint style="warning" %}
Keep in mind that $load does not validate inserted resources for the sake of performance. Pay attention to the structure of data you insert and use the correct URL for your data format, i.e.: use /fhir prefix for FHIR data.
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

### $load multiple resource types

Or load the whole synthea package:

```yaml
POST /fhir/$load
Content-Type: text/yaml

source: 'https://storage.googleapis.com/aidbox-public/synthea/100/all.ndjson.gz'

# resp

{CarePlan: 356, Observation: 20382, MedicationAdministration: 150, .... }
```

# $load

You can efficiently load data into Aidbox  in _ndjson_ _gz_ format from external web service or bucket. There are two versions of $load - `/$load` and `/[resourceType]/$load`.  First can load multiple resource types from one ndjson file, second is more efficient, but loads only for a specific resource type. Both operations accept body with **source** element, which should be publicly available url. If you want to secure your import use Signed URLs by Amazon S3 or Google Storage. 

There are two versions of this operation - `/fhir/$load` accepts data in FHIR format,  `/$load` works with Aidbox format.

{% hint style="danger" %}
Keep in mind that $load does not validate inserted resources for the sake of performance. Be mindful of the data you insert and use correct URL for your data format.
{% endhint %}

Here how you can load 100 synthea Patients \(see [tutorial](synthea-by-bulk-api.md)\):

```yaml
POST /fhir/Patient/$load

source: 'https://storage.googleapis.com/aidbox-public/synthea/100/Patient.ndjson.gz'

#resp
{total: 124}
```

Or load the whole synthea package:

```yaml
POST /fhir/$load

source: 'https://storage.googleapis.com/aidbox-public/synthea/100/all.ndjson.gz'

# resp

{CarePlan: 356, Observation: 20382, MedicationAdministration: 150, .... }
```


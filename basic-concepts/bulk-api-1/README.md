---
description: Bulk export & import operations
---

# Bulk API

## $dump 

You can dump all resources of specific type with $dump operation - `GET [resource-type]/$dump` - Aidbox will respond with [Chunked Transfer Encoding](https://en.wikipedia.org/wiki/Chunked_transfer_encoding) [ndjson](http://ndjson.org/) stream. This is memory efficient operation - Aidbox just stream database cursor to socket. If your HTTP Client support processing of Chunked Encoding  you can process resources in stream one by one without waiting for end of response.

```yaml
GET /Patient/$dump

#response

HTTP/1.1 200 OK
Content-Type: application/ndjson
Transfer-Encoding: chunked

{"resourceType": "Patient", "id": .............}
{"resourceType": "Patient", "id": .............}
{"resourceType": "Patient", "id": .............}
{"resourceType": "Patient", "id": .............}
.........
```

Here is example how you can dump all patients from your box \(assuming you have a client with access policy\):

```bash
curl -u client:secret -H 'content-type:application/json' \
  https://<box-url>/Patient/\$dump | gzip > patients.ndjson.gz
```

## $dump-sql

Take sql query and responds with Chunked Encoded stream in CSV format. Useful to export data for analytics.

```yaml
POST /$dump-sql

query:  select id, resource#>>'{name,0,family}'
format: csv # ndjson; sql; elastic-bulk?

HTTP/1.1 200 OK
Content-Type: application/CSV
Transfer-Encoding: chunked

pt-1    Doe    John
pt-2    Smith    Mike
................
```

## $load 

You can efficiently load data into Aidbox  in _ndjson_ _gz_ format from external web service or bucket. There are two versions of $load - `/$load` and `/[resourceType]/$load`.  First can load multiple resource types from one ndjson file, second is more efficient, but loads only for a specific resource type. Both operations accept body with **source** element, which should be publicly available url. If you want to secure your import use Signed URLs by Amazon S3 or Google Storage. 

There are two versions of this operation - `/fhir/$load` accepts data in FHIR format,  `/$load` works with Aidbox format.

{% hint style="danger" %}
Keep in mind that $load does not validate inserted resources for the sake of performance. Be mindful about data you insert and use correct URL for your data format.
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

## $import & /fhir/$import

$import is implementation of upcoming FHIR Bulk Import API. This is async Operation, which returns url to monitor progress. Here is self descriptive example:

```yaml
POST /fhir/$import

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

You post import body with id and can monitor progress of import using:

```yaml
GET /BulkImportStatus/[id]
```

## Read more

* Load Synthea with Bulk API [tutorial](synthea-by-bulk-api.md)
* $dump-sql for analytics [tutorial](usddump-sql-tutorial.md)


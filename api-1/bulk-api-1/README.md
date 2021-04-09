---
description: Bulk export & import operations
---

# Bulk API

## $dump 

You can dump all resources of a specific type with $dump operation - `GET [resource-type]/$dump` - Aidbox will respond with [Chunked Transfer Encoding](https://en.wikipedia.org/wiki/Chunked_transfer_encoding) [ndjson](http://ndjson.org/) stream. This is a memory efficient operation - Aidbox just streams database cursor to socket. If your HTTP Client supports processing of Chunked Encoding, you can process resources in stream one by one without waiting for end of the response.

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

Here is an example of how you can dump all patients from your box \(assuming you have a client with access policy\):

```bash
curl -u client:secret -H 'content-type:application/json' \
  https://<box-url>/Patient/\$dump | gzip > patients.ndjson.gz
```

{% api-method method="get" host="\[base\]/:resourceType/$dump" path="" %}
{% api-method-summary %}
Dump data
{% endapi-method-summary %}

{% api-method-description %}
Dumps data as NDJSON, optionally in FHIR format or GZIPped
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-path-parameters %}
{% api-method-parameter name="resourceType" type="string" required=true %}
name of the resource type to be exported
{% endapi-method-parameter %}
{% endapi-method-path-parameters %}

{% api-method-query-parameters %}
{% api-method-parameter name="\_since" type="string" required=false %}
Date in ISO format; if present, exported data will contain only the resources created after the date.
{% endapi-method-parameter %}

{% api-method-parameter name="fhir" type="boolean" required=false %}
Convert data to the FHIR format. If disabled, the data is exported in the Aidbox format.
{% endapi-method-parameter %}

{% api-method-parameter name="gzip" type="boolean" required=false %}
GZIP the result. If enabled, HTTP headers for gzip encoding are also set.
{% endapi-method-parameter %}
{% endapi-method-query-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}
NDJSON representing the resource  
  
Example request:  
  
`GET /Appointment/$dump?fhir=true`
{% endapi-method-response-example-description %}

```
{"id":"ap-1","meta":{"versionId":15,"lastUpdated":"2021-04-02T16:03:31.057462+03:00","extension":[{"url":"ex:createdAt","valueInstant":"2021-04-02T16:03:09.419823+03:00"}]},"start":"2021-02-02T16:02:50.997+03:00","status":"fullfilled","participant":[{"status":"accepted"}],"resourceType":"Appointment"}
{"id":"ap-2","meta":{"versionId":26,"lastUpdated":"2021-04-02T16:04:24.695862+03:00","extension":[{"url":"ex:createdAt","valueInstant":"2021-04-02T16:03:38.168497+03:00"}]},"start":"2020-02-02T16:02:50.997+03:00","status":"fullfilled","participant":[{"status":"accepted"}],"resourceType":"Appointment"}
{"id":"ap-3","meta":{"versionId":40,"lastUpdated":"2021-04-02T16:08:41.198887+03:00","extension":[{"url":"ex:createdAt","valueInstant":"2021-04-02T16:03:55.869199+03:00"}]},"start":"2021-04-02T16:02:50.996+03:00","status":"fullfilled","participant":[{"status":"accepted"}],"resourceType":"Appointment"}
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

## $dump-sql

Takes the sql query and responds with the Chunked Encoded stream in CSV format. Useful to export data for analytics.

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


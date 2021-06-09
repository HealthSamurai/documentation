---
description: Bulk export & import operations
---

# Bulk API

## Access policies

Let's create a new client and access policy which allows the client to use only bulk API

```yaml
PUT /

- resourceType: Client
  id: bulk-client
  secret: secret
  grant_types: ['basic']

- resourceType: AccessPolicy
  id: bulk-client-dump
  engine: matcho
  matcho:
    $one-of:
    - uri: /$dump-sql
      request-method: post
    - uri: "#/.*/\\$dump$"
      request-method: get
    - uri: "#/.*/\\$dump-csv$"
      request-method: get
    - uri:
        $one-of:
          - /$import
          - /fhir/$import
      request-method: post
    - uri:
        $one-of:
          - /$load
          - /fhir/$load
          - "#/.*/\\$load$"
  link:
  - {id: 'bulk-client', resourceType: 'Client'}
```

## Create sample data

We will use this query to prepare sample data for the examples below. It will create:

* FHIR citizenship extension
* 3 patients one of which uses the citizenship extension
* 3 appointments with references to patients

```yaml
POST /

resourceType: bundle
type: transaction
entry:

- request:
    method: PUT
    url: "/Attribute/Patient.citizenship"
  resource:
    resourceType: Attribute
    description: "The patient's legal status as citizen of a country."
    resource:
      id: Patient
      resourceType: Entity
    path: [citizenship]
    id: Patient.citizenship
    isCollection: true
    extensionUrl: "http://hl7.org/fhir/StructureDefinition/patient-citizenship"
- request:
    method: PUT
    url: "/Attribute/Patient.citizenship.code"
  resource:
    resourceType: Attribute
    description: "Nation code representing the citizenship of patient."
    resource:
      id: Patient
      resourceType: Entity
    path: [citizenship, code]
    id: Patient.citizenship.code
    type: {id: CodeableConcept, resourceType: Entity}
    extensionUrl: "code"

- request:
    method: POST
    url: "/Patient"
  resource:
    id: pt-1
    name:
      - given: ["Alice"]
- request:
    method: POST
    url: "/Patient"
  resource:
    id: pt-2
    name:
      - given: ["Bob"]
- request:
    method: POST
    url: "/Patient"
  resource:
    id: pt-3
    name:
      - given: ["Charles"]
    citizenship:
      - code: {text: "ru"}

- request:
    method: POST
    url: "/Appointment"
  resource:
    id: ap-1
    status: fulfilled
    participant:
      - status: accepted
        actor: 
          resourceType: Patient
          id: pt-1
- request:
    method: POST
    url: "/Appointment"
  resource:
    id: ap-2
    status: booked
    participant:
      - status: accepted
        actor: 
          resourceType: Patient
          id: pt-1
- request:
    method: POST
    url: "/Appointment"
  resource:
    id: ap-3
    status: fulfilled
    participant:
      - status: accepted
        actor: 
          resourceType: Patient
          id: pt-2
```

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

{% api-method method="get" host="\[base\]/" path=":resourceType:/$dump" %}
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
{"id": "pt-1", "meta": {"createdAt": "2021-06-09T11:07:23.022196Z", "versionId": 388, "lastUpdated": "2021-06-09T11:07:23.022196Z"}, "name": [{"given": ["alice"]}], "resourceType": "Patient"}
{"id": "pt-2", "meta": {"createdAt": "2021-06-09T11:07:23.022196Z", "versionId": 390, "lastUpdated": "2021-06-09T11:07:23.022196Z"}, "name": [{"given": ["bob"]}], "resourceType": "Patient"}
{"id": "pt-3", "meta": {"createdAt": "2021-06-09T13:33:02.903654Z", "versionId": 433, "lastUpdated": "2021-06-09T13:33:41.192117Z"}, "name": [{"given": ["Charles"]}], "citizenship": [{"code": {"text": "ru"}}], "resourceType": "Patient"}
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

```yaml
# if fhir enabled
{"id":"pt-1","meta":{"versionId":388,"lastUpdated":"2021-06-09T11:07:23.022196Z","extension":[{"url":"ex:createdAt","valueInstant":"2021-06-09T11:07:23.022196Z"}]},"name":[{"given":["alice"]}],"resourceType":"Patient"}
{"id":"pt-2","meta":{"versionId":390,"lastUpdated":"2021-06-09T11:07:23.022196Z","extension":[{"url":"ex:createdAt","valueInstant":"2021-06-09T11:07:23.022196Z"}]},"name":[{"given":["bob"]}],"resourceType":"Patient"}
{"id":"pt-3","meta":{"versionId":433,"lastUpdated":"2021-06-09T13:33:41.192117Z","extension":[{"url":"ex:createdAt","valueInstant":"2021-06-09T13:33:02.903654Z"}]},"name":[{"given":["Charles"]}],"resourceType":"Patient","extension":[{"url":"http://hl7.org/fhir/StructureDefinition/patient-citizenship","extension":[{"url":"code","valueCodeableConcept":{"text":"ru"}}]}]}
```

## $dump-sql: Dump results of the sql query

Takes the sql query and responds with the Chunked Encoded stream in CSV format or in NDJSON format. Useful to export data for analytics.

{% api-method method="post" host="\[base\]/" path="$dump-sql" %}
{% api-method-summary %}
Dump the result of the SQL query
{% endapi-method-summary %}

{% api-method-description %}

{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-headers %}
{% api-method-parameter name="content-type" type="string" required=true %}
Content-type of the query body
{% endapi-method-parameter %}
{% endapi-method-headers %}

{% api-method-query-parameters %}
{% api-method-parameter name="\_format" type="string" required=false %}
- json/ndjson: return output as ndjosn  
- otherwise: return output as TSV
{% endapi-method-parameter %}
{% endapi-method-query-parameters %}

{% api-method-body-parameters %}
{% api-method-parameter name="query" type="string" required=true %}
SQL query to execute
{% endapi-method-parameter %}
{% endapi-method-body-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}
 The data returned by SQL query in either NDJSON format or TSV format
{% endapi-method-response-example-description %}

```

```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

### Example

{% tabs %}
{% tab title="Request" %}
```yaml
POST /$dump-sql
# Headers
Content-Type: application/yaml
# In the example we use basic authorization for our newly created client
# It's pair of id:password will be bulk-client:secret
# Which in base64 encoding is YnVsay1jbGllbnQ6c2VjcmV0
Authorization: Basic YnVsay1jbGllbnQ6c2VjcmV0

# Body
query: select id, resource#>>'{name,0,given,0}' from patient
```
{% endtab %}

{% tab title="Response" %}
```yaml
# Headers
Content-Type: text/tab-separated-values

# Body
pt-1	Alice
pt-2	Bob
pt-3	Charles
```
{% endtab %}
{% endtabs %}

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


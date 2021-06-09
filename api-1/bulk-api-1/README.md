---
description: Bulk export & import operations
---

# Bulk API

## Access policies

Let's create a new client and an access policy that allows the client to use only bulk API

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

## Generate sample data for Bulk API

You can use sample data for executing Bulk API endpoints from this documentation section.

Sample data contains:

* FHIR citizenship extension
* 3 patients one of which uses the citizenship extension
* 3 appointments with references to patients

{% hint style="success" %}
You can quickly copy sample requests using the icon in the top right corner of code blocks and execute them in the Aidbox REST console. 
{% endhint %}

{% tabs %}
{% tab title="Create Sample data" %}
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
{% endtab %}
{% endtabs %}

## $dump 

You can dump all resources of a specified type. Aidbox will respond with [Chunked Transfer Encoding](https://en.wikipedia.org/wiki/Chunked_transfer_encoding) [ndjson](http://ndjson.org/) stream, optionally you can get the output in FHIR format or GZIPped.

This is a memory-efficient operation. Aidbox just streams the database cursor to a socket. If your HTTP Client supports processing of Chunked Encoding, you can process resources in stream one by one without waiting for the end of the response.

{% tabs %}
{% tab title="Request format" %}
```typescript
GET [base]/:resourceType:/$dump
```
{% endtab %}

{% tab title="Request" %}
```typescript
GET /Patient/$dump
```
{% endtab %}

{% tab title="Response" %}
```yaml
HTTP/1.1 200 OK
Content-Type: application/ndjson
Transfer-Encoding: chunked

{"resourceType": "Patient", "id": .............}
{"resourceType": "Patient", "id": .............}
{"resourceType": "Patient", "id": .............}
{"resourceType": "Patient", "id": .............}
.........
```
{% endtab %}
{% endtabs %}

| Parameter | Required? | Type | Description |
| :--- | :--- | :--- | :--- |
| Path Parameters |  |  |  |
| **resourceType** | true | String | name of the resource type to be exported |
| Query Parameters |  |  |  |
| **\_since** | false | String | Date in ISO format; if present, exported data will contain only the resources created after the date. |
| **fhir** | false | Boolean | Convert data to the FHIR format. If disabled, the data is exported in the Aidbox format. |
| **gzip** | optional | Boolean | GZIP the result. If enabled, HTTP headers for gzip encoding are also set. |

### Example

Here is an example of how you can dump all patients from your box \(assuming you have a client with an access policy\):

{% tabs %}
{% tab title="Request format" %}
```bash
curl -u client:secret -H 'content-type:application/json' \
  https://<box-url>/Patient/\$dump | gzip > patients.ndjson.gz
```
{% endtab %}

{% tab title="Request example" %}
```typescript
GET /Appointment/$dump?fhir=true
```
{% endtab %}

{% tab title="Response" %}
```yaml
# if fhir enabled
{"id":"pt-1","meta":{"versionId":388,"lastUpdated":"2021-06-09T11:07:23.022196Z","extension":[{"url":"ex:createdAt","valueInstant":"2021-06-09T11:07:23.022196Z"}]},"name":[{"given":["alice"]}],"resourceType":"Patient"}
{"id":"pt-2","meta":{"versionId":390,"lastUpdated":"2021-06-09T11:07:23.022196Z","extension":[{"url":"ex:createdAt","valueInstant":"2021-06-09T11:07:23.022196Z"}]},"name":[{"given":["bob"]}],"resourceType":"Patient"}
{"id":"pt-3","meta":{"versionId":433,"lastUpdated":"2021-06-09T13:33:41.192117Z","extension":[{"url":"ex:createdAt","valueInstant":"2021-06-09T13:33:02.903654Z"}]},"name":[{"given":["Charles"]}],"resourceType":"Patient","extension":[{"url":"http://hl7.org/fhir/StructureDefinition/patient-citizenship","extension":[{"url":"code","valueCodeableConcept":{"text":"ru"}}]}]}
```
{% endtab %}
{% endtabs %}

## $dump-sql: Dump results of the sql query

Takes the sql query and responds with the Chunked Encoded stream in CSV format or in NDJSON format. Useful to export data for analytics.

```typescript
POST [base]/$dump-sql
```

<table>
  <thead>
    <tr>
      <th style="text-align:left">Parameter</th>
      <th style="text-align:left">Required?</th>
      <th style="text-align:left">Type</th>
      <th style="text-align:left">Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="text-align:left">Headers</td>
      <td style="text-align:left"></td>
      <td style="text-align:left"></td>
      <td style="text-align:left"></td>
    </tr>
    <tr>
      <td style="text-align:left"><b>content-type</b>
      </td>
      <td style="text-align:left">true</td>
      <td style="text-align:left">String</td>
      <td style="text-align:left">Content-type of the query body</td>
    </tr>
    <tr>
      <td style="text-align:left">Query parameters</td>
      <td style="text-align:left"></td>
      <td style="text-align:left"></td>
      <td style="text-align:left"></td>
    </tr>
    <tr>
      <td style="text-align:left"><b>_format</b>
      </td>
      <td style="text-align:left">false</td>
      <td style="text-align:left">String</td>
      <td style="text-align:left">
        <p></p>
        <ul>
          <li>json/ndjson: return output as ndjosn</li>
          <li>otherwise: return output as TSV</li>
        </ul>
      </td>
    </tr>
    <tr>
      <td style="text-align:left">Body parameters</td>
      <td style="text-align:left"></td>
      <td style="text-align:left"></td>
      <td style="text-align:left"></td>
    </tr>
    <tr>
      <td style="text-align:left"><b>query</b>
      </td>
      <td style="text-align:left">true</td>
      <td style="text-align:left">String</td>
      <td style="text-align:left">Sql query to execute</td>
    </tr>
  </tbody>
</table>

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


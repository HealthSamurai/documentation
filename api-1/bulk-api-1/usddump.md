# $dump

You can dump all resources of a specified type. Aidbox will respond with [Chunked Transfer Encoding](https://en.wikipedia.org/wiki/Chunked_transfer_encoding) [ndjson](http://ndjson.org/) stream, optionally you can get the output in FHIR format or GZIPped.

This is a memory-efficient operation. Aidbox just streams the database cursor to a socket. If your HTTP Client supports processing of Chunked Encoding, you can process resources in stream one by one without waiting for the end of the response.

{% tabs %}
{% tab title="Request format" %}
```text
GET [base]/<resourceType>/$dump
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

### Examples

Here is an example of how you can dump all patients from your box \(assuming you have a client with an access policy\):

{% tabs %}
{% tab title="Request" %}
**Curl**

```bash
curl -u bulk-client:secret $AIDBOX_BASE_URL/Patient/\$dump
```

**REST Console**

```typescript
GET /Patient/$dump
```
{% endtab %}

{% tab title="Response" %}
#### Status

200 OK

#### Headers

| Header | Value |
| :--- | :--- |
| Content-Type | application/ndjson |
| Transfer-Encoding | chunked |

#### Body

```text
{"id": "pt-1", "meta": {"createdAt": "2021-06-10T08:26:10.707454Z", "versionId": 481, "lastUpdated": "2021-06-10T08:26:10.707454Z"}, "name": [{"given": ["Alice"]}], "resourceType": "Patient"}
{"id": "pt-2", "meta": {"createdAt": "2021-06-10T08:26:10.707454Z", "versionId": 482, "lastUpdated": "2021-06-10T08:26:10.707454Z"}, "name": [{"given": ["Bob"]}], "resourceType": "Patient"}
{"id": "pt-3", "meta": {"createdAt": "2021-06-10T08:26:10.707454Z", "versionId": 483, "lastUpdated": "2021-06-10T08:26:10.707454Z"}, "name": [{"given": ["Charles"]}], "citizenship": [{"code": {"text": "ru"}}], "resourceType": "Patient"}
```

#### Body \(manually formatted\)

```yaml
{
  "id": "pt-1",
  "meta": {
    "createdAt": "2021-06-10T08:26:10.707454Z",
    "versionId": 481,
    "lastUpdated": "2021-06-10T08:26:10.707454Z"
  },
  "name": [
    {
      "given": [
        "Alice"
      ]
    }
  ],
  "resourceType": "Patient"
}
{
  "id": "pt-2",
  "meta": {
    "createdAt": "2021-06-10T08:26:10.707454Z",
    "versionId": 482,
    "lastUpdated": "2021-06-10T08:26:10.707454Z"
  },
  "name": [
    {
      "given": [
        "Bob"
      ]
    }
  ],
  "resourceType": "Patient"
}
{
  "id": "pt-3",
  "meta": {
    "createdAt": "2021-06-10T08:26:10.707454Z",
    "versionId": 483,
    "lastUpdated": "2021-06-10T08:26:10.707454Z"
  },
  "name": [
    {
      "given": [
        "Charles"
      ]
    }
  ],
  "citizenship": [
    {
      "code": {
        "text": "ru"
      }
    }
  ],
  "resourceType": "Patient"
}
```
{% endtab %}
{% endtabs %}

Dump appointments

{% tabs %}
{% tab title="Request" %}
#### REST Console

```text
GET /Appointment
```

#### Curl

```bash
curl -u bulk-client:secret $AIDBOX_BASE_URL/Appointment/\$dump
```
{% endtab %}

{% tab title="Response" %}
#### Status

200 OK

#### Headers

| Header | Value |
| :--- | :--- |
| Content-Type | application/ndjson |
| Transfer-Encoding | Chunked |

#### Body

```yaml
{"id": "ap-1", "meta": {"createdAt": "2021-06-10T08:26:10.707454Z", "versionId": 484, "lastUpdated": "2021-06-10T08:26:10.707454Z"}, "status": "fulfilled", "participant": [{"actor": {"id": "pt-1", "resourceType": "Patient"}, "status": "accepted"}], "resourceType": "Appointment"}
{"id": "ap-2", "meta": {"createdAt": "2021-06-10T08:26:10.707454Z", "versionId": 485, "lastUpdated": "2021-06-10T08:26:10.707454Z"}, "status": "booked", "participant": [{"actor": {"id": "pt-1", "resourceType": "Patient"}, "status": "accepted"}], "resourceType": "Appointment"}
{"id": "ap-3", "meta": {"createdAt": "2021-06-10T08:26:10.707454Z", "versionId": 486, "lastUpdated": "2021-06-10T08:26:10.707454Z"}, "status": "fulfilled", "participant": [{"actor": {"id": "pt-2", "resourceType": "Patient"}, "status": "accepted"}], "resourceType": "Appointment"}
```

#### Body \(manually formatted\)

```yaml
{
  "id": "ap-1",
  "meta": {
    "createdAt": "2021-06-10T08:26:10.707454Z",
    "versionId": 484,
    "lastUpdated": "2021-06-10T08:26:10.707454Z"
  },
  "status": "fulfilled",
  "participant": [
    {
      "actor": {
        "id": "pt-1",
        "resourceType": "Patient"
      },
      "status": "accepted"
    }
  ],
  "resourceType": "Appointment"
}
{
  "id": "ap-2",
  "meta": {
    "createdAt": "2021-06-10T08:26:10.707454Z",
    "versionId": 485,
    "lastUpdated": "2021-06-10T08:26:10.707454Z"
  },
  "status": "booked",
  "participant": [
    {
      "actor": {
        "id": "pt-1",
        "resourceType": "Patient"
      },
      "status": "accepted"
    }
  ],
  "resourceType": "Appointment"
}
{
  "id": "ap-3",
  "meta": {
    "createdAt": "2021-06-10T08:26:10.707454Z",
    "versionId": 486,
    "lastUpdated": "2021-06-10T08:26:10.707454Z"
  },
  "status": "fulfilled",
  "participant": [
    {
      "actor": {
        "id": "pt-2",
        "resourceType": "Patient"
      },
      "status": "accepted"
    }
  ],
  "resourceType": "Appointment"
}

```
{% endtab %}
{% endtabs %}

Dump patients in FHIR format

{% tabs %}
{% tab title="Request" %}
#### REST Console

```text
GET /Patient?fhir=true
```

#### Curl

```bash
curl -u bulk-client:secret $AIDBOX_BASE_URL/Patient/\$dump?fhir=true
```
{% endtab %}

{% tab title="Response" %}
#### Status

200 OK

#### Headers

| Header | Value |
| :--- | :--- |
| Content-Type | application/ndjson |
| Transfer-Encoding | chunked |

#### Body

```text
{"id":"pt-1","meta":{"versionId":481,"lastUpdated":"2021-06-10T08:26:10.707454Z","extension":[{"url":"ex:createdAt","valueInstant":"2021-06-10T08:26:10.707454Z"}]},"name":[{"given":["Alice"]}],"resourceType":"Patient"}
{"id":"pt-2","meta":{"versionId":482,"lastUpdated":"2021-06-10T08:26:10.707454Z","extension":[{"url":"ex:createdAt","valueInstant":"2021-06-10T08:26:10.707454Z"}]},"name":[{"given":["Bob"]}],"resourceType":"Patient"}
{"id":"pt-3","meta":{"versionId":483,"lastUpdated":"2021-06-10T08:26:10.707454Z","extension":[{"url":"ex:createdAt","valueInstant":"2021-06-10T08:26:10.707454Z"}]},"name":[{"given":["Charles"]}],"citizenship":[{"code":{"text":"ru"}}],"resourceType":"Patient"}
```

#### Body \(manually formatted\)

```yaml
{
  "id": "pt-1",
  "meta": {
    "versionId": 481,
    "lastUpdated": "2021-06-10T08:26:10.707454Z",
    "extension": [
      {
        "url": "ex:createdAt",
        "valueInstant": "2021-06-10T08:26:10.707454Z"
      }
    ]
  },
  "name": [
    {
      "given": [
        "Alice"
      ]
    }
  ],
  "resourceType": "Patient"
}
{
  "id": "pt-2",
  "meta": {
    "versionId": 482,
    "lastUpdated": "2021-06-10T08:26:10.707454Z",
    "extension": [
      {
        "url": "ex:createdAt",
        "valueInstant": "2021-06-10T08:26:10.707454Z"
      }
    ]
  },
  "name": [
    {
      "given": [
        "Bob"
      ]
    }
  ],
  "resourceType": "Patient"
}
{
  "id": "pt-3",
  "meta": {
    "versionId": 483,
    "lastUpdated": "2021-06-10T08:26:10.707454Z",
    "extension": [
      {
        "url": "ex:createdAt",
        "valueInstant": "2021-06-10T08:26:10.707454Z"
      }
    ]
  },
  "name": [
    {
      "given": [
        "Charles"
      ]
    }
  ],
  "citizenship": [
    {
      "code": {
        "text": "ru"
      }
    }
  ],
  "resourceType": "Patient"
}

```
{% endtab %}
{% endtabs %}








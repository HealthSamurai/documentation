# $dump

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
{% tab title="Request" %}
**Curl**

```bash
curl -u client:secret -H 'content-type:application/json' \
  https://<box-url>/Patient/\$dump | gzip > patients.ndjson.gz
```

**REST Console**

```typescript
GET /Appointment/$dump?fhir=true
```
{% endtab %}
{% endtabs %}


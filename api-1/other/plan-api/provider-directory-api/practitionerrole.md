---
description: >-
  Represents a role a practitioner performs at an organization. Contains
  information about Practitioner, Network, Location, Organization and
  HealthcareService resources associated with a practitioner.
---

# PractitionerRole

Get 100 practitioner roles (by default):

<pre><code><strong>GET [base]/fhir/PractitionerRole
</strong></code></pre>

Get practitioner role by resource `id`:

<pre><code><strong>GET [base]/fhir/PractitionerRole/[id]
</strong></code></pre>

The full list of FHIR search parameters for PractitionerRole is found [below](practitionerrole.md#parameters). Aidbox also offers a selection of query parameters to further refine search results. This includes pagination, full-text search, sorting etc. Refer to the [FHIR API Search docs](broken-reference) for information.

The search results are returned in [Bundles](../../../api/bundle.md) by default. Alternatively, you can specify a `_result` parameter with value `array` to receive resources in a JSON array.

### Example search scenario

1. Get practitioner role by specialty:

<pre><code><strong>GET [base]/fhir/PractitionerRole?specialty=2084P0802X
</strong></code></pre>

2. Find practitioner `id`:

```json
"practitioner": {
  "reference": "Practitioner/bc3d21a5-af67-4ff9-9f08-9758b71a7e0e"
  .......
}
```

3. Get practitioner by `id`:

<pre><code><strong>GET [base]/fhir/Practitioner/[id]
</strong></code></pre>

### Bulk API

The following bulk operations on PractitionerRole are available.

#### [`$dump`](../../../bulk-api-1/usddump.md)

Aidbox will respond with [Chunked Transfer Encoding](https://en.wikipedia.org/wiki/Chunked_transfer_encoding) [ndjson](http://ndjson.org/) stream, optionally you can get the output in FHIR format or GZIPped.

This is a memory-efficient operation. Aidbox just streams the database cursor to a socket. If your HTTP Client supports processing of Chunked Encoding, you can process resources in stream one by one without waiting for the end of the response. See full docs [here](../../../bulk-api-1/usddump.md).

{% tabs %}
{% tab title="Request" %}
```
GET [base]/PractitionerRole/$dump
```
{% endtab %}

{% tab title="Response" %}
```yaml
HTTP/1.1 200 OK
Content-Type: application/ndjson
Transfer-Encoding: chunked

{"resourceType": "PractitionerRole", "id": .............}
{"resourceType": "PractitionerRole", "id": .............}
{"resourceType": "PractitionerRole", "id": .............}
{"resourceType": "PractitionerRole", "id": .............}
.........
```
{% endtab %}
{% endtabs %}

#### [`$dump-csv`](../../../bulk-api-1/usddump-csv.md)

Dump PractitionerRole resources in CSV format.

This operation dumps resource data in Aidbox format as CSV ([RFC4180](https://datatracker.ietf.org/doc/html/rfc4180)). In this format, columns are paths to JSON values and Rows are values. It includes the header.

Neither the specific order of columns nor the order of rows is guaranteed. See full docs [here](../../../bulk-api-1/usddump-csv.md).

```
GET [base]/PractitionerRole/$dump-csv
```

### Parameters

| Parameter                          | Description                                                                                        |
| ---------------------------------- | -------------------------------------------------------------------------------------------------- |
| PractitionerRole.active            | true/false                                                                                         |
| PractitionerRole.date              | the period during which the practitioner is authorized to perform in these role(s)                 |
| PractitionerRole.email             | email                                                                                              |
| PractitionerRole.endpoint          | technical endpoints providing access to services operated for the practitioner with this role      |
| PractitionerRole.healthcareService | the list of healthcare services that this worker provides for this role's Organization/Location(s) |
| PractitionerRole.identifier        | identifier in a system                                                                             |
| PractitionerRole.location          | the location(s) at which this practitioner provides care                                           |
| PractitionerRole.network           | associated networks                                                                                |
| PractitionerRole.organization      | organization where the roles are available                                                         |
| PractitionerRole.phone             | phone                                                                                              |
| PractitionerRole.practitioner      | associated practitioner(s)                                                                         |
| PractitionerRole.role              | the code of a role                                                                                 |
| PractitionerRole.service           | the list of healthcare services that this worker provides for this role's Organization/Location(s) |
| PractitionerRole.specialty         | practitioner specialty                                                                             |
| PractitionerRole.telecom           | telecom                                                                                            |


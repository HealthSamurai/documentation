---
description: >-
  An organization is a formal or informal grouping of people or organizations
  with a common purpose, such as a company, institution, corporation, community
  group, or healthcare practice.
---

# Organization

Get 100 organizations (by default):

<pre><code><strong>GET [base]/fhir/Organization
</strong></code></pre>

Get organization by name:

<pre><code><strong>GET [base]/fhir/Organization?name=ACME
</strong></code></pre>

Get organizations by address:

<pre><code><strong>GET [base]/fhir/Organization?address=BRONX
</strong></code></pre>

Get organization by type:

<pre><code><strong>GET [base]/fhir/Organization?type=HS
</strong></code></pre>

Get organization by identifier:

<pre><code><strong>GET [base]/fhir/Organization?identifier=1023024882
</strong></code></pre>

Get organizations by address AND type:

<pre><code><strong>GET [base]/fhir/Organization?address=BRONX&#x26;type=HS
</strong></code></pre>

The full list of FHIR search parameters for Organization is found [below](organization.md#parameters). Aidbox also offers a selection of query parameters to further refine search results. This includes pagination, full-text search, sorting etc. Refer to the [FHIR API Search docs](../../fhir-api/search-1/) for information.

The search results are returned in [Bundles](../../fhir-api/bundle.md) by default. Alternatively, you can specify a `_result` parameter with value `array` to receive resources in a JSON array.

### Bulk API

The following bulk operations on Organization are available.

#### [`$dump`](../../bulk-api-1/usddump.md)

Aidbox will respond with [Chunked Transfer Encoding](https://en.wikipedia.org/wiki/Chunked\_transfer\_encoding) [ndjson](http://ndjson.org/) stream, optionally you can get the output in FHIR format or GZIPped.

This is a memory-efficient operation. Aidbox just streams the database cursor to a socket. If your HTTP Client supports processing of Chunked Encoding, you can process resources in stream one by one without waiting for the end of the response. See full docs [here](../../bulk-api-1/usddump.md).

{% tabs %}
{% tab title="Request" %}
```
GET [base]/Organization/$dump
```
{% endtab %}

{% tab title="Response" %}
```yaml
HTTP/1.1 200 OK
Content-Type: application/ndjson
Transfer-Encoding: chunked

{"resourceType": "Organization", "id": .............}
{"resourceType": "Organization", "id": .............}
{"resourceType": "Organization", "id": .............}
{"resourceType": "Organization", "id": .............}
.........
```
{% endtab %}
{% endtabs %}

#### [`$dump-csv`](../../bulk-api-1/usddump-csv.md)

Dump Organization resources in CSV format.

This operation dumps resource data in Aidbox format as CSV ([RFC4180](https://datatracker.ietf.org/doc/html/rfc4180)). In this format, columns are paths to JSON values and Rows are values. It includes the header.

Neither the specific order of columns nor the order of rows is guaranteed. See full docs [here](../../bulk-api-1/usddump-csv.md).

```
GET [base]/Organization/$dump-csv
```

### Parameters

| Parameter                        | Description                                                                    |
| -------------------------------- | ------------------------------------------------------------------------------ |
| Organization.active              | true/false                                                                     |
| Organization.address             | address                                                                        |
| Organization.address-city        | city                                                                           |
| Organization.address-country     | country                                                                        |
| Organization.address-postalcode  | postal code                                                                    |
| Organization.address-state       | state                                                                          |
| Organization.address-use         | use                                                                            |
| Organization.boardCertifiedCode  |                                                                                |
| Organization.boardCertifiedValue |                                                                                |
| Organization.endpoint            | technical endpoints providing access to services operated for the organization |
| Organization.identifier          | identifier in a system                                                         |
| Organization.name                | organization name                                                              |
| Organization.partof              | the organization of which this organization forms a part                       |
| Organization.phonetic            |                                                                                |
| Organization.type                | type of organization                                                           |


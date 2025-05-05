---
description: >-
  Describes relationships between two or more organizations, including the
  services one organization provides another, the location(s) where they provide
  services etc.
---

# OrganizationAffiliation

Get 100 organizations (by default):

<pre><code><strong>GET [base]/fhir/OrganizationAffiliation
</strong></code></pre>

Get practitioner role by resource `id`:

<pre><code><strong>GET [base]/fhir/OrganizationAffiliation/[id]
</strong></code></pre>

The full list of FHIR search parameters for OrganizationAffiliation is found [below](organizationaffiliation.md#parameters). Aidbox also offers a selection of query parameters to further refine search results. This includes pagination, full-text search, sorting etc. Refer to the [FHIR API Search docs](broken-reference) for information.

The search results are returned in [Bundles](../../../rest-api/bundle.md) by default. Alternatively, you can specify a `_result` parameter with value `array` to receive resources in a JSON array.

### Example search scenario

1. Get organization affiliation by network:

<pre><code><strong>GET [base]/fhir/OrganizationAffiliation?network=Direct
</strong></code></pre>

2. Find organization `id`:

```json
"organization": {
  "reference": "Organization/bc3d21a5-af67-4ff9-9f08-9758b71a7e0e"
  .......
}
```

3. Get organization by `id`:

<pre><code><strong>GET [base]/fhir/Organization/[id]
</strong></code></pre>

### Bulk API

The following bulk operations on OrganizationAffiliation are available.

#### [`$dump`](../../../bulk-api-1/usddump.md)

Aidbox will respond with [Chunked Transfer Encoding](https://en.wikipedia.org/wiki/Chunked_transfer_encoding) [ndjson](http://ndjson.org/) stream, optionally you can get the output in FHIR format or GZIPped.

This is a memory-efficient operation. Aidbox just streams the database cursor to a socket. If your HTTP Client supports processing of Chunked Encoding, you can process resources in stream one by one without waiting for the end of the response. See full docs [here](../../../bulk-api-1/usddump.md).

{% tabs %}
{% tab title="Request" %}
```
GET [base]/OrganizationAffiliation/$dump
```
{% endtab %}

{% tab title="Response" %}
```yaml
HTTP/1.1 200 OK
Content-Type: application/ndjson
Transfer-Encoding: chunked

{"resourceType": "OrganizationAffiliation", "id": .............}
{"resourceType": "OrganizationAffiliation", "id": .............}
{"resourceType": "OrganizationAffiliation", "id": .............}
{"resourceType": "OrganizationAffiliation", "id": .............}
.........
```
{% endtab %}
{% endtabs %}

#### [`$dump-csv`](../../../bulk-api-1/usddump-csv.md)

Dump Organization resources in CSV format.

This operation dumps resource data in Aidbox format as CSV ([RFC4180](https://datatracker.ietf.org/doc/html/rfc4180)). In this format, columns are paths to JSON values and Rows are values. It includes the header.

Neither the specific order of columns nor the order of rows is guaranteed. See full docs [here](../../../bulk-api-1/usddump-csv.md).

```
GET [base]/OrganizationAffiliation/$dump-csv
```

### Parameters

<table><thead><tr><th width="320">Parameter</th><th>Description</th></tr></thead><tbody><tr><td>OrganizationAffiliation.active</td><td>true/false</td></tr><tr><td>OrganizationAffiliation.date</td><td></td></tr><tr><td>OrganizationAffiliation.email</td><td>email</td></tr><tr><td>OrganizationAffiliation.endpoint</td><td>technical endpoints providing access to services operated for this role</td></tr><tr><td>OrganizationAffiliation.identifier</td><td>identifier in a system</td></tr><tr><td>OrganizationAffiliation.location</td><td>associated location</td></tr><tr><td>OrganizationAffiliation.network</td><td>associated network</td></tr><tr><td>OrganizationAffiliation.organization</td><td>associated organization</td></tr><tr><td>OrganizationAffiliation.participating-organization</td><td>organization that provides/performs the role (e.g. providing services or is a member of)</td></tr><tr><td>OrganizationAffiliation.phone</td><td>phone</td></tr><tr><td>OrganizationAffiliation.primary-organization</td><td>organization where the role is available</td></tr><tr><td>OrganizationAffiliation.role</td><td>definition of the role the participatingOrganization plays</td></tr><tr><td>OrganizationAffiliation.service</td><td>associated healthcare service</td></tr><tr><td>OrganizationAffiliation.specialty</td><td>specific specialty of the participatingOrganization in the context of the role</td></tr><tr><td>OrganizationAffiliation.telecom</td><td>contact details at the participatingOrganization relevant to this Affiliation</td></tr></tbody></table>


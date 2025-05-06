---
description: >-
  Practitioner is a person who is directly or indirectly involved in the
  provisioning of healthcare.
---

# Practitioner

Get 100 practitioners (by default):

<pre><code><strong>GET [base]/fhir/Practitioner
</strong></code></pre>

Get practitioner by name:

<pre><code><strong>GET [base]/fhir/Practitioner?name=SMITH%20JOHN
</strong></code></pre>

Get practitioners by language:

<pre><code><strong>GET [base]/fhir/Practitioner?communication=es
</strong></code></pre>

Get practitioners by specialty:

<pre><code><strong>GET [base]/fhir/Practitioner?qualificationName=PHYSICAL%20THERAPY
</strong></code></pre>

Get practitioner by identifier:

<pre><code><strong>GET [base]/fhir/Practitioner?identifier=1861919391
</strong></code></pre>

Get practitioners by language AND specialty:

<pre><code><strong>GET [base]/fhir/Practitioner?communication=es&#x26;qualificationName=PHYSICAL%20THERAPY
</strong></code></pre>

Get practitioner by resource `id`:

<pre><code><strong>GET [base]/fhir/Practitioner/[id]
</strong></code></pre>

The full list of FHIR search parameters for Practitioner is found [below](practitioner.md#parameters). Aidbox also offers a selection of query parameters to further refine search results. This includes pagination, full-text search, sorting etc. Refer to the [FHIR API Search docs](../../../rest-api/fhir-search/README.md) for information.

The search results are returned in [Bundles](../../../rest-api/bundle.md) by default. Alternatively, you can specify a `_result` parameter with value `array` to receive resources in a JSON array.

Example response:

{% tabs %}
{% tab title="Bundle" %}
```
GET [base]/fhir/Practitioner?ilike=joh+smit
```

{% code lineNumbers="true" %}
```json
{
   "resourceType":"Bundle",
   "type":"searchset",
   "meta":{
      "versionId":"0"
   },
   "total":2,
   "link":[
      {
         "relation":"first",
         "url":"/fhir/Practitioner?ilike=joh+smit&page=1"
      },
      {
         "relation":"self",
         "url":"/fhir/Practitioner?ilike=joh+smit&page=1"
      }
   ],
   "entry":[
      {
         "resource":{
            "name":[
               {
                  "given":[
                     "John"
                  ],
                  "family":"Smith"
               }
            ],
            "birthDate":"1983-03-27",
            "resourceType":"Practitioner",
            "active":false,
            "id":"0399387c-74d9-4b9f-b52f-eff2778775e8",
            "gender":"male"
         },
         "search":{
            "mode":"match"
         }
      },
      {
         "resource":{
            "name":[
               {
                  "given":[
                     "John"
                  ],
                  "family":"Smith"
               }
            ],
            "birthDate":"1983-03-27",
            "resourceType":"Practitioner",
            "active":true,
            "id":"24f0943f-93d4-430b-848a-3bb21c27440c",
            "gender":"male"
         },
         "search":{
            "mode":"match"
         }
      }
   ]
}
```
{% endcode %}
{% endtab %}

{% tab title="JSON array" %}
```
GET [base]/fhir/Practitioner?ilike=joh+smit&_result=array
```

{% code lineNumbers="true" %}
```json
[
   {
      "meta":{
         "versionId":"15043688"
      },
      "name":[
         {
            "given":[
               "John"
            ],
            "family":"Smith"
         }
      ],
      "birthDate":"1983-03-27",
      "resourceType":"Practitioner",
      "active":false,
      "id":"0399387c-74d9-4b9f-b52f-eff2778775e8",
      "gender":"male"
   },
   {
      "meta":{
         "versionId":"27083967"
      },
      "name":[
         {
            "given":[
               "John"
            ],
            "family":"Smith"
         }
      ],
      "birthDate":"1983-03-27",
      "resourceType":"Practitioner",
      "active":true,
      "id":"24f0943f-93d4-430b-848a-3bb21c27440c",
      "gender":"male"
   }
]
```
{% endcode %}
{% endtab %}
{% endtabs %}

### Bulk API

The following bulk operations on Practitioner are available.

#### [`$dump`](../../../bulk-api/usddump.md)

Aidbox will respond with [Chunked Transfer Encoding](https://en.wikipedia.org/wiki/Chunked_transfer_encoding) [ndjson](http://ndjson.org/) stream, optionally you can get the output in FHIR format or GZIPped.

This is a memory-efficient operation. Aidbox just streams the database cursor to a socket. If your HTTP Client supports processing of Chunked Encoding, you can process resources in stream one by one without waiting for the end of the response. See full docs [here](../../../bulk-api/usddump.md).

{% tabs %}
{% tab title="Request" %}
```
GET [base]/Practitioner/$dump
```
{% endtab %}

{% tab title="Response" %}
```yaml
HTTP/1.1 200 OK
Content-Type: application/ndjson
Transfer-Encoding: chunked

{"resourceType": "Practitioner", "id": .............}
{"resourceType": "Practitioner", "id": .............}
{"resourceType": "Practitioner", "id": .............}
{"resourceType": "Practitioner", "id": .............}
.........
```
{% endtab %}
{% endtabs %}

#### [`$dump-csv`](../../../bulk-api/usddump-csv.md)

Dump Practitioner resources in CSV format.

This operation dumps resource data in Aidbox format as CSV ([RFC4180](https://datatracker.ietf.org/doc/html/rfc4180)). In this format, columns are paths to JSON values and Rows are values. It includes the header.

Neither the specific order of columns nor the order of rows is guaranteed. See full docs [here](../../../bulk-api/usddump-csv.md).

```
GET [base]/Practitioner/$dump-csv
```

### Parameters

<table><thead><tr><th width="303">Parameter</th><th>Description</th></tr></thead><tbody><tr><td>Practitioner.active</td><td>true/false</td></tr><tr><td>Practitioner.address</td><td>address</td></tr><tr><td>Practitioner.address-city</td><td>city</td></tr><tr><td>Practitioner.address-country</td><td>country</td></tr><tr><td>Practitioner.address-postalcode</td><td>postal code</td></tr><tr><td>Practitioner.address-state</td><td>state</td></tr><tr><td>Practitioner.address-use</td><td>the use of an address</td></tr><tr><td>Practitioner.boardCertifiedCode</td><td>certification code of a practitioner in ABMS</td></tr><tr><td>Practitioner.boardCertifiedValue</td><td>certification value of a practitioner in ABMS</td></tr><tr><td>Practitioner.communication</td><td>spoken language of a practitioner</td></tr><tr><td>Practitioner.email</td><td>email</td></tr><tr><td>Practitioner.family</td><td>family name</td></tr><tr><td>Practitioner.gender</td><td>gender</td></tr><tr><td>Practitioner.given</td><td>given name</td></tr><tr><td>Practitioner.identifier</td><td>identifier in a system</td></tr><tr><td>Practitioner.name</td><td>full name</td></tr><tr><td>Practitioner.phone</td><td>phone</td></tr><tr><td>Practitioner.phonetic</td><td>part of a family or given name using a phonetic matching algorithm</td></tr><tr><td>Practitioner.qualification</td><td>qualification</td></tr><tr><td>Practitioner.qualificationCode</td><td>qualification code</td></tr><tr><td>Practitioner.qualificationName</td><td>qualification name</td></tr><tr><td>Practitioner.telecom</td><td>telecom</td></tr></tbody></table>

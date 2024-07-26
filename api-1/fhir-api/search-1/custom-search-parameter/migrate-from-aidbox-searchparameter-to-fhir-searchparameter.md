# Migrate from Aidbox SearchParameter to FHIR SearchParameter

{% hint style="info" %}
If you are using Zen project to define your SearchParameter resources, you can also follow this guide, but you will need to remove the SearchParameter definitions from your Zen project.
{% endhint %}

To migrate Aidbox SearchParameter you need to redefine it as regular [FHIR SearchParameters](https://hl7.org/fhir/R5/searchparameter.html) using REST API:

```json
POST /fhir/SearchParameter
content-type: application/json
accept: application/json
```

Let's imagine we had Aidbox SearchParameter `Patient.city`:

```json
GET /SearchParameter/Patient.city

​{
  "name": "city",
  "type": "token",
  "resource": {
    "id": "Patient",
    "resourceType": "Entity"
  },
  "expression": [
    [
      "address",
      "city"
    ]
  ]
}
```

To define FHIR SearchParameter you should know the meaning of some basic FHIR SearchParameter properties, required fields marked as \*:

<table data-full-width="true"><thead><tr><th width="172">Property</th><th width="294">Description</th><th>Additional Notes</th></tr></thead><tbody><tr><td><code>base</code> *</td><td>The resource type(s) this search parameter applies to.</td><td>In Aidbox SearchParameter it was <code>reference</code> to Entity in <code>resource</code> property.</td></tr><tr><td><code>url</code> *</td><td>Canonical identifier for this search parameter, represented as a URI (globally unique).</td><td>Didn't exists in Aidbox SearchParameter.</td></tr><tr><td><code>expression</code> *</td><td><a href="https://www.hl7.org/fhir/fhirpath.html">FHIRPath expression</a> that extracts the values.</td><td>You need to manually convert Adibox <code>SearchParameter.expression</code> to <a href="https://www.hl7.org/fhir/fhirpath.html">F</a><a href="https://www.hl7.org/fhir/fhirpath.html">HIRPath expression</a>.</td></tr><tr><td><code>name</code> *</td><td>Computationally friendly name of the search parameter.</td><td>The same as in Adibox <code>SearchParameter.name</code>.</td></tr><tr><td><code>status</code> *</td><td><code>draft | active | retired | unknown</code>. Binding to <a href="https://hl7.org/fhir/R5/valueset-publication-status.html">publication-status</a> ValueSet.</td><td>Didn't exist in Aidbox SearchParameter. Use <code>active</code> status.</td></tr><tr><td><code>type</code> *</td><td><code>number | date | string | token | reference | composite | quantity | uri | special</code>. Binding to <a href="https://hl7.org/fhir/R4/valueset-search-param-type.html">search-param-type</a> ValueSet.</td><td>Transfer this value as it was in Adbox <code>SearchParameter.type</code>.</td></tr></tbody></table>

Here is the `Patient.city` SearchParameter after migration to FHIR format.

```json
PUT /fhir/SearchParameter/patient-city
content-type: application/json
accept: application/json

{
  "resourceType": "SearchParameter",
  "name": "city",
  "url": "http://example.org/fhir/SearchParameter/patient-city",
  "status": "active",
  "description": "Search by city",
  "code": "city",
  "base": ["Patient"],
  "type": "token",
  "expression": "Patient.address.city"
}
```

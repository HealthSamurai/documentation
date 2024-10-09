# Custom Search Parameters

## Configure Aidbox

To begin using custom search parameters, enable the FHIR Schema validator engine in Aidbox.

{% content-ref url="../../../../modules/profiling-and-validation/fhir-schema-validator/setup.md" %}
[setup.md](../../../../modules/profiling-and-validation/fhir-schema-validator/setup.md)
{% endcontent-ref %}

## Create a custom search parameter via FHIR API

Let's define a custom search parameter that allows searching Patient resources by a specific extension and its value.

### Request

{% tabs %}
{% tab title="YAML" %}
{% code lineNumbers="true" %}
```yaml
POST /fhir/SearchParameter
Content-Type: text/yaml
Accept: text/yaml

resourceType: SearchParameter
id: patient-occupation
url: http://example.org/fhir/SearchParameter/patient-occupation
version:  '1.0.0'
name: occupation
status: active
description: Search patients by occupation
code: occupation
base:
- Patient
type: string
expression: Patient.extension.where(url='http://example.org/fhir/StructureDefinition/occupation').value.as(string)
```
{% endcode %}
{% endtab %}

{% tab title="JSON" %}
{% code lineNumbers="true" %}
```
POST /fhir/SearchParameter
Content-Type: application/json
Accept: application/json

{
  "resourceType": "SearchParameter",
  "id": "patient-occupation",
  "url": "http://example.org/fhir/SearchParameter/patient-occupation",
  "version": "1.0.0",
  "name": "occupation",
  "status": "active",
  "description": "Search patients by occupation",
  "code": "occupation",
  "base": [
    "Patient"
  ],
  "type": "string",
  "expression": "Patient.extension.where(url='http://example.org/fhir/StructureDefinition/occupation').value.as(string)"
}
```
{% endcode %}
{% endtab %}
{% endtabs %}

### Response

{% code title="Status: 200" lineNumbers="true" %}
```yaml
description: Search patients by occupation
expression: Patient.extension.where(url='http://example.org/fhir/StructureDefinition/occupation').value.as(string)
name: occupation
type: string
resourceType: SearchParameter
status: active
id: patient-occupation
url: http://example.org/fhir/SearchParameter/patient-occupation
code: occupation
base: 
- Patient
version: 1.0.0
```
{% endcode %}

### Test Search Parameter:

Create Patient resource with required extension:

{% tabs %}
{% tab title="YAML" %}
{% code lineNumbers="true" %}
```yaml
POST /fhir/Patient
content-type: text/yaml
accept: text/yaml

resourceType: Patient
id: example-patient
extension:
- url: http://example.org/fhir/StructureDefinition/occupation
  valueString: Engineer
name:
- family: Doe
  given:
  - John
gender: male
birthDate: '1980-01-01'
```
{% endcode %}
{% endtab %}

{% tab title="JSON" %}
{% code lineNumbers="true" %}
```json
POST /fhir/Patient
content-type: application/json
accept: application/json

{
    "resourceType": "Patient",
    "id": "example-patient",
    "extension": [
        {
            "url": "http://example.org/fhir/StructureDefinition/occupation",
            "valueString": "Engineer"
        }
    ],
    "name": [
        {
            "family": "Doe",
            "given": [
                "John"
            ]
        }
    ],
    "gender": "male",
    "birthDate": "1980-01-01"
}
```
{% endcode %}
{% endtab %}
{% endtabs %}

Search for Patient by occupation:

```
GET /fhir/Patient?occupation=Engineer
```

Response:

{% code title="Status: 200" lineNumbers="true" %}
```yaml
resourceType: Bundle
type: searchset
meta:
  versionId: '0'
total: 1
link:
  - relation: first
    url: https://example.aidbox.app/fhir/Patient?occupation=Engineer&page=1
  - relation: self
    url: https://example.aidbox.app/fhir/Patient?occupation=Engineer&page=1
entry:
  - resource:
      name:
        - given:
            - John
          family: Doe
      gender: male
      birthDate: '1980-01-01'
      extension:
        - url: http://example.org/fhir/StructureDefinition/occupation
          valueString: Engineer
      id: >-
        example-patient
      resourceType: Patient
      meta:
        lastUpdated: '2024-06-07T10:21:29.110510Z'
        versionId: '6'
        extension:
          - url: https://fhir.aidbox.app/fhir/StructureDefinition/created-at
            valueInstant: '2024-06-07T10:21:29.110510Z'
    search:
      mode: match
    fullUrl: https://example.aidbox.app/Patient/example-patient
    link:
      - relation: self
        url: https://example.aidbox.app/Patient/example-patient
```
{% endcode %}

## SearchParamter resource structure

This table contains properties required by the FHIR specification and properties that Aidbox interprets.

<table><thead><tr><th width="144">Property</th><th width="167">FHIR datatype</th><th>Description</th></tr></thead><tbody><tr><td>url</td><td>uri</td><td>Search parameter unique canonical url</td></tr><tr><td>version</td><td>string</td><td>Search parameter version</td></tr><tr><td>name</td><td>string</td><td>Search parameter name, used to perform actual search queries</td></tr><tr><td>status</td><td>code</td><td>draft | active | retired | unknown</td></tr><tr><td>description</td><td>markdown</td><td>Natural language description of the search parameter</td></tr><tr><td>code</td><td>code</td><td>Recommended name for parameter in search url (Aidbox ignores it, and use <code>.name</code> instead)</td></tr><tr><td>base</td><td>code[]</td><td>The resource type(s) this search parameter applies to</td></tr><tr><td>type</td><td>code</td><td>number | date | string | token | reference | composite | quantity | uri | special</td></tr><tr><td>expression</td><td>string</td><td>FHIRPath expression that extracts the values</td></tr><tr><td>component</td><td>BackboneElement</td><td>For Composite resources to define the parts</td></tr></tbody></table>

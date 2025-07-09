# Create

```javascript
POST [base]/fhir/[type]
```

FHIR API [ignores](https://www.hl7.org/fhir/http.html#create) `id` in `POST` requests, but Aidbox API respects `id` inside the request body and creates a resource with a specific `id`. This decision was made because we didn't find any reasons to ignore it and to make Aidbox API be closer to the SQL `INSERT` query. As a result, a new response code `409 Conflict` appeared.

| Response code | Text              | Description                                                                      |
| ------------- | ----------------- | -------------------------------------------------------------------------------- |
| **`201`**     | **Created**       | Resource successfully created                                                    |
| **`400`**     | **Bad Request**   | Resource could not be parsed or fail basic FHIR validation rules                 |
| **`409`**     | **Conflict**      | Resource with such id already exists                                             |
| **`422`**     | **Unprocessable** | The proposed resource violated applicable FHIR profiles or server business rules |

## create

```javascript
POST [base]/fhir/[type]
```

This is one of the most basic interactions which gives an ability to create resources. It uses the `POST` HTTP method accepting a resource type via path parameters and the resource as a body of the request. A response to this interaction may be one of the following:

| Response code | Text              | Description                                                                      |
| ------------- | ----------------- | -------------------------------------------------------------------------------- |
| **`201`**     | **Created**       | Resource successfully created                                                    |
| **`400`**     | **Bad Request**   | Resource could not be parsed or fail basic FHIR validation rules                 |
| **`409`**     | **Conflict**      | Resource with such id already exists                                             |
| **`422`**     | **Unprocessable** | The proposed resource violated applicable FHIR profiles or server business rules |

A successful response (`2xx`) also contains a created resource as a body and additional headers `Location`, `ETag`, `Last-Modified` which contain the full path to a resource (base url, resource type and id of a newly created resource), additionally information about version (vid) and modification time of that resource.

```yaml
Location: [base]/[type]/[id]/_history/[vid]
ETag: [vid]
Last-Modified: [modification-datetime]
```

An unsuccessful response (`4xx`) contains `OperationOutcome` resource which describes issues the server faced during the creation of this resource.

### `201` Created

{% tabs %}
{% tab title="Request" %}
```yaml
POST /fhir/Patient

name: [{given: ["Bob"]}]
```
{% endtab %}

{% tab title="Response" %}
**Status:** `201`

```yaml
name:
  - given:
      - Bob
id: '17b69d79-3d9b-45f8-af79-75f958502763'
resourceType: Patient
meta:
  lastUpdated: '2018-11-29T10:44:10.588Z'
  versionId: '13'
  extension:
    - url: 'ex:createdAt'
      valueInstant: '2018-11-29T10:44:10.588Z'
```
{% endtab %}
{% endtabs %}

### `422` Unprocessable entity

{% tabs %}
{% tab title="Request" %}
```yaml
POST /fhir/Patient

name: "Bob"
```
{% endtab %}

{% tab title="Response" %}
**Status:** `422`

```yaml
resourceType: OperationOutcome
text:
  status: generated
  div: Invalid resource
issue:
  - severity: fatal
    code: invalid
    expression:
      - Patient.name
    diagnostics: expected array
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
Aidbox REST API doesn't ignore`id` and treats it as all other attributes in contrast to FHIR API. Read more about differences [here](../other/aidbox-and-fhir-formats.md).
{% endhint %}

## Conditional Create

```
POST [base]/fhir/[type]?[search parameters]
```

A more complex way to create a resource (it requires the knowledge of [search](../fhir-search/README.md)) but it gives some additional flexibility. If you provide search parameters, `create` becomes `conditional create` and works in the following way (depending on the number of search results):

* **No matches**: The server performs a `create` interaction
* **One Match**: The server returns the found resource and `200 OK`
* **Multiple matches**: The server returns a `412 Precondition Failed` error indicating the client's criteria were not selective enough

### `200` OK

Create a patient if there is no patient with the name Bob.

{% tabs %}
{% tab title="Request" %}
```yaml
POST /fhir/Patient?name=Bob

name: [{given: ["Bob"]}]
gender: male
```
{% endtab %}

{% tab title="Response" %}
**Status:** `200`

```yaml
name:
- given: [Bob]
gender: male
id: 17b69d79-3d9b-45f8-af79-75f958502763
resourceType: Patient
meta:
  lastUpdated: '2018-11-29T10:44:10.588Z'
  versionId: '13'
  extension:
    - url: 'ex:createdAt'
      valueInstant: '2018-11-29T10:44:10.588Z'
```
{% endtab %}
{% endtabs %}

A patient was not created, an existing patient was returned.

### If-None-Exist header
You can also use the `If-None-Exist` header: 

Create a patient if there is no patient with the name Bob.

{% tabs %}
{% tab title="Request" %}
```yaml
POST /fhir/Patient
If-None-Exist: name=Bob

name: [{given: ["Bob"]}]
gender: male
```
{% endtab %}

{% tab title="Response" %}
**Status:** `200`

```yaml
name:
- given: [Bob]
gender: male
id: 17b69d79-3d9b-45f8-af79-75f958502763
resourceType: Patient
meta:
  lastUpdated: '2018-11-29T10:44:10.588Z'
  versionId: '13'
  extension:
    - url: 'ex:createdAt'
      valueInstant: '2018-11-29T10:44:10.588Z'
```
{% endtab %}
{% endtabs %}

### Conditional create via Bundle

When performing a conditional create using a Bundle, note that the `ifNoneExist` field in the request is case-sensitive according to the FHIR specification. Ensure that it is spelled exactly as `ifNoneExist` — using any other casing (e.g., IfNoneExist, ifnoneexist) will result in it being ignored.

When performing a POST operation (including within a transaction Bundle), do not include an id field in the resource body. The server will assign a new id automatically if the resource is not existing. 

```json
POST /fhir/

{
  "resourceType": "Bundle",
  "type": "transaction",
  "entry": [
    {
      "fullUrl": "urn:uuid:patient-1",
      "resource": {
        "resourceType": "Patient",
        "name": [
          {
            "use": "official",
            "given": ["Bob"]
          }
        ]
      },
      "request": {
        "method": "POST",
        "url": "Patient",
        "ifNoneExist": "name=Bob"
      }
    },
    {
      "fullUrl": "urn:uuid:encounter-1",
      "resource": {
        "resourceType": "Encounter",
        "status": "in-progress",
        "class": {
          "system": "http://terminology.hl7.org/CodeSystem/v3-ActCode",
          "code": "AMB",
          "display": "ambulatory"
        },
        "subject": {
          "reference": "urn:uuid:patient-1"
        },
        "period": {
          "start": "2025-07-01T09:00:00+05:30",
          "end": "2025-07-01T10:00:00+05:30"
        }
      },
      "request": {
        "method": "POST",
        "url": "Encounter",
        "ifNoneExist": "patient:Patient.name=Bob&status=in-progress"
      }
    }
  ]
}
```

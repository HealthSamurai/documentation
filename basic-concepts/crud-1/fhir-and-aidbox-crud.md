# Create

```http
POST [base]/[type]
```

FHIR API [ignores](https://www.hl7.org/fhir/http.html#create) `id` in `POST` requests, but Aidbox API respect `id` inside request body and creates resource with specific `id`. This decision was made because we didn't find any reasons to ignore it and to make Aidbox API be closer to sql `INSERT` query. As a result new response code `409 Conflict` appeared:\`

* **`201` Created** - resource successfully created
* **`400` Bad Request** - resource could not be parsed or failed basic FHIR validation rules
* **`409`** **Conflict** - resource with such id already exists
* **`422` Unprocessable Entity** - the proposed resource violated applicable FHIR profiles or server business rules



### Conditional create

```
POST [base]/[type]?[search parameters]
```

Instead of using `If-None-Exist` header, Aidbox uses query parameters as in ordinary `read` operation. This done to make all conditional operations to look the same \(use search query parameters\). 

* **No matches**: The server performs a `create` interaction \(Aidbox version of create\)
* **One Match**: The server returns the found resource and `200 OK`
* **Multiple matches**: The server returns a `412 Precondition Failed` error indicating the client's criteria were not selective enough

## create

```text
POST [base]/[type]
```

This is one of the most basic interactions which gives an ability to create resources. It uses `POST` HTTP method, accepts a resource type via path parameters, and resource as a body of the request. A response to this interaction may be one of the following:

* **`201`** **Created** — resource successfully created
* **`400`** **Bad Request** — resource could not be parsed or failed basic FHIR validation rules
* **`409`** **Conflict** — resource with such id already exists
* **`422`** **Unprocessable Entity** — the proposed resource violated applicable FHIR profiles or server business rules

A successful response \(`2xx`\) also contains a created resource as a body and additional headers `Location`, `ETag`, `Last-Modified` which contain full path to resource \(base url, resource type and id of newly created resource\), additionally information about version \(vid\) and modification time of that resource.

```text
Location: [base]/[type]/[id]/_history/[vid]
ETag: [vid]
Last-Modified: [modification-datetime]
```

An unsuccessful response  \(`4xx`\) contains `OperationOutcome` resource which describes issues server faced during creation of this resource.

### `201` Created

{% tabs %}
{% tab title="Request" %}
```yaml
POST /Patient

name: [{given: ["Bob"]}]
```
{% endtab %}

{% tab title="Response" %}
**Status:** `201`

```yaml
name:
- given: [Bob]
id: 17b69d79-3d9b-45f8-af79-75f958502763
resourceType: Patient
meta:
  lastUpdated: '2018-11-29T10:44:10.588Z'
  versionId: '13'
  tag:
  - {system: 'https://aidbox.app', code: created}
```
{% endtab %}
{% endtabs %}

### `422` Unprocessable entity

{% tabs %}
{% tab title="Request" %}
```yaml
POST /Patient

name: "Bob"
```
{% endtab %}

{% tab title="Response" %}
**Status:** `422`

```yaml
resourceType: OperationOutcome
errors:
- path: [name]
  message: expected array
warnings: []
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
Aidbox REST API doesn't ignore`id` and treat it as all other attributes in contrast to FHIR API. Read more about differences [here]().
{% endhint %}

## conditional create

```text
POST [base]/[type]?[search parameters]
```

Much more complex way to create a resource \(it requires knowledge of [search](../search-1/)\) but it gives some additional flexibility. If you provide search parameters, `create` becomes `conditional create` and works in the following way \(depending on the number of search results\): 

* **No matches**: The server performs a `create` interaction
* **One Match**: The server returns the found resource and `200 OK`
* **Multiple matches**: The server returns a `412 Precondition Failed` error indicating the client's criteria were not selective enough

### `200` OK

Create a patient if there is no patient with name Bob.

{% tabs %}
{% tab title="Request" %}
```yaml
POST /Patient?name=Bob

name: [{given: ["Bob"]}]
gender: male
```
{% endtab %}

{% tab title="Response" %}
**Status:** `200`

```yaml
name:
- given: [Bob]
id: 17b69d79-3d9b-45f8-af79-75f958502763
resourceType: Patient
meta:
  lastUpdated: '2018-11-29T10:44:10.588Z'
  versionId: '13'
  tag:
  - {system: 'https://aidbox.app', code: created}
```
{% endtab %}
{% endtabs %}

A patient was not created, an existing patient was returned.


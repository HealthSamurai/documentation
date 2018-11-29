---
description: Basic endpoints to manage resources
---

# CRUD

This part of documentation describes how to create, read, update and delete resources. Also, it covers some advanced topic like conditional create, update and delete. Aidbox REST API slightly differs from canonical FHIR REST API. There is an [article](../../basic-concepts/aidbox-vs-fhir.md), which describes those differences.

## Terms

A **resource** is an object with a type, associated data, relationships to other resources \(all that information can be found in FHIR [specification](https://www.hl7.org/fhir/resourcelist.html) or through Aidbox [metadata](../custom-metadata.md)\), and a set of methods that operate on it. In most cases a resource represented as a JSON/XML/YAML document.

Each resource has its own resource **type**, this type define set of data, which can be stored with this resource and possible relationships with other resources.

Attribute is a part of resource definition, which describe what fields can or must be present in resource document, type of such field and cardinality.

Every resource type has the same set of **interactions** available. Those interactions are described below. 

Each interaction can fail with:

* **`403`** **Forbidden** - client are not authorized to perform the interaction

## create

```text
POST [base]/[type]
```

One of the most basic interactions, which gives an ability to create a resource. It uses `POST` HTTP method, accepts resource type via path params and resource as a body of request. Response of this interaction may be one of the following:

* **`201`** **Created** - resource successfully created
* **`400`** **Bad Request** - resource could not be parsed or failed basic FHIR validation rules
* **`409`** **Conflict** - resource with such id already exists
* **`422`** **Unprocessable Entity** - the proposed resource violated applicable FHIR profiles or server business rules

Successful response `2xx` also contains a created resource as a body and additional headers `Location`, `ETag`, `Last-Modified`, which contains full path to resource \(base url, resource type and id of newly created resource\) and additionally information about version \(vid\) and modification time of that resource.

```text
Location: [base]/[type]/[id]/_history/[vid]
ETag: [vid]
Last-Modified: [modification-datetime]
```

Unsuccessful response  `4xx` contains `OperationOutcome` resource, which describes issues server faced creating this resource.

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
Aidbox REST API doesn't ignore `id` and treat it as all other attributes in contrast to FHIR API. Read more about differences [here](../../basic-concepts/aidbox-vs-fhir.md).
{% endhint %}

## conditional create

```text
POST [base]/[type]?[search parameters]
```

Much more complex way to create a resource \(it requires knowledge of [search](../history.md)\), but it gives some additional flexibility. If you provide search parameters `create` becomes `conditional create` and works in following way \(depending on the number of search results\): 

* **No matches**: The server performs a `create` interaction
* **One Match**: The server ignore the post and returns `200 OK`
* **Multiple matches**: The server returns a `412 Precondition Failed` error indicating the client's criteria were not selective enough

### `200` OK

Create patient if there is no patient with name Bob.

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

Patient not created, existing patient was returned.

## read

```text
GET [base]/[type]/[id]
```

One of the most basic interactions, used to obtain a resource by a given `id`. For more advanced options for getting resources check out [Search](../history.md).

* **`200`** **OK** - resource successfully found and returned
* **`404`** **Not Found** - resource with a given `id` doesn't exist on the server
* **`410`** **Gone** - resource was deleted

### `200` OK

Get existing patient:

{% tabs %}
{% tab title="Request" %}
```text
GET /Patient/17b69d79-3d9b-45f8-af79-75f958502763
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

### `404` Not Found

Attempt to get not-existing patient:

{% tabs %}
{% tab title="Request" %}
```text
GET /Patient/some-not-existing-id
```
{% endtab %}

{% tab title="Response" %}
**Status:** `404`

```yaml
resourceType: OperationOutcome
status: 404
text: Resource Patient/some-not-existing-id not found
```
{% endtab %}
{% endtabs %}

## vread

```text
GET [base]/[type]/[id]/_history/[vid]
```

Another operation, which returns a specific version resource. Similar to read, but additionally requires to specify version id.

### `200` OK

Version id `13` was extracted from response of `create` interaction.

{% tabs %}
{% tab title="Request" %}
```text
GET /Patient/17b69d79-3d9b-45f8-af79-75f958502763/_history/13
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

## update

```text
PUT [base]/[type]/[id]
```

Interaction, which allows to modify existing resource \(create new version of it\). After performing this interaction resource will be replaced with new version of resource provided in the body of request. `id` of a resource can't be changed \(at least cause of versioning\) and `id` in the body of the resource is ignored in update interaction \(it's done to make conditional update possible without knowing logical id of the resource\). If a resource with `id` \(provided in the url\) doesn't exist new resource will be created. Following codes can be returned by the server:

* **`200`** **OK** - resource successfully updated
* **`201`** **Created** - resource successfully created
* **`422`** **Unprocessable Entity** - the proposed resource violated applicable FHIR profiles or server business rules

### **`200`** OK

Update patient by given id:

{% tabs %}
{% tab title="Request" %}
```yaml
PUT /Patient/17b69d79-3d9b-45f8-af79-75f958502763

name: [{given: ["Bob"]}]
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
  lastUpdated: '2018-11-29T13:58:03.875Z'
  versionId: '38'
  tag:
  - {system: 'https://aidbox.app', code: updated}
```
{% endtab %}
{% endtabs %}

### `201` Created

Create a patient with specified id:

{% tabs %}
{% tab title="Request" %}
```yaml
PUT /Patient/tom-id

name: [{given: ["Tom"]}]
```
{% endtab %}

{% tab title="Response" %}
**Status:** `201`

```yaml
name:
- given: [Tom]
id: tom-id
resourceType: Patient
meta:
  lastUpdated: '2018-11-29T14:01:09.336Z'
  versionId: '40'
  tag:
  - {system: 'https://aidbox.app', code: created}
```
{% endtab %}
{% endtabs %}

## conditional update

```text
PUT [base]/[type]?[search parameters]
```

More complex way to update a resource, but gives more power, it gives ability to update a resource without knowing `id`, but requires knowledge of [Search](../history.md). Different response codes will be returned \(based on the number of search results\):

* **No matches**: The server performs a `create` interaction \(Aidbox version of create\)
* **One Match**: The server performs the update against the matching resource
* **Multiple matches**: The server returns a `412 Precondition Failed` error indicating the client's criteria were not selective enough

### `200` OK

Update patient by name.

{% tabs %}
{% tab title="Request" %}
```yaml
PUT /Patient?name=Tom

name: [{given: ["Tom"]}]
gender: male
```
{% endtab %}

{% tab title="Response" %}
**Status:** `200`

```yaml
name:
- given: [Tom]
gender: male
id: tom-id
resourceType: Patient
meta:
  lastUpdated: '2018-11-29T14:10:31.885Z'
  versionId: '42'
  tag:
  - {system: 'https://aidbox.app', code: updated}
```
{% endtab %}
{% endtabs %}

### `201` Created

 Create a patient with a name Julie and specified id if no other patient with the same name exists:

{% tabs %}
{% tab title="Request" %}
```yaml
PUT /Patient?name=Julie

id: julie-id
name: [{given: ["Julie"]}]
gender: female
```
{% endtab %}

{% tab title="Response" %}
**Status:** `201`

```yaml
name:
- given: [Julie]
gender: female
id: julie-id
resourceType: Patient
meta:
  lastUpdated: '2018-11-29T14:13:03.416Z'
  versionId: '43'
  tag:
  - {system: 'https://aidbox.app', code: created}
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
If patient with name Julie already exists `update` interaction will be performed and `id` will be ignored.
{% endhint %}

## delete

```text
DELETE [base]/[type]/[id]
```

Interaction deletes a resource, respond with `200 OK` on successful delete, but on deletion of already deleted resource respond with `204 No Content`. 

To get `204 No Content` always instead of `200 OK` use `_no-content=true` query parameter.

* **`200`** **OK** - resource successfully delete
* **`204`** **No Content** - resource already deleted
* **`404`** **Not Found** - resource not found

### `200` OK

Basic case for delete:

{% tabs %}
{% tab title="Request" %}
```text
DELETE /Patient/tom-id
```
{% endtab %}

{% tab title="Response" %}
**Status:** `200`

```yaml
name:
- given: [Tom]
gender: male
id: tom-id
resourceType: Patient
meta:
  lastUpdated: '2018-11-29T14:33:17.429Z'
  versionId: '44'
  tag:
  - {system: 'https://aidbox.app', code: deleted}
```
{% endtab %}
{% endtabs %}

### `204` No Content

Attempt to delete already deleted resource:

{% tabs %}
{% tab title="Request" %}
```text
DELETE /Patient/tom-id
```
{% endtab %}

{% tab title="Response" %}
**Status:** `204`

```text

```
{% endtab %}
{% endtabs %}

## conditional delete


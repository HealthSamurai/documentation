# Update

```http
PUT [base]/[type]/[id]
```

Aidbox doesn't have atomic update yet. It also allows to omit `id` in resource body

* **`200` OK** - resource successfully updated
* **`201` Created** - resource successfully created
* **`422` Unprocessable Entity** - the proposed resource violated applicable FHIR profiles or server business rules



### Conditional Update

```
PUT [base]/[type]?[search parameters]
```

In contrast to FHIR, Aidbox conditional update allows to create a resource with specific `id`. `Conditional update` in case of one match ignores id coming in request body, that mean that resource `id` can't be changed by any `update` operation.

* **No matches**: The server performs a `create` interaction \(Aidbox version of create\)
* **One Match**: The server performs the update against the matching resource
* **Multiple matches**: The server returns a `412 Precondition Failed` error indicating the client's criteria were not selective enough

## **versioned update**

While you do update there is a risk of  override latest changes done by another operation. To escape this situation you can use versioned update by sending with update `If-Match` header with `versionId` of resource you want to update. If server has same version of resources update will be successful, if versions do not match - you will get OperationOutcome with conflict code.

#### Example

Let say we created patient:

{% code-tabs %}
{% code-tabs-item title="create-patient-request" %}
```yaml
POST /Patient

id: pt-1
name: [{family: ['Wrong']}]
```
{% endcode-tabs-item %}
{% endcode-tabs %}

```yaml
status: 201

resourceType: Patient
id: pt-1
meta: {lastUpdated: '2019-04-04T09:15:25.210Z', versionId: '30'}
name:
- {family: Wrong}
```

Now to fix family for this patient without risk override someone else changes - we use versioned update request: 

{% code-tabs %}
{% code-tabs-item title="versioned-update-request" %}
```yaml
PUT /Patient/pt-id
If-Match: 30

name: [{family: ['Smith']}]
```
{% endcode-tabs-item %}
{% endcode-tabs %}

If someone already touched same patient, his version id was changed and we get OperationOutcome.

{% code-tabs %}
{% code-tabs-item title="conflict-response" %}
```yaml
status: 409

resourceType: OperationOutcome
id: conflict
text: {status: generated, div: Version Id mismatch}
issue:
- {severity: fatal, code: conflict, diagnostics: Version Id mismatch}
```
{% endcode-tabs-item %}
{% endcode-tabs %}

## conditional update

```text
PUT [base]/[type]?[search parameters]
```

This is more complex way to update a resource but it gives more power. It gives the ability to update a resource without knowing `id` but requires knowledge of [Search](../search-1/). Different response codes will be returned \(based on the number of search results\):

* **No matches**: The server performs a `create` interaction
* **One Match**: The server performs the update against the matching resource
* **Multiple matches**: The server returns a `412 Precondition Failed` error indicating the client's criteria were not selective enough

### `200` OK

Update the patient by name.

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

 Create a patient with the name Julie and specified id if no other patients with the same name exist:

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
If a patient with name Julie already exists, `update` interaction will be performed and `id` will be ignored.
{% endhint %}

## update

```text
PUT [base]/[type]/[id]
```

This is interaction which allows to modify existing resource \(create a new version of it\). After performing this interaction, the resource will be replaced with a new version of resource provided in the body of the request. `id` of a resource can't be changed \(at least cause of versioning\) and `id` in the body of the resource is ignored in `update` interaction \(it's done to make a `conditional update` possible without knowing logical id of the resource\). If a resource with `id` \(provided in the url\) doesn't exist, new resource will be created. Following codes can be returned by the server:

* **`200`** **OK** — resource successfully updated
* **`201`** **Created** — resource successfully created
* **`422`** **Unprocessable Entity** — the proposed resource violated applicable FHIR profiles or server business rules

### **`200`** OK

Update a patient by given id:

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

## 


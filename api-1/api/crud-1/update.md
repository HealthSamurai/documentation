# Update

```http
PUT [base]/[type]/[id]
```

Aidbox doesn't have an atomic update yet. It also allows omitting `id` in the resource body.

| Response code | Text | Description |
| :--- | :--- | :--- |
| **`200`** | **OK** | Resource successfully updated |
| **`201`** | **Created** | Resource successfully created |
| **`422`** | **Unprocessable Entity** | The proposed resource violated applicable FHIR profiles or server business rules |

### Conditional Update

```
PUT [base]/[type]?[search parameters]
```

In contrast to FHIR, Aidbox conditional update allows creating a resource with a specific `id`. In case of one match, `conditional update`  ignores the id coming in the request body. That means that resource `id` can't be changed by any `update` operation.

* **No matches**: The server performs a `create` interaction \(Aidbox version of create\)
* **One Match**: The server performs the update against the matching resource
* **Multiple matches**: The server returns a `412 Precondition Failed` error indicating the client's criteria were not selective enough

## **Versioned Update**

While you update, there is a risk of overriding the latest changes done by another operation. To escape this situation, you can use a versioned update by sending with update `If-Match` header with `versionId` of resource you want to update. If the server has the same version of resources, the update will be successful. If versions do not match, you will get OperationOutcome with conflict code.

#### Example

Let say we created a patient:

{% tabs %}
{% tab title="Request \(FHIR\)" %}
{% code title="create-patient-request" %}
```yaml
POST /fhir/Patient

id: pt-1
name: [{family: 'Wrong'}]
```
{% endcode %}
{% endtab %}

{% tab title="Request \(Aidbox\)" %}
```yaml
create-patient-request

POST /Patient

id: pt-1
name: [{family: 'Wrong'}]
```
{% endtab %}

{% tab title="Response \(FHIR\)" %}
**Status:** `201`

```yaml
name:
  - family: Wrong
id: 'pt-1'
resourceType: Patient
meta:
  lastUpdated: '2019-04-04T09:15:25.210Z'
  versionId: '471'
  extension:
    - url: 'ex:createdAt'
      valueInstant: '2019-04-04T09:15:25.210Z'
```
{% endtab %}

{% tab title="Response \(Aidbox\)" %}
**Status:** `201`

```yaml
name:
  - family: Wrong
id: 'pt-1'
resourceType: Patient
meta:
  lastUpdated: '2019-04-04T09:15:25.210Z'
  createdAt: '2019-04-04T09:15:25.210Z'
  versionId: '471'
```
{% endtab %}
{% endtabs %}

To fix the family for this patient without the risk of overriding someone else's changes, we can use a versioned update request:

{% tabs %}
{% tab title="Request \(FHIR\)" %}
{% code title="versioned-update-request" %}
```yaml
PUT /fhir/Patient/pt-id
If-Match: 30

name: [{family: ['Smith']}]
```
{% endcode %}
{% endtab %}

{% tab title="Request \(Aidbox\)" %}
```yaml
versioned-update-request

PUT /Patient/pt-id
If-Match: 30

name: [{family: ['Smith']}]
```
{% endtab %}
{% endtabs %}

If someone has already edited the same patient, his version id was changed, and we got OperationOutcome.

{% tabs %}
{% tab title="Response \(FHIR\)" %}
{% code title="conflict-response" %}
```yaml
status: 409

resourceType: OperationOutcome
id: 'conflict'
text:
  status: generated
  div: Version Id mismatch
issue:
  - severity: fatal
    code: conflict
    diagnostics: Version Id mismatch
```
{% endcode %}
{% endtab %}

{% tab title="Response \(Aidbox\)" %}
```yaml
conflict-response

status: 409

resourceType: OperationOutcome
id: 'conflict'
text:
  status: generated
  div: Version Id mismatch
issue:
  - severity: fatal
    code: conflict
    diagnostics: Version Id mismatch


```
{% endtab %}
{% endtabs %}

## Conditional Update

```text
PUT [base]/[type]?[search parameters]
```

This is a more complex way to update a resource, but it gives more power. You can update a resource without knowing the `id` , but it requires the knowledge of [Search](../../fhir-api/search-1/). Different response codes will be returned \(based on the number of search results\):

* **No matches**: The server performs a `create` interaction
* **One Match**: The server performs the update against the matching resource
* **Multiple matches**: The server returns a `412 Precondition Failed` error indicating the client's criteria were not selective enough

### `200` OK

Update the patient by name.

{% tabs %}
{% tab title="Request \(FHIR\)" %}
```yaml
PUT /fhir/Patient?name=Tom

name: [{given: ["Tom"]}]
gender: male
```
{% endtab %}

{% tab title="Request \(Aidbox\)" %}
```yaml
PUT /Patient?name=Tom

name: [{given: ["Tom"]}]
gender: male
```
{% endtab %}

{% tab title="Response \(FHIR\)" %}
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

{% tab title="Response \(Aidbox\)" %}

{% endtab %}
{% endtabs %}

### `201` Created

Create a patient with the name Julie and specified id if no other patients with the same name exist:

{% tabs %}
{% tab title="Request \(FHIR\)" %}
```yaml
PUT /fhir/Patient?name=Julie

id: julie-id
name: [{given: ["Julie"]}]
gender: female
```
{% endtab %}

{% tab title="Request \(Aidbox\)" %}
```yaml
PUT /Patient?name=Julie

id: julie-id
name: [{given: ["Julie"]}]
gender: female
```
{% endtab %}

{% tab title="Response \(FHIR\)" %}
**Status:** `201`

```yaml
name:
- given: Julie
gender: female
id: julie-id
resourceType: Patient
meta:
  lastUpdated: '2018-11-29T14:13:03.416Z'
  versionId: '43'
  extension:
    - url: 'ex:createdAt'
      valueInstant: '2018-11-29T14:13:03.416Z'
```
{% endtab %}

{% tab title="Response \(Aidbox\)" %}
**Status:** `201`

```yaml
name:
  - given:
      - Julie
gender: female
id: 'julie-id'
resourceType: Patient
meta:
  lastUpdated: '2018-11-29T14:13:03.416Z'
  createdAt: '2018-11-29T14:13:03.416Z'
  versionId: '43'
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
If a patient with the name Julie already exists, `update` interaction will be performed and `id` will be ignored.
{% endhint %}

## Update

```text
PUT [base]/[type]/[id]
```

This interaction allows modifying an existing resource \(creating a new version of it\). After performing this interaction, the resource will be replaced with a new version of the resource provided in the body of the request. `id` of a resource can't be changed \(at least cause of versioning\) and `id` in the body of the resource is ignored in `update` interaction \(in order to make a `conditional update` possible without knowing the logical id of the resource\). If a resource with `id` \(provided in the url\) doesn't exist, a new resource will be created. Following codes can be returned by the server:

| Response code | Text | Description |
| :--- | :--- | :--- |
| **`200`** | **OK** | Resource successfully updated |
| **`201`** | **Created** | Resource successfully created |
| **`422`** | **Unprocessable Entity** | The proposed resource violated applicable FHIR profiles or server business rules |

### **`200`** OK

Update a patient by a given id:

{% tabs %}
{% tab title="Request \(FHIR\)" %}
```yaml
PUT /fhir/Patient/17b69d79-3d9b-45f8-af79-75f958502763

name: [{given: ["Bob"]}]
```
{% endtab %}

{% tab title="Request \(Aidbox\)" %}
```yaml
PUT /Patient/17b69d79-3d9b-45f8-af79-75f958502763

name: [{given: ["Bob"]}]
```
{% endtab %}

{% tab title="Response \(FHIR\)" %}
**Status:** `200`

```yaml
name:
  - given:
      - Bob
id: 17b69d79-3d9b-45f8-af79-75f958502763
resourceType: Patient
meta:
  lastUpdated: '2018-11-29T13:58:03.875Z'
  versionId: '38'
  extension:
    - url: 'ex:createdAt'
      valueInstant: '2018-11-29T13:58:03.875Z'
```
{% endtab %}

{% tab title="Response \(Aidbox\)" %}
**Status:** `200`

```yaml
name:
- given: Bob
id: 17b69d79-3d9b-45f8-af79-75f958502763
resourceType: Patient
meta:
  lastUpdated: '2018-11-29T13:58:03.875Z'
  createdAt: '2018-11-29T13:58:03.875Z'
```
{% endtab %}
{% endtabs %}

### `201` Created

Create a patient with a specified id:

{% tabs %}
{% tab title="Request \(FHIR\)" %}
```yaml
PUT /fhir/Patient/tom-id

name: [{given: ["Tom"]}]
```
{% endtab %}

{% tab title="Request \(Aidbox\)" %}
```yaml
PUT /Patient/tom-id

name: [{given: ["Tom"]}]
```
{% endtab %}

{% tab title="Response \(FHIR\)" %}
**Status:** `201`

```yaml
name:
  - given:
      - Tom
id: 'tom-id'
resourceType: Patient
meta:
  lastUpdated: '2018-11-29T14:01:09.336Z'
  versionId: '40'
  extension:
    - url: 'ex:createdAt'
      valueInstant: '2018-11-29T14:01:09.336Z'
```
{% endtab %}

{% tab title="Response \(Aidbox\)" %}
**Status:** `201`

```yaml
name:
  - given:
      - Tom
id: 'tom-id'
resourceType: Patient
meta:
  lastUpdated: '2018-11-29T14:01:09.336Z'
  createdAt: '2018-11-29T14:01:09.336Z'
  versionId: '40'
```
{% endtab %}
{% endtabs %}

## 


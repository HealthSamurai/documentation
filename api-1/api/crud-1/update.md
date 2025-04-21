# Update

This interaction allows modifying an existing resource. If a resource with `id` (provided in the URL) doesn't exist, a new resource will be created.

```http
PUT [base]/[type]/[id]
```

After performing this interaction, the resource will be replaced with a new version of the resource provided in the body of the request.

{% hint style="info" %}
Once the resource is created, `id` of a resource can't be changed.
{% endhint %}

{% hint style="info" %}
Aidbox allows omitting `id` in the body. `id` in the body of the resource is ignored (in order to make a `conditional update` possible without knowing the logical id of the resource)
{% endhint %}

## Examples

### Update a patient by a given id

```
PUT /fhir/Patient/17b69d79-3d9b-45f8-af79-75f958502763

name: [{given: ["Bob"]}]
```

### Create a patient with a specified id

```
PUT /fhir/Patient/tom-id

name: [{given: ["Tom"]}]
```

### Update patient by name

```
PUT /fhir/Patient?name=Tom

name: [{given: ["Tom"]}]
gender: male
```

## Response codes

The following codes can be returned by the server:

| Response code | Text                     | Description                                                                      |
| ------------- | ------------------------ | -------------------------------------------------------------------------------- |
| **`200`**     | **OK**                   | Resource successfully updated                                                    |
| **`201`**     | **Created**              | Resource successfully created                                                    |
| **`422`**     | **Unprocessable Entity** | The proposed resource violated applicable FHIR profiles or server business rules |

## Conditional Update

```
PUT [base]/[type]?[search parameters]
```

This is a more complex way to update a resource, but it gives more power. You can update a resource without knowing the `id` , but it requires the knowledge of [Search](broken-reference). Different response codes will be returned (based on the number of search results):

* **No matches**: The server performs a `create` interaction
* **One Match**: The server performs the update against the matching resource
* **Multiple matches**: The server returns a `412 Precondition Failed` error indicating the client's criteria were not selective enough

In contrast to FHIR, Aidbox conditional update allows the creation of a resource with a specific `id`. In case of one match, `conditional update` ignores the id coming in the request body. That means that `id` can't be changed by any `update` operation.

### Example

Create a patient with the name Julie and specified id if no other patients with the same name exist:

```
PUT /fhir/Patient?name=Julie

id: julie-id
name: [{given: ["Julie"]}]
gender: female
```

{% hint style="info" %}
If a patient with the name Julie already exists, `update` will be performed and `id` will be ignored.
{% endhint %}

## **Versioned Update**

While you update, there is a risk of overriding the latest changes done by another operation. To escape this situation, you can use a versioned update by sending `If-Match` header with `versionId` of the resource you want to update. If the server has the same version of resources, the update will be successful. If versions do not match, you will get OperationOutcome with conflict code.

### Example

Let's say we created a patient:

```
POST /fhir/Patient

id: pt-1
name: [{family: 'Wrong'}]
```

To fix the family for this patient without the risk of overriding someone else's changes, we can use a versioned update request:

```
PUT /fhir/Patient/pt-id
If-Match: 30

name: [{family: ['Smith']}]
```

If someone has already edited the same patient, his version id was changed, and we got OperationOutcome.

<pre><code><strong>resourceType: OperationOutcome
</strong>id: 'conflict'
text:
  status: generated
  div: Version Id mismatch
issue:
  - severity: fatal
    code: conflict
    diagnostics: Version Id mismatch
</code></pre>

## Isolation levels

To prevent anomalies Aidbox uses `serializable` transaction isolation level by default. This may lead to `412` errors when you modify resources concurrently. Please refer to [the Postgres documentation](https://www.postgresql.org/docs/15/transaction-iso.html) to learn more about transaction isolation.

If you wish to use a lower isolation level, use the `x-max-isolation-level` header.

Allowed values are:

* `serializable`
* `repeatable-read`
* `read-commited`

Example:

```
PUT /fhir/Patient?name=Julie
x-max-isolation-level: repeatable-read

id: julie-id
```

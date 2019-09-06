# Delete

```
DELETE [base]/[type]/[id]
```

Respond with `200 OK` on successful delete, but on deletion of already deleted resource respond with `204 No Content` \(conforming FHIR specification\). This feature added to make delete work in the same way as SQL `DELETE RETURNING *`.

To get `204 No Content` instead of `200 OK` use `_no-content=true` query parameter. 

* **`200` OK** - resource successfully delete
* **`204` No Content** - resource already deleted
* **`404` Not Found** - resource not found



### Conditional Delete

```text
DELETE [base]/[type]?[search parameters]
```

It's not clear how to perform an ordinary `delete` on no matches, that's why `404 Not Found` will be returned in this case.

* **No matches:** The respond with `404 Not Found`
* **One Match**: The server performs an ordinary `delete` on the matching resource
* **Multiple matches**: Servers respond with `412 Precondition Failed` error indicating the client's criteria were not selective enough



## delete

```text
DELETE [base]/[type]/[id]
```

This interaction deletes a resource, responds with `200 OK` on a successful delete, but on deletion of an already deleted resource it responds with `204 No Content`. 

To get `204 No Content` always instead of `200 OK`, use `_no-content=true` query parameter.

* **`200`** **OK** — resource successfully delete
* **`204`** **No Content** — resource already deleted

### `200` OK

Delete a patient by id:

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

Attempt to delete an already deleted resource:

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

```text
DELETE [base]/[type]?[search parameters]
```

Depending on the number of resources meeting the search criteria, different actions will be performed and response codes will be returned:

* **No matches:** Respond with `404 Not Found`
* **One Match**: The server performs an ordinary `delete` on the matching resource
* **Multiple matches**: Servers respond with `412 Precondition Failed` error indicating the client's criteria were not selective enough


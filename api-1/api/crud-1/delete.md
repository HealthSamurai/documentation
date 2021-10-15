# Delete

```
DELETE [base]/[type]/[id]
```

Responds with `200 OK` on the successful deletion, but when removing a resource deleted earlier, responds with `204 No Content` (conforming FHIR specification). This feature was added to make deletion work the same way as in SQL `DELETE RETURNING *`.

Supports `If-Match` header, with `versionId` as ETAG.

To get `204 No Content` instead of `200 OK`, use the `_no-content=true` query parameter. 

* **`200` OK** - resource successfully deleted
* **`204` No Content** - resource already deleted
* **`404` Not Found** - resource not found
* **`412`** **Precondition Failed **- requested ETAG doesn't match actual

{% hint style="info" %}
Delete of non-existing resource will return 204 status and no body. Read [this thread](https://chat.fhir.org/#narrow/stream/179177-conformance/topic/Delete.20error.20codes) for more details.
{% endhint %}



### Conditional Delete

```
DELETE [base]/[type]?[search parameters]
```

It's not clear how to perform an ordinary `delete` on no matches. That's why `404 Not Found` will be returned in this case.

* **No matches: **The respond with `404 Not Found`
* **One Match**: The server performs an ordinary `delete` on the matching resource
* **Multiple matches**: Servers respond with `412 Precondition Failed` error indicating the client's criteria were not selective enough



## delete

```
DELETE [base]/[type]/[id]
```

This interaction deletes a resource, responds with `200 OK` on the successful deletion, but when removing an already deleted resource, it responds with `204 No Content`. 

To always get `204 No Content` instead of `200 OK`, use `_no-content=true` query parameter.

* **`200`** **OK** — resource successfully deleted
* **`204`** **No Content** — resource already deleted

### `200` OK

Delete a patient by id:

{% tabs %}
{% tab title="Request" %}
```
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
```
DELETE /Patient/tom-id
```
{% endtab %}

{% tab title="Response" %}
**Status:** `204`

```
```
{% endtab %}
{% endtabs %}

## conditional delete

```
DELETE [base]/[type]?[search parameters]
```

Depending on the number of resources meeting the search criteria, different actions will be performed and response codes will be returned:

* **No matches:** Respond with `404 Not Found`
* **One Match**: The server performs an ordinary `delete` on the matching resource
* **Multiple matches**: Servers respond with `412 Precondition Failed` error indicating the client's criteria were not selective enough

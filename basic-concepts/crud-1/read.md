# Read

```text
GET [base]/[type]/[id]
```

One of the most basic interactions used to obtain a resource by a given `id`. For more advanced options for getting resources check out [Search](../search-1/).

* **`200`** **OK** — resource successfully found and returned
* **`404`** **Not Found —** resource with a given `id` doesn't exist on the server
* **`410`** **Gone —** resource was deleted

### `200` OK

Get an existing patient:

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

Attempt to get a non-existing patient:

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

This is another read interaction but it returns a specific version resource. Similar to read but additionally requires to specify version id.

### `200` OK

Version id `13` was extracted from the response of a `create` interaction.

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

## 


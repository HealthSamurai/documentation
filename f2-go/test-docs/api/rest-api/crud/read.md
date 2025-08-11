# Read

```
GET [base]/[type]/[id]
```

One of the most basic interactions is used to obtain a resource by a given `id`. For more advanced options for getting resources, check out [Search](../fhir-search/README.md).

| Response code | Text          | Description                                            |
| ------------- | ------------- | ------------------------------------------------------ |
| **`200`**     | **OK**        | Resource successfully found and returned               |
| **`404`**     | **Not Found** | Resource with a given `id` doesn't exist on the server |
| **`410`**     | **Gone**      | Resource was deleted                                   |

### `200` OK

Get an existing patient:

{% tabs %}
{% tab title="Request (FHIR)" %}
```
GET /fhir/Patient/17b69d79-3d9b-45f8-af79-75f958502763
```
{% endtab %}

{% tab title="Request (Aidbox)" %}
```
GET /Patient/17b69d79-3d9b-45f8-af79-75f958502763
```
{% endtab %}

{% tab title="Response (FHIR)" %}
**Status:** `200`

```yaml
name:
- given: Bob
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

{% tab title="Response (Aidbox)" %}
**Status:** `200`

```yaml
name:
- given: Bob
gender: male
id: 17b69d79-3d9b-45f8-af79-75f958502763
resourceType: Patient
meta:
  lastUpdated: '2018-11-29T10:44:10.588Z'
  createdAt: '2018-11-29T10:44:10.588Z'
  versionId: '13'
```
{% endtab %}
{% endtabs %}

### `404` Not Found

Attempt to get a non-existing patient:

{% tabs %}
{% tab title="Request (FHIR)" %}
```
GET /fhir/Patient/some-not-existing-id
```
{% endtab %}

{% tab title="Request (Aidbox)" %}
```
GET /Patient/some-not-existing-id
```
{% endtab %}

{% tab title="Request (FHIR)" %}
**Status:** `404`

```yaml
resourceType: OperationOutcome
id: 'not-found'
text:
  status: generated
  div: Resource Patient/some-not-existing-id not found
issue:
  - severity: fatal
    code: not-found
    diagnostics: Resource Patient/some-not-existing-id not found
```
{% endtab %}

{% tab title="Response (Aidbox)" %}
**Status:** `404`

```yaml
resourceType: OperationOutcome
id: 'not-found'
text:
  status: generated
  div: Resource Patient/some-not-existing-id not found
issue:
  - severity: fatal
    code: not-found
    diagnostics: Resource Patient/some-not-existing-id not found
```
{% endtab %}
{% endtabs %}

## vread

```
GET [base]/[type]/[id]/_history/[vid]
```

This one is another read interaction, but it returns a specific version resource. Similar to read, but it additionally requires to specify version id.

### `200` OK

Version id `13` was extracted from the response of a `create` interaction.

{% tabs %}
{% tab title="Request (FHIR)" %}
```
GET /fhir/Patient/17b69d79-3d9b-45f8-af79-75f958502763/_history/13
```
{% endtab %}

{% tab title="Request (Aidbox)" %}
```
GET /Patient/17b69d79-3d9b-45f8-af79-75f958502763/_history/13
```
{% endtab %}

{% tab title="Response (FHIR)" %}
**Status:** `200`

```yaml
name:
  - given:
      - Bob
gender: male
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

{% tab title="Response (Aidbox)" %}
**Status:** `200`

```yaml
name:
  - given:
      - Bob
gender: male
id: '17b69d79-3d9b-45f8-af79-75f958502763'
resourceType: Patient
meta:
  lastUpdated: '2018-11-29T10:44:10.588Z'
  createdAt: '2018-11-29T10:44:10.588ZZ'
  versionId: '13'
```
{% endtab %}
{% endtabs %}

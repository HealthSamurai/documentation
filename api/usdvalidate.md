# $validate

## Introduction

The tool introduced by FHIR to provide a separate validation mechanism for 2 step commit workflow or for development needs. It works for create, update and delete operations, calls using `?mode=` query parameter with values `create`, `update`, `delete`, but changes won't be committed, instead a requester will get an `OperationOutcome` with information about validation results. 

```text
POST [base]/[type]/$validate
POST [base]/[type]/[id]/$validate
```

Such requests check resource structure, internal business rules and returns a list of problems if some exist.

* **`200`** **OK** — those request always returns status 200

Success and failure of validation request is determined by `id` of `OperationOutcome` resource. `allok` and `validationfail` is self-descriptive.

## Examples

### Validation success

Request contains valid `Patient` resource inside body.

{% tabs %}
{% tab title="Request" %}
```yaml
POST /Patient/$validate?mode=create

name: []
```
{% endtab %}

{% tab title="Response" %}
**Status:** `200`

```yaml
id: allok
resourceType: OperationOutcome
```
{% endtab %}
{% endtabs %}

### Validation failure

Patient name must be an array, `test` is non-existing field.

{% tabs %}
{% tab title="Request" %}
```yaml
POST /Patient/example/$validate?mode=update

name: "Bob"
test: value
```
{% endtab %}

{% tab title="Response" %}
**Status:** `200`

```yaml
id: validationfail
resourceType: OperationOutcome
errors:
- path: [name]
  message: expected array
- path: [test]
  message: extra property
warnings: []
```
{% endtab %}
{% endtabs %}


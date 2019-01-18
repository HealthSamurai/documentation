# $validate

## Introduction

The tool introduced by FHIR to provide a separate validation mechanism for 2-steps commit workflow or for development needs. It works for create, update and delete operations, is called using `?mode=` query parameter with values `create`, `update`, `delete` but changes won't be committed, instead a requester will get an `OperationOutcome` with information about validation results. See [http://hl7.org/fhir/resource-operation-validate.html](http://hl7.org/fhir/resource-operation-validate.html) for the official documentation. 

```text
POST [base]/[type]/$validate
POST [base]/[type]/[id]/$validate
```

Such requests check resource structure, internal business rules and return a list of problems if some exist.

* **`200`** **OK** — those requests always return status 200

Success and failure of a validation request is determined by `id` of `OperationOutcome` resource. `allok` and `validationfail` are self-descriptive.

## Examples

### Validation Success

Request contains valid `Patient` resource inside the body.

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

### Validation Failure

Patient name must be an array, `test` is a non-existing field.

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


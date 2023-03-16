---
description: >-
  This article shows how to create an access policy with restricted set of
  operations on Patient resource
---

# Restrict operations on resource type

### Create a patient

```yaml
POST /Patient

id: pt-1
resourceType: Patient
name:
  - given:
      - John
```

### Create an access policy

Create an access policy which allows only `[Fhir]Read` operation on Patient.

```yaml
POST /AccessPolicy

engine: matcho
matcho:
  operation:
    id:
      $enum: [Read, FhirRead]
  params:
    resource/type: Patient
```

### Try it

#### Create

We won't be able to create new patient because we're not allowed to use `[Fhir]Create` operation

{% tabs %}
{% tab title="Request" %}
```yaml
POST /Patient

id: pt-2
resourceType: Patient
name:
  - given:
      - Jane
```
{% endtab %}

{% tab title="Response" %}
```yaml
resourceType: OperationOutcome
id: forbidden
text:
  status: generated
  div: Forbidden
issue:
- severity: fatal
  code: forbidden
  diagnostics: Forbidden
```
{% endtab %}
{% endtabs %}

#### Read

`[Fhir]Read` operation works as expected and you are able to read previously created patient

{% tabs %}
{% tab title="Request" %}
```yaml
GET /Patient/pt-1
```
{% endtab %}

{% tab title="Response" %}
```yaml
name:
- given: [John]
id: pt-1
resourceType: Patient
```
{% endtab %}
{% endtabs %}

### What's going on here

When you make a query

```
GET /{resource-type}/{resource-id}
```

Aidbox router stores data in the request object:

* Reference to the `Read` operation in the `operation` property.
* Resource type `Patient` from the url to the `params.recource/type` property.

Access policy engine evaluates request object. And here it checks that `operation.id` property contains either `Read` or `FhirRead` string.

You can inspect request object [using `__debug` query parameter](../how-to-guides/debug.md#\_\_debug-query-string-parameter).

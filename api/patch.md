---
description: With patch operation you can update only part of FHIR resource.
---

# Patch

For most of Operations in FHIR you manipulate by resource as a whole \(create, update etc\). But sometimes you want to update specific data elements in resource and do not care about others. I.e. you  need element/attribute level operation. 

With `Patch` operation you can update part of resource by sending declarative description of operations, which should be performed on existing resource. To describe this operations in aidbox you can use different notations \(methods\):

* merge-patch  - the simple merge semantic \([read more in RFC](https://tools.ietf.org/html/rfc7386)\)
* json-patch - advanced json transformation \([read more in RFC](https://tools.ietf.org/html/rfc6902)\)

FHIR and aidbox team also working on brand new FHIR patch method - [https://www.hl7.org/fhir/fhirpatch.html](https://www.hl7.org/fhir/fhirpatch.html) - which is currently in it's early draft.

### Patch method

You can specify patch method by `content-type` header or by `_method` parameter.

| method | parameter | header |
| :--- | :--- | :--- |
| **json-patch** | json-patch | application/json-patch+json |
| **merge-patch** | merge-patch | application/merge-patch+json |
| **fhir-patch** | fhir-patch | application/fhir-patch+json |

If method is not specified, aidbox will try to guess it by following algorithm: 

* if payload is array - `json-merge`
* if payload is object with `resourceType = 'Parameter'` - `fhir-patch` 
* else  `merge-patch`

### Operation Description

{% api-method method="patch" host="\[base-url\]/:resourceType/:id" path="" %}
{% api-method-summary %}
Patch Operation
{% endapi-method-summary %}

{% api-method-description %}

{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-path-parameters %}
{% api-method-parameter name="\_method" type="string" required=false %}
can be json-patch, merge-patch \(and fhir-patch in a future\)
{% endapi-method-parameter %}
{% endapi-method-path-parameters %}

{% api-method-headers %}
{% api-method-parameter name="content-type" type="string" required=false %}
see content-type header in table above.
{% endapi-method-parameter %}
{% endapi-method-headers %}

{% api-method-body-parameters %}
{% api-method-parameter name="" type="string" required=false %}
JSON or YAML representation of transformation rules in accordance with \_method
{% endapi-method-parameter %}
{% endapi-method-body-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}

{% endapi-method-response-example-description %}

```
Updated resource
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

### Example

{% hint style="info" %}
You can exercise this tutorial using [REST Console](../aidbox-ui/rest-console.md) - just copy paste queries into console!
{% endhint %}

Let's suppose we've created Patient resource with id `pt-1`

```yaml
POST /Patient

resourceType: Patient
id: pt-1
active: true
name:
  - given: ['John']
    family: Doe
    use: official
  - given: ['Johny']
    family: Doe
telecom:
  -  system: phone
     value: '(03) 5555 6473'
     use: work
     rank: 1
birthDate: '1979-01-01'
```

{% hint style="info" %}
You can copy paste this request into REST console of Aidbox.Cloud
{% endhint %}

### Merge patch

Let's say, we want to switch active flag to false and remove telecom:

```yaml
PATCH /Patient/pt-1?_method=merge-patch

active: false
telecom: null
```

```yaml
# response 200
resourceType: Patient
id: pt-1
active: false
name:
  - given: ['John']
    family: Doe
    use: official
  - given: ['Johny']
    family: Doe
birthDate: '1979-01-01'

```

### JSON patch

With JSON patch we can do more sophisticated transformations - change family name in first name and delete second name:

```yaml
PATCH /Patient/pt-1

- op: replace
  path: '/name/0/given/0'
  value: Nikolai
- op: remove
  path: '/name/1'
- op: replace
  path: '/active'
  value: true
```

```yaml
# response 200
resourceType: Patient
id: pt-1
name:
- use: official
  given: [John]
  family: Jackson
active: true
birthDate: '1979-01-01'

```

### FHIR patch

We are working on FHIR path specification and implementation.

{% hint style="warning" %}
In development...
{% endhint %}


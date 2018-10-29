---
description: With patch operation you can update a part of FHIR resource.
---

# Patch

All examples can be run in Postman. Here's [web view](https://documenter.getpostman.com/view/5552124/RWgxtEs8) of these examples.

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/view-collection/f6bc1ce7c9eeb0c2baa0?referrer=https%3A%2F%2Fapp.getpostman.com%2Frun-collection%2Ff6bc1ce7c9eeb0c2baa0%23%3Fenv%5BAidbox.Cloud%5D%3DW3sia2V5IjoiYmFzZSIsInZhbHVlIjoiaHR0cHM6Ly9tZXJlZGl0aC5haWRib3guYXBwIiwiZGVzY3JpcHRpb24iOiIiLCJlbmFibGVkIjp0cnVlfV0%3D&_ga=2.109779141.1133756186.1540376522-1595564802.1538573158)

For most of Operations in FHIR you manipulate a resource as a whole \(create, update, delete operations\). But sometimes you want to update specific data elements in a resource and do not care about the rest. In other words, you need an element/attribute level operation. 

With the `patch` operation you can update a part of resource by sending a declarative description of operations which should be performed on an existing resource. To describe this operations in Aidbox you can use different notations \(methods\):

* merge-patch — simple merge semantics \([read more in RFC](https://tools.ietf.org/html/rfc7386)\);
* json-patch — advanced JSON transformation \([read more in RFC](https://tools.ietf.org/html/rfc6902)\);

### Patch Method

You can specify a `patch` method by the `content-type` header or by the `_method` parameter.

| method | parameter | header |
| :--- | :--- | :--- |
| **json-patch** | `json-patch` | application/json-patch+json |
| **merge-patch** | `merge-patch` | application/merge-patch+json |

If method is not specified, Aidbox will try to guess it by the following algorithm: 

* if the payload is an array — `json-merge`
* else `merge-patch`

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
Can be `json-patch`, `merge-patch` \(and `fhir-patch` in the future\)
{% endapi-method-parameter %}
{% endapi-method-path-parameters %}

{% api-method-headers %}
{% api-method-parameter name="content-type" type="string" required=false %}
See the `content-type` header in the table above
{% endapi-method-parameter %}
{% endapi-method-headers %}

{% api-method-body-parameters %}
{% api-method-parameter name="" type="string" required=false %}
JSON or YAML representation of transformation rules in accordance with `_method`
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
You can exercise this tutorial using [REST Console](../tutorials/rest-console.md) — just copy/paste queries into console!
{% endhint %}

Let's suppose we've created a Patient resource with the id `pt-1`

{% tabs %}
{% tab title="Request" %}
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
{% endtab %}

{% tab title="Response" %}
```yaml
id: pt-1
resourceType: Patient
name:
- use: official
  given:
  - John
  family: Doe
- given:
  - Johny
  family: Doe
active: true
telecom:
- use: work
  rank: 1
  value: "(03) 5555 6473"
  system: phone
birthDate: '1979-01-01'
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
You can copy/paste this request into REST Console of Aidbox.Cloud.
{% endhint %}

### Merge Patch

Let's say we want to switch `active` flag to false and remove `telecom`:

{% tabs %}
{% tab title="Request" %}
```yaml
PATCH /Patient/pt-1?_method=merge-patch

active: false
telecom: null
```
{% endtab %}

{% tab title="Response" %}
```yaml
# response 200

id: pt-1
resourceType: Patient
name:
- use: official
  given:
  - John
  family: Doe
- given:
  - Johny
  family: Doe
active: false
birthDate: '1979-01-01'
```
{% endtab %}
{% endtabs %}

### JSON Patch

With JSON patch, we can do more sophisticated transformations — change first `given` name, delete second `name`, and change the `active` attribute value to `true`:

{% tabs %}
{% tab title="Request" %}
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
{% endtab %}

{% tab title="Response" %}
```yaml
# response 200

id: pt-1
resourceType: Patient
name:
- use: official
  given:
  - Nikolai
  family: Doe
active: true
birthDate: '1979-01-01'
```
{% endtab %}
{% endtabs %}




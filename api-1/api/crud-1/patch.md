---
description: Updating a part of your resource
---

# Patch

All examples can be run in Postman. Here's a [web view](https://documenter.getpostman.com/view/5552124/RWgxtEs8) of these examples.

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/view-collection/f6bc1ce7c9eeb0c2baa0?referrer=https%3A%2F%2Fapp.getpostman.com%2Frun-collection%2Ff6bc1ce7c9eeb0c2baa0%23%3Fenv%5BAidbox.Cloud%5D%3DW3sia2V5IjoiYmFzZSIsInZhbHVlIjoiaHR0cHM6Ly9tZXJlZGl0aC5haWRib3guYXBwIiwiZGVzY3JpcHRpb24iOiIiLCJlbmFibGVkIjp0cnVlfV0%3D&\_ga=2.109779141.1133756186.1540376522-1595564802.1538573158)

In most Operations in FHIR, you manipulate a resource as a whole (create, update, delete operations). But sometimes you want to update specific data elements in a resource and do not care about the rest. In other words, you need an element/attribute level operation.&#x20;

With the `patch` operation, you can update a part of a resource by sending a declarative description of operations that should be performed on an existing resource. To describe these operations in Aidbox, you can use different notations (methods):

* merge-patch — simple merge semantics ([read more in RFC](https://tools.ietf.org/html/rfc7386));
* json-patch — advanced JSON transformation ([read more in RFC](https://tools.ietf.org/html/rfc6902));

### Patch Method

You can specify a `patch` method by the `content-type` header or by the `_method` parameter.

| method          | parameter     | header                       |
| --------------- | ------------- | ---------------------------- |
| **json-patch**  | `json-patch`  | application/json-patch+json  |
| **merge-patch** | `merge-patch` | application/merge-patch+json |

If the method is not specified, Aidbox will try to guess it by the following algorithm:&#x20;

* if the payload is an array — `json-patch`
* else `merge-patch`

### Operation Description

{% swagger baseUrl="[base-url]/:resourceType/:id" path="" method="patch" summary="Patch Operation" %}
{% swagger-description %}

{% endswagger-description %}

{% swagger-parameter in="path" name="_method" type="string" %}
Can be 

`json-patch`

, 

`merge-patch`

 (and 

`fhir-patch`

 in the future)
{% endswagger-parameter %}

{% swagger-parameter in="header" name="content-type" type="string" %}
See the 

`content-type`

 header in the table above
{% endswagger-parameter %}

{% swagger-parameter in="body" name="" type="string" %}
JSON or YAML representation of transformation rules in accordance with 

`_method`
{% endswagger-parameter %}

{% swagger-response status="200" description="" %}
```
Updated resource
```
{% endswagger-response %}
{% endswagger %}

### Example

{% hint style="info" %}
You can exercise this tutorial using [REST Console](../../../overview/aidbox-ui/rest-console-1.md) — just copy/paste queries into your console!
{% endhint %}

Let's suppose we've created a Patient resource with the id `pt-1`

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

# 200

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

{% hint style="info" %}
You can copy/paste this request into REST Console of Aidbox.Cloud.
{% endhint %}

### Merge Patch

Let's say we want to switch an `active` flag to false and remove `telecom`:

```yaml
PATCH /Patient/pt-1?_method=merge-patch

active: false
telecom: null

# 200

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

### JSON Patch

With JSON patch, we can do more sophisticated transformations — change the first `given` name, delete the second `name`, and change the `active` attribute value to `true`:

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
  
# 200

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


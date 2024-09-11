---
description: Updating a part of your resource
---

# Patch

In most Operations in FHIR, you manipulate a resource as a whole (create, update, delete operations). But sometimes you want to update specific data elements in a resource and do not care about the rest. In other words, you need an element/attribute level operation.

With the `patch` operation, you can update a part of a resource by sending a declarative description of operations that should be performed on an existing resource. To describe these operations in Aidbox, you can use different notations (methods):

* merge-patch — simple merge semantics ([read more in RFC](https://tools.ietf.org/html/rfc7386));
* json-patch — advanced JSON transformation ([read more in RFC](https://tools.ietf.org/html/rfc6902));

### Patch Method

You can specify a `patch` method by the `content-type` header or by the `_method` parameter.

| method          | parameter     | header                       |
| --------------- | ------------- | ---------------------------- |
| **json-patch**  | `json-patch`  | application/json-patch+json  |
| **merge-patch** | `merge-patch` | application/merge-patch+json |

If the method is not specified, Aidbox will try to guess it by the following algorithm:

* if the payload is an array — `json-patch`
* else `merge-patch`

### Operation Description

## Patch Operation

<mark style="color:purple;">`PATCH`</mark> `[base-url]/<resourceType>/<id>`

#### Path Parameters

| Name     | Type   | Description                                                                   |
| -------- | ------ | ----------------------------------------------------------------------------- |
| \_method | string | <p></p><p><code>json-patch</code></p><p>or</p><p><code>merge-patch</code></p> |

#### Headers

| Name         | Type   | Description                                                                    |
| ------------ | ------ | ------------------------------------------------------------------------------ |
| content-type | string | <p>See the</p><p><code>content-type</code></p><p>header in the table above</p> |

#### Request Body

JSON or YAML representation of transformation rules in accordance with`_method`

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

# 200 OK
```

{% hint style="info" %}
You can copy/paste this request into REST Console of Aidbox.Cloud.
{% endhint %}

### Merge Patch

Let's say we want to switch to an `active` flag to false and remove `telecom`:

```yaml
PATCH /Patient/pt-1

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
```

Response:

```
# 200 OK
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

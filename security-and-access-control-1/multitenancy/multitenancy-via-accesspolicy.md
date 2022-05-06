---
description: Build Multitenancy with AccessPolicy
---

# 🎓 Multitenancy via AccessPolicy

Aidbox stores all the tenants in a single database and serves number of them at once. It obtains `tenant-id` from each request and returns the data belonging to the tenant.

### Key concepts

* All resources have to be created with the `tenant-id`
* `tenant id` is stored within the `identifier` attribute at the resource
* Access policies require `tenant-id` parameter in each request

### Multitenancy setup and requests example

#### Add `Client`

There are several ways to create client. We use the simplest one to do it: [Basic Auth](https://docs.aidbox.app/security-and-access-control-1/auth/basic-auth).

```yaml
PUT /Client/org1
Accept: text/yaml
Content-Type: text/yaml

id: org1
secret: secret
grant_types:
  - basic
```

#### Add `org-1` tenant resource

Create patient providing it's `tenant-id` in the `identifier` property.

```yaml
PUT /Patient/test-patient-1
Content-Type: text/yaml

id: test-patient-1
identifier:
- system: tenantId
  value: org1
name:
- given:
  - John
gender: male
```

#### Define `AccessPolicy` for multi-tenancy

Create access policy to ensure `tenant-id` is provided in all requests.

```yaml
PUT /AccessPolicy/org1-patient-policy
Content-Type: text/yaml

link:
- id: org1
  resourceType: Client
engine: matcho
matcho:
  "$one-of":
  - request-method: get
    params:
      identifier:
        "$one-of":
        - tenantId|org1
        - "$contains": tenantId|org1
  - request-method: post
    body:
      identifier:
        "$contains":
          value: org1
          system: tenantId
  - request-method: put
    params:
      identifier:
        "$one-of":
        - tenantId|org1
        - "$contains": tenantId|org1
    body:
      identifier:
        "$contains":
          value: org1
          system: tenantId
  - request-method: delete
    params:
      identifier:
        "$one-of":
        - tenantId|org1
        - "$contains": tenantId|org1
```

#### Multi-tenant request examples

Search the `Patient` with the correct `tenant-id` returns the resource.

{% tabs %}
{% tab title="Request" %}
```http
GET /Patient?identifier=tenantId|org1&_id=test-patient-1
```
{% endtab %}

{% tab title="Response" %}
{% code title="Status: 200" %}
```yaml
id: test-patient-1
identifier:
- system: tenantId
  value: org1
name:
- given:
  - John
gender: male
```
{% endcode %}
{% endtab %}
{% endtabs %}

Update the `Patient` resource.

{% tabs %}
{% tab title="Request" %}
```yaml
PUT /Patient?identifier=tenantId|org1&_id=test-patient-1
Content-Type: text/yaml

identifier:
- system: other
  value: foo
- system: tenantId
  value: org1
name:
- given:
  - John
gender: male
```
{% endtab %}

{% tab title="Response" %}
{% code title="Status: 200" %}
```yaml
identifier:
- system: other
  value: foo
- system: tenantId
  value: org1
name:
- given:
  - John
gender: male
```
{% endcode %}
{% endtab %}
{% endtabs %}

Read updated `Patient` resource.&#x20;

{% tabs %}
{% tab title="Request" %}
```http
GET /Patient?identifier=tenantId|org1&identifier=other|foo
```
{% endtab %}

{% tab title="Response" %}
{% code title="Status: 200" %}
```yaml
identifier:
- system: other
  value: foo
- system: tenantId
  value: org1
name:
- given:
  - John
gender: male
```
{% endcode %}
{% endtab %}
{% endtabs %}

Delete `Patient` resource.

{% tabs %}
{% tab title="Request" %}
```http
DELETE /Patient?_id=test-patient-1&identifier=tenantId|org1
```
{% endtab %}

{% tab title="Resoponse" %}
{% code title="Status: 200" %}
```
// empty body
```
{% endcode %}
{% endtab %}
{% endtabs %}

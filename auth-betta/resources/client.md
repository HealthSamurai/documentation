---
description: >-
  Clients represent applications (agents) interacting with API on behalf of the
  user or yourself.
---

# Client

Here is a list of key attributes for Client:

| Attribute | Description |
| :--- | :--- |
| **id** | Unique id of client |
| **secret** | Secret used to confirm client identity in Basic Auth or Authorization Code Flows for example |
| **grant\_types** | List of allowed ways to get access to API: basic, code, implicit, password, client\_credentials |

### First Party

Sometimes there is one major application on top of your API. 

Clients with attribute`.first_party = true` 

### Basic Auth 

{% code-tabs %}
{% code-tabs-item title="basic-client.yaml" %}
```yaml
resourceType: Client
grant_types: ['basic']
```
{% endcode-tabs-item %}
{% endcode-tabs %}

### OAuth 2.0: Client Credentials

{% code-tabs %}
{% code-tabs-item title="client-credentials" %}
```yaml
resourceType: Client
grant_types: ['basic']

```
{% endcode-tabs-item %}
{% endcode-tabs %}

### OAuth 2.0: User Password

{% code-tabs %}
{% code-tabs-item title="user-password" %}
```yaml
resourceType: Client
id: password-client
grant_types: ['basic']

```
{% endcode-tabs-item %}
{% endcode-tabs %}

### OAuth 2.0: Authorization Code

{% code-tabs %}
{% code-tabs-item title="auth-code-client" %}
```yaml
resourceType: Client
id: code-client
grant_types: ['code']
```
{% endcode-tabs-item %}
{% endcode-tabs %}

### OAuth 2.0: Implicit

{% code-tabs %}
{% code-tabs-item title="implicit-client" %}
```yaml
resourceType: Client
id: implicit-client
grant_types: ['implicit']

```
{% endcode-tabs-item %}
{% endcode-tabs %}

### SMART on FHIR

{% code-tabs %}
{% code-tabs-item title="smart-client" %}
```yaml
resourceType: Client
id: smart-client
grant_types: ['basic']

```
{% endcode-tabs-item %}
{% endcode-tabs %}


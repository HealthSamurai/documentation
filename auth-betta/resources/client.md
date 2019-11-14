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

{% code title="basic-client.yaml" %}
```yaml
resourceType: Client
grant_types: ['basic']
```
{% endcode %}

### OAuth 2.0: Client Credentials

{% code title="client-credentials" %}
```yaml
resourceType: Client
grant_types: ['basic']

```
{% endcode %}

### OAuth 2.0: User Password

{% code title="user-password" %}
```yaml
resourceType: Client
id: password-client
grant_types: ['basic']

```
{% endcode %}

### OAuth 2.0: Authorization Code

{% code title="auth-code-client" %}
```yaml
resourceType: Client
id: code-client
grant_types: ['code']
```
{% endcode %}

### OAuth 2.0: Implicit

{% code title="implicit-client" %}
```yaml
resourceType: Client
id: implicit-client
grant_types: ['implicit']

```
{% endcode %}

### SMART on FHIR

{% code title="smart-client" %}
```yaml
resourceType: Client
id: smart-client
grant_types: ['basic']

```
{% endcode %}


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

{% tabs %}
{% tab title="basic-client.yaml" %}
```yaml
resourceType: Client
grant_types: ['basic']
```
{% endtab %}
{% endtabs %}

### OAuth 2.0: Client Credentials

{% tabs %}
{% tab title="client-credentials" %}
```yaml
resourceType: Client
grant_types: ['basic']

```
{% endtab %}
{% endtabs %}

### OAuth 2.0: User Password

{% tabs %}
{% tab title="user-password" %}
```yaml
resourceType: Client
id: password-client
grant_types: ['basic']

```
{% endtab %}
{% endtabs %}

### OAuth 2.0: Authorization Code

{% tabs %}
{% tab title="auth-code-client" %}
```yaml
resourceType: Client
id: code-client
grant_types: ['code']
```
{% endtab %}
{% endtabs %}

### OAuth 2.0: Implicit

{% tabs %}
{% tab title="implicit-client" %}
```yaml
resourceType: Client
id: implicit-client
grant_types: ['implicit']

```
{% endtab %}
{% endtabs %}

### SMART on FHIR

{% tabs %}
{% tab title="smart-client" %}
```yaml
resourceType: Client
id: smart-client
grant_types: ['basic']

```
{% endtab %}
{% endtabs %}


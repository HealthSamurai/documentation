---
description: Serce to service integration
---

# Basic Auth

## Basic Auth

The simplest way to programmatically access your box API is to use [Basic Authentication](https://tools.ietf.org/html/rfc7617) with your Client.id and Client.secret . To do this you have to register Client and enable basic authentication by including "basic" into `grant_types` property:

```yaml
POST /Client

id: basic
secret: secret
grant_types:
  - basic
```

Next step is to configure access policy for this client. In a simple scenario you can create dedicated AccessPolicy for this client \(read more about [Access Policies\)](../security/access-control.md). 

```yaml
POST /AccessPolicy

id: for-basic
link:
  - resourceType: Client
    id: basic
engine: allow
```

Now you can make HTTP requests with Authorization header set to `base64("${client.id}:$(client.secret)"):`

{% code-tabs %}
{% code-tabs-item title="basic-request" %}
```yaml
GET /Patient
Authorization: Basic Basic YmFzaWM6c2VjcmV0Cg==
```
{% endcode-tabs-item %}
{% endcode-tabs %}

or use http client built-in support for Basic Auth:

```bash
curl -u basic:secret https://yourbox/Patient
curl -H 'Authorization: Basic YmFzaWM6c2VjcmV0Cg==' https://yourbox/Patient
```

### Test Basic in Auth Sandbox

{% embed url="https://www.youtube.com/watch?v=xWtNNi\_Q-dU" %}




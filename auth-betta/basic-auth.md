---
description: Access Aidbox API from your Service
---

# Basic Auth

## Basic Auth

The simplest way to programmatically interact with  Aidbox API is to use [Basic Access Authentication](https://tools.ietf.org/html/rfc7617). In this scheme you provide client credentials with every HTTP request in special header  - `Authorization: Basic <credentials>`,  where `<credentials>` is the base64 encoding of Client.id and Client.secret joined by a colon:

```yaml
GET /Patient
Authorization: Basic {base64(Client.id + ':' + Client.secret)}
```

### Register Client

The first step is to create resource [Client](resources/client.md) with id & secret and add `'basic'` to it's `grant_types` collection:

```yaml
POST /Client

id: basic
secret: secret
grant_types: ['basic']
```

By default your client does not have any permissions to access Aidbox REST API. So you probably want to configure some. Access Policy can be _linked_ to specific client, by providing reference to clients in `link` collection. For more sophisticated configuration see [Access Policies](../security/access-control.md). 

```yaml
POST /AccessPolicy

id: api-clients
engine: allow # which means it has permisions for everything
description: Root access to specific clients
link:
  # link policy with client
  - resourceType: Client
    id: basic # client.id 

```

### Making Requests with Basic Auth

Now you can make HTTP requests with Authorization header set to `Basic base64(client.id +":" + client.secret):`

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

### 

### Test Basic in Auth Sandbox

{% embed url="https://www.youtube.com/watch?v=xWtNNi\_Q-dU" %}




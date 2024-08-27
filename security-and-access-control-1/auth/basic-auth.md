---
description: Access Aidbox API from your Service
---

# Basic Auth

The simplest way to programmatically interact with Aidbox API is to use [Basic Access Authentication](https://tools.ietf.org/html/rfc7617). In this scheme you provide client credentials with every HTTP request in the special header - `Authorization: Basic <credentials>`, where `<credentials>` is the base64 encoding of Client.id and Client.secret joined by a colon:

```yaml
GET /Patient
Accept: text/yaml
Authorization: Basic {base64(Client.id + ':' + Client.secret)}
```

### Easy way

The easiest way to test Basic Auth is to run through the [Aidbox Sandbox UI](./#auth-sandbox) (_Auth -> Sandbox -> Basic Auth_).

<figure><img src="../../.gitbook/assets/image (2).png" alt=""><figcaption><p>Sandbox UI</p></figcaption></figure>

### Register Client

The first step is to create resource [Client](../../modules-1/security-and-access-control/readme-1/overview.md) with id & secret and add `'basic'` to it's `grant_types` collection:

```yaml
POST /Client
Accept: text/yaml
Content-Type: text/yaml

id: basic
secret: secret
grant_types:
  - basic
```

By default, your client does not have any permissions to access Aidbox REST API. So you probably want to configure some using Aidbox Access Policy. Access Policy can be _linked_ to the specific client by providing the reference to clients in `link` collection. For more sophisticated configuration, see [Access Policies](../../modules-1/security-and-access-control/security/access-control.md) documentation.

```yaml
POST /AccessPolicy
Accept: text/yaml
Content-Type: text/yaml

id: api-clients
engine: allow # which means it has permisions for everything
description: Root access to specific clients
link:
  # link policy with client
  - resourceType: Client
    id: basic # client.id 

```

### Making Requests with Basic Auth

Now you can make HTTP requests with `Authorization` header set to `'Basic ' + base64(client.id +':' + client.secret):`

{% code title="basic-request" %}
```yaml
GET /Patient
Accept: text/yaml
Authorization: Basic YmFzaWM6c2VjcmV0Cg==
```
{% endcode %}

Example with curl:

```bash
curl -u basic:secret https://yourbox/Patient
curl -H 'Authorization: Basic YmFzaWM6c2VjcmV0Cg==' https://yourbox/Patient
```

Most HTTP clients will do `Authorization` header construction for you:

{% code title="js-example" %}
```javascript
axios.get('<box>/Patient', {
  auth: {
    username: client.id,
    password: client.secret
  }
}).then(function(response) {
   console.log('Authenticated');
}).catch(function(error) {
   console.log('Error on Authentication');
});

// or you can always do it by manualy set headers
fetch('<box>/Patient', {
   headers: {"Authorization": 'Basic ' + btoa(client.id + ':' + client.secret)}
}).then(resp) { ... }

```
{% endcode %}

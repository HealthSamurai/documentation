---
description: Serce to service integration
---

# API Consumer

## Basic Auth

The simplest way to programmatically access your box API is to use [Basic Authentication](https://tools.ietf.org/html/rfc7617) with your Client.id and Client.secret . To do this you have to register Client and enable basic authentication by including "basic" into `grant_types` property:

```yaml
POST /Client

id: api-consumer
secret: verysecretstring
grant_types:
  - basic
```

Next step is to configure access policy for this client. In a simple scenario you can create dedicated AccessPolicy for this client \(read more about Access Policies\). Let's create policy for our client to only allow read patients and encounters:

```yaml
POST /AccessPolicy

id: for-api-consumer
link:
  - resourceType: Client
    id: api-consumer
engine: json-schema
schema:
  type: object
  properties:
    request-method: { constant: 'get' }
    uri: { enum: ['/Patient', 'Encounter'] }
```

Now you can make HTTP requests with Authorization header set to `base64("${client.id}:$(client.secret)")`

or use http client built-in support for Basic Auth:

```bash
curl -u $client_id:$client_secret https://yourbox/Patient
```

## OAuth 2.0 Client Credentials

If you need more granular control over client session you can use [ OAuth 2.0 Client Credentials Flow](https://tools.ietf.org/html/rfc6749#section-1.3.4) to get access token by Client.id and secret and use it for API calls.

![](../../.gitbook/assets/auth-sequence-m2m-flow.png)

Register client with client\_credentials enabled:

```yaml
POST /Client

id: api-consumer
secret: verysecretstring
grant_types:
  - client_credentials
auth:
  client_credentials:
    access_token_expiration: 3600 # in seconds
    access_token_format: jwt
```

Get access\_token

```yaml
POST /auth/token

client_id: api-consumer
client_secret: verysecretstring
grant_type: client_credentials
# optional
audience: https://another-service
```

Parameters can be send in body in any format supported by Aidbox or `application/x-www-form-urlencoded`

{% code-tabs %}
{% code-tabs-item title="response" %}
```yaml
access_token: 
token_type: bearer
expires_in: 3600
```
{% endcode-tabs-item %}
{% endcode-tabs %}

Access Token is always JWT with session id, client id, expiration time \(if enabled\) and optional audience.


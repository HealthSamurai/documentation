# Client Credentials Grant

### Description

The Client Credentials grant is used when applications request an access token to access their own resources, not on behalf of a user \(for example, background services and daemons\). It must be used only by confidential clients.

![Basic scheme](../.gitbook/assets/untitled-diagram.svg)

[Aidbox](https://www.health-samurai.io/aidbox) OAuth module support Client Credentials Grant flow in different formats. First- Strict adherence to specifications for better compatibility. Second - JSON request as a more modern and simple way. Also we have availability send all data in POST Body. Read official [OAuth2.0 specification](https://tools.ietf.org/html/rfc6749#section-4.4) for more details.

### Configure Client

To start use this flow you have to create  and configure Client. Minimal required parameters is `secret` and you have to enable this flow for client by `grant_types: ['client_credentials']`

{% code-tabs %}
{% code-tabs-item title="create-client-request" %}
```yaml
PUT /Client/api-client

secret: verysecret
grant_types:
  - client_credentials
```
{% endcode-tabs-item %}
{% endcode-tabs %}

You also can configure token format and expiration, as well refresh token:

| attribute | options | desc |
| :--- | :--- | :--- |
| _auth.client\_credentials_**.token\_format** | jwt | use access token in jwt format |
| _auth.client\_credentials_**.token\_expiration** | int \(seconds\) | token expiration time from issued at |
| _auth.client\_credentials_**.refresh\_token** | true/false | enable refresh\_token |

{% code-tabs %}
{% code-tabs-item title="jwt-token-client" %}
```yaml
PUT /Client/api-client

secret: verysecret
grant_types:
  - client_credentials
auth:
  client_credentials:
    access_token_expiration: 600
    token_format: jwt
    refresh_token: true
```
{% endcode-tabs-item %}
{% endcode-tabs %}

Probably you want to configure AccessPolicy for this specific client:

{% code-tabs %}
{% code-tabs-item title="policy" %}
```yaml
PUT AccessPolicy/api-client

engine: allow
link:
  - id: api-client
    resourceType: Client
```
{% endcode-tabs-item %}
{% endcode-tabs %}

### Get Access Token

Next step is exchange client id and secret for  Access Token.

 Using Basic & form-url-encode:

{% code-tabs %}
{% code-tabs-item title="using-basic" %}
```yaml
POST /auth/token
Authorization: Basic base64(client.id, client.secret)
Content-Type: application/x-www-form-urlencoded

grant_type=client_credentials
```
{% endcode-tabs-item %}
{% endcode-tabs %}

Or by JSON request:

{% code-tabs %}
{% code-tabs-item title="json-request" %}
```yaml
POST /auth/token
Content-Type: application/json

{
  "grant_type": "client_credentials",
  "client_id": "api-client",
  "client_secret": "secret"
}
```
{% endcode-tabs-item %}
{% endcode-tabs %}

For simple client configuration case you will get JSON with access\_token in response:

{% code-tabs %}
{% code-tabs-item title="token-response" %}
```yaml
status: 200

{
 "token_type": "Bearer",
 "access_token": "ZjQyNGFhY2EtNTY2MS00NjVjLWEzYmEtMjIwYjFkNDI5Yjhi"
}
```
{% endcode-tabs-item %}
{% endcode-tabs %}

For JWT with refresh token you will get something like this:

```yaml
status: 200

{
 "token_type": "Bearer",
 "expires_in": 3000,
 "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODEiLCJzdWIiOiJhdXRoLWNsaWVudCIsImlhdCI6MTU1NDQ3MDA3NCwianRpIjoiOWJlMTY1YzMtOTQzZS00NGU0LTkxMWEtYzk1OGY3MWRhMTdkIiwiYXVkIjoiaHR0cDovL3Jlc291cmNlLnNlcnZlci5jb20iLCJleHAiOjE1NTQ0NzMwNzR9.cR9N1Z-pKidENTrtYu5aVADRzAigZM6RvoFAzbeLkBecRcY03j4VVXnqRG1yJo744FvJ0qfetHQ2JTSQFxLrtQ",
 "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODEiLCJzdWIiOiJhdXRoLWNsaWVudCIsImp0aSI6IjliZTE2NWMzLTk0M2UtNDRlNC05MTFhLWM5NThmNzFkYTE3ZCIsInR5cCI6InJlZnJlc2gifQ.lsxtjkW0MVku4lh1W-vOEz-4wJjRN-Dkmbt2NpjezPAGj-z7FBGVyKVfH8Q0nY0smuvUnkXEAxajIb_zZdXQtw"
}
```

### Using Access Token

You can use access token in Authorization header for Aidbox API calls:

{% code-tabs %}
{% code-tabs-item title="authorized-request" %}
```yaml
GET /Patient
Authorization: Bearer ZjQyNGFhY2EtNTY2MS00NjVjLWEzYmEtMjIwYjFkNDI5Yjhi
```
{% endcode-tabs-item %}
{% endcode-tabs %}

```text
curl -H 'Authorization: Bearer ZjQyNGFhY2EtNTY2MS00NjVjLWEzYmEtMjIwYjFkNDI5Yjhi' /Patient
```

### Revoke Access Token \(Close Session\)

Aidbox create  Session \(resource\) for each Access Token, which can be closed with special endpoint `DELETE /Session` with token in Authorization header:

{% code-tabs %}
{% code-tabs-item title="close-session" %}
```yaml
DELETE /Session
Authorization: Bearer ZjQyNGFhY2EtNTY2MS00NjVjLWEzYmEtMjIwYjFkNDI5Yjhi
```
{% endcode-tabs-item %}
{% endcode-tabs %}

Session is just Resource and you can inspect and manipulate with sessions by standard Search & CRUD API for example get all sessions - `GET /Session`

### Auth Sandbox


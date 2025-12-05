---
description: Configure OAuth 2.0 Resource Owner Password Credentials Grant for user authentication in Aidbox applications.
---

# How to configure Resource Owner Grant flow

The Password grant type is used by first-party clients to exchange a user's credentials for an access token. Since this involves the client asking the user for their password, it should not be used by third party clients. In this flow, the user's username and password are exchanged directly for an access token. The application acts on behalf of the user. Refer to [OAuth 2.0 spec](https://tools.ietf.org/html/rfc6749#section-4.3) for more details.

![Basic scheme](<../../.gitbook/assets/resource-owner-grant (1).svg>)

### Easy way

The easiest way to test Resource Owner Grant flow is to run through the [Aidbox Sandbox UI](resource-owner-grant.md#auth-sandbox) (_Auth -> Sandbox ->_ Resource Owner).

<figure><img src="../../.gitbook/assets/6e34ac69-c4ae-48a7-a8f4-dc5bdb7f804c.png" alt="Sandbox UI for Resource Owner Grant testing"><figcaption><p>Sandbox UI</p></figcaption></figure>

### Configure Client

The first step is to configure Client for Resource Owner Grant with secret and password grant type:

```yaml
PUT /fhir/Client/myapp
Accept: text/yaml
Content-Type: text/yaml

secret: verysecret
grant_types:
  - password
```

Client will act on behalf of the user, which means Access Policies should be configured for User, not for Client.

You can configure Client for JWT tokens, set token expiration and enable refresh token:

| attribute                                     | options       | desc                                 |
| --------------------------------------------- | ------------- | ------------------------------------ |
| _auth.password_**.token\_format**             | jwt           | use access token in jwt format       |
| _auth.password_**.access\_token\_expiration** | int (seconds) | token expiration time from issued at |
| _auth.password_**.refresh\_token**            | true/false    | enable refresh\_token                |
| _auth.password_**.secret\_required**          | true/false    | require client secret for token      |

### Get Access Token

Next step is to collect username & password and exchange username, password, client id and secret (if required) for Access Token.

Using Authorization header `{base64(Client.id + ':' + Client.secret)}` or by JSON request with `client_id` and `client_secret` in body:

{% tabs %}
{% tab title="Request with Authorization header " %}
```yaml
POST /auth/token
Authorization: Basic base64(client.id, client.secret)
Content-Type: application/x-www-form-urlencoded

grant_type=password&username=user&password=password
```
{% endtab %}

{% tab title="Request with credentials in body" %}
```yaml
POST /auth/token
Content-Type: application/json

{
  "grant_type": "password",
  "client_id": "myapp",
  "client_secret": "verysecret",
  "username": "user",
  "password": "password"
}
```
{% endtab %}
{% endtabs %}

If provided credentials are correct, you will get a response with the access token, user information and refresh token (if enabled):

{% code title="token-response" %}
```yaml
status: 200

{
 "token_type": "Bearer",
 "expires_in": 3600,
 "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODEiLCJzdWIiOiJ1c2VyIiwiaWF0IjoxNTU0NDczOTk3LCJqdGkiOiI0ZWUwZDY2MS0wZjEyLTRlZmItOTBiOS1jY2RmMzhlMDhkM2QiLCJhdWQiOiJodHRwOi8vcmVzb3VyY2Uuc2VydmVyLmNvbSIsImV4cCI6MTU1NDQ3NzU5N30.lCdwkqzFWOe4IcXPC1dIB8v7aoZdJ0fBoIKlzCRFBgv4YndSJxGoJOvIPq2rGMQl7KG8uxGU0jkUVlKxOtD8YA",
 "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODEiLCJzdWIiOiJwYXNzd29yZC1jbGllbnQiLCJqdGkiOiI0ZWUwZDY2MS0wZjEyLTRlZmItOTBiOS1jY2RmMzhlMDhkM2QiLCJ0eXAiOiJyZWZyZXNoIn0.XWHYpw0DysrqQqMNhqTPSdNamBM4ZDUAgh_VupSa7rkzdJ3uZXqesoAo_5y1naJZ31S92-DjPKtPEAyD_8PloA"
 "userinfo": {
  "email": "user@mail.com",
  "id": "user",
  "resourceType": "User",
 },
}
```
{% endcode %}

### Use Access Token

You can use the access token in Authorization header for Aidbox API calls:

{% code title="authorized-request" %}
```yaml
GET /fhir/Patient
Authorization: Bearer ZjQyNGFhY2EtNTY2MS00NjVjLWEzYmEtMjIwYjFkNDI5Yjhi
```
{% endcode %}

```
curl -H 'Authorization: Bearer ZjQyNGFhY2EtNTY2MS00NjVjLWEzYmEtMjIwYjFkNDI5Yjhi' /Patient
```

### Revoke Access Token (Close Session)

Aidbox creates a Session resource for each Access Token, which can be closed with a special endpoint `DELETE /Session` with the token in the Authorization header:

{% code title="close-session" %}
```yaml
DELETE /Session
Authorization: Bearer ZjQyNGFhY2EtNTY2MS00NjVjLWEzYmEtMjIwYjFkNDI5Yjhi
```
{% endcode %}

Session is just a Resource and you can inspect and manipulate sessions by a standard Search & CRUD API. For example, to get all sessions: `GET /Session`

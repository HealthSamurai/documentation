---
description: >-
  An OAuth 2.0 flow that involves a user granting authorization to a client
  application
---

# Authorization Code Grant

#### Description

The Authorization Code Grant is an OAuth 2.0 flow that regular web apps use in order to access an API, typically as web applications with backend and frontend (browser-based SPA, for example).

In this flow the application receives authorization from the user. Once the user has authorized the application, they get redirected back to it with a temporary access code in the URL. The application exchanges that code for an access token. For more detailed information read [OAuth 2.0 specification](https://tools.ietf.org/html/rfc6749#section-4.1).

![Basic scheme](../../../.gitbook/assets/untitled-diagram-page-3.svg)

For applications that are able to securely store a secret it is recommended to supply the secret in the token request due to security considerations. Otherwise, if the application is unable to securely store a secret (i.e. a frontend only app), we suggest using [PKCE](https://datatracker.ietf.org/doc/html/rfc7636). Both methods are supported by Aidbox.

## Configure Client

The first step is to configure Client for Authorization Grant with `secret` and `redirect_uri`, as well as `code` grant type:

{% tabs %}
{% tab title="Secret" %}
```yaml
PUT /Client/webapp
Accept: text/yaml
Content-Type: text/yaml

secret: verysecret
first_party: true
grant_types:
  - code
auth:
  authorization_code:
    redirect_uri: 'http://myapp.app'
    access_token_expiration: 360 # 6 minutes
    token_format: jwt
    secret_required: true
    refresh_token: true
    refresh_token_expiration: 86400 # 24 hours
```
{% endtab %}

{% tab title="PKCE" %}
```yaml
PUT /Client/webapp
Accept: text/yaml
Content-Type: text/yaml

first_party: true
grant_types:
  - code
auth:
  authorization_code:
    redirect_uri: 'http://myapp.app'
    access_token_expiration: 360 # 6 minutes
    token_format: jwt
    pkce: true
    refresh_token: true
```
{% endtab %}
{% endtabs %}

Client will act on behalf of the user, which means Access Policies should be configured for User, not for Client.

You can configure Client for JWT tokens, set token expiration and enable a refresh token:

| auth\_.\_authorization\_code.  | options       | desc                                                                                                 |
| ------------------------------ | ------------- | ---------------------------------------------------------------------------------------------------- |
| **token\_format**              | jwt           | use access token in jwt format                                                                       |
| **access\_token\_expiration**  | int (seconds) | token expiration time from issued at                                                                 |
| **refresh\_token**             | true/false    | enable refresh\_token                                                                                |
| **refresh\_token\_expiration** | int (seconds) | refresh token expiration time from issued at or last usage. If not present, token will be expireless |
| **secret\_required**           | true/false    | require secret for token                                                                             |
| **pkce**                       | true/false    | enable PKCE flow                                                                                     |

{% hint style="info" %}
If you want to use Authorization Code Grant for **Single Page Application** you do not need to set the `secret` attribute, use PKCE instead!
{% endhint %}

{% hint style="info" %}
If your application is a major consumer of Aidbox API, you can set **first\_party** attribute as **true.** This means that the same User Session will be shared between Aidbox and client, so if you close the client session, Aidbox User Session will be closed too.
{% endhint %}

## Get Code

The next step is to query an authorize endpoint with `client_id` and `response_type` with value `code.`

For PKCE you will need to additionally supply `code_challenge` and `code_challenge_method`. First create a high-entropy string value with a minimum length of 43 characters and a maximum length of 128 characters, then produce a `code_challenge` using the S256 hashing method.

{% tabs %}
{% tab title="Secret" %}
```json
GET /auth/authorize?\
response_type=code&\
client_id=webapp&\
redirect_uri=http://myapp.app&state=somestate
```
{% endtab %}

{% tab title="PKCE" %}
```json
GET /auth/authorize?\
response_type=code&\
client_id=webapp&\
redirect_uri=http://myapp.app&\
state=somestate&\    
code_challenge=46mfdzfFFfZJtfN8UfAzUQgnq9_Tei33CUVXyeAeiwE&\
code_challenge_method=S256
```
{% endtab %}
{% endtabs %}

To keep your client stateless, you can send a `state` parameter with arbitrary content, which will be sent back in the redirect response.

If users are not logged in, they will see the default login screen.

![](../../../.gitbook/assets/login-screen-with-id-field.png)

If a client is not **first\_party** or the user has not yet granted permissions to the client, the user will see the grant page:

![](<../../../.gitbook/assets/image (4) (1) (1) (1).png>)

If the client was granted permission, the user agent will be redirected to the url configured in **Client.auth.authorization\_code.redirect\_uri** with the authorization code parameter.

```
<redirect_uri>?code=****&state=***
```

## Get Access Token

With this code and client secret, you can request an Access Token with`grant_type: authorization_code`. If you're using PKCE, you will need to supply `code_verifier` used to produce the `code_challenge`.

{% tabs %}
{% tab title="Secret" %}
```yaml
POST /auth/token
Content-Type: application/json

{
 "client_id": "webapp",
 "client_secret": "verysecret",
 "code": <code>,
 "grant_type": "authorization_code"
}
```
{% endtab %}

{% tab title="PKCE" %}
```yaml
POST /auth/token
Content-Type: application/json

{
 "client_id": "webapp",
 "code_verifier": "nzr2UsqTGUgd9wC55Sc4m8OzwbR0Lqu_Bh...",
 "code": <code>,
 "grant_type": "authorization_code"
}
```
{% endtab %}
{% endtabs %}

If the provided code is accurate, you will get access token, user information and refresh token (if enabled):

{% tabs %}
{% tab title="token-response" %}
```yaml
status: 200

{
 "token_type": "Bearer",
 "expires_in": 3600,
 "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODEiLCJzdWIiOiJ1c2VyIiwiaWF0IjoxNTU0NDczOTk3LCJqdGkiOiI0ZWUwZDY2MS0wZjEyLTRlZmItOTBiOS1jY2RmMzhlMDhkM2QiLCJhdWQiOiJodHRwOi8vcmVzb3VyY2Uuc2VydmVyLmNvbSIsImV4cCI6MTU1NDQ3NzU5N30.lCdwkqzFWOe4IcXPC1dIB8v7aoZdJ0fBoIKlzCRFBgv4YndSJxGoJOvIPq2rGMQl7KG8uxGU0jkUVlKxOtD8YA",
 "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODEiLCJzdWIiOiJwYXNzd29yZC1jbGllbnQiLCJqdGkiOiI0ZWUwZDY2MS0wZjEyLTRlZmItOTBiOS1jY2RmMzhlMDhkM2QiLCJ0eXAiOiJyZWZyZXNoIn0.XWHYpw0DysrqQqMNhqTPSdNamBM4ZDUAgh_VupSa7rkzdJ3uZXqesoAo_5y1naJZ31S92-DjPKtPEAyD_8PloA",
 "userinfo": {
  "email": "user@mail.com",
  "id": "user",
  "resourceType": "User",
 }
}
```
{% endtab %}
{% endtabs %}

### Use Access Token

You can use access token in the Authorization header for Aidbox API calls:

{% tabs %}
{% tab title="authorized-request" %}
```yaml
GET /Patient
Authorization: Bearer ZjQyNGFhY2EtNTY2MS00NjVjLWEzYmEtMjIwYjFkNDI5Yjhi
```
{% endtab %}
{% endtabs %}

```
curl -H 'Authorization: Bearer ZjQyNGFhY2EtNTY2MS00NjVjLWEzYmEtMjIwYjFkNDI5Yjhi' /Patient
```

### Refresh Access Token

To get new access token using refresh token

{% tabs %}
{% tab title="request" %}
```yaml
POST /auth/token
Content-Type: application/json

{
  "grant_type": "refresh_token",
  "client_id": "webapp",
  "refresh_token": "eyJhb..PloA"
}
```
{% endtab %}

{% tab title="response" %}
```yaml
status: 200

{
  "access_token": "eyJh..D8YA", # new access_token
  "expores_id": 360
}
```
{% endtab %}
{% endtabs %}

### Revoke Access Token (Close Session)

Aidbox creates a Session resource for each Access Token which can be closed with a special endpoint `DELETE /Session` with the token in Authorization header:

{% tabs %}
{% tab title="close-session" %}
```yaml
DELETE /Session
Authorization: Bearer ZjQyNGFhY2EtNTY2MS00NjVjLWEzYmEtMjIwYjFkNDI5Yjhi
```
{% endtab %}
{% endtabs %}

Session is just a resource and you can inspect and manipulate sessions with standard Search & CRUD API. For example, use `GET /Session` to get all sessions.

## Auth Sandbox Demo

{% embed url="https://youtu.be/w8rscpqApMU" %}

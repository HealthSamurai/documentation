# Authorization Code Grant

#### Description

The Authorization Code Grant s an OAuth 2.0 flow that regular web apps use in order to access an API, typically as Web applications with backend and frontend. For more detailed information read [OAuth2.0 specifcation](https://tools.ietf.org/html/rfc6749#section-4.1). This flow is also applied for a browser-based application \(SPA\) and it didn't use `client-secret`, because the source code is available in a browser - it isn't secure. Instead of this user authorizes the application and redirected back to the application with a temporary code in the URL. The application exchanges that code for the access token.

![Basic scheme](../.gitbook/assets/untitled-diagram-page-3.svg)

## Configure Client

Fist step is configure Client for Authorization Grant with `secret` and `redirect_uri`, as well `code` grant type:

{% code-tabs %}
{% code-tabs-item title="client" %}
```yaml
PUT Client/webapp

secret: verysecret
first_party: true
grant_types:
  - code
auth:
  authorization_code:
    redirect_uri: 'http://myapp.app'
    access_token_expiration: 360
    token_format: jwt
    secret_required: true
    refresh_token: true
```
{% endcode-tabs-item %}
{% endcode-tabs %}

Client will act on behalf of the user,  which mean Access Policies should be configured for User, not for Client. 

You can configure Client for JWT tokens,  set token expiration and enable refresh token:

| _auth._authorization\_code. | options | desc |
| :--- | :--- | :--- |
| **token\_format** | jwt | use access token in jwt format |
| **token\_expiration** | int \(seconds\) | token expiration time from issued at |
| **refresh\_token** | true/false | enable refresh\_token |
| **secret\_required** | true/false | require secret for token |

{% hint style="info" %}
If you want to use _Authorizatin Code Grant_ for **Single Page Application** you do not need `secret` attribute to be set!
{% endhint %}

{% hint style="info" %}
If your application is major consumer of Aidbox API you can set **first\_party** attribute into **true.** This mean, that same User Session will be shared between Aidbox by client,  otherwise new session will be created for client & user.
{% endhint %}

## Get Code

Next step is to redirect user from your application to Aidbox **authorize** endpoint with **client\_id** and **response\_type** - code:

```text
https://<box>.aidbox.app/auth/authorize?client_id=webapp&response_type=code&state=...
```

To keep you client stateless you can send **state** parameter with arbitrary content, which will be send you back on redirect.

If user is not logged in - she will see login screen.

![](../.gitbook/assets/image%20%282%29.png)

If client is not first\_party or user not yet granted permissions to client, user will see grant page:

![](../.gitbook/assets/image%20%281%29.png)

If client granted permissions user agent will be redirected to url configured in **Client.auth.authorization\_code.redirect\_uri** with authorization code parameter.

```text
<redirect_uri>?code=****&state=***
```

## Get Access Token

With this code and client secret you can request for  Access Token with `grant_type: authorization_code`.

{% code-tabs %}
{% code-tabs-item title="json-request" %}
```yaml
POST /auth/tokenGet Access Token
Content-Type: application/json
{
 "client_id": "web-app",
 "client_secret": "verysecret",
 "code": <code>,
 "grant_type": "authorization_code"
}
```
{% endcode-tabs-item %}
{% endcode-tabs %}

When everything is accurate, you will get response with access token, user information and refresh token \(if enabled\):

{% code-tabs %}
{% code-tabs-item title="token-response" %}
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
{% endcode-tabs-item %}
{% endcode-tabs %}

### Use Access Token

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

## Auth Sandbox Demo

{% embed url="https://youtu.be/w8rscpqApMU" %}




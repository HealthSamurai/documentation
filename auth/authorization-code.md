# Authorization Code Grant

#### Description

The Authorization Code Grant is an OAuth 2.0 flow that regular web apps use in order to access an API, typically as web applications with backend and frontend \(browser-based SPA, for example\). This flow doesn't use `client-secret` due to security considerations - frontend application source code is available in a browser. Instead, user authorises the application and gets redirected back to it with a temporary access code in the URL. Application exchanges that code for the access token. For more detailed information read [OAuth 2.0 specification](https://tools.ietf.org/html/rfc6749#section-4.1). 

![Basic scheme](../.gitbook/assets/untitled-diagram-page-3.svg)

## Configure Client

First step is to configure Client for Authorization Grant with `secret` and `redirect_uri`, as well `code` grant type:

{% tabs %}
{% tab title="client" %}
```yaml
PUT /Client/webapp

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
{% endtab %}
{% endtabs %}

Client will act on behalf of the user, which means Access Policies should be configured for User, not for Client. 

You can configure Client for JWT tokens,  set token expiration and enable refresh token:

| auth_._authorization\_code. | options | desc |
| :--- | :--- | :--- |
| **token\_format** | jwt | use access token in jwt format |
| **token\_expiration** | int \(seconds\) | token expiration time from issued at |
| **refresh\_token** | true/false | enable refresh\_token |
| **secret\_required** | true/false | require secret for token |

{% hint style="info" %}
If you want to use Authorization Code Grant for **Single Page Application** you do not need `secret` attribute to be set!
{% endhint %}

{% hint style="info" %}
If your application is a major consumer of Aidbox API you can set **first\_party** attribute into **true.** This means that the same User Session will be shared between Aidbox and client, so if you close client session Aidbox User Session will be closed too.
{% endhint %}

## Get Code

Next step is to redirect user from your application to **authorize** endpoint with **client\_id** and **response\_type** - code:

```text
https://<box>.aidbox.app/auth/authorize?client_id=webapp&response_type=code&state=...
```

To keep you client stateless you can send **state** parameter with arbitrary content, which will be send you back on redirect.

If user is not logged in then she will see the login screen.

![](../.gitbook/assets/image%20%284%29.png)

If client is not first\_party or user not yet granted permissions to client, user will see grant page:

![](../.gitbook/assets/image%20%282%29.png)

If client granted permissions, user agent will be redirected to url configured in **Client.auth.authorization\_code.redirect\_uri** with authorization code parameter.

```text
<redirect_uri>?code=****&state=***
```

## Get Access Token

With this code and client secret you can request for Access Token with`grant_type: authorization_code`.

{% tabs %}
{% tab title="json-request" %}
```yaml
POST /auth/token
Content-Type: application/json
{
 "client_id": "web-app",
 "client_secret": "verysecret",
 "code": <code>,
 "grant_type": "authorization_code"
}
```
{% endtab %}
{% endtabs %}

If provided code is accurate, you get access token, user information and refresh token \(if enabled\):

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

You can use access token in Authorization header for Aidbox API calls:

{% tabs %}
{% tab title="authorized-request" %}
```yaml
GET /Patient
Authorization: Bearer ZjQyNGFhY2EtNTY2MS00NjVjLWEzYmEtMjIwYjFkNDI5Yjhi
```
{% endtab %}
{% endtabs %}

```text
curl -H 'Authorization: Bearer ZjQyNGFhY2EtNTY2MS00NjVjLWEzYmEtMjIwYjFkNDI5Yjhi' /Patient
```

### Revoke Access Token \(Close Session\)

Aidbox creates Session resource for each Access Token which can be closed with special endpoint `DELETE /Session` with token in Authorization header:

{% tabs %}
{% tab title="close-session" %}
```yaml
DELETE /Session
Authorization: Bearer ZjQyNGFhY2EtNTY2MS00NjVjLWEzYmEtMjIwYjFkNDI5Yjhi
```
{% endtab %}
{% endtabs %}

Session is just a resource and you can inspect and manipulate sessions with standard Search & CRUD API. For example, to get all sessions initiate `GET /Session`

## Auth Sandbox Demo

{% embed url="https://youtu.be/w8rscpqApMU" %}




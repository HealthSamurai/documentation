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

To keep you client stateless you can send **state** parameter with arbitrary content, which will be send you back and back redirect.

## Get Access Token

Next step is collect username & password and exchange username, password, client id and secret \(if required\) for  Access Token.

 Using Basic & form-url-encode:

{% code-tabs %}
{% code-tabs-item title="using-basic" %}
```yaml
POST /auth/token
Authorization: Basic base64(client.id, client.secret)
Content-Type: application/x-www-form-urlencoded

grant_type=password&username=user&password=password
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
  "grant_type": "password",
  "client_id": "myapp",
  "client_secret": "verysecret",
  "username": "user",
  "password": "password"
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
 "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODEiLCJzdWIiOiJwYXNzd29yZC1jbGllbnQiLCJqdGkiOiI0ZWUwZDY2MS0wZjEyLTRlZmItOTBiOS1jY2RmMzhlMDhkM2QiLCJ0eXAiOiJyZWZyZXNoIn0.XWHYpw0DysrqQqMNhqTPSdNamBM4ZDUAgh_VupSa7rkzdJ3uZXqesoAo_5y1naJZ31S92-DjPKtPEAyD_8PloA"
 "userinfo": {
  "email": "user@mail.com",
  "id": "user",
  "resourceType": "User",
 },
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

## Authorization Request

The client initiates the flow by directing the resource owner's user-agent to the authorization endpoint. The client includes its client identifier, requested scope, local state, and a redirection URI to which the authorization server will send the user-agent back once access is granted \(or denied\).

{% api-method method="get" host="\[base\]" path="/auth/authorize" %}
{% api-method-summary %}
Authorization Endpoint
{% endapi-method-summary %}

{% api-method-description %}
Obtaining `code`
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-query-parameters %}
{% api-method-parameter name="scope" type="string" required=false %}
requested scope
{% endapi-method-parameter %}

{% api-method-parameter name="state" type="string" required=false %}
value used by the client to maintain state between the request and callback
{% endapi-method-parameter %}

{% api-method-parameter name="redirect\_uri" type="string" required=false %}
client `redirect_uri`  
If provided it must match with registered redirect URI
{% endapi-method-parameter %}

{% api-method-parameter name="response\_type" type="string" required=true %}
value MUST set to `code`
{% endapi-method-parameter %}

{% api-method-parameter name="client\_id" type="string" required=true %}
client ID
{% endapi-method-parameter %}
{% endapi-method-query-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=302 %}
{% api-method-response-example-description %}

{% endapi-method-response-example-description %}

```text
     Location: [redirect_url]?code=SplxlOBeZQQYbYS6WxSbIA
               &state=xyz
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

#### Example

{% tabs %}
{% tab title="Request" %}
```bash
curl -X GET \
  'http://localhost:8081/auth/authorize?client_id=ac-client&state=example&response_type=code'
```
{% endtab %}
{% endtabs %}

After this request you will be redirected to Log-in/Sign-up page

![Example](../.gitbook/assets/screenshot-2019-02-11-18.15.41.png)

Next step - user authentication and granting access.

![Example](../.gitbook/assets/screenshot-2019-02-11-19.47.39.png)

Assuming the resource owner grants access, the authorization server redirects the user-agent back to the client using the redirection URI provided earlier \(in the request or during client registration\). The redirection URI includes an authorization code and any local state provided by the client earlier.

### Access Token Request

The `client` requests an access token from the authorization server's token endpoint by including the authorization `code` received in the previous step. When making the request, the client authenticates with the authorization server. The client includes the redirection URI used to obtain the authorization code for verification.

### OAuth2.0 RFC Specification way

Basic header, and `body` should be in `application/x-www-form-urlencoded` format and `grant_type` parameter value MUST be set to `authorization_code`

{% api-method method="post" host="\[base\]/auth/" path="token" %}
{% api-method-summary %}
Access token endpoint
{% endapi-method-summary %}

{% api-method-description %}
Obtaining `access_token`
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-headers %}
{% api-method-parameter name="Content-Type" type="string" required=true %}
application/x-www-form-urlencoded
{% endapi-method-parameter %}

{% api-method-parameter name="Authorization" type="string" required=true %}
client credentials base64 encoded
{% endapi-method-parameter %}
{% endapi-method-headers %}

{% api-method-form-data-parameters %}
{% api-method-parameter name="scope" type="string" required=false %}
requested scope
{% endapi-method-parameter %}

{% api-method-parameter name="grant\_type" type="string" required=true %}
value must be set of `authorization_code`
{% endapi-method-parameter %}
{% endapi-method-form-data-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}

{% endapi-method-response-example-description %}

```http
Content-Type: application/json;charset=UTF-8
Cache-Control: no-store
Pragma: no-cache

{
       "access_token": "2YotnFZFEjr1zCsicMWpAA" (required),
       "token_type":"example" (required),
       "expires_in":3600 (optional),
       "refresh_token": "2YotnFZFEjr1zCsiaasdasdaqe" (optional)
}
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

#### Example

{% tabs %}
{% tab title="Request" %}
```bash
curl -X POST \
  http://localhost:8081/auth/token \
  -H 'Authorization: Y2MtY2xpZW50OnZlcnlzZWNyZXRzZWNyZXQ=' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'grant_type=authorization_code'
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
 "token_type": "Bearer",
 "access_token": "OTJhNDdiOTgtNGYxMS00ZDdhLTg1NWQtOGRiN2Y2ZTNlNzJm"
}
```
{% endtab %}
{% endtabs %}

## JSON/Form parameters request

{% api-method method="post" host="\[base\]/" path="auth/token" %}
{% api-method-summary %}
Token Endpoint
{% endapi-method-summary %}

{% api-method-description %}
Obtaining `access_token`
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-body-parameters %}
{% api-method-parameter name="client\_secret" type="string" required=false %}
required for confidential clients
{% endapi-method-parameter %}

{% api-method-parameter name="client\_id" type="string" required=true %}
client ID
{% endapi-method-parameter %}

{% api-method-parameter name="redirect\_uri" type="string" required=false %}
required if it provided in authorization code request
{% endapi-method-parameter %}

{% api-method-parameter name="code" type="string" required=true %}
authorization code received early
{% endapi-method-parameter %}

{% api-method-parameter name="grant\_type" type="string" required=true %}
value MUST set of `authorization_code`
{% endapi-method-parameter %}
{% endapi-method-body-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}

{% endapi-method-response-example-description %}

```javascript
{
 "token_type": "Bearer",
 "access_token": "OTJhNDdiOTgtNGYxMS00ZDdhLTg1NWQtOGRiN2Y2ZTNlNzJm"
}
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

#### Example

{% tabs %}
{% tab title="JSON request" %}
```javascript
curl -X POST \
  http://localhost:8081/auth/token \
  -H 'Content-Type: application/json' \
  -d '{"code":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyIiwiY2xpIjoiYWMtY2xpZW50IiwiZXhwIjoxNTQ5ODk2NzA1LCJqdGkiOiJPRGMzT1RWa016WXRORGMyTkMwMFl6bG1MV0ZoTXpJdE5ESTBZbVJtTnpZNE1qUXgifQ.1lC4EJlwZJcxLW_WMgAALDM8OAuUcaSsjkhMI9RxERM",
      "client_id":"ac-client",
      "grant_type":"authorization_code"}'
```
{% endtab %}

{% tab title="Form parameters request" %}
```javascript
curl -X POST \
  http://localhost:8081/auth/token \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'grant_type=authorization_code
      &code=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyIiwiY2xpIjoiYWMtY2xpZW50IiwiZXhwIjoxNTQ5ODk3MDM4LCJqdGkiOiJNakpoTVRNNVpqY3RaRGcwT0MwME1qZzRMVGxrTkRRdFpESTRORGcwTldSaU9EVXoifQ.-W-QMB1oT_1iQoJXW0A59WfcZ0WHnOOy21-SLaOr9j0
      &client_id=ac-client'
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
    "token_type": "Bearer",
    "access_token": "NmM1NzFjM2MtYzIzYi00NDgyLWFlM2YtMTgyMWVhZDU1YzRm"
}
```
{% endtab %}
{% endtabs %}

Confidential clients or clients for which was issued `client_id` must be authenticated by server. Identifier can be provided as parameter like the others in body or we can use authorization header with encoded client credentials.

{% tabs %}
{% tab title="JSON requst" %}
```bash
curl -X POST \
  http://localhost:8081/auth/token \
  -H 'Authorization: Basic Y2MtY2xpZW50OnZlcnlzZWNyZXRzZWNyZXQ=' \
  -H 'Content-Type: application/json' \
  -d '{
    "grant_type": "authorization_code",
    "client_id": "ac-client"
}'
```
{% endtab %}

{% tab title="Form parameters request" %}
```bash
curl -X POST \
  http://localhost:8081/auth/token \
  -H 'Authorization: Basic Y2MtY2xpZW50OnZlcnlzZWNyZXRzZWNyZXQ=' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'grant_type=authorization_code&client_id=ac-client'
```
{% endtab %}
{% endtabs %}

### Client examples

{% tabs %}
{% tab title="Basic client" %}
```yaml
POST /Client

id: ac-client
resourceType: Client
grant_types:
- authorization_code
auth:
  authorization_code:
    redirect_uri: http://localhost:3449/auth.html
```
{% endtab %}

{% tab title="Client with secret" %}
```yaml
POST /Client

id: ac-basic-client
secret: verysecretsecret
resourceType: Client
grant_types:
- authorization_code
auth:
  authorization_code:
    secret_required: true
    redirect_uri: http://localhost:3449/auth.html
```
{% endtab %}

{% tab title="Client with JWT" %}
```yaml
POST /Client

id: ac-jwt-client
resourceType: Client
grant_types:
- authorization_code
auth:
  authorization_code:
    token_format: jwt
    refresh_token: true
    access_token_expiration: 3600
    redirect_uri: http://localhost:3449/auth.html

```
{% endtab %}

{% tab title="First party client" %}
```yaml
POST /Client

id: fp-ac-client
resourceType: Client
first_party: true
grant_types:
- authorization_code
auth:
  authorization_code:
    redirect_uri: http://localhost:3449/auth.html
```
{% endtab %}
{% endtabs %}


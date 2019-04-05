# Resource Owner Grant

#### Description

The Password grant type is used by first-party clients to exchange a user's credentials for an access token. Since this involves the client asking the user for their password, it should not be used by third party clients. In this flow, the user's username and password are exchanged directly for an access token. The application acts on behalf of the user.

![Basic scheme](../.gitbook/assets/untitled-diagram-page-2.svg)

### Configure Client

Fist step is configure Client for Resource Owner Grant with secret and password grant type:

{% code-tabs %}
{% code-tabs-item title="client" %}
```yaml
PUT Client/myapp

secret: verysecret
grant_types:
  - password
```
{% endcode-tabs-item %}
{% endcode-tabs %}

Client will act on behalf of the user,  which mean Access Policies should be configured for User, not for Client.

You can configure Client for JWT tokens,  set token expiration and enable refresh token:



## Additional flows

### Secure code flow

For more protection we have an addition flow which can receive secret code and exchange it for an access token. This flow consists of few steps. At first you need obtain `code`with redirect flow. Redirect URI must previously set in client registration.

{% api-method method="get" host="\[base\]/auth/authorize" path="/" %}
{% api-method-summary %}
Authorization code endpoint
{% endapi-method-summary %}

{% api-method-description %}
Obtaining secure code
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-query-parameters %}
{% api-method-parameter name="state" type="string" required=false %}
some data that will be provided in redirected response
{% endapi-method-parameter %}

{% api-method-parameter name="grant\_type" type="string" required=true %}
value must set to `password`
{% endapi-method-parameter %}

{% api-method-parameter name="client\_id" type="string" required=true %}
client ID
{% endapi-method-parameter %}
{% endapi-method-query-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=302 %}
{% api-method-response-example-description %}
Obtain code and state in uri fragment
{% endapi-method-response-example-description %}

```javascript
[redirect_uri]#code=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJyby1kZWZhdWx0LWNsaWVudCIsImV4cCI6MTU0OTY0MjA2MCwianRpIjoiTnpVeU9XSTJPVFl0TVRCallTMDBOVEUyTFdGa09UZ3RaRFk1T0RNNE1HRTNPV05oIn0.XWfXJI67zAkvGVRthPPrkFVvHXOqur3N-Jhiarmeloo
&state=example
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

`code` will be provided in fragment of URI after redirect.

Next step - exchanging `code` and client credentials for `access_token`

{% hint style="info" %}
**code** can be exchanged for the **access\_token** only once, for another try needs to obtain new
{% endhint %}

{% api-method method="post" host="\[base\]" path="/auth/token" %}
{% api-method-summary %}
Token Endpoint
{% endapi-method-summary %}

{% api-method-description %}
Get `access_token` token via Resource Owner Credentials Grant. All parameters packed in JSON.
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-body-parameters %}
{% api-method-parameter name="code" type="string" required=true %}
obtained previously code
{% endapi-method-parameter %}

{% api-method-parameter name="grant\_type" type="string" required=true %}
value MUST be set to `password`
{% endapi-method-parameter %}

{% api-method-parameter name="client\_id" type="string" required=true %}
client ID
{% endapi-method-parameter %}

{% api-method-parameter name="password" type="string" required=true %}
user password
{% endapi-method-parameter %}

{% api-method-parameter name="username" type="string" required=true %}
user identification email
{% endapi-method-parameter %}
{% endapi-method-body-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}

{% endapi-method-response-example-description %}

```javascript
{
    "token_type": "Bearer", (required)
    "access_token": "YjFhMTU3NWEtNDUzYi00ZDIxLWI2YTAtOTEzNjRiMDhj", (required)
    "expires_in":3600, (optional)
    "refresh_token":"tGzv3JOkF0XG5Qx2TlKWIA" (optional)
}
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

As well as it described previously - data can be sent as JSON or form parameters.

#### Example

{% tabs %}
{% tab title="JSON request" %}
```javascript
curl -X POST \
  http://localhost:8081/auth/token \
  -H 'Content-Type: application/json' \
  -d '{"code":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJyby1kZWZhdWx0LWNsaWVudCIsImV4cCI6MTU0OTg4MzA2MCwianRpIjoiT0RCaU5tTXhOalF0WVRSa1l5MDBNV1V6TFdJM1pETXRaakE0TlRabFpHTmlaRGRtIn0.Oib74zmGjj3_pUkSCPejAalRzguebdLEppJcGitD1bE",
 "client_id":"ro-default-client",
 "grant_type":"password",
 "username":"user@mail.com",
 "password":"password"}'
```
{% endtab %}

{% tab title="Form parameters request" %}
```bash
curl -X POST \
  http://localhost:8081/auth/token \
  -d 'grant_type=password
     &client_id=ro-default-client
     &username=user%40mail.com
     &password=password
     &code=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJyby1kZWZhdWx0LWNsaWVudCIsImV4cCI6MTU0OTk4NTkwOSwianRpIjoiWVRObFlqUmpORFF0Tmpnek1pMDBZamRpTFRsbU9UTXRNR0k0WlRZNE1UVXdOV1l6In0.8DQ0OYiePgrfXpFAMN37-CNObfUVchSArAZvJx0oX10'
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
    "token_type": "Bearer",
    "access_token": "NTNmOTM4ODYtOGIyZi00MDZkLTkzM2MtMDgxNWE3Yzg2OGJk"
}
```
{% endtab %}
{% endtabs %}

### Client  examples

{% tabs %}
{% tab title="Basic client" %}
```yaml
POST /Client

id: ro-basic-client
resourceType: Client
grant_types:
- password
```
{% endtab %}

{% tab title="Client with redirect" %}
```yaml
POST /Client

id: ro-default-client
resourceType: Client
grant_types:
- password
auth:
  password:
    redirect_uri: http://localhost:3449/auth.html
```
{% endtab %}

{% tab title="Client with secret" %}
```yaml
POST /Client

id: ro-secret-client
secret: verysecretsecret
resourceType: Client
grant_types:
- password
auth:
  password:
    secret_required: true
```
{% endtab %}

{% tab title="Client with JWT" %}
```yaml
POST /Client

id: ro-jwt-client
grant_types:
- password
resourceType: Client
auth:
  password:
    token_format: jwt
    refresh_token: true
    access_token_expiration: 3600
```
{% endtab %}
{% endtabs %}


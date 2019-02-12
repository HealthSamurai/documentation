# Resource Owner Credentials Grant

#### Description

The resource owner password credentials grant type is suitable in cases where the resource owner has a trust relationship with the client, such as the device operating system or a highly privileged. The application acts on behalf of the user.

![Basic scheme](../../.gitbook/assets/untitled-diagram-page-2.svg)

[Aidbox](https://www.health-samurai.io/aidbox) OAuth module support this flow in different formats. First- Strict adherence to specifications for better compatibility. Second - JSON request as a more modern and simple way. Also we have availability send all data in POST Body form parameters. Read official [OAuth2.0 specification](https://tools.ietf.org/html/rfc6749#section-4.3) for more details.

## JSON Request

You need to specify `client_id`, `client_secret` ,  `grant_type` ,  `username,` and `password` .  All parameters are required.

{% api-method method="post" host="\[base\]" path="/auth/token" %}
{% api-method-summary %}
Token Endpoint
{% endapi-method-summary %}

{% api-method-description %}
Get `access_token` token via Resource Owner Credentials Grant.
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-headers %}
{% api-method-parameter name="Content-Type" type="string" required=true %}
application/json
{% endapi-method-parameter %}
{% endapi-method-headers %}

{% api-method-body-parameters %}
{% api-method-parameter name="grant\_type" type="string" required=true %}
Value MUST be set to `password`
{% endapi-method-parameter %}

{% api-method-parameter name="client\_id" type="string" required=true %}
Client Id
{% endapi-method-parameter %}

{% api-method-parameter name="password" type="string" required=true %}
User password
{% endapi-method-parameter %}

{% api-method-parameter name="username" type="string" required=true %}
User identification email
{% endapi-method-parameter %}
{% endapi-method-body-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}
Return access\_token
{% endapi-method-response-example-description %}

```javascript
{
    "token_type": "Bearer",
    "access_token": "OTZmNGVlZTAtMjhmMC00NTE5LWJjOTctZDZiYzMxOTQ3NzQ1"
}
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

### Example

{% tabs %}
{% tab title="Request" %}
```bash
curl -X POST \
  http://localhost:8081/auth/token \
  -H 'Content-Type: application/json' \
  -d '{
	"grant_type": "password",
	"client_id": "ro-basic-client",
	"username": "user@mail.com",
	"password": "password"
}'
```
{% endtab %}
{% endtabs %}

## POST Body parameters way 

You need to specify `client_id` ,  `grant_type` ,  `username,` and `password` .  All parameters are required.

{% api-method method="post" host="\[base\]" path="/auth/token" %}
{% api-method-summary %}
Token Endpoint
{% endapi-method-summary %}

{% api-method-description %}
Get `access_token` token via Resource Owner Credentials Grant.
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-body-parameters %}
{% api-method-parameter name="grant\_type" type="string" required=true %}
Value MUST be set to `password`
{% endapi-method-parameter %}

{% api-method-parameter name="client\_id" type="string" required=true %}
Client Id
{% endapi-method-parameter %}

{% api-method-parameter name="password" type="string" required=true %}
User password
{% endapi-method-parameter %}

{% api-method-parameter name="username" type="string" required=true %}
User identification email
{% endapi-method-parameter %}
{% endapi-method-body-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}
Return access\_token
{% endapi-method-response-example-description %}

```javascript
{
    "token_type": "Bearer",
    "access_token": "OTZmNGVlZTAtMjhmMC00NTE5LWJjOTctZDZiYzMxOTQ3NzQ1"
}
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

### Example

{% tabs %}
{% tab title="Request" %}
```bash
curl -X POST \
  http://localhost:8081/auth/token \
  -d 'grant_type=password&client_id=ro-basic-client&username=user%40mail.com&password=password'
```
{% endtab %}
{% endtabs %}

## Additional flows

You can additionally use  `client_secret`. On POST parameters data or JSON format.  All parameters are required.

{% api-method method="post" host="\[base\]" path="/auth/token" %}
{% api-method-summary %}
Token Endpoint
{% endapi-method-summary %}

{% api-method-description %}
Get `access_token` token via Resource Owner Credentials Grant.
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-body-parameters %}
{% api-method-parameter name="client\_secret" type="string" required=true %}
Client secret
{% endapi-method-parameter %}

{% api-method-parameter name="grant\_type" type="string" required=true %}
Value MUST be set to `password`
{% endapi-method-parameter %}

{% api-method-parameter name="client\_id" type="string" required=true %}
Client Id
{% endapi-method-parameter %}

{% api-method-parameter name="password" type="string" required=true %}
User password
{% endapi-method-parameter %}

{% api-method-parameter name="username" type="string" required=true %}
User identification email
{% endapi-method-parameter %}
{% endapi-method-body-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}
Return access\_token
{% endapi-method-response-example-description %}

```javascript
{
    "token_type": "Bearer",
    "access_token": "OTZmNGVlZTAtMjhmMC00NTE5LWJjOTctZDZiYzMxOTQ3NzQ1"
}
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

### Example

{% tabs %}
{% tab title="Request" %}
```bash
curl -X POST \
  http://localhost:8081/auth/token \
  -d 'grant_type=password&client_secret=verysecretsecret&client_id=ro-secret-client&username=user%40mail.com&password=password&undefined='
```
{% endtab %}
{% endtabs %}

### Secure code flow

For more security we can receive secret code and exchange it for an access token. This flow consists of few steps. At first you need obtain `code`with redirect flow. Redirect URI must  previously set in client registration.

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
Some data that will be provided in redirected response
{% endapi-method-parameter %}

{% api-method-parameter name="grant\_type" type="string" required=true %}
MUST set as `password`
{% endapi-method-parameter %}

{% api-method-parameter name="client\_id" type="string" required=true %}
Client ID
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
Obtained previously code
{% endapi-method-parameter %}

{% api-method-parameter name="grant\_type" type="string" required=true %}
Value MUST be set to `password`
{% endapi-method-parameter %}

{% api-method-parameter name="client\_id" type="string" required=true %}
Client Id
{% endapi-method-parameter %}

{% api-method-parameter name="password" type="string" required=true %}
User password
{% endapi-method-parameter %}

{% api-method-parameter name="username" type="string" required=true %}
User identification email
{% endapi-method-parameter %}
{% endapi-method-body-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}

{% endapi-method-response-example-description %}

```

```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

#### Example

{% tabs %}
{% tab title="Request" %}
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

{% tab title="Response" %}
```javascript
{
    "token_type": "Bearer",
    "access_token": "NTNmOTM4ODYtOGIyZi00MDZkLTkzM2MtMDgxNWE3Yzg2OGJk"
}
```
{% endtab %}
{% endtabs %}


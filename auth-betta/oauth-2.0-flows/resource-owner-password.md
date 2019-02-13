# Resource Owner Credentials Grant

#### Description

The resource owner password credentials grant type is suitable in cases where the resource owner has a trust relationship with the client, such as the device operating system or a highly privileged application. The application acts on behalf of the user.

![Basic scheme](../../.gitbook/assets/untitled-diagram-page-2.svg)

[Aidbox](https://www.health-samurai.io/aidbox) OAuth module support this flow in different formats. First- Strict adherence to specifications for better compatibility. Second - JSON request as a more modern and simple way. Also we have availability send all data in POST Body form parameters. Read official [OAuth2.0 specification](https://tools.ietf.org/html/rfc6749#section-4.3) for more details.

## JSON/Form parameters request

You need to specify required params `client_id` ,  `grant_type` ,  `username,` and `password` .

If the client type is confidential or the client was issued client credentials than `client_secret` is required.

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
{% api-method-parameter name="client\_secret" type="string" required=false %}
client secret key \(REQUIRED if was issued by client\)
{% endapi-method-parameter %}

{% api-method-parameter name="scope" type="string" required=false %}
requested scope
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

Instead of sending parameters in JSON you can sent it in form parameters 

### Example

{% tabs %}
{% tab title="JSON request" %}
```bash
curl -X POST \
  http://localhost:8081/auth/token \
  -H 'Content-Type: application/json' \
  -d '{"grant_type": "password",
 "client_id": "modernapp",
 "client_secret": "thesecret",
 "username": "admin@admin.io",
 "password": "secret"}'
```
{% endtab %}

{% tab title="Form parameters request" %}
```bash
curl -X POST \
  http://localhost:8081/auth/token \
  -d 'grant_type=password&client_id=modernapp&client_secret=thesecret&username=admin%40admin.io&password=secret'
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
    "token_type": "Bearer",
    "expires_in": 3600,
    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODEiLCJzdWIiOiJtb2Rlcm5hcHAiLCJqdGkiOiJjMTYxMTZjZS1iNGVhLTQwMjctYWI4OC0zNmJkZmE0YjQzM2QiLCJleHAiOjE1NDk5NzEzNDd9.QsaPNzS6DaA-6TgtrVZFVfg0Os45GJ3l4tQW92o4xq7aqultAwRi_E-NTOCqLO21l0QgNfr5HfAre-0o3O6Bg7nEuiyt4iO5au80YwQIS_L41OwMzNgtGORb3EHfafLa2Al5bzh7gmRIFuxCG7m8P4SypfsAGhWfILvOdAFqpamLoNAUOZGGyJLv_MRvyFKgSQgRGnb_F-e874hzgoNIrHXRnX1FaThHldoc9yE8E5wuLjLLXTKI23hCNOBzscNf1toAOnuJOdlTQSBScpX6yNQYBYIGAIbq_Qz6x7wfcbU2yBFzZpIv8OKRRb0tful_oFhkpry1LH8nM6J1XLfSpA",
    "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODEiLCJzdWIiOiJtb2Rlcm5hcHAiLCJqdGkiOiJjMTYxMTZjZS1iNGVhLTQwMjctYWI4OC0zNmJkZmE0YjQzM2QiLCJ0eXAiOiJyZWZyZXNoIn0.cBn5Mm8yHAX4wABsqhD6EohiHJRCRDMbJruHaObPJ7WYglkMjRQ2JuEmPTqBRjMJQvsx-eLlnGPmDLIGhoPi0du_V0UrXVBrbZrA8V4kELMGmJlnR-eNptpJIzrQVhLxyh6AhvxPEqZMI5xqKFEF6ealbnbEcazc8x2BHIaQZPeTjHouZkB5AHKsZyAjByVLz_7nGSG0ziW5iBNSyMNE-Tn6fS2lmhk0_IIetJYT_10TWAIRNXxiBnYJeFO18yBhzqupQYBvXWKTn84WsJNMGq7qiUxWQwV8E6a_SsgCuvk04oTzXDG8_mV4MiEYd16wz52u9DyHP_2JSeHICaSefg"
}
```
{% endtab %}
{% endtabs %}

## Additional flows

### Secure code flow

For more protection we have an addition flow which can receive secret code and exchange it for an access token. This flow consists of few steps. At first you need obtain `code`with redirect flow. Redirect URI must  previously set in client registration.

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


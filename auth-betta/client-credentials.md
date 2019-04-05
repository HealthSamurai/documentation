# Client Credentials Grant

### Description

The Client Credentials grant is used when applications request an access token to access their own resources, not on behalf of a user \(for example, background services and daemons\). It must be used only by confidential clients.

![Basic scheme](../.gitbook/assets/untitled-diagram.svg)

[Aidbox](https://www.health-samurai.io/aidbox) OAuth module support Client Credentials Grant flow in different formats. First- Strict adherence to specifications for better compatibility. Second - JSON request as a more modern and simple way. Also we have availability send all data in POST Body. Read official [OAuth2.0 specification](https://tools.ietf.org/html/rfc6749#section-4.4) for more details.

## OAuth2.0 RFC Specification way

As described in [OAuth2.0 specification](https://tools.ietf.org/html/rfc6749#section-4.4) client credentials should be presented via Authorization Basic header, and `body` should be in `application/x-www-form-urlencoded` format and `grant_type` parameter value MUST be set to `client_credentials`

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
value must be set of `client_credentials`
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
       "access_token":"2YotnFZFEjr1zCsicMWpAA" (required),
       "token_type":"example" (required),
       "expires_in":3600 (optional)
}
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

### Example

{% tabs %}
{% tab title="Request" %}
```javascript
curl -X POST \
  http://localhost:8081/auth/token \
  -H 'Authorization: Y2MtY2xpZW50OnZlcnlzZWNyZXRzZWNyZXQ=' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'grant_type=client_credentials'
```
{% endtab %}

{% tab title="Response" %}
```text
Will be here after implementation
```
{% endtab %}
{% endtabs %}

## JSON/Body parameters request  way

You need specify `client_id`, `client_secret` and `grant_type` value MUST be set to `client_credentials` . All parameters is required.

{% api-method method="post" host="\[base\]/auth/" path="token" %}
{% api-method-summary %}
Access token endpoint
{% endapi-method-summary %}

{% api-method-description %}
Obtaining `access_token`, all described parameters packed into JSON
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-headers %}
{% api-method-parameter name="Content-Type" type="string" required=true %}
application/json
{% endapi-method-parameter %}
{% endapi-method-headers %}

{% api-method-body-parameters %}
{% api-method-parameter name="scope" type="string" required=false %}
requested scope
{% endapi-method-parameter %}

{% api-method-parameter name="grant\_type" type="string" required=true %}
value must be set of `client_credentials`
{% endapi-method-parameter %}

{% api-method-parameter name="client\_secret" type="string" required=true %}
client secret key
{% endapi-method-parameter %}

{% api-method-parameter name="client\_id" type="string" required=true %}
client ID
{% endapi-method-parameter %}
{% endapi-method-body-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}

{% endapi-method-response-example-description %}

```javascript
Content-Type: application/json;charset=UTF-8
Cache-Control: no-store
Pragma: no-cache

{
       "access_token":"2YotnFZFEjr1zCsicMWpAA" (required),
       "token_type":"example" (required),
       "expires_in":3600 (optional)
}
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

Also you have an ability pass all parameters in form-data

### Example

{% tabs %}
{% tab title="JSON request" %}
```javascript
curl -X POST \
  http://localhost:8081/auth/token \
  -H 'Content-Type: application/json' \
  -d '{
    "grant_type": "client_credentials",
    "client_secret": "verysecretsecret",
    "client_id": "cc-client"
}'
```
{% endtab %}

{% tab title="Form-data request" %}
```javascript
curl -X POST \
  http://localhost:8081/auth/token \
  -d 'grant_type=client_credentials&client_secret=verysecretsecret&client_id=cc-client'
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
    "token_type": "Bearer",
    "access_token": "NDE0ZGIyYjQtMzNjZi00ZWQwLWFiNDYtNDMyNjI5NzhlODQ0"
}
```
{% endtab %}
{% endtabs %}


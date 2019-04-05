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

### 

### Get Access Token

Next step is to get Access Token using Basic & form-url-encode:

{% code-tabs %}
{% code-tabs-item title="using-basic" %}
```text
POST /auth/token
Authorization: Basic base64(client.id, client.secret)
Content-Type: application/x-www-form-urlencoded

grant_type=client_credentials
```
{% endcode-tabs-item %}
{% endcode-tabs %}

or passing all params in  JSON request body:

{% code-tabs %}
{% code-tabs-item title="json-request" %}
```text
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

## OAuth2.0 RFC Specification way

As described in [OAuth2.0 specification](https://tools.ietf.org/html/rfc6749#section-4.4) client credentials should be presented via Authorization Basic header, and `body` should be in `application/x-www-form-urlencoded` format and `grant_type` parameter value MUST be set to `client_credentials`

```text

```

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


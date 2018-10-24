# Client Credentials Grant

Aidbox OAuth module support Client Credentials Grant flow in two formats. First- Strict adherence to specifications for better compatibility. Second - JSON request as a more modern and simple way. Read official [OAuth2.0 specification](https://tools.ietf.org/html/rfc6749#section-4.4) for more details

## **JSON Request**

In JSON Request you need specify `client_id`, `client_secret` and `grant_type` value MUST be set to `client_credentials` .  All parameters is required.

{% api-method method="post" host="\[base\]" path="/oauth2/token" %}
{% api-method-summary %}
Token Endpoint
{% endapi-method-summary %}

{% api-method-description %}
Get `access_access` token via Client Credentials Grant.
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-body-parameters %}
{% api-method-parameter name="grant\_type" type="string" required=true %}
Value MUST be set to `client_credentials`
{% endapi-method-parameter %}

{% api-method-parameter name="client\_secret" type="string" required=true %}
Client secret
{% endapi-method-parameter %}

{% api-method-parameter name="client\_id" type="string" required=true %}
Client Id
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
    "access_token": "cd97340505f2-42c4-4267-a472-cd97340505f2",
    "token_type": "bearer"
}
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

### Example:

{% tabs %}
{% tab title="Request" %}
```bash
curl -X POST \
  http://localhost:8888/oauth2/token \
  -H 'content-type: application/json' \
  -d '{"client_id":"web-app","client_secret":"client-secret", "grant_type":"client_credentials"}'
```
{% endtab %}

{% tab title="Response" %}
```bash
STATUS: 200
{
    "access_token": "sdffa3c18c-42c4-4267-a472-cd97340505f2",
    "token_type": "bearer"
}
```
{% endtab %}
{% endtabs %}

## OAuth2.0 RFC Specification

As described in [OAuth2.0 specification](https://tools.ietf.org/html/rfc6749#section-4.4) client credentials  should be presented via Authorization Basic header, and `body` should be in `application/x-www-form-urlencoded` format  and `grant_type` parameter value MUST be set to `client_credentials`.

{% api-method method="post" host="\[base\]" path="/oauth2/token" %}
{% api-method-summary %}
Token Endpoint
{% endapi-method-summary %}

{% api-method-description %}
Get `access_token` via Client Credentials Grant
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-headers %}
{% api-method-parameter name="Content-Type" type="string" required=true %}
application/x-www-form-urlencoded
{% endapi-method-parameter %}

{% api-method-parameter name="Authorization" type="string" required=true %}
Basic credentials of client
{% endapi-method-parameter %}
{% endapi-method-headers %}

{% api-method-form-data-parameters %}
{% api-method-parameter name="grant\_type" type="string" required=true %}
Value MUST be set to `client_credentials`
{% endapi-method-parameter %}
{% endapi-method-form-data-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}
Return `access_token`
{% endapi-method-response-example-description %}

```javascript
{
    "access_token": "f2a3c18c-42c4-4267-a472-cd97340505f2",
    "token_type": "bearer"
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
  http://localhost:8888/oauth2/token \
  -H 'Authorization: Basic d2ViLWFwcDpjbGllbnQtc2VjcmV0' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'grant_type=client_credentials'
```
{% endtab %}

{% tab title="Response" %}
```bash
STATUS: 200
{
    "access_token": "f2a3c18c-42c4-4267-a472-cd97340505f2",
    "token_type": "bearer"
}
```
{% endtab %}
{% endtabs %}




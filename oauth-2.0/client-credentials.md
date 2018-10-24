# Client Credentials Grant

Aidbox OAuth module support Client Credentials Grant flow in two formats. First- Strict adherence to specifications for better compatibility. Second - JSON request as a more modern and simple way.

### **JSON Request**

In JSON Request you need specify `client_id`, `client_secret` and `grant_type`. All parameters is required.

| Parameter | Description |
| :--- | :--- |
| client\_id | Id of Client |
| client\_secret | Client secret |
| grant\_type | Value MUST be set to  `client_credentials` |

{% tabs %}
{% tab title="Request" %}
```bash
curl -X POST \
  http://localhost:7777/oauth2/token \
  -H 'content-type: application/json' \
  -d '{"client_id":"web-app","client_secret":"client-secret", grant_type":"client_credentials"}'
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

### OAuth2.0 RFC Specification

As described in [OAuth2.0 specification](https://tools.ietf.org/html/rfc6749#section-4.4) client credentials  should be presented via Authorization Basic header, and `body` should be in `application/x-www-form-urlencoded` format  and `grant_type` parameter value MUST be set to `client_credentials`.

{% tabs %}
{% tab title="Request" %}
```bash
curl -X POST \
  http://localhost:7777/oauth2/token \
  -H 'Authorization: Basic d2ViLWFwcDpjbGllbnQtc2VjcmV0' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d grant_type=client_credentials
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




# Resource Owner Credentials

Aidbox OAuth module support Resource Owner Credentials flow in two formats. First- Strict adherence to specifications for better compatibility. Second - JSON request as a more modern and simple way. Read official [OAuth2.0 specification](https://tools.ietf.org/html/rfc6749#section-4.3) for more details

### JSON Request

In JSON Request you need specify `client_id`, `client_secret` ,  `grant_type` ,  `username` and `password` .  All parameters is required.

| Parameter | Description |
| :--- | :--- |
| username | User identification email |
| password | User password |
| client\_id | Id of Client |
| client\_secret | Client secret |
| grant\_type | Value MUST be set to  `password` |

{% tabs %}
{% tab title="Request" %}
```bash
curl -X POST \
  http://localhost:7777/oauth2/token \
  -H 'content-type: application/json' \
  -d '{"client_id":"web-app","client_secret":"client-secret", 
       "username": "admin@example.com", "password": "long-user-password",
       "grant_type":"password"}'
```
{% endtab %}

{% tab title="Response" %}
```bash
STATUS: 200
{
    "access_token": "cd97340505f2-42c4-4267-a472-cd97340505f2",
    "token_type": "bearer"
}
```
{% endtab %}
{% endtabs %}

### OAuth2.0 RFC Specification

As described in [OAuth2.0 specification](https://tools.ietf.org/html/rfc6749#section-4.4) client credentials  should be presented via Authorization Basic header, and `body` should be in `application/x-www-form-urlencoded` format  and `grant_type` , `username` and `password` parameter. `grant_type` parameter value MUST be set to `password`.

```text
POST [base]/oauth2/token
     Authorization: Basic d2ViLWFwcDpjbGllbnQtc2VjcmV0
     Content-Type: application/x-www-form-urlencoded
     
grant_type=password&username=admin@example.com&password=long-user-password
```




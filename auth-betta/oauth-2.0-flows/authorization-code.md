# Authorization Code Grant

#### Description

The Authorization Code Grant ****is an OAuth 2.0 flow that regular web apps use in order to access an API, typically as Web applications with backend and frontend. For more detailed information read [OAuth2.0 specifcation](https://tools.ietf.org/html/rfc6749#section-4.1). This flow is also applied for a browser-based application \(SPA\) and it didn't use `client-secret`, because the source code is available in a browser - it isn't secure. Instead of this user authorizes the application and  redirected back to the application with a temporary code in the URL. The application exchanges that code for the access token. 

![Basic scheme](../../.gitbook/assets/untitled-diagram-page-3.svg)

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

```
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

![Example](../../.gitbook/assets/screenshot-2019-02-11-18.15.41.png)

Next step - user authentication and granting access.

![Example](../../.gitbook/assets/screenshot-2019-02-11-19.47.39.png)

  
Assuming the resource owner grants access, the authorization server redirects the user-agent back to the client using the redirection URI provided earlier \(in the request or during client registration\). The redirection URI includes an authorization code and any local state provided by the client earlier.

### Access Token Request

The `client` requests an access token from the authorization server's token endpoint by including the authorization `code` received in the previous step. When making the request, the client authenticates with the authorization server. The client includes the redirection URI used to obtain the authorization code for verification.

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


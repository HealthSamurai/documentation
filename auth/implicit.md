# Implicit Grant

## Description

Implicit Grant flow is an alternative for Authorization Code flow. This flow instead of obtaining secure`code` just receives `access_token` in query string fragment. It's indented for client-side apps use in order to access an API, typically as Web SPA applications. For more detailed information, read [OAuth 2.0 specification](https://tools.ietf.org/html/rfc6749#section-4.2).

![Basic scheme](../.gitbook/assets/untitled-diagram-page-4.svg)

{% api-method method="get" host="\[base\]/" path="auth/authorize" %}
{% api-method-summary %}
Authorization Endpoint
{% endapi-method-summary %}

{% api-method-description %}
Obtaining access token
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-query-parameters %}
{% api-method-parameter name="state" type="string" required=false %}
a value used by the client to maintain state between the request and callback
{% endapi-method-parameter %}

{% api-method-parameter name="scope" type="string" required=false %}
scope of the access request
{% endapi-method-parameter %}

{% api-method-parameter name="redirect\_uri" type="string" required=false %}
client redirect URI
{% endapi-method-parameter %}

{% api-method-parameter name="client\_id" type="string" required=true %}
client ID
{% endapi-method-parameter %}

{% api-method-parameter name="response\_type" type="string" required=true %}
value MUST be set to `token`
{% endapi-method-parameter %}
{% endapi-method-query-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=302 %}
{% api-method-response-example-description %}
Redirect
{% endapi-method-response-example-description %}

```text
[redirect_uri]#access_token=YzI3ZjQ1M2MtYzFlYi00ZjI3LWI2MzgtOTQ0MWI0ZmIzZjBi&state=eyJoYXNoIjoiIy9pbXBsaWNpdC9iYXNpYyIsImZvcm0tZGF0YSI6eyJ0eXBlIjoiYmFzaWMiLCJiYXNpYyI6eyJjbGllbnQtaWQiOiJpbXAtY2xpZW50In19LCJmb3JtLXBhdGgiOiJpbXBsaWNpdC1wYWdlIn0%3D
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

After this request resource owner \(user\) will be redirected to Log-in/Sign-up page.

![Example](../.gitbook/assets/screenshot-2019-02-11-18.15.41.png)

Next step is granting access to the client:

![Example](../.gitbook/assets/screenshot-2019-02-11-19.47.39.png)

After granting access user is redirected to the redirect\_uri from the client configuration with `access_token` in query string fragment.

## Example

{% tabs %}
{% tab title="Create client" %}
```yaml
POST /Client

id: imp-client
resourceType: Client
grant_types:
- implicit
auth:
  implicit:
    redirect_uri: http://localhost:3449/auth.html
```
{% endtab %}
{% endtabs %}

{% tabs %}
{% tab title="Request access token" %}
```bash
curl -X GET \
  'http://localhost:8081/auth/authorize?
        state=example
        &client_id=imp-client
        &redirect_uri=http%3A%2F%2Flocalhost%3A3449%2Fauth.html
        &response_type=token'
```
{% endtab %}

{% tab title="Response" %}
```text
HTTP/1.1 302 Found

Location: http://localhost:3449/auth.html#access_token=ZGE0ZmQzZTYtOGU0OC00MDJhLWFkN2ItZTg5ZmViYjdmNTQ2
&state=example
```
{% endtab %}
{% endtabs %}


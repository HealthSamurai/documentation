# Implicit Grant

## Description

Implicit Grant flow is an alternative for Authorization Code flow. This flow just receives `access_token` in query string fragment instead of obtaining secure`code`. It's indented for client-side apps use in order to access an API, typically as Web SPA applications. For more detailed information, read [OAuth 2.0 specification](https://tools.ietf.org/html/rfc6749#section-4.2).

![Basic scheme](../../.gitbook/assets/untitled-diagram-page-4.svg)

{% swagger baseUrl="[base]/" path="auth/authorize" method="get" summary="Authorization Endpoint" %}
{% swagger-description %}
Obtaining access token
{% endswagger-description %}

{% swagger-parameter in="query" name="state" type="string" %}
a value used by the client to maintain state between the request and callback
{% endswagger-parameter %}

{% swagger-parameter in="query" name="scope" type="string" %}
scope of the access request
{% endswagger-parameter %}

{% swagger-parameter in="query" name="redirect_uri" type="string" %}
client redirect URI
{% endswagger-parameter %}

{% swagger-parameter in="query" name="client_id" type="string" %}
client ID
{% endswagger-parameter %}

{% swagger-parameter in="query" name="response_type" type="string" %}
value MUST be set to 

`token`
{% endswagger-parameter %}

{% swagger-response status="302" description="Redirect" %}
```
[redirect_uri]#access_token=YzI3ZjQ1M2MtYzFlYi00ZjI3LWI2MzgtOTQ0MWI0ZmIzZjBi&state=eyJoYXNoIjoiIy9pbXBsaWNpdC9iYXNpYyIsImZvcm0tZGF0YSI6eyJ0eXBlIjoiYmFzaWMiLCJiYXNpYyI6eyJjbGllbnQtaWQiOiJpbXAtY2xpZW50In19LCJmb3JtLXBhdGgiOiJpbXBsaWNpdC1wYWdlIn0%3D
```
{% endswagger-response %}
{% endswagger %}

After this request, the resource owner (user) will be redirected to Log-in/Sign-up page.

![Example](../../.gitbook/assets/screenshot-2019-02-11-18.15.41.png)

Next step is granting access to the client:

![Example](../../.gitbook/assets/screenshot-2019-02-11-19.47.39.png)

After granting access the user is redirected to the redirect\_uri from the client configuration with `access_token` in query string fragment.

## Example

{% tabs %}
{% tab title="Create client" %}
```yaml
POST /Client
Accept: text/yaml
Content-Type: text/yaml

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
```
HTTP/1.1 302 Found

Location: http://localhost:3449/auth.html#access_token=ZGE0ZmQzZTYtOGU0OC00MDJhLWFkN2ItZTg5ZmViYjdmNTQ2
&state=example
```
{% endtab %}
{% endtabs %}

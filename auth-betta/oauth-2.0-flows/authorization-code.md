# Authorization Code Grant

#### Description

The Authorization Code Grant ****is an OAuth 2.0 flow that regular web apps use in order to access an API, typically as Web applications with backend and frontend. For more detailed information read [OAuth2.0 specifcation](https://tools.ietf.org/html/rfc6749#section-4.1). This flow is applied for a browser-based application \(SPA\) and it didn't use `client-secret`, because the source code is available in a browser - it isn't secure. Instead of this user authorizes the application, they are redirected back to the application with a temporary code in the URL. The application exchanges that code for the access token. 

## Authorization Request

The client initiates the flow by directing the resource owner's user-agent to the authorization endpoint. The client includes its client identifier, requested scope, local state, and a redirection URI to which the authorization server will send the user-agent back once access is granted \(or denied\).

{% api-method method="get" host="\[base\]" path="/auth/authorize" %}
{% api-method-summary %}
Authorization Endpoint
{% endapi-method-summary %}

{% api-method-description %}

{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-query-parameters %}
{% api-method-parameter name="client\_id" type="string" required=true %}
Client ID
{% endapi-method-parameter %}
{% endapi-method-query-parameters %}
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


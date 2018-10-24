# Well Known endpoint

{% api-method method="get" host="\[base\]" path="/.well-known/openid-configuration" %}
{% api-method-summary %}
OpenID Connect Discovery
{% endapi-method-summary %}

{% api-method-description %}
Exposes OIDC discovery documents
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}

{% endapi-method-response-example-description %}

```javascript
{
    grant_types_supported: [
        "authorization_code",
        "implicit",
        "password",
        "client_credentials"
    ],
    //userinfo_endpoint: "http://localhost:8888/oauth2/userinfo",
    //token_endpoint_auth_methods_supported: [
    //    "client_secret_post",
    //   "client_secret_basic",
    //    "client_secret_jwt",
    //    "private_key_jwt"
    //],
    claims_supported: [
        "profile",
        "openid",
        "exp",
        "family_name",
        "given_name",
        "iat",
        "iss",
        "locale",
        "name",
        "picture",
        "sub"
    ],
    subject_types_supported: [
        "public"
    ],
    authorization_endpoint: "http://localhost:8888/oauth2/authoraize",
    scopes_supported: [
        "openid",
        "profile",
        "email",
        "groups"
    ],
    issuer: "http://localhost:8888",
    response_types_supported: [
        "code",
        "token",
        "token id_token",
        "code id_token"
    ],
    token_endpoint: "http://localhost:8888/oauth2/token",
    id_token_signing_alg_values_supported: [
        "RS256"
    ]
}
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}


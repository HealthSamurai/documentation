---
description: Aidbox as a Resource Server
---

# External OAuth 2.0 Providers

Aidbox can validate access tokens issued by 3rd-party servers. This way Aidbox acts as a Resource Server and leaves Identity management to a separate server.

To configure Aidbox as a Resource Server, you need to create one or more instances of `TokenIntrospector` resource. `TokenIntrospector` defines how access token validation is performed. There are two different algorithms to validate tokens:

1. JWT validation according to [RFC-7519](https://tools.ietf.org/html/rfc7519)
2. opaque token introspection according to [RFC-7662](https://tools.ietf.org/html/rfc7662)

## Validating JWT Access Tokens

For JWT validation, you need to specify either JWKs endpoint URL ([RFC-7517](https://tools.ietf.org/html/rfc7517)) or a pre-shared secret string if tokens are signed with HS256 algorithm:

```yaml
resourceType: TokenIntrospector
type: jwt
jwks_uri: https://.../.well-known/jwks.json
jwt:
  iss: https://my-provider.example # shoud be equal to `iss` claim of the JWT
  secret: "xxxxxxxx"               # pre-shared key if JWT alg = HS256
```

When Aidbox validates the JWT token, it tries to find a matching `TokenIntrospector` using `jwt.iss` and `type` attributes. If suitable TokenIntrospector is found, token is being validated with either JWK obtained from `jwks_uri` or with `jwt.secret`, depending on the signing algorithm. Token expiration (`exp` claim) is also being checked.

If JWT is valid, Aidbox will put it's claims into the request object under `jwt` key, so you'll be able to access them with [AccessPolicy checks](../security/access-control.md). If the token failed validation (it's expired or signature isn't correct) then the client will get a 401 "Unauthorised" response.

## Validating Opaque (non-JWT) Tokens

When using old-fashioned (opaque) tokens, Aidbox can be configured to call a special endpoint with every new access token it receives. This endpoint, called a token introspection endpoint, returns information about access token - scopes, username and client ID associated with it, expiration time, etc. Most importantly, it tells if access token is active or not.

To configure Aidbox in this way, create TokenIntrospector instance with `opaque` type and `introspection_endpoint` attribute:

```yaml
resourceType: TokenIntrospector
type: opaque
introspection_endpoint:
  url: https://.../oauth/token_introspection  # endpoint URL
  authorization: Bearer xxxyyyzzz             # value for Authorization header
```

According to [RFC-7662](https://tools.ietf.org/html/rfc7662), the only required attribute in token introspection's response is `active`. Aidbox uses this attribute to consider if token is valid or not. If token is valid, entire token introspection's response will be put into the request's object under `token` key, so you'll be able to use it in AccessPolicy checks. If token isn't valid, Aidbox will try to validate access token against currently active local sessions.

## X-Client-Auth

In some situations (like micro-services), you want to add middle-ware client authentication. You can use the **X-Client-Auth** header with basic auth value for client id and secret to add client authentication to the JWT workflow.

```yaml
GET /Patient?_debug=query
Authorization: Bearer <your-jwt>
X-Client-Auth: Basic <basicEncode(Client.id, Client,secret)>

# 200

request:
  client: .....
  jwt: ....

```

## Create a user automatically

{% hint style="warning" %}
It works with `JWT` token introspection only
{% endhint %}

You can configure Aidbox to create user from access token automatically. When Aidbox encounters token, it tries to resolve a user. And if the user is not found, Aidbox gets user info from the identity provider and creates it.

To configure automatic user creation follow these **two steps**:

#### 1. Define ENV variable&#x20;

```
BOX_FEATURES_AUTHENTICATION_INTROSPECTION_CREATE__USER=true
```

#### 2. Add identity\_provider to the TokenIntrospector resource

```yaml
# ...
identity_provider:
  id: <identity-provider-id>
  resourceType: IdentityProvider
# ...
```

Here `<identity-provider-id>` is the id of the `IdentityProvider` resource which issues tokens.

## Examples

Access control with Validating JWT Access Tokens and AccessPolicy

Create `TokenInspector`

```yaml
resourceType: TokenIntrospector
type: jwt
jwks_uri: https://.../.well-known/jwks.json
jwt:
  iss: https://my-provider.example # shoud be equal to `iss` claim of the JWT
  secret: "xxxxxxxx"               # pre-shared key if JWT alg = HS256
```

Your JWT token should contain `sub` attribute that equal `User.id` on your box.

```yaml
# JWT sample
...
claims:
  ...
  sub: box-user-id
```

Also you can put box user id in to `box_user` claim attribute. This makes sense when you use external oauth provider or any other identity system that manages `sub` attribute itself. In this case, you can put the box user id in to `box_user` .

```yaml
# JWT sample
...
claims:
  ...
  sub: some-user-id-on-external-system
  box_user: box-user-id
```

When Aidbox receives request with JWT and `box_user` or `sub` attribute, Aidbox injects this user and their roles to the request. Now we can create some `AccessPolicy`.

```yaml
# AccessPolicy example
resourceType: AccessPolicy
engine: allow
link:
  - resourceType: User
    id: box-user-id
```

## How to set-up identity provider

{% content-ref url="../how-to-guides/set-up-external-identity-provider/" %}
[set-up-external-identity-provider](../how-to-guides/set-up-external-identity-provider/)
{% endcontent-ref %}

---
description: >-
  Aidbox can validate access tokens issued by 3rd-party servers. This way Aidbox
  acts as a Resource Server and leaves Identity management to a separate server.
---

# Validating Foreign Access Tokens

To configure Aidbox as a Resource Server, you need to create one or more instances of TokenIntrospector resource. TokenIntrospector resources defines how access token validation will be performed. There are two different algorithms to validate tokens: JWT validation according to [RFC-7519](https://tools.ietf.org/html/rfc7519) and opaque token introspection according to [RFC-7662](https://tools.ietf.org/html/rfc7662). 

### Validating JWT Access Tokens

For JWT validation, you need to specify either JWKs endpoint URL \([RFC-7517](https://tools.ietf.org/html/rfc7517)\) or a pre-shared secret string if tokens are signed with HS256 algorithm:

```yaml
resourceType: TokenIntrospector
type: jwt
jwks_uri: https://.../.well-known/jwks.json
jwt:
  iss: https://my-provider.example # shoud be equal to `iss` claim of the JWT
  secret: "xxxxxxxx"               # pre-shared key if JWT alg = HS256
```

When Aidbox validates JWT token, it tries to find matching TokenIntrospector using `jwt.iss` and `type` attributes. If suitable TokenIntrospector found, token is being validated with either JWK obtained from `jwks_uri` or with `jwt.secret`, depending on signing algorithm. Token expiration \(`exp` claim\) is also being checked.

If JWT is valid, Aidbox will put it's claims into the request object under `jwt` key, so you'll be able to access them with [AccessPolicy checks](../security/access-control.md). If token failed validation \(it's expired or signature isn't correct\) then client will get an 401 "Unauthorised" response.

### Validating Opaque \(non-JWT\) Tokens

When using old-fashioned \(opaque\) tokens, Aidbox can be configured to call a special endpoint with every new access token it receives. This endpoint, called a token introspection endpoint, returns information about access token - scopes, username and client ID associated with it, expiration time, etc. Most importantly, it tells if access token is active or not.

To configure Aidbox in this way, create TokenIntrospector instance with `opaque` type and `introspection_endpoint` attribute:

```yaml
resourceType: TokenIntrospector
type: opaque
introspection_endpoint:
  url: https://.../oauth/token_introspection  # endpoint URL
  authorization: Bearer xxxyyyzzz             # value for Authorization header
```

According to [RFC-7662](https://tools.ietf.org/html/rfc7662), the only required attribute in token introspection's response is `active`. Aidbox uses this attribute to consider if token is valid or not. If token is valid, entire token introspection's response will be put into the request's object under `token` key, so you'll be able to use it in AccessPolicy checks. If token isn't valid, Aidbox will try to validate access token against currently active local sessions.




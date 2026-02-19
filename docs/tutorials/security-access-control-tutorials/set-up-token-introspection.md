---
description: This guide explains how to configure Aidbox to trust external JWT
---

# Set-up token introspection

Token introspection is the setup when Aidbox trusts JWT issued by external server.

{% hint style="info" %}
In this guide external auth server URL is `https://auth.example.com`
{% endhint %}

## JWT Token

### Create `TokenIntrospector`

Aidbox provides three ways to validate JWT tokens with `TokenIntrospector` resource:

#### 1. Token secret

Set `jwt.secret` property to the secret value used to sign the JWT.

```http
PUT /TokenIntrospector/external-auth-server
content-type: text/yaml

resourceType: TokenIntrospector
id: external-auth-server
type: jwt
jwt:
  iss: https://auth.example.com
  secret: very-secret
```

#### 2. JWKS URI

Set `jwks_uri` property to the URL of the JWKS endpoint.

```http
PUT /TokenIntrospector/external-auth-server
content-type: text/yaml

resourceType: TokenIntrospector
id: external-auth-server
type: "jwt"
jwt:
  iss: <AUTH_SERVER_URL>
jwks_uri: <AUTH_SERVER_URL>/.well-known/jwks.json"
```

#### 3. Cryptographic keys

{% hint style="warning" %}
Cryptographic keys functionality is available starting from version 2505.
{% endhint %}

TokenIntrospector allows you to use `RSA` `EC` `OCT` keys to validate a JWT token.

```http
PUT /TokenIntrospector/external-auth-server
content-type: text/yaml

resourceType: TokenIntrospector
id: external-auth-server
resourceType: TokenIntrospector
id: with-keys
type: jwt
jwt:
  iss: <AUTH_SERVER_URL>
  keys:
    - pub: <RSA_PUBLIC_KEY>
      kty: RSA
      alg: RS256
      format: PEM
    - pub: <RSA_PUBLIC_KEY>
      kty: RSA
      alg: RS384
      format: PEM
    - pub: <EC_PUBLIC_KEY>
      kty: EC
      alg: ES256
      format: PEM
    - k: <OCT_SECRET>
      kty: OCT
      alg: HS256
      format: plain
```

{% hint style="warning" %}
Only one of the options listed above can be configured for each TokenIntrospector resource.
{% endhint %}

### Configure Cache TTL (Optional)

The `cache_ttl` property controls how long Aidbox caches validation data. This is important for handling temporary unavailability of external endpoints.

{% hint style="info" %}
**Important: What gets cached?**

- **JWT with JWKS URI**: `cache_ttl` caches the **public keys** fetched from the JWKS endpoint, not the JWT tokens themselves. Each JWT is still validated on every request, but using cached public keys. Different tokens can use the same cached keys.

- **Opaque tokens**: `cache_ttl` caches the **introspection result for each specific token**. The cache key includes the token value itself, meaning each unique token has its own cache entry. If the same token is used in multiple requests within the cache TTL, the introspection result is reused from cache.

- **ASPXAUTH tokens**: Similar to opaque tokens - caches the **introspection result for each specific cookie value**. Each unique cookie has its own cache entry.
{% endhint %}

**Configuration:**

```yaml
cache_ttl: 3600  # Cache duration in seconds
```

**Valid range:** 1-86400 seconds (1 second to 24 hours)
**Default:** 300 seconds (5 minutes)

#### Why configure cache TTL?

When using JWKS URI or introspection endpoints, Aidbox needs to fetch data from external servers. If these endpoints become temporarily unavailable:

- **Without caching:** Aidbox fails to validate tokens immediately when the endpoint is down
- **With longer cache:** Aidbox continues to work using cached public keys/results until cache expires

**Example with JWKS URI:**

```http
PUT /TokenIntrospector/external-auth-server
content-type: text/yaml

resourceType: TokenIntrospector
id: external-auth-server
type: jwt
cache_ttl: 3600  # Cache public keys for 1 hour
jwt:
  iss: https://auth.example.com
jwks_uri: https://auth.example.com/.well-known/jwks.json
```

**Example with Opaque Token:**

```http
PUT /TokenIntrospector/external-auth-server
content-type: text/yaml

resourceType: TokenIntrospector
id: external-auth-server
type: opaque
cache_ttl: 7200  # Cache introspection results for 2 hours
introspection_endpoint:
  url: https://auth.example.com/introspect
  authorization: Basic base64credentials
```

{% hint style="warning" %}
**Security considerations:**

- **JWT with JWKS URI**: Longer cache TTL means revoked or rotated public keys will continue to be used until cache expires. However, individual JWT tokens are still validated (expiration, signature) on every request.

- **Opaque/ASPXAUTH tokens**: Longer cache TTL means a revoked token will continue to work in Aidbox until its cache entry expires. If you revoke a token at the identity provider, it can still be used in Aidbox for up to `cache_ttl` seconds.

Balance availability needs with security requirements when choosing cache duration.
{% endhint %}

### Define `AccessPolicy`

```http
PUT /AccessPolicy/external-auth-server
content-type: text/yaml

resourceType: AccessPolicy
id: external-auth-server
engine: json-schema
schema:
  required:
    - jwt
  properties:
    jwt:
      required:
        - iss
      properties:
        iss:
          constant: https://auth.example.com
```

### Create `User`

```http
PUT /User/some-user-id
content-type: text/yaml

resourceType: User
id: some-user-id
data:
  id: basic
  sub: basic
  email: basic@example.com
```

### Validating introspector works

Build `JWT`

Use [this tool](http://jwtbuilder.jamiekurtz.com/) to build your JWT. Mind the claims:

* `issuer` should be `https://auth.example.com`
* `expiration` should be in the future
* `subject` should be `basic` (user id)
* `key` should be `very-secret` string

Press `Create Signed JWT` button to get signed JWT. The generated `JWT` looks like this

```
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE2NTc4ODA4NjMsImV4cCI6MTY4OTQxNjg2MywiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoianJvY2tldEBleGFtcGxlLmNvbSIsIkdpdmVuTmFtZSI6IkpvaG5ueSIsIlN1cm5hbWUiOiJSb2NrZXQiLCJFbWFpbCI6Impyb2NrZXRAZXhhbXBsZS5jb20iLCJSb2xlIjpbIk1hbmFnZXIiLCJQcm9qZWN0IEFkbWluaXN0cmF0b3IiXX0.TvlrkjPfNAATDW6tHOcgRh3ZNl2tYpUPkFBS_UjU6TY
```

### Use the `JWT` to get the access

Make an HTTP request providing `authorization` header with the `JWT` as a `Bearer` token.

```http
GET /fhir/Patient
Authorization: Bearer eyJ0...U6TY
```

## Opaque Token

### Create Token Introspector

with `introspection_endpoint`:

```http
PUT /TokenIntrospector/external-auth-server
content-type: text/yaml

resourceType: TokenIntrospector
id: external-auth-server
type: opaque
introspection_endpoint:
  url: <token-introspection-endpoint>
  authorization: <authorization-header>
```

### Create AccessPolicy

```http
PUT /AccessPolicy/external-auth-server
content-type: text/yaml

engine: matcho
matcho:
  // Everything Aidbox retrieved from  introspection_endpoint
  // will be available under `token` in AccessPolicy
  token:
    active: true
resourceType: AccessPolicy
```

See all available [TokenIntrospector properties](../../reference/system-resources-reference/core-module-resources.md).

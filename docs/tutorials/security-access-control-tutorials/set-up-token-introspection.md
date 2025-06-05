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

See all available [TokenIntrospector properties](../../reference/system-resources-reference/iam-module-resources.md).

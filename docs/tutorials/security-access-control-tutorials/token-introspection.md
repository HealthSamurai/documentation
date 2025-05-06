---
description: This guide explains how to configure Aidbox to trust external JWT
---

# Set-up token introspection

Token introspection is the setup when Aidbox trusts `JWT` issued by external server.

{% hint style="info" %}
In this guide external auth server URL is `https://auth.example.com`
{% endhint %}

## JWT Token

### Create `TokenIntrospector`

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

The other example would be:&#x20;

```html
PUT /TokenIntrospector/external-auth-server
content-type: text/yaml

resourceType: TokenIntrospector
id: external-auth-server
type: "jwt"
jwt:
  iss: <AUTH_SERVER_URL>
jwks_uri: <AUTH_SERVER_URL>/.well-known/jwks.json"
```

{% hint style="warning" %}
Currently we use common `secret` to validate our introspector works. In production installations it's better to switch to `jwks_uri` instead
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

### Create Token Introspector&#x20;

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

All available `TokenIntrospector` attributes:

{% content-ref url="broken-reference" %}
[Broken link](broken-reference)
{% endcontent-ref %}

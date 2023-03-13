---
description: >-
  This guide explains how to set up Role-Based Access Control with JWT
  containing a role claim
---

# RBAC with JWT containing role

{% hint style="info" %}
This guide is based on the [TokenInstrospector tutorial](../token-introspection.md). But we won't' create `User` resource
{% endhint %}

## Token introspection

To make Aidbox trust `JWT` issued by external server token introspection is used.

{% hint style="info" %}
In this guide external auth server URL is `https://auth.example.com`
{% endhint %}

## Create `TokenIntrospector`

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

{% hint style="info" %}
Currently we use a common `secret` to validate our introspector works. In production installations it's better to switch to `jwks_uri` instead
{% endhint %}

## Create `AccessPolicy` for the role `manager`

```http
PUT /AccessPolicy/as-manager-get-users-list
content-type: text/yaml

resourceType: AccessPolicy
id: as-manager-get-users-list
engine: json-schema
schema:
  required:
  - jwt
  properties:
    uri:
      enum:
        - /User
      type: string
    request-method:
      constant: get
    jwt:
      required:
        - iss
        - role
      properties:
        iss:
          constant: https://auth.example.com
        role:
          constant: manager
```

## Create `JWT` token

To build `JWT` use [this tool](http://jwtbuilder.jamiekurtz.com). Mind the claims:

* `issuer` claim should be `https://auth.example.com`
* `role` additional claim should be `manager`
* `expiration` claim should be in the future
* `subject` claim can be any value

{% hint style="info" %}
The `key` should be `very-secret`
{% endhint %}

To get signed JWT press the `Create Signed JWT` button. The generated `JWT` looks like this

```
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2F1dGguZXhhbXBsZS5jb20iLCJpYXQiOjE2NzU3NTgzMDEsImV4cCI6MTcwNzI5NDMwMSwiYXVkIjoiaHR0cHM6Ly9hdXRoLmV4YW1wbGUuY29tIiwic3ViIjoiYWxpY2VAZXhhbXBsZS5jb20iLCJyb2xlIjoibWFuYWdlciJ9.X7sibz1LloKlMPVV5Q39gSAJBxxutCORtYq4oRt1eAo
```

### Use the `JWT` to get the access

Make an HTTP request providing `authorization` header with the `JWT` as a `Bearer` token.

{% tabs %}
{% tab title="Request" %}
```javascript
GET /User
content-type: text/yaml
Authorization: Bearer eyJ0...1eAo
```
{% endtab %}

{% tab title="Response, status: 200" %}
```yaml
entry: []
link:
- relation: first
  url: "/User?page=1"
- relation: self
  url: "/User?page=1"
query-sql:
- 'SELECT "user".* FROM "user" LIMIT ? OFFSET ? '
- 100
- 0
query-time: 4
query-timeout: 60000
resourceType: Bundle
total: 0
type: searchset
```
{% endtab %}
{% endtabs %}

---
description: Validate OAuth 2.0 JWT and opaque tokens from external identity providers with TokenIntrospector resource.
---

# Token Introspector

The `TokenIntrospector` resource in Aidbox is used to validate tokens issued by external authentication systems. It enables Aidbox to integrate with various identity providers and authentication servers by supporting different token validation methods.

## Overview

When users authenticate with an external system and receive a token, Aidbox can use TokenIntrospector to validate these tokens and extract identity information. This allows for secure integration between Aidbox and external authentication systems.

## Supported Token Types

Aidbox TokenIntrospector supports two main types of tokens:

**1. JWT Tokens** — JSON Web Tokens (JWTs) are self-contained tokens that include claims and can be verified using a signature.

**2. Opaque Tokens** — Non-transparent tokens that require validation against an external introspection endpoint.

## TokenIntrospector Schema

See [TokenIntrospector schema](../../reference/system-resources-reference/core-module-resources.md#tokenintrospector).

## Token Validation Process

1. Client sends a request to Aidbox with a Bearer token
2. Aidbox identifies the token type
3. Based on the token type:
   * For JWT: Aidbox validates the signature and claims
   * For opaque: Aidbox sends the token to the introspection endpoint
4. If valid, Aidbox applies the relevant AccessPolicy
5. If the policy allows access, the request proceeds

## User Resolution

After validating a JWT token, Aidbox resolves the user context from the token claims. Aidbox looks up a `User` resource by ID and loads all `Role` resources linked to that user. The resolved `User` and `Role` data is then available in [AccessPolicy](../authorization/access-policies.md) for authorization decisions.

### How it works

1. Aidbox checks for the `box_user` custom claim in the JWT
2. If `box_user` is not present, Aidbox falls back to the standard `sub` (subject) claim
3. The resulting value is used as the `User.id` to look up the user in the database
4. All `Role` resources linked to that user are automatically loaded
5. The full `User` resource and `Role` array become available as `user` and `role` in the [AccessPolicy request object](../authorization/access-policies.md)

For example, if you have a `User` resource with custom data:

```yaml
resourceType: User
id: my-user
email: user@example.com
data:
  practitioner_id: pract-123
  department: cardiology
```

After user resolution, all of this data is available in AccessPolicy. For instance, you can restrict access based on `user.data.department`:

```yaml
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    data:
      department: cardiology
```

{% hint style="info" %}
Without user resolution, only raw JWT claims are available in AccessPolicy under the `jwt` key. User resolution is what makes the full `User` resource and `Role` data from the database available under the `user` and `role` keys.
{% endhint %}

### The `box_user` claim

By default, Aidbox uses the `sub` (subject) claim to look up `User.id`. The `box_user` custom claim allows you to override this — when present, **`box_user` takes priority over `sub`**.

This is useful when the `sub` claim contains an external identity provider ID (e.g., a UUID from Keycloak or Auth0) that does not match the `User.id` in Aidbox.

**Example JWT payload:**

```json
{
  "iss": "https://auth.example.com",
  "sub": "keycloak-uuid-1234",
  "box_user": "my-aidbox-user",
  "exp": 1700000000
}
```

In this example, Aidbox will resolve `User/my-aidbox-user` and load its data and roles into the AccessPolicy context. The `sub` claim remains available under `jwt.sub` for AccessPolicy evaluation.

## Caching

TokenIntrospector supports configurable caching via the `cache_ttl` property (in seconds):

- **JWT with JWKS URI**: Caches public keys fetched from the JWKS endpoint (default: 300 seconds)
- **Opaque tokens**: Caches introspection results for each specific token (default: 300 seconds)
- **ASPXAUTH tokens**: Caches introspection results for each specific cookie (default: 300 seconds)

Valid range: 1-86400 seconds (1 second to 24 hours)

Caching improves resilience when external endpoints are temporarily unavailable. See the tutorial for detailed configuration examples and security considerations.

## See also

* [Set up token introspection tutorial](../../tutorials/security-access-control-tutorials/set-up-token-introspection.md)
* [External Secrets](../../configuration/secret-files.md) — store `jwt.secret`, `jwt.keys.k`, and `introspection_endpoint.authorization` values outside the database

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

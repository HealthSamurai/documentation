---
title: "Token Introspection in FHIR: A Complete Guide to Modern Token Validation"
slug: "token-introspection-in-fhir"
published: "2025-08-19"
author: "Rostislav Antonov"
reading-time: "8 min"
tags: []
category: "FHIR"
teaser: "Token Introspection is the critical bridge between Authentication and Authorization. When a client presents a token to access FHIR resources, the server must check if the token is valid."
image: "cover.png"
---

## 1. Authentication, Authorization, and Token Introspection

In the world of API security, two fundamental concepts work together to protect resources: **Authentication** and **Authorization**.

**Authentication** answers the question "Who are you?" It's the process of verifying identity, confirming that users are who they claim to be. This typically happens at your Identity Provider (IdP) when users log in with credentials, biometrics, or other factors. The result is a token that represents the authenticated identity.

**Authorization** answers "What can you do?" Once identity is established, authorization determines which resources the authenticated user can access and what operations they can perform. In FHIR servers, this is where AccessPolicy resources come into play.

**Token Introspection** is the critical bridge between these two concepts. When a client presents a token to access FHIR resources, the server must check if the token is valid. This process also reveals who the token belongs to and what information it carries, so the server can make authorization decisions.

## 2. Why Token Introspection?

Modern digital ecosystems, particularly in healthcare, are complex. They often consist of numerous applications, from patient portals and EHRs to mobile apps for clinicians, each potentially connected to different Identity and Access Management (IAM) systems or IdPs.

When a FHIR server is introduced into this architecture, it shouldn't force a change in your established authentication flows. Re-architecting your IAM system for a new service is costly and disruptive. The goal is integration, not replacement. [The FHIR server](https://www.health-samurai.io/fhir-server) should be able to trust and validate tokens issued by these external authentication systems.

This is where token introspection becomes essential. It allows your FHIR server to act as a secure **resource server**, delegating the authentication process back to your trusted IdPs. When a client presents a token, the FHIR server uses introspection to answer a critical question: "Is this token authentic?" This process enables the FHIR server to protect its resources without becoming an IdP itself.

## 3. Token Introspection in Aidbox

Aidbox provides the *TokenIntrospector* resource to validate tokens issued by external IdP. It can handle two different kinds of tokens:

- **JWT (JSON Web Token):.** A self-contained token that carries user claims and is signed. TokenIntrospector verifies the signature locally and reads the claims —- no round trip to the auth server is needed.

- **Opaque token.** A random-looking string with no embedded claims. To verify it, Aidbox calls the issuer’s introspection\_endpoint (per [RFC 7662](https://datatracker.ietf.org/doc/html/rfc7662)) and asks whether the token is active and who it represents.

*TokenIntrospector* validates the token using the correct method for its format. After that, Aidbox applies *AccessPolicy* rules to decide what the requester can access.

### 3.1 Opaque Token Introspection Endpoint

Technically, Introspection Endpoint could be used for any kind of token, but it's most commonly used for opaque tokens, because JWT tokens are self-contained and have different options to be validated locally.

Aidbox implements the OAuth 2.0 Token Introspection standard (RFC 7662). When an opaque token arrives, the *TokenIntrospector* makes an HTTP POST request to the configured *introspection\_endpoint*, sending the token for validation.

```javascript
{
  "resourceType": "TokenIntrospector",
  "id": "opaque-example",
  "type": "opaque",
  "introspection_endpoint": {
    "url": "https://auth.example.com/oauth/introspect",
    "authorization": "Basic Y2xpZW50OnNlY3JldA=="
  }
}
```

The external authentication server responds with a JSON object indicating whether the token is active and includes relevant claims:

```javascript
{
  "active": true,
  "sub": "user123",
  "scope": "read write",
  "exp": 1684567890,
  "client_id": "my-client"
}
```

Aidbox receives this response and makes the claims available to *AccessPolicy* resources under the token context. This approach delegates all token validation logic to your existing identity infrastructure, while keeping Aidbox as a pure resource server.

### 3.2 Three Ways to Validate a JWT

For JWT tokens, Aidbox offers three validation methods, each suited to different scenarios. Choose based on your security requirements, infrastructure constraints, and operational needs.

#### 3.2.1 Secret-based Validation

Secret-based validation uses a shared secret for HMAC-based signatures (HS256). Your IdP signs tokens with this secret, and Aidbox verifies them using the same key.

```javascript
{
  "resourceType": "TokenIntrospector",
  "id": "simple-secret",
  "type": "jwt",
  "jwt": {
    "iss": "https://myidp.example.com",
    "secret": "your-256-bit-secret-here"
  }
}
```

This method works well for development or simple deployments. However, it requires the same secret to be stored both in your IdP and Aidbox, which can be a security concern.

#### 3.2.2 JWKS URI Validation

JSON Web Key Set (JWKS) URI validation fetches public keys from your IdP's well-known endpoint. This standard approach enables automatic key rotation without updating Aidbox configuration.

```javascript
{
  "resourceType": "TokenIntrospector",
  "id": "jwks-example",
  "type": "jwt",
  "jwt": {
    "iss": "https://myidp.example.com",
    "jwks_uri": "https://myidp.example.com/.well-known/jwks.json"
  }
}
```

Your IdP publishes its public keys at the JWKS endpoint. Aidbox fetches these keys and caches them, automatically refreshing when they change. This approach works with standard OAuth 2.0/OIDC providers and supports RSA and Elliptic Curve algorithms like RS256 and ES256.

#### 3.2.3 Cryptographic Keys (New in Aidbox since version 2505)

This method lets you specify multiple cryptographic keys directly in the *TokenIntrospector* configuration. It allows you to manage keys explicitly within Aidbox, supporting multiple algorithms and key types simultaneously.

```javascript
{
  "resourceType": "TokenIntrospector",
  "id": "multi-key-example",
  "type": "jwt",
  "jwt": {
    "iss": "https://myidp.example.com",
    "keys": [
      {
        "alg": "RS256",
        "format": "PEM",
        "pub": "-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA...\n-----END PUBLIC KEY-----\n"
      },
      {
        "alg": "ES256",
        "format": "PEM",
        "pub": "-----BEGIN PUBLIC KEY-----\nMFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE...\n-----END PUBLIC KEY-----\n"
      }
    ]
  }
}
```

This method is particularly useful in environments where direct key management is preferred or when JWKS endpoints are not available.

## 4. Deep Dive: Cryptographic Keys Configuration

### 4.1 Why Multiple Keys?

Supporting multiple keys in a single TokenIntrospector solves several real-world challenges:

- **Key Rotation**: During key rotation, you often need to support both old and new keys simultaneously. Some tokens might be signed with the previous key, while new ones use the updated key.****
- **Mixed Algorithms**: Different applications in your ecosystem might use different cryptographic algorithms. Mobile apps might prefer ES256 for performance, while web applications use RS256 for compatibility.****
- **Parallel IdP**: Large organizations often have multiple IdPs or environments. A single *TokenIntrospector* can validate tokens from different sources, each with its own keys.****
- **Gradual Migration**: When transitioning between key types or providers, you can add new keys without immediately removing old ones, ensuring zero-downtime updates.

### 4.2 Supported Key Types and Formats

Aidbox supports the following cryptographic keys and algorithms:

| Key Type ([kty](https://datatracker.ietf.org/doc/html/rfc7518#section-6.1)) | Algorithms (alg) | Format | Key Field | Use Case |
| --- | --- | --- | --- | --- |
| RSA | RS256, RS384 | PEM | pub | Wide compatibility, established standard |
| EC | ES256 | PEM | pub | Smaller keys, better performance |
| OCT | HS256 | plain | [k](https://datatracker.ietf.org/doc/html/rfc7518#section-6.4.1) | Shared secrets, symmetric signing |

**Key Configuration Rules:**

- **Asymmetric keys** (RSA, EC): Use *pub* field with *PEM* format
- **Symmetric keys** (OCT): Use *k* field with *plain* format
- **Format requirements**: *PEM* is mandatory for asymmetric algorithms, *plain* for symmetric algorithms

### 4.3 Complete JSON Example

Here's a comprehensive example showing four different keys in one TokenIntrospector:

```javascript
{
  "resourceType": "TokenIntrospector",
  "id": "comprehensive-keys",
  "type": "jwt",
  "jwt": {
    "iss": "https://myidp.example.com",
    "keys": [
      {
        "kty": "RSA",
        "alg": "RS256",
        "format": "PEM",
        "pub": "-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4f5wg5l2hKsTeNem/V41fGnJm6gOdrj8ym3rFkEjWT2btf0hEkNKsP8d9xwnSsWLXY0vhU7LWNBTSwJjJGrQ6cOq5m4eUzjbRpLo8Oez7UyO8vRrGI7w2E1+BZrFf6rZ0KS8yDJ8nKnEWP+a5CKJmLkZqzZ8oVMODbqPG6fOj8+qr5VZ6jJ7XzB9W8qR2s7LQ+5t9W8vq2XZ4gPw5Vp9X7+Wz6B+o8C3k7Q3g+t8D7h9+4e7u7Bp9E+P0Q0q4g8OO7L8qO5x5D7aF3R9g+O2OP3O+D3q+t5/yb1nRO8UuE3WO0u1xZ4qP1HJE3K+Tl+vE/Pt6Cw2EPz1EOpJu6F/JO9H8XQIDAQAB\n-----END PUBLIC KEY-----\n"
      },
      {
        "kty": "RSA",
        "alg": "RS384",
        "format": "PEM",
        "pub": "-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA7Z8...\n-----END PUBLIC KEY-----\n"
      },
      {
        "kty": "EC",
        "alg": "ES256",
        "format": "PEM",
        "pub": "-----BEGIN PUBLIC KEY-----\nMFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE4f5wg5l2hKsTeNem/V41fGnJm6gOdrj8ym3rFkEjWT2btf0hEkNKsP8d9xwnSsWLXY0vhU7LWNBTSwJjJGrQ==\n-----END PUBLIC KEY-----\n"
      },
      {
        "kty": "OCT",
        "alg": "HS256",
        "format": "plain",
        "k": "your-256-bit-secret-key-here-must-be-long-enough"
      }
    ]
  }
}
```

### 4.4 How Aidbox Picks the Right Key

When validating a JWT, Aidbox follows a specific algorithm to select the appropriate key:

1. **Algorithm Matching**: Aidbox extracts the *alg* claim from the JWT header and looks for keys with matching algorithms.

2. **Sequential Testing**: Aidbox tests each key with matching algorithm alongside with checking the key format and kty (key type) until one successfully validates the signature.

This approach ensures that tokens are validated correctly even when key rotation is in progress or when multiple algorithms are in use.

### 4.5 Common Mistakes to Avoid

**Wrong Format**: The most common error is mismatching the format field with the actual key encoding. PEM keys must include headers and proper line breaks, while plain format expects the raw key material.

**Missing Issuer**: Forgetting to set the jwt.iss field means tokens won't match this TokenIntrospector, causing validation failures.

**Algorithm Mismatch**: Using alg: "RS256" with an Elliptic Curve key, or specifying ES256 with an RSA key will cause cryptographic errors.

**Incorrect Key Encoding**: Base64 encoding issues are common with plain format keys. Ensure your key material is properly encoded.

## 5. AccessPolicy Example

Token validation only confirms that a JWT is legitimate, it doesn't determine what the user can access. That's where AccessPolicy resources come in.

After successful token validation, Aidbox extracts claims from the JWT and makes them available for authorization decisions. A simple AccessPolicy might look like:

```javascript
{
  "id": "external-auth-server",
  "engine": "json-schema",
  "schema": {
    "required": [
      "jwt"
    ],
    "properties": {
      "jwt": {
        "required": [
          "iss"
        ],
        "properties": {
          "iss": {
            "constant": "https://auth.example.com"
          }
        }
      }
    }
  }
}
```

This policy ensures that only tokens with the correct issuer can access resources. You can create more sophisticated policies based on user roles, scopes, or other JWT claims.

Remember: **validation ≠ authorization**. A valid token doesn't automatically grant access, your AccessPolicies determine what each user can do.

## 6. Best Practices and Tips for Token Introspection

**Rotate Keys Regularly**: Implement a key rotation schedule, especially for production environments. Use the multiple keys feature to enable seamless rotation.

**Prefer Asymmetric Algorithms**: When possible, use RS256 or ES256 over HS256. Asymmetric keys eliminate the need to share secrets between systems.

**Keep Issuer Claims Consistent**: Ensure your IdP always uses the same iss value across all tokens. Changing issuers requires updating TokenIntrospector configurations.

**Use Environment-Specific Introspectors**: [Create separate](https://docs.aidbox.app/configuration/init-bundle) TokenIntrospector resources for development, staging, and production environments. This prevents cross-environment token acceptance.

**Monitor Key Expiration**: Track when your cryptographic keys expire and plan rotation accordingly. Expired keys will cause validation failures.

## 7. Next Steps for Your FHIR Security

Aidbox TokenIntrospector provides comprehensive token validation options for your FHIR infrastructure. Whether you choose opaque token introspection, secret-based validation, JWKS URI, or direct cryptographic key configuration, each method offers distinct advantages for different use cases and architectural requirements.

By supporting multiple validation approaches and key types simultaneously, Aidbox enables you to implement robust security practices that align with your existing identity infrastructure and operational needs.

Looking for a FHIR server that works with your current authentication setup? Aidbox has token introspection built in. [Try it today](https://www.health-samurai.io/fhir-server) and see how it can fit into your system.

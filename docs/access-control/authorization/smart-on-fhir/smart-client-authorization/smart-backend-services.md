---
description: Enable backend services to access FHIR APIs using client credentials grant with JWT bearer authentication.
---

# SMART Backend Services

SMART Backend Services enables **server-to-server authentication** without user interaction. It's designed for automated systems like:

- Data pipelines and ETL jobs
- Backend microservices
- Bulk data export clients
- Integration engines

This flow uses OAuth 2.0 **client credentials grant** combined with **JWT bearer client authentication** ([RFC 7523](https://tools.ietf.org/html/rfc7523)). Instead of a shared secret, the client proves its identity by signing a JWT with its private key.

## How it works

```mermaid
sequenceDiagram
    participant S as Your Backend Service
    participant A as Aidbox

    Note over S: Has Private Key
    Note over A: Has Public Key<br/>(in Client resource)

    S->>S: Create JWT and sign with Private Key
    S->>A: POST /auth/token<br/>client_assertion=<signed JWT>
    A->>A: Verify JWT signature with Public Key
    A-->>S: access_token

    S->>A: GET /fhir/Patient<br/>Authorization: Bearer <token>
    A-->>S: FHIR resources
```

**Key principle:** The private key never leaves your service. Aidbox only stores the public key and uses it to verify JWT signatures.

## Step 1: Generate a key pair

Generate an RSA key pair. The private key stays in your service, the public key goes to Aidbox.

### Option A: Using OpenSSL

```bash
# Generate private key (keep this secret!)
openssl genrsa -out private-key.pem 2048

# Extract public key
openssl rsa -in private-key.pem -pubout -out public-key.pem
```

To convert the public key to JWK format, use an online tool like [https://jwkconvert.vercel.app](https://jwkconvert.vercel.app) or a library.

### Option B: Using Node.js / JavaScript

```javascript
const crypto = require('crypto');

// Generate key pair
const { publicKey, privateKey } = crypto.generateKeyPairSync('rsa', {
  modulusLength: 2048,
});

// Export private key as PEM (store securely!)
const privateKeyPem = privateKey.export({ type: 'pkcs8', format: 'pem' });

// Export public key as JWK (for Aidbox Client resource)
const publicKeyJwk = publicKey.export({ format: 'jwk' });

// Generate a key ID
const keyId = crypto.randomUUID();

console.log('Private Key (PEM):', privateKeyPem);
console.log('Public Key (JWK):', { ...publicKeyJwk, kid: keyId, use: 'sig' });
console.log('Key ID:', keyId);
```

You'll get something like:

```json
{
  "kty": "RSA",
  "n": "0vx7agoebGcQSuu...",
  "e": "AQAB",
  "kid": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "use": "sig"
}
```

## Step 2: Register a Client in Aidbox

Create a Client resource with your **public key** in the `jwks` field.

```http
PUT /Client/my-backend-service
Content-Type: application/json

{
  "id": "my-backend-service",
  "grant_types": ["client_credentials"],
  "scope": ["system/*.read"],
  "auth": {
    "client_credentials": {
      "client_assertion_types": [
        "urn:ietf:params:oauth:client-assertion-type:jwt-bearer"
      ],
      "access_token_expiration": 300
    }
  },
  "jwks": [
    {
      "kty": "RSA",
      "n": "0vx7agoebGcQSuu...",
      "e": "AQAB",
      "kid": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
      "use": "sig"
    }
  ]
}
```

| Field | Description |
|-------|-------------|
| `id` | Your client identifier (used as `iss` and `sub` in JWT) |
| `grant_types` | Must include `client_credentials` |
| `scope` | Allowed scopes (e.g., `system/*.read`, `system/Patient.write`) |
| `jwks` | Array of public keys in JWK format |
| `jwks[].kid` | Key ID — must match `kid` in your JWT header |
| `jwks[].use` | Must be `sig` (signing) |

### Alternative: Use `jwks_uri`

If you prefer to host your public keys at a URL:

```json
{
  "id": "my-backend-service",
  "jwks_uri": "https://your-service.com/.well-known/jwks.json"
}
```

Your endpoint must return a JWKS (note the `keys` wrapper — different from inline `jwks`):

```json
{
  "keys": [
    { "kty": "RSA", "n": "...", "e": "AQAB", "kid": "...", "use": "sig" }
  ]
}
```

{% hint style="info" %}
**Inline `jwks`** is a direct array: `"jwks": [{ ... }]`

**Remote `jwks_uri`** returns an object with `keys`: `{ "keys": [{ ... }] }`
{% endhint %}

## Step 3: Create an AccessPolicy

Grant the client access to FHIR resources:

```http
PUT /AccessPolicy/my-backend-service-policy
Content-Type: application/json

{
  "engine": "allow",
  "link": [
    {
      "id": "my-backend-service",
      "resourceType": "Client"
    }
  ]
}
```

## Step 4: Create a JWT (client_assertion)

Your service must create a signed JWT to authenticate. The JWT has three parts: header, payload, and signature.

### JWT Header

```json
{
  "alg": "RS384",
  "typ": "JWT",
  "kid": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
}
```

| Field | Description |
|-------|-------------|
| `alg` | Signing algorithm: `RS384` (required) |
| `typ` | Must be `JWT` |
| `kid` | Key ID — must match a key in your Client's `jwks` |

### JWT Payload (Claims)

```json
{
  "iss": "my-backend-service",
  "sub": "my-backend-service",
  "aud": "https://your-aidbox.com/auth/token",
  "exp": 1734009926,
  "iat": 1734009626,
  "jti": "unique-id-12345"
}
```

| Claim | Description |
|-------|-------------|
| `iss` | Issuer — your Client ID (required, validated) |
| `sub` | Subject — your Client ID, same as `iss` (required, validated) |
| `aud` | Audience — token endpoint URL (recommended by spec, not validated by Aidbox) |
| `exp` | Expiration time, Unix timestamp, max 5 minutes from now (required, validated) |
| `iat` | Issued at, Unix timestamp (recommended) |
| `jti` | JWT ID — unique identifier, prevents replay attacks (required, validated) |

### Signature

Sign the JWT with your **private key** using the algorithm specified in the header.

```
base64url(header) + "." + base64url(payload)
  → sign with private key
  → base64url(signature)

Final JWT: header.payload.signature
```

### Example: Creating JWT in JavaScript

```javascript
async function createClientAssertion(privateKeyPem, clientId, tokenEndpoint, keyId) {
  // Header
  const header = {
    alg: 'RS384',
    typ: 'JWT',
    kid: keyId
  };

  // Payload
  const now = Math.floor(Date.now() / 1000);
  const payload = {
    iss: clientId,
    sub: clientId,
    aud: tokenEndpoint,
    exp: now + 300,  // 5 minutes
    iat: now,
    jti: crypto.randomUUID()
  };

  // Encode
  const encodedHeader = btoa(JSON.stringify(header))
    .replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, '');
  const encodedPayload = btoa(JSON.stringify(payload))
    .replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, '');

  // Sign
  const signingInput = `${encodedHeader}.${encodedPayload}`;
  const signature = await signWithPrivateKey(signingInput, privateKeyPem);

  return `${signingInput}.${signature}`;
}
```

## Step 5: Request an access token

Send a POST request to the token endpoint:

```http
POST /auth/token
Content-Type: application/x-www-form-urlencoded

grant_type=client_credentials
&scope=system/*.read
&client_assertion_type=urn:ietf:params:oauth:client-assertion-type:jwt-bearer
&client_assertion=eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCIsImtpZCI6ImExYjJjM2Q0LSJ9...
```

| Parameter | Value |
|-----------|-------|
| `grant_type` | `client_credentials` |
| `scope` | Requested scopes (must be subset of Client's allowed scopes) |
| `client_assertion_type` | `urn:ietf:params:oauth:client-assertion-type:jwt-bearer` |
| `client_assertion` | Your signed JWT |

### Response

```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIs...",
  "token_type": "Bearer",
  "expires_in": 300
}
```

## Step 6: Access FHIR API

Use the access token in the `Authorization` header:

```http
GET /fhir/Patient
Authorization: Bearer eyJhbGciOiJSUzI1NiIs...
```

## Validation rules

Aidbox validates the JWT as follows:

1. **Parse JWT** — extract header, payload, signature
2. **Check `typ`** — must be `JWT`
3. **Check `iss`** — must equal the Client ID
4. **Check `sub`** — must equal the Client ID
5. **Check `exp`** — must be present and valid (between now and now + 10 minutes)
6. **Check `jti`** — must be present and unique (prevents replay attacks)
7. **Find public key** — lookup by `kid` in Client's `jwks` or `jwks_uri`
8. **Verify signature** — using the RSA public key

{% hint style="warning" %}
**Note:** Aidbox does not validate the `aud` claim. However, including it is recommended per the SMART specification.
{% endhint %}

If any check fails, Aidbox returns `400 invalid_client`.

## Scopes

Backend services use **system scopes** (not user scopes):

| Scope | Description |
|-------|-------------|
| `system/*.read` | Read all resource types |
| `system/*.write` | Write all resource types |
| `system/Patient.read` | Read Patient resources |
| `system/Observation.write` | Write Observation resources |

## Security considerations

- **Never expose your private key** — it should only exist in your backend service
- **Use short JWT expiration** — max 5 minutes as per spec
- **Generate unique `jti`** — prevents replay attacks
- **Use RS384** — Aidbox requires RS384 algorithm for signing
- **Rotate keys periodically** — update `jwks` and your service together

## References

- [SMART Backend Services (HL7)](https://hl7.org/fhir/smart-app-launch/backend-services.html)
- [Client Authentication: Asymmetric Keys (HL7)](https://hl7.org/fhir/smart-app-launch/client-confidential-asymmetric.html)
- [RFC 7523: JWT Bearer Client Authentication](https://tools.ietf.org/html/rfc7523)
- [RFC 6749: OAuth 2.0](https://tools.ietf.org/html/rfc6749)

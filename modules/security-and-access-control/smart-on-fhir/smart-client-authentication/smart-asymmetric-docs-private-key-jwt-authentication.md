# SMART: Asymmetric (“private key JWT”) authentication

Aidbox supports **SMART Asymmetric Private Key JWT Authentication** to securely manage client authentication.&#x20;

## JWKS

When registering with a FHIR authorization server, the client should provide its public key for authentication. This key should be included in a JSON Web Key (JWK) format within a JWK Set (JWKS).&#x20;

#### **Key Requirements for JWKs:**

No matter how a JWK Set is communicated to the Aidbox FHIR authorization server, each JWK shall represent an asymmetric key by including the `kty` and `kid` properties, with content conveyed using “bare key” properties (i.e., direct base64 encoding of key material as integer values). Specifically:

* **For RSA public keys**:
  * Each JWK shall include the `n` (modulus) and `e` (exponent) properties.
  * Each JWK shall include the `crv` (curve), `x` (x-coordinate), and `y` (y-coordinate) properties.

Aidbox provides JWKS endpoint with RSA public key.

{% tabs %}
{% tab title="Request" %}
```http
GET /.well-known/jwks.json
```
{% endtab %}

{% tab title="Response" %}
```json
{
  "keys": [
    {
      "kty": "RSA",
      "e": "AQAB",
      "n": "vhXfdVSCpvayTzwstnNhrl0QW9LhgTV0AwPrEw-7O7E7-Z7vdfiY6_3n9p92-yQC-dYbfX8-psnd5aypmus_Z0UMXGi8bKy8rMWAb0Ggwb1LndkTJI5MzITY-dcVIeIkGlH21v1Hnsl80UTNdNwatdLK6BtZmqxxXN1VNn-iQF_eztXhrCP-cn3xOugYiwByk5El6DEiSIe4X6xHY1U3WlBdndCHaDoC2enUlCqWmJi0ttrLsQUwdHWvyhFfVfOnYM77oU4nSeFgxTVO0vT8PFY5yUYNjh2_KoDkotASLpzxSluWOXWoX84GSRIdURK9-7qzsZTKOT5cdmp4Ai7c8Q",
      "alg": "RS256"
    }
  ]
}

```
{% endtab %}
{% endtabs %}

So you can use Aidbox's JWKS or your own.

## Registering Client

Before a SMART client can run against a FHIR server, the client SHALL generate or obtain an asymmetric key pair and register its public key set as `jsks_uri` in Client resource. Aidbox provides  `.well-known/jwks.json` endpoint so you can use it.

```json
PUT /Client/inferno-my-clinic-bulk-client
content-type: application/json
accept: application/json

{
  "type": "bulk-api-client",
  "active": true,
  "auth": {
    "client_credentials": {
      "client_assertion_types": [
        "urn:ietf:params:oauth:client-assertion-type:jwt-bearer"
      ],
      "access_token_expiration": 300,
      "token_format": "jwt"
    }
  },
  "scope": [
    "system/*.read"
  ],
  "jwks_uri": "<AIDBOX_BASE_URL>/.well-known/jwks.json",
  "grant_types": [
    "client_credentials"
  ]
}
```

## Authenticating to the Token endpoint

Once the client generates an authentication JWT, it requests an access token based on either the SMART App Launch or SMART Backend Services specifications. The authentication details are included using these additional properties in the token request:

**Authentication JWT Header Values:**

<table><thead><tr><th width="252">Header Value</th><th>Description</th></tr></thead><tbody><tr><td><code>alg</code> *</td><td>Fixed value - <code>RS384</code></td></tr><tr><td><code>kid</code> *</td><td>The identifier for the key pair used to sign this JWT must be unique within the client's JWK Set.</td></tr><tr><td><code>typ</code> *</td><td>Fixed value: <code>JWT</code>.</td></tr><tr><td><code>jku</code></td><td>The TLS-protected URL to the JWK Set that contains the public key(s) accessible without authentication or authorization. When present, this shall match the <code>Client.jwks_uri</code> value.</td></tr></tbody></table>

\*- required value

**Authentication JWT Claims:**

<table><thead><tr><th width="151">Claim</th><th>Description</th></tr></thead><tbody><tr><td><code>iss</code> *</td><td>Client ID (issuer).</td></tr><tr><td><code>sub</code> *</td><td>Client ID (subject).</td></tr><tr><td><code>aud</code> *</td><td>Token endpoint URL - <code>&#x3C;AIDBOX_BASE_URL>/auth/token</code>.</td></tr><tr><td><code>exp</code> *</td><td>The expiration time for this authentication JWT is an integer, representing the number of seconds since the "Epoch" (1970-01-01T00:00:00Z UTC). This value must not exceed five minutes into the future.</td></tr><tr><td><code>jti</code> *</td><td>Unique token identifier to prevent replay attacks.</td></tr></tbody></table>

\*- required value

### Example

JWT:

```
eyJhbGciOiJSUzM4NCIsImtpZCI6ImI0MTUyOGI2ZjM3YTk1MDBlZGI4YTkwNWE1OTViZGQ3IiwidHlwIjoiSldUIn0.eyJpc3MiOiJpbmZlcm5vLW15LWNsaW5pYy1idWxrLWNsaWVudCIsInN1YiI6ImluZmVybm8tbXktY2xpbmljLWJ1bGstY2xpZW50IiwiYXVkIjoiaHR0cHM6Ly9nMTB0ZXN0LmVkZ2UuYWlkYm94LmFwcC9hdXRoL3Rva2VuIiwiZXhwIjoxNzM0MDA5NjI2LCJqdGkiOiJkZGI4NzQ5OTk1YjFkNWRiNDVkNTQ2NDVmZmU0ZmExZTkxODRhODI3YjlmOWM5MDY5ZDQxYzRmYjJhNjBjYTY3In0.hxKAec655NTH7Gs6qy2Cz2CXvETWnxF0jydjEdXNKYyrQvecBWct_ITc92eFiDnZ5jubhExqojeE2HUDn3lmS89Q9qFfGEsByLWXy4nJqSHa2y5mWxD5aI3LF3c4oSOZXSj-jFxAlSmxhV7MxumnJ2XP-6e81QQT-QQ9mDomWhgrIjqaHhv5yPQzI6CqDad9XBInMcE7S_TZ9QTpq3WtzC520-8SH3KdVF9dILO6pBGOOrlZ8468Vwfl5WL6XuhhwjbIIp8B5F0qAOGIGiA8V_-eE6PM1CNZtKQfrZNvVh0VwSu4T2k3gL4ZfI_8nhpUt8EEusOsu_6EvK3sP1yv7w
```

JWT's parsed headers:

```json
{
  "alg": "RS384",
  "kid": "b41528b6f37a9500edb8a905a595bdd7",
  "typ": "JWT"
}
```

JWT's parsed claims:

```json
{
  "iss": "inferno-my-clinic-bulk-client",
  "sub": "inferno-my-clinic-bulk-client",
  "aud": "https://g10test.edge.aidbox.app/auth/token",
  "exp": 1734009626,
  "jti": "ddb8749995b1d5db45d54645ffe4fa1e9184a827b9f9c9069d41c4fb2a60ca67"
}
```

## Requesting an Access Token

This JWT should be used as `client_assertion` a parameter in the access token request. See the full example in [SMART Backend services](https://docs.aidbox.app/modules/security-and-access-control/smart-on-fhir/smart-client-authorization/smart-backend-services#obtain-access-token).


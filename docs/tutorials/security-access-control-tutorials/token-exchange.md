# How to configure Token Exchange

Since version **2412** Aidbox supports [the OAuth Token Exchange extension](https://oauth.net/2/token-exchange/).

Token Exchange enables secure access delegation by exchanging an incoming token for a new token with different attributes, such as a modified scope, audience, or expiration. This mechanism is handy in external identity providers, cross-system integrations, or service-to-service authentication scenarios.

## Use Cases for Token Exchange

* **External Identity Provider Integration:** Allowing tokens issued by trusted third-party identity providers to be exchanged for tokens compatible with the Aidbox.
* **Scoped Access Delegation:** A limited scope or audience for certain operations without exposing the full privileges of an original token is granted.&#x20;
* **Service-to-Service Communication:** The capability of backend services to securely communicate with downstream services by exchanging tokens in a way that maintains user context to enable correct authorization and auditability of user-specific actions.

## Using Token Exchange

### **Create a Client**

Define the client configuration to support the `urn:ietf:params:oauth:grant-type:token-exchange` grant type.

```json
PUT /Client/tokenexchange
content-type: application/json
accept: application/json

{
  "secret": "test",
  "grant_types": [
    "urn:ietf:params:oauth:grant-type:token-exchange"
  ],
  "allowedIssuers": [
    "<your-app>"
  ],
  "auth": {
    "token_exchange": {
      "token_format": "jwt",
      "refresh_token": true,
      "access_token_expiration": 360,
      "refresh_token_expiration": 864000
    }
  }
}
```

* **`grant_types`**: Must contain `urn:ietf:params:oauth:grant-type:token-exchange`.
* **`allowedIssuers`**: Lists the issuers allowed for token exchange.
* **`auth.token_exchange`**:
  * `token_format`: Must be `jwt`. If omitted, an opaque token will be produced.
  * `refresh_token`: Indicates whether a refresh token is issued.
  * `access_token_expiration`: Time-to-live for an access token, in seconds.
  * `refresh_token_expiration`: Time-to-live for refresh token, in seconds.

### **Create a Token Introspector**

A Token Introspector validates incoming tokens from external issuers.

```json
PUT /TokenIntrospector/external-auth-server
content-type: application/json
accept: application/json

{
  "jwt": {
    "iss": "<your-app>",
    "secret": "<your-secret>"
  },
  "type": "jwt"
}
```

* **`jwt`**:
  * `iss`: Specifies the trusted issuer.
  * `secret`: The secret used to validate JWTs.
* **`type`**: Must be`jwt`.

### **Create a User**

Define the user resource that will participate in the token exchange process:

```json
POST /User
content-type: application/json
accept: application/json

{
  "id": "<your-user-id>",
  "resourceType": "User"
}
```

### **Token Exchange Request**

To perform a token exchange, make a POST request to the authorization endpoint with the following details:

```json
POST /auth/token
Authorization: Basic dG9rZW5leGNoYW5nZTp0ZXN0
Content-Type: application/json

{
  "grant_type": "urn:ietf:params:oauth:grant-type:token-exchange",
  "subject_token_type": "urn:ietf:params:oauth:token-type:jwt",
  "scope": "openid",
  "subject_token": "<your-token>"
}
```

* **`grant_type`**: Must be `urn:ietf:params:oauth:grant-type:token-exchange`.
* **`subject_token_type`**: Must be `urn:ietf:params:oauth:token-type:jwt`.
* **`subject_token`**: Your JWT token.
* **`scope`**: Scope of the requested&#x20;

### Token requirements

Your token provided in `subject_token`must contain `iss`, `sub` and `exp`:

```json
{
  "iss": "<issuer-form-token-introspector>",
  "exp": "<seconds-since-unix-epoch>",
  "sub": "<your-user-id>"
}
```

Also, it must be signed with `<your-secret>`.

### **The response**

Upon successful token exchange, the response will include the issued access token and optionally a refresh token:

```json
{
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImV4cCI6MTk5MzMzMjIyMCwiaXNzIjoiaHR0cHM6Ly9teWFwcC5teWNvbXBhbnkuY29tIiwiYXVkIjoiaHR0cDovL2FpZGJveCJ9.QrHuhEksAmT0nReK_EX5VFo39p-qScErWOPWI-9I5_c",
  "token_type": "Bearer",
  "expires_in": 360,
  "refresh_token": "d2ViOnNpbXBsZS1yZWZyZXNoLXRva2Vu"
}
```

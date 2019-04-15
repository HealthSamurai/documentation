# Access by JWT

Aidbox can verify JWT tokens agains 3rd-party identity provider's secrets and keys. To achieve this you need to declare external authentication provider's settings with JwtAuthenticator resource:

```yaml
resourceType: JwtAuthenticator
iss: https://my-provider.example               # shoud be equal to `iss` claim of the JWT
alg: "RS256" or "HS256"                        # should match `alg` claim of the JWT
secret: "xxxxxxxx"                             # pre-shared key if alg = HS256
jwksUrl: https://....../.well-known/jwks.json  # JWKs URL if alg = RS256 (optional)
```

When Aidbox verifies JWT token, it tries to find matching JwtAuthenticator first. The matching is performed with `iss` and `alg` claims. If matching JwtAuthenticator is found, token is being validated with either `jwksUrl` or `secret` \(depends on signing algorithm\). If no JwtAuthencator found, then token will be validated against Aidbox's own keys/secrets.


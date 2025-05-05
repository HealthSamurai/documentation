# External Auth Providers

[Aidbox](https://www.health-samurai.io/aidbox) can verify access tokens agains 3rd-party identity provider's secrets and keys. To achieve this you need to declare external authentication provider's settings with ExternalAuthProvider resource:

```yaml
resourceType: ExternalAuthProvider
iss: https://my-provider.example              # shoud be equal to `iss` claim of the JWT
alg: "RS256" or "HS256"                       # should match `alg` claim of the JWT
secret: "xxxxxxxx"                            # pre-shared key if alg = HS256
jwkUrl: https://....../.well-known/jwks.json  # JWKs URL if alg = RS256
```

When Aidbox verifies JWT token, it tries to find matching ExternalAuthProvider first. The matching is performed with `iss` and `alg` claims. If matching ExternalAuthProvider is found, token is being validated with either `jwkUrl` or `secret` \(depends on signing algorithm\). If no ExternalAuthProvider found, then token will be validated against Aidbox's own public key.


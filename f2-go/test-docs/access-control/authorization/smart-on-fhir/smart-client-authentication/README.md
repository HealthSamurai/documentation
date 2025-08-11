# SMART Client Authentication

When clients need to authenticate, SMART App Launch implementation guide defines two methods:

- [Authenticates a client using an asymmetric keypair](./smart-asymmetric-docs-private-key-jwt-authentication.md). This is SMART’s preferred authentication method because it avoids sending a shared secret over the wire.
- [SMART: Symmetric (“client secret”) authentication](./smart-symmetric-docs-client-secret-authentication.md). Authenticates a client using a secret that has been pre-shared between the client and server.

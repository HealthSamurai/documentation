# SMART Client Authentication

When clients need to authenticate, SMART App Launch implementation guide defines two methods:

1. Authenticates a client using an asymmetric keypair. This is SMART’s preferred authentication method because it avoids sending a shared secret over the wire.

{% content-ref url="smart-asymmetric-private-key-jwt-authentication.md" %}
[smart-asymmetric-private-key-jwt-authentication.md](smart-asymmetric-private-key-jwt-authentication.md)
{% endcontent-ref %}

2. Authenticates a client using a secret that has been pre-shared between the client and server.

{% content-ref url="smart-symmetric-client-secret-authentication.md" %}
[smart-symmetric-client-secret-authentication.md](smart-symmetric-client-secret-authentication.md)
{% endcontent-ref %}


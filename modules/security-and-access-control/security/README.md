# Access Control

[Aidbox](https://www.health-samurai.io/aidbox) provides a flexible model to customise request authorization rules. You can declare a set of checks for all incoming requests. If an incoming request satisfies those checks, it is considered authorised and being processed further. Otherwise the request is denied and the client gets `403 Unauthorized` response. Such checks are declared with `AccessPolicy` resource and can be specified in a variety of ways including JSON Schema and SQL.

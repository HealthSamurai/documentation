# How to prepare Surescripts mTLS

{% hint style="warning" %}
probably you'll need to connect under VPN, because Surescripts requires a static IP
{% endhint %}

Surescripts uses [mTLS (mutual TLS)](https://www.cloudflare.com/learning/access-management/what-is-mutual-tls/) for authentication and authorization.

![How mutual TLS authentication works](https://www.cloudflare.com/resources/images/slt3lc6tev37/5SjaQfZzDLEGqyzFkA0AA4/d227a26bbd7bc6d24363e9b9aaabef55/how_mtls_works-what_is_mutual_tls.png)

Please, follow an instructions provided by Surescripts. And once you will have all certs, you can proceed to [preparing authentication files](how-to-prepare-surescripts-mtls.md#how-to-get-proper-authentication-files).

## How to get proper authentication files

### Client Certificate

This is what referenced as `surescripts-cert` in [compose file example](../getting-started.md#deploy-the-docker-application)

1. Use Surescripts provided cert (`*.p7b` file).
2. Create a `pem` certificate from it
   1. if file is encrypted: `openssl pkcs7 -inform der -in client.p7b -print_certs -out client.pem`
   2. if not encrypted: `openssl pkcs7 -in client.p7b -print_certs -out client.pem`
3. Edit result file and keep only **last** entry (first entries are CA related).

### Private Key

This is what referenced as `surescripts-private` in [compose file example](../getting-started.md#deploy-the-docker-application)

1. You have to use a same private key that was used to obtain client cert `p7b`.

Note, that your private key must be **PKCS#8**, in case it's PKCS#1 please use following command for conversion:

```bash
openssl pkcs8 -topk8 -inform PEM -outform PEM \
  -in your.key \
  -out private.key -nocrypt
```

**How to check that keys match**

1. Create module from private key: `openssl rsa -noout -modulus -in private.key -out private.module`
2. Create module from client cert: `openssl x509 -noout -modulus -in client.pem -out client.module`
3. Compare: `diff private.module client.module`
4. Keys match if there is no diff

### Certificate Authority

This is what referenced as `surescripts-authority` in [compose file example](../getting-started.md#deploy-the-docker-application)

Here is two options:

**Use Surescripts provided cert**

1. Use cert form documentation portal: `OutboundStaging.surescripts.net.p7b`
2. Create a `pem` cert from it: `openssl pkcs7 -inform der -in ca.p7b -print_certs -out ca.pem`

**Use from client cert**

1. In `client.pem` file before entries deletion - there are ones at the beginning that actually a CA part.
2. Create a `pem` cert from it – just copy past into `ca.pem` file.

## Troubleshooting

Consider starting module with JVM args for tracing TLS:

```
-Djavax.net.debug=ssl:handshake:verbose:keymanager
```

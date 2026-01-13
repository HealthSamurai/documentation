---
description: Configure Aidbox and Multibox Docker deployments with environment variables, JAVA options, and performance settings.
---

# Configure Aidbox and Multibox

[Aidbox](https://hub.docker.com/r/healthsamurai/aidboxone) is a full-featured single instance of the Aidbox FHIR server. If you are interested in multi-tenant Aidbox, consider using [Multibox](https://hub.docker.com/r/healthsamurai/multibox) distribution.

All distributions can be used with standard PostgreSQL or managed PostgreSQL services.

Basic Aidbox installation consists of two components: the backend and the database. Both are released as docker images and can be pulled from HealthSamurai [docker hub](https://hub.docker.com/u/healthsamurai). For each type of Aidbox license an individual backend image is available — either [Aidbox](https://hub.docker.com/r/healthsamurai/aidboxone) or [Multibox](https://hub.docker.com/r/healthsamurai/multibox).

Aidbox works with standard PostgreSQL 13 and higher. See [PostgreSQL Requirements](../database/postgresql-requirements.md) for details.

## Recommended environment variables

{% content-ref url="recommended-envs.md" %}
[recommended-envs.md](recommended-envs.md)
{% endcontent-ref %}


### JAVA\_OPTS

```
JAVA_OPTS="<string>"
```

Configure general JAVA options. For example - request and max heap size configuration.

```
JAVA_OPTS="-Xms1024m -Xmx1024m"
```

See also: [How to configure Aidbox to use a proxy](../tutorials/other-tutorials/how-to-configure-aidbox-to-use-proxy.md)

## Configure performance

By default, Aidbox and Multibox runs with 8 web workers and 8 DB connection pool size. That means that Aidbox can process at the same time 8 concurrent connections.

A good practice is stayed pool size the same as CPU count of your database. For example, if your database has 16 CPU cores, you can set `BOX_DB_POOL_MAXIMUM__POOL__SIZE=16`. Box web workers count is dependent on your load profile. For example, if you have a lot of fast read queries, you can set `BOX_WEB_THREAD` equal x2 or x3 of your DB pool size (32 or 48). Or if you have a lot of batch insert queries, we recommend stay web workers count as the same DB pool size.

You can configure this parameter using following environment variables.

```bash
BOX_DB_POOL_MAXIMUM_POOL_SIZE=8
BOX_WEB_THREAD=8
```

Aidbox usually needs 2-4 Gb of memory:

```
JAVA_OPTS="-Xms4096m -Xmx4096m"
```

### Configuring SSL connection with PostgreSQL

Parameters prefixed with AIDBOX\_DB\_PARAM is passed to JDBC PostgreSQL connection string.

For an instance:

`AIDBOX_DB_PARAM_SSL=true`\
`AIDBOX_DB_PARAM_SSLMODE=verify-ca`

will add `ssl=true&sslmode=verify-ca` params to connection string.

These parameters will enable SSL connection from Aidbox to postgresql Docs on JDBC PostgreSQL connection string are [here](https://jdbc.postgresql.org/documentation/use/). See also [how to use SSL](https://jdbc.postgresql.org/documentation/head/ssl-client.html).

The next step is to configure your database to accept SSL connections. You can do that by passing your own postgresql.conf with argument -c config\_file passed into the db containter and probably you want to set up postgres to receive only SSL connections, you can do that by passing your own pg\_hba.conf file with -c hba\_file

### Use different PostgreSQL schema

By default Aidbox uses `public` schema. If you want Aidbox to use different schema, set [JDBC parameter `currentSchema`](https://jdbc.postgresql.org/documentation/head/connect.html#connection-parameters) using environment variable `BOX_DB_EXTENSION_SCHEMA`:

```
BOX_DB_EXTENSION_SCHEMA=myschema
```

PostgreSQL extensions can create objects. By default PostgreSQL sets up extension to use current schema. If you are going to share database between multiple applications, we recommend to create a dedicated schema for the extensions.

Use `BOX_DB_EXTENSION_SCHEMA` environment variable to set up Aidbox to use dedicated extension schema:

```
BOX_DB_EXTENSION_SCHEMA=myextensionschema
```



Note: if your database already has extensions installed and you change extension schema (or current schema if extension schema is not configured), then you need to drop extensions from previous schema:

```
DROP EXTENSION IF EXISTS fuzzystrmatch
                       , jsonknife
                       , pg_stat_statements
                       , pg_trgm
                       , pgcrypto
                       , unaccent;
```

Then change `BOX_DB_EXTENSION_SCHEMA` and restart Aidbox.


### Set up RSA private/public keys and secret

Aidbox generates JWT tokens for different purposes:

* As part of OAuth 2.0 authorization it generates authorization\_code in JWT format
* If you specify auth token format as JWT, then your access\_token and refresh\_token will be in JWT format.

Aidbox supports two signing algorithms: RS256 and HS256. RS256 expects providing private key for signing JWT and public key for verifing it. As far as HS256 needs only having secret for both operations.

{% hint style="warning" %}
Attention: by default Aidbox generates both keypair and secret on every startup. This means that on every start all previously generated JWT will be invalid. In order to avoid such undesirable situation, you may pass RSA keypair and secret as Aidbox parameters.

It is required to pass RSA keypair and secret as Aidbox parameters if you have multiple replicas of the same Aidbox/Multibox instance.
{% endhint %}

#### Generate RSA keypair

Generate private key with `openssl genrsa -traditional -out key.pem 2048` in your terminal. Private key will be saved in file `key.pem`. To generate public key run `openssl rsa -in key.pem -outform PEM -pubout -out public.pem`. You will find public key in `public.pem` file.

Use next env vars to pass RSA keypair:

```yaml
BOX_AUTH_KEYS_PRIVATE: "-----BEGIN RSA PRIVATE KEY-----\n...\n-----END RSA PRIVATE KEY-----"
BOX_AUTH_KEYS_PUBLIC: "-----BEGIN PUBLIC KEY-----\n...\n-----END PUBLIC KEY-----"
```

You can also use YAML multi-line strings for passing values of the keys:

```yaml
      BOX_AUTH_KEYS_PUBLIC: |
        -----BEGIN PUBLIC KEY-----
        MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtknsklLTP1y6HPtR2oYs
        ...
        ewIDAQAB
        -----END PUBLIC KEY-----
```

#### Generate secret

To generate random string for HS256 algoritm you can run `openssl rand -base64 36` command. The length of the random string must be more than 256 bits (32 bytes).

use next env var to pass secret param:

```
BOX_SECURITY_AUTH_KEYS_SECRET=<rand_string>
```

## References

Refer to the following pages for the specific image description and the list of available configuration options.

{% content-ref url="../database/postgresql-requirements.md" %}
[postgresql-requirements.md](../database/postgresql-requirements.md)
{% endcontent-ref %}

{% content-ref url="../reference/all-settings.md" %}
[all-settings.md](../reference/all-settings.md)
{% endcontent-ref %}

You can also see the environment variables in the **Settings page** in **AidboxUI**.

If you are looking for the latest versions of the docker images or general release cycle explanation go to the Versioning page.

{% content-ref url="../overview/versioning.md" %}
[versioning.md](../overview/versioning.md)
{% endcontent-ref %}

See also deployment documentation:

{% content-ref url="../deployment-and-maintenance/deploy-aidbox/" %}
[deploy-aidbox](../deployment-and-maintenance/deploy-aidbox/)
{% endcontent-ref %}

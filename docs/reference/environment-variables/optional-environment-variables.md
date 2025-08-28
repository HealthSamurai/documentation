# Optional Environment Variables

### JAVA\_OPTS

```
JAVA_OPTS="<string>"
```

Configure general JAVA options. For example - request and max heap size configuration.

```
JAVA_OPTS="-Xms1024m -Xmx1024m"
```

### BOX\_ID

old env:

```
AIDBOX_BOX_ID:<id>
```

new env:

```
BOX_ID:<id>
```

Assigns unique id for Aidbox instance. Important to set if you deploy few Aidbox instances and wish to separate their telemetry data (logs, metrics, traces) in your observability system.

### BOX\_WEB\_BASE\_URL

old env:

```
AIDBOX_BASE_URL=<url>
```

new env:

```
BOX_WEB_BASE_URL=<url>
```

Aidbox Base URL is URL Aidbox is available at. It consists of schema (http, https), domain, port (optional) and URL path (optional). Trailing slash is not allowed.

Default is

```
http://localhost:[BOX_WEB_PORT]
```

{% hint style="info" %}
Examples:

`BOX_WEB_BASE_URL`=`http://fhir.example.com`

`BOX_WEB_BASE_URL`=`http://fhir.example.com:8080`

`BOX_WEB_BASE_URL`=`http://fhir.example.com/aidbox`
{% endhint %}

### AIDBOX\_DB\_PARAM\_\*

```
AIDBOX_DB_PARAM_<parameter name>=<parameter value>
```

Parameters prefixed with `AIDBOX_DB_PARAM_` will be passed to [JDBC PostgreSQL connection string](https://jdbc.postgresql.org/documentation/use/#connection-parameters).

### BOX\_OBSERVABILITY\_ELASTIC\_SEARCH\_URL

old env:

```
AIDBOX_ES_URL=<url>
```

new env:

```
BOX_OBSERVABILITY_ELASTIC_SEARCH_URL=<url>
```

If provided, enables mode to push logs to ElasticSearch.

### BOX\_FHIR\_SEARCH\_DEFAULT\_PARAMS\_TOTAL

old env:

```
box_search_default__params_total=<value>
```

new env:

```
BOX_FHIR_SEARCH_DEFAULT_PARAMS_TOTAL=<value>
```

`value` is one of: `none`, `estimate`, `accurate`.

Sets the default total search parameter value.

{% hint style="warning" %}
if you use `BOX_FHIR_SEARCH_DEFAULT_PARAMS_TOTAL=none` you still get `total`when:

1. you don't use `_page`
2. the number of returned resources is less than `_count` (by default is 100).
{% endhint %}

### BOX\_OBSERVABILITY\_ELASTIC\_SEARCH\_AUTH

old env:

```
AIDBOX_ES_AUTH=<user>:<password>
```

new env:

```
BOX_OBSERVABILITY_ELASTIC_SEARCH_AUTH=<user>:<password>
```

Basic auth credentials for ElasticSearch. API key is not supported.

### BOX\_OBSERVABILITY\_ELASTIC\_BATCH\_SIZE

old env:

```
AIDBOX_ES_BATCH_SIZE=<size>
```

new env:

```
BOX_OBSERVABILITY_ELASTIC_BATCH_SIZE=<size>
```

Log batch size used to optimize log shipping performance. The default value is 200

### BOX\_OBSERVABILITY\_ELASTIC\_BATCH\_TIMEOUT

old env:

```
AIDBOX_ES_BATCH_TIMEOUT=<timeout>
```

new env:

```
BOX_OBSERVABILITY_ELASTIC_BATCH_TIMEOUT=<timeout>
```

Timeout to post a batch to ElasticSearch. If there is not enough records to reach full batch size

### BOX\_OBSERVABILITY\_ELASTIC\_INDEX\_PATTERN

old env:

```
AIDBOX_ES_INDEX_PAT=<format>
```

new\_env:

```
BOX_OBSERVABILITY_ELASTIC_INDEX_PATTERN=<format>
```

Custom index format string. The default value is 'aidbox-logs'-yyyy-MM-dd.

### BOX\_OBSERVABILITY\_LOG\_FILE\_PATH

old env:

```
AIDBOX_LOGS=<filepath>
```

new env:

```
BOX_OBSERVABILITY_LOG_FILE_PATH=<filepath>
```

If provided, enables mode to pipe logs as json into the file by specified path. If ElasticSearch URL is provided then the file is used as a fallback in case if ElasticSearch is not available

### BOX\_OBSERVABILITY\_LOG\_FILE\_MAX\_LINES

old env:

```
AIDBOX_LOGS_MAX_LINES=<max-lines>
```

new env:

```
BOX_OBSERVABILITY_LOG_FILE_MAX_LINES=<max-lines>
```

Sets the limit of log records to push into the file. When the limit is reached, the current log file is renamed with ".old" postfix and a new log file is created. The default value is "10000"

### BOX\_OBSERVABILITY\_DISABLE\_HEALTH\_LOGS

old env:

```
BOX_LOGGING_DISABLE__HEALTH__LOGS=<boolean>
```

new env:

```
BOX_OBSERVABILITY_DISABLE_HEALTH_LOGS=<boolean>
```

Disable `/health` endpoint requests logging. Default value is `false`.

### BOX\_OBSERVABILITY\_SQL\_MIN\_DURATION

old env:

```
BOX_LOGGING_SQL_MIN__DURATION=<integer>
```

new env:

```
BOX_OBSERVABILITY_SQL_MIN_DURATION=<integer>
```

Threshold for logging only long queries. Analogue from Postgres.

Log only requests whose execution time exceeds the specified number of milliseconds.

### BOX\_OBSERVABILITY\_SQL\_MAX\_LENGTH

old env:

```
BOX_LOGGING_SQL_MAX__LENGTH=<integer>
```

new env:

```
BOX_OBSERVABILITY_SQL_MAX_LENGTH=<integer>
```

Max length of a query to be logged.

### BOX\_DB\_INSTALL\_PG\_EXTENSIONS

old env:

```
AIDBOX_INSTALL_PG_EXTENSIONS=<boolean>
```

new env:

```
BOX_DB_INSTALL_PG_EXTENSIONS=<boolean>
```

Says Aidbox to install PostgreSQL extensions at startup time. The default value is `true`.

### BOX\_OBSERVABILITY\_STDOUT\_LOG\_LEVEL

old env:

```
AIDBOX_STDOUT_JSON=<log-level>
```

new env:

```
BOX_OBSERVABILITY_STDOUT_LOG_LEVEL=<log-level>
```

`log-level` is one of: `off`, `fatal`, `error`, `warn`, `info`, `debug`, `trace`, `all`.

By setting one of these values you would also get all the values to the left. e.g. if you set log level to `warn` you would also get log events with `fatal` and `error` levels (`off` is excluded).

#### Example of the log output

```json
{"sql":"SELECT 1","d":2,"ts":"2022-10-26T10:59:59.825Z","w":"main","ev":"db/q"}
```

### BOX\_OBSERVABILITY\_STDOUT\_GOOGLE\_LOG\_LEVEL

Produces logs in Google Logging format (see [LogEntry](https://cloud.google.com/logging/docs/reference/v2/rest/v2/LogEntry)).

old env:

```
AIDBOX_STDOUT_GOOGLE_JSON=<log-level>
```

new env:

```
BOX_OBSERVABILITY_STDOUT_GOOGLE_LOG_LEVEL=<log-level>
```

`log-level` is one of: `off`, `fatal`, `error`, `warn`, `info`, `debug`, `trace`, `all`.

By setting one of these values you would also get all the values to the left. e.g. if you set log level to `warn` you would also get log events with `fatal` and `error` levels (`off` is excluded).

#### Example of the log output

```json
{"sql":"SELECT 1","d":2,"timestamp":"2022-10-26T10:59:59.825Z","severity":"INFO","w":"main","ev":"db/q"}
```

### BOX\_OBSERVABILITY\_STDOUT\_PRETTY\_LOG\_LEVEL

old env:

```
AIDBOX_STDOUT_PRETTY=<log-level>
```

new env:

```
BOX_OBSERVABILITY_STDOUT_PRETTY_LOG_LEVEL=<log-level>
```

`log-level` is one of: `off`, `fatal`, `error`, `warn`, `info`, `debug`, `trace`, `all`.

By default `log-level` is `error`.

By setting one of these values you would also get all the values to the left. e.g. if you set log level to `warn` you would also get log events with `fatal` and `error` levels (`off` is excluded).

#### Example of the log output

```
11:01:12 main [1ms] SELECT 1
```

### BOX\_OBSERVABILITY\_DATADOG\_API\_KEY

old env:

```
AIDBOX_DD_API_KEY=<key>
```

new env:

```
BOX_OBSERVABILITY_DATADOG_API_KEY=<key>
```

If provided, enables mode to push logs to DataDog

### BOX\_OBSERVABILITY\_DATADOG\_BATCH\_SIZE

old env:

```
AIDBOX_DD_BATCH_SIZE=<batch-size>
```

new env:

```
BOX_OBSERVABILITY_DATADOG_BATCH_SIZE=<batch-size>
```

Size of log batch, used to optimize performance of log shipping. The default value is 200

### BOX\_OBSERVABILITY\_DATADOG\_BATCH\_TIMEOUT

old env:

```
AIDBOX_DD_BATCH_TIMEOUT=<timeout-ms>
```

new env:

```
BOX_OBSERVABILITY_DATADOG_BATCH_TIMEOUT=<timeout-ms>
```

Timeout (in ms) to post a batch to DataDog if there are not enough records to reach full batch size. Default value: 3600000 (1 hour)

### BOX\_OBSERVABILITY\_DATADOG\_LOGS

old env:

```
AIDBOX_DD_LOGS=<filepath>
```

new env:

```
BOX_OBSERVABILITY_DATADOG_LOGS=<filepath>
```

Fallback file to write logs in if uploading to DataDog fails

### BOX\_FHIR\_CREATEDAT\_URL

old env:

```
AIDBOX_CREATED_AT_URL=<url>
```

new env:

```
BOX_FHIR_CREATEDAT_URL=<url>
```

Overrides createdAt extension url, default is `ex:createdAt`

### BOX\_FHIR\_CORRECT\_AIDBOX\_FORMAT

old env:

```
AIDBOX_CORRECT_AIDBOX_FORMAT=true
```

new env:

```
BOX_FHIR_CORRECT_AIDBOX_FORMAT=true
```

If provided, activates transforming unknown polymorphic extensions to the correct Aidbox format avoiding keeping them at FHIR-format.

For example, `extension.*.valueString` stored as `extension.0.value.string`

### BOX\_CACHE\_REPLICATION\_DISABLE

```
BOX_CACHE_REPLICATION_DISABLE=true
```

By default, Aidbox works in multi-replica mode, so more than one Aidbox replica could be connected to the same database. If you are sure you'll be running only one Aidbox replica, you could disable replication mechanism with this variable. Check [Highly Available Aidbox](../../database/aidboxdb-image/ha-aidboxdb.md) for additional information.

### BOX\_SECURITY\_DEV\_MODE

old env:

```
AIDBOX_DEV_MODE=true
```

new env:

```
BOX_SECURITY_DEV_MODE=true
```

Enables `_debug=policy` for [access policy debugging](../../tutorials/security-access-control-tutorials/debug-access-control.md)

### BOX\_ZEN\_PROJECT\_ENTRYPOINT

old env:

```
AIDBOX_ZEN_ENTRYPOINT=<entrypoint>
```

new env:

```
BOX_ZEN_PROJECT_ENTRYPOINT=<entrypoint>
```

Specifies entry point for loading Aidbox configuration. Example:

```
BOX_ZEN_PROJECT_ENTRYPOINT=main/box
```

### BOX\_ZEN\_PROJECT\_DEV\_MODE

old env:

```
AIDBOX_ZEN_DEV_MODE=true
```

new env:

```
BOX_ZEN_PROJECT_DEV_MODE=true
```

Enables watcher which reloads zen namespaces when they change.

### BOX\_ZEN\_PROJECT\_PATHS

old env:

```
AIDBOX_ZEN_PATHS=<source>:<format>:<path>[,<source>:<format>:<path>]*
```

new env:

```
BOX_ZEN_PROJECT_PATHS=<source>:<format>:<path>[,<source>:<format>:<path>]*
```

`<source>` is either `url`, or `path`.

* `url` is used to load project from remote location
* `path` is used to load project from local location

`<format>` is either `zip`, or `dir`, or `edn`.

Table of sources and format compatibility:

|               |       |       |       |
| ------------- | ----- | ----- | ----- |
| source/format | `zip` | `dir` | `edn` |
| `url`         | ✓     |       | ✓     |
| `path`        | ✓     | ✓     | ✓     |

### BOX\_DB\_EXTENSION\_SCHEMA

old env:

```
AIDBOX_EXTENSION_SCHEMA=<schema>
```

new env:

```
BOX_DB_EXTENSION_SCHEMA=<schema>
```

Schema for PostgreSQL extensions. Default is current schema. See [use different PostgreSQL schema section](optional-environment-variables.md#use-different-postgresql-schema).

### BOX\_SECURITY\_AUDIT\_LOG\_ENABLED

old env:

```
AIDBOX_SECURITY_AUDIT__LOG_ENABLED=true
```

new env:

```
BOX_SECURITY_AUDIT_LOG_ENABLED=true
```

Enable producing audit logs in FHIR AuditEvent format for significant events.

### BOX\_FHIR\_SEARCH\_DEFAULT\_PARAMS\_COUNT

old env:

```
BOX_SEARCH_DEFAULT__PARAMS_COUNT=<count>
```

new env:

```
BOX_FHIR_SEARCH_DEFAULT_PARAMS_COUNT=<count>
```

Overrides the default count search parameter value. 100 is the default value. The provided value should be <= 1000

### BOX\_FHIR\_SEARCH\_COMPARISONS

old env:

```
BOX_SEARCH_FHIR__COMPARISONS=true
```

new env:

```
BOX_FHIR_SEARCH_COMPARISONS=true
```

Use FHIR compliant [date search](https://www.hl7.org/fhir/search.html#prefix).

### BOX\_FHIR\_SEARCH\_INCLUDE\_CONFORMANT

old env:

```
BOX_SEARCH_INCLUDE_CONFORMANT=true
```

new env:

```
BOX_FHIR_SEARCH_INCLUDE_CONFORMANT=true
```

When set to true, the behavior of \_include and \_revinclude becomes FHIR conformant:

1. Without the :recur or :iterate modifier \_(rev)include is only applied to the initial result.
2. With the :recur or :iterate modifier \_(rev)include is repeatedly applied to the resources found in the previous step.

### BOX\_FHIR\_SEARCH\_AUTHORIZE\_INLINE\_REQUESTS

old env:

```
BOX_SEARCH_AUTHORIZE_INLINE_REQUESTS=true
```

new env:

```
BOX_FHIR_SEARCH_AUTHORIZE_INLINE_REQUESTS=true
```

[Authorize inline requests](../../api/rest-api/fhir-search/include-and-revinclude.md)

### BOX\_FHIR\_SEARCH\_INCLUDE\_ITERATE\_MAX

old env:

```
BOX_SEARCH_INCLUDE_ITERATE__MAX=10
```

new env:

```
BOX_FHIR_SEARCH_INCLUDE_ITERATE_MAX=10
```

Maximum number of iterations for \_include and \_revinclude with :recur or :iterate modifier. The default value is 10. If set to 0, queries for \_(rev)include will not be performed. If set to a negative value, no limit will be applied.

### BOX\_ZEN\_PROJECT\_SEARCH\_RESOURCE\_COMPAT

old env:

```yaml
BOX_SEARCH_RESOURCE__COMPAT=false
```

new env:

```
BOX_ZEN_PROJECT_SEARCH_RESOURCE_COMPAT=false
```

`false` to use preferred version of zen-search (`true` to backward compatibility zen search)

### BOX\_FHIR\_JSON\_SCHEMA\_DATETIME\_REGEX

old env:

```
BOX_COMPATIBILITY_VALIDATION_JSON__SCHEMA_REGEX="#{:fhir-datetime}"
```

new env:

```
BOX_FHIR_JSON_SCHEMA_DATETIME_REGEX="#{:fhir-datetime}"
```

Enables strict date time validation in JSON schema validation engine per [FHIR spec](https://www.hl7.org/fhir/datatypes.html#dateTime).

### BOX\_COMPATIBILITY\_AUTH\_PKCE\_CODE\_\_CHALLENGE\_S256\_CONFORMANT

```
BOX_COMPATIBILITY_AUTH_PKCE_CODE__CHALLENGE_S256_CONFORMANT=true
```

Use conformant S256 code challenge validation scheme.

### BOX\_SECURITY\_DEBUG\_SU\_ENABLE

old env:

```
BOX_DEBUG_SU_ENABLE=true
```

new env:

```
BOX_SECURITY_DEBUG_SU_ENABLE=true
```

Enables `su` request header functionality.

### BOX\_FHIR\_VALIDATION\_SKIP\_REFERENCE

old env:

```
BOX_FEATURES_VALIDATION_SKIP_REFERENCE=true
```

new env:

```
BOX_FHIR_VALIDATION_SKIP_REFERENCE=true
```

Enables skipping resource reference validation.

### BOX\_WEB\_MAX\_BODY

old env:

```
BOX_WEB_MAX__BODY=<max-size-bytes>
```

new env:

```
BOX_WEB_MAX_BODY=<max-size-bytes>
```

Maximum size of request body in bytes. Default is 20971520 (20 MiB)

### BOX\_WEB\_THREAD

```
BOX_WEB_THREAD=<count-of-web-worker-threads>
```

Count of HTTP server web workers. Default is 8

### BOX\_WEB\_MAX\_LINE

old env:

```
BOX_WEB_MAX__LINE=<max-line-bytes>
```

new env:

```
BOX_WEB_MAX_LINE=<max-line-bytes>
```

Length limit for HTTP initial line and per header, 414(Request-URI Too Long) will be returned if exceeding this limit. Default to 8192.

### BOX\_SECURITY\_CORS\_ENABLED

old env:

```
BOX_WEB_CORS_ENABLED=true
```

new env:

```
BOX_SECURITY_CORS_ENABLED=true
```

Allow CORS requests

### BOX\_SECURITY\_CORS\_ORIGINS

old env:

```
BOX_WEB_CORS_ORIGINS=*
```

new env:

```
BOX_SECURITY_CORS_ORIGINS=*
```

Comma-separated list of allowed origins `[schema]://[domain]:[port]`

The default value is wildcard "\*"

### BOX\_ZEN\_PROJECT\_TERMINOLOGY\_IMPORT\_SYNC

old env:

```
BOX_FEATURES_TERMINOLOGY_IMPORT_SYNC=true
```

new env:

```
BOX_ZEN_PROJECT_TERMINOLOGY_IMPORT_SYNC=true
```

Enables synchronous terminology bundle import

### BOX\_SECURITY\_INTROSPECTION\_CREATE\_USER

old env:

```
BOX_FEATURES_AUTHENTICATION_INTROSPECTION_CREATE__USER=<boolean>
```

new env:

```
BOX_SECURITY_INTROSPECTION_CREATE_USER=<boolean>
```

Create a user when using foreign JWT access token and the user does not already exist.

### BOX\_MODULE\_GRAPHQL\_WARMUP\_ON\_STARTUP

old env:

```
BOX_FEATURES_GRAPHQL_WARMUP__ON__STARTUP=<boolean>
```

new env:

```
BOX_MODULE_GRAPHQL_WARMUP_ON_STARTUP=<boolean>
```

Warmup graphql caches on startup

### BOX\_MODULE\_GRAPHQL\_TIMEOUT

old env:

```
BOX_FEATURES_GRAPHQL_TIMEOUT=<integer>
```

new env:

```
BOX_MODULE_GRAPHQL_TIMEOUT=<integer>
```

Sets timeout for graphql queries in seconds. Default value is `60`.

### BOX\_FHIR\_TRANSACTION\_MAX\_ISOLATION\_LEVEL

old env:

```
BOX_FEATURES_FHIR_TRANSACTION_MAX__ISOLATION__LEVEL=<isolation-level>
```

new env:

```
BOX_FHIR_TRANSACTION_MAX_ISOLATION_LEVEL=<isolation-level>
```

`isolation-level` is one of: `none`, `read-committed`, `repeatable-read`, `serializable`.

Sets maximum (inclusive) isolation level for transactions. This value can be overridden by `x-max-isolation-level` header (see [here](../../api/rest-api/crud/update.md#isolation-levels)).

### BOX\_ZEN\_PROJECT\_INDEX\_SYNC\_ON\_START

old env:

```
BOX_CONFIG_FEATURES_INDEX_SYNC__ON__START=<boolean>
```

new env:

```
BOX_ZEN_PROJECT_INDEX_SYNC_ON_START=<boolean>
```

If enabled, Aidbox synchronizes managed index on startup.

### Enable Aidbox compliance mode

```
BOX_FHIR_COMPLIANT_MODE=true
```

\- Adds various attributes and endpoints info to CapabilityStatement

\- Sanitises CapabilityStatement (i.e. removes attributes containing `null` values and empty arrays)

\- Adds `/fhir` to base URL for FHIR search parameters definitions in CapabilityStatement

\- Adds AIDBOX\_BASE\_URL in `Bundle.link.url`

\- Adds FHIR date search parameter validation on `lastUpdated` search parameter

\- Adds "alg": "RS256" entry for JWKS

\- Changes validation error status to 422 (instead of 400)

\- Changes `cache-control` header to `no-store` on authorization code auth flow (instead of `no-cache, no-store, max-age=0, must-revalidate`)

\- Removes `Bundle.entry` if empty

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

### Configure Aidbox WEB server workers and DB connection pool

By default Aidbox and Multibox runs with 8 web workers and 8 DB connection pool size. That means that Aidbox can process at same time 8 concurrent connections.

A good practice is stay pool size the same as CPU count of your database. For example, if your database has 16 CPU cores you can set `BOX_DB_POOL_MAXIMUM_POOL_SIZE=16`. Box web workers count is dependent on your load profile. For example, if you have a lot of fast read queries you can set `BOX_WEB_THREAD` equal x2 or x3 of your DB pool size (32 or 48). Or if you have a lot of batch insert queries we recommend stay web workers count as the same DB pool size.

You can configure this parameter using following environment variables.

```
BOX_DB_POOL_MAXIMUM_POOL_SIZE=8
BOX_WEB_THREAD=8
```

### Telemetry

By default, Aidbox collects and sends high-level anonymous API usage statistics used solely for Aidbox improvement.

#### BOX\_TELEMETRY\_ERRORS

```
BOX_TELEMETRY_ERRORS=false
```

Disable sending anonymous errors data.

#### BOX\_USAGE\_STATS

old env:

```
BOX_TELEMETRY_USAGE_STATS=false
```

new env:

```
BOX_USAGE_STATS=false
```

Disable sending anonymous API usage statistics.

### Security Labels

#### BOX\_SECURITY\_LBAC\_ENABLED

The [Security Labels](optional-environment-variables.md#security-labels) access control feature is disabled by default. To enable it, set the env to true.

```ini
BOX_SECURITY_LBAC_ENABLED=true
```

#### BOX\_SECURITY\_LBAC\_STRIP\_LABELS

By default, stripping is disabled. To enable it, set the env to true.

```ini
BOX_SECURITY_LBAC_STRIP_LABELS=true
```

{% hint style="info" %}
Stripping is only applied during the masking.
{% endhint %}

### Observability

Follow the link below to learn how Aidbox metrics work.

{% content-ref url="../../modules/observability/metrics/" %}
[metrics](../../modules/observability/metrics/)
{% endcontent-ref %}

#### **BOX\_METRICS\_PORT**

Defines the port which will be used to expose metrics.

```
BOX_METRICS_PORT=<port>
```

#### BOX\_OBSERVABILITY\_METRICS\_ENABLE\_POSTGRES\_METRICS

```
BOX_OBSERVABILITY_METRICS_ENABLE_POSTGRES_METRICS=false
```

If you have a different pg exporter, disable Aidbox PostgreSQL metrics to avoid metrics duplication by setting the env to false.

#### BOX\_OBSERVABILITY\_GRAFANA\_URL

Specify the Grafana instance URL in this env.

#### BOX\_OBSERVABILITY\_GRAFANA\_USER

Specify the Grafana user name.

#### BOX\_OBSERVABILITY\_GRAFANA\_PASSWORD

Specify the Grafana user password.

### Content security policy

BOX\_SECURITY\_CONTENT\_SECURITY\_POLICY\_HEADER

A Content Security Policy (CSP) is a security mechanism that helps protect web applications from threats like Cross-Site Scripting (XSS), data injection, and clickjacking. It works by specifying rules for browsers about which resources (e.g., scripts, styles, images) can be loaded and executed.

```ini
BOX_SECURITY_CONTENT_SECURITY_POLICY_HEADER=default-src 'self'; script-src 'report-sample' 'self' 'unsafe-inline' 'unsafe-eval'; style-src 'report-sample' 'self' 'unsafe-inline'; object-src 'none'; base-uri 'self'; connect-src 'self'; font-src 'self'; frame-src 'self'; frame-ancestors 'self'; img-src 'self' data:; manifest-src 'self'; media-src 'self'; worker-src 'self';
```

Recommended policies:

```
default-src 'self'; 
script-src 'report-sample' 'self' 'unsafe-inline' 'unsafe-eval';
style-src 'report-sample' 'self' 'unsafe-inline'; 
object-src 'none'; 
base-uri 'self'; 
connect-src 'self'; 
font-src 'self';
frame-src 'self'; 
frame-ancestors 'self'; 
img-src 'self' data:; 
manifest-src 'self'; 
media-src 'self'; 
worker-src 'self';
```

Explanation:

| **Directive**     | **Allowed Sources**                                             | **Description**                                                                          | **Security Implications**                                                                             |
| ----------------- | --------------------------------------------------------------- | ---------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------- |
| `default-src`     | `'self'`                                                        | Sets the default policy for all resource types unless overridden by specific directives. | Restricts all resources to the same origin unless explicitly allowed elsewhere.                       |
| `script-src`      | `'report-sample'`, `'self'`, `'unsafe-inline'`, `'unsafe-eval'` | Controls JavaScript sources.                                                             | Allows same-origin scripts but also permits inline scripts and `eval()`, which are security risks.    |
| `style-src`       | `'report-sample'`, `'self'`, `'unsafe-inline'`                  | Defines valid sources for stylesheets.                                                   | Allows same-origin styles but permits inline styles, which can be exploited if not carefully managed. |
| `object-src`      | `'none'`                                                        | Blocks `<object>` elements entirely.                                                     | Prevents the use of potentially dangerous `<object>` elements, mitigating XSS risks.                  |
| `base-uri`        | `'self'`                                                        | Restricts the URLs allowed in `<base>` elements to the same origin.                      | Protects against base URL manipulation attacks.                                                       |
| `connect-src`     | `'self'`                                                        | Limits connections (e.g., AJAX, WebSocket) to the same origin.                           | Prevents data exfiltration to unauthorized endpoints.                                                 |
| `font-src`        | `'self'`                                                        | Restricts font loading to the same origin.                                               | Reduces risks from malicious or unauthorized fonts.                                                   |
| `frame-src`       | `'self'`                                                        | Allows embedding content in frames only from the same origin.                            | Mitigates clickjacking attacks by disallowing external framing of your content.                       |
| `frame-ancestors` | `'self'`                                                        | Ensures that only pages from the same origin can embed this page in a frame.             | Further protects against clickjacking by controlling who can frame Aidbox pages .                     |
| `img-src`         | `'self'` `data:`                                                | Limits image sources to the same origin.                                                 | Prevents data leaks via malicious or unauthorized images.                                             |
| `manifest-src`    | `'self'`                                                        | Ensures that web app manifests are loaded only from the same origin.                     | Protects against unauthorized or malicious web app manifests being loaded into Aidbox.                |
| `media-src`       | `'self'`                                                        | Restricts audio and video sources to the same origin.                                    | Prevents unauthorized media files from being loaded into Aidbox                                       |
| `worker-src`      | `'self'`                                                        | Limits web workers and shared workers to scripts from the same origin.                   | Reduces risks of malicious workers being executed within your Aidbox context.                         |

Please refer to the [OWASP Content Security Policy Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Content_Security_Policy_Cheat_Sheet.html) for additional guidance

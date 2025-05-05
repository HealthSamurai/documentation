# Optional environment variables

### JAVA\_OPTS

```
JAVA_OPTS="<string>"
```

Configure general JAVA options. For example - request and max heap size configuration.

```
JAVA_OPTS="-Xms1024m -Xmx1024m"
```

### AIDBOX\_BOX\_ID

Assigns unique id for Aidbox instance. Important to set if you deploy few Aidbox instances and wish to separate their telemetry data (logs, metrics, traces) in your observability system.

### AIDBOX\_BASE\_URL

```
AIDBOX_BASE_URL=<url>
```

Aidbox Base URL is URL Aidbox is available at. It consists of schema (http, https), domain, port (optional) and URL path (optional). Trailing slash is not allowed.

Default is

```
http://localhost:[AIDBOX_PORT]
```

{% hint style="info" %}
Examples:

`AIDBOX_BASE_URL`=`http://fhir.example.com`

`AIDBOX_BASE_URL`=`http://fhir.example.com:8080`

`AIDBOX_BASE_URL`=`http://fhir.example.com/aidbox`
{% endhint %}

### AIDBOX\_DB\_PARAM\_\*

```
AIDBOX_DB_PARAM_<parameter name>=<parameter value>
```

Parameters prefixed with `AIDBOX_DB_PARAM_` will be passed to [JDBC PostgreSQL connection string](https://jdbc.postgresql.org/documentation/use/#connection-parameters).

### AIDBOX\_ES\_URL

If provided, enables mode to push logs to ElasticSearch.

### BOX\_SEARCH\_DEFAULT\_\_PARAMS\_TOTAL

```
box_search_default__params_total=<value>
```

`value` is one of: `none`, `estimate`, `accurate`.

Sets the default total search parameter value.

{% hint style="warning" %}
if you use `box_search_default__params_total=none` you still get `total`when:

1. you don't use `_page`
2. the number of returned resources is less than `_count` (by default is 100).
{% endhint %}

### AIDBOX\_ES\_AUTH

```
AIDBOX_ES_AUTH=<user>:<password>
```

Basic auth credentials for ElasticSearch. API key is not supported.

### AIDBOX\_ES\_BATCH\_SIZE

```
AIDBOX_ES_BATCH_SIZE=<size>
```

Log batch size used to optimize log shipping performance. The default value is 200

### AIDBOX\_ES\_BATCH\_TIMEOUT

```
AIDBOX_ES_BATCH_TIMEOUT=<timeout>
```

Timeout to post a batch to ElasticSearch. If there is not enough records to reach full batch size

### AIDBOX\_ES\_INDEX\_PAT

```
AIDBOX_ES_INDEX_PAT=<format>
```

Custom index format string. The default value is 'aidbox-logs'-yyyy-MM-dd.

### AIDBOX\_LOGS

```
AIDBOX_LOGS=<filepath>
```

If provided, enables mode to pipe logs as json into the file by specified path. If ElasticSearch URL is provided then the file is used as a fallback in case if ElasticSearch is not available

### AIDBOX\_LOGS\_MAX\_LINES

```
AIDBOX_LOGS_MAX_LINES=<max-lines>
```

Sets the limit of log records to push into the file. When the limit is reached, the current log file is renamed with ".old" postfix and a new log file is created. The default value is "10000"

### BOX\_LOGGING\_DISABLE\_\_HEALTH\_\_LOGS

```
BOX_LOGGING_DISABLE__HEALTH__LOGS=<boolean>
```

Disable `/health` endpoint requests logging. Default value is `false`.

### BOX\_LOGGING\_SQL\_MIN\_\_DURATION

```
BOX_LOGGING_SQL_MIN__DURATION=<integer>
```

Threshold for logging only long queries. Analogue from Postgres.

Log only requests whose execution time exceeds the specified number of milliseconds.

### BOX\_LOGGING\_SQL\_MAX\_\_LENGTH

```
BOX_LOGGING_SQL_MAX__LENGTH=<integer>
```

Max length of a query to be logged.

### AIDBOX\_INSTALL\_PG\_EXTENSIONS

```
AIDBOX_INSTALL_PG_EXTENSIONS=<boolean>
```

Says Aidbox to install PostgreSQL extensions at startup time. The default value is `true`.

### AIDBOX\_STDOUT\_JSON

```
AIDBOX_STDOUT_JSON=<log-level>
```

`log-level` is one of: `off`, `fatal`, `error`, `warn`, `info`, `debug`, `trace`, `all`.

By setting one of these values you would also get all the values to the left. e.g. if you set log level to `warn` you would also get log events with `fatal` and `error` levels (`off` is excluded).

#### Example of the log output

```json
{"sql":"SELECT 1","d":2,"ts":"2022-10-26T10:59:59.825Z","w":"main","ev":"db/q"}
```

### AIDBOX\_STDOUT\_GOOGLE\_JSON

Produces logs in Google Logging format (see [LogEntry](https://cloud.google.com/logging/docs/reference/v2/rest/v2/LogEntry)).

```
AIDBOX_STDOUT_GOOGLE_JSON=<log-level>
```

`log-level` is one of: `off`, `fatal`, `error`, `warn`, `info`, `debug`, `trace`, `all`.

By setting one of these values you would also get all the values to the left. e.g. if you set log level to `warn` you would also get log events with `fatal` and `error` levels (`off` is excluded).

#### Example of the log output

```json
{"sql":"SELECT 1","d":2,"timestamp":"2022-10-26T10:59:59.825Z","severity":"INFO","w":"main","ev":"db/q"}
```

### AIDBOX\_STDOUT\_PRETTY

```
AIDBOX_STDOUT_PRETTY=<log-level>
```

`log-level` is one of: `off`, `fatal`, `error`, `warn`, `info`, `debug`, `trace`, `all`.

By default `log-level` is `error`.

By setting one of these values you would also get all the values to the left. e.g. if you set log level to `warn` you would also get log events with `fatal` and `error` levels (`off` is excluded).

#### Example of the log output

```
11:01:12 main [1ms] SELECT 1
```

### AIDBOX\_DD\_API\_KEY

```
AIDBOX_DD_API_KEY=true
```

If provided, enables mode to push logs to DataDog

### AIDBOX\_DD\_BATCH\_SIZE

```
AIDBOX_DD_BATCH_SIZE=<batch-size>
```

Size of log batch, used to optimize performance of log shipping. The default value is 200

### AIDBOX\_DD\_BATCH\_TIMEOUT

```
AIDBOX_DD_BATCH_TIMEOUT=<timeout-ms>
```

Timeout (in ms) to post a batch to DataDog if there are not enough records to reach full batch size. Default value: 3600000 (1 hour)

### AIDBOX\_DD\_LOGS

```
AIDBOX_DD_LOGS=<filepath>
```

Fallback file to write logs in if uploading to DataDog fails

### AIDBOX\_CREATED\_AT\_URL

```
AIDBOX_CREATED_AT_URL=<url>
```

Overrides createdAt extension url, default is `ex:createdAt`

### AIDBOX\_CORRECT\_AIDBOX\_FORMAT

```
AIDBOX_CORRECT_AIDBOX_FORMAT=true
```

If provided, activates transforming unknown polymorphic extensions to the correct Aidbox format avoiding keeping them at FHIR-format.

For example, `extension.*.valueString` stored as `extension.0.value.string`

### BOX\_CACHE\_REPLICATION\_DISABLE

```
BOX_CACHE_REPLICATION_DISABLE=true
```

By default, Aidbox works in multi-replica mode, so more than one Aidbox replica could be connected to the same database. If you are sure you'll be running only one Aidbox replica, you could disable replication mechanism with this variable. Check [Highly Available Aidbox](https://docs.aidbox.app/getting-started/run-aidbox-in-kubernetes/high-available-aidbox) for additional information.

### AIDBOX\_DEV\_MODE

```
AIDBOX_DEV_MODE=true
```

Enables `_debug=policy` for [access policy debugging](https://docs.aidbox.app/security-and-access-control-1/security/access-policy#policy-debugging)

### AIDBOX\_ZEN\_ENTRYPOINT

```
AIDBOX_ZEN_ENTRYPOINT=<entrypoint>
```

Specifies entry point for loading Aidbox configuration. Example:

```
AIDBOX_ZEN_ENTRYPOINT=main/box
```

### AIDBOX\_ZEN\_DEV\_MODE

```
AIDBOX_ZEN_DEV_MODE=true
```

Enables watcher which reloads zen namespaces when they change.

### AIDBOX\_ZEN\_PATHS

```
AIDBOX_ZEN_PATHS=<source>:<format>:<path>[,<source>:<format>:<path>]*
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

### AIDBOX\_EXTENSION\_SCHEMA

```
AIDBOX_EXTENSION_SCHEMA=<schema>
```

Schema for PostgreSQL extensions. Default is current schema. See [use different PostgreSQL schema section](optional-environment-variables.md#use-different-postgresql-schema).

### AIDBOX\_SECURITY\_AUDIT\_\_LOG\_ENABLED

```
AIDBOX_SECURITY_AUDIT__LOG_ENABLED=true
```

Enable producing audit logs in FHIR AuditEvent format for significant events.

### BOX\_SEARCH\_DEFAULT\_\_PARAMS\_COUNT

```
BOX_SEARCH_DEFAULT__PARAMS_COUNT=<count>
```

Overrides the default count search parameter value. 100 is the default value. The provided value should be <= 1000

### BOX\_SEARCH\_FHIR\_\_COMPARISONS

```
BOX_SEARCH_FHIR__COMPARISONS=true
```

Use FHIR compliant [date search](https://www.hl7.org/fhir/search.html#prefix).

### BOX\_SEARCH\_INCLUDE\_CONFORMANT

```
BOX_SEARCH_INCLUDE_CONFORMANT=true
```

When set to true, the behavior of \_include and \_revinclude becomes FHIR conformant:

1. Without the :recur or :iterate modifier \_(rev)include is only applied to the initial result.
2. With the :recur or :iterate modifier \_(rev)include is repeatedly applied to the resources found in the previous step.

### BOX\_SEARCH\_AUTHORIZE\_\_INLINE\_\_REQUESTS

```
BOX_SEARCH_AUTHORIZE_INLINE_REQUESTS=true
```

[https://docs.aidbox.app/api-1/fhir-api/search-1/search-parameters-list/\_include-and-\_revinclude#authorize-inline-requests-mode](https://docs.aidbox.app/api-1/fhir-api/search-1/search-parameters-list/_include-and-_revinclude#authorize-inline-requests-mode)

### BOX\_SEARCH\_INCLUDE\_ITERATE\_\_MAX

```
BOX_SEARCH_INCLUDE_ITERATE__MAX=10
```

Maximum number of iterations for \_include and \_revinclude with :recur or :iterate modifier. The default value is 10. If set to 0, queries for \_(rev)include will not be performed. If set to a negative value, no limit will be applied.

### BOX\_SEARCH\_RESOURCE\_\_COMPAT

```yaml
BOX_SEARCH_RESOURCE__COMPAT=false
```

`false` to use preferred version of zen-search (`true` to backward compatibility zen search)

### BOX\_COMPATIBILITY\_VALIDATION\_JSON\_\_SCHEMA\_REGEX

```
BOX_COMPATIBILITY_VALIDATION_JSON__SCHEMA_REGEX="#{:fhir-datetime}"
```

Enables strict date time validation in JSON schema validation engine per [FHIR spec](https://www.hl7.org/fhir/datatypes.html#dateTime).

### BOX\_COMPATIBILITY\_AUTH\_PKCE\_CODE\_\_CHALLENGE\_S256\_CONFORMANT

```
BOX_COMPATIBILITY_AUTH_PKCE_CODE__CHALLENGE_S256_CONFORMANT=true
```

Use conformant S256 code challenge validation scheme.

### BOX\_DEBUG\_SU\_ENABLE

```
BOX_DEBUG_SU_ENABLE=true
```

Enables `su` request header [functionalty](https://docs.aidbox.app/security-and-access-control-1/security/debug#su-request-header).

### BOX\_FEATURES\_VALIDATION\_SKIP\_REFERENCE

```
BOX_FEATURES_VALIDATION_SKIP_REFERENCE=true
```

Enables skip resource reference validation [functionality](../../modules/profiling-and-validation/profiling.md).

### BOX\_WEB\_MAX\_\_BODY

```
BOX_WEB_MAX__BODY=<max-size-bytes>
```

Maximum size of request body in bytes. Default is 20971520 (20 MiB)

### BOX\_WEB\_THREAD

```
BOX_WEB_THREAD=<count-of-web-worker-threads>
```

Count of HTTP server web workers. Default is 8

### BOX\_WEB\_MAX\_\_LINE

```
BOX_WEB_MAX__LINE=<max-line-bytes>
```

Length limit for HTTP initial line and per header, 414(Request-URI Too Long) will be returned if exceeding this limit. Default to 8192.

### BOX\_WEB\_CORS\_ENABLED

```
BOX_WEB_CORS_ENABLED=true
```

Allow CORS requests

### BOX\_WEB\_CORS\_ORIGINS

```
BOX_WEB_CORS_ORIGINS=*
```

Comma-separated list of allowed origins `[schema]://[domain]:[port]`

The default value is wildcard "\*"

### BOX\_FEATURES\_TERMINOLOGY\_IMPORT\_SYNC

```
BOX_FEATURES_TERMINOLOGY_IMPORT_SYNC=true
```

Enables synchronous terminology bundle import

### BOX\_FEATURES\_AUTHENTICATION\_INTROSPECTION\_CREATE\_\_USER

```
BOX_FEATURES_AUTHENTICATION_INTROSPECTION_CREATE__USER=<boolean>
```

Create a user when using foreign JWT access token and the user does not already exist.

### BOX\_FEATURES\_GRAPHQL\_WARMUP\_\_ON\_\_STARTUP

`BOX_FEATURES_GRAPHQL_WARMUP__ON__STARTUP=<boolean>`

Warmup graphql caches on startup

### BOX\_FEATURES\_GRAPHQL\_TIMEOUT

BOX\_FEATURES\_GRAPHQL\_TIMEOUT`=<integer>`

Sets timeout for graphql queries in seconds. Default value is `60`.

### BOX\_FEATURES\_FHIR\_TRANSACTION\_MAX\_\_ISOLATION\_\_LEVEL

BOX\_FEATURES\_FHIR\_TRANSACTION\_MAX\_\_ISOLATION\_\_LEVEL=\<isolation-level>

`isolation-level` is one of: `none`, `read-committed`, `repeatable-read`, `serializable`.

Sets maximum (inclusive) isolation level for transactions. This value can be overridden by `x-max-isolation-level` header (see [here](../../api/rest-api/crud-1/update.md#isolation-levels)).

### BOX\_CONFIG\_FEATURES\_INDEX\_SYNC\_\_ON\_\_START

BOX\_CONFIG\_FEATURES\_INDEX\_SYNC\_\_ON\_\_START=\<boolean>

If enabled, Aidbox synchronizes managed index on startup.

### Enable Aidbox compliance mode

`AIDBOX_COMPLIANCE=enabled`:

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

These parameters will enable SSL connection from Aidbox to postgresql Docs on JDBC PostgreSQL connection string are here: [https://jdbc.postgresql.org/documentation/80/connect.html](https://jdbc.postgresql.org/documentation/80/connect.html) [https://jdbc.postgresql.org/documentation/head/ssl-client.html](https://jdbc.postgresql.org/documentation/head/ssl-client.html)

The next step is to configure your database to accept SSL connections. You can do that by passing your own postgresql.conf with argument -c config\_file passed into the db containter and probably you want to set up postgres to receive only SSL connections, you can do that by passing your own pg\_hba.conf file with -c hba\_file

### Use different PostgreSQL schema

By default Aidbox uses `public` schema. If you want Aidbox to use different schema, set [JDBC parameter `currentSchema`](https://jdbc.postgresql.org/documentation/head/connect.html#connection-parameters) using environment variable `AIDBOX_DB_PARAM_CURRENT_SCHEMA`:

```
AIDBOX_DB_PARAM_CURRENT_SCHEMA=myschema
```

PostgreSQL extensions can create objects. By default PostgreSQL sets up extension to use current schema. If you are going to share database between multiple applications, we recommend to create a dedicated schema for the extensions.

Use `AIDBOX_EXTENSION_SCHEMA` environment variable to set up Aidbox to use dedicated extension schema:

```
AIDBOX_EXTENSION_SCHEMA=myextensionschema
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

Then change `AIDBOX_EXTENSION_SCHEMA` and restart Aidbox.

### Set up RSA private/public keys and secret

Aidbox generates JWT tokens for different purposes:

* As part of OAuth 2.0 authorization it generates authorization\_code in JWT format
* If you specify auth token format as JWT, then your access\_token and refresh\_token will be in JWT format.

Aidbox supports two signing algorithms: RS256 and HS256. RS256 expects providing private key for signing JWT and public key for verifing it. As far as HS256 needs only having secret for both operations.

{% hint style="warning" %}
Attention: by default Aidbox generates both keypair and secret on every startup. This means that on every start all previously generated JWT will be invalid. In order to avoid such undesirable situation, you may pass RSA keypair and secret as Aidbox parameters.\\

It is required to pass RSA keypair and secret as Aidbox parameters if you have multiple replicas of the same Aidbox/Multibox instance.
{% endhint %}

#### Generate RSA keypair

Generate private key with `openssl genrsa -traditional -out key.pem 2048` in your terminal. Private key will be saved in file `key.pem`. To generate public key run `openssl rsa -in key.pem -outform PEM -pubout -out public.pem`. You will find public key in `public.pem` file.

Use next env vars to pass RSA keypair:

```
BOX_AUTH_KEYS_PRIVATE=-----BEGIN RSA PRIVATE KEY-----\n...\n-----END RSA PRIVATE KEY-----
BOX_AUTH_KEYS_PUBLIC=-----BEGIN PUBLIC KEY-----\n...\n-----END PUBLIC KEY-----
```

#### Generate secret

To generate random string for HS256 algoritm you can run `openssl rand -base64 36` command. The length of the random string must be more than 256 bits (32 bytes).

use next env var to pass secret param:

```
BOX_AUTH_KEYS_SECRET=<rand_string>
```

### Configure Aidbox WEB server workers and DB connection pool

By default Aidbox and Multibox runs with 8 web workers and 8 DB connection pool size. That means that Aidbox can process at same time 8 concurrent connections.

A good practice is stay pool size the same as CPU count of your database. For example, if your database has 16 CPU cores you can set `BOX_DB_POOL_MAXIMUM__POOL__SIZE=16`. Box web workers count is dependent on your load profile. For example, if you have a lot of fast read queries you can set `BOX_WEB_THREAD` equal x2 or x3 of your DB pool size (32 or 48). Or if you have a lot of batch insert queries we recommend stay web workers count as the same DB pool size.

You can configure this parameter using following environment variables.

```
BOX_DB_POOL_MAXIMUM__POOL__SIZE=8
BOX_WEB_THREAD=8
```

### Telemetry

By default, Aidbox collects and sends high-level anonymous API usage statistics used solely for Aidbox improvement.

#### BOX\_TELEMETRY\_ERRORS

```
BOX_TELEMETRY_ERRORS=false
```

Disable sending anonymous errors data.

#### BOX\_TELEMETRY\_USAGE\_\_STATS

```
BOX_TELEMETRY_USAGE_STATS=false
```

Disable sending anonymous API usage statistics.

### Security Labels

#### BOX\_FEATURES\_SECURITY\_\_LABELS\_ENABLE

The [Security Labels](optional-environment-variables.md#security-labels) access control feature is disabled by default. To enable it, set the env to true.

```ini
BOX_FEATURES_SECURITY__LABELS_ENABLE=true
```

#### BOX\_FEATURES\_SECURITY\_\_LABELS\_STRIP\_\_LABELS

By default, stripping is disabled. To enable it, set the env to true.

```ini
BOX_FEATURES_SECURITY__LABELS_STRIP__LABELS=true
```

{% hint style="info" %}
Stripping is only applied during the masking. More [details about masking](../../modules/access-control/authorization/security-labels.md#resource-element-level-access-masking)
{% endhint %}

### Observability

Follow the link below to learn how Aidbox metrics work.

{% content-ref url="../../modules/observability/metrics/" %}
[metrics](../../modules/observability/metrics/)
{% endcontent-ref %}

#### **BOX\_METRICS\_PORT**

Defines the port which will be used to expose metrics.

#### **BOX\_METRICS\_POSTGRES\_ON**

```
BOX_METRICS_POSTGRES_ON=false
```

If you have a different pg exporter, disable Aidbox PostgreSQL metrics to avoid metrics duplication by setting the env to false.

#### **BOX\_METRICS\_GRAFANA\_URL**

Specify the Grafana instance URL in this env.

#### **BOX\_METRICS\_GRAFANA\_USER**

Specify the Grafana user name.

#### **BOX\_METRICS\_GRAFANA\_PASSWORD**

Specify the Grafana user password.

### Content security policy

AIDBOX\_CONTENT\_SECURITY\_POLICY\_HEADER

A Content Security Policy (CSP) is a security mechanism that helps protect web applications from threats like Cross-Site Scripting (XSS), data injection, and clickjacking. It works by specifying rules for browsers about which resources (e.g., scripts, styles, images) can be loaded and executed.

```ini
AIDBOX_CONTENT_SECURITY_POLICY_HEADER=default-src 'self'; script-src 'report-sample' 'self' 'unsafe-inline' 'unsafe-eval'; style-src 'report-sample' 'self' 'unsafe-inline'; object-src 'none'; base-uri 'self'; connect-src 'self'; font-src 'self'; frame-src 'self'; frame-ancestors 'self'; img-src 'self' data:; manifest-src 'self'; media-src 'self'; worker-src 'self';
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

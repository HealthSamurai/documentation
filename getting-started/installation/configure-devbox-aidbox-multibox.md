---
description: >-
  The topic explains configuration options of Devbox, Aidbox and Multibox
  distributions
---

# Configure Devbox/Aidbox/Multibox

## Introduction

[Devbox](https://hub.docker.com/r/healthsamurai/devbox) is a lightweight version of Aidbox. It is a free version aimed at local development and can not be used to store real healthcare data or PHI.

[Aidbox](https://hub.docker.com/r/healthsamurai/aidboxone) is a full-featured single instance of the Aidbox FHIR server. If you are interested in multi-tenant Aidbox, consider using [Multibox ](https://docs.aidbox.app/getting-started/installation/use-devbox-aidbox)distribution.

All distributions can be used both on  [healthsamurai/aidboxdb](https://hub.docker.com/r/healthsamurai/aidboxdb) PostgreSQL distribution or managed PostgreSQLs.

All distributions require license details to be provided. Alongside with online license server, we offer a special license server distribution for the deployment environments that do not have access to the internet. Contact your support team for the details.

### Required environment variables

| Env variable name       | Meaning                                                           |
| ----------------------- | ----------------------------------------------------------------- |
| `AIDBOX_PORT`           | A port that webserver will listen to                              |
| `AIDBOX_FHIR_VERSION`   | Version of FHIR - 1.0.2, 1.4.0, 1.8.0, 3.0.1, 3.2.0, 3.3.0, 4.0.0 |
| `PGHOST`                | Aidboxdb host address                                             |
| `PGPORT`                | Aidboxdb port                                                     |
| `PGDATABASE`            | Name of the database that is used to store resources              |
| `PGUSER`                | Database role name to use                                         |
| `PGPASSWORD`            | A password of that role                                           |
| `AIDBOX_CLIENT_ID`      | Root Client resource id to create on startup                      |
| `AIDBOX_CLIENT_SECRET`  | A secret for that Client                                          |
| `AIDBOX_ADMIN_ID`       | Root User resource id to create on startup                        |
| `AIDBOX_ADMIN_PASSWORD` | A password for that User                                          |
| `AIDBOX_LICENSE_ID`     | License id obtained from the license server                       |
| `AIDBOX_LICENSE_KEY`    | Key for that license                                              |

### Optional environment variables

| Env variable name                                                    | Meaning                                                                                                                                                                                                                                                                                                                                                                   |
| -------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `AIDBOX_BASE_URL`                                                    | <p>URL to use in links between resources. </p><p>Defaults to http://localhost:[AIDBOX_PORT]</p>                                                                                                                                                                                                                                                                           |
| `AIDBOX_DB_PARAM_*`                                                  | <p>Parameters prefixed with <code>AIDBOX_DB_PARAM_</code> will be passed to <a href="https://jdbc.postgresql.org/documentation/80/connect.html">JDBC PostgreSQL connection string</a>. </p><p></p><p>More details <a href="https://docs.aidbox.app/getting-started/installation/configure-devbox-aidbox-multibox#configuring-ssl-connection-with-postgresql">here</a></p> |
| `AIDBOX_ES_URL`                                                      | If provided, enables mode to push logs to ElasticSearch                                                                                                                                                                                                                                                                                                                   |
| `AIDBOX_ES_AUTH`                                                     | Basic auth credentials for ElasticSearch in `user:password` format                                                                                                                                                                                                                                                                                                        |
| `AIDBOX_ES_BATCH_SIZE`                                               | Log batch size used to optimize log shipping performance. The default value is 200                                                                                                                                                                                                                                                                                        |
| `AIDBOX_ES_BATCH_TIMEOUT`                                            | Timeout to post a batch to ElasticSearch. If there is not enough records to reach full batch size                                                                                                                                                                                                                                                                         |
| `AIDBOX_ES_INDEX_PAT`                                                | Custom index format string. The default value is 'aidbox-logs'-yyyy-MM-dd.                                                                                                                                                                                                                                                                                                |
| `AIDBOX_LOGS`                                                        | If provided, enables mode to pipe logs as json into the file by specified path. If ElasticSearch URL is provided then the file is used as a fallback in case if ElasticSearch is not available                                                                                                                                                                            |
| `AIDBOX_LOGS_MAX_LINES`                                              | Sets the limit of log records to push into the file. When the limit is reached, the current log file is renamed with ".old" postfix and a new log file is created. The default value is "10000"                                                                                                                                                                           |
| `AIDBOX_STDOUT_JSON`                                                 | If provided, enables mode to write logs as json into stdout                                                                                                                                                                                                                                                                                                               |
| `AIDBOX_STDOUT_PRETTY`                                               | If provided, enables mode to write logs in prettified format into stdout                                                                                                                                                                                                                                                                                                  |
| `AIDBOX_DEVLOGS`                                                     | If provided, pushes logs into \_logs table of aidboxdb. Can be useful for testing and debugging                                                                                                                                                                                                                                                                           |
| `AIDBOX_DD_API_KEY`                                                  | If provided, enables mode to push logs to DataDog                                                                                                                                                                                                                                                                                                                         |
| `AIDBOX_DD_BATCH_SIZE`                                               | Size of log batch, used to optimize performance of log shipping. The default value is 200                                                                                                                                                                                                                                                                                 |
| `AIDBOX_DD_BATCH_TIMEOUT`                                            | Timeout (in ms) to post a batch to DataDog if there are not enough records to reach full batch size. Default value: 3600000 (1 hour)                                                                                                                                                                                                                                      |
| `AIDBOX_DD_LOGS`                                                     | Fallback file to write logs in if uploading to DataDog fails                                                                                                                                                                                                                                                                                                              |
| `AIDBOX_CREATED_AT_URL`                                              | Overrides createdAt extension url, default is `ex:createdAt`                                                                                                                                                                                                                                                                                                              |
| `AIDBOX_CORRECT_AIDBOX_FORMAT`                                       | <p>If provided, activates transforming unknown polymorphic extensions to the correct Aidbox format avoiding keeping them at FHIR-format.</p><p></p><p>For example,</p><p><code>extension.*.valueString</code> stored as <code>extension.0.value.string</code></p>                                                                                                         |
| `AIDBOX_DEV_MODE`                                                    | Enables  `_debug=policy` for [access policy debugging](https://docs.aidbox.app/security-and-access-control-1/security/access-policy#policy-debugging)                                                                                                                                                                                                                     |
| `AIDBOX_ZEN_ENTRYPOINT`                                              | Specifies entry point for loading Aidbox configuration                                                                                                                                                                                                                                                                                                                    |
| `AIDBOX_ZEN_PATHS`                                                   | Specifies how Aidbox loads project                                                                                                                                                                                                                                                                                                                                        |
| `box_search_default__params_count`                                   | Overrides the default count search parameter value. 100 is the default value. The provided value should be <= 1000                                                                                                                                                                                                                                                        |
| `box_compatibility_validation_json__schema_regex="#{:fhir-datetime}` | Enables strict date time validation in JSON schema validation engine per [FHIR spec](https://www.hl7.org/fhir/datatypes.html#dateTime).                                                                                                                                                                                                                                   |
| `box_compatibility_auth_pkce_code__challenge_s256_conformant`        | Use conformant S256 code challenge validation scheme.                                                                                                                                                                                                                                                                                                                     |
| `box_debug_su_enable`        | Enables `su` request header [functionalty](https://docs.aidbox.app/security-and-access-control-1/security/debug#su-request-header) |
| `box_feature_validation_skip_reference`        | Enables skip resource reference validation [functionality](/profiling-and-validation/profiling#aidbox-validation-skip-request-header) |



### Multibox required environment variables

| Env variable name       | Meaning                                                                                                                                              |
| ----------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------- |
| `AIDBOX_LICENSE`        | Multibox instance license                                                                                                                            |
| `AIDBOX_CLUSTER_SECRET` | Secret to access cluster                                                                                                                             |
| `AIDBOX_CLUSTER_DOMAIN` | <p>Used to route users between the Aidbox instances</p><p></p><p>For example: <code>aidbox.example.com</code></p>                                    |
| `AIDBOX_SUPERUSER`      | <p>Root user login and password separated by colon character (<code>:</code>) for Multibox</p><p></p><p>For example: <code>login:password</code></p> |

### Enable Aidbox compliance mode

`AIDBOX_COMPLIANCE=enabled`:

\- Adds various attributes and endpoints info to CapabilityStatement

\- Adds CapabilityStatement sanitizing (i.e. removes attributes containing `null` values and empty arrays)

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

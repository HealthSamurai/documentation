# Environment Variables

The Auditbox application can be configured using the following environment variables.

## Elasticsearch URI<a href="#elastic-uri" id="elastic-uri"></a>

```yaml
ELASTIC_URI: "http://elasticsearch:9200"
```

URI for Elasticsearch connection.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Required</td><td><code>true</code></td></tr></tbody></table></details>

## Auditbox base URL<a href="#auditbox-base-url" id="auditbox-base-url"></a>

```yaml
AUDITBOX_BASE_URL: "http://localhost:3002"
```

Base URL where Auditbox is hosted.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Required</td><td><code>true</code></td></tr></tbody></table></details>

## Identity provider authorize endpoint<a href="#idp-authorize-endpoint" id="idp-authorize-endpoint"></a>

```yaml
IDP_AUTHORIZE_ENDPOINT: "http://localhost:8888/realms/auditbox/protocol/openid-connect/auth"
```

Authorization endpoint for your identity provider.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Required</td><td><code>true</code></td></tr></tbody></table></details>

## Identity provider token endpoint<a href="#idp-token-endpoint" id="idp-token-endpoint"></a>

```yaml
IDP_TOKEN_ENDPOINT: "http://keycloak:8888/realms/auditbox/protocol/openid-connect/token"
```

Token endpoint for your identity provider.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Required</td><td><code>true</code></td></tr></tbody></table></details>

## Identity provider client ID<a href="#idp-client-id" id="idp-client-id"></a>

```yaml
IDP_CLIENT_ID: "auditbox"
```

OAuth client ID for authentication.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Required</td><td><code>true</code></td></tr></tbody></table></details>

## Identity provider client secret<a href="#idp-client-secret" id="idp-client-secret"></a>

```yaml
IDP_CLIENT_SECRET: "<String>"
```

OAuth client secret for authentication.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Required</td><td><code>true</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value should be kept secret</td></tr></tbody></table></details>

## API authentication enabled<a href="#auditbox-api-auth-enabled" id="auditbox-api-auth-enabled"></a>

```yaml
AUDITBOX_API_AUTH_ENABLED: true
```

Enable or disable API authentication.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Required</td><td><code>true</code></td></tr></tbody></table></details>

## Capability statement path<a href="#capability-statement-path" id="capability-statement-path"></a>

```yaml
CAPABILITY_STATEMENT_PATH: "capability_statement.edn"
```

Path to the capability statement file.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>capability_statement.edn</code></td></tr><tr><td>Required</td><td><code>false</code></td></tr></tbody></table></details>

## Network binding<a href="#binding" id="binding"></a>

```yaml
BINDING: "127.0.0.1"
```

Network binding address for the server.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>127.0.0.1</code></td></tr><tr><td>Required</td><td><code>false</code></td></tr></tbody></table></details>

## Port<a href="#port" id="port"></a>

```yaml
PORT: 3000
```

Port on which the application listens.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>Type</td><td>Integer</td></tr><tr><td>Default value</td><td><code>3000</code></td></tr><tr><td>Required</td><td><code>false</code></td></tr></tbody></table></details>

## BALP version<a href="#balp-version" id="balp-version"></a>

```yaml
BALP_VERSION: "1.1.3"
```

Supported BALP (Basic Audit Log Patterns) version.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>1.1.0</code><br /><code>1.1.1</code><br /><code>1.1.2</code><br /><code>1.1.3</code></td></tr><tr><td>Default value</td><td><code>1.1.3</code></td></tr><tr><td>Required</td><td><code>false</code></td></tr></tbody></table></details>

## Elasticsearch authentication<a href="#auditbox-es-auth" id="auditbox-es-auth"></a>

```yaml
AUDITBOX_ES_AUTH: "<user>:<password>"
```

Elasticsearch basic authentication credentials. Implemented and tested for the `superadmin` role in Elasticsearch.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>Type</td><td>String</td></tr><tr><td>Format</td><td><code>&lt;user&gt;:&lt;password&gt;</code></td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Required</td><td><code>false</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value should be kept secret</td></tr></tbody></table></details>

## Log level<a href="#auditbox-log-level" id="auditbox-log-level"></a>

```yaml
AUDITBOX_LOG_LEVEL: "debug"
```

Application logging level.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>off</code><br /><code>debug</code><br /><code>info</code><br /><code>error</code></td></tr><tr><td>Default value</td><td><code>debug</code></td></tr><tr><td>Required</td><td><code>false</code></td></tr></tbody></table></details>

## S3 archive enabled<a href="#auditbox-archive-s3-enabled" id="auditbox-archive-s3-enabled"></a>

```yaml
AUDITBOX_ARCHIVE_S3_ENABLED: false
```

Enables S3 archival strategy with backups.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Required</td><td><code>false</code></td></tr></tbody></table></details>

## Data retention days<a href="#auditbox-data-retention-days" id="auditbox-data-retention-days"></a>

```yaml
AUDITBOX_DATA_RETENTION_DAYS: 30
```

Number of days to retain audit events before archiving (must be >= 1).

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>Type</td><td>Integer</td></tr><tr><td>Default value</td><td><code>30</code></td></tr><tr><td>Required</td><td><code>false</code></td></tr></tbody></table></details>

## Snapshot repository name<a href="#auditbox-snapshot-repository-name" id="auditbox-snapshot-repository-name"></a>

```yaml
AUDITBOX_SNAPSHOT_REPOSITORY_NAME: "default"
```

Name of the Elasticsearch snapshot repository for backups.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>default</code></td></tr><tr><td>Required</td><td><code>false</code></td></tr></tbody></table></details>

## S3 bucket name<a href="#auditbox-s3-bucket-name" id="auditbox-s3-bucket-name"></a>

```yaml
AUDITBOX_S3_BUCKET_NAME: "<String>"
```

S3 bucket name for snapshot storage. Required when `AUDITBOX_ARCHIVE_S3_ENABLED` is `true`.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Required</td><td><code>true</code> when <code>AUDITBOX_ARCHIVE_S3_ENABLED</code> is enabled</td></tr></tbody></table></details>

## S3 endpoint<a href="#auditbox-s3-endpoint" id="auditbox-s3-endpoint"></a>

```yaml
AUDITBOX_S3_ENDPOINT: "https://s3.amazonaws.com"
```

S3 endpoint URL. Required when `AUDITBOX_ARCHIVE_S3_ENABLED` is `true`.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Required</td><td><code>true</code> when <code>AUDITBOX_ARCHIVE_S3_ENABLED</code> is enabled</td></tr></tbody></table></details>

## Example Configuration

Here's an example `docker-compose.yml` environment section with all key variables:

```yaml
environment:
  # Required
  ELASTIC_URI: http://elasticsearch:9200
  AUDITBOX_BASE_URL: http://localhost:3002
  IDP_AUTHORIZE_ENDPOINT: http://localhost:8888/realms/auditbox/protocol/openid-connect/auth
  IDP_TOKEN_ENDPOINT: http://keycloak:8888/realms/auditbox/protocol/openid-connect/token
  IDP_CLIENT_ID: auditbox
  IDP_CLIENT_SECRET: super-secret
  AUDITBOX_API_AUTH_ENABLED: true

  # Optional
  BINDING: 0.0.0.0
  PORT: 3000
  BALP_VERSION: 1.1.3
  AUDITBOX_LOG_LEVEL: info

  AUDITBOX_ARCHIVE_S3_ENABLED: true
  AUDITBOX_DATA_RETENTION_DAYS: 90
  AUDITBOX_SNAPSHOT_REPOSITORY_NAME: my-s3
  AUDITBOX_S3_BUCKET_NAME: es-backups-bucket
  AUDITBOX_S3_ENDPOINT: http://minio:9000
```

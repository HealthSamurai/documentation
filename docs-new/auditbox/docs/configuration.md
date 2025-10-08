# Configuration

## Environment Variables

The Auditbox application can be configured using the following environment variables:

### Required Variables

- **`ELASTIC_URI`** (required)
  URI for Elasticsearch connection (e.g., `http://elasticsearch:9200`)

- **`AUDITBOX_BASE_URL`** (required)
  Base URL where Auditbox is hosted (e.g., `http://localhost:3002`)

- **`IDP_AUTHORIZE_ENDPOINT`** (required)
  Authorization endpoint for your identity provider (e.g., `http://localhost:8888/realms/auditbox/protocol/openid-connect/auth`)

- **`IDP_TOKEN_ENDPOINT`** (required)
  Token endpoint for your identity provider (e.g., `http://keycloak:8888/realms/auditbox/protocol/openid-connect/token`)

- **`IDP_CLIENT_ID`** (required)
  OAuth client ID for authentication (e.g., `auditbox`)

- **`IDP_CLIENT_SECRET`** (required)
  OAuth client secret for authentication

- **`AUDITBOX_API_AUTH_ENABLED`** (required)
  Enable or disable API authentication (`true` or `false`)

### Optional Variables

- **`CAPABILITY_STATEMENT_PATH`**
  Default: `capability_statement.edn`
  Path to the capability statement file

- **`BINDING`**
  Default: `127.0.0.1`
  Network binding address for the server

- **`PORT`**
  Default: `3000`
  Port on which the application listens

- **`BALP_VERSION`**
  Default: `1.1.3`
  Supported BALP (Basic Audit Log Patterns) version
  Options: `1.1.0`, `1.1.1`, `1.1.2`, `1.1.3`

- **`AUDITBOX_ES_AUTH`**
  Format: `<user>:<password>`
  Elasticsearch basic authentication credentials
  Implemented and tested for the `superadmin` role in Elasticsearch

- **`AUDITBOX_LOG_LEVEL`**
  Default: `off`
  Application logging level
  Options: `off`, `info`, `debug`, `error`

- **`AUDITBOX_DATA_RETENTION_DAYS`**
  Default: `30`
  Number of days to retain audit events before archiving (must be >= 1)

- **`AUDITBOX_SNAPSHOT_REPOSITORY_NAME`**
  Default: `default`
  Name of the Elasticsearch snapshot repository for backups

- **`AUDITBOX_VERIFY_BACKUP_REPOSITORY`**
  Default: `false`
  Enable backup repository verification at startup (`true` or `false`)

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
  AUDITBOX_DATA_RETENTION_DAYS: 90
  AUDITBOX_SNAPSHOT_REPOSITORY_NAME: default
  AUDITBOX_VERIFY_BACKUP_REPOSITORY: false
```

# Database

Database settings

## Primary

Primary database settings

### Database host<a href="#db.host" id="db.host"></a>

```yaml
BOX_DB_HOST: "postgres"
```

Database host address.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.host</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Recommended value</td><td><code>postgres</code></td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_DB_HOST</code></td></tr><tr><td>Deprecated environment variables</td><td><code>PGHOST</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Database port<a href="#db.port" id="db.port"></a>

```yaml
BOX_DB_PORT: 5432
```

Database port.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.port</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Recommended value</td><td><code>5432</code></td></tr><tr><td>Default value</td><td><code>5432</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_PORT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>PGPORT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Database name<a href="#db.database" id="db.database"></a>

```yaml
BOX_DB_DATABASE: "aidbox"
```

The database name. `postgres` name is not allowed.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.database</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Recommended value</td><td><code>aidbox</code></td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_DB_DATABASE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>PGDATABASE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Database user<a href="#db.user" id="db.user"></a>

```yaml
BOX_DB_USER: "aidbox"
```

The database username.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.user</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Recommended value</td><td><code>aidbox</code></td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_DB_USER</code></td></tr><tr><td>Deprecated environment variables</td><td><code>PGUSER</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Database password<a href="#db.password" id="db.password"></a>

```yaml
BOX_DB_PASSWORD: "<pg-password>"
```

A password of database role name.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.password</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Recommended value</td><td><code>&lt;pg-password&gt;</code></td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_DB_PASSWORD</code></td></tr><tr><td>Deprecated environment variables</td><td><code>PGPASSWORD</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Install PostgreSQL extensions at startup<a href="#db.install-pg-extensions" id="db.install-pg-extensions"></a>

```yaml
BOX_DB_INSTALL_PG_EXTENSIONS: true
```

Automatically installs PostgreSQL extensions (pgcrypto, unaccent, pg_trgm, fuzzystrmatch) during server startup.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.install-pg-extensions</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>true</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_INSTALL_PG_EXTENSIONS</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_INSTALL_PG_EXTENSIONS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Database extension schema<a href="#db.extension-schema" id="db.extension-schema"></a>

```yaml
BOX_DB_EXTENSION_SCHEMA: "<String>"
```

Schema for PostgreSQL extensions. Default is current schema.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.extension-schema</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_DB_EXTENSION_SCHEMA</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_EXTENSION_SCHEMA</code> , <br /><code>AIDBOX_DB_PARAM_CURRENT_SCHEMA</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### ViewDefinition materialization schema<a href="#db.view-definition-schema" id="db.view-definition-schema"></a>

```yaml
BOX_VIEW_DEFINITION_SCHEMA: "sof"
```

Schema for storing `ViewDefinition` materialization. Changing this setting does not affect already materialized views; it applies only to new ones. It is recommended to use a dedicated schema for `ViewDefinition` to avoid potential collisions.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.view-definition-schema</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>sof</code></td></tr><tr><td>Environment variable</td><td><code>BOX_VIEW_DEFINITION_SCHEMA</code></td></tr><tr><td>Available from</td><td><code>2508</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Pool connection timeout<a href="#db.pool.connection-timeout" id="db.pool.connection-timeout"></a>

```yaml
BOX_DB_POOL_CONNECTION_TIMEOUT: 30000
```

Maximum wait time (in milliseconds) for a database connection from the pool before timing out.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.pool.connection-timeout</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>30000</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_POOL_CONNECTION_TIMEOUT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_DB_POOL_CONNECTION__TIMEOUT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Pool connection init SQL<a href="#db.pool.connection-init-sql" id="db.pool.connection-init-sql"></a>

```yaml
BOX_DB_POOL_CONNECTION_INIT_SQL: "select 1"
```

Specifies a SQL statement that will be executed after every new connection creation before adding it to the pool.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.pool.connection-init-sql</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>select 1</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_POOL_CONNECTION_INIT_SQL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_DB_POOL_CONNECTION__INIT__SQL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Pool idle timeout<a href="#db.pool.idle-timeout" id="db.pool.idle-timeout"></a>

```yaml
BOX_DB_POOL_IDLE_TIMEOUT: 10000
```

Maximum timeout (in milliseconds) to close idle connection.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.pool.idle-timeout</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>10000</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_POOL_IDLE_TIMEOUT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_DB_POOL_IDLE__TIMEOUT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Pool minimum idle<a href="#db.pool.minimum-idle" id="db.pool.minimum-idle"></a>

```yaml
BOX_DB_POOL_MINIMUM_IDLE: 0
```

Minimum number of connections.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.pool.minimum-idle</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>0</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_POOL_MINIMUM_IDLE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_DB_POOL_MINIMUM__IDLE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Pool size<a href="#db.pool.maximum-pool-size" id="db.pool.maximum-pool-size"></a>

```yaml
BOX_DB_POOL_MAXIMUM_POOL_SIZE: 8
```

Maximum number of simultaneous database connections.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.pool.maximum-pool-size</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>8</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_POOL_MAXIMUM_POOL_SIZE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_DB_POOL_MAXIMUM__POOL__SIZE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Propagate DB health status to Aidbox<a href="#db.propagate-db-health-status-to-box" id="db.propagate-db-health-status-to-box"></a>

```yaml
BOX_PROPAGATE_DB_HEALTH_STATUS_TO_BOX: false
```

If enabled, the health status of the database will be reflected in the overall health status of Aidbox.
 If the database is unhealthy, Aidbox will also be considered unhealthy.

**NOTE**: Aidbox caches DB connections.
 That may cause inconsistencies when DB status is already `fail` but Aidbox instance still has valid connections.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.propagate-db-health-status-to-box</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_PROPAGATE_DB_HEALTH_STATUS_TO_BOX</code></td></tr><tr><td>Available from</td><td><code>2509</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

## Read-only replica

Read-only database replica settings

### Read-only replica enable<a href="#db.ro-replica.enabled" id="db.ro-replica.enabled"></a>

```yaml
BOX_DB_RO_REPLICA_ENABLED: false
```

Enable options to process requests to read-only db replica.
If enabled you should define: `db.ro-replica.database`, `db.ro-replica.host`,
`db.ro-replica.port`, `db.ro-replica.user`, and `db.ro-replica.password`.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.enabled</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_RO_REPLICA_ENABLED</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Database name<a href="#db.ro-replica.database" id="db.ro-replica.database"></a>

```yaml
BOX_DB_RO_REPLICA_DATABASE: "<String>"
```

The database name. `postgres` name is not allowed.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.database</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_DB_RO_REPLICA_DATABASE</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Database host<a href="#db.ro-replica.host" id="db.ro-replica.host"></a>

```yaml
BOX_DB_RO_REPLICA_HOST: "<String>"
```

AidboxDB host address.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.host</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_DB_RO_REPLICA_HOST</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Database port<a href="#db.ro-replica.port" id="db.ro-replica.port"></a>

```yaml
BOX_DB_RO_REPLICA_PORT: "<Int>"
```

The database port number.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.port</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_DB_RO_REPLICA_PORT</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Database user<a href="#db.ro-replica.user" id="db.ro-replica.user"></a>

```yaml
BOX_DB_RO_REPLICA_USER: "<String>"
```

The database username.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.user</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_DB_RO_REPLICA_USER</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Database password<a href="#db.ro-replica.password" id="db.ro-replica.password"></a>

```yaml
BOX_DB_RO_REPLICA_PASSWORD: "<String>"
```

The database password.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.password</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_DB_RO_REPLICA_PASSWORD</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Database extension schema<a href="#db.ro-replica.extension-schema" id="db.ro-replica.extension-schema"></a>

```yaml
AIDBOX_EXTENSION_SCHEMA: "<String>"
```

The database extension schema.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.extension-schema</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>AIDBOX_EXTENSION_SCHEMA</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_DB_PARAM_CURRENT_SCHEMA</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Maximum wait time<a href="#db.ro-replica.pool.connection-timeout" id="db.ro-replica.pool.connection-timeout"></a>

```yaml
BOX_DB_RO_REPLICA_POOL_CONNECTION_TIMEOUT: 30000
```

Maximum wait time (in milliseconds) for a database connection from the pool before timing out.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.pool.connection-timeout</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>30000</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_RO_REPLICA_POOL_CONNECTION_TIMEOUT</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Pool idle timeout<a href="#db.ro-replica.pool.idle-timeout" id="db.ro-replica.pool.idle-timeout"></a>

```yaml
BOX_DB_RO_REPLICA_POOL_IDLE_TIMEOUT: 10000
```

Maximum timeout (in milliseconds) to close idle connection.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.pool.idle-timeout</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>10000</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_RO_REPLICA_POOL_IDLE_TIMEOUT</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Pool minimum idle<a href="#db.ro-replica.pool.minimum-idle" id="db.ro-replica.pool.minimum-idle"></a>

```yaml
BOX_DB_RO_REPLICA_POOL_MINIMUM_IDLE: 0
```

Minimum number of connections.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.pool.minimum-idle</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>0</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_RO_REPLICA_POOL_MINIMUM_IDLE</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Pool size<a href="#db.ro-replica.pool.maximum-pool-size" id="db.ro-replica.pool.maximum-pool-size"></a>

```yaml
BOX_DB_RO_REPLICA_POOL_MAXIMUM_POOL_SIZE: 8
```

Maximum number of simultaneous database connections.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.pool.maximum-pool-size</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>8</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_RO_REPLICA_POOL_MAXIMUM_POOL_SIZE</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Pool connection init SQL<a href="#db.ro-replica.pool.connection-init-sql" id="db.ro-replica.pool.connection-init-sql"></a>

```yaml
BOX_DB_RO_REPLICA_POOL_CONNECTION_INIT_SQL: "select 1"
```

The pool connection initialization SQL statement.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.pool.connection-init-sql</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>select 1</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_RO_REPLICA_POOL_CONNECTION_INIT_SQL</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

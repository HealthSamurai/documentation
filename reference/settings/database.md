## Database

Database settings

### Database host<a href="#db.host" id="db.host"></a>

Database host address.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.host</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_DB_HOST</code> , <br /><code>PGHOST</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Can be setted by</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Database port<a href="#db.port" id="db.port"></a>

Database port.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.port</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_DB_PORT</code> , <br /><code>PGPORT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Can be setted by</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Database name<a href="#db.database" id="db.database"></a>

The database name. `postgres` name is not allowed.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.database</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_DB_DATABASE</code> , <br /><code>PGDATABASE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Can be setted by</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Database user<a href="#db.user" id="db.user"></a>

The database username.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.user</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_DB_USER</code> , <br /><code>PGUSER</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Can be setted by</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Database password<a href="#db.password" id="db.password"></a>

A password of database role name.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.password</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_DB_PASSWORD</code> , <br /><code>PGPASSWORD</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — can be set only via environment variable</td></tr><tr><td>Can be setted by</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Install PostgreSQL extensions at startup<a href="#db.install-pg-extensions" id="db.install-pg-extensions"></a>

Automatically installs PostgreSQL extensions (pgcrypto, unaccent, pg_trgm, fuzzystrmatch) during server startup.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.install-pg-extensions</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>true</code></td></tr><tr><td>Environment variables</td><td><code>BOX_DB_INSTALL_PG_EXTENSIONS</code> , <br /><code>AIDBOX_INSTALL_PG_EXTENSIONS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Can be setted by</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Database extension schema<a href="#db.extension-schema" id="db.extension-schema"></a>

Schema for PostgreSQL extensions. Default is current schema.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.extension-schema</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_DB_EXTENSION_SCHEMA</code> , <br /><code>AIDBOX_EXTENSION_SCHEMA</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Can be setted by</td><td>User interface<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Pool connection timeout<a href="#db.pool.connection-timeout" id="db.pool.connection-timeout"></a>

Maximum wait time (in milliseconds) for a database connection from the pool before timing out.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.pool.connection-timeout</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>30000</code></td></tr><tr><td>Environment variables</td><td><code>BOX_DB_POOL_CONNECTION_TIMEOUT</code> , <br /><code>BOX_DB_POOL_CONNECTION__TIMEOUT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Can be setted by</td><td>User interface<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Pool connection init SQL<a href="#db.pool.connection-init-sql" id="db.pool.connection-init-sql"></a>

Specifies a SQL statement that will be executed after every new connection creation before adding it to the pool.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.pool.connection-init-sql</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>select 1</code></td></tr><tr><td>Environment variables</td><td><code>BOX_DB_POOL_CONNECTION_INIT_SQL</code> , <br /><code>BOX_DB_POOL_CONNECTION__INIT__SQL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Can be setted by</td><td>User interface<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Pool idle timeout<a href="#db.pool.idle-timeout" id="db.pool.idle-timeout"></a>

Maximum timeout (in milliseconds) to close idle connection.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.pool.idle-timeout</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>10000</code></td></tr><tr><td>Environment variables</td><td><code>BOX_DB_POOL_IDLE_TIMEOUT</code> , <br /><code>BOX_DB_POOL_IDLE__TIMEOUT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Can be setted by</td><td>User interface<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Pool minimum idle<a href="#db.pool.minimum-idle" id="db.pool.minimum-idle"></a>

Minimum number of connections.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.pool.minimum-idle</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>0</code></td></tr><tr><td>Environment variables</td><td><code>BOX_DB_POOL_MINIMUM_IDLE</code> , <br /><code>BOX_DB_POOL_MINIMUM__IDLE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Can be setted by</td><td>User interface<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Pool size<a href="#db.pool.maximum-pool-size" id="db.pool.maximum-pool-size"></a>

Maximum number of simultaneous database connections.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.pool.maximum-pool-size</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>8</code></td></tr><tr><td>Environment variables</td><td><code>BOX_DB_POOL_MAXIMUM_POOL_SIZE</code> , <br /><code>BOX_DB_POOL_MAXIMUM__POOL__SIZE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Can be setted by</td><td>User interface<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

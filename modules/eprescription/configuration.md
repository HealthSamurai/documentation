# Configuration

### Environment Variables

Here is a full list of environment variables used by the module:

* `E_PRESCRIPTION_PORT` – HTTP port of the module
* `LOG_LEVEL` – Level of logs severity. Available values: trace, debug, info, warn, error, fatal, report, default is - **info.**
* `LOG_FORMAT` – Output format for logs. Available values: json, text, default is - **text**.
* `DISABLE_SCHEDULER` - boolean variable for disabling internal task scheduler, by default scheduler is on
* `JAVA_OPTS` – allows to pass additional JVM options



* `AIDBOX_BASE_URL` – Aidbox base URL (without trailing slash)
* `AIDBOX_CREDENTIALS` – Aidbox Client secret for module access; **base64-encoded** client name and secret from App resource definition: `base64("client_name:client_secret")`
* `TENANT_ORGANIZATION_ID` - top level organization ID for multi-tenant setup



* `QUEUE_DB_SERVER` - Queue database server address
* `QUEUE_DB_PORT` - Queue database server port
* `QUEUE_DB_NAME` - Queue database name
* `QUEUE_DB_SCHEMA` - Queue database schema
* `QUEUE_DB_USERNAME` - Queue database username
* `QUEUE_DB_PASSWORD` - Queue database password



* `SURESCRIPTS_ENV` – Surescripts environment (`staging` or `production`)
* `SURESCRIPTS_PORTAL_NAME` – Surescripts provided portal name
* `SURESCRIPTS_PORTAL_ID` – Surescripts provided portal ID
* `SURESCRIPTS_ACCOUNT_ID` – Surescripts provided account ID
* `SURESCRIPTS_SYSTEM_ID` – Surescripts provided system ID (Directory sender ID)
* `SURESCRIPTS_CA` or `SURESCRIPTS_CA_PATH` – mTLS Surescripts Certificate Authority (certificate or path)
* `SURESCRIPTS_CERT` or `SURESCRIPTS_CERT_PATH` – mTLS Surescripts Certificate (certificate or path)
* `SURESCRIPTS_PRIVATE_KEY` or `SURESCRIPTS_PRIVATE_KEY_PATH` – mTLS Surescripts Private Key (key or path)



* `FDB_KEY` – FDB API key to use medication database

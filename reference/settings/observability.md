## Observability

Observability settings


## Observability Logs

Logs settings

### Disable health logs<a href="#observability.disable-health-logs" id="observability.disable-health-logs"></a>

Disable `/health` endpoint requests logging. Default value is `false`

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.disable-health-logs</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_DISABLE_HEALTH_LOGS</code> , <br /><code>BOX_LOGGING_DISABLE__HEALTH__LOGS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>true</code> — can be changed at runtime</td></tr></tbody></table>

### Log file path<a href="#observability.log-file.path" id="observability.log-file.path"></a>

If provided, enables mode to pipe logs as JSON into the file by specified path.

If ElasticSearch URL is provided then the file is used as a fallback in case if ElasticSearch is not available.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.log-file.path</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_LOG_FILE_PATH</code> , <br /><code>AIDBOX_LOGS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Logs max lines<a href="#observability.log-file.max-lines" id="observability.log-file.max-lines"></a>

Sets the limit of log records to push into the file
When the limit is reached, the current log file is renamed with `.old` postfix and a new log file is created

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.log-file.max-lines</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>10000</code></td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_LOG_FILE_MAX_LINES</code> , <br /><code>AIDBOX_LOGS_MAX_LINES</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Logging SQL min duration<a href="#observability.sql.min-duration" id="observability.sql.min-duration"></a>

Threshold for logging only long queries. Analogous from PostgreSQL.
Log only requests whose execution time exceeds the specified number of milliseconds.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.sql.min-duration</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>-1</code></td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_SQL_MIN_DURATION</code> , <br /><code>BOX_LOGGING_SQL_MIN__DURATION</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Logging SQL max length<a href="#observability.sql.max-length" id="observability.sql.max-length"></a>

Max length of a query to be logged.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.sql.max-length</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>500</code></td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_SQL_MAX_LENGTH</code> , <br /><code>BOX_LOGGING_SQL_MAX__LENGTH</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

## Observability Metrics

Metrics settings

### Metrics server port<a href="#observability.metrics.server-port" id="observability.metrics.server-port"></a>

Port on which Aidbox will expose metrics.
To disable metrics server, leave this setting empty

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.metrics.server-port</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_METRICS_PORT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Enable Postgres metrics<a href="#observability.metrics.enable-postgres-metrics" id="observability.metrics.enable-postgres-metrics"></a>

Controls whether to provide metrics [related to PostgreSQL](https://docs.aidbox.app/modules-1/observability/metrics/monitoring/aidbox-metrics-server#postgres)

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.metrics.enable-postgres-metrics</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>true</code></td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_METRICS_ENABLE_POSTGRES_METRICS</code> , <br /><code>BOX_METRICS_POSTGRES_ON</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

## Observability Stdout

Stdout settings

### Stdout log level<a href="#observability.stdout.log-level" id="observability.stdout.log-level"></a>

Controls stdout with specified logs level.
Possible values: off, fatal, error, warn, info, debug, trace, all, true 
By setting one of these levels you would also get all the levels to the left. 
e.g. if you set log level to `warn` you would also get log events with `fatal` 
and `error` levels (off is excluded).

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.stdout.log-level</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code></code> — <br /><code></code> — <br /><code></code> — <br /><code></code> — <br /><code></code> — <br /><code></code> — <br /><code></code> — <br /><code></code> — <br /><code></code> — </td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_STDOUT_LOG_LEVEL</code> , <br /><code>AIDBOX_STDOUT_JSON</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Pretty print log level<a href="#observability.stdout.pretty-log-level" id="observability.stdout.pretty-log-level"></a>

Controls pretty print of logs to stdout with specified level.
Possible values: off, fatal, error, warn, info, debug, trace, all, true 
By setting one of these levels you would also get all the levels to the left. 
e.g. if you set log level to `warn` you would also get log events with `fatal` 
and `error` levels (off is excluded).

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.stdout.pretty-log-level</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code></code> — <br /><code></code> — <br /><code></code> — <br /><code></code> — <br /><code></code> — <br /><code></code> — <br /><code></code> — <br /><code></code> — <br /><code></code> — </td></tr><tr><td>Default value</td><td><code>warn</code></td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_STDOUT_PRETTY_LOG_LEVEL</code> , <br /><code>AIDBOX_STDOUT_PRETTY</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Google log level<a href="#observability.stdout.google-log-level" id="observability.stdout.google-log-level"></a>

Produces in Google Logging format with specified log level.
Possible values: off, fatal, error, warn, info, debug, trace, all, true 
By setting one of these levels you would also get all the levels to the left. 
e.g. if you set log level to `warn` you would also get log events with `fatal` 
and `error` levels (off is excluded).

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.stdout.google-log-level</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code></code> — <br /><code></code> — <br /><code></code> — <br /><code></code> — <br /><code></code> — <br /><code></code> — <br /><code></code> — <br /><code></code> — <br /><code></code> — </td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_STDOUT_GOOGLE_LOG_LEVEL</code> , <br /><code>AIDBOX_STDOUT_GOOGLE_JSON</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

## Observability Loki

Grafana Loki settings

### Loki URL<a href="#observability.loki-url" id="observability.loki-url"></a>

Loki URL to enable Aidbox logs uploading into Loki

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.loki-url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_LOKI_URL</code> , <br /><code>AIDBOX_LK_URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Loki batch size<a href="#observability.loki.batch-size" id="observability.loki.batch-size"></a>

Loki batch size for log uploading.
Aidbox uploads logs when either at least specific `observability.loki.batch-size` collected
or time passed from previous log uploading exceeds `observability.loki.batch-timeout`.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.loki.batch-size</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_LOKI_BATCH_SIZE</code> , <br /><code>AIDBOX_LK_BATCH_SIZE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Loki batch timeout<a href="#observability.loki.batch-timeout" id="observability.loki.batch-timeout"></a>

How long to wait before uploading
Aidbox uploads logs when either at least `observability.loki.batch-size` entries collected
or time passed from previous log uploading exceeds `observability.loki.batch-timeout`.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.loki.batch-timeout</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_LOKI_BATCH_TIMEOUT</code> , <br /><code>AIDBOX_LK_BATCH_TIMEOUT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Loki stream<a href="#observability.loki.stream" id="observability.loki.stream"></a>

Stream refers to the labels or metadata associated with a log stream
Is defined by a unique set of labels, which serve as the stream key.
For example: `{"box": "aidbox"}`

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.loki.stream</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_LOKI_STREAM</code> , <br /><code>AIDBOX_LK_STREAM</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

## Observability Datadog

Datadog settings

### Datadog API Key<a href="#observability.datadog.api-key" id="observability.datadog.api-key"></a>

Datadog API Key.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.datadog.api-key</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_DATADOG_API_KEY</code> , <br /><code>BOX_DD_API_KEY</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — can be set only via environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Datadog regional site<a href="#observability.datadog.site" id="observability.datadog.site"></a>

The regional site for a Datadog customer.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.datadog.site</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code></code> — <br /><code></code> — <br /><code></code> — <br /><code></code> — <br /><code></code> — <br /><code></code> — </td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_DATADOG_SITE</code> , <br /><code>BOX_DD_SITE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Datadog log tags<a href="#observability.datadog.tags" id="observability.datadog.tags"></a>

Tags associated with your logs.
Convenient for transferring the name of the environment.
For example `env:staging`.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.datadog.tags</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_DATADOG_TAGS</code> , <br /><code>BOX_DD_TAGS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Datadog log file path<a href="#observability.datadog.logs" id="observability.datadog.logs"></a>

Fallback file to write logs in if uploading to Datadog fails

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.datadog.logs</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_DATADOG_LOGS</code> , <br /><code>AIDBOX_DD_LOGS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Datadog log batch size<a href="#observability.datadog.batch-size" id="observability.datadog.batch-size"></a>

How many log entries to collect before uploading.
Aidbox uploads logs when either at least specific `observability.datadog.batch-size` collected
or time passed from previous log uploading exceeds `observability.datadog.batch-timeout`.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.datadog.batch-size</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_DATADOG_BATCH_SIZE</code> , <br /><code>BOX_DD_BATCH_SIZE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Datadog log batch timeout<a href="#observability.datadog.batch-timeout" id="observability.datadog.batch-timeout"></a>

How long to wait before uploading
Aidbox uploads logs when either at least specific `observability.datadog.batch-size` collected
or time passed from previous log uploading exceeds `observability.datadog.batch-timeout`.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.datadog.batch-timeout</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_DATADOG_BATCH_TIMEOUT</code> , <br /><code>BOX_DD_BATCH_TIMEOUT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

## Observability Elastic

Elastic settings

### Elastic Search auth<a href="#observability.elastic.search-auth" id="observability.elastic.search-auth"></a>

Format: `<user>:<password>`
Basic auth credentials for ElasticSearch. API key is not supported.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.elastic.search-auth</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_ELASTIC_SEARCH_AUTH</code> , <br /><code>AIDBOX_ES_AUTH</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — can be set only via environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Elastic Search URL<a href="#observability.elastic.search-url" id="observability.elastic.search-url"></a>

If provided, enables mode to push logs to ElasticSearch

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.elastic.search-url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_ELASTIC_SEARCH_URL</code> , <br /><code>AIDBOX_ES_URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Elastic Search batch size<a href="#observability.elastic.batch-size" id="observability.elastic.batch-size"></a>

Log batch size used to optimize log shipping performance. The default value is 200

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.elastic.batch-size</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_ELASTIC_BATCH_SIZE</code> , <br /><code>AIDBOX_ES_BATCH_SIZE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Elastic Search batch timeout<a href="#observability.elastic.batch-timeout" id="observability.elastic.batch-timeout"></a>

Timeout to post a batch to ElasticSearch. If there is not enough records to reach full batch size

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.elastic.batch-timeout</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>60000</code></td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_ELASTIC_BATCH_TIMEOUT</code> , <br /><code>AIDBOX_ES_BATCH_TIMEOUT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Elastic Search log format<a href="#observability.elastic.index-pattern" id="observability.elastic.index-pattern"></a>

Custom index format string. The default value is 'aidbox-logs'-yyyy-MM-dd.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.elastic.index-pattern</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>&apos;aidbox-logs&apos;-yyyy-MM-dd</code></td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_ELASTIC_INDEX_PATTERN</code> , <br /><code>AIDBOX_ES_INDEX_PAT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

## Observability Grafana

Grafana settings

### Grafana URL for metrics dashboards<a href="#observability.grafana.url" id="observability.grafana.url"></a>

Grafana URL to update the metrics dashboards.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.grafana.url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_GRAFANA_URL</code> , <br /><code>BOX_METRICS_GRAFANA_URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Grafana user for metrics dashboards<a href="#observability.grafana.user" id="observability.grafana.user"></a>

Grafana user to update the metrics dashboards.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.grafana.user</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_GRAFANA_USER</code> , <br /><code>BOX_METRICS_GRAFANA_USER</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Grafana password for metrics dashboards<a href="#observability.grafana.password" id="observability.grafana.password"></a>

Grafana password to update the metrics dashboards.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.grafana.password</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_GRAFANA_PASSWORD</code> , <br /><code>BOX_METRICS_GRAFANA_PASSWORD</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — can be set only via environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

## Observability OTEL

OpenTelemetry settings

### OTEL auth token<a href="#observability.otel.default-auth-token" id="observability.otel.default-auth-token"></a>

The default bearer token for authentication.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.otel.default-auth-token</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_OTEL_DEFAULT_AUTH_TOKEN</code> , <br /><code>BOX_METRICS_OTEL_DEFAULT_AUTH_TOKEN</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — can be set only via environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### OTEL metrics interval<a href="#observability.otel.default-interval" id="observability.otel.default-interval"></a>

The default time interval (in seconds) for sending metrics to OTEL.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.otel.default-interval</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>5</code></td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_OTEL_DEFAULT_INTERVAL</code> , <br /><code>BOX_METRICS_OTEL_DEFAULT_INTERVAL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### OTEL request headers<a href="#observability.otel.default-request-headers" id="observability.otel.default-request-headers"></a>

The default headers for OTEL requests, formatted as HeaderName:HeaderValue.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.otel.default-request-headers</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_OBSERVABILITY_OTEL_DEFAULT_REQUEST_HEADERS</code> , <br /><code>BOX_METRICS_OTEL_DEFAULT_REQUEST_HEADERS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

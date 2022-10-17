# Aidbox Metrics Server

### Setup and environment variable

Define`BOX_METRICS_PORT`environment variable with monitoring server port number.

### Start metrics server

Aidbox starts monitoring server on startup automatically.

{% hint style="info" %}
To check the monitoring server works make `GET <AIDBOX_BASE_URL>:<BOX_METRICS_PORT>` request. The output should be a string "aidbox metrics".
{% endhint %}

### Metrics server endpoints

There are three types of metrics Aidbox collects and exposes. All endpoints are available on a separate port, e.g. `GET <AIDBOX_BASE_URL>:<BOX_METRICS_PORT>/metrics`.

| Endpoint               | Update frequency |
| ---------------------- | ---------------- |
| `GET /metrics`         | continuous       |
| `GET /metrics/minutes` | every minute     |
| `GET /metrics/hours`   | every hour       |

{% hint style="info" %}
The `/metrics/hours` response can take some time since it collects a lot of information from the database. Make sure your metrics scraper timeout is sufficient.
{% endhint %}

#### Prometheus example scrapers configuration

```yaml
global:
  # omitted global configuration values
  external_labels:
    monitor: 'aidbox'
scrape_configs:
  # omitted default scrappers configuration
  
  - job_name: aidbox
    scrape_interval: 5s
    metrics_path: /metrics
    static_configs:
      - targets: [ 'aidbox.example.com:9999' ]  # should be <AIDBOX_BASE_URL>:<BOX_METRICS_PORT

  - job_name: aidbox-minutes
    scrape_interval: 30s
    metrics_path: /metrics/minutes
    static_configs:
      - targets: [ 'aidbox.example.com:9999' ]  # should be <AIDBOX_BASE_URL>:<BOX_METRICS_PORT

  - job_name: aidbox-hours
    scrape_interval: 1m
    scrape_timeout: 30s                         # increased timeout
    metrics_path: /metrics/hours
    static_configs:
      - targets: [ 'aidbox.example.org:com' ]   # should be <AIDBOX_BASE_URL>:<BOX_METRICS_PORT
```

### Collected metrics

#### HTTP

| Metric                                        | Update frequency | Description                                           |
| --------------------------------------------- | ---------------- | ----------------------------------------------------- |
| `aidbox_http_request_duration_seconds_bucket` | continuous       | request duration  as cumulative counters for buckets  |
| `aidbox_http_request_duration_seconds_count`  | continuous       | request duration events count                         |
| `aidbox_http_request_duration_seconds_sum`    | continuous       | sum of request duration events value                  |
| `aidbox_http_request_wait_seconds_bucket`     | continuous       | queue waiting time as cumulative counters for buckets |
| `aidbox_http_request_wait_seconds_count`      | continuous       | queue wait time events count                          |
| `aidbox_http_request_wait_seconds_sum`        | continuous       | sum of queue wait time events value                   |

#### Postgres

| Metric                                     | Update frequency | Description                          |
| ------------------------------------------ | ---------------- | ------------------------------------ |
| `pg_requests_total`                        | continuous       | number of executed selects requests  |
| `pg_inserts_total`                         | continuous       | number of executed insert statements |
| `pg_updates_total`                         | continuous       | number of executed update statements |
| `pg_deletes_total`                         | continuous       | number of executed delete statements |
| `pg_blks_hit`                              | continuous       | number of shared block cache hits    |
| `pg_blks_read`                             | continuous       | number of shared blocks read         |
| `pg_tup_fetched`                           | continuous       | fetched tuples number                |
| `pg_tup_returned`                          | continuous       | returned tuples number               |
| `pg_errors_total`                          | continuous       | number of errors                     |
| `pg_activity_count`                        | continuous       | number of PG workers                 |
| `pg_idx_scan`                              | every minute     | number of index scans                |
| `pg_seq_scan`                              | every minute     | number of sequential scans           |
| `pg_stat_statements_total_calls`           | every minute     | number of times executed             |
| `pg_stat_statements_stddev_execution_time` | every minute     | statement execution time             |
| `pg_stat_statements_mean_execution_time`   | every minute     | mean statement execution time        |
| `pg_table_size`                            | every hour       | table size                           |
| `pg_database_size`                         | every hour       | database size                        |
| `pg_activity_max`                          | every hour       | maximum number of connections        |

#### Hikari (Postgres connection pool)

| Metric                                  | Update frequency | Description                                             |
| --------------------------------------- | ---------------- | ------------------------------------------------------- |
| `hikari_active_count`                   | continuous       | number of active connections                            |
| `hikari_idle_count`                     | continuous       | number of idle connections                              |
| `hikari_acquire_created_seconds_bucket` | continuous       | time taken to create an actual physical connection      |
| `hikari_acquire_created_seconds_count`  | continuous       | number of created physical connections                  |
| `hikari_acquire_created_seconds_sum`    | continuous       | total amount of time to create all physical connections |
| `hikari_acquired_total`                 | continuous       | number of obtained connections                          |
| `hikari_acquire_wait_seconds_bucket`    | continuous       | time taken to obtain a connection                       |
| `hikari_acquire_wait_seconds_sum`       | continuous       | total amount of time to obtain all connections          |
| `hikari_acquire_used_seconds_bucket`    | continuous       | time consumed by a connection                           |
| `hikari_max_size`                       | every hour       | maximum number of connections                           |

#### JVM

| Metric                          | Update frequency | Description                                                        |
| ------------------------------- | ---------------- | ------------------------------------------------------------------ |
| `jvm_gc_time`                   | continuous       | garbage collector execution time                                   |
| `jvm_gc_count`                  | continuous       | garbage collector count of launch                                  |
| `jvm_heap_memory`               | continuous       | heap memory usage                                                  |
| `jvm_non_heap_memory`           | continuous       | non-heap memory usage                                              |
| `jvm_thread_count`              | continuous       | number of live threads including both daemon and non-daemon thread |
| `jvm_thread_peak_count`         | continuous       | peak live thread count                                             |
| `jvm_thread_daemon_count`       | continuous       | number of daemon thread                                            |
| `jvm_available_processors_size` | every hour       | number of processors available to the JVM                          |
| `jvm_max_memory_size`           | every hour       | maximum amount of memory that JVM will attempt to use              |
| `jvm_total_memory_size`         | every hour       | total amount of memory in JVM                                      |

#### Disable PostgreSQL metrics

If you have a different pg exporter you can disable Aidbox PostgreSQL metrics for avoiding metrics duplication.

In this case, you should set `BOX_METRICS_POSTGRES_ON` to `false` value

---
description: This guide explains how to set up and use Aidbox Metrics Server
---

# Use Aidbox Metrics Server

## Setup and environment variable

Define`BOX_METRICS_PORT`environment variable with monitoring server port number.

## Start metrics server

Aidbox exposes metrics on the endpoint `<AIDBOX_BASE_URL>/<BOX_METRICS_PORT>`.

{% hint style="info" %}
To check if the monitoring server works make the `GET <AIDBOX_BASE_URL>:<BOX_METRICS_PORT>` request. The output should be a string "aidbox metrics".
{% endhint %}

## Metrics server endpoints

There are three types of metrics Aidbox collects and exposes. All endpoints are available on a separate port, e.g. `GET <AIDBOX_BASE_URL>:<BOX_METRICS_PORT>/metrics`.

| Endpoint               | Update frequency |
| ---------------------- | ---------------- |
| `GET /metrics`         | continuous       |
| `GET /metrics/minutes` | every minute     |
| `GET /metrics/hours`   | every hour       |

{% hint style="info" %}
The `/metrics/hours` response can take some time since it collects much information from the database. Make sure your metrics scraper timeout is sufficient.
{% endhint %}

### Prometheus example scrapers configuration

```yaml
global:
  # omitted global configuration values
  external_labels:
    monitor: 'aidbox'
scrape_configs:
  # omitted default scrappers configuration
  
  - job_name: aidbox
    honor_labels: true
    scrape_interval: 10s
    metrics_path: /metrics
    static_configs:
      - targets: [ 'aidbox.example.com:9999' ]  # should be <AIDBOX_BASE_URL>:<BOX_METRICS_PORT

  - job_name: aidbox-minutes
   honor_labels: true
    scrape_interval: 1m
    metrics_path: /metrics/minutes
    static_configs:
      - targets: [ 'aidbox.example.com:9999' ]  # should be <AIDBOX_BASE_URL>:<BOX_METRICS_PORT

  - job_name: aidbox-hours
    honor_labels: true
    scrape_interval: 10m
    scrape_timeout: 30s                         # increased timeout
    metrics_path: /metrics/hours
    static_configs:
      - targets: [ 'aidbox.example.com:9999' ]   # should be <AIDBOX_BASE_URL>:<BOX_METRICS_PORT
```

## Collected metrics

### HTTP

<table><thead><tr><th width="285.3333333333333">Metric</th><th>Update frequency</th><th>Description</th></tr></thead><tbody><tr><td><code>aidbox_http_request_duration_seconds_bucket</code></td><td>continuous</td><td>request duration  as cumulative counters for buckets</td></tr><tr><td><code>aidbox_http_request_duration_seconds_count</code></td><td>continuous</td><td>request duration events count</td></tr><tr><td><code>aidbox_http_request_duration_seconds_sum</code></td><td>continuous</td><td>sum of request duration events value</td></tr><tr><td><code>aidbox_http_request_wait_seconds_bucket</code></td><td>continuous</td><td>queue waiting time as cumulative counters for buckets</td></tr><tr><td><code>aidbox_http_request_wait_seconds_count</code></td><td>continuous</td><td>queue waiting time events count</td></tr><tr><td><code>aidbox_http_request_wait_seconds_sum</code></td><td>continuous</td><td>sum of queue waiting time events value</td></tr></tbody></table>

### Postgres

<table><thead><tr><th width="190.33333333333331">Metric</th><th>Update frequency</th><th>Description</th></tr></thead><tbody><tr><td><code>pg_requests_total</code></td><td>continuous</td><td>number of executed selects requests</td></tr><tr><td><code>pg_inserts_total</code></td><td>continuous</td><td>number of executed insert statements</td></tr><tr><td><code>pg_updates_total</code></td><td>continuous</td><td>number of executed update statements</td></tr><tr><td><code>pg_deletes_total</code></td><td>continuous</td><td>number of executed delete statements</td></tr><tr><td><code>pg_blks_hit</code></td><td>continuous</td><td>number of shared block cache hits</td></tr><tr><td><code>pg_blks_read</code></td><td>continuous</td><td>number of shared blocks read</td></tr><tr><td><code>pg_tup_fetched</code></td><td>continuous</td><td>fetched tuples number</td></tr><tr><td><code>pg_tup_returned</code></td><td>continuous</td><td>returned tuples number</td></tr><tr><td><code>pg_errors_total</code></td><td>continuous</td><td>number of errors</td></tr><tr><td><code>pg_activity_count</code></td><td>continuous</td><td>number of PG workers</td></tr><tr><td><code>pg_idx_scan</code></td><td>every minute</td><td>number of index scans</td></tr><tr><td><code>pg_seq_scan</code></td><td>every minute</td><td>number of sequential scans</td></tr><tr><td><code>pg_stat_statements_total_calls</code></td><td>every minute</td><td>number of times executed</td></tr><tr><td><code>pg_stat_statements_stddev_execution_time</code></td><td>every minute</td><td>statement execution time</td></tr><tr><td><code>pg_stat_statements_mean_execution_time</code></td><td>every minute</td><td>mean statement execution time</td></tr><tr><td><code>pg_table_size</code></td><td>every hour</td><td>table size</td></tr><tr><td><code>pg_database_size</code></td><td>every hour</td><td>database size</td></tr><tr><td><code>pg_activity_max</code></td><td>every hour</td><td>maximum number of connections</td></tr></tbody></table>

### Hikari (Postgres connection pool)

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

### JVM

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

### Disable PostgreSQL metrics

If you have a different pg exporter you can disable Aidbox PostgreSQL metrics for avoiding metrics duplication.

In this case, you should set `BOX_METRICS_POSTGRES_ON` to `false` value

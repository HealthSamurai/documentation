---
description: Basic Aidbox metrics
---

# Monitoring

Aidbox starts an additional monitoring server on startup.

### Setup and environment variable

Define`BOX_METRIC_PORT`environment variable with the monitoring server port number.

### Metric server endpoints

There are three types of metrics Aidbox collects and exposes. All endpoints are available on a separate port, e.g. `GET <AIDBOX_BASE_URL>:<BOX_METRICS_PORT>/metrics`.

| Endpoint               | Update frequency |
| ---------------------- | ---------------- |
| `GET /metrics`         | continuous       |
| `GET /metrics/minutes` | every minute     |
| `GET /metrics/hours`   | every minute     |

{% hint style="info" %}
The `/metrics/hours` response can take some time since it collects a lot of information from the database. Make sure your metrics scraper timeout is sufficient.
{% endhint %}

### Collected metrics

#### Postgres

* `pg_requests_total` — continuous\
  selects requests number
* `pg_inserts_total` — continuous\
  inserts number
* `pg_updates_total` — continuous\
  updates number
* `pg_deletes_total` — continuous\
  deletes number
* `pg_deletes_total` — continuous\
  deletes number
* `pg_blks_hit` — continuous\
  number of shared block cache hits
* `pg_blks_read` — continuous\
  number of shared blocks read
* `pg_tup_fetched` — continuous\
  fetched tuples number
* `pg_tup_returned` — continuous\
  returned tuples number
* `pg_errors_total` — continuous\
  number of errors
* `pg_activity_count` — continuous\
  number of workers by state

---
description: Basic Aidbox metrics
---

# Monitoring

Aidbox starts an additional monitoring server on startup.

### Setup and environment variable

Define`BOX_METRIC_PORT`environment variable with the monitoring server port number.

### Metric server endpoints

There are three types of metrics Aidbox collects and exposes. All endpoints are available on a separate port, e.g. `GET <AIDBOX_BASE_URL>:<BOX_METRICS_PORT>/metrics`.

| Update frequency | Endpoint               |
| ---------------- | ---------------------- |
| continuously     | `GET /metrics`         |
| every minute     | `GET /metrics/minutes` |
| every hour       | `GET /metrics/hours`   |

{% hint style="info" %}
The `/metrics/hours` response  can take some time since it collects a lot of information from the database. Make sure your metrics scraper timeout is sufficient.
{% endhint %}

### Collected metrics

#### Postgres

* `pg_requests_total` — continuously\
  selects requests number
* `pg_inserts_total` — continuously\
  inserts number
* `pg_updates_total` — continuously\
  updates number
* `pg_deletes_total` — continuously\
  deletes number
* `pg_deletes_total` — continuously\
  deletes number
* `pg_blks_hit` — continuously\
  number of shared block cache hits
* `pg_blks_read` — continuously\
  number of shared blocks read
* `pg_tup_fetched` — continuously\
  fetched tuples number
* `pg_tup_returned` — continuously\
  returned tuples number
* `pg_errors_total` — continuously\
  number of errors
* `pg_activity_count` — gauge\
  number of workers by state

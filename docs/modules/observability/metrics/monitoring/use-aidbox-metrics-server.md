---
description: This guide explains how to set up and use Aidbox Metrics Server
---

# Use Aidbox Metrics Server

## Setup and environment variable

Define`BOX_METRICS_PORT`environment variable with monitoring server port number.

## Start metrics server

Aidbox exposes metrics on the endpoint `<AIDBOX_BASE_URL>:<BOX_METRICS_PORT>`.

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


### Disable PostgreSQL metrics

If you have a different pg exporter you can disable Aidbox PostgreSQL metrics for avoiding metrics duplication.

In this case, you should set `BOX_METRICS_POSTGRES_ON` to `false` value

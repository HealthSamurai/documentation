---
description: Export Aidbox metrics using OpenTelemetry or Prometheus for monitoring, alerting, and performance analysis.
---

# Metrics

## Export metrics with OTEL spec

Aidbox supports the [OpenTelemetry protocol](https://opentelemetry.io/) and exports metrics in Protobuf format to any consumer that supports this specification.

{% content-ref url="monitoring/how-to-export-metrics-to-the-otel-collector.md" %}
[how-to-export-metrics-to-the-otel-collector.md](monitoring/how-to-export-metrics-to-the-otel-collector.md)
{% endcontent-ref %}

## Export metrics to Prometheus

Aidbox exposes Prometheus-format metrics on a dedicated metrics server (see `BOX_METRICS_PORT`). Available endpoints:

| Endpoint | Update frequency |
|----------|------------------|
| `GET /metrics` | Continuous |
| `GET /metrics/minutes` | Every minute (e.g. pg_idx_scan, pg_stat_statements) |
| `GET /metrics/hours` | Every hour (e.g. pg_table_size, jvm_max_memory_size). Response may take longer; set sufficient scraper timeout. |

See [Use Aidbox Metrics Server](monitoring/use-aidbox-metrics-server.md) for setup and Prometheus scrape examples.

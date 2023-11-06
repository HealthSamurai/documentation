---
description: Aidbox metrics export types
---

# Metrics

## Export metrics with OTEL spec

Aidbox supports the [OpenTelemetry protocol](https://opentelemetry.io/) and exports metrics in Protobuf format to any consumer that supports this specification.

{% content-ref url="how-to-export-metrics-to-the-otel-collector.md" %}
[how-to-export-metrics-to-the-otel-collector.md](how-to-export-metrics-to-the-otel-collector.md)
{% endcontent-ref %}

## Export metrics to Prometheus

To export metrics to Prometheus, set up Aidbox Metrics server.

{% content-ref url="monitoring/aidbox-metrics-server.md" %}
[aidbox-metrics-server.md](monitoring/aidbox-metrics-server.md)
{% endcontent-ref %}

{% content-ref url="monitoring/grafana-integration.md" %}
[grafana-integration.md](monitoring/grafana-integration.md)
{% endcontent-ref %}

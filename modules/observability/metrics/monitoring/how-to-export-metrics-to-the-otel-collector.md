---
description: This guide explains how to export Aidbox metrics in OpenTelemetry format
---

# How to export metrics to the OTEL collector

{% hint style="info" %}
Aidbox supports exporting metrics using the Protobuf protocol in line with the OTEL specification. This guide configures Aidbox to export metrics to the OpenTelemetry collector, but the setup can also be modified to export to other metrics consumers that adhere to this specification.
{% endhint %}

{% hint style="info" %}
This way of enabling OTEL capabilities is available in Aidbox versions 2503 and later. On previous AIdbox versions it was possible to enable OTEL with [Aidbox configuration project](https://docs.aidbox.app/modules/observability/getting-started/how-to-export-telemetry-to-the-otel-collector#how-to-enable-export-telemetry-to-the-otel-collector-with-aidbox-configuration-project).
{% endhint %}

## Prerequisites&#x20;

1. [OTEL collector](https://opentelemetry.io/docs/collector/) should be deployed and [configured](https://opentelemetry.io/docs/collector/configuration/) to receive metrics.

## How to enable metrics export to the OTEL collector

To  enable exporting metrics to the OTEL collector set the OTEL collector metrics receiver endpoint to the environment variable `BOX_OBSERVABILITY_OTEL_METRICS_URL`

## How to check the OTEL collector receives metrics&#x20;

### Set up `debug` exporter and `metrics` pipeline in the OTEL collector configuration:

```yaml
receivers:
receivers:
  otlp:
    protocols:
      http:
        endpoint: <your-collector-resiever-endpoint>

exporters:
  debug:
    verbosity: detailed

service:
  pipelines:
    metrics:
      receivers: [otlp]
      exporters: [debug] # OTEL prints metrics to the stdout
```

### See Aidbox metrics in the OTEL collector stdout

Open OTEL collector stdout and see the logs.



Check all available Aidbox OTEL metrics exporter configuration options are here:&#x20;

{% content-ref url="../technical-reference/otel-metrics-exporter-parameters.md" %}
[otel-metrics-exporter-parameters.md](../technical-reference/otel-metrics-exporter-parameters.md)
{% endcontent-ref %}

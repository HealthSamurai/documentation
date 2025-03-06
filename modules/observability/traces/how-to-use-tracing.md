---
description: >-
  This guide explains how to activate traces and export them to the OTEL
  collector
---

# How to use tracing

{% hint style="info" %}
Aidbox supports exporting traces using the Protobuf protocol in line with the OTEL specification. This guide configures Aidbox to export traces to the OpenTelemetry collector, but the setup can also be modified to export to other traces consumers that adhere to this specification.
{% endhint %}

{% hint style="info" %}
This way of enabling OTEL capabilities is available in Aidbox versions 2503 and later. On previous AIdbox versions it was possible to enable OTEL with [Aidbox configuration project](https://docs.aidbox.app/modules/observability/getting-started/how-to-export-telemetry-to-the-otel-collector#how-to-enable-export-telemetry-to-the-otel-collector-with-aidbox-configuration-project).
{% endhint %}

## Prerequisites&#x20;

1. [OTEL collector](https://opentelemetry.io/docs/collector/) should be deployed and [configured](https://opentelemetry.io/docs/collector/configuration/) to receive traces.

## How to enable traces export to the OTEL collector

To  enable exporting traces to the OTEL collector set the OTEL collector traces receiver endpoint to the Aidbox setting [observability.otel.traces-url](https://docs.aidbox.app/reference/settings/observability#observability.otel.traces-url)

## How to check the OTEL collector receives traces&#x20;

### Set up `debug` exporter and `traces` pipeline in the OTEL collector configuration:

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
    traces:
      receivers: [otlp]
      exporters: [debug] # OTEL prints traces to the stdout
```

### See Aidbox traces in the OTEL collector stdout

Open OTEL collector stdout and see the traces.

### Check traces sending status

The common endpoint for checking the status of sending metrics process

```yaml
GET /telemetry/main/otel-trace-exporter/$status

queue-size: 10
history:
- ts: 1700661071
  processed-count: 34
```

### Force flush OTEL traces

To force flush all the traces Aidbox has in the queue use `$flush` endpoint:

```http
POST /telemetry/main/otel-trace-exporter/$flush
```

Check all available Aidbox OTEL traces exporter configuration options are here:&#x20;

{% content-ref url="otel-traces-exporter-parameters.md" %}
[otel-traces-exporter-parameters.md](otel-traces-exporter-parameters.md)
{% endcontent-ref %}


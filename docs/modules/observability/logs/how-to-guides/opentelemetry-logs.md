---
description: This guide explains how to export Aidbox logs in OpenTelemetry format
---

# OpenTelemetry logs

{% hint style="info" %}
Aidbox supports exporting logs using the Protobuf protocol in line with the OTEL specification. This guide configures Aidbox to export logs to the OpenTelemetry collector, but the setup can also be modified to export to other logs consumers that adhere to this specification.
{% endhint %}

{% hint style="info" %}
This way of enabling OTEL capabilities is available in Aidbox versions 2503 and later. On previous AIdbox versions it was possible to enable OTEL with [Aidbox configuration project](https://docs.aidbox.app/modules/observability/getting-started/how-to-export-telemetry-to-the-otel-collector#how-to-enable-export-telemetry-to-the-otel-collector-with-aidbox-configuration-project).
{% endhint %}

## Prerequisites

1. [OTEL collector](https://opentelemetry.io/docs/collector/) should be deployed and [configured](https://opentelemetry.io/docs/collector/configuration/) to receive logs.

## How to enable logs export to the OTEL collector

To enable exporting logs to the OTEL collector set the OTEL collector logs receiver endpoint to the Aidbox setting [observability.otel.logs-url](https://docs.aidbox.app/reference/settings/observability#observability.otel.logs-url)

## How to check the OTEL collector receives logs

### Set up `debug` exporter and `logs` pipeline in the OTEL collector configuration:

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
    logs:
      receivers: [otlp]
      exporters: [debug] # OTEL prints logs to the stdout
```

### See Aidbox logs in the OTEL collector stdout

Open OTEL collector stdout and see the logs.

### Check logs sending status

The common endpoint for checking status of sending metrics process

```yaml
GET /telemetry/main/otel-logs-appender/$status

queue-size: 10
history:
- ts: 1700661071
  processed-count: 34
```

### Force flush OTEL logs

To force flush all the logs Aidbox has in the queue use `$flush` endpoint:

```http
POST /telemetry/main/otel-logs-appender/$flush
```

Check all available Aidbox OTEL logs exporter configuration options are here:

{% content-ref url="../technical-reference/otel-logs-exporter-parameters.md" %}
[otel-logs-exporter-parameters.md](../technical-reference/otel-logs-exporter-parameters.md)
{% endcontent-ref %}

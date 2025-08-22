---
description: This guide explains how to export Aidbox metrics in OpenTelemetry format
---

# How to export metrics to the OTEL collector

{% hint style="info" %}
Aidbox supports exporting metrics using the Protobuf protocol in line with the OTEL specification. This guide configures Aidbox to export metrics to the OpenTelemetry collector, but the setup can also be modified to export to other metrics consumers that adhere to this specification.
{% endhint %}

{% hint style="info" %}
This way of enabling OTEL capabilities is available in Aidbox versions 2503 and later. On previous AIdbox versions it was possible to enable OTEL with Aidbox configuration project.
{% endhint %}

{% hint style="info" %}
As an alternative, for testing purposes, you can add an OTEL collector as an additional service within your docker-compose:

````
services:
  aidbox:
    environment:
```
      BOX_OBSERVABILITY_OTEL_TRACES_URL: http://otel-collector:4318
      BOX_OBSERVABILITY_OTEL_LOGS_URL: http://otel-collector:4318
      BOX_OBSERVABILITY_OTEL_METRICS_URL: http://otel-collector:4318
```
  otel-collector:
    image: otel/opentelemetry-collector-contrib
    volumes:
    - ./otel-collector-config.yaml:/etc/otelcol-contrib/config.yaml
    ports:
    - 4318:4318
```
````

And in the same directory as your docker-compose, add a configuration file called otel-collector-config.yaml for OTEL:

```
receivers:
  otlp:
    protocols:
      http:
        endpoint: 0.0.0.0:4318 

exporters:
  debug:
    verbosity: detailed

service:
  pipelines:
    logs:
      receivers: [otlp]
      exporters: [debug] # OTEL prints logs to the stdout
    metrics:
      receivers: [otlp]
      exporters: [debug] # OTEL prints metrics to the stdout
    traces:
      receivers: [otlp]
      exporters: [debug] # OTEL prints traces to the stdout
```

And with that, the OTEL test environment is ready.

You can view the incoming logs, metrics, and traces directly from the container using the command:

```
docker logs -f otel-collector
```
{% endhint %}

## Prerequisites

1. [OTEL collector](https://opentelemetry.io/docs/collector/) should be deployed and [configured](https://opentelemetry.io/docs/collector/configuration/) to receive metrics.&#x20;

## How to enable metrics export to the OTEL collector

To enable exporting metrics to the OTEL collector set the OTEL collector metrics receiver endpoint to the Aidbox setting [observability.otel.metrics-url](../../../../reference/settings/observability.md#observability.otel.metrics-url).

## How to check the OTEL collector receives metrics

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

Check all available Aidbox OTEL metrics exporter configuration options are here:

{% content-ref url="../technical-reference/otel-metrics-exporter-parameters.md" %}
[otel-metrics-exporter-parameters.md](../technical-reference/otel-metrics-exporter-parameters.md)
{% endcontent-ref %}

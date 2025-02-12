---
description: This guide explains how to export Aidbox metrics in OpenTelemetry format
---

# How to export metrics to the OTEL collector

{% hint style="info" %}
Aidbox supports exporting metrics using the Protobuf protocol in line with the OTEL specification. This guide configures Aidbox to export metrics to the OpenTelemetry collector, but the setup can also be modified to export to other metrics consumers that adhere to this specification.
{% endhint %}

## Prerequisites&#x20;

1. [OTEL collector](https://opentelemetry.io/docs/collector/) should be deployed and [configured](https://opentelemetry.io/docs/collector/configuration/) to receive metrics.
2. Aidbox should be configured with [Aidbox configuration project](../../../deprecated/deprecated/zen-related/aidbox-zen-lang-project/).

## How to enable export metrics to the OTEL collector

To  enable exporting metrics to the OTEL collector:

1. Import `aidbox.telemetry.metrics`
2. Define `open-telemetry-metrics-exporter`
3. Restart Adibox

```clojure
{:ns     main
 :import #{aidbox
           aidbox.telemetry.metrics} ; import aidbox.telemetry.metrics

 open-telemetry-metrics-exporter
 {:zen/tags #{aidbox.telemetry.metrics/otlp-exporter}
  :url "http://url-to-otel-collector/v1/metrics" ; metrics consumer endpoint
  :period 5 #_"sec"}} ; period in seconds to send metrics 
```

## How to check the OTEL collector receives metrics&#x20;

### Set up `logging` exporter and `metrics` pipeline in the OTEL collector configuration:

```yaml
receivers:
  otlp:
    protocols:
      http:

exporters:
  logging:
    loglevel: debug

service:
  pipelines:
    metrics:
      receivers: [otlp]
      exporters: [logging] # OTEL prints metrics to the stdout
```

### See Aidbox metrics in the OTEL collector stdout

Open OTEL collector stdout and see the metrics.

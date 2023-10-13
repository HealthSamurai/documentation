---
description: This guide explains how to export Aidbox telemetry in OpenTelemetry format
---

# How to export telemetry to the OTEL collector

Aidbox produces three types of signals: logs, metrics and traces in OTEL specification.&#x20;

## Prerequisites&#x20;

1. [OTEL collector](https://opentelemetry.io/docs/collector/) should be deployed and [configured](https://opentelemetry.io/docs/collector/configuration/) to receive logs, metrics and traces.
2. Aidbox should be configured with [Aidbox configuration project](../../../aidbox-configuration/aidbox-zen-lang-project/).

<figure><img src="../../../.gitbook/assets/Telemetry (1).png" alt=""><figcaption></figcaption></figure>

## How to enable export telemetry to the OTEL collector

To  enable exporting telemetry to the OTEL collector:

1. Import `aidbox.log`, `aidbox.telemetry.metrics`, `aidbox.telemetry.trace`
2. Define `open-telemetry-appender`, `open-telemetry-metrics-exporter`, `otel-trace-exporter`
3. Add `otel-appender` to the services
4. Restart Aidbox

```clojure
{:ns     main
 :import #{aidbox
           aidbox.log ; import aidbox.log
           aidbox.telemetry.metrics ; import aidbox.telemetry.metrics
           aidbox.telemetry.trace ; import aidbox.telemetry.trace
           config}

 open-telemetry-appender
 {:zen/tags   #{aidbox/service}
  :engine     aidbox.log/open-telemetry-appender
  :config     {:url "http://url-to-otel-collector/v1/logs"}} ; otel collector logs endpoint

 open-telemetry-metrics-exporter
 {:zen/tags #{aidbox.telemetry.metrics/otlp-exporter}
  :url "http://url-to-otel-collector/v1/metrics" ; otel collector metrics endpoint
  :period 5 #_"sec"} ; period in seconds to send metrics 
 
 otel-trace-exporter
 {:zen/tags #{aidbox.telemetry.trace/exporter}
  :engine   aidbox.telemetry.trace/otlp-exporter
  :url      "http://otel-collector-url/v1/traces"} ; otel collector traces endpoint

 box
 {:zen/tags #{aidbox/system}
  :config   config/zen-config
  :services {:otel-appender open-telemetry-appender}}} ; add otel-appender
```

## How to check the OTEL collector receives telemetry&#x20;

### Set up `logging` exporter and `logs`, `metrics`, `traces` pipelines in the OTEL collector configuration:

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
    logs:
      receivers: [otlp]
      exporters: [logging] # OTEL prints logs to the stdout
    metrics:
      receivers: [otlp]
      exporters: [logging] # OTEL prints metrics to the stdout
    traces:
      receivers: [otlp]
      exporters: [logging] # OTEL prints traces to the stdout
```

### Run any request in Aidbox

Use Aidbox REST console to perform a request like this:

```yaml
GET /fhir/Patient
```

### See Aidbox telemetry in the OTEL collector stdout

Open OTEL collector stdout and see the logs, metrics and traces.




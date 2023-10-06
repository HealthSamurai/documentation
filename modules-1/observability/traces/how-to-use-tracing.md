---
description: >-
  This guide explains how to activate traces and export them to the OTEL
  collector
---

# How to use tracing

## Prerequisites&#x20;

1. [OTEL collector](https://opentelemetry.io/docs/collector/) should be deployed and [configured](https://opentelemetry.io/docs/collector/configuration/) to receive traces.
2. Aidbox should be configured with [Aidbox configuration project](../../../aidbox-configuration/aidbox-zen-lang-project/).

## How to enable tracing&#x20;

To enable tracing in Aidbox:

1. Import `aidbox.telemetry.trace`
2. Define `otel-trace-exporter`
3. Restart Aidbox

```clojure
{ns main
 import #{aidbox
          aidbox.telemetry.trace} ; Import aidbox.telemetry.trace
 
; add otel-trace-exporter definition
 otel-trace-exporter
 {:zen/tags #{aidbox.telemetry.trace/exporter}
  :engine   aidbox.telemetry.trace/otlp-exporter
  :url      "http://otel-collector-url/v1/traces"}} ; otel collector traces endpoint
```

## How to check the OTEL collector receives traces&#x20;

### Set up `logging` exporter and `traces` pipeline in the OTEL collector configuration:

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
    traces:
      receivers: [otlp]
      exporters: [logging] # OTEL prints traces to the stdout
```

### Run any request in Aidbox

Use Aidbox REST console to perform a request like this:

```yaml
GET /fhir/Patient
```

### See Aidbox traces in the OTEL collector stdout

Open OTEL collector stdout and see the traces.

---
description: >-
  This guide explains how to activate traces and export them to the OTEL
  collector
---

# How to use tracing

{% hint style="info" %}
Aidbox supports exporting traces using the Protobuf protocol in line with the OTEL specification. This guide configures Aidbox to export traces to the OpenTelemetry collector, but the setup can also be modified to export to other traces consumers that adhere to this specification.
{% endhint %}

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
  :url      "http://otel-collector-url/v1/traces"}} ; traces consumer endpoint
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

### Check traces sending status

The common endpoint for checking the status of sending metrics process

```
GET /telemetry/<zen-namespace>/<zen-symbol-name>/$status
```

In this case

```yaml
GET /telemetry/main/otel-trace-exporter/$status

queue-size: 10
history:
- ts: 1700661071
  processed-count: 34
```

---
description: This guide explains how to export Aidbox telemetry in OpenTelemetry format
---

# How to export telemetry to the OTEL collector

Aidbox produces three types of signals: logs, metrics and traces in OTEL specification.&#x20;

{% hint style="info" %}
Aidbox supports exporting telemetry using the Protobuf protocol in line with the OTEL specification. This guide configures Aidbox to export telemetry to the OpenTelemetry collector, but the setup can also be modified to export to other telemetry consumers that adhere to this specification.
{% endhint %}

## Prerequisites

1. [OTEL collector](https://opentelemetry.io/docs/collector/) should be deployed and [configured](https://opentelemetry.io/docs/collector/configuration/) to receive logs, metrics and traces.

<figure><img src="../../../../.gitbook/assets/Telemetry (1).png" alt=""><figcaption></figcaption></figure>

## How to enable export telemetry to the OTEL collector

{% hint style="info" %}
This way of enabling OTEL capabilities is available in Aidbox versions 2503 and later. On previous AIdbox versions it was possible to enable OTEL with [Aidbox configuration project](#how-to-enable-export-telemetry-to-the-otel-collector-with-aidbox-configuration-project).
{% endhint %}

To  enable exporting telemetry to the OTEL collector, set up the following Aidbox settings:

* [observability.otel.metrics-url](broken-reference)
* [observability.otel.traces-url](broken-reference)
* [observability.otel.logs-url](broken-reference)

Set OTEL collector receiver endpoint in each of the variables.

## How to check the OTEL collector receives telemetry&#x20;

### Set up `debug` exporter and `logs`, `metrics`, `traces` pipelines in the OTEL collector configuration:

```yaml
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
    metrics:
      receivers: [otlp]
      exporters: [debug] # OTEL prints metrics to the stdout
    traces:
      receivers: [otlp]
      exporters: [debug] # OTEL prints traces to the stdout
```

### Run any request in Aidbox

Use Aidbox REST console to perform a request like this:

```yaml
GET /fhir/Patient
```

### See Aidbox telemetry in the OTEL collector stdout

Open OTEL collector stdout and see the logs, metrics and traces.

## How to enable export telemetry to the OTEL collector with Aidbox configuration project

{% hint style="danger" %}
Aidbox configuration project is [deprecated](https://www.health-samurai.io/news/aidbox-transitions-to-the-fhir-schema-engine).&#x20;
{% endhint %}

To  enable exporting telemetry to the OTEL collector:

1. Import `aidbox.log`, `aidbox.telemetry.metrics`, `aidbox.telemetry.trace`
2. Define `open-telemetry-appender`, `open-telemetry-metrics-exporter`, `otel-trace-exporter`
3. Add `otel-appender` to the services
4. Restart Aidbox

```clojure
{:ns     main
 :import #{aidbox
           aidbox.log               ; import aidbox.log
           aidbox.telemetry.metrics ; import aidbox.telemetry.metrics
           aidbox.telemetry.trace   ; import aidbox.telemetry.trace
           config}

 open-telemetry-appender
 {:zen/tags   #{aidbox/service}
  :engine     aidbox.log/open-telemetry-appender
  :config     {:url "http://url-to-otel-collector/v1/logs"}} ; logs consumer endpoint

 open-telemetry-metrics-exporter
 {:zen/tags #{aidbox.telemetry.metrics/otlp-exporter}
  :url "http://url-to-otel-collector/v1/metrics" ; metrics consumer endpoint
  :period 5 #_"sec"} ; period in seconds to send metrics 
 
 otel-trace-exporter
 {:zen/tags #{aidbox.telemetry.trace/exporter}
  :engine   aidbox.telemetry.trace/otlp-exporter
  :url      "http://otel-collector-url/v1/traces"} ; traces consumer endpoint

 box
 {:zen/tags #{aidbox/system}
  :config   config/zen-config
  :services {:otel-appender open-telemetry-appender}}} ; add otel-appender
```


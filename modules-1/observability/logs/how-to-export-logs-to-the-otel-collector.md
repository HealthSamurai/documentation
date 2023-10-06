---
description: This guide explains how to export Aidbox logs in OpenTelemetry format
---

# How to export logs to the OTEL collector

## Prerequisites&#x20;

1. [OTEL collector](https://opentelemetry.io/docs/collector/) should be deployed and [configured](https://opentelemetry.io/docs/collector/configuration/) to receive logs.
2. Aidbox should be configured with [Aidbox configuration project](../../../aidbox-configuration/aidbox-zen-lang-project/).

## How to enable export logs to the OTEL collector

To  enable exporting logs to the OTEL collector:

1. Import `aidbox.log`
2. Define `open-telemetry-appender`
3. Add `otel-appender` to the services
4. Restart Aidbox

```clojure
{:ns     main
 :import #{aidbox
           aidbox.log ; import aidbox.log
           config}

 open-telemetry-appender
 {:zen/tags   #{aidbox/service}
  :engine     aidbox.log/open-telemetry-appender
  :config     {:url "http://url-to-otel-collector/v1/logs"}} ; otel collector logs endpoint

 box
 {:zen/tags #{aidbox/system}
  :config   config/zen-config
  :services {:otel-appender open-telemetry-appender}}} ; add otel-appender
```

## How to check OTEL receives logs&#x20;

### Set up `logging` exporter and `logs` pipeline in the OTEL collector configuration:

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
```

### See Aidbox logs in the OTEL collector stdout

Open OTEL collector stdout and see the logs.

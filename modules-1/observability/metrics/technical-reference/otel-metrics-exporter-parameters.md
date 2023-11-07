---
description: This page explains what parameters OTEL metrics exporter has
---

# OTEL metrics exporter parameters

## OTEL metrics exporter example configuration

```clojure
{:ns     main
 :import #{aidbox
           aidbox.telemetry.metrics}

 open-telemetry-metrics-exporter
 {:zen/tags   #{aidbox.telemetry.metrics/otlp-exporter}
  :url        "http://url-to-otel-collector/v1/metrics"
  :auth-token "authorization-your-bearer-token"
  :period     5}}
```

### Parameters

* `url` the URL of the consumer of the metrics
* `auth-token` Bearer header authorization
* `period` says how often metrics are sent to the consumer

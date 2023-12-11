---
description: This page explains what parameters OTEL traces exporter has
---

# OTEL traces exporter parameters

## OTEL traces exporter example configuration

```clojure
{:ns     main
 :import #{aidbox
           aidbox.telemetry.trace}

 otel-trace-exporter
 {:zen/tags   #{aidbox.telemetry.trace/exporter}
  :engine     aidbox.telemetry.trace/otlp-exporter
  :url        "http://otel-collector-url/v1/traces"
  :auth-token "authorization-your-bearer-token"
  :headers {"X-custom-trace-header" "header value"}}}
```

### Parameters

* `url` the URL of the consumer of the metrics
* `auth-token` Bearer header authorization
* `headers` Custom headers for traces export request

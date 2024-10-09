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
  :period     5
  :headers {"X-custom-metric-header" "header value"}}}
```

### Parameters

* `url` the URL of the consumer of the metrics
* `auth-token` Bearer header authorization
* `period` Says how often metrics are sent to the consumer
* `headers` Custom headers for metrics export request

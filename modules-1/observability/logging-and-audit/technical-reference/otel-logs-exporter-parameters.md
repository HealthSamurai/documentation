---
description: This page explains what parameters OTEL logs exporter has
---

# OTEL logs exporter parameters

### OTEL logs exporter example configuration <a href="#otel-traces-exporter-example-configuration" id="otel-traces-exporter-example-configuration"></a>

```clojure
{:ns     main
 :import #{aidbox
           aidbox.log
           config}

 open-telemetry-appender
 {:zen/tags   #{aidbox/service}
  :engine     aidbox.log/open-telemetry-appender
  :config     {:url "http://url-to-otel-collector/v1/logs"}}

 box
 {:zen/tags #{aidbox/system}
  :config   config/zen-config
  :services {:otel-appender open-telemetry-appender}}}
```

#### Parameters <a href="#parameters" id="parameters"></a>

* `config`
  * `url` the URL of the consumer of the logs
  * `auth-token` Bearer header authorization

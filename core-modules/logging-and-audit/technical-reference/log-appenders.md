---
description: This page explains what log appenders are and their parameters
---

# Log appenders

## What log appenders are

There are appenders in Aidbox to process log messages. An appender is a processing pipeline which:

1. Filters log messages according its rules
2. Transforms logs according its rules
3. Delivers logs to the desired consumer

Each appender has independent set of filters and transformers. You can think of appenders as of streams that preprocess log messages and delivers them to consumers.

### How appender can be attached <a href="#how-appender-can-be-attached" id="how-appender-can-be-attached"></a>

To attach an appender:

1. Define the appender in Aidbox configuration
2. Connect the appender as Aidbox service

```clojure
{:ns     main
 :import #{aidbox
           aidbox.log
           config}

 db-q-transform
 {:zen/tags #{aidbox.log/transform}
  :transforms
   [{:filter {:path [:d] :op :min :value 100}}
    {:trim {:path [:sql] :length 50}}]}

 stdout-appender
 {:zen/tags #{aidbox/service}
  :engine   aidbox.log/stdout-appender
  :transforms {:qb/q db-q-transform}}

 box
 {:zen/tags #{aidbox/system}
  :config   config/zen-config
  :services {:seed            config/admin-seed
             :stdout-appender stdout-appender}}}
```

In this example `stdout-appender` is defined and connected to Aidbox as a service.

## stdout-appender

The `stdout-appender` directs the logs stream to the standard output (stdout).

```clojure
 stdout-appender
 {:zen/tags   #{aidbox/service}
  :engine     aidbox.log/stdout-appender
  :pretty     true}
```

### Parameters

* `:pretty` enables prettiness if it is true

## elasticsearch-appender

It directs the stream of log messages to the Elasticsearch instance.

```
{:ns     main
 :import #{aidbox
           aidbox.log
           config}

 elasticsearch-appender
 {:zen/tags   #{aidbox/service}
  :engine     aidbox.log/elasticsearch-appender
  :config     {:url           "http://localhost:9200"
               :index-pattern "'aidbox'-yyyy-MM-dd"
               :batch         {:size 200
                               :timeout 5000}}}

 box
 {:zen/tags #{aidbox/system}
  :config   config/zen-config
  :services {:seed                   config/admin-seed
             :elasticsearch-appender elasticsearch-appender}}}
```

### Parameters

* `config`
  * `url` Elasticsearch url
  * `index-pattern` Сustom index name format string. Default value is `‘aidbox-logs’-yyyy-MM-dd`
  * `auth` Basic auth credentials if necessary
    * `username`
    * `password`
  * `batch`
    * `size` Size of elastic search post batch. Default value is 200
    * `timeout` Timeout (in millisecond) when to post batch if there not enough messages to post a full batch. Default value is 1000 (1 second)

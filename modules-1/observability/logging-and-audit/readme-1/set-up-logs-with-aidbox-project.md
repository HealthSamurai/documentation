---
description: This guide explains how logging can be activated with the Aidbox project
---

# Set up logs with Aidbox project

We expect your [Aidbox project](../../../../core-modules/logging-and-audit/readme-1/broken-reference/) is set up and running.

## How do logs work? <a href="#how-logs-work" id="how-logs-work"></a>

There are appenders in Aidbox to process log messages. Appender is a processing pipeline which:

1. Filters logs according to appender's rules
2. Transforms logs according to appender's rules
3. Delivers logs to the desired log destination

Each appender has independent set of filters and transformers.

You can think of appenders as of streams that preprocess logs and deliver log messages.

### How appender can be attached <a href="#how-appender-can-be-attached" id="how-appender-can-be-attached"></a>

To attach an appender:

1. Define the appender in Aidbox configuration
2. Connect the appender as Aidbox service

More detailed appenders information can be found in tech reference section.

{% content-ref url="../technical-reference/log-appenders.md" %}
[log-appenders.md](../technical-reference/log-appenders.md)
{% endcontent-ref %}

### stdout-appender

Stdout-appender directs the logs stream to the standard output (stdout). Stdout appender sample configuration.

```clojure
 stdout-appender
 {:zen/tags   #{aidbox/service}
  :engine     aidbox.log/stdout-appender
  :pretty     true}
```

### elasticsearch-appender

This appenders directs the stream of logs to the Elasticsearch instance. Elasticsearch appender sample configuration.

```clojure
 elasticsearch-appender
 {:zen/tags   #{aidbox/service}
  :engine     aidbox.log/elasticsearch-appender
  :config     {:url           "http://host.docker.internal:9200"
               :index-pattern "'aidbox'-yyyy-MM-dd"
               :batch         {:size 1 :timeout 0}}}
```

### open-telemetry-appender

This appenders directs the stream of logs to the OpenTelemetry Collector. OpenTelemetry appender sample configuration.

```clojure
 open-telemetry-appender
 {:zen/tags   #{aidbox/service}
  :engine     aidbox.log/open-telemetry-appender
  :config     {:url "http://host.docker.internal:4318/v1/logs"}} ; otel collector logs endpoint
```

## Attached appenders snippet

In this configuration two appenders (stdout and Elasticsearch) are defined and attached as Aidbox services.

```clojure
{:ns     main
 :import #{aidbox
           aidbox.log
           config}

 stdout-appender
 {:zen/tags   #{aidbox/service}
  :engine     aidbox.log/stdout-appender
  :pretty     true}

 elasticsearch-appender
 {:zen/tags   #{aidbox/service}
  :engine     aidbox.log/elasticsearch-appender
  :config     {:url           "http://host.docker.internal:9200"
               :index-pattern "'aidbox'-yyyy-MM-dd"
               :batch         {:size 1 :timeout 0}}}

 open-telemetry-appender
 {:zen/tags   #{aidbox/service}
  :engine     aidbox.log/open-telemetry-appender
  :config     {:url "http://host.docker.internal:4318/v1/logs"}} ; otel collector logs endpoint

 box
 {:zen/tags #{aidbox/system}
  :config   config/zen-config
  :services {:seed                   config/admin-seed
             :stdout                 stdout-appender
             :otel-appender          open-telemetry-appender
             :elasticsearch-appender elasticsearch-appender}}}
```

### Logs output

When configuration is ready run Aidbox and login to the Aidbox UI.

**Perform REST request**

1. Click the `REST Console` menu item in the left sidebar
2. Press the `Send` button

**Perform SQL request**

1. Click the `DB Console` menu item in the left sidebar
2. Enter sql statement `select pg_sleep(0)`
3. Press the green button
4. Enter sql statement `select pg_sleep(1)`
5. Press the green button

In the terminal you should see a lot of lines like those below.

```
aidbox-project-aidbox-1 | devbox 11:06:57 w18 GET /fhir/Patient
aidbox-project-aidbox-1 | devbox 11:06:57 w18 policy admin-seed-policy
aidbox-project-aidbox-1 | devbox 11:06:57 w19 PUT /ui_history/4a3133aa3d02ef4a03caa05f2aec108ac0710eee
aidbox-project-aidbox-1 | devbox 11:06:57 w19 policy admin-seed-policy
aidbox-project-aidbox-1 | devbox 11:06:57 w20 POST /rpc?_format=transit&_m=aidbox.product/mixpanel-track-event
aidbox-project-aidbox-1 | devbox 11:06:57 w20 policy admin-seed-policy
aidbox-project-aidbox-1 | devbox 11:06:57 :info w20 [0ms] :rpc/call {:rpc_p {:type "rest.send", :props {:isFhir true, :resourceType "Patient", :requestMethod "GET"}}, :rpc_m aidbox.product/mixpanel-track-event, :rpc_u {:email nil}, :op "rpc:mixpanel-track-event", :d 0, :ctx "87af9e90-6edd-4aa8-b747-30dc2c8bdc44"}
aidbox-project-aidbox-1 | devbox 11:06:57 w20 [3ms] 200
aidbox-project-aidbox-1 | devbox 11:06:57 w18 [23ms] 200
aidbox-project-aidbox-1 | devbox 11:06:57 w19 :resource/update-dup {:rid "4a3133aa3d02ef4a03caa05f2aec108ac0710eee", :rtp "ui_history", :ctx "a6958320-48c2-4484-a28c-48b3a99c13aa", :op "update"}
aidbox-project-aidbox-1 | devbox 11:06:57 w19 [28ms] 200
aidbox-project-aidbox-1 | devbox 11:06:59 w17 GET /ui_history?.type=sql&_sort=-_lastUpdated&_count=200
aidbox-project-aidbox-1 | devbox 11:06:59 w17 policy admin-seed-policy
aidbox-project-aidbox-1 | devbox 11:06:59 w17 [15ms] 200
aidbox-project-aidbox-1 | devbox 11:06:59 w22 POST /rpc?_format=transit&_m=aidbox.product/mixpanel-track-event
aidbox-project-aidbox-1 | devbox 11:06:59 w22 policy admin-seed-policy
aidbox-project-aidbox-1 | devbox 11:06:59 :info w22 [0ms] :rpc/call {:rpc_p {:type "db.view"}, :rpc_m aidbox.product/mixpanel-track-event, :rpc_u {:email nil}, :op "rpc:mixpanel-track-event", :d 0, :ctx "a1b4f6df-5988-4a9a-95b9-01c68dec79cd"}
aidbox-project-aidbox-1 | devbox 11:06:59 w22 [3ms] 200
aidbox-project-aidbox-1 | devbox 11:07:01 w21 POST /$psql
aidbox-project-aidbox-1 | devbox 11:07:01 w23 PUT /ui_history/8981ec63fdea55f009c5c9ab618a5a8d4d79f5da
aidbox-project-aidbox-1 | devbox 11:07:01 w24 POST /rpc?_format=transit&_m=aidbox.product/mixpanel-track-event
aidbox-project-aidbox-1 | devbox 11:07:01 w21 policy admin-seed-policy
aidbox-project-aidbox-1 | devbox 11:07:01 w24 policy admin-seed-policy
aidbox-project-aidbox-1 | devbox 11:07:01 w23 policy admin-seed-policy
aidbox-project-aidbox-1 | devbox 11:07:01 :info w24 [0ms] :rpc/call {:rpc_p {:type "db.send"}, :rpc_m aidbox.product/mixpanel-track-event, :rpc_u {:email nil}, :op "rpc:mixpanel-track-event", :d 0, :ctx "55d64d4e-b954-4c9c-97ec-a43d2687725d"}
aidbox-project-aidbox-1 | devbox 11:07:01 w24 [3ms] 200
aidbox-project-aidbox-1 | devbox 11:07:01 w21 [11ms] 200
aidbox-project-aidbox-1 | devbox 11:07:01 w23 :resource/update-dup {:rid "8981ec63fdea55f009c5c9ab618a5a8d4d79f5da", :rtp "ui_history", :ctx "334bf96a-bf84-435b-8036-410ac45e2f2b", :op "update"}
aidbox-project-aidbox-1 | devbox 11:07:01 w23 [36ms] 200
aidbox-project-aidbox-1 | devbox 11:07:03 w27 POST /rpc?_format=transit&_m=aidbox.product/mixpanel-track-event
aidbox-project-aidbox-1 | devbox 11:07:03 w25 PUT /ui_history/6a8f43773760128b07b76433a1ab216ca8cdedb1
aidbox-project-aidbox-1 | devbox 11:07:03 w26 POST /$psql
aidbox-project-aidbox-1 | devbox 11:07:03 w27 policy admin-seed-policy
aidbox-project-aidbox-1 | devbox 11:07:03 w26 policy admin-seed-policy
aidbox-project-aidbox-1 | devbox 11:07:03 w25 policy admin-seed-policy
aidbox-project-aidbox-1 | devbox 11:07:03 :info w27 [0ms] :rpc/call {:rpc_p {:type "db.send"}, :rpc_m aidbox.product/mixpanel-track-event, :rpc_u {:email nil}, :op "rpc:mixpanel-track-event", :d 0, :ctx "df8c9b9a-a598-43e2-a326-49ab734f13bf"}
aidbox-project-aidbox-1 | devbox 11:07:03 w27 [7ms] 200
aidbox-project-aidbox-1 | devbox 11:07:03 w25 :resource/update-dup {:rid "6a8f43773760128b07b76433a1ab216ca8cdedb1", :rtp "ui_history", :ctx "31fd9de4-f7c3-439a-a728-32859ac652a8", :op "update"}
aidbox-project-aidbox-1 | devbox 11:07:03 w25 [21ms] 200
aidbox-project-aidbox-1 | devbox 11:07:04 w26 [1008ms] select pg_sleep(1); 
aidbox-project-aidbox-1 | devbox 11:07:04 w26 [1021ms] 200
```

{% hint style="info" %}
The same set of lines is sent to Elasticsearch
{% endhint %}

## Add transformation

Transformations is the tool to filter and alter log messages.

The full list of supported transformations can be found in the technical reference.

{% content-ref url="../technical-reference/log-transformations.md" %}
[log-transformations.md](../technical-reference/log-transformations.md)
{% endcontent-ref %}

### Filter noisy :w/req

To remove http requests and responses messages from the stdout logs use `aidbox.log/ignore` transformation.

```clojure
 stdout-appender
 {:zen/tags   #{aidbox/service}
  :engine     aidbox.log/stdout-appender
  :pretty     true
  :transforms {:w/req  aidbox.log/ignore
               :w/resp aidbox.log/ignore}}
```

**Updated snippet**

```clojure
{:ns     main
 :import #{aidbox
           aidbox.log
           config}

 stdout-appender
 {:zen/tags   #{aidbox/service}
  :engine     aidbox.log/stdout-appender
  :pretty     true
  :transforms {:w/req  aidbox.log/ignore
               :w/resp aidbox.log/ignore}}

 elasticsearch-appender
 {:zen/tags   #{aidbox/service}
  :engine     aidbox.log/elasticsearch-appender
  :config     {:url           "http://host.docker.internal:9200"
               :index-pattern "'aidbox'-yyyy-MM-dd"
               :batch         {:size 1 :timeout 0}}}

 box
 {:zen/tags #{aidbox/system}
  :config   config/zen-config
  :services {:seed                   config/admin-seed
             :stdout                 stdout-appender
             :elasticsearch-appender elasticsearch-appender}}}
```

Restart Aidbox and [repeat the actions](set-up-logs-with-aidbox-project.md#logs-output) we perform to issue log messages.

**Logs output**

There are fewer lines in the output.

```
aidbox-project-aidbox-1 | devbox 11:21:29 w15 [9ms] SELECT "id" FROM "ui_history" WHERE "id" = ? ["4a3133aa3d02ef4a03caa05f2aec108ac0710eee"]
aidbox-project-aidbox-1 | devbox 11:21:29 w13 [24ms] SELECT "patient".* FROM "patient" LIMIT ? OFFSET ?  ["100" "0"]
aidbox-project-aidbox-1 | devbox 11:21:29 w15 :resource/update-dup {:rid "4a3133aa3d02ef4a03caa05f2aec108ac0710eee", :rtp "ui_history", :ctx "017963eb-6010-4d92-8ba8-71619b0fb565", :op "update"}
aidbox-project-aidbox-1 | devbox 11:21:31 w16 [14ms] SELECT "ui_history".* FROM "ui_history" WHERE ("ui_history".resource #>> ? in (?)) ORDER BY "ui_history".ts DESC LIMIT ? OFFSET ?  ["{type}" "sql" "200" "0"]
aidbox-project-aidbox-1 | devbox 11:21:33 w19 [7ms] SELECT "id" FROM "ui_history" WHERE "id" = ? ["8981ec63fdea55f009c5c9ab618a5a8d4d79f5da"]
aidbox-project-aidbox-1 | devbox 11:21:33 w18 [6ms] select pg_sleep(0); 
aidbox-project-aidbox-1 | devbox 11:21:33 w19 :resource/update-dup {:rid "8981ec63fdea55f009c5c9ab618a5a8d4d79f5da", :rtp "ui_history", :ctx "6d48e015-0d42-4d74-b760-6e8f7744c18e", :op "update"}
aidbox-project-aidbox-1 | devbox 11:21:36 w23 policy admin-seed-policy
aidbox-project-aidbox-1 | devbox 11:21:36 w23 policy admin-seed-policy
aidbox-project-aidbox-1 | devbox 11:21:36 w23 policy admin-seed-policy
aidbox-project-aidbox-1 | devbox 11:21:36 w25 [5ms] SELECT "id" FROM "ui_history" WHERE "id" = ? ["6a8f43773760128b07b76433a1ab216ca8cdedb1"]
aidbox-project-aidbox-1 | devbox 11:21:36 w25 :resource/update-dup {:rid "6a8f43773760128b07b76433a1ab216ca8cdedb1", :rtp "ui_history", :ctx "e7123073-001f-4ba4-b652-08c4a5cde095", :op "update"}
aidbox-project-aidbox-1 | devbox 11:21:37 w22 [1004ms] select pg_sleep(1); 
```

{% hint style="info" %}
Keep in mind Elasticsearch still receives logs without omissions
{% endhint %}

### Filter noisy :db/q logs

To remove fast SQL queries from logs define the transformer and apply it to the appender.

```clojure
 db-q-transform
 {:zen/tags #{aidbox.log/transform}
  :transforms
  [{:filter {:path [:d] :op :min :value 100}}
   {:trim {:path [:sql] :length 20}}]}
```

This transformation does two things:

1. Filter the log message out if the `d` (duration) property is less than 100 (milliseconds). Only slow queries appear
2. Takes 20 first chars from the `sql` statement

**Updated snipped**

```clojure
{:ns     main
 :import #{aidbox
           aidbox.log
           config}

 db-q-transform
 {:zen/tags #{aidbox.log/transform}
  :transforms
  [{:filter {:path [:d] :op :min :value 100}}
   {:trim {:path [:sql] :length 20}}]}

 stdout-appender
 {:zen/tags   #{aidbox/service}
  :engine     aidbox.log/stdout-appender
  :pretty     true
  :transforms {:w/req  aidbox.log/ignore
               :w/resp aidbox.log/ignore
               :db/q   db-q-transform}}}

 elasticsearch-appender
 {:zen/tags   #{aidbox/service}
  :engine     aidbox.log/elasticsearch-appender
  :config     {:url           "http://host.docker.internal:9200"
               :index-pattern "'aidbox'-yyyy-MM-dd"
               :batch         {:size 1 :timeout 0}}}

 box
 {:zen/tags #{aidbox/system}
  :config   config/zen-config
  :services {:seed                   config/admin-seed
             :stdout                 stdout-appender
             :elasticsearch-appender elasticsearch-appender}}}
```

**Logs output**

There is only one line related to the SQL. It is there since the query was slow. Elasticsearch still receives all logs without omissions and transformations.

```
aidbox-project-aidbox-1 | devbox 11:38:11 w18 policy admin-seed-policy
aidbox-project-aidbox-1 | devbox 11:38:11 w17 policy admin-seed-policy
aidbox-project-aidbox-1 | devbox 11:38:11 w19 policy admin-seed-policy
aidbox-project-aidbox-1 | devbox 11:38:12 w18 :resource/update-dup {:rid "4a3133aa3d02ef4a03caa05f2aec108ac0710eee", :rtp "ui_history", :ctx "f7a23c9e-d54f-459c-9afb-4530ceecad0d", :op "update"}
aidbox-project-aidbox-1 | devbox 11:38:17 w24 :resource/update-dup {:rid "8981ec63fdea55f009c5c9ab618a5a8d4d79f5da", :rtp "ui_history", :ctx "9e587633-a350-4dc6-9b56-3fa02b200038", :op "update"}
aidbox-project-aidbox-1 | devbox 11:38:20 w26 policy admin-seed-policy
aidbox-project-aidbox-1 | devbox 11:38:20 w25 policy admin-seed-policy
aidbox-project-aidbox-1 | devbox 11:38:20 w27 policy admin-seed-policy
aidbox-project-aidbox-1 | devbox 11:38:20 w25 :resource/update-dup {:rid "6a8f43773760128b07b76433a1ab216ca8cdedb1", :rtp "ui_history", :ctx "e8752190-1cfb-41e0-ac70-eb6103cb7bd4", :op "update"}
aidbox-project-aidbox-1 | devbox 11:38:21 w27 [1004ms] select pg_sleep(1); 
```

## Conclusion

This tutorial explained:

* What log appenders are
* How to activate logs with Aidbox configuration
* What transformers can be used to ignore log types entirely
* How transformer is useful to filter log conditionally and alter the log message according to business rules

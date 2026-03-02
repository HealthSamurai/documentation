---
description: Configure and manage Aidbox logging with Logs for observability and monitoring.
---

# Logs

Aidbox produces structured logs on every significant event to the internal stream which you may direct to different ways:

* [stdout in json or pretty string formats](./#stdout-log)
* [file](./#log-to-file)
* [Elasticsearch](how-to-guides/elastic-logs-and-monitoring-integration.md)
* [Datadog](how-to-guides/datadog-log-management-integration.md)
* [Loki](how-to-guides/loki-log-management-integration.md)

{% hint style="info" %}
Aidbox logs can be used in audit. Please, see our [article on auditing](../../../access-control/audit-and-logging.md) for more info.
{% endhint %}

## Logs exporting with OTEL spec

Aidbox supports the [OpenTelemetry protocol](https://opentelemetry.io/) and exports logs in Protobuf format to any consumer that supports this specification.

See also [OpenTelemetry Logs](how-to-guides/opentelemetry-logs.md)

## Logs API

You can get current logs through REST API by `GET /_logs` - response will be Chunked Transfer Encoding stream of new line separated JSON objects:

```yaml
GET /_logs

# resp
{"ev":"w/req","w_url":"/Patient","w":"w3","w_m":"get","tn":"edgeniquola","ts":"2019-04-18T13:35:43Z","w_addr":"83.243.75.14, 35.244.249.127","ctx":"d0625fcf-f1a7-4b78-bbdf-b4ec87b6fb57","w_qs":null}
{"d":3,"sql":"\nselect true from _box where id = 'self'\nand resource @>\njsonb_build_object(\n  'participant',\n  jsonb_build_array(json_build_object('user', json_build_object('id', ?::text )))\n) ","db_prm":["github-32066"],"ts":"2019-04-18T13:35:43Z","w":"w3","ev":"db/q","tn":"edgeniquola","ctx":"d0625fcf-f1a7-4b78-bbdf-b4ec87b6fb57"}
{"d":8,"sql":"SELECT \"patient\".* FROM \"patient\" LIMIT ? OFFSET ?","db_prm":["100","0"],"ts":"2019-04-18T13:35:43Z","w":"w3","ev":"db/q","tn":"edgeniquola","ctx":"d0625fcf-f1a7-4b78-bbdf-b4ec87b6fb57"}
{"ev":"w/resp","ctx_end":true,"w_url":"/Patient","w":"w3","w_m":"get","tn":"edgeniquola","ts":"2019-04-18T13:35:43Z","d":15,"w_st":200,"ctx":"d0625fcf-f1a7-4b78-bbdf-b4ec87b6fb57"}
....
```

{% hint style="info" %}
Please, pay attention, that you will not see the response for `GET /_logs` request in the Aidbox REST console. Use the terminal, or the browser console instead.
{% endhint %}

### Logs in browser

You can open `[aidbox-base]/_logs` in your browser, and Aidbox will produce logs in pretty format, similar to [AIDBOX\_STDOUT\_PRETTY](./#stdout-log) format for stdout. Appeared in v2210. Available formats: ui (default), json, event-stream. Use the following parameter to change the format `[aidbox-base]/_logs?format=json`

## Configure logging

### Stdout log

Aidbox sends logs into stdout if one of these env variables defined: [`BOX_OBSERVABILITY_STDOUT_LOG_LEVEL`](../../../reference/all-settings.md#observability.stdout.log-level), [`BOX_OBSERVABILITY_STDOUT_PRETTY_LOG_LEVEL`](../../../reference/all-settings.md#observability.stdout.pretty-log-level). Deprecated names `AIDBOX_STDOUT_JSON` and `AIDBOX_STDOUT_PRETTY` are still accepted.

Both environment variables enable logging to stdout but the difference is log format.

#### Example of the log output when `BOX_OBSERVABILITY_STDOUT_LOG_LEVEL` enabled

```json
{"sql":"SELECT 1","d":2,"ts":"2022-10-26T10:59:59.825Z","w":"main","ev":"db/q"}
```

#### Example of the log output when `BOX_OBSERVABILITY_STDOUT_PRETTY_LOG_LEVEL` enabled

```
11:01:12 main [1ms] SELECT 1
```

### Log to file

If you prefer to write logs into the file system, in the .env file specify `AIDBOX_LOGS` environment variable with a relative path to the desired location.

```yaml
AIDBOX_LOGS=/logs/aidbox
```

Another option is to store logs in:

* [ElasticSearch](how-to-guides/elastic-logs-and-monitoring-integration.md)
* [Datadog](how-to-guides/datadog-log-management-integration.md)
* [Loki](how-to-guides/loki-log-management-integration.md)

# Aidbox Logs

Aidbox produces structured logs on every signifact event to the internal stream which you may direct to different ways:
- stdout in json or pretty string formats
- file
- Elasticsearch
- Datadog
- Loki

## Logs Schema

### General attributes

| Attribute   | Type          | Description                                                                    |
| ----------- | ------------- | ------------------------------------------------------------------------------ |
| **tn**      | string        | tenant id - i.e. box id                                                        |
| **ev**      | string        | log event name                                                                 |
| **lvl**     | enum          | empty - info, warn, error, panic                                               |
| **ts**      | date string   | event timestamp in ISO 8601 format                                             |
| **d**       | number        | duration in milliseconds                                                       |
| **w**       | string        | worker name                                                                    |
| **msg**     | string        | event description                                                              |
| **err**     | string        | error description                                                              |
| **etr**     | string        | exception stack trace                                                          |
| **ctx**     | string        | context id                                                                     |
| **ctx_end** | bool          | flag last item in  context, used to help aggregate log entries for one context |

### Event types

In table below you can see the most popular log events:

| event name (ev)                                                 | description                                  |
| --------------------------------------------------------------- | -------------------------------------------- |
| **w/req**                                                       | HTTP request                                 |
| **w/resp**                                                      | HTTP response                                |
| **db/q**                                                        | DB query                                     |
| **auth/authorized-access-policy**                               | Access Policy Evaluation                     |
| **resource/create**                                             | Resource created                             |
| **resource/update**                                             | Resource updated                             |
| **resource/delete**                                             | Resource deleted                             |
| **resource/create-dup**                                         | Create duplicate                             |
| **resource/update-dup**                                         | Update duplicate                             |
| **auth/login**                                                  | Succesful user login                         |
| **auth/authorize-failed**                                       | Failed login attempt                         |
| <p><strong>auth/warn </strong></p><p>err: wrong_credentials</p> | Failed login attempt due to bad credentials  |
| **db/db-size**                                                  | Size of database                             |
| **db/rel-size**                                                 | Size of table                                |

### REST logs

| Attribute     | type   | Description                                                                                 |
| ------------- | ------ | ------------------------------------------------------------------------------------------- |
| **ev**        | string | **w/req** - start request, **w/resp** - end of request                                      |
| **ctx**       | string | request id                                                                                  |
| **w_url**     | url    | request URL                                                                                 |
| **w_r**       | string | request URL template (i.e. GET /Patient/?)                                                  |
| **w_m**       | string | HTTP method in lower-case                                                                   |
| **w_qs**      | string | request query string                                                                        |
| **w_st**      | int    | HTTP response status                                                                        |
| **w_addr**    | string | Comma separated client and middleware IPs                                                   |
| **w_corr_id** | string | X-Correlation-Id header of original request                                                 |
| **w_audit**   | json   | [X-Audit header](../../app-development-guides/receive-logs-from-your-app/x-audit-header.md) |
| **d**         | int    | Request duration in ms                                                                      |
| **w_uid**     | string | User id                                                                                     |
| **w_cid**     | string | Client id                                                                                   |

### SQL logs

| Attribute  | Type      | Description                                       |
| ---------- | --------- | ------------------------------------------------- |
| **ev**     | string    | **db/q** - for query and **db/ex** for statements |
| **sql**    | string    | SQL query                                         |
| **db_prm** | string\[] | Collection of query parameters                    |
| **d**      | int       | Query duration in ms                              |

### Auth logs

| Attributes | Type   | Description |
| ---------- | ------ | ----------- |
| **ev**     | string | a/warn      |

## Logs API

You can get current logs through REST API by `GET /_logs`  - response will be  Chunked Transfer Encoding stream of new line separated JSON objects:

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

## Configure logging

Aidbox sends logs into stdout if one of these env variables defined: `AIDBOX_STDOUT_JSON`, `AIDBOX_STDOUT_PRETTY`

If you prefer to write logs into the file system,  in the .env file specify `AIDBOX_LOGS` environment variable with a relative path to the desired location.

```yaml
AIDBOX_LOGS=/logs/aidbox
```

Another option is to store logs in the ElasticSearch. Please, read the details [here ](https://docs.aidbox.app/app-development-guides/receive-logs-from-your-app/elastic-logs-and-monitoring-integration)


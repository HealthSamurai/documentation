# Logging & Audit

{% hint style="warning" %}
This feature is a beta version which is still undergoing final testing before its official release
{% endhint %}

This feature is a beta version which is still undergoing final testing before its official release

### Aidbox Logging

Aidbox generate structured logs in Newline Delimited JSON format \(ndjson\). 

Logs format are friendly to load into PostgreSQL, ElasticSearch and other database. Upcoming Aidbox.CLI util will help you with that.

Aidbox logger can be configured to log into file system with built-in rotation or to stdout.



### Logs Schema

#### General attributes

| Attribute | Type | Description |
| :--- | :--- | :--- |
| **tn** | string | tenant id - i.e. box id |
| **ev** | string | log event name |
| **lvl** | enum | empty - info, warn, error, panic |
| **ts** | date string   | event timestamp in ISO 8601 format |
| **d** | number | duration in milliseconds |
| **w** | string | worker name |
| **msg** | string  | event description |
| **err** | string | error description |
| **etr** | string | exception stack trace |
| **ctx** | string | context id |
| **ctx\_end** | bool | flag last item in  context, used to help aggregate log entries for one context |

#### REST logs

| Attribute | type | Description |
| :--- | :--- | :--- |
| **ev** | string | **w/req** - start request, **w/resp** - end of request |
| **ctx** | string | request id |
| **w\_url** | url | request URL |
| **w\_m** | string | HTTP method in lower-case |
| **w\_qs** | string | request query string |
| **w\_addr** | string | Comma separated client and middleware IPs |
| **w\_corr\_id** | string | X-Correlation-Id header of original request |
| **w\_audit** | json | [X-Audit header](x-audit-header.md) |
| **d** | int | Request duration in ms |

#### SQL logs

| Attribute | Type | Description |
| :--- | :--- | :--- |
| **ev** | string | **db/q** - for query and **db/ex** for statements |
| **sql** | string | SQL query |
| **db\_prm** | string\[\] | Collection of query parameters  |
| **d** | int | Query duration in ms |

### Auth logs

| Attributes | Type | Description |
| :--- | :--- | :--- |
| **ev** | string | a/warn |

### Logs API

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



### Demos

#### Watch server logs in browser console:

{% embed url="https://www.youtube.com/watch?v=sLfUHQHT5CU" %}

#### Server logs with Aidbox.CLI

#### Configure logging






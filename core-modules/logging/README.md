# Logging

## Overview

Aidbox produces structured logs on every signifact event to the internal stream which you may direct to different ways:
- stdout in json or pretty string formats
- file
- Elasticsearch
- Datadog
- Loki

System logs are designed to help developer troubleshoot errors in the first place. It can be alse used to build an audit system on top of it.

### Log Schema

#### General Attributes

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

#### Event types

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


## Audit logs

Aidbox [automatically logs](https://docs.aidbox.app/core-modules/logging-and-audit) all auth, API, database and network events, so in most cases basic audit log may be derived from Aidbox system log.

In rare cases Aidbox system logs are not enough. For instance, you would like to track business-relevant events happening outside of Aidbox control.

> Example:
> Showing list of patients on UI and export patient list to a file may require the same request to Aidbox
> You can use the same patient search operation for, let's say, for just showing the patient list on UI or to make an export file, which are different events from business perspective.


Aidbox provides you two ways to recieve your events. ???

Extending Aidbox logs. Aidbox allows you to enhance logs with your own data and push your own logs into Aidbox stream along with its internal logs.

Use FHIR AuditEvent to store business-relevant events. FHIR introduced AuditEvent resource which plays well in FHIR ecosystem. Aidbox provides all FHIR API operation for AuditEvent resource.

http://hl7.org/fhir/auditevent.html


## Extending Aidbox logs

There few ways you may extend Aidbox logs

### X-Audit-Req-Body

By default aidbox doesn't log request body, because in most cases it's redudant. It may contain sensitve information (like passwords) or it may be pretty big and clog the log.

In case you want Aidbox to log request body (e.g. [FHIR Search over POST](http://hl7.org/fhir/http.html#search)) you can provide `X-Audit-Req-Body: true` header in the request.

```
POST /Patient/_search
Content-Type: application/x-www-form-urlencoded

name=John
```

```json
{
    "ev": "w/req",
    "w_url": "/Patient/_search",
    "w": "w2",
    "w_m": "post",
    "tn": "devbox",
    "op": "search",
    "ts": "2022-08-18T13:51:08.810Z",
    "w_addr": "127.0.0.1",
    "ctx": "182bdb39120ac310b9e84d1fd809e539"
}
...
{
    "ev": "w/resp",
    "w_url": "/Patient/_search",
    "w": "w2",
    "w_m": "post",
    "tn": "devbox",
    "op": "search",
    "ts": "2022-08-18T13:51:08.821Z",
    "w_ip": "127.0.0.1",
    "rt": "Patient",
    "w_r": "POST /Patient/_search",
    "w_uid": "admin",
    "d": 12,
    "w_st": 200,
    "ctx": "182bdb39120ac310b9e84d1fd809e539"
}
```

```
POST /Patient/_search
Content-Type: application/x-www-form-urlencoded
X-Audit-Req-Body: true

name=John
```

```json
{
    "ev": "w/req",
    "w_url": "/Patient/_search",
    "w": "w5",
    "w_m": "post",
    "tn": "devbox",
    "op": "search",
    "ts": "2022-08-18T13:54:47.623Z",
    "w_addr": "127.0.0.1",
    "w_b": "{\"name\":\"John\"}",
    "ctx": "534e2a281adeb00fa06b28f41b862ce3"
}
...
{
    "ev": "w/resp",
    "w_url": "/Patient/_search",
    "w": "w5",
    "w_m": "post",
    "tn": "devbox",
    "op": "search",
    "ts": "2022-08-18T13:54:47.652Z",
    "w_ip": "127.0.0.1",
    "rt": "Patient",
    "w_r": "POST /Patient/_search",
    "w_uid": "admin",
    "d": 30,
    "w_st": 200,
    "ctx": "534e2a281adeb00fa06b28f41b862ce3"
}
```

## X-Audit Header

---
description: Add custom fields to logs
---

If you want to add custom data to logs, you can use `X-Audit` header in the format `base64(json)` — all properties from this json will be merged with `w/req` and `w/resp` events.

```yaml
POST /Operation
X-Audit: eyJyb2xlIjogIm51cnNlIn0=
# base64({"role": "nurse"})

...body

# in logs
{ev: 'w/req', ....., role: 'nurse'}
{ev: 'w/resp', ....., role: 'nurse'}
```

## $loggy endpoint

Aidbox provides /$loggy endpoint that accepts logs with the defined structure
  from your application. These logs are ingested into the elastic log. You can
  find examples below.

### Input params

| Param name | Required | Type | Description |
| :--- | :--- | :--- | :--- |
| **type** | Yes | string | It is a special keyword for your logs like "ui", "ui-error", "backend-crush" |
| **message** | Yes | JSON | It contains information that you want to log. It should be noted that we record your log data only from the message property |
| **v** | No | string | It means your app version.  |
| **fx** | No | string | fx or event on your side from which you want to log data or error.  |

### Example

```yaml
 POST /$loggy

 type: ui
 v: "2020.02"
 fx: "fetchUsers"
 message:
    error:
        message: "Access Denied."

# will produce log
w_uid: admin
ctx: request-uid
message:
  error: {message: Access denied.}
w_app_v: '2020.02'
w_cid: box-ui
w_app_fx: fetchUsers
w_m: post
```



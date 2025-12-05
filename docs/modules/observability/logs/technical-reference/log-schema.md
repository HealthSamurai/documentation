---
description: Configure and manage Aidbox logging with Log Schema for observability and monitoring.
---

# Log Schema

## General attributes

| Attribute    | Type        | Description                                                                   |
| ------------ | ----------- | ----------------------------------------------------------------------------- |
| **tn**       | string      | tenant id - that is box id                                                    |
| **ev**       | string      | log event name                                                                |
| **lvl**      | enum        | empty - info, warn, error, panic                                              |
| **ts**       | date string | event timestamp in ISO 8601 format                                            |
| **d**        | number      | duration in milliseconds                                                      |
| **w**        | string      | worker name                                                                   |
| **msg**      | string      | event description                                                             |
| **err**      | string      | error description                                                             |
| **etr**      | string      | exception stack trace                                                         |
| **ctx**      | string      | context id                                                                    |
| **ctx\_end** | bool        | flag last item in context, used to help aggregate log entries for one context |

## Event types

In table below you can see the most popular log events:

| event name (ev)                                                | description                                 |
| -------------------------------------------------------------- | ------------------------------------------- |
| **w/req**                                                      | HTTP request                                |
| **w/resp**                                                     | HTTP response                               |
| **db/q**                                                       | DB query                                    |
| **auth/authorized-access-policy**                              | Access Policy Evaluation                    |
| **resource/create**                                            | Resource created                            |
| **resource/update**                                            | Resource updated                            |
| **resource/delete**                                            | Resource deleted                            |
| **resource/create-dup**                                        | Create duplicate                            |
| **resource/update-dup**                                        | Update duplicate                            |
| **auth/login**                                                 | Succesful user login                        |
| **auth/authorize-failed**                                      | Failed login attempt                        |
| <p><strong>auth/warn</strong></p><p>err: wrong_credentials</p> | Failed login attempt due to bad credentials |
| **db/db-size**                                                 | Size of database                            |
| **db/rel-size**                                                | Size of table                               |

## REST logs

| Attribute       | type   | Description                                                                                                                                 |
| --------------- | ------ | ------------------------------------------------------------------------------------------------------------------------------------------- |
| **ev**          | string | **w/req** - start request, **w/resp** - end of request                                                                                      |
| **ctx**         | string | request id                                                                                                                                  |
| **w\_url**      | url    | request URL                                                                                                                                 |
| **w\_r**        | string | request URL template (i.e. GET /Patient/?)                                                                                                  |
| **w\_m**        | string | HTTP method in lower-case                                                                                                                   |
| **w\_qs**       | string | request query string                                                                                                                        |
| **w\_st**       | int    | HTTP response status                                                                                                                        |
| **w\_addr**     | string | Comma separated client and middleware IPs                                                                                                   |
| **w\_corr\_id** | string | X-Correlation-Id header of original request                                                                                                 |
| **w\_audit**    | json   | [X-Audit header](../../../../deprecated/deprecated/other/app-development-deprecated-tutorials/receive-logs-from-your-app/x-audit-header.md) |
| **d**           | int    | Request duration in ms                                                                                                                      |
| **w\_uid**      | string | User id                                                                                                                                     |
| **w\_cid**      | string | Client id                                                                                                                                   |

## SQL logs

| Attribute   | Type      | Description                                       |
| ----------- | --------- | ------------------------------------------------- |
| **ev**      | string    | **db/q** - for query and **db/ex** for statements |
| **sql**     | string    | SQL query                                         |
| **db\_prm** | string\[] | Collection of query parameters                    |
| **d**       | int       | Query duration in ms                              |

## Auth logs

| Attributes | Type   | Description |
| ---------- | ------ | ----------- |
| **ev**     | string | a/warn      |

## App logs

| Attributes | Type   | Description                          |
| ---------- | ------ | ------------------------------------ |
| **ev**     | string | **op/timeout** - operation timed out |
| **op**     | string | id of the operation                  |
| **app**    | string | id of the app                        |

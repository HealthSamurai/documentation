# Logs Schema

## General attributes

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

## Event types

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

## REST logs

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

## SQL logs

| Attribute  | Type      | Description                                       |
| ---------- | --------- | ------------------------------------------------- |
| **ev**     | string    | **db/q** - for query and **db/ex** for statements |
| **sql**    | string    | SQL query                                         |
| **db_prm** | string\[] | Collection of query parameters                    |
| **d**      | int       | Query duration in ms                              |

## Auth logs

| Attributes | Type   | Description |
| ---------- | ------ | ----------- |
| **ev**     | string | a/warn      |

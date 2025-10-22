# Extending Aidbox Logs

There few ways you may extend Aidbox logs:

* `X-Correlation-Id` to add correlation id.
* `X-Audit-Req-Body` to log request body,
* `X-Audit` to add custom attributes to logs,
* `POST /$loggy` to add your own log into Aidbox stream.

## X-Correlation-Id

You may send `X-Correlation-Id` header and Aidbox will propagate it to `w/req` & `w/resp` log events.

```yaml
# without X-Correlation-Id

GET /Patient

# in logs

{"ev": "w/req", "w_url": "/Patient", "w_m": "get", ...}
{"ev": "w/resp", "w_url": "/Patient", "w_m": "get", "w_st": 200, ...}


# with X-Correlation-Id

POST /Patient
X-Correlation-Id: some-uuid

# in logs

{"ev": "w/req", "w_url": "/Patient", "w_m": "get", "w_corr_id": "my-corr-id", ...}
{"ev": "w/resp", "w_url": "/Patient", "w_m": "get", "w_st": 200, "w_corr_id": "my-corr-id", ...}
```

## X-Audit-Req-Body

By default Aidbox doesn't log request body, because in most cases it's redudant. It may contain sensitve information (like passwords) or it may be pretty big and clog the log.

In case you want Aidbox to log request body (for example, [FHIR Search over POST](http://hl7.org/fhir/http.html#search)) you can provide `X-Audit-Req-Body: true` header in the request.

```yaml
# without X-Audit-Req-Body

POST /Patient/_search
Content-Type: application/x-www-form-urlencoded

name=John

# in logs

{"ev": "w/req", "w_url": "/Patient/_search", "w_m": "post", ...}
{"ev": "w/resp", "w_url": "/Patient/_search", "w_m": "post", "w_st": 200, ...}


# with X-Audit-Req-Body

POST /Patient/_search
Content-Type: application/x-www-form-urlencoded
X-Audit-Req-Body: true

name=John

# in logs
{"ev": "w/req", "w_url": "/Patient/_search", "w_m": "post", "w_b": "{\"name\":\"John\"}", ...}
{"ev": "w/resp", "w_url": "/Patient/_search", "w_m": "post", "w_st": 200, ...}
```

## X-Audit Header

If you want to add custom data to logs, you can use `X-Audit` header in the format `base64(json)` — all properties from this json will be merged with `w/req` and `w/resp` events.

```yaml
POST /Operation
X-Audit: eyJyb2xlIjogIm51cnNlIn0=
# base64({"role": "nurse"})

...body

# in logs
{ev: 'w/req', role: 'nurse', ...}
{ev: 'w/resp', role: 'nurse', ...}
```

## $loggy Endpoint

Aidbox provides `POST /$loggy` endpoint that accepts logs with the defined structure from your application. These logs are ingested into the elastic log. You can find examples below.

### $loggy Input Params

| Param name  | Required | Type   | Description                                                                                                                  |
| ----------- | -------- | ------ | ---------------------------------------------------------------------------------------------------------------------------- |
| **type**    | Yes      | string | It is a special keyword for your logs like "ui", "ui-error", "backend-crush"                                                 |
| **message** | Yes      | JSON   | It contains information that you want to log. It should be noted that we record your log data only from the message property |
| **v**       | No       | string | It means your app version.                                                                                                   |
| **fx**      | No       | string | fx or event on your side from which you want to log data or error.                                                           |

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

---
description: >-
  Aidbox provides /$loggy endpoint that accepts logs with the defined structure
  from your application. These logs are ingested into the elastic log. You can
  find examples below.
---

# Receive logs from your app

### Input params

| Param name | Required | Type | Description |
| :--- | :--- | :--- | :--- |
| **type** | Yes | string | It is a special keyword for your logs like "ui", "ui-error", "backend-crush" |
| **message** | Yes | JSON | It contains information that you want to log. It should be noted that we record your log data only from the message property |
| **v** | No | string | It means your app version.  |
| **fx** | No | string | fx or event on your side from which you want to log data or error.  |

### Example

#### Request example

```yaml
 POST /$loggy

 type: ui
 v: "2020.02"
 fx: "fetchUsers"
 message:
    error:
        message: "Access Denied."
```

#### In log example

```yaml
w_uid: admin
ctx: request-uid
message:
  error: {message: Access denied.}
w_app_v: '2020.02'
w_cid: box-ui
w_app_fx: fetchUsers
w_m: post
```

Aidbox logs structure is described [here](https://docs.aidbox.app/core-modules/logging-and-audit)


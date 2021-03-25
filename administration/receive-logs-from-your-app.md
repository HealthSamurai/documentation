---
description: >-
  We provide a special endpoint that accepts logs from your application in a
  special structure. This log will be applied to the elastic log. You can find
  examples below.
---

# Receive logs from your app

### Input params

* **type**: string - It is a special keyword for your logs like "ui", "ui-error", "backend-crush"
* **message**: any JSON -  which should contain information which you want to log. It should be noted that we record your log data only from the message property
* **v**: string - it means your app version. This parameter is optional
* **fx**: string - fx or event from you want log data or error. This parameter is optional

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


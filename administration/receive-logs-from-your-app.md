---
description: >-
  We provide a special endpoint that accepts logs from your application in a
  special structure. This log will be applied to the elastic log. Below you can
  find examples
---

# Receive logs from your app

### Input params

* **type**: string - It special keyword for you logs like "ui","ui-error","backend-crush"
* **message**: any JSON -  which should contain information which you want logging. An important note  we record your log data only from the message property
* **v**: string - it means your app version. This parameter optional
* **fx**: string - fx or event from you want log data or error. This parameter optional

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


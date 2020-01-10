---
description: Simple API to react on resource changes
---

# Changes API



By `GET /<resource-type>/$changes` without `version` parameter you will get latest version, which can be used to poll for changes by `GET /<resource-type>/$changes?version=<version>`

Polling request is cheap! If you want to watch rare changes \(minutes-hours\), this API is very resource efficient  \(no subscriptions, no queues\) and provide you a lot of control. If nothing has been changed - you will get  response with status `302`,  otherwise list of changes and new **version** to poll next time.

```yaml
---
GET /Patient/$changes

# status 200
version: 1

---
GET /Patient/$changes?version=1

# status 302 (not changed)

---
POST /Patient

id: pt-1

---
GET /Patient/$changes?version=1

# status 200
version: 2
changes:
- event: created
  resource: {id: pt-1}

```




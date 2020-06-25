---
description: Simple API to react on resource changes
---

# Changes API

By `GET /<resource-type>/$changes` without `version` parameter you will get latest version, which can be used to poll for changes by `GET /<resource-type>/$changes?version=<version>`

Polling request is cheap! If you want to watch rare changes \(minutes-hours\), this API is very resource efficient  \(no subscriptions, no queues\) and provide you a lots of control. If nothing has been changed - you will get  response with status `304`,  otherwise list of changes and new **version** to poll next time.

You can filter resources by equality. It's provided by params started with dot, e.g. `.name.0.family`.

```yaml
---
GET /Patient/$changes

# status 200
version: 1

---
GET /Patient/$changes?version=1

# status 304 (Not Modified)

---
POST /Patient

id: pt-1
name:
- family: Smith
  given: [John]

---
POST /Patient

id: pt-2
name:
- family: Wood
  given: [Amanda]

---
GET /Patient/$changes?version=1

# status 200
version: 3
changes:
- event: created
  resource:
    id: pt-1
    name:
    - family: Smith
      given: [John]
- event: created
  resource:
    id: pt-2
    name:
    - family: Wood
      given: [Amanda]

---
GET /Patient/$changes?version=1&.name.0.family=Wood

# status 200
version: 3
changes:
- event: created
  resource:
    id: pt-1
    name:
    - family: Smith
      given: [John]
```










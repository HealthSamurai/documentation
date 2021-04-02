---
description: Search by the resource id
---

# \_id

Search by the resource id

```yaml
GET /Patient?_id=pt-1
```

You can search by multiple ids separated by comma:

```text
GET /Patient?_id=pt-1,pt-2,pt-3
```

You can an use \_id parameter in a sort expressions:

```yaml
GET /Entity?_sort=_id

# 200
resourceType: Bundle
type: searchset
entry:
- resource:
    type: resource
    isMeta: true
    module: auth
    source: code
    resourceType: Entity
    id: AccessPolicy
    meta: {lastUpdated: '2019-07-30T16:51:52.763470Z', versionId: '0'}
....
```


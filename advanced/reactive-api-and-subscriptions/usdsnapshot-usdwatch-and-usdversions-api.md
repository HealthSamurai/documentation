---
description: Simple API to react on resource changes
---

# $snapshot, $changes & $versions API

If you want to **watch** changes \(creations, updates and deletion\) of specific resource type, you can use $snapshot, $changes and $versions API. This APIs are cheap and efficient.

```yaml
# get initial snapshot of data
GET /Patient/$snapshot

# ndjson response
# ....

# get changes
GET /Patient/$changes?\
  from=max(versionId)
  \&match={status: 'active'}
  \&response = http | stream | websocket

# stream of changes (ndjson, batch)

# persist latest versionids
PUT /$versions/myservice

{ Paitent: max(versionId) }

# get latest versionids
GET /$versions/myservice

#response
200
{ Patient: <latest-version> }


```


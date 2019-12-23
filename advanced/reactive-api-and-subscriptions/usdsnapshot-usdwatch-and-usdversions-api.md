---
description: Simple API to react on resource changes
---

# $snapshot, $watch & $versions API

If you want to **watch** changes \(creations, updates and deletion\) of specific resource type, you can use $snapshot, $watch and $offset API. This APIs are cheap and efficient.

```yaml
# get initial snapshot of data
GET /Patient/$snapshot

# ndjson response
# ....

# get changes
GET /Patient/$watch?\
  version=max(versionId)
  \&match={status: 'active'}
  \&response = http | stream
  \&format= ndjson | json

# stream of changes (ndjson, batch)

# persist latest versionids
PUT /$offsets/myservice

{ Paitent: max(versionId) }

# get latest versionids
GET /$offsets/myservice

#response
200
{ Patient: <latest-version> }


```


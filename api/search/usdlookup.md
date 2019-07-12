---
description: Efficient lookup for resources by key attributes
---

# $lookup



```yaml
GET /Patient/$lookup?\
  els=name.family,name.given,birthDate,identifier.value;address.city,address.line&\
  sort=name.family,name.given&\
  q=Joh+Do+1980&\
  count=50&\
  limit=200
```


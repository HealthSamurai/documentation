---
description: Paging search results
---

# \_count & \_page

Search results can contain many records, for more convenient work we can use pagination. Available parameters are: **\_count** — total records on the page \(default value — 100\),  **page** — specific page, output Bundle includes link section with **first**, **self**, **next**, **previous,** and **last** page constructed URLs.

```yaml
GET /Patient?_count=10&_page=3

# 200
resourceType: Bundle
type: searchset
entry:
- resource:
    ...
total: 206
link:
- {relation: first, url: '/Patient?_count=10&_page=1'}
- {relation: self, url: '/Patient?_count=10&_page=3'}
- {relation: next, url: '/Patient?_count=10&_page=4'}
- {relation: previous, url: '/Patient?_count=10&_page=2'}
- {relation: last, url: '/Patient?_count=10&_page=21'}
```


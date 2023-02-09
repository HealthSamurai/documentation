---
description: Sorting search results
---

# \_sort

### Supported search parameters types

* string
* uri
* number
* reference
* date
* token
* quantity
* \_id
* _\__lastUpdated
* \_createdAt
* \_score

Aidbox also supports sorting with [jsonb dot expressions](../.-expressions.md).

You can sort by multiple parameters:

```javascript
GET /Organization?_sort=name,id
```

### Sorting direction

You can change the sorting direction by prefixing parameter with `-` sign

```javascript
GET /Organization?_sort=-name
```

### Examples

```javascript
GET /Organization?_sort=_id
GET /Organization?_sort=-lastUpdated
// . expression
GET /Patient?_sort=.name.0.family
```

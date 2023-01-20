---
description: Sorting search results
---

# \_sort

We can sort results by search parameters or [dot expressions](../.-expressions.md).

```javascript
GET /Organization?_sort=name
```

You can sort by multiple parameters:

```javascript
GET /Organization?_sort=name,id
```

### Sorting direction

You can change the sorting direction by prefixing parameter with `-` sign

```javascript
GET /Organization?_sort=-name
```

### \_id & \_lastUpdated

You can use `_id and _lastUpdated` parameters:

```javascript
GET /Organization?_sort=_id
GET /Organization?_sort=-lastUpdated
```

### . expressions

[Dot expressions](../.-expressions.md) can be used in sorting:

```javascript
GET /Patient?_sort=.name.0.family
```

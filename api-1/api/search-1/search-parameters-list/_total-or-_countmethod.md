---
description: Calculate or not total count of query
---

# \_total or \_totalMethod

By default, for all search requests Aidbox returns the total number in the result which represents how many resources matched the criteria. But to do this, we run the second query for the count which takes some additional time. **Sometimes the count query is longer than your query**.

To get a response faster you can change this behavior using the **\_total** (or **\_totalMethod**) parameter. The **\_total** parameter can have the following values:

* `none` - do not run count query (fastest)
* `estimate` - roughly estimate number of results (fast)
* `accurate`- run accurate count (could be slow)

{% hint style="warning" %}
if you use `_total=none` you still get `total`when:

1. you don't use `_page`
2. the number of returned resources is less than [`_count`](_count-and-_page.md) (by default is 100).
{% endhint %}

### Settings default \_total

```
BOX_SEARCH_DEFAULT__PARAMS_TOTAL=<value>
```

Sets the default total search parameter value. `value` is one of: `none`, `estimate`, `accurate`.

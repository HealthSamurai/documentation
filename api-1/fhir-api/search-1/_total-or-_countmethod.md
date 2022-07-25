---
description: Calculate or not total count of query
---

# \_total or \_totalMethod

By default, for all search requests Aidbox returns the total number in the result which represents how many resources matched the criteria. But to do this, we run the second query for the count which takes some additional time. Sometimes the count query is longer than your major query. To get a response faster, especially on big amounts of data, you can change this behavior using the **\_total** (or **\_totalMethod**) parameter. The **\_total** parameter can have following values:

* `none` - do not run count query&#x20;
* `estimate` - roughly estimate number of results
* `accurate`- run accurate count

Default value for total method can be set with `box_search_default__params_total` env

{% code title="Example:" %}
```
box_search_default__params_total=none
```
{% endcode %}

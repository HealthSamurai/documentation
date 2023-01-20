---
description: Calculate or not total count of query
---

# \_total or \_totalMethod

By default, for all search requests Aidbox returns the total number in the result which represents how many resources matched the criteria. But to do this, we run the second query for the count which takes some additional time. Sometimes the count query is longer than your query.&#x20;

To get a response faster you can change this behavior using the **\_total** (or **\_totalMethod**) parameter. The **\_total** parameter can have the following values:

* `none` - do not run count query&#x20;
* `estimate` - roughly estimate number of results
* `accurate`- run accurate count

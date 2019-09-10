---
description: Calculate or not total count of query
---

# \_total or \_countMethod

By default, for all search requests Aidbox returns total number in result which represents how many resources matched the criteria. But to do this, we run second query for count which takes some additional  time.  Sometimes count query is longer than your major query. To get a response faster, especially on big amounts of data, you can change this behavior using the **\_total** \(or **countMethod**\) parameter. The **\_total** parameter can have following values:

* `none` - do not run count query 
* `estimated` - roughly estimate number of results
* `accurate` - run accurate count


---
description: Calculate or not total count of query
---

# \_total or \_totalMethod

By default, for all search requests Aidbox returns the total number in the result which represents how many resources matched the criteria. But to do this, we run the second query for the count which takes some additional time. Sometimes the count query is longer than your query.&#x20;

To get a response faster you can change this behavior using the **\_total** (or **\_totalMethod**) parameter. The **\_total** parameter can have the following values:

* `none` - do not run count query&#x20;
* `estimate` - roughly estimate number of results
* `accurate`- run accurate count

Default value for total method can be set with [box\_search\_default\_\_params\_total ](../../../reference/configuration/environment-variables/optional-environment-variables.md#optional-environment-variables)env

{% code title="Example:" %}
```
box_search_default__params_total=none
```
{% endcode %}



{% hint style="warning" %}
if you use [`box_search_default__params_total`](../../../reference/configuration/environment-variables/optional-environment-variables.md#optional-environment-variables)`=none` you still get `total`when:

1. &#x20; you don't use `_page`
2. &#x20;the number of returned resources is less than `_count` (by default is 100).
{% endhint %}

.

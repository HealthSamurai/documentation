---
description: Parameter to manage query timeout
---

# \_timeout

With **\_timeout** parameter, you can control the search query timeout in seconds. If the query takes more than the timeout value, it will be cancelled. The default timeout value is about one minute.

Timeout for each of include queries is 180 s and is not configurable.

Default timeout value can be set with `box_search_default__params_timeout` env

{% code title="Example" %}
```
box_search_default__params_timeout=120
```
{% endcode %}

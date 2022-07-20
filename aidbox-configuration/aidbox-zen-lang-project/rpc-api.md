---
description: RPC-API for Aidbox project
---

# RPC-API

`aidbox.zen` provides a set of operations for working with Aidbox projects.

### `aidbox.zen/reload-namespaces`

Reloads all zen namespaces in aidbox project. \
There is `AIDBOX_ZEN_DEV_MODE` variable to enable the hot reloading of the aidbox project. 
If you don't want to enable it, but you need to reload namespaces without restarting Aidbox, you can use `aidbox.zen/reload-namespaces`.

{% tabs %}
{% tab title="Parameters" %}
Expects no parameters
{% endtab %}

{% tab title="Result" %}
Returns the string "Success namespaces reload"
{% endtab %}

{% tab title="Error" %}
Returns error message
{% endtab %}
{% endtabs %}

# Aidbox project RPC reference

{% hint style="warning" %}
Since the 2405 release, using Aidbox in FHIR schema validation engine is recommended, which is incompatible with zen or Entity/Attribute options.

[Setup Aidbox with FHIR Schema validation engine](https://docs.aidbox.app/modules-1/profiling-and-validation/fhir-schema-validator/setup)
{% endhint %}



## RPC API

### `aidbox.zen/reload-namespaces`

Reloads all zen namespaces in aidbox project.\
There is `AIDBOX_ZEN_DEV_MODE` variable to enable the hot reloading of the aidbox project. If you don't want to enable it, but you need to reload namespaces without restarting Aidbox, you can use `aidbox.zen/reload-namespaces`.



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


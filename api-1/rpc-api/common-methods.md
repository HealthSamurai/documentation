# Common methods

{% hint style="info" %}
🚧 This section is still under active development check it out later. Please start [a discussion](https://github.com/Aidbox/Issues/discussions) or [contact](../../contact-us.md) us if you have questions, feedback, or suggestions.
{% endhint %}

#### Requesting Aidbox Version

Aidbox version is available over following `rpc` method:&#x20;

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: aidbox/version
```
{% endtab %}

{% tab title="Response" %}
{% code title="Status 200" %}
```yaml
result:
  version: '2204'
  channel: edge
  commit: f69019c0
  zen-fhir-version: 0.5.8
```
{% endcode %}
{% endtab %}
{% endtabs %}

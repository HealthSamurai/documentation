# Subscription API

{% hint style="warning" %}
Work in progress
{% endhint %}

### `POST /fhir/Subscription`

Creates a new R5 Subscription resource.

#### Params:

<table data-full-width="true"><thead><tr><th width="223">Parameter</th><th width="99">Type</th><th width="90" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>id</td><td>string</td><td>false</td><td><p>Identifier of the resource.</p><p><br><em>Default value: generated</em></p></td></tr><tr><td>topic</td><td>string</td><td>true</td><td><p>Url reference to subscription topic.</p><p><br><em>Example:</em> <br><em>http://aidbox.app/SubscriptionTopic/patient-test-1</em></p></td></tr><tr><td>reason</td><td>string</td><td>false</td><td>Human readable reason for Subscription.</td></tr><tr><td><strong>filterBy</strong></td><td>object[]</td><td>false</td><td>Filters applied to the resources that would be included in the notification. <br>When multiple filters are applied to one resource type, evaluates to true if all the conditions are met.</td></tr><tr><td><strong>filterBy</strong>.resourceType</td><td>string</td><td>false</td><td>Resource for this Subscription filter.<br><br><em>Example: Patient</em></td></tr><tr><td><strong>filterBy</strong>.filterParameter</td><td>string</td><td>false</td><td></td></tr><tr><td><strong>filterBy</strong>.value</td><td>string</td><td>false</td><td></td></tr><tr><td><strong>filterBy</strong>.comparator</td><td></td><td>false</td><td></td></tr><tr><td><strong>filterBy</strong>.modifier</td><td></td><td>false</td><td></td></tr><tr><td><strong>channelType</strong></td><td>object</td><td>false</td><td></td></tr><tr><td><strong>channelType</strong>.system</td><td></td><td>false</td><td></td></tr><tr><td><strong>channelType</strong>.code</td><td></td><td>false</td><td></td></tr><tr><td>endpoint</td><td></td><td>false</td><td></td></tr><tr><td>heartbeatPeriod</td><td></td><td>false</td><td></td></tr><tr><td><strong>parameter</strong></td><td>object[]</td><td>false</td><td></td></tr><tr><td><strong>parameter</strong>.name</td><td></td><td>false</td><td></td></tr><tr><td><strong>parameter</strong>.value</td><td></td><td>false</td><td></td></tr><tr><td>timeout</td><td></td><td>false</td><td></td></tr><tr><td>content</td><td></td><td>false</td><td></td></tr><tr><td>maxCount</td><td></td><td>false</td><td></td></tr><tr><td></td><td></td><td>false</td><td></td></tr></tbody></table>

#### Result:

<table><thead><tr><th width="136">Parameter</th><th width="92">Type</th><th width="503">Description</th></tr></thead><tbody><tr><td>resources</td><td>object[]</td><td>AidboxTask resources with <code>execId</code>.</td></tr></tbody></table>

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: awf.task/poll
params:
    taskDefinitions: [aidbox.bulk/import-resource-task]
```
{% endtab %}
{% endtabs %}

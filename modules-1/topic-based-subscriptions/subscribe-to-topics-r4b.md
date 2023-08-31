# Subscribe to Topics (R4B)

### Choose a topic

Use FHIR API to discover available topics

{% tabs %}
{% tab title="First Tab" %}

{% endtab %}

{% tab title="Second Tab" %}

{% endtab %}
{% endtabs %}

### Launch subscriber service

Aidbox expects a URL from a subscriber service&#x20;

### Create Subscription (R4B)

Create a Subscription resource with all the necessary attritutes

{% tabs %}
{% tab title="Request" %}

{% endtab %}

{% tab title="Response" %}

{% endtab %}
{% endtabs %}

As a result of this step Aidbox will try to perform a handshake with the subscriber service. By default Aidbox expects `Status:200`  response.

{% tabs %}
{% tab title="Request" %}

{% endtab %}

{% tab title="Response " %}
Status: active
{% endtab %}
{% endtabs %}

{% hint style="info" %}
&#x20;In case there's no reply from subscriber service 3 times Aidbox will mark the subscription as `error`
{% endhint %}

### Trigger a notification

To ensure the subscrtiption works as expected trigger a notification.

* Create an Observation resource using FHIR API

{% tabs %}
{% tab title="Request" %}

{% endtab %}

{% tab title="Response" %}

{% endtab %}
{% endtabs %}

* Check the notification is delivered
* Additionally you can request Subscription status using $status operation

{% tabs %}
{% tab title="Request" %}

{% endtab %}

{% tab title="Response" %}

{% endtab %}
{% endtabs %}

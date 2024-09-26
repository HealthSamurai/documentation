# Webhook AidboxTopicDestination

This page describes an AidboxTopicDestination, which allows sending events described by an AidboxSubscriptionTopic to a specific HTTP endpoint.

The webhook AidboxTopicDestination works in the following way:

* Aidbox stores events in the database within the same transaction as the CRUD operation.
* After the CRUD operation, Aidbox collects unsent messages (refer to the `maxEventNumberInBatch` parameter) from the database and sends them to the specified endpoint via a POST request.
* If an error occurs during sending, Aidbox will continue retrying until the message is successfully delivered.

{% content-ref url="./" %}
[.](./)
{% endcontent-ref %}

## Configuration

To use Webhook with [#aidboxsubscriptiontopic](./#aidboxsubscriptiontopic "mention") you have to create [#aidboxtopicdestination](./#aidboxtopicdestination "mention") resource.

You need to specify the following profile:

```
http://aidbox.app/StructureDefinition/aidboxtopicdestination-webhook-at-least-once
```

### Available Parameters

<table data-full-width="false"><thead><tr><th width="263">Parameter name</th><th width="161">Value type</th><th>Description</th></tr></thead><tbody><tr><td><code>endpoint</code> *</td><td>valueUrl</td><td>Webhook URL.</td></tr><tr><td><code>timeout</code></td><td>valueUnsignedInt</td><td>Timeout in seconds to attempt notification delivery (default: 30).</td></tr><tr><td><code>maxEventNumberInBatch</code></td><td>valueUnsignedInt</td><td>Maximum number of events that can be combined in a single notification (default: 20).</td></tr><tr><td><code>header</code></td><td>valueString</td><td>HTTP header for webhook request in the following format: <code>&#x3C;Name>: &#x3C;Value></code>. Zero or many.</td></tr></tbody></table>

\* required parameter.

## Examples

<pre><code><strong>POST /fhir/AidboxTopicDestination
</strong>content-type: application/json
accept: application/json

{
  "meta": {
    "profile": [
      "http://aidbox.app/StructureDefinition/aidboxtopicdestination-webhook-at-least-once"
    ]
  },
  "kind": "webhook-at-least-once",
  "id": "webhook-destination",
  "topic": "http://example.org/FHIR/R5/SubscriptionTopic/QuestionnaireResponse-topic",
  "parameter": [
    {
      "name": "endpoint",
      "valueUrl": "https://aidbox.requestcatcher.com/test"
    },
    {
      "name": "timeout",
      "valueUnsignedInt": 30
    },
    {
      "name": "maxEventNumberInBatch",
      "valueUnsignedInt": 20
    },
    {
      "name": "header",
      "valueString": "User-Agent: Aidbox Server"
    }
  ]
}
</code></pre>

## **Status Introspection**

Aidbox provides `$status` operation wich provides short status information of the integration status:

{% tabs %}
{% tab title="Request" %}
```yaml
GET /fhir/AidboxTopicDestination/<topic-destination-id>/$status
content-type: application/json
accept: application/json
```
{% endtab %}

{% tab title="Response" %}
{% code title="200 OK" %}
```json
{
 "start-time": "2024-09-26T12:47:44.655835Z",
 "status": "active",
 "successfully-delivered": 1,
 "enqueued-events-count": 0,
 "in-process": 0,
 "failed-delivery": 0,
 "failed-delivery-attempt": 0,
 "last-error": null
}
```
{% endcode %}
{% endtab %}
{% endtabs %}

Responce format:

<table data-full-width="false"><thead><tr><th width="188">Property</th><th width="128">Type</th><th>Description</th></tr></thead><tbody><tr><td><code>start-time</code></td><td>string</td><td><code>AidboxTopicDestination</code> start time in UTC.</td></tr><tr><td><code>status</code></td><td>string</td><td><code>AidboxTopicDestination</code> status is always <code>active</code>, which means that <code>AidboxTopicDestination</code> will try to send all received notifications.</td></tr><tr><td><code>in-process</code></td><td>number</td><td>Current number of events in the buffer being processed for delivery.</td></tr><tr><td><code>enqueued-events-count</code></td><td>number</td><td>Number of events pending in the queue for send.</td></tr><tr><td><code>successfully-delivered</code></td><td>number</td><td>Total number of events that have been successfully delivered.</td></tr><tr><td><code>failed-delivery</code></td><td>string</td><td>Total number of events that failed to be delivered. This count remains 0.</td></tr><tr><td><code>failed-delivery-attempt</code></td><td>Object</td><td><p>Number of delivery attempts that failed. </p><p>It represents the overall failed delivery attempts.</p></td></tr><tr><td><code>last-error</code></td><td>string</td><td>Error message of the last failed attempt to send event.</td></tr></tbody></table>

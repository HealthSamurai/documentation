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

<table data-full-width="false"><thead><tr><th width="260">Parameter name</th><th width="161">Value type</th><th>Description</th></tr></thead><tbody><tr><td><code>endpoint</code> *</td><td>valueUrl</td><td>Webhook URL.</td></tr><tr><td><code>timeout</code></td><td>valueUnsignedInt</td><td>Timeout in seconds to attempt notification delivery (default: 30).</td></tr><tr><td><code>keepAlive</code></td><td>valueInteger</td><td>the time in seconds that the host will allow an idle connection to remain open before it is closed (default: 120, <code>-1</code> - disable).</td></tr><tr><td><code>maxMessagesInBatch</code></td><td>valueUnsignedInt</td><td>Maximum number of events that can be combined in a single notification (default: 20).</td></tr><tr><td><code>header</code></td><td>valueString</td><td>HTTP header for webhook request in the following format: <code>&#x3C;Name>: &#x3C;Value></code>. Zero or many.</td></tr></tbody></table>

\* required parameter.

## Examples

<pre class="language-json"><code class="lang-json"><strong>POST /fhir/AidboxTopicDestination
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
      "name": "maxMessagesInBatch",
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
 "resourceType": "Parameters",
 "parameter": [
  {
   "valueDecimal": 2,
   "name": "messageBatchesDelivered"
  },
  {
   "valueDecimal": 0,
   "name": "messageBatchesDeliveryAttempts"
  },
  {
   "valueDecimal": 2,
   "name": "messagesDelivered"
  },
  {
   "valueDecimal": 0,
   "name": "messagesDeliveryAttempts"
  },
  {
   "valueDecimal": 0,
   "name": "messagesInProcess"
  },
  {
   "valueDecimal": 0,
   "name": "messagesQueued"
  },
  {
   "valueDateTime": "2024-10-03T07:23:00Z",
   "name": "startTimestamp"
  },
  {
   "valueString": "active",
   "name": "status"
  },
  {
   "name": "lastErrorDetail",
   "part": [
    {
     "valueString": "Connection refused",
     "name": "message"
    },
    {
     "valueDateTime": "2024-10-03T08:44:09Z",
     "name": "timestamp"
    }
   ]
  }
 ]
}
```
{% endcode %}
{% endtab %}
{% endtabs %}

Response format:

<table data-full-width="false"><thead><tr><th width="243">Property</th><th width="151">Type</th><th>Description</th></tr></thead><tbody><tr><td><code>messageBatchesDelivered</code></td><td>valueDecimal</td><td>Total number of batches that have been successfully delivered.</td></tr><tr><td><code>messageBatchesDeliveryAttempts</code></td><td>valueDecimal</td><td><p>Number of batch delivery attempts that failed. </p><p>It represents the overall failed delivery attempts.</p></td></tr><tr><td><code>messagesDelivered</code></td><td>valueDecimal</td><td>Total number of events that have been successfully delivered.</td></tr><tr><td><code>messagesDeliveryAttempts</code></td><td>valueDecimal</td><td><p>Number of delivery attempts that failed. </p><p>It represents the overall failed delivery attempts.</p></td></tr><tr><td><code>messagesInProcess</code></td><td>valueDecimal</td><td>Current number of events in the buffer being processed for delivery.</td></tr><tr><td><code>messagesQueued</code></td><td>valueDecimal</td><td>Number of events pending in the queue for send.</td></tr><tr><td><code>startTimestamp</code></td><td>valueDateTime</td><td><code>AidboxTopicDestination</code> start time in UTC.</td></tr><tr><td><code>status</code></td><td>valueString</td><td><code>AidboxTopicDestination</code> status is always <code>active</code>, which means that <code>AidboxTopicDestination</code> will try to send all received notifications.</td></tr><tr><td><code>lastErrorDetail</code></td><td>part</td><td>Information about errors of the latest failed attempt to send an event. This parameter can be repeated up to 5 times. Includes the following parameters.</td></tr><tr><td><p><code>lastErrorDetail</code></p><p><code>.message</code></p></td><td>valueString</td><td>Error message of the given error.</td></tr><tr><td><p><code>lastErrorDetail</code></p><p><code>.timestamp</code></p></td><td>valueDateTime</td><td>Timestamp of the given error.</td></tr></tbody></table>

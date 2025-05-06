# GCP Pub/Sub AidboxTopicDestination

{% hint style="info" %}
This functionality is available starting from version 2411 and requires [FHIR Schema](../../profiling-and-validation/fhir-schema-validator/) validation engine to be [enabled](../../profiling-and-validation/fhir-schema-validator/README.md).
{% endhint %}

The GCP Pub/Sub AidboxTopicDestination works in the following way:

* Aidbox stores events in the database within the same transaction as the CRUD operation.
* After the CRUD operation, Aidbox collects unsent messages from the database and sends them to the GCP Pub/Sub.
* If an error occurs during sending, Aidbox will continue retrying until the message is successfully delivered. So Aidbox guarantees at least once delivery for an event.

{% content-ref url="./" %}
[.](./)
{% endcontent-ref %}

## Configuration

To use Webhook with [#aidboxsubscriptiontopic](./#aidboxsubscriptiontopic "mention") you have to create [#aidboxtopicdestination](./#aidboxtopicdestination "mention") resource.

You need to specify the following profile:

```
http://aidbox.app/StructureDefinition/aidboxtopicdestination-gcp-pubsub-at-least-once
```

### Available Parameters

<table data-full-width="false"><thead><tr><th width="204">Parameter name</th><th width="192">Value type</th><th>Description</th></tr></thead><tbody><tr><td><code>projectId</code> *</td><td>valueString</td><td>GCP project identifier.</td></tr><tr><td><code>topicId</code> *</td><td>valueString</td><td>The name of the Pub/Sub topic to which you want to publish messages.</td></tr><tr><td><code>timeout</code></td><td>valueUnsignedInt</td><td>Set the delay threshold to use for batching.<br><br><strong>Default value (ms)</strong> - 1 </td></tr><tr><td><code>maxCount</code></td><td>valueUnsignedInt</td><td>Set the element count threshold to use for batching. After this many elements are accumulated, they will be wrapped up in a batch and sent. <br><br>Aidbox always generates one <a href="https://docs.aidbox.app/modules/topic-based-subscriptions/wip-dynamic-subscriptiontopic-with-destinations#notification-shape">notification</a> for each event. Therefore, you will receive as many notifications as there are events, but they will be sent in batches.<br><br><strong>Default value</strong> - 100</td></tr><tr><td><code>bytesThreshold</code></td><td>valueUnsignedInt</td><td>Set the request byte threshold to use for batching. After this many bytes are accumulated, the elements will be wrapped up in a batch and sent.<br><br><strong>Default value (bytes)</strong> - 1000</td></tr></tbody></table>

\* required parameter.

## Examples

<pre class="language-json"><code class="lang-json"><strong>POST /fhir/AidboxTopicDestination
</strong>content-type: application/json
accept: application/json

{
  "resourceType": "AidboxTopicDestination",
  "meta": {
    "profile": [
      "http://aidbox.app/StructureDefinition/aidboxtopicdestination-gcp-pubsub-at-least-once"
    ]
  },
  "kind": "gcp-pubsub-at-least-once",
  "id": "gcp-pub-sub-destination",
  "topic": "http://example.org/FHIR/R5/SubscriptionTopic/QuestionnaireResponse-topic",
  "parameter": [
    {
      "name": "projectId",
      "valueString": "project-id"
    },
    {
      "name": "topicId",
      "valueString": "topic-id"
    },
    {
      "name": "timeout",
      "valueUnsignedInt": 1000
    },
    {
      "name": "maxCount",
      "valueUnsignedInt": 4
    },
    {
      "name": "bytesThreshold",
      "valueUnsignedInt": 10000
    }
  ]
}
</code></pre>

## **Status Introspection**

Aidbox provides `$status` operation which provides short status information of the integration status:

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

<table data-full-width="false"><thead><tr><th width="243">Property</th><th width="151">Type</th><th>Description</th></tr></thead><tbody><tr><td><code>messagesDelivered</code></td><td>valueDecimal</td><td>Total number of events that have been successfully delivered.</td></tr><tr><td><code>messagesDeliveryAttempts</code></td><td>valueDecimal</td><td><p>Number of delivery attempts that failed. </p><p>It represents the overall failed delivery attempts.</p></td></tr><tr><td><code>messagesInProcess</code></td><td>valueDecimal</td><td>Current number of events in the buffer being processed for delivery.</td></tr><tr><td><code>messagesQueued</code></td><td>valueDecimal</td><td>Number of events pending in the queue for send.</td></tr><tr><td><code>startTimestamp</code></td><td>valueDateTime</td><td><code>AidboxTopicDestination</code> start time in UTC.</td></tr><tr><td><code>status</code></td><td>valueString</td><td><code>AidboxTopicDestination</code> status is always <code>active</code>, which means that <code>AidboxTopicDestination</code> will try to send all received notifications.</td></tr><tr><td><code>lastErrorDetail</code></td><td>part</td><td>Information about errors of the latest failed attempt to send an event. This parameter can be repeated up to 5 times. Includes the following parameters.</td></tr><tr><td><p><code>lastErrorDetail</code></p><p><code>.message</code></p></td><td>valueString</td><td>Error message of the given error.</td></tr><tr><td><p><code>lastErrorDetail</code></p><p><code>.timestamp</code></p></td><td>valueDateTime</td><td>Timestamp of the given error.</td></tr></tbody></table>

## Enable GCP Pub/Sub

To enable Aidbox to send messages to Pub/Sub, you must set up [GCP Application Default Credentials](https://cloud.google.com/docs/authentication/provide-credentials-adc) in the Aidbox environment.

### Running Pub/Sub Integration with a local emulator

Aidbox supports integrating GCP Pub/Sub with a local emulator for testing and development purposes. To set up this, specify the following environment variable in the Aidbox configuration:

```
BOX_SUBSCRIPTIONS_PUBSUB_EMULATOR__URL=<pub-sub-emulator-url>
```

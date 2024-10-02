# Kafka AidboxTopicDestination

This page describes an AidboxTopicDestination which allow to store events described by a AidboxSubscriptionTopic in Kafka.

Aibox provides two kinds of Kafka integrations:

1. _Best effort_: Aidbox stores events in the memory. In some cases (for example, in the event of an Aidbox crash or Kafka being unavailable), events can be lost.
2. At least once: Aidbox stores events in the database in the same transaction with a CRUD operation. Aidbox guarantees at least once delivery for an event.

Best effort incurs a lower performance cost than the at least once approach. Choose at least once if performance is not a concern for you.

{% content-ref url="./" %}
[.](./)
{% endcontent-ref %}

## Configuration

To use Kafka with [#aidboxsubscriptiontopic](./#aidboxsubscriptiontopic "mention") you have to create [#aidboxtopicdestination](./#aidboxtopicdestination "mention") resource.

There a two FHIR profiles available to use with Kafka:&#x20;

for best effort:&#x20;

```
http://aidbox.app/StructureDefinition/aidboxtopicdestination-kafka-best-effort
```

for _at least once_:

```
http://aidbox.app/StructureDefinition/aidboxtopicdestination-kafka-at-least-once
```

### Available Parameters

{% hint style="info" %}
For additional details see [Kafka Producer Configs Documentation](https://kafka.apache.org/documentation/#producerconfigs)
{% endhint %}

<table data-full-width="false"><thead><tr><th width="245">Parameter name</th><th width="136">Value type</th><th>Description</th></tr></thead><tbody><tr><td><code>kafkaTopic</code> *</td><td>valueString</td><td>The Kafka topic where the data should be sent.</td></tr><tr><td><code>bootstrapServers</code> *</td><td>valueString</td><td>Comma-separated string. Specifies the Kafka broker to connect to. Only one broker can be listed.</td></tr><tr><td><code>compressionType</code></td><td>valueString</td><td>Specify the final compression type for a given topic. This configuration accepts the standard compression codecs ('gzip', 'snappy', 'lz4', 'zstd').</td></tr><tr><td><code>batchSize</code></td><td>valueInteger</td><td>This configuration controls the default batch size in bytes.</td></tr><tr><td><code>deliveryTimeoutMs</code></td><td>valueInteger</td><td>A maximum time limit for reporting the success or failure of a record sent by a producer, covering delays before sending, waiting for broker acknowledgment, and handling retriable errors. </td></tr><tr><td><code>maxBlockMs</code></td><td>valueInteger</td><td>The configuration controls how long the <code>KafkaProducer</code>'s <code>send()</code>method will block. </td></tr><tr><td><code>maxRequestSize</code></td><td>valueInteger</td><td>The maximum size of a request in bytes.</td></tr><tr><td><code>requestTimeoutMs</code></td><td>valueInteger</td><td>The maximum amount of time the client will wait for the response of a request.</td></tr><tr><td><code>sslKeystoreKey</code></td><td>valueString</td><td>Private key in the format specified by 'ssl.keystore.type'.</td></tr><tr><td><code>securityProtocol</code></td><td>valueString</td><td>Protocol used to communicate with brokers.</td></tr><tr><td><code>saslMechanism</code></td><td>valueString</td><td>SASL mechanism used for client connections.</td></tr><tr><td><code>saslJaasConfig</code></td><td>valueString</td><td>JAAS login context parameters for SASL connections in the format used by JAAS configuration files.</td></tr><tr><td><code>saslClientCallbackHandlerClass</code></td><td>valueString</td><td>The fully qualified name of a SASL client callback handler class that implements the AuthenticateCallbackHandler interface.</td></tr></tbody></table>

\* required parameter.

## Examples

### Standalone Kafka (at least once)

Full example see on [Github](https://github.com/Aidbox/app-examples/tree/main/aidbox-subscriptions-to-kafka)

<pre><code><strong>POST /fhir/AidboxTopicDestination
</strong>content-type: application/json
accept: application/json

{
  "meta": {
    "profile": [
      "http://aidbox.app/StructureDefinition/aidboxtopicdestination-kafka-at-least-once"
    ]
  },
  "kind": "kafka-at-least-once",
  "id": "kafka-destination",
  "topic": "http://example.org/FHIR/R5/SubscriptionTopic/QuestionnaireResponse-topic",
  "parameter": [
    {
      "name": "kafkaTopic",
      "valueString": "aidbox-forms"
    },
    {
      "name": "bootstrapServers",
      "valueString": "kafka:29092"
    }
  ]
}
</code></pre>

### AWS MSK Kafka with IAM (best effort)

```
POST /fhir/AidboxTopicDestination
content-type: application/json
accept: application/json

{
  "meta": {
    "profile": [
      "http://aidbox.app/StructureDefinition/aidboxtopicdestination-kafka-best-effort"
    ]
  },
  "kind": "kafka-best-effort",
  "id": "kafka-destination",
  "topic": "http://example.org/FHIR/R5/SubscriptionTopic/QuestionnaireResponse-topic",
  "parameter": [
    {
      "name": "kafkaTopic",
      "valueString": "aidbox-forms"
    },
    {
      "name": "bootstrapServers",
      "valueString": "<...>.amazonaws.com:9098,<...>.amazonaws.com:9098"
    },
    {
      "name": "securityProtocol",
      "valueString": "SASL_SSL"
    },
    {
      "name": "saslMechanism",
      "valueString": "AWS_MSK_IAM"
    },
    {
      "name": "saslJaasConfig",
      "valueString": "software.amazon.msk.auth.iam.IAMLoginModule required;"
    },
    {
      "name": "saslClientCallbackHandlerClass",
      "valueString": "software.amazon.msk.auth.iam.IAMClientCallbackHandler"
    }
  ]
}
```



## **Status Introspection**

Aidbox provides `$status` operation which provides short status information on the integration status:

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
  "start-time": "2024-09-27T12:41:10.655293Z",
  "status": "active",
  "success-event-delivery": 0,
  "enqueued-events-count": 0,
  "event-in-process": 0,
  "fail-event-delivery": 0,
  "fail-event-delivery-attempt": 0,
  "last-error": "org.apache.kafka.common.errors.TimeoutException: Topic aidbox-forms not present in metadata after 10000 ms.",
}
```
{% endcode %}
{% endtab %}
{% endtabs %}

Response format:

<table data-full-width="false"><thead><tr><th width="188">Property</th><th width="128">Type</th><th>Description</th></tr></thead><tbody><tr><td><code>startTimestamp</code></td><td>string</td><td><code>AidboxTopicDestination</code> start time in UTC.</td></tr><tr><td><code>status</code></td><td>string</td><td><code>AidboxTopicDestination</code> status is always <code>active</code>, which means that <code>AidboxTopicDestination</code> will try to send all received notifications.</td></tr><tr><td><code>messagesDelivered</code></td><td>number</td><td>Total number of events that have been successfully delivered.</td></tr><tr><td><code>messagesQueued</code></td><td>number</td><td>Number of events pending in the queue for dispatch to the Kafka driver. This count remains <code>0</code> for the <em>best-effor</em> approach.</td></tr><tr><td><code>messagesInProcess</code></td><td>number</td><td>Current number of events in the buffer being processed for delivery.</td></tr><tr><td><code>messagesLost</code></td><td>string</td><td>Total number of events that failed to be delivered. This count remains <code>0</code> for the <em>at-least-once</em> approach.</td></tr><tr><td><code>messagesDeliveryAttempts</code></td><td>number</td><td><p> Number of delivery attempts that failed. </p><p>For the <em>best-effor</em> approach, this matches the <code>failed-delivery</code> count. </p><p>For the <em>at-least-once</em> approach, it represents the overall failed delivery attempts.</p></td></tr><tr><td><code>lastErrorDetail</code></td><td>string</td><td>Error message of the last failed attempt to send event.</td></tr></tbody></table>

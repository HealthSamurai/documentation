# Kafka TopicDestination

To proceed with Kafka integration please configure `AidboxSubscriptionTopic` module.

{% content-ref url="./" %}
[.](./)
{% endcontent-ref %}

### Behaviour Overview

1. By default, Aidbox buffers notifications for a short period before sending them to Kafka, in accordance with the configuration of the Kafka `TopicDestination` resource.
2. If Kafka is unavailable, Aidbox will buffer notifications according to the `delivery.timeout.ms` parameter.
   1. If the connection is restored, the buffered notifications will be sent.
   2. If a notification cannot be sent within the specified timeout, it will be lost.
3. These buffers are in-memory, so if Aidbox is restarted, the buffered data will be lost.
4. The status of successful or failed notifications can be monitored via Status Introspection.

### Kafka in Private Environment

Full example see on [Github](https://github.com/Aidbox/app-examples/tree/main/aidbox-subscriptions-to-kafka)

{% tabs %}
{% tab title="Request" %}
```json
POST /fhir/TopicDestination
content-type: application/json
accept: application/json

{
  "meta": {
    "profile": [
      "http://fhir.aidbox.app/StructureDefinition/TopicDestinationKafka"
    ]
  },
  "kind": "kafka",
  "id": "kafka-destination",
  "topic": "http://example.org/FHIR/R5/SubscriptionTopic/QuestionnaireResponse-topic",
  "parameter": [
    {
      "name": "kafkaTopic",
      "valueString": "aidbox-forms"
    },
    {
      "name": "bootstrap.servers",
      "valueString": "kafka:29092"
    }
  ]
}
```
{% endtab %}

{% tab title="Response" %}
{% code title="201 OK" %}
```json

{
  "meta": {
    "profile": [
      "http://fhir.aidbox.app/StructureDefinition/TopicDestinationKafka"
    ]
  },
  "kind": "kafka",
  "id": "kafka-destination",
  "topic": "http://example.org/FHIR/R5/SubscriptionTopic/QuestionnaireResponse-topic",
  "parameter": [
    {
      "name": "kafkaTopic",
      "valueString": "aidbox-forms"
    },
    {
      "name": "bootstrap.servers",
      "valueString": "kafka:29092"
    }
  ]
}
```
{% endcode %}
{% endtab %}
{% endtabs %}

### MSK Kafka in AWS with IAM

{% tabs %}
{% tab title="Request" %}
```json
POST /fhir/TopicDestination
content-type: application/json
accept: application/json

{
  "meta": {
    "profile": [
      "http://fhir.aidbox.app/StructureDefinition/TopicDestinationKafka"
    ]
  },
  "kind": "kafka",
  "id": "kafka-destination",
  "topic": "http://example.org/FHIR/R5/SubscriptionTopic/QuestionnaireResponse-topic",
  "parameter": [
    {
      "name": "kafkaTopic",
      "valueString": "aidbox-forms"
    },
    {
      "name": "bootstrap.servers",
      "valueString": "b-1.checkmsktbd.9szqwn.c16.kafka.us-east-1.amazonaws.com:9098,b-3.checkmsktbd.9szqwn.c16.kafka.us-east-1.amazonaws.com:9098,b-2.checkmsktbd.9szqwn.c16.kafka.us-east-1.amazonaws.com:9098"
    },
    {
      "name": "security.protocol",
      "valueString": "SASL_SSL"
    },
    {
      "name": "sasl.mechanism",
      "valueString": "AWS_MSK_IAM"
    },
    {
      "name": "sasl.jaas.config",
      "valueString": "software.amazon.msk.auth.iam.IAMLoginModule required;"
    },
    {
      "name": "sasl.client.callback.handler.class",
      "valueString": "software.amazon.msk.auth.iam.IAMClientCallbackHandler"
    }
  ]
}
```
{% endtab %}

{% tab title="Response" %}
201 OK

```json
{
 "id": "",
 "kind": "kafka",
 "meta": {
  "profile": [
   "http://fhir.aidbox.app/StructureDefinition/TopicDestinationKafka"
  ],
  "lastUpdated": "2024-08-30T07:50:26.494982Z",
  "versionId": "111",
  "extension": [
   {
    "url": "ex:createdAt",
    "valueInstant": "2024-08-30T07:50:26.494982Z"
   }
  ]
 },
 "topic": "http://example.org/FHIR/R5/SubscriptionTopic/QuestionnaireResponse-topic",
 "parameter": [
  {
   "name": "kafkaTopic",
   "valueString": "aidbox-forms"
  },
  {
   "name": "bootstrap.servers",
   "valueString": "b-1.checkmsktbd.9szqwn.c16.kafka.us-east-1.amazonaws.com:9098,b-3.checkmsktbd.9szqwn.c16.kafka.us-east-1.amazonaws.com:9098,b-2.checkmsktbd.9szqwn.c16.kafka.us-east-1.amazonaws.com:9098"
  },
  {
   "name": "security.protocol",
   "valueString": "SASL_SSL"
  },
  {
   "name": "sasl.mechanism",
   "valueString": "AWS_MSK_IAM"
  },
  {
   "name": "sasl.jaas.config",
   "valueString": "software.amazon.msk.auth.iam.IAMLoginModule required;"
  },
  {
   "name": "sasl.client.callback.handler.class",
   "valueString": "software.amazon.msk.auth.iam.IAMClientCallbackHandler"
  }
 ],
 "resourceType": "TopicDestination"
}
```
{% endtab %}
{% endtabs %}

### All Available Parameters

<table data-full-width="false"><thead><tr><th width="206">Parameter name</th><th width="135">Value type</th><th>Description</th></tr></thead><tbody><tr><td><code>kafkaTopic</code> *</td><td>valueString</td><td>The Kafka topic where the data should be sent.</td></tr><tr><td>bootstrap.servers *</td><td>valueString</td><td>Comma-separated string. Specifies the Kafka broker to connect to. Only one broker can be listed.</td></tr><tr><td>compression.type</td><td>valueString</td><td>Specify the final compression type for a given topic. This configuration accepts the standard compression codecs ('gzip', 'snappy', 'lz4', 'zstd').</td></tr><tr><td>batch.size</td><td>valueInteger</td><td>This configuration controls the default batch size in bytes.</td></tr><tr><td>delivery.timeout.ms</td><td>valueInteger</td><td>A maximum time limit for reporting the success or failure of a record sent by a producer, covering delays before sending, waiting for broker acknowledgment, and handling retriable errors. </td></tr><tr><td>max.block.ms</td><td>valueInteger</td><td>The configuration controls how long the <code>KafkaProducer</code>'s <code>send()</code>method will block. </td></tr><tr><td>max.request.size</td><td>valueInteger</td><td>The maximum size of a request in bytes.</td></tr><tr><td>request.timeout.ms</td><td>valueInteger</td><td>The maximum amount of time the client will wait for the response of a request.</td></tr><tr><td>ssl.keystore.key</td><td>valueString</td><td>Private key in the format specified by 'ssl.keystore.type'.</td></tr><tr><td>security.protocol</td><td>valueString</td><td>Protocol used to communicate with brokers.</td></tr><tr><td>sasl.mechanism</td><td>valueString</td><td>SASL mechanism used for client connections.</td></tr><tr><td>sasl.jaas.config</td><td>valueString</td><td>JAAS login context parameters for SASL connections in the format used by JAAS configuration files.</td></tr><tr><td><code>sasl.client.callback.handler.class</code></td><td>valueString</td><td>The fully qualified name of a SASL client callback handler class that implements the AuthenticateCallbackHandler interface.</td></tr></tbody></table>

\* required parameter.

{% hint style="info" %}
For additional details see [Kafka Producer Configs Documentation](https://kafka.apache.org/documentation/#producerconfigs)
{% endhint %}

### **Status Introspection**

{% tabs %}
{% tab title="Request" %}
```yaml
GET /fhir/TopicDestination/<topic-destination-id>/$status
content-type: application/json
accept: application/json
```
{% endtab %}

{% tab title="Response" %}
{% code title="200 OK" %}
```json
{
 "start-time": "2024-08-28T12:21:10.445306Z",
 "status": "active",
 "success-processed": 4,
 "in-process": 0,
 "fail-processed": 3,
 "last-error": "org.apache.kafka.common.errors.TimeoutException: Topic aidbox-forms not present in metadata after 10000 ms.",
 "kafka-metrics": {
  "batch-split-total": 0,
  "successful-authentication-rate": 0,
  "successful-authentication-total": 0,
  "connection-count": 1,
  "reauthentication-latency-max": "NaN",
  "record-send-rate": 0,
  "io-wait-time-ns-avg": 29975927777.333332,
  "failed-reauthentication-total": 0,
  "buffer-total-bytes": 33554432,
  "txn-commit-time-ns-total": 0,
  "io-wait-ratio": 1.1841328259243653,
  "select-rate": 0.039502791530601496,
  "io-time-ns-total": 1051582604,
  "record-retry-rate": 0,
  "txn-send-offsets-time-ns-total": 0,
  "record-size-max": "NaN",
  "connection-creation-rate": 0,
  "produce-throttle-time-max": "NaN",
  "record-size-avg": "NaN",
  "successful-reauthentication-total": 0,
  "failed-reauthentication-rate": 0,
  "batch-split-rate": 0,
  "request-latency-max": "NaN",
  "failed-authentication-total": 0,
  "request-total": 5,
  "response-rate": 0,
  "buffer-exhausted-total": 0,
  "connection-close-total": 1752,
  "record-error-total": 0,
  "failed-authentication-rate": 0,
  "successful-authentication-no-reauth-total": 0,
  "txn-begin-time-ns-total": 0,
  "bufferpool-wait-time-total": 0,
  "batch-size-avg": "NaN",
  "incoming-byte-total": 642,
  "network-io-total": 50,
  "io-ratio": 0.000005874575333807379,
  "compression-rate-avg": "NaN",
  "metadata-wait-time-ns-total": 0,
  "bufferpool-wait-ratio": 0,
  "record-error-rate": 0,
  "buffer-available-bytes": 33554432,
  "request-latency-avg": "NaN",
  "record-queue-time-max": "NaN",
  "commit-id": "771b9576b00ecf5b",
  "network-io-rate": 0,
  "batch-size-max": "NaN",
  "count": 112,
  "io-time-ns-avg": 148709,
  "txn-init-time-ns-total": 0,
  "requests-in-flight": 0,
  "flush-time-ns-total": 0,
  "select-total": 32214,
  "waiting-threads": 0,
  "byte-total": 6546,
  "incoming-byte-rate": 0,
  "outgoing-byte-rate": 0,
  "buffer-exhausted-rate": 0,
  "start-time-ms": 1724847670445,
  "response-total": 21,
  "version": "3.8.0",
  "record-retry-total": 0,
  "io-waittime-total": 6042608558702,
  "byte-rate": 0,
  "produce-throttle-time-avg": "NaN",
  "reauthentication-latency-avg": "NaN",
  "record-send-total": 4,
  "bufferpool-wait-time-ns-total": 0,
  "metadata-age": 105.946,
  "connection-creation-total": 5,
  "request-size-avg": "NaN",
  "record-queue-time-avg": "NaN",
  "iotime-total": 1051582604,
  "compression-rate": "NaN",
  "successful-reauthentication-rate": 0,
  "records-per-request-avg": "NaN",
  "connection-close-rate": 0,
  "io-wait-time-ns-total": 6042608558702,
  "outgoing-byte-total": 7356,
  "txn-abort-time-ns-total": 0,
  "request-size-max": "NaN",
  "request-rate": 0
 }
}
```
{% endcode %}
{% endtab %}
{% endtabs %}

Responce format:

<table data-full-width="false"><thead><tr><th width="188">Property</th><th width="128">Type</th><th>Description</th></tr></thead><tbody><tr><td><code>start-time</code></td><td>string</td><td><code>TopicDestination</code> start time in UTC.</td></tr><tr><td><code>status</code></td><td>string</td><td><code>TopicDestination</code> status is always <code>active</code>, which means that <code>TopicDestination</code> will try to send all received notifications.</td></tr><tr><td><code>in-process</code></td><td>number</td><td>Current number of events in the Kafka buffer being processed for delivery.</td></tr><tr><td><code>enqueued-events-count</code></td><td>number</td><td>Number of events pending in the queue for dispatch to the Kafka driver. This count remains 0 when the atLeastOneGuarantee is set to false.</td></tr><tr><td><code>successfully-delivered</code></td><td>number</td><td>Total number of events that have been successfully delivered.</td></tr><tr><td><code>failed-delivery</code></td><td>string</td><td>Total number of events that failed to be delivered. This count is always 0 when the atLeastOneGuarantee is true.</td></tr><tr><td><code>failed-delivery-attempt</code></td><td>Object</td><td> Number of delivery attempts that failed. When atLeastOneGuarantee is false, this matches the :failed-delivery count. When atLeastOneGuarantee is true, it represents the overall failed delivery attempts.</td></tr></tbody></table>

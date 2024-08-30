# AidboxSubscriptionTopic with destinations

{% hint style="danger" %}
**Feature Under Development**\
\
This feature is currently under development and will be available as a beta version in the upcoming release. Please note that this feature is subject to change and may contain incomplete or experimental functionality.
{% endhint %}

This Subscription feature allows users to subscribe to changes in FHIR resources and receive notifications sent to various destinations, such as Kafka.

## Key Components

1. **AidboxSubscriptionTopic** - a custom Aidbox resource modeled after the [FHIR R5 SubscriptionTopic](https://www.hl7.org/fhir/subscriptiontopic.html) resource. It defines the available subscription options and specifies the filters and notification shapes that can be used.
2. **TopicDestination** - custom Aidbox resource that is responsible for defining where and how the notifications triggered by a `AidboxSubscriptionTopic` should be sent. This resource provides the flexibility to specify different types of destinations.

## AidboxSubscriptionTopic

This resource describes data sources for Subscriptions. It allows to subscribe events in Aidbox and filter them by user-defined triggers. Triggers are defined under `AidboxSubscriptionTopic.resourceTrigger` property. All supported `resourceTrigger` properties are available in the table below:

<table data-full-width="true"><thead><tr><th width="257">Property</th><th width="91">Type</th><th>Description</th></tr></thead><tbody><tr><td><code>resource</code> *</td><td>uri</td><td>Resource (reference to definition) for this trigger definition. It is binding to <a href="https://www.hl7.org/fhir/valueset-all-resource-types.html">All Resource Types</a>.</td></tr><tr><td><code>fhirPathCriteria</code></td><td>string</td><td>FHIRPath based trigger rule. Only current resource state is allowed.</td></tr><tr><td><code>description</code></td><td>string</td><td>Text representation of the event trigger.</td></tr></tbody></table>

\* required property.

{% hint style="warning" %}
The current version of AidboxSubscriptionTopic resource does not support `eventTrigger`, `canFilterBy`,`notificationShape` capabilities of FHIR R5 SubscriptionTopic.
{% endhint %}

To create `AidboxSubscriptionTopic` resource use FHIR API:

```json
POST /fhir/AidboxSubscriptionTopic
content-type: application/json
accept: application/json

{
  "resourceType": "AidboxSubscriptionTopic",
  "id": "example",
  "url": "http://example.org/AidboxSubscriptionTopic/example",
  "status": "active",
  "description": "Example topic for completed QuestionnaireResponses",
  "resourceTrigger": [
    {
      "description": "An QuestionnaireResponse has been completed",
      "resource": "http://hl7.org/fhir/StructureDefinition/QuestionnaireResponse",
      "fhirPathCriteria": "status = 'completed'"
    }
  ]
}
```

## TopicDestination

It is a custom Aidbox resource that comes with profiles for different destinations. The`TopicDestination` has a link to a `AidboxSubscriptionTopic` to describe events that will be delivered. It is possible to create multiple `TopicDestination` linked to the same `AidboxSubscriptionTopic` in case you need to deliver events to different destinations.

**`TopicDestination` properties**:

<table data-full-width="true"><thead><tr><th width="188">Property</th><th width="128">Type</th><th>Description</th></tr></thead><tbody><tr><td><code>kind</code> *</td><td>code</td><td>Defines the destination for sending notifications.<br><code>Kafka</code> - the only possible value for now. Expected to be expanded.</td></tr><tr><td><code>topic</code> *</td><td>string</td><td>Url of <code>AidboxSubscriptionTopic</code> resource.</td></tr><tr><td><code>parameter</code> *</td><td><a href="https://www.hl7.org/fhir/parameters.html">FHIR parameters</a></td><td>Defines the destination parameters for sending notifications. Parameters are restricted by profiles for each destination.</td></tr><tr><td><code>status</code> </td><td>code</td><td><code>active</code> - the only possible value for now. Expected to be expanded.</td></tr></tbody></table>

\* required property.

To **start** processing subscription data **create** `TopicDestination` resource with reference to `AidboxSubscriptionTopic`. Examples of `TopicDestination` resources see in kind specific sections.

To **stop** processing subscription data **delete** `TopicDestination` resource.

### Kafka

To send notifications to `Kafka` create `TopicDestination` resource with `http://fhir.aidbox.app/StructureDefinition/TopicDestinationKafka` profile, kind `kafka`, and specify parameters.

TopicDestination parameters are [FHIR parameters](https://www.hl7.org/fhir/parameters.html).

All available parameters:

<table data-full-width="true"><thead><tr><th width="206">Parameter name</th><th width="135">Value type</th><th>Description</th></tr></thead><tbody><tr><td>kafkaTopic *</td><td>valueString</td><td>The Kafka topic where the data should be sent.</td></tr><tr><td>bootstrap.servers *</td><td>valueString</td><td>Comma-separated string. Specifies the Kafka broker to connect to. Only one broker can be listed.</td></tr><tr><td>compression.type</td><td>valueString</td><td>Specify the final compression type for a given topic. This configuration accepts the standard compression codecs ('gzip', 'snappy', 'lz4', 'zstd').</td></tr><tr><td>batch.size</td><td>valueInteger</td><td>This configuration controls the default batch size in bytes.</td></tr><tr><td>delivery.timeout.ms</td><td>valueInteger</td><td>A maximum time limit for reporting the success or failure of a record sent by a producer, covering delays before sending, waiting for broker acknowledgment, and handling retriable errors. </td></tr><tr><td>max.block.ms</td><td>valueInteger</td><td>The configuration controls how long the <code>KafkaProducer</code>'s <code>send()</code>method will block. </td></tr><tr><td>max.request.size</td><td>valueInteger</td><td>The maximum size of a request in bytes.</td></tr><tr><td>request.timeout.ms</td><td>valueInteger</td><td>The maximum amount of time the client will wait for the response of a request.</td></tr><tr><td>ssl.keystore.key</td><td>valueString</td><td>Private key in the format specified by 'ssl.keystore.type'.</td></tr><tr><td>security.protocol</td><td>valueString</td><td>Protocol used to communicate with brokers.</td></tr><tr><td>sasl.mechanism</td><td>valueString</td><td>SASL mechanism used for client connections.</td></tr><tr><td>sasl.jaas.config</td><td>valueString</td><td>JAAS login context parameters for SASL connections in the format used by JAAS configuration files.</td></tr><tr><td>sasl.client.callback.handler.class</td><td>valueString</td><td>The fully qualified name of a SASL client callback handler class that implements the AuthenticateCallbackHandler interface.</td></tr></tbody></table>

\* required parameter.

{% hint style="info" %}
For additional details see [Kafka Producer Configs Documentation](https://kafka.apache.org/documentation/#producerconfigs)
{% endhint %}

**TopicDestination Kafka behavior on Kafka connection errors (on the Aidbox start or during regular work):**

* Kafka disconnected.
* SubscriptionTopic produces a new event. The event is put into the buffer of the Kafka Producer.
  * Buffer size: `buffer.memory` (default: 33554432 bytes)
  * If the buffer is already full:
    * The side effect was performed (e.g. resource creation/update).
    * If Kafka Producer can't send data in time (`delivery.timeout.ms`) - the data will be lost.
  * If `delivery.timeout.ms` is exceeded, the event will be lost. The number of failed processes will increase. The last error will also be shown in the `$status` response.
  * If the connection is restored, the Kafka Producer will submit the data.

#### Example: Kafka in private local environment (full example see here: [Github](https://github.com/Aidbox/app-examples/tree/main/aidbox-subscriptions-to-kafka))

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

#### Example: MSK Kafka in AWS environment with IAM

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

**Status introspection**

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

## Notification Shape

Notification is a [FHIR Bundle](https://build.fhir.org/bundle.html) resource with `subscription-notification` type and resources that belong to the notification in the entry.

```json
{
  "resourceType": "Bundle",
  "type": "subscription-notification",
  "timestamp": "2024-08-28T11:10:13.675866Z",
  "entry": [
    {
      "resource": {
        "type": "event-notification",
        "topic": "http://example.org/FHIR/R5/SubscriptionTopic/QuestionnaireResponse-topic",
        "resourceType": "TopicDestinationStatus",
        "topic-destination": {
          "reference": "TopicDestination/kafka-destination"
        }
      }
    },
    {
      "resource": {
        "id": "3df44906-a578-4437-915c-f0c006838b2d",
        "item": [
          {
            "text": "ROS Defaults",
            "answer": [
              {
                "valueString": "sdfvzbdfgqearcxvbgadfgqwerdtasdf"
              }
            ],
            "linkId": "1"
          },
          {
            "text": "Constitutional ",
            "linkId": "2"
          }
        ],
        "meta": {
          "lastUpdated": "2024-08-28T11:10:13.673430Z",
          "versionId": "124",
          "extension": [
            {
              "url": "ex:createdAt",
              "valueInstant": "2024-08-28T11:09:51.431354Z"
            }
          ]
        },
        "status": "in-progress",
        "resourceType": "QuestionnaireResponse",
        "questionnaire": "http://forms.aidbox.io/questionnaire/ros|0.1.0"
      },
      "request": {
        "method": "POST",
        "url": "/fhir/Questionnaire/$process-response"
      }
    }
  ]
}
```

# \[WIP] Dynamic SubscriptionTopic with destinations

{% hint style="danger" %}
**Feature Under Development**\
\
This feature is currently under development and will be available as a beta version in the upcoming release. Please note that this feature is subject to change and may contain incomplete or experimental functionality.&#x20;
{% endhint %}

This Subscription feature allows users to subscribe to changes in FHIR resources and receive notifications sent to various destinations, such as Kafka.&#x20;

## Key Components

1. [**SubscriptionTopic**](https://www.hl7.org/fhir/subscriptiontopic.html) - FHIR resource that defines what is available for subscription, and specifies available filters and shapes of the notification.
2. **TopicDestination** - custom Aidbox resource that is responsible for defining where and how the notifications triggered by a `SubscriptionTopic` should be sent. This resource provides the flexibility to specify different types of destinations.

## SubscriptionTopic

The FHIR SubscriptionTopic resource is available since the R4B version. To make this feature work with FHIR R4 version Aidbox will import it from FHIR R5.

The current implementation does not support all `SubscriptionTopic.resourceTrigger` properties/capabilities.  All supported `SubscriptionTopic.resourceTrigger` properties are available in the table below:

<table data-full-width="true"><thead><tr><th width="257">Property</th><th width="91">Type</th><th>Description</th></tr></thead><tbody><tr><td><code>resource</code> *</td><td>uri</td><td>Resource (reference to definition) for this trigger definition. It is binding to <a href="https://www.hl7.org/fhir/valueset-all-resource-types.html">All Resource Types</a>.</td></tr><tr><td><code>supportedInteraction</code></td><td>code</td><td>create | update | delete</td></tr><tr><td><code>fhirPathCriteria</code></td><td>string</td><td>FHIRPath based trigger rule. Only current resource state is allowed.</td></tr><tr><td><code>description</code></td><td>string</td><td>Text representation of the event trigger.</td></tr></tbody></table>

\* required property.

{% hint style="warning" %}
The current beta version does not support `eventTrigger`, `canFilterBy`,`notificationShape` capabilities of SubscriptionTopic.
{% endhint %}

To create `SubscriptionTopic` resource use FHIR API:

```json
POST /fhir/SubscriptionTopic
content-type: application/json
accept: application/json

{
  "resourceType": "SubscriptionTopic",
  "id": "example",
  "url": "http://example.org/FHIR/R5/SubscriptionTopic/example",
  "status": "active",
  "description": "Example topic for completed encounters",
  "resourceTrigger": [
    {
      "description": "An Encounter has been completed",
      "resource": "http://hl7.org/fhir/StructureDefinition/Encounter",
      "supportedInteraction": [
        "update"
      ],
      "fhirPathCriteria": "(%current.status = 'completed')"
    }
  ]
}
```

## TopicDestination

It is a custom Aidbox resource that comes with profiles for different destinations. The`TopicDestination` has a link to a `SubscriptionTopic` to describe events that will be delivered. It is possible to create multiple `TopicDestination` linked to the same `SubscriptionTopic` in case you need to deliver events to different destinations.

**`TopicDestination` properties**:

<table data-full-width="true"><thead><tr><th width="188">Property</th><th width="128">Type</th><th>Description</th></tr></thead><tbody><tr><td><code>kind</code> *</td><td>code</td><td>Defines the destination for sending notifications.<br><code>Kafka</code> - the only possible value for now. Expected to be expanded.</td></tr><tr><td><code>topic</code> * </td><td>string</td><td>Url of <code>SubscriptionTopic</code> resource. </td></tr><tr><td><code>status</code> * </td><td>code</td><td><code>active</code> - the only possible value for now. Expected to be expanded.</td></tr><tr><td><code>parameter</code> * </td><td><a href="https://www.hl7.org/fhir/parameters.html">FHIR parameters</a></td><td>Defines the destination parameters for sending notifications. Parameters are restricted by profiles for each destination.</td></tr></tbody></table>

\* required property.

To **start** the subscription **create** `TopicDestination` resource with `active` status and reference to `SubscriptionTopic`.

To **stop** the subscription **delete** `TopicDestination` resource.

### TopicDestionation API

**Create**

{% tabs %}
{% tab title="Request" %}
```json
POST /fhir/TopicDestination
content-type: application/json
accept: application/json

{
  "resourceType": "TopicDestination",
  "meta": {
    "profile": [
      "http://aidbox.app/TopicDestination/Kafka|0.0.1"
    ]
  },
  "kind": "Kafka",
  "topic": "http://example.org/FHIR/R5/SubscriptionTopic/example",
  "status": "active",
  "parameter": [
    {
      "name": "bootstrapServer",
      "valueString": "kafka-broker1:9092"
    },
    {
      "name": "authToken",
      "valueString": "eY...your-auth-token-here"
    },
    {
      "name": "kafkaTopic",
      "valueString": "patient-topic"
    },
    {
      "name": "username",
      "valueString": "your-kafka-username"
    },
    {
      "name": "password",
      "valueString": "your-kafka-password"
    },
    {
      "name": "retries",
      "valueInteger": 5
    }
  ]
}
```
{% endtab %}

{% tab title="Response" %}
{% code title="201 OK" %}
```json

{
  "id": "topic-destination-id",
  "meta": {
    "profile": ["http://aidbox.app/TopicDestination/Kafka|0.0.1"]
  },
  "resourceType": "TopicDestination",
  "kind": "Kafka",
  "topic": "http://example.org/FHIR/R5/SubscriptionTopic/example",
  "status": "active",
  "parameter": [
    {
      "name": "bootstrapServer",
      "valueString": "kafka-broker1:9092"
    },
    {
      "name": "authToken",
      "valueString": "eY...your-auth-token-here"
    },
    {
      "name": "kafkaTopic",
      "valueString": "patient-topic"
    },
    {
      "name": "username",
      "valueString": "your-kafka-username"
    },
    {
      "name": "password",
      "valueString": "your-kafka-password"
    },
    {
      "name": "retries",
      "valueInteger": 5
    }
  ]
}
```
{% endcode %}
{% endtab %}
{% endtabs %}

**Read**

{% tabs %}
{% tab title="Request" %}
```yaml
GET /fhir/TopicDestination/<topic-destination-id>
content-type: application/json
accept: application/json
```
{% endtab %}

{% tab title="Response" %}
{% code title="200 OK" %}
```json
{
  "resourceType": "TopicDestination",
  "meta": {
    "profile": ["http://aidbox.app/TopicDestination/Kafka|0.0.1"]
  },
  "kind": "Kafka",
  "topic": "http://example.org/FHIR/R5/SubscriptionTopic/example",
  "status": "active",
   "parameter": [
    {
      "name": "bootstrapServer",
      "valueString": "kafka-broker1:9092"
    },
    {
      "name": "authToken",
      "valueString": "eY...your-auth-token-here"
    },
    {
      "name": "kafkaTopic",
      "valueString": "patient-topic"
    },
    {
      "name": "username",
      "valueString": "your-kafka-username"
    },
    {
      "name": "password",
      "valueString": "your-kafka-password"
    },
    {
      "name": "retries",
      "valueInteger": 5
    }
  ]
}
```
{% endcode %}
{% endtab %}
{% endtabs %}

**Search**

{% tabs %}
{% tab title="Request" %}
```
GET /fhir/TopicDestination
content-type: application/json
accept: application/json
```
{% endtab %}

{% tab title="Response" %}
{% code title="200 OK" %}
```json
{
  "resourceType": "Bundle",
  "type": "searchset",
  "total": 1,
  "entry": [
    {
      "resourceType": "TopicDestination",
      "meta": {
        "profile": [
          "http://aidbox.app/TopicDestination/Kafka|0.0.1"
        ]
      },
      "kind": "Kafka",
      "topic": "http://example.org/FHIR/R5/SubscriptionTopic/example",
      "status": "active",
      "parameter": [
        {
          "name": "bootstrapServer",
          "valueString": "kafka-broker1:9092"
        },
        {
          "name": "authToken",
          "valueString": "eY...your-auth-token-here"
        },
        {
          "name": "kafkaTopic",
          "valueString": "patient-topic"
        },
        {
          "name": "username",
          "valueString": "your-kafka-username"
        },
        {
          "name": "password",
          "valueString": "your-kafka-password"
        },
        {
          "name": "retries",
          "valueInteger": 5
        }
      ]
    }
  ]
}
```
{% endcode %}
{% endtab %}
{% endtabs %}

**Delete**

{% tabs %}
{% tab title="Request" %}
```yaml
DELETE /fhir/TopicDestination/<topic-destination-id>
content-type: application/json
accept: application/json
```
{% endtab %}

{% tab title="Response" %}
{% code title="200 OK" %}
```json
{
  "resourceType": "TopicDestination",
  "meta": {
    "profile": ["http://aidbox.app/TopicDestination/Kafka|0.0.1"]
  },
  "kind": "Kafka",
  "topic": "http://example.org/FHIR/R5/SubscriptionTopic/example",
  "status": "active",
  "parameter": [
    {
      "name": "bootstrapServer",
      "valueString": "kafka-broker1:9092"
    },
    {
      "name": "authToken",
      "valueString": "eY...your-auth-token-here"
    },
    {
      "name": "kafkaTopic",
      "valueString": "patient-topic"
    },
    {
      "name": "username",
      "valueString": "your-kafka-username"
    },
    {
      "name": "password",
      "valueString": "your-kafka-password"
    },
    {
      "name": "retries",
      "valueInteger": 5
    }
  ]
}
```
{% endcode %}
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
  "kind": "Kafka",
  "status": "running",
  "queueSize": 100,
  "sentCount": 888, 
  "failedCount": 9
}
```
{% endcode %}
{% endtab %}
{% endtabs %}

### Kafka

To send notifications to `Kafka` create `TopicDestination` resource with `http://aidbox.app/TopicDestination/Kafka|0.0.1` profile, kind `Kafka`, and specify parameters.

TopicDestination parameters are [FHIR parameters](https://www.hl7.org/fhir/parameters.html).&#x20;

All available parameters:

<table data-full-width="true"><thead><tr><th width="188">Parameter</th><th width="128">Type</th><th>Description</th></tr></thead><tbody><tr><td>bootstrapServer *</td><td>valueString</td><td>Specifies the Kafka broker to connect to. Only one broker can be listed.</td></tr><tr><td>kafkaTopic * </td><td>valueString</td><td>The Kafka topic where the data should be sent.</td></tr><tr><td>authToken </td><td>valueString</td><td>A token for authentication if your Kafka setup requires one.</td></tr><tr><td>username </td><td>valueString</td><td>Your Kafka username.</td></tr><tr><td>password</td><td>valueString</td><td>Your Kafka password.</td></tr><tr><td>retries</td><td>valueInteger</td><td>Number of times to retry sending the message in case of a failure.</td></tr></tbody></table>

\* required parameter.

- kafkaTopic
- Kafka Producer Settings (for additional details see [Kafka Producer Configs Documantation](https://kafka.apache.org/documentation/#producerconfigs)):
    - bootstrap.servers (comma separated string)
    - compression.type
    - batch.size
    - delivery.timeout.ms
    - max.block.ms
    - max.request.size
    - request.timeout.ms  
    - ssl.keystore.key

**TopicDestinationKafka behavior on Kafka connection errors (on the Aidbox start or during regular work):**

- Kafka disconnected.
- SubscriptionTopic produces a new event. The event is put into the buffer of the Kafka Producer.
    - Buffer size: `buffer.memory` (default: 33554432 bytes)
    - If the buffer is already full, Kafka sending starts to work synchronously with the CRUD request:
        - The CRUD request will freeze for `delivery.timeout.ms`;
        - The CRUD request will fail when it reaches the timeout;
        - The side effect was performed.
- If the connection is restored, the Kafka Producer will submit the data.
- If `delivery.timeout.ms` is exceeded, the event will be lost. The number of failed processes will increase. The last error will also be shown in the `$status` response.

Example (full example see here: [Github](https://github.com/Aidbox/app-examples/tree/main/aidbox-forms-and-kafka-topic-destination)):

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

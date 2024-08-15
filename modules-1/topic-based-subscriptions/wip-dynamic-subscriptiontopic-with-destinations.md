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

## Notification Shape

Notification is a [FHIR Bundle](https://build.fhir.org/bundle.html) resource with `subscription-notification` type and resources that belong to the notification in the entry.

```json
{
  "resourceType": "Bundle",
  "id": "00b99077-2bda-436e-98cc-a4f65d6c2fe0",
  "type": "subscription-notification",
  "timestamp": "2020-04-17T10:24:13.1882432-05:00",
  "entry": [
    {
      "fullUrl": "https://example.org/FHIR/R5/Encounter/2",
      "resource": {
        "resourceType": "Encounter",
        "id": "2",
        "status": "in-progress",
        "class": [
          {
            "coding": [
              {
                "system": "http://terminology.hl7.org/CodeSystem/v3-ActCode",
                "code": "VR",
                "display": "virtual"
              }
            ]
          }
        ],
        "subject": {
          "reference": "Patient/ABC"
        }
      },
      "request": {
        "method": "PUT",
        "url": "Encounter/2"
      },
      "response": {
        "status": "201"
      }
    }
  ]
}
```

## Destinations

### Kafka

To send notifications to `Kafka` create `TopicDestination` resource with `http://aidbox.app/TopicDestination/Kafka|0.0.1` profile, kind `Kafka`, and specify parameters.

TopicDestination parameters are [FHIR parameters](https://www.hl7.org/fhir/parameters.html).&#x20;

All available parameters:

<table data-full-width="true"><thead><tr><th width="188">Parameter</th><th width="128">Type</th><th>Description</th></tr></thead><tbody><tr><td>bootstrapServer *</td><td>valueString</td><td>Specifies the Kafka broker to connect to. Only one broker can be listed.</td></tr><tr><td>kafkaTopic * </td><td>valueString</td><td>The Kafka topic where the data should be sent.</td></tr><tr><td>authToken </td><td>valueString</td><td>A token for authentication if your Kafka setup requires one.</td></tr><tr><td>username </td><td>valueString</td><td>Your Kafka username.</td></tr><tr><td>password</td><td>valueString</td><td>Your Kafka password.</td></tr><tr><td>retries</td><td>valueInteger</td><td>Number of times to retry sending the message in case of a failure.</td></tr></tbody></table>

\* required parameter.

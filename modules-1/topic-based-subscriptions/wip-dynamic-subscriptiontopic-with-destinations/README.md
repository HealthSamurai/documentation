# Aidbox topic-based subscriptions

{% hint style="danger" %}
This functionality was introduced in the 2408 release and is available in Beta. Please note that this feature is subject to change and may contain incomplete or experimental functionality.
{% endhint %}

### Overview

This feature enables dynamic subscriptions to changes in FHIR resources, allowing users/systems to receive notifications through multiple channels, including Kafka.

<figure><img src="../../../.gitbook/assets/Screenshot 2024-09-13 at 11.14.30.png" alt=""><figcaption></figcaption></figure>

For an application example, refer to [Aidbox Subscriptions & Kafka AidboxTopicDestination](https://github.com/Aidbox/app-examples/tree/main/aidbox-subscriptions-to-kafka)

## Key Components

* **`AidboxSubscriptionTopic`** is a custom Aidbox resource modeled after the [FHIR R6 SubscriptionTopic](https://build.fhir.org/subscriptiontopic.html) resource. The resource allows defining a set of events that clients can subscribe to, such as changes in specific resources.&#x20;

{% hint style="warning" %}
**FHIR Compliance:**&#x20;

* **For FHIR R4**: Use the `AidboxSubscriptionTopic` as it is, since FHIR does not include this functionality in R4.
* **For FHIR R4b, R5, and R6**: You can use either the `AidboxSubscriptionTopic` or the FHIR SubscriptionTopic resource, depending on the FHIR version used. These version-specific resources will be added in the upcoming releases.&#x20;
{% endhint %}

* **`AidboxTopicDestination`** is a custom Aidbox resource that defines where and how the notifications triggered by an `AidboxSubscriptionTopic` should be sent. This resource offers flexibility in specifying various types of destinations. And is considered as a system configuration resource.

## AidboxSubscriptionTopic

The `AidboxSubscriptionTopic` resource describes the data sources for subscriptions. It allows clients to subscribe to events in Aidbox and filter them using user-defined triggers, which are specified by the `trigger` element. Supported properties:

<table data-full-width="false"><thead><tr><th width="257">Property</th><th width="91">Type</th><th>Description</th></tr></thead><tbody><tr><td><code>resource</code> *</td><td>uri</td><td>Resource (reference to definition) for this trigger definition. It is binding to <a href="https://www.hl7.org/fhir/valueset-all-resource-types.html">All Resource Types</a>.</td></tr><tr><td><code>supportedInteraction</code></td><td>code</td><td>create | update | delete</td></tr><tr><td><code>fhirPathCriteria</code></td><td>string</td><td>FHIRPath based trigger rule. Only current resource state is allowed.</td></tr><tr><td><code>description</code></td><td>string</td><td>Text representation of the event trigger.</td></tr></tbody></table>

\* required.

#### Create `AidboxSubscriptionTopic` resource

```json
POST /fhir/AidboxSubscriptionTopic
content-type: application/json
accept: application/json

{
  "resourceType": "AidboxSubscriptionTopic",
  "url": "http://example.org/FHIR/R5/SubscriptionTopic/QuestionnaireResponse-topic",
  "status": "active",
  "trigger": [
    {
      "resource": "QuestionnaireResponse",
      "fhirPathCriteria": "status = 'completed' or status = 'amended'"
    }
  ]
}
```

## AidboxTopicDestination

The `AidboxTopicDestination` resource is used to define channel configurations for processing subscription data.

#### Create a TopicDestination

To start processing subscription data, create a `AidboxTopicDestination` resource with a reference to the `AidboxSubscriptionTopic`. Examples of `AidboxTopicDestination` resources can be found in kind-specific sections.

#### Stop subscription data processing

To stop processing subscription data, delete the `AidboxTopicDestination` resource.

#### AidboxTopicDestination Profile

Ensure that the resource metadata contains the kind-specific `AidboxTopicDestination` profile.

#### **Elements**

<table data-full-width="false"><thead><tr><th width="188">Property</th><th width="128">Type</th><th>Description</th></tr></thead><tbody><tr><td><code>status</code> </td><td>code</td><td><code>active</code> - the only possible value for now. Expected to be expanded.</td></tr><tr><td><code>topic</code> *</td><td>string</td><td>Url of <code>AidboxSubscriptionTopic</code> resource.</td></tr><tr><td><code>kind</code> *</td><td>code</td><td>Defines the destination for sending notifications. Supported values: <code>kafka-at-least-once</code>, <code>kafka-best-effort</code>, <code>webhook-at-least-once</code>.</td></tr><tr><td><code>parameter</code> *</td><td><a href="https://www.hl7.org/fhir/parameters.html">FHIR parameters</a></td><td>Defines the destination parameters for sending notifications. Parameters are restricted by profiles for each destination.</td></tr></tbody></table>

\* required.

#### Currently supported channels

{% content-ref url="kafka-topicdestination.md" %}
[kafka-topicdestination.md](kafka-topicdestination.md)
{% endcontent-ref %}

{% content-ref url="webhook-aidboxtopicdestination.md" %}
[webhook-aidboxtopicdestination.md](webhook-aidboxtopicdestination.md)
{% endcontent-ref %}

## Notification Shape

Notification is a [FHIR Bundle](https://build.fhir.org/bundle.html) resource with `subscription-notification` type, containing relevant resources in its entries.

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
        "resourceType": "AidboxTopicDestinationStatus",
        "topic-destination": {
          "reference": "AidboxTopicDestination/kafka-destination"
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
        "url": "/fhir/Questionnaire"
      }
    }
  ]
}
```

# AidboxSubscriptionTopic

{% hint style="danger" %}
**This feature was introduced in 2408 release and currently available in Beta.**\
\
Please note that this feature is subject to change and may contain incomplete or experimental functionality.
{% endhint %}

This Subscription feature allows users to subscribe to changes in FHIR resources and receive notifications in different channels like Kafka.

<figure><img src="../../../.gitbook/assets/image (107).png" alt=""><figcaption></figcaption></figure>

See application example here: [Aidbox Subscriptions & Kafka TopicDestination](https://github.com/Aidbox/app-examples/tree/main/aidbox-subscriptions-to-kafka)

## Key Components

1. **`AidboxSubscriptionTopic`** - a custom Aidbox resource modelled after the [FHIR R6 SubscriptionTopic](https://build.fhir.org/subscriptiontopic.html) resource. These resources describe what notifications should be propagated to external systems. It defines the available subscription options and specifies the filters and notification shapes that can be used.
2. **`TopicDestination`** - custom Aidbox resource that is responsible for defining where and how the notifications triggered by a `AidboxSubscriptionTopic` should be sent. This resource provides the flexibility to specify different types of destinations.

{% hint style="warning" %}
**FHIR Compliance:** The `AidboxSubscriptionTopic` resource mirrors the structure of the FHIR `SubscriptionTopic` in version R6, supporting the features detailed below.

* For FHIR R4: Use the `AidboxSubscriptionTopic` as is, since FHIR does not provide this feature in R4.
* For FHIR R4b, R5, and R6: Use either the `AidboxSubscriptionTopic` or the FHIR `SubscriptionTopic` resource, depending on the FHIR version being used (support for the last is coming soon).
{% endhint %}

## AidboxSubscriptionTopic

This resource describes data sources for Subscriptions. It allows to subscribe events in Aidbox and filter them by user-defined triggers. Triggers are defined by `resourceTrigger` element. All supported `resourceTrigger` properties:

<table data-full-width="false"><thead><tr><th width="257">Property</th><th width="91">Type</th><th>Description</th></tr></thead><tbody><tr><td><code>resource</code> *</td><td>uri</td><td>Resource (reference to definition) for this trigger definition. It is binding to <a href="https://www.hl7.org/fhir/valueset-all-resource-types.html">All Resource Types</a>.</td></tr><tr><td><code>fhirPathCriteria</code></td><td>string</td><td>FHIRPath based trigger rule. Only current resource state is allowed.</td></tr><tr><td><code>description</code></td><td>string</td><td>Text representation of the event trigger.</td></tr></tbody></table>

\* required property.

### Create `AidboxSubscriptionTopic` resource

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
      "resource": "http://hl7.org/fhir/StructureDefinition/QuestionnaireResponse",
      "fhirPathCriteria": "status = 'completed'",
      "description": "An QuestionnaireResponse has been completed"
    }
  ]
}
```

## TopicDestination

Use TopicDestination resource to define channel configurations.

To **start** processing subscription data **create** `TopicDestination` resource with reference to `AidboxSubscriptionTopic`. Examples of `TopicDestination` resources see in kind specific sections.

To **stop** processing subscription data **delete** `TopicDestination` resource.

### **Elements**

<table data-full-width="false"><thead><tr><th width="188">Property</th><th width="128">Type</th><th>Description</th></tr></thead><tbody><tr><td><code>status</code> </td><td>code</td><td><code>active</code> - the only possible value for now. Expected to be expanded.</td></tr><tr><td><code>topic</code> *</td><td>string</td><td>Url of <code>AidboxSubscriptionTopic</code> resource.</td></tr><tr><td><code>kind</code> *</td><td>code</td><td>Defines the destination for sending notifications.<br><code>Kafka</code> - the only possible value for now. Expected to be expanded.</td></tr><tr><td><code>parameter</code> *</td><td><a href="https://www.hl7.org/fhir/parameters.html">FHIR parameters</a></td><td>Defines the destination parameters for sending notifications. Parameters are restricted by profiles for each destination.</td></tr></tbody></table>

\* required property.

### Currently supported channels:

{% content-ref url="kafka-topicdestination.md" %}
[kafka-topicdestination.md](kafka-topicdestination.md)
{% endcontent-ref %}

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

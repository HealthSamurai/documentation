# Aidbox topic-based subscriptions

{% hint style="info" %}
This functionality is available in Aidbox versions 2409 and later and requires [FHIR Schema](../../profiling-and-validation/fhir-schema-validator/) validation engine to be [enabled](../../profiling-and-validation/fhir-schema-validator/).
{% endhint %}

### Overview

This feature enables dynamic subscriptions to changes in FHIR resources, allowing users/systems to receive notifications through multiple channels, including Kafka.

<figure><img src="../../../../.gitbook/assets/eb0e6bfe-79a6-4f5e-bb16-1913eb0106d0.png" alt=""><figcaption></figcaption></figure>

For an application example, refer to [Aidbox Subscriptions & Kafka AidboxTopicDestination](https://github.com/Aidbox/app-examples/tree/main/aidbox-subscriptions-to-kafka)

## Key Components

* **`AidboxSubscriptionTopic`** is a custom Aidbox resource modeled after the [FHIR R6 SubscriptionTopic](https://build.fhir.org/subscriptiontopic.html) resource. The resource allows defining a set of events that clients can subscribe to, such as changes in specific resources.
* **`AidboxTopicDestination`** is a custom Aidbox resource that defines where and how the notifications triggered by an `AidboxSubscriptionTopic` should be sent. This resource offers flexibility in specifying various types of destinations. And is considered as a system configuration resource.
* **`AidboxSubscriptionStatus`** is a custom Aidbox resource which describes the notifications: what messages stored in the bundle, source and destination.

## AidboxSubscriptionTopic

The `AidboxSubscriptionTopic` resource describes the data sources for subscriptions. It allows clients to subscribe to events in Aidbox and filter them using user-defined triggers, which are specified by the `trigger` element. Supported properties:

<table data-full-width="false"><thead><tr><th width="257">Property</th><th width="91">Type</th><th>Description</th></tr></thead><tbody><tr><td><code>resource</code> *</td><td>uri</td><td>Resource (reference to definition) for this trigger definition. It is binding to <a href="https://www.hl7.org/fhir/valueset-all-resource-types.html">All Resource Types</a>.</td></tr><tr><td><code>supportedInteraction</code></td><td>code</td><td>create | update | delete</td></tr><tr><td><code>fhirPathCriteria</code></td><td>string</td><td>FHIRPath based trigger rule. FHIRPath criteria are evaluated only within the context of the current resource. Supports <code>%current</code> and <code>%previous</code> variables for comparing resource states during updates.</td></tr><tr><td><code>description</code></td><td>string</td><td>Text representation of the event trigger.</td></tr></tbody></table>

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

To start processing subscription data, create a `AidboxTopicDestination` resource with a reference to the `AidboxSubscriptionTopic`. Examples of `AidboxTopicDestination` resources can be found in sub-sections.

#### Stop subscription data processing

To stop processing subscription data, delete the `AidboxTopicDestination` resource.

#### AidboxTopicDestination Profile

Ensure that the resource metadata contains the kind-specific `AidboxTopicDestination` profile.

#### **Elements**

<table data-full-width="false"><thead><tr><th width="188">Property</th><th width="128">Type</th><th>Description</th></tr></thead><tbody><tr><td><code>status</code></td><td>code</td><td><code>active</code> - the only possible value for now. Expected to be expanded.</td></tr><tr><td><code>topic</code> *</td><td>string</td><td>Url of <code>AidboxSubscriptionTopic</code> resource.</td></tr><tr><td><code>kind</code> *</td><td>code</td><td>Defines the destination for sending notifications. Supported values: <code>kafka-at-least-once</code>, <code>kafka-best-effort</code>, <code>webhook-at-least-once</code>, <code>gcp-pubsub-at-least-once</code></td></tr><tr><td><code>parameter</code> *</td><td><a href="https://www.hl7.org/fhir/parameters.html">FHIR parameters</a></td><td>Defines the destination parameters for sending notifications. Parameters are restricted by profiles for each destination.</td></tr><tr><td><code>content</code></td><td><a href="https://hl7.org/fhir/valueset-subscription-payload-content.html">Subscription payload content</a></td><td><p><code>full-resource</code> | <code>id-only</code> | <code>empty</code><br></p><p><code>full-resource</code> is the default value.</p></td></tr></tbody></table>

\* required.

## Currently supported channels

{% content-ref url="webhook-aidboxtopicdestination.md" %}
[webhook-aidboxtopicdestination.md](webhook-aidboxtopicdestination.md)
{% endcontent-ref %}

{% content-ref url="gcp-pub-sub-aidboxtopicdestination.md" %}
[gcp-pub-sub-aidboxtopicdestination.md](gcp-pub-sub-aidboxtopicdestination.md)
{% endcontent-ref %}

{% content-ref url="kafka-aidboxtopicdestination.md" %}
[kafka-aidboxtopicdestination.md](kafka-aidboxtopicdestination.md)
{% endcontent-ref %}

{% content-ref url="clickhouse-aidboxtopicdestination.md" %}
[clickhouse-aidboxtopicdestination.md](clickhouse-aidboxtopicdestination.md)
{% endcontent-ref %}

{% content-ref url="../../../tutorials/subscriptions-tutorials/aidboxtopicsubscription-nats-tutorial.md" %}
[aidboxtopicsubscription-nats-tutorial.md](../../../tutorials/subscriptions-tutorials/aidboxtopicsubscription-nats-tutorial.md)
{% endcontent-ref %}

## Notification Shape

Notification is a [FHIR Bundle](https://build.fhir.org/bundle.html) resource with `history` type, containing relevant resources in its entries. The first entry is a `AidboxSubscriptionStatus` resource, which describes the payload.\
The other entries depend on `AidboxTopicDestination` `content` parameter. This parameter is the binding to the FHIR [subscription-payload-content](https://hl7.org/fhir/valueset-subscription-payload-content.html) value set: `full-resource` | `id-only` | `empty`

{% tabs %}
{% tab title="full-resource" %}
```json
{
  "resourceType":"Bundle",
  "type":"history",
  "timestamp":"2024-10-03T10:07:55Z",
  "entry":[
    {
      "resource":{
        "resourceType":"AidboxSubscriptionStatus",
        "status":"active",
        "type":"event-notification",
        "notificationEvent":[
          {
            "eventNumber":1,
            "focus":{
              "reference":"QuestionnaireResponse/458e771c-0ff1-4858-ac07-93b7a10c8e3b"
            }
          }
        ],
        "topic":"http://example.org/FHIR/R5/SubscriptionTopic/QuestionnaireResponse-topic",
        "topic-destination":{
          "reference":"AidboxTopicDestination/kafka-destination"
        }
      }
    },
    {
      "resource":{
        "id":"458e771c-0ff1-4858-ac07-93b7a10c8e3b",
        "item":[
          {
            "text":"Leishmania sp 14kD IgG Ser Ql IB",
            "answer":[
              {
                "valueString":"123"
              }
            ],
            "linkId":"128852"
          },
          {
            "text":"Leishmania sp 16kD IgG Ser Ql IB",
            "answer":[
              {
                "valueString":"432"
              }
            ],
            "linkId":"128851"
          }
        ],
        "meta":{
          "lastUpdated":"2024-10-03T10:07:55.843374Z",
          "versionId":"1970",
          "extension":[
            {
              "url":"ex:createdAt",
              "valueInstant":"2024-10-03T10:07:42.342731Z"
            }
          ]
        },
        "author":{
          "identifier":{
            "type":{
              "coding":[
                {
                  "code":"UID",
                  "system":"urn:system:aidbox",
                  "display":"User ID"
                }
              ]
            },
            "value":"admin",
            "system":"http://localhost:8765"
          }
        },
        "status":"completed",
        "authored":"2024-10-03T10:07:55.664Z",
        "resourceType":"QuestionnaireResponse",
        "questionnaire":"http://loinc.org/q/100109-8"
      },
      "fullUrl": "http://aidbox-server/fhir/QuestionnaireResponse/458e771c-0ff1-4858-ac07-93b7a10c8e3b",
      "request":{
        "method":"POST",
        "url":"/fhir/QuestionnaireResponse"
      }
    }
  ]
}
```
{% endtab %}

{% tab title="id-only" %}
```json
{
  "resourceType":"Bundle",
  "type":"history",
  "timestamp":"2024-10-03T10:07:55Z",
  "entry":[
    {
      "resource":{
        "resourceType":"AidboxSubscriptionStatus",
        "status":"active",
        "type":"event-notification",
        "notificationEvent":[
          {
            "eventNumber":1,
            "focus":{
              "reference":"QuestionnaireResponse/458e771c-0ff1-4858-ac07-93b7a10c8e3b"
            }
          }
        ],
        "topic":"http://example.org/FHIR/R5/SubscriptionTopic/QuestionnaireResponse-topic",
        "topic-destination":{
          "reference":"AidboxTopicDestination/kafka-destination"
        }
      }
    },
    {
      "fullUrl": "http://aidbox-server/fhir/QuestionnaireResponse/458e771c-0ff1-4858-ac07-93b7a10c8e3b",
      "request":{
        "method":"POST",
        "url":"/fhir/QuestionnaireResponse"
      }
    }
  ]
}
```
{% endtab %}

{% tab title="empty" %}
```json
{
  "resourceType":"Bundle",
  "type":"history",
  "timestamp":"2024-10-03T10:07:55Z",
  "entry":[
    {
      "resource":{
        "resourceType":"AidboxSubscriptionStatus",
        "status":"active",
        "type":"event-notification",
        "notificationEvent":[
          {
            "eventNumber":1,
            "focus":{
              "reference":"QuestionnaireResponse/458e771c-0ff1-4858-ac07-93b7a10c8e3b"
            }
          }
        ],
        "topic":"http://example.org/FHIR/R5/SubscriptionTopic/QuestionnaireResponse-topic",
        "topic-destination":{
          "reference":"AidboxTopicDestination/kafka-destination"
        }
      }
    }
  ]
}
```
{% endtab %}
{% endtabs %}

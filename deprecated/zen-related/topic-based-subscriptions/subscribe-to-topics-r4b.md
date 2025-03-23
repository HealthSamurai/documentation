# Tutorial: Subscribe to Topic (R4B)

## a

{% hint style="warning" %}
While FHIR topic-based subscriptions are functional, they will no longer receive active development or new features. For enhanced capabilities and ongoing support, please use [Aidbox topic-based subscriptions](../../../../modules/topic-based-subscriptions/wip-dynamic-subscriptiontopic-with-destinations/). This newer implementation offers improved performance, flexibility, and will continue to be developed to meet future needs.&#x20;
{% endhint %}

## Introduction

This tutorial requires configuring your Aidbox instance with a specific SubscriptionTopic for Observation resource. Additionally, a web service that will receive `rest-hook` notifications is required. This [Repo](https://github.com/Aidbox/aidbox-project-template/tree/topic-based-subscription-r4b) contains a suitable template project, which should be used for this tutorial.

### Choose a topic

Use FHIR API to discover available topics. Each topic contains `URL` field which should be specified as a `criteria` field of Subscription.

{% tabs %}
{% tab title="Request" %}
```
GET /fhir/SubscriptionTopic
accept: application/json
```
{% endtab %}

{% tab title="Response" %}
```json
{
 "resourceType": "Bundle",
 "type": "searchset",
 "meta": {
  "versionId": "0"
 },
 "total": 1,
 "link": [
  {
   "relation": "first",
   "url": "http://localhost:8765/fhir/SubscriptionTopic?page=1"
  },
  {
   "relation": "self",
   "url": "http://localhost:8765/fhir/SubscriptionTopic?page=1"
  }
 ],
 "entry": [
  {
   "resource": {
    "id": "cf153a1fde850de90215a6cd0f0abcf5",
    "url": "http://aidbox.app/SubscriptionTopic/observations",
    "meta": {
     "slot_name": "tbs_aidbox_with_subscriptions__observation_topic_srv",
     "queue_table_name": "observation_topic",
     "subscription_status_table_name": "observation_topic_subs_status",
     "lastUpdated": "2023-08-31T15:17:40.355938Z",
     "versionId": "0",
     "extension": [
      {
       "url": "ex:createdAt",
       "valueInstant": "2023-08-31T15:17:40.355938Z"
      }
     ]
    },
    "status": "active",
    "canFilterBy": [
     {
      "modifier": [
       "eq",
       "gt",
       "lt",
       "ge",
       "le"
      ],
      "resource": "Observation",
      "filterParameter": "value"
     },
     {
      "modifier": [
       "eq"
      ],
      "resource": "Observation",
      "filterParameter": "value-increase"
     }
    ],
    "resourceType": "SubscriptionTopic",
    "resourceTrigger": [
     {
      "resource": "Observation",
      "fhirPathCriteria": "%current.value.ofType(Quantity).value > 10"
     }
    ]
   },
   "search": {
    "mode": "match"
   },
   "fullUrl": "http://localhost:8765/SubscriptionTopic/cf153a1fde850de90215a6cd0f0abcf5",
   "link": [
    {
     "relation": "self",
     "url": "http://localhost:8765/SubscriptionTopic/cf153a1fde850de90215a6cd0f0abcf5"
    }
   ]
  }
 ]
}
```
{% endtab %}
{% endtabs %}

In response, one configured topic is available, with `"url": "http://aidbox.app/SubscriptionTopic/observations"`.

### Create Subscription (R4B)

Create a Subscription resource with all the necessary attributes.

Profile `http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-subscription` is required for R4B.

Most interesting part are:

* `"criteria" : "http://aidbox.app/SubscriptionTopic/observations"` - the Topic that a subscription are created for.
* `{"url" : "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-max-count", "valuePositiveInt" : 2}` notification will be delivered immediately when specified number of suitable events is met.
* `{"url" : "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-heartbeat-period", valueUnsignedInt" : 20}` period in seconds when all available to the moment messages will be delivered. if no messages collected - heartbeat event will be fired.
* `{"url" : "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-payload-content", "valueCode" : "id-only"}` notification will only contain ids of resources. The other options are `full-resource` and `empty`.
* `"endpoint" : "http://subscription-demo-server:9000/callback-test-1"` endpoint to which `POST` request with notifications will be sent.
* `{"url" : "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-filter-criteria", "valueString" : "Observation?value=42"}` specifies, that only observation with `value=42` should be delivered for this notification. Available filters or resources may be configured in SubscriptionTopic.

{% tabs %}
{% tab title="Request" %}
```json
POST /fhir/Subscription
content-type: application/json
accept: application/json

{
  "meta" : {
    "profile" : [ "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-subscription" ]
  },
  "criteria" : "http://aidbox.app/SubscriptionTopic/observations",
  "channel" : {
    "extension" : [ {
      "url" : "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-heartbeat-period",
      "valueUnsignedInt" : 20
    }, {
      "url" : "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-timeout",
      "valueUnsignedInt" : 60
    }, {
      "url" : "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-max-count",
      "valuePositiveInt" : 2
    } ],
    "type" : "rest-hook",
    "endpoint" : "http://subscription-demo-server:9000/callback-test-1",
    "payload" : "application/fhir+json",
    "_payload" : {
      "extension" : [ {
        "url" : "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-payload-content",
        "valueCode" : "id-only"
      } ]
    }
  },
  "_criteria" : {
    "extension" : [ {
      "url" : "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-filter-criteria",
      "valueString" : "Observation?value=42"
    } ]
  },
  "resourceType" : "Subscription",
  "reason" : "R4/B Test Topic-Based Subscription for Observation",
  "status" : "requested",
  "id" : "test-sub-1",
  "end" : "2024-12-31T12:00:00.000-00:00"
}
```
{% endtab %}

{% tab title="Response" %}
<pre class="language-json"><code class="lang-json">Status: 201

<strong>{
</strong> "meta": {
  "profile": [
   "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-subscription"
  ],
  "lastUpdated": "2023-08-31T16:30:26.411663Z",
  "versionId": "12",
  "extension": [
   {
    "url": "ex:createdAt",
    "valueInstant": "2023-08-31T16:30:26.411663Z"
   }
  ]
 },
 "criteria": "http://aidbox.app/SubscriptionTopic/observations",
 "channel": {
  "type": "rest-hook",
  "payload": "application/fhir+json",
  "extension": [
   {
    "url": "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-timeout",
    "valueUnsignedInt": 60
   },
   {
    "url": "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-max-count",
    "valuePositiveInt": 4
   },
   {
    "url": "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-heartbeat-period",
    "valueUnsignedInt": 120
   }
  ],
  "_payload": {
   "extension": [
    {
     "url": "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-payload-content",
     "valueCode": "id-only"
    }
   ]
  },
  "endpoint": "http://localhost:27193/callback-url"
 },
 "_criteria": {
  "extension": [
   {
    "url": "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-filter-criteria",
    "valueString": "Observation?value=10"
   }
  ]
 },
 "resourceType": "Subscription",
 "reason": "R4/B Test Topic-Based Subscription for Observation",
 "status": "requested",
 "id": "test-sub-id",
 "end": "2024-12-31T12:00:00.000-00:00"
}

</code></pre>
{% endtab %}
{% endtabs %}

As a result of this step Aidbox will try to perform a handshake with the subscriber service. By default Aidbox expects `Status:200` response.

You may notice `handshake` event in demo server UI:

![](<../../../../.gitbook/assets/Screenshot 2023-09-04 at 16.14.03.png>)

After the successful handshake, the status of the Subscription will be `active`.

{% tabs %}
{% tab title="Request" %}
```
GET /fhir/Subscription/test-sub-id/$status
content-type: application/json
accept: application/json
```
{% endtab %}

{% tab title="Response " %}
Status: 200

```json
{
 "resourceType": "Bundle",
 "type": "history",
 "timestamp": "2023-08-31T16:33:59.161865Z",
 "entry": [
  {
   "resource": {
    "resourceType": "SubscriptionStatus",
    "status": "active",
    "type": "query-status",
    "eventsSinceSubscriptionStart": 0,
    "subscription": {
     "reference": "http://localhost:8765/fhir/Subscription/test-sub-id"
    },
    "topic": "http://aidbox.app/SubscriptionTopic/observations",
    "error": []
     }
    ]
   }
  }
 ]
}
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
Aidbox will attempt a handshake with the service three times at 10-second intervals. If no successful response is received, the subscription will be shifted to an _`"errored"`_ status. To restart the process, the subscription should be deleted and recreated
{% endhint %}

# Subscribe to Topics (R4B)

### Choose a topic

Use FHIR API to discover available topics

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

In response, one configured topic is available, with `"url": "http://aidbox.app/SubscriptionTopic/observations"`. This url should be specified in `topic` field of a subscription.

### Launch subscriber service

Lunch a web service which will receive notification. This service should expose url, which should be specified as `endpoint` field of subscription.

### Create Subscription (R4B)

Create a Subscription resource with all the necessary attributes.

Profile `http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-subscription` is required for R4B.

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
      "valueUnsignedInt" : 120
    }, {
      "url" : "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-timeout",
      "valueUnsignedInt" : 60
    }, {
      "url" : "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-max-count",
      "valuePositiveInt" : 4
    } ],
    "type" : "rest-hook",
    "endpoint" : "http://localhost:27193/callback-url",
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
      "valueString" : "Observation?value=10"
    } ]
  },
  "resourceType" : "Subscription",
  "reason" : "R4/B Test Topic-Based Subscription for Observation",
  "status" : "requested",
  "id" : "test-sub-id",
  "end" : "2024-12-31T12:00:00.000-00:00"
}

```
{% endtab %}

{% tab title="Response" %}
<pre class="language-json"><code class="lang-json">Status: 201
<strong>
</strong><strong>{
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

As a result of this step Aidbox will try to perform a handshake with the subscriber service. By default Aidbox expects `Status:200`  response.

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


# Setup SubscriptionTopic

### Declare SubscriptionTopics in Aidbox configuration project

To allow clients to subscribe to topics, configure the topic definition and topic storage first through the Aidbox configuration project

{% content-ref url="../../aidbox-configuration/aidbox-zen-lang-project/" %}
[aidbox-zen-lang-project](../../aidbox-configuration/aidbox-zen-lang-project/)
{% endcontent-ref %}

Below is an example of an Aidbox entrypoint configured with one topic for observation.

{% code lineNumbers="true" fullWidth="false" %}
```clojure
{ns     aidbox-with-subscriptions
 import #{fhir.topic-based-subscription
          ;; the profile required for FHIR 4.3.0 version:
          hl7-fhir-uv-subscriptions-backport-r4b}

 ;; topic definition structure corresponds to https://build.fhir.org/subscriptiontopic.html
 observation-topic
 {:zen/tags #{fhir.topic-based-subscription/topic-definition}
  ;; SubscriptionTopic url should be an absolute URI (globally unique)
  :url "http://aidbox.app/SubscriptionTopic/observations"
  :resourceTrigger [
                    ;; an SubscriptionTopic may consist of any number of resources
                    {:resource "Observation"
                     :fhirPathCriteria "%current.value.ofType(Quantity).value > 10"}]
  :canFilterBy [{:resource        "Observation"
                 :filterParameter "value"
                 ;; _fhirPath specifies how to calculate value for the filter
                 :_fhirPath       "%current.value.ofType(Quantity).value"
                 :modifier        ["eq" "gt" "lt" "ge" "le"]}

                {:resource        "Observation"
                 :filterParameter "value-increase"
                 ;; both %current and %previous state of the resource are available
                 :_fhirPath       "%current.value.ofType(Quantity).value > %previous.value.ofType(Quantity).value"
                 :modifier        ["eq"]}]}


 ;; topic-storage specifies where to store and how to process events queue
 postgres-observation-topic-storage
 {:zen/tags #{fhir.topic-based-subscription/topic-storage}
  ;; At the moment only PostgreSQL is available as a storage
  :storage-type fhir.topic-based-subscription/postgres
  ;; The name of the table which will be created to store topic events:
  :table-name "observation_topic"
  ;; Possible value for maxContent are: "empty" | "id-only" | "full-resource"
  ;; The actual Resource is only stored in queue when "full-resource" value are specified.
  ;; When deciding which payload type to request, systems SHOULD consider both ease of processing and security of PHI. To mitigate the risk of information leakage, systems SHOULD use the minimum level of detail consistent with the use case. In practice, id-only provides a good balance between security and performance for many real-world scenarios.
  :maxContent "full-resource"
  ;; The period, in seconds, during which events from the replication slot will be buffered before being written to storage:
  :timeout  10
  ;; The maximum number of events the replication slot will buffer before writing to storage:
  :maxCount 100
  ;; Interval in seconds periodic heartbeat record generation in cdc_topic_heartbeat_table, to reclaim the WAL space:
  :heartbeat-rate 120
  ;; The number of workers responsible for notification delivery. min number 4 is advised. One worker can handle up to 1024 subsrciptions:
  :senders-number 4}


 ;; Service which binds storage with topic definition
 observation-topic-srv
 {:zen/tags #{aidbox/service}
  :engine fhir.topic-based-subscription/change-data-capture-service-engine
  :topic-definition observation-topic
  :topic-storage postgres-observation-topic-storage}


 ;; Entrypoint for the instance with corresponding service
 box
 {:zen/tags #{aidbox/system}
  :zen/desc "box for topic test"
  :services {:observation-topic-srv observation-topic-srv}}}
```
{% endcode %}

### Verify the SubscriptionTopic is available

* Run Aidbox and check logs for the following output

```log
15:17:41 cro-1 :fhir.topic-based-subscription.topic/starting-replication-service {:service aidbox-with-subscriptions/observation-topic-srv, :slot-name "tbs_aidbox_with_subscriptions__observation_topic_srv"}
15:17:41 cro-2 :fhir.topic-based-subscription.topic/starting-pg-persist-thread {:service aidbox-with-subscriptions/observation-topic-srv, :slot-name "tbs_aidbox_with_subscriptions__observation_topic_srv"}
15:17:41 40c7a :fhir.topic-based-subscription.topic/service-started {:service aidbox-with-subscriptions/observation-topic-srv}
15:17:41 cro-3 :fhir.topic-based-subscription.topic/starting-delivery-thread {:service aidbox-with-subscriptions/observation-topic-srv, :slot-name "tbs_aidbox_with_subscriptions__observation_topic_srv"}
15:17:41 cro-4 :fhir.topic-based-subscription.subscription/start-notification-sender {:service aidbox-with-subscriptions/observation-topic-srv, :idx 0}
15:17:41 cro-5 :fhir.topic-based-subscription.subscription/start-notification-sender {:service aidbox-with-subscriptions/observation-topic-srv, :idx 1}

```

* Discover SubscriptionTopic resources in Aidbox using FHIR API

{% tabs %}
{% tab title="Request" %}
```json
GET /fhir/SubscriptionTopic
content-type: application/json
accept: application/json
```
{% endtab %}

{% tab title="Response" %}
<pre class="language-json"><code class="lang-json"><strong>{
</strong> "resourceType": "Bundle",
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
</code></pre>
{% endtab %}
{% endtabs %}

* Open Aidbox UI -> Subscription Topics to check the topic status

<figure><img src="../../.gitbook/assets/image (1).png" alt=""><figcaption></figcaption></figure>

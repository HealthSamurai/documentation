# Setup SubscriptionTopic

This page describes how to setup SubscriptionTopic for each Topic Queue Storage type:

[For PostgreSQL](setup-subscriptiontopic.md#for-postgresql)

[For Google Cloud Pub/Sub](setup-subscriptiontopic.md#for-google-cloud-pub-sub)

SubscriptionTopic is configured by Aidbox configuration project by zen-lang.

{% content-ref url="../../aidbox-configuration/aidbox-zen-lang-project/" %}
[aidbox-zen-lang-project](../../aidbox-configuration/aidbox-zen-lang-project/)
{% endcontent-ref %}

In general, to setup the topic in Aidbox configuration project, you need to:

1. Import `fhir.topic-based-subscription` namespace (and `hl7-fhir-uv-subscriptions-backport-r4b` if you're using FHIR 4.3.0).
2. Create topic definition which configures triggers and filters.
3. Specify storage type, e.g. Google Pub/Sub or PostgreSQL.
4. Link topic definition from step 2 and storage type from step 3 into one service.
5. Link this service in the entrypoint (e.g. box).

{% hint style="info" %}
Description below contains settings in camel and kebab case.&#x20;

Camel case is used for settings from FHIR Topic Definition resource ([https://build.fhir.org/subscriptiontopic.html](https://build.fhir.org/subscriptiontopic.html)). Kebab-case is used for Aidbox-specific configuration.
{% endhint %}

{% hint style="info" %}
Remember that all these settings are stored in fhir.topic-based-subscription schema and can be read in Aidbox UI -> Profiles -> Namespaces -> fhir -> topic-based-subscription.
{% endhint %}

## Topic Definition configuration

Topic Definition is a zen schema which is tagged with `fhir.topic-based-subscription/topic-definition` and corresponds to [FHIR SubscriptionTopic](https://build.fhir.org/subscriptiontopic.html) resource.

Topic Definition contains:

* url - should be absolute URI (globally unique)
* resourceTrigger - an array of triggers, each specifies what resource types and FhirPath ([https://hl7.org/fhir/fhirpath.html](https://hl7.org/fhir/fhirpath.html)) criteria should trigger Aidbox to send changes to subscribers of the topic.
*   canFilterBy - list of properties by which Subscriptions on the SubscriptionTopic can be filtered, contains FHIR [SubscriptionTopic.canFilterBy](https://build.fhir.org/subscriptiontopic-definitions.html#SubscriptionTopic.canFilterBy) and also FhirPath:

    * description
    * resource
    * filterParameter
    * modifier
    * \_fhirPath&#x20;

    Example:

```clojure
    ...
    patient-topic
    {:zen/tags #{fhir.topic-based-subscription/topic-definition}
     :url "<YOUR URL>"
     :resourceTrigger [{:resource "Patient"
                        :fhirPathCriteria "%current.birthDate > '2023'}
                       {:resource "Organization"}
                       < ... other resources ... >]
     :canFilterBy [{:resource        "Patient"
                    :filterParameter "first-family-name"
                    :_fhirPath       "%current.name[0].family"
                    :modifier        ["eq"]}
                   {:resource        "Patient"
                    :filterParameter "first-family-name-alternative"
                    :_fhirPath       "name[0].family"
                    :modifier        ["eq"]}
                   <... other filters ... >]}
    ...
```

## Storage Type configuration

Topic storage specifies where to store and how to process events queue.

It is zen schema tagged with `fhir.topic-based-subscription/topic-storage`, containing:&#x20;

* storage-type, e.g. `fhir.topic-based-subscription/google-pubsub` or `fhir.topic-based-subscription/postgres`
* heartbeat-rate: Heartbeat rate requested for current storage in sec. Default is 10sec.
* senders-number: Number of services that deliver subsctiptions.
* timeout: in seconds
* maxCount: max number of resources to hold on in Aidbox before timeout (batch sending)
* maxContent: `empty`, `id-only` or `full-resource`
* include-transaction: For tests purposes. Turns on includeTransaction replication slot option. Default is false
* fhirpath-ignore-on-error: Ignore FhirPath execution errors on persisting queues.
* status-interval: For test purpose mainly. Specifies the number of time between status packets sent back to the server. This allows for easier monitoring of the progress from server. A value of zero disables the periodic status updates completely, although an update will still be sent when requested by the server, to avoid timeout disconnect. The default value is 10 seconds.

For PostgreSQL storage-type:&#x20;

* table-name: name for table to store events

For Google Pub/Sub:&#x20;

* topic-name: topic name in GCP.
* bytes-threshold: max bytes to hold until publishing. Default is 10000.
* project-name: project name in GCP.
* enable-message-ordering: If true, enables message ordering. Uses focusResourceType and focusId as messageOrderingKey. Applied only when maxContent is 'id-only' or 'full-resource'.

Example:

```
pubsub-patient-storage
    {:zen/tags #{fhir.topic-based-subscription/topic-storage}
     :storage-type fhir.topic-based-subscription/google-pubsub
     :status-interval 0.5
     :maxContent "full-resource"
     :heartbeat-rate 0.5
     :project-name "local-project"
     :topic-name "test-topic"
     :enable-message-ordering true
     :timeout 1
     :bytes-threshold 1000000
     :maxCount 10}
```

## Example configuration for PostgreSQL&#x20;

Below is an example of an Aidbox entrypoint configured with one topic for observation.

{% code lineNumbers="true" fullWidth="false" %}
```clojure
{ns     aidbox-with-subscriptions
 import #{fhir.topic-based-subscription
          ;; the profile required for FHIR 4.3.0 version:
          hl7-fhir-uv-subscriptions-backport-r4b}

 observation-topic
 {:zen/tags #{fhir.topic-based-subscription/topic-definition}
  ;; SubscriptionTopic url should be an absolute URI (globally unique)
  :url "http://aidbox.app/SubscriptionTopic/observations"
  :resourceTrigger [
                    ;; an SubscriptionTopic may consist 
                    ;; of any number of resources
                    {:resource "Observation"
                     :fhirPathCriteria "%current.value.ofType(Quantity).value > 10"}]
  :canFilterBy [{:resource        "Observation"
                 :filterParameter "value"
                 ;; _fhirPath specifies how to calculate value for the filter
                 :_fhirPath       "%current.value.ofType(Quantity).value"
                 :modifier        ["eq" "gt" "lt" "ge" "le"]}

                {:resource        "Observation"
                 :filterParameter "value-increase"
                 ;; both %current and %previous state of the 
                 ;; resource are available
                 :_fhirPath       "%current.value.ofType(Quantity).value > %previous.value.ofType(Quantity).value"
                 :modifier        ["eq"]}]}


 postgres-observation-topic-storage
 {:zen/tags #{fhir.topic-based-subscription/topic-storage}
  ;; At the moment only PostgreSQL is available as a storage
  :storage-type fhir.topic-based-subscription/postgres
  ;; The name of the table which will be created to store topic events:
  :table-name "observation_topic"
  ;; Possible value for maxContent are: "empty" | "id-only" | "full-resource"
  ;; The actual Resource is only stored in queue when "full-resource" value 
  ;; are specified. When deciding which payload type to request, 
  ;; systems SHOULD consider both ease of processing and security of PHI. 
  ;; To mitigate the risk of information leakage, systems SHOULD use the 
  ;; minimum level of detail consistent with the use case. 
  ;; In practice, id-only provides a good balance between security 
  ;; and performance for many real-world scenarios.
  :maxContent "full-resource"
  ;; The period, in seconds, during which events from the replication slot 
  ;; will be buffered before being written to storage:
  :timeout  10
  ;; The maximum number of events the replication slot will buffer before
  ;; writing to storage:
  :maxCount 100
  ;; Interval in seconds periodic heartbeat record generation 
  ;; in cdc_topic_heartbeat_table, to reclaim the WAL space:
  :heartbeat-rate 120
  ;; The number of workers responsible for notification delivery. 
  ;; min number 4 is advised. One worker can handle up to 1024 subsrciptions:
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

{% code fullWidth="false" %}
```log
15:17:41 cro-1 :fhir.topic-based-subscription.topic/starting-replication-service {:service aidbox-with-subscriptions/observation-topic-srv, :slot-name "tbs_aidbox_with_subscriptions__observation_topic_srv"}
15:17:41 cro-2 :fhir.topic-based-subscription.topic/starting-pg-persist-thread {:service aidbox-with-subscriptions/observation-topic-srv, :slot-name "tbs_aidbox_with_subscriptions__observation_topic_srv"}
15:17:41 40c7a :fhir.topic-based-subscription.topic/service-started {:service aidbox-with-subscriptions/observation-topic-srv}
15:17:41 cro-3 :fhir.topic-based-subscription.topic/starting-delivery-thread {:service aidbox-with-subscriptions/observation-topic-srv, :slot-name "tbs_aidbox_with_subscriptions__observation_topic_srv"}
15:17:41 cro-4 :fhir.topic-based-subscription.subscription/start-notification-sender {:service aidbox-with-subscriptions/observation-topic-srv, :idx 0}
15:17:41 cro-5 :fhir.topic-based-subscription.subscription/start-notification-sender {:service aidbox-with-subscriptions/observation-topic-srv, :idx 1}

```
{% endcode %}

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

<figure><img src="../../.gitbook/assets/image (1) (1).png" alt=""><figcaption></figcaption></figure>

## Example configuration for Google Cloud Pub/Sub

{% code fullWidth="false" %}
```
{ns pub-sub-topic
    import #{fhir.topic-based-subscription}

    patient-topic
    {:zen/tags #{fhir.topic-based-subscription/topic-definition}
     :url "some-url-2"
     :resourceTrigger [{:resource "Patient"}
                       {:resource "Organization"}]
     :canFilterBy [{:resource        "Patient"
                    :filterParameter "first-family-name"
                    :_fhirPath       "%current.name[0].family"
                    :modifier        ["eq"]}
                   {:resource        "Patient"
                    :filterParameter "first-family-name-alternative"
                    :_fhirPath       "name[0].family"
                    :modifier        ["eq"]}
                   {:resource        "Patient"
                    :filterParameter "prev-first-family-name"
                    :_fhirPath       "%previous.name[0].family"
                    :modifier        ["eq"]}]}


    pubsub-patient-storage
    {:zen/tags #{fhir.topic-based-subscription/topic-storage}
     :storage-type fhir.topic-based-subscription/google-pubsub
     :status-interval 0.5
     :maxContent "full-resource"
     :heartbeat-rate 0.5
     :project-name "local-project"
     :topic-name "test-topic"
     :enable-message-ordering true
     :timeout 1
     :bytes-threshold 1000000
     :maxCount 10}

    patient-topic-srv
    {:zen/tags #{aidbox/service}
     :engine fhir.topic-based-subscription/change-data-capture-service-engine
     :topic-definition patient-topic
     :topic-storage pubsub-patient-storage}

    box
    {:zen/tags #{aidbox/system}
     :zen/desc "box for topic test"
     :services {:patient-topic-srv patient-topic-srv}}}
```
{% endcode %}

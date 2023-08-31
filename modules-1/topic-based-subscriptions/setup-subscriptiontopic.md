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
          ;; profiles required for FHIR 4.3.0 version:
          hl7-fhir-r4b-core hl7-fhir-uv-subscriptions-backport-r4b}

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

```
// Some cod
```

* Discover SubscriptionTopic resources in Aidbox using FHIR API&#x20;

{% tabs %}
{% tab title="Request" %}

{% endtab %}

{% tab title="Response R4B" %}

{% endtab %}

{% tab title="Response R5" %}

{% endtab %}
{% endtabs %}

* Open Aidbox UI -> Subscription Topics to check the topic status

<figure><img src="../../.gitbook/assets/image (1).png" alt=""><figcaption></figcaption></figure>

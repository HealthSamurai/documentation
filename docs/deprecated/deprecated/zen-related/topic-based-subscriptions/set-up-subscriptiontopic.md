# Set up SubscriptionTopic

{% hint style="warning" %}
While FHIR topic-based subscriptions are functional, they will no longer receive active development or new features. For enhanced capabilities and ongoing support, please use [Aidbox topic-based subscriptions](../../../../modules/topic-based-subscriptions/wip-dynamic-subscriptiontopic-with-destinations/README.md). This newer implementation offers improved performance, flexibility, and will continue to be developed to meet future needs.
{% endhint %}

This page describes how to set up SubscriptionTopic for each Topic Queue Storage type.

SubscriptionTopic is configured by Aidbox configuration project by zen-lang.

{% hint style="warning" %}
Since the 2405 release, using Aidbox in FHIR schema validation engine is recommended, which is incompatible with zen or Entity/Attribute options. Setup Aidbox with FHIR Schema validation engine
{% endhint %}

* [Aidbox configuration project](../aidbox-zen-lang-project/)

In general, to set up the topic in Aidbox configuration project, you need the following steps:

1. Import **`fhir.topic-based-subscription`** namespace (and **`hl7-fhir-uv-subscriptions-backport-r4b`** if you're using FHIR 4.3.0).
2. Create **Topic Definition** which configures triggers and filters.
3. Specify **Storage Type** (e.g. PostgreSQL or Google Pub/Sub) and configuration values for it.
4. Link **Topic Definition** from step 2 and **Storage Type** from step 3 into one service.
5. Link this service from step 4 in the entry point (e.g. box).

## Database Configuration

SubscriptionTopic-based services rely on [PostgreSQL Logical Replication](https://www.postgresql.org/docs/current/logical-replication.html). Aidbox plays the role of the `Subscriber` to receive and process relevant events from the database. A replication slot is created automatically for each service when it starts. Therefore, in general, the database should be configured to support logical replication, and the `POSTGRES_USER` should have replication privileges.

### Self Hosted Database

If you use [aidboxdb-image](../../../../database/aidboxdb-image/README.md) then it's already configured to work properly with SubscriptionTopic.

Otherwise, check that `wal_level` is set to `logical` in `postgresql.conf` file:

```
wal_level = logical
```

Check for other relevant settings in [PostgreSQL documentation](https://www.postgresql.org/docs/current/logical-replication-config.html).

### Cloud Databases

#### AWS RDS PostgreSQL

To enable a database instance hosted with [AWS RDS](https://aws.amazon.com/rds/postgresql/) to work with the SubscriptionTopic services parameter `rds.logical_replication` should be set to `1`. One possible way to accomplish this is as follows:

1. Navigate to **RDS**, this should take you to the RDS Dashboard.
2. Click **Parameter Groups** in the **Resources** panel on the dashboard.
3. Create parameter group
4. Click the `Edit parameters`
5. Search for `rds.logical_replication` and set its value to `1`.
6. Navigate to the database instance, click **Modify**, and in the `DB parameter group` menu select the parameter group created in Step 3.

<figure><img src="../../../../.gitbook/assets/image%20(99).png" alt=""><figcaption></figcaption></figure>

<figure><img src="../../../../.gitbook/assets/image%20(102).png" alt=""><figcaption></figcaption></figure>

To check that the setting is applied run query `SHOW wal_level;` he result should be `logical`.

#### Azure Database for PostgreSQL - Flexible Server <a href="#logical-replication-and-logical-decoding-in-azure-database-for-postgresql---flexible-server" id="logical-replication-and-logical-decoding-in-azure-database-for-postgresql---flexible-server"></a>

Setup your database instance according to [the official guide ](https://learn.microsoft.com/en-us/azure/postgresql/flexible-server/concepts-logical#prerequisites-for-logical-replication-and-logical-decoding)(Prerequisites for logical replication and logical decoding):

1. Go to server parameters page on the portal.
2. Set the server parameter `wal_level` to `logical`.
3. Update `max_worker_processes` parameter value to at least 16. Otherwise, you may run into issues like `WARNING: out of background worker slots`.
4. Save the changes and restart the server to apply the changes.
5.  Grant the user with which Aidbox connects to the database replication permissions: SQLCopy.

    ```sql
    ALTER ROLE <username> WITH REPLICATION;
    ```

<figure><img src="../../../../.gitbook/assets/image%20(98).png" alt=""><figcaption><p>Server Parameters</p></figcaption></figure>

\\

## Topic Definition configuration

Topic Definition is a zen schema which is tagged with **`fhir.topic-based-subscription/topic-definition`** and corresponds to [FHIR SubscriptionTopic](https://build.fhir.org/subscriptiontopic.html) resource.

**Topic Definition** contains:

* **url** - should be absolute URI (globally unique)
* **resourceTrigger** - an array of triggers, each specifies what resource types and FhirPath ([https://hl7.org/fhir/fhirpath.html](https://hl7.org/fhir/fhirpath.html)) criteria should trigger Aidbox to send changes to subscribers of the topic.
*   **canFilterBy** - list of properties by which Subscriptions on the SubscriptionTopic can be filtered, contains FHIR [SubscriptionTopic.canFilterBy](https://build.fhir.org/subscriptiontopic-definitions.html#SubscriptionTopic.canFilterBy) and also FhirPath:

    * `description`
    * `resource`
    * `filterParameter`
    * `modifier`
    * `_fhirPath`

    Example:

```clojure
    ...
    patient-topic
    {:zen/tags #{fhir.topic-based-subscription/topic-definition}
     :url "<YOUR URL>"
     :resourceTrigger [{:resource "Patient"
                        :fhirPathCriteria "%current.birthDate > '2023'"}
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

It is zen schema tagged with **`fhir.topic-based-subscription/topic-storage`**, containing:

* **storage-type**: Choose one of the following:
  * _PostgreSQL_: `fhir.topic-based-subscription/postgres`
  * _Google Pub/Sub_: `fhir.topic-based-subscription/gcp-pubsub`
  * _Aidbox Workflow Engine Connector_: `fhir.topic-based-subscription/awf-connector`
* **heartbeat-rate**: Heartbeat rate requested for current storage in sec. Default is 10sec.
* **timeout**: Timeout value for batching in seconds
* **maxCount**: Max number of resources to hold on in Aidbox before timeout (batch sending)
* **maxContent**: `empty`, `id-only` or `full-resource`. Defines the permission of how much the event messages of Subscription can have information about the triggered resource. It isn't allowed to create Subscriptions with `Subscription.content` value that requires more content tha`maxContent` value of its own topic. Default is `empty`.
* **include-transaction**: For tests purposes. Turns on includeTransaction replication slot option. Default is false
* **fhirpath-ignore-on-error**: Ignore FhirPath execution errors on persisting queues.
* **status-interval**: For test purpose mainly. Specifies the number of time between status packets sent back to the server. This allows for easier monitoring of the progress from server. A value of zero disables the periodic status updates completely, although an update will still be sent when requested by the server, to avoid timeout disconnect. The default value is 10 seconds.

In addition to the above, there are several properties for each specific **storage-type**.

For _PostgreSQL_:

* **table-name**: Name for table to store events
* **senders-number**: Number of services that deliver subsctiptions.

For _Google Pub/Sub_:

* **topic-name**: Topic name in GCP.
* **bytes-threshold**: Max bytes to hold until publishing. Default is 10000.
* **project-name**: Project name in GCP.
* **enable-message-ordering**: If true, enables message ordering. Uses focusResourceType and focusId as messageOrderingKey. Applied only when maxContent is 'id-only' or 'full-resource'.

For _Aidbox Workflow Engine Connector_:

* **rules**: Trigger rule for Task/Workflow. Either `task` or `workflow` below should be used to indicate which activity should be triggered.
  * **filterBy**: The list of filtering criteria for events on Topic (has the same properties as[ FHIR Subscription.filterBy](https://www.hl7.org/fhir/subscription-definitions.html#Subscription.filterBy): `resourceType`, `filterParameter`, `comparator`, `modifier`, `value`)
  * **task**: Indicate which Task should be triggered by `definition` field.
  * **workflow**: Indicate which Workflow should be triggered by `definition` field.

Example:

<pre><code><strong>...
</strong><strong>pubsub-patient-storage
</strong>    {:zen/tags #{fhir.topic-based-subscription/topic-storage}
     :storage-type fhir.topic-based-subscription/gcp-pubsub
     :status-interval 0.5
     :maxContent "full-resource"
     :heartbeat-rate 0.5
     :project-name "local-project"
     :topic-name "test-topic"
     :enable-message-ordering true
     :timeout 1
     :bytes-threshold 1000000
     :maxCount 10}
...
</code></pre>

####

## Configuration Examples

Examples of an Aidbox entry point for each storage type, configured with one topic:

[#postgresql-queue-storage](set-up-subscriptiontopic.md#postgresql-queue-storage)

[#google-cloud-pub-sub](set-up-subscriptiontopic.md#google-cloud-pub-sub)

[#aidbox-workflow-engine-connector](set-up-subscriptiontopic.md#aidbox-workflow-engine-connector)

### PostgreSQL Queue Storage

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

### Google Cloud Pub/Sub

<pre class="language-clojure" data-full-width="false"><code class="lang-clojure">{ns pub-sub-topic
    import #{fhir.topic-based-subscription}

    observation-topic
    {:zen/tags #{fhir.topic-based-subscription/topic-definition}
     ;; SubscriptionTopic url should be an absolute URI (globally unique)
     :url "http://aidbox.app/SubscriptionTopic/observations"
     :resourceTrigger [
                       ;; an SubscriptionTopic may consist 
                       ;; of any number of resources
<strong>                       {:resource "Observation"
</strong><strong>                        :fhirPathCriteria "%current.value.ofType(Quantity).value > 10"}]
</strong>     :canFilterBy [{:resource        "Observation"
                    :filterParameter "value"
<strong>                    ;; _fhirPath specifies how to calculate value for the filter
</strong>                    :_fhirPath       "%current.value.ofType(Quantity).value"
<strong>                    :modifier        ["eq" "gt" "lt" "ge" "le"]}
</strong>
                   {:resource        "Observation"
                    :filterParameter "value-increase"
                    ;; both %current and %previous state of the 
                    ;; resource are available
                    :_fhirPath       "%current.value.ofType(Quantity).value > %previous.value.ofType(Quantity).value"
                    :modifier        ["eq"]}]}


    gcp-pubsub-observation-topic-storage
    {:zen/tags #{fhir.topic-based-subscription/topic-storage}
     :storage-type fhir.topic-based-subscription/gcp-pubsub
     :status-interval 10
     :heartbeat-rate 120
     :maxContent "full-resource"
<strong>     :project-name "aidbox-cloud-demo"
</strong>     :topic-name "aidbox-tbs-test"
     :timeout  1
     :maxCount 100
     :bytes-threshold 10000
     :enable-message-ordering false}

    observation-topic-srv
    {:zen/tags #{aidbox/service}
     :engine fhir.topic-based-subscription/change-data-capture-service-engine
     :topic-definition observation-topic
     :topic-storage gcp-pubsub-observation-topic-storage}

    box
    {:zen/tags #{aidbox/system}
     :zen/desc "box for topic test"
     :services {:patient-topic-srv patient-topic-srv}}}
</code></pre>

### Aidbox Workflow Engine Connector

<pre class="language-clojure" data-full-width="false"><code class="lang-clojure">{ns awf-connector-topic
    import #{fhir.topic-based-subscription}

    topic-def
    {:zen/tags #{fhir.topic-based-subscription/topic-definition}
     :url "http://aidbox.app/SubscriptionTopic/observations"
     :resourceTrigger [{:resource "Patient"}
                       {:resource "Encounter"
                        :fhirPathCriteria "%current.status = 'completed'"}]

<strong>     :canFilterBy [{:resource        "Patient"
</strong>                    :filterParameter "Patient-deceased"
                    :_fhirPath       "(%previous.deceased.exists().not() or %previoius.deceased = false) and (%current.deceased.exists() and %current.deceased = true)"
                    :modifier        ["eq"]}
                   {:filterParameter "resource-type"
                    :_fhirPath       "resourceType"
                    :modifier        ["eq"]}]}

    awf-connector
    {:zen/tags #{fhir.topic-based-subscription/topic-storage}
     :storage-type fhir.topic-based-subscription/awf-connector
     :maxContent "full-resource"
<strong>     :rules [{:task     {:definition      encounter-completed-task}
</strong><strong>              :filterBy [{:filterParameter "resource-type"
</strong>                          :modifier        "eq"
                          :value           "Encounter"}]}
             {:task     {:definition      patient-change-task}
              :filterBy [{:filterParameter "resource-type"
                          :modifier        "eq"
                          :value           "Patient"}]}
             {:task     {:definition       patient-deceased-task}
              :filterBy [{:resourceType    "Patient"
                          :filterParameter "Patient-deceased"
                          :modifier        "eq"
                          :value           "true"}]}]}

    patient-topic-srv
    {:zen/tags #{aidbox/service}
     :engine fhir.topic-based-subscription/change-data-capture-service-engine
     :topic-definition topic-def
     :topic-storage awf-connector}

    box
    {:zen/tags #{aidbox/system}
     :zen/desc "box for topic test"
     :services {:patient-topic-srv patient-topic-srv}}}
     
</code></pre>

## Verify the SubscriptionTopic is available

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

<figure><img src="../../../../.gitbook/assets/image%20(1)%20(1)%20(1)%20(1).png" alt=""><figcaption></figcaption></figure>

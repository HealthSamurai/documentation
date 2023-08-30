# Topic-Based Subscriptions

## Overview

{% hint style="info" %}
The **Topic-Based Subscriptions** module is currently in **preview** and is exclusively available through the _**:edge**_ build channel.
{% endhint %}

Aidbox's Topic-Based Subscription module offers a robust and efficient mechanism designed to allow clients to ask for notifications when data changes. It is an active notification system, an Aidbox server actively sends notifications to clients as changes occur.&#x20;



**Key Features of the Module:**

* **FHIR Conformance**: Our implementation strictly adheres to the [FHIR Topic-Based Subscriptions Framework Guide](https://build.fhir.org/subscriptions.html), ensuring interoperability and standardized practices.
* **Fault-Tolerant**: Built with resilience in mind, the system gracefully handles disruptions, ensuring uninterrupted operations and continuous data flow.
* **Data Integrity Guarantee**: Relying on the [Change Data Capture](https://en.wikipedia.org/wiki/Change\_data\_capture) (CDC) methodology, the module ensures that every piece of data is accurately captured and relayed, minimizing the risk of data loss or inconsistency.
* **Flexibility**: Administrators enjoy a significant degree of flexibility, especially when it comes to defining Topics. The system offers a versatile range of filters that subscribers can use to refine the data they receive.
* **High Performance**: Engineered for efficiency, our Topic-Based Subscription module is capable of effortlessly managing thousands of subscribers, maintaining its performance standards even under heavy loads.

### **Main Components**

The Subscription mechanism is composed of two main parts.

1. [**SubscriptionTopic**](https://build.fhir.org/subscriptiontopic.html) **-** defines what is available for subscription, and specifies available filters and shapes of the notification. SubscriptionTopics should be added through the configuration project, only READ and SEARCH operations are available for them.
2. [**Subscription**](https://build.fhir.org/subscription.html#Subscription) **-** describes client request to be notified about events described by SubscriptionTopic,  specifies what actual filters to apply, and specifies channel, endpoint, and payload shape. Subscriptions are created by a client through the[ FHIR API](https://build.fhir.org/subscriptions.html#creating-a-subscription)

### Supported Features

* #### [Multi-Resource Topics](https://build.fhir.org/subscriptions.html#multi-resource-topics)&#x20;
* #### [**FHIRPath Trigger Definitions**](https://build.fhir.org/subscriptiontopic.html#fhirpath-criteria) and [Resource Interactions](https://build.fhir.org/subscriptiontopic.html#resource-operation-pairs)&#x20;
* #### [Defining Allowed Filters](https://build.fhir.org/subscriptiontopic.html#filters)
* #### `empty`, `id-only`, and `full-resource` [ Payload Types](https://build.fhir.org/subscription.html#payloads)
* [**Batching Results**](https://build.fhir.org/subscription.html#batching)
* [**REST-Hook**](https://build.fhir.org/subscription.html#rest-hook) **Channel**
* **`handshake`** , **`heartbeat`** , **`event-notification`** [**notification types**](https://build.fhir.org/subscriptionstatus.html#notification-types)
* [**Event Numbering**](https://build.fhir.org/subscriptionstatus.html#event-numbering)
* [**`$events` operation**](https://build.fhir.org/subscription-operation-events.html) **and** [**`$status` operation**](https://build.fhir.org/subscription-operation-status.html)

### Supported FHIR versions

Aidbox Supports Topic-Based Subscriptions for both **R5** and **R4B** (4.3.0) versions. To work with the R4B version [Resource Profile: R4/B Topic-Based Subscription ](https://build.fhir.org/ig/HL7/fhir-subscription-backport-ig/StructureDefinition-backport-subscription.html)is required.

## Architecture

The following image shows the architecture of a topic-based subscription module:

<figure><img src="../.gitbook/assets/Integrations domain.png" alt=""><figcaption><p>topic-based subscription module</p></figcaption></figure>

the module is composed of two loosely coupled services:

**Change Data Capture (CDC) Service -** is responsible for processing data change log stream from the PostgreSQL replication slot. The replication slot is created and processed according to topic configuration, which received from [aidbox-zen-lang-project](../aidbox-configuration/aidbox-zen-lang-project/ "mention"). It's CDC service that is responsible for selecting only events who are match the **Topic Trigger** definition, evaluating **canFilterBy** expressions, and decoding events into proper FHIR resources.

The final events are stored in **Topic Queue Storage.**  At the moment, only PostgreSQL is available as a queue storage, but support for other options is planned.&#x20;

Every **event** consists of headers, which include `focusResourceType`, `focusResourceId`, and `canFilterByValues`, with the optional inclusion of triggered `resources`.



**Delivery Service** - is responsible for delivering notifications from Topic Queue Storage to a client who created a subscription via specified channels.  For every subscription, the service tracks all sent notifications to allow detection of delivery errors. The delivery is carried out by workers, the number of whom can be adjusted for each topic based on the anticipated number of subscriptions and available resources.

The requests for **Subscriptions** are created by clients with [create](https://www.hl7.org/fhir/http.html#create) operations. When processing a request for a subscription, the following are _some_ checks that a server validates:

* that the `SubscriptionTopic` is valid and implemented by the server
* that all requested filters are defined in the requested topic
* that the channel type is known and implemented by the server
* that the payload configuration is known and allowed by `SubscriptionTopic`

**Subscriptions** are initiated in the requested status. after which the handshake procedure is started. Following this, the handshake procedure begins. If successful, the subscription is activated, and corresponding events begin to be delivered from that point onward. These events are grouped into a single notification based on the `maxCount` and `heartbeatPeriod` fields of the subscriptions.

## Configuration

To allow clients to subscribe to topics, configure the topic definition and topic storage first through the [aidbox-zen-lang-project](../aidbox-configuration/aidbox-zen-lang-project/ "mention")

Below is an example of an Aidbox entrypoint configured with one topic for observation.

Check for comments for explanation.

{% code lineNumbers="true" fullWidth="true" %}
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

As a result of an Aidbox start with such configuration, a topic with URL `http://aidbox.app/SubscriptionTopic/observations` will available through READ and SEARCH API and for subscription.

The state of the topic can be monitored with Aidbox Console UI:&#x20;

<figure><img src="../.gitbook/assets/image.png" alt=""><figcaption></figcaption></figure>


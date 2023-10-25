# Topic-Based Subscriptions

## Overview

Aidbox's Topic-Based Subscription module offers a robust and efficient mechanism designed to allow clients to ask for notifications when data changes. It is an active notification system, an Aidbox server actively sends notifications to clients as changes occur.

**Key Features of the Module:**

* **FHIR Conformance**: Our implementation strictly adheres to the [FHIR Topic-Based Subscriptions Framework Guide](https://build.fhir.org/subscriptions.html), ensuring interoperability and standardized practices.
* **Fault-Tolerant**: Built with resilience in mind, the system gracefully handles disruptions, ensuring uninterrupted operations and continuous data flow.
* **Data Integrity Guarantee**: Relying on the [Change Data Capture](https://en.wikipedia.org/wiki/Change\_data\_capture) (CDC) methodology, the module ensures that every piece of data is accurately captured and relayed, minimizing the risk of data loss or inconsistency.
* **Flexibility**: Administrators enjoy a significant degree of flexibility, especially when it comes to defining Topics. The system offers a versatile range of filters that subscribers can use to refine the data they receive.
* **High Performance**: Engineered for efficiency, our Topic-Based Subscription module is capable of effortlessly managing thousands of subscribers, maintaining its performance standards even under heavy loads.

## **Main Components**

The Subscription mechanism is composed of two main parts.

1. [**SubscriptionTopic**](https://build.fhir.org/subscriptiontopic.html) **-** defines what is available for subscription, and specifies available filters and shapes of the notification. SubscriptionTopics should be added through the configuration project, only READ and SEARCH operations are available for them.
2. [**Subscription**](https://build.fhir.org/subscription.html#Subscription) **-** describes client request to be notified about events described by SubscriptionTopic, specifies what actual filters to apply, and specifies channel, endpoint, and payload shape. Subscriptions are created by a client through the[ FHIR API](https://build.fhir.org/subscriptions.html#creating-a-subscription)

## Architecture

The following image shows the architecture of the Aidbox topic-based subscriptions module:

<figure><img src="../../.gitbook/assets/topic-based-subscription.png" alt=""><figcaption><p>topic-based subscription module</p></figcaption></figure>

{% hint style="info" %}
At the moment, **PostgreSQL, Google Cloud Pub/Sub**, and **Aidbox Workflow Engine** are supported as the _Topic Queue Storage_.  We are planning to support other options too.
{% endhint %}

### PostgreSQL Queue Storage

If _Topic Queue Storage_ is **PostgreSQL**, all the processes of topic-based subscription - from triggering to delivery - will be handled by Aidbox.&#x20;

The module is composed of two loosely coupled services :

1. **Change Data Capture (CDC) Service -** is responsible for processing data change log stream from the PostgreSQL replication slot. The replication slot is created and processed according to the topic configuration defined by [aidbox-zen-lang-project](../../aidbox-configuration/aidbox-zen-lang-project/ "mention"). The CDC service is responsible for topic-level event filtering by **resourceTrigger** definition, evaluating **canFilterBy** expressions which will be used for subscription-level event filtering, and decoding events into proper FHIR resources.\
   \
   Finally, events will be stored in a table of PostgreSQL. \
   Every **event** consists of `headers` and `body`. \
   The `headers` include `focusResourceType`, `focusResourceId`, and `values` for subscription-level event filtering.\
   As the `body` , an event can have the triggered resource in `focusResource` field. Depending on `maxContent` value, the content of `body` changes.\

2. **Delivery Service** - is responsible for delivering notifications from Topic Queue Storage to a client who created a subscription via specified channels. This service will be run only if _Topic Queue Storage_ is **PostgreSQL**.\
   \
   For every subscription, the service tracks all sent notifications to allow detection of delivery errors. The delivery is carried out by workers, whose number can be adjusted for each topic, based on the anticipated number of subscriptions and available resources.\
   \
   **Subscription** resources are created by clients with [create](https://www.hl7.org/fhir/http.html#create) operations.\
   \
   **Subscriptions** are initiated in the requested status, and then, immediately the handshake request will be sent to the configured endpoint (subscriber).  If the handshake is successful, the subscription will be activated, and start delivering the events from that point onward. These events can be batched into a single notification based on the `maxCount` and `heartbeatPeriod` fields of the subscriptions.

### Other Topic Queue Storage (connector to external service)

If you choose the other _Topic Queue Storage_ than PostgreSQL, Aidbox will work with the connector to external service.  In this case, Aidbox will be responsible for only publishing events to the selected Queue Storage.&#x20;

**Change Data Capture (CDC) Service** works in the same way as for PostgreSQL Queue Storage. \
The only difference is that the events will be stored in the external message queue service instead of PostgreSQL.\
\
For example, in case the Queue Storage Type is _Google Cloud Pub/Sub_, Aidbox publishes events to the _Google Cloud Pub/Sub_'s Topic as configured, and Aidbox completes its own responsibilities.\
All the other processes like filtering events or delivery to the endpoint will be done by _Google Cloud Pub/Sub_.

Accordingly, it is necessary to manage the external message queue service outside Aidbox, including creating Topics and Subscriptions as the exact external service requires.\


### Aidbox Workflow Engine Connector

Instead of delivering the events, **Aidbox Workflow Engine Connector** enables to directly produce Aidbox Task/Workflow, right after the CDC service processes the events.&#x20;

This connector guarantees the execution of Task/Workflow for each triggered event.  You can simply leverage convenient features like auto-retrying or detecting failed processes just by integrating with API of [workflow-engine](../workflow-engine/ "mention").

The events will be stored in the AidboxTask / AidboxWorkflow resource as their `params`.



## Supported FHIR versions

Aidbox Supports Topic-Based Subscriptions for both **R5** and **R4B** (4.3.0) versions. To work with the R4B version [Resource Profile: R4/B Topic-Based Subscription ](https://build.fhir.org/ig/HL7/fhir-subscription-backport-ig/StructureDefinition-backport-subscription.html)is required.

## Currently supported features

* [**Multi-Resource Topics**](https://build.fhir.org/subscriptions.html#multi-resource-topics)
* [**FHIRPath Trigger Definitions**](https://build.fhir.org/subscriptiontopic.html#fhirpath-criteria) **and** [**Resource Interactions**](https://build.fhir.org/subscriptiontopic.html#resource-operation-pairs)
* [**Defining Allowed Filters**](https://build.fhir.org/subscriptiontopic.html#filters)
* **`empty`, `id-only`, and `full-resource`** [**Payload Types**](https://build.fhir.org/subscription.html#payloads)
* [**Batching Results**](https://build.fhir.org/subscription.html#batching)
* [**REST-Hook**](https://build.fhir.org/subscription.html#rest-hook) **Channel**
* **`handshake`** , **`heartbeat`** , **`event-notification`** [**notification types**](https://build.fhir.org/subscriptionstatus.html#notification-types)
* [**Event Numbering**](https://build.fhir.org/subscriptionstatus.html#event-numbering)
* [**`$events` operation**](https://build.fhir.org/subscription-operation-events.html) **and** [**`$status` operation**](https://build.fhir.org/subscription-operation-status.html)

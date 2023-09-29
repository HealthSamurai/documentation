# Topic-Based Subscriptions

## Overview

Aidbox's Topic-Based Subscription module offers a robust and efficient mechanism designed to allow clients to ask for notifications when data changes. It is an active notification system, an Aidbox server actively sends notifications to clients as changes occur.

{% hint style="info" %}
At the moment, as queue storage below, **PostgreSQL** and **Google Cloud Pub/Sub** are supported.  We are planning to support other options like **Apache Kafka** and **RabbitMQ**.
{% endhint %}

## **Key Features of the Module:**

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

The module is composed of two loosely coupled services:

1. **Change Data Capture (CDC) Service -** is responsible for processing data change log stream from the PostgreSQL replication slot. The replication slot is created and processed according to the topic configuration defined by [aidbox-zen-lang-project](../../aidbox-configuration/aidbox-zen-lang-project/ "mention"). The CDC service is responsible for selecting only events that match the **Topic Trigger** definition, evaluating **canFilterBy** expressions, and decoding events into proper FHIR resources.\
   \
   The final events are stored in **Topic Queue Storage.** At the moment, PostgreSQL and Google Cloud Pub/Sub are available as queue storage, but support for other options is planned.\
   \
   Every **event** consists of headers, which include `focusResourceType`, `focusResourceId`, and `canFilterByValues`, with the optional inclusion of triggered `resources`.\

2. **Delivery Service** - is responsible for delivering notifications from Topic Queue Storage to a client who created a subscription via specified channels. This service will be run only if _Topic Queue Storage_ is **PostgreSQL**.\
   \
   For every subscription, the service tracks all sent notifications to allow detection of delivery errors. The delivery is carried out by workers, the number of whom can be adjusted for each topic based on the anticipated number of subscriptions and available resources.\
   \
   The requests for **Subscriptions** are created by clients with [create](https://www.hl7.org/fhir/http.html#create) operations.\
   \
   **Subscriptions** are initiated in the requested status. after which the handshake procedure is started. Following this, the handshake procedure begins. If successful, the subscription is activated, and corresponding events begin to be delivered from that point onward. These events are grouped into a single notification based on the `maxCount` and `heartbeatPeriod` fields of the subscriptions.

### Supported FHIR versions

Aidbox Supports Topic-Based Subscriptions for both **R5** and **R4B** (4.3.0) versions. To work with the R4B version [Resource Profile: R4/B Topic-Based Subscription ](https://build.fhir.org/ig/HL7/fhir-subscription-backport-ig/StructureDefinition-backport-subscription.html)is required.

### Currently supported features

* [**Multi-Resource Topics**](https://build.fhir.org/subscriptions.html#multi-resource-topics)
* [**FHIRPath Trigger Definitions**](https://build.fhir.org/subscriptiontopic.html#fhirpath-criteria) **and** [**Resource Interactions**](https://build.fhir.org/subscriptiontopic.html#resource-operation-pairs)
* [**Defining Allowed Filters**](https://build.fhir.org/subscriptiontopic.html#filters)
* **`empty`, `id-only`, and `full-resource`** [**Payload Types**](https://build.fhir.org/subscription.html#payloads)
* [**Batching Results**](https://build.fhir.org/subscription.html#batching)
* [**REST-Hook**](https://build.fhir.org/subscription.html#rest-hook) **Channel**
* **`handshake`** , **`heartbeat`** , **`event-notification`** [**notification types**](https://build.fhir.org/subscriptionstatus.html#notification-types)
* [**Event Numbering**](https://build.fhir.org/subscriptionstatus.html#event-numbering)
* [**`$events` operation**](https://build.fhir.org/subscription-operation-events.html) **and** [**`$status` operation**](https://build.fhir.org/subscription-operation-status.html)

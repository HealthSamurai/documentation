---
title: "Aidbox Subscriptions: A comprehensive overview of data integration, challenges, and solutions"
slug: "subscriptions-on-fhir-overview-of-data-integration-challenges-and-solutions"
published: "2024-09-19"
author: "Marat Surmashev"
reading-time: "8 min read"
tags: []
category: "FHIR"
teaser: "Learn how subscriptions simplify data management with automated, event-driven workflows. They improve care coordination, decision-making, and streamline processes like data syncing and automation. Plus, Aidbox offers reliable, scalable solutions for both simple and high-load systems."
image: "cover.png"
---

Subscriptions offer a standardized way to establish automated, event-driven data flows, allowing them to stay informed about important changes in data. This capability opens up new possibilities for improving care coordination, enhancing clinical decision support, and streamlining workflows across the healthcare ecosystem.

Common use cases for subscriptions:

- **Data replication:** Automatically propagate data changes to queue or message-based services.
- **Data aggregation:** Collect and centralize data from multiple FHIR server sources into a central Clinical Data Repository (CDR).
- **Data replication to analytical systems:** Push data to external systems for analytics or reporting.
- **Workflow automation:** Trigger workflows based on specific conditions.
- **System extension and customization:** Extend your existing system functionality. For example, a *Task* resource can be created automatically when an *Appointment* is created.
- **User-specific notifications:** Enable end-users to subscribe to specific events, like receiving email when an *Appointment* is canceled.

## Key objectives for subscription implementation

When implementing subscriptions, three fundamental objectives must be addressed:

1. **Event handling:** Efficiently capturing changes at either the application or database level.****
2. **Race conditions:** Ensuring events are processed correctly to avoid inconsistencies ****
3. **Delivery guarantees:** Guaranteeing the delivery of events to ensure data reliability.

## Event handling: application and database-level capture

#### **Application-level capture**

This approach involves capturing events only through the API on the application layer. While it's simpler to implement, this approach does not capture data migrations or bulk data ingestion at the database level. This can lead to missed events if large-scale data updates occur outside the application’s API.

#### **Database-level capture**

This approach captures all possible changes directly from the database, offering a more complete solution. However, it is more complex and expensive to implement and maintain. Additionally, granular event filtering can be challenging and may require post-processing to filter out unnecessary events.

## Race conditions

Maintaining the correct order of events is critical, especially in scenarios where concurrent updates might occur. Consider a situation where two concurrent requests update the same *Appointment*:

- *Request\_A* sets *Appointment.status: 'canceled'*

- *Request\_B* sets *Appointment.status: 'pending'*

If events are handled at the application level in separate threads or across different application instances, the events might be processed out of order. For instance, the event sequence could appear as: *status = pending,* *status = canceled.*

However, the actual commit order in the database could be *status = canceled*, *status = pending*

As a result, the final state of the database shows *pending*, but the last event processed by the subscriber shows *canceled*.

To solve this problem, there are the following options:

#### **Single-threaded, Single-instance Execution**

This may be feasible for applications with minimal workloads, but it's not a scalable solution. High-performance systems typically require multi-threading and multi-replica setups for performance and high availability.

#### **Optimistic locking**

Use optimistic locking with the FHIR `If-None-Match: W/"[ETag]"` header to manage updates. However, this is often inconvenient and impractical, especially when you can’t modify the original request.

#### **Database-level serialization**

Use database-level mechanisms to ensure event serialization. For example, in PostgreSQL, a replication slot can guarantee that all events are processed sequentially in commit order. However, this approach has significant drawbacks, including performance bottlenecks on huge workloads, as the replication slot can only be read by one thread. Additionally, maintaining this setup can be complex.

## Delivery guarantees

There are three levels of delivery guarantees that need to be considered when implementing subscriptions:

#### **At Most Once**

Events are sent without requiring acknowledgment of receipt. This approach is suitable in scenarios where events are generated at a high rate and resource utilization needs to be minimized.

#### **At Least Once**

This approach guarantees that each event will be delivered at least once, even in case of failures, though duplicates may occur. It is suitable for critical operations, such as system-level data changes where reliability is essential, but duplicates should be managed on the receiving side.

#### **Exactly Once**

In this model, the system guarantees that each event is delivered exactly once, ensuring both reliability and avoiding duplicates. This level of guarantee requires support on both the sending and receiving systems and can be more resource-intensive to implement.

## Aidbox capabilities

The [Aidbox FHIR Platform](https://www.health-samurai.io/aidbox) provides several implementations of subscriptions:

| Implementation | Event Handling | Race Condition | Guarantee | Channel |
| --- | --- | --- | --- | --- |
| [Aidbox Subscriptions](https://docs.aidbox.app/api-1/reactive-api-and-subscriptions/subscriptions-1) | Application | Possible | At most once | Webhook |
| [FHIR topic-based Subscriptions](https://docs.aidbox.app/modules-1/topic-based-subscriptions/topic-based-subscriptions) | Database | Not Possible | At least once | Webhook |
| [Aidbox topic-based Subscriptions](https://docs.aidbox.app/modules-1/topic-based-subscriptions/wip-dynamic-subscriptiontopic-with-destinations) | Application | Possible | At least once At most once | Kafka Webhook (coming soon) |

> [Aidbox Subscriptions](https://docs.aidbox.app/api-1/reactive-api-and-subscriptions/subscriptions-1) — Best for simple, non-critical interactions between services.
>
> - Easy to use with minimal setup.
> - No strict delivery guarantees are suitable when message loss is acceptable.
> - Ideal for lightweight systems where simplicity is a priority.

> [FHIR topic-based subscriptions](https://docs.aidbox.app/modules-1/topic-based-subscriptions/topic-based-subscriptions) — Best for systems that have serious requirements for consistency and delivery but don't operate under huge workloads.
>
> - FHIR compliant.
> - No race conditions with delivery guarantees.

> [Aidbox topic-based subscriptions](https://docs.aidbox.app/modules-1/topic-based-subscriptions/wip-dynamic-subscriptiontopic-with-destinations) — Best for high-load systems requiring scalability and delivery guarantees.
>
> - Integrates with Kafka for high-throughput messaging.
> - Maintains high performance under heavy load.
> - Crucial for applications where data loss is unacceptable.

##  Examples

- Aidbox topic-based subscriptions demo — <https://github.com/Aidbox/app-examples/tree/main/aidbox-subscriptions-to-kafka>
- FHIR topic-based subscriptions — <https://github.com/Aidbox/aidbox-project-template/tree/topic-based-subscription-r4b-fhir-schema-mode>[](https://github.com/Aidbox/aidbox-project-template/tree/topic-based-subscription-r4b-fhir-schema-mode)

## The Future of Aidbox Subscriptions

[Aidbox Topic-based Subscriptions](https://docs.aidbox.app/modules-1/topic-based-subscriptions/wip-dynamic-subscriptiontopic-with-destinations) are becoming the go-to solution for real-time notifications and updates in healthcare applications, providing an ideal blend of simplicity, scalability, and reliability.

We’re dedicated to continually improving Aidbox Topic-Based Subscriptions by expanding AidboxTopicDestination to support additional channels like webhooks, RabbitMQ, Amazon SNS, and more. We're also working on an Event (or Message) API, similar to Amazon SNS, to offer even greater flexibility for your integration needs.

> [Reach out to us](https://www.health-samurai.io/contacts) with your use cases and requirements or get started with Aidbox topic-based subscriptions for free with [our development licenses](https://aidbox.app/ui/portal#/signin).

*Author*:   
[**Marat Surmashev**](https://www.linkedin.com/in/marat-surmashev-1b852b59/?originalSubdomain=me),   
VP of Engineering at Health Samurai

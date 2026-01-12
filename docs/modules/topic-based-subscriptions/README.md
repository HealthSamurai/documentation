---
description: FHIR Subscriptions Subscriptions for real-time notifications and event-driven workflows.
---

# Subscriptions

Subscriptions enable real-time notifications and updates between servers and clients. This mechanism allows to respond dynamically to changes in patient data or resource states.\
\
By leveraging the subscriptions, developers can specify notification criteria, define delivery channels, and monitor specific events or changes in healthcare data. This push-based approach minimizes the need for constant polling, optimizing resource utilization and improving the timeliness of information dissemination.\
\
Aidbox provides several implementations of subscriptions. For enhanced capabilities and ongoing support, please use Aidbox topic-based subscriptions. This newer implementation offers improved performance, flexibility, and will continue to be developed to meet future needs.

<table><thead><tr><th width="215">Implementation</th><th>Delivery guarantee</th><th>Channels</th><th>Status</th></tr></thead><tbody><tr><td><a href="fhir-topic-based-subscriptions.md">FHIR Topic-Based Subscriptions</a></td><td>At least once</td><td>REST-hook</td><td>Maintained</td></tr><tr><td><a href="aidbox-topic-based-subscriptions">Aidbox topic-based subscriptions</a></td><td><ul><li>At most once</li><li>At least once</li></ul></td><td><ul><li>Kafka</li><li>Webhook</li><li>GCP Pub/Sub</li><li>NATS</li><li>AMQP message broker</li></ul>*additonal channels will be implemented by request</td><td>Maintained</td></tr><tr><td><a href="./aidbox-subsubscriptions.md">Aidbox SubSubscriptions</a></td><td>At most once</td><td>Webhook</td><td>Deprecated</td></tr><tr><td><a href="../../deprecated/deprecated/zen-related/topic-based-subscriptions/">FHIR topic-based subscriptions (Zen)</a></td><td>At least once</td><td>Webhook</td><td>Deprecated</td></tr></tbody></table>

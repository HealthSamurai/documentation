# Subscriptions

Subscriptions enable real-time notifications and updates between servers and clients. This mechanism allows to respond dynamically to changes in patient data or resource states.\
\
By leveraging the subscriptions, developers can specify notification criteria, define delivery channels, and monitor specific events or changes in healthcare data. This push-based approach minimizes the need for constant polling, optimizing resource utilization and improving the timeliness of information dissemination.\
\
Aidbox provides several implementations of subscriptions. We recommend using Aidbox's topic-based subscriptions for most use cases.

<table><thead><tr><th width="296">Implementation</th><th width="135">Race conditions</th><th>Delivery quarantee</th><th>Channels</th></tr></thead><tbody><tr><td><a href="../../api-1/reactive-api-and-subscriptions/subscriptions-1.md">Aidbox SubSubscriptions</a></td><td>Possible</td><td>At most once</td><td>Webhook</td></tr><tr><td><a href="topic-based-subscriptions/">FHIR topic-based subscriptions</a></td><td>Not possible</td><td>At least once</td><td>Webhook</td></tr><tr><td><a href="wip-dynamic-subscriptiontopic-with-destinations/">Aidbox topic-based subscriptions</a></td><td>Possible</td><td><ul><li>At most once</li><li>At least once</li></ul></td><td>Kafka<br>*additonal channels will be implemented by request</td></tr></tbody></table>




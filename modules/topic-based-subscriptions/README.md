# Subscriptions

Subscriptions enable real-time notifications and updates between servers and clients. This mechanism allows to respond dynamically to changes in patient data or resource states.\
\
By leveraging the subscriptions, developers can specify notification criteria, define delivery channels, and monitor specific events or changes in healthcare data. This push-based approach minimizes the need for constant polling, optimizing resource utilization and improving the timeliness of information dissemination.\
\
Aidbox provides several implementations of subscriptions. For enhanced capabilities and ongoing support, please use Aidbox topic-based subscriptions. This newer implementation offers improved performance, flexibility, and will continue to be developed to meet future needs.

<table><thead><tr><th width="296">Implementation</th><th width="135">Race conditions</th><th>Delivery quarantee</th><th>Channels</th></tr></thead><tbody><tr><td><a href="subscriptions-1.md">Aidbox SubSubscriptions</a></td><td>Possible</td><td>At most once</td><td>Webhook</td></tr><tr><td><a href="topic-based-subscriptions/">FHIR topic-based subscriptions</a></td><td>Not possible</td><td>At least once</td><td>Webhook</td></tr><tr><td><a href="wip-dynamic-subscriptiontopic-with-destinations/">Aidbox topic-based subscriptions</a></td><td>Possible</td><td><ul><li>At most once</li><li>At least once</li></ul></td><td><p>Kafka</p><p>Webhook</p><p>GCP Pub/Sub<br>*additonal channels will be implemented by request</p></td></tr></tbody></table>




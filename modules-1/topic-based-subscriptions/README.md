# Subscriptions

Subscriptions enable real-time notifications and updates between servers and clients. This mechanism allows to respond dynamically to changes in patient data or resource states, enhancing the overall efficiency of clinical workflows. \
\
By leveraging the subscriptions, developers can specify criteria for notifications, define delivery channels, and monitor specific events or changes in healthcare data. This push-based approach minimizes the need for constant polling, thereby optimizing resource utilization and improving the timeliness of information dissemination.

<table><thead><tr><th width="300">Implementation</th><th>Race conditions</th><th>Delivery quarantee</th><th>Destinations</th></tr></thead><tbody><tr><td><a href="../../api-1/reactive-api-and-subscriptions/subscriptions-1.md">Aidbox SubSubscriptions</a></td><td>Possible</td><td>At Most Once</td><td>Web Hook</td></tr><tr><td><a href="topic-based-subscriptions/">FHIR Topic-based Subscriptions</a></td><td>Not possible</td><td>At Least Once</td><td>Web Hook</td></tr><tr><td><a href="wip-dynamic-subscriptiontopic-with-destinations/">Aidbox Topic-based Subscriptions</a></td><td>Possible</td><td>At Most Once<br>At Least Once</td><td>Kafka</td></tr></tbody></table>

{% content-ref url="../../api-1/reactive-api-and-subscriptions/subscriptions-1.md" %}
[subscriptions-1.md](../../api-1/reactive-api-and-subscriptions/subscriptions-1.md)
{% endcontent-ref %}

{% content-ref url="topic-based-subscriptions/" %}
[topic-based-subscriptions](topic-based-subscriptions/)
{% endcontent-ref %}

{% content-ref url="wip-dynamic-subscriptiontopic-with-destinations/" %}
[wip-dynamic-subscriptiontopic-with-destinations](wip-dynamic-subscriptiontopic-with-destinations/)
{% endcontent-ref %}


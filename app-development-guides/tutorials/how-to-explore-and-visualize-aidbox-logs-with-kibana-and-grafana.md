---
description: >-
  This tutorial will help you explore logs in Kibana and visualize logs in
  Grafana
---

# Log analysis and visualization

## Update devbox

## Update Docker Image

You may want to track logs in AidBox with Kibana. You can do anything from tracking query load to understanding the way requests flow through your apps.   


To begin using pre-configured Kibana and Grafana please clone[ this repository](https://github.com/Aidbox/devbox) and execute `make up` operation in your shell:

## **Explore logs with Kibana**

```bash
git clone https://github.com/Aidbox/devbox.git
cd devbox
make up
```

## **Explore logs with Kibana**

To get deeper understanding of Aidbox logs please go to the [log documentation](https://docs.aidbox.app/core-modules/logging-and-audit) and explore logs schema available there.

{% hint style="info" %}
Aidbox Elastic appender sends logs as bundles of multiple records so if there's not enough data **you won't see any logs in Kibana**. For testing purposes reduce bundle size to 1 record by setting environment variable:

```yaml
AIDBOX_ES_BATCH_SIZE=1
```

Aidbox Elastic appender documentation is available [here](../receive-logs-from-your-app/elastic-logs-and-monitoring-integration.md#elasticsearch-logging).
{% endhint %}



Example below will help you read REST API logs:

Please put + to toggle `Time`, `w_m`, `w_r`_,_`w_url`, `d`, `w_st`, `w_uid` fields into the document table:

![](../../.gitbook/assets/image%20%2840%29.png)

Now you can browse and explore REST API logs

![](../../.gitbook/assets/image%20%287%29.png)

_Kibana's **Discover** enables you to quickly search and filter your data, get information about the structure of the fields, and visualize your data with **Lens** and **Maps**. You can customize and save your searches and place them on a dashboard._

Check [Kibana documentation](https://www.elastic.co/guide/en/kibana/current/discover.html) for additional info or [let us know](https://t.me/aidbox) if you need help.

## **Visualize logs with Grafana**

## **Visualize logs with Grafana**

We've built a custom Grafana dashboard available by default where you can see basic metrics. 

The pre-configured views are the following:

1. Requests per minute.
2. Request duration heatmap.
3. Number of requests per day.
4. Clients/users online.
5. DB size.
6. Number of errors. 
7. Response time.  
8. Slow queries.

![Aidbox Grafana dashboard](../../.gitbook/assets/image%20%2851%29.png)

You can update the default dashboard with your own custom views. For more info please refer to [Grafana documentation](https://grafana.com/tutorials/grafana-fundamentals/).

If you have any questions, requests, or want to share your best practices using Kibana and Grafana feel free to contact us via [Aidbox user chat.](https://t.me/aidbox)

You can easily add your own custom views on your dashboards using Grafana. For more info please go to Grafana documentation. 

If you have any questions, requests or problems regarding Aidbox log monitoring, **please contact us via Aidbox user chat.**


---
description: >-
  This tutorial will help you explore logs in Kibana and visualize logs in
  Grafana
---

# Log analysis and visualization

## Update Docker Image

To begin using pre-congigured Kibana and Grafana please [pull this repository](https://github.com/Aidbox/devbox/blob/master/docker-compose.yaml) and execute `make up` operation in your shell.

## **Explore logs with Kibana**

To get deeper understanging of Aidbox logs please go to Aidbox [log documentation](https://docs.aidbox.app/core-modules/logging-and-audit) and explore logs schema available there.

For example, using the listed attributes will help you read REST API logs.

Please put + to toggle `Time`, `w_m`, `w_r`_,_`w_url`, `d`, `w_st`, `w_uid` fields into the document table:

![](../../.gitbook/assets/image%20%287%29.png)

_Kibana's **Discover** enables you to quickly search and filter your data, get information about the structure of the fields, and visualize your data with **Lens** and **Maps**. You can customize and save your searches and place them on a dashboard._

Check [Kibana documentation](https://www.elastic.co/guide/en/kibana/current/discover.html) for additional info or let us know if you need help.

## **Visualize logs with Grafana**

We've built a custom Grafana dashboard that can be available by default. So now you can see basic metrics out of the box. 

The pre-configured views are the following:

1. Requests per minute.
2. Request duration heatmap.
3. Number of requests per day.
4. Clients/users online.
5. DB size.
6. Number of errors. 
7. Response time.  
8. Slow quires.

![Aidbox Grafana dashboard](../../.gitbook/assets/image%20%2851%29.png)

You can update the default dashboard with your own custom views. Please read [Grafana documentaton](https://grafana.com/tutorials/grafana-fundamentals/) for more info.

If you have any questions, requests, or want to share your best practices using Kibana and Grafana feel free to contact us via [Aidbox user chat.](https://t.me/aidbox)


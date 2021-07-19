---
description: >-
  This tutorial will help you explore logs in Kibana and visualize logs in
  Grafana
---

# Log analysis and visualization

## **Install** 

You may want to track logs in AidBox with Kibana, and Grafana. You can do anything from tracking query load to understanding the way requests flow through your apps.   


We prepared for you docker-compose.yaml file with Elastic search, Kibana and Grafana pre-configured. For log tracking, you only have to [pull this repository](https://github.com/Aidbox/devbox/blob/master/docker-compose.yaml) and execute `make up` operation in your shell.   


Subsequently, please open the localhost address in the browser. You can look up the address in docker-compose.

## **Explore with Kibana**

To start with Kibana please go to Aidbox log documentation \([Logging & Audit](https://docs.aidbox.app/core-modules/logging-and-audit)\) and explore logs schema available here with brief attribute description.

Using the listed attributes helps you to read REST logs in Elastic search filtering them type by type.

Please choose `Time`, `w_m`, `w_r`_,_`w_url`, `d`, `w_st`, `w_uid` parameters to filter view REST logs in Elastic:

![Elastic Search REST logging](../../.gitbook/assets/image%20%287%29.png)

_Kibana's **Discover** enables you to quickly search and filter your data, get information about the structure of the fields, and visualize your data with **Lens** and **Maps**. You can customize and save your searches and place them on a dashboard._ 

Check [Kibana's documentation](https://www.elastic.co/guide/en/kibana/current/discover.html) for further info.

## **Visualize with Grafana**

_Query, visualize, alert on, and understand your data no matter where it’s stored. With Grafana you can create, explore and share all of your data through beautiful, flexible dashboards._©

We build custom dashboards that can be avalible default. This dashboard visualizes logs. Heatmap illustrates buckets with color grading correlating with request number.

![Grafana UI logging](../../.gitbook/assets/image%20%2851%29.png)

This dashboard aggregate Response Time on different user operations

![](https://lh5.googleusercontent.com/SFFHXVdIj6WP3afHK5sHtDaWmFSVC15Mez7gWxYde1_ozuOWluL47gsaCgiUK8ia6wiVVyM3vEU8JUFOM2NDAx5n-BaHc0pGZpUjL2M0jWXV5Y1Z6-HLEPfMZgHojX-NVLEDn3bn)

To review slow requests:

![Response Time logging](../../.gitbook/assets/image%20%2846%29.png)

Please read [Grafanas’s documentation](https://grafana.com/tutorials/grafana-fundamentals/?pg=docs) to be comfortable with its interface and operate freely. Message us if you have any troubles or watch a video.  



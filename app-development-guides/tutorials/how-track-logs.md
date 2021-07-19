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

Go to Aidbox log documentation \([Logging & Audit](https://docs.aidbox.app/core-modules/logging-and-audit)\). 

Logs schema available here with brief attribute description.

Using the listed attributes helps you to read REST logs in Elastic search filtering them type by type.

![Elastic Search REST logging](../../.gitbook/assets/image%20%287%29.png)

Precompiled log dashboard for Grafana is available freely and intuitive to read.  


## **Visualize with Grafana**

This dashboard aggregate Response Time on different user operations

![](https://lh5.googleusercontent.com/SFFHXVdIj6WP3afHK5sHtDaWmFSVC15Mez7gWxYde1_ozuOWluL47gsaCgiUK8ia6wiVVyM3vEU8JUFOM2NDAx5n-BaHc0pGZpUjL2M0jWXV5Y1Z6-HLEPfMZgHojX-NVLEDn3bn)

This dashboard heatmap illustrates buckets with color grading correlating with request number.

![Grafana UI logging](../../.gitbook/assets/image%20%2843%29.png)

To review slow requests:

![Response Time logging](../../.gitbook/assets/image%20%2846%29.png)

Please read Grafanas’s documentation to be comfortable with its interface and operate freely. Message us if you have any troubles or watch a video.  



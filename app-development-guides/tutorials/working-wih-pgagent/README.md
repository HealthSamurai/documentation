---
description: You can create pgagent jobs from pgadmin interface
---

# Scheduling a job in pgAdmin

{% hint style="info" %}
Refer to [https://www.pgadmin.org/](https://www.pgadmin.org/) for pgAdmin and pgAgent documentation
{% endhint %}

Create server connection  

![](../../../.gitbook/assets/screen-shot-2021-07-15-at-18.06.35.png)

{% hint style="warning" %}
It's important that **Maintenance database** is the database `pgagent` connected to.  
By default it's `postgres` database, you can control it with env variables, more info at [aidboxdb image docs](../../../getting-started/installation/aidboxdb-image.md#optional-environment-variables).
{% endhint %}

Now you can schedule a job as described at [pgAgent docs](https://www.pgadmin.org/docs/pgadmin4/development/pgagent_jobs.html).

![](../../../.gitbook/assets/screen-shot-2021-07-15-at-18.10.18.png)


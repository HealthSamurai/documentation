---
description: >-
  This page describes which notifications can be sent from the MPI to external
  systems and how to configure their delivery.
hidden: true
---

# Notifications for External Systems

### Merge Notifications



### Unmerge Notifications



### Notification Settings

{% embed url="https://github.com/Aidbox/helm-charts/tree/main/mpi" %}

Values:

<pre class="language-yaml"><code class="lang-yaml"><strong>  MPI_NOTIFICATION_WORKER_ENABLE: false # turn on or turn off notification worker
</strong>  MPI_NOTIFICATION_CONSUMER_URL: http://localhost:9876
  MPI_NOTIFICATION_INTERVAL: 1000 # time interval between sending notifications (in ms)
  MPI_NOTIFICATION_BATCH_SIZE: 10
  MPI_NOTIFICATION_LOCK_ID: 12345 # will be used for pg_try_advisory_xact_lock, this is needed to avoid race condition or deadlock between mpi instances (should be the same for all mpi instances and different from other lock ids)
</code></pre>


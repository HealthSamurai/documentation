---
description: This section describes how to integrate your Aidbox with Datadog.
---

# Datadog Log management integration

[Datadog](https://www.datadoghq.com/) offers cloud-based monitoring and analytics platform which integrates and automates infrastructure monitoring, application performance monitoring, and log management for real-time observability of customers.

## Datadog logging

{% hint style="info" %}
Please, do not use these settings with`AIDBOX_ES_URL` \([Elastic Logs and Monitoring](elastic-logs-and-monitoring-integration.md)\) enabled. These are two alternative approaches for log management.
{% endhint %}

To enable Aidbox logs uploading into Datadog, you need to set `AIDBOX_DD_API_KEY` environment variable.

```yaml
AIDBOX_DD_API_KEY = <Datadog API Key>
# Required.
# Datadog API Key.

AIDBOX_DD_BATCH_SIZE
# Optional; default: 200.
# How many log entries to collect before uploading.
# Aidbox uploads logs when either at least AIDBOX_DD_BATCH_SIZE entries collected 
# or time passed from previous log uploading exceeds AIDBOX_BATCH_TIMEOUT

AIDBOX_DD_BATCH_TIMEOUT
# Optional; default: 3600000 (1 hour)
# How long to wait before uploading
# Aidbox uploads logs when either at least AIDBOX_DD_BATCH_SIZE entries collected 
# or time passed from previous log uploading exceeds AIDBOX_BATCH_TIMEOUT

AIDBOX_DD_LOGS
# Optional.
# Path to file in which logs will be written in case of error 
# in uploading logs to Datadog.
# If not provided, in case of uploading error logs will be printed to stdout.

AIDBOX_DD_TAGS
# Optional.
# Tags associated with your logs.
# Convenient for transferring the name of the environment.
# For example "env:staging"
```

{% hint style="warning" %}
By default, Aidbox sends logs as bundles of multiple records so if there's not enough data **you won't see any logs in Datadog**. For testing purposes reduce bundle size to 1 record by setting environment variable:

AIDBOX\_DD\_BATCH\_SIZE=1
{% endhint %}


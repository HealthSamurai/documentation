---
description: This section describes how to integrate your Aidbox with Loki.
---

# Loki Log management integration

[Loki](https://grafana.com/oss/loki/) is a log aggregation system designed to store and query logs.

## Loki logging

To enable Aidbox logs uploading into Loki, you need to set `AIDBOX_LK_URL` environment variable.

```yaml
AIDBOX_LK_URL
# Required

AIDBOX_LK_BATCH_SIZE
# Optional; default: 200.
# How many log entries to collect before uploading.
# Aidbox uploads logs when either at least AIDBOX_LK_BATCH_SIZE entries collected 
# or time passed from previous log uploading exceeds AIDBOX_LK_BATCH_TIMEOUT

AIDBOX_LK_BATCH_TIMEOUT
# Optional; default: 3600000 (1 hour)
# How long to wait before uploading
# Aidbox uploads logs when either at least AIDBOX_LK_BATCH_SIZE entries collected 
# or time passed from previous log uploading exceeds AIDBOX_LK_BATCH_TIMEOUT
```

{% hint style="warning" %}
By default, Aidbox sends logs as bundles of multiple records so if there's not enough data **you won't see any logs in Loki**. For testing purposes reduce bundle size to 1 record by setting environment variable:

AIDBOX\_LK\_BATCH\_SIZE=1
{% endhint %}

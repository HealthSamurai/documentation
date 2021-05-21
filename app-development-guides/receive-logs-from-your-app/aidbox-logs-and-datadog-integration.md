# Aidbox Logs and Datadog integration

### Datadog logging

To enable Aidbox logs upload to Datadog, you need to set `AIDBOX_DD_API_KEY` environment variable.

```text
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
```


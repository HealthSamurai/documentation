---
description: Configure Aidbox to use a proxy for outgoing requests.
---

# How to configure Aidbox to use a proxy for outgoing requests

## Objectives

* Configure Aidbox to use a proxy for outgoing requests.

## Before you begin

* You must have an active proxy server.

## Configure Aidbox to use a proxy for outgoing requests

To configure Aidbox to use a proxy for outgoing requests, you need to set the following environment variable:

```yaml
JAVA_OPTS: -Dhttps.proxyHost=proxyhost -Dhttps.proxyPort=proxyPort
```
Please note that this configuration will proxy **all outbound** requests from Aidbox, including things like Subscriptions with webhook destinations.

See also:

* https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/net/doc-files/net-properties.html

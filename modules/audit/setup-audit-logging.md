---
description: >-
  This page explains how to configure Aidbox to record audit events that occur
  within the system
---

# Configure Audit Log

This guide shows you how to enable audit logging in Aidbox and receive audit logs within FHIR API and Audit log viewer UI application. It is expected, that you have Aidbox up & running in accordance to [Run Aidbox locally guide](../../getting-started/run-aidbox-locally-with-docker/).

### Two ways to enable Audit Log

1. **Environment variable**

To enable audit logging in Aidbox set the following variable to `true`. The default value is `false`.

```
AIDBOX_SECURITY_AUDIT__LOG_ENABLED=true
```

2. **Aidbox configuration project**

&#x20;If you use aidbox configuration project describe `:audit` in your `aidbox/system` entry point:

{% code title="zrc/main.edn" %}
```clojure
{ns main
 import #{aidbox}
 
 box
 {:zen/tags #{aidbox/system}
  :audit {:storage "AuditEvent"} ;; Add this line to your aidbox project
  }}
```
{% endcode %}

Once the configuration is updated, start Aidbox.

## Run some auditable operations

To force Aidbox produce audit events, run any FHIR CRUD operation, e.g.

```yaml
POST /fhir/Patient
content-type: text/yaml
accept: text/yaml

name:
- given: [John]
  family: Smith
  
# 201 Created
```

## Find audit logs with FHIR API

To see audit logs with FHIR API, run `GET /fhir/AuditEvent?_sort=-createdAt`

## Find audit logs with Audit log viewer application

To see audit logs with Audit event viewer app, go to Aidbox Console UI → Audit Log.

<figure><img src="../../.gitbook/assets/Screenshot 2024-09-23 at 15.19.38.png" alt=""><figcaption></figcaption></figure>

And find the audit event, produced by patient create operation.

<figure><img src="../../.gitbook/assets/Screenshot 2023-09-07 at 12.58.32.png" alt=""><figcaption></figcaption></figure>

## Talk to a Health Samurai Engineer

If you'd like to learn more about using Aidbox or have any questions about this guide, [connect with us on Telegram](https://t.me/aidbox). We're happy to help.

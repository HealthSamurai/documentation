---
description: >-
  This page explains how to configure Aidbox to record audit events that occur
  within the system
---

# Configure Audit Log

This guide shows you how to enable audit logging in Aidbox and receive audit logs within FHIR API and Audit log viewer UI application. It is expected, that you have Aidbox up & running in accordance to [Run Aidbox locally guide](../../getting-started-1/run-aidbox/run-aidbox-locally-with-docker.md).

## Enable audit logging in aidbox configuration project

To enable audit logging in Aidbox, import `aidbox.audit-record-repository` and describe `:audit` in your `aidbox/system` entry point:

{% code title="zrc/main.edn" %}
```clojure
{ns main
 import #{aidbox
          aidbox.audit-record-repository} ;; Add import aidbox.audit-record-repository
 
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
POST /Patient
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

To see audit logs with Audit event viewer app, go to Aidbox Console UI → Audit events.

<figure><img src="../../.gitbook/assets/Screenshot 2023-09-07 at 12.39.24 (2).png" alt=""><figcaption></figcaption></figure>

And find the audit event, produced by patient create operation.

<figure><img src="../../.gitbook/assets/Screenshot 2023-09-07 at 12.58.32.png" alt=""><figcaption></figcaption></figure>

## Talk to a Health Samurai Engineer

If you'd like to learn more about using Aidbox or have any questions about this guide, [connect with us on Telegram](https://t.me/aidbox). We're happy to help.

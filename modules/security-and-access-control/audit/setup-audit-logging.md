---
description: >-
  This page explains how to configure Aidbox to record audit events that occur
  within the system
---

# Configure Audit Log

This guide shows you how to enable audit logging in Aidbox and receive audit logs within FHIR API and Audit log viewer UI application. It is expected, that you have Aidbox up & running in accordance to [Run Aidbox locally guide](../../../getting-started/run-aidbox-locally-with-docker.md).

### Enable Audit Log

To enable audit logging in Aidbox, set the following variable to `true`.

```
AIDBOX_SECURITY_AUDIT__LOG_ENABLED=true
```

### Install the required package

The Basic Audit Log Patterns (BALP) Implementation Guide is a Content Profile that defines some basic and reusable AuditEvent patterns. This includes basic audit log profiles for FHIR RESTful operations to be used when no more specific audit event is determined.

```
AIDBOX_FHIR_PACKAGES="<hl7.fhir...>:ihe.iti.balp#1.1.3"
```

[More about FHIR package import via environment variables.](../../profiling-and-validation/fhir-schema-validator/upload-fhir-implementation-guide/environment-variable/)

You can also install it on the fly through [Aidbox UI](../../profiling-and-validation/fhir-schema-validator/upload-fhir-implementation-guide/aidbox-ui/ig-package-from-aidbox-registry/).

## Run some auditable operations

To force Aidbox to produce audit events, run any FHIR CRUD operation, e.g.

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

<figure><img src="../../../.gitbook/assets/Screenshot 2024-09-23 at 15.19.38.png" alt=""><figcaption></figcaption></figure>

And find the audit event, produced by patient create operation.

<figure><img src="../../../.gitbook/assets/Screenshot 2023-09-07 at 12.58.32.png" alt=""><figcaption></figcaption></figure>

## Talk to a Health Samurai Engineer

If you'd like to learn more about using Aidbox or have any questions about this guide, [connect with us on Telegram](https://t.me/aidbox). We're happy to help.

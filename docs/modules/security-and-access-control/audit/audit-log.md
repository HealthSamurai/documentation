---
description: This page describes Aidbox audit log module
---

# Audit Log

### Aidbox as a source of audit events

When audit logging is enabled, Aidbox produces audit logs for significant events:

* FHIR CRUD & Search operations for basic FHIR resources and custom resources
* FHIR CRUD & Search operations for patient compartment FHIR resources and custom resources
* \[WIP] Authentication & Authorization events (login, logout, SMART on FHIR authorization, etc)
* \[WIP] Security & configuration updates

Aidbox produces audit logs in conformance with [Basic Audit Log Patterns IG](https://profiles.ihe.net/ITI/BALP/).

{% hint style="warning" %}
At this time, Aidbox is only capable of producing `AuditEvent` in the `FHIR R4` version.
{% endhint %}

### Aidbox as an Audit record repository

Aidbox acts as [Audit record repository](https://profiles.ihe.net/ITI/TF/Volume1/ch-9.html#9.1.1.3) (ARR) for FHIR AuditEvent resources. Aidbox supports

* `POST /fhir/AuditEvent` to record events
* `GET /fhir/AuditEvent` to receive them

<figure><img src="../../../.gitbook/assets/Screenshot%202023-09-07%20at%2013.23.08.png" alt=""><figcaption></figcaption></figure>

## Next steps

{% content-ref url="../../../tutorials/security-access-control-tutorials/how-to-configure-audit-log.md" %}
[how-to-configure-audit-log.md](../../../tutorials/security-access-control-tutorials/how-to-configure-audit-log.md)
{% endcontent-ref %}

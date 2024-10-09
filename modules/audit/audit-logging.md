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

* `POST /fhir/AuditEvent` to record events&#x20;
* `GET /fhir/AuditEvent` to receive them

<figure><img src="../../.gitbook/assets/Screenshot 2023-09-07 at 13.23.08.png" alt=""><figcaption></figcaption></figure>

## Next steps

{% content-ref url="setup-audit-logging.md" %}
[setup-audit-logging.md](setup-audit-logging.md)
{% endcontent-ref %}

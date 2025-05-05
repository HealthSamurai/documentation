# Audit & Logging

Audit logging is essential in healthcare systems because it:

* **Protects Patient Privacy**: Tracks who accessed sensitive medical records, ensuring compliance with privacy laws like HIPAA
* **Prevents Data Breaches**: Helps detect and investigate unauthorized access to patient data
* **Ensures Accountability**: Records all changes to medical records, creating a clear trail of who modified what and when
* **Supports Legal Requirements**: Provides evidence for compliance audits and legal investigations

Aidbox provides comprehensive audit and logging capabilities:

* FHIR Basic Audit Logging Profile (BALP) implementation
* FHIR Resource versioning
* Logging configuration

### FHIR Basic Audit Logging Profile (BALP) implementation

Aidbox supports the FHIR [BALP](https://profiles.ihe.net/ITI/BALP/index.html) Implementation Guide.

<figure><img src="../../.gitbook/assets/image (175).png" alt=""><figcaption></figcaption></figure>

#### Aidbox as a source of audit events

When audit logging is enabled, Aidbox produces audit logs for significant events:

* FHIR CRUD & Search operations for basic FHIR resources and custom resources
* FHIR CRUD & Search operations for patient compartment, FHIR resources, and custom resources
* \[WIP] Authentication & Authorization events (login, logout, SMART on FHIR authorization, etc)
* \[WIP] Security & configuration updates.

#### Aidbox as an Audit record repository

Aidbox is an [Audit record repository](https://profiles.ihe.net/ITI/TF/Volume1/ch-9.html#9.1.1.3) (ARR) for FHIR AuditEvent resources. Aidbox supports

* `POST /fhir/AuditEvent` to record events&#x20;
* `GET /fhir/AuditEvent` to receive them



See tutorial:

[setup-audit-logging.md](../../tutorials/security-access-control-tutorials/setup-audit-logging.md "mention")

## FHIR Resource versioning

A separate version is recorded in the history table each time a resource is created, updated, or deleted.

All versions can be accessed using the [\_history](../../api/rest-api/history.md) operation.

## Logging configuration

Aidbox automatically logs all auth, API, database, and network events, so in most cases, basic audit logs may be derived from [Aidbox logs](../observability/logging-and-audit/).

Aidbox also provides ways to [extend](../observability/logging-and-audit/extending-access-logs.md) Aidbox logs.


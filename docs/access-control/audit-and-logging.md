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

## FHIR Basic Audit Logging Profile (BALP) implementation

Aidbox supports the FHIR [BALP](https://profiles.ihe.net/ITI/BALP/index.html) Implementation Guide.

<figure><img src="../../.gitbook/assets/63f5c07d-e571-42d6-922e-b8b0e4c48000.png" alt=""><figcaption></figcaption></figure>

### Aidbox as a source of audit events

When audit logging is enabled, Aidbox produces audit logs for significant events:

* FHIR CRUD & Search operations for basic FHIR resources and custom resources
* FHIR CRUD & Search operations for patient compartment, FHIR resources, and custom resources
* Authentication & Authorization events (login, logout, SMART on FHIR authorization, etc)
* \[WIP] Security & configuration updates.

### Aidbox as an Audit record repository

Aidbox is an [Audit record repository](https://profiles.ihe.net/ITI/TF/Volume1/ch-9.html#9.1.1.3) (ARR) for FHIR AuditEvent resources. Aidbox supports

* `POST /fhir/AuditEvent` to record events
* `GET /fhir/AuditEvent` to receive them

### External Audit record repository support

Aidbox can also send Audit Events to a dedicated, external repository. In this case, Aidbox groups outgoing events into a single **FHIR Bundle** of type `collection` and delivers it to the target endpoint.

For setup instructions and payload examples, see the [**External Audit Repository Configuration**](../tutorials/security-access-control-tutorials/how-to-configure-audit-log.md#external-audit-repository-configuration) section of the guide.

## FHIR Resource versioning

A separate version is recorded in the history table each time a resource is created, updated, or deleted.

All versions can be accessed using the [\_history](../api/rest-api/history.md) operation.

## Logging configuration

Aidbox automatically logs all auth, API, database, and network events, so in most cases, basic audit logs may be derived from [Aidbox logs](../modules/observability/logs/).

Aidbox also provides ways to [extend](../modules/observability/logs/extending-aidbox-logs.md) Aidbox logs.

## See also:

{% content-ref url="../tutorials/security-access-control-tutorials/how-to-configure-audit-log.md" %}
[how-to-configure-audit-log.md](../tutorials/security-access-control-tutorials/how-to-configure-audit-log.md)
{% endcontent-ref %}

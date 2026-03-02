---
description: Track access to patient data with FHIR BALP audit events, resource versioning, and OpenTelemetry structured logging.
---

# Audit and Logging

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

<figure><img src="../.gitbook/assets/63f5c07d-e571-42d6-922e-b8b0e4c48000.png" alt="FHIR Basic Audit Logging Profile (BALP) implementation diagram"><figcaption></figcaption></figure>

### Aidbox as a source of audit events

When audit logging is enabled, Aidbox produces audit logs for significant events:

* FHIR CRUD & Search operations for basic FHIR resources and custom resources
* FHIR CRUD & Search operations for patient compartment, FHIR resources, and custom resources
* Authentication & Authorization events (login, logout, SMART on FHIR authorization, etc)
* \[WIP] Security & configuration updates.

### BALP profile selection

Aidbox assigns a BALP profile to each AuditEvent based on the operation type and whether the operation involves a Patient.

| Operation | Generic Profile | Patient-specific Profile |
|---|---|---|
| Create | `IHE.BasicAudit.Create` | `IHE.BasicAudit.PatientCreate` |
| Read / VRead | `IHE.BasicAudit.Read` | `IHE.BasicAudit.PatientRead` |
| Update / Patch | `IHE.BasicAudit.Update` | `IHE.BasicAudit.PatientUpdate` |
| Delete | `IHE.BasicAudit.Delete` | `IHE.BasicAudit.PatientDelete` |
| Search / Query | `IHE.BasicAudit.Query` | `IHE.BasicAudit.PatientQuery` |

**When Patient-specific profiles are used:**

* The resource **is** a Patient — CRUD operations directly on the Patient resource (e.g. `PUT /fhir/Patient/123`)
* The resource is in the [Patient Compartment](https://www.hl7.org/fhir/compartmentdefinition-patient.html) and references a Patient (e.g. creating an Observation with `subject` pointing to a Patient)

{% hint style="info" %}
Patient **search** (`GET /fhir/Patient?...`) uses the generic `IHE.BasicAudit.Query` profile, not `IHE.BasicAudit.PatientQuery`. This is because a search does not reference a specific Patient. The `PatientQuery` profile is used when searching compartment resources that reference a Patient (e.g. `GET /fhir/Observation?patient=123`).
{% endhint %}

#### Example: AuditEvent for Patient update

When you update a Patient resource, the generated AuditEvent uses the `IHE.BasicAudit.PatientUpdate` profile:

```json
{
  "resourceType": "AuditEvent",
  "meta": {
    "profile": [
      "https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.PatientUpdate"
    ]
  },
  "type": {
    "system": "http://terminology.hl7.org/CodeSystem/audit-event-type",
    "code": "rest",
    "display": "RESTful Operation"
  },
  "subtype": [
    {
      "system": "http://hl7.org/fhir/restful-interaction",
      "code": "update",
      "display": "update"
    }
  ],
  "action": "U",
  "recorded": "2026-02-25T12:00:00Z",
  "outcome": "0",
  "agent": [
    {
      "who": {
        "reference": "Client/my-client"
      },
      "requestor": true
    }
  ],
  "source": {
    "observer": {
      "display": "Aidbox"
    }
  },
  "entity": [
    {
      "what": {
        "reference": "Patient/example"
      },
      "role": {
        "system": "http://terminology.hl7.org/CodeSystem/object-role",
        "code": "1",
        "display": "Patient"
      }
    }
  ]
}
```

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

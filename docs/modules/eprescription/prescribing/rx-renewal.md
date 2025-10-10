# RxRenewal Message

### Overview

RxRenewal is a type of message sent by pharmacy to subscriber when the medication should be renewed.
It is required that RxRenewal is preceded by NewRx.

### Receiving RxRenewalRequest

Once the pharmacy sends an RxRenewalRequest, ePrescription module receives Surescripts message at `/eprescriptions/rx` endpoint and saves it to Aidbox.
Resources created:
- **MedicationRequest** reflecting the incoming data
- **DetectedIssue** - if only there were some mismatches in related resources
- **Provenance** - solely as a record that an event has occurred.
  It is intended to serve as an audit/logging artifact, and should not be relied upon for driving business logic.

There are several ways to track the newly created **MedicationRequest**s:
- [Either of Aidbox Subscriptions mechanisms](../../topic-based-subscriptions/README.md)
- Manual/automated tracking based on **MedicationRequest** modification date and/or status

The ePrescription module creates a **DetectedIssue** whenever inbound RxRenewal data diverges from existing records; see [Detected Issues](./detected-issue.md) for the full list of checks and a sample payload.

### Renewal statuses

The status of the request is stored in the created **MedicationRequest**'s `extension` field under `http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-renewal-status` extension.
There's also `http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-renewal-status-reason` with a human-readable description of a status.

```yaml
GET /fhir/MedicationRequest

resourceType: Bundle
type: searchset
meta:
  versionId: '0'
total: ...
link:
  - relation: first
    url: http://127.0.0.1:8789/fhir/MedicationRequest?page=1
  - relation: self
    url: http://127.0.0.1:8789/fhir/MedicationRequest?page=1
entry:
  ...
  - resource:
      ...
      resourceType: MedicationRequest
      extension:
        - url: >-
            http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-renewal-status
          valueCode: active
      ...
```

The initial status is `active`. For the rest, consult [NewRx status table](./newrx-message.md)

### Responding to RxRenewalRequest

Response to RxRenewalRequest consists of two parts: changing the status and responding to renewal request.

Changing the status of the **MedicationRequest** in Aidbox happens via Resource Browser resource editing or via REST Console. Note that the actual status resides in `extension`, and not in the `status` field. Here's an example query for REST console:

```yaml
PATCH /fhir/MedicationRequest/mr1

- op: add
  path: '/extension/-'
  value: {"url": "http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-renewal-decision", "valueCode": "approved"}
```

In case there's a need to change an earlier decision (that wasn't yet sent with `/eprescription/rx/respond-to-renewal`), you can use a similar patch, but with `replace` operation:

```yaml
PATCH /fhir/MedicationRequest/mr1

- op: replace
  # Replace with the index of the necessary extension
  path: '/extension/3'
  value: {"url": "http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-renewal-decision", "valueCode": "denied"}
```

After the decision is made, call the `/eprescription/rx/respond-to-renewal` endpoint with the ID of the MedicationRequest created by the initial RxRenewalRequest.
```json
POST /eprescription/rx/respond-to-renewal

{
  "medicationRequestId": "mr1"
}
```

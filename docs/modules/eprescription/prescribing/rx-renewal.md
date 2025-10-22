# RxRenewal Message

## Overview

RxRenewal is a type of message sent by pharmacy to subscriber when the medication should be renewed.
It is required that RxRenewal is preceded by NewRx.

## Receiving RxRenewalRequest

Once the pharmacy sends an RxRenewalRequest, ePrescription module receives Surescripts message at `/eprescriptions/rx`
endpoint and saves it to Aidbox.
Resources created:

- **MedicationRequest** reflecting the incoming data
- **DetectedIssue** - if only there were some mismatches in related resources
- **Provenance** - solely as a record that an event has occurred.
  It is intended to serve as an audit/logging artifact, and should not be relied upon for driving business logic.

Ways to track the newly created **MedicationRequest**s:

- [Either of Aidbox Subscriptions mechanisms](../../topic-based-subscriptions/README.md)
- Manual/automated tracking based on **MedicationRequest** modification date and/or status

The ePrescription module creates a **DetectedIssue** whenever inbound RxRenewal data diverges from existing records;
see [Detected Issues](./detected-issue.md) for the full list of checks and a sample payload.

## Renewal statuses

The status of the request is stored in the created **MedicationRequest**'s `extension` field under
`http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-renewal-status` extension.
There's also `http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-renewal-status-reason` with a
human-readable description of a status.

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

## Responding to RxRenewalRequest

### Setting a decision

Available decisions are `approved`, `approved-with-changes`, `denied`, `pending` or `replace`.
It is represented as an extension:

```yaml
extension:
  - url: >-
      http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-renewal-decision
    valueCode: denied
```

<details>
<summary>Shortcut to change decision</summary>
In case you need to change an earlier decision (that wasn't yet sent with `/eprescription/rx/respond-to-change`), you can use the following patch:

```yaml
PATCH /fhir/MedicationRequest/your-medication-request-id

- op: replace
  # Replace with the index of the necessary extension
  path: '/extension/3'
  value: { "url": "http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-change-decision", "valueCode": "denied" }
```

Once you are ready to submit your decision (with all related steps), call the `/eprescription/rx/respond-to-renewal` endpoint to commit your decision:

```json
POST /eprescription/rx/respond-to-renewal

{
  "medicationRequestId": "your-medication-request-id"
}
```

</details>

### Denied

<sub>Same as for [RxChange](./rx-change.md#denied).</sub>

`denied` is the decision that requires the least amount of steps. Setting it in the extension is the only requirement.
You can set the denial reason (free text) in decision note extension (
`http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-renewal-decision-note`), but this is optional.

### Pending

<sub>Same as for [RxChange](./rx-change.md#pending).</sub>

Before committing the `pending` decision you need to

Set the decision itself:

```yaml
extension:
  - url: >-
      http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-renewal-decision
    valueCode: pending
```

Set the expected pended response date:

```yaml
extension:
  - url: >-
      http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-renewal-expected-pended-response-date
    valueDate: '2025-12-12'
```

Set the decision note (contrarily to `denied` decision, it's required for `pending`):

```yaml
extension:
  - url: >-
      http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-renewal-decision-note
    valueString: Provider is on vacation
```

Set the reason code:

```yaml
extension:
  - url: >-
      http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-change-decision-reason-code
    valueCodeableConcept:
      coding:
        - code: HR
          system: urn:app:aidbox:e-prescriptions:surescripts:decision-reason-code
          display: Provider is unavailable and no one is available to make a decision
```

Available reason codes are listed in the table below (same for RxChange case):

| reason code | meaning                                                                         |
|-------------|---------------------------------------------------------------------------------|
| **HP**      | Patient has an appointment scheduled                                            |
| **HQ**      | Patient has labs/tests pending                                                  |
| **HR**      | Provider is unavailable and no one is available to make a decision              |
| **HS**      | Patient is currently undergoing acute care and a decision is pending resolution |
| **HT**      | Provider wishes to delay for reasons not otherwise indicated                    |

### Approved

`approved` decision assumes that only minor changes were made.
While the e-prescriptions module could prefill fields like `MedicationRequest.dispenseRequest` and `dosageInstruction`,
the general approach is to let the prescriber do that.

Set the decision:

```yaml
extension:
  - url: >-
      http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-renewal-decision
    valueCode: approved
```

If during the process of handling incoming renewal request there was a `DetectedIssue` created
(meaning there were mismatches between incoming and existing data),
make sure you have resolved the references to implicated contained resources:

- `Patient` - `MedicationRequest.subject`
- `PractitionerRole` - `MedicationRequest.performer`
- `Practitioner` - `PractitionerRole.practitioner`
- `Location` - `PractitionerRole.location` or `PractitionerRole.organization`
- `Pharmacy` - `MedicationRequest.requester`
- `Medication` - for medications contained resource is supported, so it's up to you whether to resolve it or leave as
  is - `MedicationRequest.medication`

Set `MedicationRequest.dispenseRequest` and `MedicationRequest.dosageInstruction`.
Typically, they are the same as in the request for NewRx:
Make sure you set

- `MedicationRequest.dispenseRequest.performer` (the pharmacy of initial renewal request)
- `MedicationRequest.dispenseRequest.numberOfRepeatsAllowed` - for `approved` decision it should be the same as pharmacy
  requested

```yaml
dispenseRequest:
  quantity:
    code: C48542
    unit: Tablet
    value: 60
    system: urn:app:aidbox:e-prescriptions:ncpdp:QuantityUnitOfMeasure
  performer:
    reference: Organization/my-pharmacy
  expectedSupplyDuration:
    code: d
    unit: days
    value: 30
    system: http://unitsofmeasure.org
  numberOfRepeatsAllowed: 1
dosageInstruction:
  - text: TAKE ONE TABLET TWO TIMES A DAY UNTIL GONE
```

### ApprovedWithChanges

TODO

---

HTTP response with status 202 designates that the response was successfully transitioned to Surescripts.
Surescripts response is reflected in extensions for status and status reason:

- `http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-renewal-status`
- `http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-renewal-status-reason`

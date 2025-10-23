# RxRenewal Message

## Overview

RxRenewal is a type of message sent by pharmacy to subscriber when the medication should be renewed.
It is required that RxRenewal is preceded by NewRx.

## Responding to RxRenewalRequest

### Denied

<sub>Same as for [RxChange](rx-change.md#denied).</sub>

`denied` is the decision that requires the least amount of steps. Setting it in the extension is the only requirement.
You can set the denial reason (free text) in decision note extension (
`http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-renewal-decision-note`), but this is optional.

### Pending

<sub>Same as for [RxChange](rx-change.md#pending).</sub>

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

### Approved, ApprovedWithChanges

These decisions are used when prescriber approves the renewal requests.

<details>
<summary>
Decisions differ by the approved number of refills
</summary>

The difference between `approved` and `approved-with-changes` is about the allowed number of refills.

The number of refills requested by the pharmacy is stored in the following extension:
```yaml
extension:
  - url: >-
      http://aidbox.app/ePrescription/FHIRSchema/medication-request-pharmacy-request-refills
    valueInteger: 1
```
The number of refills approved by the prescriber is taken from `MedicationRequest.dispenseRequest.numberOfRepeatsAllowed`.

Usage of `approved` decision expects these values to be equal.
Usage of `approved-with-changes` decision expects these values to differ.

</details>

While the e-prescriptions module could prefill fields like `MedicationRequest.dispenseRequest` and `dosageInstruction`,
the general approach is to let the prescriber do that.

Set the decision:

```yaml
extension:
  - url: >-
      http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-renewal-decision
    valueCode: approved
#   valueCode: approved-with-changes
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
- `MedicationRequest.dispenseRequest.numberOfRepeatsAllowed`

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

### Replace

TODO

# RxChange Message

## Overview

RxChange is a type of message sent by pharmacy to subscriber when the medication should be changed to a different one.
It is required that RxChange is preceded by NewRx.

## Decisions use cases

{% hint style="info" %}

**MedicationRequest.extension.where(url = "http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-change-decision-reason-code")** referred as **ReasonCode**

**MedicationRequest.extension.where(url = "http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-change-message-request-code")** referred as **MessageRequestCode**

**MedicationRequest.extension.where(url = "TODO")** referred as **MessageRequestSubCodes**

**PractitionerRole.identifier.where(system = 'http://hl7.org/fhir/sid/us-npi')** referred as **NPI**

**PractitionerRole.identifier.where(system = 'http://terminology.hl7.org/CodeSystem/v2-0203' and code = 'DEA')** referred as **DEANumber**

{% endhint %}

*Example 1:* The pharmacy sent an RxChange request to the prescriber proposing to substitute the unavailable Lipitor 20 mg with Atorvastatin 20 mg under the same parameters. The prescriber agreed to the substitution without any changes and sent an RxChangeResponse with the Approved type, allowing the pharmacy to dispense the generic instead of the brand product. So `approved` response should be used.

*Example 2:* The pharmacy sent an RxChange request proposing to substitute the unavailable Lipitor 20 mg with Atorvastatin 20 mg as one of the MedicationRequested options. The prescriber responded with an RxChangeResponse of type `approved-with-changes` for the same patient, selecting the requested Atorvastatin but changing the directions from "take one tablet daily" to "take two tablets daily" and instructing the pharmacy to cancel the original prescription and dispense the updated one.

*Example 3:* The pharmacy sent an RxChange request proposing to substitute the unavailable Lipitor 20 mg with Atorvastatin 20 mg. The prescriber responded with an RxChangeResponse of type `denied` for the same patient, providing **ReasonCode**: "Patient allergy to the proposed product".

*Example 4:* The pharmacy sent an RxChange request proposing to substitute Lipitor 20 mg with Atorvastatin 20 mg. The prescriber responded with an RxChangeResponse of type `pending`, indicating that a decision could not yet be made and that further review was required. The prescriber must wait for subsequent RxChangeRequest messages from the pharmacy, as once a request has been answered with `pending`, it cannot later be responded to again with `approved` or `denied` for the same transaction.

*Example 5:* The pharmacy sent an RxChange request for Prescriber Authorization with **MessageRequestCode** = U and **MessageRequestSubCodes** = [G, B] requesting the prescriber's NPI and DEA number. The prescriber replied with an RxChangeResponse of type Validated, echoing **MessageRequestCode** = U and SubCodes G and B, providing ResponseReasonCode = GM for each and populating **NPI** with "1234567890" and **DEANumber** with "AB1234567" to confirm active registration.

## Supported Request Types

- **D**: Drug Use Evaluation
- **G**:  Generic Substitution - A modification of the product prescribed to a generic equivalent.
- **OS**: Pharmacy is out of stock of the medication prescribed and it cannot be obtained in a clinically appropriate timeframe.
- **S**: Script Clarification
- **T**: Therapeutic Interchange/Substitution - A modification of the product prescribed to a preferred product choice
- **RM**: REMS dispensing authorization requirements not met
- **U**: Prescriber Authorization – Resolution of the prescriber authorization conflict related to state/federal regulatory requirements is required before dispensing.
  - This is the only message type Validated response type can be used for.
<!-- - **P**: Prior Authorization Required - A request to obtain prior authorization before dispensing. -->

## Responding to RxChangeRequest

### Setting a decision

Available decisions are `approved`, `approved-with-changes`, `denied`, `pending` or `validated`.
It is represented as an extension:

```yaml
extension:
  - url: >-
      http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-change-decision
    valueCode: denied
```

Table of compatibility between decisions and request types:

| MessageRequestCode | approved | approved-with-changes | denied | pending | validated |
|--------------------|----------|-----------------------|--------|---------|-----------|
| **G**              | ✓        | ✓                     | ✓      | ✓       |           |
| **T**              | ✓        | ✓                     | ✓      | ✓       |           |
| **S**              | ✓        | ✓                     | ✓      | ✓       |           |
| **OS**             | ✓        | ✓                     | ✓      | ✓       |           |
| **D**              | ✓        | ✓                     | ✓      | ✓       |           |
| **U**              |          |                       | ✓      | ✓       | ✓         |

[//]: # (| **P**              | ✓        | ✗                   | ✓      | ✓       |           |)

### Denied

<sub>Same as for [RxRenewal](./rx-change.md#denied).</sub>

`denied` is the decision that requires the least amount of steps. Setting it in the extension is the only requirement.
You can set the denial reason (free text) in decision note extension (
`http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-change-decision-note`), but this is optional.
Call the `/eprescription/rx/respond-to-change` endpoint to commit your decision:

```json
POST /eprescription/rx/respond-to-change

{
  "medicationRequestId": "your-medication-request-id"
}
```

### Pending

<sub>Same as for [RxRenewal](rx-renewal.md#pending).</sub>

Before committing the `pending` decision you need to

Set the decision itself:

```yaml
extension:
  - url: >-
      http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-change-decision
    valueCode: pending
```

Set the expected pended response date:

```yaml
extension:
  - url: >-
      http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-change-expected-pended-response-date
    valueDate: '2025-12-12'
```

Set the decision note (contrarily to `denied` decision, it's required for `pending`):

```yaml
extension:
  - url: >-
      http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-change-decision-note
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

Use this decision when medication change was allowed unaltered. No changes allowed to the stored **MedicationRequest** and the resources it references.

Set the decision:

```yaml
extension:
  - url: >-
      http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-change-decision
    valueCode: approved
```

Set `MedicationRequest.dispenseRequest`, `MedicationRequest.dosageInstruction` and `MedicationRequest.medication` the way it reflects the data from one of the suggested changes.
<details>
<summary>What does it mean?</summary>
When ePrescription module handles incoming RxChangeRequest, it creates a <b>MedicationRequest</b> with contained resources, including another <b>MedicationRequests</b> which represent the options suggested by pharmacy.
Note that <code>MedicationRequest.medication</code> stored initially is NOT one of the suggested medications, but the currently prescribed medication.

E. g., if contained part of created <b>MedicationRequest</b> looks like this:

```yaml
...
contained:
- substitution: {allowedBoolean: true}
  dispenseRequest:
    quantity: {code: C48480, value: 20.0, system: 'urn:app:aidbox:e-prescriptions:ncpdp:QuantityUnitOfMeasure'}
    expectedSupplyDuration: {code: d, unit: days, value: 10, system: 'http://unitsofmeasure.org'}
    numberOfRepeatsAllowed: 1
  medicationCodeableConcept:
    text: doxycycline hyclate 100 mg capsule
    coding:
    - {code: '00143314250', system: 'http://hl7.org/fhir/sid/ndc'}
  resourceType: MedicationRequest
  dosageInstruction:
  - {text: Take 1 capsule orally every 12 hours for 10 days}
  ...
- substitution: {allowedBoolean: false}
  dispenseRequest:
    quantity: {code: C48542, value: 150.0, system: 'urn:app:aidbox:e-prescriptions:ncpdp:QuantityUnitOfMeasure'}
    expectedSupplyDuration: {code: d, unit: days, value: 10, system: 'http://unitsofmeasure.org'}
    numberOfRepeatsAllowed: 2
  medicationCodeableConcept:
    text: amoxicillin 400 mg/5 mL oral suspension
    coding:
    - {code: '68115002830', system: 'http://hl7.org/fhir/sid/ndc'}
  resourceType: MedicationRequest
  dosageInstruction:
  - {text: Take 5 mL orally twice daily for 10 days}
  ...
...
```

then the higher level of this <b>MedicationRequest</b> should look like this:

```yaml
medicationCodeableConcept:
  text: amoxicillin 400 mg/5 mL oral suspension
  coding:
    - {code: '68115002830', system: 'http://hl7.org/fhir/sid/ndc'}
dispenseRequest:
  quantity: {code: C48542, value: 150.0, system: 'urn:app:aidbox:e-prescriptions:ncpdp:QuantityUnitOfMeasure'}
  expectedSupplyDuration: {code: d, unit: days, value: 10, system: 'http://unitsofmeasure.org'}
  numberOfRepeatsAllowed: 2
dosageInstruction:
  - {text: Take 5 mL orally twice daily for 10 days}
```

</details>

Set `MedicationRequest.dispenseRequest.performer`. It should be the pharmacy that initiated the change.

Set `MedicationRequest.authoredOn`. It should be the date of committing the response.

### ApprovedWithChanges

Means the medication was allowed with a change to any included resource/field of the **MedicationRequest**, except the **Patient**. It is not recommended to change anything but **Medication**/**MedicationRequest**.

Set the decision:

```yaml
extension:
  - url: >-
      http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-change-decision
    valueCode: approved-with-changes
```

Set `MedicationRequest.dispenseRequest.performer`. It should be the pharmacy that initiated the change.

Set `MedicationRequest.authoredOn`. It should be the date of committing the response.

### Validated

Can only be used for **U** request type, alongside with **denied**. When a provider Approves an *RxChangeRequest* with a MessageRequestCode value of “Provider Authorization” (U) each element being responded to will have a Validated tag and will include the appropriate Response Reason Code and supporting data element. Ex: If NPI was requested, the response will echo back SubCode G, Response reason code GM and will have a populated <NPI> tag. If multiple data elements were requested, a provider may send a validation response to fewer elements however at least one element must be returned.

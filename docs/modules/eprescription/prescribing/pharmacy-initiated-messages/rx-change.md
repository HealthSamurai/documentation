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

Can only be used for **U** request type, alongside with **denied**. When a provider Approves an *RxChangeRequest* with a MessageRequestCode value of “Provider Authorization” (U) each element being responded to will have a Validated tag and will include the appropriate Response Reason Code and supporting data element. Ex: If NPI was requested, the response will echo back SubCode G, Response reason code GM and will have a populated <NPI> tag.

**NOTE:** If multiple data elements were requested, a provider may send a validation response to fewer elements however at least one element must be returned.

In order to understand which fields are requested to populate see the notation below

{% hint style="info" %}

**Practitioner.identifier.where(type.coding.where(code = 'SL').exists()).value** referred as **StateLicenseNumber**

**Practitioner.identifier.where(type.coding.where(code = 'DEA').exists()).value** referred as **DEANumber**

**Practitioner.identifier.where(system = "urn:app:aidbox:e-prescriptions:ncpdp:StateControlSubstanceNumber").value** referred as **StateControlSubstanceNumber**

**Practitioner.identifier.where(system = "urn:app:aidbox:e-prescriptions:ncpdp:Data2000WaiverID").value** referred as **Data2000WaiverID**

**Practitioner.identifier.where(system = "http://hl7.org/fhir/sid/us-npi").value** referred as **NPI**

**Practitioner.identifier.where(system = "urn:app:aidbox:e-prescriptions:ncpdp:CertificateToPrescribe").value** referred as **CertificateToPrescribe**

**PractitionerRole.specialty.coding.where(system = 'http://nucc.org/provider-taxonomy').code** referred as **Specialty**

{% endhint %}

**MessageRequestSubCode** values are stored as **MedicationRequest** extension

```yaml
...
extension:
  - url: >-
      http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-change-message-request-sub-codes
    extension:
      - url: subcode
        valueCoding:
          code: A
          system: urn:app:aidbox:e-prescriptions:surescripts:MessageRequestSubCode
      - url: subcode
        valueCoding:
          code: B
          system: urn:app:aidbox:e-prescriptions:surescripts:MessageRequestSubCode
      - url: subcode
        valueCoding:
          code: D
          system: urn:app:aidbox:e-prescriptions:surescripts:MessageRequestSubCode
      - url: subcode
        valueCoding:
          code: F
          system: urn:app:aidbox:e-prescriptions:surescripts:MessageRequestSubCode
      - url: subcode
        valueCoding:
          code: G
          system: urn:app:aidbox:e-prescriptions:surescripts:MessageRequestSubCode
      - url: subcode
        valueCoding:
          code: I
          system: urn:app:aidbox:e-prescriptions:surescripts:MessageRequestSubCode
      - url: subcode
        valueCoding:
          code: M
          system: urn:app:aidbox:e-prescriptions:surescripts:MessageRequestSubCode
...
```

The following **MessageRequestSubCode** values can be specified by the pharmacy:

| SubCode          | Message Request Description                                                                                      | Reason Code | Response Reason Description                                      | Specific Tag to Populate        |
| ---------------- | ---------------------------------------------------------------------------------------------------------------- | ----------- | ---------------------------------------------------------------- | ------------------------------- |
| **A**            | Prescriber must confirm their State license status                                                               | GM          | Active Registration Status                                       | `StateLicenseNumber`          |
|  | Prescriber must confirm In-Active License with prescriptive authority based on state/federal regulations         | GN          | In-Active License                                                | `StateLicenseNumber`          |
| **B**            | Prescriber must confirm their DEA license status in prescribing state                                            | GM          | Active Registration Status                                       | `DEANumber`                   |
| **C**            | Prescriber must confirm their DEA registration by DEA class – prescribed product class                           | GP          | Active with Prescriptive Authority                               | `DEANumber`                   |
| **D**            | Prescriber must confirm their State Controlled Substance Registration license status                             | GM          | Active Registration Status                                       | `StateControlSubstanceNumber` |
| **E**            | Prescriber must confirm their registration by State Controlled Substance Registration – prescribed product class | GP          | Active with Prescriptive Authority                               | `StateControlSubstanceNumber` |
| **F**            | Prescriber must confirm their NADEAN license status                                                              | GM          | Active Registration Status                                       | `Data2000WaiverID`            |
| **G**            | Prescriber must obtain/validate Type 1 NPI                                                                       | GM          | Active Registration Status                                       | `NPI`                         |
| **H**            | Prescriber must enroll/re-enroll with prescription benefit plan                                                  | GT          | Enrolled/Re-Enrolled                                             | `Date сurrently not supported`                        |
| **I**            | Prescriber must confirm prescriptive authority criteria for prescribed medication is met                         | GQ          | Active with Prescriptive Authority                               | `Specialty сurrently not supported`                   |
| **J**            | Prescriber must enroll/re-enroll in REMS                                                                         | GT          | Enrolled/Re-Enrolled                                             | `Date сurrently not supported`                        |
| **L**            | Prescriber must obtain/validate their supervising prescriber                                                     | GR          | Active with Prescriptive Authority – Supervising Prescriber Type | `Supervisor currently not suppoerted`                  |
| **M**            | Prescriber must confirm their Certificate To Prescribe Number status                                             | GM          | Active Registration Status                                       | `CertificateToPrescribe`      |

**NOTE**: ReasonCode values are prefilled for each **MessageRequestSubCode** value

For example Pharmacy requested to populated the following data:
 * StateLicenseNumber (MessageRequestSubCode = A)
 * DEANumber (MessageRequestSubCode = B)
 * Data2000WaiverID (MessageRequestSubCode = F)
 * StateControlSubstanceNumber (MessageRequestSubCode = D)
 * CertificateToPrescribe (MessageRequestSubCode = M)
 * NPI (MessageRequestSubCode = G)
 * Specialty (MessageRequestSubCode = I)

**Practitioner** resource related fields:

```yaml
...
identifier:
  - value: MD123456
    type:
      coding:
        - system: http://terminology.hl7.org/CodeSystem/v2-0203
          code: SL
  - value: AB1234563
    type:
      coding:
        - system: http://terminology.hl7.org/CodeSystem/v2-0203
          code: DEA
  - value: XB1234563
    system: urn:app:aidbox:e-prescriptions:ncpdp:Data2000WaiverID
  - value: MA-CS-456789
    system: urn:app:aidbox:e-prescriptions:ncpdp:StateControlSubstanceNumber
  - value: CTP.OH.123456
    system: urn:app:aidbox:e-prescriptions:ncpdp:CertificateToPrescribe
  - value: '9102210835'
    system: http://hl7.org/fhir/sid/us-npi
    type:
      coding:
        - system: http://terminology.hl7.org/CodeSystem/v2-0203
          code: NPI
...
```

If only an inactive **StateLicenseNumber** is available, set the **use** field of the corresponding **identifier** to "old"

```yaml
...
  - value: MD123456
    use: old
    type:
      coding:
        - system: http://terminology.hl7.org/CodeSystem/v2-0203
          code: SL
...
```

**PractitionerRole** resource related fields:

```yaml
...
specialty:
  - coding:
      - system: http://nucc.org/provider-taxonomy
        code: 207Q00000X
        display: Family Medicine
    text: Family Medicine
...
```

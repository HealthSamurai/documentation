# RxChange Message

### Overview

RxChange is a type of message sent by pharmacy to subscriber when the medication should be changed to a different one.
It is required that RxChange is preceded by NewRx.

### Receiving RxChangeRequest

Once the pharmacy sends an RxChangeRequest, ePrescription module receives Surescripts message at `/eprescriptions/rx` endpoint and saves it to Aidbox.
The message is converted to a **MedicationRequest** and the creation is registered as a **Provenance** resource.
There are several ways to track the newly created **MedicationRequest**s:
- [Either of Aidbox Subscriptions mechanisms](../../topic-based-subscriptions/README.md).
- Manual/automated tracking based on **MedicationRequest** modification date and/or status.
- **Provenance** tracking.

#### Detected Issues

In case some related resources (**Patient**, **Organisation**, **PractitionerRole**, **Practitioner**, **Medication**, or **Location**) were not found or didn't match the provided ones, ePrescription module additionally creates a **DetectedIssue** resource in Aidbox.
There's only one **DetectedIssue** per RxChange, containing all the problematic resources.

### Supported Request Types

- **D**: Drug Use Evaluation
- **G**:  Generic Substitution - A modification of the product prescribed to a generic equivalent.
- **OS**: Pharmacy is out of stock of the medication prescribed and it cannot be obtained in a clinically appropriate timeframe.
<!-- - **P**: Prior Authorization Required - A request to obtain prior authorization before dispensing. -->
- **S**: Script Clarification
- **T**: Therapeutic Interchange/Substitution - A modification of the product prescribed to a preferred product choice
- **RM**: REMS dispensing authorization requirements not met
- **U**: Prescriber Authorization – Resolution of the prescriber authorization conflict related to state/federal regulatory requirements is required before dispensing.
  - This is the only message type Validated response type can be used for.

### Supported Response Types:

Response type is set in `http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-change-decision` and can be one of `approved`, `approved-with-changes`, `denied`, `pending`, and `validated`:

```yaml
PATCH /fhir/MedicationRequest/mr1

- op: add
  path: '/extension/-'
  value: {"url": "http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-change-decision", "valueCode": "approved"}
```

In case there's a need to change an earlier decision (that wasn't yet sent with `/eprescription/rx/respond-to-renewal`), you can use a similar patch, but with `replace` operation:

```yaml
PATCH /fhir/MedicationRequest/mr1

- op: replace
  # Replace with the index of the necessary extension
  path: '/extension/3'
  value: {"url": "http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-change-decision", "valueCode": "denied"}
```

Allowed decisions are:
- **approved**: Medication change was allowed unaltered.
  - No changes allowed to the stored MedicationRequest and the resources it references.
- **approved-with-changes**: Medication was allowed with a change to any included resource/field of the MedicationRequest, except the Patient.
  - Uses `http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-change-decision-note` as a decision note.
  - It is not recommended to change anything but Medications/MedicationRequest.
- **denied**: Medication change was denied, medication dispensing should continue as usual.
  - Uses `http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-change-decision-note` as a decision note.
- **pending**: Medication change was put on hold.
  - Uses `http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-change-expected-pended-response-date` to store the date until which the decision will be made and re-sent.
  - `http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-change-decision-reason-code` is set to Surescripts ReasonCode
  - Uses `http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-change-decision-note` as a decision note.
- **validated**: Can only be used for **U** request type, alongside **denied**.

After the response is set, `/api/rx/respond-to-change` endpoint should be invoked with the ID of the decision-containing MedicationRequest.

```json
POST /eprescription/rx/respond-to-renewal

{
  "medicationRequestId": "mr1"
}
```


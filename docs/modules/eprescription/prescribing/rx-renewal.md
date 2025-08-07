# RxRenewal Message

### Overview

RxRenewal is a type of message sent by pharmacy to subscriber when the medication should be renewed.

### Receiving RxRenewalRequest

Once the pharmacy sends an RxRenewal, ePrescription module receives Surescripts message at `/eprescriptions/rx` endpoint and saves it to Aidbox.
The message is converted to a **MedicationRequest** and the creation is registered as a **Provenance** resource.
There are several ways to track the newly created **MedicationRequest**s:
- [Either of Aidbox Subscriptions mechanisms](../../topic-based-subscriptions/README.md).
- Manual/automated tracking based on **MedicationRequest** modification date and/or status.
- **Provenance** tracking.

#### Detected Issues

In case some related resources (**Patient**, **Organisation**, **PractitionerRole**, **Practitioner**, or **Location**) were not found or didn't match the provided ones, ePrescription module additionally creates a **DetectedIssue** resource in Aidbox.
TODO: ARE THERE OTHER EXCEPTIONAL SITUATIONS? I DIDN'T FIND ANY?

### Renewal statuses

There might be several statuses stored in the created **MedicationRequest**. The initial status is `active`. For the rest, consult [NewRx status table](./newrx-message.md)
TODO: ARE THESE ALWAYS MATCHING THE NEWRX ONES?

### Responding to RxRenewalRequest

Response to RxRenewalRequest consists of two parts:
- Changing the status of the **MedicationRequest** in Aidbox. Note that the actual status resides in `extension`, and not in the `status` field.
```yaml
PATCH /fhir/MedicationRequest/mr1?_method=fhir-patch

resourceType: Parameters
parameter:
  - name: operation
    part:
      - name: type
        valueCode: replace
      - name: path
        valueString: MedicationRequest.extension('http://aidbox.app/ePrescription/FHIRSchema/medication-request-renewal-decision').value
      - name: value
        valueString: approved
```
- And calling the `/eprescription/rx/respond-to-renewal` endpoint with the ID of the MedicationRequest created by the initial RxRenewalRequest.
# RxRenewal Message

### Overview

RxRenewal is a type of message sent by pharmacy to subscriber when the medication should be renewed.
It is required that RxRenewal is preceded by NewRx.

### Receiving RxRenewalRequest

Once the pharmacy sends an RxRenewal, ePrescription module receives Surescripts message at `/eprescriptions/rx` endpoint and saves it to Aidbox.
The message is converted to a **MedicationRequest** and the creation is registered as a **Provenance** resource.
There are several ways to track the newly created **MedicationRequest**s:
- [Either of Aidbox Subscriptions mechanisms](../../topic-based-subscriptions/README.md).
- Manual/automated tracking based on **MedicationRequest** modification date and/or status.
- **Provenance** tracking.

#### Detected Issues

In case some related resources (**Patient**, **Organisation**, **PractitionerRole**, **Practitioner**, **Medication**, or **Location**) were not found or didn't match the provided ones, ePrescription module additionally creates a **DetectedIssue** resource in Aidbox.
There's only one **DetectedIssue** per RxRenewal, containing all the problematic resources.
TODO: ARE THERE OTHER EXCEPTIONAL SITUATIONS? I DIDN'T FIND ANY?

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
TODO: ARE THESE ALWAYS MATCHING THE NEWRX ONES?

### Responding to RxRenewalRequest

Response to RxRenewalRequest consists of two parts:
1. Changing the status of the **MedicationRequest** in Aidbox. Note that the actual status resides in `extension`, and not in the `status` field.
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
2. And calling the `/eprescription/rx/respond-to-renewal` endpoint with the ID of the MedicationRequest created by the initial RxRenewalRequest.
```json
POST /eprescription/rx/respond-to-renewal

{
  "medicationRequestId": "mr1"
}
```
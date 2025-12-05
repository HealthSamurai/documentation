---
description: Handle pharmacy-initiated ePrescription messages including refill requests and prescription changes.
---

# Pharmacy-initiated messages

This includes **RxChangeRequest** and **RxRenewalRequest** types of messages.

{% hint style="info" %}
Throughout this page, the examples use **RxRenewal** for illustration. However, all explanations and examples equally
apply to **RxChange** messages, which follow the same principles but use corresponding values.
{% endhint %}

## Receiving requests

Once the pharmacy sends an RxRenewalRequest (or RxChangeRequest), ePrescription module receives Surescripts message at
`/eprescriptions/rx`
endpoint and saves it to Aidbox.
Resources created:

- **MedicationRequest** reflecting the incoming data
- **DetectedIssue** - if only there were some mismatches in related resources
- **Provenance** - solely as a record that an event has occurred.
  It is intended to serve as an audit/logging artifact, and should not be relied upon for driving business logic.

Ways to track the newly created **MedicationRequest**s:

- [Either of Aidbox Subscriptions mechanisms](../../../../modules/topic-based-subscriptions/README.md)
- Manual/automated tracking based on **MedicationRequest** modification date and/or status

The ePrescription module creates a **DetectedIssue** whenever inbound request data diverges from existing records;
see [Detected Issues](../detected-issue.md) for the full list of checks and a sample payload.

### Statuses

The status of the request is stored in the created **MedicationRequest**'s `extension` field.
There's also an extension for status reason with a human-readable description of a status.

|           | status                                                                            | status reason                                                                            |
|-----------|-----------------------------------------------------------------------------------|------------------------------------------------------------------------------------------|
| RxRenewal | `http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-renewal-status` | `http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-renewal-status-reason` |
| RxChange  | `http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-change-status`  | `http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-change-status-reason`  |

Example

```yaml
GET /fhir/MedicationRequest

resourceType: Bundle
type: searchset
meta:
  versionId: '0'
total: ...
link: ...
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

The initial status is `active`. For the rest, consult [NewRx status table](../newrx-message.md).

## RelatesToMessageId

In most cases, when working with the ePrescription module, you don’t need to dig into the underlying details.
However, this section is essential for understanding why the outcomes of incoming request processing can vary significantly.

`RelatesToMessageId` is a part of header in the Surescripts messages:

```xml
<?xml version="1.0" encoding="utf-8"?>
<Message xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" ...>
    <Header>
        ...
        <RelatesToMessageID>54321</RelatesToMessageID>
        ...
    </Header>
...
```

As the name suggests, this field is used to indicate a link to a previous message in Surescripts.
In our case, `RelatedToMessageID` is expected to reference a prior **NewRx** message or preceding message of other type.
When processing incoming requests, the ePrescription module relies on this field to find related data.

If a match is found, an internal mechanism is triggered to reconcile existing information with the newly received data, followed by a link resolution process.

<details>
<summary>Example</summary>

```yaml
...
resourceType: MedicationRequest
id: 517780a0-af8d-41e4-b609-32ae4ac639f2
status: draft
requester:
  reference: Organization/example-pharmacy
identifier:
  - type:
      coding:
        - code: Renewal
          system: urn:app:aidbox:e-prescriptions:surescripts:serviceLevel
    value: 80ac0515b77a4871a89f947f0eded47588d
    system: urn:app:aidbox:e-prescriptions:surescripts:message-id
intent: proposal
priorPrescription:
  identifier:
    value: '54321'
    system: urn:app:aidbox:e-prescriptions:surescripts:message-id
medicationReference:
  reference: Medication/medication-1
subject:
  reference: Patient/example-patient
performer:
  reference: PractitionerRole/example-practitioner-role
extension:
  - url: http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-renewal-decision
    valueCode: approved-with-changes
  - url: http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-raw-request
    valueReference:
      reference: Binary/c23a9ae3-df1a-47b6-812b-1809807eb835
  - url: http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-renewal-status
    valueCode: active
  - url: http://aidbox.app/ePrescription/FHIRSchema/medication-request-pharmacy-request-refills
    valueInteger: 1

...
```

In this best case scenario all possible references were resolved.
`Medication.contained` and `MedicationRequest.detectedIssue` are absent.
Existing **Organization** (pharmacy), **Patient**, **PractitionerRole** (with underlying **Practitioner** and **Location**) and **Medication** are used as links for `subject`, `requester` etc.
Search by identifier from `MedicationRequest.priorPrescription` will end in success.

</details>

If the field is missing or no corresponding **MedicationRequest** can be found, the ePrescription module - lacking a single source of truth — does not attempt to restore any existing information and instead stores all received data within `MedicationRequest.contained`.

<details>
<summary>Example</summary>

```yaml
...
resourceType: MedicationRequest
id: b9f838bf-8cff-4817-992b-9cdc67c91ee5
status: draft
intent: proposal
identifier:
  - type:
      coding:
        - code: Renewal
          system: urn:app:aidbox:e-prescriptions:surescripts:serviceLevel
    value: 80ac0515b77a4871a89f947f0eded47588d
    system: urn:app:aidbox:e-prescriptions:surescripts:message-id
priorPrescription:
  identifier:
    value: '12345'
    system: urn:app:aidbox:e-prescriptions:surescripts:message-id
requester:
  reference: '#019a113d-f142-73c8-8051-ffa0f82beb35'
medicationReference:
  reference: '#019a113d-f142-7289-97b7-691034516f87'
subject:
  reference: '#019a113d-f142-7d2a-bec6-72f7971751a0'
performer:
  reference: '#019a113d-f142-7b2e-9936-a2248e5ef27d'
detectedIssue:
  - reference: DetectedIssue/019a113d-f17c-702a-a80b-12415cbf8970
extension:
  - url: http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-raw-request
    valueReference:
      reference: Binary/0974e6d8-f721-4480-9939-16e675f5510b
  - url: http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-renewal-status
    valueCode: active
  - url: http://aidbox.app/ePrescription/FHIRSchema/medication-request-pharmacy-request-refills
    valueInteger: 1
contained:
  - resourceType: Patient
    id: 019a113d-f142-7d2a-bec6-72f7971751a0
    name:
      - use: official
        given:
          - Mary
        family: Smith
    gender: female
    ...
  - resourceType: Organization
    id: 019a113d-f142-73c8-8051-ffa0f82beb35
    name: Example Pharmacy
    ...
  - resourceType: Practitioner
    id: 019a113d-f142-7a8a-ba6f-e7c59f5806c0
    name: ...
  - resourceType: Location
    id: 019a113d-f142-7e7e-82bf-8f29bbe19b85
    ...
  - resourceType: PractitionerRole
    id: 019a113d-f142-7b2e-9936-a2248e5ef27d
    location:
      - reference: '#019a113d-f142-7e7e-82bf-8f29bbe19b85'
    specialty:
      - coding:
          - code: 207V00000X
            system: http://nucc.org/provider-taxonomy
    practitioner:
      reference: '#019a113d-f142-7a8a-ba6f-e7c59f5806c0'
    ...
  - resourceType: Medication
    id: 019a113d-f142-7289-97b7-691034516f87
    code:
      text: Calan SR 240MG
      coding:
        - code: '00025189131'
          system: http://hl7.org/fhir/sid/ndc
...
```

At this stage, `MedicationRequest.detectedIssue` is present, and no attempts were made to retrieve existing data from Aidbox.
All received information is stored within contained resources, and the responsibility for replacing those contained resources with existing ones rests with the system that integrates the ePrescription module.

If `RelatesToMessageId` is missing from the incoming message, then `MedicationRequest.priorPrescription` will be missing as well.

</details>

## Responding

Table of available decision:

| decision            | RxRenewalResponse | RxChangeResponse | Comment                                      |
|---------------------|-------------------|------------------|----------------------------------------------|
| Approved            | ✓                 | ✓                |                                              |
| ApprovedWithChanges | ✓                 | ✓                | meaning differs significantly                |
| Denied              | ✓                 | ✓                |                                              |
| Pending             | ✓                 | ✓                |                                              |
| Replace             | ✓                 |                  |                                              |
| Validated           |                   | ✓                | is only valid for a subset of RxChange cases |

Decision is represented as an extension:

```yaml
extension:
  - url: >-
      http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-renewal-decision
#     http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-change-decision
    valueCode: denied
```

<details>
<summary>Shortcut to change decision</summary>
In case you need to change an earlier decision (that wasn't yet sent with `/eprescription/rx/respond-to-renewal`), you can use the following patch:

```yaml
PATCH /fhir/MedicationRequest/your-medication-request-id

- op: replace
  # Replace with the index of the necessary extension
  path: '/extension/3'
  value: { "url": "http://aidbox.app/ePrescription/FHIRSchema/medication-request-rx-renewal-decision", "valueCode": "denied" }
```

</details>

Once you are ready to submit your decision, call the `/eprescription/rx/respond-to-renewal` (or `/eprescription/rx/respond-to-change`)
endpoint to commit your decision:

```json
POST /eprescription/rx/respond-to-renewal

{
  "medicationRequestId": "your-medication-request-id"
}
```

HTTP response with status 202 designates that the response was successfully transitioned to Surescripts.
Surescripts response is reflected in extensions for status and status reason.

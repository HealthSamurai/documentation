---
description: >-
  This page explains how to configure Aidbox to record audit events that occur
  within the system
---

# How to configure Audit Log

## Objectives

* Enable audit logging in Aidbox.
* Receive audit logs from the FHIR API.
* View audit log event in the Audit log viewer UI application.

## Configure Audit Log

### Enable Audit Log

To enable audit logging in Aidbox, use the following setting: [security.audit-log.enabled](../../reference/settings/security-and-access-control.md#security.audit-log.enabled).

### Install the BALP package

The Basic Audit Log Patterns (BALP) Implementation Guide is a Content Profile that defines some basic and reusable AuditEvent patterns. This includes basic audit log profiles for FHIR RESTful operations to be used when no more specific audit event is determined.

Navigate to the **FHIR Packages** screen in Aidbox Console UI.

<figure><img src="df034719-9732-4901-b0d9-454a517cc130.png" alt=""><figcaption></figcaption></figure>

Click the **Import FHIR Package** button.

<figure><img src="8cb5550f-5ef0-4834-bfc9-c86fdd0e687e.png" alt=""><figcaption></figcaption></figure>

Find the BALP package and hit the Import button.

<figure><img src="5596c50e-f2a4-4259-a77f-67d411142281.png" alt=""><figcaption></figcaption></figure>

### Run some auditable operations

To force Aidbox to produce audit events, run any FHIR CRUD operation, e.g. run the following request in Aidbox REST Console:

```yaml
POST /fhir/Patient
content-type: text/yaml
accept: text/yaml

name:
- given: [John]
  family: Smith
  
# 201 Created
```

## Find audit logs with the FHIR API

To see audit logs with the FHIR API, run `GET /fhir/AuditEvent?_sort=-createdAt`

## Find audit logs with the Audit Log Viewer application

To see audit logs with the Audit event viewer app, navigate to the **Audit Log** tab in Aidbox Console UI.

<figure><img src="../../.gitbook/assets/cae433a3-05ca-487a-8004-782c49d5b753.png" alt=""><figcaption></figcaption></figure>

And find the audit event, produced by the patient create operation.

<figure><img src="01d17537-0703-43a2-a5c5-8c1c7baa0536.png" alt=""><figcaption></figcaption></figure>

### Use FHIR API to create Audit Log Event

Aidbox can receive Audot Log events from external services via a regular FHIR API.

Navigate to REST Console in Aidbox UI.

Create the Client.

```json
PUT /fhir/Client/basic
content-type: application/json
accept: application/json

{
 "secret": "secret",
 "grant_types": [
  "basic"
 ]
}
```

Create the AccessPolicy for the client.

```json
PUT /fhir/AccessPolicy/basic-policy
content-type: application/json
accept: application/json

{
 "engine": "allow",
 "link": [
  {
   "id": "basic",
   "resourceType": "Client"
  }
 ]
}
```

Run the following **curl** command in your terminal to simulate the Aidbox FHIR API from an external service

```bash
curl -X POST "http://localhost:8080/fhir/AuditEvent" \
  -H "Authorization: Basic YmFzaWM6c2VjcmV0" \
  -H "Content-Type: application/json" \
  -d '{
    "type": {
      "system": "http://dicom.nema.org/resources/ontology/DCM",
      "code": "110100",
      "display": "Application Activity"
    },
    "outcome": "0",
    "resourceType": "AuditEvent",
    "source": {
      "site": "Development",
      "observer": {
        "display": "Grahames Laptop"
      },
      "type": [
        {
          "system": "http://dicom.nema.org/resources/ontology/DCM",
          "code": "110122",
          "display": "Login"
        }
      ]
    },
    "recorded": "2010-01-03T22:04:27+11:00",
    "agent": [
      {
        "type": {
          "coding": [
            {
              "system": "http://terminology.hl7.org/CodeSystem/extra-security-role-type",
              "code": "humanuser",
              "display": "human user"
            }
          ]
        },
        "role": [
          {
            "text": "Service User (Logon)"
          }
        ],
        "who": {
          "identifier": {
            "value": "Grahame"
          }
        },
        "requestor": false,
        "network": {
          "address": "127.0.0.1",
          "type": "2"
        }
      },
      {
        "type": {
          "coding": [
            {
              "system": "http://dicom.nema.org/resources/ontology/DCM",
              "code": "110153",
              "display": "Source Role ID"
            }
          ]
        },
        "who": {
          "identifier": {
            "system": "urn:oid:2.16.840.1.113883.4.2",
            "value": "2.16.840.1.113883.4.2"
          }
        },
        "altId": "6580",
        "requestor": false,
        "network": {
          "address": "Workstation1.ehr.familyclinic.com",
          "type": "1"
        }
      }
    ],
    "id": "example",
    "action": "C",
    "entity": [
      {
        "what": {
          "identifier": {
            "type": {
              "coding": [
                {
                  "system": "http://terminology.hl7.org/CodeSystem/v2-0203",
                  "code": "SNO"
                }
              ],
              "text": "Dell Serial Number"
            },
            "value": "ABCDEF"
          }
        },
        "type": {
          "system": "http://terminology.hl7.org/CodeSystem/audit-entity-type",
          "code": "4",
          "display": "Other"
        },
        "role": {
          "system": "http://terminology.hl7.org/CodeSystem/object-role",
          "code": "4",
          "display": "Domain Resource"
        },
        "lifecycle": {
          "system": "http://terminology.hl7.org/CodeSystem/dicom-audit-lifecycle",
          "code": "6",
          "display": "Access / Use"
        },
        "name": "Grahames Laptop"
      }
    ],
    "subtype": [
      {
        "system": "http://dicom.nema.org/resources/ontology/DCM",
        "code": "110120",
        "display": "Application Start"
      },
      {
        "system": "http://dicom.nema.org/resources/ontology/DCM",
        "code": "110131"
      }
    ],
    "text": {
      "status": "generated",
      "div": "test"
    }
  }'
```

Navigate to the **Audit Log** tab in Aidbox Console UI and find the Audit event:

<figure><img src="../../.gitbook/assets/ba8f6029-d61c-4dc2-9ac2-aabf0e052b22.png" alt=""><figcaption></figcaption></figure>

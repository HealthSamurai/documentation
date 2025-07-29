---
description: >-
  This page explains how to configure Aidbox to record audit events that occur
  within the system
---

# How to Configure FHIR Audit Log

Objectives

* Enable audit logging in Aidbox.
* Receive audit logs from the FHIR API.
* View audit log event in the Audit log viewer UI application.
* Send Audit Events to an external Audit record repository.
* Create Audit Event using FHIR API.

Limitation: currently works with only FHIR R4 version. If you need to support R5 or DSTU2, please contact us.

## Configure FHIR Audit Log

### Enable FHIR Audit Log

To enable audit logging in Aidbox, use the following setting: [security.audit-log.enabled](/reference/settings/security-and-access-control.md#security.audit-log.enabled). When enabled, Aidbox will generate structured audit logs in FHIR R4 AuditEvent format.

### Install the BALP Package

The Basic Audit Log Patterns (BALP) Implementation Guide is a Content Profile that defines some basic and reusable AuditEvent patterns. This includes basic audit log profiles for FHIR RESTful operations to be used when no more specific audit event is determined.

Navigate to the **FHIR Packages** screen in Aidbox Console UI.

<figure><img src="/.gitbook/assets/df034719-9732-4901-b0d9-454a517cc130.png" alt=""><figcaption></figcaption></figure>

Click the **Import FHIR Package** button.

<figure><img src="/.gitbook/assets/8cb5550f-5ef0-4834-bfc9-c86fdd0e687e.png" alt=""><figcaption></figcaption></figure>

Find the BALP package and hit the Import button.

<figure><img src="/.gitbook/assets/5596c50e-f2a4-4259-a77f-67d411142281.png" alt=""><figcaption></figcaption></figure>

### Run some auditable operations

### Run Some Auditable Operations

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

## Find Audit Logs with the FHIR API

To see audit logs with the FHIR API, run `GET /fhir/AuditEvent?_sort=-createdAt`

## Find Audit Logs with the Audit Log Viewer Application

To see audit logs with the Audit event viewer app, navigate to the **Audit Log** tab in Aidbox Console UI.

And find the audit event, produced by the patient create operation.

<figure><img src="/.gitbook/assets/01d17537-0703-43a2-a5c5-8c1c7baa0536.png" alt=""><figcaption></figcaption></figure>

## External Audit Repository Configuration

{% hint style="warning" %}
This functionality is available starting from version 2506.
{% endhint %}

Aidbox supports forwarding audit data to an external repository. It batches individual **AuditEvent** resources into a FHIR `Bundle` of type `collection` and send the bundle to the configured repository endpoint with an HTTP `POST` request.

To configure an external Audit log repository, use the following settings:

- URL of the external destination where Aidbox streams all audit events
* [security.audit-log.flush-interval](/reference/settings/security-and-access-control.md#security.audit-log.flush-interval) - Interval time in ms to flush audit events to Audit Log Repository
* [security.audit-log.max-flush-interval](/reference/settings/security-and-access-control.md#security.audit-log.max-flush-interval) - Maximum interval for retries if sending audit events fails
* [security.audit-log.batch-count](/reference/settings/security-and-access-control.md#security.audit-log.batch-count) - Maximum count of Audit Events in a batch
* [security.audit-log.request-headers](/reference/settings/security-and-access-control.md#security.audit-log.request-headers) - Custom headers to add to repository requests

Audit log Bundle example:

```json
{
  "resourceType": "Bundle",
  "type": "collection",
  "entry": [
    {
      "resource": {
        "resourceType": "AuditEvent",
        "id": "audit-1",
        "meta": {
          "profile": [
            "https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.Update"
          ]
        },
        "type": {
          "system": "http://terminology.hl7.org/CodeSystem/audit-event-type",
          "code": "rest",
          "display": "Restful Operation"
        },
        "subtype": [
          {
            "system": "http://hl7.org/fhir/restful-interaction",
            "code": "update",
            "display": "update"
          }
        ],
        "action": "U",
        "recorded": "2025-06-09T14:44:48.924655Z",
        "outcome": "0",
        "agent": [
          {
            "type": {
              "coding": [
                {
                  "system": "http://dicom.nema.org/resources/ontology/DCM",
                  "code": "110152",
                  "display": "Destination Role ID"
                }
              ]
            },
            "who": {
              "display": "Aidbox",
              "type": "Device"
            },
            "requestor": false,
            "network": {
              "address": "http://localhost:8765",
              "type": "5"
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
                "system": "http://localhost:8765",
                "value": "box-ui"
              },
              "type": "Device"
            },
            "requestor": false,
            "network": {
              "address": "0:0:0:0:0:0:0:1",
              "type": "2"
            }
          },
          {
            "requestor": true,
            "altId": "admin",
            "who": {
              "identifier": {
                "type": {
                  "coding": [
                    {
                      "system": "urn:system:aidbox",
                      "code": "UID",
                      "display": "User ID"
                    }
                  ]
                },
                "system": "http://localhost:8765",
                "value": "admin"
              }
            }
          }
        ],
        "source": {
          "site": "http://localhost:8765",
          "observer": {
            "display": "Aidbox",
            "type": "Device"
          },
          "type": [
            {
              "system": "http://terminology.hl7.org/CodeSystem/security-source-type",
              "code": "4",
              "display": "Application Server"
            }
          ]
        },
        "entity": [
          {
            "what": {
              "resourceType": "Patient",
              "id": "pt-3"
            },
            "type": {
              "system": "http://terminology.hl7.org/CodeSystem/audit-entity-type",
              "code": "2",
              "display": "System Object"
            },
            "role": {
              "system": "http://terminology.hl7.org/CodeSystem/object-role",
              "code": "4",
              "display": "Domain Resource"
            }
          },
          {
            "what": {
              "identifier": {
                "value": "a34460d947050a96"
              }
            },
            "type": {
              "system": "https://profiles.ihe.net/ITI/BALP/CodeSystem/BasicAuditEntityType",
              "code": "XrequestId"
            }
          }
        ]
      }
    },
    {
      "resource": {
        "resourceType": "AuditEvent",
        "id": "audit-2",
        "meta": {
          "profile": [
            "https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.Update"
          ]
        },
        "type": {
          "system": "http://terminology.hl7.org/CodeSystem/audit-event-type",
          "code": "rest",
          "display": "Restful Operation"
        },
        "subtype": [
          {
            "system": "http://hl7.org/fhir/restful-interaction",
            "code": "update",
            "display": "update"
          }
        ],
        "action": "U",
        "recorded": "2025-06-09T14:44:48.924655Z",
        "outcome": "0",
        "agent": [
          {
            "type": {
              "coding": [
                {
                  "system": "http://dicom.nema.org/resources/ontology/DCM",
                  "code": "110152",
                  "display": "Destination Role ID"
                }
              ]
            },
            "who": {
              "display": "Aidbox",
              "type": "Device"
            },
            "requestor": false,
            "network": {
              "address": "http://localhost:8765",
              "type": "5"
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
                "system": "http://localhost:8765",
                "value": "box-ui"
              },
              "type": "Device"
            },
            "requestor": false,
            "network": {
              "address": "0:0:0:0:0:0:0:1",
              "type": "2"
            }
          },
          {
            "requestor": true,
            "altId": "admin",
            "who": {
              "identifier": {
                "type": {
                  "coding": [
                    {
                      "system": "urn:system:aidbox",
                      "code": "UID",
                      "display": "User ID"
                    }
                  ]
                },
                "system": "http://localhost:8765",
                "value": "admin"
              }
            }
          }
        ],
        "source": {
          "site": "http://localhost:8765",
          "observer": {
            "display": "Aidbox",
            "type": "Device"
          },
          "type": [
            {
              "system": "http://terminology.hl7.org/CodeSystem/security-source-type",
              "code": "4",
              "display": "Application Server"
            }
          ]
        },
        "entity": [
          {
            "what": {
              "resourceType": "Patient",
              "id": "pt-3"
            },
            "type": {
              "system": "http://terminology.hl7.org/CodeSystem/audit-entity-type",
              "code": "2",
              "display": "System Object"
            },
            "role": {
              "system": "http://terminology.hl7.org/CodeSystem/object-role",
              "code": "4",
              "display": "Domain Resource"
            }
          },
          {
            "what": {
              "identifier": {
                "value": "a34460d947050a96"
              }
            },
            "type": {
              "system": "https://profiles.ihe.net/ITI/BALP/CodeSystem/BasicAuditEntityType",
              "code": "XrequestId"
            }
          }
        ]
      }
    }
  ]
}

```

## Use FHIR API to Create Audit Log Event

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

<figure><img src="/.gitbook/assets/ba8f6029-d61c-4dc2-9ac2-aabf0e052b22.png" alt=""><figcaption></figcaption></figure>

## Reliability of Audit Event Storing

Aidbox prioritizes the reliability of audit event storage, even when dealing with validation errors or system inconsistencies. When an incorrect AuditEvent is generated (e.g., BALP package is not installed):

- The system will still write it to the Aidbox database with a warning in the log
- The AuditEvent will be forwarded to the external repository (if configured)

For external repositories, there is a possibility of receiving errors (such as HTTP 500) when sending malformed AuditEvents. In this case:

- Aidbox will retry sending according to the configured flush intervals
- Repository systems should implement appropriate error handling for invalid payloads to prevent infinite retry loops

If an exception occurs during the AuditEvent preparation or saving process, Aidbox will fail the user request rather than creating an incomplete audit trail. This approach ensures the integrity of audit logging even at the cost of operation availability.

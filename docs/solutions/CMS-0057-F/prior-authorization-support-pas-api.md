# Prior Authorization Support (PAS) API

## Overview

The Aidbox Prior Authorization Support (PAS) API enables direct submission of prior authorization requests from EHR systems using FHIR. Built on the [HL7 Da Vinci Prior Authorization Support Implementation Guide](https://hl7.org/fhir/us/davinci-pas/), it streamlines the authorization workflow between healthcare providers and payers.

When combined with Coverage Requirements Discovery (CRD) and Documentation Templates and Rules (DTR), PAS ensures authorizations are submitted when necessary with all required information for initial decision-making.

## Supported Operations

All following PAS operations are NOT publicly accessible. If you want to access them from third party application read [Application/Client Management](../../access-control/identity-management/application-client-management.md) — configure Client resources for programmatic API access with OAuth 2.0 flows or Basic Auth.

### Claim/$submit

Submits a prior authorization request for adjudication.

**Endpoint:** `POST [base]/fhir/Claim/$submit`

**Request:** A FHIR Bundle containing a PAS Claim Request and all referenced resources.

**Response:** A FHIR Bundle containing the PAS Claim Response and referenced resources, or an OperationOutcome on error.

| Direction | Parameter | Type | Cardinality | Description |
|-----------|-----------|------|-------------|-------------|
| IN | resource | Bundle | 1..1 | [PAS Request Bundle](https://build.fhir.org/ig/HL7/davinci-pas/StructureDefinition-profile-pas-request-bundle.html) containing a single Claim and all referenced resources |
| OUT | return | Bundle \| OperationOutcome | 1..1 | [PAS Response Bundle](https://build.fhir.org/ig/HL7/davinci-pas/StructureDefinition-profile-pas-response-bundle.html) with ClaimResponse, or OperationOutcome on error |

**Example:**

{% tabs %}
{% tab title="Request" %}
```http
POST /fhir/Claim/$submit
Content-Type: application/json
Accept: application/json

{
  "resourceType": "Bundle",
  "meta": {
    "profile": [
      "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-pas-request-bundle"
    ]
  },
  "type": "collection",
  "identifier": {
    "system": "http://example.org/PATIENT_EVENT_TRACE_NUMBER",
    "value": "test-bundle-3"
  },
  "timestamp": "2025-12-08T16:48:02.531010Z",
  "entry": [
    {
      "fullUrl": "urn:uuid:claim-1765213116210",
      "resource": {
        "resourceType": "Claim",
        "id": "claim-1765213116210",
        "meta": {
          "profile": [
            "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-claim"
          ]
        },
        "identifier": [
          {
            "system": "http://example.org/claim-id",
            "value": "claim-1765213116210"
          }
        ],
        "status": "active",
        "type": {
          "coding": [
            {
              "system": "http://terminology.hl7.org/CodeSystem/claim-type",
              "code": "professional"
            }
          ]
        },
        "use": "preauthorization",
        "patient": {
          "reference": "Patient/patient-1"
        },
        "created": "2025-12-08T16:58:36.210Z",
        "insurer": {
          "reference": "Organization/payer-org-1"
        },
        "provider": {
          "reference": "Organization/requesting-org-1",
          "display": "Acme Care Clinic"
        },
        "priority": {
          "coding": [
            {
              "system": "http://terminology.hl7.org/CodeSystem/processpriority",
              "code": "normal"
            }
          ]
        },
        "insurance": [
          {
            "sequence": 1,
            "focal": true,
            "coverage": {
              "reference": "Coverage/coverage-1"
            }
          }
        ],
        "item": [
          {
            "sequence": 1,
            "category": {
              "coding": [
                {
                  "system": "https://codesystem.x12.org/005010/1365",
                  "code": "42",
                  "display": "Home Health Care"
                }
              ]
            },
            "productOrService": {
              "coding": [
                {
                  "system": "http://www.ama-assn.org/go/cpt",
                  "code": "99213",
                  "display": "Established patient office visit"
                }
              ]
            },
            "detail": [
              {
                "sequence": 1,
                "productOrService": {
                  "coding": [
                    {
                      "system": "http://www.cms.gov/Medicare/Coding/HCPCSReleaseCodeSets",
                      "code": "E0250",
                      "display": "Hospital bed, fixed height, with mattress"
                    }
                  ]
                }
              }
            ]
          }
        ]
      }
    },
    {
      "fullUrl": "urn:uuid:patient-patient-1",
      "resource": {
        "resourceType": "Patient",
        "id": "patient-1",
        "meta": {
          "profile": [
            "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-beneficiary",
            "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-subscriber"
          ]
        },
        "identifier": [
          {
            "system": "http://hospital.example.org/patients",
            "value": "MRN123456"
          }
        ],
        "active": true,
        "name": [
          {
            "use": "official",
            "family": "Smith",
            "given": ["John", "Robert"]
          }
        ],
        "telecom": [
          {
            "system": "phone",
            "value": "555-123-4567",
            "use": "home"
          }
        ],
        "gender": "male",
        "birthDate": "1970-05-15",
        "address": [
          {
            "use": "home",
            "line": ["123 Main Street"],
            "city": "Springfield",
            "state": "IL",
            "postalCode": "62701",
            "country": "USA"
          }
        ]
      }
    },
    {
      "fullUrl": "urn:uuid:coverage-coverage-1",
      "resource": {
        "resourceType": "Coverage",
        "id": "coverage-1",
        "meta": {
          "profile": [
            "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-coverage"
          ]
        },
        "status": "active",
        "type": {
          "coding": [
            {
              "system": "http://terminology.hl7.org/CodeSystem/v3-ActCode",
              "code": "HIP",
              "display": "health insurance plan policy"
            }
          ]
        },
        "subscriber": {
          "reference": "Patient/patient-1"
        },
        "beneficiary": {
          "reference": "Patient/patient-1"
        },
        "payor": [
          {
            "reference": "Organization/payer-org-1"
          }
        ],
        "class": [
          {
            "type": {
              "coding": [
                {
                  "system": "http://terminology.hl7.org/CodeSystem/coverage-class",
                  "code": "plan",
                  "display": "Plan"
                }
              ]
            },
            "value": "GOLD-PLAN-001",
            "name": "Gold Plus Plan"
          }
        ]
      }
    },
    {
      "fullUrl": "urn:uuid:org-requesting-org-1",
      "resource": {
        "resourceType": "Organization",
        "id": "requesting-org-1",
        "meta": {
          "profile": [
            "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-requestor"
          ]
        },
        "identifier": [
          {
            "system": "http://hl7.org/fhir/sid/us-npi",
            "value": "8189991234"
          }
        ],
        "active": true,
        "name": "Acme Care Clinic",
        "telecom": [
          {
            "system": "phone",
            "value": "1-800-555-1234"
          }
        ],
        "address": [
          {
            "line": ["100 Market Street"],
            "city": "Springfield",
            "state": "IL",
            "postalCode": "62701",
            "country": "US"
          }
        ]
      }
    },
    {
      "fullUrl": "urn:uuid:org-payer-org-1",
      "resource": {
        "resourceType": "Organization",
        "id": "payer-org-1",
        "meta": {
          "profile": [
            "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-insurer"
          ]
        },
        "identifier": [
          {
            "system": "http://hl7.org/fhir/sid/us-npi",
            "value": "1234567893"
          }
        ],
        "active": true,
        "name": "Acme Health Insurance",
        "telecom": [
          {
            "system": "phone",
            "value": "1-800-555-ACME"
          }
        ]
      }
    }
  ]
}
```
{% endtab %}
{% tab title="Response" %}
```json
{
  "resourceType": "Bundle",
  "type": "collection",
  "meta": {
    "profile": [
      "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-pas-response-bundle"
    ]
  },
  "identifier": {
    "system": "http://example.org/PATIENT_EVENT_TRACE_NUMBER",
    "value": "test-bundle-3"
  },
  "timestamp": "2025-12-17T13:34:24.333467Z",
  "entry": [
    {
      "fullUrl": "ClaimResponse/c0d73c37-12ee-4cde-bfc6-aa6ed216f4dd",
      "resource": {
        "patient": {
          "reference": "Patient/patient-1"
        },
        "request": {
          "reference": "Claim/claim-1765213116210"
        },
        "meta": {
          "profile": [
            "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-claimresponse"
          ],
          "lastUpdated": "2025-12-17T13:34:24.320436Z",
          "versionId": "935",
          "extension": [
            {
              "url": "https://aidbox.app/ex/createdAt",
              "valueInstant": "2025-12-17T13:34:24.320436Z"
            }
          ]
        },
        "use": "preauthorization",
        "item": [
          {
            "adjudication": [
              {
                "category": {
                  "coding": [
                    {
                      "code": "submitted",
                      "system": "http://terminology.hl7.org/CodeSystem/adjudication"
                    }
                  ]
                }
              }
            ],
            "itemSequence": 1
          }
        ],
        "type": {
          "coding": [
            {
              "code": "professional",
              "system": "http://terminology.hl7.org/CodeSystem/claim-type"
            }
          ]
        },
        "created": "2025-12-17T13:34:24.316526Z",
        "outcome": "queued",
        "resourceType": "ClaimResponse",
        "insurer": {
          "reference": "Organization/payer-org-1"
        },
        "status": "active",
        "id": "c0d73c37-12ee-4cde-bfc6-aa6ed216f4dd"
      }
    },
    {
      "fullUrl": "Patient/patient-1",
      "resource": {
        "address": [
          {
            "use": "home",
            "city": "Springfield",
            "line": [
              "123 Main Street"
            ],
            "state": "IL",
            "country": "USA",
            "postalCode": "62701"
          }
        ],
        "meta": {
          "profile": [
            "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-beneficiary",
            "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-subscriber"
          ],
          "lastUpdated": "2025-12-17T07:50:30.618974Z",
          "versionId": "138",
          "extension": [
            {
              "url": "https://aidbox.app/ex/createdAt",
              "valueInstant": "2025-12-16T08:07:36.639379Z"
            }
          ]
        },
        "name": [
          {
            "use": "official",
            "given": [
              "John",
              "Robert"
            ],
            "family": "Smith"
          }
        ],
        "birthDate": "1970-05-15",
        "resourceType": "Patient",
        "active": true,
        "id": "patient-1",
        "identifier": [
          {
            "value": "MRN123456",
            "system": "http://hospital.example.org/patients"
          }
        ],
        "telecom": [
          {
            "use": "home",
            "value": "555-123-4567",
            "system": "phone"
          }
        ],
        "gender": "male"
      }
    },
    {
      "fullUrl": "Claim/claim-1765213116210",
      "resource": {
        "patient": {
          "reference": "Patient/patient-1"
        },
        "insurance": [
          {
            "focal": true,
            "coverage": {
              "reference": "Coverage/coverage-1"
            },
            "sequence": 1
          }
        ],
        "meta": {
          "profile": [
            "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-claim"
          ],
          "lastUpdated": "2025-12-16T10:21:42.922009Z",
          "versionId": "93",
          "extension": [
            {
              "url": "https://aidbox.app/ex/createdAt",
              "valueInstant": "2025-12-16T10:21:42.922009Z"
            }
          ]
        },
        "use": "preauthorization",
        "item": [
          {
            "detail": [
              {
                "sequence": 1,
                "productOrService": {
                  "coding": [
                    {
                      "code": "E0250",
                      "system": "http://www.cms.gov/Medicare/Coding/HCPCSReleaseCodeSets",
                      "display": "Hospital bed, fixed height, with mattress"
                    }
                  ]
                }
              }
            ],
            "category": {
              "coding": [
                {
                  "code": "42",
                  "system": "https://codesystem.x12.org/005010/1365",
                  "display": "Home Health Care"
                }
              ]
            },
            "sequence": 1,
            "productOrService": {
              "coding": [
                {
                  "code": "99213",
                  "system": "http://www.ama-assn.org/go/cpt",
                  "display": "Established patient office visit"
                }
              ]
            }
          }
        ],
        "type": {
          "coding": [
            {
              "code": "professional",
              "system": "http://terminology.hl7.org/CodeSystem/claim-type"
            }
          ]
        },
        "created": "2025-12-08T16:58:36.210Z",
        "resourceType": "Claim",
        "insurer": {
          "reference": "Organization/payer-org-1"
        },
        "priority": {
          "coding": [
            {
              "code": "normal",
              "system": "http://terminology.hl7.org/CodeSystem/processpriority"
            }
          ]
        },
        "status": "active",
        "id": "claim-1765213116210",
        "identifier": [
          {
            "value": "claim-1765213116210",
            "system": "http://example.org/claim-id"
          }
        ],
        "provider": {
          "display": "Acme Care Clinic",
          "reference": "Organization/requesting-org-1"
        }
      }
    },
    {
      "fullUrl": "Organization/payer-org-1",
      "resource": {
        "meta": {
          "profile": [
            "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-insurer"
          ],
          "lastUpdated": "2025-12-17T08:11:38.523298Z",
          "versionId": "198",
          "extension": [
            {
              "url": "https://aidbox.app/ex/createdAt",
              "valueInstant": "2025-12-16T08:07:36.649727Z"
            }
          ]
        },
        "name": "Acme Health Insurance",
        "active": true,
        "telecom": [
          {
            "value": "1-800-555-ACME",
            "system": "phone"
          }
        ],
        "identifier": [
          {
            "value": "1234567893",
            "system": "http://hl7.org/fhir/sid/us-npi"
          }
        ],
        "id": "payer-org-1",
        "resourceType": "Organization"
      }
    },
    {
      "fullUrl": "Coverage/coverage-1",
      "resource": {
        "meta": {
          "profile": [
            "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-coverage"
          ],
          "lastUpdated": "2025-12-17T07:50:30.612513Z",
          "versionId": "136",
          "extension": [
            {
              "url": "https://aidbox.app/ex/createdAt",
              "valueInstant": "2025-12-16T08:07:36.635155Z"
            }
          ]
        },
        "beneficiary": {
          "reference": "Patient/patient-1"
        },
        "type": {
          "coding": [
            {
              "code": "HIP",
              "system": "http://terminology.hl7.org/CodeSystem/v3-ActCode",
              "display": "health insurance plan policy"
            }
          ]
        },
        "resourceType": "Coverage",
        "subscriber": {
          "reference": "Patient/patient-1"
        },
        "payor": [
          {
            "reference": "Organization/payer-org-1"
          }
        ],
        "status": "active",
        "id": "coverage-1",
        "class": [
          {
            "name": "Gold Plus Plan",
            "type": {
              "coding": [
                {
                  "code": "plan",
                  "system": "http://terminology.hl7.org/CodeSystem/coverage-class",
                  "display": "Plan"
                }
              ]
            },
            "value": "GOLD-PLAN-001"
          }
        ]
      }
    },
    {
      "fullUrl": "Organization/requesting-org-1",
      "resource": {
        "address": [
          {
            "city": "Springfield",
            "line": [
              "100 Market Street"
            ],
            "state": "IL",
            "country": "US",
            "postalCode": "62701"
          }
        ],
        "meta": {
          "profile": [
            "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-requestor"
          ],
          "lastUpdated": "2025-12-16T10:21:42.922009Z",
          "versionId": "96",
          "extension": [
            {
              "url": "https://aidbox.app/ex/createdAt",
              "valueInstant": "2025-12-16T10:21:42.922009Z"
            }
          ]
        },
        "name": "Acme Care Clinic",
        "resourceType": "Organization",
        "active": true,
        "id": "requesting-org-1",
        "identifier": [
          {
            "value": "8189991234",
            "system": "http://hl7.org/fhir/sid/us-npi"
          }
        ],
        "telecom": [
          {
            "value": "1-800-555-1234",
            "system": "phone"
          }
        ]
      }
    }
  ]
}
```
{% endtab %}
{% endtabs %}

### Claim/$inquire

Checks the status of a previously submitted prior authorization request.

**Endpoint:** `POST [base]/fhir/Claim/$inquire`

**Request:** A FHIR Bundle containing a PAS Claim Inquiry Request.

**Response:** A FHIR Bundle containing the PAS Claim Inquiry Response, or an OperationOutcome on error.

| Direction | Parameter | Type | Cardinality | Description |
|-----------|-----------|------|-------------|-------------|
| IN | resource | Bundle | 1..1 | [PAS Inquiry Request Bundle](https://build.fhir.org/ig/HL7/davinci-pas/StructureDefinition-profile-pas-inquiry-request-bundle.html) containing inquiry criteria |
| OUT | return | Bundle \| OperationOutcome | 1..1 | [PAS Inquiry Response Bundle](https://build.fhir.org/ig/HL7/davinci-pas/StructureDefinition-profile-pas-inquiry-response-bundle.html) with status information |

**Example:**

{% tabs %}
{% tab title="Request" %}
```http
POST /fhir/Claim/$inquire
Content-Type: application/json
Accept: application/json

{
  "resourceType": "Bundle",
  "meta": {
    "profile": [
      "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-pas-inquiry-request-bundle"
    ]
  },
  "type": "collection",
  "identifier": {
    "system": "http://example.org/PATIENT_EVENT_TRACE_NUMBER",
    "value": "test-bundle-3"
  },
  "timestamp": "2025-12-08T16:48:02.531010Z",
  "entry": [
    {
      "fullUrl": "urn:uuid:claim-5d4963ef-8cd8-4fa7-b002-a5c67fa2f1da",
      "resource": {
        "resourceType": "Claim",
        "id": "claim-1765213116210",
        "meta": {
          "profile": [
            "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-claim"
          ]
        },
        "identifier": [
          {
            "system": "http://example.org/claim-id",
            "value": "claim-1765213116210"
          }
        ],
        "status": "active",
        "type": {
          "coding": [
            {
              "system": "http://terminology.hl7.org/CodeSystem/claim-type",
              "code": "professional"
            }
          ]
        },
        "use": "preauthorization",
        "patient": {
          "reference": "Patient/patient-1"
        },
        "created": "2025-12-08T16:58:36.210Z",
        "insurer": {
          "reference": "Organization/payer-org-1"
        },
        "provider": {
          "reference": "Organization/requesting-org-1",
          "display": "Acme Care Clinic"
        },
        "priority": {
          "coding": [
            {
              "system": "http://terminology.hl7.org/CodeSystem/processpriority",
              "code": "normal"
            }
          ]
        },
        "insurance": [
          {
            "sequence": 1,
            "focal": true,
            "coverage": {
              "reference": "Coverage/coverage-1"
            }
          }
        ],
        "item": [
          {
            "sequence": 1,
            "category": {
              "coding": [
                {
                  "system": "https://codesystem.x12.org/005010/1365",
                  "code": "42",
                  "display": "Home Health Care"
                }
              ]
            },
            "productOrService": {
              "coding": [
                {
                  "system": "http://www.ama-assn.org/go/cpt",
                  "code": "99213",
                  "display": "Established patient office visit"
                }
              ]
            },
            "detail": [
              {
                "sequence": 1,
                "productOrService": {
                  "coding": [
                    {
                      "system": "http://www.cms.gov/Medicare/Coding/HCPCSReleaseCodeSets",
                      "code": "E0250",
                      "display": "Hospital bed, fixed height, with mattress"
                    }
                  ]
                }
              }
            ]
          }
        ]
      }
    }
  ]
}
```
{% endtab %}
{% tab title="Response" %}
```json
{
  "resourceType": "Bundle",
  "type": "collection",
  "meta": {
    "profile": [
      "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-pas-inquiry-response-bundle"
    ]
  },
  "identifier": {
    "system": "http://example.org/PATIENT_EVENT_TRACE_NUMBER",
    "value": "test-bundle-3"
  },
  "timestamp": "2025-12-17T13:35:44.019420Z",
  "entry": [
    {
      "fullUrl": "ClaimResponse/c0d73c37-12ee-4cde-bfc6-aa6ed216f4dd",
      "resource": {
        "patient": {
          "reference": "Patient/patient-1"
        },
        "request": {
          "reference": "Claim/claim-1765213116210"
        },
        "meta": {
          "profile": [
            "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-claimresponse"
          ],
          "lastUpdated": "2025-12-17T13:34:24.320436Z",
          "versionId": "935",
          "extension": [
            {
              "url": "https://aidbox.app/ex/createdAt",
              "valueInstant": "2025-12-17T13:34:24.320436Z"
            }
          ]
        },
        "use": "preauthorization",
        "item": [
          {
            "adjudication": [
              {
                "category": {
                  "coding": [
                    {
                      "code": "submitted",
                      "system": "http://terminology.hl7.org/CodeSystem/adjudication"
                    }
                  ]
                }
              }
            ],
            "itemSequence": 1
          }
        ],
        "type": {
          "coding": [
            {
              "code": "professional",
              "system": "http://terminology.hl7.org/CodeSystem/claim-type"
            }
          ]
        },
        "created": "2025-12-17T13:34:24.316526Z",
        "outcome": "queued",
        "resourceType": "ClaimResponse",
        "insurer": {
          "reference": "Organization/payer-org-1"
        },
        "status": "active",
        "id": "c0d73c37-12ee-4cde-bfc6-aa6ed216f4dd"
      }
    },
    {
      "fullUrl": "Organization/payer-org-1",
      "resource": {
        "meta": {
          "profile": [
            "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-insurer"
          ],
          "lastUpdated": "2025-12-17T08:11:38.523298Z",
          "versionId": "198",
          "extension": [
            {
              "url": "https://aidbox.app/ex/createdAt",
              "valueInstant": "2025-12-16T08:07:36.649727Z"
            }
          ]
        },
        "name": "Acme Health Insurance",
        "active": true,
        "telecom": [
          {
            "value": "1-800-555-ACME",
            "system": "phone"
          }
        ],
        "identifier": [
          {
            "value": "1234567893",
            "system": "http://hl7.org/fhir/sid/us-npi"
          }
        ],
        "id": "payer-org-1",
        "resourceType": "Organization"
      }
    },
    {
      "fullUrl": "Claim/claim-1765213116210",
      "resource": {
        "patient": {
          "reference": "Patient/patient-1"
        },
        "insurance": [
          {
            "focal": true,
            "coverage": {
              "reference": "Coverage/coverage-1"
            },
            "sequence": 1
          }
        ],
        "meta": {
          "profile": [
            "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-claim"
          ],
          "lastUpdated": "2025-12-16T10:21:42.922009Z",
          "versionId": "93",
          "extension": [
            {
              "url": "https://aidbox.app/ex/createdAt",
              "valueInstant": "2025-12-16T10:21:42.922009Z"
            }
          ]
        },
        "use": "preauthorization",
        "item": [
          {
            "detail": [
              {
                "sequence": 1,
                "productOrService": {
                  "coding": [
                    {
                      "code": "E0250",
                      "system": "http://www.cms.gov/Medicare/Coding/HCPCSReleaseCodeSets",
                      "display": "Hospital bed, fixed height, with mattress"
                    }
                  ]
                }
              }
            ],
            "category": {
              "coding": [
                {
                  "code": "42",
                  "system": "https://codesystem.x12.org/005010/1365",
                  "display": "Home Health Care"
                }
              ]
            },
            "sequence": 1,
            "productOrService": {
              "coding": [
                {
                  "code": "99213",
                  "system": "http://www.ama-assn.org/go/cpt",
                  "display": "Established patient office visit"
                }
              ]
            }
          }
        ],
        "type": {
          "coding": [
            {
              "code": "professional",
              "system": "http://terminology.hl7.org/CodeSystem/claim-type"
            }
          ]
        },
        "created": "2025-12-08T16:58:36.210Z",
        "resourceType": "Claim",
        "insurer": {
          "reference": "Organization/payer-org-1"
        },
        "priority": {
          "coding": [
            {
              "code": "normal",
              "system": "http://terminology.hl7.org/CodeSystem/processpriority"
            }
          ]
        },
        "status": "active",
        "id": "claim-1765213116210",
        "identifier": [
          {
            "value": "claim-1765213116210",
            "system": "http://example.org/claim-id"
          }
        ],
        "provider": {
          "display": "Acme Care Clinic",
          "reference": "Organization/requesting-org-1"
        }
      }
    },
    {
      "fullUrl": "Patient/patient-1",
      "resource": {
        "address": [
          {
            "use": "home",
            "city": "Springfield",
            "line": [
              "123 Main Street"
            ],
            "state": "IL",
            "country": "USA",
            "postalCode": "62701"
          }
        ],
        "meta": {
          "profile": [
            "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-beneficiary",
            "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-subscriber"
          ],
          "lastUpdated": "2025-12-17T07:50:30.618974Z",
          "versionId": "138",
          "extension": [
            {
              "url": "https://aidbox.app/ex/createdAt",
              "valueInstant": "2025-12-16T08:07:36.639379Z"
            }
          ]
        },
        "name": [
          {
            "use": "official",
            "given": [
              "John",
              "Robert"
            ],
            "family": "Smith"
          }
        ],
        "birthDate": "1970-05-15",
        "resourceType": "Patient",
        "active": true,
        "id": "patient-1",
        "identifier": [
          {
            "value": "MRN123456",
            "system": "http://hospital.example.org/patients"
          }
        ],
        "telecom": [
          {
            "use": "home",
            "value": "555-123-4567",
            "system": "phone"
          }
        ],
        "gender": "male"
      }
    },
    {
      "fullUrl": "Organization/requesting-org-1",
      "resource": {
        "address": [
          {
            "city": "Springfield",
            "line": [
              "100 Market Street"
            ],
            "state": "IL",
            "country": "US",
            "postalCode": "62701"
          }
        ],
        "meta": {
          "profile": [
            "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-requestor"
          ],
          "lastUpdated": "2025-12-16T10:21:42.922009Z",
          "versionId": "96",
          "extension": [
            {
              "url": "https://aidbox.app/ex/createdAt",
              "valueInstant": "2025-12-16T10:21:42.922009Z"
            }
          ]
        },
        "name": "Acme Care Clinic",
        "resourceType": "Organization",
        "active": true,
        "id": "requesting-org-1",
        "identifier": [
          {
            "value": "8189991234",
            "system": "http://hl7.org/fhir/sid/us-npi"
          }
        ],
        "telecom": [
          {
            "value": "1-800-555-1234",
            "system": "phone"
          }
        ]
      }
    },
    {
      "fullUrl": "Coverage/coverage-1",
      "resource": {
        "meta": {
          "profile": [
            "http://hl7.org/fhir/us/davinci-pas/StructureDefinition/profile-coverage"
          ],
          "lastUpdated": "2025-12-17T07:50:30.612513Z",
          "versionId": "136",
          "extension": [
            {
              "url": "https://aidbox.app/ex/createdAt",
              "valueInstant": "2025-12-16T08:07:36.635155Z"
            }
          ]
        },
        "beneficiary": {
          "reference": "Patient/patient-1"
        },
        "type": {
          "coding": [
            {
              "code": "HIP",
              "system": "http://terminology.hl7.org/CodeSystem/v3-ActCode",
              "display": "health insurance plan policy"
            }
          ]
        },
        "resourceType": "Coverage",
        "subscriber": {
          "reference": "Patient/patient-1"
        },
        "payor": [
          {
            "reference": "Organization/payer-org-1"
          }
        ],
        "status": "active",
        "id": "coverage-1",
        "class": [
          {
            "name": "Gold Plus Plan",
            "type": {
              "coding": [
                {
                  "code": "plan",
                  "system": "http://terminology.hl7.org/CodeSystem/coverage-class",
                  "display": "Plan"
                }
              ]
            },
            "value": "GOLD-PLAN-001"
          }
        ]
      }
    }
  ]
}
```
{% endtab %}
{% endtabs %}
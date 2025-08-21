# NewRx Message

### Overview

NewRx is a message type used for sending new prescriptions electronically from a prescriber to a pharmacy through the Surescripts network.

### How to Send NewRx Message

To send a NewRx message, follow these steps:

1. Prepare the required FHIR resources as described in the "Required FHIR Resources" section.
2. Use the NewRx API endpoint to send the message: `POST /e-prescription/rx/new`.\
   Provide payload as:
   1. a group identifier. This identifier links multiple MedicationRequests together, as during a visit a provider can prescribe multiple medications. The system will process each MedicationRequest separately, sending one NewRx message per medication, since each MedicationRequest represents exactly one prescribed medication;
   2. a MedicationRequest id to send a single prescription without dealing with group references <sub>_(since 3.0)._</sub>
3. Monitor the status of the message using the provided status management system.

#### Required FHIR Resources

The following FHIR resources are needed to create a NewRx message:

1. **MedicationRequest** (Required)
   * Contains core prescription details
   * Must be in "draft" status initially
   * Must include requester medication details and dispense details
   * Must have `dosageInstruction.text` non-empty
   * Must include **Medication**
     * as a reference to resource <sub>_(since 2.0)_</sub>
     * as a reference to `contained` resource
     * as a codeable concept <sub>_(since 2.0)_</sub>
   * May specify number of refills to specify a number of refills via `dispenseRequest.numberOfRepeatsAllowed`, if not provided - will be 0
2. **Patient** (Required)
   * Patient demographics
   * Must have name, birth date, and address
   * For patients ≤18 years, height and weight observations are required
3. **Organization** (Required)
   * Represents the pharmacy
   * Must be active and of type "pharm"
   * Must have NCPDP ID, name, and phone number
   * Must have valid service period dates
4. **PractitionerRole** (Required)
   * Links prescriber to their organization/location
   * Must have SPI (Surescripts Provider ID)
   * Must have valid period dates
5. **Practitioner** (Required)
   * Prescriber information
   * Must have NPI and name
6. **Location** (Required)
   * Practice location details
   * Must have address and phone number
   * Will fallback to PractitionerRole's **Organization**, if not provided and casted to **Location**.
7. **Observation** (Conditional)
   * Required for patients ≤18 years old
   * Must include height and weight measurements
   * Must use UCUM units

The system performs extensive validation checks for each resource, any validation failure will prevent the **whole group** from being submitted and return appropriate error details to the client.

### Status Management

Once accepted, system will update the MedicationRequest status to `active` with "Submission started" reason and generate a unique identifier **MessageId** for the submission by which you can track a message at Surescripts side.

#### Status flow

<figure><img src="../../../.gitbook/assets/image (1) (1) (1).png" alt=""><figcaption></figcaption></figure>

#### Status structure

Statuses stored in the MedicationRequest resource like this:

```json
{
  ...
  "status": "entered-in-error",
  "statusReason": {
    "text": "Some verbose description of what happened",
    "coding": [
      {
        "system": "urn:app:aidbox:e-prescriptions:surescripts:TransactionErrorCode",
        "code": "900",
        "display": "Transaction rejected"
      },
      {
        "system": "urn:app:aidbox:e-prescriptions:surescripts:DescriptionCode",
        "code": "144",
        "display": "Number of refills invalid"
      }
    ]
  }
  // other MedicationRequest data
  ...
}
```

#### **Status properties**

First `statusReason.coding` is always main code while the rest are description ones.

| Field                       | Description                                                                                                 |
| --------------------------- | ----------------------------------------------------------------------------------------------------------- |
| status                      | Status derived from a code received from Surescripts.                                                       |
| statusReason.text           | A description of an error or a status. Presumably, a string of free format. Comes from `Error.Description`. |
| statusReason.coding.system  | Surescripts StatusCode, TransactionErrorCode or DescriptionCode                                             |
| statusReason.coding.code    | Status code, transaction error code or description code (see NCPDP reference)                               |
| statusReason.coding.display | Meaning of code (see NCPDP reference)                                                                       |

#### **Statuses description**

| Status           | How to handle                                                                               |
| ---------------- | ------------------------------------------------------------------------------------------- |
| draft            | It's an initial status and indication that message will be sent first time.                 |
| on-hold          | System should **not** sent message.                                                         |
| active           | Message was accepted for sending, nothing to do.                                            |
| completed        | Message was sent and processed, nothing to do.                                              |
| entered-in-error | It is okay to try the message again with a **new** MessageID when correction has been made. |
| stopped          | Surescripts sent error, system should **not** sent message again.                           |
| cancelled        | Message was cancelled, nothing to do.                                                       |
| unknown          | Investigation required.                                                                     |

#### Timeout

Sometimes pharmacies can respond immediately, but more often it will take some time. It's expected to get a response from pharmacy within 24 hours.

If no response is received within this time frame, status will be updated to `unknown` <sub>_(since 2.0)_</sub>

### Example

Note that you have to put real NCPDP, SPI and NPI values.

```json
{
  "resourceType": "Bundle",
  "type": "collection",
  "entry": [
    {
      "resource": {
        "resourceType": "Organization",
        "id": "example-organization",
        "meta": {
          "tag": [
            {
              "system": "urn:app:aidbox:e-prescriptions:surescripts:serviceLevel",
              "display": "New"
            },
            {
              "system": "urn:app:aidbox:e-prescriptions:surescripts:serviceLevel",
              "display": "Cancel"
            }
          ]
        },
        "active": true,
        "name": "123th Street Pharmacy",
        "type": [
          {
            "coding": [
              {
                "system": "urn:app:aidbox:e-prescriptions:surescripts:organization",
                "code": "pharm"
              }
            ]
          }
        ],
        "identifier": [
          {
            "system": "http://hl7.org/fhir/sid/us-npi",
            "type": {
              "coding": [
                {
                  "system": "http://terminology.hl7.org/CodeSystem/v2-0203",
                  "code": "NPI"
                }
              ]
            },
            "value": "<NPI>"
          },
          {
            "system": "urn:app:aidbox:e-prescriptions:ncpdp",
            "value": "<NCPDP>"
          }
        ],
        "address": [
          {
            "line": ["1234 Some St."],
            "city": "Seattle",
            "state": "WA",
            "postalCode": "98103",
            "country": "US"
          }
        ],
        "extension": [
          {
            "url": "http://hl7.org/fhir/StructureDefinition/organization-period",
            "valuePeriod": {
              "start": "2025-04-14T11:20:34.184918Z",
              "end": "2030-05-04T11:20:34.185024Z"
            }
          }
        ],
        "telecom": [
          {
            "system": "phone",
            "value": "6152219800",
            "rank": 1
          }
        ]
      }
    },
    {
      "resource": {
        "resourceType": "Practitioner",
        "id": "example-practitioner",
        "name": [
          {
            "family": "Allen",
            "given": ["Barry"]
          }
        ],
        "identifier": [
          {
            "system": "http://hl7.org/fhir/sid/us-npi",
            "type": {
              "coding": [
                {
                  "system": "http://terminology.hl7.org/CodeSystem/v2-0203",
                  "code": "NPI"
                }
              ]
            },
            "value": "<NPI>"
          }
        ]
      }
    },
    {
      "resource": {
        "resourceType": "PractitionerRole",
        "id": "example-practitioner-role",
        "meta": {
          "tag": [
            {
              "system": "urn:app:aidbox:e-prescriptions:surescripts:serviceLevel",
              "display": "New"
            },
            {
              "system": "urn:app:aidbox:e-prescriptions:surescripts:serviceLevel",
              "display": "Cancel"
            }
          ]
        },
        "practitioner": {
          "reference": "Practitioner/example-practitioner"
        },
        "location": [
          {
            "reference": "Location/example-practitioner-role-location"
          }
        ],
        "identifier": [
          {
            "system": "urn:app:aidbox:e-prescriptions:surescripts:spi",
            "value": "<SPI>"
          }
        ],
        "specialty": [
          {
            "coding": [
              {
                "code": "208D00000X",
                "system": "http://nucc.org/provider-taxonomy"
              }
            ]
          }
        ],
        "period": {
          "start": "2025-04-14T11:20:34.185298Z",
          "end": "2025-05-04T11:20:34.185318Z"
        }
      }
    },
    {
      "resource": {
        "resourceType": "Location",
        "id": "example-practitioner-role-location",
        "name": "example practitioner role location",
        "meta": {
          "tag": [
            {
              "system": "urn:app:aidbox:e-prescriptions:surescripts:providerDirectory"
            }
          ]
        },
        "telecom": [
          {
            "system": "phone",
            "value": "6152219800"
          },
          {
            "value": "6152219800",
            "system": "fax"
          }
        ],
        "address": {
          "line": ["211 Central Road"],
          "city": "Jonesville",
          "state": "TN",
          "postalCode": "37777",
          "country": "US"
        }
      }
    },
    {
      "resource": {
        "resourceType": "Patient",
        "id": "example-patient",
        "name": [
          {
            "family": "Smith",
            "given": ["Mary"]
          }
        ],
        "gender": "female",
        "birthDate": "2018-12-25",
        "address": [
          {
            "line": ["45 East Road SW"],
            "city": "Clancy",
            "state": "WI",
            "postalCode": "54999",
            "country": "US"
          }
        ],
        "telecom": [
          {
            "system": "phone",
            "value": "6152219800"
          }
        ]
      }
    },
    {
      "resource": {
        "resourceType": "Observation",
        "id": "example-patient-observation-height",
        "status": "final",
        "subject": {
          "reference": "Patient/example-patient"
        },
        "code": {
          "coding": [
            {
              "code": "8302-2",
              "system": "http://loinc.org",
              "display": "Body height"
            }
          ]
        },
        "effectiveDateTime": "2025-01-15T23:30:26.115Z",
        "valueQuantity": {
          "code": "[in_i]",
          "unit": "in",
          "value": 69,
          "system": "http://unitsofmeasure.org"
        }
      }
    },
    {
      "resource": {
        "resourceType": "Observation",
        "id": "example-patient-observation-weight",
        "status": "final",
        "subject": {
          "reference": "Patient/example-patient"
        },
        "code": {
          "coding": [
            {
              "code": "3141-9",
              "system": "http://loinc.org",
              "display": "Body weight"
            }
          ]
        },
        "effectiveDateTime": "2025-01-15T23:30:35.493Z",
        "valueQuantity": {
          "code": "[lb_av]",
          "unit": "lbs",
          "value": 123,
          "system": "http://unitsofmeasure.org"
        }
      }
    },
    {
      "resource": {
        "resourceType": "MedicationRequest",
        "id": "example-medicationrequest-1",
        "groupIdentifier": {
          "system": "urn:uuid",
          "value": "example-group-id-1"
        },
        "status": "draft",
        "intent": "order",
        "authoredOn": "2025-04-01",
        "requester": {
          "reference": "PractitionerRole/example-practitioner-role"
        },
        "subject": {
          "reference": "Patient/example-patient"
        },
        "medicationReference": {
          "reference": "#medication-1"
        },
        "contained": [
          {
            "id": "medication-1",
            "resourceType": "Medication",
            "code": {
              "text": "Calan SR 240MG",
              "coding": [
                {
                  "code": "00025189131",
                  "system": "http://hl7.org/fhir/sid/ndc",
                  "display": "Calan SR 240MG"
                }
              ]
            },
            "form": {
              "coding": [
                {
                  "code": "C42998",
                  "system": "urn:app:aidbox:e-prescriptions:ncpdp:StrengthForm",
                  "display": "Oral tablet"
                }
              ]
            },
            "ingredient": [
              {
                "itemCodeableConcept": {
                  "text": "verapamil"
                },
                "isActive": true,
                "strength": {
                  "numerator": {
                    "code": "C28253",
                    "unit": "mg",
                    "value": 240,
                    "system": "urn:app:aidbox:e-prescriptions:ncpdp:StrengthUnitOfMeasure"
                  },
                  "denominator": {
                    "value": 1,
                    "unit": "Tablet",
                    "system": "urn:app:aidbox:e-prescriptions:ncpdp:QuantityUnitOfMeasure",
                    "code": "C48542"
                  }
                }
              }
            ]
          }
        ],
        "substitution": {
          "allowedBoolean": false
        },
        "dispenseRequest": {
          "performer": {
            "reference": "Organization/example-organization"
          },
          "quantity": {
            "value": 60,
            "unit": "Tablet",
            "code": "C48542",
            "system": "urn:app:aidbox:e-prescriptions:ncpdp:QuantityUnitOfMeasure"
          },
          "expectedSupplyDuration": {
            "value": 30,
            "unit": "days",
            "system": "http://unitsofmeasure.org",
            "code": "d"
          },
          "numberOfRepeatsAllowed": 1
        },
        "dosageInstruction": [
          {
            "text": "TAKE ONE TABLET TWO TIMES A DAY UNTIL GONE"
          }
        ],
        "note": [
          {
            "text": "Please provide appropriate documentation to patient for how to use this product."
          }
        ]
      }
    }
  ]
}
```

### Created resources

After the NewRx message is sent, it creates two main resources:

* **MedicationRequest** with all the references and data about the prescription.
  * Contains references to **Practitioner**, **Patient**, **Organization** (pharmacy) and others from above.
* **Provenance** tracking the time and other metadata of the NewRx creation
  * Other **Provenance** events are created for CancelRx and other (future) Rx events.
  * You can use this **Provenance** to get the created **.entity** and IDs of e.g. the created **Provider**.
  * Inside this **.entity**, there are two main sub-entities:
    * **role: derivation** is ePrescription-sent request.
    * **role: source** is a Surescript response.

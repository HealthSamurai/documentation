# DetectedIssue

This guidance applies to inbound RxRenewal and RxChange messages processed by the ePrescription module.

In the ideal scenario, the resources sent by the pharmacy and those already stored in the existing Aidbox instance should match. 
In practice, discrepancies may occur, and in such cases a `DetectedIssue` will be created and there will be a link to it: `MedicationRequest.detectedIssue`. 
This resource is intended to illustrate inconsistencies for the EHR platform using the ePrescription Module, and it does not impose any further constraints.

Below is the list of checks leading to `DetectedIssue` creation.

`Medication`:
- NDC. Existing `Medication.code.coding` with `system` = `http://hl7.org/fhir/sid/ndc` should equal incoming NDC code.

Pharmacy (`Organization`):
- name (verbatim)
- identifiers (incoming ones should be present in the existing pharmacy `identifier`)
- address
- telecom

Provider location (represented as `Location` or `Organization`):
- address
- telecom

`Practitioner`:
- identifiers (incoming ones should be present in the existing practitioner)
- names (`family` and `given` only)

`PractitionerRole`:
- SPI (`identifier` with `system` = `urn:surescripts:spi`)
- specialty (`specialty.coding` with `system` = `http://nucc.org/provider-taxonomy`)
- mismatches in related `Practitioner` or `Location`

`Patient`:
- names
- gender
- birth date
- identifiers
- address
- any telecom channel (phone, fax, pager, email)

Example `DetectedIssue` (all resources mismatched):
```json
{
  "resourceType": "DetectedIssue",
  "id": "4b2dd7a6-41f6-4b4c-a3b6-d8e9c1a8c765",
  "status": "registered",
  "contained": [
    {
      "resourceType": "Medication",
      "id": "3c6cbd71-02a8-4337-98b5-2c1f4a87e603",
      "code": {
        "coding": [
          {
            "system": "http://hl7.org/fhir/sid/ndc",
            "code": "0074-4339-22",
            "display": "Amoxicillin 500 mg capsule"
          }
        ],
        "text": "Amoxicillin 500 mg oral capsule"
      }
    },
    {
      "resourceType": "Organization",
      "id": "6fb84e57-8a26-4b31-89a3-2f5859011a0c",
      "name": "Walgreens Pharmacy #4567",
      "identifier": [
        {
          "system": "urn:oid:1.2.840.114350",
          "value": "RX-4567"
        }
      ],
      "address": [
        {
          "line": ["1501 5th Ave"],
          "city": "Seattle",
          "state": "WA",
          "postalCode": "98101"
        }
      ],
      "telecom": [
        {
          "system": "phone",
          "value": "206-555-4120"
        },
        {
          "system": "fax",
          "value": "206-555-4121"
        }
      ]
    },
    {
      "resourceType": "Location",
      "id": "a3f2b1d0-9c4c-4f69-9af8-3a7f4a1a8b29",
      "name": "Northgate Medical Center Suite 210",
      "address": {
        "line": ["401 NE Northgate Way", "Suite 210"],
        "city": "Seattle",
        "state": "WA",
        "postalCode": "98125"
      },
      "telecom": [
        {
          "system": "phone",
          "value": "206-555-2180"
        }
      ]
    },
    {
      "resourceType": "Practitioner",
      "id": "dbe0b7d4-92da-44c0-8a65-1f50f9d3e8ad",
      "name": [
        {
          "family": "Nguyen",
          "given": ["John", "T"],
          "prefix": ["Dr."]
        }
      ],
      "identifier": [
        {
          "system": "http://hl7.org/fhir/sid/us-npi",
          "value": "1780654390"
        }
      ]
    },
    {
      "resourceType": "PractitionerRole",
      "id": "f14e6f2b-261d-4bda-9387-44ec84ec4c72",
      "identifier": [
        {
          "system": "urn:surescripts:spi",
          "value": "SPI-662331"
        }
      ],
      "specialty": [
        {
          "coding": [
            {
              "system": "http://nucc.org/provider-taxonomy",
              "code": "207Q00000X",
              "display": "Family Medicine"
            }
          ],
          "text": "Family Medicine"
        }
      ]
    },
    {
      "resourceType": "Patient",
      "id": "9a1d6cf5-5ccf-49a7-9f8e-7a06d9d183b2",
      "identifier": [
        {
          "system": "http://hospital.example.org/mrn",
          "value": "MRN-204859"
        }
      ],
      "name": [
        {
          "family": "Garcia",
          "given": ["Maria", "Elena"]
        }
      ],
      "gender": "female",
      "birthDate": "1985-03-10",
      "address": [
        {
          "line": ["7820 Greenwood Ave N"],
          "city": "Seattle",
          "state": "WA",
          "postalCode": "98103"
        }
      ],
      "telecom": [
        {
          "system": "phone",
          "value": "206-555-9034"
        },
        {
          "system": "email",
          "value": "maria.garcia@example.com"
        }
      ]
    }
  ],
  "implicated": [
    { "reference": "#3c6cbd71-02a8-4337-98b5-2c1f4a87e603" },
    { "reference": "Medication/8f3b4c55-0d69-4b40-b7a7-5ad0c2e51d28" },
    { "reference": "#6fb84e57-8a26-4b31-89a3-2f5859011a0c" },
    { "reference": "Organization/1de45c01-9d0a-4b7f-a24c-94fa2b0b9f67" },
    { "reference": "#a3f2b1d0-9c4c-4f69-9af8-3a7f4a1a8b29" },
    { "reference": "Location/7311c57a-78ac-472f-9c6c-219c2f478255" },
    { "reference": "#dbe0b7d4-92da-44c0-8a65-1f50f9d3e8ad" },
    { "reference": "Practitioner/d0f8d2ac-1fb9-4c4c-8e82-3fb16d0a15e4" },
    { "reference": "#f14e6f2b-261d-4bda-9387-44ec84ec4c72" },
    { "reference": "PractitionerRole/2b4d7f45-5d13-4778-94df-8123f254ba2e" },
    { "reference": "#9a1d6cf5-5ccf-49a7-9f8e-7a06d9d183b2" },
    { "reference": "Patient/b6ddf4ce-e9c4-4ae3-9d31-1b3d2f8af9ae" }
  ]
}
```

# AddProviderLocation Message

### Overview

AddProviderLocation is a message type used to add a provider location to the Surescripts directory. This allows providers to register their practice locations for electronic prescribing services.

### How to Send AddProviderLocation Message

To send an AddProviderLocation message, use the following endpoint: `POST /directories/providers`

The required payload is a PractitionerRole identifier.

#### Required FHIR Resources

The following FHIR resources are needed to create an AddProviderLocation message:

1. **PractitionerRole** (Required)
   * Must have valid period dates (if not provided, start defaults to current date and end defaults to 2/31/2099 12:00:00 AM EST)
   * Must have service levels defined in `meta.tag`
   * Must include Practitioner and Location references
   * Links prescriber to their organization/location
2. **Practitioner** (Required)
   * Must have NPI identifier
   * Must NOT have SPI (use [update provider flow](updateproviderlocation-message.md) instead)
   * Must have name
   * Contains prescriber's information
3. **Location** (Required)
   * PractitionerRole can have multiple Locations, so for Location to be selected it's required for it to have a tag with system `urn:app:aidbox:e-prescriptions:surescripts:providerDirectory` (value does not matter)
   * Must have address
   * Must have primary phone number (exactly 10 digits)
   * Must have primary fax number
   * Phone numbers validation rules:
     * Must not be all repeating digits
     * Must not be sequential
     * Must not start with 555
   * Will fallback to PractitionerRole's **Organization**, if not provided and casted to **Location**.

### Response

#### Success Response (201 Created)

* Provider location was successfully added to Surescripts Directory
* PractitionerRole resource will be updated with SPI (Surescripts Provider ID), system `urn:app:aidbox:e-prescriptions:surescripts:spi`

#### Error Responses

**400 Bad Request** – Surescripts response error with details.

**422 Unprocessable Entity** - Various validation errors.

### Example

```json
{
  "resourceType": "Bundle",
  "type": "collection",
  "entry": [
    {
      "resource": {
        "resourceType": "PractitionerRole",
        "id": "example-practitioner-role",
        "meta": {
          "tag": [
            {
              "system": "urn:app:aidbox:e-prescriptions:surescripts:serviceLevel",
              "code": "New"
            },
            {
              "system": "urn:app:aidbox:e-prescriptions:surescripts:serviceLevel",
              "code": "Cancel"
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
        "identifier": [],
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
          "end": "2026-05-04T11:20:34.185318Z"
        }
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
    }
  ]
}
```

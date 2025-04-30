# GetProviderLocation Message

### Overview

GetProviderLocation is a message type used to retrieve providers locations information from the Surescripts directory. This allows querying for provider details using various identifiers.

### How to Send GetProviderLocation Message

To get provider location information, use the following endpoint: `GET /directories/providers`

The query parameters must include **exactly one** of:

* `identifier` - A fully qualified identifier `system|value`
* `identifier:of-type` - A value with the type of identifier `type-system|type-code|value`

#### Supported Identifiers

Only one of the following identifiers can be used for searching:

* DEA Number
* NPI (National Provider Identifier)
* SPI (Surescripts Provider ID)

### Response

#### Success Response (200 OK)

* Returns a FHIR Bundle containing PractitionerRole resources with all other related resources.

The response format follows the same structure as in [Add](addproviderlocation-message.md) or [Update](updateproviderlocation-message.md) provider location messages, where PractitionerRole is accompanied by Practitioner and Location resources. The main difference is that related resources are included as `contained` resources within PractitionerRole, rather than as separate bundle entries.

#### Error Responses

**400 Bad Request** – Surescripts response error with details.

**422 Unprocessable Entity** - Invalid identifier format, multiple query parameters provided or missing required query parameter.

#### Example

```json
{
  "resourceType": "Bundle",
  "type": "searchset",
  "entry": [
    {
      "resource": {
        "resourceType": "PractitionerRole",
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
          ],
          "source": "surescripts"
        },
        "practitioner": {
          "reference": "#practitioner"
        },
        "location": [
          {
            "reference": "#location"
          }
        ],
        "identifier": [
          {
            "system": "urn:app:aidbox:e-prescriptions:surescripts:spi",
            "value": "<SPI>"
          }
        ],
        "period": {
          "start": "2025-04-14T11:20:34.185298Z",
          "end": "2026-05-04T11:20:34.185318Z"
        },
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
        "contained": [
          {
            "resourceType": "Practitioner",
            "id": "practitioner",
            "name": [
              {
                "family": "Smith",
                "given": ["John"]
              }
            ],
            "identifier": [
              {
                "system": "http://hl7.org/fhir/sid/us-npi",
                "value": "<NPI>"
              }
            ]
          },
          {
            "resourceType": "Location",
            "id": "location",
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
                "system": "fax",
                "value": "6152219800"
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
        ]
      }
    }
  ]
}
```

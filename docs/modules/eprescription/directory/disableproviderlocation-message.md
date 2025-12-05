---
description: Disable provider locations in Surescripts directory using DisableProviderLocation message.
---

# DisableProviderLocation Message

### Overview

DisableProviderLocation is a message type used to disable an existing provider location in the Surescripts directory. This effectively removes a provider's ability to send electronic prescriptions from that location.

### How to Send DisableProviderLocation Message

To send a DisableProviderLocation message, use the following endpoint:`DELETE /directories/providers`

The required payload is a PractitionerRole identifier.

#### Required FHIR Resources

The following FHIR resource is needed to create a DisableProviderLocation message:

1. **PractitionerRole** (Required)
   * Must have SPI (Surescripts Provider ID) in identifiers
   * This is the only resource required for disabling a location

Unlike [Add](addproviderlocation-message.md) and [Update](updateproviderlocation-message.md) operations, DisableProviderLocation only requires the PractitionerRole resource with SPI, as this identifier is sufficient to identify the location that needs to be disabled.

### Response

#### Success Response (200 OK)

* Provider location is disabled in Surescripts directory
* SPI identifier is removed from the PractitionerRole resource

**Note:** This operation cannot be undone. To re-enable a location, you would need to submit a new AddProviderLocation message.

#### Error Responses

**400 Bad Request** – Surescripts response error with details.

**422 Unprocessable Entity** - Various validation errors.

### Example

```json
{
  "resourceType": "PractitionerRole",
  "id": "example-practitioner-role",
  "identifier": [
    {
      "system": "urn:app:aidbox:e-prescriptions:surescripts:spi",
      "value": "<SPI>"
    }
  ]
}
```

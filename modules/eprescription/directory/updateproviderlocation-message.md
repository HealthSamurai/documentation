# UpdateProviderLocation Message

### Overview

UpdateProviderLocation is a message type used to update an existing provider location information in the Surescripts directory. This allows providers to modify their practice location details for electronic prescribing services.

### How to Send UpdateProviderLocation Message

To send an UpdateProviderLocation message, use the following endpoint:`PUT /directories/providers`

The required payload is a PractitionerRole identifier.

#### Required FHIR Resources

UpdateProviderLocation requires the same FHIR resources as [AddProviderLocation](addproviderlocation-message.md) message, with identical validation rules and required fields. The only difference is that for UpdateProviderLocation PractitionerRole **must have SPI** identifier.

### Response

#### Success Response (201 Created)

* Provider location was successfully updated

#### Error Responses

_400 Bad Request_ – Surescripts response error with details._422 Unprocessable Entity_ - Various validation errors.

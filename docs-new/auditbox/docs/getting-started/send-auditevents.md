# Send AuditEvents to Auditbox

This guide describes sending your AuditEvents to Auditbox.

## Authentication

Auditbox uses **Keycloak** for authentication and authorization,
although any compliant OAuth2 identity provider (IDP) will work with
proper setup. To access the FHIR API, you need to obtain a JWT access
token from your IDP instance.

### Getting a Token

All FHIR API requests must include an `Authorization` header
with a Bearer token issued by the identity provider:

```http
Authorization: Bearer [your-jwt-token]
```

To obtain a token from Keycloak, our default IDP, use the OAuth 2.0
client credentials or password grant flow. For example:

```bash
curl "${keycloak_url}/realms/auditbox/protocol/openid-connect/token" \
  -X POST \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=${client_id}" \
  -d "client_secret=${client_secret}" \
  -d "grant_type=client_credentials"
```

The JSON response will contain an `access_token` key that
you need to use for API requests.

## Creating AuditEvents

One of the main operations you'll do on Auditbox is storing
AuditEvents. There are two options to upload events - single event
and bulk.

Both endpoints in the end return your request, but with some changes:
- Event's meta.lastUpdated gets bumped.
- Event's id field is replaced with a new value.

### Single event upload

The `/AuditEvent` endpoint accepts a single AuditEvent for upload to
Auditbox.

```bash
curl "${auditbox_url}/AuditEvent" \
  -X POST \
  -H "Content-Type: application/fhir+json" \
  -H "Authorization: Bearer ${token}" \
  -d \
  '{
     "resourceType": "AuditEvent",
     "type": {
       "system": "http://dicom.nema.org/resources/ontology/DCM",
       "code": "110100",
       "display": "Application Activity"
     },
     "action": "E",
     "recorded": "2026-01-16T16:03:16Z",
     "outcome": "0",
     "agent": [
       {
         "requestor": true,
         "altId": "user@example.com"
       }
     ],
     "source": {
       "observer": {
         "display": "My System"
       }
     }
   }'
```

Upon finishing, one of those status codes may be returned:

| Code | Meaning                              |
|------|--------------------------------------|
|  201 | Event has been created successfully  |
|  400 | Request body is invalid              |
|  500 | An internal server error has occured |


### Bulk upload

Whenever you have more than one AuditEvent you want to upload
to Auditbox, it's best to do a bulk request. Instead of sending
10 requests with a single resource, you send one request with
10 resources, which is more efficient in regards to network and
compute resources.

**Example:**

```bash
curl "${auditbox-url}/" \
  -X POST \
  -H "Content-Type: application/fhir+json" \
  -H "Authorization: Bearer ${token}" \
  -d '{
    "resourceType": "Bundle",
    "type": "collection",
    "entry": [
      {
        "resource": {
          "resourceType": "AuditEvent",
          "type": {
            "system": "http://dicom.nema.org/resources/ontology/DCM",
            "code": "110100",
            "display": "Application Activity"
          },
          "action": "E",
          "recorded": "2026-01-16T16:03:16Z",
          "outcome": "0",
          "agent": [
            {
              "requestor": true,
              "altId": "user@example.com"
            }
          ],
          "source": {
            "observer": {
              "display": "My System"
            }
          }
        }
      }
    ]
  }'
```

| Code | Meaning                              |
|------|--------------------------------------|
|  201 | Events had been created successfully |
|  400 | Request body is invalid              |
|  500 | An internal server error has occured |

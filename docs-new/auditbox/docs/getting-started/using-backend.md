# Intro

This guide describes using an Auditbox server programmatically.
Whenever you see *${...}* in this page, replace it mentally with
a value that applies to you.

## Authentication

Auditbox uses **Keycloak** for authentication and authorization,
although any compliant OAuth2 identity provider (IDP) will work with
proper setup. To access the FHIR API, you need to obtain a JWT access
token from your IDP instance.

### Getting a Token

All FHIR API requests must include an `Authorization` header
with a Bearer token issued by the identity provider:

```http
Authorization: Bearer ${your-jwt-token}
```

To obtain a token from Keycloak, our default IDP, use the OAuth 2.0
client credentials or password grant flow. For example:

```bash
curl "${keycloak_url}/realms/auditbox/protocol/openid-connect/token" \
  -X POST \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=${client_id}" \
  -d "client_secret=${client_secret}" \
  -d "username=${username}" \
  -d "password=${password}"
```

The JSON response will contain an `access_token` key that
you need to use for API requests.

## Checking if server is up

In order to use Auditbox, you need to make sure it's healthy.
For that you can use /healthcheck endpoint:
```bash
curl -v "${auditbox_url}/healthcheck"
```

Based on status code, there are a few options:

| Code | Meaning                                        |
|------|------------------------------------------------|
|  200 | Server and related infrastructure are healthy  |
|  500 | Server is unhealthy                            |

## Creating AuditEvents

One of the main operations you'll do on Auditbox is storing
AuditEvents. There are two options to upload events - single resource
and bulk.

### Single event upload

As the title suggests, */AuditEvent* endpoint is used for uploading
a single AuditEvent to Auditbox.

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

## Reading AuditEvents by ID

**Example:**
```bash
curl \
  -H "Authorization: Bearer ${token}" \
  "${auditbox_url}/AuditEvent/${id}"
```
Unless there's an error raised from the server, you'll get a
status code 200. If there exists an event with provided ID, then
it shall be returned, or response body will be empty.

## Response Format

All FHIR API responses use the `application/fhir+json` content type and follow FHIR JSON formatting conventions.

### Success Response

```json
{
  "resourceType": "AuditEvent",
  "id": "example-123",
  "meta": {
    "lastUpdated": "2025-01-31T16:03:16Z"
  },
  "type": {
    "system": "http://dicom.nema.org/resources/ontology/DCM",
    "code": "110100"
  },
  "action": "E",
  "recorded": "2025-01-31T16:03:16Z",
  "outcome": "0",
  "agent": [{
    "requestor": true,
    "who": {
      "identifier": {
        "value": "user@example.com"
      }
    }
  }],
  "source": {
    "observer": {
      "display": "My System"
    }
  }
}
```

### Error Response

```json
{
  "resourceType": "OperationOutcome",
  "issue": [{
    "severity": "error",
    "code": "invalid",
    "diagnostics": "Validation error details here"
  }]
}
```

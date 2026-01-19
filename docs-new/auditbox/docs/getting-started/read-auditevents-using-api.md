# Read AuditEvents using API

This guide describes reading data out of your Auditbox instance
using FHIR API.

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
  -d "grant_type=client_credentials" \
  -d "client_id=${client_id}" \
  -d "client_secret=${client_secret}" \
  -d "username=${username}" \
  -d "password=${password}"
```

The JSON response will contain an `access_token` key that
you need to use for API requests.

## Get an AuditEvent by ID

If you have an ID of an auditevent, you can get the full entry like
this:

**Example:**
```bash
curl "${auditbox_url}/AuditEvent/${id}" \
  -H "Authorization: Bearer ${token}"
```

Unless there's an error raised from the server, you'll get a
status code 200. If there exists an event with provided ID, then
it shall be returned, or response body will be empty.

## Search AuditEvents

Search for AuditEvent resources using FHIR search parameters.

```http
GET [base]/AuditEvent?[parameters]
Authorization: Bearer <token>
```

Or using POST:

```http
POST [base]/AuditEvent/_search
Content-Type: application/x-www-form-urlencoded
Authorization: Bearer <token>

[parameters]
```

You can read about all supported search parameters [here](./../api.md).

**Examples:**
```bash
# Events which have an agent with altId field "john.doe".
curl "http://localhost:3002/AuditEvent?altId=john.doe" \
  -H "Authorization: Bearer <token>"

# Events that are NOT create actions
curl "http://localhost:3002/AuditEvent?action:not=C" \
  -H "Authorization: Bearer <token>"

# Events recorded after January 1, 2025
curl "http://localhost:3002/AuditEvent?date=ge2025-01-01" \
  -H "Authorization: Bearer <token>"

# Date range
curl "http://localhost:3002/AuditEvent?date=ge2025-01-01&date=le2025-01-31" \
  -H "Authorization: Bearer <token>"
```

### Pagination

Search results support pagination using the `_count` and `_offset` parameters:

```bash
# Get first 10 results
curl "http://localhost:3002/AuditEvent?_count=10" \
  -H "Authorization: Bearer <token>"

# Get next 10 results
curl "http://localhost:3002/AuditEvent?_count=10&_offset=10" \
  -H "Authorization: Bearer <token>"
```

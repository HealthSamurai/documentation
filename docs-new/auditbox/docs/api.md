# API Reference

Auditbox provides a FHIR-compliant REST API for managing AuditEvent resources.

## FHIR API Endpoints

### Metadata / Capability Statement

Get the server's capability statement describing supported operations
and search parameters. While AuditBox is a FHIR server, it specializes
on AuditLogs, thus not implementing features you may expect from
Aidbox.

```http
GET [base]/metadata
```

**Response:** FHIR CapabilityStatement resource

### Create AuditEvent

Create a new AuditEvent resource.

```http
POST [base]/AuditEvent
Content-Type: application/fhir+json
Authorization: Bearer <token>
```

**Request Body:** FHIR AuditEvent resource

**Response:**
- **201 Created** - Returns the created AuditEvent with generated ID
- **400 Bad Request** - Validation errors in the request body
- **500 Internal Server Error** - Server error occurred

### Upload Bundle

Create multiple AuditEvent resources in a single transaction.

```http
POST [base]/
Content-Type: application/fhir+json
Authorization: Bearer <token>
```

**Request Body:** FHIR Bundle resource with AuditEvent entries

**Response:**
- **201 Created** - Returns a Bundle with created resources
- **400 Bad Request** - Validation errors
- **500 Internal Server Error** - Server error occurred

### Read AuditEvent

Retrieve a specific AuditEvent by ID.

```http
GET [base]/AuditEvent/[id]
Authorization: Bearer <token>
```

**Path Parameters:**
- `id` - The AuditEvent resource ID

**Response:** FHIR AuditEvent resource

**Response:**
- **200 OK** - Returns the FHIR AuditEvent resource or nothing
- **500 Internal Server Error** - Server error occurred

### Search AuditEvents

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

**Response:** FHIR Bundle containing matching AuditEvent resources

## Supported Search Parameters

Auditbox supports the following FHIR search parameters for AuditEvent:

### AuditEvent-Specific Parameters

| Parameter | Type | Description | Example |
|-----------|------|-------------|---------|
| `action` | token | Type of action performed | `?action=C` |
| `address` | string | Network address of the client | `?address=192.168.1.1` |
| `agent` | reference | Identifier of who performed the action | `?agent=Practitioner/123` |
| `agent-name` | string | Human-friendly name for the agent | `?agent-name=John` |
| `agent-role` | token | Agent role in the event | `?agent-role=110153` |
| `altId` | token | Alternative identifier for the agent | `?altId=user123` |
| `date` | date | Time when the event was recorded | `?date=2025-01-31` |
| `entity` | reference | Specific instance of resource | `?entity=Patient/456` |
| `entity-name` | string | Name of the entity | `?entity-name=John` |
| `entity-role` | token | What role the entity played | `?entity-role=1` |
| `entity-type` | token | Type of entity involved | `?entity-type=1` |
| `outcome` | token | Event outcome indicator | `?outcome=0` |
| `patient` | reference | Patient reference | `?patient=Patient/456` |
| `policy` | uri | Policy that authorized the event | `?policy=http://example.com/policy` |
| `site` | token | Logical source location | `?site=Hospital-Main` |
| `source` | reference | Source identity | `?source=Device/789` |
| `subtype` | token | More specific type/subtype | `?subtype=110101` |
| `type` | token | Type/identifier of event | `?type=110100` |

### Common FHIR Parameters

| Parameter | Type | Description | Example |
|-----------|------|-------------|---------|
| `_id` | token | Resource ID | `?_id=123` |
| `_lastUpdated` | date | Last update time | `?_lastUpdated=ge2025-01-01` |
| `_tag` | token | Search by tag | `?_tag=important` |
| `_profile` | uri | Search by profile | `?_profile=http://example.com/profile` |
| `_security` | token | Security labels | `?_security=restricted` |
| `_source` | uri | Source system | `?_source=http://example.com/system` |
| `_text` | string | Full-text search | `?_text=login` |
| `_content` | string | Content search | `?_content=patient` |

### Pagination Parameters

| Parameter | Type | Description | Example |
|-----------|------|-------------|---------|
| `_offset` | number | Number of entries to skip | `?_offset=10` |
| `_count` | number | Number of entries to return | `?_count=10` |
### Search Modifiers

Auditbox supports standard FHIR search modifiers and prefixes:

#### Date Prefixes
- `eq` - Equal (default)
- `ne` - Not equal
- `lt` - Less than
- `le` - Less than or equal
- `gt` - Greater than
- `ge` - Greater than or equal

#### Token Modifiers
- `:not` - Negation
- `:text` - Text search in display/description

### Combining Parameters

Multiple parameters can be combined using `&`:

```bash
# Find all events that create a resource for Patient/456:
curl "http://localhost:3002/AuditEvent?action=C&patient=Patient/456" \
  -H "Authorization: Bearer <token>"
```

You can search for one of the values by separating them with `,`:

```bash
# Find either Create or Update events:
curl "${url}/AuditEvent?action=C,U" \
  -H "Authorization: Bearer <token>"
```

## Search Parameters Endpoint

Get a list of all supported search parameters:

```http
GET /SearchParameter
```

## Health Check

Check if the Auditbox service is running and Elasticsearch is accessible:

```http
GET /healthcheck
```

**Response:**
- **200 OK** - Service is healthy
- **500 Internal Server Error** - Service is unhealthy

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

## Rate Limiting

Currently, Auditbox does not implement rate limiting. However, for production deployments, it's recommended to implement rate limiting at the API gateway or load balancer level.

## CORS

Cross-Origin Resource Sharing (CORS) can be enabled by setting the `AUDITBOX_CORS_ORIGIN_HEADERS` environment variable. When enabled, the API will respond with appropriate CORS headers for cross-origin requests.

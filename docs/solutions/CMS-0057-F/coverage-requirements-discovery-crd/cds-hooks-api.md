# CDS Hooks API

## Supported Operations

All following CDS Hooks operations are NOT publicly accessible. If you want to access them from third party application read [Application/Client Management](../../../access-control/identity-management/application-client-management.md) — configure Client resources for programmatic API access with OAuth 2.0 flows or Basic Auth.

### Discovery

Returns the list of CDS Services available on this server.

**Endpoint:** `GET [base]/cds-services`

**Response:** A JSON object containing an array of available CDS Services with their configuration, including supported hooks, prefetch templates, and usage requirements.

**Example:**

{% tabs %}
{% tab title="Request" %}
```http
GET /cds-services
Accept: application/json
```
{% endtab %}

{% tab title="Response" %}
```json
{
  "services": [
    {
      "id": "order-sign-crd",
      "hook": "order-sign",
      "title": "CRD Prior Authorization (Order Sign)",
      "description": "Provides final prior authorization determination when orders are being signed",
      "prefetch": {
        "patient": "Patient/{{context.patientId}}",
        "encounter": "Encounter/{{context.encounterId}}",
        "coverage": "Coverage?patient={{context.patientId}}&status=active"
      },
      "usageRequirements": "Requires context.patientId and at least one order in context.draftOrders"
    }
  ]
}
```
{% endtab %}
{% endtabs %}

| Field | Type | Description |
|-------|------|-------------|
| services | array | Array of available CDS Service definitions |
| services[].id | string | Unique identifier for this CDS Service |
| services[].hook | code| The hook this service should be invoked on (e.g., `order-sign`, `order-select`) |
| services[].title | string | Human-readable name of this service |
| services[].description | string | Description of what this service does |
| services[].prefetch | object | Key/value pairs of FHIR queries for prefetch data |
| services[].usageRequirements | string | Human-readable description of any preconditions for using this service |
